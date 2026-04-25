# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 8)

**Iteration**: 8

**Generated**: 2026-04-25

**Total Questions**: 100

**Difficulty Split**: 9 Easy / 55 Medium / 36 Hard

**Answer Types**: 100 `one`

---

## Questions

---

### Question 1 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark execution model — jobs, stages, and tasks relationship

When a Spark action is invoked, the DAGScheduler breaks the work into stages, and each stage is divided into tasks. What determines the number of tasks in a given stage?

- A) The number of executor cores requested via `--executor-cores`
- B) The number of partitions in the RDD or DataFrame at that stage boundary — each partition produces exactly one task
- C) The value of `spark.sql.shuffle.partitions` always, regardless of the input
- D) The number of distinct keys in the dataset when a `groupBy` is present

---

### Question 2 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark memory management — storage vs execution memory borrowing under the unified model

Under the Unified Memory Model, storage memory and execution memory share a single region controlled by `spark.memory.fraction` (default 0.6). What happens when execution memory needs more space but the storage region is occupied by cached data?

- A) Execution memory can never borrow from storage; both regions are strictly isolated
- B) Spark evicts as many cached blocks as necessary to satisfy the execution request — cached data that does not fit is either written to disk (if the storage level permits) or dropped and must be recomputed on next access; this makes execution memory effectively dominant over storage memory under pressure
- C) Spark pauses the task and waits until cached blocks are manually unpersisted by the application
- D) The execution memory request is denied and the task fails with an `OutOfMemoryError` immediately

---

### Question 3 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: shuffle write — sort-based shuffle and intermediate file layout

In Spark's default sort-based shuffle, what files are written per mapper task, and what is the significance of the index file?

- A) Each mapper writes one file per reducer partition, resulting in M × R files total; no index file is used
- B) Each mapper writes exactly **one data file** (all reducer partition data concatenated in sorted order) and **one index file** (containing byte offsets for each reducer partition's segment within the data file); the index file allows each reducer to seek directly to its slice of the data file without reading unrelated bytes — this design limits the total file count to `2 × M` instead of `M × R`
- C) Each mapper writes one file per node; the index file records which nodes hold which partitions
- D) Each mapper writes one data file and one checksum file; the index file is only created when `spark.shuffle.sort.bypassMergeThreshold` is exceeded

---

### Question 4 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: TaskContext — task-level metadata accessible inside a transformation

Which of the following correctly retrieves the **partition index** and **attempt number** of the current task from within a `mapPartitionsWithIndex` call?

- A) Use `SparkContext.getOrCreate().taskMetrics()` to access current task metadata
- B) Call `TaskContext.get()` inside the partition function — `TaskContext.get().partitionId()` returns the zero-based partition index and `TaskContext.get().attemptNumber()` returns the task attempt count (0 for the first attempt, 1 for the first retry, etc.); the `TaskContext` is thread-local and only valid during task execution — it returns `null` on the driver
- C) Task metadata is only available through the Spark web UI; there is no programmatic in-task API
- D) Use `pyspark.TaskContext.partitionId()` as a module-level function without calling `get()` first

---

### Question 5 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: barrier execution mode — spark.barrier() and barrier tasks

What is Spark's barrier execution mode, and in what scenario is it required?

- A) Barrier mode forces all tasks in a stage to run on the same executor, eliminating shuffle overhead
- B) Barrier mode (enabled by wrapping an RDD with `rdd.barrier().mapPartitions(...)`) ensures that **all tasks in the stage start simultaneously and the stage fails as a whole if any single task fails** — unlike normal Spark tasks that can be independently retried; this is required for deep-learning training frameworks (e.g., PyTorch `DistributedDataParallel`, Horovod) where all workers must initialize together, exchange addresses (e.g., via `BarrierTaskContext.barrier()` followed by `allGather()`), and run in lock-step; if one task dies, the entire stage is re-launched
- C) Barrier mode prevents the Spark driver from scheduling new jobs until all existing jobs are complete, avoiding resource contention
- D) Barrier mode is an alias for `spark.scheduler.mode=FAIR`, ensuring equal resource allocation across all concurrent jobs

---

### Question 6 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark resource scheduling — FAIR vs FIFO scheduler within a single application

By default, Spark runs jobs submitted within a single application using which scheduler, and how does the FAIR scheduler change this behavior?

- A) The default is FAIR; FIFO must be explicitly configured to serialize job execution
- B) The default is **FIFO** — jobs are queued and run one at a time in submission order, so the first job monopolizes all cluster resources until it completes; configuring `spark.scheduler.mode=FAIR` enables the Fair Scheduler, which interleaves tasks from multiple concurrent jobs across executors so that short jobs can complete without waiting for long-running jobs; pools with weights and `minShare` can be configured to give certain job groups priority; this is most useful in multi-tenant interactive applications or notebooks that submit multiple parallel jobs
- C) FIFO and FAIR produce identical behavior for single-threaded applications; the difference only matters when using more than 100 executors
- D) FAIR scheduling is the default in Databricks Runtime; FIFO is only the default in local mode

---

### Question 7 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Python worker lifecycle — process forking and PySpark worker reuse

How does PySpark manage Python worker processes, and what does `spark.python.worker.reuse` control?

- A) PySpark starts a single Python process shared by all executors on a node; `spark.python.worker.reuse` controls whether this global process is restarted between applications
- B) For each JVM executor, PySpark forks a Python worker process per task by default — when `spark.python.worker.reuse=true` (the default), the worker process is **reused across tasks on the same executor** (the worker calls `recv_task()` in a loop), avoiding repeated Python interpreter startup and module import overhead; when `false`, a new Python process is spawned for every task; reuse is beneficial for tasks with heavy imports (pandas, NumPy) but means any global state mutations persist across tasks in the same worker — use `false` if your code relies on a clean interpreter state
- C) `spark.python.worker.reuse` controls whether Python UDFs are cached in the JVM to avoid serialization on re-use
- D) PySpark creates one Python worker per partition at job start; `spark.python.worker.reuse` has no effect on performance

---

### Question 8 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: RDD lineage and checkpointing — when to use checkpoint vs persist

When should `rdd.checkpoint()` be preferred over `rdd.persist(StorageLevel.DISK_ONLY)`, and what is the key operational difference?

- A) They are equivalent; `checkpoint()` is just a convenience alias for disk-only persistence
- B) `rdd.checkpoint()` **truncates the RDD lineage** — after checkpointing, Spark treats the checkpointed data as a new base RDD with no parent dependencies; the checkpoint is written to a fault-tolerant filesystem (configured via `sc.setCheckpointDir()`); this is valuable for very long lineage chains (iterative algorithms like PageRank) where re-computing from scratch after a failure would be prohibitively expensive; `rdd.persist(DISK_ONLY)` caches to the local executor disk but **retains the lineage** — if the cached data is lost (executor dies), Spark recomputes from the original source; checkpoint guarantees durability at the cost of an extra write pass
- C) `checkpoint()` stores data in the driver's memory; `persist(DISK_ONLY)` stores data on executor local disks
- D) `checkpoint()` is only available for streaming RDDs; batch RDDs must use `persist()` for fault tolerance

---

### Question 9 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.locality.wait — data locality degradation strategy

How does Spark's data locality degradation work, and what is the role of `spark.locality.wait`?

- A) Spark always schedules tasks on the node that holds the data; locality cannot be traded off
- B) Spark prefers to run tasks on the node (or rack) that holds the required input partition to minimise data transfer; when the preferred node is busy, Spark **waits** `spark.locality.wait` milliseconds (default `3s`) before relaxing to the next locality level: `PROCESS_LOCAL` → `NODE_LOCAL` → `RACK_LOCAL` → `ANY`; individual thresholds can be overridden with `spark.locality.wait.process`, `spark.locality.wait.node`, and `spark.locality.wait.rack`; increasing the wait reduces network I/O but may leave executor cores idle; reducing it gets tasks running sooner at the cost of extra data transfer
- C) `spark.locality.wait` is the timeout before Spark marks a task as failed due to data unavailability
- D) Data locality only applies to HDFS sources; for S3 and GCS, `spark.locality.wait` has no effect and is ignored

---

### Question 10 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Tungsten — off-heap binary format and unsafe row representation

What is the UnsafeRow format used by Tungsten, and what performance advantage does it provide over Java object representation?

- A) UnsafeRow stores row data as standard Java `Object[]` arrays; performance gains come from JIT compilation of generated code
- B) `UnsafeRow` is a **binary, off-heap (or on-heap) row format** that stores a row's fields as a compact sequence of bytes with a fixed-width null bitset at the start and variable-length fields appended after the fixed section; because data is stored in raw binary rather than as boxed Java objects, rows do not generate garbage — GC pressure is dramatically reduced; fields are accessed via `getLong()`, `getInt()`, `getUTF8String()` etc. with direct memory reads, and row data can be compared, sorted, and hashed without deserialization; this is the internal representation used in SQL physical operators, sort-merge join, and aggregate hash maps
- C) UnsafeRow serializes rows as JSON strings in off-heap memory; this reduces JVM object count but increases CPU usage for parsing
- D) UnsafeRow is only used when `spark.sql.inMemoryColumnarStorage.enableVectorizedReader=true`

---

### Question 11 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.executor.instances vs dynamic allocation — interaction and precedence

What happens when both `spark.executor.instances=10` and `spark.dynamicAllocation.enabled=true` are set in the same Spark configuration?

- A) Both settings are applied; dynamic allocation adjusts between 0 and 10 executors
- B) `spark.executor.instances` is **ignored** when dynamic allocation is enabled — dynamic allocation manages executor count based on task queue depth; setting a fixed count conflicts with dynamic scaling and Spark logs a warning; to set an upper bound use `spark.dynamicAllocation.maxExecutors`, and a lower bound with `spark.dynamicAllocation.minExecutors`; the only exception is when `spark.dynamicAllocation.enabled=false`, in which case `spark.executor.instances` takes effect
- C) Dynamic allocation is automatically disabled when `spark.executor.instances` is explicitly set to any positive value
- D) `spark.executor.instances` sets the initial executor count; dynamic allocation then scales up or down from that starting point

---

### Question 12 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.files.maxPartitionBytes — controlling input partition size

What does `spark.sql.files.maxPartitionBytes` (default 128 MB) control, and when would you reduce it?

- A) It sets the maximum size of Parquet row groups; reducing it makes each row group smaller
- B) It caps the **maximum amount of data Spark reads into a single input partition** when scanning files (Parquet, ORC, CSV, etc.) — Spark splits large files into 128 MB chunks so that each task reads at most this much data; the actual number of partitions is `ceil(total_bytes / maxPartitionBytes)` (subject to `spark.sql.files.openCostInBytes` overhead); reducing this value increases partition count (more parallelism) and is useful when tasks are slow because they each process too much data or when memory per executor is limited; increasing it reduces the number of tasks and is useful for many small files
- C) It limits the maximum size of a single Parquet file written during `df.write`; files exceeding this limit are silently split
- D) It controls the shuffle buffer size for wide transformations, capping the amount of data buffered per reducer partition

---

### Question 13 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Accumulator semantics — idempotency and double-counting risk

An accumulator is updated inside a `df.map()` transformation. The job is run once. In what scenario could the accumulator count be **higher than expected**?

- A) Accumulators always produce exact counts; double-counting cannot occur in Spark
- B) If a task fails and is **retried**, the retry re-executes the transformation and updates the accumulator again — the failed attempt's increment is not rolled back; similarly, if a cached DataFrame's partition is evicted and the transformation is re-executed for a downstream action, accumulators may be incremented more than once; accumulators are therefore only guaranteed to be accurate when computed inside an **action** (not in a transformation); for exact counting, consider using aggregation functions instead
- C) Accumulators only count once per executor regardless of how many tasks run on that executor
- D) The accumulator will always be zero if updated inside a `map()` rather than a `foreach()`

---

### Question 14 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.broadcastTimeout — broadcast join timeout configuration

What does `spark.sql.broadcastTimeout` (default 300 seconds) control, and what symptom appears when it is exceeded?

- A) It limits how long the driver waits for executors to acknowledge receipt of all broadcast variables; a timeout causes the entire application to abort
- B) It sets the maximum time the **driver waits to complete the broadcast of a relation** to all executors during a broadcast hash join; if the broadcast (serialization + transport) takes longer than this threshold, the query fails with a `SparkException: Could not execute broadcast in X secs`; increasing this value is necessary for very large broadcast tables (close to `spark.sql.autoBroadcastJoinThreshold`) on slow networks, or for tables with complex nested schemas that take time to serialize
- C) `spark.sql.broadcastTimeout` is the maximum time a task waits for its local broadcast variable copy; exceeding it falls back to a sort-merge join automatically
- D) This setting controls the Py4J RPC timeout for serializing DataFrames from Python to the JVM; exceeding it causes a `Py4JJavaError`

---

### Question 15 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.submit.deployMode vs --deploy-mode — precedence rules

A developer sets `spark.submit.deployMode=client` in `spark-defaults.conf` but passes `--deploy-mode cluster` on the `spark-submit` command line. Which deploy mode is used?

- A) `spark-defaults.conf` always takes precedence over command-line flags
- B) Command-line flags passed directly to `spark-submit` **override** values in `spark-defaults.conf` — the precedence order from lowest to highest is: `spark-defaults.conf` → `SparkConf` set programmatically inside the application → `spark-submit` command-line flags; so `--deploy-mode cluster` wins, and the driver runs on a cluster worker node
- C) A conflict between `spark-defaults.conf` and the command line causes `spark-submit` to fail with a configuration error
- D) The deploy mode is always determined by the cluster manager; user configuration is advisory only

---

### Question 16 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.rdd.compress — RDD serialization compression toggle

What does `spark.rdd.compress` (default `false`) control, and what is the trade-off when enabling it?

- A) It enables columnar compression for in-memory cached DataFrames; enabling it reduces memory usage at no CPU cost
- B) When `spark.rdd.compress=true`, serialized RDD partitions stored in memory (e.g., with `MEMORY_ONLY_SER`) or on disk are **compressed** using the codec specified by `spark.io.compression.codec`; this reduces the memory or disk footprint of cached data at the cost of additional CPU cycles for compression and decompression on each read; it is most beneficial when memory is the bottleneck and CPUs are underutilized; for columnar DataFrames use `spark.sql.inMemoryColumnarStorage.compressed` instead
- C) `spark.rdd.compress` controls whether Spark compresses shuffle output files; it is independent of in-memory storage
- D) This setting only applies to RDDs with more than 1 million rows; smaller RDDs are never compressed regardless of the setting

---

### Question 17 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.driver.maxResultSize — limiting action result size

What does `spark.driver.maxResultSize` (default 1 GB) guard against, and what exception is thrown when exceeded?

- A) It limits the size of a single Parquet file written by the driver; exceeding it causes a silent truncation
- B) `spark.driver.maxResultSize` caps the **total size of serialized results** that can be collected to the driver from all tasks in a single action (e.g., `collect()`, `take()`); if the combined result exceeds this threshold, Spark throws `org.apache.spark.SparkException: Job aborted due to stage failure: Total size of serialized results of N tasks ... is bigger than spark.driver.maxResultSize`; setting it to `0` disables the limit; this is a safety guard against driver OOM from accidental large collects
- C) It controls the maximum memory the driver can allocate for SQL result sets; SQL queries automatically paginate results that exceed this size
- D) `maxResultSize` is the maximum size of a DataFrame schema string; exceeding it causes schema inference to fall back to `StringType` for all columns

---

### Question 18 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SparkContext.addFile() vs sc.addJar() — distributing files vs JARs

What is the difference between `sc.addFile(path)` and `sc.addJar(path)`, and how do workers access files added via `addFile()`?

- A) `addFile` distributes files to S3; `addJar` distributes JARs to HDFS; workers access both via shared storage
- B) `sc.addFile(path)` distributes an arbitrary file (e.g., a config file, model weights, shell script) to the working directory of **every executor**; workers retrieve the local path using `SparkFiles.get("filename")`; `sc.addJar(path)` distributes a JAR and **adds it to the executor classpath**, making its classes available to tasks without `import` issues; both methods propagate files to all executors both currently running and those added later (when dynamic allocation is active)
- C) `addFile` and `addJar` are identical; the distinction is only cosmetic for documentation purposes
- D) `addFile` only works with local filesystem paths; `addJar` supports HDFS, S3, and local paths

---

### Question 19 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.execution.arrow.pyspark.enabled — Arrow optimization for toPandas/createDataFrame

When `spark.sql.execution.arrow.pyspark.enabled=true`, what performance improvement is achieved and what constraint exists?

- A) Arrow mode pre-fetches Parquet columns in parallel, reducing I/O for `df.read.parquet()` calls
- B) With Arrow enabled, `df.toPandas()` and `spark.createDataFrame(pandas_df)` use **Apache Arrow's columnar IPC format** to batch-transfer data between the JVM and Python processes, bypassing row-by-row serialization via Py4J; this can be 10–100× faster for large DataFrames; the constraint is that **all columns must have Arrow-compatible types** — `MapType`, `ArrayType` of complex types, and some date/decimal variations may fall back to the slower Py4J path (with a warning); structurally incompatible schemas cause the conversion to fall back or raise an error depending on `spark.sql.execution.arrow.pyspark.fallback.enabled`
- C) Arrow mode is only used for streaming DataFrames; batch DataFrames always use Java serialization
- D) Arrow converts DataFrames to Pandas format on the executor before returning results, allowing Python transformations to run distributed across executors

---

### Question 20 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.ui.retainedJobs / retainedStages — history retained in the live UI

What do `spark.ui.retainedJobs` (default 1000) and `spark.ui.retainedStages` (default 1000) control?

- A) They set the maximum number of jobs and stages that can run concurrently in a single Spark application
- B) These settings control how many completed **job and stage summaries** are kept in the driver's in-memory Web UI cache; once the limit is reached, the oldest entries are evicted (FIFO); this prevents the driver from accumulating unbounded memory for UI metadata in long-running applications (e.g., Spark Streaming, notebooks); increasing these values gives longer history in the live UI but uses more driver heap; the Spark History Server uses separate `spark.history.ui.maxApplications` and related settings for persisted event logs
- C) They control the retention policy of the Spark event log files written to `spark.eventLog.dir`
- D) `retainedJobs` limits the number of jobs that can be submitted before the driver rejects further submissions

---

### Question 21 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: regexp_replace() — pattern-based string replacement

What does `regexp_replace(col, pattern, replacement)` return when no part of the string matches the pattern?

- A) It returns `null`
- B) It returns the **original string unchanged** — `regexp_replace` replaces all non-overlapping occurrences of `pattern` in the string with `replacement`; if the pattern does not match anywhere, the original string is returned as-is; the function is null-safe: if the input column is `null`, the result is `null`
- C) It returns an empty string `""`
- D) It raises a `PatternSyntaxException` at runtime if the pattern doesn't match any row

---

### Question 22 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: overlay() function — in-place string replacement by position

What does `overlay(str, replace, pos, len)` do, and how does it differ from `regexp_replace()`?

- A) `overlay` is identical to `regexp_replace`; `pos` and `len` are ignored
- B) `overlay(str, replace, pos, len)` **replaces a substring by position** — starting at 1-based character position `pos` for `len` characters — with the `replace` string; unlike `regexp_replace`, no regex is involved; the result length may differ from the input if `len` differs from `length(replace)`; `overlay(str, replace, pos)` with no `len` argument defaults to `length(replace)` characters replaced; it returns `null` if any argument is `null`
- C) `overlay()` only works on binary columns; for string columns `insert()` must be used
- D) `overlay()` appends `replace` at position `pos` without removing existing characters; `len` is the number of characters to skip before appending

---

### Question 23 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: soundex() — phonetic encoding for fuzzy name matching

What does the `soundex(str)` function return, and in what use case is it applied?

- A) It returns a normalized lowercase version of the string with punctuation removed
- B) `soundex(str)` returns the **Soundex code** for the string — a 4-character phonetic code (e.g., `'Robert'` → `'R163'`) where the first character is the first letter of the input and the remaining three digits encode consonant sounds, grouping similar-sounding letters; it is used for **fuzzy matching of names** where spelling may vary (e.g., joining customer records where the same person's name is spelled differently) — `WHERE soundex(name1) = soundex(name2)` finds phonetically similar names; it returns `null` for `null` input and an empty string for empty input
- C) `soundex()` computes an MD5-like hash of the string for deduplication purposes; phonetic similarity is not considered
- D) `soundex()` breaks the string into phonemes and returns an `ArrayType(StringType)` of individual sounds

---

### Question 24 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: initcap() vs upper() vs lower() — string case conversion functions

How does `initcap(str)` differ from `upper(str)` and `lower(str)`?

- A) `initcap` and `upper` are equivalent; `lower` returns the reverse of `upper`
- B) `upper(str)` converts every character to uppercase; `lower(str)` converts every character to lowercase; `initcap(str)` converts the **first letter of each word to uppercase and the remaining letters to lowercase** (title case) — words are delimited by any non-alphabetic character; for example, `initcap('hello world')` → `'Hello World'`; all three functions return `null` when the input is `null`
- C) `initcap` is an aggregate function that returns the most frequent capitalization pattern across all rows; `upper` and `lower` are scalar functions
- D) `initcap` only capitalizes the very first character of the entire string; subsequent words are unaffected

---

### Question 25 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: array_distinct() — deduplication of array elements

What does `array_distinct(array_col)` return, and how does it handle `null` elements within the array?

- A) It returns the array sorted in ascending order with all `null` values removed
- B) `array_distinct(array_col)` returns a **new array containing only the unique elements** of the input array, preserving the first occurrence order; `null` elements are treated as a distinct value — if the array contains one or more `null` values, the result contains exactly one `null` at the position of the first occurrence; if the column itself is `null` (not an array containing `null`), the function returns `null`
- C) `array_distinct` removes all `null` elements from the array but keeps duplicates, equivalent to `filter(array, x -> x is not null)`
- D) `array_distinct` returns a `SetType` (unordered collection); use `sort_array()` after to get a deterministic order

---

### Question 26 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: array_union() / array_intersect() / array_except() — set operations on array columns

What is the difference between `array_union(a1, a2)`, `array_intersect(a1, a2)`, and `array_except(a1, a2)`?

- A) They are SQL aliases for the relational set operations `UNION`, `INTERSECT`, and `EXCEPT` applied to full DataFrames
- B) All three operate element-wise on array columns: `array_union(a1, a2)` returns the distinct elements from both arrays combined (duplicates removed); `array_intersect(a1, a2)` returns distinct elements that appear in **both** arrays; `array_except(a1, a2)` returns distinct elements from `a1` that are **not** present in `a2`; all three preserve uniqueness within the result (duplicate elements are deduplicated); `null` elements are treated as equal for comparison purposes; all return `null` if either input is `null`
- C) `array_union` concatenates two arrays including duplicates; `array_intersect` finds the common prefix; `array_except` removes the last element of `a1`
- D) These functions only operate on `ArrayType(IntegerType)` columns; string arrays must use `concat_ws` and `split` instead

---

### Question 27 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: aggregate() higher-order function — fold / reduce over an array

What does `aggregate(array, initialValue, merge, finish)` compute, and when is the `finish` argument used?

- A) `aggregate` is an alias for `reduce()` and always returns the last element of the array; the `finish` and `initialValue` parameters are ignored
- B) `aggregate(array, zero, merge)` is a **fold-left** operation — it starts with `zero` as the accumulator and applies `merge(accumulator, element)` for each element in the array left-to-right, returning the final accumulator value; the optional fourth argument `finish(accumulator)` is a **post-processing function** applied once to the final accumulator before returning — useful for computing averages (accumulate `(sum, count)` as a struct, then divide in `finish`); if `array` is `null`, the result is `null`; if any element is `null`, the element is passed as-is to `merge`
- C) `aggregate` applies `merge` in parallel across all array elements and combines results; the order of evaluation is non-deterministic
- D) `aggregate` computes a running total and returns an array of cumulative values; it does not return a scalar

---

### Question 28 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: forall() / exists() — boolean short-circuit higher-order functions

What do `forall(array, predicate)` and `exists(array, predicate)` return, and how do they handle empty arrays?

- A) `forall` and `exists` both return `null` for empty arrays
- B) `forall(array, pred)` returns `true` if `pred(element)` is `true` for **every** element in the array (vacuously `true` for an empty array); `exists(array, pred)` returns `true` if `pred(element)` is `true` for **at least one** element (`false` for an empty array); both short-circuit: `forall` stops at the first `false`, `exists` stops at the first `true`; both return `null` if the array column is `null`; `null` elements passed to the predicate propagate as `null` within the predicate expression
- C) `forall` returns the count of elements that match; `exists` returns the first matching element
- D) Empty arrays cause both functions to throw an `ArrayIndexOutOfBoundsException`

---

### Question 29 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: zip_with() higher-order function — element-wise merge of two arrays

What does `zip_with(array1, array2, func)` return when the two arrays have different lengths?

- A) It raises an `AnalysisException` at query planning time if the two arrays may have different lengths
- B) `zip_with(a1, a2, func)` applies `func(element_from_a1, element_from_a2)` element-wise and returns a result array with length equal to the **longer** array; elements of the shorter array beyond its end are supplied as `null` to the function — `func` must therefore handle `null` gracefully; this is analogous to Python's `zip_longest`; if either array is `null`, the result is `null`
- C) `zip_with` truncates both arrays to the length of the shorter one before applying `func`, producing a result with `min(len(a1), len(a2))` elements
- D) `zip_with` only applies when both arrays have the same static length known at compile time; dynamic-length arrays require `arrays_zip` followed by `transform`

---

### Question 30 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: map_from_entries() — constructing a map from an array of structs

What input does `map_from_entries(array_of_structs)` expect, and what is returned?

- A) It accepts a flat array of alternating key-value strings and produces a `MapType(StringType, StringType)`
- B) `map_from_entries(array_of_structs)` expects an `ArrayType(StructType)` where each struct has **exactly two fields** — the first field becomes the map key and the second becomes the map value; it returns a `MapType` whose key and value types match the struct's field types; if the input array contains duplicate keys, the **last value wins** (later entries overwrite earlier ones); if the input is `null` or if any struct element is `null`, the result is `null`
- C) `map_from_entries` only accepts structs with fields named `key` and `value`; other field names raise an `AnalysisException`
- D) `map_from_entries` returns a `StructType` with one field per key rather than a `MapType`

---

### Question 31 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: to_utc_timestamp() / from_utc_timestamp() — timezone conversion functions

What does `to_utc_timestamp(ts, timezone)` do, and how does it differ from `from_utc_timestamp(ts, timezone)`?

- A) They are inverses of each other: `to_utc_timestamp(ts, tz)` interprets `ts` as a local time in `tz` and converts it **to UTC**; `from_utc_timestamp(ts, tz)` interprets `ts` as UTC and converts it **to the local time** in `tz`; for example, `to_utc_timestamp('2026-04-25 10:00:00', 'US/Eastern')` returns `'2026-04-25 14:00:00'` (UTC is +4 h ahead of US/Eastern during EDT); both accept IANA timezone strings or UTC offset strings like `+05:30`
- B) Both functions convert to UTC; `from_utc_timestamp` is the deprecated name for `to_utc_timestamp`
- C) `to_utc_timestamp` adds the UTC offset to the timestamp; `from_utc_timestamp` subtracts it
- D) Both return a `StringType` formatted timestamp; neither returns a `TimestampType`

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: make_date() / make_timestamp() — constructing date and timestamp from components

What does `make_date(year, month, day)` return when an invalid date is provided, such as February 30?

- A) It rounds to the nearest valid date; February 30 becomes March 2 (or 1 in a leap year)
- B) `make_date(year, month, day)` returns **`null`** when any component is out of range (e.g., month > 12, day > days-in-month, year < 0001 or > 9999); no exception is thrown — invalid combinations silently produce `null`; `make_timestamp(year, month, day, hour, min, sec, timezone)` follows the same null-on-invalid convention; this is useful when constructing dates from potentially dirty data columns without needing a `try/catch`
- C) `make_date` raises a `DateTimeException` at runtime when an invalid date component is encountered
- D) `make_date` always succeeds by wrapping around the calendar; February 30 becomes March 1 or 2 depending on the year

---

### Question 33 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: date_diff() vs timestampdiff() — choosing the right date subtraction function

When should `datediff(end, start)` be used instead of `timestampdiff('DAY', start, end)`?

- A) `datediff` should always be used; `timestampdiff` was removed in Spark 3.4
- B) `datediff(end, start)` computes the **integer number of calendar days** between two `DateType` (or `TimestampType`) values and returns an `IntegerType`; `timestampdiff('DAY', start, end)` (Spark 3.3+) computes the same value but accepts any time unit (`SECOND`, `MINUTE`, `HOUR`, `DAY`, `MONTH`, `YEAR`) and also returns an `IntegerType` (truncated, not rounded); use `datediff` for simple day subtraction and `timestampdiff` when you need sub-day or larger-than-day granularity in a single call; both return negative values when `end < start`
- C) `datediff` operates on timestamps and preserves fractional days; `timestampdiff` operates on dates only and returns whole days
- D) `datediff` is a SQL-only function and cannot be used with the `F.` function API; `timestampdiff` supports both SQL and DataFrame API

---

### Question 34 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: sequence() function — generating a range array between two values

What does `sequence(start, stop, step)` return when `step` is negative?

- A) `sequence` always generates values in ascending order; a negative `step` is treated as its absolute value
- B) `sequence(start, stop, step)` generates an `ArrayType` of values from `start` to `stop` (inclusive) incrementing by `step`; a **negative step generates a descending sequence** — `start` must be greater than `stop` for a non-empty result; `sequence(10, 1, -3)` → `[10, 7, 4, 1]`; for dates and timestamps, `step` must be an `INTERVAL`; if `step` is positive but `start > stop`, or negative but `start < stop`, the result is an empty array (not null or an error)
- C) A negative `step` raises a runtime exception because sequences must be ascending
- D) `sequence` only accepts `IntegerType` start/stop; date ranges must use `explode(array_repeat(...))` instead

---

### Question 35 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: try_cast() and try_divide() — error-tolerant arithmetic and casting

How do `try_cast(expr AS type)` and `try_divide(dividend, divisor)` handle errors?

- A) Both functions raise exceptions; `try_` prefix is not a valid Spark SQL syntax modifier
- B) `try_cast(expr AS type)` attempts a type cast and returns **`null` instead of throwing** if the cast is impossible (e.g., `try_cast('abc' AS INT)` → `null`); this is unlike `cast()` which may return `null` for some types silently but raises runtime errors for others in strict mode; `try_divide(a, b)` returns **`null` instead of throwing** when `b = 0` (avoiding `ArithmeticException: / by zero`); both are part of the `try_*` family of error-tolerant SQL functions introduced in Spark 3.2 and are useful for ETL pipelines with dirty data where individual bad values should be coerced to `null` rather than failing the whole job
- C) `try_cast` and `try_divide` are Databricks-proprietary functions not available in open-source Spark
- D) `try_cast` retries the cast up to three times with progressively looser type coercion rules before returning `null`

---

### Question 36 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: PIVOT and UNPIVOT in Spark SQL — SQL syntax vs DataFrame API

What is the SQL syntax for an `UNPIVOT` operation in Spark 3.4+, and what does it produce?

- A) `UNPIVOT` is not supported in Spark SQL; use `stack()` as a workaround
- B) `UNPIVOT` was introduced in Spark 3.4 SQL syntax: `SELECT id, quarter, revenue FROM sales UNPIVOT (revenue FOR quarter IN (Q1, Q2, Q3, Q4))`; it converts **wide-format columns into rows** — the column names specified in the `IN` clause become values in the `quarter` column and their cell values go into the `revenue` column; this is the inverse of `PIVOT`; `null` values in pivoted columns appear as `null` in the unpivoted rows (they are not dropped by default); the DataFrame API equivalent is `stack(4, 'Q1', Q1, 'Q2', Q2, 'Q3', Q3, 'Q4', Q4)` via `selectExpr`
- C) `UNPIVOT` is available only through the DataFrame API via `df.unpivot()`; there is no SQL keyword
- D) `UNPIVOT` produces an array column containing all the pivoted values in a single row rather than multiple rows

---

### Question 37 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: QUALIFY clause — post-window-function filtering in Spark SQL

What does the `QUALIFY` clause do in Spark SQL, and why is it more efficient than wrapping a query in a subquery?

- A) `QUALIFY` is a synonym for `HAVING`; it filters grouped aggregate results
- B) `QUALIFY` filters rows based on the result of a **window function** without requiring a subquery — e.g., `SELECT *, row_number() OVER (PARTITION BY dept ORDER BY salary DESC) AS rn FROM emp QUALIFY rn = 1` returns only the top-paid employee per department; without `QUALIFY`, the equivalent requires wrapping the window expression in a subquery or CTE and then `WHERE`-filtering the outer query; `QUALIFY` is evaluated after `WHERE`, `GROUP BY`, `HAVING`, and window function computation; it was added in Spark 3.3
- C) `QUALIFY` filters rows before the `WHERE` clause, acting as a pre-filter to reduce data scanned by window functions
- D) `QUALIFY` is an alias for `FILTER(WHERE ...)` inside aggregate function calls; it applies within a single aggregate, not across the whole query

---

### Question 38 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: TABLESAMPLE — sampling rows in SQL queries

What does `SELECT * FROM large_table TABLESAMPLE (10 PERCENT)` do, and how does it differ from `TABLESAMPLE (100 ROWS)`?

- A) Both are identical; the unit (`PERCENT` vs `ROWS`) is decorative
- B) `TABLESAMPLE (10 PERCENT)` performs a **Bernoulli row-level sample** — each row is independently included with approximately 10% probability; the actual count varies per run; `TABLESAMPLE (100 ROWS)` returns **at most 100 rows** deterministically, similar to `LIMIT 100` but with sampling semantics (it may use partition-level skipping for performance); neither form is reproducible across runs without a fixed seed; to get a reproducible percentage sample use `df.sample(fraction=0.1, seed=42)` in the DataFrame API
- C) `TABLESAMPLE (10 PERCENT)` always returns exactly 10% of the total rows; `TABLESAMPLE (100 ROWS)` returns a random 100 rows
- D) `TABLESAMPLE` is only valid for Hive-format tables; Delta Lake and Parquet tables raise a parse error

---

### Question 39 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: LATERAL VIEW with explode — SQL explode of nested arrays

What does `LATERAL VIEW OUTER explode(tags) t AS tag` do differently from `LATERAL VIEW explode(tags) t AS tag`?

- A) Both produce the same result; `OUTER` is a no-op keyword in Spark SQL
- B) `LATERAL VIEW explode(tags)` generates one output row per element in the array and **drops** rows where `tags` is `null` or an empty array; `LATERAL VIEW OUTER explode(tags)` preserves those rows, emitting a single row with `tag = null` for each row where `tags` is `null` or empty — this is the SQL equivalent of `F.explode_outer()` in the DataFrame API; use `OUTER` when you need to keep all source rows even when the array has no elements
- C) `LATERAL VIEW OUTER` generates the cross product of the array elements with all other tables in the query
- D) `OUTER` causes `explode` to include the original array column alongside the exploded element column

---

### Question 40 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: schema_of_json() — inferring schema from a JSON sample string

What does `schema_of_json('{"id":1,"name":"Alice"}')` return, and how is it typically used?

- A) It parses the JSON string and returns a `StructType` Java object that can be used directly in `StructType()` constructors
- B) `schema_of_json(json_sample)` returns a **DDL-formatted schema string** (e.g., `'STRUCT<id: BIGINT, name: STRING>'`) inferred from the provided JSON sample literal; it is typically used together with `from_json()` to avoid hardcoding the schema: `df.withColumn("parsed", F.from_json(F.col("json_col"), F.schema_of_json(sample_json)))`; the schema is inferred once at query planning from the literal and applied to every row at runtime; `schema_of_json` only accepts a **string literal**, not a column expression — to infer schema dynamically across rows use `spark.read.json(rdd_of_strings)` instead
- C) `schema_of_json` parses each row of the JSON column at runtime and returns a `MapType(StringType, StringType)` representation
- D) `schema_of_json` is a driver-side Python function available only through the `pyspark.sql.types` module; it is not callable from SQL

---

### Question 41 — DataFrame / DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.write.mode() — write mode semantics

What is the behavior of `df.write.mode("overwrite").parquet(path)` when the target directory already contains data?

- A) It appends new data to the existing files in the directory
- B) The existing directory is **completely deleted and replaced** with the new output — all previously written files at `path` are removed before the new data is written; this is the equivalent of `DROP TABLE` + `INSERT`; if the directory does not exist, it is created; `"error"` (the default) raises an exception if the path exists; `"ignore"` skips the write silently if the path exists; `"append"` adds new files without touching existing ones
- C) The new data is written to a subdirectory (e.g., `path/_overwrite/`) and atomically swapped in only after the write completes
- D) `overwrite` mode removes only files matching the output filename pattern; unrelated files in the directory are preserved

---

### Question 42 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.write.partitionBy() — directory structure and file layout

When `df.write.partitionBy("year", "month").parquet(output_path)` is executed, how are files laid out on disk?

- A) All Parquet files are written to `output_path` with the partition columns embedded in the filename
- B) Spark creates a **Hive-style partition directory tree**: `output_path/year=2025/month=01/part-00000.parquet`, `output_path/year=2025/month=02/part-00001.parquet`, etc.; the `year` and `month` columns are removed from the Parquet file contents (they are encoded in the path); when Spark reads the directory later, it reconstructs the partition column values from the directory names via partition discovery; using `partitionBy` enables partition pruning — queries with `WHERE year=2025` skip irrelevant directories entirely
- C) A single Parquet file is written with all rows sorted by `year` then `month`; partition directories are a Hive-only feature
- D) Files are written as `output_path/part-00000-year_month.parquet` with the partition values in the filename; subdirectories are not created

---

### Question 43 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.write.bucketBy() — how bucketing affects reads and joins

What does `df.write.bucketBy(64, "user_id").sortBy("user_id").saveAsTable("bucketed_table")` accomplish, and how does it speed up joins?

- A) `bucketBy` partitions the data into 64 Parquet files; it only affects write performance, not read or join performance
- B) `bucketBy(64, "user_id")` writes the data into **64 fixed buckets** based on the hash of `user_id`; all rows with the same `user_id` go to the same bucket file (and are sorted within it if `sortBy` is used); when two tables are bucketed by the same column with the same number of buckets, a join on `user_id` can **skip the shuffle entirely** — Spark knows corresponding buckets from each table already contain matching keys and can directly merge-join them bucket by bucket; this is only effective for tables registered in the Hive Metastore (requires `saveAsTable`, not `save()`)
- C) `bucketBy` creates 64 balanced partitions in memory; the physical files on disk are not pre-grouped by bucket
- D) `bucketBy` enables dynamic partition pruning; queries with `WHERE user_id = X` scan only 1 of the 64 bucket files

---

### Question 44 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.writeTo() API — V2 table write operations (append, overwrite, createOrReplace)

How does `df.writeTo("catalog.schema.table").append()` differ from `df.write.mode("append").saveAsTable("catalog.schema.table")`?

- A) They are identical; `writeTo()` is just a newer syntax for the same underlying operation
- B) `df.writeTo(tableName)` uses the **DataSource V2 (DS V2) Table API** and supports richer semantics: `.append()`, `.overwrite(condition)`, `.overwritePartitions()`, `.create()`, `.replace()`, `.createOrReplace()`; unlike the V1 `write.saveAsTable()`, it respects the table's existing schema and partition layout without re-creating metadata; `.overwrite(condition)` can delete only the rows matching a predicate before inserting (like a partial overwrite); V2 tables (e.g., Delta, Iceberg) expose additional operations through this API that V1 `saveAsTable` cannot perform
- C) `writeTo()` only works for external tables stored in HDFS; managed tables require `write.saveAsTable()`
- D) `writeTo()` always creates a new table; it raises an error if the table already exists

---

### Question 45 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.observe() — attaching named metrics to a query

What does `df.observe("my_metrics", F.count(F.lit(1)).alias("row_count"), F.mean("value").alias("avg_val"))` do?

- A) `observe()` immediately executes the aggregate functions and prints the metrics to stdout; no listener is needed
- B) `df.observe(name, *exprs)` attaches named aggregate metric observations to a DataFrame — the metrics are **computed during a subsequent action** (e.g., `.write`, `.collect()`) **in a single pass** alongside the normal data processing without extra scans; the results are retrieved via a `QueryExecutionListener` registered on the Spark session (the `onSuccess` callback receives an `ObservedMetrics` map keyed by the observation name); this is useful for capturing quality metrics (null counts, min/max, row counts) during ETL writes without an additional `agg()` pass over the data
- C) `observe()` schedules the aggregate expressions to run asynchronously on the driver after the action completes
- D) `observe()` is a streaming-only API; in batch mode it is ignored and the DataFrame is returned unchanged

---

### Question 46 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.stat.freqItems() — finding frequent items (heavy hitters)

What algorithm does `df.stat.freqItems(cols, support)` use, and what does the `support` parameter control?

- A) `freqItems` performs an exact count of all distinct values and returns those appearing at least `support * total_rows` times; it requires a full shuffle
- B) `df.stat.freqItems(cols, support=0.01)` uses the **Misra-Gries / Space-Saving approximate heavy-hitter algorithm** to find items that appear in at least `support * total_rows` rows (default support 1%); it is **approximate** — it may include false positives (items slightly below the threshold) but will not miss true heavy hitters; the result is a DataFrame with one row and one `ArrayType` column per input column containing the frequent item values; it is computed in a single pass without full shuffle, making it much cheaper than `groupBy().count()` followed by filtering; increasing `support` returns fewer, more certain frequent items
- C) `freqItems` is an exact operation that uses a sorted hash map and returns the top-N items by count; `support` is the minimum count (not fraction)
- D) `freqItems` returns a frequency histogram as a `MapType(StringType, LongType)` mapping each value to its count

---

### Question 47 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Pandas UDF (scalar) — execution model and Arrow batch processing

How does a `@F.pandas_udf(returnType)` scalar UDF differ from a standard Python UDF in terms of how data is passed to the function?

- A) Both pass data row-by-row to Python; the only difference is that Pandas UDFs are compiled to native code
- B) A standard Python UDF receives one Python scalar value per call (one row at a time), which requires row-by-row serialization through Py4J and object boxing; a **scalar Pandas UDF** receives a `pandas.Series` (or multiple `Series` for multi-column UDFs) containing an entire Arrow batch of values and must return a `Series` of the same length; data is transferred between JVM and Python via **Apache Arrow** in columnar batches, eliminating per-row overhead; this can be 10–100× faster than a standard Python UDF for numeric operations; the function is still called multiple times (once per batch), not once per partition
- C) Pandas UDFs pass entire DataFrames (all columns, all rows) to the function in one call; standard UDFs pass one column at a time
- D) Standard UDFs and Pandas UDFs use identical serialization; performance differences come only from NumPy vectorization inside the function body

---

### Question 48 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.localCheckpoint() — in-memory checkpointing without HDFS

What does `df.localCheckpoint()` do, and how is it different from `df.checkpoint()`?

- A) `localCheckpoint()` is identical to `checkpoint()`; both require `sc.setCheckpointDir()` to be configured
- B) `df.localCheckpoint()` **materialises the DataFrame on executor local disk (and optionally memory)** and truncates its lineage — it does **not** require a configured checkpoint directory or writing to HDFS/S3; the checkpointed data is stored on the executor's local storage and is therefore **not fault-tolerant** (if the executor fails, the data is lost and the lineage cannot be recomputed); `df.checkpoint()` writes to the configured reliable filesystem (HDFS/S3) and is fault-tolerant but slower; use `localCheckpoint()` for iterative algorithms where you want to truncate lineage cheaply and can tolerate stage re-execution on executor failure
- C) `localCheckpoint()` writes the checkpoint to the driver's local filesystem; `checkpoint()` writes to HDFS
- D) `localCheckpoint()` materialises the DataFrame in the Spark driver's memory; `checkpoint()` materialises it in executor memory

---

### Question 49 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.input_file_name() — tracking source file per row

When reading a directory of CSV files with `spark.read.csv(directory_path)`, how do you add a column containing the **source filename** for each row?

- A) Add the option `option("includeSourceFile", "true")` to the reader; Spark automatically adds a `_filename` column
- B) Call `df.withColumn("source_file", F.input_file_name())` after reading — `F.input_file_name()` is a built-in function that returns the **fully qualified path** of the file being read for the current row's partition; it is evaluated per task and works for file-based sources (CSV, JSON, Parquet, ORC, text); the value is an empty string for non-file sources (e.g., JDBC); it is commonly used in data lineage tracking and to identify which source file introduced anomalies
- C) `F.input_file_name()` returns the file name without the path; use `F.input_file_block_start()` for the full URI
- D) This is only possible for Parquet files because Parquet stores the source filename in row group metadata

---

### Question 50 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.stat.crosstab() — computing a contingency table

What does `df.stat.crosstab("col1", "col2")` return?

- A) A correlation matrix between `col1` and `col2` as a 2×2 `DataFrame`
- B) `df.stat.crosstab("col1", "col2")` returns a **contingency table** (cross-tabulation) — a `DataFrame` where each distinct value of `col1` becomes a **row** (in a column named `col1_col2`) and each distinct value of `col2` becomes a **column**; cell values are the count of rows where that combination of (`col1`, `col2`) values occurs; it is equivalent to a grouped `count` pivot; the result has at most `spark.sql.crossJoin.maxValues` columns (default 1000); values exceeding the limit are grouped into an `other` column; it is useful for EDA to understand the joint distribution of two categorical variables
- C) `crosstab` returns a single `Row` with two fields: `col1_values` (array of distinct values) and `col2_values` (array of distinct values)
- D) `crosstab` computes the χ² (chi-squared) statistic and p-value for independence between `col1` and `col2`

---

### Question 51 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.regexp_extract() — capturing groups from a pattern

What does `F.regexp_extract(col, pattern, groupIndex)` return when the pattern does not match?

- A) It returns `null` when there is no match
- B) `F.regexp_extract(col, pattern, groupIndex)` returns the substring captured by the `groupIndex`-th group in `pattern`; when the pattern **does not match**, it returns an **empty string `""`**, not `null`; group index 0 returns the entire match (same as no grouping); if the input column is `null`, the result is `null`; use `F.regexp_extract_all()` (Spark 3.1+) to get an array of all matches in the string
- C) It returns the original column value unchanged when there is no match
- D) It throws a `java.util.regex.PatternSyntaxException` at runtime if there are no matches in any row

---

### Question 52 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.split() — splitting a string with a limit argument

What is the effect of the optional `limit` parameter in `F.split(str, pattern, limit)`?

- A) `limit` sets the maximum number of regex pattern occurrences to search; splits always produce exactly `limit + 1` tokens
- B) `F.split(str, pattern, limit)` splits `str` by `pattern` at most `limit - 1` times, producing an array of at most `limit` elements — the last element contains the **remaining unsplit portion** of the string; `limit=0` (the default) splits on every occurrence and removes trailing empty strings; `limit=-1` splits on every occurrence but preserves trailing empty strings; this matches Java's `String.split(regex, limit)` semantics exactly
- C) `limit` specifies the maximum length of each individual token; tokens exceeding this length are truncated
- D) Setting `limit` converts the result from `ArrayType(StringType)` to a fixed-length `StructType` with `limit` fields

---

### Question 53 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.from_json() with schema — parsing JSON strings into structs

When using `F.from_json(col("payload"), schema)`, what happens to rows where the JSON string does not match the provided schema?

- A) `from_json` raises a runtime exception and the entire job fails
- B) `F.from_json(col, schema)` is **permissive by default** — if a field in `schema` is absent in the JSON string it is returned as `null`; extra fields in the JSON not in the schema are silently ignored; if the entire string is malformed (unparseable JSON), the result is a struct where **all fields are `null`**; you can capture parsing errors by including a `columnNameOfCorruptRecord` option in the options map passed as the third argument to `from_json` — the corrupt raw string is placed in that field instead of silently returning nulls
- C) `from_json` validates the JSON schema at query planning time and refuses to compile the query if schema mismatches are possible
- D) `from_json` returns `null` for any row where even one field is missing from the JSON, making it strict by default

---

### Question 54 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: StructType construction — from DDL string vs programmatic StructField list

What is the difference between `StructType.fromDDL("id BIGINT, name STRING, ts TIMESTAMP")` and building a `StructType` programmatically with `StructType([StructField(...), ...])`?

- A) `fromDDL` only works for reading Parquet files; programmatic `StructType` must be used for JSON
- B) `StructType.fromDDL(ddl_string)` parses a **Spark SQL DDL string** and constructs an equivalent `StructType` — it is a convenience method that avoids verbose `StructField` boilerplate for complex nested schemas; both produce identical `StructType` objects at runtime; `fromDDL` is particularly useful when the schema is stored as a string in a config file or returned by `schema_of_json()`; all fields created via `fromDDL` are **nullable** by default; to mark fields as non-nullable you must use the programmatic API with `StructField(name, dtype, nullable=False)`
- C) `fromDDL` returns a `DataType` object, not a `StructType`; it must be cast before use with `read.schema()`
- D) Programmatic `StructType` preserves field metadata (e.g., comments); `fromDDL` strips all metadata

---

### Question 55 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.randomSplit() — deterministic splits with seed

A developer writes `train, test = df.randomSplit([0.8, 0.2], seed=42)` and then calls `train.count()` followed by `test.count()`. What caveat should they be aware of?

- A) The seed is ignored for `randomSplit`; results are always non-deterministic
- B) `randomSplit` generates split boundaries by hashing each row with the provided `seed` — **the splits are reproducible**, meaning calling `randomSplit(..., seed=42)` on the same DataFrame again produces identical splits; however, `train` and `test` are **lazy DataFrames derived from the same scan** — if the source data is non-deterministic (e.g., a file with an unstable row order or a non-deterministic source) or if the DataFrame has not been cached, the source **may be re-scanned separately** for `train.count()` and `test.count()`, and rows could theoretically appear in both splits (though with a fixed seed and stable file this is typically consistent); to guarantee no overlap and avoid double-scanning, `cache()` or `persist()` the source DataFrame before calling `randomSplit`
- C) `randomSplit` always produces exactly 80% and 20% of rows; the seed controls only the ordering
- D) `seed` is only used for the first split; subsequent splits within the same call are independently random

---

### Question 56 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.hint() — optimizer hints for join strategy

What does `df.hint("broadcast")` tell the Catalyst optimizer, and when would you use it?

- A) `hint("broadcast")` pins the DataFrame to a specific executor and prevents it from being shuffled
- B) `df.hint("broadcast")` instructs the Catalyst optimizer to use a **broadcast hash join** for this DataFrame regardless of its size relative to `spark.sql.autoBroadcastJoinThreshold`; this is useful when you know a table is small enough to broadcast but its size statistics are stale, missing, or below the auto-broadcast threshold; equivalent SQL hints: `/*+ BROADCAST(t) */`; if the hinted table is too large to broadcast (exceeds driver/executor memory), the job will fail with an OOM — the hint overrides the threshold but not physical memory limits; other join hints include `MERGE` (sort-merge), `SHUFFLE_HASH`, and `SHUFFLE_REPLICATE_NL`
- C) `hint("broadcast")` enables a streaming broadcast that continuously updates the broadcasted relation as new data arrives
- D) `hint("broadcast")` is only respected when both sides of the join use the hint; a one-sided hint is silently ignored

---

### Question 57 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.encode() / F.decode() — binary string encoding conversion

What do `F.encode(str_col, charset)` and `F.decode(binary_col, charset)` do?

- A) `encode` converts a string column to its base64 representation; `decode` reverses base64 back to a string
- B) `F.encode(str_col, charset)` converts a `StringType` column to a `BinaryType` column using the specified character set encoding (e.g., `'UTF-8'`, `'UTF-16'`, `'US-ASCII'`, `'ISO-8859-1'`); `F.decode(binary_col, charset)` converts a `BinaryType` column back to `StringType` using the given charset; they are inverses of each other and useful for handling data stored or transmitted in non-UTF-8 encodings; both return `null` for `null` input
- C) `encode` and `decode` are URL encoding/decoding functions; spaces become `%20` and special characters are percent-escaped
- D) `encode` compresses the binary content using the Spark IO codec; `decode` decompresses it

---

### Question 58 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.write.maxRecordsPerFile — controlling output file size

What does `df.write.option("maxRecordsPerFile", 100000).parquet(path)` do?

- A) It creates at most 100,000 Parquet files regardless of partition count
- B) `maxRecordsPerFile` limits the number of records written to **each individual output file** — if a single output partition contains more than 100,000 rows, Spark splits it into multiple files each containing at most 100,000 rows; this prevents very large files from individual partitions and is useful for generating uniformly-sized files for downstream consumers that have per-file size constraints; it does not affect the number of tasks (partitions) — it only splits the output within a task into multiple files
- C) `maxRecordsPerFile` causes Spark to repartition the DataFrame into chunks of 100,000 rows before writing, adding an extra shuffle
- D) This option is only available for the CSV and JSON formats; Parquet ignores it and uses row-group size instead

---

### Question 59 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.sortWithinPartitions() vs df.orderBy() — local vs global sort

What is the operational difference between `df.sortWithinPartitions("col")` and `df.orderBy("col")`?

- A) `sortWithinPartitions` is a legacy alias for `orderBy`; both produce a globally sorted DataFrame
- B) `df.orderBy("col")` produces a **globally sorted DataFrame** — it inserts a range-partitioning shuffle (`RangePartitioner`) so that all rows across all partitions are in total ascending order; it requires a full shuffle and results in a single output partition (or few) unless `spark.sql.shuffle.partitions` is tuned; `df.sortWithinPartitions("col")` sorts rows **within each partition independently** without a shuffle — the result is locally sorted but globally unsorted across partitions; use `sortWithinPartitions` before writing to improve within-file sort order for predicate pushdown (e.g., Parquet min/max statistics) without the cost of a global shuffle
- C) Both functions shuffle data; `sortWithinPartitions` just uses fewer output partitions
- D) `orderBy` sorts only the first partition; remaining partitions are unsorted

---

### Question 60 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.na.drop() vs df.na.fill() — null handling API

What is the difference between `df.na.drop(how="all")` and `df.na.drop(how="any")`?

- A) `how="all"` drops rows where **all** specified (or all) columns are `null`; `how="any"` drops rows where **at least one** specified column is `null`; the optional `subset` parameter limits null-checking to named columns; `df.na.fill(value)` or `df.na.fill({"col1": 0, "col2": "unknown"})` replaces `null` values with a scalar or per-column map — it only fills columns whose type is compatible with the provided value; `fill` does not drop rows
- B) `how="all"` drops rows where any column is `null`; `how="any"` drops rows only when every column is `null`
- C) `how="all"` removes all null values including within arrays and structs; `how="any"` only removes top-level nulls
- D) `how` is not a valid parameter for `na.drop()`; the only form is `df.na.drop(subset=["col1"])`

---

### Question 61 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Unresolved ambiguous column after join — resolving duplicate column names

After `result = df1.join(df2, df1.id == df2.id, "inner")`, what is the recommended way to select `id` without an `AnalysisException: Resolved attribute missing from child`?

- A) Refer to `result["id"]` directly; Spark automatically resolves duplicates by picking the left DataFrame's column
- B) After joining with explicit column expressions (`df1.id == df2.id`) rather than a string key, both `id` columns exist in the result with the same name but different internal IDs; to avoid ambiguity, **reference the original DataFrame's column object directly**: `result.select(df1["id"], ...)` rather than `result.select("id")` or `result.select(F.col("id"))`; alternatively, rename one side before joining (`df2.withColumnRenamed("id", "id_right")`) or join using a list-of-string syntax `df1.join(df2, ["id"])` which deduplicates by keeping only one `id` column in the output
- C) Always use `result.drop(df2["id"])` immediately after every join to remove the duplicate
- D) The `AnalysisException` only occurs in Python; Scala and Java Spark automatically deduplicate same-name columns after joins

---

### Question 62 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.when() chaining — conditional column expressions

What is returned for a row where none of the `F.when()` conditions are true and no `.otherwise()` is specified?

- A) A runtime exception is raised because every `when` chain must have an `otherwise` clause
- B) When a `F.when(cond, val).when(cond2, val2)` chain has **no `.otherwise()`**, rows that do not match any condition produce **`null`** for that column; this is equivalent to `CASE WHEN ... END` in SQL without an `ELSE` clause; if an `otherwise(default)` is added, unmatched rows receive `default`; the return type of the column is inferred from the `val` expressions; if they differ, Spark applies implicit type coercion
- C) The last `when` clause's value is used as a fallback for unmatched rows
- D) An empty string `""` is returned for string columns and `0` for numeric columns when no condition matches and `.otherwise()` is absent

---

### Question 63 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.select(F.col("*"), new_col) — selecting all columns plus a new computed column

What does `df.select(F.col("*"), F.lit(1).alias("flag"))` return?

- A) It raises an `AnalysisException` because `col("*")` and additional column expressions cannot coexist in `select`
- B) It returns a new DataFrame containing **all columns from `df`** followed by the additional column `flag` (a constant integer `1`); `F.col("*")` (or the string `"*"`) acts as a glob that expands to all existing columns at query planning time; this is equivalent to `df.withColumn("flag", F.lit(1))` but using `select`; using `select("*", ...)` is useful when you want to control column ordering or add multiple new columns without repeating all existing column names
- C) `F.col("*")` returns a single `ArrayType` column containing all values for a row; the result DataFrame has two columns: `*` and `flag`
- D) `select(F.col("*"))` is only valid in SQL string expressions; use `df.columns` to expand column names in the DataFrame API

---

### Question 64 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.withColumn() on an existing column name — silent overwrite

What happens when `df.withColumn("price", F.col("price") * 1.1)` is called on a DataFrame that already has a column named `price`?

- A) It raises an `AnalysisException: Column 'price' already exists`
- B) `withColumn` **silently overwrites** the existing `price` column with the new expression — the old column is replaced in-place; the schema retains only one `price` column; this is by design and is the standard pattern for updating a column in Spark; no error or warning is emitted; if you want to keep the old value alongside the new one, rename it first with `withColumnRenamed`
- C) A new column `price_1` is automatically created to avoid collision; the original `price` is preserved
- D) `withColumn` appends the new expression as a second column named `price`; queries referencing `price` become ambiguous

---

### Question 65 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.toDF(*newColumnNames) — batch column rename

What does `df.toDF("a", "b", "c")` do, and what error occurs if the name count is wrong?

- A) `toDF` renames only the columns passed; unmentioned columns are dropped
- B) `df.toDF(*new_names)` returns a new DataFrame with **all columns renamed** to the provided names in order — the number of names must match the number of columns in `df`; if the count does not match, Spark raises an `AnalysisException: The number of columns doesn't match`; it is a concise alternative to chaining multiple `withColumnRenamed()` calls or using `select([F.col(old).alias(new) for old, new in zip(df.columns, new_names)])`
- C) `toDF` creates a new table in the Hive metastore with the given column names; the DataFrame is unchanged
- D) `toDF("a", "b", "c")` only works if the DataFrame was created from a Pandas DataFrame; for Spark-native DataFrames use `withColumnRenamed`

---

### Question 66 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.crossJoin() — cartesian product and row count

What does `df1.crossJoin(df2)` produce, and what warning should you heed when using it on large tables?

- A) `crossJoin` is equivalent to `df1.join(df2, how="outer")` and performs a full outer join
- B) `df1.crossJoin(df2)` produces the **Cartesian product** — every row of `df1` is paired with every row of `df2`, resulting in `M × N` output rows; no join condition is required or allowed; for large tables this can produce an enormous number of rows and cause out-of-memory errors or extremely long runtimes; by default `spark.sql.crossJoin.enabled=true` (Spark 3.x), so no additional configuration is needed; Catalyst does not push predicates into cross joins, so any filtering must happen before or after the join explicitly; common uses include generating test data or small-scale combinatorial enumeration
- C) `crossJoin` is disabled by default and requires `spark.sql.crossJoin.enabled=true`; attempting it without this setting raises an `AnalysisException`
- D) `crossJoin` on two DataFrames with the same column names raises an `AnalysisException` due to column name conflicts

---

### Question 67 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.selectExpr() — SQL expression strings in the DataFrame API

How does `df.selectExpr("*", "price * 0.9 as discounted", "upper(name) as name_upper")` work?

- A) `selectExpr` accepts only column reference strings (like `select`); arithmetic expressions are not valid
- B) `df.selectExpr(*exprs)` accepts **SQL expression strings** — each string is parsed by the Catalyst SQL parser and evaluated as a column expression; `"*"` expands to all columns; `"price * 0.9 as discounted"` computes a new column named `discounted`; this is a powerful shorthand that avoids importing `F.` functions for simple SQL operations; it is equivalent to `df.select(F.expr("..."))` for each expression; invalid SQL syntax causes an `AnalysisException` at plan analysis time
- C) `selectExpr` requires a `SparkSession.sql()` call first to register the DataFrame as a temp view; it cannot operate on in-memory DataFrames directly
- D) `selectExpr("*")` selects only the first column; use `selectExpr("df.*")` to expand all columns

---

### Question 68 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.dropDuplicates() vs df.distinct() — deduplication with and without subset

What is the difference between `df.distinct()` and `df.dropDuplicates(["id"])`?

- A) They are identical; `distinct()` is a deprecated alias for `dropDuplicates()`
- B) `df.distinct()` removes rows that are exact duplicates across **all columns**; `df.dropDuplicates(["id"])` removes rows with duplicate values in the **specified subset** of columns (`id`), keeping only the first occurrence per `id` value and retaining all columns — even if other columns differ between duplicates; `dropDuplicates()` with no argument is equivalent to `distinct()`; `dropDuplicates` is therefore more flexible for deduplicating by a key while preserving non-key column values
- C) `distinct()` removes rows with any `null` values; `dropDuplicates` only removes exact non-null duplicates
- D) `dropDuplicates(["id"])` fails if `id` contains `null` values; `distinct()` handles nulls correctly

---

### Question 69 — DataFrame / DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.col() vs df["col"] vs F.expr() — three ways to reference a column

When should `F.expr("col1 + col2")` be preferred over `F.col("col1") + F.col("col2")`?

- A) They are completely interchangeable; there is no situation where one is preferred over the other
- B) `F.expr("col1 + col2")` parses an arbitrary **SQL expression string** — it is preferred when the expression is complex (e.g., window functions, CASE WHEN, nested function calls) and writing it with `F.` chaining would be verbose or impossible; `F.col("col1") + F.col("col2")` uses Python operator overloading and is more readable for simple arithmetic; `F.expr()` also accepts SQL constructs not available as `F.` functions (e.g., `STRUCT<...>` literals, named window clause references); both produce identical physical plans for equivalent expressions
- C) `F.expr()` is evaluated on the driver immediately; `F.col() + F.col()` is lazy and evaluated on executors
- D) `F.expr()` only works inside `selectExpr()`; it cannot be used inside `withColumn()` or `filter()`

---

### Question 70 — DataFrame / DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Window function frame — ROWS vs RANGE unbounded/current row

What is the difference between `Window.rowsBetween(Window.unboundedPreceding, Window.currentRow)` and `Window.rangeBetween(Window.unboundedPreceding, Window.currentRow)` in a running total calculation?

- A) They always produce identical results for `sum()`; the difference only matters for `rank()`
- B) `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` includes **exactly the physical rows** from the first row of the partition up to and including the current row — it counts rows by position; `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` includes **all rows with an ORDER BY value ≤ the current row's ORDER BY value** — rows with equal ORDER BY values (ties) are all included in the current row's frame; for `SUM()`, this means ROWS gives a strict running total while RANGE gives the running total up to and including all tied peers, which can produce identical values for tied rows (the frame extends to include peers); RANGE requires an `ORDER BY` clause with a sortable numeric or date column; for non-unique ORDER BY values RANGE may include more rows than expected
- C) `rowsBetween` and `rangeBetween` are interchangeable unless the window is unbounded on both sides
- D) `rangeBetween` only works with `RANK()` and `DENSE_RANK()`; aggregate functions must use `rowsBetween`

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: AQE coalesce post-shuffle — avoiding tiny output partitions

After a large filter reduces data to almost nothing, AQE's post-shuffle coalesce feature kicks in. What configuration enables this, and what does it do?

- A) AQE coalesce requires `spark.sql.adaptive.coalescePartitions.enabled=true` (which is `true` by default when `spark.sql.adaptive.enabled=true`); after a shuffle, AQE examines the actual shuffle output sizes and **merges small adjacent shuffle partitions** into fewer, larger partitions based on `spark.sql.adaptive.advisoryPartitionSizeInBytes` (default 64 MB); this avoids launching hundreds of tasks that each process only a few bytes after an aggressive filter; the coalesced partition count is bounded below by `spark.sql.adaptive.coalescePartitions.minPartitionNum`; coalescing only applies to adjacent partitions (not arbitrary merging), so the strategy is applied greedily from left to right
- B) AQE coalesce is enabled by `spark.sql.shuffle.partitions=auto`; it sets shuffle partitions to a fixed number based on input table size
- C) AQE post-shuffle coalesce is triggered only for `groupBy` operations and does not apply to filtered datasets
- D) AQE coalesce splits large partitions into smaller ones; small partition merging is handled by `spark.sql.files.maxPartitionBytes`

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.memory.storageFraction — preventing storage eviction in unified memory

What does `spark.memory.storageFraction` (default 0.5) control within the Spark memory region bounded by `spark.memory.fraction`?

- A) It sets the absolute maximum heap size for storage; remaining heap is reserved for user code
- B) `spark.memory.storageFraction` defines the **fraction of the unified memory region** (bounded by `spark.memory.fraction * heap`) that is **protected from eviction** by execution memory; storage memory below this watermark cannot be evicted by execution even under pressure — execution must spill to disk instead; storage above the watermark can be evicted; this means setting `storageFraction` higher protects more cached data from being evicted during heavy computation but constrains execution memory; the default `0.5` means storage and execution each start with half the unified pool and can borrow from each other only above their respective fractions
- C) `storageFraction` sets the fraction of executor heap used for Python worker off-heap storage; it does not affect Java memory
- D) `storageFraction` controls how much of the shuffle write buffer is reserved for storage operations during a sort-merge join

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: EXPLAIN CODEGEN — inspecting Whole-Stage Code Generation output

What does running `EXPLAIN CODEGEN SELECT ...` (or `df.explain("codegen")`) reveal, and when is Whole-Stage Code Generation not applicable?

- A) It shows the physical plan with estimated row counts; it does not show generated Java code
- B) `EXPLAIN CODEGEN` displays the **generated Java source code** for each Whole-Stage CodeGen (WSCG) pipeline in the physical plan — operators fused into a single pipeline are compiled into a single `generate` class that processes multiple operators in a tight loop without materialising intermediate rows, eliminating virtual dispatch and object allocation overhead; the output shows `/* 001 */ public Object generate(Object[] references)` style Java code; operators that do not support WSCG (e.g., certain Python UDFs, `SortMergeJoin` without specific conditions, some streaming operators) are shown as separate pipeline boundaries marked with `!`, indicating a pipeline break where data must be materialised
- C) `EXPLAIN CODEGEN` is equivalent to `EXPLAIN EXTENDED`; both show the same textual physical plan
- D) Whole-Stage Code Generation is only enabled when the number of columns is fewer than 100; `EXPLAIN CODEGEN` reports the column count that triggers or prevents WSCG

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Skew join — symptoms and AQE skew join optimization

What symptoms indicate a skew join, and how does AQE's skew join optimization address it?

- A) A skew join produces incorrect results; AQE's optimization re-orders join sides to fix correctness
- B) A **skew join** is indicated by a few tasks taking significantly longer than others (straggler tasks) while most finish quickly — visible in the Spark UI stage detail as a few tasks with vastly higher input sizes or shuffle read bytes; it occurs when a join key has a highly uneven distribution (e.g., a `user_id` of `null` or a popular product ID has millions of rows while others have few); AQE detects skewed partitions using `spark.sql.adaptive.skewJoin.enabled=true` (default true when AQE is enabled) and `spark.sql.adaptive.skewJoin.skewedPartitionFactor` / `skewedPartitionThresholdInBytes`; it splits skewed partitions into smaller sub-partitions and replicates the corresponding partition from the other side to process the sub-partitions in parallel, effectively eliminating the stragglers
- C) Skew joins only occur in broadcast hash joins; sort-merge joins are immune to skew
- D) AQE resolves skew by adding a salt column to the join key before planning the query; the user must apply the salt manually

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.autoBroadcastJoinThreshold — disabling automatic broadcast

How do you completely disable automatic broadcast joins in a Spark session?

- A) Set `spark.sql.broadcastTimeout=0` to prevent any broadcast from completing
- B) Set `spark.sql.autoBroadcastJoinThreshold=-1` — a value of `-1` **disables** automatic broadcast joins entirely; Spark will then prefer sort-merge join or shuffle hash join for all joins regardless of table size; this is useful for reproducible performance testing (eliminating plan variability), or when broadcast causes OOM on the driver/executors for tables near the threshold; you can also disable it per-query with the `NO_BROADCAST` SQL hint; setting it to `0` disables it for all tables except those that are trivially empty (0 bytes)
- C) Set `spark.sql.autoBroadcastJoinThreshold=0` to disable; `-1` has no special meaning
- D) Broadcast joins cannot be disabled globally; they can only be suppressed per join using `df.hint("merge")`

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ORC vs Parquet — choosing between columnar formats

What are the key differences between ORC and Parquet as columnar storage formats in the Spark ecosystem, and when would you choose ORC over Parquet?

- A) ORC is row-based and Parquet is columnar; choose Parquet for OLAP workloads and ORC for OLTP
- B) Both are **columnar, splittable, compressed file formats** supporting predicate pushdown and schema evolution, but they differ in several ways: **ORC** uses Hive-native type system, supports ACID transactions natively (in Hive), has built-in lightweight indexes (row-group statistics, bloom filters, row indexes at stripe level), and compresses better for low-cardinality string columns; **Parquet** is the de facto standard for the broader Apache ecosystem (Arrow, Delta Lake, Iceberg, Spark, Flink, Presto), supports nested (complex) types more richly (e.g., deeply nested `MAP<LIST<STRUCT>>`) and is the default format in Databricks and most cloud lakehouse formats; choose ORC when interoperating with Hive ACID tables or legacy pipelines that rely on Hive; choose Parquet for Delta Lake, streaming, Iceberg, or cross-platform tooling
- C) Parquet stores row groups; ORC stores stripes — these are synonymous and there is no practical difference between the formats for Spark workloads
- D) ORC does not support predicate pushdown; Parquet is the only columnar format with filter pushdown in Spark

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: GC tuning — G1GC and JVM heap settings for Spark executors

When Spark executors experience long GC pauses, which JVM garbage collector is recommended, and what is the key JVM flag to enable it?

- A) Serial GC is recommended for Spark executors because it minimises CPU overhead during GC
- B) **G1GC** (Garbage-First GC) is the recommended collector for Spark executors — it is enabled with `-XX:+UseG1GC` in `spark.executor.extraJavaOptions`; G1GC is designed for large heaps (>4 GB) and reduces stop-the-world pause times by incrementally collecting regions instead of sweeping the entire old generation at once; useful tuning flags include `-XX:G1HeapRegionSize` (set to 16m-32m for large heaps), `-XX:InitiatingHeapOccupancyPercent=35` (trigger concurrent marking earlier to avoid full GCs), and `-XX:MaxGCPauseMillis=200`; long GC pauses in Spark often manifest as heartbeat timeouts (`SparkException: Lost executor`) or slow task completion
- C) CMS (Concurrent Mark Sweep) is the recommended Spark GC; G1GC causes excessive fragmentation for Spark's off-heap memory usage
- D) ZGC is the default collector for Spark 3.x; `-XX:+UseG1GC` is only needed on JDK 8

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.inMemoryColumnarStorage.batchSize — columnar cache batch tuning

What does `spark.sql.inMemoryColumnarStorage.batchSize` (default 10000) control?

- A) It sets the number of Parquet row groups read into memory at once during a scan
- B) It controls the **number of rows per columnar batch** when caching a DataFrame with `df.cache()` or `df.persist(MEMORY_AND_DISK)` using the columnar in-memory format; larger batches improve compression ratios (more data per column vector for run-length or dictionary encoding) and vectorized processing throughput, but consume more memory per batch; smaller values reduce memory per batch at the cost of encoding efficiency; changing it requires uncaching and re-caching the DataFrame to take effect
- C) `batchSize` sets the maximum number of rows returned by `df.collect()` per round trip between executor and driver
- D) It limits the number of concurrent columnar scans on a cached table; additional scans queue until a batch slot is free

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.eventLog.enabled — Spark History Server event logging

What does `spark.eventLog.enabled=true` do, and where are event logs stored?

- A) It enables the Spark driver web UI; disabling it hides job/stage information from the browser
- B) Setting `spark.eventLog.enabled=true` instructs Spark to **write all application events** (job start/end, stage start/end, task metrics, executor added/removed) to a serialized log file at the path defined by `spark.eventLog.dir` (must be a shared/distributed filesystem such as HDFS or S3 so the History Server can read it); the Spark History Server reads these event logs to reconstruct completed application UIs after the application has terminated; without event logging, metrics from completed applications are unrecoverable
- C) Event logging writes application logs (stdout/stderr) from executors to the driver's local filesystem
- D) `spark.eventLog.enabled` is automatically set to `true` in cluster mode; it only has effect in local mode

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Shuffle spill — diagnosing and reducing shuffle spill to disk

What causes shuffle spill to disk, and which two metrics in the Spark UI confirm it is occurring?

- A) Shuffle spill occurs when too many files are written; it is confirmed by the `Shuffle Write Size` metric
- B) Shuffle spill occurs when the **sort buffer for a shuffle map task** (or the aggregation hash map for a reduce task) exceeds available execution memory; Spark spills sorted data to local disk, then merges the spill files; in the Spark UI Stage detail, two metrics confirm spill: **`Shuffle Spill (Memory)`** — the amount of in-memory data serialized and written to disk — and **`Shuffle Spill (Disk)`** — the actual compressed bytes written to disk; to reduce spill: increase executor memory, reduce `spark.sql.shuffle.partitions` (fewer, larger tasks may have more memory per task), or increase `spark.memory.fraction`; large spill causes significant slowdowns due to disk I/O
- C) Shuffle spill is diagnosed by high `GC Time` in the task metrics; memory spill does not have dedicated UI metrics
- D) Shuffle spill only occurs during a sort-merge join; `groupBy` and `reduceByKey` never spill because they use hash maps

---

### Question 81 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: StreamingQueryListener — monitoring streaming queries programmatically

How do you register a `StreamingQueryListener` and what events does it receive?

- A) Attach a listener using `spark.streams.addListener(myListener)` where `myListener` extends `StreamingQueryListener`; it receives three callback events: `onQueryStarted(event)` — fired when a new streaming query begins; `onQueryProgress(event)` — fired at the end of each micro-batch with a `StreamingQueryProgress` object containing input rows, processing rate, batch duration, source offsets, and sink commit details; `onQueryTerminated(event)` — fired when a query stops (normally or with an exception); this is the standard way to export streaming metrics to external monitoring systems (Prometheus, Datadog) without polling the `StreamingQuery` object
- B) `StreamingQueryListener` is a Databricks-proprietary API; open-source Spark uses `SparkListener` for streaming metrics
- C) `onQueryProgress` is called once per partition, not once per micro-batch; the total events per batch equals the number of partitions
- D) `StreamingQueryListener` can only be registered before the SparkSession is created; listeners added after the session starts are ignored

---

### Question 82 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: mapGroupsWithState — custom stateful processing with timeout

What is the key behavioral difference between `mapGroupsWithState` and `flatMapGroupsWithState`?

- A) `mapGroupsWithState` operates on RDDs; `flatMapGroupsWithState` operates on DataFrames
- B) Both enable custom stateful processing where state is maintained per group key across micro-batches; the difference is the **output cardinality**: `mapGroupsWithState` requires returning **exactly one output record per group** per batch (even if the group had no new events — the function is called with an empty iterator for timeout-triggered calls); `flatMapGroupsWithState` can return **zero or more output records per group**, making it more flexible for use cases like sessionisation (emit nothing until a session closes) or event pattern detection; `flatMapGroupsWithState` also supports `OutputMode.Append` (in addition to `Update`), while `mapGroupsWithState` only supports `Update` mode
- C) `mapGroupsWithState` supports arbitrary state types; `flatMapGroupsWithState` only supports primitive state types
- D) `mapGroupsWithState` is evaluated after the watermark has advanced; `flatMapGroupsWithState` is evaluated before the watermark

---

### Question 83 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Kafka source — failOnDataLoss and offset gaps

What does `option("failOnDataLoss", "true")` do in a Kafka streaming source?

- A) It makes Spark fail if the Kafka broker becomes unreachable for more than 30 seconds
- B) `failOnDataLoss=true` (the default) causes the streaming query to **raise an exception** if Spark detects that offsets it was supposed to read are **no longer available** in Kafka — this happens when Kafka topics have a short retention period and messages expire before Spark has processed them, or when a topic partition is deleted; setting `failOnDataLoss=false` makes Spark skip the missing offsets and continue from the next available offset (logging a warning instead of failing); `false` is safer for production pipelines with aggressive Kafka retention, at the cost of potentially silently skipping unprocessed data
- C) `failOnDataLoss=true` prevents the consumer group from advancing its offset, ensuring no records are lost between restarts
- D) `failOnDataLoss` only applies to the Kafka sink; for the source, offset loss always causes a silent skip

---

### Question 84 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Streaming write — output modes (append, complete, update)

When must `outputMode("complete")` be used, and what is its limitation?

- A) `complete` mode must always be used with Kafka sinks; other modes are not supported for Kafka
- B) `outputMode("complete")` **rewrites the entire result table to the sink** on every micro-batch — it is required when the query uses **stateful aggregations without a watermark** (e.g., `groupBy().count()` where old groups can still be updated); because every aggregation result (including historical groups) is written each batch, `complete` mode is only suitable for **small result tables** (e.g., aggregations to an in-memory table or small console output); it is not suitable for append-only sinks like file systems (where overwriting the entire output every batch is impractical or impossible without special sink support)
- C) `complete` mode is identical to `update` mode but also flushes watermark state eagerly
- D) `complete` mode can be used with any aggregation; it is optional — `update` mode can always be substituted

---

### Question 85 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Trigger.Once() vs Trigger.AvailableNow() — batch-style streaming triggers

What is the difference between `Trigger.Once()` and `Trigger.AvailableNow()` in Structured Streaming?

- A) `Trigger.Once()` and `Trigger.AvailableNow()` are synonyms; the names were changed in Spark 3.3
- B) `Trigger.Once()` processes all available data in **a single micro-batch** and then stops the query — useful for scheduled batch-mode streaming jobs; `Trigger.AvailableNow()` (introduced in Spark 3.3) also processes all available data and stops, but does so using **multiple micro-batches** with the normal rate limits (e.g., `maxFilesPerTrigger`, `maxOffsetsPerTrigger`) applied per batch — this avoids the risk of a single enormous micro-batch overloading the cluster when a large backlog has accumulated; `AvailableNow` is preferred over `Once` for backfill scenarios
- C) `Trigger.Once()` processes only one record and stops; `Trigger.AvailableNow()` processes all available data in a loop until no new data arrives
- D) `Trigger.Once()` is deprecated in Spark 3.x; all users should migrate to `Trigger.ProcessingTime("0 seconds")` instead

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: maxFilesPerTrigger — controlling micro-batch input rate from file sources

What does `option("maxFilesPerTrigger", 10)` do in a streaming `spark.readStream.format("parquet")` source?

- A) It limits the total number of Parquet files that can ever be read by the streaming query
- B) `maxFilesPerTrigger` limits how many **new files** the streaming file source picks up in each micro-batch — Spark processes at most 10 new files per trigger regardless of how many new files have arrived; this is useful for controlling throughput and memory pressure when files arrive faster than Spark can process them, or when you want predictable, bounded micro-batch sizes; unprocessed files are queued and picked up in subsequent batches; the default is no limit (all available files per batch)
- C) `maxFilesPerTrigger` sets the maximum number of files that can be open simultaneously across all executor tasks
- D) This option only works with the `cloudFiles` (Auto Loader) format; standard Parquet streaming ignores it

---

### Question 87 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Stream-static join — semantics and limitations

What are the semantics of joining a streaming DataFrame with a static DataFrame, and what limitation exists?

- A) Stream-static joins are not supported; both sides of a streaming join must be streaming sources
- B) In a **stream-static join**, the streaming side is the changing input and the static side is treated as a fixed broadcast or hash lookup: for each micro-batch the streaming side's new rows are joined against the **full static DataFrame** (re-read from the static source each batch unless the static data is cached); output mode must be `append`; the result contains only rows matched in the current micro-batch; the limitation is that **late data from the streaming side that missed a match cannot be recovered** — if a streaming row arrives after the static table has been updated (with a matching key added), the earlier streaming row is already processed and gone; watermarks and state are also not maintained for the static side
- C) Stream-static joins always use broadcast hash join; if the static table exceeds `autoBroadcastJoinThreshold`, the query fails
- D) Stream-static joins accumulate results across all batches and support `complete` output mode; they behave identically to stream-stream joins

---

### Question 88 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Streaming sort — why global sort is unsupported in streaming

Why does Structured Streaming raise an `AnalysisException` when `orderBy()` is called on a streaming DataFrame?

- A) Sorting is not supported because streaming data arrives out-of-order and Spark cannot guarantee consistent results
- B) Structured Streaming cannot perform a **global sort** on an unbounded streaming DataFrame — global ordering requires seeing **all rows before emitting any output**, which is impossible for an infinite stream; Spark raises `AnalysisException: Sorting is not supported on streaming DataFrames/Datasets, unless it is on aggregated DataFrame/Dataset in Complete output mode`; sorting is allowed **within a streaming aggregation using `complete` mode** (where the full result table is written each batch); within-partition sort (`sortWithinPartitions`) is similarly unsupported in streaming because output order is not meaningful for sinks like Kafka or files
- C) Sorting requires a stable partition count; since streaming uses dynamic partitioning, Spark cannot plan a stable sort
- D) `orderBy` is supported in streaming when `trigger=Trigger.Once()`; the limitation only applies to continuous triggers

---

### Question 89 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Continuous processing trigger — low-latency streaming

What is Spark's **continuous processing** trigger (`Trigger.Continuous("1 second")`), and how does it differ from micro-batch processing?

- A) Continuous processing is another name for micro-batch with a 1-second interval; no architectural difference exists
- B) **Continuous processing** (experimental in Spark 3.x) runs tasks **continuously without forming discrete micro-batches** — tasks are long-running and each task asynchronously reads from the source, processes records, and writes to the sink record-by-record; the epoch interval (e.g., `"1 second"`) controls how frequently the system commits offsets and checkpoints state (not how often batches are processed); this achieves **end-to-end latency in the millisecond range** (vs seconds for micro-batch); limitations include: only `map`-like stateless operations are supported (no aggregations, no stream-stream joins, no watermarks), only certain sources and sinks (Kafka, Rate), and checkpointing is asynchronous; most production pipelines still use micro-batch due to its broader operator support
- C) Continuous processing processes each partition continuously in parallel; micro-batch processes all partitions in synchronised waves; both support all streaming operators equally
- D) Continuous processing is triggered once and then stops; the interval specifies the duration to run before terminating

---

### Question 90 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Rate source — testing streaming applications without external systems

What does `spark.readStream.format("rate").option("rowsPerSecond", 100).load()` produce, and what are the columns in the output?

- A) It generates random integers at 100 rows/second; the output has one column named `value`
- B) The **rate source** generates synthetic streaming data at a specified `rowsPerSecond` rate without any external dependency — it is designed for load testing and development; the output DataFrame has exactly **two columns**: `timestamp` (`TimestampType` — the time the row was generated) and `value` (`LongType` — a monotonically increasing counter starting from 0); `option("numPartitions", N)` controls parallelism; use it to test streaming query logic, watermarks, stateful operations, and sink behavior without needing Kafka or file sources
- C) The rate source emits rows from a built-in sample dataset at the specified rate; `rowsPerSecond` determines sampling frequency not generation rate
- D) The output has three columns: `timestamp`, `value`, and `partition_id`; `partition_id` indicates which executor generated each row

---

### Question 91 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect architecture — client-server separation

What fundamental architectural change does Spark Connect introduce compared to the classic Spark driver model?

- A) Spark Connect moves all query execution from the driver to the executors, making the driver a thin proxy
- B) Spark Connect introduces a **decoupled client-server architecture**: the Spark driver runs as a **remote gRPC server** and language clients (Python, Scala, Java, Go, R) communicate with it over the network via a well-defined gRPC/protobuf protocol; the client library constructs a **logical plan (unresolved plan tree)** locally and sends it to the server for analysis, optimisation, and execution — no Spark internals run in the client process; this means the Spark driver can be a shared, long-lived cluster service and clients can be arbitrary processes (notebooks, CI pipelines, microservices) connecting remotely, enabling **true multi-language support, driver isolation, and upgradeable server-side Spark without restarting client processes**
- C) Spark Connect replaces the JVM driver with a Python process, making PySpark the primary runtime for all languages
- D) Spark Connect is a thin wrapper around JDBC that allows any JDBC-compliant client to query Spark SQL without installing the Spark SDK

---

### Question 92 — Spark Connect

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark Connect — client-side plan construction and what executes locally vs remotely

In a Spark Connect Python client, when you call `df.filter(F.col("age") > 30)`, what happens locally in the client process vs on the server?

- A) The filter is executed immediately in the Python client using a local pandas-based engine; only the result is sent to the server for storage
- B) In Spark Connect, **the client builds an unresolved logical plan tree locally** — `df.filter(F.col("age") > 30)` creates a `Filter` node on top of the DataFrame's existing plan tree **in the client's memory** without any network call; no analysis, optimisation, or execution occurs client-side; when an **action** (e.g., `df.show()`, `df.collect()`, `df.write`) is triggered, the client **serialises the entire plan as a protobuf message and sends it to the Spark Connect server** via gRPC; the server analyses, optimises, and executes it, streaming results back; this deferred plan-construction model allows complex query chains to be built offline and submitted atomically
- C) Each DataFrame transformation immediately sends a gRPC call to the server; results are eagerly evaluated and cached locally in the client
- D) Client-side plan construction only applies to Python; Scala and Java Spark Connect clients execute transformations eagerly on the server

---

### Question 93 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect — gRPC transport and SSL configuration

How is a secure TLS/SSL connection configured when using the Spark Connect Python client?

- A) Spark Connect always uses unencrypted HTTP/2; TLS is handled by the network infrastructure (e.g., a service mesh)
- B) The Spark Connect Python client accepts the connection URL in the format `sc://<host>:<port>/;use_ssl=true` — setting `use_ssl=true` in the connection string enables TLS for the underlying gRPC channel; additional TLS options (custom CA certificate, client certificate for mTLS) can be passed as extra parameters in the URL (`token=<bearer_token>` for token authentication); to create the session: `SparkSession.builder.remote("sc://myhost:15002/;use_ssl=true;token=mytoken").getOrCreate()`; Databricks clusters expose Spark Connect with token auth and TLS enabled by default via the workspace URL
- C) TLS is configured via `spark.ssl.enabled=true` in the server-side `spark-defaults.conf`; the client automatically negotiates TLS without any client-side configuration
- D) Spark Connect uses HTTP/1.1 with Basic Auth; SSL is not supported because gRPC requires HTTP/2 which conflicts with Spark's Netty transport

---

### Question 94 — Spark Connect

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Spark Connect — limitations vs classic Spark (SparkContext APIs, RDDs, addFile)

Which of the following operations is NOT available through the Spark Connect client API?

- A) `df.select()`, `df.groupBy().agg()`, and `df.write.parquet()`
- B) **`SparkContext`-level APIs** are not available through Spark Connect — this includes `sc.parallelize()`, `sc.addFile()`, `sc.addJar()`, `sc.broadcast()` (low-level), `sc.setCheckpointDir()`, and direct RDD operations; Spark Connect exposes only the **DataFrame/Dataset/SQL** surface area through its gRPC protocol; RDD operations require direct access to the driver JVM which is not exposed over gRPC; this is by design — Spark Connect is a structured API layer; applications that depend on RDD APIs or `SparkContext` methods must use the classic (non-Connect) driver mode
- C) `spark.sql()`, `spark.read.parquet()`, and `spark.createDataFrame()`
- D) Scalar UDFs and Pandas UDFs registered via `spark.udf.register()`

---

### Question 95 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect — multi-language support and versioning

What advantage does Spark Connect's protobuf-based protocol provide for language client versioning?

- A) Protobuf ensures all language clients must be the same version as the server; mismatched versions are rejected
- B) Spark Connect's gRPC/protobuf protocol provides **forward and backward compatibility** for language clients — because protobuf fields are identified by field numbers (not names), older clients can send plans to newer servers (unknown fields are ignored) and newer clients can connect to older servers (missing fields use default values); this means the **Python, Scala, Java, and Go clients can be versioned independently from the Spark server** — a new language feature can be added to the Python client library without requiring a Spark server upgrade, as long as the server already supports the underlying plan nodes; this decoupling is a key operational advantage for managed Spark services where server upgrades lag client library releases
- C) Protobuf forces all clients to re-compile against the server's proto definition on every Spark version upgrade
- D) Versioning is not a concern because Spark Connect clients must always exactly match the Spark Connect server version

---

### Question 96 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: ps.from_pandas() — converting a Pandas DataFrame to a Spark Pandas-on-Spark DataFrame

How do you convert an existing `pandas.DataFrame` named `pdf` to a Pandas API on Spark DataFrame?

- A) `spark.createDataFrame(pdf)` — this returns a Pandas-on-Spark DataFrame directly
- B) `ps.from_pandas(pdf)` — this is the canonical function in the `pyspark.pandas` (aliased as `ps`) module to convert a `pandas.DataFrame` to a Pandas API on Spark DataFrame; the resulting object exposes the `pandas`-compatible API while executing on the Spark engine; `ps.DataFrame(pdf)` is equivalent; the inverse is `.to_pandas()` to get back a native Pandas DataFrame
- C) `pdf.to_spark()` — Pandas DataFrames have a built-in method for converting to Spark
- D) `ps.read_pandas(pdf)` — the `read_*` family is the only supported ingestion path for Pandas API on Spark

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ops_on_diff_frames — enabling operations across separate Pandas-on-Spark DataFrames

Why does `psdf1 + psdf2` raise an error by default when `psdf1` and `psdf2` were created independently?

- A) The `+` operator is not defined for Pandas API on Spark DataFrames; use `ps.concat([psdf1, psdf2])` instead
- B) By default, operations between two **separately created** Pandas API on Spark DataFrames (i.e., DataFrames with different internal index origins) are disallowed because aligning them requires a potentially expensive **shuffle join**; the error `ValueError: Cannot combine the series or dataframe because it comes from a different dataframe` is raised; enabling `ps.set_option("compute.ops_on_diff_frames", True)` permits such operations by implicitly joining on the index — this makes the code work but may introduce unexpected shuffles and slower performance; use with awareness of the cost
- C) The error occurs because `psdf1` and `psdf2` must share the same column names for `+` to work
- D) Cross-DataFrame operations are disabled only in local mode; in cluster mode they work by default

---

### Question 98 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.get_dummies() — one-hot encoding in Pandas API on Spark

What does `ps.get_dummies(psdf, columns=["category"])` return?

- A) A single new column named `category_encoded` with integer label encoding
- B) `ps.get_dummies(psdf, columns=["category"])` performs **one-hot encoding** — it replaces the `category` column with multiple binary columns, one per distinct value, named `category_<value>` (e.g., `category_A`, `category_B`, `category_C`); each binary column is `1` if the row had that value and `0` otherwise; the original `category` column is dropped; `null` values in the column are represented as all-zeros across the new columns by default (unlike the `dummy_na=True` variant which adds a `category_nan` column); the behavior and API mirror `pandas.get_dummies`
- C) `get_dummies` returns a `ps.Series` of integers mapping each category to an ordinal index
- D) `get_dummies` is not available in the Pandas API on Spark; use `StringIndexer` from MLlib instead

---

### Question 99 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ps.options.compute.shortcut_limit — eager computation for small DataFrames

What does `ps.options.compute.shortcut_limit` control?

- A) It sets the maximum number of rows in a Pandas-on-Spark DataFrame before computations are distributed to executors
- B) `ps.options.compute.shortcut_limit` (default 1000) controls when the Pandas API on Spark **short-circuits to local Pandas computation** — if a Pandas API on Spark DataFrame has fewer rows than this limit, certain operations (e.g., head display in notebooks, repr) are computed by **collecting the data to the driver and using native Pandas** rather than distributing the work across the cluster; this avoids Spark job overhead for trivially small DataFrames; setting it to `0` disables the short-circuit (all operations go through Spark regardless of size); setting it higher can make interactive notebook exploration faster for small DataFrames
- C) `shortcut_limit` is the maximum number of columns allowed before `repr()` truncates the display
- D) It controls the maximum number of Spark tasks used for Pandas API on Spark operations; operations requiring more tasks than this limit fall back to Pandas

---

### Question 100 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Distributed-sequence index vs default index — index type implications

What is the difference between `default_index_type = "sequence"` and `default_index_type = "distributed-sequence"` in Pandas API on Spark?

- A) `sequence` uses an integer index starting at 0; `distributed-sequence` uses a UUID per row; both are equivalent for joins
- B) The **`sequence`** index type assigns globally unique, **consecutive integers starting from 0** to each row (0, 1, 2, …) — this requires a **full sort/shuffle** to assign stable consecutive IDs, making it expensive for large DataFrames but producing a pandas-compatible integer index; the **`distributed-sequence`** index type (the default in Pandas API on Spark) assigns integers that are **unique but not necessarily consecutive or globally ordered** — it uses monotonically increasing IDs within each partition, avoiding the shuffle; the result is a valid unique index but not a contiguous range, so operations that rely on positional access (e.g., `iloc[5]`) may produce unexpected results; `ps.set_option("compute.default_index_type", "sequence")` switches to the expensive but pandas-compatible sequential index
- C) `sequence` distributes row numbering across partitions; `distributed-sequence` computes the full sequential order on the driver
- D) Both index types use consecutive integers; the difference is only the starting value (`sequence` starts at 0, `distributed-sequence` starts at 1)

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | Tasks in a stage = number of input partitions; each task processes exactly one partition. | topic1-prompt1-spark-architecture.md | Easy |
| 2 | B | Unified memory: execution can evict storage blocks above the storageFraction watermark. | topic1-prompt1-spark-architecture.md | Hard |
| 3 | B | Sort-based shuffle writes exactly 1 data file + 1 index file per mapper, reducing file count. | topic1-prompt4-shuffling-performance.md | Hard |
| 4 | B | `TaskContext.get().partitionId()` and `.attemptNumber()` expose task metadata inside a task. | topic1-prompt1-spark-architecture.md | Hard |
| 5 | B | Barrier mode requires all tasks in the stage to start simultaneously before any can proceed. | topic1-prompt1-spark-architecture.md | Hard |
| 6 | B | Default scheduler is FIFO; FAIR scheduler interleaves tasks from concurrent jobs. | topic1-prompt1-spark-architecture.md | Hard |
| 7 | B | `spark.python.worker.reuse=true` keeps Python workers alive and reuses them across tasks. | topic1-prompt1-spark-architecture.md | Hard |
| 8 | B | `checkpoint()` truncates RDD lineage; `persist(DISK_ONLY)` writes to disk but retains lineage. | topic1-prompt6-fault-tolerance.md | Hard |
| 9 | B | Locality preference degrades: PROCESS_LOCAL → NODE_LOCAL → RACK_LOCAL → ANY after wait timeouts. | topic1-prompt1-spark-architecture.md | Hard |
| 10 | B | UnsafeRow is an off-heap binary row format (Tungsten) that avoids Java object GC pressure. | topic1-prompt1-spark-architecture.md | Hard |
| 11 | B | `spark.executor.instances` is ignored when dynamic allocation is enabled. | topic1-prompt1-spark-architecture.md | Medium |
| 12 | B | `spark.sql.files.maxPartitionBytes` caps the maximum data read per input partition. | topic1-prompt1-spark-architecture.md | Medium |
| 13 | B | Accumulators double-count when a task is retried; use only in actions or idempotently. | topic1-prompt1-spark-architecture.md | Medium |
| 14 | B | `spark.sql.broadcastTimeout` is the max driver wait time to complete a broadcast; exceeded = SparkException. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 15 | B | spark-submit CLI flags take highest precedence, overriding spark-defaults.conf and programmatic config. | topic1-prompt1-spark-architecture.md | Medium |
| 16 | B | `spark.rdd.compress=true` compresses serialized RDD partitions on disk/memory at CPU cost. | topic1-prompt1-spark-architecture.md | Medium |
| 17 | B | `spark.driver.maxResultSize` caps the total serialized size of results returned to the driver. | topic1-prompt1-spark-architecture.md | Medium |
| 18 | B | `sc.addFile()` distributes files accessible via `SparkFiles.get()`; `sc.addJar()` adds JARs to executor classpaths. | topic1-prompt1-spark-architecture.md | Medium |
| 19 | B | With Arrow enabled, `toPandas()` and `createDataFrame()` use columnar Arrow IPC format for transfer. | topic1-prompt1-spark-architecture.md | Medium |
| 20 | B | `spark.ui.retainedJobs` and `retainedStages` control the in-memory Web UI history cache size (FIFO eviction). | topic1-prompt1-spark-architecture.md | Medium |
| 21 | B | `regexp_replace` returns the original string unchanged when the pattern has no match. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 22 | B | `overlay(str, replace, pos, len)` replaces a substring by position, not by regex pattern. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 23 | B | `soundex()` returns a 4-character phonetic code useful for fuzzy name matching. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 24 | B | `initcap()` uppercases the first letter of each word (title case); `upper()`/`lower()` affect the whole string. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 25 | B | `array_distinct()` returns unique elements; `null` is treated as a distinct value and retained once. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 26 | B | `array_union`, `array_intersect`, `array_except` perform set operations on arrays and deduplicate results. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 27 | B | `aggregate()` is a fold-left; the optional `finish` lambda post-processes the final accumulator value. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 28 | B | `forall()` is vacuously true for an empty array; `exists()` returns false for an empty array. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 29 | B | `zip_with()` result length equals the longer array; the shorter array is padded with null. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 30 | B | `map_from_entries()` expects an array of 2-field structs; duplicate keys keep the last value. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 31 | A | `to_utc_timestamp` converts local→UTC; `from_utc_timestamp` converts UTC→local; they are inverses. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 32 | B | `make_date()` returns null for invalid date inputs rather than throwing an exception. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 33 | B | `datediff` returns integer days between dates; `timestampdiff` supports any time unit. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 34 | B | `sequence(start, stop, step)` with a negative step generates a descending integer array. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 35 | B | `try_cast` returns null instead of an error; `try_divide` returns null for division by zero. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 36 | B | `UNPIVOT` (Spark 3.4+ SQL) rotates wide columns into rows without a self-join workaround. | topic2-prompt8-spark-sql-fundamentals.md | Hard |
| 37 | B | `QUALIFY` filters on window function results inline, avoiding a subquery (Spark 3.3+). | topic2-prompt10-window-functions.md | Hard |
| 38 | B | `TABLESAMPLE(10 PERCENT)` is Bernoulli ~10%; `TABLESAMPLE(100 ROWS)` returns at most 100 rows. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 39 | B | `LATERAL VIEW OUTER` preserves rows where the array is null or empty (OUTER semantics). | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 40 | B | `schema_of_json()` returns a DDL-format schema string, typically used as input to `from_json()`. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 41 | B | `write.mode("overwrite")` deletes the existing output directory entirely before writing new data. | topic3-prompt19-reading-writing-dataframes.md | Easy |
| 42 | B | `write.partitionBy()` creates a Hive-style directory tree; partition columns are removed from the data files. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 43 | B | `bucketBy(64, "user_id")` creates 64 fixed buckets; joins on matching-bucket tables skip the shuffle phase. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 44 | B | `writeTo()` uses the DataSource V2 API with richer semantics such as `overwrite(condition)`. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 45 | B | `df.observe()` attaches named aggregate metrics computed in a single pass during the action. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 46 | B | `df.stat.freqItems()` uses the Misra-Gries approximate algorithm; `support` is the minimum frequency fraction. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 47 | B | Pandas scalar UDFs receive a `pandas.Series` per Arrow batch; typically 10–100× faster than row UDFs. | topic3-prompt22-udfs.md | Hard |
| 48 | B | `localCheckpoint()` materialises on executor local disk without HDFS; not fault-tolerant across executor loss. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 49 | B | `F.input_file_name()` returns the fully qualified source file path for each row. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 50 | B | `df.stat.crosstab()` returns a contingency table: col1 values = rows, col2 values = columns, cell = count. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 51 | B | `regexp_extract()` returns `""` when there is no match; returns null only when the input is null. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 52 | B | `split(str, pattern, limit)` produces at most `limit` elements (at most `limit-1` splits). | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 53 | B | `from_json()` is permissive: missing fields → null, extra fields ignored, malformed input → all-null struct. | topic3-prompt21-schemas-data-types.md | Hard |
| 54 | B | `StructType.fromDDL()` parses a DDL string into a StructType; all fields are nullable by default. | topic3-prompt21-schemas-data-types.md | Hard |
| 55 | B | `randomSplit()` is reproducible with a seed; cache the source DataFrame to avoid double-scanning. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 56 | B | `df.hint("broadcast")` forces a broadcast hash join regardless of the auto-broadcast size threshold. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 57 | B | `encode(str, charset)` returns Binary; `decode(binary, charset)` returns String using the named charset. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 58 | B | `maxRecordsPerFile` limits the number of rows per output file, splitting within a task if needed. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 59 | B | `sortWithinPartitions()` sorts locally with no shuffle; `orderBy()` performs a global sort with a shuffle. | topic3-prompt20-repartition-coalesce.md | Hard |
| 60 | A | `na.drop(how="all")` drops rows where all columns are null; `na.drop(how="any")` drops rows where any column is null. | topic3-prompt16-handling-nulls.md | Hard |
| 61 | B | After a join, reference an ambiguous column via `df1["id"]` directly or pass a column list `["id"]` to `join()`. | topic3-prompt17-joins.md | Hard |
| 62 | B | A `when()` chain with no `.otherwise()` returns null for all rows that match no condition. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 63 | B | `df.select(F.col("*"), new_col)` returns all existing columns plus the new column appended. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 64 | B | `withColumn()` called with an existing column name silently overwrites that column. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 65 | B | `df.toDF(*names)` renames all columns in order; a count mismatch raises an AnalysisException. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 66 | B | `df1.crossJoin(df2)` produces the Cartesian product (M × N rows); enabled by default in Spark 3.x. | topic3-prompt17-joins.md | Hard |
| 67 | B | `selectExpr()` accepts SQL expression strings parsed by Catalyst; `"*"` expands to all columns. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 68 | B | `distinct()` deduplicates on all columns; `dropDuplicates(["id"])` deduplicates on the specified subset. | topic3-prompt14-filtering-row-manipulation.md | Medium |
| 69 | B | `F.expr()` is preferred for complex SQL expressions; `F.col() + F.col()` is cleaner for simple arithmetic. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 70 | B | `rowsBetween` counts physical row positions; `rangeBetween` includes all rows with tied ORDER BY values. | topic2-prompt10-window-functions.md | Hard |
| 71 | A | AQE coalesce requires `coalescePartitions.enabled=true`; it merges small adjacent post-shuffle partitions. | topic4-prompt24-performance-tuning.md | Hard |
| 72 | B | `storageFraction` sets the fraction of unified memory protected from eviction by execution; default 0.5. | topic4-prompt24-performance-tuning.md | Hard |
| 73 | B | `EXPLAIN CODEGEN` shows the generated Java source code for each Whole-Stage Code Generation pipeline. | topic4-prompt26-debugging.md | Hard |
| 74 | B | Skew join causes straggler tasks; AQE splits skewed partitions and replicates the matching side. | topic4-prompt24-performance-tuning.md | Medium |
| 75 | B | Set `spark.sql.autoBroadcastJoinThreshold=-1` to completely disable automatic broadcast joins. | topic4-prompt24-performance-tuning.md | Medium |
| 76 | B | Both ORC and Parquet are columnar; Parquet is the ecosystem standard; ORC is preferred for Hive ACID. | topic4-prompt24-performance-tuning.md | Hard |
| 77 | B | G1GC is recommended for large executor heaps; enable with `-XX:+UseG1GC` in `executor.extraJavaOptions`. | topic1-prompt7-garbage-collection.md | Hard |
| 78 | B | `inMemoryColumnarStorage.batchSize` controls the number of rows per columnar cache batch (default 10000). | topic4-prompt24-performance-tuning.md | Medium |
| 79 | B | `spark.eventLog.enabled=true` writes all application events to `spark.eventLog.dir` for the History Server. | topic4-prompt26-debugging.md | Medium |
| 80 | B | Shuffle spill is confirmed by `Shuffle Spill (Memory)` and `Shuffle Spill (Disk)` metrics in the Stage UI. | topic4-prompt24-performance-tuning.md | Medium |
| 81 | A | Register a listener with `spark.streams.addListener()`; callbacks: `onQueryStarted`, `onQueryProgress`, `onQueryTerminated`. | topic5-prompt27-structured-streaming.md | Hard |
| 82 | B | `mapGroupsWithState` emits exactly 1 record per group; `flatMapGroupsWithState` emits 0 or more records. | topic5-prompt28-stateful-streaming.md | Hard |
| 83 | B | `failOnDataLoss=true` raises an exception when Kafka offsets are missing; `false` skips them with a warning. | topic5-prompt27-structured-streaming.md | Hard |
| 84 | B | `complete` mode rewrites the entire result table each micro-batch; required for aggregations without a watermark. | topic5-prompt27-structured-streaming.md | Medium |
| 85 | B | `Trigger.Once()` processes all data in one micro-batch then stops; `Trigger.AvailableNow()` uses multiple rate-limited micro-batches. | topic5-prompt27-structured-streaming.md | Medium |
| 86 | B | `maxFilesPerTrigger` limits new files consumed per micro-batch; excess files queue for the next batch. | topic5-prompt27-structured-streaming.md | Medium |
| 87 | B | Stream-static join re-reads the static side each batch; late streaming rows that missed a match cannot be recovered. | topic5-prompt27-structured-streaming.md | Hard |
| 88 | B | Global `orderBy()` on a streaming DataFrame raises AnalysisException because all rows must be seen first. | topic5-prompt27-structured-streaming.md | Medium |
| 89 | B | Continuous processing runs tasks without micro-batches achieving millisecond latency; only stateless ops supported. | topic5-prompt27-structured-streaming.md | Hard |
| 90 | B | The rate source produces two columns: `timestamp` (TimestampType) and `value` (LongType counter from 0). | topic5-prompt27-structured-streaming.md | Medium |
| 91 | B | Spark Connect decouples client and driver via a gRPC/protobuf client-server architecture. | topic6-prompt29-spark-connect.md | Medium |
| 92 | B | Transformations build a local plan tree; only an action serialises the plan as protobuf and sends it to the server. | topic6-prompt29-spark-connect.md | Hard |
| 93 | B | TLS is configured with `use_ssl=true` in the Spark Connect URL: `sc://host:port/;use_ssl=true`. | topic6-prompt29-spark-connect.md | Medium |
| 94 | B | SparkContext-level APIs (RDD, `addFile`, `addJar`, `sc.parallelize`) are not available via Spark Connect. | topic6-prompt29-spark-connect.md | Hard |
| 95 | B | Protobuf field numbering provides forward/backward compatibility; clients and server can be versioned independently. | topic6-prompt29-spark-connect.md | Medium |
| 96 | B | `ps.from_pandas(pdf)` converts a `pandas.DataFrame` to a Pandas API on Spark DataFrame. | topic7-prompt30-pandas-api.md | Easy |
| 97 | B | Cross-DataFrame operations raise an error by default; enable with `ps.set_option("compute.ops_on_diff_frames", True)`. | topic7-prompt30-pandas-api.md | Medium |
| 98 | B | `ps.get_dummies()` performs one-hot encoding, adding a binary column per distinct category value. | topic7-prompt30-pandas-api.md | Medium |
| 99 | B | `shortcut_limit` (default 1000) short-circuits small DataFrames to local Pandas to avoid Spark job overhead. | topic7-prompt30-pandas-api.md | Hard |
| 100 | B | `sequence` index assigns consecutive integers (requires a shuffle); `distributed-sequence` assigns unique but non-consecutive integers per partition. | topic7-prompt30-pandas-api.md | Hard |
