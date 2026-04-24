# DataFrame Partitioning — repartition and coalesce

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Partitioning controls how data is distributed across executors. Managing partition count correctly is crucial for performance: too many small partitions waste scheduler overhead; too few large partitions underutilise the cluster and cause out-of-memory errors.

---

## `repartition()` — Full Shuffle

```python
# Repartition to N partitions (increases or decreases — full shuffle either way)
df.repartition(10)
df.repartition(200)  # increase or decrease

# Repartition by a column (hash-partitions by that column — co-locates same key)
df.repartition('customer_id')
df.repartition(100, 'customer_id')   # 100 partitions, hashed by customer_id

# Repartition by multiple columns
df.repartition(50, 'region', 'date')
```

### Key Facts

- Always performs a **full shuffle** — all data moves between executors
- Can **increase or decrease** partition count
- When a column is specified, rows with the same key value land in the same partition (useful before `groupBy` or `join`)
- Produces roughly equal-sized partitions when using a column with good cardinality

---

## `coalesce()` — No Shuffle (Merge)

```python
# Reduce to N partitions by merging local partitions (no shuffle)
df.coalesce(1)    # merge all to 1 file — useful for writing single-file output
df.coalesce(10)   # reduce from 200 → 10 (if currently 200)
```

### Key Facts

- **No shuffle** — combines partitions on the same executor
- Can **only decrease** partition count (cannot increase)
- Produces unequal partition sizes (merges whole partitions rather than redistributing)
- `df.coalesce(1)` is a common pattern before writing a single output file

---

## repartition() vs coalesce() — Comparison

| | `repartition(n)` | `coalesce(n)` |
|---|---|---|
| Shuffle | **Yes** — full shuffle | **No** — local merge only |
| Direction | Increase OR decrease | **Decrease only** |
| Partition size | Roughly equal | Unequal |
| Cost | Expensive | Cheap |
| When to use | Need balanced partitions; join co-location | Just need fewer partitions; writing output |

---

## `spark.sql.shuffle.partitions`

```python
# Default: 200 (post-shuffle partition count)
spark.conf.set('spark.sql.shuffle.partitions', '50')   # tune down for small data
spark.conf.set('spark.sql.shuffle.partitions', '2000') # tune up for very large data
```

This setting only affects **shuffle output** (after `groupBy`, `join`, `distinct`, `orderBy`). It does not affect partitions created by `repartition()` or `coalesce()`.

**Rule of thumb:** `shuffle.partitions ≈ 2–4 × total executor cores`

---

## Checking and Monitoring Partitions

```python
# Current number of partitions
print(df.rdd.getNumPartitions())

# Examine partition sizes (development tool)
print(df.rdd.mapPartitions(lambda it: [sum(1 for _ in it)]).collect())
```

---

## write.partitionBy vs repartition

| | `write.partitionBy(col)` | `repartition(n, col)` |
|---|---|---|
| Scope | **Storage** — creates directory hierarchy on disk | **In-memory** — hash-partitions data across executors |
| Effect | One directory per unique value | N partitions, co-located by key |
| Use case | Enable partition pruning when reading | Optimise joins and aggregations |
| Both together | Often used together: repartition → write.partitionBy | — |

```python
# Common pattern: in-memory partitioning for shuffle efficiency + on-disk for read pruning
df.repartition(50, 'country') \
  .write \
  .partitionBy('country') \
  .mode('overwrite') \
  .parquet('/output')
```

---

## When to Repartition

| Scenario | Recommendation |
|----------|---------------|
| Before a `join` on a large table | `repartition(n, join_key)` — co-locate keys |
| Before `groupBy` on large data | `repartition(n, group_key)` |
| Writing to a single output file | `coalesce(1)` |
| Post-shuffle has too many small tasks | Reduce `spark.sql.shuffle.partitions` or `coalesce()` |
| Data is skewed by key | `repartition()` with salt key |

---

## Exam Checklist

- [ ] Know `repartition()` always shuffles; `coalesce()` never shuffles
- [ ] Know `repartition()` can increase OR decrease; `coalesce()` can only decrease
- [ ] Know `spark.sql.shuffle.partitions` default is 200
- [ ] Know `coalesce(1)` is used to write a single output file
- [ ] Know `repartition(n, col)` co-locates rows with the same key value
- [ ] Know the difference between `write.partitionBy` (storage/disk) and `repartition` (in-memory)
- [ ] Know how to check partition count: `df.rdd.getNumPartitions()`

---

[⬅️ topic3-prompt19-reading-writing-dataframes.md](topic3-prompt19-reading-writing-dataframes.md) | **20 / 32** | [Next ➡️ topic3-prompt21-schemas-data-types.md](topic3-prompt21-schemas-data-types.md)
