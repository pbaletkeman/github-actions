# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 4)

**Iteration**: 4

**Generated**: 2026-04-26

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 78 `one` / 22 `many`

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
**Topic**: spark-submit-flags

**Question**:
When submitting a Python Spark application that depends on an additional `.py` utility module, which `spark-submit` flag packages that module and distributes it to executors?

- A) `--jars`
- B) `--py-files`
- C) `--files`
- D) `--packages`

---

### Question 2 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: yarn-application-master

**Question**:
In YARN cluster deploy mode, which component runs the Spark Driver?

- A) The YARN ResourceManager
- B) A dedicated NodeManager on the client machine
- C) The ApplicationMaster container launched on a worker node
- D) A separate standalone Spark Master process

---

### Question 3 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: driver-max-result-size

**Question**:
The configuration `spark.driver.maxResultSize` controls what?

- A) The maximum size of data that can be written to HDFS in a single write call
- B) The total size of serialized results sent back to the driver from all tasks in an action
- C) The maximum heap size allocated to the driver JVM
- D) The maximum number of records returned by `collect()`

---

### Question 4 — Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: history-server-port

**Question**:
The Spark History Server, which displays completed application UIs, listens by default on which port?

- A) 4040
- B) 7077
- C) 8080
- D) 18080

---

### Question 5 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: partitioner-types

**Question**:
A developer calls `rdd.partitionBy(new HashPartitioner(10))` on a PairRDD. What determines which partition a key is assigned to?

- A) The natural sort order of the key relative to all other keys
- B) `key.hashCode() % numPartitions`, placing keys with the same hash in the same partition
- C) The size in bytes of the key value
- D) The insertion order of the key in the original dataset

---

### Question 6 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: rdd-partitioner-property

**Question**:
After calling `rdd.groupByKey()`, what is the value of `rdd.partitioner`?

- A) `None`, because groupByKey does not attach a partitioner to the result RDD
- B) `HashPartitioner(spark.default.parallelism)`
- C) `RangePartitioner` based on the key distribution
- D) The same partitioner as the parent RDD

---

### Question 7 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: storage-level-replication

**Question**:
What does the storage level `MEMORY_ONLY_2` do differently from `MEMORY_ONLY`?

- A) It serializes the cached data using Kryo before storing in memory
- B) It stores two replicated copies of each partition in memory across two different executors
- C) It uses two memory tiers: first off-heap, then on-heap if off-heap is full
- D) It caches only the first two partitions of the RDD in memory

---

### Question 8 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: task-max-failures

**Question**:
By default, Spark retries a failed task up to a certain number of times before aborting the stage. Which configuration key controls the maximum number of times a task can fail before the job is aborted?

- A) `spark.task.maxRetries`
- B) `spark.task.maxFailures`
- C) `spark.executor.maxTaskFailures`
- D) `spark.stage.maxTaskAttempts`

---

### Question 9 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: max-partition-bytes

**Question**:
When Spark reads a large Parquet file, which configuration determines the target maximum size of each input partition?

- A) `spark.sql.shuffle.partitions`
- B) `spark.default.parallelism`
- C) `spark.sql.files.maxPartitionBytes`
- D) `spark.sql.files.openCostInBytes`

---

### Question 10 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: dynamic-allocation-config

**Question**:
Which pair of configuration properties sets the lower and upper bounds on the number of executors when Dynamic Resource Allocation is enabled?

- A) `spark.dynamicAllocation.minExecutors` and `spark.dynamicAllocation.maxExecutors`
- B) `spark.executor.minInstances` and `spark.executor.maxInstances`
- C) `spark.dynamicAllocation.lowerBound` and `spark.dynamicAllocation.upperBound`
- D) `spark.resource.min` and `spark.resource.max`

---

### Question 11 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: executor-memory-breakdown

**Question**:
The total memory allocated to an executor is divided into several regions. Which statement correctly describes the relationship between `spark.executor.memory` and the reserved system memory?

- A) `spark.executor.memory` is the total container memory; system overhead is deducted automatically
- B) Spark internally reserves 300 MB for system overhead; the remaining heap is split by `spark.memory.fraction`
- C) `spark.executor.memory` already excludes system overhead — no further deduction occurs
- D) System memory reservation is configured separately via `spark.executor.memoryOverhead`

---

### Question 12 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: many
**Topic**: shuffle-spill

**Question**:
During a shuffle operation, when does Spark write spill files to disk? *(Select all that apply.)*

- A) When the in-memory shuffle buffer (execution memory) for a reducer is full
- B) When the shuffle data exceeds `spark.reducer.maxSizeInFlight`
- C) When the serialized partition size exceeds `spark.sql.files.maxPartitionBytes`
- D) When available execution memory is exhausted and cannot be borrowed from storage memory

---

### Question 13 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: warehouse-dir

**Question**:
What is the default location where Spark SQL stores managed table data when using the built-in `spark.sql.warehouse.dir` configuration?

- A) `hdfs:///tmp/spark-warehouse`
- B) `spark-warehouse` in the current working directory
- C) `/user/hive/warehouse` on HDFS
- D) The directory specified by `spark.executor.workDir`

---

### Question 14 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: thrift-server

**Question**:
Which Spark component allows JDBC and ODBC clients to connect to Spark SQL and run queries through a standard SQL interface?

- A) Spark History Server
- B) Spark Thrift Server (HiveServer2-compatible)
- C) Spark Standalone Master UI
- D) Spark REST Submission Server

---

### Question 15 — Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pipeline-stage-vs-shuffle-stage

**Question**:
In the Spark execution model, which transformation type causes the DAGScheduler to insert a new **stage boundary**?

- A) `filter()` followed by `map()`
- B) `select()` followed by `withColumn()`
- C) `repartition()` — because it triggers a full shuffle
- D) `coalesce(1)` — because it reduces partition count

---

### Question 16 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: open-cost-in-bytes

**Question**:
The configuration `spark.sql.files.openCostInBytes` is used during file partitioning. What is its purpose?

- A) It sets the maximum size a single split can grow to before a new partition is created
- B) It adds an estimated cost (in bytes) per file to account for file-open overhead, encouraging Spark to co-locate small files into the same partition
- C) It sets the minimum compressed size of a Parquet row group before it is read
- D) It limits total data read per executor during a scan

---

### Question 17 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: many
**Topic**: hive-metastore

**Question**:
When Spark is configured to use a Hive metastore, which of the following statements are true? *(Select all that apply.)*

- A) Tables registered in the Hive metastore persist across Spark sessions
- B) External tables retain their data on disk when `DROP TABLE` is executed
- C) Managed tables have their data deleted from the warehouse directory when `DROP TABLE` is executed
- D) Temporary views created with `createOrReplaceTempView` are stored in the Hive metastore
- E) `spark.table('db.my_table')` can read a Hive-metastore-registered table

---

### Question 18 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: taskset-manager

**Question**:
A TaskSetManager inside Spark's TaskScheduler is responsible for which function?

- A) Converting the logical plan into physical Stages
- B) Tracking which tasks in a Stage have succeeded, failed, or are pending and managing retries
- C) Allocating executor containers from YARN or Kubernetes
- D) Maintaining the broadcast variable registry across all executors

---

### Question 19 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: extra-java-options

**Question**:
A Spark administrator needs to pass a custom JVM flag (e.g., `-XX:+PrintGCDetails`) to executor JVMs. Which configuration key is used for this?

- A) `spark.executor.jvmFlags`
- B) `spark.executor.extraJavaOptions`
- C) `spark.jvm.executorOptions`
- D) `spark.executor.gcOptions`

---

### Question 20 — Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: network-timeout

**Question**:
How do `spark.network.timeout` and `spark.executor.heartbeatInterval` relate to each other?

- A) They are independent settings with no relationship
- B) `spark.executor.heartbeatInterval` must be significantly less than `spark.network.timeout` to prevent executors from being incorrectly considered dead
- C) `spark.network.timeout` must be less than `spark.executor.heartbeatInterval` to ensure timely failure detection
- D) Both must be set to the same value or Spark raises a configuration error

---

### Question 21 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: regexp-extract

**Question**:
Which built-in Spark SQL function extracts a specific capture group from a string using a regular expression?

- A) `regexp_replace(col, pattern, replacement)`
- B) `regexp_extract(col, pattern, idx)`
- C) `regexp_match(col, pattern)`
- D) `extract_group(col, pattern, idx)`

---

### Question 22 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: instr-locate

**Question**:
The function `instr(str, substr)` in Spark SQL returns what?

- A) The number of times `substr` appears in `str`
- B) The 1-based position of the first occurrence of `substr` in `str`, or 0 if not found
- C) A boolean indicating whether `substr` exists in `str`
- D) The substring of `str` starting from `substr`

---

### Question 23 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: add-months

**Question**:
What does `add_months(date_col, 3)` return?

- A) The date three days after `date_col`
- B) The date three weeks after `date_col`
- C) The date three months after `date_col`
- D) The date three years after `date_col`

---

### Question 24 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: size-cardinality

**Question**:
In Spark SQL, what does `size(array_col)` return when `array_col` is `NULL`?

- A) `0`
- B) `NULL`
- C) `-1`
- D) It throws a `NullPointerException`

---

### Question 25 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: substring-index

**Question**:
Given `substring_index('a.b.c.d', '.', 2)`, what is the result?

- A) `'c.d'`
- B) `'a.b'`
- C) `'b.c'`
- D) `'a.b.c'`

---

### Question 26 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: translate

**Question**:
What does `translate('Hello', 'aeiou', '*')` return?

- A) `'H*ll*'`
- B) `'H*ll*'` — it replaces only lowercase vowels found in `'Hello'`
- C) `'H*ll*'` is wrong; `translate` is case-sensitive, so only `'e'` and `'o'` match, giving `'H*ll*'`
- D) `'H*ll*'` — same result; `translate` replaces each character in the second argument with the corresponding character in the third, so `e→*` and `o→*`

---

### Question 27 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: format-number

**Question**:
The function `format_number(1234567.891, 2)` returns which of the following?

- A) `'1234567.89'`
- B) `'1,234,567.89'`
- C) `'1.23M'`
- D) `1234567.89` as a `DoubleType`

---

### Question 28 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: unix-timestamp

**Question**:
What does `from_unixtime(unix_timestamp_col)` return?

- A) A `TimestampType` column representing the local timezone equivalent of the Unix epoch seconds
- B) A `StringType` column formatted as `'yyyy-MM-dd HH:mm:ss'` in the session local timezone
- C) A `DateType` column truncated to the day
- D) A `LongType` column with milliseconds since epoch

---

### Question 29 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: date-trunc

**Question**:
The expression `date_trunc('month', '2024-07-15')` returns which value?

- A) `2024-07-01`
- B) `2024-07-15`
- C) `2024-07-31`
- D) `2024-01-01`

---

### Question 30 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: to-utc-timestamp

**Question**:
`to_utc_timestamp(ts_col, 'America/New_York')` does which of the following?

- A) Converts a UTC timestamp to `America/New_York` local time
- B) Treats `ts_col` as a local `America/New_York` timestamp and converts it to UTC
- C) Adds the UTC offset of `America/New_York` to `ts_col` without changing the timezone label
- D) Returns a `StringType` with the ISO-8601 representation in UTC

---

### Question 31 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: arrays-overlap

**Question**:
Given `arrays_overlap(array(1, 2, 3), array(3, 4, 5))`, what is the result?

- A) `false`
- B) `true`
- C) `[3]`
- D) `null`

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: map-from-arrays

**Question**:
What does `map_from_arrays(array('a','b'), array(1, 2))` return?

- A) `array(struct('a',1), struct('b',2))`
- B) `map('a', 1, 'b', 2)`
- C) `struct(a INT, b INT)`
- D) Two separate columns: `key` and `value`

---

### Question 33 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: rows-between-vs-range-between

**Question**:
In a window specification, `ROWS BETWEEN 1 PRECEDING AND CURRENT ROW` vs `RANGE BETWEEN 1 PRECEDING AND CURRENT ROW` differ in which key way?

- A) `ROWS` is based on the physical row offset; `RANGE` is based on the logical value distance from the current row's `ORDER BY` value
- B) `RANGE` is always larger than `ROWS` when the `ORDER BY` column has duplicate values
- C) `ROWS` can only be used with numeric columns while `RANGE` works on any type
- D) Both are identical unless the window includes a `PARTITION BY` clause

---

### Question 34 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: map-concat

**Question**:
When `map_concat(map1, map2)` is called and both maps share the same key, which value is retained?

- A) The value from `map1` (left map wins)
- B) The value from `map2` (right map wins)
- C) Both values are merged into an array for that key
- D) An `AnalysisException` is raised due to duplicate keys

---

### Question 35 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: tablesample

**Question**:
Which SQL clause randomly samples approximately 10% of rows from a table scan?

- A) `SELECT * FROM t LIMIT 0.1`
- B) `SELECT * FROM t TABLESAMPLE (10 PERCENT)`
- C) `SELECT * FROM t SAMPLE BY 0.1`
- D) `SELECT * FROM t WHERE RAND() < 0.1`

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: many
**Topic**: rollup-nulls

**Question**:
The query `SELECT region, country, SUM(sales) FROM t GROUP BY ROLLUP(region, country)` produces super-aggregate rows. Which statements about the null values in those rows are true? *(Select all that apply.)*

- A) A row where `region` IS NULL and `country` IS NULL represents the grand total
- B) A row where `region` has a value and `country` IS NULL represents a subtotal for that region
- C) NULL values in super-aggregate rows are indistinguishable from actual NULL data values unless `GROUPING()` is used
- D) `ROLLUP(region, country)` produces the same set of grouping sets as `CUBE(region, country)`

---

### Question 37 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: overlay

**Question**:
`overlay('Spark SQL', 'DataFrame', 7)` replaces the portion of the first string starting at position 7 with the second string. What is the result?

- A) `'Spark SDataFrame'`
- B) `'Spark DataFrame'`
- C) `'Spark SDataFrameQL'`
- D) `'Spark SQLDataFrame'`

---

### Question 38 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: except-all

**Question**:
How does `EXCEPT ALL` differ from `EXCEPT` (also written `EXCEPT DISTINCT`) in Spark SQL?

- A) `EXCEPT ALL` removes only one occurrence of a matching row for each occurrence in the right relation, preserving extra duplicates in the left
- B) `EXCEPT ALL` removes all occurrences of any row that appears anywhere in the right relation
- C) `EXCEPT ALL` is an alias for `MINUS` and behaves identically to `EXCEPT DISTINCT`
- D) `EXCEPT ALL` is not supported in Spark SQL and raises a parse error

---

### Question 39 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: qualify-clause

**Question**:
The `QUALIFY` clause introduced in Spark 3.4 filters rows after a window function is evaluated. Which SQL query correctly retrieves only the row with the highest salary per department?

- A) `SELECT * FROM employees WHERE RANK() OVER (PARTITION BY dept ORDER BY salary DESC) = 1`
- B) `SELECT * FROM employees QUALIFY RANK() OVER (PARTITION BY dept ORDER BY salary DESC) = 1`
- C) `SELECT * FROM (SELECT *, RANK() OVER (PARTITION BY dept ORDER BY salary DESC) AS rk FROM employees) WHERE rk = 1`
- D) Both B and C produce the same result, but B is the more concise syntax

---

### Question 40 — Spark SQL

**Difficulty**: Hard
**Answer Type**: many
**Topic**: intersect-all

**Question**:
Which statements correctly distinguish `INTERSECT ALL` from `INTERSECT` (DISTINCT)? *(Select all that apply.)*

- A) `INTERSECT ALL` preserves duplicate rows up to the minimum count appearing in both sides
- B) `INTERSECT DISTINCT` deduplicates the result so each row appears at most once
- C) `INTERSECT ALL` is semantically equivalent to `INTERSECT DISTINCT` when both sides have no duplicates
- D) `INTERSECT ALL` is a standard SQL operation supported in Spark SQL since Spark 3.0
- E) `INTERSECT DISTINCT` can return more rows than `INTERSECT ALL` when duplicates are present

---

### Question 41 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: col-reference-styles

**Question**:
Which of the following column reference styles can cause an `AnalysisException` when used after a join if both DataFrames have a column with the same name?

- A) `F.col('column_name')` when scoped to a specific DataFrame object
- B) `df['column_name']` using bracket syntax
- C) `df.column_name` using attribute syntax
- D) `F.col('column_name')` used without a DataFrame qualifier

---

### Question 42 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: write-compression

**Question**:
When writing a DataFrame to Parquet with Snappy compression, which call is correct?

- A) `df.write.parquet('/output', compression='snappy')`
- B) `df.write.option('compression', 'snappy').parquet('/output')`
- C) `df.write.option('codec', 'snappy').parquet('/output')`
- D) `df.write.parquet('/output').compress('snappy')`

---

### Question 43 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: input-file-name

**Question**:
Which function returns the file path of the source file that contributed each row when reading from a directory of files?

- A) `F.source_file()`
- B) `F.file_name()`
- C) `F.input_file_name()`
- D) `F.origin_file()`

---

### Question 44 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: write-text-requirement

**Question**:
`df.write.text('/output')` requires the DataFrame to have which schema?

- A) Any schema — all columns are automatically concatenated
- B) Exactly one column of `StringType`
- C) Exactly one column of any type (automatically cast to String)
- D) A schema with exactly two columns: key and value

---

### Question 45 — DataFrame API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: schema-json

**Question**:
After defining a schema as a `StructType`, which method produces a JSON string representation of that schema?

- A) `schema.prettyJson()`
- B) `schema.json()`
- C) `schema.toJson()`
- D) `schema.asJson()`

---

### Question 46 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: max-records-per-file

**Question**:
A team wants to ensure no output Parquet file exceeds 100,000 rows when writing. Which option achieves this?

- A) `df.write.option('maxRecordsPerFile', 100000).parquet('/out')`
- B) `df.write.option('rowLimit', 100000).parquet('/out')`
- C) `df.repartition(math.ceil(df.count()/100000)).write.parquet('/out')`
- D) `df.write.option('partitionMaxRows', 100000).parquet('/out')`

---

### Question 47 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: read-csv-null-value

**Question**:
When reading a CSV file where missing values are represented by the string `'N/A'`, which read option correctly maps those strings to `null`?

- A) `spark.read.csv('/path', nullValue='N/A')`
- B) `spark.read.option('na', 'N/A').csv('/path')`
- C) `spark.read.option('missingValue', 'N/A').csv('/path')`
- D) `spark.read.csv('/path', emptyValue='N/A')`

---

### Question 48 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: path-glob-filter

**Question**:
A directory contains both `.parquet` and `.crc` files. Which read option restricts Spark to reading only `.parquet` files?

- A) `spark.read.option('fileExtension', '.parquet').parquet('/data')`
- B) `spark.read.option('pathGlobFilter', '*.parquet').parquet('/data')`
- C) `spark.read.parquet('/data/*.parquet')`
- D) `spark.read.option('includeOnly', '*.parquet').parquet('/data')`

---

### Question 49 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: f-broadcast

**Question**:
What is the correct import to use `F.broadcast()` for hinting a DataFrame join?

- A) `from pyspark.sql import broadcast`
- B) `from pyspark.sql.functions import broadcast`
- C) `from pyspark.sql.hints import broadcast`
- D) `from pyspark import broadcast`

---

### Question 50 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df-rdd-caveats

**Question**:
When converting a DataFrame to an RDD using `df.rdd`, which statement is true?

- A) The resulting RDD contains Python dicts
- B) The resulting RDD contains `Row` objects where fields are accessible by name
- C) The resulting RDD contains plain Python tuples
- D) The conversion is zero-cost because DataFrames are backed by RDDs internally in modern Spark

---

### Question 51 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: withcolumn-overwrite

**Question**:
When `df.withColumn('existing_col', new_expr)` is called and `existing_col` already exists in `df`, what happens?

- A) An `AnalysisException` is raised because the column already exists
- B) A new column is appended alongside the original, resulting in two columns with the same name
- C) The existing column is replaced in place with the result of `new_expr`
- D) The original column is renamed to `existing_col_old` and the new one takes the original name

---

### Question 52 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: coalesce-column-function

**Question**:
`F.coalesce(col1, col2, col3)` returns what?

- A) The sum of the first non-null value across columns
- B) The first non-null value from the provided columns, evaluated left to right
- C) A combined column that merges non-null values from all inputs
- D) The count of non-null values across the provided columns

---

### Question 53 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: except-all-intersect-all

**Question**:
How does `df1.exceptAll(df2)` differ from `df1.subtract(df2)`?

- A) `exceptAll` preserves duplicates by removing one matching row from `df1` per occurrence in `df2`; `subtract` removes all occurrences of any matching row
- B) `subtract` preserves duplicates; `exceptAll` removes all occurrences
- C) They are identical in behavior
- D) `exceptAll` sorts the result while `subtract` does not

---

### Question 54 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: schema-inference-parquet

**Question**:
When reading a directory of Parquet files with `spark.read.parquet('/path')`, how does Spark determine the schema?

- A) It reads all files fully and infers the schema from data values
- B) It reads the schema embedded in the Parquet file footer metadata
- C) It defaults to all `StringType` columns unless an explicit schema is provided
- D) It samples 100 rows and infers the schema from those samples

---

### Question 55 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: pandas-udf-decorator

**Question**:
Which decorator and import are required to define a Pandas UDF (vectorized UDF) that takes a `pd.Series` and returns a `pd.Series`?

- A) `@udf(returnType=DoubleType())` from `pyspark.sql.functions`
- B) `@pandas_udf(returnType=DoubleType())` from `pyspark.sql.functions`
- C) `@vectorized_udf` from `pyspark.sql`
- D) `@spark.udf.pandas` applied to the function

---

### Question 56 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cache-count-pattern

**Question**:
Why do developers often call `df.cache().count()` together rather than just `df.cache()`?

- A) `count()` is required to register the cache entry with the Spark UI
- B) `cache()` is lazy — it only marks the DataFrame for caching; `count()` is an action that forces materialization so data is actually stored in memory
- C) `count()` prevents garbage collection of the cached DataFrame
- D) Without `count()`, the cache is stored on disk rather than in memory

---

### Question 57 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: createdataframe-from-rows

**Question**:
Which of the following correctly creates a DataFrame from a list of `Row` objects?

- A) `spark.createDataFrame([Row(a=1, b='x'), Row(a=2, b='y')])`
- B) `spark.createDataFrame(Row(1, 'x'), Row(2, 'y'))`
- C) `spark.fromRows([Row(a=1), Row(a=2)])`
- D) `spark.DataFrame.fromRows([Row(a=1, b='x')])`

---

### Question 58 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: dataframewriter-jdbc

**Question**:
When writing a DataFrame to a relational database table using `df.write.jdbc()`, which parameter specifies the target table name?

- A) `table`
- B) `tableName`
- C): `dbtable`
- D) `targetTable`

---

### Question 59 — DataFrame API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: read-jdbc

**Question**:
Which options can improve parallelism when reading a large database table via `spark.read.jdbc()`? *(Select all that apply.)*

- A) Specifying `numPartitions`, `lowerBound`, `upperBound`, and `partitionColumn`
- B) Setting `fetchsize` to a larger value
- C) Using `predicates` to pass a list of WHERE clauses, one per partition
- D) Setting `batchsize` to control how many rows are written back per batch

---

### Question 60 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: write-csv-header

**Question**:
By default, when writing a DataFrame to CSV with `df.write.csv('/out')`, are column headers written to the output files?

- A) Yes, headers are always written
- B) No, headers are not written by default; set `.option('header', True)` to include them
- C) Headers are written only when the schema contains named columns
- D) Headers are written only when `inferSchema` was used during the read

---

### Question 61 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: coalesce-vs-repartition-output-files

**Question**:
A DataFrame with 100 partitions must be written as exactly 5 output files. `df.coalesce(5).write.parquet('/out')` vs `df.repartition(5).write.parquet('/out')` — what is the key operational difference?

- A) `coalesce(5)` avoids a full shuffle by merging existing partitions; `repartition(5)` performs a full shuffle redistributing data evenly
- B) `repartition(5)` avoids a shuffle; `coalesce(5)` triggers a full shuffle
- C) Both perform an identical shuffle; the only difference is the resulting partition count
- D) `coalesce(5)` can only be used when reducing partition count by more than 50%

---

### Question 62 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: selectexpr-sql-string

**Question**:
A developer wants to compute `age * 2` and rename it to `double_age` in a single step without importing any functions. Which approach achieves this?

- A) `df.select(F.col('age') * 2).alias('double_age')`
- B) `df.selectExpr('age * 2 AS double_age')`
- C) `df.withColumn('double_age', 'age * 2')`
- D) `df.select('age * 2 AS double_age')`

---

### Question 63 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: when-full-null-branch

**Question**:
Given `F.when(F.col('x') > 0, 'positive').when(F.col('x') < 0, 'negative')` with no `.otherwise()`, what value is produced when `x == 0`?

- A) `'zero'`
- B) An `AnalysisException` is raised because `.otherwise()` is mandatory
- C) `null`
- D) The expression returns the string `'None'`

---

### Question 64 — DataFrame API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: to-pandas-on-spark

**Question**:
What does `sdf.to_pandas_on_spark()` return?

- A) A regular `pandas.DataFrame` collected to the driver
- B) A `pyspark.pandas.DataFrame` (Pandas API on Spark), keeping data distributed
- C) An RDD of pandas DataFrames, one per partition
- D) A Spark DataFrame with a pandas-compatible schema

---

### Question 65 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: structtype-equality

**Question**:
Two `StructType` schemas are defined independently with the same field names and types. When comparing them with `==`, what is the result?

- A) `False` — schemas are objects and use identity (reference) equality
- B) `True` — `StructType` implements value-based equality; two schemas with identical fields compare equal
- C) A `TypeError` is raised because `StructType` does not support `==`
- D) `True` only if the `nullable` flags also match for every field

---

### Question 66 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: pandas-udf-groupedmap

**Question**:
A Pandas UDF of type `PandasUDFType.GROUPED_MAP` (or `applyInPandas`) receives data as what structure?

- A) One large concatenated `pd.DataFrame` containing all data for all groups
- B) A `pd.DataFrame` per group, with one function call per group
- C) A `pd.Series` per group, one value at a time
- D) An iterator of `pd.Series`, one series per column within the group

---

### Question 67 — DataFrame API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: df-rdd-get-partitions

**Question**:
Which of the following correctly retrieve the number of partitions in a DataFrame? *(Select all that apply.)*

- A) `df.rdd.getNumPartitions()`
- B) `df.getNumPartitions()`
- C) `len(df.rdd.partitions)`
- D) `df.repartitionNum`

---

### Question 68 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark-read-load-generic

**Question**:
The following code reads a Delta table using the generic load API:

```python
df = spark.read.format('delta').load('/data/events')
```

Which of the following is a valid equivalent?

- A) `spark.read.delta('/data/events')`
- B) `spark.read.load('/data/events', format='delta')`
- C) `spark.read.load('/data/events').format('delta')`
- D) `spark.read.option('format', 'delta').load('/data/events')`

---

### Question 69 — DataFrame API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: write-jdbc-dbtable-param

**Question**:
A developer reads from a JDBC source specifying a subquery instead of a table name. Which option key supports this?

- A) `table`
- B) `query`
- C) `dbtable`
- D) Both B and C — `dbtable` can accept a subquery wrapped in parentheses, and `query` is a separate explicit option

---

### Question 70 — DataFrame API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: write-options-parquet

**Question**:
Which of the following write options are valid when writing a DataFrame to Parquet? *(Select all that apply.)*

- A) `compression` — sets the codec (e.g., `snappy`, `gzip`, `zstd`)
- B) `maxRecordsPerFile` — limits the number of rows per output file
- C) `header` — writes column names as the first row of each file
- D) `mergeSchema` — merges schema differences across existing Parquet files on write
- E) `partitionOverwriteMode` — controls dynamic vs static partition overwrite behavior

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: disable-broadcast-join

**Question**:
To prevent Spark from ever using a broadcast join, regardless of table size, a developer sets which configuration?

- A) `spark.sql.autoBroadcastJoinThreshold = 0`
- B) `spark.sql.autoBroadcastJoinThreshold = -1`
- C) `spark.sql.broadcastJoin.enabled = false`
- D) `spark.sql.disableBroadcastJoin = true`

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: analyze-table

**Question**:
Which SQL command updates the column-level statistics used by Spark's Cost-Based Optimizer for a table named `orders`?

- A) `UPDATE STATISTICS orders`
- B) `ANALYZE TABLE orders COMPUTE STATISTICS FOR ALL COLUMNS`
- C) `COMPUTE STATS orders`
- D) `REFRESH TABLE orders`

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cache-table-sql

**Question**:
A developer runs `CACHE TABLE sales`. What does Spark do?

- A) Caches the table metadata in the Hive metastore
- B) Eagerly scans and caches the entire `sales` table in memory as a distributed in-memory DataFrame
- C) Lazily marks `sales` for caching; data is stored only when first queried
- D) Creates an in-memory copy only on the driver node

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: aqe-advisory-partition-size

**Question**:
`spark.sql.adaptive.advisoryPartitionSizeInBytes` controls which AQE behaviour?

- A) The minimum size of a broadcast table
- B) The target partition size that AQE aims for when coalescing post-shuffle partitions
- C) The maximum size of a single sort spill file
- D) The threshold above which AQE marks a partition as skewed

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: orc-vs-parquet

**Question**:
Which of the following is a scenario where ORC format is typically preferred over Parquet?

- A) Spark reads data produced by Apache Hive workloads that use ORC natively
- B) The data is consumed primarily by Python pandas code outside Spark
- C) The schema is deeply nested with many array-of-struct columns
- D) The data is ingested by Apache Kafka consumers

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cbo-join-reorder

**Question**:
Which configuration enables the Cost-Based Optimizer to reorder multiple joins to minimize data shuffled?

- A) `spark.sql.cbo.enabled = true` alone is sufficient
- B) `spark.sql.cbo.joinReorder.enabled = true` (requires `spark.sql.cbo.enabled = true`)
- C) `spark.sql.adaptive.joinReorder.enabled = true`
- D) `spark.sql.optimizer.joinReorder = cost`

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: prefer-sortmerge-join

**Question**:
Setting `spark.sql.join.preferSortMergeJoin = true` influences join selection how?

- A) It disables broadcast hash join entirely
- B) It makes Spark prefer SortMergeJoin over ShuffledHashJoin when both are applicable
- C) It forces all joins to use SortMergeJoin, overriding AQE decisions
- D) It applies only when joining two tables each larger than the broadcast threshold

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: explain-cost

**Question**:
Which `explain()` mode includes estimated row counts and data sizes used by the Cost-Based Optimizer in the output plan?

- A) `df.explain('extended')`
- B) `df.explain('codegen')`
- C) `df.explain('cost')`
- D) `df.explain('formatted')`

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: many
**Topic**: aqe-min-partition-num

**Question**:
`spark.sql.adaptive.coalescePartitions.minPartitionNum` ensures what when AQE coalesces post-shuffle partitions? *(Select all that apply.)*

- A) It sets a lower bound on the number of partitions AQE will keep after coalescing
- B) It prevents AQE from reducing the partition count below this number, even if partitions are very small
- C) It guarantees at least this many executors will be active during the shuffle read
- D) Setting it to `1` means AQE may collapse all shuffle output to a single partition

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: in-memory-columnar-storage

**Question**:
When caching a DataFrame with `df.cache()`, Spark stores it in columnar format internally. Which configuration controls whether this columnar in-memory data is additionally compressed?

- A) `spark.sql.inMemoryColumnarStorage.compressed`
- B) `spark.memory.columnCompression.enabled`
- C) `spark.sql.cache.compression.codec`
- D) `spark.sql.inMemoryColumnarStorage.codec`

---

### Question 81 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: trigger-once

**Question**:
`trigger(once=True)` on a streaming query causes what behaviour?

- A) The query runs continuously, processing one micro-batch per second
- B) The query processes all available data in one micro-batch, then automatically stops
- C) The query runs exactly once per hour on a schedule
- D) The query runs for exactly one second regardless of data availability

---

### Question 82 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: checkpoint-required

**Question**:
Which streaming write option is required to enable fault tolerance and exactly-once processing guarantees in Structured Streaming?

- A) `option('watermarkDelay', '10 minutes')`
- B) `option('checkpointLocation', '/path/to/checkpoint')`
- C) `option('outputMode', 'append')`
- D) `option('trigger', 'once')`

---

### Question 83 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: update-output-mode

**Question**:
The `update` output mode in Structured Streaming writes which rows on each trigger?

- A) All rows in the result table on every trigger
- B) Only the rows that were newly added since the last trigger
- C) Only the rows whose result-table values changed (updated or new) since the last trigger
- D) Only rows that were deleted from the result table

---

### Question 84 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: update-mode-limitation

**Question**:
For a streaming query with no aggregation, which output mode is NOT valid?

- A) `append`
- B) `update`
- C) `complete`
- D) Both B and C — update and complete require aggregation

---

### Question 85 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: window-with-watermark

**Question**:
A streaming query computes 5-minute tumbling window aggregations with a 10-minute watermark. Given an event with timestamp `12:07`, to which window(s) does it contribute?

- A) The `[12:05, 12:10)` window only
- B) The `[12:00, 12:05)` window only
- C) Both `[12:05, 12:10)` and `[12:10, 12:15)` windows
- D) No window — the event is immediately dropped by the watermark

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: foreach-batch-multi-sink

**Question**:
A team needs to write streaming output to both a Delta table and an external REST API simultaneously. Which Structured Streaming feature enables this pattern?

- A) Writing to two separate `writeStream` chains from the same streaming DataFrame
- B) `foreachBatch` — receives each micro-batch as a static DataFrame, allowing arbitrary multi-sink writes
- C) `foreach` — applies a row-level function that can call the REST API per row
- D) `outputMode('complete')` with a custom sink class

---

### Question 87 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: kafka-fail-on-data-loss

**Question**:
Setting `failOnDataLoss = false` when reading a Kafka stream means what?

- A) Spark will silently skip offsets that are no longer available in Kafka (e.g., due to retention expiry) rather than failing the query
- B) The query ignores all Kafka connection errors and retries indefinitely
- C) Spark disables exactly-once delivery guarantees for Kafka sources
- D) Consumer group offsets will not be committed to Kafka on each trigger

---

### Question 88 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: recent-progress

**Question**:
`query.recentProgress` returns what?

- A) A single dictionary with the most recent micro-batch statistics
- B) A list of dictionaries, each representing a recently completed micro-batch progress report
- C) A streaming DataFrame of progress events
- D) A JSON file path where progress statistics are written

---

### Question 89 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: streaming-query-listener

**Question**:
To monitor all active streaming queries in a Spark application without modifying each query individually, a developer should implement which interface?

- A) `SparkListener` from `pyspark.SparkContext`
- B) `StreamingQueryListener` from `pyspark.sql.streaming`
- C) `QueryProgressCallback` from `pyspark.sql`
- D) `StreamingMetricsListener` from `pyspark.sql.streaming`

---

### Question 90 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: many
**Topic**: window-watermark-combined

**Question**:
When combining `withWatermark()` and `window()` in a streaming aggregation, which statements are correct? *(Select all that apply.)*

- A) `withWatermark()` must be called on the timestamp column before `groupBy(window(...))`
- B) The watermark determines when a window's state is finalized and old state is dropped
- C) In `append` output mode, results for a window are emitted only after the watermark passes the window's end time
- D) In `complete` output mode, all window results are emitted on every trigger regardless of the watermark
- E) The watermark column must be the same column used in the `window()` expression

---

### Question 91 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark-remote-env

**Question**:
Which environment variable can be set to automatically configure the Spark Connect server URL for `SparkSession.builder.getOrCreate()` without calling `.remote()` explicitly?

- A) `SPARK_CONNECT_URL`
- B) `SPARK_REMOTE`
- C) `SPARK_SERVER_URL`
- D) `SPARK_CONNECT_SERVER`

---

### Question 92 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: arrow-transfer

**Question**:
In Spark Connect, what serialization format is used to transfer data between the client and the server over gRPC?

- A) Java serialization (Kryo)
- B) Protocol Buffers (Protobuf) only
- C) Apache Arrow
- D) JSON

---

### Question 93 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark-session-active-connect

**Question**:
In a Spark Connect client application, `SparkSession.getActiveSession()` (or `SparkSession.active`) returns what?

- A) The local in-process Spark session on the driver
- B) The Spark Connect client-side session proxy object representing the remote connection
- C) `None` — Spark Connect does not support `SparkSession.active`
- D) The server-side session object marshalled back over gRPC

---

### Question 94 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: databricks-session-serverless

**Question**:
In Databricks, which builder pattern creates a Spark Connect session that connects to Databricks Serverless compute?

- A) `SparkSession.builder.remote('sc://databricks').getOrCreate()`
- B) `DatabricksSession.builder.serverless().getOrCreate()`
- C) `SparkSession.builder.config('spark.databricks.serverless', True).getOrCreate()`
- D) `DatabricksSession.builder.remote('serverless').getOrCreate()`

---

### Question 95 — Spark Connect

**Difficulty**: Hard
**Answer Type**: many
**Topic**: connect-vs-spark-submit

**Question**:
How does deploying a Spark application using Spark Connect differ from traditional `spark-submit`? *(Select all that apply.)*

- A) With Spark Connect, the client code runs as a lightweight local process; the heavy computation runs on a remote Spark server
- B) `spark-submit` packages and ships the driver to the cluster; Spark Connect keeps the driver on the client machine
- C) Spark Connect applications can use `SparkContext` and `RDD` APIs the same way as `spark-submit` applications
- D) Spark Connect allows multiple clients to share the same running Spark server session
- E) `spark-submit` is deprecated and will be removed in Spark 4.0

---

### Question 96 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: ps-sql

**Question**:
Which function allows running a SQL query against Pandas API on Spark DataFrames registered as temp views?

- A) `ps.execute_sql('SELECT ...')`
- B) `ps.sql('SELECT ...')`
- C) `ps.query('SELECT ...')`
- D) `ps.DataFrame.sql('SELECT ...')`

---

### Question 97 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: ps-read-parquet

**Question**:
How do you read a Parquet file into a Pandas API on Spark DataFrame?

- A) `ps.read_parquet('/path')`
- B) `ps.from_parquet('/path')`
- C) `spark.read.parquet('/path').pandas_api()`
- D) Both A and C are valid approaches

---

### Question 98 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps-apply-axis

**Question**:
`psdf.apply(func, axis=1)` applies `func` how?

- A) Column-wise — `func` receives each column as a `pd.Series`
- B) Row-wise — `func` receives each row as a `pd.Series`
- C) It applies `func` to the entire DataFrame as one `pd.DataFrame`
- D) `axis=1` is not supported in Pandas API on Spark and raises a `ValueError`

---

### Question 99 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps-rolling

**Question**:
`psdf['value'].rolling(3).mean()` in Pandas API on Spark computes what?

- A) A 3-second time-based rolling average
- B) A 3-row rolling average; the first two rows will be `NaN`
- C) The average of the entire `value` column across 3 partitions
- D) This operation is not supported and raises a `NotImplementedError`

---

### Question 100 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: many
**Topic**: ps-concat

**Question**:
Which statements about `ps.concat([psdf1, psdf2])` are correct? *(Select all that apply.)*

- A) By default it concatenates along `axis=0` (stacks rows)
- B) `axis=1` concatenation joins the DataFrames side-by-side by index and requires `ops_on_diff_frames = True` if the DataFrames originate from different Spark plans
- C) The result always has a `default` integer index regardless of the input indices
- D) `ps.concat` with `ignore_index=True` resets the index of the resulting DataFrame
- E) Concatenating along `axis=1` performs a distributed merge on the index values

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | `--py-files` distributes `.py`/`.egg`/`.zip` modules to executors and adds them to the Python path; `--files` distributes arbitrary files without path registration; `--jars` is for Java JARs. | topic1-prompt1-spark-architecture.md | Easy |
| 2 | C | In YARN cluster mode the Spark Driver runs inside the ApplicationMaster container, which the ResourceManager launches on a worker node; the client detaches after submission. | topic1-prompt1-spark-architecture.md | Easy |
| 3 | B | `spark.driver.maxResultSize` caps the total serialised size of all task results returned to the driver (e.g., via `collect()`); it does not limit driver heap size (`spark.driver.memory`). | topic1-prompt1-spark-architecture.md | Easy |
| 4 | D | The Spark History Server listens on port 18080 by default. Port 4040 is the live application UI; 7077 is the Standalone Master RPC port; 8080 is the Standalone Master web UI. | topic1-prompt1-spark-architecture.md | Easy |
| 5 | B | `HashPartitioner` assigns key `k` to partition `abs(k.hashCode()) % numPartitions`, ensuring keys with the same hash always land on the same partition. | topic1-prompt4-shuffling-performance.md | Medium |
| 6 | B | `groupByKey()` with no explicit partition count triggers a shuffle and attaches a `HashPartitioner(spark.default.parallelism)` to the resulting RDD. | topic1-prompt4-shuffling-performance.md | Medium |
| 7 | B | The `_2` suffix on a storage level means two replicated copies of each partition are stored in memory across two different executors, providing in-memory fault tolerance. | topic1-prompt1-spark-architecture.md | Medium |
| 8 | B | `spark.task.maxFailures` (default 4) controls how many times a single task attempt may fail before Spark aborts the containing job. | topic1-prompt1-spark-architecture.md | Medium |
| 9 | C | `spark.sql.files.maxPartitionBytes` (default 128 MB) sets the target maximum bytes per partition when reading file-based sources such as Parquet. | topic1-prompt1-spark-architecture.md | Medium |
| 10 | A | Dynamic Resource Allocation bounds are set by `spark.dynamicAllocation.minExecutors` (floor) and `spark.dynamicAllocation.maxExecutors` (ceiling). | topic1-prompt1-spark-architecture.md | Medium |
| 11 | B | Spark reserves 300 MB of the executor JVM heap for internal overhead; the remaining memory is divided by `spark.memory.fraction` (default 0.6) into Execution and Storage pools. | topic1-prompt7-garbage-collection.md | Medium |
| 12 | A, D | Shuffle spill occurs when (A) the in-memory shuffle buffer for a reducer is full and (D) execution memory is exhausted and cannot borrow from storage memory; both describe the same condition at different abstraction levels. | topic1-prompt4-shuffling-performance.md | Medium |
| 13 | B | `spark.sql.warehouse.dir` defaults to `${system:user.dir}/spark-warehouse`, which is a `spark-warehouse` subdirectory of the JVM's current working directory. | topic1-prompt1-spark-architecture.md | Medium |
| 14 | B | The Spark Thrift Server is a HiveServer2-compatible JDBC/ODBC gateway that lets BI tools and SQL clients issue queries against Spark SQL over standard drivers. | topic1-prompt1-spark-architecture.md | Medium |
| 15 | C | `repartition()` triggers a full shuffle exchange, causing the DAGScheduler to insert a new stage boundary; `coalesce()` is narrow (no shuffle), as are `filter()` and `select()`. | topic1-prompt4-shuffling-performance.md | Medium |
| 16 | B | `spark.sql.files.openCostInBytes` (default ~4 MB) adds a per-file overhead cost when computing partition splits, biasing Spark toward co-locating small files into the same partition to amortise open cost. | topic1-prompt1-spark-architecture.md | Hard |
| 17 | A, B, C, E | Hive-metastore tables persist across sessions (A); external table data survives `DROP TABLE` (B); managed table data is deleted on `DROP TABLE` (C); `spark.table()` can read a registered table (E). D is false — temporary views are session-scoped and never persisted to the Hive metastore. | topic1-prompt1-spark-architecture.md | Hard |
| 18 | B | `TaskSetManager` tracks the success, failure, and pending state of every task in a stage, manages retry scheduling up to `spark.task.maxFailures`, and implements locality-aware task launch. | topic1-prompt1-spark-architecture.md | Hard |
| 19 | B | `spark.executor.extraJavaOptions` passes additional JVM flags (GC tuning, agents, diagnostic options) to the executor JVM process at launch time. | topic1-prompt1-spark-architecture.md | Hard |
| 20 | B | `spark.executor.heartbeatInterval` (default 10 s) must be significantly less than `spark.network.timeout` (default 120 s) so the driver does not incorrectly declare a live executor dead between heartbeats. | topic1-prompt1-spark-architecture.md | Hard |
| 21 | B | `regexp_extract(col, pattern, groupIdx)` extracts the capture group at position `groupIdx` from the first regex match; `groupIdx=0` returns the full match. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 22 | B | `instr(str, substr)` returns the 1-based position of the first occurrence of `substr` in `str`, or `0` if not found; it never returns `null` for non-null inputs. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 23 | C | `add_months(date_col, 3)` returns the date three calendar months after `date_col`, correctly handling month-end edge cases (e.g., Jan 31 + 1 month → Feb 28/29). | topic2-prompt9-builtin-sql-functions.md | Easy |
| 24 | B | In Spark 3.0+, `size(null)` returns `null` by default. The legacy behaviour of returning `-1` can be restored by setting `spark.sql.legacy.sizeOfNull = true`. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 25 | B | `substring_index('a.b.c.d', '.', 2)` returns the leftmost 2 period-delimited segments: `'a.b'`. A negative count would return segments from the right. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 26 | D | `translate` replaces each character in the input found in `matchingString` with the corresponding character in `replaceString`; 'e' and 'o' in `'Hello'` both map to `'*'`, giving `'H*ll*'`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 27 | B | `format_number(value, decimalPlaces)` formats a number as a locale string with thousands separators; `format_number(1234567.891, 2)` → `'1,234,567.89'`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 28 | B | `from_unixtime(unix_ts)` converts Unix epoch seconds to a `StringType` column formatted as `'yyyy-MM-dd HH:mm:ss'` in the session's local timezone. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 29 | A | `date_trunc('month', '2024-07-15')` truncates to the first day of the month: `2024-07-01`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 30 | B | `to_utc_timestamp(ts_col, 'America/New_York')` interprets `ts_col` as New York local time and converts it to UTC (adding the UTC offset, e.g., +5 h in EST). | topic2-prompt9-builtin-sql-functions.md | Medium |
| 31 | B | `arrays_overlap(array(1,2,3), array(3,4,5))` returns `true` because the value `3` appears in both arrays. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 32 | B | `map_from_arrays(keys, values)` constructs a `MapType` from two equal-length arrays; the result is `map('a', 1, 'b', 2)`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 33 | A | `ROWS BETWEEN` uses physical row offsets (the nth preceding/following row by position); `RANGE BETWEEN` uses logical value distance so multiple rows with the same ORDER BY value may all fall within the range boundary. | topic2-prompt10-window-functions.md | Medium |
| 34 | B | `map_concat(map1, map2)` merges maps left-to-right; when both maps share a key, the right map's value overwrites the left map's value. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 35 | B | `SELECT * FROM t TABLESAMPLE (10 PERCENT)` randomly samples approximately 10% of the rows using Spark SQL's native TABLESAMPLE syntax. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 36 | A, B, C | ROLLUP super-aggregate rows: both columns NULL = grand total (A); region set, country NULL = regional subtotal (B); the `GROUPING()` function is needed to distinguish super-aggregate NULLs from actual NULL data (C). D is false: `ROLLUP(a,b)` produces 3 grouping sets; `CUBE(a,b)` produces 4. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 37 | B | `overlay('Spark SQL', 'DataFrame', 7)` takes the first 6 characters (`'Spark '`), inserts `'DataFrame'`, and discards the rest of the original string → `'Spark DataFrame'`. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 38 | A | `EXCEPT ALL` removes exactly one occurrence of a matching row per occurrence in the right relation, preserving extra duplicates in the left. `EXCEPT DISTINCT` removes all occurrences of any row present in the right. | topic2-prompt8-spark-sql-fundamentals.md | Hard |
| 39 | D | Both B (`QUALIFY` clause, added in Spark 3.4) and C (subquery with a `WHERE` filter on the window rank column) correctly retrieve the highest-salary row per department; B is the more concise syntax. A is invalid — window functions cannot appear in a `WHERE` clause. | topic2-prompt8-spark-sql-fundamentals.md | Hard |
| 40 | A, B, C, D | `INTERSECT ALL` preserves duplicate rows up to the minimum count in both sides (A); `INTERSECT DISTINCT` deduplicates the result (B); they are equivalent when no duplicates exist (C); both are supported in Spark SQL (D). E is false — DISTINCT returns fewer or equal rows than ALL. | topic2-prompt8-spark-sql-fundamentals.md | Hard |
| 41 | D | `F.col('column_name')` without a DataFrame qualifier is ambiguous after a join when both sides have the same column name, raising `AnalysisException: Reference is ambiguous`. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 42 | B | `.option('compression', 'snappy').parquet('/output')` is the correct chained call; option C uses `'codec'` which is not the standard Parquet write option key. | topic3-prompt19-reading-writing-dataframes.md | Easy |
| 43 | C | `F.input_file_name()` returns the full source file path for each row when reading from a directory of files; useful for file provenance tracking. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 44 | B | `df.write.text()` requires the DataFrame to have exactly one column of `StringType`; any other type must be explicitly cast to string first. | topic3-prompt19-reading-writing-dataframes.md | Easy |
| 45 | B | `schema.json()` returns the JSON string representation of a `StructType`; `schema.prettyJson()` returns a formatted multi-line version (both methods exist, but `json()` is the standard compact form). | topic3-prompt21-schemas-data-types.md | Easy |
| 46 | A | `.option('maxRecordsPerFile', 100000)` instructs the DataFrameWriter to cap each output file at 100,000 rows, splitting into additional files as needed. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 47 | A | `spark.read.csv('/path', nullValue='N/A')` is valid; the `csv()` reader accepts `nullValue` as a keyword argument and maps the literal string `'N/A'` to `null` in the resulting DataFrame. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 48 | B | `.option('pathGlobFilter', '*.parquet')` restricts the file scan to only files matching the glob pattern, excluding `.crc` checksums and other non-data files. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 49 | B | `from pyspark.sql.functions import broadcast` is the correct import; `broadcast()` is a regular function in the `pyspark.sql.functions` module, not a separate hints module. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 50 | B | `df.rdd` returns an RDD of `Row` objects; fields are accessible by name (e.g., `row.col_name`) or by index, not as plain dicts or tuples. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 51 | C | `df.withColumn('existing_col', new_expr)` replaces the named column in-place with the result of `new_expr`; it does not append a duplicate column or raise an error. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 52 | B | `F.coalesce(col1, col2, col3)` evaluates columns left-to-right and returns the first non-null value; it is the standard pattern for null-safe fallback chains. | topic3-prompt16-handling-nulls.md | Medium |
| 53 | A | `df1.exceptAll(df2)` removes one matching row from `df1` per occurrence in `df2`, preserving extra duplicates. `df1.subtract(df2)` removes ALL occurrences of any row present in `df2` (equivalent to `EXCEPT DISTINCT`). | topic3-prompt18-combining-dataframes.md | Medium |
| 54 | B | Parquet files embed the schema in each file's footer metadata; Spark reads this footer without scanning row data, making schema inference fast and accurate. | topic3-prompt21-schemas-data-types.md | Medium |
| 55 | B | `@pandas_udf(returnType=DoubleType())` from `pyspark.sql.functions` is the correct decorator for a vectorised UDF; it uses Apache Arrow for efficient `pd.Series` ↔ JVM transfer. | topic3-prompt22-udfs.md | Medium |
| 56 | B | `cache()` is a lazy transformation that only marks the DataFrame for caching; calling `count()` (an action) triggers plan execution and physically materialises the data in memory. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 57 | A | `spark.createDataFrame([Row(a=1, b='x'), Row(a=2, b='y')])` correctly creates a DataFrame from a list of `Row` objects; Spark infers the schema from the Row field names. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 58 | A | `DataFrameWriter.jdbc(url, table, mode, properties)` — the second positional parameter is named `table` and specifies the target database table name. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 59 | A, C | Parallel JDBC reads: `numPartitions` + `partitionColumn` + `lowerBound`/`upperBound` (A) splits the query into N range-based partitions; `predicates` (C) provides custom per-partition WHERE clauses. `fetchsize` (B) affects batch fetch size but not parallelism; `batchsize` (D) is a write option. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 60 | B | By default `df.write.csv()` does NOT write column headers; set `.option('header', True)` to include the header row in each output CSV file. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 61 | A | `coalesce(5)` is a narrow transformation that merges existing partitions without a shuffle; `repartition(5)` performs a full shuffle to redistribute data evenly across exactly 5 partitions. | topic3-prompt20-repartition-coalesce.md | Medium |
| 62 | B | `df.selectExpr('age * 2 AS double_age')` evaluates the SQL expression string inline, requiring no function imports, and renames the result in a single step. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 63 | C | Without `.otherwise()`, a `when()` chain implicitly returns `null` for any row where no condition is met — equivalent to appending `.otherwise(F.lit(None))`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 64 | B | `sdf.to_pandas_on_spark()` returns a `pyspark.pandas.DataFrame` (Pandas API on Spark), keeping data distributed on the cluster rather than collecting to the driver. | topic7-prompt30-pandas-api.md | Medium |
| 65 | B | `StructType` implements value-based `__eq__`; two independently created schemas with identical field names, data types, and nullable flags compare equal regardless of object identity. | topic3-prompt21-schemas-data-types.md | Hard |
| 66 | B | `applyInPandas` (the modern replacement for `GROUPED_MAP` Pandas UDF) invokes the function once per group key, passing all rows for that group as a single `pd.DataFrame`. | topic3-prompt22-udfs.md | Hard |
| 67 | A, C | `df.rdd.getNumPartitions()` (A) is the standard method; `len(df.rdd.partitions)` (C) accesses the partitions list directly. `df.getNumPartitions()` (B) does not exist on `DataFrame`; `df.repartitionNum` (D) does not exist. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 68 | B | `spark.read.load('/data/events', format='delta')` is valid; `DataFrameReader.load()` accepts `format` as a keyword argument. `spark.read.delta()` (A) is a Delta Lake convenience shortcut available in Databricks but not in vanilla Spark. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 69 | D | For JDBC reads with a subquery: both `dbtable` (C) accepting a parenthesised subquery with an alias AND the explicit `query` option (B) are supported; D correctly identifies that both work. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 70 | A, B, E | Valid Parquet write options: `compression` (A), `maxRecordsPerFile` (B), and `partitionOverwriteMode` (E). `header` (C) is a CSV-only option; `mergeSchema` (D) is a Parquet read option (or a Delta Lake write option, not plain Parquet). | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 71 | B | Setting `spark.sql.autoBroadcastJoinThreshold = -1` explicitly disables automatic BroadcastHashJoin for all joins regardless of table size. | topic4-prompt24-performance-tuning.md | Easy |
| 72 | B | `ANALYZE TABLE orders COMPUTE STATISTICS FOR ALL COLUMNS` collects column-level statistics (min, max, NDV, null count) required by the Cost-Based Optimizer for join ordering and strategy selection. | topic4-prompt24-performance-tuning.md | Easy |
| 73 | B | `CACHE TABLE sales` in Spark SQL is eager: it immediately scans and materialises the table in distributed in-memory columnar format. `CACHE LAZY TABLE` is the lazy variant. | topic4-prompt24-performance-tuning.md | Medium |
| 74 | B | `spark.sql.adaptive.advisoryPartitionSizeInBytes` (default 64 MB) is the target partition size AQE aims for when coalescing small post-shuffle partitions; it is advisory, not a hard cap. | topic4-prompt24-performance-tuning.md | Medium |
| 75 | A | ORC is the native format for Apache Hive; workloads that produce ORC files via Hive benefit from native ORC statistics, BloomFilters, and stripe-level predicate pushdown when read back with Spark. | topic4-prompt24-performance-tuning.md | Medium |
| 76 | B | CBO join reordering requires both `spark.sql.cbo.enabled = true` (to activate the CBO) and `spark.sql.cbo.joinReorder.enabled = true` (to enable the multi-join reorder sub-feature). | topic4-prompt24-performance-tuning.md | Medium |
| 77 | B | `spark.sql.join.preferSortMergeJoin = true` makes Spark prefer SortMergeJoin over ShuffledHashJoin when both are applicable; it does not disable BroadcastHashJoin. | topic4-prompt24-performance-tuning.md | Medium |
| 78 | C | `df.explain('cost')` annotates the logical and physical plan with CBO estimates (row count, data size per operator). `'extended'` shows all plan stages; `'formatted'` is human-readable but omits cost figures. | topic4-prompt26-debugging.md | Medium |
| 79 | A, B, D | `spark.sql.adaptive.coalescePartitions.minPartitionNum` sets a lower bound on partition count after AQE coalescing (A, B), preventing over-coalescing. Setting it to `1` allows AQE to collapse all output to a single partition (D). It has no direct effect on executor count (C). | topic4-prompt24-performance-tuning.md | Hard |
| 80 | A | `spark.sql.inMemoryColumnarStorage.compressed` (default `true`) enables Snappy compression on in-memory columnar cached data, reducing memory footprint at a small CPU cost. | topic4-prompt24-performance-tuning.md | Hard |
| 81 | B | `trigger(once=True)` processes all available source data in a single micro-batch and then automatically stops the streaming query; useful for scheduled incremental batch processing. | topic5-prompt27-structured-streaming.md | Easy |
| 82 | B | `option('checkpointLocation', '/path')` is mandatory for fault tolerance and exactly-once guarantees; the checkpoint stores source offsets and the sink commit log for recovery. | topic5-prompt27-structured-streaming.md | Easy |
| 83 | C | `update` output mode emits only the rows whose aggregate values changed or were newly created since the last trigger; unchanged rows are not re-emitted. | topic5-prompt27-structured-streaming.md | Medium |
| 84 | C | `complete` output mode requires a full aggregation so that the entire result table can be recomputed and output on every trigger; it is not supported for non-aggregated streaming queries. | topic5-prompt27-structured-streaming.md | Medium |
| 85 | A | A 5-minute tumbling window `[HH:mm, HH:mm+5)` is non-overlapping; event at `12:07` falls in `[12:05, 12:10)` only. | topic5-prompt28-stateful-streaming.md | Medium |
| 86 | B | `foreachBatch(func)` receives each micro-batch as a complete static DataFrame plus a batch ID, enabling arbitrary multi-sink writes, deduplication, or any processing not supported by built-in sinks. | topic5-prompt27-structured-streaming.md | Medium |
| 87 | A | `failOnDataLoss = false` instructs Spark to silently skip Kafka offsets no longer available due to topic retention policies rather than failing the streaming query. | topic5-prompt27-structured-streaming.md | Medium |
| 88 | B | `query.recentProgress` returns a Python list of dicts, each representing one recently completed micro-batch progress report (batchId, inputRowsPerSecond, durationMs breakdown, etc.). | topic5-prompt27-structured-streaming.md | Medium |
| 89 | B | `StreamingQueryListener` from `pyspark.sql.streaming` provides `onQueryStarted`, `onQueryProgress`, and `onQueryTerminated` callbacks for cluster-wide streaming query monitoring without modifying individual query code. | topic5-prompt27-structured-streaming.md | Hard |
| 90 | A, B, C, D, E | All statements are correct: `withWatermark()` must precede `groupBy(window(...))` (A); watermark governs state eviction (B); `append` mode emits window results only after the watermark advances past the window end (C); `complete` mode emits all current state on every trigger regardless of watermark (D); the watermark and window should operate on the same timestamp column (E). | topic5-prompt28-stateful-streaming.md | Hard |
| 91 | B | The `SPARK_REMOTE` environment variable, when set to a `sc://` URL, is automatically picked up by `SparkSession.builder.getOrCreate()` to establish a Spark Connect connection without calling `.remote()` explicitly. | topic6-prompt29-spark-connect.md | Easy |
| 92 | C | Spark Connect uses Apache Arrow columnar format for efficient binary serialisation of query results between client and server over gRPC, avoiding Java/Kryo serialisation overhead. | topic6-prompt29-spark-connect.md | Easy |
| 93 | B | In a Spark Connect application, `SparkSession.getActiveSession()` returns the client-side proxy session object representing the remote connection, not a server-side session or `None`. | topic6-prompt29-spark-connect.md | Medium |
| 94 | B | `DatabricksSession.builder.serverless().getOrCreate()` is the Databricks-specific builder pattern for creating a Spark Connect session that targets Databricks Serverless compute. | topic6-prompt29-spark-connect.md | Medium |
| 95 | A, B, D | With Spark Connect: client code runs as a lightweight local process (A); `spark-submit` ships the driver to the cluster while Spark Connect keeps it on the client machine (B); multiple clients can share a running Spark server session (D). C is false — Spark Connect does not expose `SparkContext`/RDD APIs. E is false — `spark-submit` is not deprecated. | topic6-prompt29-spark-connect.md | Hard |
| 96 | B | `ps.sql('SELECT ...')` executes a SQL query against Pandas API on Spark DataFrames registered as temp views; analogous to `spark.sql()` in the standard PySpark API. | topic7-prompt30-pandas-api.md | Easy |
| 97 | D | Both `ps.read_parquet('/path')` (A) and `spark.read.parquet('/path').pandas_api()` (C) are valid approaches for reading a Parquet file into a Pandas API on Spark DataFrame. | topic7-prompt30-pandas-api.md | Easy |
| 98 | B | `psdf.apply(func, axis=1)` applies `func` row-wise: each row is passed to `func` as a `pd.Series` with column names as the index, enabling row-level transformations. | topic7-prompt30-pandas-api.md | Medium |
| 99 | B | `psdf['value'].rolling(3).mean()` computes a 3-row trailing rolling average distributed across Spark partitions; the first two rows yield `NaN` because there are fewer than 3 preceding values. | topic7-prompt30-pandas-api.md | Medium |
| 100 | A, B, D, E | Default concat stacks rows (axis=0) (A); axis=1 joins side-by-side by index and requires `ops_on_diff_frames=True` when DataFrames originate from different plans (B); `ignore_index=True` resets the resulting index (D); axis=1 concat performs a distributed index-based merge (E). C is false — the original index is preserved unless `ignore_index=True`. | topic7-prompt30-pandas-api.md | Hard |
