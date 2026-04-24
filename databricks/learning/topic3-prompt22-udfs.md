# User Defined Functions (UDFs)

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

When Spark's built-in functions don't cover a requirement, you can write custom logic in Python via UDFs. However, UDFs have performance implications that must be understood for the exam.

---

## Performance Preference Hierarchy

```
Built-in SQL functions  >  Pandas UDF (Vectorized)  >  Regular Python UDF  (slowest)
```

**Always try built-in functions first.** If you must use a UDF, prefer Pandas UDFs for any computation that can be expressed over a Series/batch.

---

## Regular Python UDFs

### Why They Are Slow

1. Spark serialises each row from the JVM to Python using **Pickle**
2. Python executes the function **row by row**
3. Results are serialised back to JVM
4. This round-trip happens for every single row

### Creating a Regular UDF

```python
from pyspark.sql.functions import udf
from pyspark.sql.types import StringType, IntegerType

# Method 1: udf() wrapping a function
def format_name(name):
    return name.strip().title() if name else None

format_name_udf = udf(format_name, StringType())

# Method 2: udf() with lambda
upper_udf = udf(lambda x: x.upper() if x else None, StringType())

# Method 3: @udf decorator
@udf(returnType=StringType())
def clean_name(name):
    return name.strip().title() if name else None
```

**CRITICAL: Always specify `returnType`.** If omitted, Spark defaults to `StringType()` silently — your function may return integers/booleans that get silently converted.

### Using Regular UDFs

```python
# In select / withColumn
df.withColumn('clean', clean_name(col('name')))
df.select(upper_udf(col('product')).alias('upper_product'))

# In filter
df.filter(is_valid_udf(col('email')))
```

---

## Registering UDFs for SQL

```python
# Register under a SQL name
spark.udf.register('clean_name', clean_name_fn, StringType())

# Use in SQL
spark.sql("SELECT clean_name(name) AS clean FROM users")

# Or via createOrReplaceTempView
df.createOrReplaceTempView('users')
spark.sql("SELECT clean_name(name) FROM users")
```

---

## Null Handling in UDFs

```python
# BAD: will raise NullPointerException if name is null
@udf(returnType=StringType())
def bad_udf(name):
    return name.strip()   # AttributeError if name is None

# GOOD: guard against None/null
@udf(returnType=StringType())
def safe_udf(name):
    if name is None:
        return None   # propagate null
    return name.strip()
```

---

## Pandas UDFs (Vectorized UDFs)

Pandas UDFs process a **pandas Series** (or DataFrame) at a time, using Apache Arrow for efficient JVM ↔ Python data transfer without row-by-row serialisation.

### Scalar Pandas UDF (most common)

```python
from pyspark.sql.functions import pandas_udf
from pyspark.sql.types import DoubleType
import pandas as pd

@pandas_udf(returnType=DoubleType())
def normalise(s: pd.Series) -> pd.Series:
    return (s - s.mean()) / s.std()

df.withColumn('normalised', normalise(col('score')))
```

### Iterator of Series Pandas UDF

```python
from typing import Iterator

@pandas_udf(returnType=StringType())
def batch_clean(iterator: Iterator[pd.Series]) -> Iterator[pd.Series]:
    for s in iterator:
        yield s.str.strip().str.title()

df.withColumn('clean', batch_clean(col('name')))
```

### Grouped Map: `applyInPandas`

```python
def normalise_within_group(df: pd.DataFrame) -> pd.DataFrame:
    df['score_normalised'] = (df['score'] - df['score'].mean()) / df['score'].std()
    return df

schema = 'id INT, group STRING, score DOUBLE, score_normalised DOUBLE'
df.groupBy('group').applyInPandas(normalise_within_group, schema=schema)
```

---

## UDF Performance Comparison

| UDF Type | Serialisation | Execution | Use When |
|----------|-------------|-----------|----------|
| Built-in function | None | JVM native | Always prefer |
| Pandas UDF | Apache Arrow (batch) | Python (vectorised) | Complex Python logic, ML transforms |
| Regular UDF | Pickle (row-by-row) | Python interpreter | Simple logic, when Pandas not worth the overhead |

---

## Exam Checklist

- [ ] Know the performance hierarchy: built-in > Pandas UDF > Regular UDF
- [ ] Know why regular UDFs are slow (Pickle serialisation, row-by-row)
- [ ] Know the 3 ways to create a regular UDF: `udf()`, lambda in `udf()`, `@udf` decorator
- [ ] **Know that `returnType` is REQUIRED — omitting it silently defaults to StringType**
- [ ] Know how to register a UDF for SQL: `spark.udf.register('name', fn, returnType)`
- [ ] Know to always guard against None in UDF body
- [ ] Know Pandas UDFs use Apache Arrow for efficient batch transfer
- [ ] Know `applyInPandas` for group-wise DataFrame-to-DataFrame transformations

---

[⬅️ topic3-prompt21-schemas-data-types.md](topic3-prompt21-schemas-data-types.md) | **22 / 32** | [Next ➡️ topic3-prompt23-end-to-end-scenarios.md](topic3-prompt23-end-to-end-scenarios.md)
