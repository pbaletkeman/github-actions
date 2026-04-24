# Shuffling and Performance

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 1 — Spark Architecture & Internals (20%)

---

## Overview

A **shuffle** is the process of redistributing data across partitions and executor nodes. It is the most expensive operation in Spark because it involves disk writes, network transfers, and disk reads. Understanding which operations shuffle — and how to minimise shuffles — is a core performance skill.

---

## How a Shuffle Works

```
Stage N (Shuffle Write)            Network Transfer              Stage N+1 (Shuffle Read)
  Executor A: Partition 0  ──────────────────────────────►  Executor A: Partition 0'
  Executor B: Partition 1  ──────────────────────────────►  Executor B: Partition 1'
  Executor A: Partition 2  ──────────────────────────────►  Executor C: Partition 2'
              │                                                       │
     Write shuffle files to local disk            Read shuffle files from remote executors
```

Three phases:
1. **Shuffle write** — each task writes its output partitioned by key to local disk
2. **Network transfer** — data moves between executors
3. **Shuffle read** — downstream tasks read the shuffle files from remote executors

Shuffles are expensive because they involve disk I/O twice plus network I/O.

---

## Operations That Cause a Shuffle

| Operation | Reason |
|-----------|--------|
| `groupBy().agg()` | Must co-locate all rows with the same key |
| `join()` — SortMergeJoin | Both sides must be sorted and co-partitioned |
| `distinct()` | Must deduplicate across partitions |
| `orderBy()` / `sort()` | Total ordering requires a global sort |
| `repartition(n)` | Explicit full data redistribution |
| `cube()`, `rollup()` | Multidimensional aggregation |

---

## Operations That Do NOT Shuffle

| Operation | Why No Shuffle |
|-----------|---------------|
| `filter()` | Each partition processed independently |
| `select()` | Column projection — no row movement |
| `withColumn()` | Row-local operation |
| `union()` | Just appends partition lists |
| `coalesce(n)` | Merges local partitions; no full redistribution |
| Broadcast Hash Join | Small table broadcast to each executor; no movement of large table |

---

## `spark.sql.shuffle.partitions`

| Property | Default | Significance |
|----------|---------|-------------|
| `spark.sql.shuffle.partitions` | **200** | Number of partitions created after every shuffle |

On **small datasets**, 200 partitions creates excessive overhead (200 tiny tasks). Tune this down:

```python
spark.conf.set('spark.sql.shuffle.partitions', 10)  # for small/medium data
```

As a rule of thumb: `shuffle.partitions ≈ 2–4 × total executor cores`.

---

## Data Skew

Data skew occurs when one key has far more rows than others. In a grouped aggregation or join, this results in one Task processing vastly more data than others — a **straggler task** that holds up the entire Stage.

**Symptoms:** In the Spark UI Tasks tab, median task duration is 2 seconds but the max is 5 minutes.

**Remedies:**
- Enable AQE (Adaptive Query Execution): `spark.sql.adaptive.enabled = true`
- Use salting (add a random prefix to the skewed key)
- Pre-repartition by the join key before the shuffle

---

## Minimising Shuffle — Key Strategies

1. **Use broadcast joins** for small tables (`spark.sql.autoBroadcastJoinThreshold`, default 10 MB)
2. **Reduce `spark.sql.shuffle.partitions`** on small datasets
3. **Filter early** — push filters before shuffle operations
4. **Enable AQE** — let Spark dynamically optimise at runtime
5. **Use `coalesce()` instead of `repartition()`** when reducing partition count (no shuffle)
6. **Avoid `orderBy()` on large DataFrames** — global sort is extremely expensive

---

## Exam Checklist

- [ ] Know which operations cause a shuffle
- [ ] Know which operations do NOT cause a shuffle
- [ ] Know that `spark.sql.shuffle.partitions` defaults to 200
- [ ] Understand the 3-phase shuffle process (write → network → read)
- [ ] Know what data skew is and how to detect it (median vs max task time in Spark UI)
- [ ] Know that `coalesce()` avoids a shuffle; `repartition()` always shuffles

---

[⬅️ topic1-prompt3-lazy-evaluation.md](topic1-prompt3-lazy-evaluation.md) | **4 / 32** | [Next ➡️ topic1-prompt5-broadcasting-optimization.md](topic1-prompt5-broadcasting-optimization.md)
