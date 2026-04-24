# DataFrame Creation, Column Selection, and Renaming

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

This section covers all the ways to create DataFrames, define schemas explicitly or let Spark infer them, and the core column selection and renaming operations.

---

## Creating DataFrames

### From Python Data

```python
# From a list of tuples
data = [('Alice', 30, 'Engineering'), ('Bob', 25, 'Marketing')]
columns = ['name', 'age', 'department']

df = spark.createDataFrame(data, schema=columns)   # schema as list of column names
df = spark.createDataFrame(data, schema='name STRING, age INT, department STRING')  # DDL string
```

### From a Range

```python
df = spark.range(10)           # DataFrame with column 'id' from 0 to 9
df = spark.range(0, 100, 2)    # start=0, stop=100, step=2
```

### From Files

```python
df = spark.read.csv('/data/file.csv', header=True, inferSchema=True)
df = spark.read.parquet('/data/parquet/')
df = spark.read.json('/data/file.json')
df = spark.read.orc('/data/file.orc')

# Generic format API
df = spark.read.format('csv').option('header', True).load('/data/file.csv')
```

### From RDD

```python
rdd = sc.parallelize([('Alice', 30), ('Bob', 25)])
df = rdd.toDF(['name', 'age'])
df = spark.createDataFrame(rdd, schema=['name', 'age'])
```

### From SQL

```python
df = spark.sql('SELECT * FROM my_table')
df = spark.table('my_table')    # shorthand — reads from catalog
```

---

## Schema Definition

### StructType / StructField

```python
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

schema = StructType([
    StructField('name',       StringType(),  nullable=True),
    StructField('age',        IntegerType(), nullable=False),
    StructField('department', StringType(),  nullable=True),
])

df = spark.createDataFrame(data, schema=schema)
```

### DDL String (Concise)

```python
df = spark.createDataFrame(data, schema='name STRING NOT NULL, age INT, dept STRING')
```

### Schema Inference

```python
# inferSchema=True — reads the data to determine types; slower, may be wrong
df = spark.read.csv('/path', header=True, inferSchema=True)
```

**Best practice:** Define schema explicitly for production jobs. Schema inference scans the data twice and can infer wrong types (e.g., all-integer column inferred as `LongType`).

---

## Column Selection

### `select()`

```python
from pyspark.sql.functions import col, upper

df.select('name', 'age')                    # string column names
df.select(col('name'), col('age'))           # Column objects
df.select(df['name'], df['age'])             # DataFrame column syntax
df.select('*')                               # all columns
df.select(col('name').alias('full_name'))    # with alias
df.select(upper(col('name')).alias('upper_name'))  # with transformation
```

### `selectExpr()` — SQL Expressions

```python
df.selectExpr('name AS full_name', 'age + 1 AS next_age', 'upper(name) AS upper_name')
```

---

## Column Renaming

```python
# Rename a single column
df.withColumnRenamed('name', 'full_name')

# Rename using alias in select
df.select(col('name').alias('full_name'), col('age'))

# toDF() — rename all columns at once (list must match current column order)
df.toDF('full_name', 'age', 'department')
```

---

## Accessing Columns

```python
# Three equivalent ways to reference a column:
col('name')          # column object (preferred — independent of DataFrame)
df['name']           # DataFrame subscript (useful in joins to disambiguate)
df.name              # attribute (don't use — fails if column name has spaces)
```

---

## Schema Inspection

```python
df.printSchema()    # pretty-print schema tree (NOT an action — no job triggered)
df.schema           # StructType object
df.dtypes           # list of (name, type_string) tuples
df.columns          # list of column name strings
```

---

## Exam Checklist

- [ ] Know all 7 DataFrame creation methods
- [ ] Know how to define a schema with StructType/StructField vs DDL string
- [ ] Know that explicit schemas are preferred over `inferSchema=True` in production
- [ ] Know `select()` vs `selectExpr()` — when to use each
- [ ] Know `withColumnRenamed()` vs `alias()` vs `toDF()` for renaming
- [ ] Know `printSchema()` is NOT an action
- [ ] Know `spark.table()` vs `spark.sql('SELECT * FROM table')`

---

[⬅️ topic2-prompt11-query-optimization.md](topic2-prompt11-query-optimization.md) | **12 / 32** | [Next ➡️ topic3-prompt13-column-manipulation-expressions.md](topic3-prompt13-column-manipulation-expressions.md)
