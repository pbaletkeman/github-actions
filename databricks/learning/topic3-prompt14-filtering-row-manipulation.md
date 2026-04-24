# Data Filtering and Row Manipulation

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Filtering rows, sorting, deduplication, and limiting output are among the most fundamental DataFrame operations. This section covers the exact syntax, operator nuances, and exam-tested equivalences.

---

## `filter()` and `where()`

`filter()` and `where()` are **exact aliases** — they are identical in every way.

```python
# Both are identical
df.filter(col('age') > 30)
df.where(col('age') > 30)

# String predicate (SQL expression)
df.filter('age > 30')
df.where('age > 30 AND department = "Engineering"')
```

---

## Boolean Operators

**Critical exam point:** Use `&` (AND), `|` (OR), `~` (NOT) — NOT Python's `and`/`or`/`not`.

Always wrap each condition in **parentheses** when combining:

```python
# Correct: & | ~ with parentheses around each condition
df.filter((col('age') > 30) & (col('dept') == 'Engineering'))
df.filter((col('age') > 30) | (col('dept') == 'Marketing'))
df.filter(~col('active'))          # NOT — equivalent to col('active') == False

# Wrong: Python and/or do NOT work on Column objects
df.filter(col('age') > 30 and col('dept') == 'Engineering')  # ❌ raises TypeError
```

---

## Comparison Operators

| Operator / Method | Example | Description |
|------------------|---------|-------------|
| `==` | `col('dept') == 'Eng'` | Equality (NOT Python `is`) |
| `!=` | `col('dept') != 'Eng'` | Not equal |
| `>`, `>=`, `<`, `<=` | `col('age') >= 18` | Numeric comparison |
| `.isin(*values)` | `col('dept').isin('Eng', 'HR')` | In a set of values |
| `.between(lo, hi)` | `col('age').between(20, 30)` | Inclusive range |
| `.isNull()` | `col('name').isNull()` | Is NULL |
| `.isNotNull()` | `col('name').isNotNull()` | Is not NULL |
| `.startswith(s)` | `col('name').startswith('A')` | String prefix |
| `.endswith(s)` | `col('name').endswith('son')` | String suffix |
| `.contains(s)` | `col('name').contains('ali')` | Substring match |
| `.like(pattern)` | `col('name').like('A%')` | SQL LIKE (% = any chars) |
| `.rlike(pattern)` | `col('name').rlike('^[A-Z]')` | Regex match |

---

## `isin()` vs SQL IN

```python
# Python API
df.filter(col('status').isin('active', 'pending'))
df.filter(col('status').isin(['active', 'pending']))   # list also works

# Negation: use ~ or ==False
df.filter(~col('status').isin('deleted', 'archived'))
df.filter(col('status').isin('deleted', 'archived') == False)

# Equivalent SQL
spark.sql("SELECT * FROM t WHERE status IN ('active', 'pending')")
```

---

## `limit()`, `orderBy()`, `sort()`

```python
# Limit rows returned
df.limit(100)

# Order — ascending by default
df.orderBy('salary')
df.sort('salary')    # exact synonym for orderBy()

# Descending
df.orderBy(col('salary').desc())
df.orderBy(F.col('salary').desc(), F.col('name').asc())

# Null ordering
df.orderBy(col('salary').desc_nulls_last())
df.orderBy(col('salary').asc_nulls_first())
```

**`orderBy()` and `sort()` are exact aliases.**

---

## Deduplication

```python
# Remove all fully duplicate rows
df.distinct()

# Remove duplicates based on specific columns
df.dropDuplicates(['first_name', 'last_name'])

# Keep first occurrence within each duplicate group (requires window function)
from pyspark.sql import Window
w = Window.partitionBy('email').orderBy('created_at')
df.withColumn('rn', F.row_number().over(w)).filter(col('rn') == 1).drop('rn')
```

`distinct()` is equivalent to `dropDuplicates()` with all columns.

---

## Combining Filters

```python
# Chain multiple filter calls (AND semantics)
df.filter(col('age') > 25) \
  .filter(col('dept') == 'Engineering') \
  .filter(col('salary') > 70000)

# Single filter with & (equivalent)
df.filter((col('age') > 25) & (col('dept') == 'Engineering') & (col('salary') > 70000))
```

Both approaches generate the same physical plan — Catalyst combines them.

---

## Exam Checklist

- [ ] Know that `filter()` and `where()` are **exact aliases**
- [ ] Know that `orderBy()` and `sort()` are **exact aliases**
- [ ] Know to use `&` `|` `~` (NOT `and`/`or`/`not`) for boolean conditions
- [ ] Know to wrap each condition in parentheses: `(cond1) & (cond2)`
- [ ] Know `isin()` for in-list membership; `~col().isin()` for NOT IN
- [ ] Know `isNull()` / `isNotNull()` vs `isnan()` (NaN is NOT the same as NULL)
- [ ] Know `distinct()` is equivalent to `dropDuplicates()` on all columns

---

[⬅️ topic3-prompt13-column-manipulation-expressions.md](topic3-prompt13-column-manipulation-expressions.md) | **14 / 32** | [Next ➡️ topic3-prompt15-aggregations-grouping.md](topic3-prompt15-aggregations-grouping.md)
