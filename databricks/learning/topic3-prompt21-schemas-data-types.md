# Schemas and Data Types

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Spark's type system is strongly typed. Understanding how to define and inspect schemas, work with the full range of primitive and complex data types, and access nested structures is fundamental to DataFrame programming.

---

## Defining Schemas

### StructType / StructField (Programmatic)

```python
from pyspark.sql.types import (
    StructType, StructField,
    StringType, IntegerType, LongType, DoubleType, FloatType,
    BooleanType, DateType, TimestampType, DecimalType,
    ArrayType, MapType
)

schema = StructType([
    StructField('id',         LongType(),    nullable=False),
    StructField('name',       StringType(),  nullable=True),
    StructField('age',        IntegerType(), nullable=True),
    StructField('salary',     DoubleType(),  nullable=True),
    StructField('is_active',  BooleanType(), nullable=True),
    StructField('hire_date',  DateType(),    nullable=True),
])
```

`StructField` parameters: `name` (str), `dataType` (DataType instance), `nullable` (bool, default True), `metadata` (dict, default `{}`).

### DDL String (Concise)

```python
schema = 'id LONG NOT NULL, name STRING, age INT, salary DOUBLE'

# Complex types in DDL
schema = 'id INT, tags ARRAY<STRING>, props MAP<STRING, INT>'
```

---

## Primitive Data Types

| Spark Type | Python Type | DDL Keyword | Notes |
|-----------|------------|-------------|-------|
| `StringType` | str | `STRING` | UTF-8 |
| `IntegerType` | int | `INT` / `INTEGER` | 32-bit signed |
| `LongType` | int | `LONG` / `BIGINT` | 64-bit signed |
| `ShortType` | int | `SHORT` / `SMALLINT` | 16-bit signed |
| `ByteType` | int | `BYTE` / `TINYINT` | 8-bit signed |
| `FloatType` | float | `FLOAT` | 32-bit |
| `DoubleType` | float | `DOUBLE` | 64-bit |
| `DecimalType(p,s)` | Decimal | `DECIMAL(p,s)` | Exact decimal |
| `BooleanType` | bool | `BOOLEAN` | |
| `DateType` | datetime.date | `DATE` | yyyy-MM-dd |
| `TimestampType` | datetime.datetime | `TIMESTAMP` | With timezone |
| `TimestampNTZType` | datetime.datetime | `TIMESTAMP_NTZ` | No timezone (Spark 3.4+) |
| `BinaryType` | bytes | `BINARY` | Raw bytes |
| `NullType` | None | `NULL` | Only null values |

---

## Complex Data Types

### ArrayType

```python
from pyspark.sql.types import ArrayType, StringType

# StructField
StructField('tags', ArrayType(StringType(), containsNull=True))

# DDL
'tags ARRAY<STRING>'
```

`containsNull`: whether array elements can be null (default True).

### MapType

```python
from pyspark.sql.types import MapType, StringType, IntegerType

# StructField
StructField('props', MapType(StringType(), IntegerType(), valueContainsNull=True))

# DDL
'props MAP<STRING, INT>'
```

### Nested StructType

```python
address_schema = StructType([
    StructField('street', StringType()),
    StructField('city',   StringType()),
    StructField('zip',    StringType()),
])

schema = StructType([
    StructField('id',      IntegerType()),
    StructField('name',    StringType()),
    StructField('address', address_schema),  # nested struct
])
```

---

## Accessing Complex Types

```python
# Nested struct: two ways
df.select('address.city')             # dot notation (string)
df.select(col('address').getField('city'))   # getField()

# Array element (0-based)
df.select(col('scores')[0])            # subscript
df.select(col('scores').getItem(0))    # getItem()

# Map element by key
df.select(col('props')['color'])       # subscript
df.select(col('props').getItem('color'))  # getItem()
```

---

## Schema Inspection

```python
df.printSchema()   # tree view with types and nullable flags — NOT an action
df.schema          # StructType object
df.dtypes          # list of (col_name, type_string) tuples
df.columns         # list of column name strings

# Access a field's type programmatically
field = df.schema['salary']
print(field.dataType)    # DoubleType()
print(field.nullable)    # True/False
```

---

## Schema Inference Trade-offs

| Approach | Pros | Cons |
|----------|------|------|
| `inferSchema=True` (CSV/JSON) | Convenient | Requires extra data scan; may infer wrong types |
| DDL string | Concise, readable | No IDE support; typos not caught at definition |
| StructType/StructField | Full control; programmatic | Verbose |
| Parquet/ORC schema | Auto-detected; embedded in file | Must trust the file's embedded schema |

---

## Common Schema Operations

```python
# Cast a column type
df.withColumn('age', col('age').cast(IntegerType()))
df.withColumn('age', col('age').cast('int'))

# Check for schema compatibility before union
df_a.schema == df_b.schema

# Create DataFrame from schema only (zero rows — useful for empty DataFrame)
empty_df = spark.createDataFrame([], schema=schema)
```

---

## Exam Checklist

- [ ] Know `StructType`, `StructField`, and the 4 StructField parameters
- [ ] Know common primitive types and their DDL keywords
- [ ] Know `ArrayType(elementType, containsNull)` and `MapType(keyType, valueType, valueContainsNull)`
- [ ] Know how to access nested structs: dot notation or `getField()`
- [ ] Know how to access array/map elements: subscript `[0]` or `getItem(0)`
- [ ] Know `printSchema()` is NOT an action (no Spark job triggered)
- [ ] Know `df.schema` returns StructType; `df.dtypes` returns list of tuples

---

[⬅️ topic3-prompt20-repartition-coalesce.md](topic3-prompt20-repartition-coalesce.md) | **21 / 32** | [Next ➡️ topic3-prompt22-udfs.md](topic3-prompt22-udfs.md)
