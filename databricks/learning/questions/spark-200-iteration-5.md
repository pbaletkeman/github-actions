# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 5)

**Iteration**: 5

**Generated**: 2026-04-25

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 78 `one` / 22 `many`

---

## Topic Distribution

| Topic | Questions | Range |
|-------|-----------|-------|
| Topic 1: Apache Spark Architecture & Internals — 20% | 20 | Q1–Q20 |
| Topic 2: Spark SQL — 20% | 20 | Q21–Q40 |
| Topic 3: DataFrame/DataSet API — 30% | 30 | Q41–Q70 |
| Topic 4: Troubleshooting & Tuning — 10% | 10 | Q71–Q80 |
| Topic 5: Structured Streaming — 10% | 10 | Q81–Q90 |
| Topic 6: Spark Connect — 5% | 5 | Q91–Q95 |
| Topic 7: Pandas API on Spark — 5% | 5 | Q96–Q100 |

---

## Questions

---

### Question 1 — Apache Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
Which statement correctly describes the difference between `spark.sql.shuffle.partitions` and `spark.default.parallelism`?

- A) Both control the same thing; they are interchangeable aliases for the same configuration
- B) `spark.sql.shuffle.partitions` governs the number of post-shuffle partitions in Spark SQL and DataFrame operations; `spark.default.parallelism` governs the default partition count for RDD operations such as `parallelize` and `reduceByKey`
- C) `spark.default.parallelism` controls Spark SQL shuffle partitions; `spark.sql.shuffle.partitions` controls RDD-level parallelism
- D) `spark.sql.shuffle.partitions` only applies to streaming pipelines; `spark.default.parallelism` applies to batch operations

---

### Question 2 — Apache Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
What is the default value of `spark.driver.memory` when it is not explicitly configured in the application or cluster settings?

- A) 512m
- B) 1g
- C) 2g
- D) 4g

---

### Question 3 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
An executor storing a partition at `StorageLevel.DISK_ONLY` fails. What does Spark do when that partition is subsequently needed by a downstream operation?

- A) The job fails immediately with a `TaskFailedException` because disk-only partitions cannot be recovered
- B) Spark recomputes the partition from its RDD or DataFrame lineage
- C) Spark reads the partition from the driver's local disk copy
- D) Spark retrieves the partition from a peer executor's disk cache

---

### Question 4 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Apache Spark Architecture

**Question**:
A data engineering team wants the Spark History Server to display completed application data. Which of the following must be correctly configured? *(Select all that apply.)*

- A) `spark.eventLog.enabled` must be set to `true` in the application configuration
- B) `spark.eventLog.dir` must point to a shared, durable storage location (such as HDFS or S3) that is accessible by both the application and the History Server
- C) `spark.history.fs.logDirectory` on the History Server must be set to the same path as `spark.eventLog.dir`
- D) `spark.ui.enabled` must be set to `true` to allow the History Server to serve the application UI

---

### Question 5 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
A developer calls `df.cache()` followed by `df.count()` to materialize the cache. They then run a second action on `df`. In the Spark UI, what is shown for the stages whose output is already in the cache?

- A) The stages are marked as **Failed** and then immediately retried from the cache
- B) The stages are marked as **Skipped** — Spark reads from the in-memory cache and does not re-execute them
- C) The stages are shown as **Running** briefly and then completed, because Spark always re-validates cached data
- D) The stages are not shown in the UI at all when cache is used

---

### Question 6 — Apache Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
What is the primary responsibility of the Spark Block Manager running on each executor?

- A) Managing only the shuffle write files written to local executor disk
- B) Managing only broadcast variables received from the driver
- C) Managing in-memory and on-disk storage of all blocks, including RDD partitions, cached DataFrame partitions, broadcast variable data, and shuffle data
- D) Managing only the execution memory consumed by running tasks

---

### Question 7 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Apache Spark Architecture

**Question**:
Which of the following statements about `spark.memory.storageFraction` (default `0.5`) are correct? *(Select all that apply.)*

- A) It defines the fraction of Spark's unified memory region that is initially reserved for storage (caching)
- B) The remaining fraction of unified memory is available for execution (shuffle, sort, aggregation)
- C) If execution memory runs low, Spark can evict cached blocks from the storage region to free space
- D) Setting `spark.memory.storageFraction=1.0` would leave no memory available for execution tasks

---

### Question 8 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
What distinguishes `StorageLevel.MEMORY_AND_DISK_SER` from `StorageLevel.MEMORY_AND_DISK`?

- A) `MEMORY_AND_DISK_SER` stores partitions in serialized (binary) format in memory and on disk, using less memory at the cost of CPU for serialization/deserialization; `MEMORY_AND_DISK` stores partitions as deserialized JVM objects
- B) `MEMORY_AND_DISK_SER` replicates each partition to a second executor's disk before writing to local disk
- C) `MEMORY_AND_DISK_SER` stores partitions off-heap in memory and on disk simultaneously
- D) `MEMORY_AND_DISK_SER` applies Java serialization; `MEMORY_AND_DISK` uses Kryo serialization

---

### Question 9 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
When deploying a Spark application on Kubernetes using `spark-submit` with `--master k8s://...`, which configuration property specifies the Docker image used for the driver and executor pods?

- A) `spark.kubernetes.executor.image`
- B) `spark.kubernetes.container.image`
- C) `spark.kubernetes.docker.image`
- D) `spark.executor.docker.containerImage`

---

### Question 10 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Apache Spark Architecture

**Question**:
Which of the following statements about `spark.rpc.message.maxSize` (default `128` MB) are correct? *(Select all that apply.)*

- A) Attempting to send an RPC message larger than this limit causes a `SparkException` to be thrown
- B) Broadcast variables transferred over RPC are subject to this size limit
- C) Large result sets returned by `collect()` from executors to the driver are governed by `spark.driver.maxResultSize`, not by `spark.rpc.message.maxSize`
- D) The limit can be raised by setting a larger integer value, for example `spark.rpc.message.maxSize=256`

---

### Question 11 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
In a sort-merge join, which phase is typically more expensive in terms of disk I/O, and why?

- A) The shuffle read phase, because each reducer must pull data from all mapper output files across the network and disk before sorting begins
- B) The shuffle write phase, because each mapper must sort its output records into partition buckets and write them to disk before any reducer can begin reading
- C) Both phases have exactly equal I/O cost by design in a symmetric sort-merge join
- D) Neither phase generates disk I/O because Spark uses in-memory shuffle pipelines exclusively

---

### Question 12 — Apache Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
Starting with Apache Spark 3.2, what is the default value of `spark.sql.adaptive.enabled`?

- A) `false` — Adaptive Query Execution must be explicitly enabled
- B) `true` — Adaptive Query Execution is enabled by default
- C) It depends on the cluster manager: `true` for YARN, `false` for Kubernetes
- D) It is determined at query planning time based on the estimated query cost

---

### Question 13 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
A Spark application is configured with both `spark.executor.instances=10` and `spark.dynamicAllocation.enabled=true`. What is the result?

- A) Both settings are honoured; `spark.executor.instances` becomes the fixed upper bound used by dynamic allocation
- B) Spark throws a configuration validation error at startup and refuses to launch
- C) Spark logs a warning and `spark.executor.instances` is ignored; dynamic allocation controls the executor count using `spark.dynamicAllocation.minExecutors` and `spark.dynamicAllocation.maxExecutors`
- D) Dynamic allocation is silently disabled; Spark starts exactly 10 executors and keeps them throughout the job

---

### Question 14 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
What storage requirement does Spark impose on the directory set via `sc.setCheckpointDir()`?

- A) It must be a local filesystem path writable by the driver process only
- B) It must be on a reliable, distributed file system (such as HDFS or S3) accessible by all executors, because checkpoint data must survive executor failures
- C) It must be on the same machine as the Spark master process
- D) There is no requirement; any writable path including `/tmp` provides reliable checkpointing

---

### Question 15 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Scenario**:
```python
df = spark.read.parquet("s3://bucket/data/")
result = df.repartition(100).groupBy("region").agg(F.sum("sales"))
result.show()
```

**Question**:
How many Spark stages does the above code produce?

- A) 1 stage
- B) 2 stages
- C) 3 stages
- D) 4 stages

---

### Question 16 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
Spark's task scheduler attempts to achieve data locality by waiting `spark.locality.wait` (default `3s`) before downgrading to the next level. In what order does Spark try locality levels, from most to least preferred?

- A) `ANY` → `RACK_LOCAL` → `NODE_LOCAL` → `PROCESS_LOCAL`
- B) `PROCESS_LOCAL` → `NODE_LOCAL` → `RACK_LOCAL` → `ANY`
- C) `NODE_LOCAL` → `PROCESS_LOCAL` → `RACK_LOCAL` → `ANY`
- D) `PROCESS_LOCAL` → `RACK_LOCAL` → `NODE_LOCAL` → `ANY`

---

### Question 17 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
What does the configuration `spark.reducer.maxReqsInFlight` control?

- A) The maximum number of concurrent shuffle block fetch requests a single reducer task can issue to remote executors simultaneously
- B) The maximum number of in-flight RPC messages from the driver to all executors combined
- C) The maximum number of speculative tasks that may run simultaneously across the cluster
- D) The maximum number of parallel JDBC partition read requests during a partitioned table scan

---

### Question 18 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Apache Spark Architecture

**Question**:
Spark evicts a partition cached at `StorageLevel.MEMORY_ONLY` to free space for an execution operation. Which of the following statements about this eviction are correct? *(Select all that apply.)*

- A) The evicted partition is dropped from memory entirely — it is not written to disk
- B) If the evicted partition is subsequently needed, Spark recomputes it from its lineage
- C) Eviction candidates within the storage region are selected based on a Least Recently Used (LRU) policy
- D) Because `MEMORY_ONLY` includes a disk fallback, the partition is transparently moved to disk after eviction

---

### Question 19 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
Which statement best describes how Spark executes multiple transformations (e.g., `filter`, `map`, `select`) that belong to the same stage?

- A) Each transformation fully materializes its entire output to memory before the next transformation begins
- B) Transformations within a stage are pipelined — each record flows through all operators in sequence without intermediate full materialization between them
- C) Transformations within a stage are executed in parallel on separate JVM threads
- D) Each transformation is compiled to a separate JVM class and executed in a forked process

---

### Question 20 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Apache Spark Architecture

**Question**:
What is the default value of `spark.sql.autoBroadcastJoinThreshold`, and what does it represent?

- A) `64 MB` — the maximum size of each individual partition that may be broadcast in a join
- B) `10 MB` — the maximum estimated size of a table that Spark will automatically broadcast in a join without requiring a broadcast hint
- C) `200 MB` — the threshold above which Spark switches from a broadcast join to a sort-merge join
- D) `10 MB` — the maximum total size of the broadcast variable stored across all executors combined

---

### Question 21 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Given the expression `datediff(endDate, startDate)`, what does the function return?

- A) The number of days from `endDate` to `startDate`, i.e., `startDate − endDate`
- B) The number of days from `startDate` to `endDate`, i.e., `endDate − startDate`
- C) The absolute (unsigned) number of days between the two dates regardless of order
- D) A `DayTimeIntervalType` column representing the duration

---

### Question 22 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What is the return type of `unix_timestamp()` when called with no arguments?

- A) `TimestampType`
- B) `StringType`
- C) `LongType`
- D) `DoubleType`

---

### Question 23 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What does `initcap("hello world")` return?

- A) `"HELLO WORLD"`
- B) `"Hello world"`
- C) `"Hello World"`
- D) `"hello World"`

---

### Question 24 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Given `nullif(expr1, expr2)`, what does the function return?

- A) `expr1` if `expr1` is not null, otherwise returns `expr2`
- B) `NULL` if `expr1` equals `expr2`; otherwise returns `expr1`
- C) `NULL` if either `expr1` or `expr2` is null
- D) `expr2` if `expr1` is null; otherwise returns `expr1`

---

### Question 25 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Which statement correctly distinguishes `trunc(date, fmt)` from `date_trunc(fmt, timestamp)`?

- A) `trunc()` accepts a `DateType` column and returns a `DateType` truncated to the specified unit (e.g., `'month'`, `'year'`); `date_trunc()` accepts a `TimestampType` column and returns a `TimestampType` truncated to the specified unit
- B) `trunc()` and `date_trunc()` are interchangeable aliases that accept the same argument types and return the same type
- C) `trunc()` is for numeric rounding only; `date_trunc()` is the sole function for truncating date and timestamp values
- D) `date_trunc()` always returns midnight UTC regardless of the input timezone; `trunc()` preserves the original timezone offset

---

### Question 26 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What does `locate("world", "hello world", 1)` return?

- A) `0` — `locate` uses 0-based indexing and `"world"` starts at position 6
- B) `7` — `locate` uses 1-based indexing and returns the starting position of the first occurrence of the substring
- C) `6` — `locate` uses 0-based indexing and returns the index of the first character of the match
- D) `-1` — `locate` returns `-1` when the substring is not found; any other value means it was found at that 0-based position

---

### Question 27 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Which function returns the day of the week as an integer for a given date, where `1` represents Sunday?

- A) `dayofmonth(date)`
- B) `dayofyear(date)`
- C) `dayofweek(date)`
- D) `weekday(date)`

---

### Question 28 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What does `last_day('2026-04-10')` return?

- A) `2026-04-01`
- B) `2026-04-28`
- C) `2026-04-30`
- D) `2026-05-01`

---

### Question 29 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Which statement correctly describes the difference between `percentile(col, 0.5)` and `percentile_approx(col, 0.5)`?

- A) `percentile` computes the exact median using a full sort of the column data; `percentile_approx` uses a Greenwald–Khanna sketch to compute an approximate median with bounded error, and is more scalable for large datasets
- B) `percentile` is an alias for `percentile_approx`; both compute the same approximate value
- C) `percentile` works on `DoubleType` only; `percentile_approx` works on any numeric type
- D) `percentile_approx` always returns an exact result when `accuracy=1000000`; `percentile` may be inexact for non-integer values

---

### Question 30 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What does `count_if(condition)` compute when used as an aggregate function?

- A) The total number of rows in the group, ignoring the condition entirely
- B) The number of rows in the group where the boolean `condition` evaluates to `true`
- C) The number of rows where the `condition` is non-null (regardless of its boolean value)
- D) A count of distinct values where the condition is `true`

---

### Question 31 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Which aggregate function, introduced in Spark 3.3, returns the value of a second column from the row that contains the maximum value of a first column within the group?

- A) `first(col ORDER BY other_col DESC)`
- B) `max_by(value_col, ordering_col)`
- C) `argmax(ordering_col, value_col)`
- D) `collect_list(value_col ORDER BY ordering_col DESC)[0]`

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Spark SQL

**Question**:
A developer writes a Spark SQL query using a Common Table Expression (CTE):
```sql
WITH ranked AS (
  SELECT *, rank() OVER (PARTITION BY dept ORDER BY salary DESC) AS rnk
  FROM employees
)
SELECT * FROM ranked WHERE rnk = 1
```
Which of the following statements about CTEs in Spark SQL are correct? *(Select all that apply.)*

- A) The `WITH` clause defines a named subquery (`ranked`) that is referenced in the main `SELECT`
- B) Spark SQL supports multiple CTEs in a single `WITH` clause separated by commas
- C) CTEs in Spark SQL are always materialised to disk before the outer query executes
- D) Recursive CTEs (e.g., `WITH RECURSIVE`) are not supported in Spark SQL as of Spark 3.5

---

### Question 33 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Given the following SQL:
```sql
SELECT stack(2, 'a', 1, 'b', 2) AS (letter, num)
```
What does this query return?

- A) A single row containing the array `['a', 'b']` and the array `[1, 2]`
- B) Two rows: `('a', 1)` and `('b', 2)` — `stack(n, v1, v2, ...)` transposes every `n` values into a new row
- C) A struct column with fields `letter='a'` and `num=1` only
- D) An error — `stack()` is not supported in the SELECT clause without a LATERAL VIEW

---

### Question 34 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What does `get_json_object('{"user":{"name":"Alice"}}', '$.user.name')` return?

- A) A `StructType` column containing the parsed `user.name` field
- B) The string `"Alice"` — `get_json_object` extracts a scalar value from a JSON string using a JSONPath expression and returns `StringType`
- C) The string `'{"name":"Alice"}'` — it returns the entire matched sub-object as a JSON string
- D) `NULL` — nested JSONPath expressions like `$.user.name` are not supported by `get_json_object`

---

### Question 35 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What is the effect of calling `sort_array(array_col, False)`?

- A) Removes duplicate elements and returns the remaining elements in ascending order
- B) Sorts the elements of the array in descending order; `False` means ascending is disabled
- C) Sorts the elements in ascending order; `False` has no effect because ascending is the default
- D) Returns the array unchanged — the second argument controls nulls-first behaviour, not sort direction

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Which syntax correctly defines a named window specification using the SQL `WINDOW` clause and then references it in a window function?

- A)
```sql
SELECT name, rank() OVER w AS rnk
FROM employees
WINDOW w AS (PARTITION BY dept ORDER BY salary DESC)
```
- B)
```sql
SELECT name, rank() OVER (NAMED w PARTITION BY dept ORDER BY salary DESC) AS rnk
FROM employees
```
- C)
```sql
DEFINE WINDOW w PARTITION BY dept ORDER BY salary DESC;
SELECT name, rank() OVER w AS rnk FROM employees
```
- D)
```sql
SELECT name, rank() OVER (USE w) AS rnk
FROM employees
WITH WINDOW w AS (PARTITION BY dept ORDER BY salary DESC)
```

---

### Question 37 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What does `array_repeat('x', 3)` return?

- A) `['x']` — it creates an array with only one element and ignores the count
- B) `['x', 'x', 'x']` — it returns an array containing the element repeated the specified number of times
- C) A string `'xxx'` — `array_repeat` repeats and concatenates the element as a string
- D) An error if the second argument is greater than 2

---

### Question 38 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
What does `map_entries(map('a', 1, 'b', 2))` return?

- A) Two separate columns: one containing the keys `['a', 'b']` and one containing the values `[1, 2]`
- B) An `ArrayType` of `StructType(key, value)` entries: `[{a, 1}, {b, 2}]`
- C) An `ArrayType(StringType)` containing the keys only: `['a', 'b']`
- D) A `MapType` with keys and values swapped: `map(1, 'a', 2, 'b')`

---

### Question 39 — Spark SQL

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Spark SQL

**Question**:
A query uses `ROLLUP(dept, team)` and references `GROUPING_ID(dept, team)`. Which of the following statements about `GROUPING_ID` are correct? *(Select all that apply.)*

- A) `GROUPING_ID` returns a bitmask integer where each bit indicates whether the corresponding column is aggregated (1) or grouped (0) in that row
- B) A `GROUPING_ID` value of `0` means all listed columns are present in the grouping key (the finest-grained row)
- C) A `GROUPING_ID` value of `3` (binary `11`) means both `dept` and `team` are rolled up, i.e., this is the grand-total row
- D) `GROUPING_ID` accepts column names in the same order as listed in the `GROUP BY` / `ROLLUP` clause, and changing the order changes the bitmask values

---

### Question 40 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark SQL

**Question**:
Which statement correctly describes the behaviour of the `nth_value(col, n)` window function?

- A) Returns the value of `col` from the row that has the `n`-th largest value of the ORDER BY expression within the window partition
- B) Returns the value of `col` from the `n`-th row of the window frame, using the ORDER BY expression to determine row ordering; returns `NULL` if fewer than `n` rows exist in the frame
- C) Returns the `n`-th distinct value of `col` within the partition, skipping duplicates
- D) Is equivalent to `lag(col, n-1)` and can always be rewritten using `lag` without any behavioural difference

---

### Question 41 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `df.columns` return in PySpark?

- A) A `StructType` object listing all column names and their data types
- B) A Python list of column name strings
- C) A list of `(name, dtype_string)` tuples — one per column
- D) A `DataFrame` with a single column containing the column names of `df`

---

### Question 42 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
In PySpark, `df.transform(func)` applies a user-defined function `func` to the entire DataFrame. Which statement best describes this method?

- A) `df.transform(func)` is equivalent to `F.transform(col, func)` — both apply `func` element-wise to an array column
- B) `df.transform(func)` calls `func(df)` and returns the resulting DataFrame; it is used to chain DataFrame-level transformations cleanly in a pipeline
- C) `df.transform(func)` applies `func` to each row and returns an RDD, similar to `df.rdd.map(func)`
- D) `df.transform(func)` is only available on Datasets in Scala; PySpark does not support this method

---

### Question 43 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What is the key advantage of `df.toLocalIterator()` over `df.collect()`?

- A) `toLocalIterator()` is always faster than `collect()` because it uses parallel transfers from all executors simultaneously
- B) `toLocalIterator()` streams partitions one at a time to the driver, limiting peak driver memory usage to one partition at a time; `collect()` materialises the entire result in driver memory at once
- C) `toLocalIterator()` avoids triggering a Spark job; `collect()` always triggers at least two jobs
- D) `toLocalIterator()` returns a Pandas DataFrame directly; `collect()` returns a list of Row objects

---

### Question 44 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
Given:
```python
from pyspark.sql import functions as F
result = df.withColumn("coords", F.struct(F.col("lat"), F.col("lon")))
```
What is the schema of the `coords` column?

- A) `ArrayType(DoubleType)` — `F.struct` creates an array when given column arguments
- B) `StructType([StructField("lat", ...), StructField("lon", ...)])` — `F.struct` builds a struct column whose fields are named after the input columns
- C) `MapType(StringType, DoubleType)` — `F.struct` with two columns creates a key-value map
- D) `StringType` — `F.struct` serialises the column values to a JSON string

---

### Question 45 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
Introduced in Spark 3.4, what does `df.withColumnsRenamed({"old1": "new1", "old2": "new2"})` do?

- A) Adds two new columns named `new1` and `new2`, while keeping the original `old1` and `old2` columns unchanged
- B) Renames `old1` to `new1` and `old2` to `new2` in a single call, equivalent to chaining two `withColumnRenamed` calls
- C) Swaps the column names — `old1` gets the name `new2` and `old2` gets the name `new1`
- D) This method does not exist; the correct method is `df.renameColumns(mapping)`

---

### Question 46 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: DataFrame/DataSet API

**Question**:
Which of the following statements correctly distinguish `df.foreach(func)` from `df.foreachPartition(func)`? *(Select all that apply.)*

- A) `df.foreach(func)` calls `func` once per row, receiving a single `Row` object each time
- B) `df.foreachPartition(func)` calls `func` once per partition, receiving an iterator of `Row` objects; this allows expensive setup (e.g., opening a database connection) to be done once per partition rather than once per row
- C) Both `foreach` and `foreachPartition` return a new DataFrame with the transformed results
- D) `df.foreachPartition(func)` is generally more efficient than `df.foreach(func)` for operations involving external connections because connection overhead is amortised over all rows in the partition

---

### Question 47 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `df.mapInPandas(func, schema)` do?

- A) Converts the entire DataFrame to a single Pandas DataFrame on the driver, applies `func`, and distributes the result back to executors
- B) Applies `func` to each partition of the DataFrame, where each partition is passed as a Pandas DataFrame iterator; the function must return an iterator of Pandas DataFrames; results are combined into a Spark DataFrame with the specified `schema`
- C) Applies `func` row-by-row to each row, receiving a Pandas Series of values for that row
- D) Applies `func` after grouping — it is equivalent to `groupBy().applyInPandas(func, schema)` with an implicit group key of all columns

---

### Question 48 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What is the key behavioural difference between `df.write.saveAsTable("my_table")` and `df.write.insertInto("my_table")`?

- A) `saveAsTable` creates or replaces the table using the DataFrame schema; `insertInto` appends or overwrites an existing table by column position (not column name), and the target table must already exist
- B) `saveAsTable` always creates an external (unmanaged) table; `insertInto` always creates a managed table
- C) `insertInto` respects the DataFrame column names when inserting; `saveAsTable` inserts by position
- D) Both methods behave identically — they are interchangeable aliases for writing to a catalog table

---

### Question 49 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `F.levenshtein(col1, col2)` return?

- A) A boolean column that is `true` when the two strings are within one edit of each other
- B) An `IntegerType` column containing the Levenshtein edit distance (minimum single-character insertions, deletions, or substitutions) between the two string columns
- C) A `DoubleType` column containing a normalised similarity score between `0.0` and `1.0`
- D) An error — `levenshtein` is a SQL-only function and is not available via `pyspark.sql.functions`

---

### Question 50 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does the `thresh` parameter in `df.na.drop(thresh=2)` control?

- A) Rows are dropped if they contain more than `thresh` null values
- B) Rows are dropped if they contain fewer than `thresh` non-null values (i.e., a row is kept only if at least `thresh` columns are non-null)
- C) Rows are dropped if any column has a null rate exceeding `thresh` percent across the entire DataFrame
- D) Rows are dropped if the total number of nulls in the row is equal to `thresh`

---

### Question 51 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
Introduced in Spark 3.4, what does `df.unpivot(ids, values, variableColumnName, valueColumnName)` do?

- A) Pivots the DataFrame by expanding the values of one column into multiple columns, the inverse of `df.groupBy().pivot()`
- B) Transforms the DataFrame from wide format to long format — for each row, the columns listed in `values` are melted into two columns: one holding the original column name (named `variableColumnName`) and one holding the value (named `valueColumnName`); `ids` columns are kept as-is
- C) Transposes rows and columns — each row becomes a column and each column becomes a row
- D) Drops the listed `values` columns and adds a single JSON-encoded column containing all their values per row

---

### Question 52 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What is the default truncation behaviour of `df.show()`, and how do you display full column values without truncation?

- A) `df.show()` truncates string values to 20 characters by default; pass `truncate=False` to display full values
- B) `df.show()` truncates string values to 50 characters by default; pass `truncate=0` to disable truncation
- C) `df.show()` never truncates; all values are always shown in full
- D) `df.show()` truncates based on terminal width; the `truncate` parameter is not supported in PySpark

---

### Question 53 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `F.raise_error(msg)` do when used as a column expression in Spark 3.1+?

- A) Logs a warning message to the driver log for every row where the column is evaluated, without interrupting execution
- B) Always raises a `RuntimeException` with the given message when the column expression is evaluated — it is typically used inside a `when()` condition to enforce data quality rules
- C) Returns a column containing the error message string for every row, with no side effects
- D) Raises a `SparkException` only when the column is passed to an action such as `show()` or `collect()`; lazy evaluation defers it until then

---

### Question 54 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
When reading from a JDBC source, what does the `fetchsize` option control?

- A) The maximum number of JDBC connections opened in parallel by Spark executors
- B) The number of rows fetched per round-trip from the database server; increasing it reduces the number of network round-trips at the cost of higher executor memory usage per task
- C) The maximum size in bytes of each individual JDBC result set row
- D) The timeout in seconds before a JDBC query is cancelled if the database server has not returned any rows

---

### Question 55 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What performance trade-off does `spark.read.csv(...).option("inferSchema", "true")` introduce compared to reading without `inferSchema`?

- A) There is no performance difference; schema inference is done lazily and incurs no extra computation
- B) Spark makes two passes over the data — the first pass reads all values as strings to infer types; the second pass reads the data with the inferred schema. This doubles the I/O cost compared to providing an explicit schema
- C) Schema inference only affects the driver; executor performance is unchanged
- D) `inferSchema` disables predicate pushdown for the entire DataFrame, resulting in a full table scan regardless of applied filters

---

### Question 56 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
Which read option configures a custom field delimiter when reading a pipe-delimited CSV file?

- A) `.option("delimiter", "|")`  only — `sep` is not a valid alias
- B) `.option("sep", "|")` only — `delimiter` is not a valid alias
- C) Either `.option("sep", "|")` or `.option("delimiter", "|")` — both are accepted aliases for the same option
- D) `.option("fieldSeparator", "|")` — the canonical option name for CSV field delimiter

---

### Question 57 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: DataFrame/DataSet API

**Question**:
Which of the following correctly distinguish `F.hash(*cols)` from `F.xxhash64(*cols)` in PySpark? *(Select all that apply.)*

- A) `F.hash()` uses MurmurHash3 and returns an `IntegerType` (32-bit) column
- B) `F.xxhash64()` uses the xxHash64 algorithm and returns a `LongType` (64-bit) column
- C) `F.xxhash64()` is a cryptographic hash function suitable for security-sensitive use cases
- D) Both functions are non-cryptographic and intended for partitioning or bucketing use cases, not for password hashing

---

### Question 58 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `F.conv("FF", 16, 10)` return, and what is the return type?

- A) The integer `255` as an `IntegerType` column
- B) The string `"255"` as a `StringType` column — `F.conv` always returns `StringType`
- C) The string `"11111111"` — `F.conv("FF", 16, 10)` converts hexadecimal to binary, not decimal
- D) `NULL` — `F.conv` only supports base conversions involving base 2 (binary)

---

### Question 59 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `F.unhex(col)` return?

- A) The decimal integer representation of a hexadecimal string column, returned as `LongType`
- B) The binary (`BinaryType`) representation obtained by interpreting each pair of hexadecimal digits in the input string as a byte — it is the inverse of `F.hex(col)`
- C) The ASCII character corresponding to the hexadecimal number, returned as `StringType`
- D) The input string with all hex escape sequences (`\xNN`) decoded back to their original characters

---

### Question 60 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Scenario**:
```python
df.write.format("delta") \
    .mode("overwrite") \
    .save("/delta/my_table")
```
The DataFrame has a new column not present in the existing Delta table's schema.

**Question**:
What happens by default, and what option is required to allow the schema change?

- A) Delta Lake automatically merges the new column into the existing schema without any additional options
- B) Spark raises an `AnalysisException` because the schema does not match; add `.option("overwriteSchema", "true")` to allow the schema to be replaced with the new DataFrame's schema
- C) Spark silently drops the new column to match the existing schema and writes the data without error
- D) The write succeeds but the new column values are stored as `NULL` to maintain backward schema compatibility

---

### Question 61 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What is the difference between `df.schema.simpleString()` and `df.schema.toDDL()`?

- A) `simpleString()` returns a compact Spark-internal type representation like `struct<id:int,name:string>`; `toDDL()` returns a SQL DDL-compatible type string like `` `id` INT,`name` STRING `` suitable for use in `CREATE TABLE` statements
- B) `simpleString()` and `toDDL()` return identical strings and are interchangeable
- C) `simpleString()` includes column nullability information; `toDDL()` omits nullability
- D) `toDDL()` returns a JSON string; `simpleString()` returns a Python `dict`

---

### Question 62 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `F.to_csv(struct_col)` return?

- A) Writes the struct column's values to a CSV file on the driver; returns `NULL` as a column value
- B) A `StringType` column where each struct value is serialised to a CSV-formatted string, with fields appearing in the order they are defined in the struct schema
- C) An `ArrayType(StringType)` column where each element of the array corresponds to one field of the struct
- D) A `BinaryType` column containing the CSV-encoded bytes of the struct

---

### Question 63 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does the `subset` parameter in `df.na.fill(0, subset=["age", "score"])` control?

- A) Null values in all numeric columns are replaced with `0`; `subset` is ignored
- B) Only null values in the columns `age` and `score` are replaced with `0`; all other columns are left unchanged
- C) The `subset` parameter specifies which rows to fill based on a row filter expression
- D) The `subset` parameter limits the scan to a random sample of the DataFrame before filling nulls

---

### Question 64 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What is the key difference in null handling between `F.sort_array(col, asc=True)` and `F.array_sort(col)`?

- A) `sort_array` places nulls at the beginning in ascending order; `array_sort` places nulls at the end — both sort non-null elements in ascending order
- B) `sort_array` raises an error if the array contains nulls; `array_sort` silently removes nulls before sorting
- C) `array_sort` is always descending; `sort_array` supports both ascending and descending via the `asc` parameter
- D) There is no difference in null handling — both functions place nulls at the beginning of ascending-sorted arrays

---

### Question 65 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `F.reverse(col)` do when applied to a `StringType` column versus an `ArrayType` column?

- A) It raises an `AnalysisException` on `StringType` columns — `reverse` only works on arrays
- B) On `StringType`, it reverses the characters of the string; on `ArrayType`, it reverses the order of the array elements — both return the same type as the input
- C) It returns the last character for `StringType` and the last element for `ArrayType`
- D) It raises an `AnalysisException` on `ArrayType` columns — `reverse` only works on strings

---

### Question 66 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `F.format_string("%s has %d items", F.col("name"), F.col("count"))` return?

- A) A `StructType` column containing the format template and the substituted values as separate fields
- B) A `StringType` column where `%s` and `%d` are replaced by the values of `name` and `count` respectively, similar to `sprintf` or `printf` formatting
- C) An error — `format_string` does not support column references as arguments; only literal values are accepted
- D) The literal string `"%s has %d items"` unchanged — format substitution requires calling `.format()` on the result

---

### Question 67 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
When writing a CSV file using `df.write.csv(path)`, what do the `nullValue` and `emptyValue` options control, and how do they differ?

- A) `nullValue` and `emptyValue` are aliases — both control how `NULL` values are written; the string `""` is always written for empty strings
- B) `nullValue` sets the string written for `NULL` column values (default `""`); `emptyValue` sets the string written for empty (zero-length) string values (default `""`). They are distinct: a `NULL` and an empty string are different values
- C) `nullValue` controls the CSV header row for null-type columns; `emptyValue` controls how empty arrays are serialised
- D) `emptyValue` applies only to numeric columns; `nullValue` applies only to string columns

---

### Question 68 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
What does `F.soundex(col)` return, and what is a typical use case?

- A) A `DoubleType` score between 0 and 1 representing string similarity; used for fuzzy deduplication
- B) A `StringType` column containing the Soundex phonetic code for each string value; used to match names that sound similar despite different spellings (e.g., "Smith" and "Smyth")
- C) A `BinaryType` column containing the SHA-256 hash of the string; used for secure string comparison
- D) A `BooleanType` column that is `true` if two strings have the same phonetic pronunciation; `false` otherwise

---

### Question 69 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: DataFrame/DataSet API

**Question**:
Which of the following statements about `F.assert_true(condition, errMsg)` (Spark 3.1+) are correct? *(Select all that apply.)*

- A) It returns a `NULL` column value for every row where `condition` evaluates to `true`
- B) It raises a `RuntimeException` with the given `errMsg` for any row where `condition` evaluates to `false` or `NULL`
- C) It can be used inside `df.select()` to perform inline data quality validation during a Spark action
- D) It is equivalent to using a UDF that raises a Python exception — both have the same performance characteristics

---

### Question 70 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrame/DataSet API

**Question**:
Introduced in Spark 3.4, what does `df.offset(n)` do?

- A) Returns a new DataFrame containing only every `n`-th row, sampled deterministically
- B) Returns a new DataFrame that skips the first `n` rows of `df`; equivalent to the SQL `OFFSET n` clause
- C) Returns the `n`-th partition of the DataFrame as a new DataFrame
- D) Shifts all row index values in the DataFrame forward by `n` positions

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
What does enabling `spark.sql.execution.arrow.pyspark.enabled = true` improve, and what is required on the Python environment?

- A) It enables GPU-accelerated SQL execution using NVIDIA RAPIDS; CUDA toolkit must be installed
- B) It uses Apache Arrow as the in-memory columnar format for data transfer between JVM and Python processes (e.g., in `toPandas()`, `createDataFrame(pandas_df)`, and Pandas UDFs), significantly reducing serialisation overhead; PyArrow must be installed in the Python environment
- C) It enables vectorised I/O when reading Parquet files, bypassing the JVM entirely; no additional Python packages are required
- D) It replaces Java serialization with Arrow IPC format for shuffle data between executors, requiring PyArrow on every executor

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
In the Catalyst optimizer, what is **column pruning** and how does it differ from **predicate pushdown**?

- A) Column pruning and predicate pushdown are the same optimisation — both push filtering logic to the data source
- B) Column pruning eliminates columns from the query plan that are not referenced by any downstream operation, reducing I/O and memory usage; predicate pushdown moves row-filtering conditions closer to the data source to reduce the number of rows read — they are complementary but distinct optimisations
- C) Column pruning is a physical plan optimisation; predicate pushdown is a logical plan optimisation — they cannot both apply to the same query
- D) Column pruning only applies to ORC files; predicate pushdown only applies to Parquet files

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
What does `spark.sql.broadcastTimeout` (default `300` seconds) govern, and when would you increase it?

- A) It controls how long the driver waits for each executor to acknowledge receipt of a broadcast variable; increase it when executors are slow to start or the broadcast payload is very large and takes time to fetch over a slow network
- B) It controls the maximum duration of any SQL query that uses a broadcast join; queries exceeding this limit are automatically cancelled
- C) It controls how long broadcast variable data is kept in executor memory before being automatically evicted
- D) It is the timeout applied to the `ANALYZE TABLE` command when computing broadcast-related table statistics

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
An executor is configured with `spark.executor.cores=8`. A task requires two CPU cores (e.g., it uses a native multi-threaded library). What configuration change enables this, and what is the side effect?

- A) Set `spark.task.cpus=2`; the side effect is that only 4 tasks can run concurrently per executor instead of 8, halving the task-level parallelism per executor
- B) Set `spark.executor.coresPerTask=2`; no side effect — task count per executor remains at 8
- C) Set `spark.task.resource.cpu.amount=2`; this has no impact on parallelism because Spark schedules all tasks regardless of CPU cores
- D) There is no Spark configuration to request multiple cores per task; use `repartition()` to spread work across more single-core tasks

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
What is the significance of `spark.sql.optimizer.maxIterations` (default `100`), and what happens if the optimizer reaches this limit?

- A) The query is cancelled and a `PlanNotConvergedException` is thrown; the only remedy is to rewrite the query or increase the limit
- B) Catalyst applies optimisation rules iteratively in fixed-point batches until the plan stabilises or the iteration limit is reached; if the limit is reached before convergence, Spark logs a warning and continues with the best plan produced so far — increasing the limit may improve plan quality for very complex queries
- C) Catalyst stops optimising after `maxIterations` rule applications total across all batches, regardless of whether the plan has stabilised
- D) Reaching the iteration limit causes Catalyst to fall back to a non-optimised physical plan that performs all operations as broad hash joins

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
What does setting `spark.sql.files.ignoreMissingFiles=true` do, and when would you use it?

- A) It silently skips partition directories that are empty, treating them as if they contain zero rows
- B) It allows a query to continue without error when one or more input files are missing or deleted between query planning and execution — useful in data lake environments where files may be compacted or deleted by concurrent processes
- C) It disables schema validation so that files with mismatched schemas are silently skipped rather than causing an `AnalysisException`
- D) It suppresses `FileNotFoundException` errors only during checkpoint recovery, not during normal query execution

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
What does `spark.shuffle.file.buffer` (default `32k`) control, and how does increasing it help performance?

- A) It sets the maximum total size of all shuffle write files generated by a single executor; larger values allow more data to be buffered before a spill is triggered
- B) It sets the in-memory write buffer size for each shuffle output file written by a mapper task; increasing it reduces the number of system calls and disk seeks by allowing larger sequential write bursts before flushing to disk
- C) It controls the size of the network receive buffer used by reducers fetching shuffle blocks from remote executors
- D) It sets the maximum number of shuffle partitions that can be buffered in memory before Spark falls back to an on-disk sort-merge approach

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Troubleshooting & Tuning

**Question**:
Which of the following statements about off-heap memory in Spark are correct? *(Select all that apply.)*

- A) Off-heap memory is enabled by setting `spark.memory.offHeap.enabled=true` and `spark.memory.offHeap.size` to a positive byte value
- B) When enabled, Spark can allocate memory for storage (caching) and execution (shuffle, sort) outside the JVM heap, reducing GC pressure
- C) Off-heap memory is counted as part of the executor's JVM heap and contributes to `spark.memory.fraction`
- D) Off-heap memory is not subject to JVM garbage collection because it is allocated outside the JVM heap

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
What does `spark.sql.execution.sortBeforeRepartition` (default `true`) control?

- A) When `true`, Spark sorts each partition by the repartition key before writing shuffle files, which improves data locality in downstream stages that also sort by the same key
- B) When `true`, Spark sorts records within each partition before the `repartition()` call, ensuring the output partitions are sorted — this is required for downstream window functions
- C) When `true`, Spark performs a global sort of the entire DataFrame as a pre-step before any `repartition()` call
- D) When `false`, Spark skips the shuffle phase entirely and uses a local sort to approximate repartitioning

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Troubleshooting & Tuning

**Question**:
A Spark job produces 2,000 shuffle partitions but most are very small (a few KB each), causing excessive task scheduling overhead. AQE is enabled. Which AQE feature automatically addresses this, and which configuration sets the target merged partition size?

- A) AQE skew join handling; configured by `spark.sql.adaptive.skewJoin.skewedPartitionFactor`
- B) AQE post-shuffle partition coalescing; the target size is configured by `spark.sql.adaptive.advisoryPartitionSizeInBytes` (and the result is lower-bounded by `spark.sql.adaptive.coalescePartitions.minPartitionNum`)
- C) AQE dynamic broadcast join conversion; configured by `spark.sql.adaptive.autoBroadcastJoinThreshold`
- D) AQE local shuffle reader; the merged partition size is configured by `spark.sql.adaptive.localShuffleReader.enabled`

---

### Question 81 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Structured Streaming

**Question**:
In Spark 3.3+, how does `trigger(availableNow=True)` differ from `trigger(once=True)`?

- A) They are identical — `availableNow=True` is simply the new API name for `trigger(once=True)` introduced to deprecate the old name
- B) `trigger(once=True)` processes all available data in a single micro-batch and stops; `trigger(availableNow=True)` processes all available data at the time of the call but uses multiple micro-batches (respecting `maxFilesPerTrigger` or `maxBytesPerTrigger` limits per batch), then stops — giving better parallelism and fault tolerance for large backfills
- C) `trigger(availableNow=True)` runs continuously and never stops; `trigger(once=True)` stops after one micro-batch
- D) `trigger(availableNow=True)` applies only to Kafka sources; `trigger(once=True)` applies only to file-based sources

---

### Question 82 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Structured Streaming

**Question**:
What does `query.inputRowsPerSecond` represent in a streaming `StreamingQueryProgress` report?

- A) The number of rows that were fully processed and written to the sink per second in the last micro-batch
- B) The rate at which rows arrived at the source and were ingested by the streaming query, expressed as rows per second for the last micro-batch interval
- C) The average throughput of the streaming query across all micro-batches since the query started
- D) The maximum throughput the streaming query is theoretically capable of based on available executor resources

---

### Question 83 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Structured Streaming

**Question**:
A developer writes a streaming query reading from a file source without specifying a schema:
```python
df = spark.readStream.format("json").load("/data/events/")
```
What happens?

- A) Spark infers the schema by scanning all files currently in the directory before the stream starts, and uses that schema for all future batches
- B) Spark raises an `AnalysisException` — streaming file sources require an explicit schema to be provided; schema inference is not supported for streaming reads
- C) Spark uses a default schema of a single `StringType` column named `value` for all JSON streaming reads
- D) Spark infers the schema from the first micro-batch and dynamically updates it as new fields appear in subsequent files

---

### Question 84 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Structured Streaming

**Question**:
Which of the following statements about the streaming `console` sink are correct? *(Select all that apply.)*

- A) The console sink writes each micro-batch's output to standard output (stdout) on the driver
- B) The console sink is intended for development and testing only — it is not suitable for production use
- C) The console sink supports all three output modes: `append`, `update`, and `complete`
- D) The console sink persists data to a durable store and supports checkpoint recovery across restarts

---

### Question 85 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Structured Streaming

**Question**:
A streaming aggregation query uses `groupBy(window("event_time", "10 minutes")).count()` with a watermark of `5 minutes`. An event with `event_time = 10:03` arrives at processing time `10:20`. What happens to this event?

- A) It is included in the `[10:00, 10:10)` window because the window boundary has not yet been finalised
- B) It is dropped as late data — the watermark at processing time `10:20` is approximately `10:20 − 5 min = 10:15`, which is past the end of the `[10:00, 10:10)` window, so the event is discarded
- C) It is included in the next open window `[10:10, 10:20)` because Spark automatically re-assigns late events to the nearest open window
- D) The event causes the query to throw a `WatermarkViolationException` and halt

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Structured Streaming

**Question**:
What type of streaming window does `session_window("event_time", "30 minutes")` define in Spark Structured Streaming?

- A) A fixed tumbling window where each session is exactly 30 minutes long regardless of event timing
- B) A dynamic window that groups events into sessions based on activity gaps — a new session starts when no event is observed within the 30-minute gap timeout after the last event; introduced in Spark 3.2
- C) A sliding window that advances by 30 minutes each micro-batch and covers the last 60 minutes of data
- D) A global window spanning the entire stream lifetime, with a 30-minute grace period for late arrivals

---

### Question 87 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Structured Streaming

**Question**:
When reading from Kafka in Structured Streaming, what is the schema of the resulting DataFrame?

- A) The Kafka DataFrame has a flexible schema that Spark infers from the message content
- B) The Kafka DataFrame always has a fixed schema including columns: `key` (BinaryType), `value` (BinaryType), `topic` (StringType), `partition` (IntegerType), `offset` (LongType), `timestamp` (TimestampType), and `timestampType` (IntegerType)
- C) The Kafka DataFrame has only two columns: `key` (StringType) and `value` (StringType)
- D) The Kafka DataFrame schema depends on the `kafka.value.deserializer` class specified in the read options

---

### Question 88 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Structured Streaming

**Question**:
What is the purpose of the `kafka.group.id` option when reading from Kafka in Structured Streaming, and what is the default behaviour when it is not set?

- A) `kafka.group.id` must always be explicitly set — without it, Spark raises an error because Kafka requires a consumer group ID
- B) When `kafka.group.id` is set, Spark uses a fixed consumer group with that ID, which may cause offset management conflicts with Spark's internal checkpoint offsets; when not set, Spark generates a unique group ID per query to manage offsets internally
- C) `kafka.group.id` controls the group used for partition assignment negotiation; when not set, Spark assigns partitions using round-robin without a group
- D) `kafka.group.id` only affects the rate of reads; it has no impact on offset management or fault tolerance

---

### Question 89 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Structured Streaming

**Question**:
What does the `maxOffsetsPerTrigger` option control when reading from Kafka in Structured Streaming?

- A) The maximum total number of Kafka messages the streaming query will ever read across its lifetime
- B) The maximum number of Kafka offsets (messages) to process per micro-batch trigger, limiting throughput to prevent a single trigger from processing an unbounded backlog
- C) The maximum number of Kafka partitions that a single executor can read in one trigger
- D) The maximum time in milliseconds the query waits for new Kafka offsets before proceeding with an empty micro-batch

---

### Question 90 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Structured Streaming

**Question**:
Which of the following statements about `flatMapGroupsWithState` in PySpark Structured Streaming are correct? *(Select all that apply.)*

- A) `flatMapGroupsWithState` supports arbitrary stateful processing where each call can emit zero or more output rows per group, unlike `mapGroupsWithState` which must emit exactly one row per group
- B) `flatMapGroupsWithState` supports a timeout mechanism (`GroupStateTimeout.ProcessingTimeTimeout` or `EventTimeTimeout`) that triggers the function for groups that have not received new data within the timeout period
- C) `flatMapGroupsWithState` requires the output mode to be `update` or `append` — `complete` is not supported
- D) `flatMapGroupsWithState` state data is persisted in the streaming checkpoint and survives query restarts

---

### Question 91 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect

**Question**:
When using Spark Connect, at what point are `AnalysisException` errors (such as referencing a non-existent column) surfaced to the client?

- A) Errors are detected immediately when the column reference is created using `F.col("nonexistent")` — the client validates column names against a local schema cache
- B) Errors are surfaced when an action (such as `collect()`, `show()`, or `count()`) is called — the logical plan is sent to the server for analysis at action time, and any analysis errors are returned to the client as exceptions
- C) Errors are surfaced at the time the DataFrame transformation is called (e.g., `df.select(F.col("nonexistent"))`) because the client eagerly validates the plan via a gRPC call
- D) Analysis errors are only logged on the server and never propagated back to the client in Spark Connect

---

### Question 92 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect

**Question**:
How do you connect to a Spark Connect server that requires token-based authentication using the connection string?

- A) Pass the token as a separate argument: `SparkSession.builder.remote("sc://host:15002").option("token", "my_token").getOrCreate()`
- B) Embed the token in the connection URL using semicolon-separated parameters: `SparkSession.builder.remote("sc://host:15002/;token=my_token").getOrCreate()`
- C) Set the environment variable `SPARK_CONNECT_TOKEN=my_token` before starting the Python process — the builder picks it up automatically
- D) Token-based authentication is not supported by open-source Spark Connect; it is only available in Databricks

---

### Question 93 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect

**Question**:
Which of the following is a valid use of the Spark Connect Python client that would fail in classic PySpark (without Spark Connect)?

- A) Running `spark.sql("SELECT 1")` from a Python process on a machine that does not have the JVM installed
- B) Importing `from pyspark.sql import SparkSession` on a machine without Java installed, then connecting to a remote Spark Connect server to execute queries
- C) Creating a Spark UDF using `@udf(returnType=LongType())`
- D) Reading a Parquet file with `spark.read.parquet("s3://bucket/data")`

---

### Question 94 — Spark Connect

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Spark Connect

**Question**:
Which of the following statements about how Spark Connect handles user-defined functions (UDFs) are correct? *(Select all that apply.)*

- A) UDFs registered via `spark.udf.register()` on a Spark Connect client are serialised and sent to the server for execution on the executors
- B) UDFs that reference external Python libraries must have those libraries available on the executor environment (e.g., via `--py-files` or a pre-installed cluster library), not just on the client machine
- C) Because Spark Connect decouples the client from the server, Python UDFs cannot be used at all — only SQL built-in functions are supported
- D) Arrow-optimised Pandas UDFs (`@pandas_udf`) are supported via Spark Connect because they use Apache Arrow for data serialisation between the server and executors

---

### Question 95 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect

**Question**:
What happens to other active Spark Connect client sessions if the Spark Connect **server** process crashes and is restarted?

- A) All in-progress queries are automatically replayed from their last checkpoint upon reconnection — clients experience no data loss
- B) In-progress queries are lost; after the server restarts, clients must re-establish a new session and re-submit their queries. The server crash does not crash the client process itself
- C) All connected client processes crash immediately when the server crashes, because the gRPC connection is synchronous and blocking
- D) The server state (including all DataFrames and temp views registered by clients) is preserved across restarts because it is serialised to a persistent state store

---

### Question 96 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Pandas API on Spark

**Question**:
How do you persist (cache) a pandas-on-Spark DataFrame in Spark's memory, and what does it return?

- A) Call `psdf.cache()` directly — it returns the cached pandas-on-Spark DataFrame, equivalent to `sdf.cache()` on the underlying Spark DataFrame
- B) Call `psdf.spark.cache()` — this caches the underlying Spark DataFrame and returns a new pandas-on-Spark DataFrame backed by the cached data
- C) Convert to Spark first with `psdf.to_spark().cache()` — there is no `.cache()` or `.spark.cache()` method on pandas-on-Spark DataFrames
- D) Call `ps.cache(psdf)` — caching must go through the module-level function, not as a method on the DataFrame

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Pandas API on Spark

**Question**:
What does `psdf.spark.explain()` display?

- A) The pandas-on-Spark execution plan expressed as a sequence of pandas operations
- B) The Spark physical execution plan for the underlying Spark DataFrame — identical to calling `psdf.to_spark().explain()`
- C) A summary of memory usage per partition for the cached pandas-on-Spark DataFrame
- D) The SQL query that would be generated if the DataFrame were written using `ps.sql()`

---

### Question 98 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Pandas API on Spark

**Question**:
What is the default value of `ps.options.compute.default_index_type`, and what are the trade-offs of the available index types?

- A) Default is `"sequence"` — it assigns globally ordered integers using a global sort, which is slow but produces a strictly increasing index; `"distributed"` assigns integers per partition without sorting, which is faster but produces non-globally-ordered values; `"distributed-sequence"` is a faster approximation of sequential ordering
- B) Default is `"distributed-sequence"` — it assigns monotonically increasing integers per partition without a global sort (fast), but does not guarantee global ordering; `"sequence"` triggers a global sort (slow but strictly ordered); `"distributed"` is the fastest but produces non-contiguous values
- C) Default is `"distributed"` — it assigns random UUIDs per row; `"sequence"` and `"distributed-sequence"` are not supported
- D) The default index type is always `RangeIndex` identical to pandas; no configuration option controls this behaviour

---

### Question 99 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Pandas API on Spark

**Question**:
How do you write a pandas-on-Spark DataFrame to a Delta table at a given path?

- A) `psdf.to_delta("/path/to/delta_table")`
- B) `psdf.to_spark().write.format("delta").save("/path/to/delta_table")`
- C) Both A and B are valid — `psdf.to_delta()` is a convenience method that internally calls the Spark Delta writer
- D) `ps.write_delta(psdf, "/path/to/delta_table")` — writing to Delta requires a module-level function

---

### Question 100 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Pandas API on Spark

**Question**:
Which of the following statements about missing value handling in the Pandas API on Spark (compared to native pandas) are correct? *(Select all that apply.)*

- A) In native pandas, `None` and `float('nan')` are both treated as missing for numeric columns; in pandas-on-Spark, missing values are represented as Spark SQL `NULL` internally, and `NaN` in float columns is treated as a distinct non-null value
- B) `psdf.fillna(0)` behaves the same as `df.fillna(0)` in native pandas — it replaces all `NaN` and `None` values with `0`
- C) `psdf.dropna()` drops rows containing Spark SQL `NULL` values; `NaN` values in float columns are not dropped by `dropna()` unless explicitly handled
- D) `psdf.isna()` and `psdf.isnull()` both return `True` for Spark SQL `NULL` values but return `False` for `NaN` in float columns
