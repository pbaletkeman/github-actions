# Stateful Streaming

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 5 — Structured Streaming (12%)

---

## Overview

Stateful streaming allows Spark to maintain and update state across micro-batches. This enables time-windowed aggregations, sessionisation, and arbitrary stateful processing.

---

## Stateless vs Stateful Operations

| Type | Examples | State |
|------|---------|-------|
| Stateless | `filter`, `select`, `map`, `withColumn` | No state — each row independent |
| Stateful | Windowed aggregations, `dropDuplicates` with watermark, `flatMapGroupsWithState` | Maintained across batches |

---

## Event Time vs Processing Time

| | Event Time | Processing Time |
|--|------------|----------------|
| **Definition** | Time embedded in the data (when the event occurred) | Time the event arrives at Spark |
| **Recommended** | ✅ Yes — handles late data correctly | ❌ No — drops late data silently |
| **Used for** | Windowed aggregations on real event times | Ingestion-time windows only |

---

## Tumbling Windows (Non-Overlapping)

Each event falls into exactly one window.

```python
from pyspark.sql.functions import window, col

windowed_df = (
    streaming_df
    .withWatermark('event_ts', '10 minutes')     # must come BEFORE groupBy
    .groupBy(
        window(col('event_ts'), '5 minutes'),    # window duration
        col('user_id')
    )
    .count()
)
```

**Window output schema:** `window STRUCT<start: TIMESTAMP, end: TIMESTAMP>`, `user_id`, `count`

```python
# Access window boundaries
windowed_df.select(
    col('window.start'),
    col('window.end'),
    'user_id',
    'count'
)
```

---

## Sliding Windows (Overlapping)

Events can appear in multiple windows.

```python
windowed_df = (
    streaming_df
    .withWatermark('event_ts', '10 minutes')
    .groupBy(
        window(col('event_ts'), '10 minutes', '5 minutes'),  # duration=10, slide=5
        col('user_id')
    )
    .count()
)
# Every event appears in 10/5 = 2 windows
```

---

## Watermarking

Watermarks tell Spark the maximum expected lateness of events. Data older than the watermark is discarded from state.

```python
# Declare watermark: tolerate up to 10 minutes of late data
df.withWatermark('event_ts', '10 minutes')
```

**State retention:** Spark keeps state until `max_event_time_seen - watermark_delay`. State for windows older than this threshold is dropped.

**CRITICAL: `withWatermark()` MUST come BEFORE `groupBy()`** for the watermark to apply to the aggregation.

```python
# CORRECT
df.withWatermark('event_ts', '10 minutes').groupBy(window(...)).agg(...)

# WRONG — watermark declared after groupBy; won't bound state
df.groupBy(window(...)).agg(...).withWatermark('event_ts', '10 minutes')
```

---

## Output Modes for Windowed Aggregations

| Output Mode | Behaviour | State Requirement |
|------------|-----------|------------------|
| `append` | Emit window result only **after watermark passes** the window end | Requires watermark |
| `update` | Emit updated partial results at each trigger | Works with or without watermark |
| `complete` | Rewrite all accumulated window results every trigger | ⚠️ Keeps ALL state — can OOM for unbounded streams |

---

## Full Windowed Streaming Example

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import window, col, count

spark = SparkSession.builder.appName('WindowedStream').getOrCreate()

schema = 'user_id STRING, event_ts TIMESTAMP, event_type STRING'

events = (
    spark.readStream
         .schema(schema)
         .format('csv')
         .load('/ingest/events/')
)

result = (
    events
    .withWatermark('event_ts', '10 minutes')     # tolerate 10-min late data
    .groupBy(
        window(col('event_ts'), '5 minutes'),    # 5-minute tumbling windows
        col('event_type')
    )
    .agg(count('*').alias('event_count'))
)

query = (
    result.writeStream
          .outputMode('append')                  # emit when window is finalised
          .format('parquet')
          .option('path', '/output/windowed')
          .option('checkpointLocation', '/checkpoints/windowed')
          .start()
)

query.awaitTermination()
```

---

## `flatMapGroupsWithState` / `mapGroupsWithState`

For advanced arbitrary stateful processing where built-in window functions aren't sufficient.

```python
from pyspark.sql.streaming import GroupState

def update_state(key, rows, state: GroupState):
    # key: the group key
    # rows: iterator of rows in current micro-batch for this key
    # state: mutable state object

    # Read existing state
    if state.exists:
        current_count = state.get
    else:
        current_count = 0

    # Update state
    new_count = current_count + sum(1 for _ in rows)
    state.update(new_count)

    yield (key, new_count)

# Apply
result = (
    streaming_df
    .groupBy('user_id')
    .flatMapGroupsWithState(
        outputMode='append',
        timeoutConf=GroupStateTimeout.NoTimeout,
        func=update_state
    )
)
```

---

## Exam Checklist

- [ ] Know stateless operations have no state; stateful maintain state across batches
- [ ] Know event time is preferred over processing time for correctness
- [ ] Know tumbling windows: `window(col, '5 minutes')` — no overlap
- [ ] Know sliding windows: `window(col, '10 minutes', '5 minutes')` — 3rd arg is slide
- [ ] Know `withWatermark('col', 'delay')` bounds state; **MUST come before `groupBy()`**
- [ ] Know state retention formula: `max_seen_event_time - watermark_delay`
- [ ] Know output modes for windowed aggs: `append` (needs watermark), `update`, `complete` (risky)
- [ ] Know `complete` mode keeps all state and can cause OOM on unbounded streams

---

[⬅️ topic5-prompt27-structured-streaming.md](topic5-prompt27-structured-streaming.md) | **28 / 32** | [Next ➡️ topic6-prompt29-spark-connect.md](topic6-prompt29-spark-connect.md)
