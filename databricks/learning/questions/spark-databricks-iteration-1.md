# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 1)

**Iteration**: 1

**Generated**: 2026-04-24

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 77 `one` / 23 `many`

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
**Topic**: driver role

**Question**:
What is the primary responsibility of the **Driver** in a Spark application?

- A) Execute Tasks on individual partitions of data
- B) Allocate CPU and memory resources to Executors
- C) Convert user code into a DAG of Stages and schedule Tasks on Executors
- D) Store cached DataFrame blocks in off-heap memory

---

### Question 2 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: SparkSession vs SparkContext

**Question**:
In Spark 2.0+, which object is the recommended **unified entry point** for DataFrame operations, Spark SQL, and structured streaming?

- A) `SparkContext`
- B) `SparkConf`
- C) `SQLContext`
- D) `SparkSession`

---

### Question 3 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: many
**Topic**: actions vs transformations

**(Select all that apply)**
Which of the following operations are **actions** that trigger Spark job execution?

- A) `df.filter(col('age') > 25)`
- B) `df.count()`
- C) `df.show()`
- D) `df.write.parquet('/output/path')`
- E) `df.withColumn('bonus', col('salary') * 0.1)`

---

### Question 4 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: narrow vs wide transformations

**Question**:
Which of the following is classified as a **narrow** transformation?

- A) `df.groupBy('dept').count()`
- B) `df.distinct()`
- C) `df.orderBy('salary')`
- D) `df.filter(col('status') == 'active')`

---

### Question 5 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DAGScheduler

**Question**:
What is the role of the **DAGScheduler** in Spark's execution model?

- A) It schedules individual Tasks on available Executor slots
- B) It converts the logical plan DAG into physical Stages and handles stage-level fault tolerance
- C) It broadcasts variables to all Executors before task execution
- D) It manages memory allocation between storage and execution regions

---

### Question 6 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: stage boundaries

**Question**:
Which of the following operations causes a new **Stage boundary** in the Spark execution plan?

- A) `df.select('id', 'name')`
- B) `df.filter(col('amount') > 100)`
- C) `df.join(other_df, on='id', how='inner')` using SortMergeJoin
- D) `df.withColumn('tax', col('price') * 0.1)`

---

### Question 7 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: tasks and partitions

**Question**:
In a given Spark Stage, how many Tasks are created?

- A) One Task per Executor
- B) One Task per CPU core across the cluster
- C) One Task per partition processed in that Stage
- D) One Task per distinct key in the data

---

### Question 8 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: lazy evaluation benefits

**(Select all that apply)**
Which of the following are benefits of Spark's **lazy evaluation** model?

- A) Catalyst can optimise and reorder operations before execution begins
- B) Multiple narrow transformations can be fused into a single pass (no intermediate data materialisation)
- C) Actions execute immediately without building a query plan
- D) Lost partitions can be recomputed from their lineage without requiring full re-scan of the source
- E) Shuffle data is always written to disk before the next transformation begins

---

### Question 9 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: shuffle partitions configuration

**Question**:
After a shuffle operation (e.g., `groupBy()` or `join()`), Spark determines the number of output partitions using which configuration property?

- A) `spark.default.parallelism`
- B) `spark.executor.cores`
- C) `spark.sql.shuffle.partitions`
- D) `spark.sql.files.maxPartitionBytes`

---

### Question 10 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: broadcast threshold configuration

**Question**:
Spark automatically uses a **BroadcastHashJoin** instead of SortMergeJoin when one table is smaller than the threshold controlled by which configuration property?

- A) `spark.sql.broadcastTimeout`
- B) `spark.sql.autoBroadcastJoinThreshold`
- C) `spark.broadcast.blockSize`
- D) `spark.sql.join.preferSortMergeJoin`

---

### Question 11 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: fault tolerance

**(Select all that apply)**
Which of the following are **true** about Spark's fault tolerance mechanism for DataFrames?

- A) Lost partitions are recomputed from their lineage (the recorded DAG of transformations)
- B) Spark replicates each partition across two Executor nodes by default
- C) The DAGScheduler re-submits a failed Stage to recover from Executor failures
- D) Calling `checkpoint()` breaks the lineage and stores a materialised snapshot to a reliable store
- E) Spark requires HDFS to enable fault tolerance

---

### Question 12 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: executor responsibilities

**Question**:
Which statement correctly describes what an **Executor** does in Spark?

- A) It orchestrates the scheduling of Stages across the cluster
- B) It executes Tasks assigned by the TaskScheduler and stores cached data blocks
- C) It reads the physical plan from the Catalyst optimizer
- D) It manages network communication between the Driver and Cluster Manager

---

### Question 13 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: wide transformations

**(Select all that apply)**
Which of the following transformations are **wide** (i.e., they cause a shuffle)?

- A) `df.repartition(10)`
- B) `df.filter(col('x') > 0)`
- C) `df.groupBy('category').sum('amount')`
- D) `df.distinct()`
- E) `df.withColumn('new_col', col('old_col') * 2)`

---

### Question 14 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: coalesce

**Scenario**:
A DataFrame has 200 partitions but after filtering only about 5% of the data remains. You want to reduce the partition count without triggering a full shuffle.

**Question**:
Which method is most appropriate?

- A) `df.repartition(10)`
- B) `df.coalesce(10)`
- C) `df.repartition(10, 'id')`
- D) `df.sortWithinPartitions('id')`

---

### Question 15 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: narrow transformations

**(Select all that apply)**
Which of the following operations are **narrow** transformations?

- A) `df.select('id', 'name')`
- B) `df.union(other_df)`
- C) `df.orderBy('created_at')`
- D) `df.withColumn('double_price', col('price') * 2)`
- E) `df.join(other_df, on='id')`

---

### Question 16 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: execution hierarchy

**Question**:
Which option correctly represents Spark's execution hierarchy from **coarsest** to **finest**?

- A) Task → Stage → Job → Application
- B) Application → Job → Stage → Task
- C) Job → Application → Stage → Task
- D) Stage → Task → Job → Application

---

### Question 17 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: stage count analysis

**Scenario**:
Consider the following code:
```python
df = spark.read.parquet('/data/sales')
df_filtered = df.filter(col('year') == 2024)
df_joined = df_filtered.join(broadcast(df_regions), on='region_id')
df_agg = df_joined.groupBy('category').sum('amount')
df_agg.write.parquet('/output')
```

**Question**:
How many Stages would this query plan generate (approximately)?

- A) 1 Stage — the broadcast join avoids all shuffles
- B) 2 Stages — one for reading/filtering/joining (broadcast avoids shuffle here), one for the groupBy shuffle
- C) 3 Stages — filter, join, and groupBy each create their own Stage
- D) 4 Stages — one per operation in the chain

---

### Question 18 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: fault tolerance recovery

**Scenario**:
While a Spark job is running, one Executor JVM crashes mid-stage, losing all in-memory partition data it held.

**Question**:
What does Spark do by default (without explicit checkpointing)?

- A) The entire job fails and the user must resubmit
- B) The Driver re-broadcasts all input data to the remaining Executors
- C) The DAGScheduler re-submits the failed Stage, recomputing lost partitions from their lineage
- D) The TaskScheduler marks failed Tasks as skipped and continues with partial results

---

### Question 19 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: garbage collection tuning

**Scenario**:
Spark application logs show frequent GC pause warnings and slow task execution. The executor heap is 8 GB.

**Question**:
Which change is most likely to reduce GC pressure on Executors?

- A) Increasing `spark.sql.shuffle.partitions` from 200 to 2000
- B) Switching from G1GC to Serial GC for Executor JVMs
- C) Using `persist(StorageLevel.MEMORY_AND_DISK_SER)` for frequently accessed DataFrames to reduce live object count in the heap
- D) Reducing `spark.executor.memory` to force fewer objects per GC cycle

---

### Question 20 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: many
**Topic**: persist storage levels

**(Select all that apply)**
Which `StorageLevel` options store data **in memory with automatic disk spillover** when memory is insufficient?

- A) `MEMORY_ONLY`
- B) `MEMORY_AND_DISK`
- C) `MEMORY_AND_DISK_SER`
- D) `DISK_ONLY`
- E) `OFF_HEAP`

---

### Question 21 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: temp view creation

**Question**:
Which method creates or replaces a session-scoped temporary view **without** raising an error if the view already exists?

- A) `df.createTempView('sales')`
- B) `df.createOrReplaceTempView('sales')`
- C) `df.createGlobalTempView('sales')`
- D) `spark.catalog.createView('sales', df)`

---

### Question 22 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.sql() return type

**Question**:
What does `spark.sql("SELECT category, SUM(amount) FROM sales GROUP BY category")` return?

- A) A Python list of Row objects
- B) A Pandas DataFrame
- C) A PySpark DataFrame
- D) A Python dictionary keyed by column name

---

### Question 23 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: global temp view reference

**Question**:
A global temporary view named `events_global` was created with `df.createOrReplaceGlobalTempView('events_global')`. How must it be referenced in a SQL query?

- A) `SELECT * FROM events_global`
- B) `SELECT * FROM temp.events_global`
- C) `SELECT * FROM global_temp.events_global`
- D) `SELECT * FROM spark_catalog.events_global`

---

### Question 24 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: selectExpr usage

**Question**:
Which expression correctly uses `selectExpr()` to apply a 10% salary increase and alias the result?

- A) `df.selectExpr(col('salary') * 1.1).alias('new_salary')`
- B) `df.selectExpr('salary * 1.1 AS new_salary')`
- C) `df.selectExpr(F.expr('salary * 1.1').alias('new_salary'))`
- D) `df.selectExpr({'salary': 'new_salary'})`

---

### Question 25 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: temp view lifetime

**Question**:
What is the lifetime of a **session-scoped** temporary view?

- A) It persists until the Spark application ends
- B) It persists until the cluster is restarted
- C) It persists only for the current SparkSession; it is dropped automatically when that session ends
- D) It persists for 24 hours after creation

---

### Question 26 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: catalog API

**Question**:
Which Catalog API call lists all tables and temporary views available in the current database?

- A) `spark.catalog.listDatabases()`
- B) `spark.catalog.listTables()`
- C) `spark.catalog.listColumns('default')`
- D) `spark.catalog.showTables()`

---

### Question 27 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: window function partitionBy

**Scenario**:
You need to compute a running total of `sales` partitioned by `region` and ordered by `date`.

**Question**:
Which window specification is correct?

- A) `Window.partitionBy('date').orderBy('region')`
- B) `Window.orderBy('date')`
- C) `Window.partitionBy('region').orderBy('date')`
- D) `Window.groupBy('region').orderBy('date')`

---

### Question 28 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: built-in string functions

**(Select all that apply)**
Which of the following are valid **string manipulation** functions available in `pyspark.sql.functions`?

- A) `F.upper(col('name'))`
- B) `F.trim(col('name'))`
- C) `F.split(col('email'), '@')`
- D) `F.str_replace(col('name'), 'a', 'b')`
- E) `F.regexp_replace(col('name'), '[aeiou]', '*')`

---

### Question 29 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: rank vs dense_rank

**Question**:
What is the difference between `rank()` and `dense_rank()` in window functions?

- A) `rank()` assigns contiguous integers with no gaps; `dense_rank()` skips numbers after ties
- B) `dense_rank()` assigns contiguous integers with no gaps after ties; `rank()` skips numbers after ties
- C) They are identical — both skip numbers after ties
- D) `rank()` starts from 0; `dense_rank()` starts from 1

---

### Question 30 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: window frame

**Question**:
What does the following window specification define?
```python
Window.partitionBy('dept').orderBy('hire_date').rowsBetween(Window.unboundedPreceding, Window.currentRow)
```

- A) A sliding window of exactly 2 rows around the current row
- B) A cumulative frame from the first row of the partition to the current row (running total frame)
- C) All rows in the partition regardless of order
- D) All rows after the current row to the end of the partition

---

### Question 31 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Catalyst optimizer operations

**(Select all that apply)**
Which of the following operations are performed by the **Catalyst Optimizer**?

- A) Parsing SQL strings into an unresolved logical plan
- B) Resolving column references against the catalog schema
- C) Applying rule-based optimizations such as predicate pushdown and constant folding
- D) Scheduling individual Tasks on available Executor slots
- E) Generating a physical execution plan from the optimized logical plan

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: AQE features

**(Select all that apply)**
Which optimizations are enabled by **Adaptive Query Execution (AQE)**?

- A) Dynamically coalescing small shuffle partitions into fewer, larger ones
- B) Dynamically switching from SortMergeJoin to BroadcastHashJoin based on runtime statistics
- C) Automatically replacing Python UDFs with native Catalyst expressions
- D) Handling data skew by splitting severely skewed shuffle partitions
- E) Rewriting SQL subqueries into correlated joins

---

### Question 33 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cost-based optimizer

**Question**:
The **Cost-Based Optimizer (CBO)** uses table statistics to choose optimal join ordering and strategies. How are these statistics collected?

- A) They are collected automatically after every write operation
- B) By running `ANALYZE TABLE <table> COMPUTE STATISTICS` or the equivalent DataFrame API call
- C) By reading file sizes from the underlying storage system
- D) By running `EXPLAIN COST` before the actual query

---

### Question 34 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: expr() usage

**(Select all that apply)**
Which of the following correctly demonstrate the use of `expr()` or `selectExpr()` in PySpark?

- A) `df.withColumn('adjusted', F.expr('salary * 1.1'))`
- B) `df.filter(F.expr('salary > 50000 AND dept = "Engineering"'))`
- C) `df.select(F.expr('salary AS annual_pay'))`
- D) `df.withColumn('tax', F.expr(col('salary') * 0.2))`
- E) `df.groupBy(F.expr('YEAR(hire_date)')).count()`

---

### Question 35 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: higher-order functions

**Question**:
Which category of built-in functions in Spark is optimized for working with **arrays, maps, and nested structures** (e.g., `transform()`, `filter()`, `aggregate()`)?

- A) Aggregate functions
- B) Window functions
- C) Higher-order / collection functions
- D) String functions

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: tableExists catalog

**Question**:
Which of the following checks whether a temporary view named `orders` exists in the current session?

- A) `spark.sql("SHOW VIEWS LIKE 'orders'")`
- B) `spark.catalog.tableExists('orders')`
- C) `'orders' in spark.catalog.listTables()`
- D) `spark.catalog.hasTable('orders')`

---

### Question 37 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: row_number vs rank vs dense_rank scenario

**Scenario**:
Four employees in the Sales department have salaries: 90000, 75000, 75000, 60000. A window function assigns a ranking to each employee ordered by `salary DESC`. After the two tied employees (75000), the next employee (60000) receives the value `4` (not `3`).

**Question**:
Which window function was used?

- A) `dense_rank()`
- B) `rank()`
- C) `row_number()`
- D) `ntile(4)`

---

### Question 38 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: AQE coalescing scenario

**Scenario**:
A query generates 200 shuffle partitions but the total data is only 1 GB. Most tasks complete in milliseconds and produce tiny output files.

**Question**:
Which AQE feature and configuration resolves this inefficiency?

- A) Enable `spark.sql.adaptive.skewJoin.enabled` and tune `spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes`
- B) Enable `spark.sql.adaptive.enabled` and ensure `spark.sql.adaptive.coalescePartitions.enabled = true` to automatically merge small post-shuffle partitions
- C) Disable `spark.sql.adaptive.enabled` and manually set `spark.sql.shuffle.partitions = 5`
- D) Enable `spark.sql.adaptive.localShuffleReader.enabled` to read all shuffle data without a remote exchange

---

### Question 39 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: predicate pushdown limitation scenario

**Scenario**:
```python
df = spark.read.parquet('/data/events')
df2 = df.withColumn('processed_date', F.to_date(col('raw_ts')))
result = df2.filter(col('processed_date') == '2024-01-01')
```

**Question**:
Why might this query **NOT** benefit from predicate pushdown into the Parquet reader?

- A) Parquet files do not support predicate pushdown at all
- B) The filter is on a derived column (`processed_date`), not the original source column; Spark cannot push this filter below the `withColumn` step to the FileScan
- C) `to_date()` is not a supported pushdown function in the Parquet connector
- D) The filter must use `F.lit()` to wrap the date string for pushdown to work

---

### Question 40 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: window cumulative count scenario

**Scenario**:
```python
w = Window.partitionBy('dept').orderBy('hire_date')
df.withColumn('running_headcount', F.count('*').over(w))
```

**Question**:
What does `running_headcount` represent for each row?

- A) The total number of employees across the entire DataFrame
- B) The number of employees hired after the current employee in the same department
- C) The cumulative count of employees hired on or before the current employee's `hire_date` within the same department
- D) The count of distinct departments in the DataFrame

---

### Question 41 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: select

**Question**:
Which of the following selects the columns `id`, `name`, and `email` from a DataFrame `df`?

- A) `df.get(['id', 'name', 'email'])`
- B) `df.select('id', 'name', 'email')`
- C) `df.pick('id', 'name', 'email')`
- D) `df.columns(['id', 'name', 'email'])`

---

### Question 42 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: filter and where equivalence

**Question**:
Which two methods are **exact aliases** for filtering rows in a PySpark DataFrame?

- A) `select()` and `where()`
- B) `filter()` and `where()`
- C) `drop()` and `filter()`
- D) `where()` and `dropDuplicates()`

---

### Question 43 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: withColumn behaviour

**Question**:
What does the following code do?
```python
df.withColumn('discounted_price', col('price') * 0.9)
```

- A) Modifies the `price` column in place by applying a 10% discount
- B) Adds a new column `discounted_price` equal to 90% of `price`; if `discounted_price` already exists it is replaced
- C) Renames the `price` column to `discounted_price`
- D) Filters rows where `price` is 10% above the average

---

### Question 44 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: dropDuplicates subset

**Question**:
Which method removes duplicate rows from a DataFrame based on a **subset of columns**?

- A) `df.distinct()`
- B) `df.drop('id')`
- C) `df.dropDuplicates(['email', 'department'])`
- D) `df.deduplicate(subset=['email', 'department'])`

---

### Question 45 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: fillna with dictionary

**Question**:
Which method fills `NULL` values in `salary` with `0` and `NULL` values in `department` with `'Unknown'` in a single call?

- A) `df.fillna({'salary': 0, 'department': 'Unknown'})`
- B) `df.na.fill(0).na.fill('Unknown')`
- C) `df.replace(None, {'salary': 0, 'department': 'Unknown'})`
- D) `df.withColumn('salary', col('salary').na.fill(0))`

---

### Question 46 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: reading CSV with header

**Question**:
Which option instructs Spark to treat the **first row** of a CSV file as column names when reading?

- A) `.option('firstRowHeader', True)`
- B) `.option('columns', 'first_row')`
- C) `.option('header', True)`
- D) `.option('columnNames', True)`

---

### Question 47 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: withColumnRenamed

**Question**:
Which of the following correctly renames the column `emp_id` to `employee_id` in a DataFrame?

- A) `df.rename('emp_id', 'employee_id')`
- B) `df.withColumnRenamed('emp_id', 'employee_id')`
- C) `df.select(col('emp_id').name('employee_id'))`
- D) `df.renameColumn('emp_id', 'employee_id')`

---

### Question 48 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: sort vs orderBy

**Question**:
In PySpark's DataFrame API, what is the relationship between `sort()` and `orderBy()`?

- A) They differ — `sort()` performs a local per-partition sort while `orderBy()` performs a global sort
- B) They are exact aliases — both perform a global sort of the entire DataFrame
- C) They differ — `sort()` uses Timsort while `orderBy()` uses merge sort
- D) They differ — `sort()` returns a sorted RDD while `orderBy()` returns a sorted DataFrame

---

### Question 49 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: aggregate functions and NULLs

**(Select all that apply)**
Which of the following aggregate functions **ignore NULL values** in their computation?

- A) `F.sum('salary')`
- B) `F.avg('salary')`
- C) `F.count('*')`
- D) `F.max('salary')`
- E) `F.count(col('salary'))`

---

### Question 50 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: join types

**(Select all that apply)**
Which join types will **include rows from the left DataFrame even when there is no matching row** in the right DataFrame?

- A) `inner`
- B) `left` (left outer)
- C) `left_anti`
- D) `left_semi`
- E) `full` (full outer)

---

### Question 51 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: union by position vs by name

**Scenario**:
```python
df_a = spark.createDataFrame([(1, 'Alice')], ['id', 'name'])
df_b = spark.createDataFrame([('Bob', 2)], ['name', 'id'])
result = df_a.union(df_b)
result.show()
```

**Question**:
What is the value in the `id` column for the second row?

- A) `2` — `union()` aligns columns by name
- B) `'Bob'` — `union()` aligns by position; the first positional column of `df_b` is `'Bob'`
- C) `null` — a type mismatch between string and integer causes a null
- D) An `AnalysisException` is raised because the column names differ

---

### Question 52 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: dropna behaviour

**(Select all that apply)**
Which of the following are **true** about `dropna()` in PySpark?

- A) `df.dropna()` and `df.na.drop()` are functionally equivalent
- B) The default value of the `how` parameter is `'any'`
- C) `dropna()` also removes rows that contain NaN values in floating-point columns
- D) `dropna(thresh=3)` drops rows that have fewer than 3 non-null values
- E) `dropna(subset=['col1', 'col2'])` drops rows where `col1` or `col2` is null

---

### Question 53 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: StructType / StructField

**Scenario**:
```python
schema = StructType([
    StructField('id', LongType(), nullable=False),
    StructField('name', StringType(), nullable=True),
])
```

**Question**:
Which statement is **true** about the `id` column?

- A) `id` is a 32-bit integer that cannot contain null
- B) `id` is a 64-bit integer that cannot contain null
- C) `id` is a 64-bit integer that can contain null
- D) `id` is a string column that cannot contain null

---

### Question 54 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: write modes

**(Select all that apply)**
Which write modes do **not** raise an error when the destination path already contains data?

- A) `'overwrite'`
- B) `'append'`
- C) `'ignore'`
- D) `'error'`
- E) `'errorIfExists'`

---

### Question 55 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: repartition vs coalesce

**(Select all that apply)**
Which of the following statements about `repartition()` and `coalesce()` are **correct**?

- A) `repartition()` always causes a full shuffle regardless of whether the partition count increases or decreases
- B) `coalesce()` avoids a full shuffle and can only **decrease** the number of partitions
- C) `repartition(n, col)` distributes data by the hash of the specified column
- D) `coalesce(1)` produces a single partition while minimising shuffle overhead compared to `repartition(1)`
- E) Both `repartition()` and `coalesce()` can increase the partition count

---

### Question 56 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: NULL vs NaN detection

**Question**:
Which function detects a **NaN** (Not a Number) value in a floating-point column named `score`?

- A) `col('score').isNull()`
- B) `F.isnull(col('score'))`
- C) `F.isnan(col('score'))`
- D) `col('score').isNaN()`

---

### Question 57 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: subtract vs intersect

**Question**:
What does `df_a.subtract(df_b)` return?

- A) Rows that appear in both `df_a` and `df_b` (deduplicated)
- B) Rows in `df_a` that do NOT appear in `df_b`, with automatic deduplication of the result
- C) Rows in `df_b` that do NOT appear in `df_a`
- D) The symmetric difference — rows that appear in exactly one of the two DataFrames

---

### Question 58 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: nested struct access

**(Select all that apply)**
A DataFrame has a nested struct column `address` containing a `city` field. Which expressions correctly access the `city` value?

- A) `df.select('address.city')`
- B) `df.select(col('address').getField('city'))`
- C) `df.select(col('address')['city'])`
- D) `df.select(col('address.city'))`
- E) `df.select(F.struct(col('address'), 'city'))`

---

### Question 59 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: UDF registration for Spark SQL

**Question**:
What additional step is required to use a Python UDF in **Spark SQL** (i.e., inside a `spark.sql()` call)?

- A) The UDF must be compiled to a JAR and added via `spark.addJar()`
- B) The UDF must be registered with `spark.udf.register('function_name', my_udf, returnType)`
- C) The UDF must be decorated with `@F.udf` before it can be used in SQL
- D) No extra step — Python UDFs defined via `F.udf()` are automatically available in SQL

---

### Question 60 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: collect_list vs collect_set

**(Select all that apply)**
Which of the following statements about `collect_list()` and `collect_set()` are **correct**?

- A) `collect_list()` preserves duplicates; `collect_set()` removes duplicates
- B) Both functions return a column of `ArrayType`
- C) `collect_set()` guarantees a deterministic ordering of elements in the array
- D) The order of elements in `collect_list()` depends on partition processing order and may vary between runs
- E) `collect_set()` is equivalent to calling `collect_list()` followed by `.distinct()`

---

### Question 61 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pivot result schema

**Scenario**:
```python
df.groupBy('region').pivot('quarter', ['Q1', 'Q2', 'Q3', 'Q4']).sum('revenue')
```

**Question**:
How many columns does the resulting DataFrame have?

- A) 4 (one per quarter)
- B) 5 (`region` + 4 quarter columns)
- C) 5 only if `region` has a single distinct value
- D) 4 × the number of distinct regions

---

### Question 62 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cube vs rollup

**Question**:
How does `cube()` differ from `rollup()` when called with two column arguments?

- A) `cube()` is only valid for date/time columns; `rollup()` works with any column type
- B) `cube()` computes aggregates for **all combinations** of the columns (including grand total); `rollup()` computes only hierarchical left-to-right subtotals
- C) `rollup()` produces the same output as `cube()` but is faster by using a different algorithm
- D) `cube()` requires specifying expected group values; `rollup()` discovers them dynamically

---

### Question 63 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: countDistinct

**Question**:
Which function counts the number of **distinct non-null** values in a column within a `groupBy().agg()` call?

- A) `F.count(F.distinct(col('product')))`
- B) `F.distinct_count('product')`
- C) `F.countDistinct('product')`
- D) `F.count('product').distinct()`

---

### Question 64 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: forcing broadcast join

**(Select all that apply)**
Which approaches can force Spark to use a **BroadcastHashJoin** for a specific join?

- A) Wrapping the smaller DataFrame with `broadcast(small_df)` inside the join call
- B) Setting `spark.sql.autoBroadcastJoinThreshold` to a value larger than the smaller DataFrame's estimated size
- C) Calling `small_df.hint('broadcast')` before the join
- D) Calling `.cache()` on the smaller DataFrame before the join
- E) Adding the `/*+ BROADCAST(small_table) */` SQL hint in a `spark.sql()` query

---

### Question 65 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: union positional alignment pitfall

**Scenario**:
```python
schema_a = ['customer_id', 'email', 'score']
schema_b = ['email', 'customer_id', 'score']

df_a = spark.createDataFrame([(101, 'alice@x.com', 9.5)], schema_a)
df_b = spark.createDataFrame([('bob@x.com', 102, 8.0)], schema_b)

combined = df_a.union(df_b)
combined.show()
```

**Question**:
What is the value in the `customer_id` column for the second row of `combined`?

- A) `102` — `union()` aligns columns by name
- B) `'bob@x.com'` — `union()` aligns by position; the first positional column of `df_b` is `'bob@x.com'`
- C) `null` — a type mismatch between string and integer causes a null
- D) An `AnalysisException` is raised because column names differ

---

### Question 66 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: NaN not handled by fillna

**Scenario**:
```python
df = spark.createDataFrame([(1, float('nan')), (2, None), (3, 5.0)], ['id', 'value'])
result = df.fillna(0.0, subset=['value'])
result.show()
```

**Question**:
What is the `value` for `id = 1` after this operation?

- A) `0.0` — `fillna()` replaces both NULL and NaN with the fill value
- B) `NaN` — `fillna()` only replaces NULL values, not NaN values
- C) `null` — NaN is silently converted to null before `fillna()` processes it
- D) An error is raised because `float('nan')` is not a supported Spark value

---

### Question 67 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: map of arrays access

**Scenario**:
A DataFrame has a column `profile` of type `MAP<STRING, ARRAY<STRING>>`. You need to access the **first element** of the array stored under the key `'skills'`.

**Question**:
Which expression is correct?

- A) `col('profile')['skills'][0]`
- B) `col('profile').getItem('skills').getItem(0)`
- C) `col('profile.skills')[0]`
- D) Both A and B are correct

---

### Question 68 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: UDF NULL handling

**Scenario**:
```python
@F.udf(returnType=StringType())
def classify(score):
    if score is None:
        return None
    return 'high' if score > 80 else 'low'
```
The `score` column is `DoubleType`. The developer calls `df.withColumn('class', classify(col('score')))`.

**Question**:
What is **true** about this UDF and its NULL handling?

- A) Spark raises an error because the input type is not declared in the UDF signature
- B) The UDF correctly handles NULL; Python `None` is passed for SQL NULL values, and returning `None` maps back to SQL NULL
- C) The UDF will fail at runtime because `score > 80` cannot compare `None` with an integer
- D) The `@F.udf` decorator requires explicit input type declarations to handle NULLs correctly

---

### Question 69 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: partitionBy write directory structure

**Scenario**:
```python
df.write \
  .partitionBy('year', 'month') \
  .mode('overwrite') \
  .parquet('/output/events')
```

**Question**:
What directory structure will Spark create?

- A) A single flat directory `/output/events` with one file per partition number
- B) A nested structure such as `/output/events/year=2024/month=01/part-*.parquet`
- C) A flat structure with files named `/output/events/2024_01_part-*.parquet`
- D) An error, because `partitionBy` and `overwrite` mode cannot be combined

---

### Question 70 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: multiple aggregation schema

**Scenario**:
```python
result = df.groupBy('dept') \
           .agg(
               F.avg('salary').alias('avg_salary'),
               F.stddev('salary').alias('std_salary'),
               F.count('*').alias('headcount')
           )
```

**Question**:
What is the schema of `result`?

- A) `dept: STRING, avg_salary: DOUBLE, std_salary: DOUBLE, headcount: BIGINT`
- B) `dept: STRING, avg_salary: DOUBLE, std_salary: DOUBLE, headcount: INT`
- C) `avg_salary: DOUBLE, std_salary: DOUBLE, headcount: BIGINT`
- D) `dept: STRING, agg_col: STRUCT<avg_salary: DOUBLE, std_salary: DOUBLE, headcount: BIGINT>`

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark UI URL

**Question**:
What is the default URL to access the Spark UI when running in **local mode** and the application is currently active?

- A) `http://localhost:8080`
- B) `http://localhost:18080`
- C) `http://localhost:4040`
- D) `http://localhost:7077`

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: explain() default output

**Question**:
What does calling `df.explain()` with no arguments display?

- A) The unresolved logical plan
- B) The optimised logical plan
- C) The physical execution plan only
- D) Both the logical and physical plans side by side

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SortMergeJoin vs BroadcastHashJoin

**Scenario**:
A query plan shows `SortMergeJoin` even though one of the joined tables has only 10 MB of data. `spark.sql.autoBroadcastJoinThreshold` is set to `10MB`.

**Question**:
What is the most likely cause?

- A) `SortMergeJoin` is always used when joining more than two tables
- B) Spark cannot use `BroadcastHashJoin` on non-equi joins (range-based conditions)
- C) Spark could not determine the table's size statistically and conservatively fell back to `SortMergeJoin`
- D) `BroadcastHashJoin` is only available in cluster mode, not local mode

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: shuffle partitions sizing

**Scenario**:
A Spark job aggregates a 500 MB dataset. The default `spark.sql.shuffle.partitions = 200` results in many tiny (~2.5 MB) tasks that finish in milliseconds.

**Question**:
What is the most appropriate adjustment?

- A) Increase shuffle partitions to 2000 to give each task less data
- B) Reduce shuffle partitions to a smaller number (e.g., 10–20) to produce reasonably-sized partitions
- C) Set `spark.sql.adaptive.enabled = false` to prevent Spark from changing partition counts
- D) Add `df.coalesce(200)` before the aggregation to reduce input partitions

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: many
**Topic**: data skew indicators

**(Select all that apply)**
Which observations in the Spark UI **Tasks** tab most clearly indicate **data skew** in a shuffle stage?

- A) The maximum task duration is substantially longer (10–100×) than the median task duration in the same stage
- B) All tasks complete within a similar time window
- C) A small number of tasks process significantly more shuffle read bytes than the majority of tasks
- D) All stages show identical GC time percentages
- E) The shuffle write phase produces more output files than the number of partitions

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: many
**Topic**: AQE problem resolution

**(Select all that apply)**
Which of the following problems can **Adaptive Query Execution (AQE)** address automatically at runtime?

- A) Merging many small post-shuffle partitions into fewer, larger partitions
- B) Switching a `SortMergeJoin` to a `BroadcastHashJoin` when a build side is discovered to be small
- C) Replacing Python UDFs with equivalent native Catalyst expressions
- D) Splitting a severely skewed shuffle partition into multiple smaller sub-partitions
- E) Automatically caching DataFrames that are reused multiple times

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: explain extended

**Question**:
What additional information does `df.explain('extended')` provide compared to `df.explain()`?

- A) The generated Java bytecode from WholeStageCodegen
- B) All four plan stages: Unresolved Logical → Analyzed Logical → Optimized Logical → Physical
- C) Estimated row counts and byte sizes for each plan node
- D) A visual graph of the DAG with stage boundaries highlighted

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: many
**Topic**: predicate pushdown

**(Select all that apply)**
Which of the following statements about **predicate pushdown** in Spark are correct?

- A) Predicate pushdown reduces the amount of data physically read from storage by filtering at the source
- B) Predicate pushdown is only supported for Parquet and Delta Lake — not for CSV or JSON
- C) Filters on columns derived from transformations (e.g., computed columns) cannot be pushed down to the FileScan
- D) The `PushedFilters` list in a `FileScan` node shows which filters were successfully pushed to the reader
- E) Predicate pushdown for supported formats is enabled by default

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: physical plan analysis

**Scenario**:
A query's physical plan contains the following nodes (bottom to top):
```
*(1) FileScan parquet [...]  PushedFilters: []
*(1) Filter (status#3 = active)
Exchange hashpartitioning(dept#1, 200), ENSURE_REQUIREMENTS
*(2) HashAggregate(...)
```

**Question**:
What performance problem does this plan reveal?

- A) The aggregation is running on only one Executor because of `hashpartitioning`
- B) The `Filter` is NOT being pushed down to the file reader — all data is read from Parquet first, then filtered in Spark memory
- C) The `FileScan` is reading too many partitions because `PushedFilters` is empty
- D) `HashAggregate` indicates the aggregation is approximate and may be inaccurate

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: driver OOM on collect

**Scenario**:
A Spark job calls `df.collect()` to retrieve a large result set and the Driver throws `java.lang.OutOfMemoryError: Java heap space`.

**Question**:
What is the root cause and the correct resolution?

- A) Executor heap is exhausted during serialisation; increase `spark.executor.memory`
- B) The entire result is materialised in the **Driver** JVM; increase `spark.driver.memory` or reduce the dataset before calling `collect()`
- C) The shuffle buffer overflows during the exchange before collection; increase `spark.shuffle.file.buffer`
- D) The default GC algorithm is pausing too frequently on the Driver; switch to G1GC

---

### Question 81 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: isStreaming property

**Question**:
Which attribute of a DataFrame indicates that it is a **streaming** (unbounded) DataFrame?

- A) `df.isStreaming`
- B) `df.streaming`
- C) `df.schema.isStreaming`
- D) `type(df) == StreamingDataFrame`

---

### Question 82 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: output modes

**Question**:
Which output mode writes **only newly appended rows** since the last micro-batch and is compatible with stateless streaming queries?

- A) `complete`
- B) `update`
- C) `append`
- D) `incremental`

---

### Question 83 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: many
**Topic**: trigger types that stop

**(Select all that apply)**
Which trigger options cause a Structured Streaming query to process all available data and **stop automatically** upon completion?

- A) `trigger(processingTime='0 seconds')`
- B) `trigger(once=True)`
- C) `trigger(availableNow=True)` (Spark 3.3+)
- D) `trigger(continuous='1 second')`
- E) No trigger argument (default behavior)

---

### Question 84 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: file source schema requirement

**Question**:
When reading from a **file-based streaming source** (e.g., a CSV or JSON directory), which of the following is **mandatory**?

- A) Setting `.option('maxFilesPerTrigger', 1)` to limit batch size
- B) Providing an explicit schema; Spark cannot infer schema from a file streaming source
- C) Using Parquet format; text-based formats are not supported for streaming
- D) Registering the input directory as a global temp view before starting the stream

---

### Question 85 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: checkpoint purpose

**Question**:
Why is a **checkpoint location** essential for production Structured Streaming applications?

- A) It stores the physical execution plan so it does not need to be recomputed on restart
- B) It stores query progress (processed offsets) and operator state, enabling the query to resume from exactly where it stopped after a failure
- C) It acts as an in-memory buffer for micro-batch data before processing begins
- D) It is required only for stateful operations such as windowed aggregations

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: watermark

**Question**:
In Structured Streaming, what is the primary purpose of a **watermark**?

- A) It limits the maximum number of records processed per micro-batch
- B) It defines a lateness threshold for event-time data, allowing Spark to safely discard old state once a window is guaranteed to be complete
- C) It sets the output trigger interval for time-based micro-batches
- D) It marks a column as the event-time timestamp without affecting late data handling

---

### Question 87 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: many
**Topic**: output mode validity

**(Select all that apply)**
Which output mode / operation combinations are **valid** in Structured Streaming?

- A) `append` mode with a stateless `filter()` query
- B) `complete` mode with a `groupBy().count()` aggregation
- C) `append` mode with a `groupBy().count()` aggregation and **no watermark**
- D) `update` mode with a `groupBy().count()` aggregation
- E) `complete` mode with a stateless `select()` query (no aggregation)

---

### Question 88 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Kafka source schema

**Question**:
When reading from a Kafka source in Structured Streaming, what is the data type of the `value` field in the resulting DataFrame?

- A) `StringType`
- B) `BinaryType`
- C) `StructType` containing the deserialized JSON fields
- D) `ArrayType(ByteType)`

---

### Question 89 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: watermark append mode emission

**Scenario**:
```python
events = df.withWatermark('timestamp', '10 minutes') \
           .groupBy(F.window('timestamp', '5 minutes')) \
           .count()
events.writeStream.outputMode('append').start()
```

**Question**:
When will a window's count be **emitted to the output sink** using `append` mode?

- A) Immediately when the first event in the window arrives
- B) At the end of each micro-batch regardless of late data
- C) After the watermark advances past the window's end time, guaranteeing no more late data can belong to that window
- D) Only when `trigger(once=True)` is used

---

### Question 90 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: stateful streaming and checkpoint failure

**Scenario**:
A streaming query uses `dropDuplicates()` on an event ID column to deduplicate events. The query has been running for weeks **without a checkpoint location configured**. After an application failure and restart, what problem occurs?

- A) The query fails with `StreamingQueryException` because deduplication requires AQE to be enabled
- B) The previously seen event IDs are lost; the restarted query re-processes events from the retained Kafka offset and emits duplicates that were already output before the failure
- C) Spark automatically persists deduplication state to cloud storage even without an explicit checkpoint
- D) The query restarts from the very beginning of the Kafka topic, not from the last committed offset

---

### Question 91 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark Connect URL scheme

**Question**:
What URL scheme is used to connect to a **Spark Connect** server?

- A) `spark://`
- B) `sc://`
- C) `grpc://`
- D) `connect://`

---

### Question 92 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect limitations

**Question**:
Which of the following is **NOT available** when using Spark Connect (as of Spark 3.4)?

- A) The DataFrame API (`df.filter()`, `df.select()`, etc.)
- B) `spark.sql()` queries
- C) The RDD API and direct access to `SparkContext`
- D) User-defined functions (UDFs)

---

### Question 93 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: communication protocol

**Question**:
Spark Connect communicates between the client and server using which technologies?

- A) HTTP/1.1 with JSON payloads for both plans and results
- B) gRPC over HTTP/2 for logical plans (Protocol Buffers); Apache Arrow record batches for result data transfer
- C) WebSocket for streaming results and REST for plan submission
- D) Py4J JVM socket bridge, identical to classic Spark

---

### Question 94 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect stability version

**Question**:
In which Apache Spark release did **Spark Connect** graduate from preview to **stable**?

- A) Spark 3.0
- B) Spark 3.2
- C) Spark 3.3
- D) Spark 3.4

---

### Question 95 — Spark Connect

**Difficulty**: Hard
**Answer Type**: one
**Topic**: RDD migration to Spark Connect

**Scenario**:
A PySpark application currently uses:
```python
sc = spark.sparkContext
rdd = sc.textFile('/data/raw.txt')
word_counts = rdd.flatMap(lambda l: l.split()) \
                 .map(lambda w: (w, 1)) \
                 .reduceByKey(lambda a, b: a + b)
word_counts.collect()
```
This code is migrated to use `SparkSession.remote()`.

**Question**:
What change is **required** for the code to work via Spark Connect?

- A) Replace `sc.textFile()` with `spark.read.text()` and rewrite the RDD operations as DataFrame transformations, since the RDD API is not supported via Spark Connect
- B) Add `spark.conf.set('spark.connect.rdd.enabled', True)` to enable RDD support
- C) Wrap `sc` with `RemoteSparkContext` before calling `textFile()`
- D) No changes needed — Spark Connect proxies RDD calls transparently to the server

---

### Question 96 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: import statement

**Question**:
Which import statement is correct for using the **Pandas API on Spark** (distributed execution on the cluster)?

- A) `import pandas as ps`
- B) `from pyspark.sql import pandas`
- C) `import pyspark.pandas as ps`
- D) `from pyspark import pandas_api as ps`

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: PySpark to pyspark.pandas conversion

**Question**:
Which method converts a **PySpark DataFrame** (`sdf`) into a `pyspark.pandas` DataFrame?

- A) `sdf.to_pandas()`
- B) `sdf.pandas_api()`
- C) `ps.from_spark(sdf)`
- D) `ps.DataFrame(sdf)`

---

### Question 98 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: to_pandas() risk

**Question**:
Why is calling `psdf.to_pandas()` on a large `pyspark.pandas` DataFrame potentially dangerous in production?

- A) It requires a full shuffle of the DataFrame before the conversion
- B) It converts to a PySpark DataFrame first, which re-partitions all data
- C) It collects **all data to the Driver node's memory**, which can cause an OutOfMemoryError for large DataFrames
- D) It acquires a cluster-wide lock during conversion, blocking other applications

---

### Question 99 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: row order in pyspark.pandas

**Scenario**:
A developer calls `psdf.sort_values('score')` and notices that on repeated runs the result order differs for rows with equal scores.

**Question**:
Why does this happen?

- A) `sort_values()` in `pyspark.pandas` uses a non-deterministic random tiebreaker by default
- B) Distributed DataFrames have no guaranteed row order; ties may be resolved differently across partitions in different runs
- C) The Pandas API on Spark does not support `sort_values()` — it silently returns the original order
- D) `sort_values()` performs an approximate sort in the Pandas API on Spark

---

### Question 100 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: one
**Topic**: default_index_type performance

**Scenario**:
A `pyspark.pandas` DataFrame is used with the default index type `'distributed'`. A developer switches to `'sequence'` and notices a significant performance degradation on large DataFrames.

**Question**:
Why does the `'sequence'` index type perform worse than `'distributed'`?

- A) `'sequence'` serializes the entire DataFrame to the Driver before generating the index
- B) `'sequence'` triggers a **global sort** across all partitions to produce sequential integers, which requires a full shuffle
- C) `'sequence'` disables WholeStageCodegen optimization, forcing row-by-row processing
- D) `'sequence'` generates a new column using a UDF, which bypasses Catalyst optimization

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | C | The Driver's job is to convert user code into a DAG of Stages and schedule Tasks on Executors via the TaskScheduler. Executors run Tasks; the Cluster Manager handles resource allocation. | topic1-prompt1-spark-architecture.md | Easy |
| 2 | D | `SparkSession` is the unified entry point introduced in Spark 2.0, replacing the separate `SparkContext`, `SQLContext`, and `HiveContext`. | topic1-prompt1-spark-architecture.md | Easy |
| 3 | B, C, D | `count()`, `show()`, and `write` are actions that trigger execution. `filter()` and `withColumn()` are transformations — they are lazy and do not trigger a job. | topic1-prompt3-lazy-evaluation.md | Easy |
| 4 | D | `filter()` is a narrow transformation — each output partition depends on exactly one input partition, with no shuffle required. `groupBy`, `distinct`, and `orderBy` all cause shuffles. | topic1-prompt3-lazy-evaluation.md | Easy |
| 5 | B | The DAGScheduler converts the logical plan into physical Stages by splitting at shuffle boundaries, and handles stage-level fault tolerance by re-submitting failed stages. | topic1-prompt1-spark-architecture.md | Medium |
| 6 | C | A `SortMergeJoin` requires both sides to be sorted by the join key, which involves a shuffle — creating a new Stage boundary. `select`, `filter`, and `withColumn` are all narrow operations. | topic1-prompt4-shuffling-performance.md | Medium |
| 7 | C | Spark creates one Task per partition in a given Stage. If a stage processes 100 partitions, 100 Tasks are launched — they may run in parallel across available Executor cores. | topic1-prompt1-spark-architecture.md | Medium |
| 8 | A, B, D | Lazy evaluation lets Catalyst optimise the whole plan (A), enables pipelining of narrow transformations (B), and supports lineage-based recomputation (D). Actions do build a plan first (C is wrong), and shuffle data going to disk is a cost, not a benefit (E is wrong). | topic1-prompt3-lazy-evaluation.md | Medium |
| 9 | C | `spark.sql.shuffle.partitions` (default 200) controls the number of partitions produced by a shuffle. `spark.default.parallelism` applies to RDD operations; `maxPartitionBytes` controls input split size. | topic1-prompt4-shuffling-performance.md | Medium |
| 10 | B | `spark.sql.autoBroadcastJoinThreshold` (default 10 MiB) is the size threshold below which Spark automatically broadcasts a table. `broadcastTimeout` is how long to wait for the broadcast to complete. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 11 | A, C, D | Spark reconstructs lost partitions from lineage (A); the DAGScheduler re-submits failed stages (C); `checkpoint()` materialises a snapshot and breaks the lineage (D). Spark does NOT replicate partitions by default (B), and HDFS is not required (E). | topic1-prompt6-fault-tolerance.md | Medium |
| 12 | B | Executors run Tasks and store cached block data. The Driver orchestrates stage scheduling; the Cluster Manager handles resource allocation. | topic1-prompt1-spark-architecture.md | Medium |
| 13 | A, C, D | `repartition` shuffles data across the network (A); `groupBy().sum()` is an aggregation requiring a shuffle (C); `distinct()` requires a shuffle to find global duplicates (D). `filter` and `withColumn` are narrow. | topic1-prompt4-shuffling-performance.md | Medium |
| 14 | B | `coalesce(n)` merges existing partitions without a full shuffle. `repartition(10)` or `repartition(10, 'id')` both trigger a full shuffle. `sortWithinPartitions` does not reduce partition count. | topic1-prompt4-shuffling-performance.md | Medium |
| 15 | A, B, D | `select` (A), `union` (B — no shuffle, just appends partition lists), and `withColumn` (D) are all narrow. `orderBy` (C) requires a global sort shuffle; `join` (E) typically requires a shuffle. | topic1-prompt3-lazy-evaluation.md | Medium |
| 16 | B | The Spark execution hierarchy from coarsest to finest is: Application → Job → Stage → Task. An Application contains multiple Jobs (one per action); each Job has Stages; each Stage has Tasks. | topic1-prompt1-spark-architecture.md | Medium |
| 17 | B | The `broadcast()` hint makes the join narrow (no shuffle), but `groupBy().sum()` requires a shuffle — creating one stage boundary. So there are approximately 2 stages: read/filter/broadcast-join, then groupBy aggregate. | topic1-prompt4-shuffling-performance.md | Hard |
| 18 | C | By default, the DAGScheduler re-submits the failed Stage and the TaskScheduler retries individual Tasks, recomputing lost partitions from lineage. The entire job does not fail on a single Executor loss. | topic1-prompt6-fault-tolerance.md | Hard |
| 19 | C | `MEMORY_AND_DISK_SER` stores serialised (compacted) objects, dramatically reducing the number of live heap objects the GC must scan. Increasing shuffle partitions or reducing heap makes things worse; Serial GC is slower than G1GC for large heaps. | topic1-prompt7-garbage-collection.md | Hard |
| 20 | B, C | `MEMORY_AND_DISK` and `MEMORY_AND_DISK_SER` both keep data in memory and automatically spill to disk when memory is insufficient. `MEMORY_ONLY` evicts partitions (does not spill); `DISK_ONLY` uses disk exclusively; `OFF_HEAP` uses off-heap memory only. | topic1-prompt6-fault-tolerance.md | Hard |
| 21 | B | `createOrReplaceTempView` creates or silently replaces the view. `createTempView` raises `TempTableAlreadyExistsException` if the view already exists. | topic2-prompt8-spark-sql-fundamentals.md | Easy |
| 22 | C | `spark.sql()` always returns a PySpark DataFrame, regardless of the SQL operation. It is interchangeable with the DataFrame API. | topic2-prompt8-spark-sql-fundamentals.md | Easy |
| 23 | C | Global temporary views live in the `global_temp` database and must be referenced as `global_temp.<view_name>`. Session-scoped views can be referenced by name alone. | topic2-prompt8-spark-sql-fundamentals.md | Easy |
| 24 | B | `selectExpr()` accepts SQL expression strings. `'salary * 1.1 AS new_salary'` is valid SQL syntax. The other options mix API objects with SQL strings incorrectly. | topic2-prompt8-spark-sql-fundamentals.md | Easy |
| 25 | C | A session-scoped temp view is tied to its `SparkSession`. When the session ends, the view is automatically dropped. A global temp view persists across sessions within the same application. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 26 | B | `spark.catalog.listTables()` returns all tables and views in the current database, including temporary views. `listDatabases()` returns databases; `listColumns()` takes a table name argument. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 27 | C | The partition key defines the independent group boundaries (region), and the order key defines the sort within each partition (date). Swapping them would compute a running total per date ordered by region, which is semantically wrong. | topic2-prompt10-window-functions.md | Medium |
| 28 | A, B, C, E | `upper`, `trim`, `split`, and `regexp_replace` are all valid PySpark string functions. `F.str_replace` does not exist in PySpark — use `F.regexp_replace` instead. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 29 | B | `dense_rank()` assigns consecutive integers with no gaps after ties (1, 2, 2, 3). `rank()` skips values after ties to reflect the true ordinal position (1, 2, 2, 4). | topic2-prompt10-window-functions.md | Medium |
| 30 | B | `Window.unboundedPreceding` to `Window.currentRow` defines a cumulative (running) frame from the very first row of the partition to the current row — the standard running total frame. | topic2-prompt10-window-functions.md | Medium |
| 31 | A, B, C, E | Catalyst parses SQL (A), resolves column references (B), applies rule-based optimisations (C), and generates the physical plan (E). Task scheduling (D) is performed by the TaskScheduler, not Catalyst. | topic2-prompt11-query-optimization.md | Medium |
| 32 | A, B, D | AQE coalesces small shuffle partitions (A), can switch SortMergeJoin to BroadcastHashJoin at runtime (B), and handles skewed partitions by splitting them (D). It does not replace UDFs (C) or rewrite subqueries into correlated joins (E). | topic2-prompt11-query-optimization.md | Medium |
| 33 | B | Statistics must be explicitly collected via `ANALYZE TABLE ... COMPUTE STATISTICS` (SQL) or equivalent DataFrame calls. They are not collected automatically on write, and `EXPLAIN COST` reads existing stats rather than collecting them. | topic2-prompt11-query-optimization.md | Medium |
| 34 | A, B, C, E | A, B, C, and E all pass SQL expression strings to `expr()` or `selectExpr()`. D is invalid — `F.expr()` requires a string argument; wrapping a `col()` Column object inside `F.expr()` raises an error. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 35 | C | Higher-order / collection functions (`transform`, `filter`, `aggregate`, `explode`, etc.) are optimised for arrays, maps, and nested structures. They are distinct from aggregate, window, and string function categories. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 36 | B | `spark.catalog.tableExists('orders')` returns `True` or `False` and works for both permanent tables and temporary views. Option C would check `Name` attributes of `Table` objects, not strings directly. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 37 | B | `rank()` assigns 1, 2, 2, 4 — it skips rank 3 because two rows share rank 2. `dense_rank()` would assign 1, 2, 2, 3. `row_number()` assigns unique sequential numbers regardless of ties. | topic2-prompt10-window-functions.md | Hard |
| 38 | B | AQE's partition coalescing (`spark.sql.adaptive.coalescePartitions.enabled = true`) automatically merges small post-shuffle partitions at runtime. This is the correct feature for the described problem of many tiny partitions. | topic2-prompt11-query-optimization.md | Hard |
| 39 | B | Predicate pushdown can only be applied to filters on the original source columns. `processed_date` is a derived column computed by `withColumn`; Spark cannot push this filter past the `withColumn` step to the file reader. | topic2-prompt11-query-optimization.md | Hard |
| 40 | C | With `Window.partitionBy('dept').orderBy('hire_date')` and no explicit frame, the default for ordered windows is `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`. So `count('*').over(w)` gives the cumulative count within the department up to and including the current row's hire date. | topic2-prompt10-window-functions.md | Hard |
| 41 | B | `df.select('id', 'name', 'email')` is the correct syntax. There is no `get()`, `pick()`, or `columns()` method for column selection in PySpark. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 42 | B | `filter()` and `where()` are exact aliases in PySpark — they compile to the same execution plan. | topic3-prompt14-filtering-row-manipulation.md | Easy |
| 43 | B | `withColumn` adds a new column with the given name. If a column with that name already exists, it is replaced. The original `price` column is not modified. | topic3-prompt13-column-manipulation-expressions.md | Easy |
| 44 | C | `dropDuplicates(['email', 'department'])` removes rows where both `email` and `department` are duplicated. `distinct()` considers all columns; there is no `deduplicate()` method in PySpark. | topic3-prompt16-handling-nulls.md | Easy |
| 45 | A | `df.fillna({'salary': 0, 'department': 'Unknown'})` fills different columns with different values in a single call. Option B applies a single fill value to all columns of matching type. | topic3-prompt16-handling-nulls.md | Easy |
| 46 | C | `.option('header', True)` tells the CSV reader to treat the first row as column names. The other option names do not exist in the PySpark CSV reader. | topic3-prompt19-reading-writing-dataframes.md | Easy |
| 47 | B | `df.withColumnRenamed('emp_id', 'employee_id')` is the correct method. There is no `rename()` or `renameColumn()` method; `.name()` is not a Column method. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 48 | B | `sort()` and `orderBy()` are exact aliases in PySpark's DataFrame API — both perform a global sort. There is no per-partition sort variant in the DataFrame API (that is `sortWithinPartitions()`). | topic3-prompt14-filtering-row-manipulation.md | Medium |
| 49 | A, B, D, E | `sum`, `avg`, `max`, and `count(column)` all ignore NULL values. Only `F.count('*')` or `F.count(F.lit(1))` counts every row including those with NULLs. | topic3-prompt15-aggregations-grouping.md | Medium |
| 50 | B, C, E | `left` (left outer) returns all left rows with NULLs for unmatched right (B); `left_anti` returns only left rows with NO match (C); `full` outer returns all rows from both sides (E). `inner` (A) and `left_semi` (D) only return rows that have a match. | topic3-prompt17-joins.md | Medium |
| 51 | B | `union()` aligns by column position, not name. `df_b`'s first column is `name` (value `'Bob'`), which lands in `df_a`'s first positional column `id`. So the second row's `id` value is the string `'Bob'`. | topic3-prompt18-combining-dataframes.md | Medium |
| 52 | A, B, D, E | `dropna()` and `na.drop()` are equivalent (A); default `how='any'` (B); `thresh=3` drops rows with fewer than 3 non-null values (D); `subset` limits the null check to those columns (E). C is false — `dropna()` removes SQL NULLs only, not floating-point NaN values. | topic3-prompt16-handling-nulls.md | Medium |
| 53 | B | `LongType()` is a 64-bit integer (equivalent to Python `int` / Java `long`). `nullable=False` means the column does not accept null values. `IntegerType()` would be 32-bit. | topic3-prompt21-schemas-data-types.md | Medium |
| 54 | A, B, C | `overwrite` replaces existing data; `append` adds to it; `ignore` silently skips the write if the path exists. `error` and `errorIfExists` (the default) both raise an `AnalysisException` if the path already contains data. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 55 | A, B, C, D | `repartition` always shuffles (A); `coalesce` avoids a full shuffle and can only decrease partition count (B); `repartition(n, col)` uses hash partitioning on the column (C); `coalesce(1)` is more efficient than `repartition(1)` (D). E is false — `coalesce` cannot increase partition count. | topic3-prompt20-repartition-coalesce.md | Medium |
| 56 | C | `F.isnan(col('score'))` returns `True` for IEEE 754 NaN values in floating-point columns. `isNull()` / `isnull()` only detect SQL NULL; they return `False` for NaN. | topic3-prompt16-handling-nulls.md | Medium |
| 57 | B | `subtract()` (equivalent to SQL `EXCEPT DISTINCT`) returns rows from `df_a` that do NOT appear in `df_b`, with deduplication applied to the result. It is not a symmetric difference. | topic3-prompt18-combining-dataframes.md | Medium |
| 58 | A, B, D | Struct fields can be accessed with dot notation in a string (`'address.city'`, A), with `.getField()` (B), or with `col('address.city')` (D). The subscript `[]` notation (C) is for array/map access, not structs. Option E constructs a new struct rather than accessing a field. | topic3-prompt21-schemas-data-types.md | Medium |
| 59 | B | To use a Python UDF in `spark.sql()`, it must be registered by name with `spark.udf.register('function_name', func, returnType)`. A `@F.udf`-decorated function is only available in the DataFrame API, not automatically in the SQL namespace. | topic3-prompt22-udfs.md | Medium |
| 60 | A, B, D | `collect_list` preserves duplicates (A); both return `ArrayType` (B); `collect_list` order is non-deterministic across runs (D). `collect_set` does NOT guarantee ordering (C is false); it is not equivalent to a column-level `distinct()` call (E is false). | topic3-prompt15-aggregations-grouping.md | Medium |
| 61 | B | `pivot` produces one column per distinct value of the pivot column (4 quarters) plus the group-by column(s) (1 region column) = 5 columns total. | topic3-prompt15-aggregations-grouping.md | Medium |
| 62 | B | `cube()` computes aggregates for every possible combination of the provided columns including the grand total. `rollup()` only computes hierarchical left-to-right subtotals (e.g., for a 2-column rollup: grand total, subtotal by col1, then col1+col2). | topic3-prompt15-aggregations-grouping.md | Medium |
| 63 | C | `F.countDistinct('product')` counts distinct non-null values. There is no `F.distinct_count()`; `F.count()` returns a `Column` not a distinct count; the other options are syntactically invalid. | topic3-prompt15-aggregations-grouping.md | Medium |
| 64 | A, B, C, E | Broadcast joins can be forced by: `broadcast(small_df)` in the join (A), raising `autoBroadcastJoinThreshold` above the table size (B), `.hint('broadcast')` (C), or the SQL `/*+ BROADCAST(t) */` hint (E). Simply caching a DataFrame (D) does not instruct Spark to broadcast it. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 65 | B | `union()` aligns by column position. `df_b` is `['email', 'customer_id', 'score']`, so its first positional value `'bob@x.com'` lands in `df_a`'s first column `customer_id`. The result's `customer_id` for the second row is the string `'bob@x.com'`. | topic3-prompt18-combining-dataframes.md | Hard |
| 66 | B | `fillna()` / `na.fill()` only replaces SQL NULL values. Floating-point NaN is a distinct value and is left unchanged by `fillna`. Use `F.isnan()` combined with `when().otherwise()` to replace NaN. | topic3-prompt16-handling-nulls.md | Hard |
| 67 | D | Both `col('profile')['skills'][0]` (A) and `col('profile').getItem('skills').getItem(0)` (B) are valid and equivalent ways to access a map key then an array element. D correctly identifies both as correct. | topic3-prompt21-schemas-data-types.md | Hard |
| 68 | B | Python UDFs automatically receive `None` when a SQL NULL is passed. Returning `None` from a Python UDF maps back to SQL NULL. No explicit null handling annotation is needed. | topic3-prompt22-udfs.md | Hard |
| 69 | B | `partitionBy('year', 'month')` creates a nested Hive-style directory hierarchy: `/output/events/year=2024/month=01/part-*.parquet`. The column values become directory names and the columns are removed from the data files. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 70 | A | `groupBy` produces the group column as its declared type (STRING); `avg` returns DOUBLE; `stddev` returns DOUBLE; `count('*')` returns BIGINT (64-bit integer). INT (32-bit) is not the return type for `count`. | topic3-prompt15-aggregations-grouping.md | Hard |
| 71 | C | The Spark UI for an active application is available at `http://localhost:4040` by default in local mode. Port 8080 is the standalone master UI; 18080 is the History Server; 7077 is the standalone cluster manager port. | topic4-prompt26-debugging.md | Easy |
| 72 | C | `df.explain()` with no arguments displays only the physical execution plan. Use `explain('extended')` for all four plan stages, `explain('cost')` for cost estimates, or `explain('codegen')` for generated code. | topic4-prompt26-debugging.md | Easy |
| 73 | C | When Spark cannot determine a table's size (e.g., no statistics collected, or the data was just written and stats are stale), it conservatively defaults to `SortMergeJoin` even if the table would fit within the broadcast threshold. | topic4-prompt24-performance-tuning.md | Medium |
| 74 | B | With 500 MB of data and 200 partitions, each partition is ~2.5 MB — far too small. Reducing shuffle partitions to 10–20 produces 25–50 MB per partition, which is a more efficient task size. Increasing to 2000 makes the problem worse. | topic4-prompt24-performance-tuning.md | Medium |
| 75 | A, C | The clearest skew signals are: one or a few tasks taking far longer than others (A), and those same tasks reading disproportionately more shuffle bytes (C). Uniform completion times (B), identical GC (D), and extra output files (E) do not indicate skew. | topic4-prompt25-common-errors.md | Medium |
| 76 | A, B, D | AQE addresses: merging small post-shuffle partitions (A), runtime join strategy switching (B), and splitting skewed partitions (D). AQE does not replace Python UDFs (C) or automatically cache reused DataFrames (E). | topic4-prompt24-performance-tuning.md | Medium |
| 77 | B | `explain('extended')` shows all four plan stages: Parsed Logical → Analyzed Logical → Optimized Logical → Physical Plan. `explain('cost')` adds row/size estimates; `explain('codegen')` shows generated code. | topic4-prompt26-debugging.md | Medium |
| 78 | A, C, D, E | Predicate pushdown reduces physical reads (A); filters on derived columns cannot be pushed down (C); the `PushedFilters` list in `FileScan` shows which filters were pushed (D); pushdown is enabled by default for supported formats (E). B is false — CSV and JSON also support predicate pushdown in newer Spark versions (though less efficiently). | topic4-prompt26-debugging.md | Medium |
| 79 | B | `PushedFilters: []` in the `FileScan` node means no filters were pushed to the Parquet reader. The `Filter` node above `FileScan` confirms the filter runs in Spark memory after reading all data — a significant performance problem for large datasets. | topic4-prompt26-debugging.md | Hard |
| 80 | B | `collect()` sends the full result to the Driver JVM heap. If the result is larger than the Driver's heap, it throws `OutOfMemoryError` on the Driver. The fix is to increase `spark.driver.memory` or reduce the data with `limit()`, aggregations, or filters before collecting. | topic4-prompt25-common-errors.md | Hard |
| 81 | A | `df.isStreaming` is the property that returns `True` for streaming DataFrames and `False` for batch DataFrames. The other options do not exist. | topic5-prompt27-structured-streaming.md | Easy |
| 82 | C | `append` mode writes only the new rows added in each micro-batch. It is the default and only valid mode for stateless queries. `complete` re-emits the full result table; `update` emits changed rows. | topic5-prompt27-structured-streaming.md | Easy |
| 83 | B, C | `trigger(once=True)` processes all available data and stops (B). `trigger(availableNow=True)` (Spark 3.3+) does the same but with better parallelism (C). `processingTime='0 seconds'` (A) runs continuously; `continuous` (D) runs indefinitely; no trigger (E) also runs continuously. | topic5-prompt27-structured-streaming.md | Medium |
| 84 | B | File-based streaming sources require an explicit schema. Unlike batch reads, Spark cannot infer the schema from a streaming source because it needs the schema before any files arrive. | topic5-prompt27-structured-streaming.md | Medium |
| 85 | B | The checkpoint location stores the processed offset log and stateful operator state to a durable store. On restart, Spark reads the checkpoint to resume exactly where it left off, enabling fault tolerance and exactly-once semantics. | topic5-prompt27-structured-streaming.md | Medium |
| 86 | B | A watermark defines a lateness threshold: Spark tracks the maximum event time seen and considers any event arriving more than the threshold behind that maximum as too late. This allows state for completed windows to be safely cleaned up. | topic5-prompt28-stateful-streaming.md | Medium |
| 87 | A, B, D | Valid combinations: `append` + stateless query (A); `complete` + aggregation (B); `update` + aggregation (D). Invalid: `append` + aggregation without watermark (C — Spark cannot finalise windows without a watermark); `complete` + stateless query (E — complete requires an aggregation). | topic5-prompt27-structured-streaming.md | Medium |
| 88 | B | Kafka messages are inherently byte arrays. The `value` (and `key`) fields in a Kafka streaming DataFrame are `BinaryType`. The application must explicitly cast/parse them (e.g., `cast('string')` or deserialise from Avro/JSON). | topic5-prompt27-structured-streaming.md | Medium |
| 89 | C | In `append` mode with a watermark, a window's results are emitted only after the watermark advances past the window's end time — guaranteeing that no more late events can fall within that window. This is the defining behaviour of append mode with windowed aggregations. | topic5-prompt28-stateful-streaming.md | Hard |
| 90 | B | Without a checkpoint, Spark has no persistent record of previously seen event IDs. On restart, the deduplication state is lost and the query re-processes events from the retained Kafka offset, emitting events it had already output before the failure. | topic5-prompt28-stateful-streaming.md | Hard |
| 91 | B | Spark Connect uses the `sc://` URL scheme (e.g., `sc://hostname:15002`). `spark://` is for the Standalone cluster manager; `grpc://` is the underlying protocol but not the Spark Connect URL scheme. | topic6-prompt29-spark-connect.md | Easy |
| 92 | C | The RDD API and direct `SparkContext` access are not available via Spark Connect. The client communicates only through the logical plan layer; there is no JVM bridge to access low-level RDD operations. | topic6-prompt29-spark-connect.md | Medium |
| 93 | B | Spark Connect uses gRPC over HTTP/2 to transmit serialised logical plans (using Protocol Buffers). Query results are returned as Apache Arrow record batches for efficient in-memory columnar transfer without serialising Python objects. | topic6-prompt29-spark-connect.md | Medium |
| 94 | D | Spark Connect graduated to stable in Apache Spark 3.4. It was introduced as a preview/experimental feature in Spark 3.3. | topic6-prompt29-spark-connect.md | Medium |
| 95 | A | The RDD API (`sc.textFile`, `flatMap`, `reduceByKey`, etc.) is not supported through Spark Connect. All code must be rewritten using the DataFrame/Dataset API, which Spark Connect translates into a serialisable logical plan sent to the server. | topic6-prompt29-spark-connect.md | Hard |
| 96 | C | `import pyspark.pandas as ps` is the correct import for the Pandas API on Spark (formerly Koalas). Using `import pandas as ps` imports standard Pandas running on the local driver only. | topic7-prompt30-pandas-api.md | Easy |
| 97 | B | `sdf.pandas_api()` converts a PySpark DataFrame to a `pyspark.pandas` DataFrame (Spark 3.2+). The older API was `sdf.to_pandas_on_spark()`. `sdf.to_pandas()` collects all data to the driver as a regular Pandas DataFrame. | topic7-prompt30-pandas-api.md | Medium |
| 98 | C | `psdf.to_pandas()` calls `collect()` internally, pulling all distributed data to the Driver node's memory as a local Pandas DataFrame. On a large dataset this will exhaust Driver memory and cause an OutOfMemoryError. | topic7-prompt30-pandas-api.md | Medium |
| 99 | B | Distributed DataFrames have no inherent global row ordering. When row values are equal on the sort key, the tiebreaker order depends on partition assignment and task execution order, which can differ between runs. | topic7-prompt30-pandas-api.md | Medium |
| 100 | B | The `'sequence'` index type requires globally sequential integers across all partitions, which forces a global sort (full shuffle) to assign monotonically increasing IDs. The `'distributed'` index uses partition-local IDs and avoids any shuffle. | topic7-prompt30-pandas-api.md | Hard |
