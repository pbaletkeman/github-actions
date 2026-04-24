# Performance Tuning Techniques

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 4 — Optimisation and Troubleshooting (12%)

---

## Overview

Performance tuning in Spark involves understanding memory management, choosing the right configurations, and using the Spark UI to identify bottlenecks. This section covers the most important tuning levers and how to use them effectively.

---

## Spark UI

The Spark UI is the primary tool for diagnosing performance problems.

| Tab | What to Look For |
|-----|----------------|
| **Jobs** | Failed jobs, total duration, number of stages |
| **Stages** | Stage duration, skew (wide range between min/max task times), shuffle read/write |
| **Tasks** | Individual task metrics, outlier tasks (stragglers), full exception for failures |
| **SQL/DataFrame** | Full query plan, operator statistics, stage boundaries |
| **Storage** | Cached DataFrame sizes, fraction of data in memory vs disk |
| **Environment** | Spark configuration values in effect |
| **Executors** | Memory usage, GC time, shuffle I/O per executor |

**Navigating to a failed task:**
Jobs tab → failed job → Stages tab → failed stage → Tasks tab → click **Full Exception** link

Access: `localhost:4040` (local/client mode active job)

---

## Memory Architecture

```
Executor JVM Heap
├── Spark Memory (spark.memory.fraction × heap)  [default: 60%]
│   ├── Storage Memory  (spark.memory.storageFraction × Spark Memory) [default: 50%]
│   │   └── Cached DataFrames, broadcast variables
│   └── Execution Memory [default: 50%]
│       └── Shuffles, joins, aggregations (can borrow from storage)
└── User Memory  [default: 40%]
    └── Python UDFs, data structures, non-Spark Java objects
```

### Key Memory Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `spark.executor.memory` | 1g | Total JVM heap per executor |
| `spark.executor.memoryOverhead` | 384MB | Off-heap memory (OS, Python, Arrow) |
| `spark.memory.fraction` | 0.6 | Fraction of heap for Spark memory |
| `spark.memory.storageFraction` | 0.5 | Fraction of Spark memory for storage |
| `spark.driver.memory` | 1g | Driver JVM heap |

---

## Key Performance Configurations

| Configuration | Default | Tuning Guidance |
|--------------|---------|----------------|
| `spark.sql.shuffle.partitions` | 200 | Tune to 2–4× total executor cores |
| `spark.sql.autoBroadcastJoinThreshold` | 10MB | Increase for larger small tables |
| `spark.executor.cores` | 1 | Typically 4–5 for good parallelism |
| `spark.sql.adaptive.enabled` | true (3.2+) | Keep enabled |

---

## Caching and Persistence

```python
# Default cache — MEMORY_AND_DISK
df.cache()                              # call on a transformed DataFrame
df.persist()                            # same as cache() with default level
df.count()                              # trigger materialisation

# Explicit storage level
from pyspark import StorageLevel
df.persist(StorageLevel.MEMORY_ONLY)
df.persist(StorageLevel.MEMORY_AND_DISK)    # default
df.persist(StorageLevel.DISK_ONLY)
df.persist(StorageLevel.MEMORY_ONLY_SER)    # serialised — smaller but slower to deserialise

# Unpersist when done
df.unpersist()
```

### Storage Level Comparison

| Level | Memory | Disk | Serialised | Replication | Notes |
|-------|--------|------|-----------|------------|-------|
| MEMORY_ONLY | ✅ | ❌ | ❌ | 1× | Fastest; OOM risk |
| MEMORY_AND_DISK | ✅ | ✅ | ❌ | 1× | **Default** — safe choice |
| MEMORY_ONLY_SER | ✅ | ❌ | ✅ | 1× | More GC pressure |
| DISK_ONLY | ❌ | ✅ | ✅ | 1× | Slowest; always available |

---

## Broadcast Variables

```python
# Broadcast a lookup table to all executors (prevents shuffle)
lookup = spark.sparkContext.broadcast({'A': 1, 'B': 2, 'C': 3})

# Access in a UDF
@udf(returnType=IntegerType())
def lookup_code(val):
    return lookup.value.get(val)

# Auto-broadcast for joins (auto-triggered at threshold)
spark.conf.set('spark.sql.autoBroadcastJoinThreshold', '50MB')

# Manual broadcast hint in join
from pyspark.sql.functions import broadcast
large_df.join(broadcast(small_df), on='id')
```

---

## AQE (Adaptive Query Execution)

```python
spark.conf.set('spark.sql.adaptive.enabled', 'true')    # default in Spark 3.2+
```

| AQE Feature | Problem Solved |
|------------|----------------|
| Dynamic partition coalescing | 200 tiny shuffle output partitions merged at runtime |
| Runtime broadcast switch | SortMergeJoin flipped to BroadcastHashJoin when table is small |
| Skew join optimisation | One oversized partition split and handled separately |

---

## Handling Data Skew (Without AQE)

```python
# Salting: add random prefix to skewed key before join
import random

salt_count = 10

# Add salt to the smaller (skewed-key) table — replicate each row salt_count times
exploded = small_df.withColumn('salt', F.explode(F.array([F.lit(i) for i in range(salt_count)])))

# Add random salt to the large table
salted_large = large_df.withColumn('salt', (F.rand() * salt_count).cast('int'))

# Join on composite key
result = salted_large.join(exploded, on=['key', 'salt'])
```

---

## Exam Checklist

- [ ] Know the Spark UI tabs and what each shows
- [ ] Know the JVM memory layout: Spark Memory (60%) = Storage + Execution; User Memory (40%)
- [ ] Know key configs: `shuffle.partitions` (200), `autoBroadcastJoinThreshold` (10MB)
- [ ] Know `df.cache()` default storage level is `MEMORY_AND_DISK`
- [ ] Know the 4 storage levels and their trade-offs
- [ ] Know AQE's 3 runtime optimisations
- [ ] Know AQE is enabled by default in Spark 3.2+

---

[⬅️ topic3-prompt23-end-to-end-scenarios.md](topic3-prompt23-end-to-end-scenarios.md) | **24 / 32** | [Next ➡️ topic4-prompt25-common-errors.md](topic4-prompt25-common-errors.md)
