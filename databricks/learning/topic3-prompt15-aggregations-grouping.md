# Aggregations and Grouping

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Aggregations in Spark are performed via `groupBy().agg()` for standard grouped aggregations, and `cube()`, `rollup()`, and `pivot()` for multidimensional analysis. All aggregate functions live in `pyspark.sql.functions`.

---

## Core Pattern: `groupBy().agg()`

```python
from pyspark.sql import functions as F

df.groupBy('department') \
  .agg(
      F.count('*').alias('headcount'),
      F.sum('salary').alias('total_salary'),
      F.avg('salary').alias('avg_salary'),
      F.max('salary').alias('max_salary'),
      F.min('salary').alias('min_salary'),
  )
```

`groupBy()` returns a `GroupedData` object. Calling `.agg()` on it returns a `DataFrame`.

---

## Shorthand Aggregation Methods

These are equivalent to `.agg(F.function())` but only call one aggregation at a time:

```python
df.groupBy('dept').count()              # count of rows per group
df.groupBy('dept').sum('salary')         # sum of salary per group
df.groupBy('dept').avg('salary')         # average salary per group
df.groupBy('dept').mean('salary')        # alias for avg()
df.groupBy('dept').max('salary')         # max salary per group
df.groupBy('dept').min('salary')         # min salary per group
```

---

## Complete Aggregate Function Reference

| Function | Description | Notes |
|----------|-------------|-------|
| `F.count('*')` | Count all rows (includes NULLs) | |
| `F.count(col)` | Count non-null values | |
| `F.countDistinct(col)` | Count distinct non-null values | Capital D |
| `F.sum(col)` | Sum | Ignores NULLs |
| `F.avg(col)` / `F.mean(col)` | Average | Ignores NULLs |
| `F.max(col)` / `F.min(col)` | Max / min | |
| `F.stddev(col)` | Sample standard deviation | |
| `F.stddev_pop(col)` | Population standard deviation | |
| `F.variance(col)` | Sample variance | |
| `F.var_pop(col)` | Population variance | |
| `F.first(col)` | First value in group (arbitrary order) | `ignorenulls=True` optional |
| `F.last(col)` | Last value in group (arbitrary order) | `ignorenulls=True` optional |
| `F.collect_list(col)` | All values as array (keeps duplicates) | |
| `F.collect_set(col)` | Unique values as array | |
| `F.approx_count_distinct(col)` | Approximate distinct count (HyperLogLog) | Fast, configurable error rate |
| `F.percentile_approx(col, p)` | Approximate percentile | e.g., `p=0.5` = median |
| `F.sum_distinct(col)` | Sum of distinct values | |

---

## Multiple Group-By Columns

```python
df.groupBy('region', 'department') \
  .agg(F.sum('revenue').alias('total_revenue'))
```

---

## `cube()` — All Combinations of Group-By Keys

```python
df.cube('region', 'department') \
  .agg(F.sum('revenue').alias('total'))
```

`cube()` computes the aggregate for **all combinations** of the specified columns, including `NULL` rows representing subtotals and the grand total.

| region | department | total |
|--------|-----------|-------|
| US | Engineering | 100 |
| US | Marketing | 80 |
| US | NULL | 180 (US subtotal) |
| EU | Engineering | 90 |
| EU | NULL | 90 (EU subtotal) |
| NULL | Engineering | 190 (Eng subtotal) |
| NULL | NULL | 270 (grand total) |

---

## `rollup()` — Hierarchical Subtotals

```python
df.rollup('year', 'quarter') \
  .agg(F.sum('revenue').alias('total'))
```

`rollup()` computes aggregates for a **hierarchy** — left-to-right combinations only (no all-combinations like cube):
- `year + quarter`
- `year` (subtotal)
- Grand total (both NULL)

---

## `pivot()` — Rows to Columns

```python
# Pivot: one row per department, one column per product
df.groupBy('department') \
  .pivot('product', ['A', 'B', 'C']) \
  .sum('sales')
```

Without specifying values, Spark scans the column to find distinct values (slower — specify values explicitly in production).

---

## Filtering Aggregation Results (`having` equivalent)

```python
# filter() after agg() is equivalent to SQL HAVING
df.groupBy('dept') \
  .agg(F.avg('salary').alias('avg_sal')) \
  .filter(col('avg_sal') > 80000)
```

---

## Exam Checklist

- [ ] Know the core pattern: `groupBy().agg(F.function().alias('name'))`
- [ ] Know the 6 shorthand methods: `count()`, `sum()`, `avg()`, `mean()`, `max()`, `min()`
- [ ] Know `countDistinct()` (capital D, not `count_distinct`)
- [ ] Know `collect_list()` (with dupes) vs `collect_set()` (unique)
- [ ] Know `approx_count_distinct()` uses HyperLogLog and is faster than exact `countDistinct()`
- [ ] Know `cube()` computes all combinations; `rollup()` computes hierarchical subtotals
- [ ] Know `pivot()` transposes rows to columns
- [ ] Know that `filter()` after `agg()` is equivalent to SQL HAVING

---

[⬅️ topic3-prompt14-filtering-row-manipulation.md](topic3-prompt14-filtering-row-manipulation.md) | **15 / 32** | [Next ➡️ topic3-prompt16-handling-nulls.md](topic3-prompt16-handling-nulls.md)
