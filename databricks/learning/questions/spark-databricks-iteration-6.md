# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 6)

**Iteration**: 6

**Generated**: 2026-04-25

**Total Questions**: 100

**Difficulty Split**: 20 Easy / 60 Medium / 20 Hard

**Answer Types**: 73 `one` / 20 `many` / 5 `all` / 2 `none`

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

### Question 1 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: RDD vs DataFrame default cache storage level

**Question**:
A developer calls `rdd.cache()` on an RDD and `df.cache()` on a DataFrame. Which statement correctly describes the default `StorageLevel` applied by each call?

- A) Both use `MEMORY_AND_DISK` — `cache()` always defaults to `MEMORY_AND_DISK` for all Spark abstractions
- B) `RDD.cache()` uses `MEMORY_ONLY`; `DataFrame.cache()` (and `Dataset.cache()`) uses `MEMORY_AND_DISK` — the default storage level differs between the two abstractions
- C) Both use `MEMORY_ONLY` — `cache()` always defaults to `MEMORY_ONLY` for all Spark abstractions
- D) `RDD.cache()` uses `MEMORY_AND_DISK`; `DataFrame.cache()` uses `MEMORY_ONLY`

---

### Question 2 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Job scheduling modes (FIFO vs FAIR)

**Question**:
What is the practical difference between setting `spark.scheduler.mode=FIFO` (the default) and `spark.scheduler.mode=FAIR`?

- A) FIFO processes all stages in a fixed order regardless of which Job submitted them; FAIR distributes resources across stages within a single Job
- B) FIFO processes Jobs in submission order — the first queued Job receives all available executor slots before later Jobs can start; FAIR distributes executor slots across all concurrently running Jobs, allowing short Jobs to make progress while a long Job is executing
- C) FIFO is only available in local mode; FAIR is required for production cluster deployments
- D) FAIR mode requires explicit pool definitions in a `fairscheduler.xml` file — without this file, Spark silently reverts to FIFO regardless of the configuration setting

---

### Question 3 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DAGScheduler vs TaskScheduler responsibilities

**Question**:
Which statement accurately distinguishes the responsibilities of the `DAGScheduler` from those of the `TaskScheduler` in a Spark driver?

- A) The `DAGScheduler` manages communication with the cluster manager to acquire executor containers; the `TaskScheduler` translates the logical query plan into a physical execution plan
- B) The `DAGScheduler` splits the RDD lineage graph into stages at shuffle boundaries, tracks stage dependencies, and submits `TaskSet`s to the `TaskScheduler`; the `TaskScheduler` interfaces with the cluster manager backend, assigns individual tasks to executors based on data locality, retries failed tasks up to `spark.task.maxFailures`, and reports completion back to the `DAGScheduler`
- C) The `DAGScheduler` handles fault recovery by restarting lost executors; the `TaskScheduler` handles stage resubmission when a task fails
- D) The `DAGScheduler` runs on each executor to coordinate tasks within a stage; the `TaskScheduler` runs on the driver and determines the order of stages

---

### Question 4 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: all
**Topic**: Barrier execution mode

**Question**:
Which of the following statements about Spark's barrier execution mode are **all** correct?

- A) In a barrier stage, all tasks must start simultaneously — if the cluster cannot provide enough slots for every task at once, the stage waits until sufficient resources are available rather than launching tasks incrementally
- B) Barrier mode is designed for MPI-style distributed workloads (such as distributed deep learning training) where peer tasks need to exchange data or synchronise checkpoints during execution
- C) A `BarrierContext` is available inside the barrier task function and provides coordination primitives such as `allGather()` for exchanging data across all running tasks in the barrier stage
- D) If any task in a barrier stage fails, Spark re-submits the entire barrier stage from scratch rather than retrying only the individual failed task, because tasks within a barrier stage cannot proceed independently

---

### Question 5 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Python worker memory — spark.executor.pyspark.memory

**Question**:
What does `spark.executor.pyspark.memory` configure, and what risk arises when it is not set?

- A) It sets the portion of the executor JVM heap reserved for Python UDF execution; without it, Python UDFs consume the entire executor heap, leaving no memory for task execution
- B) It controls the amount of memory (outside the executor JVM heap) that the Python worker process may consume per executor; if unset, Python worker memory is unbounded by Spark — only by the OS — which can cause the executor container to be killed by YARN or Kubernetes for exceeding its declared memory limit
- C) It is a legacy setting superseded by `spark.executor.memoryOverhead` in Spark 3.0+; setting either one configures the total Python and JVM overhead budget
- D) It configures JVM heap reserved for PySpark's Java-side serialization buffers; reducing it frees more heap for storage and execution memory

---

### Question 6 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Dynamic Resource Allocation — shuffle tracking

**Question**:
In Spark 3.0+, what is the purpose of `spark.dynamicAllocation.shuffleTracking.enabled`, and how does it affect the need for an external shuffle service?

- A) It launches the external shuffle service as a sidecar; without it enabled, DRA cannot remove any executors that have written shuffle data
- B) When enabled (default `true` in Spark 3.0+), Spark tracks which executors still hold shuffle data required by downstream stages; DRA may only remove executors whose shuffle data is no longer needed — making the external shuffle service optional for DRA on YARN and Kubernetes
- C) It enables compression of shuffle-tracking metadata to reduce driver memory usage; it has no effect on which executors DRA may remove
- D) It configures the re-registration interval between the external shuffle service and each executor; setting it `false` disables external shuffle service health checks

---

### Question 7 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Event log compression

**Question**:
What does setting `spark.eventLog.compress=true` do, and which configuration key selects the compression codec?

- A) It compresses in-memory execution plans on the driver to reduce heap usage; the codec is set via `spark.driver.compress.codec`
- B) It enables compression of the event log files written to `spark.eventLog.dir`; the codec is controlled by `spark.eventLog.compression.codec` (default `zstd`), producing smaller log files that the History Server decompresses transparently when rendering the Spark UI
- C) It compresses shuffle data at rest in event log storage; the codec must match `spark.io.compression.codec`
- D) It enables encrypted storage of event logs; the encryption key is provided in `spark.eventLog.encryption.key`

---

### Question 8 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: many
**Topic**: coalesce() semantics

**Question**:
Which of the following statements about `coalesce()` are correct? *(Select all that apply.)*

- A) `coalesce()` is a narrow transformation — it combines partitions without a full shuffle
- B) If the requested partition count `n` is greater than or equal to the current partition count, `coalesce(n)` is a no-op and the DataFrame retains its original number of partitions
- C) `coalesce()` can increase the number of partitions when `n` is larger than the current count by splitting partitions without a shuffle
- D) For reducing output to a single file, `coalesce(1)` is more efficient than `repartition(1)` because it avoids a full shuffle, whereas `repartition(1)` performs a full shuffle before writing

---

### Question 9 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Arrow batch size for Pandas UDFs

**Question**:
What does `spark.sql.execution.arrow.maxRecordsPerBatch` (default `10000`) control when Arrow-based Pandas UDF execution is enabled?

- A) The maximum number of Arrow record batches that may be queued per executor before back-pressure throttles data ingestion
- B) The maximum number of rows in each Arrow record batch transferred between the JVM executor and the Python worker process when executing Pandas UDFs; smaller values reduce per-batch memory pressure but increase serialization round trips, while larger values improve throughput at the cost of higher peak memory per batch
- C) The total number of records a single Pandas UDF invocation may process across its entire lifetime; exceeding this limit causes the UDF to raise a `RuntimeError`
- D) The maximum number of Python worker processes per executor that can concurrently process Arrow batches

---

### Question 10 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Physical join strategy for non-equi joins

**Question**:
A developer writes `df1.join(df2, df1.value > df2.threshold, "inner")`. Which physical join strategy does Spark choose for this non-equi join, and why?

- A) Spark uses `SortMergeJoin`, sorting both sides on the join columns and performing a merge pass to evaluate the inequality predicate
- B) Spark uses `BroadcastNestedLoopJoin` (if one side can be broadcast) or a `CartesianProduct` with a filter — because there is no equi-join key, `SortMergeJoin` and `BroadcastHashJoin` (which require hashing on equal keys) are not applicable; Spark broadcasts the smaller side when possible
- C) Spark automatically rewrites the non-equi join as an equi-join by generating a synthetic hash key for the inequality columns
- D) Spark raises an `AnalysisException` at query planning time because non-equi inner joins are not supported; the user must rewrite it as a cross join with a `filter()`

---

### Question 11 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Cluster deploy mode — driver lifecycle

**Question**:
When `spark-submit --deploy-mode cluster` is used, how does the lifecycle of the `spark-submit` process differ from `--deploy-mode client`?

- A) In cluster mode, `spark-submit` stays connected to the driver and streams driver logs back to the submitting terminal; in client mode, `spark-submit` exits immediately after submission
- B) In cluster mode, `spark-submit` launches the driver on a cluster node and exits almost immediately after acceptance; driver logs reside on the cluster node, not on the submitting machine; in client mode, the `spark-submit` process itself IS the driver and runs until the application completes
- C) In cluster mode, `spark-submit` blocks indefinitely, printing periodic progress until the job finishes; in client mode, it exits immediately after submission
- D) Cluster mode and client mode have identical `spark-submit` process lifecycles — the only difference is where executor processes are spawned

---

### Question 12 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Data locality wait — per-level configuration

**Question**:
`spark.locality.wait` (default `3s`) controls how long Spark waits for a preferred-locality slot before downgrading to a less preferred locality level. Which statement correctly describes the per-level override settings?

- A) The per-level settings `spark.locality.wait.process`, `spark.locality.wait.node`, and `spark.locality.wait.rack` independently override `spark.locality.wait` for each respective locality-level transition; for example, setting `spark.locality.wait.node=10s` makes Spark wait 10 s before downgrading from `NODE_LOCAL` to `RACK_LOCAL`
- B) `spark.locality.wait` is a global hard ceiling — per-level settings can only reduce it below the global value, never increase it
- C) Per-level locality wait settings apply only to HDFS data sources; Spark always uses the global `spark.locality.wait` for object stores such as S3
- D) Locality wait settings only apply when `spark.scheduler.mode=FAIR`; FIFO mode launches tasks at `ANY` locality immediately without waiting

---

### Question 13 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Proactive block replication

**Question**:
What is the purpose of `spark.storage.replication.proactive` (default `false`), and when is enabling it beneficial?

- A) When `true`, Spark automatically increases the replication factor of all cached blocks from 1 to 2, overriding the user-specified storage level
- B) When `true`, Spark detects that a cached block replica has been lost (e.g., due to an executor being evicted by DRA) and immediately replicates the block from a surviving replica to another live executor — replenishing the lost copy before a cache miss forces full recomputation; most beneficial on clusters with executor churn such as those using Dynamic Resource Allocation with replication-factor storage levels (`MEMORY_AND_DISK_2`, etc.)
- C) When `true`, Spark pre-replicates all DataFrame partitions to every executor before the first action is called, eliminating all remote reads
- D) When `true`, Spark uses proactive replication to push shuffle map output blocks to reducer executors before they are explicitly requested, reducing shuffle fetch latency

---

### Question 14 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: sc.parallelize() partition count

**Question**:
Which statement correctly describes how `sc.parallelize(data, numSlices)` determines the number of partitions in the resulting RDD?

- A) The number of partitions always equals `len(data)` — one element per partition regardless of `numSlices`
- B) When `numSlices` is provided, Spark creates exactly that many partitions; when omitted, Spark uses `spark.default.parallelism` as the default partition count
- C) The number of partitions is always equal to the cluster's total number of executor cores, regardless of `numSlices`
- D) `numSlices` is treated as a maximum — Spark creates fewer partitions if the data is too small to fill each partition with at least one element

---

### Question 15 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: many
**Topic**: spark.driver.supervise behaviour and supported modes

**Question**:
Which of the following statements about `spark.driver.supervise=true` are correct? *(Select all that apply.)*

- A) When enabled, Spark automatically restarts the driver process if it exits with a non-zero exit code (i.e., on application failure or crash)
- B) `spark.driver.supervise` is only effective in `--deploy-mode cluster`; it has no effect when the driver runs in client mode
- C) `spark.driver.supervise` is supported in Spark Standalone cluster mode, but it is not supported in YARN cluster mode or Kubernetes cluster mode
- D) When enabled, Spark also monitors driver heap usage and triggers a restart whenever JVM heap utilisation exceeds `spark.driver.memory` for more than 60 seconds

---

### Question 16 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Two-level hash map for aggregation (spark.sql.codegen.aggregate.map.twolevel.enabled)

**Question**:
What does `spark.sql.codegen.aggregate.map.twolevel.enabled` (default `true`) control in Spark's hash-based aggregation?

- A) It enables whole-stage code generation for aggregate operators, fusing them with adjacent projection operations into a single compiled method
- B) It enables a two-level hash map strategy: Spark first uses a compact, cache-friendly fixed-size hash map (Level 1); if it fills, overflow entries spill into a standard full-sized hash map (Level 2) — improving CPU cache hit rates and reducing object allocation pressure compared to a single large hash map
- C) It allows Spark to perform two passes over input data during aggregation — one pass for cardinality estimation and one for final computation — improving accuracy of approximate aggregations
- D) It controls whether Tungsten uses on-heap (Level 1) or off-heap (Level 2) memory when hash aggregation spills to disk

---

### Question 17 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Worker daemon vs Executor in Standalone mode

**Question**:
In Spark Standalone mode, what is the relationship between the **Worker** daemon and an **Executor** process?

- A) The Worker daemon IS the executor — Standalone mode uses a single-process model in which one Worker per node handles both resource management and task computation
- B) The Worker daemon is a persistent JVM process on each cluster node that registers with the Master, manages local resources, and launches separate Executor JVM processes for each application assigned to that node; multiple Executors from different applications can run under a single Worker simultaneously
- C) The Worker daemon runs exclusively on the driver node and manages executor container lifecycles via the Master
- D) Each Worker daemon manages exactly one Executor at a time; running multiple executors per node requires starting multiple Worker daemon instances

---

### Question 18 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.rdd.compress

**Question**:
What does `spark.rdd.compress=true` (default `false`) do when an RDD is cached?

- A) It compresses RDD data in transit between executors during shuffle operations — equivalent to enabling `spark.shuffle.compress`
- B) It compresses the serialized representation of cached RDD partitions in memory, reducing the amount of memory consumed by the cache at the cost of additional CPU time for compression on write and decompression on read; the codec used is controlled by `spark.io.compression.codec`
- C) It enables columnar compression for cached RDD data — similar to Parquet's column encoding — and requires the RDD to have a fixed schema
- D) It compresses RDD lineage metadata stored in driver memory, reducing driver heap usage for jobs with deeply nested transformation chains

---

### Question 19 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Stage re-computation when executor lost after shuffle write

**Question**:
An executor is lost after it has fully written its shuffle map output files but before any reduce tasks have read those files. What does Spark do?

- A) Spark marks the map output as unavailable and reschedules only the affected reduce tasks on surviving executors — no map-stage recomputation is needed
- B) Because the shuffle files were on the lost executor's local disk, they are now inaccessible; Spark must re-run the map tasks that wrote to that executor on surviving executors to regenerate the lost shuffle data before the reduce stage can proceed; if an external shuffle service was deployed and held the files independently, no recomputation is required
- C) Spark fetches the lost shuffle data from the driver's in-memory shuffle tracking metadata, which keeps a full copy of all map output for fault recovery
- D) Spark aborts the entire application and requires the user to resubmit — partial map-stage recomputation for lost shuffle files is not supported

---

### Question 20 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: many
**Topic**: spark.app.name and Application ID

**Question**:
Which of the following statements about Spark application names and Application IDs are correct? *(Select all that apply.)*

- A) `spark.app.name` is a human-readable label visible in the Spark UI, History Server, and cluster manager UI; it is set via `SparkConf` or the `--name` flag in `spark-submit`
- B) The Application ID is system-generated by the cluster manager and is accessible at runtime via `spark.sparkContext.applicationId`
- C) In YARN, Application IDs follow the format `application_<rm-start-timestamp>_<sequence-number>` (e.g., `application_1714000000000_0001`)
- D) Two applications running simultaneously with the same `spark.app.name` share the same Application ID

---

### Question 21 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: split_part() function (Spark 3.3+)

**Question**:
What does `split_part('a:b:c:d', ':', 2)` return in Spark SQL (Spark 3.3+)?

- A) `'a:b'` — `split_part` returns all tokens up to and including the nth delimiter occurrence
- B) `'b'` — `split_part` splits the string by the delimiter and returns the token at the 1-based position; `split_part('a:b:c:d', ':', 2)` returns the second token `'b'`
- C) `'b:c:d'` — `split_part` returns the substring starting from the nth delimiter position to the end
- D) `['a', 'b', 'c', 'd']` — `split_part` returns an array of all tokens; the third argument selects the delimiter only

---

### Question 22 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: try_divide() — safe division (Spark 3.4+)

**Question**:
What is the result of `SELECT try_divide(10, 0)` in Spark SQL (Spark 3.4+)?

- A) Raises an `ArithmeticException` — `try_divide` suppresses only overflow errors, not division by zero
- B) Returns `NULL` — `try_divide(dividend, divisor)` returns `NULL` when the divisor is zero instead of raising an `ArithmeticException`, making it safe for queries where the divisor column may contain zeros without requiring an explicit `CASE WHEN divisor = 0` guard
- C) Returns `Infinity` — SQL arithmetic always produces `Infinity` for division by zero in Spark
- D) Returns `0` — integer division by zero is defined as zero in Spark's SQL dialect

---

### Question 23 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: any_value() aggregate function (Spark 3.3+)

**Question**:
What does `any_value(col IGNORE NULLS)` return in Spark SQL (Spark 3.3+)?

- A) The most frequently occurring non-null value in the group — equivalent to `mode()` but without ordering guarantees
- B) An arbitrary non-null value from the group without ordering guarantees; `IGNORE NULLS` causes the function to skip `NULL` values when selecting a candidate — returning `NULL` only if every value in the group is `NULL`
- C) The first non-null value in the group in row-insertion order — a guaranteed-deterministic equivalent of `first(col, true)`
- D) `any_value` does not support the `IGNORE NULLS` clause; using it raises a `ParseException` in all Spark versions

---

### Question 24 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: make_date() function

**Question**:
What does `make_date(2026, 12, 25)` return in Spark SQL?

- A) A `StringType` value `'2026-12-25'`
- B) A `DateType` value representing December 25, 2026 — `make_date(year, month, day)` constructs a date from integer year, month, and day components and returns a `DateType` column
- C) A `TimestampType` value `2026-12-25 00:00:00`
- D) A `LongType` value representing the Unix epoch day number for December 25, 2026

---

### Question 25 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: regexp_like() vs regexp_extract()

**Question**:
What is the key difference between `regexp_like(str, pattern)` and `regexp_extract(str, pattern, idx)` in Spark SQL?

- A) `regexp_like` and `regexp_extract` are aliases — both extract and return the matched substring from the string
- B) `regexp_like(str, pattern)` returns a `BooleanType` — `true` if `str` matches the regex `pattern`, `false` otherwise; `regexp_extract(str, pattern, idx)` returns a `StringType` — the text captured by group `idx` (returning an empty string if the pattern does not match)
- C) `regexp_like` uses Java regex syntax; `regexp_extract` uses POSIX extended regex syntax
- D) `regexp_like` is only available in SQL statements; `regexp_extract` is only available via the DataFrame API as `F.regexp_extract()`

---

### Question 26 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: width_bucket() function

**Question**:
What does `width_bucket(value, min_value, max_value, num_buckets)` return in Spark SQL?

- A) The fraction of rows whose `value` falls below the bucket boundary, identical to `percent_rank()` but applied to a continuous range
- B) A 1-based integer bucket number for `value` within `num_buckets` equal-width buckets spanning `[min_value, max_value)`; values below `min_value` return `0` and values greater than or equal to `max_value` return `num_buckets + 1`
- C) The width (size) of the bucket that contains `value`, computed as `(max_value - min_value) / num_buckets`
- D) `width_bucket` is a window function only — it requires an `OVER (ORDER BY ...)` clause; using it outside a window context raises an `AnalysisException`

---

### Question 27 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: make_timestamp() function

**Question**:
What is the return type of `make_timestamp(year, month, day, hour, minute, second)` in Spark SQL, and what happens if any component value is out of range (e.g., month=13)?

- A) Returns a `StringType` formatted as `'YYYY-MM-DD HH:mm:ss'`; an out-of-range component raises a `DateTimeException`
- B) Returns a `TimestampType`; if any component is `NULL` or out of range, the function returns `NULL` rather than raising an error — matching the behaviour of `make_date()` for consistency
- C) Returns a `TimestampType`; an out-of-range component raises an `ArithmeticException` and halts the query
- D) Returns a `LongType` Unix epoch microsecond value; out-of-range components are silently clamped to the nearest valid value

---

### Question 28 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: bool_and() / bool_or() aggregate functions

**Question**:
In Spark SQL (Spark 3.0+), what do `bool_and(col)` and `bool_or(col)` return when aggregating a group that contains `NULL` values?

- A) Both functions treat `NULL` as `false` — a single `NULL` causes `bool_and` to return `false` and has no effect on `bool_or`
- B) `bool_and(col)` returns `true` only if every non-null value is `true` (NULLs are ignored); `bool_or(col)` returns `true` if at least one non-null value is `true` (NULLs are ignored); both return `NULL` only when all values in the group are `NULL`
- C) Both functions raise an `AnalysisException` when `NULL` values are present; the column must be filtered beforehand
- D) `bool_and` propagates `NULL` — any `NULL` in the group makes the result `NULL`; `bool_or` ignores `NULL`s entirely

---

### Question 29 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: bit_and / bit_or / bit_xor aggregate functions

**Question**:
A developer wants to compute the bitwise OR of an integer column across all rows in a group. Which Spark SQL aggregate function is correct, and what does it return for a group containing the values `[5, 3, 8]`?

- A) `bit_or(col)` returns `15` — it computes the bitwise OR of all non-null values: `5 | 3 | 8 = 15` (binary: `0101 | 0011 | 1000 = 1111`)
- B) `bit_or(col)` returns `0` — bitwise OR in SQL aggregates always resets to 0 for groups larger than 2 rows
- C) `bitwise_or_agg(col)` is the correct function name; `bit_or` is not supported in Spark SQL
- D) `bit_or(col)` returns the largest value in the group (8) because it selects the value with the most bits set

---

### Question 30 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: array_compact() function (Spark 3.4+)

**Question**:
What does `array_compact(array(1, NULL, 2, NULL, 3))` return in Spark SQL (Spark 3.4+)?

- A) `array(1, 2, 3)` — `array_compact` removes all `NULL` elements from the array, preserving the order of the remaining non-null elements
- B) `array(NULL, NULL, 1, 2, 3)` — `array_compact` moves `NULL` elements to the front of the array but does not remove them
- C) `array(1, 2, 3, NULL, NULL)` — `array_compact` moves `NULL` elements to the end of the array
- D) `array_compact` raises an `AnalysisException` when the array contains `NULL` elements; use `filter(arr, x -> x IS NOT NULL)` instead

---

### Question 31 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: startswith() / endswith() functions (Spark 3.3+)

**Question**:
What is the return type and behaviour of `startswith(str, prefix)` in Spark SQL (Spark 3.3+)?

- A) Returns `StringType` — the matched prefix if the string starts with the prefix, or an empty string otherwise
- B) Returns `BooleanType` — `true` if `str` begins with `prefix`, `false` otherwise; returns `NULL` if either argument is `NULL`
- C) Returns `IntegerType` — `1` if the prefix matches, `0` otherwise (SQL numeric boolean convention)
- D) `startswith` is not available in Spark SQL; use `str LIKE 'prefix%'` or `locate(prefix, str) = 1` instead

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: inline() table-generating function

**Question**:
What does `inline(array_of_structs)` do in Spark SQL?

**Scenario**:
```sql
SELECT inline(array(struct(1, 'a'), struct(2, 'b')))
```

**Question**:
What is the output?

- A) A single row containing the original array of structs as a nested column
- B) Two rows, each with the struct fields expanded into separate columns — `inline` is a table-generating function that explodes an `ArrayType(StructType(...))` column, producing one row per array element with each struct field becoming a separate output column
- C) An error — `inline` can only be used inside a `LATERAL VIEW` clause; using it directly in `SELECT` is not valid
- D) Two rows, each containing the entire struct as a single column (not unpacked) — use `explode` to unpack struct fields into separate columns

---

### Question 33 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: named_struct() vs struct()

**Question**:
What is the practical difference between `named_struct('x', col1, 'y', col2)` and `struct(col1, col2)` in Spark SQL?

- A) There is no difference — `named_struct` and `struct` are aliases that produce identical output schemas
- B) `named_struct('x', col1, 'y', col2)` allows you to specify custom field names (`x`, `y`) for the resulting `StructType`; `struct(col1, col2)` uses the input column names as the field names, which may be auto-generated names like `col1` and `col2` if the columns have no alias
- C) `named_struct` creates a `MapType` with string keys; `struct` creates a `StructType`
- D) `struct()` supports nested structs but `named_struct()` only supports flat (non-nested) structures

---

### Question 34 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: from_csv() function (Spark 3.0+)

**Question**:
What does `from_csv(col, schema_str)` return in Spark SQL (Spark 3.0+), and how does it differ from `from_json()`?

- A) Both `from_csv` and `from_json` return `StructType` columns; the only difference is the input serialisation format (CSV vs JSON)
- B) `from_csv(col, 'a INT, b STRING')` parses a CSV-formatted string column and returns a `StructType` column with the declared fields; `from_json` parses JSON strings; unlike `from_json`, `from_csv` does not support nested objects or arrays since CSV is inherently flat
- C) `from_csv` returns an `ArrayType` of values split by delimiter; `from_json` returns a `StructType`
- D) `from_csv` is available only as a DataFrame API method (`spark.read.csv`), not as a SQL scalar function; using it in a `SELECT` statement raises a `ParseException`

---

### Question 35 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: schema_of_csv() function (Spark 3.0+)

**Question**:
What does `schema_of_csv('"hello",42,true')` return in Spark SQL (Spark 3.0+)?

- A) An `ArrayType(StringType)` of the inferred column types as strings
- B) A `StringType` value containing the inferred DDL schema string such as `'_c0 STRING, _c1 INT, _c2 BOOLEAN'` — `schema_of_csv` infers the schema from a sample CSV string and returns it as a DDL-formatted string that can be passed to `from_csv()`
- C) A `StructType` object describing the schema; it cannot be used directly in SQL — only in PySpark via `F.schema_of_csv()`
- D) `schema_of_csv` always infers all columns as `StringType` and returns `'_c0 STRING, _c1 STRING, _c2 STRING'` — type inference is not supported for CSV

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: cardinality() vs size() NULL handling

**Question**:
How does `cardinality(col)` (Spark 3.0+) differ from `size(col)` when `col` is `NULL`?

- A) They behave identically — both return `0` when the array or map column is `NULL`
- B) `cardinality(col)` returns `NULL` when `col` is `NULL`; `size(col)` returns `-1` when `col` is `NULL` by default (legacy behaviour controlled by `spark.sql.legacy.sizeOfNull`); `cardinality` follows SQL-standard NULL propagation
- C) `cardinality(col)` returns `0` when `col` is `NULL`; `size(col)` returns `NULL`
- D) Both return `-1` for `NULL` inputs; the behaviour cannot be changed regardless of legacy settings

---

### Question 37 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: unix_date() function (Spark 3.1+)

**Question**:
What does `unix_date(date_col)` return in Spark SQL (Spark 3.1+)?

- A) A `LongType` value representing the Unix epoch seconds (seconds since 1970-01-01 00:00:00 UTC) for midnight on the given date — equivalent to `unix_timestamp(date_col)`
- B) An `IntegerType` value representing the number of days since the Unix epoch (`1970-01-01`); for example, `unix_date(date'2026-04-25')` returns the integer count of days from `1970-01-01` to `2026-04-25`
- C) A `StringType` value in the format `'YYYY-MM-DD'` — `unix_date` is an alias for `date_format(col, 'yyyy-MM-dd')`
- D) A `DoubleType` Unix timestamp with fractional seconds precision

---

### Question 38 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: date_from_unix_date() function (Spark 3.1+)

**Question**:
What does `date_from_unix_date(20203)` return in Spark SQL (Spark 3.1+), and what is the inverse function?

- A) A `TimestampType` value derived from multiplying `20203` by 86400 seconds to obtain epoch seconds, then converting to a timestamp
- B) A `DateType` value corresponding to the date that is 20203 days after the Unix epoch (`1970-01-01`); `date_from_unix_date` is the inverse of `unix_date(date_col)` — together they convert between `DateType` and integer day offsets from the epoch
- C) A `StringType` value `'2025-04-01'` — `date_from_unix_date` formats the integer as a date string but returns `StringType`
- D) `date_from_unix_date` is not a Spark SQL function; the correct function is `from_unixtime()` which accepts day integers

---

### Question 39 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: try_add() / try_subtract() / try_multiply() safe arithmetic (Spark 3.4+)

**Question**:
What is the result of `SELECT try_add(2147483647, 1)` (where 2147483647 is `Integer.MAX_VALUE`) in Spark SQL (Spark 3.4+)?

- A) `-2147483648` — integer overflow wraps around, identical to Java `int` overflow
- B) `NULL` — `try_add` returns `NULL` on arithmetic overflow instead of raising an `ArithmeticException`, making it safe to use when overflow is a valid possibility in the data
- C) `2147483648L` — Spark automatically promotes the result to `LongType` when overflow would occur
- D) `ArithmeticException` is raised — `try_add` only suppresses `NULL` inputs, not overflow

---

### Question 40 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: regexp_count() function (Spark 3.4+)

**Question**:
What does `regexp_count('abcabc', 'a.c')` return in Spark SQL (Spark 3.4+)?

- A) `1` — `regexp_count` returns only `0` or `1` (whether the pattern matches at all), equivalent to `regexp_like`
- B) `2` — `regexp_count` returns the total number of non-overlapping occurrences of the regex pattern in the string; `'a.c'` matches `'abc'` twice in `'abcabc'`
- C) `6` — `regexp_count` counts individual characters matching any character class in the pattern
- D) `regexp_count` is not a Spark SQL function; use `size(regexp_extract_all(str, pattern, 0))` instead

---

### Question 41 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.sampleBy() — stratified sampling

**Question**:
What does `df.sampleBy("status", fractions={"active": 0.5, "inactive": 0.1}, seed=42)` do?

- A) Randomly selects exactly 50% of all rows from the DataFrame with `seed=42`; the `fractions` dictionary is ignored in favour of the first value
- B) Performs stratified sampling — for each value in the `status` column, it independently samples the specified fraction of rows: approximately 50% of rows where `status='active'` and approximately 10% of rows where `status='inactive'`; rows whose `status` value is not in the `fractions` dictionary are excluded from the result
- C) Returns a DataFrame containing at most 50% of rows, where rows with `status='active'` are prioritised until the fraction is reached, then rows with `status='inactive'` are added up to 10%
- D) `sampleBy` is a SQL-only operation; it is not available as a DataFrame method and raises an `AttributeError`

---

### Question 42 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.checkpoint() eager vs lazy

**Question**:
What is the difference between `df.checkpoint(eager=True)` and `df.checkpoint(eager=False)`?

- A) `eager=True` writes the checkpoint to a local temp directory on the driver; `eager=False` writes to the distributed checkpoint directory set by `sc.setCheckpointDir()`
- B) `eager=True` immediately triggers a Spark action to materialise and write the DataFrame to the checkpoint directory, returning a new DataFrame backed by the checkpoint data; `eager=False` defers checkpointing until the next action is called on the returned DataFrame — the checkpoint is written lazily as part of that subsequent action
- C) `eager=True` persists the DataFrame in memory after checkpointing, equivalent to calling `checkpoint().persist()`; `eager=False` checkpoints without persisting
- D) Both options behave identically; the `eager` parameter is deprecated and has no effect since Spark 3.0

---

### Question 43 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.product() aggregate function (Spark 3.2+)

**Question**:
What does `df.groupBy("category").agg(F.product("price"))` compute, and how does it handle `NULL` values?

- A) It computes the arithmetic mean of the `price` column per `category` — `product` is an alias for `avg` in the PySpark API
- B) It computes the product (multiplication) of all non-null `price` values within each `category` group; `NULL` values are ignored in the multiplication — the same NULL-ignoring convention as `sum()` and `avg()`
- C) It computes the Cartesian product of the `price` and `category` columns, producing a result with every combination of distinct values
- D) `F.product()` is not available in PySpark; it is only accessible via `spark.sql("SELECT product(price) FROM ...")`

---

### Question 44 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.to() — project to target schema (Spark 3.4+)

**Question**:
What does `df.to(target_schema)` do in Spark 3.4+, and how does it differ from `df.select()`?

- A) `df.to(target_schema)` reorders and casts existing columns to match the `StructType` target schema — it matches columns by name (not position), casts types automatically, and raises an `AnalysisException` if a column in `target_schema` is missing from `df`; `df.select()` selects columns by explicit list and does not perform automatic type casting
- B) `df.to(target_schema)` is identical to `df.select(*target_schema.fieldNames())` — the `StructType` argument is only used for documentation purposes and does not trigger casting
- C) `df.to(target_schema)` writes the DataFrame to the schema's registered table location; it is an I/O method, not a transformation
- D) `df.to(target_schema)` converts the DataFrame to a Python dict using the field names in `target_schema` as keys

---

### Question 45 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.transform_keys() HOF on MapType (Spark 3.1+)

**Question**:
What does the following expression produce?

**Scenario**:
```python
df.select(F.transform_keys("scores", lambda k, v: F.upper(k)))
```
Where `scores` is a `MapType(StringType, IntegerType)` column containing `{"math": 90, "science": 85}`.

**Question**:
What is the result?

- A) `{"MATH": 90, "SCIENCE": 85}` — `transform_keys` applies the lambda to each key in the map, replacing keys while keeping values unchanged; `F.upper(k)` converts each key to uppercase
- B) `{"math": 90, "science": 85}` — `transform_keys` only validates that the lambda does not modify key types; it does not actually transform key values
- C) An `AnalysisException` — `transform_keys` requires a Python native lambda and cannot accept Spark Column expressions like `F.upper(k)`
- D) `{"MATH": "MATH", "SCIENCE": "SCIENCE"}` — `transform_keys` replaces both keys and values with the transformed key

---

### Question 46 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.transform_values() HOF on MapType (Spark 3.1+)

**Question**:
What does `F.transform_values("inventory", lambda k, v: v * 2)` produce for a map `{"apples": 5, "bananas": 3}`?

- A) `{"apples": 5, "bananas": 3}` — `transform_values` does not modify values; it validates the map structure only
- B) `{"apples": 10, "bananas": 6}` — `transform_values` applies the lambda `(key, value) -> value * 2` to each map entry, doubling every value while keeping keys unchanged
- C) `[10, 6]` — `transform_values` extracts and returns the transformed values as an `ArrayType`, discarding the keys
- D) An `AnalysisException` — `transform_values` requires a SQL string lambda expression, not a Python lambda

---

### Question 47 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.withField() — update nested struct field (Spark 3.1+)

**Question**:
A DataFrame has a column `address` of type `StructType(city: StringType, zip: StringType)`. A developer wants to add a `country` field to the struct without reconstructing the entire struct. Which expression is correct in Spark 3.1+?

- A) `df.withColumn("address", F.struct(df.address.city, df.address.zip, F.lit("US").alias("country")))` — reconstructing the entire struct is the only approach
- B) `df.withColumn("address", df["address"].withField("country", F.lit("US")))` — `Column.withField(fieldName, col)` adds or replaces a named field within a `StructType` column without requiring the full struct to be reconstructed
- C) `df.withField("address.country", F.lit("US"))` — `DataFrame.withField()` accepts a dot-notation path to the nested field
- D) `df.withColumn("address.country", F.lit("US"))` — `withColumn` automatically interprets the dot as a nested path into the struct

---

### Question 48 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.dropFields() — remove struct fields (Spark 3.1+)

**Question**:
Given a column `profile` of type `StructType(name: StringType, ssn: StringType, age: IntegerType)`, how do you drop the `ssn` field from the struct in Spark 3.1+?

- A) `df.withColumn("profile", df["profile"].dropFields("ssn"))` — `Column.dropFields(*fieldNames)` removes named fields from a `StructType` column and returns the modified column with the remaining fields
- B) `df.drop("profile.ssn")` — `DataFrame.drop()` accepts dot-notation to remove nested fields
- C) `df.withColumn("profile", F.struct(df.profile.name, df.profile.age))` — full struct reconstruction is the only way to remove nested fields
- D) `df.withColumn("profile", df["profile"].withField("ssn", F.lit(None)))` — dropping a field requires setting it to `NULL` first; there is no direct remove API

---

### Question 49 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.tail(n) — retrieve last n rows (Spark 3.0+)

**Question**:
What does `df.tail(5)` return in PySpark (Spark 3.0+), and how does it differ from `df.limit(5).collect()`?

- A) `df.tail(5)` returns the last 5 rows of the DataFrame as a Python list of `Row` objects; `df.limit(5).collect()` returns the first 5 rows — the two calls retrieve rows from opposite ends of the DataFrame
- B) Both return the same 5 rows because Spark DataFrames are unordered; `tail` is an alias for `limit` provided for pandas compatibility
- C) `df.tail(5)` returns a new DataFrame containing only the last 5 rows; `df.limit(5).collect()` returns a Python list of `Row` objects
- D) `df.tail(5)` triggers a global sort by row index before collecting, guaranteeing deterministic results regardless of partition order

---

### Question 50 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.array_insert() function (Spark 3.4+)

**Question**:
What does `F.array_insert(F.col("items"), 2, F.lit("new"))` produce for the array `["a", "b", "c"]` in Spark 3.4+?

- A) `["a", "b", "new", "c"]` — `array_insert(arr, pos, value)` inserts `value` at the 1-based position `pos`, shifting existing elements right; position 2 inserts before the current second element `"b"`
- B) `["a", "new", "b", "c"]` — position 2 inserts `value` after the first element, before the second
- C) `["a", "b", "new"]` — `array_insert` appends `value` at the end when `pos` equals the array length
- D) An `AnalysisException` — `array_insert` is not a valid function; use `F.concat(F.slice(col, 1, pos-1), F.array(value), F.slice(col, pos, size))` instead

---

### Question 51 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.aggregate() HOF — custom fold (Spark 3.1+)

**Question**:
What does the following expression compute for the array `[1, 2, 3, 4]`?

**Scenario**:
```python
F.aggregate(F.col("nums"), F.lit(0), lambda acc, x: acc + x)
```

**Question**:
What is the result?

- A) `[1, 3, 6, 10]` — a cumulative sum array, because `aggregate` returns intermediate accumulator values at each step
- B) `10` — `F.aggregate(col, zero, merge_func)` performs a fold over the array elements using `merge_func(accumulator, element)`, starting with the zero value; for `[1,2,3,4]` starting at `0` this computes `((((0+1)+2)+3)+4) = 10`
- C) `4` — `F.aggregate` returns only the last element processed, not the accumulated total
- D) An `AnalysisException` because `F.aggregate` requires a `finish` function argument as the fourth parameter

---

### Question 52 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.zip_with() HOF (Spark 3.1+)

**Question**:
What does `F.zip_with(F.col("a"), F.col("b"), lambda x, y: x + y)` produce for arrays `a=[10, 20, 30]` and `b=[1, 2, 3]`?

- A) `[[10, 1], [20, 2], [30, 3]]` — `zip_with` zips the two arrays into an array of pairs without applying any function
- B) `[11, 22, 33]` — `zip_with(arr1, arr2, merge_func)` produces a new array by element-wise applying `merge_func(x, y)` to corresponding elements; `10+1=11`, `20+2=22`, `30+3=33`
- C) `[10, 20, 30, 1, 2, 3]` — `zip_with` concatenates the two arrays
- D) `[31, 31, 31]` — `zip_with` broadcasts the sum of all elements of `b` across each element of `a`

---

### Question 53 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.exists() and F.forall() HOF on arrays (Spark 3.1+)

**Question**:
What does `F.forall(F.col("scores"), lambda x: x >= 60)` return for the array `[72, 85, 55, 90]`?

- A) `true` — `forall` returns `true` when the majority of elements satisfy the predicate
- B) `false` — `forall(arr, predicate)` returns `true` only when every element satisfies the predicate; because `55 < 60`, not all elements pass, so the result is `false`
- C) `55` — `forall` returns the first element that fails the predicate, or `NULL` if all pass
- D) `3` — `forall` returns the count of elements satisfying the predicate

---

### Question 54 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: F.flatten() on nested arrays

**Question**:
What does `F.flatten(F.col("nested"))` return for the value `[[1, 2], [3, 4], [5]]`?

- A) `[1, 2, 3, 4, 5]` — `flatten` takes an `ArrayType(ArrayType(...))` column and returns a single-level `ArrayType` by concatenating all inner arrays in order
- B) `[[1, 2, 3, 4, 5]]` — `flatten` wraps all inner array elements into a single outer array
- C) `[3, 5]` — `flatten` returns the lengths of each inner array
- D) `[[1, 2], [3, 4], [5]]` — `flatten` is a no-op when the outer array already contains arrays

---

### Question 55 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.writeTo() v2 DataSource API — createOrReplace vs append

**Question**:
Which statement correctly describes the difference between `df.writeTo("table").createOrReplace()` and `df.writeTo("table").append()` in the Spark v2 DataSource API?

- A) They are identical — `createOrReplace` and `append` both write data incrementally and never truncate existing rows
- B) `createOrReplace()` creates the table if it does not exist or atomically drops and recreates it if it does, replacing all existing data; `append()` creates the table if it does not exist or adds new rows to the existing table without removing prior data
- C) `createOrReplace()` only works with Delta tables; `append()` works with any file-based format
- D) `append()` requires the target table to already exist — it raises a `NoSuchTableException` if the table is missing

---

### Question 56 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.try_element_at() safe array/map access (Spark 3.4+)

**Question**:
What does `F.try_element_at(F.col("items"), F.lit(10))` return when the array `items` has fewer than 10 elements (Spark 3.4+)?

- A) Raises an `ArrayIndexOutOfBoundsException` — `try_element_at` does not suppress index-out-of-bounds errors
- B) Returns `NULL` — `try_element_at(col, index)` returns `NULL` when the 1-based index is out of range or the key is absent in a map, instead of throwing an exception; it is the safe alternative to `F.element_at()` which raises an error on out-of-bounds access
- C) Returns the last element of the array — `try_element_at` clamps the index to the array length
- D) Returns `0` or the zero value of the array element type when the index is out of range

---

### Question 57 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.array_remove() and F.array_distinct()

**Question**:
What is the difference between `F.array_remove(col, value)` and `F.array_distinct(col)`?

- A) `array_remove` removes the first occurrence of `value` from the array; `array_distinct` removes the first occurrence of every duplicate element
- B) `array_remove(col, value)` removes all occurrences of the specified `value` from the array; `array_distinct(col)` removes duplicate elements from the array, keeping the first occurrence of each distinct value — they solve different problems and can be combined
- C) They are identical when `value` is the most frequent element in the array
- D) `array_distinct` requires the array to be sorted; `array_remove` works on unsorted arrays

---

### Question 58 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.date_diff() vs F.datediff() naming

**Question**:
In PySpark (Spark 3.5+), is there a difference between `F.date_diff(end, start)` and `F.datediff(end, start)`?

- A) `F.date_diff` (snake_case) is the new Spark 3.5+ name; `F.datediff` (camelCase) is the older alias — both return an `IntegerType` count of days from `start` to `end` and are functionally identical
- B) `F.date_diff` computes the difference in calendar days; `F.datediff` computes the difference in calendar months
- C) `F.date_diff` is only available in SQL; `F.datediff` is only available in the DataFrame API
- D) `F.date_diff` includes the start date in the count; `F.datediff` excludes both endpoints

---

### Question 59 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.crossJoin()

**Question**:
What does `df1.crossJoin(df2)` produce, and what configuration guard should be set when using it?

- A) The same result as an inner join with all shared column names as implicit join keys
- B) The Cartesian product of `df1` and `df2` — every row of `df1` paired with every row of `df2`; the result has `df1.count() × df2.count()` rows; `spark.sql.crossJoin.enabled=true` must be set (or `CROSS JOIN` used explicitly in SQL) to allow unintentional Cartesian products to be caught rather than executed silently
- C) An outer join that retains all rows from both DataFrames and fills unmatched columns with `NULL`
- D) `crossJoin` is equivalent to `unionByName` — it combines rows from both DataFrames vertically without duplication

---

### Question 60 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: many
**Topic**: write.partitionBy() behaviour guarantees

**Question**:
Which of the following statements about `df.write.partitionBy("year", "month")` are correct? *(Select all that apply.)*

- A) Spark creates a directory hierarchy `year=<val>/month=<val>/` under the output path, with one or more part files inside each leaf directory
- B) The `year` and `month` columns are excluded from the data files written inside each partition directory — their values are encoded in the directory name only
- C) The number of output files per partition directory equals the number of DataFrame partitions that contain data for that partition key combination
- D) `partitionBy` guarantees exactly one output file per partition key combination — Spark coalesces each group to a single file automatically

---

### Question 61 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.write.bucketBy() + sortBy() — requirements

**Question**:
A developer uses `df.write.bucketBy(16, "customer_id").sortBy("order_date").saveAsTable("orders")`. Which statement is correct?

- A) `sortBy` is required when using `bucketBy` — omitting it raises an `AnalysisException`
- B) `bucketBy` can only be used with `saveAsTable`, not with `save()` or `format(...).save(path)` — bucketing metadata is stored in the Hive/Spark metastore and requires a named table; `sortBy` is optional and adds a sort within each bucket file for faster merge joins
- C) `bucketBy` works with any write method including `save(path)` and `insertInto()` — the bucket metadata is stored as a hidden `_bucket_metadata.json` file alongside the data
- D) `sortBy` performs a global sort across all bucket files, not a per-bucket sort — the output data is globally ordered by `order_date`

---

### Question 62 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.current_date() vs F.current_timestamp() evaluation timing

**Question**:
A developer calls `df.withColumn("ts", F.current_timestamp())` and then calls `df.show()` followed 10 seconds later by another `df.show()`. What are the timestamp values across the two `show()` calls?

- A) Both `show()` calls produce the same timestamp because `current_timestamp()` is evaluated once when `withColumn` is called and the result is cached
- B) Each `show()` triggers a new execution; `current_timestamp()` (and `current_date()`) are non-deterministic functions evaluated at the start of each query execution — so both calls may produce different timestamps depending on when each action is actually executed; within a single query execution all rows receive the same timestamp
- C) Each row receives a different timestamp reflecting the exact wall-clock time when that specific row was processed by its executor task
- D) `current_timestamp()` always returns the cluster start time, not the current wall-clock time

---

### Question 63 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.dtypes property

**Question**:
What does `df.dtypes` return in PySpark?

- A) A `StructType` object describing the schema of the DataFrame
- B) A Python list of `(column_name, type_string)` tuples — one tuple per column, where the type string is a SQL-compatible type name such as `'int'`, `'string'`, `'array<double>'`
- C) A Python dict mapping column names to their `DataType` objects (e.g., `{'age': IntegerType()}`)
- D) A list of `DataType` objects in column order, without the column names

---

### Question 64 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.overlay() — string/binary replacement (Spark 3.0+)

**Question**:
What does `F.overlay(F.col("str"), F.lit("XY"), F.lit(3), F.lit(2))` return for `str = "abcdef"`?

- A) `"abXYef"` — `overlay(input, replace, pos, len)` replaces `len` characters starting at 1-based `pos` with `replace`; positions 3–4 (`'cd'`) are replaced by `'XY'`, giving `"abXYef"`
- B) `"XYabcdef"` — `overlay` prepends `replace` to the original string
- C) `"abcXYdef"` — `pos=3` means insert after the 3rd character, pushing existing characters right
- D) `"abcdeXY"` — `overlay` appends `replace` after the character at `pos + len`

---

### Question 65 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: DataFrameWriter.option("compression") valid values for Parquet

**Question**:
Which of the following is NOT a valid value for `df.write.format("parquet").option("compression", ...)` in Spark?

- A) `"snappy"`
- B) `"gzip"`
- C) `"brotli"`
- D) `"deflate"`

---

### Question 66 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.map_zip_with() HOF (Spark 3.1+)

**Question**:
What does `F.map_zip_with(F.col("prices"), F.col("discounts"), lambda k, v1, v2: v1 - v2)` produce for `prices={"apple": 1.5, "banana": 0.8}` and `discounts={"apple": 0.1, "banana": 0.05}`?

- A) `{"apple": 0.1, "banana": 0.05}` — `map_zip_with` returns the second map unchanged
- B) `{"apple": 1.4, "banana": 0.75}` — `map_zip_with(map1, map2, merge_func)` merges two maps with the same keys by applying `merge_func(key, val1, val2)` for each key; `1.5 - 0.1 = 1.4`, `0.8 - 0.05 = 0.75`
- C) `[1.4, 0.75]` — `map_zip_with` extracts values and returns an `ArrayType`
- D) `NULL` — `map_zip_with` raises an error when the two maps have different sets of keys

---

### Question 67 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.inputFiles()

**Question**:
What does `df.inputFiles()` return?

- A) A Python list of the absolute paths of all input files that contribute data to the DataFrame — useful for auditing which files were read or for filtering based on file paths
- B) A new DataFrame with a single column `path` containing one row per input file
- C) A Python dict mapping each partition index to the list of file paths assigned to that partition
- D) `inputFiles()` is only available for RDDs; calling it on a DataFrame raises an `AttributeError`

---

### Question 68 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.percentile_approx() vs F.median() (Spark 3.4+)

**Question**:
Spark 3.4+ introduced `F.median(col)`. How does it relate to `F.percentile_approx(col, 0.5)` and `F.percentile(col, 0.5)`?

- A) `F.median(col)` is an exact aggregate that computes the true median using a sort; `F.percentile_approx(col, 0.5)` uses a sketch algorithm; `F.percentile(col, 0.5)` is not a valid PySpark function
- B) `F.median(col)` is shorthand for `F.percentile_approx(col, 0.5)` — it uses the same Greenwald-Khanna sketch algorithm and produces an approximate result; `F.percentile(col, 0.5)` computes the exact median but is only available in Spark SQL, not as a PySpark function
- C) All three — `median`, `percentile_approx(col, 0.5)`, and `percentile(col, 0.5)` — are exact and deterministic, producing identical results
- D) `F.median` uses a different algorithm from `percentile_approx` and is guaranteed to return exact results for datasets with fewer than 1 million rows

---

### Question 69 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: many
**Topic**: df.observe() — inline metrics collection (Spark 3.3+)

**Question**:
Which of the following statements about `df.observe(observation, *exprs)` are correct? *(Select all that apply.)*

- A) `df.observe()` attaches one or more aggregate expressions to the DataFrame; the metrics are computed during the action that processes the DataFrame and can be retrieved afterwards without triggering a separate aggregate query
- B) The `Observation` object's `.get` property (or `.wait()` / `.get`) blocks until the associated action completes and then returns the metric values as a Python dict
- C) `df.observe()` is only applicable to streaming DataFrames; it raises an `AnalysisException` when used with batch DataFrames
- D) Multiple `df.observe()` calls with different `Observation` objects can be chained on the same DataFrame, collecting different sets of metrics in a single pass

---

### Question 70 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ANSI mode — spark.sql.ansi.enabled

**Question**:
What happens when `spark.sql.ansi.enabled=true` and a query executes `SELECT CAST('abc' AS INT)`?

- A) The result is `NULL` — ANSI mode makes `CAST` return `NULL` for invalid conversions, matching standard SQL behaviour
- B) The result is `0` — ANSI mode maps all invalid `CAST` results to the zero value of the target type
- C) Spark raises a `NumberFormatException` (wrapped in a `SparkArithmeticException` or `SparkNumberFormatException`) — ANSI mode disables silent NULL-returning `CAST` for invalid conversions; the query fails instead of silently returning `NULL`
- D) Spark raises an `AnalysisException` at query-planning time, before execution, because ANSI mode validates `CAST` operand types statically

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.adaptive.skewJoin.enabled — AQE skew join

**Question**:
When `spark.sql.adaptive.skewJoin.enabled=true` (default `true`), how does AQE handle a skewed shuffle partition during a sort-merge join?

- A) AQE repartitions the entire input DataFrame evenly before the join, eliminating skew by rebalancing all partitions to equal size
- B) AQE detects oversized shuffle partitions (exceeding `skewedPartitionThresholdInBytes` and `skewedPartitionFactor` × median partition size); for each skewed partition it splits the skewed side into multiple sub-partitions and replicates the corresponding non-skewed matching partition from the other side, processing each sub-partition pair as a separate task — reducing the maximum task duration without shuffling the entire dataset
- C) AQE converts the skewed sort-merge join into a broadcast hash join automatically when a partition is detected as skewed
- D) AQE raises a `QueryExecutionException` when a skewed partition is detected; the developer must manually re-run the query after salting the skewed key

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.execution.arrow.pyspark.selfDestruct.enabled

**Question**:
What does `spark.sql.execution.arrow.pyspark.selfDestruct.enabled=true` do when using `df.toPandas()` with Arrow enabled?

- A) It causes the Spark executor to delete input Parquet files after they are read into an Arrow buffer — it is a data lifecycle management option
- B) It instructs the Arrow deserialization path to release each Java Arrow buffer immediately after it has been copied into the pandas DataFrame, reducing peak JVM heap usage on the driver during `toPandas()` by freeing Arrow memory as soon as each batch is consumed rather than holding all batches in memory simultaneously
- C) It automatically unpersists the Spark DataFrame from cache after `toPandas()` completes
- D) It prevents the resulting pandas DataFrame from being serialised back to Spark — `to_spark()` raises an error on DataFrames created with this option enabled

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Whole-stage code generation — spark.sql.codegen.wholeStage

**Question**:
Which of the following statements about Whole-Stage Code Generation (`spark.sql.codegen.wholeStage`, default `true`) are correct? *(Select all that apply.)*

- A) Whole-stage code generation fuses multiple Spark operators within a stage into a single compiled Java method, reducing virtual function call overhead and enabling the JVM JIT compiler to generate efficient native code for the entire pipeline
- B) Whole-stage code generation is automatically disabled for operators with more than `spark.sql.codegen.maxFields` (default `100`) input or output fields, because code generation becomes counterproductive for very wide schemas
- C) Setting `spark.sql.codegen.wholeStage=false` is a valid debugging technique — when a query fails with a cryptic codegen error, disabling whole-stage codegen may produce cleaner stack traces and help isolate the offending operator
- D) Whole-stage code generation replaces the Tungsten binary memory format with standard Java objects for improved compatibility with third-party serialization libraries

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.files.maxPartitionBytes vs spark.sql.files.openCostInBytes — interaction

**Question**:
How do `spark.sql.files.maxPartitionBytes` (default `128 MB`) and `spark.sql.files.openCostInBytes` (default `4 MB`) interact when Spark determines the number of input partitions for a file-based scan?

- A) `maxPartitionBytes` sets the maximum size of each input partition; `openCostInBytes` adds a virtual padding cost per file to account for file-open overhead — this padding effectively merges many small files into the same partition when their total padded size stays under `maxPartitionBytes`, reducing the number of tasks that each open just one tiny file
- B) `openCostInBytes` is subtracted from `maxPartitionBytes` to produce the effective partition size budget, so the actual data per partition is `maxPartitionBytes - openCostInBytes = 124 MB`
- C) `openCostInBytes` controls the minimum size of a partition; any file smaller than `openCostInBytes` is always assigned its own dedicated partition regardless of `maxPartitionBytes`
- D) The two settings are independent — `maxPartitionBytes` governs read partitioning while `openCostInBytes` governs write file sizing

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.adaptive.coalescePartitions.parallelismFirst

**Question**:
What does `spark.sql.adaptive.coalescePartitions.parallelismFirst` (default `true`) control in AQE partition coalescing?

- A) When `true`, AQE coalesces shuffle partitions targeting `advisoryPartitionSizeInBytes` and ignores all parallelism constraints; when `false`, AQE will not coalesce if doing so would reduce parallelism below `spark.default.parallelism`
- B) When `true` (default), AQE coalesces shuffle partitions primarily based on the `advisoryPartitionSizeInBytes` target and ignores `coalescePartitions.minPartitionNum` to avoid producing too few partitions; when `false`, AQE respects `minPartitionNum` as a lower bound on the merged partition count — protecting parallelism at the expense of potentially producing smaller-than-advisory partitions
- C) When `true`, AQE sorts partitions by size before coalescing so that the largest partitions are grouped first; when `false`, partitions are coalesced in their natural shuffle order
- D) This setting has no observable effect in Spark 3.3+; it was deprecated and replaced by `spark.sql.adaptive.advisoryPartitionSizeInBytes`

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.files.ignoreMissingFiles

**Question**:
What happens by default when Spark reads a file-based source (e.g., Parquet) and a file is deleted between query planning and execution?

- A) Spark automatically retries the read three times before failing with a `FileNotFoundException`
- B) By default (`spark.sql.files.ignoreMissingFiles=false`) Spark raises a `FileNotFoundException` and the query fails; setting `spark.sql.files.ignoreMissingFiles=true` causes Spark to skip any file that is not found at read time and return only the data that was successfully read — useful for directories where files may be deleted concurrently
- C) Spark silently replaces missing file data with `NULL` rows regardless of the `ignoreMissingFiles` setting
- D) Spark always succeeds because file paths are resolved lazily — a missing file only causes an error at the point the row is accessed in driver memory

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.shuffle.file.buffer — shuffle write buffer

**Question**:
What does `spark.shuffle.file.buffer` (default `32k`) control, and what is the effect of increasing it?

- A) It controls the number of shuffle output files created per mapper; increasing it reduces the total number of shuffle files
- B) It sets the in-memory write buffer size for each shuffle output file stream on the executor; increasing it from the default `32k` reduces the number of system calls made to flush shuffle data to disk, potentially improving shuffle write throughput at the cost of additional executor heap memory per concurrent shuffle write stream
- C) It controls the in-memory read buffer size when reducers fetch shuffle blocks from remote executors; it has no effect on the shuffle write path
- D) It caps the maximum size of a single shuffle block; blocks exceeding this limit are split across multiple files automatically

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Off-heap memory — spark.memory.offHeap.enabled / offHeap.size

**Question**:
Which of the following statements about Spark off-heap memory (`spark.memory.offHeap.enabled=true`, `spark.memory.offHeap.size`) are correct? *(Select all that apply.)*

- A) Off-heap memory is allocated outside the JVM heap using `sun.misc.Unsafe`; it is not subject to JVM garbage collection, which can reduce GC pause times for large memory workloads
- B) `spark.memory.offHeap.size` sets the total off-heap memory budget per executor; this is in addition to `spark.executor.memory` and is not reflected in the `--executor-memory` flag passed to the cluster manager
- C) When off-heap memory is enabled, Spark automatically migrates all cached data to off-heap storage; on-heap caching is completely disabled
- D) Off-heap memory is used by the Tungsten execution engine for storing sort buffers, hash tables, and cached data when `StorageLevel.OFF_HEAP` is explicitly specified; it is not used automatically for all operations

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.execution.sortBeforeRepartition

**Question**:
What does `spark.sql.execution.sortBeforeRepartition` (default `true`) do?

- A) It globally forces all `repartition(n)` calls to perform a sort by partition key before redistributing data, making repartition equivalent to `repartitionByRange`
- B) When writing data with `df.write.repartition(n)`, setting this to `true` sorts records within each output partition by their sort key before writing, improving downstream read performance for sorted scans
- C) Before a hash-based repartition shuffle, Spark sorts records within each map-side partition by their hash value; this improves sequential disk writes during the shuffle write phase and reduces random I/O, at the cost of a pre-sort CPU overhead — it can be disabled to trade write efficiency for lower CPU usage
- D) This setting controls whether `repartitionByRange` falls back to a hash repartition when the range boundaries cannot be sampled accurately

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.kryo.registrationRequired

**Question**:
What happens when `spark.kryo.registrationRequired=true` and Spark encounters a class that has not been registered with Kryo serialization?

- A) Spark silently falls back to Java serialization for the unregistered class, with no error
- B) Spark raises a `KryoException` with a message such as `"Class is not registered"` and the job fails; this setting enforces strict Kryo registration, which guarantees smaller serialized sizes and faster serialization but requires all serialized classes to be explicitly registered via `spark.kryo.classesToRegister` or a custom `KryoRegistrator`
- C) Spark logs a warning but continues using Kryo with the unregistered class by writing the fully qualified class name into the serialized bytes — the same behaviour as `registrationRequired=false`
- D) Spark skips serializing unregistered class instances, replacing them with `null` in the task closure

---

### Question 81 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: trigger(availableNow=True) vs trigger(once=True)

**Question**:
What is the difference between `trigger(availableNow=True)` and `trigger(once=True)` in Structured Streaming?

- A) They are identical; `availableNow` was introduced as a cleaner alias in Spark 3.3+ but behaves the same as `once`
- B) `trigger(once=True)` processes all available data in a single micro-batch and then stops; `trigger(availableNow=True)` (Spark 3.3+) processes all available data across multiple micro-batches (respecting `maxFilesPerTrigger`, `maxBytesPerTrigger`, etc.) and then stops — providing the same "catch up and stop" semantics as `once` but with better parallelism and rate limiting
- C) `trigger(availableNow=True)` runs the stream continuously until no new data arrives for 60 seconds, then stops automatically; `trigger(once=True)` always processes exactly one micro-batch
- D) `trigger(once=True)` is only supported for Kafka sources; `trigger(availableNow=True)` works with all streaming sources

---

### Question 82 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Streaming query progress — inputRowsPerSecond vs processedRowsPerSecond

**Question**:
In `query.lastProgress`, what is the difference between `inputRowsPerSecond` and `processedRowsPerSecond`?

- A) They are always equal because Spark processes every row it reads in the same micro-batch
- B) `inputRowsPerSecond` is the rate at which rows arrive at the source (measured from source metadata such as Kafka lag or file modification times); `processedRowsPerSecond` is the rate at which rows were actually processed by the streaming query in the last micro-batch — if the source is faster than the query can process, `inputRowsPerSecond > processedRowsPerSecond`; if the query is catching up, the reverse may be true
- C) `inputRowsPerSecond` counts rows before filtering; `processedRowsPerSecond` counts rows after all filter and transformation operations
- D) `processedRowsPerSecond` includes rows dropped by watermark expiry; `inputRowsPerSecond` does not

---

### Question 83 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Streaming file source — schema requirement

**Question**:
What happens when a developer reads a streaming file source without specifying a schema?

**Scenario**:
```python
spark.readStream.format("json").load("/data/events/")
```

**Question**:
What is the result?

- A) Spark infers the schema by scanning all existing files before starting the stream, then uses that inferred schema for all subsequent micro-batches — the same behaviour as batch reads
- B) Spark raises an `AnalysisException` with a message like `"Schema must be specified when creating a streaming source for json"` — unlike batch reads, streaming sources require an explicit schema because Spark cannot scan the full dataset upfront and schema inference is not available for streaming
- C) Spark infers the schema lazily from the first micro-batch of files processed; if later files have different schemas, Spark will raise a schema mismatch error
- D) Spark uses a default schema of `StructType([StructField("value", StringType())])` for all file-based streaming sources when no schema is provided

---

### Question 84 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Console sink — use case and limitations

**Question**:
Which of the following best describes the console sink in Structured Streaming?

- A) The console sink writes output rows to an HDFS path named `console/` and is the recommended sink for production monitoring dashboards
- B) The console sink prints each micro-batch's output to the driver stdout; it is intended for debugging and development only — it is not fault-tolerant (no checkpointing of output), it does not guarantee exactly-once delivery, and it should not be used in production
- C) The console sink is the only built-in sink that supports the `update` output mode; all other sinks require `append` mode
- D) The console sink buffers all output rows in driver memory until `query.stop()` is called, then prints them all at once

---

### Question 85 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Watermark + append mode — when late data is dropped

**Question**:
A streaming query uses `withWatermark("event_time", "10 minutes")` and writes in `append` mode. An event arrives with `event_time = 12:03` when the current watermark is `12:08`. What happens?

- A) The event is processed normally — the watermark only affects window aggregation, not individual row appends
- B) The event is silently dropped — in `append` mode with watermarking, Spark drops any row whose `event_time` is older than the current watermark (`12:08 - 10 min = 11:58`); since `12:03 > 11:58`, this event is actually NOT dropped — it is appended to the output
- C) The event triggers an immediate watermark advance to `12:03`, then is appended to the output
- D) The event is held in state until the next trigger fires, then emitted if no newer events arrive

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: session_window() — gap-based sessions (Spark 3.2+)

**Question**:
What does `session_window(event_time_col, "5 minutes")` define in Structured Streaming?

- A) A fixed 5-minute tumbling window aligned to the clock (e.g., 12:00–12:05, 12:05–12:10)
- B) A gap-based dynamic window: a session starts with the first event and extends as long as subsequent events arrive within 5 minutes of the previous event in the same group; the session closes when no new event arrives within the 5-minute gap — sessions have variable duration unlike fixed tumbling or sliding windows
- C) A 5-minute sliding window with a 1-minute slide interval, producing overlapping windows
- D) `session_window` is a SQL-only function; it cannot be called via the PySpark DataFrame API

---

### Question 87 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Kafka source — fixed schema

**Question**:
What is the schema of a DataFrame produced by `spark.readStream.format("kafka").load()`?

- A) The schema is inferred from the Kafka message content by parsing the first message in each partition
- B) The schema is always fixed regardless of message content: `key BinaryType, value BinaryType, topic StringType, partition IntegerType, offset LongType, timestamp TimestampType, timestampType IntegerType` — the actual message payload is in the `value` column as raw bytes and must be decoded explicitly (e.g., via `F.from_json`, `CAST(...AS STRING)`)
- C) The schema contains a single column `value StringType` with the UTF-8 decoded message body
- D) The schema is configured via `option("schema", ddl_string)` and Spark validates each Kafka message against it, dropping malformed messages

---

### Question 88 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: kafka.group.id option — risks

**Question**:
What is the risk of setting `.option("kafka.group.id", "my-group")` when reading a Kafka streaming source in Spark?

- A) There is no risk — specifying a `group.id` is required for Kafka sources; without it Spark raises an `IllegalArgumentException`
- B) When a fixed `group.id` is set, Kafka brokers track committed offsets for that consumer group; if multiple concurrent Spark streaming queries share the same `group.id`, they will interfere with each other's offset tracking — Spark recommends not setting `kafka.group.id` and instead letting Spark manage offsets internally in the checkpoint directory, which is the default behaviour
- C) Setting `kafka.group.id` forces the source into `earliest` starting offset mode regardless of the `startingOffsets` option
- D) A fixed `group.id` prevents the Spark Kafka connector from reading from multiple partitions in parallel — all partitions are assigned to a single task

---

### Question 89 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: maxOffsetsPerTrigger — Kafka rate limiting

**Question**:
What does `.option("maxOffsetsPerTrigger", "10000")` do when reading from a Kafka streaming source?

- A) It limits the total number of Kafka partitions that Spark reads from per micro-batch trigger
- B) It caps the total number of Kafka offsets (rows) read across all partitions per micro-batch trigger; this limits the amount of data processed in each batch, preventing a single large burst from creating an oversized micro-batch and causing memory pressure or long processing times
- C) It sets the maximum offset lag (difference between latest and current offset) before Spark raises an alert
- D) It sets the starting offset for the Kafka consumer; offsets above this number are excluded from the stream

---

### Question 90 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: flatMapGroupsWithState — arbitrary stateful processing

**Question**:
What is the key difference between `mapGroupsWithState` and `flatMapGroupsWithState` in Structured Streaming?

- A) `mapGroupsWithState` supports both `append` and `update` output modes; `flatMapGroupsWithState` only supports `append` mode
- B) `mapGroupsWithState` emits exactly one output row per group per trigger; `flatMapGroupsWithState` can emit zero or more output rows per group per trigger — the `flat` prefix reflects the ability to yield an `Iterator` of results, enabling patterns like emitting multiple events per state transition or emitting nothing for intermediate state updates
- C) `flatMapGroupsWithState` persists state to an external database automatically; `mapGroupsWithState` uses in-memory state only
- D) `mapGroupsWithState` is deprecated in Spark 3.0+; `flatMapGroupsWithState` is the only supported stateful operator in newer Spark versions

---

### Question 91 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect — AnalysisException timing

**Question**:
In a Spark Connect client session, when does an `AnalysisException` (e.g., referencing a non-existent column) surface?

- A) Immediately when the transformation method is called on the client — the client validates column names locally against the schema before sending anything to the server
- B) When an action is triggered — the logical plan is sent to the Spark Connect server only when an action (e.g., `collect()`, `show()`, `count()`) is called; the server performs analysis and raises the `AnalysisException` at that point, not at transformation time
- C) Never — Spark Connect suppresses `AnalysisException` and returns an empty DataFrame instead
- D) At session creation time — `SparkSession.builder.remote(url).getOrCreate()` eagerly validates all previously defined DataFrames against the server schema

---

### Question 92 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark Connect — token authentication in URL

**Question**:
How is an authentication token passed in a Spark Connect connection URL?

- A) As a separate `option("token", "...")` call on the `SparkSession.builder` — tokens cannot be embedded in the URL string
- B) Using the `;token=<value>` suffix in the `sc://` URL, for example `sc://hostname:15002/;token=mySecretToken` — the token is part of the URL's session parameter string and is forwarded by the gRPC client as a header on every request
- C) Tokens are not supported in Spark Connect — only Kerberos SPNEGO authentication can be used
- D) The token is passed as an environment variable `SPARK_CONNECT_TOKEN`; it cannot be embedded in the URL

---

### Question 93 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect — running PySpark without a local JVM

**Question**:
Spark Connect enables a PySpark client to run without a locally installed JVM. Which of the following explains why?

- A) Spark Connect uses WASM to compile the Spark JVM bytecode to run natively in the Python process
- B) In the classic PySpark architecture, a local JVM (Py4J gateway) is required on the client machine to translate Python calls to JVM calls; Spark Connect replaces Py4J with a gRPC stub that sends serialised logical plans over the network — the JVM runs only on the remote Spark Connect server, so no JVM installation is needed on the client
- C) Spark Connect compiles the DataFrame plan to native Python bytecode that is executed directly without JVM involvement anywhere in the stack
- D) The PySpark client in Spark Connect mode downloads the Spark JVM at runtime from the server and executes it in a sandboxed subprocess

---

### Question 94 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect — UDF serialisation

**Question**:
How are Python UDFs (user-defined functions) executed when using Spark Connect?

- A) Python UDFs are compiled to JVM bytecode on the client and sent to the server as a JAR file
- B) Python UDFs are serialised (pickled) on the client and sent to the Spark Connect server as part of the gRPC plan; the server deserialises and executes the UDF in a Python worker process on the executor side — identical to classic PySpark UDF execution, except the serialised function travels over the network rather than through Py4J
- C) Python UDFs are not supported in Spark Connect; only Scala and Java UDFs registered via JAR files can be used
- D) Python UDFs are executed on the client machine using the client's Python environment and the results are sent to the server as data, never as code

---

### Question 95 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect — server crash impact on clients

**Question**:
What happens to active Spark Connect client sessions when the Spark Connect server process crashes?

- A) All in-flight queries are automatically retried on a standby server with no visible interruption to clients
- B) Queries that were running at the time of the crash are lost — the server holds all session state and query execution state; client sessions receive an error on their next request; however, the client-side Python process itself survives (unlike classic PySpark where a JVM crash kills the driver process), allowing the developer to reconnect and resubmit queries
- C) The client detects the crash immediately via a gRPC health-check and automatically reconnects to the same server within 30 seconds
- D) The crash is invisible to clients — Spark Connect buffers all results in the client's local memory before the server crashes, so no data is lost

---

### Question 96 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: psdf.spark.cache() — caching a Pandas-on-Spark DataFrame

**Question**:
What does `psdf.spark.cache()` do in the Pandas API on Spark?

- A) It converts the Pandas-on-Spark DataFrame to a native pandas DataFrame and stores it in the Python process memory using pandas' internal caching mechanism
- B) It caches the underlying Spark DataFrame in memory using `MEMORY_AND_DISK` storage level — equivalent to calling `.cache()` on the underlying Spark DataFrame; subsequent operations on `psdf` that trigger Spark actions will read from the cached data rather than recomputing from the original source
- C) It writes the Pandas-on-Spark DataFrame to a temporary Delta table that serves as a materialised cache
- D) `psdf.spark.cache()` is not available; caching must be done by converting to a Spark DataFrame first with `psdf.to_spark().cache()`

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: psdf.spark.explain() — viewing the physical plan

**Question**:
What does `psdf.spark.explain(extended=True)` produce?

- A) A pandas-style summary of the DataFrame's memory usage and column statistics
- B) The logical and physical execution plans of the underlying Spark DataFrame — it is equivalent to calling `psdf.to_spark().explain(extended=True)` and is useful for understanding how the Pandas API on Spark translates pandas-style operations into Spark execution plans
- C) A diff between the current state of the DataFrame and its initial state at creation, showing which operations have been applied
- D) `psdf.spark.explain()` is not a valid method; use `ps.get_option("compute.ops_on_diff_frames")` to inspect execution settings

---

### Question 98 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: default_index_type options and performance implications

**Question**:
Which `default_index_type` option in the Pandas API on Spark provides the best performance for large DataFrames, and what is its trade-off?

- A) `"sequence"` — it assigns a globally sorted sequential index starting at 0; it is the fastest option because no extra computation is needed
- B) `"distributed"` — it assigns a monotonically increasing but non-contiguous integer index using `monotonically_increasing_id()`; it is the fastest option because it requires no shuffle or global sort, but the index values are not guaranteed to be contiguous or start at 0; operations that require a stable contiguous index (like `iloc`) may produce unexpected results
- C) `"distributed-sequence"` — it assigns a perfectly contiguous 0-based index but requires a global sort or count pass, making it the slowest option for large DataFrames; the default is `"distributed-sequence"` because it matches pandas' behaviour most closely
- D) `"sequence"` and `"distributed-sequence"` are identical; `"distributed"` is the only type that avoids a shuffle

---

### Question 99 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: psdf.to_delta() convenience method

**Question**:
What does `psdf.to_delta("/mnt/output/my_table")` do?

- A) It registers the Pandas-on-Spark DataFrame as a Delta Live Table and starts an incremental pipeline
- B) It writes the Pandas-on-Spark DataFrame to the specified path in Delta Lake format — equivalent to `psdf.to_spark().write.format("delta").save("/mnt/output/my_table")`; it is a convenience method that simplifies saving to Delta without switching to the Spark DataFrame API
- C) It appends the DataFrame to an existing Delta table at the path; if the table does not exist it raises a `DeltaTableNotFoundException`
- D) `to_delta` is not available on Pandas-on-Spark DataFrames; use `psdf.spark.to_spark().write.format("delta").save(path)`

---

### Question 100 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: many
**Topic**: NULL vs NaN semantics in Pandas API on Spark

**Question**:
Which of the following statements correctly describe the difference between `NULL` and `NaN` semantics in the Pandas API on Spark? *(Select all that apply.)*

- A) In native pandas, `NaN` is used to represent missing values in numeric columns and is propagated by most operations; in Pandas API on Spark, missing values are represented as Spark `NULL` internally — `NaN` and `NULL` are distinct and have different behaviour in aggregations and filtering
- B) `psdf.isna()` returns `True` for both `NULL` and `NaN` values, matching pandas' convention where both are considered "missing"; however, `psdf.dropna()` drops rows containing `NULL` but by default does not drop rows containing `NaN` unless those `NaN` values are stored as Spark `NULL`
- C) `psdf.fillna(0)` fills `NULL` values with `0` but does not fill `NaN` values — `NaN` values in Spark float/double columns must be handled separately using `F.isnan()` checks or `df.replace(float('nan'), None)` before calling `fillna`
- D) Spark SQL aggregations such as `sum()` and `avg()` ignore `NULL` values; `NaN` values in double columns propagate through arithmetic aggregations — `sum([1.0, NaN, 2.0])` returns `NaN`, not `3.0`

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | `RDD.cache()` defaults to `MEMORY_ONLY`; `DataFrame.cache()` (and `Dataset.cache()`) defaults to `MEMORY_AND_DISK`. | topic1-prompt1-spark-architecture.md | Easy |
| 2 | B | FIFO gives all executor slots to the first-queued job before later jobs can start; FAIR distributes slots across concurrently running jobs. | topic1-prompt1-spark-architecture.md | Medium |
| 3 | B | `DAGScheduler` splits RDD lineage into stages and submits `TaskSet`s; `TaskScheduler` assigns tasks to executors based on locality and retries failures. | topic1-prompt1-spark-architecture.md | Medium |
| 4 | A, B, C, D | All four statements are correct: barrier tasks must start simultaneously; designed for MPI-style workloads; `BarrierContext` provides `allGather()`; entire stage resubmits on any task failure. | topic1-prompt1-spark-architecture.md | Hard |
| 5 | B | `spark.executor.pyspark.memory` controls off-heap Python worker memory per executor; if unset, the Python worker is unbounded and may cause the container to be killed by the cluster manager. | topic1-prompt1-spark-architecture.md | Medium |
| 6 | B | When `shuffleTracking.enabled=true`, Spark tracks executors holding needed shuffle data and only removes executors whose shuffle data is no longer required, making the external shuffle service optional for DRA. | topic1-prompt1-spark-architecture.md | Medium |
| 7 | B | `spark.eventLog.compress=true` enables compression of event log files; the codec is controlled by `spark.eventLog.compression.codec` (default `zstd`). | topic1-prompt1-spark-architecture.md | Medium |
| 8 | A, B, D | `coalesce` is a narrow transformation (A); if `n >= current count` it is a no-op (B); `coalesce(1)` is more efficient than `repartition(1)` for reducing to a single file (D). Option C is wrong — `coalesce` cannot increase partition count. | topic1-prompt3-lazy-evaluation.md | Easy |
| 9 | B | `spark.sql.execution.arrow.maxRecordsPerBatch` controls the maximum rows per Arrow record batch transferred between the JVM executor and Python worker for Pandas UDFs. | topic1-prompt1-spark-architecture.md | Medium |
| 10 | B | Non-equi joins use `BroadcastNestedLoopJoin` (if one side is broadcastable) or `CartesianProduct` with a filter — `SortMergeJoin` and `BroadcastHashJoin` require equal-key hashing. | topic1-prompt4-shuffling-performance.md | Hard |
| 11 | B | Cluster mode: `spark-submit` exits after job acceptance; driver runs on a cluster node. Client mode: the `spark-submit` process itself IS the driver and runs until completion. | topic1-prompt1-spark-architecture.md | Medium |
| 12 | A | Per-level overrides (`spark.locality.wait.process`, `.node`, `.rack`) independently override the global `spark.locality.wait` for each locality-level transition. | topic1-prompt1-spark-architecture.md | Medium |
| 13 | B | When `true`, Spark proactively replicates a lost cached block replica to another live executor, preventing a cache miss from forcing full recomputation. | topic1-prompt1-spark-architecture.md | Hard |
| 14 | B | When `numSlices` is provided, Spark creates exactly that many partitions; when omitted, `spark.default.parallelism` is the default. | topic1-prompt1-spark-architecture.md | Easy |
| 15 | A, B, C | `spark.driver.supervise` restarts the driver on non-zero exit (A); only effective in cluster deploy mode (B); supported in Standalone but not YARN or Kubernetes cluster mode (C). Option D is fabricated. | topic1-prompt1-spark-architecture.md | Medium |
| 16 | B | The two-level hash map uses a compact fixed-size Level 1 map for cache efficiency; overflow entries spill to a full-sized Level 2 map — reducing object allocation pressure. | topic1-prompt1-spark-architecture.md | Medium |
| 17 | B | The Worker daemon is a persistent JVM process that registers with the Master and launches separate Executor JVM processes per application; multiple Executors from different applications can coexist under one Worker. | topic1-prompt1-spark-architecture.md | Medium |
| 18 | B | `spark.rdd.compress=true` compresses serialized cached RDD partitions in memory, reducing cache footprint at the cost of CPU for compression/decompression. | topic1-prompt1-spark-architecture.md | Easy |
| 19 | B | Shuffle files on the lost executor's local disk are inaccessible; Spark must re-run the map tasks on surviving executors — unless an external shuffle service held the files independently. | topic1-prompt6-fault-tolerance.md | Hard |
| 20 | A, B, C | `spark.app.name` is a human-readable label (A); Application ID is accessible via `sparkContext.applicationId` (B); YARN IDs follow `application_<timestamp>_<seq>` format (C). Option D is false — shared app names do not share Application IDs. | topic1-prompt1-spark-architecture.md | Medium |
| 21 | B | `split_part('a:b:c:d', ':', 2)` returns `'b'` — splits by delimiter and returns the 1-based token at position 2. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 22 | B | `try_divide(10, 0)` returns `NULL` instead of raising an `ArithmeticException` when the divisor is zero. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 23 | B | `any_value(col IGNORE NULLS)` returns an arbitrary non-null value from the group; returns `NULL` only if every value is `NULL`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 24 | B | `make_date(2026, 12, 25)` constructs a `DateType` value representing December 25, 2026 from integer year, month, day components. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 25 | B | `regexp_like` returns `BooleanType` (true/false); `regexp_extract` returns `StringType` — the captured group text, or empty string if no match. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 26 | B | `width_bucket` returns a 1-based integer bucket number; values below `min` return `0` and values `>= max` return `num_buckets + 1`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 27 | B | `make_timestamp` returns `TimestampType`; `NULL` or out-of-range components cause the function to return `NULL` rather than raising an error. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 28 | B | `bool_and` and `bool_or` both ignore `NULL` values; they return `NULL` only when all group values are `NULL`. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 29 | A | `bit_or([5, 3, 8])` = 15 (binary: 0101 bitwise-OR 0011 bitwise-OR 1000 = 1111). | topic2-prompt9-builtin-sql-functions.md | Medium |
| 30 | A | `array_compact` removes all `NULL` elements from the array, preserving the order of remaining non-null elements. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 31 | B | `startswith` returns `BooleanType` — `true` if `str` begins with `prefix`; returns `NULL` if either argument is `NULL`. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 32 | B | `inline` is a table-generating function that explodes `ArrayType(StructType)` — one row per element with each struct field as a separate output column. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 33 | B | `named_struct('x', c1, 'y', c2)` allows explicit custom field names; `struct(c1, c2)` uses the input column names as field names. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 34 | B | `from_csv` parses a CSV-formatted string into a `StructType` column; unlike `from_json` it does not support nested objects or arrays since CSV is inherently flat. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 35 | B | `schema_of_csv` infers a schema from a sample CSV string and returns it as a DDL-formatted `StringType` value (e.g., `'_c0 STRING, _c1 INT'`) for use with `from_csv`. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 36 | B | `cardinality` returns `NULL` for a `NULL` column (SQL-standard); `size` returns `-1` for `NULL` by default (legacy behaviour controlled by `spark.sql.legacy.sizeOfNull`). | topic2-prompt9-builtin-sql-functions.md | Medium |
| 37 | B | `unix_date` returns an `IntegerType` count of days since the Unix epoch (`1970-01-01`), not seconds. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 38 | B | `date_from_unix_date` returns a `DateType` value for the date N days after the epoch; it is the inverse of `unix_date`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 39 | B | `try_add(Integer.MAX_VALUE, 1)` returns `NULL` on integer overflow instead of raising an `ArithmeticException`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 40 | B | `regexp_count` returns the count of non-overlapping occurrences; `'a.c'` matches `'abc'` twice in `'abcabc'`, returning `2`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 41 | B | `sampleBy` performs stratified sampling using the `fractions` dict per key value; rows whose key is not in the dict are excluded from the result. | topic3-prompt14-filtering-row-manipulation.md | Medium |
| 42 | B | `checkpoint(eager=True)` immediately triggers an action to materialise and write to the checkpoint directory; `eager=False` defers checkpointing until the next action on the returned DataFrame. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 43 | B | `F.product` computes the multiplication of all non-null values per group; `NULL` values are ignored — same convention as `sum()` and `avg()`. | topic3-prompt15-aggregations-grouping.md | Medium |
| 44 | A | `df.to(target_schema)` matches columns by name, casts types automatically, and raises `AnalysisException` for missing columns; `df.select` does not perform automatic type casting. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 45 | A | `transform_keys` applies the lambda to each map key; `F.upper(k)` converts keys to uppercase, producing `{"MATH": 90, "SCIENCE": 85}`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 46 | B | `transform_values` applies the lambda `(key, value) -> value * 2` to each entry, doubling every value and producing `{"apples": 10, "bananas": 6}`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 47 | B | `Column.withField(fieldName, col)` adds or replaces a named field within a `StructType` column without reconstructing the entire struct (Spark 3.1+). | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 48 | A | `Column.dropFields(*fieldNames)` removes the named fields from a `StructType` column, returning a struct with only the remaining fields (Spark 3.1+). | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 49 | A | `df.tail(5)` returns the last 5 rows as a Python list of `Row` objects; `df.limit(5).collect()` returns the first 5 rows. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 50 | B | `array_insert(arr, 2, "new")` inserts `"new"` at 1-based position 2, shifting later elements right, producing `["a", "new", "b", "c"]`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 51 | B | `F.aggregate` performs a fold: starting at `0`, `((((0+1)+2)+3)+4) = 10`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 52 | B | `zip_with` applies the merge function element-wise: `10+1=11`, `20+2=22`, `30+3=33`, producing `[11, 22, 33]`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 53 | B | `forall` returns `true` only when every element satisfies the predicate; `55 < 60` means not all elements pass, so it returns `false`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 54 | A | `flatten` concatenates all inner arrays into a single-level array: `[[1,2],[3,4],[5]]` becomes `[1,2,3,4,5]`. | topic3-prompt13-column-manipulation-expressions.md | Easy |
| 55 | B | `createOrReplace()` atomically drops and recreates the table, replacing all data; `append()` adds rows without removing prior data. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 56 | B | `try_element_at` returns `NULL` when the 1-based index is out of range (or key absent in a map), instead of throwing an exception. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 57 | B | `array_remove` removes all occurrences of the specified value; `array_distinct` removes duplicate elements keeping the first occurrence of each distinct value — they solve different problems. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 58 | A | `F.date_diff` (Spark 3.5+ snake_case) and `F.datediff` (older camelCase) are functionally identical aliases — both return `IntegerType` days from start to end. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 59 | B | `crossJoin` produces the Cartesian product — every row of `df1` paired with every row of `df2`; `spark.sql.crossJoin.enabled=true` must be set. | topic3-prompt17-joins.md | Easy |
| 60 | A, B, C | Spark creates a `year=val/month=val/` directory hierarchy (A); partition columns are excluded from data files (B); output files per partition directory equals the DataFrame partitions with that key combination (C). Option D is wrong — no automatic coalesce to one file. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 61 | B | `bucketBy` only works with `saveAsTable` (bucketing metadata requires a named table in the metastore); `sortBy` is optional and sorts within each bucket file. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 62 | B | `current_timestamp()` is non-deterministic and evaluated at the start of each query execution; two `show()` calls may produce different timestamps, but all rows within a single execution share the same value. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 63 | B | `df.dtypes` returns a Python list of `(column_name, type_string)` tuples — one tuple per column. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 64 | A | `overlay("abcdef", "XY", 3, 2)` replaces 2 characters starting at position 3 (`'cd'`) with `'XY'`, returning `"abXYef"`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 65 | D | `"deflate"` is not a valid Parquet compression codec in Spark; valid values include `snappy`, `gzip`, `brotli`, `lz4`, `zstd`, and `uncompressed`. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 66 | B | `map_zip_with` merges two maps by applying `merge_func(key, v1, v2)` per key: `1.5-0.1=1.4`, `0.8-0.05=0.75`, giving `{"apple": 1.4, "banana": 0.75}`. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 67 | A | `df.inputFiles()` returns a Python list of absolute paths of all input files contributing data to the DataFrame. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 68 | B | `F.median` is shorthand for `F.percentile_approx(col, 0.5)` using the same Greenwald-Khanna sketch; `F.percentile` is exact but available only in Spark SQL. | topic3-prompt15-aggregations-grouping.md | Medium |
| 69 | A, B, D | `observe()` attaches aggregates computed during the action without a separate query (A); `Observation.get` blocks until the associated action completes (B); multiple `observe()` calls can be chained for different metrics in one pass (D). Option C is wrong — `observe()` works with batch DataFrames too. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 70 | C | With `spark.sql.ansi.enabled=true`, `CAST('abc' AS INT)` raises a `SparkNumberFormatException` instead of silently returning `NULL`. | topic2-prompt8-spark-sql-fundamentals.md | Hard |
| 71 | B | AQE detects oversized shuffle partitions, splits the skewed side into sub-partitions, and replicates the matching non-skewed partition for each sub-partition pair — reducing max task duration without reshuffling the full dataset. | topic4-prompt24-performance-tuning.md | Medium |
| 72 | B | `selfDestruct.enabled=true` releases each Java Arrow buffer immediately after copying into the pandas DataFrame, reducing peak JVM heap usage during `toPandas()`. | topic4-prompt24-performance-tuning.md | Medium |
| 73 | A, B, C | Whole-stage codegen fuses operators into a single compiled Java method (A); it is auto-disabled for operators exceeding `maxFields` (100) columns (B); disabling it is a valid debugging technique for isolating codegen errors (C). Option D is wrong — Tungsten binary format is unaffected. | topic4-prompt24-performance-tuning.md | Hard |
| 74 | A | `openCostInBytes` adds a virtual per-file padding cost so many small files are merged into the same partition when their total padded size stays under `maxPartitionBytes`. | topic4-prompt24-performance-tuning.md | Medium |
| 75 | B | When `parallelismFirst=true` (default), AQE prioritises `advisoryPartitionSizeInBytes` and ignores `minPartitionNum`; when `false`, `minPartitionNum` is respected as a lower bound on merged partition count. | topic4-prompt24-performance-tuning.md | Hard |
| 76 | B | By default (`ignoreMissingFiles=false`) Spark raises `FileNotFoundException` for files deleted between planning and execution; setting `true` causes Spark to skip missing files and return only successfully read data. | topic4-prompt25-common-errors.md | Medium |
| 77 | B | `spark.shuffle.file.buffer` sets the write buffer size per shuffle output stream; increasing it reduces system call frequency at the cost of additional executor heap per concurrent write stream. | topic4-prompt24-performance-tuning.md | Medium |
| 78 | A, B, D | Off-heap memory is outside the JVM heap and not GC'd (A); `offHeap.size` is per-executor and additional to `spark.executor.memory` (B); it is used by Tungsten and when `OFF_HEAP` storage level is explicitly specified (D). Option C is wrong — on-heap caching is not disabled automatically. | topic4-prompt24-performance-tuning.md | Hard |
| 79 | C | `sortBeforeRepartition=true` sorts records within each map-side partition by hash value before the repartition shuffle write, improving sequential disk access at the cost of pre-sort CPU overhead. | topic4-prompt24-performance-tuning.md | Medium |
| 80 | B | With `registrationRequired=true`, Spark raises a `KryoException` for any unregistered class, causing the job to fail — all serialised classes must be explicitly registered. | topic4-prompt25-common-errors.md | Medium |
| 81 | B | `trigger(once=True)` processes all available data in a single micro-batch then stops; `trigger(availableNow=True)` (Spark 3.3+) uses multiple micro-batches respecting rate limits, then stops. | topic5-prompt27-structured-streaming.md | Easy |
| 82 | B | `inputRowsPerSecond` is the source arrival rate from source metadata; `processedRowsPerSecond` is the actual query processing rate in the last micro-batch — source rate may exceed processing rate if the query falls behind. | topic5-prompt27-structured-streaming.md | Medium |
| 83 | B | Streaming file sources raise `AnalysisException` if no schema is specified — unlike batch reads, schema inference is not permitted for streaming sources. | topic5-prompt27-structured-streaming.md | Medium |
| 84 | B | Console sink prints each micro-batch's output to driver stdout; for debugging only — not fault-tolerant and not suitable for production use. | topic5-prompt27-structured-streaming.md | Easy |
| 85 | B | With watermark at 12:08 and delay 10 min, the drop threshold is `12:08 - 10 min = 11:58`; since `event_time 12:03 > 11:58`, the event is NOT dropped and is appended to output. | topic5-prompt28-stateful-streaming.md | Hard |
| 86 | B | `session_window` creates a gap-based dynamic window: a session extends as long as events arrive within the gap duration; it closes when no event arrives within the gap, yielding variable-duration sessions. | topic5-prompt28-stateful-streaming.md | Medium |
| 87 | B | The Kafka source always produces a fixed schema: `key BinaryType`, `value BinaryType`, `topic StringType`, `partition IntegerType`, `offset LongType`, `timestamp TimestampType`, `timestampType IntegerType`. | topic5-prompt27-structured-streaming.md | Medium |
| 88 | B | A fixed `kafka.group.id` causes Kafka brokers to track offsets per consumer group; multiple Spark queries sharing the same group ID interfere with each other's offset tracking; Spark recommends managing offsets internally via the checkpoint directory. | topic5-prompt27-structured-streaming.md | Medium |
| 89 | B | `maxOffsetsPerTrigger` caps the total Kafka offsets read across all partitions per micro-batch trigger, preventing oversized batches during backlog bursts. | topic5-prompt27-structured-streaming.md | Medium |
| 90 | B | `mapGroupsWithState` emits exactly one output row per group per trigger; `flatMapGroupsWithState` can emit zero or more rows per group per trigger via an `Iterator`. | topic5-prompt28-stateful-streaming.md | Hard |
| 91 | B | In Spark Connect, `AnalysisException` surfaces when an action is triggered — the logical plan is sent to the server only at action time, where server-side analysis is performed. | topic6-prompt29-spark-connect.md | Medium |
| 92 | B | Authentication token is passed as `;token=<value>` in the `sc://` URL, e.g., `sc://hostname:15002/;token=mySecretToken`; the token is forwarded as a gRPC header on every request. | topic6-prompt29-spark-connect.md | Easy |
| 93 | B | Spark Connect replaces Py4J with a gRPC stub that sends serialised logical plans over the network; the JVM runs only on the remote Spark Connect server, eliminating the need for a local JVM on the client. | topic6-prompt29-spark-connect.md | Medium |
| 94 | B | Python UDFs are pickled on the client and sent to the Spark Connect server as part of the gRPC plan; the server deserialises and executes them in a Python worker process on the executor. | topic6-prompt29-spark-connect.md | Medium |
| 95 | B | Running queries are lost when the server crashes; the client Python process survives (unlike classic PySpark where a JVM crash kills the driver), allowing reconnection and query resubmission. | topic6-prompt29-spark-connect.md | Medium |
| 96 | B | `psdf.spark.cache()` caches the underlying Spark DataFrame using `MEMORY_AND_DISK` storage level; subsequent actions read from cache rather than recomputing from the source. | topic7-prompt30-pandas-api.md | Easy |
| 97 | B | `psdf.spark.explain(extended=True)` shows the logical and physical execution plans — equivalent to `psdf.to_spark().explain(extended=True)`. | topic7-prompt30-pandas-api.md | Medium |
| 98 | B | `"distributed"` index type uses `monotonically_increasing_id()` — fastest option requiring no shuffle or global sort, but index values are non-contiguous and may not start at 0. | topic7-prompt30-pandas-api.md | Medium |
| 99 | B | `psdf.to_delta(path)` writes the Pandas-on-Spark DataFrame to Delta Lake format — equivalent to `psdf.to_spark().write.format("delta").save(path)`. | topic7-prompt30-pandas-api.md | Medium |
| 100 | A, C, D | `NaN` and `NULL` are distinct with different aggregation behaviour (A); `fillna(0)` fills `NULL` but not `NaN` in float columns — `NaN` must be handled separately via `F.isnan()` or `replace` (C); `NaN` propagates through arithmetic aggregations while `NULL` is ignored (D). Option B is incorrect regarding `dropna` behaviour. | topic7-prompt30-pandas-api.md | Hard |
