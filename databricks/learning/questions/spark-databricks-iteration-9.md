# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 9)

**Iteration**: 9
**Generated**: 2026-04-25
**Total Questions**: 100
**Difficulty Split**: 10 Easy / 54 Medium / 36 Hard
**Answer Types**: 100 `one`

---

## Questions

---

### Question 1 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: SparkSession vs SparkContext — relationship and entry points in Spark 2.x/3.x

**Question**:
A developer is starting a new PySpark application and needs to access both DataFrame and RDD APIs. What is the correct relationship between `SparkSession` and `SparkContext` in Spark 3.x?

- A) `SparkContext` wraps `SparkSession`; you create a `SparkContext` first and call `.session()` on it
- B) `SparkSession` and `SparkContext` are completely independent; both must be created separately
- C) `SparkSession` wraps `SparkContext`; `spark.sparkContext` returns the underlying `SparkContext` (correct answer — `SparkSession` is the unified entry point introduced in Spark 2.0; it creates and manages the `SparkContext` internally. You access the `SparkContext` via `spark.sparkContext`. There is never a reason to create a `SparkContext` manually when using `SparkSession.builder`.
- D) `SparkContext` is deprecated in Spark 3.x and cannot be accessed from `SparkSession`

---

### Question 2 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.default.parallelism vs spark.sql.shuffle.partitions — which controls what

**Question**:
A team is experiencing slow RDD-based aggregations but fast DataFrame shuffle operations. They set `spark.default.parallelism=400` hoping to fix both. Which statement correctly describes the scope of `spark.default.parallelism`?

- A) It controls the default number of shuffle partitions for both RDD and DataFrame operations
- B) It controls the default number of partitions only for RDD transformations like `reduceByKey` and `join`; DataFrame shuffle partitions are controlled by `spark.sql.shuffle.partitions` (correct answer — `spark.default.parallelism` applies to RDD-level operations. `spark.sql.shuffle.partitions` (default: 200) governs Spark SQL/DataFrame shuffles. Setting only `spark.default.parallelism` will not change DataFrame shuffle behavior.
- C) It controls the default partition count when reading files from HDFS or S3
- D) It applies to both RDD and DataFrame operations, making `spark.sql.shuffle.partitions` redundant

---

### Question 3 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Executor memory breakdown — spark.executor.memory, memoryOverhead, and off-heap regions

**Question**:
An executor is configured with `spark.executor.memory=4g`, `spark.executor.memoryOverhead=512m`, and `spark.memory.offHeap.size=1g` with `spark.memory.offHeap.enabled=true`. What is the total container memory requested from YARN for this executor?

- A) 4 GB (only `spark.executor.memory` counts toward container allocation)
- B) 4.5 GB (`spark.executor.memory` + `spark.executor.memoryOverhead`)
- C) 5.5 GB — `spark.executor.memory` (4 GB) + `spark.executor.memoryOverhead` (512 MB) + `spark.memory.offHeap.size` (1 GB) (correct answer — YARN container memory = JVM heap + off-heap overhead + off-heap Tungsten memory. All three regions are requested. The off-heap Tungsten region (`offHeap.size`) is a separate allocation outside the JVM heap and must be accounted for in the container request.
- D) 5 GB (`spark.executor.memory` + `spark.memory.offHeap.size`, overhead is internal)

---

### Question 4 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Stage boundary conditions — wide vs narrow transformations and when a new stage is created

**Question**:
Given the following PySpark chain: `rdd.map(...).filter(...).groupByKey().map(...).join(other_rdd)`, how many stages will the DAGScheduler create?

- A) 1 stage — all operations are pipelined together
- B) 2 stages — one stage before `groupByKey` and one after
- C) 3 stages — `groupByKey` creates stage 2, and `join` creates stage 3 (correct answer — each wide transformation (shuffle boundary) creates a new stage. `groupByKey` is a wide transformation introducing the first shuffle boundary (end of stage 1, start of stage 2). `join` is also a wide transformation introducing a second shuffle boundary (end of stage 2, start of stage 3). Narrow transformations (`map`, `filter`) are pipelined within stages.
- D) 4 stages — each `map` creates an additional stage

---

### Question 5 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.task.cpus — vCPUs per task and impact on executor parallelism

**Question**:
An executor is configured with `spark.executor.cores=8` and `spark.task.cpus=2`. How many tasks can run concurrently on this executor, and what is the primary use case for setting `spark.task.cpus > 1`?

- A) 8 concurrent tasks; `spark.task.cpus` only affects scheduling priority, not parallelism
- B) 4 concurrent tasks; used for tasks that leverage multi-threaded libraries like TensorFlow or BLAS (correct answer — concurrent tasks per executor = `executor.cores / task.cpus` = 8 / 2 = 4. This configuration is used when each task spawns multiple threads internally, such as deep learning inference or native linear algebra libraries, to prevent CPU over-subscription.
- C) 8 concurrent tasks; `spark.task.cpus` is a soft hint that YARN may ignore
- D) 2 concurrent tasks; `spark.task.cpus` must equal `spark.executor.cores` divided by 4

---

### Question 6 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.speculation — speculative execution conditions and risk

**Question**:
`spark.speculation=true` is enabled on a cluster. A task in stage 3 has been running for 3 minutes while the median task time is 30 seconds. What will Spark do, and what is the associated risk?

- A) Spark will kill the slow task and reschedule it; risk is data loss if the task wrote output
- B) Spark will launch a duplicate speculative copy of the task on another executor; both run simultaneously, and the first to finish wins; risk is that non-idempotent side effects may execute twice (correct answer — speculative execution launches a copy of the straggler task on a free executor. Both the original and the copy run concurrently. The first result is accepted and the other is killed. The risk is that tasks with side effects (e.g., writing to external systems) may execute twice.
- C) Spark will only speculate if the task fails; speculation does not apply to running tasks
- D) Spark will pause the slow task and give its CPU to faster tasks in the same stage

---

### Question 7 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.network.timeout vs spark.executor.heartbeatInterval — relationship and correct configuration

**Question**:
A cluster administrator sets `spark.network.timeout=120s`. What constraint must they also satisfy to prevent false executor timeouts?

- A) `spark.executor.heartbeatInterval` must be greater than `spark.network.timeout`
- B) `spark.executor.heartbeatInterval` must be less than `spark.network.timeout`; a common rule is to keep heartbeat interval at least 4× less than network timeout (correct answer — `spark.network.timeout` is the inactivity timeout for all network interactions. If `heartbeatInterval` were equal to or greater than `network.timeout`, the driver would declare the executor dead before receiving its heartbeat, causing false failures. The heartbeat interval must always be less than the network timeout.
- C) Both settings must be set to the same value for consistency
- D) `spark.executor.heartbeatInterval` is unrelated to `spark.network.timeout` and controls only RPC messaging

---

### Question 8 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: External shuffle service — spark.shuffle.service.enabled and dynamic allocation

**Question**:
A team enables dynamic allocation (`spark.dynamicAllocation.enabled=true`) but shuffle jobs fail after idle executors are removed. What configuration is required to fix this, and why?

- A) Increase `spark.dynamicAllocation.executorIdleTimeout` to prevent executor removal during shuffle
- B) Enable `spark.shuffle.service.enabled=true` on all worker nodes so shuffle files remain accessible after executors are released (correct answer — without the external shuffle service, shuffle output files are stored on executor JVM processes. When dynamic allocation removes idle executors, those shuffle files are lost, causing stage failures. The external shuffle service runs as a separate long-lived daemon and serves shuffle files independent of executor lifecycle.
- C) Set `spark.dynamicAllocation.minExecutors=1` to prevent all executors from being removed
- D) Disable speculative execution, which conflicts with dynamic allocation

---

### Question 9 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.serializer — KryoSerializer vs JavaSerializer performance characteristics

**Question**:
A Spark application processes large RDDs of custom case classes and experiences slow shuffle performance. The team switches from the default serializer to `KryoSerializer`. Which statement accurately describes what changes?

- A) Kryo is slower than Java serialization but produces smaller byte representations
- B) Kryo is faster and produces more compact output than Java serialization, but requires explicit class registration via `spark.kryo.classesToRegister` for optimal performance (correct answer — Kryo is typically 5–10× faster than Java serialization and produces 2–5× smaller output. However, without registering classes via `spark.kryo.classesToRegister` (or `spark.kryo.registrationRequired=true`), Kryo uses class names as strings, reducing but not eliminating the size advantage. Java serialization is the default for compatibility.
- C) Kryo serialization is only supported for RDD operations; DataFrames always use Java serialization
- D) Switching to Kryo requires rewriting all custom classes to implement `java.io.Serializable`

---

### Question 10 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.executor.cores — task slots per executor and YARN container relationship

**Question**:
A YARN cluster has nodes with 16 physical CPU cores. An executor is configured with `spark.executor.cores=5`. How many task slots does each executor have, and how many such executors can fit on one node (assuming only CPU is the constraint)?

- A) 5 task slots per executor; 3 executors per node (15 cores used, 1 left over)
- B) 5 task slots per executor; 3 executors per node (correct — `spark.executor.cores` directly sets the number of concurrent task slots. With 16 cores and 5 cores per executor, `floor(16/5) = 3` executors fit per node, using 15 cores. The remaining 1 core may be reserved for the OS or YARN NodeManager.
- C) 1 task slot per executor regardless of `spark.executor.cores`; the setting only requests CPU from YARN
- D) 16 task slots per executor; `spark.executor.cores` only limits CPU scheduling, not parallelism

---

### Question 11 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.eventLog.rolling.enabled — preventing oversized event log files in long-running applications

**Question**:
A streaming application has been running for 72 hours and the Spark History Server is unable to load its event log due to memory pressure. What configuration can prevent this?

- A) Set `spark.eventLog.compress=true` to reduce event log size
- B) Enable `spark.eventLog.rolling.enabled=true`; Spark will split event logs into multiple smaller files based on `spark.eventLog.rolling.maxFileSize` (correct answer — rolling event logs segment the event log into separate files, preventing any single file from growing indefinitely. The History Server can load individual segments on demand rather than the entire log, resolving memory pressure. Without rolling, a long-running application's event log can grow to gigabytes.
- C) Set `spark.ui.retainedJobs=10` to limit the number of jobs stored in the event log
- D) Configure `spark.eventLog.dir` on a fast NVMe disk to allow faster log writes

---

### Question 12 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Cluster mode vs client mode — driver location and sc.addFile() path implications

**Question**:
A developer runs `spark-submit --deploy-mode cluster` and the job fails because `sc.addFile("/local/path/data.csv")` cannot find the file. What is the root cause?

- A) `sc.addFile()` is not supported in cluster mode
- B) In cluster mode the driver runs on a worker node in the cluster, not on the submitting machine; `/local/path/data.csv` does not exist on the worker node where the driver is launched (correct answer — in client mode, the driver runs on the submitting machine so local paths work. In cluster mode, the driver JVM is started on a cluster worker, and local filesystem paths on the submit machine are inaccessible. Files must be placed on shared storage (HDFS, S3) or distributed before the job.
- C) `sc.addFile()` requires an absolute HDFS path regardless of deploy mode
- D) The file path must use a `file://` URI scheme in cluster mode

---

### Question 13 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.files.openCostInBytes — small file cost estimation and its effect on partition planning

**Question**:
A directory contains 10,000 small JSON files averaging 1 KB each. `spark.sql.files.openCostInBytes` is set to its default of 4 MB. How does this setting affect partition assignment?

- A) It has no effect on JSON reads; it only applies to Parquet files
- B) Files smaller than `openCostInBytes` are grouped together in the same partition; the cost models treating each small file as if it were 4 MB, so ~4 small files pack into one partition instead of one file per partition (correct answer — `openCostInBytes` is a synthetic "open cost" added to each file's size when computing partition splits. Since each 1 KB file is treated as 4 MB + 1 KB ≈ 4 MB, about 32 files would pack into a 128 MB partition (`maxPartitionBytes` default). This prevents creating 10,000 single-file partitions and the associated task overhead.
- C) Spark creates one partition per file; `openCostInBytes` only affects write operations
- D) `openCostInBytes` forces Spark to read each file twice for cost estimation before partitioning

---

### Question 14 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.reducer.maxSizeInFlight — max shuffle data fetched simultaneously per reduce task

**Question**:
During a large shuffle, reduce tasks are requesting map output from dozens of executors simultaneously, causing network saturation. Which configuration limits the total data a reduce task fetches concurrently?

- A) `spark.shuffle.compress` — enabling compression reduces the in-flight data volume
- B) `spark.reducer.maxSizeInFlight` — limits the total bytes a single reduce task fetches from all map outputs simultaneously (default: 48 MB) (correct answer — `spark.reducer.maxSizeInFlight` bounds how much map output data one reducer fetches at a time. Lowering it reduces network concurrency per task. Raising it can improve throughput when the network is not saturated but executors are idle waiting for more data.
- C) `spark.shuffle.maxChunksBeingTransferred` — limits the number of blocks per connection
- D) `spark.network.maxRemoteBlockSizeFetchToMem` — controls whether large shuffle blocks use disk

---

### Question 15 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.shuffle.compress vs spark.shuffle.spill.compress — what each configuration compresses

**Question**:
A developer wants to reduce shuffle disk I/O by enabling compression. They find two related settings: `spark.shuffle.compress` and `spark.shuffle.spill.compress`. What is the difference?

- A) `spark.shuffle.compress` compresses shuffle output files written to disk after a shuffle stage completes; `spark.shuffle.spill.compress` compresses data spilled to disk during the shuffle sort (correct answer — `spark.shuffle.compress` applies to the shuffle output block files that map tasks write. `spark.shuffle.spill.compress` applies to the intermediate spill files written when a task's in-memory sort buffer is exhausted. Both default to `true` and use the codec from `spark.io.compression.codec`.
- B) `spark.shuffle.compress` compresses shuffle data in memory; `spark.shuffle.spill.compress` applies only to disk writes
- C) They are aliases for the same setting; only one needs to be set
- D) `spark.shuffle.spill.compress` is deprecated since Spark 3.0; only `spark.shuffle.compress` should be used

---

### Question 16 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.broadcast.compress — whether broadcast variables are compressed

**Question**:
A broadcast variable containing a large lookup dictionary is sent to all executors. A developer notices the broadcast takes a long time. They ask whether `spark.broadcast.compress` affects broadcast transmission. What is correct?

- A) `spark.broadcast.compress` is `false` by default; broadcasts are always sent uncompressed
- B) `spark.broadcast.compress` is `true` by default; broadcast blocks are compressed before transmission using the codec from `spark.io.compression.codec`, reducing network transfer time (correct answer — compression is enabled by default for broadcast variables. The variable is serialized, compressed at the driver, and sent to executors which decompress it locally. For large broadcast variables this reduces network I/O at the cost of CPU for compression/decompression.
- C) `spark.broadcast.compress` only applies when the broadcast variable exceeds 10 MB
- D) Broadcast variables are never compressed; only shuffle data can be compressed

---

### Question 17 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.cleaner.periodicGC.interval — triggering JVM GC to release cached references

**Question**:
A long-running Spark application accumulates unreferenced broadcast variables and cached RDDs that are not being garbage collected, leading to executor memory pressure. Which configuration triggers periodic JVM garbage collection to release these stale references?

- A) `spark.memory.fraction` — reducing this forces more frequent evictions
- B) `spark.cleaner.periodicGC.interval` — sets the interval at which Spark triggers a JVM GC cycle on the driver to release weak references to unpersisted RDDs and destroyed broadcast variables (default: 30 minutes) (correct answer — Spark's `ContextCleaner` uses weak references to track cached RDDs and broadcast variables. When references become unreachable, they are enqueued for cleanup. `periodicGC.interval` triggers JVM GC so those weak references are actually collected and cleanup is performed. Without it, GC may not happen frequently enough in batch applications.
- C) `spark.storage.unrollMemoryThreshold` — raising this threshold defers caching and reduces pressure
- D) `spark.executor.heartbeatInterval` — shorter heartbeats cause executors to GC more frequently

---

### Question 18 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.memory.offHeap.enabled and spark.memory.offHeap.size — off-heap Tungsten memory

**Question**:
A Spark job processes large DataFrames and experiences frequent GC pauses. The team decides to enable off-heap memory. After setting `spark.memory.offHeap.enabled=true` and `spark.memory.offHeap.size=2g`, what portion of execution and storage memory uses off-heap?

- A) Only storage memory (cached DataFrames) uses off-heap; execution memory remains on-heap
- B) Both execution and storage memory can use the off-heap region; the unified memory manager allocates from the off-heap pool in addition to the JVM heap, helping reduce GC pressure for large DataFrame operations (correct answer — enabling off-heap memory extends Spark's unified memory model. Both execution memory (used during shuffles, aggregations, joins) and storage memory (cached blocks) can be allocated off-heap via Tungsten's direct memory manager. This reduces GC pause times since off-heap memory is not tracked by the JVM garbage collector.
- C) Only execution memory uses off-heap; storage (cache) always stays in the JVM heap
- D) Off-heap memory is used exclusively for shuffle spill files on disk

---

### Question 19 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.dynamicAllocation.executorIdleTimeout — when idle executors are removed

**Question**:
Dynamic allocation is enabled. An executor has completed all its tasks and has been sitting idle. `spark.dynamicAllocation.executorIdleTimeout` is set to `60s`. What happens after 60 seconds of idle time?

- A) The executor sends a heartbeat to the driver requesting new tasks; if none are available it shuts down after another 60 seconds
- B) Spark requests the cluster manager to decommission the idle executor after 60 seconds of inactivity, returning the resources to the pool (correct answer — `executorIdleTimeout` controls how long an executor with no running tasks is kept alive. After the timeout, the driver notifies the cluster manager to release the executor's resources. Executors with cached data are retained longer, governed by `spark.dynamicAllocation.cachedExecutorIdleTimeout`.
- C) The executor is immediately killed and its shuffle files are deleted from disk
- D) Dynamic allocation only adds executors; idle timeout is not actually implemented

---

### Question 20 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.statistics.histogram.enabled — column histograms for the CBO

**Question**:
A team runs `ANALYZE TABLE orders COMPUTE STATISTICS FOR COLUMNS amount, category`. They want the cost-based optimizer to make better join reordering decisions based on data distribution. What must be true for column histograms to be collected?

- A) Histograms are always collected by `ANALYZE TABLE`; no additional configuration is needed
- B) `spark.sql.statistics.histogram.enabled` must be set to `true` (default: false); otherwise only basic statistics (min, max, count, nullCount) are collected, not height-balanced histograms (correct answer — by default `ANALYZE TABLE` collects basic column statistics. Column histograms require explicitly enabling `spark.sql.statistics.histogram.enabled=true`. Histograms allow the CBO to estimate selectivity of range predicates and improve join ordering for skewed distributions.
- C) Histograms are only supported on numeric columns; `category` (a string) would cause an error
- D) `spark.sql.cbo.enabled` must be `false` to prevent the optimizer from ignoring histogram data

---

### Question 21 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: lpad() / rpad() — left and right padding strings to a fixed length

**Question**:
A developer needs to zero-pad all product codes to exactly 8 characters for a legacy system. The codes are stored in a column `code` of varying lengths. Which SQL expression correctly produces the padded output?

- A) `RPAD(code, 8, '0')` — pads from the right with zeros
- B) `LPAD(code, 8, '0')` — pads from the left with zeros to a total length of 8 characters (correct answer — `LPAD(str, len, pad)` extends the string to `len` characters by prepending `pad`. For a code `'123'`, `LPAD('123', 8, '0')` returns `'00000123'`. If the string is already longer than `len`, it is truncated from the right.
- C) `FORMAT('%08d', CAST(code AS INT))` — formats as zero-padded integer
- D) `CONCAT(REPEAT('0', 8 - LENGTH(code)), code)` — this is equivalent but is the definition of LPAD, not the answer

---

### Question 22 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: locate(substr, str, pos) — find position of substring; 1-based; returns 0 on no match

**Question**:
A developer runs `SELECT locate('at', 'data at scale', 6)` in Spark SQL. What does this return?

- A) 2 — the first occurrence of `'at'` in the string starting from position 1
- B) 8 — `locate` starts searching from position 6; the `'at'` in `'at scale'` starts at position 8 (correct answer — `locate(substr, str, pos)` is 1-based. It starts searching from position `pos`. The string `'data at scale'` has `'at'` at positions 2 and 6. Starting from position 6, the next occurrence of `'at'` is at position 8 (`'at scale'`). If no match, it returns 0.
- C) 6 — `locate` returns the search start position when the substring exists beyond it
- D) 0 — there is no occurrence of `'at'` after position 6

---

### Question 23 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: repeat(str, n) — repeating a string N times; behavior with 0 or negative n

**Question**:
A developer runs `SELECT repeat('ab', 0)` in Spark SQL. What is returned?

- A) `NULL` — repeat with 0 returns null
- B) `''` — an empty string (correct answer — `repeat(str, n)` concatenates `str` with itself `n` times. When `n = 0`, the result is an empty string `''`. When `n` is negative, the result is also an empty string. When `str` is null, the result is null.
- C) `'ab'` — the string is returned unchanged when n is 0
- D) An `AnalysisException` because n must be a positive integer

---

### Question 24 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: window(timeCol, windowDuration, slideDuration) — tumbling and sliding time windows in SQL for batch

**Question**:
A developer writes this SQL on a static DataFrame to create 10-minute tumbling windows: `SELECT window(event_time, '10 minutes'), count(*) FROM events GROUP BY window(event_time, '10 minutes')`. What does each row in the result represent?

- A) A single event's timestamp rounded down to the nearest 10-minute boundary
- B) A non-overlapping 10-minute interval containing all events whose `event_time` falls within that window; the `window` column is a struct with `start` and `end` fields (correct answer — `window(timeCol, windowDuration)` creates tumbling (non-overlapping) windows. With `slideDuration` equal to `windowDuration`, windows do not overlap. The result column `window` is a struct `{start: TimestampType, end: TimestampType}`. This function works in both batch and streaming.
- C) A single timestamp representing the midpoint of each 10-minute bucket
- D) `window()` is only valid in structured streaming; it raises an error on batch DataFrames

---

### Question 25 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: unix_timestamp(str, format) vs to_timestamp(str, format) — epoch seconds vs TimestampType

**Question**:
A developer has a string column `ts` with values like `'2024-03-15 10:30:00'` and wants both the epoch seconds and a `TimestampType` column. Which pair of SQL expressions is correct?

- A) `unix_timestamp(ts)` returns `TimestampType`; `to_timestamp(ts)` returns `LongType`
- B) `unix_timestamp(ts)` returns `LongType` (epoch seconds since 1970-01-01 UTC); `to_timestamp(ts)` returns `TimestampType` (correct answer — `unix_timestamp(str, fmt)` converts to a Unix epoch integer (LongType). `to_timestamp(str, fmt)` parses a string into a `TimestampType` column. Both accept an optional format string. Without a format, both use `yyyy-MM-dd HH:mm:ss` as the default pattern.
- C) Both functions return `TimestampType`; use `CAST(ts AS BIGINT)` for epoch seconds
- D) `unix_timestamp` is deprecated in Spark 3.x; only `to_timestamp` should be used

---

### Question 26 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: timestampadd(unit, delta, timestamp) — Spark 3.3+ date/timestamp arithmetic with unit strings

**Question**:
A developer wants to add 3 hours to a `TimestampType` column `event_ts` using `timestampadd`. Which Spark SQL expression is correct, and what Spark version introduced this function?

- A) `timestampadd(event_ts, 3, 'HOUR')` — arguments are `(timestamp, delta, unit)`
- B) `timestampadd('HOUR', 3, event_ts)` — unit string first, delta second, timestamp third; introduced in Spark 3.3 (correct answer — `TIMESTAMPADD(unit, intExpr, timestampExpr)` follows the SQL standard argument order: unit (string like `'HOUR'`, `'DAY'`, `'MONTH'`), integer delta, then the timestamp. It was introduced in Spark 3.3 and returns the same type as the input timestamp expression.
- C) `date_add(event_ts, 3)` — this is the equivalent; `timestampadd` does not exist in Spark
- D) `event_ts + INTERVAL 3 HOURS` — `timestampadd` is only available in Databricks Runtime, not open-source Spark

---

### Question 27 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: typeof(expr) — returns the DDL type string of any expression

**Question**:
A developer runs `SELECT typeof(ARRAY(1, 2, 3))` in Spark SQL. What is the return value?

- A) `'array'` — just the outer type name
- B) `'array<int>'` — returns the full DDL type string including element type (correct answer — `typeof(expr)` returns the DDL representation of the expression's data type as a string. For `ARRAY(1, 2, 3)` with integer elements, it returns `'array<int>'`. For a struct it would return something like `'struct<a:int,b:string>'`. This is useful for dynamic schema inspection.
- C) `'ArrayType(IntegerType,true)'` — returns the Scala/Java class name of the DataType
- D) `typeof` is not a Spark SQL function; `schema_of_json` must be used instead

---

### Question 28 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: stack(n, expr1, ...) — table-generating function that unpivots N columns to rows

**Question**:
A developer writes `SELECT stack(2, 'q1', 100, 'q2', 200)` in a Spark SQL query. How many rows and columns does this produce?

- A) 1 row, 4 columns — `stack` joins the expressions into a single row
- B) 2 rows, 2 columns — `stack(n, ...)` groups the remaining expressions into `n` rows, each with `(total_exprs / n)` columns (correct answer — `stack(2, 'q1', 100, 'q2', 200)` creates 2 rows. The 4 expressions after `n=2` are split: row 1 is `('q1', 100)` and row 2 is `('q2', 200)`. Column types are inferred (`string`, `int`). Column names default to `col0`, `col1`. Often used in `LATERAL VIEW stack(...)` for unpivoting.
- C) 4 rows, 1 column — each expression becomes its own row
- D) `stack` requires a FROM clause and cannot be used in the SELECT list

---

### Question 29 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: map_contains_key(map, key) — Spark 3.3+ function vs map[key] IS NOT NULL for null-safe key check

**Question**:
A map column `attributes` may contain keys with `null` values. A developer wants to check if the key `'color'` exists regardless of whether its value is null. Which expression is semantically correct?

- A) `attributes['color'] IS NOT NULL` — returns true only when the key exists and has a non-null value; returns false when the key exists with a null value
- B) `map_contains_key(attributes, 'color')` — returns true if the key is present regardless of whether the value is null or not (correct answer — `map_contains_key(map, key)` (Spark 3.3+) correctly returns `true` when a key exists even if its associated value is `null`. In contrast, `map['key'] IS NOT NULL` returns `false` when the value is null, making it impossible to distinguish a missing key from a key with a null value.
- C) `size(map_filter(attributes, (k,v) -> k='color')) > 0` — equivalent but `map_filter` cannot filter on keys
- D) Both `map_contains_key` and `map[key] IS NOT NULL` are equivalent for all map values

---

### Question 30 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: array_position(array, element) — 1-based position lookup returning 0 on miss

**Question**:
A developer runs `SELECT array_position(array('a', 'b', 'c'), 'd')`. What does Spark return?

- A) `NULL` — element not found returns null
- B) `0` — `array_position` returns `0` when the element is not found (correct answer — `array_position(array, value)` returns the 1-based position of the first occurrence of `value`. Positions start at 1, so `array_position(array('a','b','c'), 'a')` returns `1`. When the element is **not** found, it returns `0` (not null). This differs from many other Spark functions that return null for missing values.
- C) `-1` — element not found returns -1
- D) An `ArrayIndexOutOfBoundsException` is raised when the element is missing

---

### Question 31 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: reduce(array, zero, merge [, finish]) — higher-order function for array fold

**Question**:
A developer writes `SELECT reduce(array(1, 2, 3, 4), 0, (acc, x) -> acc + x)`. What is the result, and what optional 4th argument can modify the accumulated result?

- A) `10`; the optional 4th argument `finish` is applied to the final accumulator value before returning the result
- B) `10` with no modifications possible; `reduce` with 3 args is the full signature (correct answer A is correct) — `reduce(array, initialValue, (acc, x) -> expr)` folds the array from left to right. 0+1+2+3+4=10. The optional 4th argument `finishFunc` receives the final accumulator: `reduce(array(1,2,3), 1, (acc,x)->acc*x, acc->acc+100)` would return 106. This is the correct answer: `10`; the 4th finish function transforms the final accumulator.
- C) `[1, 3, 6, 10]` — `reduce` returns an array of running totals
- D) `reduce` is not a Spark SQL function; `aggregate` must be used instead

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: date_part(field, source) — ANSI SQL extraction equivalent to extract()

**Question**:
A developer writes `SELECT date_part('QUARTER', TIMESTAMP '2024-08-15 14:30:00')`. What is returned?

- A) `8` — the month number
- B) `3` — the quarter number (correct answer — `date_part('QUARTER', source)` extracts the quarter (1–4) from a date or timestamp. August is in Q3. The function is ANSI SQL standard and is equivalent to `EXTRACT(QUARTER FROM source)`. Other valid field strings include `'YEAR'`, `'MONTH'`, `'DAY'`, `'HOUR'`, `'MINUTE'`, `'SECOND'`, `'WEEK'`, `'DOW'`.
- C) `2024` — the year component
- D) `date_part` only accepts integer-typed field codes, not string names

---

### Question 33 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: make_interval(years, months, ..., secs) — constructing intervals for date arithmetic

**Question**:
A developer needs to add 1 year and 6 months to each `start_date` timestamp in a query. Using `make_interval`, which expression achieves this?

- A) `start_date + make_interval(1, 6)` — `make_interval(years, months)` with default zeros for remaining components (correct answer — `make_interval(years, months, weeks, days, hours, mins, secs)` creates an `INTERVAL` value. `make_interval(1, 6)` is shorthand for `make_interval(1, 6, 0, 0, 0, 0, 0)` and can be added directly to a `TimestampType` or `DateType` column. Introduced in Spark 3.0.
- B) `start_date + INTERVAL '1-6' YEAR TO MONTH` — this is the equivalent literal syntax but not the `make_interval` form
- C) `date_add(start_date, make_interval(1, 6))` — `date_add` does not accept interval expressions
- D) `make_interval(1, 6)` returns an integer representing total months; it cannot be used with `+`

---

### Question 34 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ILIKE operator (Spark 3.3+) — case-insensitive pattern matching

**Question**:
A developer wants to find rows where a column `description` contains the word `'error'` regardless of case (e.g., `'Error'`, `'ERROR'`, `'eRrOr'`). They are on Spark 3.3+. Which expression is most efficient?

- A) `LOWER(description) LIKE '%error%'` — applies LOWER to every value before matching
- B) `description ILIKE '%error%'` — case-insensitive LIKE that avoids the separate `lower()` call (correct answer — `ILIKE` (Spark 3.3+) performs case-insensitive pattern matching. It is semantically equivalent to `lower(col) LIKE lower(pattern)` but more explicit and may benefit from future optimizer improvements. It is also more readable. `ILIKE ANY (pattern1, pattern2, ...)` is also supported for multi-pattern matching.
- C) `description REGEXP '(?i)error'` — regular expressions are required for case-insensitive matching in Spark SQL
- D) `UPPER(description) LIKE '%ERROR%'` — equivalent to ILIKE but is the standard approach

---

### Question 35 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: decode(expr, search1, result1, ..., default) — SQL DECODE for conditional value mapping

**Question**:
A developer writes `SELECT decode(status, 'A', 'Active', 'I', 'Inactive', 'Unknown')`. For a row with `status = NULL`, what is returned?

- A) `NULL` — decode propagates null without matching any branch
- B) `'Unknown'` — the default value is returned when no search value matches; `NULL` does not equal any search value, so the default is returned (correct answer — SQL `DECODE` is null-safe for the search comparisons: it treats `NULL = NULL` as true. However, `status = NULL` is not equal to `'A'` or `'I'`, so no match occurs and the default `'Unknown'` is returned. Note: some sources state DECODE treats `NULL = NULL` as equal — if the search value itself were `NULL`, that branch would match.
- C) An `AnalysisException` because `status` is null
- D) `''` — an empty string is returned for null input to DECODE

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: to_csv(struct_col, options) — serializing a struct column to a CSV string

**Question**:
A DataFrame has a column `row_data` of type `StructType` with fields `id INT`, `name STRING`, `score DOUBLE`. A developer calls `to_csv(row_data, map('sep', '|'))`. What is the output for a struct value `{id: 1, name: 'Alice', score: 99.5}`?

- A) `'{"id":1,"name":"Alice","score":99.5}'` — `to_csv` produces JSON format
- B) `'1|Alice|99.5'` — `to_csv` serializes the struct fields as delimiter-separated values using `sep='|'` (correct answer — `to_csv(structCol, optionsMap)` converts a struct to a CSV-formatted string. Fields are ordered by position in the schema. The `sep` option overrides the default comma delimiter. This is the inverse of `from_csv(csvString, schema, options)`.
- C) `'id=1,name=Alice,score=99.5'` — `to_csv` uses key=value format
- D) `to_csv` is not a valid Spark SQL function; only `to_json` exists for struct serialization

---

### Question 37 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: date_part vs trunc/date_trunc — extraction vs truncation semantics

**Question**:
A developer needs two things from a timestamp column `event_ts`: (1) the month number as an integer, and (2) the timestamp truncated to the start of the month. Which pair of expressions is correct?

- A) `trunc(event_ts, 'MM')` for the integer; `date_part('MONTH', event_ts)` for the truncated timestamp
- B) `date_part('MONTH', event_ts)` returns an integer month number; `date_trunc('MONTH', event_ts)` returns the timestamp at the first moment of that month (correct answer — `date_part` / `extract` extracts a numeric component from a date/timestamp. `date_trunc(format, timestamp)` sets all time components smaller than the specified unit to their minimum, returning a truncated timestamp. For month: `date_trunc('MONTH', '2024-08-15')` → `'2024-08-01 00:00:00'`.
- C) Both `date_part` and `date_trunc` return the same type; use the output format to choose
- D) `trunc(event_ts, 'month')` returns an integer; `date_part('MONTH', event_ts)` truncates the timestamp

---

### Question 38 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: LIKE ANY / ILIKE ANY (Spark 3.3+) — multi-pattern matching avoiding multiple OR conditions

**Question**:
A developer wants rows where column `event_name` matches any of three prefix patterns: starts with `'click_'`, `'tap_'`, or `'swipe_'`. They are on Spark 3.3+. Which is the most concise equivalent expression?

- A) `event_name LIKE 'click_%' OR event_name LIKE 'tap_%' OR event_name LIKE 'swipe_%'`
- B) `event_name LIKE ANY ('click_%', 'tap_%', 'swipe_%')` — matches if the column satisfies any of the listed patterns (correct answer — `LIKE ANY (pattern1, pattern2, ...)` (Spark 3.3+) returns true if the expression matches at least one pattern. It is functionally equivalent to the OR expansion but more concise. `LIKE ALL` requires matching all patterns. `ILIKE ANY` adds case-insensitivity. Both forms support standard SQL wildcard characters `%` and `_`.
- C) `event_name RLIKE '^(click|tap|swipe)_'` — regex is required since `LIKE ANY` does not exist
- D) `event_name IN ('click_%', 'tap_%', 'swipe_%')` — `IN` supports wildcard patterns in Spark SQL

---

### Question 39 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: second(ts) returns Double with fractional seconds for TIMESTAMP precision

**Question**:
A developer runs `SELECT second(TIMESTAMP '2024-08-15 14:30:45.123456')`. What type and value is returned?

- A) `45` as an `INT` — `second()` truncates fractional seconds
- B) `45.123456` as a `DOUBLE` — `second()` returns a `DoubleType` that includes fractional seconds when the input is a `TIMESTAMP` (correct answer — For `TimestampType` input, `second(ts)` returns the seconds component including microsecond fractions as a `DoubleType`. For `DateType` input (which has no time component), it would return `0.0`. This is different from `EXTRACT(SECOND FROM ts)` which also returns a decimal.
- C) `45.123456` as a `DecimalType(8, 6)` — Spark uses Decimal for fractional seconds
- D) An error — `second()` only accepts `DateType` columns

---

### Question 40 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: to_number(str, format) / try_to_number() — parsing string to Decimal with format mask

**Question**:
A developer has a column `amount_str` with values like `'$1,234.56'` and wants to parse it to a `DecimalType`. They use `to_number(amount_str, '$9,999.99')`. What happens when a value does not match the format mask?

- A) `to_number` returns null for non-matching values, similar to `try_cast`
- B) `to_number` raises a runtime error on format mismatch; `try_to_number(amount_str, '$9,999.99')` should be used to return null instead of raising an error (correct answer — `to_number(str, format)` (Spark 3.3+) raises a `SparkNumberFormatException` when the string does not conform to the format mask. The safe variant `try_to_number(str, format)` returns `null` on mismatch instead of failing, enabling graceful handling of dirty data.
- C) `to_number` silently truncates non-matching characters and parses the numeric portion
- D) `to_number` is only available in Databricks Runtime; open-source Spark uses `cast(amount_str AS DECIMAL)`

---

### Question 41 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.withMetadata(colName, metadata) — attaching column-level metadata

**Question**:
A developer attaches metadata to a column using `df2 = df.withMetadata("score", {"description": "normalized score", "unit": "percent"})`. How can the metadata be retrieved, and does this affect the column's data?

- A) `df2.schema["score"].metadata` returns the metadata dict; the column data is unchanged (correct answer — `withMetadata(colName, metadata)` creates a new DataFrame with the specified metadata attached to the named column's `StructField`. The data itself is not modified. `df2.schema["score"].metadata` returns a `pyspark.sql.types.MetaData`-compatible dict. This is useful for column documentation, feature store metadata, and integration with ML frameworks.
- B) `df2.describe("score")` returns the metadata; the column is rewritten to include metadata in each row value
- C) Metadata is stored in the Hive metastore; it persists across sessions
- D) `withMetadata` appends a separate `_score_metadata` column to the DataFrame

---

### Question 42 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.filter(~F.col("bool_col")) — bitwise NOT on boolean column

**Question**:
A DataFrame has a `BooleanType` column `is_deleted`. A developer writes `df.filter(~F.col("is_deleted"))`. Which rows are kept, and what Python operator is `~`?

- A) Rows where `is_deleted` is `true`; `~` is the Python unary positive operator
- B) Rows where `is_deleted` is `false` or `null`; `~` is Python's bitwise NOT, which Spark overloads on `Column` objects to produce a logical NOT (correct answer — In PySpark, `~col` calls `Column.__invert__()`, which maps to `NOT col` in Spark SQL. This is functionally equivalent to `F.col("is_deleted") == False` but more idiomatic. Note: rows where `is_deleted` is `null` will also be excluded because `NOT NULL` evaluates to `null` (falsy in filter context).
- C) All rows; `~` on a `Column` is a no-op
- D) An error; the `~` operator is not supported on PySpark `Column` objects

---

### Question 43 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.array_sort(array_col, comparator) — default sort vs custom comparator

**Question**:
A developer has an `ArrayType(IntegerType)` column `scores` and wants to sort each array in descending order. Which expression correctly sorts descending?

- A) `F.array_sort(F.col("scores"), F.lit("desc"))` — pass `'desc'` as the order string
- B) `F.array_sort(F.col("scores"), lambda l, r: F.when(l < r, F.lit(1)).when(l > r, F.lit(-1)).otherwise(F.lit(0)))` — custom comparator returning 1 when left should come after right (correct answer — `F.array_sort(arr, comparator)` accepts a lambda `(l, r) -> Int` where -1 means l before r, 0 means equal, 1 means l after r. To sort descending, swap the comparator: return 1 when l < r. `F.array_sort(col)` with no comparator sorts ascending with nulls last.
- C) `F.sort_array(F.col("scores"), asc=False)` — `sort_array` is the correct function for descending; `array_sort` only supports ascending
- D) `F.array_sort(F.col("scores")).desc()` — chain `.desc()` to reverse the sort

---

### Question 44 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.window_time(window_col) — extracting window end timestamp from a window struct

**Question**:
A streaming aggregation uses `F.window("event_time", "5 minutes")` as the grouping key, producing a `window` column of type `struct<start: timestamp, end: timestamp>`. The developer needs to set a watermark on the window's end time for state cleanup. Which expression extracts the correct timestamp?

- A) `F.col("window.end")` — access the nested end field directly
- B) `F.window_time(F.col("window"))` — Spark 3.4+ function specifically designed to extract the window end time for use with `withWatermark()` (correct answer — `F.window_time(windowCol)` (Spark 3.4+) is the designated function for extracting the watermark-compatible end timestamp from a window struct created by `F.window()` or `F.session_window()`. While `F.col("window.end")` works for display, `window_time()` ensures correct integration with Spark's watermark tracking for stateful streaming operators.
- C) `F.col("window").getField("end")` — `getField` is required for struct field extraction
- D) The end time cannot be used as a watermark; only input event timestamps are valid for `withWatermark()`

---

### Question 45 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.collect_list(col).over(window.orderBy) — accumulating list with ordered window frame

**Question**:
A developer creates a window spec `w = Window.partitionBy("user_id").orderBy("event_time")` and computes `F.collect_list("event").over(w)`. For user_id=1 with 3 events in time order `['login', 'click', 'purchase']`, what does the collect_list column contain for the third row (event=`'purchase'`)?

- A) `['login', 'click', 'purchase']` — all three events including the current row (correct answer — with `orderBy()` but no explicit `rowsBetween`, the default frame is `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`. For the third row with the highest `event_time`, `collect_list` accumulates all preceding rows plus the current row. The result is `['login', 'click', 'purchase']` — a running list up to and including the current row's sort key.
- B) `['purchase']` — collect_list in a window only includes the current row's value
- C) `['login', 'click']` — the current row is excluded from the window frame
- D) `['login', 'click', 'purchase']` for every row; orderBy has no effect on collect_list

---

### Question 46 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.withWatermark() on a static batch DataFrame — no-op semantics

**Question**:
A developer accidentally calls `df = static_df.withWatermark("event_time", "10 minutes")` on a batch DataFrame (not a stream). What happens?

- A) Spark raises an `AnalysisException` because `withWatermark` is only valid for streaming DataFrames
- B) `withWatermark` on a batch DataFrame is a no-op; it returns a DataFrame with the watermark annotation but the annotation has no effect during batch execution (correct answer — Spark allows `withWatermark()` to be called on batch DataFrames without error. The watermark annotation is metadata that only affects stateful streaming operators. In a batch context, watermark information is ignored entirely during planning and execution. This allows the same code to work for both batch and streaming with minimal changes.
- C) Spark filters out all rows with `event_time` older than 10 minutes from the current time
- D) The batch DataFrame is silently converted to a streaming DataFrame

---

### Question 47 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.observe() metrics in streaming + StreamingQueryListener.onQueryProgress

**Question**:
A developer attaches custom metrics to a streaming DataFrame using `df.observe("my_metrics", F.count("*").alias("row_count"), F.sum("amount").alias("total"))`. Where are these observed metrics available during streaming execution?

- A) In the Spark UI under "Streaming" → "Metrics"; they are displayed in real-time on the dashboard
- B) In `StreamingQueryListener.onQueryProgress(progress)`: the `progress.observedMetrics["my_metrics"]` dict contains the metric values for each micro-batch (correct answer — `df.observe(name, *agg_exprs)` attaches named metrics that are computed during execution. In streaming, these metrics are collected per micro-batch and reported via `StreamingQueryProgress.observedMetrics`, which is accessible in the `onQueryProgress` callback of a `StreamingQueryListener`. The metrics are also accessible via `query.recentProgress`.
- C) In `query.lastProgress` only for the very last completed micro-batch; historical values are lost
- D) `df.observe()` only works for batch DataFrames; streaming requires `df.groupBy().agg()` for metrics

---

### Question 48 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.rint(col) — round-half-even returning DoubleType (not int)

**Question**:
A developer wants to round all values in a `DoubleType` column `value` to the nearest integer. They use `F.rint(F.col("value"))`. What is the return type and rounding behavior for the value `2.5`?

- A) `IntegerType` with result `3` — `rint` rounds half-up and converts to integer
- B) `DoubleType` with result `2.0` — `rint` uses round-half-even (banker's rounding); `2.5` rounds to `2.0` since 2 is even (correct answer — `F.rint(col)` returns `DoubleType`, not `IntegerType`. It applies IEEE 754 round-half-even (banker's rounding): values at exactly the halfway point round to the nearest even integer. `2.5` → `2.0` (even), `3.5` → `4.0` (even). Use `.cast("int")` after `F.round(col, 0)` if integer output is needed.
- C) `DoubleType` with result `3.0` — `rint` rounds half-up like `F.round()`
- D) `F.rint` is not a PySpark function; only `F.round()` exists for rounding

---

### Question 49 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: createOrReplaceTempView vs createOrReplaceGlobalTempView — scope and access pattern

**Question**:
A developer creates a global temp view: `df.createOrReplaceGlobalTempView("user_events")`. How is this view accessed in SQL, and what is its lifetime?

- A) `spark.sql("SELECT * FROM user_events")` — accessed by name; lives until the JVM shuts down
- B) `spark.sql("SELECT * FROM global_temp.user_events")` — global temp views are in the `global_temp` database; the view lives as long as the Spark application (JVM) is running, shared across all `SparkSession`s in the same JVM (correct answer — global temp views use the special database name `global_temp` for access. They are shared across multiple `SparkSession` objects within the same application (same JVM). Regular temp views created with `createOrReplaceTempView` are session-scoped and not accessible from other sessions.
- C) `spark.sql("SELECT * FROM temp.user_events")` — global temp views use the `temp` schema
- D) Global temp views are accessible without a prefix; they replace session-scoped views of the same name

---

### Question 50 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.approx_percentile SQL vs df.stat.approxQuantile — API differences and accuracy parameter direction

**Question**:
A developer needs the 50th percentile of a column. They consider `F.approx_percentile("amount", 0.5, 100)` (used in `agg()`) versus `df.stat.approxQuantile("amount", [0.5], 0.01)`. What is the key difference in the accuracy parameter direction?

- A) Both use the same scale; higher value = more accurate in both cases
- B) `approx_percentile`'s third argument is `accuracy` where higher = more accurate (default 10000); `approxQuantile`'s third argument is `relativeError` where lower = more accurate (0.0 is exact) (correct answer — the two APIs define accuracy inversely. `F.approx_percentile(col, p, accuracy)` uses a larger accuracy value for higher precision (default 10000 ≈ 0.01% relative error). `df.stat.approxQuantile(col, probs, relativeError)` uses a smaller relativeError for higher precision (0.0 = exact but slow). `approx_percentile` returns a single value or array via `agg()`; `approxQuantile` is an eager action returning a Python list.
- C) `approxQuantile` with `relativeError=0.01` means 1% absolute error; `approx_percentile` with `accuracy=100` means 100 decimal places
- D) They are identical in behavior; the parameter names differ only for historical reasons

---

### Question 51 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: dynamic partition overwrite — partitionOverwriteMode=dynamic vs static

**Question**:
A developer writes a daily incremental batch to a partitioned Parquet table: `df.write.mode("overwrite").partitionBy("date").parquet("/data/events")`. The `df` only contains data for `date=2024-08-15`. What happens to existing partitions for other dates?

- A) Only the `date=2024-08-15` partition is overwritten; other partitions are untouched — this is dynamic partition overwrite behavior
- B) With default settings (`partitionOverwriteMode=static`), ALL existing partitions in `/data/events` are deleted before writing; only `date=2024-08-15` survives (correct answer — Spark's default overwrite mode for partitioned tables is "static": it deletes the entire output directory before writing. To overwrite only the partitions present in the new data, set `spark.sql.sources.partitionOverwriteMode=dynamic` or `df.write.option("partitionOverwriteMode", "dynamic")`. This is a common source of accidental data loss.
- C) Spark raises an `AnalysisException` when writing to a partitioned table with `mode("overwrite")`
- D) Only partitions that have identical schema to the new data are overwritten

---

### Question 52 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.nullif(col1, col2) — returns null when two expressions are equal

**Question**:
A DataFrame has a column `status_code` where `-1` is a sentinel value meaning "unknown". A developer wants to convert all `-1` values to `null` while keeping other values intact. Which expression achieves this?

- A) `F.when(F.col("status_code") == -1, None).otherwise(F.col("status_code"))`
- B) `F.nullif(F.col("status_code"), F.lit(-1))` — returns `null` when the two arguments are equal, otherwise returns the first argument (correct answer — `F.nullif(expr1, expr2)` is equivalent to `CASE WHEN expr1 = expr2 THEN NULL ELSE expr1 END`. It is the concise way to replace sentinel/magic values with null. When `status_code = -1`, the result is null. For any other value, the original `status_code` is returned.
- C) `F.coalesce(F.col("status_code"), F.lit(-1))` — coalesce returns the first non-null value, not the inverse
- D) `F.nullif` requires both columns to be the same type; mixing numeric columns with literals is not supported

---

### Question 53 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.mapInPandas(func, schema) — partition-level Pandas transformation without grouping

**Question**:
A developer wants to apply a complex scikit-learn transformation to each partition of a Spark DataFrame without grouping. The function should receive and return entire Pandas DataFrames for each partition. Which API is appropriate?

- A) `df.applyInPandas(func, schema)` — `applyInPandas` processes each partition as a Pandas DataFrame
- B) `df.mapInPandas(func, schema)` — processes partitions as an iterator of Pandas DataFrames without requiring a groupBy; `func` receives `Iterator[pd.DataFrame]` and yields `Iterator[pd.DataFrame]` (correct answer — `mapInPandas` is a partition-level operation that does not require grouping. The function signature is `func(Iterator[pd.DataFrame]) -> Iterator[pd.DataFrame]`. It requires Arrow to be available. `applyInPandas` (via `groupBy().applyInPandas()`) requires a grouping key. `mapInPandas` gives full partition-level control.
- C) `df.foreach(func)` — processes each row individually using a Pandas function
- D) `df.rdd.mapPartitions(func)` — this is the RDD equivalent; Pandas DataFrames are not supported in RDD operations

---

### Question 54 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.schema.json() vs simpleString() vs treeString() — schema representation formats

**Question**:
A developer wants to programmatically inspect the full schema of a DataFrame including nullability and metadata in a parseable format. Which `StructType` method returns a JSON string with complete schema information?

- A) `df.schema.simpleString()` — returns compact DDL-like string with full metadata
- B) `df.schema.json()` — returns the full JSON representation including `nullable` flags and `metadata` for each field (correct answer — `StructType.json()` serializes the complete schema to JSON including `name`, `type`, `nullable`, and `metadata` for every field. `simpleString()` returns a compact DDL-like string (e.g., `"struct<id:int,name:string>"`). `treeString()` returns the indented human-readable tree identical to `df.printSchema()` output. For programmatic parsing, `json()` or `StructType.fromJson()` are the correct choices.
- C) `df.schema.ddl` — this attribute returns the DDL CREATE TABLE string
- D) `df.schema.treeString()` — the tree format includes JSON-compatible metadata for each column

---

### Question 55 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.xxhash64(*cols) — non-cryptographic 64-bit hash for deduplication and row fingerprints

**Question**:
A developer needs a fast, consistent hash key combining three columns (`user_id`, `event_type`, `ts`) for deduplication. They consider `F.hash()` and `F.xxhash64()`. What is the key difference?

- A) `F.hash()` returns `LongType`; `F.xxhash64()` returns `StringType`
- B) `F.xxhash64(*cols)` uses the xxHash64 algorithm (non-cryptographic, fast) and returns `LongType`; `F.hash(*cols)` uses MurmurHash3 32-bit and also returns `IntegerType`; xxHash64 has better distribution properties and produces a 64-bit hash (correct answer — `F.xxhash64(*cols)` (Spark 3.0+) applies xxHash64 to a combination of one or more columns and returns a `LongType`. `F.hash(*cols)` uses MurmurHash3 and returns `IntegerType` (32-bit). For deduplication keys across many columns, xxhash64 has lower collision probability due to the wider output space.
- C) Both functions are aliases; `xxhash64` was introduced as a rename of `hash` in Spark 3.0
- D) `F.xxhash64` is cryptographically secure; `F.hash` is not

---

### Question 56 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.lit(None).cast("type") — adding null columns with explicit types

**Question**:
A developer adds a null column to a DataFrame: `df.withColumn("optional_info", F.lit(None))`. The DataFrame is then written to Parquet, but the write fails with a type error. What is the root cause and fix?

- A) `F.lit(None)` creates a `NullType` column which most file format writers reject; fix by casting: `F.lit(None).cast("string")` (correct answer — `F.lit(None)` without a cast produces a `NullType` column. Parquet, ORC, and other writers typically do not support `NullType` columns and raise an `AnalysisException` or write error. Always cast: `F.lit(None).cast(T.StringType())` or `F.lit(None).cast("string")` to produce a properly typed nullable column.
- B) `F.lit(None)` creates a `BooleanType` null column which is incompatible with Parquet string columns
- C) Parquet requires all columns to be non-nullable; the fix is to use `F.coalesce(F.lit(None), F.lit(""))` instead
- D) `F.lit(None)` is not valid syntax; use `F.lit("null")` to represent null values

---

### Question 57 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.repartitionByRange(n, col) — range-based partitioning vs hash-based repartition

**Question**:
A developer prepares data for a range-based merge join and wants partitions to contain non-overlapping, sorted value ranges. Which method achieves this, and how does it differ from `df.repartition(n, col)`?

- A) `df.repartition(n, col)` groups similar values together; `df.repartitionByRange` uses hash-based placement
- B) `df.repartitionByRange(n, col)` samples the data to determine range boundaries and assigns rows to partitions such that each partition contains a contiguous range of values; `df.repartition(n, col)` uses hash-based assignment which distributes similar values randomly across partitions (correct answer — `repartitionByRange` performs range partitioning: it samples the column to estimate quantiles, then assigns rows to partitions based on their position in the value range. This ensures partition 0 has the lowest values and partition n-1 has the highest. Hash-based `repartition` distributes by hash, so nearby values can end up in different partitions.
- C) Both methods are equivalent; the difference is only in the internal implementation, not the output distribution
- D) `repartitionByRange` is only valid for numeric columns; string columns require `repartition`

---

### Question 58 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.regexp_extract_all(col, pattern, idx) — Spark 3.1+ all-match extraction returning an array

**Question**:
A developer has a column `log_line` and wants to extract all IP addresses matching `\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b` from each row (multiple IPs per row). Which function returns an array of all matches?

- A) `F.regexp_extract(col, pattern, 1)` — add group index 1 to get all matches
- B) `F.regexp_extract_all(col, pattern, 0)` — returns an `ArrayType(StringType)` with all non-overlapping matches; group 0 means the full match (correct answer — `F.regexp_extract_all(col, pattern, idx)` (Spark 3.1+) returns an array containing all occurrences of the pattern in the string. `idx=0` returns full matches; a positive `idx` returns a specific capture group from each match. Unlike `regexp_extract` which returns only the first match, `regexp_extract_all` captures all occurrences in the string.
- C) `F.regexp_replace(col, pattern, '|')` — split by replacing and then use `F.split`
- D) `F.regexp_extract_all` does not exist; multiple matches require a UDF

---

### Question 59 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.write.partitionBy vs df.write.bucketBy — data organization strategies

**Question**:
A team frequently runs queries filtered on `region` AND range queries on `order_date`. They consider using `partitionBy("region")` vs `bucketBy(16, "order_id")`. Which combination best supports their query patterns?

- A) `bucketBy(16, "region")` — bucketing on the filter column is always better than partitioning
- B) `df.write.partitionBy("region").bucketBy(16, "order_id").sortBy("order_date").saveAsTable("orders")` — partition by region for fast partition pruning; bucket and sort by order_id for efficient joins (correct answer — `partitionBy` enables partition pruning for equality filters on `region`. `bucketBy` with `sortBy` creates sorted files within each bucket, enabling bucket-merge joins and efficient range scans on `order_date`. These can be combined. `bucketBy` requires `saveAsTable` (not `save(path)`); `partitionBy` works with both.
- C) `partitionBy("region", "order_date")` — partitioning on both columns handles all query patterns
- D) `df.write.sortBy("order_date").save("/path")` — global sort is sufficient for range queries

---

### Question 60 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.write.saveAsTable vs df.write.format().save() — metastore registration vs path write

**Question**:
A developer writes `df.write.mode("append").saveAsTable("analytics.events")`. The table already exists in the metastore. What happens to the schema and data location?

- A) The table is dropped and recreated with the new DataFrame's schema
- B) Spark appends data to the existing table's location as determined by the metastore; the schema is validated against the existing table schema (correct answer — `saveAsTable` with `mode("append")` adds data to the existing metastore table. Spark validates that the DataFrame schema is compatible with the table schema. If the table is managed, data is written to the managed table location. If the table is external, data is appended to the specified location. No schema changes are made. To change the schema, use `mode("overwrite")` with `option("overwriteSchema", "true")` for Delta, or recreate the table.
- C) `saveAsTable` ignores the `mode` parameter; it always creates a new table, failing if the table exists
- D) Spark rewrites all existing data to merge the old schema with the new DataFrame's schema

---

### Question 61 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.read.jdbc with predicates parameter — parallel JDBC reads with custom WHERE clauses

**Question**:
A developer reads a large database table via JDBC and wants 4 parallel partitions with custom WHERE clauses: `id < 1000`, `id BETWEEN 1000 AND 4999`, `id BETWEEN 5000 AND 9999`, `id >= 10000`. Which `spark.read.jdbc` parameter enables this?

- A) `numPartitions=4` with `lowerBound=0` and `upperBound=10000` — provides uniform range split
- B) `predicates=["id < 1000", "id BETWEEN 1000 AND 4999", "id BETWEEN 5000 AND 9999", "id >= 10000"]` — each predicate generates one JDBC partition with a custom WHERE clause (correct answer — the `predicates` parameter of `spark.read.jdbc()` accepts a list of SQL WHERE clause fragments. Each element creates one partition, and Spark reads that partition by appending the predicate to the query. Unlike `numPartitions + lowerBound + upperBound` (which uses uniform numeric ranges), `predicates` allows fully custom, potentially non-uniform splits including non-numeric columns.
- C) `partitionColumn="id", lowerBound=0, upperBound=10000, numPartitions=4` — this is the only way to parallelize JDBC reads
- D) JDBC reads are always single-partition; parallelism requires reading to a file first

---

### Question 62 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.groupBy().applyInArrow(func, schema) — Arrow-native grouped map UDF

**Question**:
Spark 3.3+ introduces `applyInArrow` as an alternative to `applyInPandas` for grouped map operations. What is the key difference in the data format received by the user function?

- A) `applyInArrow` receives a `pandas.DataFrame` per group; `applyInPandas` receives a `pyarrow.RecordBatch`
- B) `applyInArrow` receives and returns `pyarrow.RecordBatch` objects; `applyInPandas` receives and returns `pandas.DataFrame` objects; `applyInArrow` avoids Pandas conversion overhead (correct answer — `applyInArrow` (Spark 3.3+) is a grouped map operation where the user function takes and returns PyArrow `RecordBatch` objects instead of Pandas DataFrames. Since Spark uses Arrow internally for columnar data transfer, `applyInArrow` skips the Arrow-to-Pandas conversion, reducing serialization overhead and enabling direct use of PyArrow vectorized operations.
- C) They are functionally identical; the only difference is the import path
- D) `applyInArrow` does not support groupBy; it must be used with `mapInArrow` instead

---

### Question 63 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.raise_error(message) — runtime error in query for data validation

**Question**:
A developer wants a query to raise an error when a `price` column contains negative values, rather than silently returning incorrect results. Which expression achieves this?

- A) `F.assert_true(F.col("price") >= 0, "Negative price detected")` — dedicated assertion function
- B) `F.when(F.col("price") < 0, F.raise_error("Negative price detected")).otherwise(F.col("price"))` — `raise_error` (Spark 3.1+) raises a `SparkRuntimeException` when evaluated (correct answer — `F.raise_error(message)` (Spark 3.1+) is a Column expression that, when evaluated, raises a `SparkRuntimeException` with the specified message. Combined with `F.when(..., F.raise_error(...)).otherwise(expr)` it acts as an inline data quality check. `F.assert_true(condition, errMsg)` is also valid and is an alias providing the same functionality via a different API.
- C) `F.col("price").check(lambda v: v >= 0, "error")` — Column objects support lambda validation
- D) Data validation must be done before creating a DataFrame; in-query errors cannot be raised

---

### Question 64 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.write.format("binaryFile") vs spark.read.format("binaryFile") — reading binary files

**Question**:
A developer reads a folder of images using `spark.read.format("binaryFile").load("/images/")`. What columns does the resulting DataFrame contain?

- A) `filename (string), data (binary)` — two columns: the file name and raw bytes
- B) `path (string), modificationTime (timestamp), length (long), content (binary)` — four columns with file metadata and raw bytes (correct answer — `binaryFile` format produces a DataFrame with four columns: `path` (full file path as string), `modificationTime` (last modification timestamp), `length` (file size in bytes as long), and `content` (file bytes as `BinaryType`). Use `option("pathGlobFilter", "*.png")` to filter by filename pattern.
- C) `id (long), path (string), content (string)` — binary files are base64-encoded to string
- D) Only `content (binary)` is returned; metadata must be retrieved separately via `dbutils`

---

### Question 65 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.write.mode("overwrite").option("overwriteSchema", "true") — Delta schema override

**Question**:
A developer tries to overwrite a Delta table with a DataFrame that has additional columns: `df.write.format("delta").mode("overwrite").save("/delta/events")`. The write fails with a schema mismatch error. What is required to overwrite both data and schema in one operation?

- A) Set `spark.databricks.delta.schema.autoMerge.enabled=true` to automatically merge schemas
- B) Add `.option("overwriteSchema", "true")` to allow overwriting both the data and the table schema in one operation (correct answer — Delta Lake rejects schema changes during overwrite by default to prevent accidental schema corruption. Adding `option("overwriteSchema", "true")` explicitly permits replacing the existing Delta schema with the new DataFrame's schema during an overwrite. Without this option, use `option("mergeSchema", "true")` to add new columns without removing existing ones.
- C) Use `mode("insert")` instead of `mode("overwrite")` to bypass schema validation
- D) Delete the Delta table directory manually before writing to work around the schema check

---

### Question 66 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.stat.cov(col1, col2) vs df.stat.corr(col1, col2) — covariance vs correlation

**Question**:
A developer calls both `df.stat.cov("sales", "profit")` and `df.stat.corr("sales", "profit")`. What do these return, and how do they differ in interpretation?

- A) Both return the same value; `corr` is just a normalized version with range [-1, 1] (correct answer is B)
- B) `df.stat.cov` returns the sample covariance (a Python float, unbounded, depends on scale of the data); `df.stat.corr` returns the Pearson correlation coefficient (a Python float in [-1, 1], scale-independent); both are eager actions (correct answer — `cov(c1, c2)` computes `E[(c1 - mean(c1)) * (c2 - mean(c2))]` using sample (N-1) denominator. `corr(c1, c2)` normalizes the covariance by the product of standard deviations, yielding a dimensionless [-1, 1] value. Both are eager actions that return Python floats.
- C) `df.stat.cov` returns a DataFrame; `df.stat.corr` returns a float
- D) `df.stat.corr` computes Spearman rank correlation; `df.stat.cov` computes Pearson covariance

---

### Question 67 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.createOrReplaceTempView scoping — view lifetime and cross-session visibility

**Question**:
In a Spark application with multiple concurrent `SparkSession` objects (e.g., using `SparkSession.newSession()`), Session A calls `df.createOrReplaceTempView("sales_staging")`. Can Session B query `sales_staging`?

- A) Yes — temp views created by any session are visible to all sessions in the same application
- B) No — temp views are scoped to the `SparkSession` that created them; Session B cannot access `sales_staging` unless it creates its own view or uses `createOrReplaceGlobalTempView` (correct answer — `createOrReplaceTempView` creates a session-scoped view. Each `SparkSession` has its own catalog of temp views. Views created in Session A are not visible in Session B. To share views across sessions, use `createOrReplaceGlobalTempView` and access them via the `global_temp` database.
- C) Yes — the underlying DataFrame is cached in memory and can be referenced by any session
- D) Session B can access the view only if it was created before the session split

---

### Question 68 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.write.format("orc").option("orc.compress", "zlib") — ORC-specific compression options

**Question**:
A developer writes a DataFrame to ORC format and wants to use ZLIB compression. Which write option syntax is correct, and what are the valid compression values for ORC?

- A) `df.write.format("orc").option("compression", "zlib")` — same as Parquet compression option
- B) `df.write.format("orc").option("orc.compress", "zlib")` — the ORC-native option key; valid values include `none`, `snappy`, `zlib`, `lzo` (correct answer — ORC uses the native `orc.compress` option key (not `compression` which is used by Parquet/CSV). Valid values: `none`, `snappy`, `zlib` (default), `lzo`. Setting `compression` instead may be silently ignored or use the default. For Parquet the equivalent would be `df.write.format("parquet").option("compression", "snappy")`.
- C) `df.write.orc(path, compression="zlib")` — use the shorthand `orc()` method with keyword argument
- D) ORC compression is controlled only via `spark.sql.orc.compression.codec` global configuration

---

### Question 69 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.array_min(array) / F.array_max(array) — min/max from array column with null handling

**Question**:
A DataFrame has a column `scores` of type `ArrayType(IntegerType)`. For the value `[3, null, 1, 5]`, what does `F.array_min(F.col("scores"))` return?

- A) `null` — any null in the array causes `array_min` to return null
- B) `1` — `array_min` ignores null elements and returns the minimum of non-null values (correct answer — `F.array_min(array)` returns the minimum element in the array, ignoring null values. For `[3, null, 1, 5]`, it returns `1`. `F.array_max` similarly returns the maximum non-null value (`5`). Both return `null` only for an empty array (`array()`) or an all-null array.
- C) `0` — null elements are treated as `0` for numeric comparison
- D) `3` — `array_min` starts from the first non-null element, not the global minimum

---

### Question 70 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.offset(n) — skipping first n rows in Spark 3.4+ DataFrame API

**Question**:
A developer needs to skip the first 100 rows of a sorted DataFrame and return the next 50. In Spark 3.4+, which DataFrame API combination achieves this efficiently?

- A) `df.limit(150).subtract(df.limit(100))` — subtract the first 100 from first 150
- B) `df.offset(100).limit(50)` — `offset(n)` (Spark 3.4+) skips the first `n` rows; combined with `limit` it implements SQL `LIMIT 50 OFFSET 100` pagination (correct answer — `df.offset(n)` (Spark 3.4+) corresponds to the SQL `OFFSET` clause and skips the first `n` rows of the DataFrame (after any sorting). Chaining `df.offset(100).limit(50)` is equivalent to `SELECT * FROM t LIMIT 50 OFFSET 100`. Note: `offset` requires a total order to be deterministic; always use with `orderBy` for reproducible results.
- C) `df.tail(n)[-50:]` — `tail(n)` returns the last n rows as a Python list
- D) `df.rdd.zipWithIndex().filter(lambda row_idx: row_idx[1] >= 100).take(50)` — RDD approach is required before Spark 3.4

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: AQE nonEmptyPartitionRatioForBroadcastJoin — switching to BHJ after shuffle

**Question**:
After a shuffle stage, AQE observes that 85% of partitions are empty. What behavior does `spark.sql.adaptive.nonEmptyPartitionRatioForBroadcastJoin` trigger in this situation?

- A) AQE coalesces the empty partitions to reduce task overhead but keeps the join strategy unchanged
- B) If the ratio of non-empty partitions falls below this threshold (default 0.2), AQE may convert the shuffle-based join to a broadcast hash join by broadcasting the small side (correct answer — `nonEmptyPartitionRatioForBroadcastJoin` (default 0.2 = 20%) is an AQE heuristic. If the post-shuffle statistics show that the non-empty partition fraction is below the threshold, AQE treats the data as small enough to broadcast. With 15% non-empty partitions (85% empty), the threshold is met and AQE may switch to BHJ.
- C) AQE automatically cancels the query and asks the developer to add `broadcast()` hints
- D) AQE removes empty partitions from the result but does not change the join algorithm

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.storage.memoryMapThreshold — mmap vs regular read for shuffle blocks

**Question**:
An administrator sets `spark.storage.memoryMapThreshold=2mb`. What does this control in Spark's I/O subsystem?

- A) Only shuffle blocks larger than 2 MB are kept in the storage memory pool; smaller blocks use execution memory
- B) Shuffle and cached RDD blocks larger than 2 MB are read from disk using memory-mapped I/O (`mmap`); smaller blocks use regular `read()` syscalls (correct answer — `spark.storage.memoryMapThreshold` determines which I/O method Spark uses for reading blocks from disk. `mmap` is more efficient for large blocks as it avoids a copy to the JVM heap. For small blocks, `mmap` overhead (page table entries, TLB pressure) outweighs benefits, so regular reads are used. The default is 2 MB.
- C) The maximum size of a cached partition that can be deserialized into memory in one call
- D) The threshold above which Spark compresses storage memory blocks before persisting them

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.parquet.int96RebaseModeInWrite — INT96 timestamp rebase correction for cross-system compatibility

**Question**:
A Spark application writes Parquet files with `TimestampType` columns that need to be read by an older tool using Hive's INT96 format. The developer encounters an `AnalysisException` about timestamp rebase. Which configuration resolves this?

- A) Set `spark.sql.parquet.writeLegacyFormat=true` to write using the old Parquet timestamp encoding
- B) Set `spark.sql.parquet.int96RebaseModeInWrite=LEGACY` to rebase timestamps to the Julian calendar used by older tools (correct answer — Spark 3.0+ writes Parquet timestamps using the Gregorian calendar (CORRECTED mode). Tools that use INT96 encoding with Hive-style Julian calendar will produce different values. Setting `int96RebaseModeInWrite=LEGACY` applies the Julian-to-Gregorian rebase correction so that values are compatible with the consuming tool. `EXCEPTION` (default in 3.x) raises an error to force the developer to choose explicitly.
- C) Set `spark.sql.legacy.parquet.nanosAsLong=true` to convert INT96 timestamps to long integers
- D) The `AnalysisException` is caused by timezone mismatch; set `spark.sql.session.timeZone` to `UTC`

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.caseSensitive — column name case sensitivity

**Question**:
A DataFrame schema contains columns `CustomerID` and `customerid` (two distinct columns loaded from a case-sensitive data source). A developer queries `df.select("customerid")`. What happens with the default Spark configuration?

- A) Spark correctly selects the `customerid` column because exact match takes priority
- B) Spark raises an `AnalysisException` due to ambiguity; with default `spark.sql.caseSensitive=false`, both `CustomerID` and `customerid` match the query, causing a conflict (correct answer — by default Spark SQL is case-insensitive (`spark.sql.caseSensitive=false`). When two columns differ only in case, selecting either by case-insensitive match raises an ambiguous reference error. Setting `spark.sql.caseSensitive=true` enables case-sensitive matching, allowing `df.select("customerid")` to correctly pick the lowercase column.
- C) Spark always selects the first matching column in schema order regardless of case
- D) Column names in Spark are always case-sensitive; the developer must use the exact column name

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.orc.impl — native vs hive ORC reader

**Question**:
A team migrates from Spark 2.2 to Spark 3.x and notices that ORC vectorized reads are now enabled by default. Which configuration controls the ORC implementation, and what is the default in Spark 3.x?

- A) `spark.sql.orc.impl=hive` is the default in Spark 3.x; the Hive ORC library is used for compatibility
- B) `spark.sql.orc.impl=native` is the default since Spark 2.3; uses Spark's built-in vectorized ORC reader/writer which supports `spark.sql.orc.enableVectorizedReader=true` (correct answer — Spark's `native` ORC implementation has been the default since 2.3. It enables vectorized ORC reads (`enableVectorizedReader=true`), which processes entire batches of rows in columnar format rather than row by row. The `hive` implementation uses the Hive ORC library and does not support vectorized reads. Setting `impl=hive` reverts to Hive compatibility mode.
- C) `spark.sql.orc.impl` does not exist; ORC implementation is selected via the JAR on the classpath
- D) Vectorized ORC reads are enabled only when `spark.sql.orc.impl=vectorized` is set explicitly

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.execution.rangeExchange.sampleSizePerPartition — range boundary accuracy

**Question**:
A developer uses `df.repartitionByRange(100, "order_date")` and observes very uneven partition sizes. They suspect the range boundary estimation is inaccurate due to skew. Which configuration improves boundary accuracy?

- A) `spark.sql.shuffle.partitions=100` — ensures the correct number of partitions after sampling
- B) `spark.sql.execution.rangeExchange.sampleSizePerPartition` — increasing this value causes Spark to sample more rows per partition when estimating range boundaries, producing more accurate splits (correct answer — `rangeExchange.sampleSizePerPartition` (default: 100) controls how many rows are sampled per input partition to estimate the value distribution for range-based partitioning. With highly skewed data, the default 100 samples per partition may not capture the distribution accurately. Increasing to 1000 or more improves boundary estimation at the cost of a larger sampling phase.
- C) `spark.sql.adaptive.skewJoin.enabled=true` — AQE skew join handling automatically fixes `repartitionByRange` skew
- D) `spark.default.parallelism` — this controls the number of samples collected across all partitions

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.planChangeLog.level — logging AQE plan changes

**Question**:
A developer suspects AQE is making suboptimal join strategy changes. They want to see log entries showing exactly when and why AQE changes a plan without enabling full DEBUG logging. Which configuration helps?

- A) `spark.sql.adaptive.enabled=false` — disabling AQE and re-enabling reveals the plan changes
- B) Set `spark.sql.planChangeLog.level=INFO`; AQE plan change events are then logged at INFO level, showing before/after plan snippets for each AQE optimization decision (correct answer — `spark.sql.planChangeLog.level` (default: `TRACE`) controls the log level at which AQE logs its plan transformations. Raising it to `INFO` makes these messages visible in standard application logs without full debug logging. Log entries include the rule name (e.g., `DynamicJoinSelection`) and the modified plan fragment.
- C) `spark.sql.adaptive.logLevel=DEBUG` — a dedicated AQE log level setting
- D) AQE plan changes are only visible in the Spark UI SQL tab; they cannot be logged to files

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.adaptive.forceApply — forcing AQE on all queries including non-shuffle ones

**Question**:
A developer is testing AQE behavior and wants to verify that AQE's partition coalescing is applied even to queries without shuffles. Which configuration forces this?

- A) `spark.sql.adaptive.coalescePartitions.enabled=true` — AQE coalescing is already applied to all queries
- B) `spark.sql.adaptive.forceApply=true` — forces AQE to process all queries including those that would normally be skipped because they have no shuffle or broadcast (correct answer — AQE normally only applies to queries that include shuffle or broadcast operations where runtime statistics are available. `forceApply=true` (default: false) bypasses this check, applying AQE to all queries. This is primarily for testing and debugging, as applying AQE to non-shuffle queries adds overhead without practical benefit in production.
- C) `spark.sql.adaptive.enabled=true` — sufficient to apply AQE to all queries including non-shuffle ones
- D) `spark.sql.optimizer.reorderJoins=true` — enables AQE for join-free queries

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.shuffle.io.serverThreads — Netty server threads for shuffle service under high load

**Question**:
A cluster with many executors experiences frequent shuffle fetch timeouts during large shuffle stages. The team suspects the shuffle server is overwhelmed. Which configuration can increase the shuffle service's capacity to handle concurrent fetch requests?

- A) `spark.shuffle.maxChunksBeingTransferred` — limits the number of chunks per connection to reduce overload
- B) `spark.shuffle.io.serverThreads` — increases the number of Netty server threads handling shuffle data requests; default is `max(3, numCores/4)`; increasing it reduces fetch timeout rates when many executors fetch simultaneously (correct answer — `spark.shuffle.io.serverThreads` controls the Netty thread pool size for the shuffle data server (either the executor's shuffle server or the external shuffle service). With many concurrent reducers, the default thread count can become a bottleneck. Increasing to 2× or 4× the default allows more parallel connections and reduces `FetchFailedException` events.
- C) `spark.reducer.maxReqsInFlight` — limits concurrent requests from a single reducer; reducing it decreases server load
- D) `spark.network.timeout` — increasing the timeout gives overloaded servers more time to respond

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.ui.prometheus.enabled — exposing Spark metrics in Prometheus format

**Question**:
A DevOps team wants to scrape Spark application metrics using Prometheus. After setting `spark.ui.prometheus.enabled=true`, at which URL does the Spark UI expose Prometheus-format metrics?

- A) `http://driver:4040/metrics/json` — the standard metrics endpoint
- B) `http://driver:4040/metrics/prometheus` — the dedicated Prometheus endpoint on the Spark UI port (correct answer — when `spark.ui.prometheus.enabled=true`, Spark exposes a Prometheus-compatible metrics endpoint at `/metrics/prometheus` on the Spark UI port (default 4040). This requires the Prometheus sink to be configured in `metrics.properties` or via `spark.metrics.conf.*` settings. The endpoint returns metrics in Prometheus text format for scraping.
- C) `http://driver:9090/prometheus` — Prometheus uses port 9090 by default
- D) Metrics are pushed to the Prometheus Pushgateway; no HTTP endpoint is exposed

---

### Question 81 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: writeStream.queryName() — streaming query naming and lookup

**Question**:
A developer starts a streaming query: `q = df.writeStream.format("console").queryName("sensor_monitor").start()`. How can this query be referenced later in the same session, and does the name need to be unique?

- A) `spark.streams.get("sensor_monitor")` returns the query; names must be globally unique across JVM instances
- B) `spark.streams.active` returns all active queries; the query can be found by iterating and checking `.name`; query names must be unique within the active session (correct answer — `queryName("name")` assigns a human-readable name. The query is accessible via `spark.streams.active` (list all) or by filtering on `.name`. If a memory sink is used, the query name becomes the table name. Names must be unique among concurrently active queries in the session. The query's UUID (`query.id`) is always unique regardless of name.
- C) `spark.streams.get("sensor_monitor")` retrieves by name; multiple queries can share the same name
- D) `queryName` is only valid for `format("memory")` sink; it has no effect on other sinks

---

### Question 82 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.streams.active and awaitAnyTermination() — managing active streaming queries

**Question**:
A PySpark application starts three streaming queries and the developer wants to block the main thread until any one of them terminates. Which method achieves this?

- A) `for q in spark.streams.active: q.awaitTermination()` — blocks until all three terminate sequentially
- B) `spark.streams.awaitAnyTermination()` — blocks the calling thread until any active streaming query in the session terminates (correct answer — `spark.streams.awaitAnyTermination()` is a session-level method that blocks until any active streaming query terminates, whether by stopping, encountering an error, or finishing. `query.awaitTermination()` blocks for a specific single query. `spark.streams.active` is a property returning the list of all currently active `StreamingQuery` objects.
- C) `spark.streams.stop()` — stops all queries and waits for them to terminate
- D) `threading.Event().wait()` — Python threading is required since Spark provides no blocking method

---

### Question 83 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: per-query checkpointLocation vs global spark.sql.streaming.checkpointLocation

**Question**:
A Spark application runs two concurrent streaming queries sharing the same global checkpoint configuration via `spark.conf.set("spark.sql.streaming.checkpointLocation", "/checkpoints")`. What problem arises, and how should this be resolved?

- A) Both queries write to the same checkpoint directory, causing state corruption; each query needs its own checkpoint location set via `writeStream.option("checkpointLocation", "/checkpoints/queryA")` (correct answer — when multiple queries use the same checkpoint root without a per-query subdirectory, they overwrite each other's checkpoint files, leading to corrupt state and incorrect results or failures. Per-query `option("checkpointLocation", uniquePath)` takes precedence over the global conf and ensures each query has an isolated checkpoint. The global conf acts only as a fallback.
- B) Both queries share checkpoint state, which reduces storage but is valid because offsets are query-specific
- C) Spark automatically creates subdirectories per query under the global checkpoint location
- D) The global configuration is ignored; checkpoint location must always be set per-query via the write option

---

### Question 84 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: StreamingQuery.exception() — checking for streaming query errors

**Question**:
A developer starts a streaming query in the background and calls `query.awaitTermination(timeout=30)`. After the timeout, how can they determine if the query stopped due to an error vs. still running?

- A) Check `query.status.isTriggerActive` — `True` means an error occurred
- B) Call `query.exception()`; if the query failed, it returns the `StreamingQueryException`; if the query is still running or stopped cleanly, it returns `None` (correct answer — `StreamingQuery.exception()` returns the exception that caused the query to terminate, or `None` if the query is active or stopped without error. The pattern `query.awaitTermination(30)` returns after the timeout or when the query stops; then `if query.exception(): ...` checks for errors. `query.status.message` gives a human-readable status string.
- C) Check `query.lastProgress["durationMs"]` — a missing key indicates failure
- D) `query.isActive` returns `False` on timeout even if the query is still running

---

### Question 85 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: foreachBatch idempotency using epochId for deduplication

**Question**:
A developer uses `writeStream.foreachBatch(func)` to write to an external database. Due to a failure, micro-batch 5 is re-executed by Spark. How should the function use the `epochId` parameter to prevent duplicate writes?

- A) Skip the write if `epochId` has already been processed, using a transactional deduplication check (e.g., conditional insert/upsert keyed on `epochId`) (correct answer — `foreachBatch(func)` passes `(DataFrame, epochId)` to the function. Since a failed micro-batch may be retried with the same `epochId`, the function must be idempotent. A common pattern is to use the `epochId` as a unique batch identifier in an idempotency table, or to use Delta Lake `MERGE` / upsert operations that naturally handle duplicates. Simply appending without checking will produce duplicate rows.
- B) The `epochId` increments monotonically on every execution including retries; comparing to a stored value is insufficient
- C) Spark guarantees exactly-once delivery to `foreachBatch`; `epochId` is only for logging and no deduplication is needed
- D) Retry prevention requires `trigger(once=True)` mode; `foreachBatch` with `continuous` trigger is always at-most-once

---

### Question 86 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Append mode + aggregation without watermark — AnalysisException requirement

**Question**:
A developer writes a streaming aggregation with output mode `append`: `df.groupBy("category").agg(F.sum("amount")).writeStream.outputMode("append").start()`. What happens when this query is started?

- A) The query starts successfully and emits aggregate results after each micro-batch
- B) Spark raises an `AnalysisException` at query planning time; append mode requires a watermark on an event-time column to know when aggregate results are final (correct answer — Append output mode for aggregations requires a watermark because Spark can only emit a row in append mode when it is guaranteed to never be updated. Without a watermark, Spark cannot determine when the state for a group is "closed" and safe to emit. The fix is to add `withWatermark("event_time", "delay")` on the streaming source and ensure the aggregation includes the event-time column. Use `complete` or `update` mode if watermarking is not feasible.
- C) The query runs in complete mode silently; Spark ignores the requested append mode for aggregations
- D) The query runs with a default watermark of 0 seconds, emitting results after each batch

---

### Question 87 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.streaming.metricsEnabled — Dropwizard metrics for streaming throughput monitoring

**Question**:
A team wants to monitor `inputRowsPerSecond` and `processedRowsPerSecond` for their streaming queries via JMX. Which configuration enables this?

- A) `spark.metrics.conf.*.sink.jmxsink.class=org.apache.spark.metrics.sink.JmxSink` — only JMX sink configuration is needed
- B) `spark.sql.streaming.metricsEnabled=true` exposes streaming query metrics through Dropwizard metrics (accessible via JMX, Prometheus, or other configured sinks); without this setting, streaming metrics are not reported to external monitoring systems (correct answer — `spark.sql.streaming.metricsEnabled` (default: false) registers Spark Structured Streaming metrics with the Dropwizard `MetricRegistry`. Once registered, they can be consumed by any configured metrics sink (JMX, Prometheus, Graphite, etc.). The metrics include `inputRowsPerSecond`, `processedRowsPerSecond`, `latency`, and others per query.
- C) Streaming metrics are always available; only the Spark UI must be enabled to access them
- D) `spark.ui.streaming.samplingInterval` — configures the metric sampling frequency for streaming queries

---

### Question 88 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Stream-stream outer join watermark requirements — both streams must have watermark

**Question**:
A developer performs a LEFT OUTER join between two streaming DataFrames (`orders_stream` and `payments_stream`) using `orders_stream.join(payments_stream, on="order_id", how="left_outer")`. Only `orders_stream` has a watermark defined. What happens?

- A) The join succeeds; only the left (outer) stream needs a watermark for outer join state cleanup
- B) Spark raises an `AnalysisException` because stream-stream outer joins require watermarks on **both** input streams to bound the join state (correct answer — for stream-stream outer joins, Spark must bound the state for both sides to know when a row will no longer be matched. Both streams must have `withWatermark()` applied for LEFT, RIGHT, or FULL OUTER joins. Without both watermarks, state grows unboundedly and Spark raises an analysis error. Inner stream-stream joins do not require watermarks but state may grow unboundedly without them.
- C) The join runs with unbounded state; Spark logs a warning but does not fail
- D) Only the right stream (the probe side) needs a watermark; the build side is kept fully in state

---

### Question 89 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: writeStream.partitionBy() — partitioning streaming output files

**Question**:
A developer writes a streaming query to a file sink partitioned by `date` and `hour`: `df.writeStream.format("parquet").partitionBy("date", "hour").option("path", "/output").start()`. What directory structure is produced?

- A) `/output/part-*.parquet` — partitioning is not supported for streaming file sinks
- B) `/output/date=2024-08-15/hour=10/part-*.parquet` — Hive-style partitioned directories are created for each unique combination of `date` and `hour` values (correct answer — `writeStream.partitionBy()` works identically to batch `write.partitionBy()`. Each micro-batch appends files under the appropriate `column=value` subdirectory. Files within a partition are never merged across micro-batches; each trigger writes new files. This enables efficient partition pruning for downstream batch readers.
- C) `/output/date=2024-08-15_hour=10/part-*.parquet` — partitions are concatenated into a single directory name
- D) Partitioning a streaming query requires using `foreachBatch` and calling batch `write.partitionBy()` inside the function

---

### Question 90 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: maxBytesPerTrigger for file source — byte-based micro-batch throttling

**Question**:
A streaming query reads from a directory that receives large files varying from 1 MB to 500 MB. The developer wants to limit processing to approximately 100 MB per micro-batch regardless of file count. Which option achieves this?

- A) `option("maxFilesPerTrigger", "1")` — limit to 1 file per batch to control volume
- B) `option("maxBytesPerTrigger", "100m")` — limits total bytes read per micro-batch; Spark reads files until the cumulative size approaches the threshold (correct answer — `maxBytesPerTrigger` limits the total data volume per micro-batch based on file sizes. Spark selects enough files to fill approximately the specified byte budget. This is more predictable than `maxFilesPerTrigger` when files vary greatly in size. The two options are mutually exclusive; only one can be set per streaming query. Value can be specified in bytes or with suffixes like `100m`, `1g`.
- C) `option("trigger", "100mb")` — trigger configuration accepts byte-based sizes
- D) Both `maxBytesPerTrigger` and `maxFilesPerTrigger` can be set simultaneously and Spark uses the more restrictive one

---

### Question 91 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark Connect overview — client/server architecture and remote SparkSession

**Question**:
A developer wants to use PySpark from a laptop without installing Java or a local Spark runtime, connecting to a remote Spark cluster. Which Spark feature enables this, and how is the connection established?

- A) Spark Thrift Server — connect via JDBC/ODBC from any SQL client
- B) Spark Connect — provides a decoupled client/server architecture using gRPC; install `pyspark[connect]` on the client and connect with `SparkSession.builder.remote("sc://cluster-host:15002").getOrCreate()` (correct answer — Spark Connect (introduced in Spark 3.4) separates the Spark client library from the cluster runtime. The lightweight client communicates with a Spark Connect server over gRPC (default port 15002) using Apache Arrow for data transfer. Clients only need the Python package and no local Java installation. This enables IDE-native development, multi-language support, and isolation between client and server versions.
- C) Spark REST API — all DataFrame operations are submitted as JSON HTTP requests
- D) `spark-submit --remote sc://host:7077` — the remote flag replaces `--master` in cluster submissions

---

### Question 92 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect gRPC protocol — client builds logical plan, server executes

**Question**:
A developer using Spark Connect calls `df.filter(...).groupBy(...).agg(...)`. Where is the logical plan built, and where is it executed?

- A) The logical plan is built and optimized on the client; execution also happens on the client using the local data copy
- B) The logical plan is built incrementally on the client side using the Spark Connect Python library; when an action is called (e.g., `collect()`), the plan is serialized as a protobuf message and sent to the Spark Connect server, which optimizes and executes it (correct answer — Spark Connect uses a strict client/server separation. The client library (Python/Scala/etc.) builds an unresolved logical plan using the Spark Connect DSL. On action, this plan is serialized to protobuf and sent via gRPC to the server. The server's Catalyst optimizer resolves, analyzes, and optimizes the plan, then executes it on the cluster. Results are streamed back as Arrow batches.
- C) Every transformation immediately triggers an RPC call to the server; the client does no plan building
- D) The server builds the logical plan; the client only sends raw Python lambdas that the server deserializes

---

### Question 93 — Spark Connect

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark Connect limitations — SparkContext, RDD APIs, and sc.addFile unavailability

**Question**:
A developer migrates an existing PySpark application to Spark Connect and encounters a `PySparkNotImplementedError` on a line that accesses `spark.sparkContext`. What is the cause and the recommended migration approach?

- A) The SparkContext was explicitly disabled; re-enable it with `spark.context.enabled=true`
- B) In a Spark Connect session, `spark.sparkContext` raises `PySparkNotImplementedError` because the SparkContext lives on the server; RDD-based operations (`sc.textFile`, `sc.parallelize`, `sc.addFile`) must be replaced with DataFrame/SparkSession equivalents (correct answer — Spark Connect isolates the client from server-side APIs. `SparkContext` runs on the Spark Connect server and is not accessible from the client. Code that uses `sc.textFile()` should use `spark.read.text()`; `sc.parallelize()` should use `spark.createDataFrame()`; `sc.addFile()` should use a distributed file system path. RDD operations require refactoring to DataFrame API.
- C) `spark.sparkContext` is available but returns a proxy object; most `SparkContext` methods work normally
- D) The error occurs because `pyspark[connect]` was not installed; reinstalling the package resolves it

---

### Question 94 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Installing Spark Connect Python client — pip install pyspark[connect]

**Question**:
A data scientist wants to use PySpark with Spark Connect on their local machine without a full Spark installation. What is the minimal installation command, and what does the `[connect]` extra provide?

- A) `pip install pyspark` — the standard PySpark package includes Spark Connect support
- B) `pip install pyspark[connect]` — installs the Spark Connect client extras including the `grpcio` and `grpcio-status` dependencies required for gRPC communication (correct answer — `pyspark[connect]` is the correct installation for Spark Connect clients. The `[connect]` optional extra adds `grpcio`, `grpcio-status`, and `googleapis-common-protos` which are needed for the gRPC transport layer. The base `pyspark` package does not include these gRPC dependencies. No Java Runtime Environment is required on the client machine.
- C) `pip install spark-connect-python` — a separate package maintained by the Spark Connect team
- D) `conda install -c conda-forge pyspark-connect` — Spark Connect requires conda; pip is not supported

---

### Question 95 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect custom extension — spark.connect.extensions.relation.classes

**Question**:
A developer wants to add a custom data source relation type to the Spark Connect protocol so that clients can trigger a proprietary data loading operation. Which configuration registers the server-side extension?

- A) `spark.sql.extensions` — registers Catalyst extension points including custom relations
- B) `spark.connect.extensions.relation.classes` — registers custom `RelationPlugin` implementations on the Spark Connect server that can handle new relation types sent by clients in the protobuf plan (correct answer — `spark.connect.extensions.relation.classes` is the server-side extension point for adding custom relation types to the Spark Connect protocol. Implementations of `org.apache.spark.sql.connect.plugin.RelationPlugin` can interpret custom protobuf `Any` messages from clients and translate them into Spark logical plans. This enables protocol extensions without modifying the core Spark Connect codebase.
- C) `spark.connect.client.customRelations` — registers client-side relation serializers
- D) Custom relations require modifying the Spark Connect protobuf schema; configuration-only extensions are not supported

---

### Question 96 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: ps.DataFrame.groupby().transform() — group-preserving transformation

**Question**:
A developer uses the Pandas API on Spark: `psdf.groupby("category")["amount"].transform(lambda s: s - s.mean())`. What does this produce?

- A) One row per group with the mean-subtracted total for that group
- B) A Series of the same length as the input DataFrame, where each value is the element's deviation from its group mean (correct answer — `transform(func)` in the Pandas API on Spark (like in pandas) applies `func` to each group and returns a result with the same index/shape as the input. Unlike `apply()` which reduces to one row per group, `transform()` is group-preserving. Each value becomes `amount - group_mean(amount)`. This enables within-group standardization and normalization while keeping the original DataFrame shape.
- C) A new column added to the DataFrame; the original `amount` column is replaced in place
- D) An error; `transform` is not supported for numeric columns in the Pandas API on Spark

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.DataFrame.spark.apply(func) — escape hatch to native Spark DataFrame

**Question**:
A developer working with a Pandas-on-Spark DataFrame needs to apply a native Spark operation not available in the Pandas API (e.g., `df.repartitionByRange`). Which method provides a bridge to the underlying Spark DataFrame?

- A) `psdf.to_spark()` — converts to a Spark DataFrame but the result cannot be wrapped back
- B) `psdf.spark.apply(lambda sdf: sdf.repartitionByRange(10, "id"))` — applies a function that takes and returns a Spark DataFrame; the result is wrapped back as a Pandas-on-Spark DataFrame (correct answer — `ps.DataFrame.spark.apply(func)` is an escape hatch that exposes the underlying Spark DataFrame to a user-supplied function. The function receives a `pyspark.sql.DataFrame` and must return a `pyspark.sql.DataFrame`. The result is automatically wrapped back as a `pyspark.pandas.DataFrame`. This enables access to any Spark API not exposed in the Pandas API surface.
- C) `psdf.native_spark_operation(repartitionByRange, 10, "id")` — a generic native operation dispatcher
- D) `spark.createDataFrame(psdf).repartitionByRange(10, "id").pandas_api()` — the only supported pattern

---

### Question 98 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ps.DataFrame.plot() — visualization triggers toPandas() with compute.max_rows limit

**Question**:
A developer calls `psdf.groupby("category").agg({"sales": "sum"}).plot(kind="bar")` on a Pandas-on-Spark DataFrame. The result only shows a subset of categories. What is the likely cause?

- A) `plot()` only displays the top 10 categories by default; use `top_n=None` to show all
- B) `ps.get_option("compute.max_rows")` is set to a value (default: 1000) that limits how many rows are collected for the plot; internally `plot()` calls `toPandas()` which respects this limit (correct answer — `ps.DataFrame.plot()` triggers `toPandas()` internally to pass data to Matplotlib. The `compute.max_rows` option (default: 1000) caps the number of rows collected by `toPandas()`. If there are more than `compute.max_rows` groups, only the first `N` rows are plotted and others are silently dropped. Use `ps.set_option("compute.max_rows", None)` to disable the limit (with caution on large datasets).
- C) Spark does not support `plot()` for aggregated DataFrames; only raw DataFrames can be plotted
- D) The bar chart is limited to 10 bars by Matplotlib; increase `plt.figure(figsize=(20,5))` to show all

---

### Question 99 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.DataFrame.spark.schema — accessing underlying Spark StructType schema

**Question**:
A developer is working with a Pandas-on-Spark DataFrame and wants to inspect the underlying Spark schema (as `StructType`) without converting to a Spark DataFrame first. Which attribute provides this?

- A) `psdf.dtypes` — returns a pandas Series mapping column names to Pandas dtype objects
- B) `psdf.spark.schema` — returns the underlying Spark `StructType` with full type information including nullability and metadata (correct answer — `ps.DataFrame.spark.schema` is a property on the `.spark` accessor that returns the `pyspark.sql.types.StructType` of the underlying Spark DataFrame. This is equivalent to `psdf.to_spark().schema`. Unlike `psdf.dtypes` which returns Pandas-compatible type objects, `spark.schema` exposes the full Spark type system including `ArrayType`, `MapType`, `StructType` nesting, and field metadata.
- C) `psdf.schema` — the `schema` attribute is available directly on the Pandas-on-Spark DataFrame
- D) `type(psdf).spark_schema` — the schema is a class attribute, not an instance property

---

### Question 100 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.MultiIndex — support and limitations in Pandas API on Spark

**Question**:
A developer creates a Pandas-on-Spark DataFrame with a `MultiIndex` using `ps.MultiIndex.from_tuples([('a', 1), ('a', 2), ('b', 1)])`. Which statement correctly describes MultiIndex support and limitations?

- A) `MultiIndex` is not supported in the Pandas API on Spark; an error is raised immediately
- B) `ps.MultiIndex` is supported for creation and basic operations; however, operations requiring total ordering across the cluster (e.g., `sortlevel`, `get_level_values` with sorting) may not be supported or may be significantly slower than in pandas due to distributed execution constraints (correct answer — Pandas API on Spark supports `MultiIndex` creation via `from_tuples`, `from_arrays`, and `from_frame`. Basic operations like indexing and filtering work. However, some operations that require a globally ordered index or full index materialization (like `sortlevel()` or `reindex()` with complex fill methods) are not implemented or have significant performance trade-offs. The documentation indicates partial support with known limitations.
- C) `MultiIndex` operations are automatically converted to single-column composite keys internally; no limitations apply
- D) `ps.MultiIndex` is supported only when the underlying data fits in driver memory

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | C | `SparkSession` wraps `SparkContext`; `spark.sparkContext` returns the underlying `SparkContext`. `SparkSession` is the unified entry point since Spark 2.0. | topic1-prompt1-spark-architecture.md | Easy |
| 2 | B | `spark.default.parallelism` only affects RDD operations like `reduceByKey` and `join`. DataFrame shuffle partitions are controlled by `spark.sql.shuffle.partitions`. | topic1-prompt1-spark-architecture.md | Medium |
| 3 | C | YARN container memory = JVM heap (4 GB) + memoryOverhead (512 MB) + offHeap.size (1 GB) = 5.5 GB total. | topic1-prompt1-spark-architecture.md | Hard |
| 4 | C | `groupByKey` creates the first shuffle boundary (stage 1&#124;2) and `join` creates the second (stage 2&#124;3); narrow transforms (`map`, `filter`) are pipelined within stages. | topic1-prompt1-spark-architecture.md | Hard |
| 5 | B | Concurrent tasks = `executor.cores / task.cpus` = 8 / 2 = 4. `spark.task.cpus > 1` is used for multi-threaded libraries like TensorFlow or BLAS. | topic1-prompt1-spark-architecture.md | Hard |
| 6 | B | Spark launches a duplicate speculative task on another executor; both run simultaneously and the first to finish wins. Risk: non-idempotent side effects may execute twice. | topic1-prompt1-spark-architecture.md | Medium |
| 7 | B | `heartbeatInterval` must be less than `network.timeout`. If heartbeat equals or exceeds the network timeout, the driver declares the executor dead before receiving its heartbeat. | topic1-prompt1-spark-architecture.md | Hard |
| 8 | B | Without the external shuffle service, shuffle files reside in executor JVM processes and are lost when dynamic allocation removes idle executors. `spark.shuffle.service.enabled=true` keeps files on a separate long-lived daemon. | topic1-prompt1-spark-architecture.md | Medium |
| 9 | B | Kryo is typically 5–10× faster and produces 2–5× smaller output than Java serialization, but requires explicit class registration via `spark.kryo.classesToRegister` for optimal performance. | topic1-prompt1-spark-architecture.md | Hard |
| 10 | B | `spark.executor.cores` sets task slots per executor. With 16 cores and 5 per executor, `floor(16/5) = 3` executors fit per node. | topic1-prompt1-spark-architecture.md | Medium |
| 11 | B | `spark.eventLog.rolling.enabled=true` splits event logs into smaller files based on `spark.eventLog.rolling.maxFileSize`, preventing single oversized logs from overwhelming the History Server. | topic1-prompt1-spark-architecture.md | Hard |
| 12 | B | In cluster mode the driver JVM runs on a worker node, not the submitting machine. Local filesystem paths on the submit machine are inaccessible; files must be on shared storage (HDFS, S3). | topic1-prompt1-spark-architecture.md | Easy |
| 13 | B | Each file smaller than `openCostInBytes` is treated as if it were 4 MB for partition planning, grouping multiple small files into one partition and preventing thousands of single-file partitions. | topic1-prompt1-spark-architecture.md | Hard |
| 14 | B | `spark.reducer.maxSizeInFlight` bounds the total bytes a single reduce task fetches from all map outputs concurrently (default 48 MB), limiting network saturation per reducer. | topic1-prompt4-shuffling-performance.md | Medium |
| 15 | A | `spark.shuffle.compress` compresses the final shuffle output block files; `spark.shuffle.spill.compress` compresses intermediate spill files written when the in-memory sort buffer is exhausted. Both default to `true`. | topic1-prompt4-shuffling-performance.md | Hard |
| 16 | B | `spark.broadcast.compress` is `true` by default. Broadcast blocks are compressed at the driver using the codec from `spark.io.compression.codec` before transmission to executors. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 17 | B | `spark.cleaner.periodicGC.interval` triggers JVM GC on the driver so weak references to unpersisted RDDs and destroyed broadcast variables are collected and cleaned up (default 30 min). | topic1-prompt1-spark-architecture.md | Medium |
| 18 | B | With off-heap enabled, both execution and storage memory can use the off-heap pool via Tungsten's direct memory manager, reducing GC pressure since off-heap memory is not tracked by the JVM GC. | topic1-prompt1-spark-architecture.md | Hard |
| 19 | B | After `executorIdleTimeout` seconds with no running tasks, Spark requests the cluster manager to decommission the executor and return its resources to the pool. | topic1-prompt1-spark-architecture.md | Medium |
| 20 | B | `spark.sql.statistics.histogram.enabled` must be `true` (default: false) for `ANALYZE TABLE` to collect height-balanced histograms; otherwise only basic stats (min, max, count, nullCount) are gathered. | topic1-prompt1-spark-architecture.md | Medium |
| 21 | B | `LPAD(code, 8, '0')` pads from the left with `'0'` to a total length of 8. If the string is already longer than 8 it is truncated from the right. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 22 | B | `locate(substr, str, pos)` is 1-based and starts searching from `pos`. `'at'` first appears at position 2 and again at position 8; starting from pos 6 the next match is at position 8. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 23 | B | `repeat(str, 0)` returns an empty string `''`. For negative `n` the result is also `''`; for null `str` the result is null. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 24 | B | `window(timeCol, windowDuration)` creates non-overlapping tumbling windows. The `window` result column is a struct `{start: TimestampType, end: TimestampType}`. Works in both batch and streaming. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 25 | B | `unix_timestamp(str, fmt)` returns `LongType` (epoch seconds). `to_timestamp(str, fmt)` returns `TimestampType`. Both default to the format `yyyy-MM-dd HH:mm:ss` when no format is supplied. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 26 | B | `TIMESTAMPADD(unit, intExpr, timestampExpr)` — unit string first, integer delta second, timestamp third. Introduced in Spark 3.3. Returns the same type as the input timestamp. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 27 | B | `typeof(expr)` returns the full DDL type string. `typeof(ARRAY(1,2,3))` returns `'array<int>'` including the element type. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 28 | B | `stack(n, ...)` splits the remaining expressions into `n` rows of `(total_exprs / n)` columns each. `stack(2, 'q1', 100, 'q2', 200)` produces 2 rows and 2 columns: `('q1',100)` and `('q2',200)`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 29 | B | `map_contains_key(map, key)` (Spark 3.3+) returns `true` when the key exists even if its value is null. `map['key'] IS NOT NULL` returns `false` when the value is null, conflating missing keys with null-valued keys. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 30 | B | `array_position(array, value)` uses 1-based indexing and returns `0` (not null) when the element is not found. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 31 | A | `reduce(array(1,2,3,4), 0, (acc,x)->acc+x)` = 10. The optional 4th `finish` argument is applied to the final accumulator before returning the result (e.g., `acc->acc+100` would return 110). | topic2-prompt9-builtin-sql-functions.md | Hard |
| 32 | B | `date_part('QUARTER', source)` extracts the quarter (1–4). August is Q3. Equivalent to `EXTRACT(QUARTER FROM source)`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 33 | A | `make_interval(years, months)` with trailing zero defaults creates an interval that can be added directly to a `TimestampType` or `DateType` column. Introduced in Spark 3.0. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 34 | B | `ILIKE` (Spark 3.3+) performs case-insensitive pattern matching without a separate `lower()` call. `ILIKE ANY (...)` supports multi-pattern matching. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 35 | B | `DECODE` treats `NULL=NULL` as equal for search comparisons, but `status=NULL` does not equal `'A'` or `'I'`, so no branch matches and the default `'Unknown'` is returned. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 36 | B | `to_csv(structCol, optionsMap)` serializes struct fields as delimiter-separated values in schema order. `map('sep', '&#124;')` overrides the default comma, producing `'1&#124;Alice&#124;99.5'`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 37 | B | `date_part('MONTH', event_ts)` extracts an integer month number. `date_trunc('MONTH', event_ts)` returns the timestamp at the first moment of that month (e.g., `2024-08-01 00:00:00`). | topic2-prompt9-builtin-sql-functions.md | Medium |
| 38 | B | `LIKE ANY (pattern1, pattern2, ...)` (Spark 3.3+) returns true if the column matches any listed pattern. More concise than an OR expansion. `ILIKE ANY` adds case-insensitivity. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 39 | B | `second(ts)` returns `DoubleType` including fractional seconds for `TimestampType` input. `second(TIMESTAMP '2024-08-15 14:30:45.123456')` returns `45.123456`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 40 | B | `to_number(str, format)` raises a `SparkNumberFormatException` on mismatch. `try_to_number(str, format)` returns `null` instead of failing, enabling graceful handling of dirty data. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 41 | A | `withMetadata(colName, metadata)` attaches metadata to the named column's `StructField` without modifying data. Retrieved via `df.schema["colName"].metadata`. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 42 | B | `~F.col("is_deleted")` calls `Column.__invert__()`, equivalent to SQL `NOT is_deleted`. Rows where `is_deleted=false` are kept; rows where `is_deleted=null` evaluate to null and are excluded by the filter. | topic3-prompt14-filtering-row-manipulation.md | Medium |
| 43 | B | `F.array_sort(arr, comparator)` accepts a lambda `(l, r) -> Int`. Return `1` when `l < r` to sort descending. Default `F.array_sort(col)` sorts ascending with nulls last. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 44 | B | `F.window_time(windowCol)` (Spark 3.4+) is the designated function for extracting the window end timestamp for use with `withWatermark()`. Direct field access via `F.col("window.end")` works for display but not for watermark integration. | topic5-prompt27-structured-streaming.md | Hard |
| 45 | A | With `orderBy()` and the default `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` frame, `collect_list` accumulates all rows up to and including the current row's sort key. The third row collects `['login', 'click', 'purchase']`. | topic2-prompt10-window-functions.md | Medium |
| 46 | B | `withWatermark()` on a batch DataFrame is a no-op — the annotation is accepted without error but is ignored during batch planning and execution. | topic5-prompt27-structured-streaming.md | Medium |
| 47 | B | `df.observe(name, *agg_exprs)` metrics are collected per micro-batch and reported in `StreamingQueryListener.onQueryProgress` via `progress.observedMetrics["name"]`. Also accessible via `query.recentProgress`. | topic5-prompt27-structured-streaming.md | Hard |
| 48 | B | `F.rint(col)` returns `DoubleType` using IEEE 754 round-half-even (banker's rounding): `2.5` → `2.0` (even), `3.5` → `4.0` (even). Use `F.round(col, 0).cast("int")` for integer output. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 49 | B | Global temp views are in the `global_temp` database: `SELECT * FROM global_temp.view_name`. They live for the lifetime of the JVM and are shared across all `SparkSession` objects in the same application. | topic2-prompt8-spark-sql-fundamentals.md | Easy |
| 50 | B | `approx_percentile`'s third arg is `accuracy` where higher = more precise (default 10000). `approxQuantile`'s third arg is `relativeError` where lower = more precise (0.0 = exact). The scales are inverted. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 51 | B | Default `spark.sql.sources.partitionOverwriteMode=static` deletes the entire output directory before writing, destroying all partitions. Set to `dynamic` to overwrite only partitions present in the new data. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 52 | B | `F.nullif(expr1, expr2)` returns `null` when both expressions are equal, otherwise returns `expr1`. Concise way to replace sentinel values (e.g., `-1`) with null. | topic3-prompt16-handling-nulls.md | Medium |
| 53 | B | `df.mapInPandas(func, schema)` processes each partition as an `Iterator[pd.DataFrame]` without requiring a `groupBy`. `applyInPandas` via `groupBy().applyInPandas()` requires a grouping key. | topic3-prompt22-udfs.md | Hard |
| 54 | B | `StructType.json()` returns the full JSON representation including `nullable` flags and `metadata` for every field. `simpleString()` gives a compact DDL string; `treeString()` gives the indented human-readable tree. | topic3-prompt21-schemas-data-types.md | Medium |
| 55 | B | `F.xxhash64(*cols)` uses the xxHash64 algorithm and returns `LongType` (64-bit). `F.hash(*cols)` uses MurmurHash3 and returns `IntegerType` (32-bit). xxHash64 has lower collision probability over a larger output space. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 56 | A | `F.lit(None)` produces a `NullType` column that Parquet and ORC writers reject. Fix by casting: `F.lit(None).cast("string")` or `F.lit(None).cast(T.StringType())`. | topic3-prompt16-handling-nulls.md | Medium |
| 57 | B | `repartitionByRange(n, col)` samples the column to estimate quantiles and assigns rows to non-overlapping contiguous value ranges. `repartition(n, col)` uses hash-based assignment, scattering similar values across partitions. | topic3-prompt20-repartition-coalesce.md | Hard |
| 58 | B | `F.regexp_extract_all(col, pattern, 0)` (Spark 3.1+) returns `ArrayType(StringType)` with all non-overlapping matches. Group 0 is the full match; a positive index extracts a specific capture group from each match. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 59 | B | `partitionBy("region")` enables partition pruning for equality filters. `bucketBy(16, "order_id").sortBy("order_date")` enables bucket-merge joins and efficient range scans. `bucketBy` requires `saveAsTable`. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 60 | B | `saveAsTable` with `mode("append")` adds data to the existing metastore table's location and validates schema compatibility. No schema changes occur; use `option("overwriteSchema", "true")` with `mode("overwrite")` to alter the schema. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 61 | B | The `predicates` parameter accepts a list of SQL WHERE clause fragments; each creates one JDBC partition with a custom filter. Unlike the numeric `lowerBound`/`upperBound` approach, custom predicates can be non-uniform and use non-numeric columns. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 62 | B | `applyInArrow` (Spark 3.3+) passes and returns `pyarrow.RecordBatch` objects per group, skipping the Arrow-to-Pandas conversion overhead. `applyInPandas` passes and returns `pandas.DataFrame` objects. | topic3-prompt22-udfs.md | Hard |
| 63 | B | `F.raise_error(message)` (Spark 3.1+) raises a `SparkRuntimeException` when evaluated. Used inside `F.when(..., F.raise_error(...)).otherwise(expr)` as an inline data quality check. `F.assert_true` is also a valid alternative. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 64 | B | `binaryFile` format produces four columns: `path (string)`, `modificationTime (timestamp)`, `length (long)`, and `content (binary)`. Use `option("pathGlobFilter", "*.png")` to filter by filename. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 65 | B | Delta Lake rejects schema changes on overwrite by default. Adding `.option("overwriteSchema", "true")` permits replacing the existing Delta schema with the new DataFrame schema in one operation. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 66 | B | `df.stat.cov` returns sample covariance (Python float, unbounded, scale-dependent). `df.stat.corr` returns Pearson correlation (Python float in [-1, 1], scale-independent). Both are eager actions. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 67 | B | `createOrReplaceTempView` is scoped to the creating `SparkSession`. Session B cannot see views created in Session A. Use `createOrReplaceGlobalTempView` and the `global_temp` database to share across sessions. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 68 | B | ORC compression uses the native option key `orc.compress` (not `compression` used by Parquet/CSV). Valid values: `none`, `snappy`, `zlib` (default), `lzo`. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 69 | B | `F.array_min` ignores null elements and returns the minimum of non-null values. For `[3, null, 1, 5]` it returns `1`. Returns `null` only for an empty or all-null array. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 70 | B | `df.offset(n)` (Spark 3.4+) skips the first `n` rows, equivalent to SQL `OFFSET`. `df.offset(100).limit(50)` implements `LIMIT 50 OFFSET 100`. Always combine with `orderBy` for deterministic results. | topic3-prompt14-filtering-row-manipulation.md | Hard |
| 71 | B | `nonEmptyPartitionRatioForBroadcastJoin` (default 0.2): if the fraction of non-empty post-shuffle partitions falls below this threshold, AQE may convert the join to a broadcast hash join. With 15% non-empty partitions the threshold is met. | topic4-prompt24-performance-tuning.md | Hard |
| 72 | B | `spark.storage.memoryMapThreshold`: blocks larger than the threshold are read from disk via `mmap`; smaller blocks use regular `read()` syscalls. `mmap` reduces JVM heap copies for large blocks but adds overhead for small ones. | topic4-prompt24-performance-tuning.md | Medium |
| 73 | B | `spark.sql.parquet.int96RebaseModeInWrite=LEGACY` rebases timestamps to the Julian calendar used by older Hive tools, ensuring cross-system compatibility. Default `EXCEPTION` forces the developer to choose explicitly. | topic4-prompt26-debugging.md | Hard |
| 74 | B | With default `spark.sql.caseSensitive=false`, `CustomerID` and `customerid` both match a case-insensitive query, causing an ambiguous reference `AnalysisException`. Set `spark.sql.caseSensitive=true` to enable case-sensitive column resolution. | topic4-prompt26-debugging.md | Medium |
| 75 | B | `spark.sql.orc.impl=native` (default since Spark 2.3) uses Spark's built-in vectorized ORC reader/writer. Setting `impl=hive` reverts to the Hive ORC library, which does not support vectorized reads. | topic4-prompt24-performance-tuning.md | Hard |
| 76 | B | `spark.sql.execution.rangeExchange.sampleSizePerPartition` (default 100) controls how many rows are sampled per partition for range boundary estimation. Increasing it improves accuracy for skewed distributions at the cost of a larger sampling phase. | topic4-prompt24-performance-tuning.md | Medium |
| 77 | B | `spark.sql.planChangeLog.level=INFO` makes AQE plan transformation log entries (including before/after plan snippets and the rule name) visible in standard application logs without full DEBUG logging. | topic4-prompt24-performance-tuning.md | Hard |
| 78 | B | `spark.sql.adaptive.forceApply=true` forces AQE to process all queries including those without shuffle or broadcast operations. Primarily used for testing and debugging; adds overhead without practical benefit in production. | topic4-prompt24-performance-tuning.md | Medium |
| 79 | B | `spark.shuffle.io.serverThreads` controls the Netty thread pool size for the shuffle data server. Increasing it above the default (`max(3, numCores/4)`) reduces `FetchFailedException` events when many executors fetch concurrently. | topic4-prompt24-performance-tuning.md | Hard |
| 80 | B | When `spark.ui.prometheus.enabled=true`, Spark exposes a Prometheus-compatible metrics endpoint at `/metrics/prometheus` on the Spark UI port (default 4040). | topic4-prompt24-performance-tuning.md | Medium |
| 81 | B | Streaming queries are listed in `spark.streams.active`. A named query can be found by iterating and checking `.name`. Query names must be unique among concurrently active queries in the session. | topic5-prompt27-structured-streaming.md | Medium |
| 82 | B | `spark.streams.awaitAnyTermination()` blocks the calling thread until any active streaming query in the session terminates (by stop, error, or completion). `query.awaitTermination()` blocks on a single specific query. | topic5-prompt27-structured-streaming.md | Easy |
| 83 | A | When multiple queries share the same checkpoint root, they overwrite each other's checkpoint files, corrupting state. Each query must have its own unique path via `writeStream.option("checkpointLocation", uniquePath)`. | topic5-prompt27-structured-streaming.md | Hard |
| 84 | B | `StreamingQuery.exception()` returns the `StreamingQueryException` that caused the query to fail, or `None` if the query is still active or stopped cleanly. Check it after `awaitTermination(timeout)` returns. | topic5-prompt27-structured-streaming.md | Medium |
| 85 | A | `foreachBatch(func)` passes `(DataFrame, epochId)`. Since a failed micro-batch may be retried with the same `epochId`, the function must be idempotent — use the `epochId` as a deduplication key (e.g., conditional upsert) to prevent duplicate writes. | topic5-prompt27-structured-streaming.md | Medium |
| 86 | B | Spark raises `AnalysisException` at planning time: append output mode for aggregations requires a watermark so Spark knows when a group's state is final. Use `withWatermark()` on an event-time column, or switch to `complete`/`update` mode. | topic5-prompt27-structured-streaming.md | Hard |
| 87 | B | `spark.sql.streaming.metricsEnabled=true` (default: false) registers streaming metrics with the Dropwizard `MetricRegistry`, making `inputRowsPerSecond`, `processedRowsPerSecond`, latency, and others available to configured sinks (JMX, Prometheus, etc.). | topic5-prompt27-structured-streaming.md | Medium |
| 88 | B | Stream-stream outer joins require watermarks on **both** input streams so Spark can bound state on both sides. Without both watermarks, Spark raises `AnalysisException` at planning. Inner stream-stream joins do not require watermarks but state may grow unboundedly. | topic5-prompt28-stateful-streaming.md | Hard |
| 89 | B | `writeStream.partitionBy("date", "hour")` creates Hive-style `date=...&#124;hour=...` subdirectories. Each micro-batch appends new files under the appropriate partition directory. Downstream batch readers can use partition pruning. | topic5-prompt27-structured-streaming.md | Easy |
| 90 | B | `option("maxBytesPerTrigger", "100m")` limits total bytes read per micro-batch. More predictable than `maxFilesPerTrigger` when files vary greatly in size. The two options are mutually exclusive. | topic5-prompt27-structured-streaming.md | Medium |
| 91 | B | Spark Connect (Spark 3.4+) uses a gRPC client/server architecture (default port 15002) with Apache Arrow for data transfer. Clients only need `pip install pyspark[connect]` and no local Java installation. Connect via `SparkSession.builder.remote("sc://host:15002").getOrCreate()`. | topic6-prompt29-spark-connect.md | Easy |
| 92 | B | The client builds an unresolved logical plan using the Spark Connect DSL. On action (e.g., `collect()`), the plan is serialized to protobuf and sent via gRPC to the server, which runs Catalyst optimization and cluster execution. Results stream back as Arrow batches. | topic6-prompt29-spark-connect.md | Medium |
| 93 | B | In a Spark Connect session, `spark.sparkContext` raises `PySparkNotImplementedError` because `SparkContext` runs on the server. Replace `sc.textFile()` with `spark.read.text()`, `sc.parallelize()` with `spark.createDataFrame()`, etc. | topic6-prompt29-spark-connect.md | Hard |
| 94 | B | `pip install pyspark[connect]` adds the `grpcio`, `grpcio-status`, and `googleapis-common-protos` extras needed for gRPC transport. No Java Runtime Environment is required on the client machine. | topic6-prompt29-spark-connect.md | Medium |
| 95 | B | `spark.connect.extensions.relation.classes` registers server-side `RelationPlugin` implementations that interpret custom protobuf `Any` messages from clients and translate them into Spark logical plans. | topic6-prompt29-spark-connect.md | Medium |
| 96 | B | `groupby().transform(func)` is group-preserving: it returns a result with the same length as the input. Each value becomes `amount - group_mean(amount)`. Unlike `apply()`, no reduction occurs. | topic7-prompt30-pandas-api.md | Easy |
| 97 | B | `psdf.spark.apply(func)` passes the underlying Spark DataFrame to `func` (which must return a Spark DataFrame) and wraps the result back as a Pandas-on-Spark DataFrame, enabling access to any native Spark API. | topic7-prompt30-pandas-api.md | Medium |
| 98 | B | `plot()` internally calls `toPandas()`, which is capped by `ps.get_option("compute.max_rows")` (default 1000). Groups beyond the limit are silently dropped. Use `ps.set_option("compute.max_rows", None)` to disable the cap. | topic7-prompt30-pandas-api.md | Hard |
| 99 | B | `psdf.spark.schema` returns the underlying Spark `StructType` including full type information, nullability, and field metadata — equivalent to `psdf.to_spark().schema`. `psdf.dtypes` returns Pandas-compatible type objects. | topic7-prompt30-pandas-api.md | Medium |
| 100 | B | `ps.MultiIndex` supports creation via `from_tuples`, `from_arrays`, and `from_frame`, and basic indexing operations. Operations requiring a globally ordered index (e.g., `sortlevel`, complex `reindex`) are not fully implemented or have significant performance trade-offs. | topic7-prompt30-pandas-api.md | Medium |
