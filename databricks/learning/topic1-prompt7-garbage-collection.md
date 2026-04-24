# Garbage Collection and JVM Memory

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 1 — Spark Architecture & Internals (20%)

---

## Overview

Spark Executors run inside JVM processes. Garbage Collection (GC) pauses can significantly impact performance when the executor heap is under pressure. Understanding the JVM memory model and GC tuning is important for diagnosing and fixing slow jobs.

---

## JVM Memory Model per Executor

```
Executor JVM Heap  (spark.executor.memory)
├── Spark Memory  (spark.memory.fraction = 0.6 × heap)
│   ├── Storage Memory    ← cache, broadcast (flexible boundary)
│   └── Execution Memory  ← shuffle, sort, aggregation (flexible boundary)
└── User Memory  (remaining 0.4 × heap) ← user data structures, UDF closures

Off-Heap
└── Memory Overhead (spark.executor.memoryOverhead) ← JVM overhead, native libs
```

### Key Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| `spark.executor.memory` | `1g` | Total JVM heap size for each executor |
| `spark.memory.fraction` | `0.6` | Fraction of heap used for execution + storage |
| `spark.memory.storageFraction` | `0.5` | Fraction of Spark memory reserved for storage (rest for execution) |
| `spark.executor.memoryOverhead` | `max(384MB, 10% of executor memory)` | Off-heap native memory |
| `spark.memory.offHeap.enabled` | `false` | Enable off-heap memory for Spark operations |
| `spark.memory.offHeap.size` | `0` | Size of off-heap memory pool |

---

## Garbage Collection

### Why GC Matters

When the JVM heap is full, GC runs and pauses all application threads (stop-the-world pauses). In Spark, this translates directly to stalled Tasks — the Spark UI shows high GC Time in the Tasks tab.

### Recommended GC: G1GC

```
-XX:+UseG1GC                          ← Use G1 garbage collector (recommended for Spark)
-XX:G1HeapRegionSize=16m              ← 16 MB regions work well for typical Spark workloads
-XX:InitiatingHeapOccupancyPercent=35 ← Start GC when heap is 35% full (default is 45%)
```

### How to Set JVM Flags

```python
spark = SparkSession.builder \
    .config('spark.executor.extraJavaOptions',
            '-XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:InitiatingHeapOccupancyPercent=35') \
    .getOrCreate()
```

---

## Monitoring GC in the Spark UI

| Location | Metric | Threshold |
|----------|--------|-----------|
| Jobs → Stages → Tasks tab | **GC Time** column | > 10% of task duration = warning |
| Executors tab | GC Time per executor | Persistent high values indicate heap pressure |

- **< 5% GC time** — Healthy
- **10–25% GC time** — Tune memory or reduce data per partition
- **> 25% GC time** — Serious; consider adding memory, reducing partition size, or serialised storage

---

## Reducing GC Pressure

| Strategy | How It Helps |
|----------|-------------|
| Use `MEMORY_ONLY_SER` or `MEMORY_AND_DISK_SER` storage levels | Serialised objects (byte arrays) are treated as one large object by GC — far less GC overhead than millions of individual Java objects |
| Use off-heap memory (`spark.memory.offHeap`) | Data stored in native memory bypasses JVM GC entirely |
| Reduce `spark.sql.shuffle.partitions` | Fewer, larger partitions → fewer JVM objects |
| Unpersist unused DataFrames | Frees heap space; reduces GC pressure |
| Use Parquet/ORC (columnar formats) | Less deserialization overhead |
| Increase executor memory | More room before GC triggers |

---

## Off-Heap Memory

Off-heap memory is allocated outside the JVM heap using Java's unsafe API (or Project Panama). Since the JVM doesn't manage it, it is invisible to the GC.

```python
spark = SparkSession.builder \
    .config('spark.memory.offHeap.enabled', 'true') \
    .config('spark.memory.offHeap.size', '4g') \
    .getOrCreate()
```

Use off-heap for:
- Very large caches where GC pauses are impacting job performance
- Spark's sort/shuffle buffers when execution memory is under pressure

---

## Exam Checklist

- [ ] Know the three memory regions: Spark Memory, User Memory, Off-Heap Overhead
- [ ] Know `spark.memory.fraction` default (0.6)
- [ ] Know G1GC is the recommended GC for Spark executors
- [ ] Know where to find GC Time in the Spark UI (Tasks tab)
- [ ] Know that > 10% GC time is a warning sign, > 25% is serious
- [ ] Know that `MEMORY_ONLY_SER` / `MEMORY_AND_DISK_SER` reduce GC pressure
- [ ] Know that off-heap memory bypasses JVM GC

---

[⬅️ topic1-prompt6-fault-tolerance.md](topic1-prompt6-fault-tolerance.md) | **7 / 32** | [Next ➡️ topic2-prompt8-spark-sql-fundamentals.md](topic2-prompt8-spark-sql-fundamentals.md)
