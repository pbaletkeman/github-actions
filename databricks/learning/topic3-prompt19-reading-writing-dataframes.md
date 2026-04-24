# Reading and Writing DataFrames

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Spark's DataFrameReader and DataFrameWriter APIs provide a unified interface for reading from and writing to many storage formats and systems. This section covers the generic API, format-specific shortcuts, write modes, and key options for CSV, JSON, Parquet, and ORC.

---

## DataFrameReader API

### Generic Form

```python
df = spark.read \
    .format('csv') \
    .option('header', True) \
    .option('inferSchema', True) \
    .load('/path/to/file.csv')
```

### Format Shortcuts

```python
df = spark.read.csv('/path/to/file.csv', header=True, inferSchema=True)
df = spark.read.json('/path/to/file.json')
df = spark.read.parquet('/path/to/file.parquet')
df = spark.read.orc('/path/to/file.orc')
df = spark.read.text('/path/to/file.txt')
df = spark.read.table('my_database.my_table')   # from Hive/Catalog
```

---

## DataFrameWriter API

### Generic Form

```python
df.write \
    .format('parquet') \
    .mode('overwrite') \
    .option('compression', 'snappy') \
    .save('/output/path')
```

### Format Shortcuts

```python
df.write.parquet('/output/path', mode='overwrite')
df.write.csv('/output/path', header=True, mode='append')
df.write.json('/output/path')
df.write.orc('/output/path')
df.write.saveAsTable('my_table')   # writes to catalog
```

---

## Write Modes

| Mode | Behaviour | SQL Equivalent |
|------|-----------|----------------|
| `'overwrite'` | Delete existing data and write new | TRUNCATE + INSERT |
| `'append'` | Add to existing data | INSERT |
| `'ignore'` | Do nothing if data exists (no error) | INSERT IF NOT EXISTS |
| `'error'` / `'errorIfExists'` | **Default** — raise error if data exists | — |

```python
df.write.mode('overwrite').parquet('/path')
df.write.mode('append').parquet('/path')
df.write.mode('ignore').parquet('/path')    # silent no-op if path exists
```

---

## CSV Options

```python
df = spark.read \
    .option('header', True)           # first row = column names
    .option('inferSchema', True)       # auto-detect types (slower)
    .option('sep', ',')                # delimiter (also: 'delimiter')
    .option('nullValue', 'NA')         # string to treat as NULL
    .option('nanValue', 'NaN')         # string to treat as NaN
    .option('dateFormat', 'yyyy-MM-dd')
    .option('timestampFormat', 'yyyy-MM-dd HH:mm:ss')
    .option('encoding', 'UTF-8')
    .option('multiLine', True)         # for fields with embedded newlines
    .option('quote', '"')              # quote character
    .option('escape', '\\')            # escape character
    .option('ignoreLeadingWhiteSpace', True)
    .option('ignoreTrailingWhiteSpace', True)
    .csv('/path/to/file.csv')

# Writing CSV
df.write \
    .option('header', True) \
    .option('sep', '|') \
    .csv('/output/csv')
```

---

## JSON Options

```python
df = spark.read \
    .option('multiLine', True)       # for multi-line JSON documents
    .option('allowComments', True)   # allow // comments
    .option('allowSingleQuotes', True) \
    .json('/path/to/file.json')
```

---

## Parquet (Recommended Format)

```python
# Read
df = spark.read.parquet('/path/to/parquet/')
df = spark.read.parquet('/path/to/parquet/', '/path/to/more/parquet/')  # multiple paths

# Write with Snappy compression (default)
df.write.mode('overwrite').parquet('/output/parquet')

# Write with GZip
df.write.option('compression', 'gzip').parquet('/output/parquet')
```

Parquet is preferred for Spark because:
- **Columnar** — column pruning reads only needed columns
- **Compressed** — smaller storage footprint
- **Schema embedded** — no need to infer schema
- **Predicate pushdown** — Parquet column statistics enable early filtering

---

## Partition Discovery and `partitionBy`

### Writing with Partitions

```python
df.write \
    .partitionBy('year', 'month') \
    .mode('overwrite') \
    .parquet('/output')

# Creates directory structure:
# /output/year=2023/month=01/part-0000.parquet
# /output/year=2023/month=02/part-0001.parquet
# /output/year=2024/month=01/part-0002.parquet
```

### Reading with Partition Pruning

```python
df = spark.read.parquet('/output')
# Filter on partition column → Spark only reads matching subdirectories
df.filter(col('year') == 2024)   # only reads /output/year=2024/
```

The partition columns are automatically added to the DataFrame schema.

---

## Schema with Reads

```python
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

schema = StructType([
    StructField('name', StringType()),
    StructField('age',  IntegerType()),
])

# Provide explicit schema — avoids inferSchema scan
df = spark.read.schema(schema).csv('/path', header=True)
```

---

## Reading Multiple Files / Wildcards

```python
# Read all parquet files matching a pattern
df = spark.read.parquet('/data/sales/year=2024/month=*/part-*.parquet')

# Read a whole directory
df = spark.read.parquet('/data/sales/')

# Read multiple specific paths
df = spark.read.parquet('/data/jan/', '/data/feb/', '/data/mar/')
```

---

## Exam Checklist

- [ ] Know both the generic API form and format shortcuts
- [ ] Know the 4 write modes (default is `error`)
- [ ] Know key CSV options: `header`, `inferSchema`, `sep`, `nullValue`
- [ ] Know why Parquet is preferred (columnar, compressed, schema embedded, predicate pushdown)
- [ ] Know that `partitionBy` on write creates a directory hierarchy
- [ ] Know that filtering on a partition column triggers partition pruning
- [ ] Know how to provide an explicit schema with `.schema(schema_obj)`

---

[⬅️ topic3-prompt18-combining-dataframes.md](topic3-prompt18-combining-dataframes.md) | **19 / 32** | [Next ➡️ topic3-prompt20-repartition-coalesce.md](topic3-prompt20-repartition-coalesce.md)
