# Common Spark Errors and How to Fix Them

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 4 — Optimisation and Troubleshooting (12%)

---

## Overview

Understanding the most common Spark errors — their root causes and fixes — is a core exam skill. Each error has a specific root cause and a targeted resolution strategy.

---

## OutOfMemoryError (Executor)

**Error message:** `java.lang.OutOfMemoryError: Java heap space` (on executor)

**Cause:** The data assigned to a single partition (or task) is too large to fit in the executor's allocated memory.

**Diagnosis:**
- Check Spark UI → Stages → look for a stage with very large shuffle read or input data per task
- Check executor memory usage in Executors tab

**Fixes:**
1. Increase `spark.executor.memory` (e.g., `--executor-memory 4g`)
2. Increase `spark.executor.memoryOverhead` for Python processes
3. Increase partition count to reduce data per partition: `df.repartition(500)`
4. Reduce `spark.sql.shuffle.partitions` if most tasks are tiny (frees memory per task)
5. Use `MEMORY_AND_DISK` storage level instead of `MEMORY_ONLY`
6. Enable AQE skew join handling

---

## OutOfMemoryError (Driver)

**Error message:** `java.lang.OutOfMemoryError: Java heap space` (on driver)

**Cause:** Too much data collected to the driver — typically via `collect()`, `toPandas()`, or `broadcast()` of a large object.

**Fixes:**
1. Never call `collect()` on a large DataFrame — use `write()` to save to storage
2. Use `limit(n)` before `collect()` to restrict result set
3. Increase `spark.driver.memory`
4. Avoid `broadcast()` on large objects

```python
# BAD: collects entire DataFrame to driver
rows = df.collect()

# GOOD: sample or limit first
rows = df.limit(100).collect()

# GOOD: write to storage instead of collecting
df.write.parquet('/output/path')
```

---

## AnalysisException: Cannot Resolve Column

**Error message:** `org.apache.spark.sql.AnalysisException: cannot resolve 'column_name'`

**Cause:**
- Column name typo or wrong case
- Column doesn't exist in the DataFrame at that point in the plan
- Referencing a column from a DataFrame that's out of scope

**Diagnosis / Fixes:**
```python
# Check exact column names
print(df.columns)
df.printSchema()

# Case sensitivity: Spark column names are case-insensitive by default in SQL,
# but case-sensitive in DataFrame API
df.filter(col('Name'))   # works even if column is 'name'
```

---

## AnalysisException: Ambiguous Column (After Join)

**Error message:** `AnalysisException: Resolved attribute(s) ... missing from child`

**Cause:** After a join using a Column expression (`df1['id'] == df2['id']`), both DataFrames contribute a column with the same name, causing ambiguity.

**Fixes:**
```python
# Solution 1: Use string join key (deduplicates automatically)
df1.join(df2, on='id')

# Solution 2: Use DataFrame aliases
a = df1.alias('a')
b = df2.alias('b')
a.join(b, col('a.id') == col('b.id')).select('a.id', 'a.name', 'b.email')

# Solution 3: Drop duplicate column after join
result = df1.join(df2, df1['id'] == df2['customer_id'])
result.drop(df2['id'])
```

---

## Task Not Serializable

**Error message:** `org.apache.spark.SparkException: Task not serializable`

**Cause:** A UDF closure captures a non-serialisable object (e.g., a `SparkSession`, a database connection, a non-serialisable class instance).

**Fix:**
```python
# BAD: captures outer spark session
spark = SparkSession.getActiveSession()
@udf(returnType=StringType())
def bad_udf(x):
    return spark.sql("...").collect()[0][0]  # ❌ SparkSession is not serialisable

# GOOD: create the non-serialisable object INSIDE the UDF
@udf(returnType=StringType())
def good_udf(x):
    import mymodule
    connection = mymodule.create_connection()  # created fresh per task
    return connection.lookup(x)

# GOOD: use broadcast variables for data
lookup_data = spark.sparkContext.broadcast({'A': 1, 'B': 2})
@udf(returnType=IntegerType())
def lookup_udf(x):
    return lookup_data.value.get(x, 0)  # broadcast is serialisable
```

---

## StackOverflowError

**Error message:** `java.lang.StackOverflowError`

**Cause:** An excessively long lineage (DAG) — typically from many iterative transformations chained without materialising intermediate results.

**Fix: Checkpoint to break the lineage**

```python
# Set checkpoint directory (once per session)
spark.sparkContext.setCheckpointDir('/tmp/spark-checkpoint')

# Checkpoint in the middle of a long iterative pipeline
df_checkpoint = df_iteration_50.checkpoint()   # forces materialisation, breaks lineage
df_iteration_51 = df_checkpoint.someTransformation()
```

---

## Straggler Tasks / Data Skew

**Symptoms:** One task takes 100× longer than the median task in the same stage.

**Diagnosis:** Spark UI → Stages → Tasks tab → compare Min/Median/Max task duration

**Fixes:**
1. Enable AQE: `spark.sql.adaptive.enabled = true` (Spark 3.2+ default)
2. Increase `spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes`
3. Manual salting of skewed join key
4. Pre-aggregate the skewed key

---

## NullPointerException in UDF

**Cause:** UDF received a NULL value and tried to call a method on it (Python: `AttributeError`; JVM: `NullPointerException`).

**Fix:**
```python
# Always guard against None at the start of every UDF
@udf(returnType=StringType())
def safe_udf(name):
    if name is None:
        return None    # propagate null
    return name.strip().upper()
```

---

## Schema Mismatch on Write (Delta)

**Error:** Schema mismatch when writing to an existing Delta table.

**Fix:**
```python
df.write \
  .format('delta') \
  .mode('overwrite') \
  .option('overwriteSchema', 'true') \   # allow schema evolution on overwrite
  .save('/delta/table')
```

---

## Exam Checklist

- [ ] Know executor OOM causes: partition too large; fix: increase memory, repartition
- [ ] Know driver OOM causes: `collect()` on large DataFrame; fix: use `limit()` or `write()`
- [ ] Know `AnalysisException` for missing columns: check `df.columns` / `df.printSchema()`
- [ ] Know ambiguous column after join: use string join or alias
- [ ] Know Task Not Serializable: SparkSession/connection in UDF closure; create inside UDF or use broadcast
- [ ] Know StackOverflow: long lineage; fix with `checkpoint()`
- [ ] Know straggler tasks = data skew; diagnose in Tasks tab (max vs median time)
- [ ] Know NullPointerException in UDF: always guard with `if x is None: return None`

---

[⬅️ topic4-prompt24-performance-tuning.md](topic4-prompt24-performance-tuning.md) | **25 / 32** | [Next ➡️ topic4-prompt26-debugging.md](topic4-prompt26-debugging.md)
