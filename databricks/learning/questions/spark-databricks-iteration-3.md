# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 3)

**Iteration**: 3

**Generated**: 2026-04-25

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 79 `one` / 21 `many`

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
**Topic**: RDD vs DataFrame lineage

**Question**:
What is the primary difference between how **RDD lineage** and **DataFrame lineage** are stored in Spark?

- A) RDDs have no lineage — they must be manually checkpointed to survive executor failure
- B) RDD lineage is a chain of parent RDD dependencies; DataFrame lineage is stored as an optimized logical plan that passes through Catalyst before execution
- C) DataFrame lineage is stored as a sequence of RDD transformations at the byte-code level
- D) Both RDDs and DataFrames use the same lineage representation internally

---

### Question 2 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: WholeStageCodegen

**Question**:
What does **Whole-Stage Code Generation (WholeStageCodegen)** do in Spark's physical execution engine?

- A) Compiles the entire Python UDF code into JVM bytecode for faster execution
- B) Fuses multiple operators (e.g., filter, project) in a pipeline stage into a **single optimized JVM bytecode function** to eliminate virtual function dispatch overhead
- C) Generates the SQL DDL statements for creating tables from a DataFrame schema
- D) Converts the logical plan into a physical plan without intermediate steps

---

### Question 3 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Tungsten project

**Question**:
The **Tungsten** project in Spark focuses on which category of improvements?

- A) Improving the Spark UI to show more detailed execution metrics
- B) Optimizing CPU and memory efficiency through binary data encoding (off-heap or managed memory), cache-aware computation, and code generation
- C) Replacing the Python-JVM Py4J bridge with a gRPC-based communication layer
- D) Extending the Catalyst optimizer with machine-learning-based plan selection

---

### Question 4 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: external shuffle service

**Question**:
What is the purpose of the **external shuffle service** on a cluster worker node?

- A) It caches broadcast variables locally so Executors can fetch them without contacting the Driver
- B) It stores shuffle intermediate files on the worker and serves them to downstream Tasks, allowing the Executor that wrote them to be safely removed (enabling Dynamic Resource Allocation)
- C) It compresses shuffle output to reduce network bandwidth
- D) It coordinates the shuffle partition assignment across all Executors in a stage

---

### Question 5 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: checkpoint vs persist tradeoffs

**Scenario**:
A developer uses `df.checkpoint()` to break a long RDD lineage chain and avoid recomputation.

**Question**:
How does `df.checkpoint()` differ from `df.persist()`?

- A) `checkpoint()` stores data in Executor memory for fast access; `persist()` writes to reliable distributed storage (HDFS/cloud) and severs the lineage
- B) `checkpoint()` writes data to a **reliable distributed storage location** (configured by `sc.setCheckpointDir()`) and **truncates the lineage graph**; `persist()` caches in memory/disk but the full lineage is retained for recomputation on failure
- C) They are identical except that `checkpoint()` replicates data across two Executors
- D) `checkpoint()` is a lazy operation; `persist()` materializes data immediately

---

### Question 6 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: task serialization

**Question**:
When Spark sends a Task to an Executor, what must be serialized and included in the Task payload?

- A) Only the query plan as a JSON document
- B) The function (closure) to execute plus any referenced variables captured in the closure, serialized using Java serialization (or Kryo if configured)
- C) The entire DataFrame including its data, so the Executor can operate locally
- D) The Spark configuration, Driver address, and application ID only

---

### Question 7 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Kryo vs Java serialization

**Question**:
When would you configure Spark to use **Kryo serialization** instead of the default Java serialization?

- A) Kryo is required whenever using RDDs; Java serialization only works with DataFrames
- B) Kryo is generally **faster and more compact** than Java serialization and is preferred for performance-sensitive applications; it is configured via `spark.serializer = org.apache.spark.serializer.KryoSerializer`
- C) Kryo serialization is only available on Databricks and not in open-source Spark
- D) Kryo must be used when storing data with `MEMORY_ONLY_SER` storage level

---

### Question 8 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: off-heap memory configuration

**Question**:
Which configuration properties must **both** be set to enable Spark's off-heap memory for Tungsten data structures?

- A) `spark.memory.offHeap.enabled = true` and `spark.memory.offHeap.size` set to a positive byte value
- B) `spark.executor.memoryOffHeap = true` and `spark.offHeap.size`
- C) `spark.tungsten.offHeap = true` and `spark.memory.offHeap.bytes`
- D) Off-heap memory is always enabled and only requires setting `spark.memory.offHeap.size`

---

### Question 9 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: executor heartbeat timeout

**Question**:
What happens when an Executor fails to send a heartbeat to the Driver within the timeout configured by `spark.executor.heartbeatInterval` and `spark.network.timeout`?

- A) The Executor is paused until it can reconnect to the Driver
- B) The Driver marks the Executor as lost, reschedules its Tasks on other Executors, and requests the Cluster Manager to launch a replacement Executor
- C) The Executor completes its current Tasks and then shuts down gracefully
- D) The Driver fails the entire application immediately

---

### Question 10 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Python worker process model

**Question**:
In PySpark, how does Spark execute Python UDFs on Executors?

- A) The UDF bytecode is compiled to JVM bytecode via GraalVM and run directly in the Executor JVM
- B) A separate **Python worker process** is spawned per Executor thread; data is serialized (pickled) from the JVM, sent to the Python process via a local socket, processed, and returned to the JVM
- C) The Driver sends Python code to Executors, which evaluate it in an embedded CPython interpreter
- D) Python UDFs are always executed on the Driver after collecting data from Executors

---

### Question 11 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: cluster manager comparison

**(Select all that apply)**
Which of the following statements correctly compare Spark's supported **cluster managers**?

- A) In **Standalone** mode, the Spark Master tracks available worker resources and schedules applications
- B) **YARN** integrates with Hadoop ecosystems and uses ResourceManager and NodeManager daemons
- C) **Kubernetes** uses Pods for Driver and Executor containers and supports native scheduling via the `k8s://` master URL
- D) Standalone mode requires a Hadoop installation to manage the cluster
- E) All three cluster managers support both client and cluster deploy modes

---

### Question 12 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: data locality levels

**Question**:
In Spark's task scheduling, what does **PROCESS_LOCAL** data locality mean?

- A) Data is stored on the same physical machine as the Executor but in a different process
- B) Data is in the **same Executor JVM process** that will execute the Task — the fastest possible data locality
- C) Data is in the local filesystem of the node but not in memory
- D) Data is local to the Spark cluster but on a different rack

---

### Question 13 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: speculative execution threshold

**Question**:
Which configuration property controls how long Spark waits (relative to median task duration) before launching a **speculative** copy of a slow Task?

- A) `spark.speculation.interval`
- B) `spark.speculation.quantile`
- C) `spark.speculation.multiplier`
- D) `spark.task.retryDelay`

---

### Question 14 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Resource Profile API

**(Select all that apply)**
Which of the following are true about Spark's **Resource Profile API** (available since Spark 3.1)?

- A) It allows different stages within the same application to request different Executor resource configurations (CPU cores, memory, GPUs)
- B) It is only supported on YARN; Kubernetes does not support per-stage resource profiles
- C) A `ResourceProfile` is attached to an RDD using `rdd.withResources(profile)`
- D) It enables requesting GPU resources per Executor for stages that require GPU-based computation
- E) The default resource profile applies to all stages that have no explicit profile assigned

---

### Question 15 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: network topology rack awareness

**Question**:
How does Spark use **rack awareness** information when scheduling Tasks for maximum data locality?

- A) It ensures Tasks always run on the same machine as their data (PROCESS_LOCAL)
- B) It uses rack topology to prefer scheduling Tasks on nodes in the **same rack** as the data (RACK_LOCAL), reducing cross-rack network bandwidth usage when PROCESS_LOCAL and NODE_LOCAL are unavailable
- C) Rack awareness only applies to broadcast variable distribution
- D) Spark ignores rack topology; network locality is handled entirely by the cluster manager

---

### Question 16 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: stage count with broadcast and shuffle

**Scenario**:
```python
large = spark.read.parquet('/data/events')         # 10 GB
small = spark.read.parquet('/data/lookup')          # 5 MB
joined = large.join(broadcast(small), 'event_type')
result = joined.groupBy('region').agg(F.sum('revenue'))
result.write.parquet('/output')
```
`spark.sql.autoBroadcastJoinThreshold` is 10 MB.

**Question**:
How many shuffle-boundary Stages does this query produce?

- A) 1 Stage — broadcast join and groupBy are fused
- B) 2 Stages — Stage 1: scan + broadcast join + partial aggregation; Stage 2: shuffle + final aggregation + write
- C) 3 Stages — one per operation
- D) 4 Stages — scan large, scan small, join, groupBy

---

### Question 17 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: memory overhead Python workers

**Scenario**:
A Spark application runs Python UDFs extensively. Executors are allocated `4g` of JVM heap (`spark.executor.memory`). The job fails with container memory limit exceeded errors on YARN.

**Question**:
What is the most likely cause and fix?

- A) Increase `spark.driver.memory` because Python UDFs execute on the Driver
- B) Python worker processes consume memory **outside the JVM heap**; increase `spark.executor.memoryOverhead` (or `spark.executor.pyspark.memory`) to account for the Python worker memory footprint
- C) Python UDFs are limited to 2 GB; reduce UDF input data size per partition
- D) Disable WholeStageCodegen to free JVM heap used by generated code

---

### Question 18 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: many
**Topic**: job cancellation

**(Select all that apply)**
Which of the following can **cancel a running Spark Job** programmatically?

- A) `spark.sparkContext.cancelJob(jobId)`
- B) `spark.sparkContext.cancelAllJobs()`
- C) `spark.sparkContext.cancelJobGroup(groupId)` after setting the job group with `spark.sparkContext.setJobGroup()`
- D) `spark.stop()` — stops the entire SparkSession, cancelling all running jobs
- E) `spark.sparkContext.cancelStage(stageId)` — cancels a specific Stage without cancelling the Job

---

### Question 19 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: unified memory model storage vs execution borrowing

**Scenario**:
An Executor has 8 GB of Spark Memory (after `spark.memory.fraction`). Storage memory holds 5 GB of cached DataFrames. A large join operation needs 6 GB of Execution Memory.

**Question**:
What does Spark do?

- A) The join fails with OOM immediately because Execution Memory cannot exceed the initial execution region
- B) Spark **evicts** some cached storage blocks from Storage Memory (if they are not pinned) to satisfy the Execution Memory demand, since the Unified Memory Model allows Execution to borrow from Storage
- C) Spark spills the join data to disk immediately without attempting to use Storage Memory
- D) Spark cancels the cache operation and returns all memory to the Execution region

---

### Question 20 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: shuffle reduce side memory

**Scenario**:
A join between two large DataFrames results in repeated `java.io.IOException: Unable to acquire memory` errors during the shuffle reduce phase.

**Question**:
Which configuration is most directly related to the amount of memory available on the **reduce side** of a shuffle?

- A) `spark.shuffle.compress` — controls whether shuffle files are compressed to reduce size
- B) `spark.reducer.maxSizeInFlight` — limits how much shuffle data a reducer fetches at one time from remote Executors, controlling concurrent in-flight shuffle data
- C) `spark.shuffle.sort.bypassMergeThreshold` — controls when a bypass merge sort is used
- D) `spark.executor.instances` — adding more Executors distributes shuffle reduce partitions across more nodes

---

### Question 21 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: from_json() function

**Question**:
Which function parses a JSON string column into a structured column (struct or map)?

- A) `F.parse_json(col('payload'), schema)`
- B) `F.from_json(col('payload'), schema)`
- C) `F.json_to_struct(col('payload'), schema)`
- D) `F.schema_of_json(col('payload'))`

---

### Question 22 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: struct() function

**Question**:
What does `F.struct(col('first_name'), col('last_name'))` return?

- A) A `StringType` column containing first and last name separated by a space
- B) A `StructType` column with two fields: `first_name` and `last_name`
- C) An `ArrayType(StringType)` with two elements
- D) A `MapType(StringType, StringType)` keyed by column name

---

### Question 23 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: to_timestamp() function

**Question**:
Which function parses a `StringType` column `event_ts` formatted as `'yyyy-MM-dd HH:mm:ss'` into a `TimestampType`?

- A) `F.cast(col('event_ts'), 'timestamp')`
- B) `F.to_timestamp(col('event_ts'), 'yyyy-MM-dd HH:mm:ss')`
- C) `F.parse_timestamp(col('event_ts'), 'yyyy-MM-dd HH:mm:ss')`
- D) Both A and B are correct; B allows an explicit format string while A uses default parsing

---

### Question 24 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: greatest() and least() functions

**Question**:
What does `F.greatest(col('price_a'), col('price_b'), col('price_c'))` return?

- A) The sum of the three columns
- B) The **largest non-null** value among the three column expressions for each row
- C) The column name with the highest value
- D) An error if any column contains a `null`

---

### Question 25 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: transform() higher-order function

**Question**:
What does the following expression do?
```python
F.transform(col('scores'), lambda x: x * 1.1)
```

- A) Applies a 10% multiplier to each element in the `scores` array column, returning a new array of scaled values
- B) Transforms the DataFrame schema by renaming the `scores` column
- C) Filters the `scores` array to include only elements above 1.1
- D) Raises an error because `transform()` only accepts SQL string expressions, not Python lambdas

---

### Question 26 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: filter() higher-order function on arrays

**Question**:
Which expression returns a new array column containing only the elements of `tags` that start with the letter `'a'`?

- A) `F.array_filter(col('tags'), lambda x: x.startswith('a'))`
- B) `F.filter(col('tags'), lambda x: x.startswith('a'))`
- C) `F.select_if(col('tags'), F.col('x').startswith('a'))`
- D) `F.where(col('tags'), lambda x: x.startswith('a'))`

---

### Question 27 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: aggregate() higher-order function

**Question**:
What does the following expression compute?
```python
F.aggregate(col('amounts'), F.lit(0), lambda acc, x: acc + x)
```

- A) The product of all elements in the `amounts` array
- B) The **sum** of all elements in the `amounts` array, starting accumulation at 0
- C) The maximum value in the `amounts` array
- D) The count of non-zero elements in the `amounts` array

---

### Question 28 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: flatten() function

**Question**:
A DataFrame has a column `nested` of type `ArrayType(ArrayType(IntegerType))`. What does `F.flatten(col('nested'))` return?

- A) A single `IntegerType` column with the sum of all nested integers
- B) An `ArrayType(IntegerType)` column where all inner arrays are **concatenated** into a single flat array
- C) A `StringType` column with the nested array printed as a JSON string
- D) An error because `flatten()` requires all inner arrays to have the same length

---

### Question 29 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: months_between() function

**Question**:
What does `F.months_between(col('end_date'), col('start_date'))` return?

- A) An `IntegerType` representing the number of complete months between the two dates
- B) A `DoubleType` representing the fractional number of months between the two dates (can be negative)
- C) A `StringType` formatted as `'3 months 5 days'`
- D) A `DateType` representing the date exactly half-way between the two dates

---

### Question 30 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: lpad() / rpad() functions

**Question**:
What does `F.lpad(col('code'), 6, '0')` produce for the value `'42'`?

- A) `'420000'` — pads zeros on the right to reach length 6
- B) `'000042'` — pads zeros on the **left** to reach length 6
- C) `'42    '` — pads spaces on the right
- D) `'42'` — no padding because `'42'` has a length less than 6

---

### Question 31 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: posexplode() function

**Question**:
What does `F.posexplode(col('items'))` return compared to `F.explode(col('items'))`?

- A) It returns fewer rows by skipping null elements
- B) It returns the same rows but also includes a `pos` column with the **zero-based integer index** of each element in the original array
- C) It returns a map of positions to values instead of individual rows
- D) They are identical — `posexplode` is just an alias for `explode`

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: SQL PIVOT syntax

**(Select all that apply)**
Which of the following statements about the **PIVOT** operation in Spark SQL are correct?

- A) PIVOT rotates rows into columns, typically used to transform long-format data to wide-format
- B) In a PIVOT query, the column whose distinct values become new column headers is specified in `FOR <column> IN (<values>)`
- C) Aggregate functions are required in a PIVOT query (e.g., `SUM`, `AVG`, `COUNT`)
- D) PIVOT is only available via the DataFrame API (`df.groupBy().pivot()`) and has no SQL syntax equivalent in Spark
- E) The values used as new column headers can be pre-specified or discovered dynamically

---

### Question 33 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: nvl() and nvl2() functions

**Question**:
What is the difference between `nvl(col1, col2)` and `nvl2(col1, col2, col3)` in Spark SQL?

- A) `nvl(col1, col2)` returns `col2` if `col1` is null, otherwise `col1`; `nvl2(col1, col2, col3)` returns `col2` if `col1` is **not** null, otherwise `col3`
- B) They are identical — both return the first non-null argument
- C) `nvl()` works only on numeric types; `nvl2()` works on all types
- D) `nvl2()` returns the second argument when the first is zero, not just null

---

### Question 34 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: map_keys() and map_values()

**Question**:
Given a column `metadata` of type `MapType(StringType, IntegerType)`, which expression extracts all map values as an array?

- A) `col('metadata').values()`
- B) `F.map_values(col('metadata'))`
- C) `F.get_map_values(col('metadata'))`
- D) `F.explode(col('metadata')).value`

---

### Question 35 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: try_cast() / try_divide()

**Question**:
What does `try_cast('abc' AS INT)` return in Spark SQL (Spark 3.4+)?

- A) Raises a `NumberFormatException` and fails the query
- B) Returns `null` instead of raising an error when the cast fails
- C) Returns `0` as a default value for failed integer casts
- D) Truncates the string to produce `0` because `'abc'` starts with no digits

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: LATERAL VIEW explode

**Question**:
What does the following Spark SQL query do?
```sql
SELECT user_id, tag
FROM events
LATERAL VIEW explode(tags) t AS tag
```

- A) It creates a new column `tag` for every element in the `tags` array, producing one row per tag per original row
- B) It filters rows where the `tags` array is empty
- C) It creates a lateral join only for rows where `tags` contains at least 2 elements
- D) It is invalid SQL — `LATERAL VIEW explode` is not supported in Spark SQL

---

### Question 37 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: broadcast hint in SQL

**Scenario**:
```sql
SELECT /*+ BROADCAST(small_table) */ a.order_id, b.product_name
FROM orders a
JOIN products b ON a.product_id = b.product_id
```

**Question**:
What does the `/*+ BROADCAST(small_table) */` hint do?

- A) It changes the join type from inner to broadcast outer join
- B) It instructs the Catalyst optimizer to use a **BroadcastHashJoin** for `products` (the table matching the hint name `small_table` in context), overriding the automatic threshold-based decision
- C) It is ignored — SQL hints are not supported in Spark SQL
- D) It increases the `autoBroadcastJoinThreshold` for this query only

---

### Question 38 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: first(ignorenulls) window function

**Scenario**:
```python
w = Window.partitionBy('dept').orderBy('hire_date')
df.withColumn('first_nonull_score', F.first('score', ignorenulls=True).over(w))
```

**Question**:
What value does `first_nonull_score` contain for rows in a partition where the first few `score` values are null?

- A) `null` — `ignorenulls` has no effect on window functions
- B) The value of `score` for the **first row in the partition that has a non-null score**, applying to all rows up to that point
- C) `0` — the default replacement for ignored nulls
- D) An error is raised because `ignorenulls` is not a supported parameter for `first()` in window context

---

### Question 39 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: schema_of_json() function

**Scenario**:
```python
sample_json = '{"id": 1, "name": "Alice", "scores": [9.5, 8.0]}'
inferred_schema = spark.sql(f"SELECT schema_of_json('{sample_json}')").first()[0]
```

**Question**:
What does `inferred_schema` contain?

- A) A Python `StructType` object
- B) A `StringType` DDL string describing the inferred schema (e.g., `'STRUCT<id: BIGINT, name: STRING, scores: ARRAY<DOUBLE>>'`)
- C) A JSON string representation of the schema
- D) `None` — `schema_of_json()` is not a scalar function and cannot be used in `spark.sql()`

---

### Question 40 — Spark SQL

**Difficulty**: Hard
**Answer Type**: many
**Topic**: window function forall() / exists() on arrays

**(Select all that apply)**
Which of the following correctly describe the `F.forall()` and `F.exists()` higher-order functions in PySpark?

- A) `F.forall(col('scores'), lambda x: x > 0)` returns `True` if **all** elements in the `scores` array satisfy the predicate
- B) `F.exists(col('scores'), lambda x: x > 100)` returns `True` if **at least one** element in `scores` exceeds 100
- C) Both `forall()` and `exists()` return `BooleanType` columns
- D) `F.exists()` is equivalent to using `F.array_contains()` when checking for a specific constant value
- E) If the array column is `null`, both `forall()` and `exists()` return `null`

---

### Question 41 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.describe() method

**Question**:
What does `df.describe('salary', 'age')` return?

- A) A DataFrame with one row per column showing the column's data type
- B) A DataFrame with summary statistics rows (`count`, `mean`, `stddev`, `min`, `max`) for the specified columns
- C) A Python `dict` with statistics for each column
- D) A single row with the mean values of `salary` and `age`

---

### Question 42 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.limit() method

**Question**:
What does `df.limit(50)` do?

- A) Sorts the DataFrame and keeps only the top 50 rows by the default sort order
- B) Returns a new DataFrame with **at most 50 rows**, with no guaranteed ordering
- C) Samples 50 rows uniformly at random from the DataFrame
- D) Limits the number of partitions to 50

---

### Question 43 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: F.create_map() function

**Question**:
What does the following expression produce?
```python
F.create_map(lit('key1'), col('val1'), lit('key2'), col('val2'))
```

- A) A `StructType` column with fields `key1` and `key2`
- B) A `MapType(StringType, <val1_type>)` column with two key-value pairs per row
- C) An `ArrayType` containing alternating keys and values
- D) An error because `create_map` requires an even number of Column arguments of the same type

---

### Question 44 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.printSchema()

**Question**:
What does `df.printSchema()` do?

- A) Returns a `StructType` Python object representing the DataFrame schema
- B) **Prints** the schema tree (column names, types, nullability) to the console and returns `None`
- C) Saves the schema as a JSON file in the working directory
- D) Triggers an action to infer the schema from the underlying data

---

### Question 45 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.sample() method

**Question**:
Which of the following correctly draws an approximate 10% random sample from a DataFrame without replacement?

- A) `df.sample(fraction=0.1, withReplacement=False)`
- B) `df.randomSample(0.1)`
- C) `df.limit(int(df.count() * 0.1))`
- D) `df.select(F.rand()).filter(col('rand') < 0.1)`

---

### Question 46 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.stat.corr() method

**Question**:
What does `df.stat.corr('price', 'quantity')` return?

- A) A DataFrame with both variables' statistics
- B) A Python `float` representing the **Pearson correlation coefficient** between `price` and `quantity`
- C) A `StructType` column with correlation details per row
- D) The slope and intercept of the linear regression between the two columns

---

### Question 47 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.stat.approxQuantile()

**Question**:
Which method computes approximate quantiles (e.g., median, 25th/75th percentile) for a column with a configurable relative error?

- A) `df.stat.quantile('salary', [0.25, 0.5, 0.75], 0.05)`
- B) `df.stat.approxQuantile('salary', [0.25, 0.5, 0.75], 0.05)`
- C) `df.agg(F.percentile_approx('salary', [0.25, 0.5, 0.75]))`
- D) Both B and C are correct; B is the DataFrame stat method, C is the SQL approach

---

### Question 48 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.randomSplit()

**Question**:
What does `train, test = df.randomSplit([0.8, 0.2], seed=42)` do?

- A) Guarantees exactly 80% of rows in `train` and 20% in `test`
- B) Splits the DataFrame into two DataFrames with approximately 80% and 20% of rows, using the seed for reproducibility; actual split may not be exact due to partitioning
- C) Sorts the DataFrame and takes the first 80% as training data
- D) Creates stratified samples by partition

---

### Question 49 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.hint() method

**Question**:
What does `df.hint('repartition', 10)` do?

- A) Immediately repartitions the DataFrame into 10 partitions
- B) Provides a hint to the optimizer to repartition this DataFrame into approximately 10 partitions during query planning
- C) Sets `spark.sql.shuffle.partitions` to 10 for this DataFrame only
- D) Adds a comment to the query plan without affecting execution

---

### Question 50 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.transform() chaining

**Question**:
What is the purpose of `df.transform(func)` in the DataFrame API?

- A) Applies a SQL-string transformation to all columns of the DataFrame
- B) Allows chaining custom DataFrame-level transformation functions cleanly: `func(df)` is called, equivalent to `func(df)` but enables method chaining (e.g., `df.transform(add_columns).transform(clean_data)`)
- C) Applies a Pandas UDF to each row of the DataFrame
- D) Converts the DataFrame into an RDD, applies the function, and converts back

---

### Question 51 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.element_at() function

**Question**:
Given an array column `colors` containing `['red', 'green', 'blue']`, what does `F.element_at(col('colors'), 2)` return?

- A) `'red'` — element at index 0 in Python convention
- B) `'green'` — element at **1-based index** 2 (Spark arrays use 1-based indexing in `element_at`)
- C) `'blue'` — element at 0-based index 2
- D) An error because `element_at` requires a map column

---

### Question 52 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: array set operations

**(Select all that apply)**
Which of the following PySpark functions perform **set-like operations** on array columns?

- A) `F.array_union(col('a'), col('b'))` — returns elements in either array, deduplicated
- B) `F.array_intersect(col('a'), col('b'))` — returns elements present in both arrays, deduplicated
- C) `F.array_except(col('a'), col('b'))` — returns elements in `a` that are **not** in `b`, deduplicated
- D) `F.array_concat(col('a'), col('b'))` — concatenates both arrays including duplicates
- E) `F.array_distinct(col('a'))` — removes duplicate elements from a single array

---

### Question 53 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.sequence() function

**Question**:
What does `F.sequence(lit(1), lit(5))` return?

- A) A `LongType` column containing the single value `5` (end of range)
- B) An `ArrayType(LongType)` column containing `[1, 2, 3, 4, 5]`
- C) A DataFrame with 5 rows numbered 1 through 5
- D) An error because `sequence()` requires a column reference, not literals

---

### Question 54 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: write.insertInto() method

**Question**:
What is the key behavioral difference between `df.write.insertInto('my_table')` and `df.write.mode('append').saveAsTable('my_table')`?

- A) They are identical — both append data to an existing table
- B) `insertInto()` matches columns **by position** (not by name) to the target table's schema; `saveAsTable().mode('append')` matches by column name
- C) `insertInto()` automatically enables schema evolution; `saveAsTable` does not
- D) `insertInto()` can only be used with partitioned tables; `saveAsTable` works with any table

---

### Question 55 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: StructType DDL string schema

**Question**:
Which approach correctly defines a schema from a DDL string?

- A) `StructType.fromDDL('id BIGINT, name STRING, active BOOLEAN')`
- B) `StructType.parse('id BIGINT, name STRING, active BOOLEAN')`
- C) `spark.sql.schema('id BIGINT, name STRING, active BOOLEAN')`
- D) `StructType({'id': 'BIGINT', 'name': 'STRING', 'active': 'BOOLEAN'})`

---

### Question 56 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.observe() method

**Question**:
What is `df.observe('my_metrics', F.count(lit(1)).alias('row_count'), F.sum('revenue').alias('total_revenue'))` used for?

- A) It adds two new columns to the DataFrame on every row
- B) It attaches named **inline metrics** to a query execution; the metrics are collected and accessible through the `QueryExecutionListener` without materializing a separate aggregation step
- C) It creates a streaming listener that monitors the DataFrame in real-time
- D) It triggers an action to compute the metrics immediately

---

### Question 57 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.stat.freqItems() method

**Question**:
What does `df.stat.freqItems(['category', 'region'], support=0.01)` return?

- A) The exact frequency distribution of each column as a dictionary
- B) A DataFrame with one row containing arrays of **approximate frequent items** (values that appear in at least 1% of rows) for each specified column
- C) The count of distinct values for `category` and `region`
- D) A pivot table showing frequencies per category-region combination

---

### Question 58 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: MapType schema construction

**Question**:
Which expression correctly defines a schema field `attributes` of type `MAP<STRING, DOUBLE>`?

- A) `StructField('attributes', MapType(StringType(), DoubleType()), True)`
- B) `StructField('attributes', {'STRING': 'DOUBLE'}, True)`
- C) `StructField('attributes', 'MAP<STRING, DOUBLE>', True)`
- D) `MapField('attributes', key=StringType(), value=DoubleType())`

---

### Question 59 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: df.write.format() generic API

**(Select all that apply)**
Which of the following are equivalent ways to write a DataFrame as Parquet?

- A) `df.write.parquet('/output/path')`
- B) `df.write.format('parquet').save('/output/path')`
- C) `df.write.format('parquet').option('path', '/output/path').saveAsTable('my_table')`
- D) `df.write.save('/output/path', format='parquet')`
- E) `df.write.format('parquet').mode('overwrite').save('/output/path')`

---

### Question 60 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.zip_with() function

**Question**:
What does the following expression do?
```python
F.zip_with(col('a'), col('b'), lambda x, y: x + y)
```

- A) Concatenates arrays `a` and `b` end-to-end
- B) Returns an array where each element is the **element-wise sum** of corresponding elements from arrays `a` and `b`
- C) Returns a map from elements of `a` to elements of `b`
- D) Filters both arrays to only include elements present in both

---

### Question 61 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: bucketBy and sortBy write

**Scenario**:
```python
df.write \
  .bucketBy(16, 'user_id') \
  .sortBy('event_time') \
  .saveAsTable('events_bucketed')
```

**Question**:
What does this write operation accomplish, and what join optimization does it enable?

- A) It partitions data into 16 directories by `user_id` hash, enabling partition pruning on reads
- B) It writes data into **16 fixed bucket files** grouped by hash of `user_id`, sorted within each bucket by `event_time`; joining two tables bucketed on the same key with the same number of buckets can **eliminate the shuffle** in SortMergeJoin
- C) It adds a `bucket_id` column to the data that enables fast lookups
- D) It creates 16 Parquet row groups sorted by `user_id` and `event_time` within each file

---

### Question 62 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.localCheckpoint() vs df.checkpoint()

**Question**:
How does `df.localCheckpoint()` differ from `df.checkpoint()`?

- A) `localCheckpoint()` saves to the checkpoint directory on HDFS; `checkpoint()` saves only to local Executor disk
- B) `localCheckpoint()` stores data on **local Executor disk** (faster, but not fault-tolerant across Executor failure); `checkpoint()` stores on **reliable distributed storage** (slower, but survives Executor failure)
- C) They are identical — the `local` prefix only affects the truncation of lineage
- D) `localCheckpoint()` does not truncate lineage; `checkpoint()` does

---

### Question 63 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.map_filter() function

**Question**:
What does the following expression produce?
```python
F.map_filter(col('scores'), lambda k, v: v > 50)
```
where `scores` is a `MapType(StringType, IntegerType)`.

- A) A filtered array of values greater than 50
- B) A new `MapType` column containing only the **key-value pairs** where the value exceeds 50
- C) A `BooleanType` column indicating whether any map value exceeds 50
- D) An error because `map_filter` operates on `ArrayType` columns only

---

### Question 64 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: na.replace() method

**Scenario**:
```python
df.na.replace(['N/A', 'Unknown', ''], 'missing', subset=['status'])
```

**Question**:
What does this operation do?

- A) Replaces `null` values in `status` with the string `'missing'`
- B) Replaces the specified string values (`'N/A'`, `'Unknown'`, `''`) in the `status` column with `'missing'`; it does **not** affect null values
- C) Replaces all string columns in the DataFrame with `'missing'` when any value matches
- D) An error is raised because `na.replace()` only works with numeric columns

---

### Question 65 — DataFrame API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: df.stat.crosstab() method

**(Select all that apply)**
Which of the following are true about `df.stat.crosstab('gender', 'education_level')`?

- A) It returns a DataFrame where rows represent distinct values of `gender` and columns represent distinct values of `education_level`
- B) Each cell contains the **count** of rows with that specific combination of `gender` and `education_level`
- C) The first column of the result is named `gender_education_level`
- D) It requires the two columns to be of the same data type
- E) The result is equivalent to `df.groupBy('gender', 'education_level').count()` pivoted on `education_level`

---

### Question 66 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.arrays_zip() function

**Question**:
A DataFrame has two array columns `names` and `scores`, each with the same number of elements. What does `F.arrays_zip(col('names'), col('scores'))` return?

- A) A new `ArrayType` that concatenates both arrays end-to-end
- B) An `ArrayType(StructType)` column where each element is a struct `{names: ..., scores: ...}` pairing corresponding elements by position
- C) A `MapType(StringType, IntegerType)` keyed by `names` with `scores` as values
- D) An error if any element in either array is null

---

### Question 67 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.slice() function

**Question**:
What does `F.slice(col('items'), start=2, length=3)` return for the array `['a', 'b', 'c', 'd', 'e']`?

- A) `['a', 'b', 'c']` — first 3 elements
- B) `['b', 'c', 'd']` — 3 elements starting at **1-based index** 2
- C) `['c', 'd', 'e']` — last 3 elements
- D) `['b', 'c']` — from index 2 up to (but not including) index 3

---

### Question 68 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.summary() vs df.describe()

**Question**:
How does `df.summary()` differ from `df.describe()`?

- A) They are identical
- B) `df.summary()` additionally includes approximate **quartile statistics** (`25%`, `50%`, `75%`) and `count` of missing values; `df.describe()` only provides `count`, `mean`, `stddev`, `min`, `max`
- C) `df.describe()` includes quartiles; `df.summary()` does not
- D) `df.summary()` only works on numeric columns; `df.describe()` works on string columns too

---

### Question 69 — DataFrame API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: reading Delta format

**(Select all that apply)**
Which of the following are true about reading Delta Lake tables in PySpark?

- A) Delta tables can be read with `spark.read.format('delta').load('/path/to/delta')`
- B) `spark.read.table('my_delta_table')` works for Delta tables registered in the metastore
- C) Delta Lake supports **time travel**: reading a specific version with `.option('versionAsOf', 2)` or `.option('timestampAsOf', '2024-01-01')`
- D) Delta tables can only be read using `spark.sql()` and not the DataFrame API
- E) Delta format natively enforces schema and prevents writing incompatible data without explicit schema evolution options

---

### Question 70 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.writeTo() API

**Question**:
What is the primary advantage of using `df.writeTo('catalog.schema.table')` (the v2 write API) over the older `df.write.saveAsTable('table')`?

- A) `writeTo()` is faster because it bypasses the Catalyst optimizer
- B) `writeTo()` supports **table-level operations** such as `.append()`, `.overwritePartitions()`, `.createOrReplace()`, and `.replace()` in a table-format-aware way; it is required for operations like `MERGE` and `REPLACE WHERE` with catalog v2 sources
- C) `writeTo()` automatically infers the output format; `saveAsTable()` always writes Parquet
- D) They are identical; `writeTo()` is only an alias introduced for readability

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: EXPLAIN FORMATTED output

**Question**:
What does `df.explain('formatted')` provide compared to `df.explain()` (default physical plan)?

- A) It shows only the unresolved logical plan
- B) It shows the **physical plan** with a more readable, formatted layout that includes node IDs, subplan details, and argument summaries — making complex plans easier to navigate
- C) It shows the generated Java bytecode from WholeStageCodegen
- D) It shows cost estimates in CPU cycles and I/O bytes

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.conf.set() at runtime

**Question**:
Which of the following correctly changes the number of shuffle partitions to 50 **for the current SparkSession at runtime**?

- A) `spark.conf.set('spark.sql.shuffle.partitions', 50)`
- B) `spark.sql.shuffle.partitions = 50`
- C) `spark.sparkContext.conf.set('spark.sql.shuffle.partitions', 50)`
- D) `spark.conf.set('spark.shuffle.partitions', 50)`

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: CBO join reordering

**Question**:
When the **Cost-Based Optimizer (CBO)** is enabled and statistics are collected, what additional join optimization can Spark perform beyond rule-based decisions?

- A) It converts all `SortMergeJoin` operations to `BroadcastHashJoin`
- B) It **reorders the sequence of joins** in a multi-table query to minimize the size of intermediate results, choosing the join order that minimizes estimated total data processed
- C) It automatically partitions tables to align shuffle boundaries
- D) It converts correlated subqueries to lateral joins automatically

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: adaptive skew join threshold

**Question**:
Which configuration property sets the **byte threshold** above which a shuffle partition is considered skewed for AQE's skew join handling?

- A) `spark.sql.adaptive.skewJoin.skewedPartitionFactor`
- B) `spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes`
- C) `spark.sql.adaptive.maxSkewedPartitionSize`
- D) `spark.sql.adaptive.skewJoin.maxSkewBytes`

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.locality.wait

**Question**:
What does `spark.locality.wait` control, and what is the performance tradeoff?

- A) The time Spark waits for a straggler Task before launching a speculative copy
- B) The time Spark waits before relaxing **data locality requirements** and scheduling a Task on a less-local node; increasing it improves data locality but may delay task launch when preferred nodes are busy
- C) The timeout for the Driver to wait for Executor heartbeats before declaring an Executor lost
- D) The interval between attempts to fetch shuffle blocks from remote Executors

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: many
**Topic**: EXPLAIN CODEGEN

**(Select all that apply)**
Which of the following statements about `EXPLAIN CODEGEN` (or `df.explain('codegen')`) are true?

- A) It shows the **generated Java source code** produced by WholeStageCodegen for each code-generated stage
- B) It identifies which stages did **not** benefit from code generation (shown without a `*` prefix in the physical plan)
- C) It shows the SQL query text reformatted to conform to Spark SQL standards
- D) It is useful for diagnosing whether complex expressions are preventing code generation
- E) `df.explain('codegen')` is equivalent to `spark.range(1).select(df).explain('codegen')`

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.shuffle.compress

**Question**:
What does enabling `spark.shuffle.compress = true` (the default) do?

- A) Compresses shuffle **input** data before sending it to Executors
- B) Compresses shuffle **output files** written to disk during the shuffle map phase, reducing disk I/O at the cost of CPU for compression/decompression
- C) Compresses broadcast variables to reduce network transfer size
- D) Compresses task serialization payloads sent from the Driver

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: io.compression.codec selection

**Question**:
Which compression codec is the **default** for Spark shuffle block compression (`spark.io.compression.codec`)?

- A) `gzip`
- B) `bzip2`
- C) `lz4`
- D) `snappy`

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: executor cores and HDFS throughput tradeoff

**Scenario**:
A cluster has 20 nodes, each with 16 CPU cores and 64 GB RAM. A developer sets `spark.executor.cores = 16` to maximize parallelism, using 1 Executor per node.

**Question**:
What performance problem is most likely to occur?

- A) Executors will run out of JVM heap space immediately
- B) All 16 Tasks on a single Executor share the same HDFS client, causing **HDFS throughput bottlenecks** because the HDFS client is not designed for high concurrent thread counts; GC pressure also increases with large numbers of concurrent Tasks sharing one JVM heap
- C) The Driver will be overloaded tracking 16 Tasks per Executor
- D) WholeStageCodegen cannot generate code for 16-core Executors

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: many
**Topic**: skewed join tuning options

**(Select all that apply)**
Which of the following approaches can mitigate the effects of a **data skew** problem in a large join?

- A) Enable AQE (`spark.sql.adaptive.enabled = true`) and `spark.sql.adaptive.skewJoin.enabled = true` to let AQE automatically split skewed partitions
- B) Apply **salting** — add a random key to the large table and replicate the small table accordingly, then join on the composite key
- C) Use `repartition(n, joinKey)` before the join to redistribute data more evenly
- D) Increase `spark.sql.shuffle.partitions` to 2000 — more partitions reduce per-partition skew severity
- E) Use `broadcast()` hint for the smaller table if it fits in memory, converting SortMergeJoin to BroadcastHashJoin (which avoids skew entirely)

---

### Question 81 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: trigger processingTime

**Question**:
What does `trigger(processingTime='30 seconds')` do in a Structured Streaming query?

- A) Sets a 30-second watermark for late data handling
- B) Starts a new micro-batch **every 30 seconds** (or when the previous batch finishes, whichever is later), processing all data accumulated since the last batch
- C) Stops the stream after 30 seconds of operation
- D) Limits each micro-batch to at most 30 seconds of event-time data

---

### Question 82 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: maxFilesPerTrigger option

**Question**:
What does `.option('maxFilesPerTrigger', 5)` do when reading from a file-based streaming source?

- A) Limits the total number of files in the source directory to 5
- B) Limits the number of **new files** processed per micro-batch to 5, controlling ingestion rate
- C) Sets the maximum file size per trigger to 5 MB
- D) Starts 5 parallel file readers per trigger

---

### Question 83 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: streaming join with static DataFrame

**Question**:
Which of the following correctly describes a **stream-static join** in Structured Streaming?

- A) Both sides of the join must be streaming DataFrames
- B) A streaming DataFrame can be joined with a **static** (batch) DataFrame; the static side is evaluated once per micro-batch using the current snapshot of the data
- C) Stream-static joins are not supported — all joins in Structured Streaming must use two streaming sources
- D) Stream-static joins require a watermark on the static DataFrame

---

### Question 84 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: query.status property

**Question**:
What does `query.status` return for a running Structured Streaming query?

- A) A `StreamingQueryProgress` object with metrics from the last completed micro-batch
- B) A Python `dict` describing the **current query state** (e.g., whether it is initialized, processing data, or waiting for data)
- C) An integer representing the number of completed micro-batches
- D) A `DataFrame` of all rows produced by the last micro-batch

---

### Question 85 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Kafka startingOffsets option

**Question**:
In Structured Streaming with Kafka, what does `.option('startingOffsets', 'earliest')` do?

- A) Starts consuming Kafka topic data from the **checkpoint-saved offset** on restarts; falls back to the earliest offset when no checkpoint exists
- B) Starts consuming from the **very beginning** of the Kafka topic (offset 0 for each partition) when no checkpoint exists; on restart, uses the checkpoint offset
- C) Starts from the latest available message, ignoring all historical data
- D) Starts from the earliest offset and never advances, re-reading all messages on every batch

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: many
**Topic**: streaming dropDuplicates with watermark

**(Select all that apply)**
Which of the following statements about `dropDuplicates()` in Structured Streaming are correct?

- A) `dropDuplicates()` in streaming requires a **watermark** so Spark knows when it is safe to drop the deduplication state for old event IDs
- B) Without a watermark, Spark must maintain deduplication state **forever**, which causes unbounded state growth
- C) `dropDuplicates()` in streaming guarantees exactly-once output with any output sink
- D) `dropDuplicates(['event_id', 'event_time'])` in streaming with a watermark on `event_time` drops state for events whose `event_time` falls below the watermark threshold
- E) `dropDuplicates()` is only supported in `append` output mode in streaming

---

### Question 87 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: continuous processing trigger

**Question**:
What is **Continuous Processing** in Structured Streaming (triggered with `trigger(continuous='1 second')`)?

- A) An alias for `trigger(processingTime='1 second')` — processes data every second
- B) A low-latency execution mode that runs Tasks continuously without waiting for a micro-batch boundary, achieving sub-second latency; checkpoints at the specified interval rather than per batch
- C) A mode that processes data exactly once per second regardless of how much data is available
- D) A mode that continuously replays checkpointed data for debugging

---

### Question 88 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: query.lastProgress

**Scenario**:
After a streaming query runs for several batches, a developer calls `query.lastProgress`.

**Question**:
What does `query.lastProgress` return?

- A) A Python list of all batch progress records since the query started
- B) A Python `dict` (JSON-like object) with metrics from the **most recently completed micro-batch**, including batch ID, input rows, processing time, and sink metadata
- C) A `DataFrame` with one row per processed micro-batch
- D) The offset information for the most recently committed Kafka/file source

---

### Question 89 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: streaming sort limitation

**Scenario**:
A developer writes:
```python
df.writeStream \
  .orderBy('event_time') \
  .format('console') \
  .start()
```

**Question**:
What happens when this streaming query is started?

- A) Results are sorted by `event_time` within each micro-batch before being written to the console
- B) An `AnalysisException` is raised because **global sort (`orderBy`)** is not supported in Structured Streaming — Spark cannot globally order an unbounded stream
- C) `orderBy` is silently ignored in streaming mode and data is output in arrival order
- D) The query sorts within each partition but not globally

---

### Question 90 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: many
**Topic**: mapGroupsWithState concept

**(Select all that apply)**
Which of the following correctly describe **`mapGroupsWithState`** in Structured Streaming?

- A) It allows arbitrary stateful processing by maintaining a user-defined state object per group key across micro-batches
- B) It is available in the Python (PySpark) API via `applyInPandasWithState`
- C) It requires a watermark to be defined; the watermark determines when group state is automatically evicted
- D) Unlike `flatMapGroupsWithState`, `mapGroupsWithState` must emit exactly one output row per group per batch
- E) It processes only new data in each micro-batch; historical state must be reloaded from the checkpoint on restart

---

### Question 91 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: SparkSession.builder.remote() pattern

**Question**:
Which of the following correctly creates a Spark Connect client session connecting to a local Spark Connect server?

- A) `SparkSession.builder.master('sc://localhost').getOrCreate()`
- B) `SparkSession.builder.remote('sc://localhost').getOrCreate()`
- C) `SparkSession.builder.config('spark.connect.url', 'sc://localhost').getOrCreate()`
- D) `SparkSession.builder.connect('localhost:15002').getOrCreate()`

---

### Question 92 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: client-side plan construction

**Question**:
In Spark Connect, when does the **logical plan** get evaluated (i.e., when are analysis errors such as missing columns raised)?

- A) When the DataFrame is defined (e.g., `df.filter(...)`) — the client validates the plan immediately
- B) When an **action** is triggered (e.g., `.count()`, `.show()`, `.collect()`) — the plan is sent to the server, analyzed there, and any errors are returned to the client
- C) During Python garbage collection when the DataFrame object is deleted
- D) Only when the SparkSession is stopped

---

### Question 93 — Spark Connect

**Difficulty**: Medium
**Answer Type**: many
**Topic**: Spark Connect in Databricks serverless

**(Select all that apply)**
Which of the following are correct about **Spark Connect in Databricks** Serverless notebooks?

- A) Databricks Serverless uses Spark Connect as the underlying communication protocol between the notebook client and the remote Spark cluster
- B) In Serverless mode, `SparkContext` and the RDD API are unavailable because the Spark Connect client does not expose them
- C) Users can access `spark.sparkContext` normally in Databricks Serverless — Spark Connect is transparent
- D) Spark Connect allows the notebook process to be isolated from the Spark Driver, improving reliability and enabling instant cluster provisioning
- E) Databricks Serverless supports multi-language notebooks (Python, Scala, SQL) via a single Spark Connect server

---

### Question 94 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: gRPC channel SSL configuration

**Question**:
When connecting to a Spark Connect server over a secure network, how is **TLS/SSL** typically configured in the connection URL?

- A) By adding `;ssl=true` to the `sc://` connection string: `sc://hostname:15002/;use_ssl=true`
- B) By setting `spark.connect.ssl.enabled = true` in the SparkSession config
- C) SSL is always enabled and cannot be disabled in Spark Connect
- D) SSL must be configured separately via the gRPC channel options outside the connection URL

---

### Question 95 — Spark Connect

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark Connect multi-language support

**Scenario**:
A data engineering team wants to allow Rust-based microservices to submit Spark queries directly to a shared Spark cluster without deploying a JVM on each microservice.

**Question**:
How does **Spark Connect** enable this use case?

- A) Rust microservices must use Py4J to call the PySpark API, which bridges to the Spark Connect server
- B) Spark Connect exposes a **language-agnostic gRPC API** with Protocol Buffers schema; any language with a gRPC client library (including Rust) can build a Spark Connect client, submit logical plans, and receive Arrow-encoded results — no local JVM required
- C) Rust is not supported — Spark Connect only has official clients for Python, Scala, and Java
- D) Spark Connect requires the client process to run on the same JVM version as the Spark cluster

---

### Question 96 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: ps.merge() function

**Question**:
Which of the following correctly performs an inner merge of two `pyspark.pandas` DataFrames `left_psdf` and `right_psdf` on the column `user_id`?

- A) `ps.merge(left_psdf, right_psdf, on='user_id', how='inner')`
- B) `left_psdf.merge(right_psdf, on='user_id', how='inner')`
- C) `left_psdf.join(right_psdf, on='user_id')`
- D) Both A and B are correct

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ops_on_diff_frames configuration

**Question**:
Why does the following code raise an error by default in `pyspark.pandas`?
```python
psdf1 = ps.DataFrame({'a': [1, 2, 3]})
psdf2 = ps.DataFrame({'b': [4, 5, 6]})
result = psdf1['a'] + psdf2['b']
```

- A) Integer addition is not supported between `pyspark.pandas` Series
- B) By default, operations between **different** `pyspark.pandas` DataFrames are disallowed because they require a join under the hood; set `ps.set_option('compute.ops_on_diff_frames', True)` to enable
- C) The `+` operator is not overloaded for `pyspark.pandas` Series
- D) Addition across DataFrames requires both to have the same number of partitions

---

### Question 98 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.read_csv() on cloud storage

**Question**:
Which of the following correctly reads a CSV file from an S3 path using the `pyspark.pandas` API?

- A) `ps.read_csv('s3://my-bucket/data/file.csv')`
- B) `ps.read_csv(open('s3://my-bucket/data/file.csv'))`
- C) `ps.from_pandas(pd.read_csv('s3://my-bucket/data/file.csv'))`
- D) `ps.read_csv` does not support cloud paths; use `spark.read.csv()` instead

---

### Question 99 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: many
**Topic**: ps.get_dummies() behavior

**(Select all that apply)**
Which of the following are correct about `ps.get_dummies(psdf, columns=['color'])` in `pyspark.pandas`?

- A) It performs **one-hot encoding**, creating a new boolean column for each distinct value in `color`
- B) The result is a `pyspark.pandas` DataFrame — data remains distributed on the cluster
- C) It is equivalent to calling `pd.get_dummies()` on a locally collected pandas DataFrame
- D) The output columns are named `color_<value>` (e.g., `color_red`, `color_blue`)
- E) `drop_first=True` can be passed to drop the first dummy column to avoid multicollinearity

---

### Question 100 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ps.options.compute.shortcut_limit

**Scenario**:
A developer calls `len(psdf)` on a large `pyspark.pandas` DataFrame and notices it executes instantly. On a much larger DataFrame, the same call triggers a full Spark job.

**Question**:
Why does `len()` sometimes return instantly and sometimes trigger a job?

- A) `len()` uses the schema metadata which always has the row count pre-cached
- B) `pyspark.pandas` has a `compute.shortcut_limit` option (default: 1000); if the DataFrame has fewer rows than this limit, Spark collects the data eagerly and caches it for instant access; above the limit, a full distributed count job is triggered
- C) `len()` on a `pyspark.pandas` DataFrame always triggers a full job; the instant result is from Python's object cache
- D) Spark caches the row count in the metastore after the first `len()` call and returns it immediately on subsequent calls

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | RDD lineage is a chain of parent RDD dependencies; DataFrame lineage is stored as an optimized logical plan that passes through Catalyst before execution. | topic1-prompt1-spark-architecture.md | Easy |
| 2 | B | WholeStageCodegen fuses multiple operators in a pipeline stage into a single optimized JVM bytecode function, eliminating virtual function call overhead. | topic1-prompt1-spark-architecture.md | Easy |
| 3 | B | Tungsten optimizes CPU and memory efficiency through binary data encoding, cache-aware computation, and runtime code generation. | topic1-prompt1-spark-architecture.md | Easy |
| 4 | B | The external shuffle service stores shuffle intermediate files on worker nodes and serves them to downstream Tasks, allowing Executors to be removed by DRA without losing shuffle data. | topic1-prompt1-spark-architecture.md | Easy |
| 5 | B | `checkpoint()` writes to reliable distributed storage and truncates the lineage; `persist()` caches in memory/disk but retains the full lineage. | topic1-prompt1-spark-architecture.md | Easy |
| 6 | B | A Task payload is the function (closure) plus any referenced variables captured in the closure, serialized using Java serialization or Kryo and sent to Executors. | topic1-prompt1-spark-architecture.md | Easy |
| 7 | B | Kryo is generally faster and more compact than Java serialization; it is configured via `spark.serializer = org.apache.spark.serializer.KryoSerializer`. | topic1-prompt1-spark-architecture.md | Easy |
| 8 | A | Both `spark.memory.offHeap.enabled = true` AND `spark.memory.offHeap.size` set to a positive value are required to enable off-heap memory. | topic1-prompt7-garbage-collection.md | Easy |
| 9 | B | When an Executor heartbeat times out, the Driver marks it lost, reschedules its Tasks on other Executors, and requests a replacement from the Cluster Manager. | topic1-prompt1-spark-architecture.md | Easy |
| 10 | B | A separate Python worker process is spawned per Executor thread for UDF execution; data is pickled and exchanged via a local socket between the JVM and the Python process. | topic1-prompt1-spark-architecture.md | Medium |
| 11 | A, B, C, E | Standalone uses Master/Worker (A); YARN uses ResourceManager/NodeManager (B); Kubernetes uses Pods (C); all three support both client and cluster deploy modes (E). D is wrong — Standalone does not require Hadoop. | topic1-prompt1-spark-architecture.md | Medium |
| 12 | B | PROCESS_LOCAL means the data is in the same Executor JVM process — the highest (fastest) data locality level. | topic1-prompt1-spark-architecture.md | Easy |
| 13 | C | `spark.speculation.multiplier` controls how much slower (as a multiple of the median task duration) a task must be before a speculative copy is launched. | topic1-prompt1-spark-architecture.md | Medium |
| 14 | A, C, D, E | Resource Profiles allow different stage configurations (A); attached via `rdd.withResources()` (C); support GPU resource requests (D); default profile applies to all unassigned stages (E). B is wrong — Kubernetes also supports Resource Profiles. | topic1-prompt1-spark-architecture.md | Medium |
| 15 | B | Rack awareness uses cluster topology to prefer scheduling Tasks on RACK_LOCAL nodes when PROCESS_LOCAL and NODE_LOCAL are unavailable, reducing cross-rack network traffic. | topic1-prompt1-spark-architecture.md | Medium |
| 16 | B | 2 Stages: Stage 1 handles scan + broadcast join + partial aggregation; Stage 2 handles the shuffle exchange + final aggregation + write. The broadcast join does not add a stage boundary. | topic1-prompt4-shuffling-performance.md | Medium |
| 17 | B | Python worker processes consume memory outside the JVM heap (off-heap). Increasing `spark.executor.memoryOverhead` allocates more off-heap memory for Python workers. | topic1-prompt7-garbage-collection.md | Medium |
| 18 | A, B, C, D | `cancelJob(jobId)` (A), `cancelAllJobs()` (B), `cancelJobGroup(groupId)` (C), and `spark.stop()` (D) all terminate running jobs. E (`cancelStage`) cancels a Stage, not a Job. | topic1-prompt1-spark-architecture.md | Medium |
| 19 | B | In the unified memory model, when Execution Memory demand exceeds available space, Spark evicts (spills) cached Storage blocks to free room for the join/shuffle operation. | topic1-prompt7-garbage-collection.md | Medium |
| 20 | B | `spark.reducer.maxSizeInFlight` limits how much shuffle data a reduce-side Task fetches concurrently from remote Executors, controlling reducer-side memory pressure. | topic1-prompt4-shuffling-performance.md | Hard |
| 21 | B | `F.from_json(col('payload'), schema)` parses a JSON string column into a StructType column according to the provided schema. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 22 | B | `F.struct()` returns a StructType column with named fields — here a struct with `first_name` and `last_name` string fields. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 23 | B | `F.to_timestamp(col('event_ts'), 'yyyy-MM-dd HH:mm:ss')` parses the string with an explicit format mask. `F.cast()` does not exist as a standalone function; the column method `col.cast('timestamp')` is used instead. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 24 | B | `F.greatest()` returns the largest non-null value across the specified columns for each row. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 25 | A | `F.transform(col('scores'), lambda x: x * 1.1)` applies the lambda to each element in the array and returns a new array with each value multiplied by 1.1. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 26 | B | `F.filter(col('tags'), lambda x: x.startswith('a'))` filters array elements to those beginning with 'a'. The correct PySpark higher-order function is `F.filter()`, not `F.array_filter()`. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 27 | B | `F.aggregate(col('scores'), lit(0), lambda acc, x: acc + x)` computes the running sum of the array starting from 0 as the initial accumulator. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 28 | B | `F.flatten()` concatenates all inner arrays of an `ArrayType(ArrayType)` column into a single flat `ArrayType`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 29 | B | `F.months_between()` returns a `DoubleType` representing the fractional number of months between two dates; the result can be negative if the first date is earlier. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 30 | B | `F.lpad(col('id'), 6, '0')` pads zeros on the LEFT to reach a total length of 6 — so 42 becomes '000042'. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 31 | B | `F.posexplode()` behaves like `explode()` but also returns a `pos` column with the zero-based integer index of each element. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 32 | A, B, C, E | PIVOT rotates row values into columns (A); uses `FOR col IN (values)` syntax (B); requires an aggregate function (C); pivot values can be pre-specified or discovered dynamically via the DataFrame API (E). D is wrong — Spark SQL does have PIVOT syntax. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 33 | A | `nvl(col1, col2)` returns col2 if col1 is null; `nvl2(col1, col2, col3)` returns col2 if col1 is NOT null, otherwise col3. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 34 | B | `F.map_values(col('metadata'))` extracts the values of a MapType column as an ArrayType. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 35 | B | `try_cast()` returns null instead of raising an exception when the cast fails, enabling safe type conversion without error handling. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 36 | A | `LATERAL VIEW explode(tags) t AS tag` creates one row per element in the `tags` array per original row, adding a `tag` column with each individual value. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 37 | B | The `/*+ BROADCAST(small_table) */` hint instructs Catalyst to use BroadcastHashJoin for the hinted table, overriding the autoBroadcastJoinThreshold-based decision. | topic2-prompt11-query-optimization.md | Medium |
| 38 | B | With `first(ignorenulls=True)` in a window function, Spark returns the value of the first non-null `score` within the partition, skipping null entries. | topic2-prompt10-window-functions.md | Medium |
| 39 | B | `F.schema_of_json()` returns a StringType DDL string (e.g., `'STRUCT<id: BIGINT, name: STRING>'`) describing the inferred schema of a JSON string. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 40 | A, B, C, E | `F.forall()` returns true if all elements satisfy the predicate (A); `F.exists()` returns true if at least one element satisfies it (B); both return BooleanType (C); a null array returns null for both (E). D overstates equivalence — `exists` supports arbitrary predicates while `array_contains` is limited to equality. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 41 | B | `df.describe()` returns a DataFrame with summary statistic rows: count, mean, stddev, min, and max for each numeric or string column. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 42 | B | `df.limit(50)` returns a new DataFrame with at most 50 rows; there is no guaranteed ordering unless `orderBy` is also applied. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 43 | B | `F.create_map()` returns a MapType column with key-value pairs built from the supplied alternating key and value expressions. | topic3-prompt13-column-manipulation-expressions.md | Easy |
| 44 | B | `df.printSchema()` prints the schema tree to the console (stdout) and returns None — it is a side-effect method, not a transformation. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 45 | A | `df.sample(fraction=0.1, withReplacement=False)` is the correct syntax for sampling approximately 10% of rows without replacement. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 46 | B | `df.stat.corr('col_a', 'col_b')` returns a Python float representing the Pearson correlation coefficient between the two columns. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 47 | D | Both `df.stat.approxQuantile('salary', [0.25, 0.5, 0.75], 0.05)` (B) and `df.agg(F.percentile_approx('salary', [0.25, 0.5, 0.75]))` (C) are valid — B is the dedicated stat method, C is the SQL aggregate approach. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 48 | B | `df.randomSplit([0.8, 0.2], seed=42)` produces approximately 80%/20% splits; the seed ensures reproducibility but the actual counts may not be exact due to partition boundaries. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 49 | B | `df.hint('repartition', 10)` provides an advisory hint to the Catalyst optimizer; it is not an immediate repartition action but a suggestion that may or may not be honored. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 50 | B | `df.transform(func)` calls `func(df)` and returns the result, enabling clean method chaining of custom DataFrame-level transformation functions. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 51 | B | `F.element_at()` uses 1-based indexing; index 2 on `['red', 'green', 'blue']` returns `'green'`. | topic3-prompt13-column-manipulation-expressions.md | Easy |
| 52 | A, B, C, E | `F.array_union()` (A), `F.array_intersect()` (B), `F.array_except()` (C), and `F.array_distinct()` (E) all exist in PySpark. D is wrong — `F.array_concat()` does not exist; the correct function for array concatenation is `F.concat()`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 53 | B | `F.sequence(lit(1), lit(5))` returns an `ArrayType(LongType)` column containing `[1, 2, 3, 4, 5]`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 54 | B | `insertInto()` matches columns by position (schema-position-based insert); `saveAsTable().mode('append')` matches columns by name, making it safer for schema changes. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 55 | A | `StructType.fromDDL('id BIGINT, name STRING, active BOOLEAN')` parses a DDL string into a StructType, providing a concise alternative to building StructType with StructField objects. | topic3-prompt21-schemas-data-types.md | Medium |
| 56 | B | `df.observe('my_metrics', ...)` attaches inline named metrics collected during query execution via a `QueryExecutionListener`, without triggering a separate aggregation job. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 57 | B | `df.stat.freqItems(['col'])` returns a DataFrame with one row containing arrays of approximate frequent items for each specified column. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 58 | A | `StructField('attributes', MapType(StringType(), DoubleType()), True)` correctly defines a nullable MapType field with string keys and double values. | topic3-prompt21-schemas-data-types.md | Medium |
| 59 | A, B, E | `df.write.parquet('/output/path')` (A), `df.write.format('parquet').save('/output/path')` (B), and `df.write.format('parquet').mode('overwrite').save('/output/path')` (E) are all valid. D is invalid — `save()` does not accept `format=` as a keyword argument. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 60 | B | `F.zip_with()` returns an array where each element is computed by applying the lambda element-wise to corresponding positions in both input arrays. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 61 | B | `bucketBy(16, 'user_id')` writes data into 16 fixed bucket files grouped by hash of `user_id`, sorted within each bucket by `event_time`; joining two tables bucketed identically on the same key can eliminate the shuffle in SortMergeJoin. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 62 | B | `localCheckpoint()` stores data on local Executor disk (faster but not fault-tolerant across Executor failure); `checkpoint()` stores on reliable distributed storage (slower but survives Executor failure). | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 63 | B | `F.map_filter()` returns a new MapType column containing only the key-value pairs where the predicate returns true — here, only entries with value > 50. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 64 | B | `df.na.replace()` replaces specified string values with a replacement; it does NOT affect null values (use `df.na.fill()` for nulls). | topic3-prompt16-handling-nulls.md | Hard |
| 65 | A, B, C, E | Rows represent distinct gender values (A); cells contain the count of that combination (B); first column is named `gender_education_level` (C); equivalent to `groupBy().count()` pivoted on education_level (E). D is wrong — the columns do not need to be the same data type. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 66 | B | `F.arrays_zip()` returns an `ArrayType(StructType)` column where each element is a struct pairing corresponding elements from both input arrays by position. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 67 | B | `F.slice()` uses 1-based indexing; `start=2, length=3` on `['a','b','c','d','e']` returns `['b','c','d']`. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 68 | B | `df.summary()` additionally includes approximate quartile statistics (`25%`, `50%`, `75%`); `df.describe()` only provides count, mean, stddev, min, and max. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 69 | A, B, C, E | Delta tables can be read with `format('delta').load()` (A) or `spark.read.table()` for metastore-registered tables (B); time travel is supported via `versionAsOf`/`timestampAsOf` (C); Delta enforces schema by default (E). D is wrong — the DataFrame API fully supports reading Delta. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 70 | B | `writeTo()` supports table-level operations (`.append()`, `.overwritePartitions()`, `.createOrReplace()`, `.replace()`) in a table-format-aware way and is required for catalog v2 operations. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 71 | B | `df.explain('formatted')` shows the physical plan with a more readable, formatted layout that includes node IDs, subplan details, and argument summaries — making complex plans easier to navigate than the default. | topic4-prompt26-debugging.md | Easy |
| 72 | A | `spark.conf.set('spark.sql.shuffle.partitions', 50)` is the correct way to change shuffle partition count for the current SparkSession at runtime. | topic4-prompt24-performance-tuning.md | Easy |
| 73 | B | When CBO is enabled and statistics are collected, Spark can reorder the sequence of joins in a multi-table query to minimize the size of intermediate results. | topic4-prompt24-performance-tuning.md | Medium |
| 74 | B | `spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes` sets the byte threshold above which a shuffle partition is considered skewed for AQE's skew join splitting. | topic4-prompt24-performance-tuning.md | Medium |
| 75 | B | `spark.locality.wait` controls how long Spark waits before relaxing data locality requirements; increasing it improves data locality but may delay task launch when preferred nodes are busy. | topic4-prompt24-performance-tuning.md | Medium |
| 76 | A, B, D | `EXPLAIN CODEGEN` shows the generated Java source code from WholeStageCodegen (A); identifies stages without code generation (B); diagnoses expressions that block codegen (D). C is wrong — it does not reformat SQL text. | topic4-prompt26-debugging.md | Medium |
| 77 | B | `spark.shuffle.compress = true` compresses shuffle output files written to disk during the shuffle map phase, reducing disk I/O at the cost of CPU for compression/decompression. | topic4-prompt24-performance-tuning.md | Medium |
| 78 | C | The default compression codec for `spark.io.compression.codec` (used for shuffle blocks and RDD compression) is `lz4`. | topic4-prompt24-performance-tuning.md | Hard |
| 79 | B | With 16 Tasks per Executor sharing one HDFS client, HDFS throughput becomes a bottleneck because the HDFS client is not designed for high concurrent thread counts; large JVM heap shared by 16 threads also increases GC pressure. | topic4-prompt25-common-errors.md | Hard |
| 80 | A, B, E | AQE skewJoin auto-splitting (A), salting with key replication (B), and broadcasting the smaller table to convert to BroadcastHashJoin (E) all effectively mitigate data skew in joins. C (repartitioning by join key) concentrates the skewed key worse. D (more partitions) provides marginal relief but doesn't fix single-key skew. | topic4-prompt24-performance-tuning.md | Hard |
| 81 | B | `trigger(processingTime='30 seconds')` starts a new micro-batch every 30 seconds (or when the previous batch completes, whichever is later), processing all data accumulated since the last batch. | topic5-prompt27-structured-streaming.md | Easy |
| 82 | B | `.option('maxFilesPerTrigger', 5)` limits the number of new files processed per micro-batch to 5, providing a rate-control mechanism for file-based streaming sources. | topic5-prompt27-structured-streaming.md | Easy |
| 83 | B | A streaming DataFrame can be joined with a static (batch) DataFrame; the static side is read once per micro-batch using the current snapshot of that data. | topic5-prompt27-structured-streaming.md | Medium |
| 84 | B | `query.status` returns a Python dict describing the current state of the streaming query (e.g., whether it is initialized, actively processing data, or waiting for the next trigger). | topic5-prompt27-structured-streaming.md | Medium |
| 85 | B | With `startingOffsets='earliest'` and no existing checkpoint, Spark consumes from the very beginning of the Kafka topic; on restart with a checkpoint, it resumes from the saved offsets. | topic5-prompt27-structured-streaming.md | Medium |
| 86 | A, B, D | `dropDuplicates()` in streaming requires a watermark to bound state size (A); without it, deduplication state grows unboundedly (B); state below the watermark threshold is safely dropped (D). C is wrong — exactly-once output depends on the sink. E is wrong — append mode is typical but not the only supported mode. | topic5-prompt28-stateful-streaming.md | Medium |
| 87 | B | Continuous Processing runs Tasks continuously without waiting for a micro-batch boundary, achieving sub-millisecond latency; it checkpoints asynchronously at the specified interval rather than per batch. | topic5-prompt27-structured-streaming.md | Medium |
| 88 | B | `query.lastProgress` returns a Python dict (JSON-like object) with metrics from the most recently completed micro-batch, including batch ID, input row count, processing time, and source/sink metadata. | topic5-prompt27-structured-streaming.md | Hard |
| 89 | B | An `AnalysisException` is raised because global `orderBy` is not supported in Structured Streaming — Spark cannot globally sort an unbounded stream. | topic5-prompt27-structured-streaming.md | Hard |
| 90 | A, B, D | `mapGroupsWithState` maintains arbitrary user-defined state per group key across batches (A); is available in Python via `applyInPandasWithState` (B); must emit exactly one output row per group per batch, unlike `flatMapGroupsWithState` (D). C is wrong — watermark is optional (state can use processing-time timeouts). E is wrong — state is automatically restored from checkpoint on restart. | topic5-prompt28-stateful-streaming.md | Hard |
| 91 | B | `SparkSession.builder.remote('sc://localhost').getOrCreate()` is the correct pattern for creating a Spark Connect client session using the `sc://` URI scheme. | topic6-prompt29-spark-connect.md | Easy |
| 92 | B | In Spark Connect, the logical plan is not analyzed locally; it is sent to the server when an action is triggered, and any analysis errors (missing columns, type mismatches) are returned from the server. | topic6-prompt29-spark-connect.md | Medium |
| 93 | A, B, D, E | Databricks Serverless uses Spark Connect as the underlying protocol (A); `SparkContext` and the RDD API are unavailable in Serverless (B); notebook process is isolated from the Spark Driver (D); multi-language notebooks are supported via a single Spark Connect server (E). C is wrong — `spark.sparkContext` is NOT available in Serverless. | topic6-prompt29-spark-connect.md | Medium |
| 94 | A | TLS/SSL for Spark Connect is configured by appending `;use_ssl=true` to the `sc://` connection URL, e.g., `sc://hostname:15002/;use_ssl=true`. | topic6-prompt29-spark-connect.md | Medium |
| 95 | B | Spark Connect exposes a language-agnostic gRPC API with Protocol Buffers schema; any language with a gRPC client library (including Rust) can submit logical plans and receive Arrow-encoded results without a local JVM. | topic6-prompt29-spark-connect.md | Hard |
| 96 | D | Both `ps.merge(left_psdf, right_psdf, on='user_id', how='inner')` (A) and `left_psdf.merge(right_psdf, on='user_id', how='inner')` (B) are valid and equivalent in `pyspark.pandas`. | topic7-prompt30-pandas-api.md | Easy |
| 97 | B | By default, operations between different `pyspark.pandas` DataFrames are disallowed because they require an implicit join under the hood; set `ps.set_option('compute.ops_on_diff_frames', True)` to enable them. | topic7-prompt30-pandas-api.md | Medium |
| 98 | A | `ps.read_csv('s3://my-bucket/data/file.csv')` works because `pyspark.pandas` delegates file I/O to Spark's distributed file system, which supports S3 and other cloud storage paths natively. | topic7-prompt30-pandas-api.md | Medium |
| 99 | A, B, D, E | `ps.get_dummies()` performs one-hot encoding (A); the result remains a distributed `pyspark.pandas` DataFrame (B); output columns are named `color_<value>` (D); `drop_first=True` is supported (E). C is wrong — the data is NOT collected locally; it stays distributed on the cluster. | topic7-prompt30-pandas-api.md | Medium |
| 100 | B | `pyspark.pandas` has a `compute.shortcut_limit` (default 1000); DataFrames with fewer rows than this limit are collected eagerly and cached locally for instant access; larger DataFrames trigger a full distributed Spark job for operations like `len()`. | topic7-prompt30-pandas-api.md | Hard |
