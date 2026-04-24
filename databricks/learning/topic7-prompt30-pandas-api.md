# Pandas API on Apache Spark

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 7 — Pandas API on Spark (6%)

---

## Overview

The Pandas API on Spark (formerly the Koalas project) allows you to write pandas-like code that executes on a distributed Spark cluster. It was merged into PySpark as of Spark 3.2+.

**Use case:** Migrate existing pandas workbooks to Spark scale with minimal code changes.

---

## Import

```python
import pyspark.pandas as ps       # ✅ Correct import
import pandas as pd               # Regular pandas (single-node only)
```

**Do NOT confuse:**
- `pyspark.pandas` → runs on Spark cluster (distributed)
- `pandas` → runs on a single machine

---

## Creating Spark Pandas DataFrames

```python
import pyspark.pandas as ps

# From a dict
psdf = ps.DataFrame({'a': [1, 2, 3], 'b': ['x', 'y', 'z']})

# From a CSV file (distributed)
psdf = ps.read_csv('/mnt/data/file.csv')

# From a pandas DataFrame
import pandas as pd
pdf = pd.DataFrame({'a': [1, 2, 3]})
psdf = ps.from_pandas(pdf)

# From a PySpark DataFrame
psdf = spark_df.pandas_api()
```

---

## Type Conversions

```python
# pandas DataFrame → pyspark.pandas DataFrame
psdf = ps.from_pandas(pdf)

# pyspark.pandas DataFrame → pandas DataFrame (COLLECTS to driver)
pdf = psdf.to_pandas()          # ⚠️ Dangerous on large DataFrames — brings all data to driver

# pyspark.pandas DataFrame → PySpark DataFrame
sdf = psdf.to_spark()

# PySpark DataFrame → pyspark.pandas DataFrame
psdf = sdf.pandas_api()
```

### Conversion Direction Map

```
pandas (pdf)  ──ps.from_pandas()──►  pyspark.pandas (psdf)
                                            │
                          psdf.to_pandas() ◄┘  (collects to driver)
                          psdf.to_spark()  →  PySpark DataFrame (sdf)
                          sdf.pandas_api() →  pyspark.pandas (psdf)
```

---

## Using the pandas API

```python
# Standard pandas operations work as expected
psdf['c'] = psdf['a'] * 2
psdf = psdf[psdf['a'] > 1]
psdf.groupby('b').sum()
psdf.sort_values('a')
psdf.describe()
psdf.head(10)
psdf.info()
```

---

## Key Differences vs Pandas

| Behaviour | pandas | pyspark.pandas |
|-----------|--------|----------------|
| Row order | Deterministic | **No guaranteed order** (distributed) |
| Index | Integer index | `default_index_type` setting controls |
| `to_pandas()` | No-op | Collects ALL data to driver — can OOM |
| Missing operations | N/A | Some operations not supported or have different semantics |
| Expensive operations | Cheap (local) | Some ops trigger shuffles |

### Default Index Type

```python
# Default: 'distributed' — generates unique but non-sequential index
ps.set_option('compute.default_index_type', 'distributed')

# Sequential: requires global sort — expensive
ps.set_option('compute.default_index_type', 'sequence')

# Distributed-sequence: uses partition + offset — no global sort
ps.set_option('compute.default_index_type', 'distributed-sequence')
```

---

## Operations to Watch

```python
# ⚠️ These trigger a full shuffle or collect:
psdf.sort_values('col')          # requires global sort
psdf.to_pandas()                  # collects all to driver
len(psdf)                         # triggers count action

# ✅ These are efficient:
psdf.head(10)                     # uses limit internally
psdf.filter(psdf['a'] > 1)       # predicate pushdown
psdf.groupby('b').mean()          # distributed aggregation
```

---

## Koalas History

- **Koalas:** Open source project started by Databricks (2019)
- **Spark 3.2:** Koalas merged into PySpark as `pyspark.pandas`
- **Legacy import:** `import databricks.koalas as ks` — still available on Databricks Runtime but deprecated

---

## Exam Checklist

- [ ] Know the import is `import pyspark.pandas as ps`
- [ ] Know the 4 ways to create a pyspark.pandas DataFrame: dict, `read_csv`, `from_pandas`, `sdf.pandas_api()`
- [ ] Know the 4 conversion methods and when each is used
- [ ] Know `to_pandas()` collects ALL data to driver — dangerous for large DataFrames
- [ ] Know there is **no guaranteed row order** in pyspark.pandas
- [ ] Know `default_index_type` config controls the index generation strategy
- [ ] Know pyspark.pandas was merged from Koalas project in Spark 3.2+
- [ ] Know `sdf.pandas_api()` converts a PySpark DataFrame to pyspark.pandas

---

[⬅️ topic6-prompt29-spark-connect.md](topic6-prompt29-spark-connect.md) | **30 / 32** | [Next ➡️ topic-prompt31-practice-exam.md](topic-prompt31-practice-exam.md)
