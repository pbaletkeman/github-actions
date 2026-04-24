# Lazy Evaluation, Transformations, and Actions

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 1 — Spark Architecture & Internals (20%)

---

## Overview

Spark uses **lazy evaluation**: transformations build up a logical plan but perform no computation until an **action** is called. This design enables query optimization (Catalyst), pipeline fusion, and efficient fault recovery.

---

## Transformations vs Actions

| Category | Definition | Examples |
|----------|-----------|---------|
| **Transformation** | Returns a new DataFrame/RDD; adds a step to the plan; **no execution triggered** | `filter()`, `select()`, `withColumn()`, `groupBy()`, `join()`, `orderBy()` |
| **Action** | Triggers the execution of the accumulated plan; returns a result to the Driver or writes to storage | `collect()`, `count()`, `show()`, `first()`, `take(n)`, `write`, `foreach()`, `toPandas()` |

**Key rule:** `printSchema()` is **NOT an action** — it does not trigger job execution.

---

## Narrow vs Wide Transformations (recap)

| Type | Shuffle? | Examples |
|------|---------|---------|
| **Narrow** | No | `filter()`, `select()`, `withColumn()`, `map()`, `flatMap()`, `union()`, `coalesce()` |
| **Wide** | **Yes** | `groupBy()`, `join()` (SortMergeJoin), `distinct()`, `orderBy()`, `repartition()`, `cube()`, `rollup()` |

---

## Benefits of Lazy Evaluation

1. **Query Optimization** — Catalyst can re-order, push down, and eliminate operations before execution begins
2. **Pipeline Fusion** — Multiple narrow transformations can be combined into a single pass (no intermediate data written)
3. **Avoid Unnecessary Computation** — If an action only needs a subset of the plan's results, unneeded work is skipped
4. **Fault Tolerance** — The DAG lineage captures everything needed to recompute a lost partition from the source

---

## Common Transformations — Reference

```python
# Narrow
df.filter(col('salary') > 50000)
df.select('id', 'name', 'salary')
df.withColumn('bonus', col('salary') * 0.1)

# Wide (trigger a stage boundary / shuffle)
df.groupBy('department').agg(F.sum('salary'))
df.join(other, on='id', how='inner')
df.orderBy('salary', ascending=False)
df.distinct()
df.repartition(10)
```

---

## Common Actions — Reference

```python
df.collect()            # returns all rows as a Python list to the Driver
df.count()              # returns total row count as a Python int
df.show(10)             # prints up to 10 rows to stdout
df.first()              # returns the first Row object
df.take(5)              # returns first 5 Row objects as a list
df.foreach(func)        # applies func to each row on the executor (no return value)
df.toPandas()           # converts entire DataFrame to a pandas DataFrame (driver memory!)
df.write.parquet(path)  # writes DataFrame to storage (triggers a job)
```

---

## Execution Flow Example

```python
# Step 1 — transformation (no execution)
df = spark.read.csv('/data/sales.csv', header=True)

# Step 2-4 — transformations (build up the plan, still no execution)
df_filtered = df.filter(col('amount') > 100)
df_grouped  = df_filtered.groupBy('category').count()

# Step 5 — ACTION → triggers plan execution for the first time
df_grouped.show()

# Step 6 — ACTION → triggers plan execution AGAIN (no caching unless .cache() was called)
df_grouped.count()
```

Without `.cache()`, each action re-executes the full lineage from scratch.

---

## `printSchema()` is NOT an Action

```python
df.printSchema()   # reads schema metadata only; does NOT scan data; does NOT trigger a job
df.dtypes          # property; same — no job triggered
df.schema          # property; same — no job triggered
df.columns         # property; same — no job triggered
```

---

## Exam Checklist

- [ ] Know the difference between a transformation and an action
- [ ] Know the four benefits of lazy evaluation
- [ ] Identify narrow vs wide transformations from a code snippet
- [ ] Know which operations trigger a shuffle (wide transformations)
- [ ] Know that `printSchema()` is NOT an action and does not trigger execution
- [ ] Know that calling two actions on an uncached DataFrame re-reads the source twice

---

[⬅️ topic1-prompt2-execution-deploy-modes.md](topic1-prompt2-execution-deploy-modes.md) | **3 / 32** | [Next ➡️ topic1-prompt4-shuffling-performance.md](topic1-prompt4-shuffling-performance.md)
