# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 2)

**Iteration**: 2

**Generated**: 2026-04-25

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 81 `one` / 19 `many`

**Topic Distribution**:
- Topic 1 (Apache Spark Architecture & Internals — 20%): Questions 1–20
- Topic 2 (Spark SQL — 20%): Questions 21–40
- Topic 3 (DataFrame API — 30%): Questions 41–70
- Topic 4 (Troubleshooting & Tuning — 10%): Questions 71–80
- Topic 5 (Structured Streaming — 10%): Questions 81–90
- Topic 6 (Spark Connect — 5%): Questions 91–95
- Topic 7 (Pandas API on Spark — 5%): Questions 96–100

---

## Questions

---

### Question 1 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: local mode threading

**Question**:
What does the master URL `local[*]` mean when creating a SparkSession?

- A) Runs Spark on a cluster, using a wildcard to match any available cluster manager
- B) Runs Spark in a single JVM using **all available logical CPU cores** as worker threads
- C) Runs Spark on YARN and allocates all available cluster nodes
- D) Runs Spark in client deploy mode with dynamic resource allocation enabled

---

### Question 2 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark-submit deploy mode flag

**Question**:
When submitting a Spark application via `spark-submit`, which flag controls whether the Driver process runs on the **submitting machine** (client) or **inside the cluster** (cluster)?

- A) `--driver-mode`
- B) `--deploy-mode`
- C) `--driver-location`
- D) `--run-mode`

---

### Question 3 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Standalone cluster manager URI

**Question**:
When using Apache Spark's built-in **Standalone** cluster manager, what is the default port in the master URI (e.g., `spark://hostname:PORT`)?

- A) 8080
- B) 4040
- C) 7077
- D) 18080

---

### Question 4 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: many
**Topic**: cluster deploy mode characteristics

**(Select all that apply)**
Which of the following statements about **cluster deploy mode** are correct?

- A) The Driver runs on one of the worker nodes inside the cluster
- B) The submitting machine can disconnect after `spark-submit` without killing the job
- C) Cluster deploy mode is the default; client mode must be explicitly requested
- D) Databricks notebooks always use cluster deploy mode by default
- E) The Driver's logs are stored on the worker node that runs it, not on the submitting machine

---

### Question 5 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: client deploy mode failure risk

**Scenario**:
A team submits a Spark application to a YARN cluster using **client deploy mode** from a CI server. Halfway through the job, the CI server is forcefully terminated.

**Question**:
What happens to the Spark job?

- A) The job continues — YARN migrates the Driver to a worker node automatically
- B) The job fails — the Driver process runs on the CI server; terminating the CI server kills the Driver and the entire application
- C) The job pauses and automatically resumes when the CI server restarts
- D) The Executors elect a new Driver from among themselves and the job continues

---

### Question 6 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: accumulators

**Question**:
What is a **Spark Accumulator**?

- A) A distributed cache that stores key-value pairs on every Executor for fast lookups
- B) A shared variable that only allows associative and commutative additions from Tasks; only the Driver can read the final accumulated value
- C) A configuration counter that tracks the number of actions triggered in a SparkSession
- D) A read-only broadcast variable that collects data from Executors back to the Driver

---

### Question 7 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: accumulator read inside a Task

**Question**:
A developer reads an accumulator's value **inside a UDF or `foreachPartition()` call** running on Executors. What does the read return?

- A) The globally accumulated value at the time of the read (same as the Driver sees)
- B) An exception — reading accumulators from Executors is not allowed
- C) The accumulator's **initial value** (e.g., `0`) — only the Driver sees the final aggregated value after an action completes; Task-side reads return the initial value
- D) Only the contributions from that Executor's own Tasks

---

### Question 8 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.default.parallelism

**Question**:
What does the configuration property `spark.default.parallelism` control?

- A) The number of shuffle partitions produced after `groupBy()` or `join()` operations on DataFrames
- B) The default number of partitions for **RDD** transformations such as `reduceByKey()` and `join()` when no explicit partition count is specified
- C) The maximum number of Tasks that can run concurrently on a single Executor
- D) The initial number of input partitions when reading a DataFrame from Parquet

---

### Question 9 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: broadcast variable access

**Question**:
A developer creates a broadcast variable with `lookup = spark.sparkContext.broadcast({'A': 1, 'B': 2})`. How is the value accessed **inside a UDF** running on Executors?

- A) `lookup['A']`
- B) `lookup.get('A')`
- C) `lookup.value['A']`
- D) `lookup.data['A']`

---

### Question 10 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: input partition count at CSV read

**Question**:
When Spark reads a large CSV file from HDFS, how does it typically determine the **initial number of input partitions**?

- A) Always defaults to `spark.sql.shuffle.partitions` (200) regardless of file size
- B) One partition per HDFS block, controlled by `spark.sql.files.maxPartitionBytes` (default 128 MB)
- C) One partition per physical CPU core across the cluster
- D) One partition for every 1 GB of file size regardless of block boundaries

---

### Question 11 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: speculative execution

**Question**:
What does Spark's **speculative execution** feature do when enabled via `spark.speculation = true`?

- A) Speculatively runs the next Stage before the current Stage finishes to pipeline work
- B) Launches duplicate copies of slow (straggler) Tasks on other Executors; uses the first result returned and cancels the remaining copies
- C) Pre-computes transformations eagerly to reduce lazy evaluation overhead at action time
- D) Pre-fetches shuffle data to local Executor disk before Tasks request it

---

### Question 12 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: TaskScheduler responsibility

**Question**:
The **TaskScheduler** receives Stages from the DAGScheduler. Which task is the TaskScheduler specifically responsible for?

- A) Converting the user's DataFrame operations into a DAG of Stages
- B) Detecting shuffle boundaries and splitting logical plans into physical Stages
- C) Allocating CPU and memory containers by negotiating with the Cluster Manager
- D) Sending individual Tasks within a Stage to available Executor slots on worker nodes

---

### Question 13 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: executor memoryOverhead

**Question**:
What does `spark.executor.memoryOverhead` configure?

- A) The fraction of Executor JVM heap reserved for in-flight shuffle and join operations
- B) Off-heap memory per Executor for OS overhead, Python worker processes, and native (non-JVM) libraries
- C) Additional heap memory reserved for user-defined data structures inside Python UDFs
- D) The maximum size of cached DataFrames that can reside on the Executor's disk

---

### Question 14 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Dynamic Resource Allocation

**(Select all that apply)**
Which of the following statements about **Dynamic Resource Allocation (DRA)** in Spark are correct?

- A) DRA automatically adds Executors when Tasks are queued and not enough Executors are available
- B) DRA automatically removes idle Executors after a configurable timeout to return resources to the cluster
- C) DRA requires an external shuffle service so that shuffle files remain accessible after Executors are removed
- D) DRA is only available on Kubernetes and cannot be used with YARN or Standalone
- E) DRA is enabled by setting `spark.dynamicAllocation.enabled = true`

---

### Question 15 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cache() default storage level

**Question**:
What is the **default storage level** used by `df.cache()` in PySpark?

- A) `MEMORY_ONLY`
- B) `DISK_ONLY`
- C) `MEMORY_AND_DISK`
- D) `MEMORY_ONLY_SER`

---

### Question 16 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: MEMORY_ONLY_SER vs MEMORY_ONLY

**Question**:
How does `StorageLevel.MEMORY_ONLY_SER` differ from `StorageLevel.MEMORY_ONLY`?

- A) `MEMORY_ONLY_SER` spills partitions to disk when memory is full; `MEMORY_ONLY` drops them
- B) `MEMORY_ONLY_SER` stores data in a serialized (compacted binary) format using less heap space but requiring deserialization on each read; `MEMORY_ONLY` stores deserialized Java/Python objects
- C) `MEMORY_ONLY_SER` replicates data across two Executors; `MEMORY_ONLY` does not
- D) `MEMORY_ONLY_SER` bypasses the JVM heap using off-heap storage; `MEMORY_ONLY` uses the JVM heap

---

### Question 17 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: many
**Topic**: broadcast variable behavior

**(Select all that apply)**
Which of the following statements about Spark **broadcast variables** are correct?

- A) Broadcast variables are serialized and cached on each Executor so Tasks access the data locally without network transfer during execution
- B) Broadcast variables can be mutated by Tasks; changes made on Executors are reflected in the Driver's copy
- C) A broadcast variable is created with `spark.sparkContext.broadcast(value)` and its value is accessed via `.value`
- D) If an Executor fails, the broadcast variable is automatically re-sent to the replacement Executor
- E) Broadcast variables are garbage-collected immediately after the action that uses them completes

---

### Question 18 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Unified Memory Model — execution memory

**Question**:
In Spark's **Unified Memory Model**, which memory region handles in-flight memory for shuffle write buffers, sort buffers, and hash join maps?

- A) Storage Memory — reserved for cached DataFrames and broadcast variables
- B) User Memory — reserved for non-Spark data structures
- C) Execution Memory — the portion of Spark Memory used by in-flight operations
- D) Off-Heap Memory — configured by `spark.memory.offHeap.size`

---

### Question 19 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: stage count analysis

**Scenario**:
```python
df = spark.read.parquet('/data/logs')
counted = df.groupBy('level').count()
sorted_df = counted.orderBy(col('count').desc())
sorted_df.write.parquet('/output/sorted_counts')
```

**Question**:
How many Stages does this query plan produce (approximately)?

- A) 1 Stage — Spark fuses all operations end-to-end
- B) 2 Stages — one for `groupBy`, one for `orderBy`
- C) 3 Stages — Stage 1: scan + partial aggregation; Stage 2: final aggregation + sort exchange; Stage 3: sort merge + write
- D) 4 Stages — one per distinct operation

---

### Question 20 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: many
**Topic**: operations that trigger a new Job

**(Select all that apply)**
Which of the following PySpark operations **trigger a new Spark Job** (i.e., they are actions)?

- A) `df.count()`
- B) `df.filter(col('status') == 'active')`
- C) `df.write.parquet('/output')`
- D) `df.groupBy('dept').agg(F.sum('salary'))`
- E) `df.show(10)`

---

### Question 21 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: createGlobalTempView duplicate

**Question**:
A developer calls `df.createGlobalTempView('events')` but a global temporary view named `events` already exists in the same Spark application. What happens?

- A) The existing view is silently replaced with the new definition
- B) A new version is created with an auto-incremented suffix (e.g., `events_1`)
- C) An `AnalysisException` is raised because the view already exists
- D) Spark automatically falls back to `createOrReplaceGlobalTempView`

---

### Question 22 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: to_json() return type

**Question**:
What does `F.to_json(col('address'))` return when `address` is a struct column?

- A) A Python `dict` object containing the struct's fields and values
- B) A Column of `StringType` containing the JSON string representation of the struct
- C) A Column of `BinaryType` containing the UTF-8 encoded JSON bytes
- D) A Column of `MapType(StringType, StringType)` keyed by field name

---

### Question 23 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: lit() function

**Question**:
What is the purpose of `F.lit(100)` in a PySpark expression?

- A) Limits the DataFrame to 100 rows
- B) Looks up the value 100 in a broadcast variable
- C) Creates a Column object representing the constant scalar value `100`
- D) Sets the output precision of numeric columns to 100 significant digits

---

### Question 24 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: catalog.dropTempView

**Question**:
Which Catalog API call removes an existing **session-scoped** temporary view named `orders`?

- A) `spark.catalog.deleteView('orders')`
- B) `spark.catalog.dropTempView('orders')`
- C) `spark.catalog.removeView('orders')`
- D) `spark.sql("DELETE VIEW orders")`

---

### Question 25 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SQL coalesce() function

**Question**:
In Spark SQL, what does `coalesce(col1, col2, col3)` return?

- A) The sum of all non-null column values across the three arguments
- B) The **first non-null** value from the ordered list of arguments
- C) A combined DataFrame built from three source columns
- D) The number of partitions after repartitioning to minimize skew

---

### Question 26 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: date_add function

**Question**:
Which of the following correctly adds **7 days** to a date column named `order_date`?

- A) `F.date_add(col('order_date'), -7)`
- B) `F.dateadd('order_date', 7)`
- C) `F.date_add(col('order_date'), 7)`
- D) `col('order_date') + F.lit(7)`

---

### Question 27 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: conditional expressions in selectExpr

**(Select all that apply)**
Which of the following are **valid** SQL expressions that can be used inside `selectExpr()` or `F.expr()` to implement conditional logic?

- A) `"CASE WHEN score > 80 THEN 'high' WHEN score > 50 THEN 'medium' ELSE 'low' END AS grade"`
- B) `"IF(score > 80, 'high', IF(score > 50, 'medium', 'low')) AS grade"`
- C) `"IIF(score > 80, 'high', 'low') AS grade"`
- D) `"SWITCH(score, 80, 'high', 50, 'medium', 'low') AS grade"`
- E) `"NULLIF(score, 0) AS adjusted_score"`

---

### Question 28 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: approx_count_distinct

**Question**:
What does `F.approx_count_distinct(col('user_id'))` do?

- A) Returns the exact distinct count, but only for columns with fewer than 10,000 distinct values
- B) Returns a statistically approximate distinct count using the **HyperLogLog** algorithm — much faster than exact counting on large datasets
- C) Returns the count of values that appear at least twice in the column
- D) Returns the count of non-null values in the column, ignoring duplicates only within each partition

---

### Question 29 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: explode() row multiplication

**Scenario**:
A DataFrame `df` has 3 rows. Each row has an `interests` column of type `ArrayType(StringType)` containing 2 elements.

**Question**:
After calling `df.select('user_id', F.explode('interests').alias('interest'))`, how many rows does the result contain?

- A) 3 (the same number of original rows — one array per row)
- B) 6 (3 source rows × 2 array elements per row)
- C) 2 (one row per distinct array element value across all rows)
- D) It depends on whether there are duplicate values across arrays

---

### Question 30 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: split() return type

**Question**:
What is the return type of `F.split(col('full_name'), ' ')`?

- A) `StringType`
- B) `MapType(IntegerType, StringType)`
- C) `ArrayType(StringType)`
- D) A `StructType` with one field per split token

---

### Question 31 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: date_format

**Question**:
What does `F.date_format(col('created_at'), 'yyyy-MM')` return?

- A) A `DateType` column truncated to the first day of the month
- B) A `StringType` column formatted as `year-month` (e.g., `'2024-03'`)
- C) A `TimestampType` column with the time component set to midnight
- D) An `IntegerType` combining year and month (e.g., `202403`)

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: current_timestamp evaluation

**Question**:
What does `F.current_timestamp()` return in PySpark?

- A) The timestamp of the most recent commit to the Delta table being read
- B) A `TimestampType` Column representing the current date and time, evaluated **once at query start** and applied uniformly to all rows
- C) A Python `datetime` object that must be wrapped with `F.lit()` before use
- D) The Unix epoch time as a `LongType` column

---

### Question 33 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: concat_ws null behavior

**Scenario**:
A row has a `parts` column containing the array `['hello', None, 'world']`. The expression `F.concat_ws('-', F.col('parts'))` is applied.

**Question**:
What is the result?

- A) The entire result is `null` because the array contains a `null` element
- B) `'hello-world'` — `null` elements are **skipped** by `concat_ws`
- C) `'hello--world'` — the `null` element is treated as an empty string
- D) An error is raised because `concat_ws` does not accept arrays containing nulls

---

### Question 34 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: GROUPING SETS

**(Select all that apply)**
Which of the following are correct statements about `GROUPING SETS` in Spark SQL?

- A) `GROUPING SETS` allows computing aggregations for multiple grouping combinations in a single query pass
- B) `GROUPING SETS ((), (region))` produces both a grand total row (empty grouping) and region-level subtotals
- C) Columns not in the active grouping set appear as `null` in the result for that row
- D) `GROUPING SETS` can only be expressed via the `rollup()` DataFrame API method and has no direct SQL syntax

---

### Question 35 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cross join result size

**Question**:
What is the result of a `CROSS JOIN` between a DataFrame with 100 rows and a DataFrame with 50 rows?

- A) 100 rows (rows matched by row number)
- B) 50 rows (the smaller DataFrame limits the result)
- C) 5,000 rows (Cartesian product — every row in the first paired with every row in the second)
- D) Between 50 and 100 rows, depending on matching values

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: percent_rank vs cume_dist

**Question**:
What is the key numerical difference between `percent_rank()` and `cume_dist()` in Spark window functions?

- A) `percent_rank()` produces values in `[0, 1]` using `(rank - 1) / (total_rows - 1)`; `cume_dist()` produces values in `(0, 1]` using `rank / total_rows`
- B) `percent_rank()` produces values in `(0, 1]` while `cume_dist()` produces values in `[0, 1)`
- C) They are identical — both divide rank by total row count
- D) `cume_dist()` requires an explicit frame clause while `percent_rank()` does not

---

### Question 37 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: lead() on last row

**Scenario**:
The following window expression is applied to 4 rows in a single partition ordered by `day`:
```python
w = Window.partitionBy('dept').orderBy('day')
df.withColumn('next_day', F.lead('day', 1).over(w))
```

**Question**:
What is the value of `next_day` for the **last row** (highest `day` value) in the partition?

- A) The same as the current row's `day` value (self-reference)
- B) The first row's `day` value (circular wrap-around)
- C) `null` — there is no next row for the last element
- D) An error is raised because `lead()` requires at least one following row

---

### Question 38 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ntile() with uneven distribution

**Scenario**:
A partition contains **10 rows**. `F.ntile(3).over(w)` is applied.

**Question**:
What tile sizes does Spark assign?

- A) All three tiles get exactly 3 rows; the 10th row is discarded
- B) Tile 1 gets 4 rows, tile 2 gets 3 rows, tile 3 gets 3 rows — extra rows fill the **earlier** tiles first
- C) Tile 1 gets 3 rows, tile 2 gets 3 rows, tile 3 gets 4 rows — extra rows fill later tiles
- D) Spark raises an error because 10 rows cannot be evenly divided into 3 tiles

---

### Question 39 — Spark SQL

**Difficulty**: Hard
**Answer Type**: many
**Topic**: valid window frame specifications

**(Select all that apply)**
Which of the following window frame specifications are **valid** in Spark SQL?

- A) `Window.partitionBy('dept').orderBy('salary').rowsBetween(Window.unboundedPreceding, Window.currentRow)`
- B) `Window.orderBy('value').rangeBetween(-100, 100)` — numeric range of ±100 units
- C) `Window.partitionBy('dept').rowsBetween(Window.currentRow, Window.unboundedFollowing)` — current row to end of partition
- D) `Window.partitionBy('dept').orderBy('salary').rowsBetween(Window.currentRow, -1)` — start after current, end 1 before current
- E) `Window.partitionBy('dept').rowsBetween(Window.unboundedPreceding, Window.unboundedFollowing)` — entire partition

---

### Question 40 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Catalyst predicate pushdown and projection pushdown

**Scenario**:
```python
df = spark.read.parquet('/data/events')
result = df.filter(col('year') == 2024) \
           .select('event_id', 'event_type') \
           .filter(col('event_type') == 'click')
```

**Question**:
After the **Catalyst optimizer** applies rule-based optimizations, what is the most likely physical execution order?

- A) Filter year → Select → Filter event_type → FileScan
- B) FileScan → Filter year → Select → Filter event_type (no reordering occurs)
- C) FileScan (with both filters pushed down to the reader; only `event_id` and `event_type` columns read) → in-memory selection
- D) Select → Filter year → Filter event_type → FileScan

---

### Question 41 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: drop() method

**Question**:
Which method removes a column named `temp_col` from a DataFrame `df`?

- A) `df.remove('temp_col')`
- B) `df.drop('temp_col')`
- C) `df.delete(col('temp_col'))`
- D) `df.select(df.columns.remove('temp_col'))`

---

### Question 42 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: when().otherwise() with no else

**Question**:
What is returned by `F.when(col('score') > 90, 'A')` — with **no `.otherwise()` chained** — for a row where `score = 75`?

- A) `'A'`
- B) An empty string `''`
- C) `null`
- D) An `AnalysisException` — `.otherwise()` is mandatory

---

### Question 43 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: default join type

**Question**:
When calling `df1.join(df2, on='id')` **without** specifying the `how` parameter, which join type is used?

- A) `'left'` (left outer join)
- B) `'full'` (full outer join)
- C) `'inner'`
- D) `'cross'` (Cartesian product)

---

### Question 44 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: agg() with multiple functions

**Question**:
Which of the following correctly computes **both** the average and maximum `salary` in a single `groupBy().agg()` call?

- A) `df.groupBy('dept').agg(F.avg('salary'), F.max('salary'))`
- B) `df.groupBy('dept').avg('salary').max('salary')`
- C) `df.groupBy('dept').agg({'salary': 'avg', 'salary': 'max'})` (dict with same key twice)
- D) `df.groupBy('dept').aggregate(avg='salary', max='salary')`

---

### Question 45 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: read.text() schema

**Question**:
When reading a plain text file with `spark.read.text('/data/file.txt')`, what schema does the resulting DataFrame have?

- A) One column named `value` of `StringType` — each line of the file becomes one row
- B) One column named `line` of `StringType`
- C) The schema is automatically inferred by parsing the text content
- D) A single column of `BinaryType` containing raw byte content

---

### Question 46 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.range() schema

**Question**:
What is the schema of the DataFrame returned by `spark.range(0, 10)`?

- A) One column named `index` of `LongType`
- B) One column named `id` of `LongType`
- C) One column named `id` of `IntegerType`
- D) Two columns: `start` (`LongType`) and `end` (`LongType`)

---

### Question 47 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: unionByName vs union

**(Select all that apply)**
Which of the following statements about `unionByName()` and `union()` are correct?

- A) `unionByName()` aligns columns **by name**, so column order in the schemas does not matter
- B) `union()` aligns columns **by position**, which can silently produce incorrect results when the two DataFrames list columns in different orders
- C) `unionByName()` raises an `AnalysisException` if one DataFrame has columns the other does not, unless `allowMissingColumns=True` is specified
- D) `unionByName()` automatically removes duplicate rows between the two DataFrames
- E) `unionByName(allowMissingColumns=True)` fills columns missing in one DataFrame with `null`

---

### Question 48 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: left_semi join semantics

**Question**:
What rows does a `left_semi` join return?

- A) All rows from the left DataFrame plus all rows from the right, with `null` where there is no match
- B) Only rows from the **left** DataFrame for which at least one matching row exists in the right DataFrame; no columns from the right are included
- C) Only rows that exist in both DataFrames (equivalent to `INTERSECT`)
- D) All rows from the left DataFrame and matching rows from the right, including right DataFrame columns

---

### Question 49 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: left_anti join semantics

**Question**:
What rows does a `left_anti` join return?

- A) All rows from the left DataFrame that **have** a matching row in the right DataFrame
- B) Rows from the right DataFrame that have no match in the left DataFrame
- C) Only rows from the **left** DataFrame that have **no matching row** in the right DataFrame
- D) The symmetric difference — rows that appear in exactly one of the two DataFrames

---

### Question 50 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: full outer join null filling

**Scenario**:
An inner join between `df_orders` and `df_customers` is changed to a `full outer join`. When a row exists in `df_orders` but has no matching row in `df_customers`:

**Question**:
What values do the `df_customers`-only columns have in the result?

- A) They contain the last known value from the previous matching row
- B) They are filled with zeros (numeric) or empty strings (string)
- C) They are `null`
- D) An error is raised because unmatched rows are not supported in full outer joins

---

### Question 51 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: explode_outer vs explode

**(Select all that apply)**
Which of the following correctly describe `explode()` and `explode_outer()`?

- A) `explode()` **drops** rows where the array column is `null` or empty; `explode_outer()` **preserves** those rows, producing a single row with `null` in the exploded column
- B) Both `explode()` and `explode_outer()` work on `ArrayType` and `MapType` columns
- C) `explode_outer()` is only available via `spark.sql()` — it cannot be used in the DataFrame API
- D) `explode()` returns one row per array element and repeats all non-array columns
- E) `posexplode()` is a variant of `explode()` that also returns the element's integer index position

---

### Question 52 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: array_contains

**Question**:
Which expression correctly tests whether the array column `skills` contains the value `'Python'`?

- A) `col('skills').contains('Python')`
- B) `F.array_contains(col('skills'), 'Python')`
- C) `F.isin(col('skills'), 'Python')`
- D) `F.in_array('Python', col('skills'))`

---

### Question 53 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: datediff return type

**Question**:
What does `F.datediff(col('end_date'), col('start_date'))` return?

- A) A `TimestampType` representing the elapsed time between the two dates
- B) An `IntegerType` Column representing the number of **days** from `start_date` to `end_date`
- C) A `StringType` formatted interval such as `'3 days 2 hours'`
- D) An error if either column is a `TimestampType` instead of `DateType`

---

### Question 54 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cast syntax

**Question**:
Which expression correctly casts the `StringType` column `amount_str` to `DoubleType`?

- A) `col('amount_str').cast('double')`
- B) `F.to_double(col('amount_str'))`
- C) `F.cast(col('amount_str'), 'double')`
- D) `col('amount_str').asDouble()`

---

### Question 55 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: monotonically_increasing_id guarantees

**Question**:
What does `F.monotonically_increasing_id()` **guarantee** about the generated values?

- A) Values are sequential integers starting from 0 with no gaps
- B) Values are monotonically increasing across partitions and unique across all rows, but are **not necessarily sequential** (gaps exist between partitions)
- C) Values restart from 0 at the beginning of each partition
- D) Values are random but globally unique, suitable as UUID surrogates

---

### Question 56 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: first() window aggregate

**Question**:
What does `F.first(col('score')).over(w)` return for each row when the window spec covers the entire partition (no frame restriction)?

- A) The minimum value of `score` in the partition
- B) The value of `score` for the current row
- C) The value of `score` for the row with the lowest integer row number
- D) The value of `score` for the **first row** in the partition as ordered by the window's `orderBy` clause (or an arbitrary row if no order is defined)

---

### Question 57 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: lag() on first row

**Scenario**:
```python
w = Window.partitionBy('store_id').orderBy('date')
df.withColumn('prev_sales', F.lag('sales', 1).over(w))
```

**Question**:
What is the value of `prev_sales` for the **first row** (earliest `date`) within each `store_id` partition?

- A) `0` — the default fill value when no `default` argument is provided
- B) The last row's `sales` value in the same partition (circular reference)
- C) `null` — there is no prior row for the first element
- D) The `sales` value from the first row of the following partition

---

### Question 58 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: write modes that preserve existing data

**(Select all that apply)**
Which `write.mode()` values **preserve** the existing data at the output path (i.e., do not delete or overwrite it)?

- A) `'overwrite'`
- B) `'append'`
- C) `'ignore'`
- D) `'error'` (also called `'errorIfExists'`)

---

### Question 59 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: read JSON multiline option

**Question**:
A JSON file stores each complete JSON document formatted across multiple lines (pretty-printed). Which option is required to correctly parse it with `spark.read.json()`?

- A) `.option('singleLine', True)`
- B) `.option('multiline', True)`
- C) `.option('pretty', True)`
- D) No special option — Spark automatically detects multi-line JSON

---

### Question 60 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: toDF() rename all columns

**Question**:
What does the following code do?
```python
df.toDF('customer_id', 'full_name', 'email_address')
```

- A) Adds three new columns using the given values as defaults
- B) Renames all existing columns **by position**: first column → `customer_id`, second → `full_name`, third → `email_address`
- C) Selects only the three columns with the given names from the DataFrame
- D) Creates a temporary view with those three column names

---

### Question 61 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: crossJoin result

**Question**:
What does `df1.crossJoin(df2)` produce?

- A) The intersection of rows common to both DataFrames
- B) A **Cartesian product** — every row in `df1` paired with every row in `df2`
- C) The union of both DataFrames aligned by column position
- D) A join on all columns with the same name in both DataFrames

---

### Question 62 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: when().when().otherwise() chaining

**Question**:
Which expression correctly assigns `'high'` when `score > 80`, `'medium'` when `score > 50`, and `'low'` otherwise?

- A) `F.when(col('score') > 80, 'high').otherwise(F.when(col('score') > 50, 'medium').otherwise('low'))`
- B) `F.when(col('score') > 80, 'high').when(col('score') > 50, 'medium').otherwise('low')`
- C) `F.case(col('score')).when(80, 'high').when(50, 'medium').otherwise('low')`
- D) Both A and B produce correct results; B is the more idiomatic form

---

### Question 63 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: filtering not-null values

**(Select all that apply)**
Which of the following expressions correctly filter rows where the `email` column is **not null**?

- A) `df.filter(col('email').isNotNull())`
- B) `df.filter(~col('email').isNull())`
- C) `df.filter(col('email') != None)` (Python `!=` with `None`)
- D) `df.na.drop(subset=['email'])`
- E) `df.filter(col('email') <> None)`

---

### Question 64 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: join on multiple conditions

**(Select all that apply)**
Which of the following correctly perform an **inner join** on both `user_id` and `event_date`?

- A) `df1.join(df2, ['user_id', 'event_date'], 'inner')`
- B) `df1.join(df2, (df1.user_id == df2.user_id) & (df1.event_date == df2.event_date), 'inner')`
- C) `df1.join(df2, on={'user_id', 'event_date'}, how='inner')`
- D) `df1.join(df2, 'user_id == df2.user_id AND event_date == df2.event_date', 'inner')`

---

### Question 65 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ambiguous column reference after join

**Scenario**:
```python
result = df_orders.join(df_customers, df_orders.id == df_customers.id, 'inner')
result.select('id').show()
```

**Question**:
What happens when `result.select('id')` is called?

- A) The `id` column from `df_orders` is returned because it was the left DataFrame
- B) An `AnalysisException` is raised — the column `id` is **ambiguous** because both DataFrames contribute a column named `id` to the join result
- C) Spark returns both `id` columns as two separate columns with the same name
- D) Spark automatically deduplicates and returns one `id` column

---

### Question 66 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: schema evolution — mergeSchema

**Scenario**:
A Parquet dataset was written in two batches. Batch 1 has columns `id` and `name`. Batch 2 adds a new column `email`. Both batches are in the same directory.

**Question**:
When calling `spark.read.parquet('/data/')` with **no additional options**, what happens?

- A) An error is raised because the schemas are incompatible
- B) Only columns common to all files (`id` and `name`) are returned; `email` is silently dropped
- C) Spark merges the schemas and reads `email` as `null` for rows from batch 1, but this requires `.option('mergeSchema', True)` or `spark.conf.set('spark.sql.parquet.mergeSchema', 'true')`
- D) Spark automatically merges schemas without any special configuration

---

### Question 67 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: shared Window spec reuse

**Scenario**:
```python
w = Window.partitionBy('dept').orderBy('salary')
df.withColumn('rank', F.rank().over(w)) \
  .withColumn('dense_rank', F.dense_rank().over(w)) \
  .withColumn('running_total', F.sum('salary').over(w))
```

**Question**:
Which statement is true about this approach?

- A) Sharing a single `Window` spec guarantees all three aggregations are fused into one pass — this is a required pattern for efficiency
- B) Each `.withColumn()` creates an independent window calculation; Spark's optimizer **may** consolidate window operations with the same spec into a single pass, but this is not guaranteed
- C) This raises an error because `rank()`, `dense_rank()`, and `sum()` cannot share the same window spec
- D) The `Window` spec `w` must be redefined before each call to avoid state contamination

---

### Question 68 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Pandas UDF performance advantage

**Question**:
Why do **Pandas UDFs** (vectorized UDFs using `@F.pandas_udf`) typically outperform standard **Python UDFs** (`@F.udf`) for the same transformation?

- A) Pandas UDFs run on the Driver while Python UDFs run on Executors, reducing network overhead
- B) Pandas UDFs serialize individual rows one at a time while Python UDFs process entire partitions
- C) Pandas UDFs transfer data in **columnar batches via Apache Arrow**, avoiding per-row serialization/deserialization between the JVM and the Python worker process
- D) Python UDFs bypass Catalyst optimization while Pandas UDFs are fully optimized

---

### Question 69 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: partition pruning on read

**Scenario**:
A DataFrame was written with `.partitionBy('country').parquet('/data/events')`. It is later read and filtered:
```python
df = spark.read.parquet('/data/events')
df.filter(col('country') == 'US').count()
```

**Question**:
What optimization does Spark apply?

- A) Spark reads all partition directories and applies the filter after loading all data into memory
- B) Spark applies **partition pruning** — it reads only the `country=US/` directory, skipping all other country directories entirely
- C) Spark applies predicate pushdown into Parquet row groups but still reads all directories
- D) Spark cannot apply any optimization because the partition column value is not stored inside the Parquet data files

---

### Question 70 — DataFrame API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: groupBy agg result schema facts

**(Select all that apply)**
After calling:
```python
result = df.groupBy('region', 'category').agg(
    F.count('*').alias('total_orders'),
    F.sum('revenue').alias('total_revenue'),
    F.avg('discount').alias('avg_discount')
)
```
Which of the following are true about the resulting DataFrame?

- A) The `groupBy` key columns (`region`, `category`) always appear first in the result schema
- B) The aggregate columns appear in the order they were specified in `agg()`
- C) The result always contains exactly 5 columns
- D) The result type of `count('*')` is `LongType` (not `IntegerType`)
- E) Calling `groupBy().agg(F.count('*'))` is fully equivalent to calling `groupBy().count()` in terms of result schema

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: cache() materialization timing

**Question**:
When is `df.cache()` (with no immediate action following it) actually **materialized in Executor memory**?

- A) Immediately when `cache()` is called
- B) During the **next action** that triggers execution of the cached plan
- C) At the end of the Python script when the SparkSession is stopped
- D) When another job in the same Spark application references the same DataFrame

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: unpersist()

**Question**:
What does `df.unpersist()` do?

- A) Permanently deletes the underlying data files from storage
- B) Removes the cached DataFrame blocks from Executor memory and disk, freeing that storage capacity for other data
- C) Marks the DataFrame as a checkpoint so future operations skip full lineage recomputation
- D) Drops the temporary view associated with the DataFrame

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: many
**Topic**: MEMORY_ONLY storage level tradeoffs

**(Select all that apply)**
Which of the following are true about `StorageLevel.MEMORY_ONLY`?

- A) Partitions that do not fit in available Executor memory are **dropped** (not spilled to disk)
- B) It is the **fastest** storage level for data that fits in memory because it stores deserialized Java/Python objects
- C) Dropped partitions are automatically recomputed from their lineage on the next access
- D) `MEMORY_ONLY` is the default storage level used by `df.cache()`
- E) `MEMORY_ONLY` replicates each partition across two Executor nodes for fault tolerance

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.memory.fraction calculation

**Scenario**:
The Executor heap is 10 GB. `spark.memory.fraction` is at its default value of `0.6`.

**Question**:
How much memory (approximately) is allocated to the combined **Spark Memory** region (storage + execution)?

- A) 6 GB
- B) 4 GB
- C) 5 GB
- D) 3 GB

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: sortWithinPartitions vs orderBy

**Question**:
How does `df.sortWithinPartitions('salary')` differ from `df.orderBy('salary')`?

- A) They are identical — both perform a global sort across all partitions
- B) `sortWithinPartitions()` sorts rows only **within each partition independently** (no shuffle required); `orderBy()` performs a **global sort** with a full shuffle to produce a totally ordered output
- C) `sortWithinPartitions()` sorts by the partition key; `orderBy()` sorts by any specified column
- D) `sortWithinPartitions()` is lazy; `orderBy()` is an action that triggers immediate execution

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: broadcast size configuration

**Question**:
Which configuration property controls the threshold below which Spark **automatically** broadcasts a table in a join?

- A) `spark.broadcast.maxSize`
- B) `spark.sql.autoBroadcastJoinThreshold`
- C) `spark.driver.maxBroadcastSize`
- D) `spark.sql.broadcastTimeout`

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: many
**Topic**: salting steps for skew mitigation

**(Select all that apply)**
Which of the following are steps in the **salting** technique for mitigating data skew in a join?

- A) Adding a random salt integer (from 0 to N-1) to the join key of the **large** (skewed) DataFrame
- B) Replicating each row of the **small** DataFrame N times, each with a different salt value (0 to N-1)
- C) Joining on the **composite key** (original join key + salt value)
- D) Repartitioning the large DataFrame by the original skewed key before the join (without salt)
- E) Disabling AQE (`spark.sql.adaptive.enabled = false`) to prevent it from interfering with the salt join

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: executor cores tuning guidance

**Question**:
Which guidance best describes how to tune `spark.executor.cores`?

- A) Set it to `1` to maximize parallelism by giving every core its own dedicated Executor JVM
- B) Set it to the total number of CPU cores in the entire cluster
- C) Set it to **4 or 5 cores per Executor** to balance parallelism with per-Executor overhead (JVM startup, GC pressure, HDFS client throughput); very high core counts per Executor can degrade HDFS throughput
- D) Always set it equal to `spark.sql.shuffle.partitions` divided by the number of Executors

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: GC overhead OOM on Executors

**Scenario**:
A Spark job fails with `java.lang.OutOfMemoryError: GC overhead limit exceeded` on Executors during a large shuffle stage.

**Question**:
Which combination of changes is most likely to resolve this?

- A) Increase `spark.driver.memory` so the Driver can offload memory pressure from Executors
- B) Reduce `spark.executor.cores` to limit per-Executor parallelism, and/or increase `spark.executor.memory`, and/or increase `spark.sql.shuffle.partitions` to produce smaller per-partition data volumes
- C) Set `spark.sql.adaptive.enabled = false` to prevent AQE from dynamically changing partition sizes
- D) Switch to local mode so all execution happens in a single JVM with more available heap

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: setLogLevel

**Question**:
Which call reduces Spark's log output to show only `ERROR`-level messages, suppressing `INFO` and `WARN`?

- A) `spark.conf.set('spark.log.level', 'ERROR')`
- B) `spark.sparkContext.setLogLevel('ERROR')`
- C) `logging.basicConfig(level=logging.ERROR)` in the Python driver script
- D) `spark.sql("SET spark.log.level = ERROR")`

---

### Question 81 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: writeStream.start() return type

**Question**:
What does `df.writeStream.start()` return?

- A) A `DataFrame` containing the results of the first micro-batch
- B) A `StreamingQuery` object used to monitor and control the running query
- C) A Python `Future` that resolves when the stream terminates
- D) `None` — the method starts the stream asynchronously with no handle returned

---

### Question 82 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: rate source schema

**Question**:
When reading from a `rate` streaming source, what schema does the resulting DataFrame have?

- A) `value BIGINT` only — a monotonically increasing counter
- B) `timestamp TIMESTAMP, value BIGINT` — a timestamp column and a monotonically increasing counter
- C) `key STRING, value STRING` — generic key-value pairs
- D) The schema depends on the `rowsPerSecond` option value

---

### Question 83 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: many
**Topic**: socket source limitations

**(Select all that apply)**
Which of the following are limitations of the **socket** streaming source that make it unsuitable for production?

- A) It is **not fault-tolerant** — data received during query downtime cannot be replayed after a restart
- B) It supports only text (string) data; binary or structured formats require a different source
- C) It can only connect to a single host and port
- D) It is a recommended source for high-volume production workloads due to its simplicity
- E) It provides no offset tracking mechanism for replaying messages

---

### Question 84 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: foreachBatch use case

**Question**:
Why is `foreachBatch` particularly useful in Structured Streaming?

- A) It is the only way to use aggregations in a streaming query
- B) It gives access to the full micro-batch DataFrame, allowing it to be written to **multiple output sinks** or processed with any batch DataFrame API operations within a single micro-batch
- C) It bypasses checkpoint requirements, making it faster for low-latency scenarios
- D) It automatically enforces exactly-once semantics without requiring a checkpoint location

---

### Question 85 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: availableNow vs once trigger

**Question**:
How does `trigger(availableNow=True)` (Spark 3.3+) differ from `trigger(once=True)`?

- A) `availableNow=True` runs continuously; `once=True` stops after one micro-batch
- B) `availableNow=True` processes all available data using **multiple micro-batches** (respecting rate limits and watermarks), then stops; `once=True` processes all available data in a **single micro-batch**, then stops
- C) They are identical — both process all available data and stop
- D) `availableNow=True` requires Delta Lake as the source; `once=True` works with any source

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: memory sink query name

**Question**:
When using the `memory` output sink in Structured Streaming, how do you query the accumulated results?

- A) The results must be registered as a temp view explicitly using `df.createOrReplaceTempView()`
- B) The `queryName` option defines the in-memory table name; query it via `spark.sql("SELECT * FROM <queryName>")`
- C) Results are accessible through `query.recentProgress` as JSON records
- D) The streaming query must be stopped before the accumulated data can be read

---

### Question 87 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Delta Lake as streaming source

**(Select all that apply)**
Which of the following statements about using **Delta Lake** as a Structured Streaming source are correct?

- A) Delta Lake's transaction log allows the stream to start from any historical table version or timestamp
- B) Delta Lake streaming supports exactly-once semantics when combined with checkpointing
- C) Delta Lake streaming sources require `trigger(once=True)` — continuous streaming is not supported
- D) Delta Lake enforces schema, causing the stream to fail if an incompatible schema change is made to the source table
- E) A Delta table can serve simultaneously as a streaming source in one query and as a streaming sink in another

---

### Question 88 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: complete output mode use case

**Question**:
When is the `complete` output mode appropriate in Structured Streaming?

- A) For any stateless streaming query where new rows need to be appended
- B) For streaming queries that include **stateful aggregations** (e.g., `groupBy().count()`) where the full updated aggregate result must be visible to consumers on every trigger
- C) For queries using watermarking where late data is discarded after the threshold
- D) Only for file sinks — `complete` mode cannot be used with Kafka or console sinks

---

### Question 89 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: watermark late-data cutoff

**Scenario**:
```python
df.withWatermark('event_time', '10 minutes') \
  .groupBy(F.window('event_time', '5 minutes')) \
  .count()
  .writeStream.outputMode('append').start()
```
An event arrives with `event_time = 12:00`. The current maximum event time seen is `12:15`.

**Question**:
Is this event included in the windowed aggregation?

- A) Yes — all events are always included regardless of watermark
- B) Yes — watermark cutoff is `12:05`, and `12:00 >= 12:05` is false... wait, this would include it
- C) No — the watermark cutoff is `12:15 − 10 minutes = 12:05`; since `12:00 < 12:05`, this event is considered **late and is dropped**
- D) No — the watermark cutoff is `12:15 − 5 minutes = 12:10`; since `12:00 < 12:10`, this event is dropped

---

### Question 90 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: awaitTermination and process exit

**Scenario**:
A Python application starts a Structured Streaming query and then the main thread exits without calling `query.awaitTermination()`.

**Question**:
What happens to the streaming query?

- A) The query continues running independently on the cluster
- B) The query terminates when the main Python thread exits, because the streaming query runs in a background thread managed by the Python driver process
- C) The query is automatically migrated to the Cluster Manager and runs without the Python driver
- D) The query pauses until the Python process is restarted

---

### Question 91 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark Connect default port

**Question**:
What is the **default gRPC port** used by a Spark Connect server?

- A) 4040
- B) 7077
- C) 8080
- D) 15002

---

### Question 92 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Py4J vs gRPC comparison

**Question**:
In **classic Spark**, Python commands communicate with the JVM Driver via a Py4J socket bridge. In **Spark Connect**, what replaces this?

- A) HTTP/1.1 REST calls with JSON-encoded requests and responses
- B) **gRPC over HTTP/2** — logical plans are serialized as Protocol Buffers (requests) and results are returned as Apache Arrow record batches
- C) The same Py4J bridge, but the JVM now runs on a cluster node instead of locally
- D) WebSocket connections for bidirectional live streaming of computation

---

### Question 93 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: client crash impact comparison

**Question**:
How does an **application crash** behave differently in classic Spark vs Spark Connect?

- A) In both cases, the Driver crashes and the job fails
- B) **Classic Spark:** the Driver is embedded in the application process — an app crash kills the Driver and the job. **Spark Connect:** the Driver runs on the cluster — an app crash does not kill the Driver; the job can continue and the client can reconnect
- C) In both cases, the Cluster Manager automatically restarts the Driver
- D) Classic Spark: the job continues because state is saved to disk. Spark Connect: the job fails because the gRPC session is terminated

---

### Question 94 — Spark Connect

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Spark Connect capabilities

**(Select all that apply)**
Which of the following statements about **Spark Connect** capabilities are correct?

- A) Any programming language that has a gRPC client library can be a Spark Connect client (Go, Rust, Java, Python, etc.)
- B) The client library does not require a local JVM installation because all communication uses gRPC
- C) Query results are returned to the client as **Apache Arrow** record batches
- D) The Spark Connect client exposes the RDD API via a remote `SparkContext` proxy
- E) Multiple clients can connect to the same Spark Connect server simultaneously

---

### Question 95 — Spark Connect

**Difficulty**: Hard
**Answer Type**: one
**Topic**: version compatibility between client and server

**Scenario**:
A company upgrades their Spark cluster from 3.4 to 3.5. Some client applications still use the Spark Connect Python library version 3.4.

**Question**:
Which statement correctly describes version compatibility behavior?

- A) The 3.4 client applications immediately fail — client and server versions must always match exactly
- B) Spark Connect supports version negotiation — a 3.4 client and a 3.5 server can interoperate, allowing teams to upgrade the server and client libraries independently
- C) Client applications must be updated to 3.5 within 24 hours or the cluster will block older client connections
- D) Only the minor version must match; patch versions can differ freely

---

### Question 96 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: from_pandas() conversion

**Question**:
A developer has a native pandas DataFrame `pdf` and wants to convert it to a `pyspark.pandas` DataFrame directly using the `pyspark.pandas` namespace. Which method should they use?

- A) `ps.DataFrame.from_pandas(pdf)`
- B) `ps.from_pandas(pdf)`
- C) `pdf.to_spark()`
- D) `pdf.pandas_api()`

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: psdf.to_spark() return type

**Question**:
What does `psdf.to_spark()` return?

- A) A native pandas DataFrame collected to the Driver
- B) A **PySpark DataFrame** (distributed; data is not collected to the Driver)
- C) A `pyspark.pandas` DataFrame backed by Spark with a new default index
- D) A Python list of Row objects representing the DataFrame's contents

---

### Question 98 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Koalas legacy import

**Question**:
A Databricks notebook contains `import databricks.koalas as ks`. What is the status of this import?

- A) It is the current recommended way to use the Pandas API on Spark in Databricks
- B) It is a **legacy import** — Koalas was merged into PySpark as `pyspark.pandas` in Spark 3.2; the Koalas import still works on Databricks Runtime but is **deprecated**
- C) It raises an `ImportError` on Databricks Runtime 12.0 and later
- D) It imports a completely separate project unrelated to `pyspark.pandas`

---

### Question 99 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: many
**Topic**: distributed-sequence index type

**(Select all that apply)**
Which of the following accurately describe the `'distributed-sequence'` default index type in `pyspark.pandas`?

- A) It generates a **globally unique** integer index without requiring a global sort (no full shuffle)
- B) It constructs the index using the **partition ID and row offset within each partition**
- C) Indices produced are strictly sequential with no gaps (0, 1, 2, 3, ...) across partitions
- D) It is **less expensive** than the `'sequence'` index type because it avoids a global sort
- E) It is the default `default_index_type` when `pyspark.pandas` is first imported

---

### Question 100 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: one
**Topic**: sequential index type performance trade-off

**Scenario**:
A downstream library requires a `pyspark.pandas` DataFrame with a **strictly sequential** integer index (0, 1, 2, 3, ...) with no gaps. A developer switches from the default `'distributed'` index type to `'sequence'` and observes significant performance degradation on a large DataFrame.

**Question**:
Why does the `'sequence'` index type perform worse?

- A) `'sequence'` serializes the entire DataFrame to the Driver before generating the index
- B) `'sequence'` generates a globally sequential index by performing a **global sort** (full shuffle) across all partitions, which is expensive at scale
- C) `'sequence'` disables WholeStageCodegen, forcing row-by-row processing
- D) `'sequence'` generates the index using a Python UDF, which bypasses Catalyst optimization

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | `local[*]` instructs Spark to run in a single JVM and use all available logical CPU cores as worker threads — ideal for local development without a cluster. | topic1-prompt1-spark-architecture.md | Easy |
| 2 | B | The `--deploy-mode` flag controls where the Driver runs: `client` (submitting machine) or `cluster` (inside the cluster). No other flag name is valid for this purpose. | topic1-prompt1-spark-architecture.md | Easy |
| 3 | C | The Standalone cluster manager listens on port **7077** by default. Port 4040 is the Spark UI, 8080 is the Standalone master web UI, and 18080 is the history server. | topic1-prompt1-spark-architecture.md | Easy |
| 4 | A, B, D, E | In cluster deploy mode the Driver runs on a worker node (A), the submitting machine can disconnect (B), and logs reside on the worker node (E). Databricks runs the Driver inside the cluster (D). C is wrong — client mode is the spark-submit default, not cluster mode. | topic1-prompt1-spark-architecture.md | Easy |
| 5 | B | In client deploy mode the Driver process runs on the submitting machine. When that machine is killed, the Driver is killed and Spark has no mechanism to migrate or restart it — the entire application fails. | topic1-prompt1-spark-architecture.md | Medium |
| 6 | B | An Accumulator is a distributed counter/sum: Tasks can only add values (associative and commutative), and only the Driver can read the final accumulated result after an action completes. | topic1-prompt1-spark-architecture.md | Medium |
| 7 | C | Accumulator reads inside Task code return the Executor-local initial value (e.g., `0`), not the globally aggregated total. The Driver sees the final accumulated value only after the action completes. | topic1-prompt1-spark-architecture.md | Medium |
| 8 | B | `spark.default.parallelism` sets the default partition count for **RDD** operations such as `reduceByKey()` and `join()`. For DataFrame shuffles, `spark.sql.shuffle.partitions` applies instead. | topic1-prompt1-spark-architecture.md | Medium |
| 9 | C | Broadcast variable values are accessed via the `.value` attribute: `lookup.value['A']`. The `.value` unwraps the broadcast wrapper to return the original Python object. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 10 | B | Spark splits HDFS files into input partitions approximately one per HDFS block. `spark.sql.files.maxPartitionBytes` (default 128 MB) controls the target maximum size per partition. | topic1-prompt1-spark-architecture.md | Medium |
| 11 | B | Speculative execution (`spark.speculation = true`) detects straggler Tasks and launches duplicate copies on other Executors; whichever finishes first wins and the others are cancelled. | topic1-prompt1-spark-architecture.md | Medium |
| 12 | D | The TaskScheduler is responsible for sending individual Tasks within a ready Stage to available Executor slots. Converting user code to a DAG and detecting stage boundaries is the DAGScheduler's job. | topic1-prompt1-spark-architecture.md | Medium |
| 13 | B | `spark.executor.memoryOverhead` allocates off-heap memory per Executor for OS-level overhead, Python worker processes (PySpark), and native (non-JVM) libraries — memory that lives outside the JVM heap. | topic1-prompt7-garbage-collection.md | Medium |
| 14 | A, B, C, E | DRA adds Executors when Tasks queue up (A), removes idle Executors (B), requires an external shuffle service so shuffle files outlive removed Executors (C), and is enabled with `spark.dynamicAllocation.enabled = true` (E). D is wrong — DRA works on YARN, Standalone, and Kubernetes. | topic1-prompt1-spark-architecture.md | Medium |
| 15 | C | `df.cache()` on a PySpark DataFrame uses `MEMORY_AND_DISK` by default — partitions that exceed available memory spill to disk rather than being dropped. RDD `cache()` defaults to `MEMORY_ONLY`. | topic1-prompt1-spark-architecture.md | Medium |
| 16 | B | `MEMORY_ONLY_SER` stores partitions as serialized binary (compact, less heap) but must deserialize on every read. `MEMORY_ONLY` stores deserialized Java/Python objects (fast reads, larger heap footprint). | topic1-prompt7-garbage-collection.md | Medium |
| 17 | A, C, D | Broadcast variables are serialized and cached locally on each Executor (A), created via `broadcast()` and read via `.value` (C), and automatically re-sent if an Executor fails (D). B is wrong — they are immutable. E is wrong — they persist until explicitly destroyed with `.destroy()`. | topic1-prompt5-broadcasting-optimization.md | Hard |
| 18 | C | **Execution Memory** within the Spark Memory region handles in-flight buffers for shuffles, sort operations, and hash join maps. Storage Memory handles cached DataFrames and broadcast data. | topic1-prompt7-garbage-collection.md | Hard |
| 19 | C | `groupBy()` introduces a shuffle (Stage 1: scan + map-side partial agg → Stage 2: reduce-side full agg). `orderBy()` introduces a second shuffle exchange (Stage 2 → Stage 3: sort merge + write), yielding approximately 3 stages. | topic1-prompt4-shuffling-performance.md | Hard |
| 20 | A, C, E | `count()` (A), `write.parquet()` (C), and `show()` (E) are actions that trigger a Spark Job. `filter()` and `groupBy().agg()` are transformations — they only extend the logical plan. | topic1-prompt3-lazy-evaluation.md | Hard |
| 21 | C | `createGlobalTempView()` raises an `AnalysisException` if the view name already exists. Use `createOrReplaceGlobalTempView()` to upsert. | topic2-prompt8-spark-sql-fundamentals.md | Easy |
| 22 | B | `F.to_json()` serializes a struct (or map) column into a `StringType` column containing the JSON string representation. It does not return a Python dict or BinaryType. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 23 | C | `F.lit(100)` creates a Spark Column object representing the constant scalar value `100` that can be used in DataFrame expressions and transformations. | topic2-prompt8-spark-sql-fundamentals.md | Easy |
| 24 | B | `spark.catalog.dropTempView('orders')` removes a session-scoped temporary view. `deleteView` and `removeView` are not valid Catalog API methods. | topic2-prompt8-spark-sql-fundamentals.md | Easy |
| 25 | B | SQL `coalesce(col1, col2, col3)` returns the **first non-null** value from the argument list in order. It is a null-safe selection function, not a partition-count function. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 26 | C | `F.date_add(col('order_date'), 7)` adds 7 days. A negative offset would subtract days. `F.dateadd` is not a valid PySpark function name. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 27 | A, B, E | `CASE WHEN` (A), `IF()` (B), and `NULLIF()` (E) are all valid Spark SQL conditional expressions usable in `selectExpr()` or `F.expr()`. `IIF()` is SQL Server syntax and `SWITCH()` does not exist in Spark SQL. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 28 | B | `F.approx_count_distinct()` uses the **HyperLogLog** probabilistic algorithm to estimate distinct counts with configurable relative error — far cheaper than an exact `COUNT(DISTINCT ...)` on large datasets. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 29 | B | `explode()` produces one output row per array element. With 3 source rows × 2 elements each = 6 output rows. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 30 | C | `F.split()` splits a string on a delimiter and returns an `ArrayType(StringType)` column — each element is one token from the original string. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 31 | B | `F.date_format()` returns a `StringType` column formatted according to the pattern. `'yyyy-MM'` yields strings like `'2024-03'`. It does not return a DateType or TimestampType. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 32 | B | `F.current_timestamp()` is evaluated **once at query planning time** and returns the same timestamp value for every row in the result — it is not a per-row real-time clock call. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 33 | B | `concat_ws` skips `null` elements when joining an array with a separator. The array `['hello', None, 'world']` produces `'hello-world'`, not an error or null result. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 34 | A, B, C | `GROUPING SETS` computes multiple grouping combinations in one pass (A), supports a grand-total empty grouping (B), and fills non-active grouping columns with `null` (C). D is wrong — `GROUPING SETS` has native SQL syntax and is not limited to the DataFrame `rollup()` API. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 35 | C | A `CROSS JOIN` produces the Cartesian product: every row from the first combined with every row from the second. 100 × 50 = 5,000 rows. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 36 | A | `percent_rank()` produces values in `[0, 1]` using `(rank − 1) / (N − 1)` so the minimum is exactly 0. `cume_dist()` produces values in `(0, 1]` using `rank / N` so the minimum is `1/N`, never 0. | topic2-prompt10-window-functions.md | Medium |
| 37 | C | `lead()` looks forward in the partition. For the last row there is no following row, so Spark returns `null` (or the specified default, if provided). | topic2-prompt10-window-functions.md | Hard |
| 38 | B | When N rows cannot be divided evenly into `ntile(k)` buckets, Spark assigns the extra rows to the **earlier** tiles. 10 ÷ 3 = 3 remainder 1, so tile 1 gets 4 rows and tiles 2–3 each get 3. | topic2-prompt10-window-functions.md | Hard |
| 39 | A, B, C, E | All are valid frame specs: cumulative from start (A), numeric range ±100 (B), current row to end (C), and entire partition (E). D is invalid because `rowsBetween(currentRow, -1)` specifies an end boundary before the start, which Spark rejects. | topic2-prompt10-window-functions.md | Hard |
| 40 | C | Catalyst's rule-based optimizer applies **predicate pushdown** (both filters pushed to the FileScan) and **projection pushdown** (only `event_id` and `event_type` columns read from Parquet), minimizing I/O before any in-memory work. | topic2-prompt11-query-optimization.md | Hard |
| 41 | B | `df.drop('temp_col')` removes the named column and returns a new DataFrame. `remove()`, `delete()`, and the `columns.remove()` pattern are not valid PySpark DataFrame methods. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 42 | C | When `.otherwise()` is omitted, `F.when()` returns `null` for all rows that do not satisfy any of the conditions. It does not raise an error. | topic3-prompt13-column-manipulation-expressions.md | Easy |
| 43 | C | The default join type in PySpark's `df.join()` is `'inner'`. An explicit `how` parameter is needed for any other join type. | topic3-prompt17-joins.md | Easy |
| 44 | A | `df.groupBy('dept').agg(F.avg('salary'), F.max('salary'))` passes multiple aggregate functions to a single `agg()` call. Option C uses a dict with a duplicate key — Python silently keeps only the last entry. | topic3-prompt15-aggregations-grouping.md | Easy |
| 45 | A | `spark.read.text()` returns a DataFrame with a single column named `value` of `StringType`, where each line of the file becomes one row. | topic3-prompt19-reading-writing-dataframes.md | Easy |
| 46 | B | `spark.range()` returns a DataFrame with one column named **`id`** of `LongType`. The column is not named `index` and is not `IntegerType`. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 47 | A, B, C, E | `unionByName()` matches by name (A), `union()` matches by position (B), `unionByName()` raises an error for missing columns unless `allowMissingColumns=True` (C), and fills missing columns with null when that flag is set (E). D is wrong — neither method deduplicates rows. | topic3-prompt18-combining-dataframes.md | Medium |
| 48 | B | A `left_semi` join returns only rows from the **left** DataFrame that have at least one match in the right; no columns from the right DataFrame appear in the result. | topic3-prompt17-joins.md | Medium |
| 49 | C | A `left_anti` join returns only rows from the **left** DataFrame that have **no** match in the right — the inverse of a left_semi join. | topic3-prompt17-joins.md | Medium |
| 50 | C | In a full outer join, when a left-side row has no matching right-side row, all columns sourced from the right DataFrame are filled with `null`. | topic3-prompt17-joins.md | Medium |
| 51 | A, B, D, E | `explode()` drops null/empty arrays; `explode_outer()` preserves them (A). Both work on ArrayType and MapType (B). `explode()` repeats non-array columns for each element (D). `posexplode()` also returns the element's index position (E). C is wrong — `explode_outer()` is available in the DataFrame API. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 52 | B | `F.array_contains(col('skills'), 'Python')` is the correct built-in function for testing array membership. `col.contains()` tests string containment, not array membership. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 53 | B | `F.datediff(end, start)` returns an `IntegerType` column representing the number of **days** from `start_date` to `end_date`. It works with both DateType and TimestampType arguments. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 54 | A | `col('amount_str').cast('double')` is the standard PySpark idiom for casting. `F.to_double()` and `F.cast()` as a standalone function do not exist. | topic3-prompt21-schemas-data-types.md | Medium |
| 55 | B | `F.monotonically_increasing_id()` guarantees values are **monotonically increasing** and **unique** across all rows, but gaps exist between partitions — the values are not sequential integers. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 56 | D | `F.first().over(w)` returns the value from the **first row** in the partition as determined by the window's `orderBy` clause. With no order defined, the "first" row is arbitrary. | topic3-prompt15-aggregations-grouping.md | Medium |
| 57 | C | `lag()` looks backward. For the first row in the partition there is no prior row, so Spark returns `null` (or the specified default value if provided as the third argument). | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 58 | B, C, D | `'append'` adds new data alongside existing (B). `'ignore'` writes nothing if the path already exists, leaving existing data untouched (C). `'error'`/`'errorIfExists'` raises an exception without touching existing data (D). `'overwrite'` deletes and replaces existing data. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 59 | B | `.option('multiline', True)` instructs the JSON reader to parse each complete JSON document that spans multiple lines. Without this, Spark expects one JSON object per line. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 60 | B | `df.toDF(*new_names)` renames all columns **by position**. The first new name replaces the first column, the second replaces the second, and so on. It does not select or add columns. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 61 | B | `crossJoin()` computes the **Cartesian product** — every row from `df1` is paired with every row from `df2`, with no filter or match condition applied. | topic3-prompt17-joins.md | Medium |
| 62 | D | Both A and B produce the same correct results: A nests `when` inside `otherwise`; B chains `.when().when().otherwise()`. B is the idiomatic PySpark form. C is not valid PySpark syntax. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 63 | A, B, D | `isNotNull()` (A) and `~isNull()` (B) are equivalent and correct. `na.drop(subset=['email'])` (D) drops rows where email is null. C is wrong — Python `!= None` comparisons do not work as expected in Spark Column logic. E is wrong — `<>` is not valid Python/PySpark comparison syntax. | topic3-prompt14-filtering-row-manipulation.md | Medium |
| 64 | A, B | Passing a list of column names (A) and using a compound boolean expression (B) are both valid multi-column inner join syntaxes. C is wrong — a Python set is not a valid join condition. D is wrong — a raw SQL string is not a valid join condition parameter. | topic3-prompt17-joins.md | Medium |
| 65 | B | After joining two DataFrames that both have a column named `id`, both columns exist in the result with the same name. Selecting `'id'` by name is ambiguous and Spark raises an `AnalysisException`. Use `df_orders.id` or rename a column before the join to resolve the ambiguity. | topic3-prompt17-joins.md | Hard |
| 66 | C | Without `mergeSchema=True` (or the conf), Spark uses the schema from one file/batch and does not merge it with schemas from other files. To read `email` as `null` for files where it is absent, you must enable `.option('mergeSchema', True)` or set `spark.sql.parquet.mergeSchema = true`. | topic3-prompt21-schemas-data-types.md | Hard |
| 67 | B | Sharing a single `Window` spec is the idiomatic pattern. Spark's optimizer **may** combine window operations with identical specs into one physical pass, but this consolidation is an optimizer hint, not a guarantee. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 68 | C | Pandas UDFs use **Apache Arrow** to transfer data in columnar batches between the JVM and the Python worker, eliminating per-row pickle serialization/deserialization. This typically gives a 10–100× speedup over standard Python UDFs. | topic3-prompt22-udfs.md | Hard |
| 69 | B | Because the data was written with `.partitionBy('country')`, Spark applies **partition pruning** and reads only the `country=US/` directory, skipping all other country sub-directories entirely. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 70 | A, B, C, D | groupBy keys appear first (A), agg columns follow in specified order (B), result has exactly 2 + 3 = 5 columns (C), and `count('*')` returns `LongType` (D). E is wrong — `groupBy().count()` names the column `count` while `groupBy().agg(F.count('*'))` names it `count(1)`, so their schemas differ. | topic3-prompt15-aggregations-grouping.md | Hard |
| 71 | B | `cache()` is lazy — it marks the DataFrame for caching but does nothing until the **next action** that forces evaluation of the cached plan materialises the partitions in Executor memory. | topic4-prompt24-performance-tuning.md | Easy |
| 72 | B | `df.unpersist()` removes all cached blocks for the DataFrame from Executor memory and disk, freeing that storage capacity. It does not delete source data or drop temp views. | topic4-prompt24-performance-tuning.md | Easy |
| 73 | A, B, C | With `MEMORY_ONLY`, partitions exceeding available memory are **dropped** (not spilled to disk) (A), it is the fastest level for data that fits in memory (B), and dropped partitions are recomputed from lineage on next access (C). D is wrong — `df.cache()` defaults to `MEMORY_AND_DISK`. E is wrong — replication requires `MEMORY_ONLY_2`. | topic4-prompt24-performance-tuning.md | Medium |
| 74 | A | Spark Memory = heap size × `spark.memory.fraction` = 10 GB × 0.6 = **6 GB**. The remaining 4 GB is reserved for User Memory and system overhead. | topic4-prompt24-performance-tuning.md | Medium |
| 75 | B | `sortWithinPartitions()` sorts rows **independently within each partition** with no shuffle — it is a narrow transformation. `orderBy()` requires a full shuffle to produce a globally ordered output across all partitions. | topic4-prompt24-performance-tuning.md | Medium |
| 76 | B | `spark.sql.autoBroadcastJoinThreshold` sets the size limit (in bytes) below which Spark automatically broadcasts a table in a join. The default is 10 MB. Setting it to `-1` disables auto-broadcasting. | topic4-prompt24-performance-tuning.md | Medium |
| 77 | A, B, C | Salting adds a random salt column to the large skewed DataFrame (A), replicates each row of the small DataFrame N times with each salt value (B), and then joins on the composite key (original key + salt) (C). D (repartitioning by the skewed key) does not fix skew. E (disabling AQE) is unnecessary. | topic4-prompt24-performance-tuning.md | Medium |
| 78 | C | Best practice is **4–5 cores per Executor**. This balances parallelism with per-Executor overhead (GC pressure, JVM startup). Very high core counts (e.g., 20+) degrade HDFS client throughput because multiple threads compete for the same HDFS client instance. | topic4-prompt24-performance-tuning.md | Hard |
| 79 | B | GC overhead OOM during a large shuffle typically means too much live data per Executor. Reducing `spark.executor.cores` limits concurrent in-flight data per JVM, increasing `spark.executor.memory` gives more heap headroom, and increasing `spark.sql.shuffle.partitions` creates smaller per-partition datasets. | topic4-prompt25-common-errors.md | Hard |
| 80 | B | `spark.sparkContext.setLogLevel('ERROR')` configures the log4j logger for the Spark context to show only ERROR messages, suppressing INFO and WARN. The Python `logging` module controls the Python driver only, not the JVM. | topic4-prompt26-debugging.md | Medium |
| 81 | B | `df.writeStream.start()` returns a **`StreamingQuery`** object. This handle exposes `.status`, `.lastProgress`, `.awaitTermination()`, and `.stop()` to monitor and control the running query. | topic5-prompt27-structured-streaming.md | Easy |
| 82 | B | The `rate` source schema is exactly `timestamp TIMESTAMP, value BIGINT`. `timestamp` is the Spark-generated event time and `value` is a monotonically increasing counter. | topic5-prompt27-structured-streaming.md | Easy |
| 83 | A, B, E | The socket source is not fault-tolerant (A — no replay on restart), only handles text data (B), and has no offset tracking (E — cannot resume after downtime). D is wrong — socket is explicitly a testing-only source. C, while true, is not the primary reason it is unsuitable for production. | topic5-prompt27-structured-streaming.md | Medium |
| 84 | B | `foreachBatch` exposes the current micro-batch as a regular PySpark DataFrame, enabling writes to **multiple sinks** in one micro-batch and access to any batch DataFrame API that is unavailable in streaming mode. | topic5-prompt27-structured-streaming.md | Medium |
| 85 | B | `availableNow=True` (Spark 3.3+) processes all available data across **multiple micro-batches** (respecting rate limits and watermarks) then stops. `once=True` processes everything in a **single micro-batch** then stops — which can cause OOM on large backlogs. | topic5-prompt27-structured-streaming.md | Medium |
| 86 | B | The `memory` sink stores results in an in-memory table named by the `queryName` option. Query the accumulated results via `spark.sql("SELECT * FROM <queryName>")` while the query is running. | topic5-prompt27-structured-streaming.md | Medium |
| 87 | A, B, D, E | Delta Lake's transaction log enables starting from any version or timestamp (A), provides exactly-once semantics with checkpointing (B), enforces schema and fails on incompatible changes (D), and can serve as both source and sink simultaneously (E). C is wrong — Delta Lake fully supports continuous incremental streaming without `trigger(once=True)`. | topic5-prompt27-structured-streaming.md | Medium |
| 88 | B | The `complete` output mode rewrites the entire updated aggregate result on every trigger. It is appropriate only for **stateful aggregation** queries (e.g., `groupBy().count()`) where consumers need the full current state. | topic5-prompt27-structured-streaming.md | Medium |
| 89 | C | The watermark cutoff = max event time − watermark delay = `12:15 − 10 min = 12:05`. The arriving event's time is `12:00`, which is earlier than the cutoff `12:05`, so it is considered **late and is dropped**. | topic5-prompt28-stateful-streaming.md | Hard |
| 90 | B | Structured Streaming queries run in a **background JVM thread managed by the Python driver process**. When the main Python thread exits, the driver process terminates, which kills the background streaming thread and terminates the query. | topic5-prompt27-structured-streaming.md | Hard |
| 91 | D | The default gRPC port for Spark Connect is **15002**. Port 4040 is the Spark UI, 7077 is the Standalone master, and 8080 is the Standalone master web UI. | topic6-prompt29-spark-connect.md | Easy |
| 92 | B | Spark Connect replaces the Py4J JVM bridge with **gRPC over HTTP/2** — client requests are logical plans serialized as Protocol Buffers, and results are returned as Apache Arrow record batches. | topic6-prompt29-spark-connect.md | Medium |
| 93 | B | In classic Spark the Driver is embedded in the application process — an app crash kills the Driver and all Tasks fail. In Spark Connect the Driver runs on the cluster independently — an app crash only severs the client connection; the Driver keeps running and the client can reconnect. | topic6-prompt29-spark-connect.md | Medium |
| 94 | A, B, C, E | Any language with a gRPC client can connect (A), no local JVM is required on the client (B), results are returned as Arrow batches (C), and multiple clients can connect to the same server (E). D is wrong — the RDD API is not available via Spark Connect. | topic6-prompt29-spark-connect.md | Medium |
| 95 | B | Spark Connect includes version negotiation in the gRPC protocol, allowing a 3.4 client to interoperate with a 3.5 server. This enables teams to upgrade the cluster and client libraries independently on different timelines. | topic6-prompt29-spark-connect.md | Hard |
| 96 | B | `ps.from_pandas(pdf)` converts a native pandas DataFrame to a `pyspark.pandas` DataFrame directly. `pdf.to_spark()` is not a standard pandas method; `pdf.pandas_api()` is a PySpark DataFrame method. | topic7-prompt30-pandas-api.md | Easy |
| 97 | B | `psdf.to_spark()` converts a `pyspark.pandas` DataFrame back to a **PySpark DataFrame** (distributed). Data is not collected to the Driver. `psdf.to_pandas()` would collect all data to the Driver. | topic7-prompt30-pandas-api.md | Medium |
| 98 | B | The Koalas project was merged into PySpark as `pyspark.pandas` in Spark 3.2. The `databricks.koalas` import still works on Databricks Runtime but is **deprecated** — users should migrate to `import pyspark.pandas as ps`. | topic7-prompt30-pandas-api.md | Medium |
| 99 | A, B, D, E | `'distributed-sequence'` generates globally unique IDs using **partition ID and within-partition row offset** (B), without a global sort (A), making it cheaper than `'sequence'` (D). It is the default `default_index_type` in `pyspark.pandas` (E). C is wrong — the partition-offset approach creates gaps between partitions, so values are not strictly sequential. | topic7-prompt30-pandas-api.md | Medium |
| 100 | B | The `'sequence'` index type generates a strictly sequential integer index by performing a **global sort (full shuffle)** across all partitions to assign monotonically increasing IDs with no gaps. On large DataFrames this shuffle is expensive, which is why `'distributed-sequence'` is the default. | topic7-prompt30-pandas-api.md | Hard |
