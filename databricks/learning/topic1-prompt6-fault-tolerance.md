# Fault Tolerance and Resilience

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 1 — Spark Architecture & Internals (20%)

---

## Overview

Spark achieves fault tolerance through **RDD lineage** — the complete record of transformations that produced each partition. If a partition is lost (e.g., an executor dies), Spark can recompute it from the source by replaying the lineage, without restarting the entire job.

---

## RDD Lineage (DAG)

The DAG of transformations forms the lineage. Each RDD/DataFrame partition knows how to recompute itself from its parent partitions.

```
Source Data (HDFS)
    │
    ▼ filter()
Filtered Partitions
    │
    ▼ groupBy().agg()
Aggregated Partitions  ← if executor 2 dies here, Spark re-reads source and replays
```

**Checkpoint** breaks the lineage by saving the DataFrame to stable storage (HDFS/S3). Spark can restart from the checkpoint rather than replaying the full lineage from source — important for long iterative jobs and streaming.

---

## Caching and Persistence

### Why Cache?

If a DataFrame is used by multiple actions, caching avoids re-computing the lineage on each action call.

```python
df.cache()     # marks the DataFrame for caching; does NOT cache yet
df.count()     # first action — triggers caching
df.count()     # second action — reads from cache (no re-computation)
```

**Caching is LAZY** — data is not stored until the first action after `.cache()` or `.persist()`.

### Storage Levels

```python
from pyspark import StorageLevel

df.persist(StorageLevel.MEMORY_ONLY)          # store in RAM only; recompute if evicted
df.persist(StorageLevel.MEMORY_AND_DISK)      # store in RAM; spill to disk if needed
df.persist(StorageLevel.DISK_ONLY)            # store only on disk
df.persist(StorageLevel.MEMORY_ONLY_SER)      # serialised in RAM (smaller footprint, slower reads)
df.persist(StorageLevel.MEMORY_AND_DISK_SER)  # serialised; spill to disk
df.persist(StorageLevel.MEMORY_ONLY_2)        # 2 replicas in RAM (fault-tolerant)
df.persist(StorageLevel.OFF_HEAP)             # store in off-heap memory (bypasses JVM GC)
```

| Call | Equivalent Storage Level |
|------|--------------------------|
| `df.cache()` (DataFrame) | `MEMORY_AND_DISK` |
| `rdd.cache()` (RDD) | `MEMORY_ONLY` |
| `df.persist()` | Same as `df.cache()` |

### Releasing Cache

```python
df.unpersist()  # removes cached partitions; data will be recomputed on next access
```

---

## Checkpointing

| Feature | Cache / Persist | Checkpoint |
|---------|----------------|-----------|
| Breaks lineage? | No | **Yes** |
| Storage | Executor memory / disk | HDFS / S3 (stable storage) |
| Survives executor failure? | Depends on storage level | Yes |
| Typical use | Reuse in multi-action pipelines | Long iterative ML; streaming |

```python
spark.sparkContext.setCheckpointDir('/tmp/checkpoints')
df.checkpoint()   # saves to checkpoint dir and breaks lineage
```

---

## Task Failure and Retry

| Property | Default | Meaning |
|----------|---------|---------|
| `spark.task.maxFailures` | **4** | Maximum number of times a single Task can fail before the Stage (and Job) is aborted |

When a Task fails, Spark retries it up to `maxFailures` times on any available Executor before failing the Stage.

---

## Exam Checklist

- [ ] Know that RDD lineage is Spark's primary fault-tolerance mechanism
- [ ] Know the difference between caching and checkpointing
- [ ] Know that `df.cache()` is equivalent to `MEMORY_AND_DISK` for DataFrames
- [ ] Know that `rdd.cache()` is equivalent to `MEMORY_ONLY` for RDDs
- [ ] Know that caching is LAZY (no data stored until the first action)
- [ ] Know `spark.task.maxFailures` default (4)
- [ ] Know all storage levels and their trade-offs
- [ ] Know when to use checkpointing vs caching

---

[⬅️ topic1-prompt5-broadcasting-optimization.md](topic1-prompt5-broadcasting-optimization.md) | **6 / 32** | [Next ➡️ topic1-prompt7-garbage-collection.md](topic1-prompt7-garbage-collection.md)
