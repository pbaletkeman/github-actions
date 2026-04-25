# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 10)

**Iteration**: 10
**Generated**: 2025-04-25
**Total Questions**: 100
**Difficulty Split**: 10 Easy / 54 Medium / 36 Hard
**Answer Types**: 100 `one`

---

## Questions

---

### Question 1 — Apache Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.sql.autoBroadcastJoinThreshold — default 10 MiB, size-based broadcast hash join activation

**Question**:
By default, Spark automatically selects a broadcast hash join when the estimated size of one join side is within what limit?

- A) 100 MiB — configured via `spark.broadcast.threshold`
- B) 10 MiB (10 485 760 bytes) — the default value of `spark.sql.autoBroadcastJoinThreshold`; Spark's Catalyst optimizer checks estimated table statistics and broadcasts the smaller side when it fits within this limit; setting the value to `-1` disables automatic broadcasting (correct answer — `spark.sql.autoBroadcastJoinThreshold` defaults to 10 MiB. When statistics indicate that one side of a join is at or below this size, Spark replaces a SortMergeJoin with a BroadcastHashJoin. AQE can also trigger a runtime broadcast conversion when it discovers a side is small after observing shuffle stage sizes.)
- C) 1 GiB — broadcasting is only triggered for very small dimension tables
- D) The threshold is dynamic; AQE adjusts it at runtime based on available executor memory

---

### Question 2 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.memory.fraction + spark.memory.storageFraction — unified memory pool fractions and soft eviction boundary

**Question**:
A developer wants to give more heap space to execution (shuffle, joins, aggregation) at the expense of cache memory in Spark's unified memory manager. Which two settings control this trade-off?

- A) `spark.executor.heapFraction` and `spark.executor.cacheFraction` — the dedicated settings for execution and storage memory
- B) `spark.memory.fraction` (default 0.6) determines what fraction of the JVM heap above reserved user memory forms the unified pool; `spark.memory.storageFraction` (default 0.5) sets the soft eviction boundary — storage memory will not be evicted below this fraction of the unified pool; lowering `storageFraction` lets execution memory claim more of the pool before evicting cached blocks (correct answer — In Spark's unified memory manager the heap is divided as: ~300 MiB reserved, `(1 - fraction) × heap` user memory, and `fraction × heap` unified pool. Within the unified pool, storage and execution share freely; `storageFraction` is a soft floor below which storage will not be shrunk by execution pressure, protecting the most-recently-used cached partitions.)
- C) `spark.executor.memory` sets the total heap; the split between storage and execution is always 50/50 and cannot be changed
- D) `spark.memory.useLegacyMode=true` must be enabled before any fraction settings are respected

---

### Question 3 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.executor.memoryOverheadFactor vs spark.executor.memoryOverhead — precedence and effective overhead calculation

**Scenario**:
An executor is configured with `spark.executor.memory=8g`. A developer wants to reserve 15% of executor heap for off-heap overhead (Python workers, native libraries) instead of the fixed 384 MiB minimum.

**Question**:
Which configuration achieves this, and what is the effective overhead when `spark.executor.memory=8g`?

- A) Set `spark.executor.memoryOverhead=0.15g`; fractional MiB values are supported
- B) Set `spark.executor.memoryOverheadFactor=0.15`; the effective overhead = `max(0.15 × 8192, 384)` = **1228 MiB**; if `spark.executor.memoryOverhead` is also set explicitly it takes precedence and `memoryOverheadFactor` is ignored (correct answer — Introduced in Spark 3.3, `spark.executor.memoryOverheadFactor` (default 0.1) calculates overhead as `max(factor × spark.executor.memory, 384 MiB)`. At 8 GiB that yields `max(819, 384) = 819 MiB` at 0.1 and `max(1228, 384) = 1228 MiB` at 0.15. Setting `spark.executor.memoryOverhead` (absolute MiB) overrides the factor calculation entirely.)
- C) `spark.executor.memoryOverheadFactor` was removed in Spark 3.0; only the fixed `spark.executor.memoryOverhead` remains valid
- D) `spark.executor.extraJavaOptions="-Xss0.15g"` must be combined with `spark.executor.memoryOverhead`

---

### Question 4 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.io.compression.codec — default lz4, supported codec options for shuffle and broadcast compression

**Question**:
Which codec does Spark use by default to compress shuffle files, broadcast variables, and serialized RDD partitions, and how can a developer switch to Zstandard for improved compression ratios?

- A) `snappy` is the default codec; switching requires setting `spark.io.compression.codec=snappy`
- B) `lz4` is the default for `spark.io.compression.codec`; supported options include `lz4`, `lzf`, `snappy`, `zstd`, and `none`; setting `spark.io.compression.codec=zstd` enables Zstandard; per-subsystem flags (`spark.shuffle.compress`, `spark.broadcast.compress`) still control whether compression is applied at all (correct answer — `lz4` (lz4-java) has been the default since Spark 1.4 due to its fast compression/decompression speed. `zstd` offers better compression ratios at comparable or better throughput on modern hardware. `snappy` was the pre-1.4 default and remains common. Changing the global codec affects all subsystems unless overridden.)
- C) `gzip` is the default because it is built into the JDK; `lz4` requires a native library pre-installed on every executor
- D) Compression is disabled by default; all codecs must be explicitly enabled per workload

---

### Question 5 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.exchange.reuse — ReusedExchangeExec eliminates redundant shuffle computations

**Scenario**:
A query groups a large DataFrame by column `user_id` for aggregation and then self-joins the aggregated result on `user_id`. The physical plan shows two `ShuffleExchangeExec` nodes with identical hash partitioning on `user_id`.

**Question**:
What does `spark.sql.exchange.reuse=true` (default) do to this physical plan?

- A) Spark eliminates one `ShuffleExchangeExec` by pushing the aggregation below the join via predicate rewriting
- B) The `ReuseExchange` optimizer rule replaces the second identical `ShuffleExchangeExec` with a `ReusedExchangeExec` wrapper; the shuffle is computed once and its materialized output is consumed by both downstream operators, eliminating the redundant shuffle stage entirely (correct answer — `ReuseExchange` (enabled by `spark.sql.exchange.reuse=true`) scans the physical plan for Exchange nodes with matching `outputPartitioning` and `outputOrdering`. Duplicates are replaced with `ReusedExchangeExec` which reads from the first exchange's already-materialized result. This is especially valuable in self-joins and multi-branch aggregations over the same keyed shuffle.)
- C) Spark buffers the first shuffle output on the driver so the second branch can read from the driver memory
- D) `spark.sql.exchange.reuse` only applies to `BroadcastExchangeExec` nodes, not `ShuffleExchangeExec`

---

### Question 6 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.adaptive.coalescePartitions.minPartitionSize — floor size for AQE-coalesced partitions

**Question**:
AQE is enabled with an advisory partition size of 64 MiB. After coalescing, many result partitions are still only 200 KB. Which configuration ensures AQE does not stop merging before reaching a meaningful minimum size?

- A) `spark.sql.adaptive.coalescePartitions.minPartitionNum` — sets the minimum number of post-coalesce partitions, not the minimum size per partition
- B) `spark.sql.adaptive.coalescePartitions.minPartitionSize` (default 1 MiB, Spark 3.2+) is the minimum acceptable size for any single coalesced partition; AQE continues merging consecutive small partitions until the combined size meets this floor, preventing the creation of many tiny tasks even when the advisory target is not yet reached (correct answer — Without this floor, AQE might stop coalescing once a partition pair exceeds the advisory size, leaving adjacent sub-MiB partitions untouched. `minPartitionSize` enforces a lower bound on partition granularity, complementing the advisory upper-bound target and the `minPartitionNum` lower count bound.)
- C) `spark.sql.adaptive.advisoryPartitionSizeInBytes` also serves as the minimum; AQE never creates partitions smaller than this value
- D) Setting `spark.sql.shuffle.partitions=1` is the only way to guarantee all data ends up in one large partition

---

### Question 7 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.shuffle.service.db.enabled — LevelDB-backed persistence for external shuffle service registrations

**Scenario**:
An external shuffle service pod on a Kubernetes worker node is restarted. Shuffle files for an in-progress Spark job still exist on local disk on that node.

**Question**:
What configuration allows the restarted shuffle service to resume serving those files without requiring a full stage re-computation?

- A) `spark.storage.replication.proactive=true` — proactively replicates shuffle blocks to another node before any restart
- B) Setting `spark.shuffle.service.db.enabled=true` (Spark 3.x, default false) persists shuffle block registration state to a local LevelDB database; when the service restarts it reads the database and resumes serving blocks whose underlying files survived on local disk, avoiding a full stage re-computation (correct answer — Without `db.enabled=true`, all block registrations are held only in the shuffle service JVM heap and are lost on restart. With it enabled, registrations survive restarts as long as the local disk files exist. This is critical for long-running jobs on clusters with frequent node-agent restarts or upgrades.)
- C) `spark.shuffle.service.enabled=true` automatically handles service restarts; no extra persistence configuration is needed
- D) External shuffle service state is always persisted to HDFS; restarts recover automatically

---

### Question 8 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark UI task timeline — scheduler delay definition and causes of high values

**Question**:
The Spark UI task timeline shows 1.8 seconds of "Scheduler Delay" at the start of many short tasks. What does this metric measure and what typically causes elevated values?

- A) Scheduler delay measures how long an executor waits for all upstream input partitions to be ready before it can start reading
- B) Scheduler delay is the time between when the TaskScheduler marks a task as submitted and when the executor begins executing it; it covers task serialization time on the driver, network transmission to the executor, and any executor-side queuing; high values indicate large captured closures (over-serialized variables), slow driver-to-executor network, or thread-pool contention in the executor (correct answer — In the Spark UI, the task timeline bar shows executor compute time in blue, plus surrounding overhead phases. Scheduler delay appears at both the beginning (launch overhead) and occasionally the end (result delivery). Closures that capture large objects serialized per-task, or many concurrent task launches on a congested network, are the most common causes.)
- C) Scheduler delay represents the shuffle write time after a task finishes, before the reducer stage can begin
- D) Scheduler delay is the time Catalyst spends optimizing the query for the stage that contains the task

---

### Question 9 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.optimizer.maxIterations — Catalyst rule-batch fixed-point iteration limit

**Question**:
A developer sees: `AnalysisException: Max iterations (100) reached for batch Resolution`. What does this error mean and how should it be resolved?

- A) The query joins more than 100 tables; exceeding the join count limit causes the analysis phase to abort
- B) Catalyst applies transformation rules in batches, repeatedly until the plan reaches a fixed point or `spark.sql.optimizer.maxIterations` (default 100) is reached; this error means the plan did not converge; increasing `spark.sql.optimizer.maxIterations` gives Catalyst more iterations to resolve highly complex or deeply nested queries (correct answer — Catalyst's analysis and optimization phases iterate rule batches until the plan stops changing. Extremely complex subqueries, deeply nested correlated subqueries, or ambiguous multi-level views can prevent convergence within 100 iterations. The setting was `spark.sql.optimizer.maxIterations` in older versions; in Spark 3.x this also covers the resolution batch. Increasing to 200–500 is a common fix.)
- C) The error means the query used 100 window functions which exceeds the analytical function limit
- D) `spark.sql.optimizer.maxIterations` controls the number of physical plan candidates evaluated; disabling CBO resolves the overflow

---

### Question 10 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SparkSession.newSession() — isolated SQL environment sharing the same SparkContext

**Question**:
A multi-tenant notebook application needs isolated temp view namespaces and SQL config overrides per user without the overhead of separate SparkContexts. What is the correct approach?

- A) `spark.sql("CREATE SESSION user_session")` — SQL syntax for creating a named isolated session
- B) `SparkSession.newSession()` creates a new SparkSession that shares the underlying `SparkContext` and Spark defaults with the parent but has its own isolated: temporary views, SQL configuration settings, and registered UDFs; changes in one session's temp views are not visible to another (correct answer — `newSession()` is the standard pattern for multi-tenant session isolation. The underlying `SparkContext` (cluster resources) is shared, avoiding resource duplication, while logical namespace separation is maintained per session. Global temp views remain shared across all sessions via the `global_temp` database.)
- C) `SparkSession.builder.config("spark.session.isolated", "true").getOrCreate()` creates a fully new isolated context
- D) `SparkSession.clone()` returns a deep copy of the session including all cached DataFrames and registered functions

---

### Question 11 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.parquet.mergeSchema — schema evolution across Parquet files with different column sets

**Question**:
A Parquet dataset was written in two batches: the first batch has columns `(id, name)` and the second has columns `(id, name, email)`. With `spark.sql.parquet.mergeSchema=true`, what happens when Spark reads all files together?

- A) Spark raises an `AnalysisException` because the schemas are incompatible and cannot be merged
- B) Spark reads the footer schema from all Parquet files and merges them into a union schema `(id, name, email)`; rows from files that lack the `email` column return `null` for that field; enabling schema merging incurs additional I/O to read footers from all files before the scan (correct answer — `spark.sql.parquet.mergeSchema` (default false) must be explicitly enabled, or passed per-read via `spark.read.option("mergeSchema", "true")`. Spark reads footer metadata from every file to build the merged schema, then scans with column projection. For large datasets the footer-reading pass can be significant; partition-level schema discovery via `spark.sql.parquet.mergeSchema` can be scoped more narrowly with `basePath`.)
- C) Spark uses the schema of the first file encountered; extra columns in later files are silently dropped
- D) Schema merging only works when all files are in the same directory; subdirectory files are excluded

---

### Question 12 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.parquet.filterPushdown — row-group skipping via column statistics in Parquet

**Question**:
A query runs `WHERE event_date > '2024-01-01'` against a large Parquet table. With `spark.sql.parquet.filterPushdown=true` (default), what physical optimization does the Parquet reader apply?

- A) Spark broadcasts a compact bit-vector of matching row IDs to all executors before scanning begins
- B) The Parquet reader checks each row group's min/max statistics for the `event_date` column and skips entire row groups where the maximum value is ≤ `'2024-01-01'`, avoiding deserializing those row groups entirely (correct answer — Parquet stores per-row-group statistics (min, max, null count) in the file footer. When `spark.sql.parquet.filterPushdown=true`, Spark pushes filter predicates into the native Parquet reader which uses these statistics to prune row groups without reading their data pages. Bloom filters (Parquet 1.12+) can further refine this for equality predicates.)
- C) Spark rewrites the filter as a column projection, reading only the `event_date` column to determine matching rows before fetching other columns
- D) Predicate pushdown requires column statistics to be collected with `ANALYZE TABLE … COMPUTE STATISTICS` first; without it the setting has no effect

---

### Question 13 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.planCacheSize — LRU cache for parsed and analyzed SQL logical plans

**Question**:
An application repeatedly executes thousands of distinct SQL strings through `spark.sql()`. CPU time on the driver is high. What does `spark.sql.planCacheSize` (default 100) control and how should it be tuned?

- A) It is the maximum number of physical execution plans held in the BlockManager across all active SparkSessions
- B) `spark.sql.planCacheSize` is the capacity of the LRU cache for parsed and analyzed logical plans from SQL strings; when the cache is full the least-recently-used plan is evicted; increasing this value for applications with many distinct, repeated SQL strings reduces repeated parsing and semantic analysis overhead (correct answer — Spark caches the `LogicalPlan` produced by `sql()` calls in a bounded LRU cache keyed by the SQL string. Hitting the cache skips parsing and analysis (though optimization and planning still occur). Applications generating many parametrized SQL strings (e.g., one per user query) should either use `Dataset` API for plan reuse or increase `planCacheSize` appropriately.)
- C) `spark.sql.planCacheSize` limits the number of subqueries that can be shared across query stages via `ReusedSubquery`
- D) The plan cache is unlimited by default; setting `spark.sql.planCacheSize=0` disables it entirely

---

### Question 14 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.files.ignoreCorruptFiles vs spark.sql.files.ignoreMissingFiles — independent error-handling flags

**Question**:
A developer reads a directory of Parquet files. One file is truncated/corrupt; another file that appeared in the listing was deleted between job planning and execution. Which settings allow the job to continue without failing?

- A) `spark.sql.files.skipInvalidFiles=true` handles both corrupt and missing files with a single setting
- B) `spark.sql.files.ignoreCorruptFiles=true` (default false) allows Spark to skip files that fail to parse or are truncated; `spark.sql.files.ignoreMissingFiles=true` (default false) allows Spark to skip files that are absent when actually read; the two settings are independent and must be enabled separately (correct answer — `ignoreCorruptFiles` causes the task reading a corrupt file to emit zero rows and continue rather than failing. `ignoreMissingFiles` handles the race condition where a file in the file listing is deleted before the task reads it. Both default to false so that data quality issues surface as errors in production by default.)
- C) `spark.sql.parquet.filterPushdown=false` disables strict schema checks and implicitly handles corrupt files
- D) Both behaviors are controlled by a single setting: `spark.sql.files.ignoreErrors=true`

---

### Question 15 — Apache Spark Architecture

**Difficulty**: Easy
**Answer Type**: one
**Topic**: AQE coalescePartitions — partition count can be reduced below spark.sql.shuffle.partitions

**Scenario**:
`spark.sql.shuffle.partitions=400` and AQE is enabled (Spark 3.2+ default). After a lightweight shuffle stage, 390 of the 400 partitions contain less than 512 KB of data each.

**Question**:
What will AQE do with these partitions?

- A) AQE always produces exactly `spark.sql.shuffle.partitions` partitions; this value is a hard lower bound
- B) AQE's `CoalesceShufflePartitions` rule merges adjacent small post-shuffle partitions to approach the advisory target size; the resulting partition count will be far below 400, dramatically reducing empty-task overhead (correct answer — `spark.sql.adaptive.coalescePartitions.enabled=true` (default) activates this rule. AQE observes post-shuffle partition sizes at runtime and merges consecutive small partitions. `spark.sql.shuffle.partitions` is a starting suggestion, not a floor; the coalesced count is bounded below by `coalescePartitions.minPartitionNum` (default 1) or `minPartitionSize` (default 1 MiB).)
- C) AQE splits the small partitions into sub-partitions to ensure every task processes at least 64 MB
- D) AQE is not triggered unless `spark.sql.adaptive.forceApply=true`; with only `spark.sql.adaptive.enabled=true` the partition count is unchanged

---

### Question 16 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SparkConf priority hierarchy — precedence of configuration sources

**Question**:
A developer sets `spark.executor.memory=4g` in `spark-defaults.conf`, passes `--executor-memory 6g` to `spark-submit`, and calls `SparkConf().set("spark.executor.memory", "8g")` in application code. Which value does Spark actually use?

- A) The `spark-defaults.conf` value (4g) — system configuration always takes precedence for safety
- B) The programmatic `SparkConf().set()` value (8g) wins; the priority order from highest to lowest is: **(1) values set directly in `SparkConf`**, **(2) flags passed to `spark-submit` / `spark-shell`**, **(3) values in `spark-defaults.conf`**; lower-priority sources supply defaults only for values not already set (correct answer — `SparkConf` programmatic overrides win over command-line flags which win over defaults files. This allows library code to set required settings without being overridden by operator defaults. Environment variable substitutions (e.g., `SPARK_EXECUTOR_MEMORY`) map to `spark-submit` flags and share that priority level.)
- C) The `spark-submit` flag (6g) always takes precedence over all sources including code
- D) All three values are merged; the highest numeric value (8g) is always selected for memory settings

---

### Question 17 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.legacy.timeParserPolicy — LEGACY vs CORRECTED vs EXCEPTION date/time parsing behavior

**Scenario**:
A Parquet table created with Spark 2.x stores date strings in `MM-dd-yyyy` format. In Spark 3.x, `to_date(col("dt_str"), "MM-dd-yyyy")` returns null for some values that worked correctly under Spark 2.x.

**Question**:
Which setting controls this parsing behavior, and what option restores Spark 2.x compatibility?

- A) `spark.sql.parquet.int96RebaseModeInRead=LEGACY` — controls the calendar system for pre-1582 dates in Parquet; switching resolves modern date parsing nulls
- B) `spark.sql.legacy.timeParserPolicy` controls the date/time parser implementation: `CORRECTED` (default in Spark 3.x) uses the strict Proleptic Gregorian calendar parser and rejects inputs valid only under the old lenient parser; `LEGACY` restores the Java `SimpleDateFormat`-based lenient parser used in Spark 2.x; `EXCEPTION` throws an error for ambiguous inputs (correct answer — Spark 3.x switched the date/time parser from a lenient `SimpleDateFormat` to a strict ISO 8601 / Proleptic Gregorian calendar parser. Dates like `"02-30-2023"` or formats relying on `SimpleDateFormat` leniency may return null or throw. Setting `spark.sql.legacy.timeParserPolicy=LEGACY` restores 2.x behavior during migration.)
- C) `spark.sql.ansi.enabled=true` causes strict date validation and must be disabled for legacy date formats
- D) `spark.sql.legacy.parquet.rebaseModeInRead=LEGACY` applies to all date parsing across all input formats

---

### Question 18 — Apache Spark Architecture

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.jsonGenerator.ignoreNullFields — null field suppression in JSON struct serialization

**Question**:
A DataFrame has a struct column with some nested fields set to null. When calling `to_json(col("details"))`, the developer wants null fields omitted from the JSON output. Which setting controls this?

- A) `df.na.drop()` before calling `to_json` — removes rows with any null values, producing clean JSON
- B) `spark.sql.jsonGenerator.ignoreNullFields=true` (default in Spark 3.x) causes `to_json` and the JSON file writer to suppress struct fields with null values; setting it to `false` produces explicit `null` values such as `{"id":1,"email":null}` (correct answer — `spark.sql.jsonGenerator.ignoreNullFields` was introduced to match common JSON conventions where absent fields are preferred over explicit `null`. It applies to both `to_json()` column function and when writing DataFrames in JSON format via `df.write.format("json")`. The default changed to `true` in Spark 3.x.)
- C) `to_json` always includes null fields; a Python UDF is required to remove them programmatically
- D) Setting the struct field's `nullable=False` in `StructField` prevents null values from appearing in the JSON output

---

### Question 19 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.sources.useV1SourceList — routing named data sources to V1 vs V2 code path

**Question**:
A developer suspects a custom DataSourceV2 connector is being bypassed and routed through Spark's V1 code path. Which configuration controls whether a named format uses the V1 or V2 API?

- A) `spark.sql.datasource.useV2=false` globally switches all data sources to the V1 code path
- B) `spark.sql.sources.useV1SourceList` is a comma-separated list of format names (e.g., `"parquet,orc,csv,json,text,socket,kafka"`) that Spark forces onto the V1 `DataSource` code path regardless of whether a V2 implementation exists; removing a format name from this list allows Spark to use its V2 `TableProvider` implementation (correct answer — Built-in formats like Parquet and ORC have both V1 and V2 implementations. By default, `useV1SourceList` routes many of them through V1 for stability. Operators testing a V2 connector should check whether their format is listed here and remove it to enable the V2 path. Custom V2 connectors not in the list use V2 automatically.)
- C) V1 vs V2 routing is determined automatically at compile time by which interfaces the connector implements; no runtime configuration can override it
- D) `spark.sql.sources.useV1SourceList` only applies to streaming sources; batch reads always use V2

---

### Question 20 — Apache Spark Architecture

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.adaptive.localShuffleReader.enabled — local shuffle read after AQE join strategy conversion

**Scenario**:
AQE detects at runtime that one side of a SortMergeJoin has shrunk to 5 MiB and converts it to a BroadcastHashJoin. The shuffle data originally written for the SortMergeJoin still exists on each executor's local disk.

**Question**:
How does `spark.sql.adaptive.localShuffleReader.enabled=true` (default) optimize the subsequent read of that shuffle data?

- A) It enables local disk reads before remote fetches for all shuffle operations regardless of join type changes
- B) After the SortMergeJoin-to-BroadcastHashJoin conversion, cross-node shuffle redistribution is no longer needed; `LocalShuffleReaderExec` replaces the standard `ShuffleQueryStage` reader so each executor reads only its own locally written shuffle files, eliminating all remote shuffle fetch overhead for that stage (correct answer — In a SortMergeJoin, each reducer reads from all mappers across the cluster. After AQE converts it to a BroadcastHashJoin, only one side needs to be shuffled; the other is broadcast. The local shuffle reader lets each executor consume only the files it wrote locally, removing network I/O from the reduce phase entirely. This is also applied after `AQE` skew join splits when sub-partitions can be read locally.)
- C) `localShuffleReader` reduces shuffle block size by pre-aggregating data on the write side before any remote sends
- D) The setting only affects the sort step within SortMergeJoin; it does not change which nodes data is fetched from

---

### Question 21 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: F.transform(array_col, func) — element-wise array transformation higher-order function

**Question**:
What does `F.transform(F.col("prices"), lambda x: x * 1.1)` return when applied to an `ArrayType(DoubleType())` column named `prices`?

- A) It applies the lambda to the entire array and returns a single scalar value (the product of all transformed prices)
- B) `F.transform(array_col, func)` applies `func` to each individual element and returns a new `ArrayType(DoubleType())` column of the same length with each price multiplied by 1.1; no elements are removed or added (correct answer — `F.transform` (SQL: `transform(array, func)`) is the array equivalent of `map()` in functional programming. The output array has the same cardinality as the input. The element type of the output array is determined by the return type of `func`. Unlike `F.filter`, elements are never dropped.)
- C) `transform()` modifies the original array column in-place; no new column is created
- D) `F.transform` only supports integer or string arrays; `DoubleType` arrays require `F.array_map` instead

---

### Question 22 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.filter(array_col, func) — element-wise array filtering HOF returning a variable-length subset

**Question**:
Given an `ArrayType(IntegerType())` column `scores`, what is the return type and behavior of `F.filter(F.col("scores"), lambda x: x >= 80)`?

- A) `filter()` returns a `BooleanType` column indicating whether at least one element in the array is ≥ 80
- B) `F.filter(array_col, func)` returns a new `ArrayType(IntegerType())` column containing only the elements for which `func` returns `true`; elements failing the predicate are dropped; the resulting arrays may be shorter than the originals (correct answer — `F.filter` (SQL: `filter(array, func)`) is the array HOF for subsetting. The output length varies per row. A row with `scores=[70, 85, 92]` yields `[85, 92]`. A row with all scores below 80 yields an empty array `[]`, not `null`. Compare with `F.exists` which returns a boolean.)
- C) `filter()` returns the zero-based index of the first matching element as an `IntegerType` column
- D) `F.filter` on an array column raises `AnalysisException`; use `array_sort` followed by `slice` for array filtering

---

### Question 23 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.posexplode(col) — generates rows with both zero-based position and element from an array

**Question**:
What output columns does `df.select(F.posexplode(F.col("tags")))` produce when `tags` is an `ArrayType(StringType())` column?

- A) A single new column named `tags_pos` containing the zero-based position of each element alongside the exploded value
- B) Two new columns named `pos` (`IntegerType`, zero-based position) and `col` (`StringType`, element value); one row is emitted per array element containing both its position and its value (correct answer — `F.posexplode` (SQL: `posexplode(array)`) is the positional variant of `explode`. It emits the pair `(pos, col)` where `pos` is 0-indexed. For `tags=["a","b","c"]` it produces rows `(0,"a"), (1,"b"), (2,"c")`. Use `F.posexplode_outer` to preserve rows where the array is null or empty, emitting `(null, null)` for those rows.)
- C) Three columns: the original `tags` column, the position, and the element value
- D) `F.posexplode` behaves identically to `F.explode`; it does not add a position column

---

### Question 24 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.explode_outer(col) vs F.explode(col) — null and empty array row handling

**Question**:
A DataFrame has an `ArrayType` column `items` where some rows have `null` and others have an empty array `[]`. What is the key behavioral difference between `F.explode("items")` and `F.explode_outer("items")`?

- A) Both produce one row per element; `explode_outer` handles `MapType` inputs while `explode` handles `ArrayType`
- B) `F.explode(col)` silently drops rows where the array is `null` or empty `[]`; `F.explode_outer(col)` preserves those rows by emitting a single row with a `null` element value, ensuring all original rows appear in the output (correct answer — `explode_outer` is the LEFT OUTER equivalent of `explode`. For `null` arrays it emits one row with `null` element. For empty arrays it also emits one row with `null` element. This is important for joins where dropping rows would change the cardinality of a left-join result. The same `_outer` variant applies to `posexplode_outer` and `inline_outer`.)
- C) `F.explode_outer` raises an exception for `null` arrays; `F.explode` silently skips them
- D) The functions behave identically for `null` arrays; `explode_outer` only differs for empty (non-null) arrays

---

### Question 25 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.arrays_zip(*cols) — output schema with struct field names derived from input column names

**Question**:
What is the schema of the output column produced by `F.arrays_zip(F.col("a"), F.col("b"))` where `a` is `ArrayType(IntegerType())` and `b` is `ArrayType(StringType())`? What happens when the arrays have different lengths?

- A) `ArrayType(StructType([StructField("a_zip", IntegerType()), StructField("b_zip", StringType())]))`
- B) `ArrayType(StructType([StructField("a", IntegerType()), StructField("b", StringType())]))` — field names are taken from the input column names; arrays are zipped element-wise; if arrays differ in length the shorter one is padded with `null` values so all rows in the output array have both fields (correct answer — `F.arrays_zip` (SQL: `arrays_zip(a, b, ...)`) produces one struct per element pair. Field names mirror input column names (or `0`, `1`, ... for unnamed expressions). Padding with `null` for the shorter array means the output length equals the length of the longest input array. Contrast with Python's `zip()` which truncates to the shortest.)
- C) `ArrayType(ArrayType(StringType()))` — arrays are concatenated end-to-end, not interleaved into structs
- D) Output schema uses positional names `StructField("0", IntegerType())` and `StructField("1", StringType())`

---

### Question 26 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: to_date(col, format) — parsing string column to DateType vs TimestampType

**Question**:
What is the return type of `to_date(F.col("event_str"), "yyyy-MM-dd")` and how does it differ from `to_timestamp()`?

- A) Both functions return `TimestampType`; `to_date` just truncates the time portion to midnight
- B) `to_date(col, format)` returns `DateType` (date only, no time component); `to_timestamp(col, format)` returns `TimestampType` (date and time with microsecond precision); use `to_date` when only the calendar date is needed and you want to avoid timezone-related discrepancies (correct answer — `DateType` stores only year, month, day. `TimestampType` stores year through microseconds and is timezone-aware (session timezone applies on display). Converting to `DateType` avoids accidental off-by-one day errors when crossing timezone boundaries. If the format string does not include time components, `to_timestamp` still returns `TimestampType` with time set to midnight in the session timezone.)
- C) `to_date` returns `StringType` with the date formatted according to ISO 8601
- D) `to_date` with a format string returns a `LongType` Unix epoch day number

---

### Question 27 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.from_json(col, schema) and F.to_json(col) — JSON string ↔ struct/map/array column conversion

**Question**:
A column `payload` contains JSON strings like `'{"user":"alice","score":95}'`. How do you parse this into a struct column, and how do you serialize a struct column back to a JSON string?

- A) Use `F.json_decode(col, schema)` to parse and `F.json_encode(col)` to serialize; `from_json` and `to_json` are deprecated aliases
- B) `F.from_json(col("payload"), schema)` parses the JSON string into a `StructType` column (or `MapType`/`ArrayType`); `F.to_json(struct_col)` serializes a struct, map, or array column back into a JSON string; unknown keys during parsing are silently dropped unless a `MapType` schema is used (correct answer — `from_json` accepts a `StructType`, `ArrayType`, or `MapType` schema. Fields in the JSON that match the schema are populated; unmatched fields are dropped. `to_json` serializes the full struct to a JSON string following `spark.sql.jsonGenerator.ignoreNullFields`. Both work on individual rows, making them useful for nested data in Kafka or Delta Lake columns.)
- C) `F.from_json` only works with `MapType(StringType, StringType)`; arbitrary struct schemas require a Python UDF
- D) `F.to_json` produces a JSON array, not a JSON object; use `F.struct_to_json` for object serialization

---

### Question 28 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.get_json_object(col, path) — JSONPath single-value extraction from a JSON string column

**Question**:
A column `raw_json` contains `'{"order":{"id":42,"items":["a","b"]}}'`. What does `F.get_json_object(F.col("raw_json"), "$.order.id")` return?

- A) An `IntegerType` column containing `42`
- B) A `StringType` column containing the string `"42"`; `get_json_object` always returns `StringType` regardless of the JSON value's original type; the JSONPath expression navigates nested objects using `.` notation and arrays using `[n]` notation (correct answer — `get_json_object(col, path)` extracts exactly one value at the given JSONPath and returns it as a `StringType`. Unlike `from_json`, it does not require a schema. If the path does not exist the result is `null`. For extracting multiple fields efficiently, prefer `json_tuple` or `from_json` with a schema to avoid parsing the JSON string multiple times.)
- C) A `StructType` column with a single field `id` of `IntegerType`
- D) `F.get_json_object` requires a `MapType` input column; it does not accept raw JSON strings

---

### Question 29 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.json_tuple(col, *fields) — extracting multiple JSON fields in a single pass

**Question**:
A column `event_json` contains JSON objects. A developer needs to extract three fields (`user_id`, `action`, `ts`) from each row. Why is `F.json_tuple` preferred over three separate `F.get_json_object` calls?

- A) `F.json_tuple` returns a `StructType`, while `get_json_object` returns a `StringType`; only `json_tuple` supports typed output
- B) `F.json_tuple(col("event_json"), "user_id", "action", "ts")` parses the JSON string once and returns multiple `StringType` columns (`c0`, `c1`, `c2`) in a single pass; three separate `get_json_object` calls each parse the same JSON string independently, tripling the parsing overhead per row (correct answer — `json_tuple` is a generator function (like `explode`) that must be used in `select` with `lateral view` in SQL or as a `select` expression returning multiple columns. It parses the JSON string exactly once regardless of how many fields are extracted. The output column names are positional (`c0`, `c1`, ...) by default; aliases must be applied. If a field is absent the corresponding column returns `null`.)
- C) `get_json_object` is limited to top-level keys; `json_tuple` supports nested paths like `$.order.id`
- D) `json_tuple` automatically infers and casts output types; `get_json_object` always returns `StringType`

---

### Question 30 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: concat_ws(sep, *cols) — joining multiple string columns with a separator

**Question**:
What does `F.concat_ws("-", F.col("year"), F.col("month"), F.col("day"))` return for the row `year="2024"`, `month="03"`, `day="15"`?

- A) `ArrayType(StringType())` containing `["2024", "03", "15"]`
- B) The string `"2024-03-15"`; `concat_ws(sep, col1, col2, ...)` joins all non-null string/column values with the separator between each pair; null values are skipped without producing a double separator (correct answer — `concat_ws` is "concat with separator". Unlike `F.concat` which includes null values as empty, `concat_ws` skips null columns in the concatenation, so if `month` were null the result would be `"2024-15"`, not `"2024--15"`. This makes it safe for building partial keys from optional fields.)
- C) `null` — if any column is null, the entire result is null
- D) `"2024-03-15"` only when all three columns are `StringType`; mixing `IntegerType` raises an `AnalysisException`

---

### Question 31 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: split(col, pattern, limit) — regex-based string splitting with an optional limit parameter

**Question**:
What does `F.split(F.col("path"), "/", 3)` return for the string `"/usr/local/bin/spark"`?

- A) `ArrayType(StringType())` with three elements: `["usr", "local", "bin/spark"]` (leading empty string omitted)
- B) `ArrayType(StringType())` with three elements: `["", "usr", "local/bin/spark"]` — the `limit` parameter caps the array length; splitting stops after `limit - 1` splits, leaving the remainder as the final element (correct answer — `F.split(col, pattern, limit=-1)` splits the string at each regex match. With `limit=3`, Spark applies at most 2 splits: `"/usr/local/bin/spark"` → `["", "usr", "local/bin/spark"]`. The leading empty string before the first `/` is retained since there is content before position 0. Without a limit (or limit=-1), the result is `["", "usr", "local", "bin", "spark"]`.)
- C) `ArrayType(StringType())` with all five elements regardless of the limit argument; the limit controls minimum not maximum splits
- D) `F.split` with a numeric limit throws `AnalysisException`; only the two-argument form is supported

---

### Question 32 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.element_at(array_col, index) — 1-based indexing and negative index semantics

**Question**:
A developer wants the last element of an `ArrayType(StringType())` column `tags`. How does `F.element_at(F.col("tags"), -1)` differ from `F.col("tags")[0]`?

- A) Both expressions return the last element; the difference is only syntactic
- B) `F.element_at(col, -1)` uses **1-based** indexing where negative values count from the end; `-1` returns the last element; positive index `1` returns the first element; `F.col("tags")[0]` uses **0-based** indexing (Spark array subscript), so `[0]` returns the first element, not the last; there is no subscript syntax for negative indexing (correct answer — Spark's `element_at` follows the 1-based convention of SQL arrays and supports negative indices for from-the-end access. The `[]` subscript operator uses 0-based Python/Java-style indexing. `element_at` returns `null` if the index is out of range (when `spark.sql.ansi.enabled=false`); the subscript `[]` also returns null for out-of-range access. `F.try_element_at` (Spark 3.4+) is the null-safe form.)
- C) `F.element_at(col, -1)` raises `ArrayIndexOutOfBoundsException` for negative indices
- D) `F.element_at` uses 0-based indexing identical to the `[]` subscript; `-1` would underflow to the second-to-last element

---

### Question 33 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.base64(col) and F.unbase64(col) — Base64 encoding and decoding of binary/string columns

**Question**:
A binary column `raw_bytes` contains binary data. What does `F.base64(F.col("raw_bytes"))` return, and how do you reverse the operation?

- A) `F.base64` returns a `BinaryType` column with the bytes encoded in Base64 format
- B) `F.base64(col)` returns a `StringType` column with the Base64-encoded representation of the binary data; `F.unbase64(col)` decodes a Base64 `StringType` column back to `BinaryType`; this pair is commonly used when binary data must be stored as a printable string in JSON or CSV (correct answer — `F.base64` (SQL: `base64(col)`) encodes `BinaryType` (or `StringType` coerced to binary) to a Base64 `StringType`. `F.unbase64` decodes a Base64 string back to `BinaryType`. These are useful for embedding binary payloads in text formats or transmitting via text channels like Kafka topics that require string values.)
- C) `F.base64` returns `LongType` (a numeric representation of the Base64 encoding)
- D) `F.base64` only accepts `StringType` input; `BinaryType` columns must be cast to string first

---

### Question 34 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.levenshtein(l, r) — edit distance between two string columns for fuzzy matching

**Question**:
A developer wants to find rows where two name columns (`name1`, `name2`) differ by at most 2 character edits. Which expression and threshold achieves this?

- A) `F.soundex(col("name1")) == F.soundex(col("name2"))` — phonetic encoding is the standard for fuzzy name matching
- B) `F.levenshtein(F.col("name1"), F.col("name2")) <= 2` — `levenshtein(l, r)` returns the minimum number of single-character insertions, deletions, or substitutions to transform one string into the other; a threshold of 2 captures typos and minor variations (correct answer — The Levenshtein distance (edit distance) is a standard metric for string similarity. `F.levenshtein` returns an `IntegerType` count of edit operations. For names, a threshold of 1–3 catches common single-keystroke typos. Performance note: computing Levenshtein on millions of cross-product pairs is expensive; consider using `startswith`/`contains` to pre-filter candidates before applying `levenshtein`.)
- C) `F.jaccard_similarity(col("name1"), col("name2")) >= 0.8` — Jaccard similarity on character bigrams
- D) `F.levenshtein` requires both columns to be the same length; padding with spaces is needed for strings of unequal length

---

### Question 35 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.hex(col) and F.unhex(col) — hexadecimal encoding and decoding

**Question**:
What does `F.hex(F.lit(255))` return, and what does `F.unhex(F.lit("FF"))` return?

- A) `F.hex(255)` returns `IntegerType` `255` in hexadecimal representation; `F.unhex("FF")` returns `IntegerType` `255`
- B) `F.hex(F.lit(255))` returns `StringType` `"FF"`; `F.unhex(F.lit("FF"))` returns `BinaryType` containing the byte `0xFF`; `unhex` converts a hexadecimal string to its binary representation, not a numeric integer (correct answer — `F.hex` accepts `IntegerType`, `LongType`, `StringType`, or `BinaryType` and returns a `StringType` hex string. `F.unhex` is the inverse: it takes a hex `StringType` and returns `BinaryType`. These are commonly used for encoding hash values or binary keys as readable strings. To convert hex to an integer, cast the `unhex` result or use `conv(hex_str, 16, 10)`.)
- C) `F.hex(255)` raises `AnalysisException`; the function only accepts `StringType` input
- D) `F.unhex("FF")` returns `StringType` `"255"` (the decimal representation of the hex value)

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.sentences(text, language, country) — tokenizing text into an array of word arrays

**Question**:
What type and structure does `F.sentences(F.col("review_text"))` return for the input `"Spark is fast. Spark is easy."`?

- A) `ArrayType(StringType())` — a flat list of all individual words from the text
- B) `ArrayType(ArrayType(StringType()))` — an array of sentences, where each sentence is an array of words; for `"Spark is fast. Spark is easy."` the result is `[["Spark","is","fast"],["Spark","is","easy"]]` (correct answer — `F.sentences(text, language="", country="")` tokenizes text using Java's `BreakIterator` for language-aware sentence and word segmentation. The result is a nested array structure (array of sentence arrays). Punctuation and case handling depend on the locale specified by the `language` and `country` parameters. This is useful for NLP preprocessing pipelines in Spark.)
- C) `StructType` with fields `sentences` (count) and `words` (total word count)
- D) `MapType(IntegerType, StringType)` mapping sentence index to full sentence string

---

### Question 37 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.map_filter(map_col, func) — filtering map entries by a key-value predicate HOF

**Question**:
A `MapType(StringType, IntegerType)` column `scores` contains entries like `{"math": 90, "english": 65, "science": 88}`. How do you keep only entries where the value is ≥ 80?

- A) Use `F.filter(col("scores"), lambda k, v: v >= 80)` — the standard `filter` HOF works on both arrays and maps
- B) `F.map_filter(F.col("scores"), lambda k, v: v >= 80)` returns a new `MapType` column containing only key-value pairs satisfying the predicate; entries with values < 80 are removed; the output for the example row would be `{"math": 90, "science": 88}` (correct answer — `F.map_filter(map_col, func)` (SQL: `map_filter(map, func)`) is the higher-order function for filtering map entries. The lambda receives two arguments: the key and the value. Only entries where `func(key, value)` returns `true` are retained. Compare with `F.filter` which operates on arrays, and `F.transform_values` which changes values without filtering.)
- C) Maps cannot be filtered with HOFs; convert to an array of structs with `F.map_entries`, filter the array, then convert back with `F.map_from_entries`
- D) `F.map_filter` is not available as a Python function; it can only be expressed as a SQL string in `selectExpr`

---

### Question 38 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.format_string(format, *args) — printf-style string formatting for column values

**Question**:
A developer wants to build a message column combining an integer `id` and a string `name` column: `"User 42: alice"`. Which expression achieves this?

- A) `F.concat("User ", col("id"), ": ", col("name"))` — standard concatenation handles mixed types
- B) `F.format_string("User %d: %s", F.col("id"), F.col("name"))` formats the output using printf-style format specifiers; it returns a `StringType` column and handles type conversions automatically (correct answer — `F.format_string(format, *cols)` (SQL: `printf(format, args...)` or `format_string(format, args...)`) applies a Java-style format string with `%d`, `%s`, `%f`, etc. It is useful when the output format is complex or when padding/alignment is needed (e.g., `"%05d"` for zero-padded IDs). Note that `F.concat` would fail on integer columns without an explicit cast.)
- C) `F.printf(F.col("id"), F.col("name"), format="User %d: %s")` — the keyword argument form required for format_string
- D) `F.format_string` is only available in Spark SQL strings via `selectExpr`; it is not a Python DataFrame API function

---

### Question 39 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.dayofyear(date) — returns the day-of-year number (1–366) from a date or timestamp column

**Question**:
What does `F.dayofyear(F.to_date(F.lit("2024-03-01")))` return, and what is the maximum possible value?

- A) Returns `60` for March 1 in a non-leap year; maximum is `365`
- B) Returns `61` (31 days in January + 29 days in February 2024 [leap year] + 1 = 61); the maximum is `366` for leap years and `365` for non-leap years (correct answer — `F.dayofyear(date)` returns an `IntegerType` from 1 to 365/366. 2024 is a leap year (divisible by 4, not divisible by 100 unless also by 400), so February has 29 days. January 31 + February 29 + March 1 = day 61. `dayofyear` is distinct from `dayofweek` (day within the week) and `dayofmonth` (day within the month).)
- C) Returns `60`; Spark counts February as having 28 days even in leap years unless `spark.sql.legacy.timeParserPolicy=LEGACY`
- D) `F.dayofyear` is not a built-in function; use `F.date_format(date, "D")` to get the day-of-year string

---

### Question 40 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.map_keys(map_col) and F.map_values(map_col) — extracting keys and values as array columns

**Question**:
A `MapType(StringType, IntegerType)` column `attrs` contains `{"color":"blue","size":10}`. What do `F.map_keys(col("attrs"))` and `F.map_values(col("attrs"))` return, and what ordering guarantee exists?

- A) `map_keys` returns `ArrayType(StringType())` sorted alphabetically; `map_values` returns `ArrayType(IntegerType())` in the same sorted order
- B) `F.map_keys(map_col)` returns `ArrayType(StringType())` and `F.map_values(map_col)` returns `ArrayType(IntegerType())`; Spark makes **no ordering guarantee** for the extracted arrays — the key at index `i` in `map_keys` output corresponds to the value at index `i` in `map_values` output, but the overall order is non-deterministic (correct answer — Map key/value order in Spark is not guaranteed to be insertion order, alphabetical, or any other fixed order. `map_keys` and `map_values` are consistent with each other (index `i` matches), but the order can differ between rows and across Spark versions or partition boundaries. To sort the keys, apply `F.array_sort` after `F.map_keys`. To get ordered key-value pairs use `F.map_entries` followed by `F.array_sort`.)
- C) `map_keys` raises `AnalysisException` for maps with non-string keys; only `StringType` keys are supported
- D) The key and value arrays are always sorted in the order the map entries were inserted during DataFrame creation

---

### Question 41 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.transform(func) — functional pipeline chaining where func takes and returns a DataFrame

**Question**:
What is the purpose of `df.transform(func)` and how does it differ from calling `func(df)` directly?

- A) `df.transform(func)` applies a column-level transformation; `func` must accept and return a single Column, not a DataFrame
- B) `df.transform(func)` is syntactic sugar for `func(df)` — it passes the DataFrame as the sole argument to `func` and returns the result; the benefit is enabling fluent method chaining: `df.filter(...).transform(add_audit_columns).transform(normalize_amounts).show()` (correct answer — `df.transform(func)` (Spark 3.0+) enables building reusable DataFrame transformation functions that can be composed in a readable pipeline without breaking the dot-chain syntax. The function signature is `func(DataFrame) → DataFrame`. Multiple transformations can be chained as `df.transform(f1).transform(f2)` instead of `f2(f1(df))`, improving readability. This is the standard pattern for modular ETL pipelines.)
- C) `df.transform` applies the function to each row in parallel using Python workers, similar to `mapInPandas`
- D) `df.transform(func)` caches the intermediate result before applying `func`, optimizing multi-use DataFrames

---

### Question 42 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.unpivot(ids, values, variableColumnName, valueColumnName) — wide-to-long transformation (Spark 3.4+)

**Scenario**:
A wide DataFrame has columns `(user_id, jan_sales, feb_sales, mar_sales)`. The developer needs a long format with columns `(user_id, month, sales)`.

**Question**:
Which Spark 3.4+ DataFrame API call produces this long-format output?

- A) `df.melt(id_vars=["user_id"], value_vars=["jan_sales","feb_sales","mar_sales"], var_name="month", value_name="sales")` — the pandas-style melt method
- B) `df.unpivot(ids=["user_id"], values=["jan_sales","feb_sales","mar_sales"], variableColumnName="month", valueColumnName="sales")` (Spark 3.4+) converts wide format to long format; the `ids` columns are kept as-is, `values` columns are stacked into rows, `variableColumnName` holds the original column name, and `valueColumnName` holds the cell value (correct answer — `df.unpivot` was introduced in Spark 3.4 (also available as `df.melt` as a pandas-API-compatible alias). Each row in the original DataFrame produces one output row per `values` column. For 3 users × 3 month columns = 9 rows. The `variableColumnName` column contains strings like `"jan_sales"`, `"feb_sales"`, etc. Earlier Spark versions require a manual `F.stack` expression in `selectExpr` to achieve the same result.)
- C) `df.pivot("user_id").agg(F.first("jan_sales"))` — pivot is the inverse; use reverse pivot for unpivoting
- D) `df.unpivot` is not available in PySpark; it is a Scala-only API

---

### Question 43 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.struct(*cols) — creating a StructType column from existing columns

**Question**:
How do you combine columns `first_name` and `last_name` into a single struct column `full_name` with fields `first` and `last`?

- A) `df.withColumn("full_name", F.array("first_name", "last_name"))` — array groups related columns
- B) `df.withColumn("full_name", F.struct(F.col("first_name").alias("first"), F.col("last_name").alias("last")))` creates a `StructType` column where `full_name.first` and `full_name.last` can be accessed as nested fields (correct answer — `F.struct(*cols)` takes any number of column expressions and packages them into a `StructType`. Field names in the struct default to the column expression names; use `.alias()` to control field names within the struct. The struct fields are accessible via dot notation: `df.select("full_name.first")` or `F.col("full_name.first")`.)
- C) `df.withColumn("full_name", F.create_struct({"first": col("first_name"), "last": col("last_name")}))` — use the dict form
- D) Struct columns can only be created by reading files that contain nested JSON; they cannot be constructed from flat columns

---

### Question 44 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.map_entries(map_col) — converting a MapType column to an array of {key, value} structs

**Question**:
How do you convert a `MapType(StringType, IntegerType)` column `metrics` to an `ArrayType(StructType([key: String, value: Int]))` for further array operations?

- A) `F.explode(col("metrics"))` — explodes the map to key-value row pairs, not to an array of structs
- B) `F.map_entries(F.col("metrics"))` returns `ArrayType(StructType([StructField("key", StringType()), StructField("value", IntegerType())]))` — each map entry becomes one struct element; this enables applying array HOFs like `F.array_sort` or `F.filter` to map contents (correct answer — `F.map_entries(map_col)` (SQL: `map_entries(map)`) converts a map to an array of `(key, value)` structs. This is the entry point for applying array HOFs to map data. For example, sorting a map by value: `F.array_sort(F.map_entries(col("metrics")), lambda x, y: x.value - y.value)` then converting back via `F.map_from_entries`. The inverse of `map_entries` is `F.map_from_entries`.)
- C) `F.map_to_array(col("metrics"))` — the built-in function for map-to-array conversion
- D) `F.struct(F.map_keys(col("metrics")), F.map_values(col("metrics")))` achieves the same result as `map_entries`

---

### Question 45 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.inline(array_of_structs) — exploding an array of structs into multiple columns (one row per struct)

**Scenario**:
A column `transactions` has type `ArrayType(StructType([amount: Double, currency: String]))`. The developer wants each transaction to become its own row with separate `amount` and `currency` columns.

**Question**:
Which expression achieves this?

- A) `df.select(F.explode("transactions")).select("col.amount", "col.currency")` — explode first, then unpack the struct
- B) `df.select(F.inline(F.col("transactions")))` explodes an array of structs directly into multiple separate columns — one column per struct field — producing one row per struct element; the field names become the output column names `amount` and `currency` (correct answer — `F.inline(array_of_structs)` is a generator function equivalent to `explode` combined with automatic struct-field projection. It is more concise than `explode` + struct-field selection. Like `explode`, it drops rows where the array is null or empty; use `F.inline_outer` to preserve them. `inline` can only be used at the top level of a `select`, not nested inside other expressions.)
- C) `df.withColumn("txn", F.explode("transactions")).select("txn.*")` — the `.*` syntax flattens struct fields in `select`
- D) `F.inline` is a SQL-only syntax; the Python API requires `F.explode` followed by `F.col("col.*")`

---

### Question 46 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.unionByName(other, allowMissingColumns=True) — cross-schema union with null-filling (Spark 3.1+)

**Question**:
Two DataFrames have schemas `(id, name)` and `(id, name, email)`. What happens with `df1.unionByName(df2, allowMissingColumns=True)`?

- A) `AnalysisException` is raised because the schemas differ; schemas must match for all union operations
- B) `unionByName` with `allowMissingColumns=True` (Spark 3.1+) aligns columns by name; columns present in one DataFrame but absent in the other are filled with `null`; the result schema is the union of all columns: `(id, name, email)` with `null` for `email` in rows from `df1` (correct answer — Without `allowMissingColumns=True`, `unionByName` requires identical schemas (same column names and order). With the flag, it performs a schema union and fills missing columns with `null`. This is valuable when incrementally adding columns to a schema over time and needing to union data from different periods. Column order in the output follows the first DataFrame's schema, with new columns appended.)
- C) Columns are aligned by position, not by name, regardless of the `allowMissingColumns` parameter
- D) The `email` column from `df2` is dropped to match `df1`'s schema; only the intersection of columns is kept

---

### Question 47 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.stat.bloomFilter(col, expectedNumItems, fpp) — probabilistic set-membership filter

**Question**:
A developer wants to efficiently pre-filter a large streaming dataset against a known set of 10 million valid `user_id` values with at most 1% false positives. What does `df.stat.bloomFilter("user_id", 10000000, 0.01)` return and how is it used?

- A) It returns a `DataFrame` filtered to rows that might be in the valid set; non-matching rows are dropped immediately
- B) `df.stat.bloomFilter(col, expectedNumItems, fpp)` returns a Spark `BloomFilter` object (not a DataFrame) built from the values in `col`; the bloom filter can then be serialized and used in downstream jobs to quickly reject rows where `filter.mightContain(value)` returns `false`, with a false-positive rate ≤ `fpp` and no false negatives (correct answer — A Bloom filter is a probabilistic data structure that says "definitely not in the set" or "possibly in the set". Building one with `df.stat.bloomFilter` scans the DataFrame to populate the bit array. The returned `BloomFilter` can be broadcast and used in `udf` or serialized via `writeTo(stream)`. False positives at rate `fpp` (1%) mean 1% of invalid IDs pass through; false negatives are impossible by design.)
- C) `bloomFilter` returns a `DataFrame` with an added boolean column indicating set membership for each row
- D) `df.stat.bloomFilter` is a SQL hint, not a Python method; use `/*+ BLOOM_FILTER(col, n, fpp) */` in a SQL comment

---

### Question 48 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.schema_of_csv(csv_str) — inferring a DDL schema string from a CSV literal sample

**Question**:
What does `F.schema_of_csv(F.lit("42,alice,true"))` return, and how is its output typically used?

- A) A `StructType` object that can be directly passed to `spark.read.csv()` as the `schema` parameter
- B) `F.schema_of_csv(literal_csv_string)` returns a `StringType` column containing a DDL schema string (e.g., `"_c0 BIGINT,_c1 STRING,_c2 BOOLEAN"`); the DDL string is then used with `from_csv` or passed to `spark.read.schema(ddl_string)` after extracting it to the driver (correct answer — `F.schema_of_csv` infers column types from a representative CSV row literal. Like `schema_of_json`, it returns a DDL `StringType` column (not a `StructType` object). To get the DDL string on the driver: `schema_ddl = df.select(F.schema_of_csv(F.lit(sample))).first()[0]`. This DDL string can then be passed to `schema` options for `from_csv` or CSV reads. Field names default to `_c0`, `_c1`, etc.)
- C) `F.schema_of_csv` is not a built-in function; use `spark.read.csv(sample_path).schema` to infer schemas from files
- D) `F.schema_of_csv` returns a `StructType` column of `StructType` type, which can be used directly in `from_csv`

---

### Question 49 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.write.format("avro") — Apache Avro write requirements and the spark-avro package

**Question**:
A developer runs `df.write.format("avro").save("/data/output")` and gets a `ClassNotFoundException` for the Avro data source. What is the most likely cause?

- A) Avro is not a supported output format in Apache Spark; use Parquet or ORC instead
- B) Spark's Avro support (`spark-avro`) is a separate package not bundled in the Spark distribution; it must be added via `--packages org.apache.spark:spark-avro_2.12:3.x.x` at launch or included as a JAR dependency (correct answer — Unlike Parquet and ORC which are built into Spark, Avro requires the external `spark-avro` package. Once included, the data source is registered as `"avro"`. Key write option: `avroSchema` (a JSON Avro schema string) overrides schema inference. Key read option: `avroSchema` for projection pushdown. Using `F.to_avro` / `F.from_avro` column functions also requires this package.)
- C) Avro writes require enabling a specific Spark configuration: `spark.sql.avro.enabled=true`
- D) The format string must be the fully-qualified class name: `df.write.format("org.apache.spark.sql.avro.AvroFileFormat")`

---

### Question 50 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.write.option("timestampFormat") — controlling timestamp serialization format in CSV/JSON writes

**Scenario**:
A DataFrame containing a `TimestampType` column is written to CSV. Downstream consumers expect timestamps in ISO 8601 format with timezone offset: `"2024-03-15T10:30:00.000+00:00"`.

**Question**:
Which option controls the timestamp serialization format in CSV/JSON writes?

- A) `df.write.format("csv").option("datetimeFormat", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX").save(path)` — `datetimeFormat` is the standard option
- B) `df.write.format("csv").option("timestampFormat", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX").save(path)` — the `timestampFormat` write option controls how `TimestampType` columns are serialized to strings in CSV and JSON output; the format follows `java.time.format.DateTimeFormatter` patterns (correct answer — `timestampFormat` (default `"yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]"`) is the write option for `TimestampType` columns in CSV and JSON. Similarly, `dateFormat` controls `DateType` columns. The `XXX` pattern in `DateTimeFormatter` formats the timezone offset as `+HH:MM`. For compatibility with Spark 2.x timestamp output, use `spark.sql.legacy.timeParserPolicy=LEGACY` on read.)
- C) The timestamp format is controlled by the session timezone only; there is no per-write format option
- D) `spark.sql.timestampType=TIMESTAMP_NTZ` must be set first; otherwise timestamp format options are ignored

---

### Question 51 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.unionByName vs df.union — column alignment by name vs by position

**Question**:
DataFrames `df1` has columns `(a, b)` and `df2` has columns `(b, a)`. What is the difference in output between `df1.union(df2)` and `df1.unionByName(df2)`?

- A) Both produce the same result; Spark always resolves column alignment before executing union operations
- B) `df1.union(df2)` aligns columns **by position** — column 1 of `df2` (`b`) is placed in position 1 (column `a`) of the result, causing incorrect pairing; `df1.unionByName(df2)` aligns columns **by name**, correctly matching `a` to `a` and `b` to `b` (correct answer — `df.union` and `df.unionAll` use positional alignment and do not consider column names; if schemas have the same columns in different orders the values will be mixed incorrectly with no error. `unionByName` uses column names as the join key and requires both DataFrames to have the same column names (unless `allowMissingColumns=True`). Always prefer `unionByName` when combining DataFrames from different sources.)
- C) `df1.union(df2)` raises `AnalysisException` when column names differ; only `unionByName` can handle mismatched names
- D) `union` performs a SQL `UNION` (deduplication); `unionByName` performs a SQL `UNION ALL` (no deduplication); the column alignment is the same

---

### Question 52 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.named_struct(name1, val1, name2, val2, ...) — constructing a StructType column with explicit field names

**Question**:
How do you create a struct column `point` with fields `x=3.0` and `y=4.0` using literal values, and what is the resulting schema?

- A) `F.struct(F.lit(3.0).alias("x"), F.lit(4.0).alias("y"))` — `F.struct` with aliases is the standard approach
- B) `F.named_struct(F.lit("x"), F.lit(3.0), F.lit("y"), F.lit(4.0))` creates `StructType([StructField("x", DoubleType()), StructField("y", DoubleType())])`; field names must be string literals alternating with values (correct answer — `F.named_struct(name1, val1, name2, val2, ...)` (SQL: `named_struct(name1, val1, ...)`) explicitly interleaves string name literals with value expressions. This is the low-level struct constructor; `F.struct(*cols)` is a higher-level alternative that uses column names automatically. `named_struct` is more explicit when field names need to be computed or differ from column names. The name arguments must be string literals, not column references.)
- C) `F.create_struct({"x": F.lit(3.0), "y": F.lit(4.0)})` — the dict syntax is supported in PySpark
- D) Struct columns with literal values can only be created via `spark.createDataFrame([Row(point=Row(x=3.0, y=4.0))])`

---

### Question 53 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.createGlobalTempView vs df.createOrReplaceGlobalTempView — behavior when view already exists

**Question**:
A developer calls `df.createGlobalTempView("events")` twice in the same application. What happens on the second call?

- A) The second call silently overwrites the first view without error, as with `createOrReplaceTempView`
- B) The second `createGlobalTempView("events")` raises a `TempTableAlreadyExistsException`; to overwrite an existing global temp view use `createOrReplaceGlobalTempView("events")` instead (correct answer — `createGlobalTempView` throws `TempTableAlreadyExistsException` if a global temp view with the same name already exists. `createOrReplaceGlobalTempView` atomically replaces it if it exists or creates it if it does not. The same distinction applies to `createTempView` (raises on conflict) vs `createOrReplaceTempView` (safe to call repeatedly). Global temp views persist in `global_temp` database across all SparkSessions until the SparkContext stops.)
- C) The second call raises `AnalysisException`; global temp views cannot be overwritten once created
- D) The second call succeeds and creates a version-2 view accessible via `global_temp.events_v2`

---

### Question 54 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.element_at(map_col, key) — null-safe map value lookup that returns null instead of raising an error

**Question**:
A `MapType(StringType, IntegerType)` column `config` contains `{"retries": 3}`. What does `F.element_at(F.col("config"), "timeout")` return when the key `"timeout"` is absent?

- A) `KeyError: "timeout"` is raised because the key does not exist in the map
- B) `F.element_at(map_col, key)` returns `null` when the specified key is not found in the map (when `spark.sql.ansi.enabled=false`); it is the null-safe alternative to the `map["key"]` subscript which raises an error for missing keys with ANSI mode enabled (correct answer — `F.element_at(map_col, key)` (SQL: `element_at(map, key)`) returns `null` for missing keys rather than throwing an exception under the default non-ANSI mode. Under `spark.sql.ansi.enabled=true`, missing keys raise `SparkNoSuchElementException`. `F.try_element_at` (Spark 3.4+) is always null-safe regardless of ANSI mode. The map subscript `col("config")["timeout"]` behaves the same as `element_at` in non-ANSI mode.)
- C) `F.element_at` returns the map's default value (0 for `IntegerType`) when a key is absent
- D) `F.element_at` on a map requires both a key and a default value argument; the two-argument form raises `AnalysisException`

---

### Question 55 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: df.stat.countMinSketch(col, eps, confidence, seed) — frequency estimation sketch for large datasets

**Question**:
What does `df.stat.countMinSketch("product_id", 0.001, 0.99, 42)` build and return, and what question can it answer?

- A) It returns a DataFrame with an approximate count column for each distinct `product_id` value
- B) `df.stat.countMinSketch(col, eps, confidence, seed)` builds a Count-Min Sketch — a probabilistic data structure that estimates how many times each value appears; for any query value, `sketch.estimateCount(value)` returns an upper-bound frequency estimate with error at most `eps × totalRows` with probability ≥ `confidence`; it never underestimates (correct answer — A Count-Min Sketch (CMS) is a sub-linear space frequency estimator. With `eps=0.001` and `confidence=0.99` it guarantees the estimate is within 0.1% of the total row count with 99% probability. The returned `CountMinSketch` object can be serialized and reused. It can answer "how many times has product X appeared?" without storing all distinct values. Unlike `freqItems` which finds heavy hitters, CMS answers per-element frequency queries.)
- C) `countMinSketch` returns the exact count for common values and an estimate for rare values
- D) `df.stat.countMinSketch` is not available in PySpark; it is a Scala/Java-only API

---

### Question 56 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.explain(mode="codegen") — viewing generated Java source code from whole-stage codegen

**Question**:
A developer wants to see the Java code generated by Spark's whole-stage code generation for a query. Which `explain` mode provides this?

- A) `df.explain(extended=True)` — the `extended` flag includes generated code in the physical plan output
- B) `df.explain(mode="codegen")` prints the generated Java source code for each codegen stage; this is useful for verifying that whole-stage codegen is active and for debugging generated code correctness (correct answer — Spark 3.x `explain` accepts a `mode` parameter: `"simple"` (default), `"extended"` (logical + physical plans), `"codegen"` (generated Java code per stage), `"cost"` (CBO-estimated statistics), and `"formatted"` (a structured multi-section view). `"codegen"` output shows the actual Java class with `process()` methods generated by `WholeStageCodegen`. If codegen is disabled for an operator, it will appear as an interpreted node without generated code.)
- C) `df.explain(mode="java")` — the Java-code mode for viewing whole-stage codegen output
- D) Generated Java code is never exposed to users; it can only be viewed by enabling debug logging at `org.apache.spark.sql.execution.WholeStageCodegenExec`

---

### Question 57 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.summary(*statistics) — computing specific named statistics beyond describe()

**Question**:
How does `df.summary("count", "25%", "75%", "max")` differ from `df.describe()`?

- A) `describe()` supports percentiles; `summary()` only computes count, mean, stddev, min, and max
- B) `df.summary(*stats)` accepts a flexible list of statistic names including percentiles (`"25%"`, `"50%"`, `"75%"`), while `df.describe()` is limited to: `count`, `mean`, `stddev`, `min`, `max`; `summary()` with no arguments computes the full set: count, mean, stddev, min, 25%, 50%, 75%, max (correct answer — `df.describe()` is a convenience method that computes the five basic statistics for numeric and string columns. `df.summary(*stats)` provides full control, accepting any subset of: `"count"`, `"mean"`, `"stddev"`, `"min"`, `"max"`, and any percentile in `"p%"` format (e.g., `"25%"`, `"99%"`). Both return a `DataFrame` with a `summary` string column plus one column per input numeric column.)
- C) `summary()` computes statistics across the entire DataFrame; `describe()` computes per-partition statistics
- D) `describe()` and `summary()` are identical functions; `describe` is an alias for `summary` with no arguments

---

### Question 58 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.percent_rank() and F.cume_dist() — analytical window functions for relative position

**Question**:
A window is defined as `Window.partitionBy("dept").orderBy("salary")`. What values do `F.percent_rank()` and `F.cume_dist()` return for the lowest-salary row in each department?

- A) Both return `0.0` for the minimum salary row in each partition
- B) `F.percent_rank()` returns `0.0` for the first row (rank 1) because `(rank - 1) / (total_rows - 1)` = `0 / n = 0`; `F.cume_dist()` returns the fraction of rows with salary ≤ current row's salary (always > 0); for the minimum salary row it returns `count_at_minimum / total_rows` (correct answer — `percent_rank()` is `(row_rank - 1) / (partition_size - 1)` (0.0 for first, 1.0 for last). `cume_dist()` is `rows_with_value_leq_current / partition_size` (always > 0 since at least the current row qualifies). For ties, `percent_rank` uses rank (skipping), while `cume_dist` uses all tied rows together. Both functions require `ORDER BY`; without it results are undefined.)
- C) `percent_rank()` returns `1.0` for the first (lowest) row; `cume_dist()` returns `0.0`
- D) Both functions return `null` for the minimum value row; they are only defined when there are higher values to compare against

---

### Question 59 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.ntile(n) window function — evenly distributing rows into n ordered buckets

**Question**:
A window is `Window.partitionBy("region").orderBy("revenue")` and a department has 10 rows. What does `F.ntile(4)` assign to each row?

- A) `ntile(4)` assigns each row a random bucket number between 1 and 4
- B) `F.ntile(n)` divides the ordered rows in each partition into `n` buckets of roughly equal size, assigning bucket numbers 1 through `n`; for 10 rows and `n=4`, buckets contain 3, 3, 2, 2 rows (larger buckets first), with bucket 1 containing the lowest-revenue rows (correct answer — `ntile(n)` is the window function for quartiles, deciles, percentile bins, etc. For 10 rows divided into 4 buckets: `ceil(10/4)=3` rows in buckets 1 and 2, `floor(10/4)=2` rows in buckets 3 and 4. Rows are ordered by the `ORDER BY` clause; the first ordered rows go into bucket 1. If there are fewer rows than `n`, some buckets are empty. Unlike `percent_rank`, `ntile` returns an integer bucket number.)
- C) `ntile(4)` divides rows into 4 equal-sized groups by row count, placing the highest-revenue rows in bucket 1
- D) `ntile` requires specifying bucket boundaries; passing only `n` raises `AnalysisException`

---

### Question 60 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: F.lag(col, offset, default) and F.lead(col, offset, default) — time-series access to preceding/following rows

**Question**:
In a window ordered by `event_time`, what does `F.lag(F.col("value"), 1, 0)` return for the first row in each partition?

- A) `null` — `lag` always returns `null` for boundary rows regardless of the default argument
- B) `0` — the `default` parameter (third argument) is returned when there is no preceding row at the specified `offset`; for `offset=1` on the first row, there is no row one position earlier, so the default `0` is used (correct answer — `F.lag(col, offset=1, default=None)` returns the value from `offset` rows before the current row in window order. `F.lead(col, offset=1, default=None)` returns from `offset` rows after. When the look-back/look-ahead goes beyond the partition boundary, the `default` value is returned instead of `null`. Both functions require `ORDER BY` in the window spec. They are commonly used for computing differences between consecutive rows in time-series data.)
- C) An `ArrayIndexOutOfBoundsException` is raised because offset 1 exceeds the partition boundary
- D) `F.lag` returns the first non-null value in the column when the offset goes beyond the start of the partition

---

### Question 61 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.withColumns(colsMap) — adding or replacing multiple columns in a single call (Spark 3.3+)

**Question**:
A developer wants to add two new columns `full_name` and `age_years` to a DataFrame at once. What is the Spark 3.3+ way to do this without chaining two `withColumn` calls?

- A) `df.select("*", new_col1, new_col2)` — select with star plus new columns achieves the same result as withColumns
- B) `df.withColumns({"full_name": F.concat_ws(" ", "first", "last"), "age_years": F.col("age_days") / 365})` (Spark 3.3+) adds or replaces multiple columns in a single method call, avoiding the overhead of creating an intermediate DataFrame for each column (correct answer — `df.withColumns(colsMap: dict)` was introduced in Spark 3.3 (PySpark: `withColumns`). It accepts a `{column_name: Column_expression}` dict and applies all column expressions at once against the same input schema. This is more efficient than chaining `withColumn` calls because Spark can plan all the new expressions in a single projection rather than nesting projections. Spark 3.3+ also added `withColumnsRenamed(renameMap)` for batch rename.)
- C) `df.withColumnRenamed` accepts a dict; pass the new column expressions as values
- D) `df.withColumns` is not available in PySpark; it is a Scala-only API where Seq of (String, Column) pairs are passed

---

### Question 62 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.slice(array_col, start, length) — extracting a sub-array with 1-based start index

**Question**:
An `ArrayType(StringType())` column `tokens` contains `["a","b","c","d","e"]`. What does `F.slice(F.col("tokens"), 2, 3)` return?

- A) `["b","c","d"]` — start index is 1-based; start=2 is element "b", length=3 takes 3 elements
- B) `["b","c","d"]` — `F.slice(array_col, start, length)` uses **1-based** indexing; `start=2` is the second element `"b"`, and `length=3` takes 3 elements: `["b","c","d"]` (correct answer — `F.slice` (SQL: `slice(array, start, length)`) follows the 1-based array convention of Spark SQL. `start=1` is the first element, `start=-1` accesses from the end. `length` is the count of elements to return. For `tokens=["a","b","c","d","e"]`, `slice(tokens, 2, 3)` = `["b","c","d"]`. If `start + length` exceeds the array length, the result is truncated to the end of the array without error.)
- C) `["a","b","c"]` — start=2 means skip 2 elements and take the next 3
- D) `["c","d","e"]` — start index is 0-based; start=2 is the third element "c"

---

### Question 63 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.greatest(*cols) and F.least(*cols) — element-wise max/min across multiple columns ignoring nulls

**Question**:
A row has columns `(a=5, b=null, c=3)`. What does `F.greatest(F.col("a"), F.col("b"), F.col("c"))` return?

- A) `null` — if any input column is null, the entire result is null
- B) `5` — `F.greatest(*cols)` returns the maximum non-null value across all specified columns; null values are ignored in the comparison; only if ALL columns are null does it return null (correct answer — `F.greatest` and `F.least` are element-wise (row-wise) functions that compare values across multiple columns for each row. They skip null values in the comparison, unlike SQL aggregate functions that also skip nulls but operate across rows. For `(5, null, 3)`: `greatest` = 5, `least` = 3. If all columns are null, the result is null. These are commonly used for computing effective dates, confidence scores, or price bounds across multiple source columns.)
- C) `5` only if `spark.sql.ansi.enabled=false`; with ANSI mode enabled null propagation makes the result null
- D) `F.greatest` requires all input columns to have the same type; mixing `IntegerType` and `null` raises `AnalysisException`

---

### Question 64 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.rand(seed) and F.randn(seed) — random number generation column functions

**Question**:
What is the difference between `F.rand(seed=42)` and `F.randn(seed=42)`, and why is using a seed important?

- A) `F.rand` generates random integers; `F.randn` generates random doubles between 0 and 1
- B) `F.rand(seed)` generates values from a **uniform** distribution in `[0.0, 1.0)`; `F.randn(seed)` generates values from a **standard normal** distribution (mean 0, stddev 1); specifying a seed makes the random values reproducible — the same seed produces the same values on re-runs, enabling deterministic testing and reproducible sampling (correct answer — `F.rand` (SQL: `rand()`) and `F.randn` (SQL: `randn()`) generate one random value per row. Without a seed the values differ on every execution. With a fixed seed (any integer) the sequence is reproducible across re-runs with the same number of partitions and data. Common use cases: `F.rand() < 0.1` for 10% sampling (approximate), or `F.randn() * sigma + mean` for synthetic data generation.)
- C) `F.rand` and `F.randn` are identical; the `n` suffix is a legacy alias
- D) `F.rand(seed=42)` produces the same sequence across all partitions; without a seed, each partition generates independent values

---

### Question 65 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.na.replace(to_replace, value, subset) — replacing specific values in selected columns

**Question**:
How do you replace the string `"N/A"` with `None` (null) in the `status` and `category` columns of a DataFrame?

- A) `df.fillna({"status": None, "category": None}, subset=["status","category"])` — use fillna with None to replace strings
- B) `df.na.replace("N/A", None, subset=["status", "category"])` replaces all occurrences of `"N/A"` with `null` in the specified columns; this is the inverse of `fillna` — instead of filling nulls, it converts specific values to null (correct answer — `df.na.replace(to_replace, value, subset)` replaces `to_replace` with `value` in the columns named in `subset` (or all columns if omitted). Both arguments can be scalars or lists/dicts. `replace(["N/A", "NA", "n/a"], None)` replaces multiple patterns at once. Note: `fillna` fills `null` values with a specified value; `replace` converts a specific value to another value. Combined, they are powerful for data cleaning.)
- C) `df.withColumn("status", F.when(F.col("status") == "N/A", None).otherwise(F.col("status")))` — the when/otherwise pattern; `na.replace` does not support replacing with null
- D) `df.na.replace` only works with numeric types; string replacement requires a UDF

---

### Question 66 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: F.from_json(col, ArrayType(schema)) — parsing a JSON string containing a JSON array

**Question**:
A column `items_json` contains strings like `'[{"id":1,"name":"a"},{"id":2,"name":"b"}]'`. How do you parse this JSON array column into an `ArrayType(StructType)` column?

- A) `F.from_json(col("items_json"), StructType([...]))` — `from_json` automatically detects arrays and wraps results in `ArrayType`
- B) `F.from_json(F.col("items_json"), ArrayType(StructType([StructField("id", IntegerType()), StructField("name", StringType())])))` — the schema must explicitly be `ArrayType(element_schema)` to parse a JSON array string; passing a `StructType` directly would parse only the first object (correct answer — `from_json` parses according to the declared schema. Passing `ArrayType(item_schema)` causes it to parse the JSON array and return each element as a struct. Passing `StructType` directly would expect a JSON object `{}` and would return null for array input `[]`. Use `schema_of_json` on a representative sample to infer the DDL schema string, then pass it to `from_json` via `schema_of_json` or `StructType` API.)
- C) `F.json_tuple` can handle JSON arrays when more than one field is extracted
- D) `F.from_json` does not support `ArrayType` schemas; use `F.explode(F.from_json(...))` with a struct schema instead

---

### Question 67 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.alias for self-join disambiguation — resolving ambiguous column references in self-joins

**Question**:
A developer performs a self-join on a DataFrame `df` to find rows where `follower_id` in one row equals `user_id` in another row. Why is `df.alias("a").join(df.alias("b"), ...)` necessary?

- A) Aliases are only needed for outer joins; inner self-joins work without them
- B) Without aliases, both copies of `df` reference the same column objects; any column reference like `col("user_id")` is ambiguous between the two sides of the join, raising `AnalysisException: Reference 'user_id' is ambiguous`; assigning distinct aliases (`"a"` and `"b"`) allows referencing columns as `col("a.user_id")` and `col("b.user_id")` unambiguously (correct answer — Spark's column lineage tracks which DataFrame each column reference originates from. In a self-join, both sides share the same physical columns, causing ambiguity. `df.alias("name")` creates a new logical plan node with a distinct identifier. After aliasing: `df_a = df.alias("a"); df_b = df.alias("b"); df_a.join(df_b, df_a["follower_id"] == df_b["user_id"])` is unambiguous.)
- C) Aliases in self-joins are a PySpark-only requirement; Scala/Java DataSet API handles self-joins without aliases
- D) `df.alias` creates a temporary view; use `df.as("a")` (the Scala syntax) for self-join disambiguation in PySpark

---

### Question 68 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Accessing nested struct fields — dot notation string path vs F.col() with dotted path

**Question**:
A DataFrame has a struct column `address` with a nested field `city`. What is the difference between `df.select("address.city")` and `df.select(F.col("address.city"))`?

- A) String path `"address.city"` uses 0-based nesting; `F.col("address.city")` uses field name lookup
- B) Both `df.select("address.city")` and `df.select(F.col("address.city"))` access the nested `city` field via dot notation and produce identical results; Spark resolves the dotted path to `GetStructField(address, city)` in both cases; use `F.col("`address.city`")` with backtick escaping if the field name itself contains a literal dot (correct answer — Dot notation in column string paths and in `F.col()` both resolve nested struct field access identically. The caveat is field names containing a literal `.` character: `col("address.city")` would try to navigate `address` → `city`; to access a field literally named `"address.city"`, use backtick escaping: `col("`address.city`")`. This is a common source of confusion when column names contain dots.)
- C) `df.select("address.city")` returns a `StructType` column; `F.col("address.city")` extracts only the scalar value
- D) String path notation flattens the struct entirely; `F.col("address.city")` returns only the `city` field

---

### Question 69 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.describe(*cols) vs df.summary(*statistics) — statistical summary capabilities

**Question**:
What statistics can `df.describe()` compute that `df.describe("col")` cannot, and which method supports computing arbitrary percentiles?

- A) `df.describe()` and `df.summary()` are identical; `describe` is simply the older alias
- B) `df.describe()` computes `count`, `mean`, `stddev`, `min`, and `max` for numeric and string columns; `df.summary()` supports all of these plus arbitrary percentiles like `"25%"`, `"50%"`, `"75%"`, `"99%"` — percentile computation is only available through `summary()` (correct answer — `df.describe(*cols)` is fixed to the five statistics: count, mean, stddev, min, max. `df.summary(*statistics)` accepts a flexible list including any percentile as `"p%"` string (e.g., `"1%"`, `"99%"`). Both return a DataFrame. `df.summary()` with no arguments computes: count, mean, stddev, min, 25%, 50%, 75%, max. Use `summary` whenever percentiles or a custom subset of statistics is needed.)
- C) `describe()` works on all column types; `summary()` only supports `DoubleType` columns for percentile computation
- D) `summary()` aggregates across multiple DataFrames; `describe()` is limited to a single DataFrame

---

### Question 70 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.write.insertInto(tableName) — V1 table insert by position vs saveAsTable by schema

**Question**:
How does `df.write.insertInto("sales_table")` differ from `df.write.mode("append").saveAsTable("sales_table")` when the DataFrame column order does not match the table's column order?

- A) Both methods match columns by name; the order of DataFrame columns does not matter for either method
- B) `insertInto` inserts columns **by position** — it matches DataFrame column 1 to table column 1 regardless of names; `saveAsTable("append")` inserts columns **by name**, matching DataFrame column names to table column names; if column order differs, `insertInto` will write wrong values to wrong columns without error (correct answer — `df.write.insertInto` is a V1 DataFrameWriter method that mirrors SQL `INSERT INTO table SELECT *` which is positional. It does not validate column names against the table schema. `saveAsTable` with `mode("append")` uses a column-name resolution that aligns by schema. This is a critical difference: always ensure column order matches when using `insertInto`, or prefer `saveAsTable("append")` for safety.)
- C) `insertInto` raises an error if the DataFrame schema does not exactly match the table schema including nullability
- D) `insertInto` uses the table's partition column to determine insert position; `saveAsTable` ignores partitioning

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.adaptive.enabled — enabled by default in Spark 3.2+, was disabled in Spark 3.1 and earlier

**Question**:
A developer migrates a job from Spark 3.1 to Spark 3.3 and notices the number of post-shuffle tasks has automatically decreased. No configuration changes were made. What most likely explains this?

- A) Spark 3.3 reduced the default value of `spark.sql.shuffle.partitions` from 200 to 50
- B) `spark.sql.adaptive.enabled` changed from `false` (default in Spark 3.1) to `true` (default in Spark 3.2+); with AQE now active, the `CoalesceShufflePartitions` rule automatically merges small post-shuffle partitions, reducing task count for lightweight shuffles (correct answer — AQE was introduced as stable in Spark 3.0 but defaulted to `false`. Spark 3.2 changed the default to `true`. Migration from 3.1 to 3.2+ automatically enables AQE, activating: partition coalescing, runtime join strategy conversion, and skew join handling. This can unexpectedly change task counts and runtimes. To restore pre-3.2 behavior: `spark.sql.adaptive.enabled=false`.)
- C) Spark 3.3 introduced automatic partition pruning that removes empty shuffle partitions during planning
- D) Spark 3.3 changed `spark.sql.shuffle.partitions` to automatically scale based on data size; no AQE involvement

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.mapKeyDedupPolicy — handling duplicate keys during map construction (EXCEPTION vs LAST_WIN)

**Question**:
A query calls `map_from_arrays(array_keys, array_values)` but the keys array contains duplicates. What does `spark.sql.mapKeyDedupPolicy` control?

- A) It configures how map keys are sorted when building the output; `ALPHABETICAL` or `INSERTION_ORDER`
- B) `spark.sql.mapKeyDedupPolicy` controls behavior when duplicate keys are encountered during map construction: `EXCEPTION` (default) raises a `RuntimeException`; `LAST_WIN` silently keeps the last value for each duplicate key, enabling deduplication behavior (correct answer — When Spark builds a `MapType` column using functions like `map_from_arrays`, `map`, or `from_json`, encountering duplicate keys is an error by default (`EXCEPTION`). Setting `spark.sql.mapKeyDedupPolicy=LAST_WIN` changes this to a "last writer wins" policy where later values in the arrays overwrite earlier ones for the same key. This is useful for ETL pipelines that tolerate or expect key duplication in source data.)
- C) The setting controls the hashing algorithm used for MapType columns; `FAST_HASH` or `STABLE_HASH`
- D) `spark.sql.mapKeyDedupPolicy` applies only to joins on MapType columns; map construction always raises on duplicates

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.columnNameOfCorruptRecord — custom column name for malformed records in CSV/JSON reads

**Question**:
When reading a CSV file with `mode="PERMISSIVE"`, where are malformed records stored, and how can the column name for those records be customized?

- A) Malformed records are stored in a `_corrupt_record` column by default; the column name cannot be changed
- B) With `mode="PERMISSIVE"` (default), Spark stores the raw string of each malformed record in a column named `_corrupt_record` by default; `spark.sql.columnNameOfCorruptRecord` sets a custom name; the schema must include a field with this name as `StringType` for the column to appear in the output (correct answer — `PERMISSIVE` mode tries to parse each record and stores parse failures as the raw string in a designated corrupt record column. The column name defaults to the value of `spark.sql.columnNameOfCorruptRecord` (default `"_corrupt_record"`). To capture these failures, the schema passed to `spark.read.schema(...)` must include a field with the matching name and `StringType`. Without this field in the schema, malformed records are silently stored in a hidden column that must be explicitly accessed.)
- C) Malformed records in `PERMISSIVE` mode are silently dropped; no corrupt record column is created
- D) `spark.sql.columnNameOfCorruptRecord` controls the column for schema mismatches only; syntax errors in CSV/JSON always raise exceptions

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.optimizer.inSetSwitchThreshold — converting large IN lists to HashSet lookups

**Question**:
A filter `WHERE product_id IN (1, 2, 3, ..., 250)` is slower than expected. Which optimization threshold controls when Spark switches from a sequential IN-list check to a HashSet lookup?

- A) `spark.sql.optimizer.maxInListSize` — limits how many values can appear in an IN clause before it is split
- B) `spark.sql.optimizer.inSetSwitchThreshold` (default 10) controls when Spark's optimizer converts an `IN(v1, v2, ..., vN)` predicate into an `InSet` (HashSet-based) lookup; when the list size exceeds the threshold, Spark builds a HashSet at plan time and performs O(1) membership tests instead of O(n) sequential comparisons (correct answer — For small IN lists (≤ threshold, default 10), Spark uses `In` which generates code doing `value == v1 || value == v2 || ...`. For larger lists, it switches to `InSet` which builds a `HashSet` from the literal values and uses `.contains()`. The `HashSet` lookup is O(1) vs O(n) for very large lists. Increasing the threshold delays the switch; decreasing it forces earlier HashSet use for even small lists.)
- C) `spark.sql.optimizer.inSetSwitchThreshold` controls when IN lists are converted to semi-joins against a broadcast table
- D) Spark always uses HashSet for IN predicates; the threshold only affects whether the HashSet is built at planning time or at execution time

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.session.timeZone — session-level timezone affecting timestamp display and conversion

**Question**:
A TimestampType column `event_ts` contains `2024-03-15 12:00:00 UTC`. When displayed in a Spark session configured with `spark.sql.session.timeZone=America/New_York`, what value is shown?

- A) `2024-03-15 12:00:00` — Spark always displays timestamps in UTC regardless of session timezone
- B) `2024-03-15 08:00:00` — Spark renders `TimestampType` values in the session timezone; UTC 12:00 is 08:00 in America/New_York (UTC-4 during EDT); `spark.sql.session.timeZone` also affects functions like `current_timestamp()`, `to_timestamp()`, `from_unixtime()`, and timezone-sensitive operations (correct answer — `spark.sql.session.timeZone` (default `UTC` or the JVM default timezone) affects how timestamps are interpreted and displayed. Internally, `TimestampType` stores microseconds since Unix epoch. On display/output, Spark converts to the session timezone. Functions like `hour(ts)` return the hour in the session timezone. This is a common source of off-by-hours bugs in multi-timezone pipelines. Set it explicitly to avoid JVM-default ambiguity.)
- C) `2024-03-15 16:00:00` — Spark converts to America/New_York (UTC+4) which is ahead of UTC
- D) `spark.sql.session.timeZone` only affects the `TIMESTAMP WITH TIME ZONE` type; regular `TimestampType` is always UTC

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.parquet.compression.codec — per-format Parquet compression independent of global codec

**Question**:
A developer sets `spark.io.compression.codec=lz4` globally but wants Parquet output files to use Snappy compression specifically. Which setting controls the Parquet compression codec independently?

- A) `spark.sql.parquet.compression.codec` overrides `spark.io.compression.codec` only for Parquet writes; valid options: `none`, `uncompressed`, `snappy` (default for Parquet), `gzip`, `lzo`, `brotli`, `lz4`, `zstd` (correct answer — Parquet has its own compression configuration (`spark.sql.parquet.compression.codec`, default `snappy`) that takes precedence over the global `spark.io.compression.codec` setting for Parquet file writes. This allows using fast network compression (lz4) for shuffle while storing files in snappy or zstd for a better storage/CPU trade-off. Per-write overrides are also possible via `df.write.option("compression", "zstd")`.)
- B) `spark.io.compression.codec` is the only setting; Parquet uses whatever codec is set globally
- C) `spark.sql.parquet.compression.codec` is a read-time setting; write compression is controlled by `spark.io.compression.codec`
- D) Parquet always uses `snappy` regardless of configuration; the codec cannot be changed without recompiling Spark

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.debug.maxToStringFields — maximum fields shown in plan toString before truncation

**Question**:
A developer logs the physical plan of a wide DataFrame (200 columns) but the logged plan is truncated with `... 170 more fields`. Which configuration removes this truncation?

- A) `spark.sql.showString.truncate` — the setting that controls plan output field count truncation
- B) Increasing `spark.sql.debug.maxToStringFields` (default 25) raises the number of struct/schema fields printed in plan `toString` output and error messages; increasing it to a value > 200 would show all fields in the plan representation (correct answer — `spark.sql.debug.maxToStringFields` limits how many fields Spark includes when converting schemas, structs, and rows to strings for debug output and plan representations. The default of 25 is intentionally small to avoid excessively long log lines. Increasing it helps when debugging wide-schema DataFrames. Note this only affects the `toString` representation; it has no impact on query execution. A related setting is `spark.sql.repl.eagerEval.maxNumRows` for notebook display.)
- C) `spark.sql.maxToStringFields` is the correct setting name; `debug` prefix was removed in Spark 3.x
- D) Schema truncation in plan output cannot be configured; write the schema using `df.printSchema()` to see all fields

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.execution.arrow.maxRecordsPerBatch — batch size for Arrow-enabled toPandas/createDataFrame

**Question**:
A developer calls `df.toPandas()` with Arrow optimization enabled (`spark.sql.execution.arrow.pyspark.enabled=true`). The executor runs out of memory during the conversion. Which setting reduces peak memory usage?

- A) `spark.executor.memory` must be increased; Arrow conversion always uses full partition memory in one step
- B) Decreasing `spark.sql.execution.arrow.maxRecordsPerBatch` (default 10000) reduces the number of rows included in each Arrow record batch during `toPandas()` and `createDataFrame()`; smaller batches lower peak memory usage at the cost of more serialization round trips (correct answer — When Arrow optimization is enabled, Spark converts data to Arrow format in chunks of `maxRecordsPerBatch` rows. With a wide schema or large field values, even 10,000 rows can create very large Arrow batches. Reducing to 1000–5000 reduces the peak memory per batch. The trade-off is more IPC overhead and Python-side append operations. If OOM persists, also consider increasing `spark.executor.memoryOverhead` to account for Arrow's off-heap buffer allocations.)
- C) `spark.sql.execution.arrow.pyspark.enabled=false` must be set; Arrow optimization always uses more memory than row-by-row conversion
- D) `spark.sql.execution.arrow.maxRecordsPerBatch` only applies to Pandas UDFs; `toPandas()` uses a fixed batch size

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.repl.eagerEval.enabled — automatic DataFrame HTML display in notebooks

**Question**:
A developer wants DataFrames to automatically display as HTML tables in a Jupyter notebook connected to a PySpark kernel without explicitly calling `.show()`. Which setting enables this?

- A) `spark.sql.displayMode=html` — enables automatic HTML rendering for all DataFrames in notebook environments
- B) `spark.sql.repl.eagerEval.enabled=true` enables eager evaluation and HTML rendering of DataFrames in REPL and notebook environments (Jupyter, Zeppelin); when enabled, displaying a DataFrame in the last expression of a notebook cell automatically renders it as an HTML table controlled by `spark.sql.repl.eagerEval.maxNumRows` (default 20) and `spark.sql.repl.eagerEval.truncate` (correct answer — `spark.sql.repl.eagerEval.enabled` (default false) activates the `_repr_html_` method on `DataFrame` objects, which Jupyter calls automatically for the last expression in a cell. Without it, DataFrames print their schema description. Related settings: `maxNumRows` controls how many rows appear in the HTML table; `truncate` controls string truncation per cell. This is different from Databricks notebooks which have their own display rendering.)
- C) Automatic DataFrame display requires using `spark.sql()` instead of the DataFrame API; Python DataFrames cannot auto-render
- D) HTML rendering is always enabled in Jupyter; `spark.sql.repl.eagerEval.enabled` only applies to Zeppelin notebooks

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.statistics.fallBackToHdfs — collecting file-level statistics when Hive catalog stats are unavailable

**Question**:
AQE and CBO are enabled but broadcast join conversion is not triggering for a Hive-managed table whose statistics were never collected with `ANALYZE TABLE`. Which configuration helps?

- A) `spark.sql.autoBroadcastJoinThreshold=-1` — disabling the threshold forces AQE to make a runtime decision
- B) `spark.sql.statistics.fallBackToHdfs=true` instructs Spark to estimate table size from the HDFS file sizes when Hive catalog statistics are absent; this gives CBO and AQE a size estimate that can trigger broadcast joins even without explicit `ANALYZE TABLE` runs (correct answer — When `spark.sql.statistics.fallBackToHdfs=true` (default false), Spark falls back to calling `fs.getContentSummary()` on the table's storage path to estimate table size. While less accurate than Hive column-level statistics, it provides a non-null size estimate that enables size-based optimizations like broadcast join selection. This is useful for legacy Hive tables that have never been analyzed. Note the HDFS call adds planning overhead for tables with many files.)
- C) `spark.sql.cbo.enabled=true` alone is sufficient; CBO always estimates table size from schema metadata without HDFS fallback
- D) `spark.sql.adaptive.forceApply=true` forces AQE to apply all optimizations regardless of statistics availability

---

### Question 81 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: trigger(processingTime="30 seconds") — fixed-interval micro-batch trigger behavior

**Question**:
A streaming job is configured with `writeStream.trigger(processingTime="30 seconds")`. The first micro-batch takes 45 seconds to process. When does the next micro-batch start?

- A) The next micro-batch starts exactly 30 seconds after the first batch begins (at T+30s), regardless of whether the first batch is complete
- B) The next micro-batch starts as soon as possible after the first batch finishes (at T+45s); with `processingTime` trigger, Spark waits for the current batch to complete, then waits for the remainder of the interval — since 45s > 30s the interval has already elapsed, so the next batch starts immediately after the first completes (correct answer — `processingTime` trigger runs micro-batches at fixed intervals but never runs two batches concurrently. If a batch takes longer than the interval, the next batch starts immediately after the previous one finishes (no delay). If a batch finishes before the interval elapses, Spark waits for the interval to pass before starting the next batch. Example: interval=30s, batch takes 10s → next batch at T+30s. Batch takes 45s → next batch at T+45s (immediately after).)
- C) The streaming job raises `StreamingQueryException` because the processing time exceeded the trigger interval
- D) Processing time triggers impose a strict maximum batch duration; Spark cancels any batch exceeding the interval

---

### Question 82 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: streaming dropDuplicates with watermark — deduplication bounded by event-time watermark

**Question**:
A developer adds `df.withWatermark("event_time", "10 minutes").dropDuplicates(["event_id", "event_time"])` to a streaming query. What role does the watermark play in deduplication?

- A) The watermark has no effect on `dropDuplicates`; it only affects aggregation output modes
- B) `dropDuplicates` in structured streaming maintains per-key state in a state store; the watermark bounds how long state is retained — once event time advances beyond `watermark_delay`, keys seen more than `10 minutes` ago are evicted from the dedup state, bounding memory growth at the cost of potentially not deduplicating very late duplicates (correct answer — Without a watermark, streaming `dropDuplicates` must keep all seen keys forever, causing unbounded state growth. The watermark defines the point after which a key's event time is considered too old to receive new duplicates. Keys with `max_event_time - 10_minutes > key_event_time` are eligible for state cleanup. This means duplicates arriving more than 10 minutes after the key was first seen may not be deduplicated. Balance between memory usage and duplicate detection completeness is controlled by the watermark delay.)
- C) The watermark causes `dropDuplicates` to only process events within a fixed window; events outside the window are always dropped
- D) `dropDuplicates` with a watermark is equivalent to `dropDuplicates` without a watermark in batch mode; no state accumulation occurs

---

### Question 83 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: writeStream.toTable(tableName) — writing a streaming query to a catalog table (Spark 3.1+)

**Question**:
How does `writeStream.toTable("catalog.bronze.events")` differ from `writeStream.format("delta").option("path", "/delta/events").start()`?

- A) `toTable` writes to an in-memory table only; `.start()` is required for persistent storage
- B) `writeStream.toTable(tableName)` (Spark 3.1+) writes to a catalog-managed or Delta table by name, inferring format and path from the catalog; the table must already exist or be created beforehand; `.start(path)` writes to a path directly without catalog registration, not updating table metadata (correct answer — `toTable` integrates with the catalog and metastore: it calls `Table.append` on the target table, honors the table's configured format (Delta, Parquet, etc.), and updates catalog metadata. `start(path)` writes to a raw path without touching the catalog. `toTable` is the recommended approach for production streaming pipelines that target managed or external Delta Lake tables, as it ensures the catalog always reflects the current state of the data.)
- C) `toTable` and `start(path)` are equivalent; `toTable` is just a shorthand alias for `start` with a catalog table path
- D) `toTable` requires `outputMode("complete")`; it does not support `append` mode

---

### Question 84 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: StreamingQuery.processAllAvailable() — blocking until all pending data is consumed (test utility)

**Question**:
In a unit test for a streaming application using a file source, how does `query.processAllAvailable()` help verify results?

- A) It cancels the query and returns the final results as a static DataFrame for assertion
- B) `processAllAvailable()` blocks the calling thread until the streaming query has processed all data that was available at the time of the call and the micro-batch queue is empty; this allows test code to await completion before asserting on the in-memory sink or result table without polling (correct answer — In tests using a memory sink or Delta table sink, `processAllAvailable()` synchronously waits for all currently available micro-batches to complete. After it returns, assertions on the output are reliable. It is intended for testing only — not for production use where it could block indefinitely if new data keeps arriving. Combine with a timeout via `query.awaitTermination(timeoutMs)` for more robust test teardown.)
- C) `processAllAvailable()` triggers a full reprocessing of all historical data from the checkpoint
- D) It is equivalent to calling `query.stop()` followed by `query.start()`; the query is briefly halted then resumed

---

### Question 85 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: stream-static join — static DataFrame refreshed each micro-batch; no watermark needed

**Scenario**:
A streaming DataFrame of `orders` is joined against a static DataFrame `customers` loaded from a database table that is refreshed periodically.

**Question**:
What are the key behaviors of a stream-static join compared to a stream-stream join?

- A) A stream-static join requires a watermark on the streaming side; without it, Spark cannot determine which orders to join
- B) In a stream-static join: the static side is **re-evaluated** (or re-read from the source) at each micro-batch trigger; no watermark or state store is needed on either side; if the static DataFrame is based on a live source, changes to it between micro-batches will be visible (correct answer — Stream-static joins do not require or maintain state: the static side is a regular batch DataFrame that is broadcast (for small tables) or shuffle-joined into each micro-batch. No state store is used, so there is no watermark requirement. The static DataFrame is logically re-evaluated each batch — if the static source is a live database read, new rows in the database will be visible in subsequent batches. Contrast with stream-stream joins which require watermarks on both sides to bound the join state.)
- C) Stream-static joins are not supported in structured streaming; both sides must be streaming sources
- D) The static side is materialized once at query start and never re-read; changes to the static source are not visible during the query's lifetime

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Rate source options — rowsPerSecond, numPartitions, rampUpTime for synthetic stream generation

**Question**:
A developer uses the Rate source for load testing: `spark.readStream.format("rate").option("rowsPerSecond", 1000).load()`. What columns does the Rate source produce?

- A) A single `value` column of `LongType` containing the row's sequence number
- B) Two columns: `timestamp` (`TimestampType`) — the time the row was generated — and `value` (`LongType`) — a monotonically increasing row counter starting from 0; `numPartitions` controls parallelism; `rampUpTime` gradually increases the rate from 0 to `rowsPerSecond` over a specified duration (correct answer — The Rate source generates synthetic streaming data at a controlled rate for testing purposes. Each row has `timestamp` (generation time) and `value` (sequential integer, global across all partitions). `numPartitions` sets the number of source partitions (default depends on available cores). `rampUpTime` is useful for simulating gradual load increases. The source is not available in Spark Connect without special configuration.)
- C) Three columns: `partition_id`, `timestamp`, and `value`; `partition_id` identifies which partition generated the row
- D) The Rate source generates only `value` (`LongType`) with no timestamp; timestamp tracking must be added manually with `F.current_timestamp()`

---

### Question 87 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.sql.streaming.stateStore.providerClass — RocksDB vs default HDFS-backed state store

**Question**:
A stateful streaming job using `flatMapGroupsWithState` accumulates large state (> 10 GB). The default state store causes excessive JVM GC pauses. What alternative does `spark.sql.streaming.stateStore.providerClass` offer?

- A) Set `providerClass` to `InMemoryStateStoreProvider` for faster in-memory state access with no GC overhead
- B) Setting `spark.sql.streaming.stateStore.providerClass=org.apache.spark.sql.execution.streaming.state.RocksDBStateStoreProvider` switches to an off-heap RocksDB state store; RocksDB manages state in native (off-heap) memory, avoiding JVM GC for large state while still supporting checkpoint-based fault tolerance (correct answer — The default `HDFSBackedStateStoreProvider` stores state in-memory on the executor JVM heap, causing GC pressure for large state. The RocksDB state store (available in Databricks Runtime and OSS Spark 3.2+ via a separate module) uses embedded RocksDB on each executor to store state off-heap with LSM-tree efficiency. It is significantly better for workloads with large state (tens of GBs per executor). Checkpointing writes RocksDB SST files to the checkpoint directory.)
- C) RocksDB state store is available only in Databricks Runtime; it is not a supported option in open-source Apache Spark
- D) `providerClass` only affects how state is checkpointed, not where it resides; state always lives in executor JVM memory

---

### Question 88 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: writeStream.format("memory").queryName("tbl") — in-memory sink for testing streaming queries

**Question**:
A developer writes a streaming test using `.format("memory").queryName("counts")`. How are the results accessed after processing?

- A) The memory sink stores results in a Kafka in-memory topic accessible via `spark.readStream.format("memory")`
- B) The memory sink stores results in an in-memory temporary view named by `queryName`; after running micro-batches, results are accessible via `spark.sql("SELECT * FROM counts")` as a regular batch DataFrame (correct answer — The memory sink is a test-only output mode that accumulates streamed rows into a named in-memory table. With `outputMode("append")`, new rows are appended. With `outputMode("complete")`, the table is overwritten with the full aggregated result each trigger. Query the table using `spark.table("counts")` or `spark.sql("SELECT * FROM counts")`. Since data is in-memory, the table is lost when the SparkSession ends. It is not suitable for production use due to memory limitations.)
- C) Results are stored in the driver's local memory and must be retrieved via `query.lastProgress["sink"]["numOutputRows"]`
- D) The memory sink writes to a temporary file in `/tmp`; use `spark.read.parquet("/tmp/counts")` to access results

---

### Question 89 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.streaming.numShufflePartitions — override shuffle partitions specifically for streaming micro-batches

**Question**:
A streaming job has `spark.sql.shuffle.partitions=200` but this produces too many tiny tasks in each micro-batch (which process small amounts of data). How can the shuffle parallelism be reduced specifically for streaming without affecting batch jobs in the same session?

- A) `spark.sql.streaming.partitions=50` — the streaming-specific partition count setting
- B) `spark.sql.streaming.numShufflePartitions` (Spark 3.x) overrides `spark.sql.shuffle.partitions` specifically for streaming queries; setting it to a lower value (e.g., 50) reduces shuffle parallelism within each micro-batch without changing batch SQL behavior in the same session (correct answer — Streaming micro-batches typically process far less data per trigger than batch queries, so the global `spark.sql.shuffle.partitions=200` default creates excessive overhead per batch. `spark.sql.streaming.numShufflePartitions` (default: inherits from `spark.sql.shuffle.partitions`) provides a streaming-scoped override. Note: with AQE enabled (`spark.sql.adaptive.enabled=true`), AQE's partition coalescing also helps by merging small post-shuffle partitions automatically.)
- C) `writeStream.option("shufflePartitions", "50")` — per-query shuffle partition option in the write stream configuration
- D) The shuffle partition count for streaming cannot be configured separately; change `spark.sql.shuffle.partitions` globally

---

### Question 90 — Structured Streaming

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Kafka startingOffsets — "earliest" vs "latest" vs per-partition JSON offset specification

**Question**:
A production streaming job reading from Kafka is restarted after being stopped for 2 hours. The checkpoint contains committed offsets. What value of `startingOffsets` is used, and when would "earliest" or "latest" take effect?

- A) `startingOffsets` is always used on every restart; the checkpoint offsets are ignored
- B) On restart with a valid checkpoint, Spark **ignores** `startingOffsets` and resumes from the checkpoint's committed offsets; `startingOffsets` only takes effect on the **first start** when there is no checkpoint; `"latest"` skips all unprocessed messages accumulated during the pause, while `"earliest"` processes all accumulated messages from the beginning (correct answer — `startingOffsets` is a bootstrap option for the initial (checkpointless) start only. Once the streaming query has written a checkpoint with committed Kafka offsets, all subsequent restarts resume from those offsets, making `startingOffsets` irrelevant. This behavior ensures exactly-once semantics. To override checkpoint offsets (e.g., to replay data), delete the checkpoint directory first. A JSON string `{"topic": {"0": 100, "1": 200}}` can specify per-partition starting offsets for the initial start.)
- C) `startingOffsets="latest"` always causes the job to start from the newest offset regardless of checkpoint state
- D) With a checkpoint present, `startingOffsets` is applied as an override to the checkpoint offsets; this is the intended mechanism for manual offset adjustment

---

### Question 91 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: SparkSession.builder.remote() — building a Spark Connect client session with the sc:// URL scheme

**Question**:
How does a PySpark client connect to a remote Spark Connect server running on `host.example.com`, and how does this differ from a standard `getOrCreate()` session?

- A) `SparkSession.builder.master("sc://host.example.com:15002").getOrCreate()` — use the `master()` method with the `sc://` URL
- B) `SparkSession.builder.remote("sc://host.example.com:15002").getOrCreate()` establishes a gRPC connection to the Spark Connect server; no local JVM Spark instance is created on the client; all plan building is done locally and execution happens on the remote server (correct answer — `builder.remote(url)` uses the `sc://` (Spark Connect) URL scheme. The client library (`pyspark[connect]`) builds a logical plan locally and sends it over gRPC to the server. No local SparkContext, DAGScheduler, or executor is created on the client side. The environment variable `SPARK_REMOTE` can also set the URL. The default Spark Connect server port is 15002. `getOrCreate()` with `remote()` returns a `SparkSession` whose `sparkContext` property raises `PySparkNotImplementedError`.)
- C) `spark = SparkSession.connect("sc://host.example.com:15002")` — the static `connect()` factory method
- D) Spark Connect uses HTTP/2; the URL scheme is `http://host.example.com:15002` not `sc://`

---

### Question 92 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.addArtifact() — Spark Connect replacement for sc.addFile() and sc.addPyFile()

**Question**:
In a Spark Connect session, a developer needs to distribute a Python helper module `utils.py` and a JAR file `custom_funcs.jar` to the Spark Connect server for use in UDFs. What replaces `sc.addFile()` and `sc.addJar()`?

- A) `spark.sparkContext.addFile("utils.py")` — SparkContext methods still work through the Connect proxy
- B) `spark.addArtifact("utils.py")` and `spark.addArtifact("custom_funcs.jar")` are the Spark Connect replacements for `sc.addFile` and `sc.addJar`; the artifacts are uploaded to the Spark Connect server and distributed to executors before the next query (correct answer — Spark Connect introduces `SparkSession.addArtifact(path)` (and the plural `addArtifacts`) as the Connect-compatible API for distributing Python modules, ZIP archives, and JARs to the server-side executor environment. `sparkContext.addFile/addJar` are unavailable in Connect sessions because there is no local SparkContext. Artifacts are uploaded over the gRPC channel and cached on the server. `spark.addArtifact("*.py", pyfile=True)` explicitly marks Python files.)
- C) `spark.conf.set("spark.files", "utils.py,custom_funcs.jar")` — configuration-based artifact distribution for Connect sessions
- D) Artifact distribution is not supported in Spark Connect; UDFs must be pure Python lambdas using only built-in modules

---

### Question 93 — Spark Connect

**Difficulty**: Hard
**Answer Type**: one
**Topic**: start-connect-server.sh — starting a Spark Connect server with additional packages

**Question**:
A developer wants to start a Spark Connect server that includes the Delta Lake and Kafka connectors. Which command starts the server with these packages?

- A) `spark-submit --class org.apache.spark.sql.connect.SparkConnectServer --packages "io.delta:delta-spark_2.12:3.0.0,org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.0"`
- B) `./sbin/start-connect-server.sh --packages "io.delta:delta-spark_2.12:3.0.0,org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.0"` — the dedicated Connect server startup script accepts standard `spark-submit` options including `--packages`, `--jars`, and `--conf` (correct answer — `start-connect-server.sh` is the script in `$SPARK_HOME/sbin/` for starting a long-running Spark Connect server process. It wraps `spark-submit` with the `SparkConnectServer` main class and forwards all extra arguments (like `--packages`, `--conf`, `--num-executors`) to the underlying spark-submit invocation. The server defaults to port 15002 (`spark.connect.grpc.binding.port`). Stop it with `stop-connect-server.sh`.)
- C) `spark-connect-server start --packages "..."` — the new unified CLI introduced in Spark 3.4
- D) The Spark Connect server must be started via a Python `subprocess.run(["python", "-m", "pyspark.connect.server"])` call

---

### Question 94 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.connect.grpc.binding.port — Spark Connect server gRPC port default (15002)

**Question**:
A developer is configuring firewalls and load balancers for a Spark Connect deployment. What is the default port the Spark Connect server listens on, and how can it be changed?

- A) Port 7077 (the default Spark master port); configure with `spark.master.port`
- B) The Spark Connect server defaults to port **15002** (`spark.connect.grpc.binding.port`); changing this setting on the server and updating the client URL `"sc://host:custom_port"` is all that is required (correct answer — Spark Connect uses gRPC over port 15002 by default. This is distinct from Spark's other ports: 4040 (Spark UI), 7077 (standalone master), 18080 (History Server). `spark.connect.grpc.binding.port` on the server side and the port in the `sc://host:port` client URL must match. For TLS, additional `spark.connect.grpc.ssl.*` configurations are needed. The `sc://host` URL with no port defaults to 15002.)
- C) Port 4040 — the Spark Connect server reuses the Spark UI port to minimize firewall configuration
- D) The Spark Connect port is not configurable; 15002 is hard-coded in the gRPC server implementation

---

### Question 95 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.conf.set() behavior in Spark Connect — session config changes propagate to the server

**Question**:
In a Spark Connect session, a developer calls `spark.conf.set("spark.sql.shuffle.partitions", "50")`. What happens?

- A) `AnalysisException` is raised; session configuration changes are not supported in Spark Connect
- B) The `conf.set()` call is serialized as part of the next logical plan sent to the Spark Connect server; the server updates the session-level configuration for that client's session only; the change is scoped to the client's logical session and does not affect other clients connected to the same server (correct answer — Spark Connect supports `spark.conf.set/get/unset` for session-level configurations. Configuration changes are transmitted to the server as part of the gRPC request context. Session isolation ensures each client has its own configuration namespace. However, some configurations that affect the Spark server itself (e.g., executor counts) may not be dynamically changeable after session creation. Static configurations set on the server at startup cannot be overridden per-session.)
- C) `conf.set()` modifies the client-side configuration only; it has no effect on the server-side execution
- D) Session configuration changes in Connect require calling `spark.conf.set()` followed by `spark.restart()` to take effect

---

### Question 96 — Pandas API on Spark

**Difficulty**: Easy
**Answer Type**: one
**Topic**: ps.DataFrame.melt(id_vars, value_vars) — reshaping wide format to long format

**Question**:
A Pandas-on-Spark DataFrame has columns `(user_id, q1_score, q2_score, q3_score)`. How do you reshape it to long format `(user_id, quarter, score)` using the Pandas API on Spark?

- A) `psdf.unpivot(ids=["user_id"], values=["q1_score","q2_score","q3_score"], variableColumnName="quarter", valueColumnName="score")` — unpivot is the Pandas-on-Spark method
- B) `psdf.melt(id_vars=["user_id"], value_vars=["q1_score","q2_score","q3_score"], var_name="quarter", value_name="score")` reshapes from wide to long format, stacking the value columns into rows; the output has one row per original row per value column (correct answer — `ps.DataFrame.melt()` mirrors `pandas.DataFrame.melt()` exactly in API. `id_vars` columns are repeated for each `value_vars` column; `var_name` names the new column holding the original column name; `value_name` names the new column holding the cell value. For 100 rows × 3 value columns = 300 rows output. Under the hood, Spark SQL's `stack()` expression or `unpivot` implements this efficiently.)
- C) `psdf.pivot(columns=["q1_score","q2_score","q3_score"], values="user_id")` — pivot is the inverse; use it with reset_index
- D) Wide-to-long reshaping is not supported in the Pandas API on Spark; convert to a regular Spark DataFrame and use `F.stack` in `selectExpr`

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.DataFrame.pivot_table(values, index, columns, aggfunc) — pivot with aggregation

**Question**:
A Pandas-on-Spark DataFrame has columns `(region, product, sales)`. How do you create a pivot table showing the total sales per region (rows) per product (columns)?

- A) `psdf.pivot("region", "product", "sales")` — the three-argument pivot method with no aggregation
- B) `psdf.pivot_table(values="sales", index="region", columns="product", aggfunc="sum")` computes aggregated pivot values; when multiple `(region, product)` pairs exist in the data, values are summed; missing combinations produce `NaN` (correct answer — `ps.DataFrame.pivot_table()` mirrors `pandas.DataFrame.pivot_table()`. The `aggfunc` argument (default `"mean"`) specifies the aggregation for duplicate `(index, columns)` combinations. The underlying Spark execution uses `groupBy(...).pivot(...).agg(...)`. `fill_value` parameter replaces `NaN` in the result (equivalent to Spark's `pivot.agg(...).na.fill()`). `margins=True` adds subtotal rows/columns similar to pandas.)
- C) `psdf.crosstab("region", "product")` — crosstab is the pivot_table equivalent; pass the value column as a parameter
- D) `ps.DataFrame.pivot_table` requires the DataFrame to be sorted by `index` first; otherwise results are non-deterministic

---

### Question 98 — Pandas API on Spark

**Difficulty**: Hard
**Answer Type**: one
**Topic**: ps.DataFrame.merge() — supported join types and performance characteristics vs native Spark join

**Question**:
A developer uses `ps1.merge(ps2, how="left", on="user_id")` in Pandas API on Spark. What join types are supported and what is the performance implication of this call vs `spark_df1.join(spark_df2, "user_id", "left")`?

- A) Only `inner` and `left` joins are supported; `right` and `outer` joins raise `NotImplementedError`
- B) `ps.DataFrame.merge()` supports all standard join types: `inner`, `left`, `right`, `outer` (full), `cross`, and also `left_semi`/`left_anti` via `how`; it translates directly to a Spark `join` under the hood with equivalent performance; no pandas data is collected to the driver for the join operation (correct answer — `ps.DataFrame.merge()` mirrors `pandas.DataFrame.merge()` semantics and is fully distributed — it does not collect data to the driver. The underlying plan is a Spark join with the same physical operators (BroadcastHashJoin, SortMergeJoin, etc.) as the native `DataFrame.join`. Performance is identical to the native Spark join. `ps.merge(left, right, ...)` is the module-level function equivalent to `left.merge(right, ...)`.)
- C) `ps.DataFrame.merge()` always collects both DataFrames to the driver and performs the join in pandas memory; avoid for large DataFrames
- D) `ps.merge` uses only the `inner` join type by default and does not support `how` parameter overrides for other join types

---

### Question 99 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.DataFrame.assign(new_col=lambda df: ...) — adding computed columns with functional syntax

**Question**:
How does `psdf.assign(full_name=lambda df: df["first"] + " " + df["last"])` work in Pandas API on Spark, and what makes it different from `psdf["full_name"] = psdf["first"] + " " + psdf["last"]`?

- A) `assign` is not supported in Pandas API on Spark; use `withColumn` on the underlying Spark DataFrame
- B) `psdf.assign(new_col=lambda df: expression)` returns a **new** DataFrame with the added column, following functional immutability; the lambda receives `df` as the full DataFrame so expressions can reference other newly assigned columns; the `[]` assignment mutates the existing `psdf` object in-place (pandas-style mutation) (correct answer — `assign()` follows pandas semantics: it returns a new DataFrame (does not modify `psdf`). Multiple columns can be added in one call and they can reference each other in order: `psdf.assign(a=lambda d: d.x + 1, b=lambda d: d.a * 2)`. In Pandas API on Spark, `psdf["col"] = value` uses `__setitem__` which performs an internal `withColumn`-equivalent mutation. The `assign` style is preferred in pipelines for its functional, method-chainable form.)
- C) `assign` and `[]` assignment are identical; `assign` is just syntactic sugar for in-place column addition
- D) `assign` requires all lambda return values to be scalar; column-expression lambdas raise `TypeError`

---

### Question 100 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.DataFrame.explode(col) — expanding list/array column to individual rows

**Question**:
A Pandas-on-Spark DataFrame has an `ArrayType` column `tags` where each row contains a list of strings. How does `psdf.explode("tags")` behave, and how does it handle null and empty list values?

- A) `explode` raises `TypeError` for array columns; convert to a Spark DataFrame and use `F.explode` instead
- B) `psdf.explode("tags")` mirrors `pandas.DataFrame.explode()`: each list element becomes its own row with all other columns repeated; `NaN` / `None` values in the list column produce a row with `NaN` for the exploded value (preserving the row); empty lists also produce `NaN` for the exploded value (correct answer — `ps.DataFrame.explode(col)` follows pandas 0.25+ `explode` semantics: null/NaN and empty list values are preserved as `NaN` rows rather than being dropped. This differs from Spark's `F.explode` (which drops null/empty rows) but matches `F.explode_outer`. Under the hood, Pandas API on Spark translates this to a Spark `explode_outer` (or equivalent) to match the pandas null-preservation behavior. The index is reset after explosion.)
- C) `explode` processes only the first element of each list and ignores the rest; use `apply(pd.Series)` for full explosion
- D) `psdf.explode("tags")` returns one row per original row with `tags` converted to a comma-separated string instead of exploded rows

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | `spark.sql.autoBroadcastJoinThreshold` defaults to 10 MiB (10 485 760 bytes); tables smaller than this are automatically broadcast. | topic1-prompt5-broadcasting-optimization.md | Easy |
| 2 | B | `spark.memory.fraction` (0.6) allocates a unified pool split between execution and storage via `spark.memory.storageFraction` (0.5). | topic1-prompt1-spark-architecture.md | Medium |
| 3 | B | `spark.executor.memoryOverheadFactor=0.15` → overhead = max(0.15 × 8192, 384) = 1228 MiB; total = 8192 + 1228 = 9420 MiB. | topic1-prompt1-spark-architecture.md | Hard |
| 4 | B | `lz4` is the default for `spark.io.compression.codec`; it balances CPU cost and compression ratio for shuffle spill and broadcast. | topic1-prompt4-shuffling-performance.md | Medium |
| 5 | B | The `ReuseExchange` optimizer rule replaces the second identical `ShuffleExchangeExec` with a `ReusedExchangeExec` node, avoiding redundant shuffle. | topic1-prompt1-spark-architecture.md | Hard |
| 6 | B | `spark.sql.adaptive.coalescePartitions.minPartitionSize` is the floor that prevents AQE from merging partitions below the minimum size. | topic1-prompt1-spark-architecture.md | Medium |
| 7 | B | `spark.shuffle.service.db.enabled=true` persists the External Shuffle Service's block metadata to LevelDB so registered executors survive driver restarts. | topic1-prompt4-shuffling-performance.md | Hard |
| 8 | B | Scheduler delay is the gap between when the TaskScheduler submits a task and when the executor begins executing it; it reflects task launch overhead. | topic1-prompt1-spark-architecture.md | Medium |
| 9 | B | Catalyst applies transformation rules repeatedly until the plan stabilizes; `spark.sql.optimizer.maxIterations` caps the loop to prevent infinite cycles. | topic1-prompt1-spark-architecture.md | Hard |
| 10 | B | `SparkSession.newSession()` shares the underlying `SparkContext` but creates an isolated catalog and configuration namespace. | topic1-prompt1-spark-architecture.md | Medium |
| 11 | B | `mergeSchema=true` reads the footer schema from every Parquet file and unions them, filling missing fields with `null`. | topic1-prompt1-spark-architecture.md | Medium |
| 12 | B | Parquet's `filterPushdown` uses column-chunk min/max statistics to skip entire row groups that cannot contain matching values. | topic1-prompt1-spark-architecture.md | Medium |
| 13 | B | `spark.sql.planCacheSize` is the LRU cache capacity for parsed and analyzed logical plans, enabling reuse across repeated SQL queries. | topic1-prompt1-spark-architecture.md | Hard |
| 14 | B | `ignoreCorruptFiles` skips unreadable files; `ignoreMissingFiles` skips files that disappear after planning; they are independent flags. | topic1-prompt1-spark-architecture.md | Medium |
| 15 | B | AQE's `CoalesceShufflePartitions` merges small post-shuffle partitions at runtime, reducing task count below `spark.sql.shuffle.partitions`. | topic1-prompt1-spark-architecture.md | Easy |
| 16 | B | Programmatic `SparkConf` > `spark-submit` flags > `spark-defaults.conf`; the programmatic setting (8g) wins over all others. | topic1-prompt1-spark-architecture.md | Medium |
| 17 | B | `spark.sql.legacy.timeParserPolicy=LEGACY` restores the Spark 2.x lenient date/time parser for formats not valid under `CORRECTED` mode. | topic1-prompt1-spark-architecture.md | Hard |
| 18 | B | `spark.sql.jsonGenerator.ignoreNullFields=true` omits keys with `null` values from JSON output, reducing payload size. | topic1-prompt1-spark-architecture.md | Medium |
| 19 | B | `spark.sql.sources.useV1SourceList` is a comma-separated list of format names that Spark routes to the V1 DataSource API instead of V2. | topic1-prompt1-spark-architecture.md | Hard |
| 20 | B | After AQE converts SortMergeJoin to BroadcastHashJoin at runtime, `LocalShuffleReaderExec` reads shuffle data locally, avoiding cross-node data transfer. | topic1-prompt1-spark-architecture.md | Hard |
| 21 | B | `F.transform(array_col, func)` applies `func` element-wise and returns a new array of the same length with transformed values. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 22 | B | `F.filter(array_col, func)` returns a new `ArrayType` column containing only elements where the predicate returns `true`; output length varies per row. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 23 | B | `posexplode` generates two columns: `pos` (`IntegerType`, 0-based index) and `col` (element value); use `posexplode_outer` to preserve null/empty rows. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 24 | B | `F.explode` silently drops rows where the array is `null` or empty; `F.explode_outer` preserves those rows with a `null` element value. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 25 | B | `arrays_zip` returns `ArrayType(StructType)` with fields named after the input columns and null-pads shorter arrays to match the longest. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 26 | B | `to_date` returns `DateType` (no time component); `to_timestamp` returns `TimestampType`; the format string is optional but required for non-ISO inputs. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 27 | B | `F.from_json(col, schema)` parses a JSON string into a typed struct/map column; `F.to_json(col)` serializes a struct or map column back to a JSON string. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 28 | B | `get_json_object(col, path)` always returns `StringType` regardless of the JSON value's actual type; use `from_json` to get typed results. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 29 | B | `json_tuple(col, *fields)` parses the JSON string once and returns multiple `StringType` columns, more efficient than multiple `get_json_object` calls. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 30 | B | `concat_ws("-", year_col, month_col, day_col)` concatenates string representations with `"-"` as separator, producing `"2024-03-15"`. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 31 | B | `split(col, "/", 3)` with `limit=3` produces exactly 3 elements: `["", "usr", "local/bin/spark"]`; the third element absorbs all remaining text. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 32 | B | `F.element_at` uses 1-based indexing; index `-1` refers to the last element, while Python's `[]` subscript is 0-based. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 33 | B | `F.base64(BinaryType)` → `StringType` (Base64 string); `F.unbase64(StringType)` → `BinaryType`; useful for embedding binary payloads in text formats. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 34 | B | `F.levenshtein(l, r) <= 2` returns rows where the minimum single-character edit distance between the two strings is at most 2. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 35 | B | `F.hex(255)` → `StringType` `"FF"`; `F.unhex("FF")` → `BinaryType` byte `0xFF`; use `conv(hex_str, 16, 10)` to convert hex to an integer. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 36 | B | `F.sentences(text)` returns `ArrayType(ArrayType(StringType()))` — each outer element is a sentence array; each inner element is a word. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 37 | B | `F.map_filter(map_col, lambda k, v: predicate)` retains only key-value pairs where the predicate is `true`, returning a new `MapType` column. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 38 | B | `F.format_string("User %d: %s", id_col, name_col)` applies printf-style formatting, returns a `StringType` column, and handles type conversion automatically. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 39 | B | 2024 is a leap year (Feb has 29 days), so March 1 is day 61 (31 + 29 + 1); `F.dayofyear` returns `IntegerType` in range 1–366. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 40 | B | `F.map_keys` and `F.map_values` both return `ArrayType` columns; index `i` in keys corresponds to index `i` in values, but overall order is non-deterministic. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 41 | B | `df.transform(func)` is syntactic sugar for `func(df)`, enabling fluent chaining of DataFrame transformation functions without nesting. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 42 | B | `df.unpivot(ids, values, variableColumnName, valueColumnName)` (Spark 3.4+) converts wide format to long; also available as `df.melt` alias. | topic3-prompt14-filtering-row-manipulation.md | Hard |
| 43 | B | `F.struct(col1.alias("first"), col2.alias("last"))` packs columns into a `StructType` with controlled field names accessible via dot notation. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 44 | B | `F.map_entries(map_col)` converts each map to `ArrayType(StructType([key, value]))`, enabling array HOFs like `F.array_sort` on map contents. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 45 | B | `F.inline(array_of_structs)` explodes an array of structs into one row per element with separate columns for each struct field. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 46 | B | `unionByName(other, allowMissingColumns=True)` (Spark 3.1+) aligns columns by name and fills missing columns with `null` instead of raising an error. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 47 | B | `df.stat.bloomFilter(col, n, fpp)` returns a `BloomFilter` object (not a DataFrame) that answers set-membership queries with no false negatives. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 48 | B | `F.schema_of_csv(lit(sample))` returns a `StringType` DDL schema string (e.g., `"_c0 BIGINT,_c1 STRING"`), not a `StructType` object. | topic3-prompt21-schemas-data-types.md | Medium |
| 49 | B | Avro is not bundled with Spark; add `org.apache.spark:spark-avro_2.12:x.x.x` via `--packages` or as a JAR to enable `format("avro")`. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 50 | B | `option("timestampFormat", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")` controls `TimestampType` serialization in CSV/JSON writes; `dateFormat` controls `DateType`. | topic3-prompt19-reading-writing-dataframes.md | Hard |
| 51 | B | `df.union` aligns columns by position (can silently mix values); `df.unionByName` aligns by name, preventing positional mismatches. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 52 | B | `F.named_struct(lit("x"), lit(3.0), lit("y"), lit(4.0))` interleaves string name literals with value expressions to explicitly build a `StructType` column. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 53 | B | `createGlobalTempView` raises `TempTableAlreadyExistsException` if the name is taken; use `createOrReplaceGlobalTempView` to safely overwrite. | topic2-prompt8-spark-sql-fundamentals.md | Medium |
| 54 | B | `F.element_at(map_col, key)` returns `null` for missing keys in non-ANSI mode; use `F.try_element_at` (Spark 3.4+) for always-null-safe access. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 55 | B | `df.stat.countMinSketch(col, eps, confidence, seed)` returns a `CountMinSketch` object that estimates per-value frequency with bounded error and no false negatives. | topic3-prompt12-dataframe-creation-selection.md | Hard |
| 56 | B | `df.explain(mode="codegen")` prints the generated Java source code from whole-stage codegen; other modes: `simple`, `extended`, `cost`, `formatted`. | topic4-prompt26-debugging.md | Medium |
| 57 | B | `df.summary(*stats)` accepts percentiles like `"25%"`, `"99%"`; `df.describe()` is fixed to count, mean, stddev, min, max only. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 58 | B | `percent_rank()` = (rank−1)/(n−1) → `0.0` for the first row; `cume_dist()` = rows_leq/n → always > 0 since the current row counts itself. | topic2-prompt10-window-functions.md | Hard |
| 59 | B | `ntile(4)` over 10 rows assigns bucket sizes 3, 3, 2, 2 (largest buckets first); bucket 1 contains the lowest-ordered rows. | topic2-prompt10-window-functions.md | Medium |
| 60 | B | `F.lag(col, 1, 0)` returns the `default` value (`0`) for the first row because no preceding row exists at offset 1. | topic2-prompt10-window-functions.md | Easy |
| 61 | B | `df.withColumns(dict)` (Spark 3.3+) adds or replaces multiple columns in a single projection, avoiding overhead from chained `withColumn` calls. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 62 | B | `F.slice(array, 2, 3)` uses 1-based indexing: start=2 is `"b"`, length=3 returns `["b","c","d"]`; exceeding the array bounds truncates silently. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 63 | B | `F.greatest(*cols)` returns the maximum non-null value across columns per row; result is null only if all inputs are null. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 64 | B | `F.rand(seed)` → uniform [0.0, 1.0); `F.randn(seed)` → standard normal (mean 0, stddev 1); a fixed seed ensures reproducible results. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 65 | B | `df.na.replace("N/A", None, subset=[...])` converts a specific string to null; the inverse of `fillna` which fills nulls with a value. | topic3-prompt16-handling-nulls.md | Medium |
| 66 | B | `F.from_json(col, ArrayType(StructType([...])))` requires an explicit `ArrayType` schema to parse a JSON array string; a bare `StructType` schema would return null. | topic3-prompt13-column-manipulation-expressions.md | Hard |
| 67 | B | Without aliases both copies of `df` share the same column lineage, causing `AnalysisException: Reference is ambiguous`; `df.alias("a")` creates a distinct logical node. | topic3-prompt17-joins.md | Medium |
| 68 | B | Both `df.select("address.city")` and `df.select(F.col("address.city"))` navigate nested struct fields identically; use backtick escaping for field names containing literal dots. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 69 | B | `df.describe()` is fixed to five statistics (count, mean, stddev, min, max); only `df.summary()` supports percentiles and a custom subset. | topic3-prompt12-dataframe-creation-selection.md | Easy |
| 70 | B | `insertInto` matches columns by **position** (like `INSERT INTO … SELECT *`); `saveAsTable("append")` matches by **name**, preventing silent column mismatches. | topic3-prompt19-reading-writing-dataframes.md | Medium |
| 71 | B | `spark.sql.adaptive.enabled` changed from `false` (default in Spark 3.1) to `true` (default in Spark 3.2+), automatically activating AQE's partition coalescing. | topic4-prompt24-performance-tuning.md | Medium |
| 72 | B | `spark.sql.mapKeyDedupPolicy=EXCEPTION` (default) raises on duplicate map keys; `LAST_WIN` silently keeps the last value for each duplicate key. | topic4-prompt24-performance-tuning.md | Hard |
| 73 | B | `PERMISSIVE` mode stores raw malformed records in `_corrupt_record` by default; `spark.sql.columnNameOfCorruptRecord` customizes this name; the schema must include it as `StringType`. | topic4-prompt26-debugging.md | Medium |
| 74 | B | `spark.sql.optimizer.inSetSwitchThreshold` (default 10) controls when `IN(v1,…,vN)` switches from O(n) sequential comparison to O(1) `HashSet` lookup. | topic4-prompt24-performance-tuning.md | Hard |
| 75 | B | `spark.sql.session.timeZone=America/New_York` renders UTC 12:00 as 08:00 (EDT, UTC-4); it also affects `current_timestamp()` and other timezone-sensitive functions. | topic4-prompt24-performance-tuning.md | Medium |
| 76 | A | `spark.sql.parquet.compression.codec` overrides the global `spark.io.compression.codec` for Parquet writes specifically; default is `snappy`. | topic4-prompt24-performance-tuning.md | Hard |
| 77 | B | `spark.sql.debug.maxToStringFields` (default 25) limits how many fields Spark includes when converting schemas and rows to strings for debug output. | topic4-prompt26-debugging.md | Medium |
| 78 | B | Decreasing `spark.sql.execution.arrow.maxRecordsPerBatch` (default 10 000) reduces rows per Arrow batch during `toPandas()`, lowering peak memory at the cost of more IPC overhead. | topic4-prompt24-performance-tuning.md | Hard |
| 79 | B | `spark.sql.repl.eagerEval.enabled=true` activates `_repr_html_` on `DataFrame` objects in Jupyter/Zeppelin, auto-rendering results as HTML tables without `.show()`. | topic4-prompt26-debugging.md | Medium |
| 80 | B | `spark.sql.statistics.fallBackToHdfs=true` estimates table size from HDFS `getContentSummary()` when Hive catalog statistics are absent, enabling broadcast join selection. | topic4-prompt24-performance-tuning.md | Hard |
| 81 | B | With `processingTime` trigger, if a batch takes longer than the interval the next batch starts immediately after the previous finishes; batches never run concurrently. | topic5-prompt27-structured-streaming.md | Medium |
| 82 | B | The watermark bounds deduplication state retention; keys with event time older than `max_event_time − watermark_delay` are evicted from the state store, preventing unbounded growth. | topic5-prompt28-stateful-streaming.md | Hard |
| 83 | B | `writeStream.toTable(name)` (Spark 3.1+) writes to a catalog-registered table (inferring format/path); `start(path)` writes to a raw path without updating the catalog. | topic5-prompt27-structured-streaming.md | Medium |
| 84 | B | `query.processAllAvailable()` blocks until all currently available data is processed; it is a test utility — not for production where new data may arrive indefinitely. | topic5-prompt27-structured-streaming.md | Easy |
| 85 | B | In a stream-static join the static DataFrame is re-evaluated each micro-batch; no watermark or state store is required, unlike stream-stream joins which need both. | topic5-prompt27-structured-streaming.md | Hard |
| 86 | B | The Rate source produces two columns: `timestamp` (`TimestampType`) and `value` (`LongType` monotonically increasing counter starting at 0). | topic5-prompt27-structured-streaming.md | Medium |
| 87 | B | `providerClass=RocksDBStateStoreProvider` switches the state store to off-heap RocksDB, eliminating JVM GC pressure for large state workloads. | topic5-prompt28-stateful-streaming.md | Hard |
| 88 | B | The memory sink stores results in an in-memory temp view named by `queryName`; query with `spark.sql("SELECT * FROM counts")` — for testing only, not production. | topic5-prompt27-structured-streaming.md | Medium |
| 89 | B | `spark.sql.streaming.numShufflePartitions` overrides `spark.sql.shuffle.partitions` for streaming micro-batches only, without affecting batch SQL in the same session. | topic5-prompt27-structured-streaming.md | Medium |
| 90 | B | With a valid checkpoint Spark ignores `startingOffsets` and resumes from committed offsets; `startingOffsets` only applies to the first (checkpointless) start. | topic5-prompt27-structured-streaming.md | Hard |
| 91 | B | `SparkSession.builder.remote("sc://host:15002").getOrCreate()` creates a gRPC client session; no local JVM Spark instance or `SparkContext` is created on the client. | topic6-prompt29-spark-connect.md | Easy |
| 92 | B | `spark.addArtifact(path)` is the Spark Connect replacement for `sc.addFile()`/`sc.addJar()`; artifacts are uploaded over gRPC and distributed to executors. | topic6-prompt29-spark-connect.md | Medium |
| 93 | B | `./sbin/start-connect-server.sh --packages "..."` starts a Spark Connect server and forwards all `spark-submit` options including `--packages`, `--jars`, and `--conf`. | topic6-prompt29-spark-connect.md | Hard |
| 94 | B | The Spark Connect server defaults to gRPC port **15002** (`spark.connect.grpc.binding.port`); update both server config and client `sc://host:port` URL to change it. | topic6-prompt29-spark-connect.md | Medium |
| 95 | B | `spark.conf.set()` in a Connect session is serialized with the next gRPC request; changes are scoped to the client's logical session and do not affect other clients. | topic6-prompt29-spark-connect.md | Medium |
| 96 | B | `psdf.melt(id_vars, value_vars, var_name, value_name)` mirrors `pandas.DataFrame.melt()`; each value column becomes a row, producing `rows × len(value_vars)` output rows. | topic7-prompt30-pandas-api.md | Easy |
| 97 | B | `psdf.pivot_table(values, index, columns, aggfunc="sum")` aggregates duplicate `(index, columns)` combinations; missing pairs produce `NaN` (use `fill_value` to replace). | topic7-prompt30-pandas-api.md | Medium |
| 98 | B | `ps.DataFrame.merge()` supports all join types (`inner`, `left`, `right`, `outer`, `cross`) and translates directly to a distributed Spark join — no driver collection occurs. | topic7-prompt30-pandas-api.md | Hard |
| 99 | B | `psdf.assign(new_col=lambda df: expr)` returns a **new** DataFrame (immutable); `psdf["col"] = value` mutates the object in-place (pandas-style `__setitem__`). | topic7-prompt30-pandas-api.md | Medium |
| 100 | B | `psdf.explode("tags")` mirrors `pandas.explode()`: null/empty list rows produce a `NaN` row (preserved); under the hood Spark uses `explode_outer` semantics. | topic7-prompt30-pandas-api.md | Medium |
