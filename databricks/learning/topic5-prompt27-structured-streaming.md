# Structured Streaming Fundamentals

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 5 — Structured Streaming (12%)

---

## Overview

Structured Streaming is Spark's scalable, fault-tolerant stream processing engine built on top of the DataFrame/Dataset API. It models a live data stream as an unbounded table to which new rows are continuously appended.

---

## Core Concept

| Concept | Description |
|---------|-------------|
| **Unbounded table** | Each micro-batch appends new rows to a conceptual table |
| **Micro-batch** | Default execution model — processes accumulated data at intervals |
| **Trigger** | Controls when each micro-batch runs |
| **Checkpoint** | Stores progress and state; mandatory for production |

---

## Input Sources

```python
# 1. Socket (demo only — single host, no fault tolerance)
df = spark.readStream \
          .format('socket') \
          .option('host', 'localhost') \
          .option('port', 9999) \
          .load()

# 2. Rate (generates synthetic rows — for load testing)
df = spark.readStream \
          .format('rate') \
          .option('rowsPerSecond', 10) \
          .load()
# Schema: timestamp TIMESTAMP, value BIGINT

# 3. File source (CSV / JSON / Parquet directory)
schema = 'id INT, name STRING, ts TIMESTAMP'   # REQUIRED — cannot infer
df = spark.readStream \
          .schema(schema) \                      # schema MUST be explicit for file source
          .format('csv') \
          .option('header', True) \
          .load('/ingest/drop_zone/')

# 4. Kafka
df = spark.readStream \
          .format('kafka') \
          .option('kafka.bootstrap.servers', 'broker:9092') \
          .option('subscribe', 'my_topic') \
          .load()
# Schema: key, value (binary), topic, partition, offset, timestamp

# 5. Delta Lake
df = spark.readStream \
          .format('delta') \
          .load('/delta/events')
```

**Key rule: Schema is REQUIRED for file streaming sources.** Spark cannot infer schema from a streaming file source.

---

## Triggers

```python
from pyspark.sql.streaming import Trigger

# Default: no trigger — run as fast as possible
.writeStream.start()

# Fixed interval
.writeStream.trigger(processingTime='5 minutes').start()

# One-time: process all available data in a single batch, then stop
.writeStream.trigger(once=True).start()

# AvailableNow (Spark 3.3+): like once=True but supports multiple micro-batches
.writeStream.trigger(availableNow=True).start()
```

---

## Output Modes

| Mode | Description | When to Use |
|------|-------------|-------------|
| `append` | Write only **new** rows added since last trigger | Default; any stateless query; windowed agg with watermark (after window closes) |
| `complete` | Rewrite the **entire result table** each trigger | Aggregated streams only |
| `update` | Write only **changed** rows since last trigger | Aggregated streams with or without watermark |

```python
.writeStream.outputMode('append')
.writeStream.outputMode('complete')
.writeStream.outputMode('update')
```

---

## Output Sinks

```python
# Console (development only)
df.writeStream \
  .format('console') \
  .option('truncate', False) \
  .start()

# Memory (development / testing — query result as a temp view)
df.writeStream \
  .format('memory') \
  .queryName('my_stream') \
  .start()
spark.sql("SELECT * FROM my_stream").show()

# File (append only)
df.writeStream \
  .format('parquet') \
  .option('path', '/output/stream') \
  .option('checkpointLocation', '/checkpoints/stream') \
  .start()

# Kafka
df.writeStream \
  .format('kafka') \
  .option('kafka.bootstrap.servers', 'broker:9092') \
  .option('topic', 'output_topic') \
  .option('checkpointLocation', '/checkpoints/kafka') \
  .start()

# foreachBatch — most flexible (access full DataFrame API per micro-batch)
def process_batch(batch_df, batch_id):
    batch_df.write.format('delta').mode('append').save('/delta/output')
    batch_df.write.jdbc(url='...', table='output')   # can write to multiple sinks

df.writeStream \
  .foreachBatch(process_batch) \
  .option('checkpointLocation', '/checkpoints/foreachBatch') \
  .start()
```

---

## Checkpointing

**Checkpointing is mandatory for production streaming jobs.**

```python
df.writeStream \
  .format('parquet') \
  .option('checkpointLocation', '/mnt/checkpoints/my_job') \
  .option('path', '/output/data') \
  .start()
```

The checkpoint stores:
- Micro-batch progress (which source offsets have been consumed)
- State for stateful operations (window aggregations, watermarks)
- Metadata for exactly-once guarantees

---

## Starting and Managing a Streaming Query

```python
# Start the query
query = df.writeStream \
          .format('console') \
          .option('checkpointLocation', '/tmp/checkpoint') \
          .start()

# Block the main thread until query terminates
query.awaitTermination()

# Stop the query
query.stop()

# Check status
print(query.status)        # dict: {message, isDataAvailable, isTriggerActive}
print(query.lastProgress)  # dict: input/output rows, batch duration, etc.
```

---

## Exam Checklist

- [ ] Know the 5 input sources: socket, rate, file, kafka, delta
- [ ] Know **schema is required** for file streaming sources
- [ ] Know the 4 trigger types: default, processingTime, once, availableNow
- [ ] Know the 3 output modes: append (new rows), complete (entire result), update (changed rows)
- [ ] Know `complete` mode is **only for aggregated streams**
- [ ] Know the output sinks: console, memory, file, kafka, foreachBatch
- [ ] Know `foreachBatch` is the most flexible sink (provides full DataFrame API)
- [ ] Know checkpointing is **mandatory** for production
- [ ] Know `query.awaitTermination()` blocks and `query.stop()` stops the stream

---

[⬅️ topic4-prompt26-debugging.md](topic4-prompt26-debugging.md) | **27 / 32** | [Next ➡️ topic5-prompt28-stateful-streaming.md](topic5-prompt28-stateful-streaming.md)
