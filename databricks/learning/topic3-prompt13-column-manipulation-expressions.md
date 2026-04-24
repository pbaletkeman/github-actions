# Column Manipulation and Expressions

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Column manipulation is the most common daily DataFrame operation. This covers adding/replacing columns with `withColumn()`, bulk mutations, type casting, dropping columns, and using `expr()` for SQL-style expressions.

---

## `withColumn()`

```python
from pyspark.sql.functions import col, upper, when, lit, expr

# Add a new column
df.withColumn('bonus', col('salary') * 0.10)

# Replace an existing column (same name)
df.withColumn('salary', col('salary').cast('double'))

# Conditional column
df.withColumn('grade', when(col('score') >= 90, 'A')
                       .when(col('score') >= 80, 'B')
                       .otherwise('C'))

# Constant (literal) column
df.withColumn('currency', lit('USD'))

# Derived from SQL expression
df.withColumn('upper_name', expr('upper(name)'))
```

### `withColumn()` Pitfall on Wide DataFrames

Each `withColumn()` call creates a new query plan node. Chaining 100+ `withColumn()` calls on a very wide DataFrame can cause:
- Slow plan analysis
- StackOverflow during planning (pre-Spark 3.x)

**Fix:** Use a single `select()` for many simultaneous column transformations:
```python
# Bad — 3 separate withColumn() plan nodes
df.withColumn('a', ...).withColumn('b', ...).withColumn('c', ...)

# Better — single plan node for all 3
df.select('*', expr('...').alias('a'), expr('...').alias('b'), expr('...').alias('c'))
```

---

## `withColumns()` — Bulk Add/Replace (Spark 3.3+)

```python
# Add or replace multiple columns in one call
df.withColumns({
    'bonus':       col('salary') * 0.10,
    'upper_name':  upper(col('name')),
    'salary_k':    (col('salary') / 1000).cast('double'),
})
```

---

## `drop()`

```python
df.drop('age')                 # drop one column
df.drop('age', 'department')   # drop multiple columns
df.drop(col('age'))            # using Column object (useful to avoid ambiguity in joins)
```

---

## Type Casting

```python
# cast() / astype() — synonyms
df.withColumn('age', col('age').cast('integer'))
df.withColumn('age', col('age').cast(IntegerType()))
df.withColumn('age', col('age').astype('int'))      # alias for cast()

# In selectExpr / expr
df.selectExpr('CAST(age AS INT) AS age')
df.withColumn('age', expr('CAST(age AS INT)'))
```

### Common Cast Types

| Python String | Equivalent Type | Notes |
|--------------|----------------|-------|
| `'int'` / `'integer'` | `IntegerType()` | 32-bit |
| `'long'` / `'bigint'` | `LongType()` | 64-bit |
| `'float'` | `FloatType()` | 32-bit float |
| `'double'` | `DoubleType()` | 64-bit float |
| `'string'` | `StringType()` | |
| `'boolean'` / `'bool'` | `BooleanType()` | |
| `'date'` | `DateType()` | `yyyy-MM-dd` |
| `'timestamp'` | `TimestampType()` | |
| `'decimal(p,s)'` | `DecimalType(p, s)` | Exact decimal |

---

## `expr()` — SQL Expressions as Column Objects

```python
from pyspark.sql.functions import expr

df.withColumn('full_name', expr("concat(first_name, ' ', last_name)"))
df.filter(expr('salary > 50000 AND dept = "Engineering"'))
df.orderBy(expr('salary DESC'))
df.select(expr('salary * 1.1 AS adjusted'))
```

`expr()` is equivalent to `selectExpr()` but returns a Column object usable anywhere `col()` is used.

---

## Column Aliasing

```python
col('salary').alias('annual_salary')
col('salary').name('annual_salary')     # synonym for .alias()

# In select
df.select(col('salary').alias('annual_salary'))

# In agg
df.groupBy('dept').agg(F.sum('salary').alias('total_salary'))
```

---

## Exam Checklist

- [ ] Know `withColumn()` for adding and replacing columns
- [ ] Know `withColumns()` is Spark 3.3+ for bulk mutations
- [ ] Know `drop()` and that it can take multiple column names
- [ ] Know `cast()` and `astype()` are synonyms; know common type strings
- [ ] Know `expr()` accepts SQL expressions and returns a Column object
- [ ] Know `selectExpr()` accepts SQL expression strings directly
- [ ] Know the performance pitfall of chaining many `withColumn()` calls on wide DataFrames

---

[⬅️ topic3-prompt12-dataframe-creation-selection.md](topic3-prompt12-dataframe-creation-selection.md) | **13 / 32** | [Next ➡️ topic3-prompt14-filtering-row-manipulation.md](topic3-prompt14-filtering-row-manipulation.md)
