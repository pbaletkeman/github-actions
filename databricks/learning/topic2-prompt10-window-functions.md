# Window Functions

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 2 — Spark SQL (20%)

---

## Overview

Window functions perform calculations across a set of rows **related to the current row**, without collapsing those rows into a single result (unlike `groupBy().agg()`). They are essential for ranking, running totals, lag/lead comparisons, and moving averages.

---

## WindowSpec — The Core Building Block

```python
from pyspark.sql import Window
from pyspark.sql import functions as F

# Full spec: partition + order + frame
windowSpec = Window \
    .partitionBy('department') \
    .orderBy(F.col('salary').desc()) \
    .rowsBetween(Window.unboundedPreceding, Window.currentRow)
```

### Building a WindowSpec

| Method | Purpose | Example |
|--------|---------|---------|
| `partitionBy(*cols)` | Groups rows (like GROUP BY — function calculated per group) | `.partitionBy('dept')` |
| `orderBy(*cols)` | Orders rows within each partition | `.orderBy(F.col('salary').desc())` |
| `rowsBetween(start, end)` | Physical row offset frame | `.rowsBetween(-3, 0)` |
| `rangeBetween(start, end)` | Value-based range frame | `.rangeBetween(-100, Window.currentRow)` |

---

## Frame Boundaries

| Constant | Value | Meaning |
|----------|-------|---------|
| `Window.unboundedPreceding` | `-sys.maxsize` | Start of partition |
| `Window.currentRow` | `0` | Current row |
| `Window.unboundedFollowing` | `sys.maxsize` | End of partition |

```python
# Common frame examples
Window.rowsBetween(Window.unboundedPreceding, Window.currentRow)  # cumulative
Window.rowsBetween(-2, 2)                                          # 5-row sliding window
Window.rowsBetween(Window.unboundedPreceding, Window.unboundedFollowing)  # full partition
```

### `rowsBetween` vs `rangeBetween`

| | `rowsBetween` | `rangeBetween` |
|---|---|---|
| Frame unit | Physical row offset | Value range of the ORDER BY column |
| Example | `-2` to `0` = previous 2 rows + current row | `-100` to `0` = all rows where value is within 100 of current row's value |
| Use case | Fixed-size sliding window | Value-based window (e.g., within $100 of salary) |

---

## Applying Window Functions

```python
# Apply with .over(windowSpec)
df.withColumn('result', F.some_function(col).over(windowSpec))
```

---

## Ranking Functions

```python
w = Window.partitionBy('department').orderBy(F.col('salary').desc())

df.withColumn('rank',        F.rank().over(w))         # gaps after ties (1,1,3)
df.withColumn('dense_rank',  F.dense_rank().over(w))   # no gaps (1,1,2)
df.withColumn('row_number',  F.row_number().over(w))   # unique — no ties (1,2,3)
df.withColumn('ntile',       F.ntile(4).over(w))        # assign quartile bucket
df.withColumn('percent_rank',F.percent_rank().over(w))  # relative rank 0.0–1.0
df.withColumn('cume_dist',   F.cume_dist().over(w))     # cumulative distribution
```

### rank() vs dense_rank() vs row_number()

| Scores | `rank()` | `dense_rank()` | `row_number()` |
|--------|---------|---------------|---------------|
| 100 | 1 | 1 | 1 |
| 100 | 1 | 1 | 2 |
| 90  | 3 | 2 | 3 |
| 80  | 4 | 3 | 4 |

---

## Analytic Functions (Offset)

```python
w = Window.partitionBy('department').orderBy('date')

df.withColumn('prev_salary', F.lag('salary', 1).over(w))     # value from N rows BEFORE
df.withColumn('next_salary', F.lead('salary', 1).over(w))    # value from N rows AFTER
df.withColumn('first_val',   F.first('salary').over(w))      # first in partition
df.withColumn('last_val',    F.last('salary').over(w))        # last in partition
```

Default `lag`/`lead` offset is 1. Pass a third argument for a default value when the offset row doesn't exist:
```python
F.lag('salary', 1, 0).over(w)   # returns 0 if no previous row
```

---

## Aggregate Window Functions (Cumulative / Moving)

```python
w_cum = Window.partitionBy('dept').orderBy('date') \
              .rowsBetween(Window.unboundedPreceding, Window.currentRow)

w_roll = Window.partitionBy('dept').orderBy('date').rowsBetween(-6, 0)

df.withColumn('cumulative_sum', F.sum('amount').over(w_cum))
df.withColumn('rolling_7day',   F.avg('amount').over(w_roll))
df.withColumn('running_max',    F.max('amount').over(w_cum))
```

---

## Common Patterns

```python
# 1. Top-N per group (dense_rank — no gaps)
w = Window.partitionBy('department').orderBy(F.col('salary').desc())
df.withColumn('rank', F.dense_rank().over(w)) \
  .filter(F.col('rank') <= 3)

# 2. Year-over-year comparison
w = Window.partitionBy('region').orderBy('year')
df.withColumn('yoy_change', F.col('revenue') - F.lag('revenue', 1).over(w))

# 3. Salary as % of department total
w_full = Window.partitionBy('department') \
               .rowsBetween(Window.unboundedPreceding, Window.unboundedFollowing)
df.withColumn('pct_of_dept', F.col('salary') / F.sum('salary').over(w_full))
```

---

## Exam Checklist

- [ ] Know the WindowSpec building methods: `partitionBy()`, `orderBy()`, `rowsBetween()`, `rangeBetween()`
- [ ] Know the three frame boundary constants: `unboundedPreceding`, `currentRow`, `unboundedFollowing`
- [ ] Understand `rowsBetween` (physical rows) vs `rangeBetween` (value range)
- [ ] Know the difference between `rank()` (gaps), `dense_rank()` (no gaps), `row_number()` (unique)
- [ ] Know `lag()` (previous rows) vs `lead()` (future rows)
- [ ] Know how to apply any window function with `.over(windowSpec)`

---

[⬅️ topic2-prompt9-builtin-sql-functions.md](topic2-prompt9-builtin-sql-functions.md) | **10 / 32** | [Next ➡️ topic2-prompt11-query-optimization.md](topic2-prompt11-query-optimization.md)
