# Handling Missing Data (NULLs and NaN)

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Spark distinguishes between **NULL** (absent or unknown value) and **NaN** (Not a Number — result of invalid floating-point arithmetic). Handling each correctly requires knowing different functions and methods.

---

## NULL vs NaN — Critical Distinction

| | NULL | NaN |
|---|---|---|
| **Meaning** | Value is absent or unknown | Invalid floating-point computation (e.g., 0.0 / 0.0) |
| **Column types** | Any column type | Float/Double only |
| **Detection** | `isNull()` / `isNotNull()` | `F.isnan()` |
| **Affected by `dropna()`?** | **Yes** | **No** |
| **Affected by `fillna()`?** | **Yes** | **No** |
| **SQL comparison** | `NULL != NULL` (three-valued logic) | `NaN == NaN` is True in Spark |
| **Aggregate behaviour** | Ignored by most functions | Propagates (sum with NaN = NaN) |

---

## Detecting NULL and NaN

```python
from pyspark.sql import functions as F

# NULL detection
df.filter(col('salary').isNull())
df.filter(col('salary').isNotNull())
df.filter(F.isnull(col('salary')))      # function form

# NaN detection (float/double only)
df.filter(F.isnan(col('salary')))
df.filter(~F.isnan(col('salary')))      # not NaN

# Check for BOTH null AND NaN
df.filter(col('salary').isNull() | F.isnan(col('salary')))
```

---

## `dropna()` — Drop Rows with NULLs

```python
df.dropna()                         # drop rows with ANY null
df.dropna(how='any')                # same — 'any' is default
df.dropna(how='all')                # drop rows where ALL values are null
df.dropna(subset=['name', 'email']) # drop rows where name OR email is null
df.dropna(thresh=3)                 # drop rows with fewer than 3 non-null values
```

`dropna()` only affects NULLs — NaN values are NOT dropped.

Equivalent: `df.na.drop()`

---

## `fillna()` — Replace NULLs with a Value

```python
df.fillna(0)                           # fill all numeric nulls with 0
df.fillna('unknown')                   # fill all string nulls with 'unknown'
df.fillna({'salary': 0, 'name': 'N/A'})   # fill specific columns with specific values
df.fillna(0, subset=['salary', 'bonus'])   # fill only these columns

# Equivalent
df.na.fill(0)
```

`fillna()` only affects NULLs — NaN values are NOT replaced.

---

## Handling NaN

Since `fillna()` does NOT fill NaN, you need `when()` + `isnan()`:

```python
# Replace NaN with 0
df.withColumn('salary',
    F.when(F.isnan(col('salary')), 0).otherwise(col('salary'))
)

# Replace NaN with null (so dropna/fillna can then handle it)
df.withColumn('salary',
    F.when(F.isnan(col('salary')), None).otherwise(col('salary'))
)

# Then fillna will work
df.withColumn('salary',
    F.when(F.isnan(col('salary')), None).otherwise(col('salary'))
).fillna({'salary': 0})
```

---

## `na.replace()` — Replace Values

```python
# Replace specific values with another value
df.na.replace('N/A', 'Unknown', subset=['name'])
df.na.replace([0, -1], None, subset=['salary'])   # replace 0 and -1 with null
```

---

## NULL in Aggregations

```python
# count('*') counts all rows including those with nulls
# count(col) counts only non-null values
df.groupBy('dept').agg(
    F.count('*').alias('total_rows'),
    F.count('salary').alias('non_null_salary_rows')
)

# NaN propagates through sum/avg
df.groupBy('dept').agg(F.sum('salary'))  # if any salary is NaN, result is NaN
# Fix: remove NaN first
df.filter(~F.isnan('salary')).groupBy('dept').agg(F.sum('salary'))
```

---

## NULL in Joins

By default, NULL keys do NOT match in joins (consistent with SQL NULL semantics):

```python
# Rows with NULL in the join key on either side are EXCLUDED from inner join
df_a.join(df_b, on='id', how='inner')  # NULL ids on either side → dropped

# To match NULLs: use a condition with eqNullSafe
df_a.join(df_b, df_a['id'].eqNullSafe(df_b['id']), how='inner')
```

---

## `DataFrameNaFunctions` (`df.na`)

The `.na` accessor provides the same methods as a fluent API:

```python
df.na.drop()              # same as dropna()
df.na.fill(0)             # same as fillna(0)
df.na.replace(old, new)   # replace exact values
```

---

## Exam Checklist

- [ ] Know the difference: NULL = absent value; NaN = invalid float (only in float/double columns)
- [ ] Know `isNull()` / `isNotNull()` are for NULLs; `F.isnan()` is for NaN
- [ ] Know `dropna()` and `fillna()` only operate on NULLs (not NaN)
- [ ] Know how to handle NaN: use `when(F.isnan(col), replacement)`
- [ ] Know `count('*')` includes NULLs; `count(col)` excludes NULLs
- [ ] Know NaN propagates through sum/avg aggregations
- [ ] Know NULL keys do NOT match in joins (use `eqNullSafe` to match NULLs)

---

[⬅️ topic3-prompt15-aggregations-grouping.md](topic3-prompt15-aggregations-grouping.md) | **16 / 32** | [Next ➡️ topic3-prompt17-joins.md](topic3-prompt17-joins.md)
