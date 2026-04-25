# Databricks Certified Associate Developer for Apache Spark — Question Bank (Iteration 7)

**Iteration**: 7

**Generated**: 2026-04-25

**Total Questions**: 100

**Difficulty Split**: 16 Easy / 80 Medium / 4 Hard

**Answer Types**: 100 `one`

---

## Questions

---

### Question 1 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.executor.memoryOverhead — container off-heap memory

**Question**:
What does `spark.executor.memoryOverhead` control when running on YARN or Kubernetes?

- A) The maximum JVM heap size for each executor; it is equivalent to `--executor-memory`
- B) The amount of off-heap memory reserved by the cluster manager (YARN/K8s) for each executor container in addition to the JVM heap (`spark.executor.memory`); this overhead accommodates Python/R worker memory, native libraries, NIO direct buffers, and OS overhead — it is not part of the Spark unified memory pool and is not GC-managed
- C) The total executor memory cap enforced by Spark's built-in memory manager; exceeding it triggers executor eviction
- D) The size of the off-heap storage region for cached DataFrames when `spark.memory.offHeap.enabled=true`

---

### Question 2 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.speculation — speculative task launch conditions

**Question**:
Under what conditions does Spark launch a speculative copy of a running task when `spark.speculation=true`?

- A) Spark immediately relaunches any task that has been running longer than `spark.speculation.timeout` (default 100 ms)
- B) A speculative task is launched when ALL of the following are true: (1) the task has been running longer than `spark.speculation.multiplier` (default 1.5) × the median task duration for the stage AND (2) the fraction of completed tasks in the stage has reached `spark.speculation.quantile` (default 0.75) — both conditions must hold simultaneously so that a meaningful median exists and the slow task is genuinely an outlier
- C) Spark relaunches any task that exceeds `spark.task.maxFailures` retries regardless of stage completion fraction
- D) Speculation is triggered only for tasks that produced at least one shuffle write failure; tasks that are simply slow are never speculated

---

### Question 3 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.task.maxFailures — job abort threshold

**Question**:
What does `spark.task.maxFailures` (default `4`) control?

- A) The maximum number of times the same stage can be retried before the job is aborted
- B) The maximum number of times any individual task attempt can fail (across all executors and retries) before Spark aborts the job with a `SparkException`; a task counts as failed each time its attempt ends in an error — once total failures for a single task reach this limit, the entire job is stopped
- C) The maximum number of executor failures allowed before the application exits; it operates at the executor level, not the task level
- D) The maximum number of consecutive task failures on a single executor before that executor is blacklisted

---

### Question 4 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: sc.setJobGroup / sc.cancelJobGroup — interactive job management

**Question**:
What is the purpose of `sc.setJobGroup(groupId, description, interruptOnCancel=False)` combined with `sc.cancelJobGroup(groupId)`?

- A) They partition the DAGScheduler's task queue into named groups so that jobs with the same `groupId` share a single stage and avoid re-computation
- B) `sc.setJobGroup` tags all Spark jobs submitted from the calling thread with a `groupId` and descriptive label (visible in the Spark UI Jobs tab); `sc.cancelJobGroup` then cancels all running and queued jobs that share that `groupId` — the pattern is useful in interactive notebooks or multi-threaded apps where a user action (e.g., "Cancel Query") should terminate several related Spark jobs at once; `interruptOnCancel=True` also interrupts the underlying Java thread
- C) They create an isolated SparkSession namespace; jobs in different groups cannot read each other's cached DataFrames
- D) They configure the FAIR scheduler pool priority; higher-priority groups get more task slots

---

### Question 5 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.default.parallelism vs spark.sql.shuffle.partitions

**Question**:
What is the key operational difference between `spark.default.parallelism` and `spark.sql.shuffle.partitions`?

- A) They are aliases; setting either one controls the post-shuffle partition count for all Spark operations
- B) `spark.default.parallelism` sets the default number of partitions for RDD operations such as `reduceByKey`, `join`, and `parallelize` (when no explicit `numSlices` is given); `spark.sql.shuffle.partitions` (default `200`) sets the number of partitions used after a shuffle in DataFrame/Dataset and Spark SQL operations — changing `spark.default.parallelism` does NOT affect DataFrame shuffle partition counts
- C) `spark.sql.shuffle.partitions` controls the input partition count when reading files; `spark.default.parallelism` controls the output partition count after a shuffle
- D) `spark.default.parallelism` applies only to streaming applications; `spark.sql.shuffle.partitions` applies only to batch SQL queries

---

### Question 6 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.network.timeout — umbrella network inactivity timeout

**Question**:
What does `spark.network.timeout` (default `120s`) govern in a Spark cluster?

- A) The maximum time Spark waits for a Kafka broker to respond during a streaming read
- B) It acts as the default timeout for all network interactions between Spark components; specifically, it controls how long the driver waits before declaring an executor lost when no heartbeat is received — if `spark.executor.heartbeatInterval` is not independently set, it should always be configured to be significantly less than `spark.network.timeout` to avoid false executor evictions
- C) It caps the total wall-clock time for a single task; tasks that run longer than this value are killed by the driver
- D) It sets the read timeout for HDFS and cloud storage file reads; exceeding it causes the file read to retry

---

### Question 7 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.rpc.message.maxSize — max RPC message size

**Question**:
What happens when the data being broadcast via `sc.broadcast()` or a broadcast join exceeds `spark.rpc.message.maxSize` (default `128 MB`)?

- A) Spark automatically splits the broadcast into multiple RPC messages and reassembles them on each executor with no visible error
- B) Spark raises a `SparkException` with a message indicating the RPC message size exceeded the configured limit; to broadcast objects larger than 128 MB the property must be increased (e.g., `spark.conf.set("spark.rpc.message.maxSize", "256")`) — the value is in megabytes
- C) Spark silently falls back to a sort-merge join instead of broadcasting the large table
- D) The broadcast succeeds but only for the first 128 MB of data; rows beyond that limit are excluded from the broadcast value

---

### Question 8 — Apache Spark Architecture & Internals

**Difficulty**: Hard
**Answer Type**: one
**Topic**: spark.shuffle.sort.bypassMergeThreshold — bypass merge-sort shuffle path

**Question**:
When does Spark use the `BypassMergeSortShuffleWriter` (the bypass merge-sort shuffle path) instead of the standard `SortShuffleWriter`?

- A) Always when `spark.shuffle.compress=false`; the bypass path is the default when compression is disabled
- B) The bypass merge-sort path is used when two conditions are met: (1) the downstream number of reduce partitions is at or below `spark.shuffle.sort.bypassMergeThreshold` (default `200`) AND (2) the shuffle does not require map-side aggregation (i.e., no `combiner`); in this path Spark writes one file per output partition directly without sorting, then concatenates them — this avoids the CPU cost of sorting but opens O(numReducePartitions) file handles simultaneously, so it is only efficient for small partition counts
- C) The bypass path is activated whenever `spark.shuffle.manager=sort` AND the stage produces more than 200 shuffle map tasks
- D) The bypass path is only used for hash-based shuffles (`spark.shuffle.manager=hash`); the sort shuffle manager always uses `SortShuffleWriter`

---

### Question 9 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.reducer.maxSizeInFlight — shuffle fetch concurrency limit

**Question**:
What does `spark.reducer.maxSizeInFlight` (default `48 MB`) control?

- A) The maximum size of a single shuffle block that can be transferred over the network; blocks larger than this are split automatically
- B) The maximum total bytes of remote shuffle blocks that a single reducer task may request simultaneously from all remote executors; limiting concurrent in-flight fetch traffic prevents reducer tasks from overwhelming remote executor block servers and reduces peak memory consumption during shuffle read — lowering it trades throughput for memory at the cost of more round trips
- C) The maximum shuffle output file size written by a map task; map output files larger than this are spilled to a secondary disk location
- D) The maximum aggregate shuffle data size after which Spark automatically increases `spark.sql.shuffle.partitions` via AQE

---

### Question 10 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.excludeOnFailure — executor/node exclusion (renamed from blacklist)

**Question**:
What is `spark.excludeOnFailure.enabled` and what changed in Spark 3.1?

- A) It is a new feature introduced in Spark 3.1 that permanently removes executors with repeated failures; it did not exist in earlier versions
- B) `spark.excludeOnFailure.enabled` (Spark 3.1+) is the renamed replacement for `spark.blacklist.enabled`; when enabled, Spark tracks per-executor and per-node task failure counts and temporarily excludes (excludes from scheduling) executors or nodes that exceed configurable thresholds — the rename was a deliberate terminology change to remove offensive language; both names are recognized in Spark 3.1 for backward compatibility but the `blacklist` prefix is deprecated
- C) It replaces `spark.task.maxFailures`; setting `excludeOnFailure.enabled=true` disables `maxFailures` enforcement
- D) It only applies to YARN; on Kubernetes and Standalone the old `spark.blacklist.enabled` flag must still be used

---

### Question 11 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.executor.heartbeatInterval — relationship to network.timeout

**Question**:
Why must `spark.executor.heartbeatInterval` be set significantly lower than `spark.network.timeout`?

- A) Because `spark.executor.heartbeatInterval` controls the shuffle block fetch frequency; if it is too close to `spark.network.timeout` the shuffle will time out
- B) Executors send periodic heartbeats to the driver to prove they are alive; if an executor's heartbeat does not arrive within `spark.network.timeout`, the driver marks the executor as lost and kills its tasks; setting `heartbeatInterval` close to or above `network.timeout` means a single delayed heartbeat (due to GC pause, I/O congestion, etc.) could exceed the timeout and cause a false executor eviction — the default interval is `10s` against a default timeout of `120s`, leaving a large safety margin
- C) Because the heartbeat carries shuffle metadata that must arrive before the timeout; if the interval is too long the shuffle output map statuses are lost
- D) The heartbeat interval controls the driver's health-check frequency; setting it too high wastes driver CPU cycles processing unnecessary heartbeats

---

### Question 12 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.scheduler.listenerbus.eventqueue.capacity — dropped events warning

**Question**:
What does the log warning `"Dropped N SparkListenerEvent events from spark"` indicate?

- A) That N executor tasks failed silently and their results were dropped from the result collection
- B) The Spark event listener bus uses a bounded asynchronous queue (default capacity controlled by `spark.scheduler.listenerbus.eventqueue.capacity`, default `10000`) to decouple event producers (the scheduler) from consumers (UI, history, custom listeners); if the queue fills up because listeners are processing events slower than they are produced, new events are dropped rather than blocking the scheduler — the warning signals that monitoring data (stage/task metrics, job progress) may be incomplete, and the queue capacity should be increased or slow custom listeners optimised
- C) That N rows were dropped by a streaming query due to watermark expiry
- D) That the Spark History Server connection was lost and N events could not be persisted to the event log

---

### Question 13 — Apache Spark Architecture & Internals

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark web UI port allocation — 4040, 4041, 18080

**Question**:
A developer starts two Spark applications on the same machine simultaneously. The first application's UI is at `http://localhost:4040`. Where will the second application's UI be served, and where is the Spark History Server UI?

- A) The second application's UI is also at port 4040; both share the same UI endpoint; the History Server is at port 8080
- B) The second application's UI is at port 4041 — Spark attempts port 4040 first; if occupied it increments by 1 until a free port is found (4041, 4042, etc.); the Spark History Server UI is separately served on port 18080 by default; live application UIs and the History Server are distinct services
- C) The second application's UI is at port 4040 on a different hostname; the History Server is at port 4040 on the master node
- D) The second application has no UI because Spark only allows one live application UI per machine

---

### Question 14 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: MEMORY_AND_DISK_SER storage level — serialized in-memory representation

**Question**:
How does `StorageLevel.MEMORY_AND_DISK_SER` differ from `StorageLevel.MEMORY_AND_DISK`?

- A) `MEMORY_AND_DISK_SER` stores partitions as serialized byte arrays in JVM memory (smaller footprint) and serialized on disk when spilling; `MEMORY_AND_DISK` stores deserialized Java objects in memory and serialized on disk; `SER` variants reduce GC pressure because the JVM sees large byte arrays rather than many small objects, but require deserialization CPU cost on each read — useful when memory is tight and GC overhead is high
- B) `MEMORY_AND_DISK_SER` stores data in off-heap memory using `sun.misc.Unsafe`; `MEMORY_AND_DISK` uses on-heap JVM memory exclusively
- C) They are functionally identical; `_SER` is simply an alias for backward compatibility with Spark 1.x
- D) `MEMORY_AND_DISK_SER` compresses data before storing it; `MEMORY_AND_DISK` stores data uncompressed

---

### Question 15 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.speculation.multiplier and spark.speculation.quantile

**Question**:
With the defaults `spark.speculation.multiplier=1.5` and `spark.speculation.quantile=0.75`, what determines whether a specific running task is a speculative candidate?

- A) The task must have been running for more than 1.5 times the total stage duration AND at least 75% of all stages in the job must be complete
- B) First, `spark.speculation.quantile=0.75` ensures speculation only considers tasks once 75% of the stage's tasks have finished (providing a stable median); then `spark.speculation.multiplier=1.5` means only tasks whose runtime exceeds `1.5 × median_completed_task_time` for that stage are launched speculatively — a task running 50% longer than average (and the stage is 75% done) becomes a speculation candidate
- C) The task's CPU utilization must be below `(1 / 1.5)` of the median CPU usage for the stage AND 75% of executors must be idle
- D) 75% of the task's input bytes must have been processed AND the remaining 25% would take more than 1.5× the median task time to complete

---

### Question 16 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.driver.memoryOverhead — container memory for driver

**Question**:
What does `spark.driver.memoryOverhead` control when submitting to YARN or Kubernetes?

- A) It sets the driver's maximum JVM heap size; it is equivalent to `spark.driver.memory`
- B) It is the amount of non-JVM memory allocated to the driver container by the cluster manager, above and beyond the JVM heap (`spark.driver.memory`); it covers Python/R process memory (when the driver is a PySpark shell), native library allocations, NIO buffers, and OS overhead — the container's total requested memory is `spark.driver.memory + spark.driver.memoryOverhead`; the default is `max(driverMemory × 0.10, 384MB)`
- C) It controls the maximum size of objects that can be collected back to the driver; `df.collect()` fails if the result exceeds this value
- D) It reserves driver memory exclusively for broadcast variables; broadcast data larger than this value is stored on executors only

---

### Question 17 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: sc.broadcast() — serialization and per-executor sharing

**Question**:
How does `sc.broadcast(value)` transmit the broadcast variable to executors, and how is it shared among tasks on the same executor?

- A) The variable is serialized separately into every task's closure; each task on every executor receives its own copy through the normal task serialization path
- B) The driver serializes the broadcast value once, sends it to each executor via the block manager (using a torrent-like peer distribution in cluster mode to reduce driver network bottleneck); within a single executor, all concurrent tasks share one deserialized copy held in the executor's broadcast cache — this is the key efficiency gain: O(executors) network transfers instead of O(tasks)
- C) The broadcast value is stored in HDFS and each executor reads it independently from the distributed file system
- D) Spark re-serializes and re-sends the broadcast value to each executor at the start of every stage that references it

---

### Question 18 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.worker.cleanup.enabled — Standalone worker cleanup

**Question**:
What does `spark.worker.cleanup.enabled` (default `false`) do in Spark Standalone mode?

- A) It automatically restarts crashed workers after a configurable delay; combined with `spark.worker.cleanup.interval`, it ensures worker daemons are always running
- B) When set to `true`, each Standalone Worker daemon periodically deletes the working directories of finished applications from its local disk; the cleanup interval is controlled by `spark.worker.cleanup.interval` (default `1800s = 30 min`) and only removes directories for applications that have been dead for at least `spark.worker.cleanup.appDataTtl` (default `7 days`) — useful for preventing disk exhaustion on long-running clusters; this feature is specific to Standalone mode and has no effect on YARN or Kubernetes
- C) It enables automatic log rotation for Spark executor logs on workers; without it, logs grow unboundedly
- D) It forces Standalone Workers to clean cached shuffle files after each job; setting it to `false` (default) causes shuffle data to accumulate until the cluster is restarted

---

### Question 19 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: client vs cluster deploy mode — driver location impact

**Question**:
A data engineer submits a PySpark application with `--deploy-mode cluster`. How does this change driver behavior compared to `--deploy-mode client`?

- A) In `cluster` mode the driver runs inside the submitting client process, so the client machine must remain connected; in `client` mode the driver runs on a cluster node and the client can disconnect after submission
- B) In `cluster` mode the driver process runs on a cluster node (chosen by the cluster manager); the `spark-submit` client process exits after the application is accepted, so the client machine does not need to stay connected; driver logs are on the cluster node (not the local terminal); `client` mode runs the driver in the submitting process — driver output and logs appear locally, but the client machine must remain alive for the duration of the job
- C) `cluster` mode enables multi-tenant driver sharing; `client` mode isolates the driver in a dedicated JVM
- D) In `cluster` mode all executor memory is doubled to compensate for the driver running remotely

---

### Question 20 — Apache Spark Architecture & Internals

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.dynamicAllocation.executorIdleTimeout — executor removal

**Question**:
What does `spark.dynamicAllocation.executorIdleTimeout` (default `60s`) control when dynamic allocation is enabled?

- A) The maximum time an executor can run a single task before it is considered stalled and removed
- B) When an executor has been idle (no running tasks) for longer than `executorIdleTimeout`, Spark requests the cluster manager to remove it; this reduces resource consumption during quiet periods; if shuffle data is cached on the idle executor and `spark.dynamicAllocation.shuffleTracking.enabled=false`, Spark will not remove it (to preserve shuffle outputs) — only executors without tracked shuffle data are eligible for removal after the timeout
- C) The time Spark waits after all executors become idle before shutting down the entire application
- D) It controls the polling interval for dynamic allocation; Spark checks executor utilization every `executorIdleTimeout` seconds

---

### Question 21 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: timestampdiff() — integer time unit difference (Spark 3.3+)

**Question**:
What does `SELECT timestampdiff('HOUR', TIMESTAMP '2026-01-01 09:00:00', TIMESTAMP '2026-01-01 13:45:00')` return?

- A) `4.75` — the fractional number of hours between the two timestamps
- B) `4` — `timestampdiff` (Spark 3.3+) returns an `IntegerType` value representing the **truncated** number of complete units elapsed; 4 hours and 45 minutes = 4 complete hours; fractional units are truncated toward zero, not rounded
- C) `285` — `timestampdiff` with `'HOUR'` returns the equivalent in minutes
- D) `NULL` — `timestampdiff` requires `DateType` inputs; `TimestampType` inputs are not supported

---

### Question 22 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: months_between() — fractional month calculation

**Question**:
What value does `months_between('2026-03-20', '2026-01-15')` return, and how is a fractional result calculated?

- A) `2` — `months_between` always returns a whole integer representing complete calendar months
- B) `2.16...` — `months_between(date1, date2)` returns a `DoubleType` value; when both dates fall on the last day of their respective months, the result is a whole number; otherwise it computes whole months plus a fractional part based on the day difference divided by 31 (a fixed assumption); `(March 20 − January 15)` = 2 whole months + 5 days → `2 + 5/31 ≈ 2.16129`
- C) `64.0` — `months_between` returns the difference expressed as days divided by the average days-per-month
- D) It raises an `AnalysisException` because `months_between` requires `DateType` columns; string literals are not automatically cast

---

### Question 23 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: last_day() — last day of month

**Question**:
What does `last_day('2026-02-10')` return, and how does it behave in a leap year?

- A) `'2026-02-28'` for a non-leap year; `'2026-02-29'` for a leap year — `last_day(date)` returns the date of the final calendar day in the same month and year as the input date; it correctly accounts for varying month lengths including February in leap years (2024 would return `'2024-02-29'`)
- B) `'2026-03-01'` — `last_day` returns the first day of the following month
- C) `28` — `last_day` returns an `IntegerType` representing the day number, not a full date
- D) It raises a `DateTimeException` for February inputs because February length is ambiguous without a timezone

---

### Question 24 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: next_day() — next occurrence of a named weekday

**Question**:
What does `next_day('2026-04-25', 'MON')` return? (2026-04-25 is a Saturday.)

- A) `'2026-04-25'` — because `next_day` returns the same date if it already falls on the specified day of the week
- B) `'2026-04-27'` — `next_day(date, dayOfWeek)` returns the first date **after** the given date that falls on the specified day of the week; since 2026-04-25 is a Saturday, the next Monday is two days later: 2026-04-27; if the input date itself were already a Monday, `next_day` would return the Monday of the following week (7 days later), not the same date
- C) `'2026-04-20'` — `next_day` returns the most recent past occurrence of the specified weekday
- D) It raises an `IllegalArgumentException` because `'MON'` is not a valid day abbreviation; the full name `'Monday'` is required

---

### Question 25 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: from_unixtime() — Unix epoch to formatted string

**Question**:
What does `from_unixtime(1745539200, 'yyyy-MM-dd')` return, and what is the return type?

- A) A `TimestampType` value representing the UTC instant at the given Unix epoch seconds
- B) A `StringType` value containing the formatted date/time string derived from the Unix epoch seconds interpreted in the session's local timezone — `from_unixtime(epoch, format)` converts a Unix timestamp (seconds since `1970-01-01 00:00:00 UTC`) into a human-readable string using the given format pattern; when no format is provided the default is `'yyyy-MM-dd HH:mm:ss'`; the return type is always `StringType`, not `TimestampType`
- C) A `LongType` value representing the epoch seconds shifted by the local timezone offset
- D) `NULL` — `from_unixtime` requires a `TimestampType` first argument; integer epoch values are not accepted

---

### Question 26 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: date_add() / date_sub() — add or subtract days from a date

**Question**:
What does `date_add('2026-01-28', 5)` return, and what type does it produce?

- A) `'2026-01-33'` — `date_add` adds calendar days without rolling over month boundaries
- B) `'2026-02-02'` — `date_add(date, n)` adds `n` calendar days and correctly rolls over month and year boundaries, returning a `DateType` value; `date_sub(date, n)` subtracts days and returns the same `DateType`; both functions accept `DateType`, `TimestampType`, or parseable string literals as the first argument
- C) A `TimestampType` value at midnight UTC on `2026-02-02`
- D) `'2026-02-02 00:00:00'` as a `StringType` formatted timestamp

---

### Question 27 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: to_timestamp() — with and without explicit format pattern

**Question**:
What is the difference between `to_timestamp('25/04/2026 14:30', 'dd/MM/yyyy HH:mm')` and `to_timestamp('2026-04-25T14:30:00')`?

- A) There is no difference; both accept any date string and auto-detect the format
- B) `to_timestamp(str, format)` parses the string using the explicit `java.time` pattern provided — required when the string deviates from ISO 8601; `to_timestamp(str)` with no format expects an ISO 8601 / default Spark timestamp string (e.g., `'yyyy-MM-dd HH:mm:ss'` or `'yyyy-MM-ddTHH:mm:ss'`); both return `TimestampType`; `to_timestamp` returns `NULL` rather than raising an exception when parsing fails (unlike `CAST(... AS TIMESTAMP)` with ANSI mode enabled)
- C) `to_timestamp` with a format string returns a `DateType`; `to_timestamp` without a format string returns a `TimestampType`
- D) `to_timestamp(str)` only works with UNIX epoch strings; `to_timestamp(str, fmt)` is required for all human-readable formats

---

### Question 28 — Spark SQL

**Difficulty**: Easy
**Answer Type**: one
**Topic**: dayofweek() — day-of-week numbering convention

**Question**:
What does `dayofweek('2026-04-25')` return? (2026-04-25 is a Saturday.)

- A) `6` — because `dayofweek` uses Monday=1 through Sunday=7 (ISO weekday convention)
- B) `7` — `dayofweek` follows the Java `Calendar` convention where Sunday=1, Monday=2, Tuesday=3, Wednesday=4, Thursday=5, Friday=6, Saturday=7; Saturday therefore maps to `7`; to convert to ISO weekday (Monday=1) use `((dayofweek(col) + 5) % 7) + 1`
- C) `0` — `dayofweek` uses a 0-based index where Sunday=0 and Saturday=6
- D) `5` — Saturday is the fifth business day of the week

---

### Question 29 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: date_trunc() vs trunc() — timestamp vs date truncation

**Question**:
What is the difference between `date_trunc('MONTH', ts_col)` and `trunc(date_col, 'MM')`?

- A) They are interchangeable aliases; both accept either `DateType` or `TimestampType` and return the same type
- B) `date_trunc('MONTH', ts_col)` operates on a `TimestampType` input and returns a `TimestampType` set to the first instant of the month (e.g., `'2026-04-01 00:00:00'`); `trunc(date_col, 'MM')` operates on a `DateType` input and returns a `DateType` representing the first day of the month (e.g., `'2026-04-01'`); use `date_trunc` when you need timestamp-precision truncation and `trunc` for date-only truncation
- C) `date_trunc` truncates to the nearest calendar boundary always rounding down; `trunc` truncates to the nearest boundary but rounds up when past the midpoint of the period
- D) `trunc(date_col, 'MM')` is a deprecated alias; `date_trunc` is the only supported function in Spark 3.0+

---

### Question 30 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: unix_timestamp() — string/timestamp to Unix epoch seconds

**Question**:
What does `unix_timestamp('2026-04-25 12:00:00', 'yyyy-MM-dd HH:mm:ss')` return?

- A) A `TimestampType` representing the given date and time
- B) A `LongType` (bigint) value containing the number of seconds elapsed since the Unix epoch (`1970-01-01 00:00:00 UTC`) for the given date-time string interpreted in the session's local timezone; `unix_timestamp(str, format)` accepts a custom format pattern; `unix_timestamp()` with no arguments returns the current time as epoch seconds; this is the inverse of `from_unixtime`
- C) A `StringType` containing the formatted date string with timezone offset appended
- D) A `DoubleType` value with millisecond precision after the decimal point

---

### Question 31 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: sha1() / sha2() — cryptographic hash functions

**Question**:
What are the return types and output lengths of `sha1(col)` and `sha2(col, 256)`?

- A) Both return `BinaryType` containing the raw hash bytes
- B) Both return `StringType` containing a lowercase hexadecimal string; `sha1(col)` produces a 40-character hex string (SHA-1, 160-bit digest); `sha2(col, 256)` produces a 64-character hex string (SHA-256, 256-bit digest); valid bit-length arguments for `sha2` are `0` (equivalent to 256), `224`, `256`, `384`, and `512` — passing an unsupported length returns `NULL`
- C) `sha1` returns `StringType`; `sha2` returns `BinaryType`
- D) Both return `IntegerType` containing a 32-bit hash code suitable for bucketing

---

### Question 32 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: base64() / unbase64() — binary column encoding

**Question**:
What are the input and output types of `base64(col)` and `unbase64(str_col)`?

- A) `base64` accepts `StringType` and returns a `StringType` with special characters escaped; `unbase64` reverses the escaping
- B) `base64(col)` accepts a `BinaryType` column and returns a `StringType` containing the Base64-encoded representation of the bytes; `unbase64(str_col)` accepts a `StringType` column of Base64-encoded text and returns `BinaryType` decoded bytes — to encode a string column, first `CAST(str_col AS BINARY)` before passing to `base64`
- C) Both functions operate on `StringType` columns; `base64` appends a `=` padding marker and `unbase64` removes it
- D) `base64` returns a `BinaryType` of compressed bytes; `unbase64` decompresses them back to the original string

---

### Question 33 — Spark SQL

**Difficulty**: Hard
**Answer Type**: one
**Topic**: sentences() — nested tokenization into array of arrays

**Question**:
What does `sentences("Hello world! How are you?")` return?

- A) `array("Hello", "world", "How", "are", "you")` — a flat array of individual words, punctuation stripped
- B) `array(array("Hello", "world"), array("How", "are", "you"))` — `sentences(str)` tokenizes text at sentence boundaries first (splitting on `.`, `!`, `?`, etc.) and then at word boundaries within each sentence, returning an `ArrayType(ArrayType(StringType))` — each inner array is one sentence's word tokens; punctuation is excluded; useful for NLP pre-processing
- C) A `StructType` with fields `sentence_count` and `word_count`
- D) `array("Hello world", "How are you")` — `sentences` splits at sentence boundaries only and returns the sentences as raw strings, not tokenized words

---

### Question 34 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: levenshtein() — edit distance between strings

**Question**:
What does `levenshtein('kitten', 'sitting')` return?

- A) `0.57` — the normalised Levenshtein similarity score (between 0 and 1)
- B) `3` — `levenshtein(str1, str2)` returns an `IntegerType` value representing the minimum number of single-character edits (insertions, deletions, substitutions) required to transform `str1` into `str2`; `'kitten' → 'sitting'` requires 3 edits (substitute `k`→`s`, substitute `e`→`i`, append `g`); in Spark 3.5+ a third argument `threshold` can be passed — if the distance exceeds the threshold, `-1` is returned instead, enabling efficient early termination
- C) `7` — `levenshtein` returns the length of the longer string when the strings have no characters in common
- D) `NULL` — `levenshtein` is a SQL-only function and is not available in the PySpark DataFrame API via `F.levenshtein`

---

### Question 35 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: element_at() — 1-based indexing and out-of-bounds behavior

**Question**:
What is the result of `element_at(array(10, 20, 30), -1)` and how does it differ from Python list indexing?

- A) It raises an `ArrayIndexOutOfBoundsException` because negative indices are not supported in Spark SQL
- B) `30` — `element_at(array_col, idx)` uses 1-based indexing for positive indices (`1` = first element) and negative indexing for reverse access (`-1` = last element, `-2` = second-to-last); when `idx` is out of bounds it returns `NULL` instead of raising an exception — unlike Python which uses 0-based indexing and raises `IndexError` for out-of-bounds access; the 1-based convention also applies to maps: `element_at(map_col, key)` returns the value for the key or `NULL` if absent
- C) `NULL` — `element_at` does not support negative indices; out-of-range access always returns `NULL`
- D) `10` — `element_at` with a negative index wraps forward from the beginning of the array

---

### Question 36 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: slice() — array sub-array extraction

**Question**:
What does `slice(array(10, 20, 30, 40, 50), 2, 3)` return?

- A) `array(20, 30, 40)` — `slice(array_col, start, length)` uses 1-based `start` position and returns `length` elements; `start=2` means begin at the second element (`20`), `length=3` means take 3 elements (`20`, `30`, `40`)
- B) `array(30, 40, 50)` — `slice` uses 0-based indexing; index 2 is the third element
- C) `array(20, 30)` — `length` in `slice` is exclusive (end index), so 3 means elements at positions 2 and 3 only
- D) It raises an `AnalysisException` because `slice` requires the second argument to be a column reference, not a literal integer

---

### Question 37 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: array_join() — join array elements with null handling

**Question**:
What does `array_join(array('a', NULL, 'c'), '-', 'N/A')` return, and what happens without the third argument?

- A) `array_join` raises a `NullPointerException` when the array contains NULL elements regardless of the third argument
- B) `'a-N/A-c'` — `array_join(array_col, delimiter, null_replacement)` joins non-null array elements with the delimiter, substituting `null_replacement` for any NULL elements; when called without the `null_replacement` argument — `array_join(array_col, delimiter)` — NULL elements are silently skipped, producing `'a-c'` in this example
- C) `'a-null-c'` — `array_join` always converts NULL to the string `"null"` regardless of the third argument
- D) `NULL` — if any element in the array is NULL, the entire result of `array_join` is NULL

---

### Question 38 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: filter() higher-order function — keep matching array elements

**Question**:
What does `filter(array(1, 2, 3, 4, 5), x -> x > 3)` return?

- A) `2` — `filter` returns the count of elements satisfying the predicate
- B) `array(4, 5)` — `filter(array_col, func)` is a higher-order function that applies the lambda predicate to each element and returns a new array containing only the elements for which the predicate evaluates to `true`; elements for which the predicate returns `false` or `NULL` are excluded; available as both a SQL function and `F.filter(col, func)` in the PySpark API (Spark 3.1+)
- C) `array(1, 2, 3)` — `filter` keeps elements that do NOT satisfy the predicate, like `WHERE NOT`
- D) `array(false, false, false, true, true)` — `filter` returns a boolean array indicating which elements passed the predicate

---

### Question 39 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: transform() higher-order function — element-wise array mapping

**Question**:
What does `transform(array(1, 2, 3), x -> x * x)` return?

- A) `6` — `transform` is an alias for `aggregate` and returns the accumulated product of all elements
- B) `array(1, 4, 9)` — `transform(array_col, func)` is a higher-order function that applies the lambda function to every element of the array and returns a new array of the same length containing the mapped values; it is the array equivalent of Python's `map()` and is available as both a SQL function and `F.transform(col, func)` in the PySpark API (Spark 3.1+)
- C) `array(2, 4, 6)` — `transform` with a single argument doubles each element by default
- D) It raises an `AnalysisException` because the lambda `x -> x * x` is not supported syntax in Spark SQL; only named functions registered via `CREATE FUNCTION` can be passed

---

### Question 40 — Spark SQL

**Difficulty**: Medium
**Answer Type**: one
**Topic**: hex() / unhex() — hexadecimal encoding functions

**Question**:
What does `hex(255)` return and what types do `hex` and `unhex` accept and produce?

- A) `'0xff'` — `hex` always returns a lowercase prefixed hex string with the `'0x'` prefix
- B) `'FF'` — `hex(col)` accepts `IntegerType`, `LongType`, or `BinaryType` and returns an uppercase `StringType` hexadecimal representation (no prefix); `hex(255)` → `'FF'`; `hex(binary_col)` converts each byte to its two-character hex representation; `unhex(str_col)` is the inverse — accepts a `StringType` hex string and returns `BinaryType` decoded bytes; `unhex('FF')` → a single byte with value 255
- C) `255` in hexadecimal base — `hex` returns an `IntegerType` representing the base-16 value
- D) `NULL` — `hex` only accepts `BinaryType` input; integer literals must be explicitly cast to binary before calling `hex`

---

### Question 41 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.exceptAll() vs df.except() — EXCEPT ALL vs EXCEPT DISTINCT

**Question**:
What is the difference between `df1.exceptAll(df2)` and `df1.except(df2)`?

- A) They are identical; `exceptAll` is simply an alias introduced in Spark 3.0
- B) `df1.except(df2)` is equivalent to SQL `EXCEPT DISTINCT` — it removes from `df1` every row that appears anywhere in `df2`, regardless of how many times it appears; `df1.exceptAll(df2)` is equivalent to SQL `EXCEPT ALL` — each occurrence in `df2` removes exactly one matching occurrence from `df1`, so if a row appears 3 times in `df1` and once in `df2`, two copies remain; use `exceptAll` when duplicate semantics matter
- C) `df1.except(df2)` requires both DataFrames to have the same number of partitions; `exceptAll` automatically repartitions before the set operation
- D) `exceptAll` includes NULL values in the set comparison; `except` treats NULL as not equal to any value, so NULL rows in `df2` never remove NULL rows from `df1`

---

### Question 42 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.intersectAll() — INTERSECT ALL preserving duplicates

**Question**:
What does `df1.intersectAll(df2)` return compared to `df1.intersect(df2)`?

- A) `intersectAll` returns the union of rows common to both DataFrames; `intersect` returns only the rows unique to `df1`
- B) `df1.intersect(df2)` is equivalent to SQL `INTERSECT DISTINCT` — returns the set of distinct rows that appear in both DataFrames; `df1.intersectAll(df2)` is equivalent to SQL `INTERSECT ALL` — returns rows common to both DataFrames preserving duplicates: if a row appears 3 times in `df1` and 2 times in `df2`, it appears `min(3,2) = 2` times in the result; `intersectAll` is available from Spark 2.4+
- C) `intersect` performs a hash-based set intersection; `intersectAll` performs a sort-merge intersection and is faster for large DataFrames
- D) Both are identical for DataFrames that have no duplicate rows; they only differ when one of the DataFrames has been explicitly deduplicated

---

### Question 43 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.withColumnsRenamed() — batch column rename (Spark 3.4+)

**Question**:
What does `df.withColumnsRenamed({"first_name": "fname", "last_name": "lname"})` do, and why might it be preferred over calling `withColumnRenamed` twice?

- A) It is not a valid API; `withColumnRenamed` must be called separately for each column
- B) `df.withColumnsRenamed(colsMap)` (Spark 3.4+) renames multiple columns atomically in a single logical plan node using a dictionary mapping old names to new names; it is preferred over chaining two `withColumnRenamed` calls because each chained call adds an additional `Project` node to the logical plan — for many renames this creates deeper plan trees that take longer to optimize; `withColumnsRenamed` collapses all renames into one plan node
- C) `withColumnsRenamed` also reorders the columns alphabetically by their new names; `withColumnRenamed` preserves column order
- D) It only works on the top-level schema; `withColumnRenamed` is required for renaming nested struct fields

---

### Question 44 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.transform() — custom transformation chaining

**Question**:
What does `df.transform(func)` do?

**Scenario**:
```python
def add_audit_cols(df):
    return df.withColumn("created_at", F.current_timestamp())

def normalize_names(df):
    return df.withColumn("name", F.upper(F.col("name")))

result = df.transform(add_audit_cols).transform(normalize_names)
```

**Question**:
What is the behavior and benefit of `df.transform(func)`?

- A) `df.transform` caches the DataFrame in memory before applying `func`, improving performance when `func` triggers multiple actions
- B) `df.transform(func)` calls `func(df)` and returns the result — it is syntactic sugar that enables DataFrame method chaining: `df.transform(f1).transform(f2).transform(f3)` is equivalent to `f3(f2(f1(df)))` but reads left-to-right; the benefit is readability and composability of modular transformation functions without nesting or intermediate variables; no special behavior beyond calling the function
- C) `df.transform` is the only way to apply a Python UDF to an entire DataFrame row-by-row; without `transform`, UDFs must be applied column by column
- D) `df.transform` executes the function immediately (eagerly) rather than lazily; the result is fully materialized before the next `transform` in the chain begins

---

### Question 45 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.greatest() / F.least() — row-wise maximum and minimum across columns

**Question**:
What is the difference between `F.greatest(col("a"), col("b"), col("c"))` and `F.max(col("a"))`?

- A) `F.greatest` is a deprecated alias for `F.max`; they are functionally identical
- B) `F.greatest(*cols)` is a row-wise function that returns the largest value among the specified columns for each individual row — it operates horizontally across columns; `F.max(col)` is an aggregate function that returns the maximum value of a single column across all rows in the group — it operates vertically across rows; they serve completely different purposes; `F.greatest` ignores `NULL` unless all arguments are `NULL` (in which case it returns `NULL`)
- C) `F.greatest` only works with numeric columns; `F.max` works with any orderable type including strings and dates
- D) `F.greatest` returns a `BooleanType` column indicating which column holds the maximum value; `F.max` returns the value itself

---

### Question 46 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: F.coalesce(*cols) vs df.coalesce(n) — function vs transformation

**Question**:
What is the difference between `F.coalesce(col("a"), col("b"), F.lit(0))` and `df.coalesce(2)`?

- A) They are both the same operation — `F.coalesce` is the column-level alias for the DataFrame-level `df.coalesce` method
- B) `F.coalesce(*cols)` is a **column expression function** that returns the first non-null value from the list of column arguments for each row — it is used in `select` or `withColumn` to handle nulls horizontally; `df.coalesce(n)` is a **DataFrame transformation** that reduces the number of partitions to `n` using a narrow transformation (no full shuffle) — they share a name but serve entirely different purposes; confusing them is a common error
- C) `F.coalesce` sorts values and returns the smallest non-null; `df.coalesce` merges small partitions by sorting them first
- D) Both return the same result; the prefix `F.` simply indicates that `F.coalesce` is a SQL-compatible wrapper around `df.coalesce`

---

### Question 47 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.nanvl() — NaN-aware null substitution for floats

**Question**:
What does `F.nanvl(col("score"), F.lit(0.0))` return for different input values?

- A) `0.0` when `score` is `NULL`; `score` unchanged when it is `NaN`; `score` unchanged otherwise
- B) The value of `col("score")` when it is a normal float (not NaN); `0.0` when `col("score")` is `NaN` (IEEE 754 "Not a Number"); `NULL` when `col("score")` is `NULL` — `F.nanvl(col1, col2)` returns `col1` if it is not NaN and returns `col2` if it is NaN, but propagates `NULL` unchanged; this is distinct from `F.coalesce` which handles `NULL` but not `NaN`; to handle both, combine: `F.coalesce(F.nanvl(col, F.lit(0.0)), F.lit(0.0))`
- C) `0.0` for both `NaN` and `NULL`; `F.nanvl` treats `NaN` and `NULL` identically
- D) Raises a `SparkException` at execution time because `NaN` values cannot exist in Spark DataFrames — all non-finite floats are automatically converted to `NULL` on ingestion

---

### Question 48 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.collect_list() vs F.collect_set() — order, duplicates, and NULLs

**Question**:
How do `F.collect_list(col)` and `F.collect_set(col)` differ with respect to duplicates, NULLs, and ordering?

- A) They are identical in behavior; `collect_set` is just an alias for `collect_list`
- B) `F.collect_list(col)` collects all values (including duplicates and NULLs) per group into an `ArrayType` — the element order within the array is non-deterministic unless used with an ordered window function; `F.collect_set(col)` collects only distinct values per group (duplicates removed), excludes NULL values entirely, and the result order is also non-deterministic; use `collect_list` when you need all values or need to preserve duplicates, `collect_set` when you need unique values only
- C) `collect_list` preserves the original row order of the DataFrame; `collect_set` sorts the collected values in ascending order
- D) `collect_set` includes NULL values; `collect_list` silently drops NULLs from the result

---

### Question 49 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.rollup() — hierarchical subtotals

**Question**:
Given `df.rollup("year", "quarter").agg(F.sum("revenue"))`, which grouping combinations appear in the output?

- A) Only `(year, quarter)` combinations — `rollup` is identical to `groupBy` for two columns
- B) Three levels: `(year, quarter)` full grouping, `(year, NULL)` subtotals per year (quarter rolled up), and `(NULL, NULL)` grand total — `rollup("a", "b")` generates n+1 grouping sets for n dimensions, rolling up from the most specific to the most general; `NULL` in the output represents the aggregated level — distinguishable from actual NULL values using `F.grouping(col)` which returns `1` when a column is aggregated at that level
- C) Four combinations: `(year, quarter)`, `(year, NULL)`, `(NULL, quarter)`, and `(NULL, NULL)` — this is the behavior of `cube`, not `rollup`; `rollup` only drops trailing dimensions
- D) Only the `(NULL, NULL)` grand total row; `rollup` is a SQL keyword for the grand total aggregation

---

### Question 50 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.pivot() — creating pivot tables with explicit values

**Question**:
Why is it more efficient to specify explicit pivot values in `df.groupBy("category").pivot("quarter", ["Q1","Q2","Q3","Q4"]).agg(F.sum("revenue"))` compared to omitting them?

- A) There is no performance difference; specifying values is purely stylistic
- B) When pivot values are not specified, Spark must execute an additional full scan of the DataFrame to discover the distinct values of the pivot column before it can construct the output schema — this is an extra Spark job; providing explicit values eliminates that preliminary scan, reducing total job count by one and also making the schema deterministic (no dependency on runtime data); explicit values also control the output column order and exclude any unexpected categories
- C) Without explicit values, `pivot` sorts all resulting columns alphabetically, which requires an extra sort stage
- D) Specifying explicit values causes Spark to filter out pivot column values not in the list, discarding data; omitting values always produces a more complete result

---

### Question 51 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.struct() — creating a StructType column from multiple columns

**Question**:
What does `F.struct(col("first"), col("last"))` produce in a `withColumn` or `select` expression?

- A) A `MapType` column with `"first"` and `"last"` as keys and their values as map values
- B) A new `StructType` column containing one field per input column, preserving the original column names and types as nested fields — `F.struct(col("first"), col("last"))` creates a struct with fields `{first: ..., last: ...}` for each row; the resulting column can be accessed with dot notation (`col("name.first")`) or `getField("first")`; you can rename nested fields using column aliases inside `struct`: `F.struct(col("first").alias("f"), col("last").alias("l"))`
- C) An `ArrayType` column containing the values of `first` and `last` in order as array elements
- D) A `StringType` column containing the JSON representation of the row's `first` and `last` values

---

### Question 52 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Accessing nested struct fields — dot notation vs getField()

**Question**:
Given a DataFrame with a column `address` of type `StructType` with field `city`, which of the following correctly extracts the `city` field?

- A) `df.select(col("address").city)` — attribute-style access on Column objects
- B) Both `df.select(col("address.city"))` and `df.select(col("address").getField("city"))` are valid and equivalent — `col("address.city")` uses dot-path string notation to navigate nested struct fields; `col("address").getField("city")` uses the explicit method; for deeply nested fields, extend the dot path: `col("a.b.c")` — the dot-path approach is more concise; `getField` is useful when field names contain special characters or dots themselves
- C) `df.select(col("address")["city"])` — bracket indexing accesses struct fields using the same syntax as array indexing
- D) `df.select(col("address").city())` — method call syntax invokes a column accessor function

---

### Question 53 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.array() — creating an ArrayType column from multiple columns

**Question**:
What does `F.array(col("jan_sales"), col("feb_sales"), col("mar_sales"))` produce?

- A) A `MapType` column mapping month names to their respective sales values
- B) A new `ArrayType` column where each row contains an array of the three column values in the specified order — `F.array(*cols)` creates an array from multiple column expressions; all columns must be of compatible types (or castable to a common type); the resulting array has a fixed length equal to the number of columns; elements can be accessed with `element_at(array_col, idx)` (1-based) or `array_col[idx]` (0-based in DataFrame API)
- C) A `StructType` column with fields `jan_sales`, `feb_sales`, `mar_sales`
- D) A `StringType` column containing the comma-separated values of the three columns

---

### Question 54 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.map_keys() / F.map_values() — extracting keys and values from MapType

**Question**:
Given a column `props` of type `MapType(StringType, IntegerType)`, what do `F.map_keys(col("props"))` and `F.map_values(col("props"))` return?

- A) `map_keys` returns the number of keys; `map_values` returns the sum of all values
- B) `F.map_keys(col("props"))` returns an `ArrayType(StringType)` column containing all keys of the map for each row; `F.map_values(col("props"))` returns an `ArrayType(IntegerType)` column containing all corresponding values — the order of keys and values is consistent between the two calls for a given row but is not guaranteed to be sorted; to iterate over both together, use `F.map_entries(col)` which returns an array of structs with `key` and `value` fields
- C) Both functions return a `MapType` column — `map_keys` removes the values and `map_values` removes the keys
- D) Both functions return `NULL` if any entry in the map has a `NULL` key or value

---

### Question 55 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: F.explode() vs F.explode_outer() — NULL row handling

**Question**:
What is the difference between `F.explode(col("items"))` and `F.explode_outer(col("items"))`?

- A) `explode_outer` only works on MapType columns; `explode` only works on ArrayType columns
- B) `F.explode(array_or_map_col)` generates one output row per element and **drops rows where the array/map is NULL or empty** — those rows disappear from the output; `F.explode_outer(array_or_map_col)` also generates one row per element but **preserves rows with NULL or empty arrays** by emitting a single row with `NULL` as the exploded value — use `explode_outer` when you must retain parent rows that have no children (analogous to a LEFT JOIN vs INNER JOIN)
- C) `explode_outer` creates an additional column containing the element index; `explode` produces only the element value
- D) They are identical; `explode_outer` is just an alias for `explode` added for SQL compatibility

---

### Question 56 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.posexplode() — explode with position index

**Question**:
What columns does `F.posexplode(col("tags"))` produce when the `tags` array contains `["spark", "python", "cloud"]`?

- A) A single column `tags` with each element on a separate row, with the original row duplicated
- B) Two columns — `pos` (0-based integer position index) and `col` (the element value) — for each element: `(0, "spark")`, `(1, "python")`, `(2, "cloud")`; `F.posexplode` is the position-aware variant of `F.explode`; the output column names default to `pos` and `col` and can be aliased in the `select` expression: `F.posexplode(col("tags")).alias("pos", "tag")`; `posexplode_outer` is the NULL-preserving variant
- C) Two columns — `index` (1-based) and `value` — matching Python list indexing convention
- D) A single struct column with fields `position` and `element` for each array entry

---

### Question 57 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.flatten() — flattening an array of arrays

**Question**:
What does `F.flatten(col("nested"))` return when `nested` is `array(array(1,2), array(3,4), array(5))`?

- A) `array(array(1,2,3,4,5))` — `flatten` wraps all elements into a single outer array
- B) `array(1, 2, 3, 4, 5)` — `F.flatten(col)` takes an `ArrayType(ArrayType(T))` column and returns an `ArrayType(T)` by concatenating all inner arrays into one flat array; it removes exactly one level of nesting; if the input has deeper nesting (array of array of arrays), only the outermost nesting level is removed
- C) `15` — `flatten` is an alias for `F.sum` on nested numeric arrays
- D) `struct(1, 2, 3, 4, 5)` — `flatten` converts a nested array into a StructType with positional fields

---

### Question 58 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.arrays_zip() — combining multiple arrays element-wise

**Question**:
What does `F.arrays_zip(col("names"), col("scores"))` return when `names=["Alice","Bob"]` and `scores=[95, 87]`?

- A) A `MapType` column mapping each name to its corresponding score
- B) An `ArrayType(StructType)` column — each element is a struct containing the corresponding elements from each input array: `[{names: "Alice", scores: 95}, {names: "Bob", scores: 87}]`; `F.arrays_zip(*array_cols)` is the column-expression equivalent of Python's `zip()` — the struct field names default to the input column names; if the arrays have different lengths, shorter arrays are padded with `NULL`; access zipped fields using `element.names` or `getField("names")` after exploding
- C) A `StringType` column containing a concatenated string of the form `"Alice:95,Bob:87"`
- D) Two separate columns produced by splitting the input arrays across rows — equivalent to calling `explode` on each array separately

---

### Question 59 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.create_map() — creating a MapType column from column pairs

**Question**:
What does `F.create_map(F.lit("name"), col("user_name"), F.lit("role"), col("user_role"))` produce?

- A) A `StructType` column with fields `name` and `role` derived from the specified column values
- B) A `MapType(StringType, StringType)` column where each row's map has the literal strings `"name"` and `"role"` as keys and the values of `user_name` and `user_role` as corresponding values — `F.create_map(*exprs)` takes an alternating sequence of key expressions and value expressions; all keys must be of the same type and all values must be of the same type; the key-value pairs are assembled per row; keys must be unique within each row
- C) A `ArrayType(StringType)` column containing the four values interleaved: `["name", "Alice", "role", "admin"]`
- D) A SQL `NULL` column because `create_map` requires all arguments to be column references (not literals)

---

### Question 60 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.map_filter() — filtering entries in a MapType column

**Question**:
What does `F.map_filter(col("scores"), lambda k, v: v >= 90)` return for `scores = {"math": 95, "english": 82, "science": 91}`?

- A) A count of entries satisfying the predicate: `2`
- B) A new `MapType` column retaining only the key-value pairs for which the lambda predicate returns `true` — result: `{"math": 95, "science": 91}`; `F.map_filter(map_col, func)` is a higher-order function available from Spark 3.0+; the lambda receives both the key and value as arguments; entries where the predicate is `false` or `NULL` are removed; the return type is the same `MapType` as the input
- C) A boolean column indicating whether any entry satisfies the predicate
- D) A `ArrayType` of the values satisfying the predicate: `array(95, 91)` — keys are discarded

---

### Question 61 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Window.rowsBetween() vs Window.rangeBetween() — frame boundary semantics

**Question**:
What is the difference between `Window.rowsBetween(Window.unboundedPreceding, Window.currentRow)` and `Window.rangeBetween(Window.unboundedPreceding, Window.currentRow)`?

- A) They are identical; `rowsBetween` and `rangeBetween` produce the same result for any ORDER BY expression
- B) `Window.rowsBetween(start, end)` defines the frame using physical row offsets — the current frame includes exactly the rows from position `start` to `end` relative to the current row regardless of the ORDER BY column values; `Window.rangeBetween(start, end)` defines the frame using value offsets based on the ORDER BY column — for `CURRENT ROW`, all rows with the same ORDER BY value as the current row are included in the frame; for numeric ORDER BY, a range offset like `rangeBetween(-7, 0)` includes rows whose ORDER BY value is within 7 units of the current row's value — they differ meaningfully when there are ties or when using numeric range offsets
- C) `rowsBetween` can only be used with `partitionBy`; `rangeBetween` is the only window frame that works without a partition clause
- D) `rangeBetween` applies to timestamp ORDER BY columns only; `rowsBetween` is used for all other types

---

### Question 62 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.lag() / F.lead() — window offset functions with default values

**Question**:
What does `F.lag(col("price"), 2, 0.0).over(w)` return for the first two rows in a partition?

- A) `NULL` — `lag` always returns `NULL` for rows where the offset goes before the start of the partition
- B) `0.0` — `F.lag(col, offset, default)` returns the value from `offset` rows before the current row within the window partition; when the offset goes before the start of the partition (i.e., there is no prior row at that distance), it returns the `default` value (here `0.0`) instead of `NULL`; `F.lead(col, offset, default)` is the forward-looking equivalent — it returns the value from `offset` rows after the current row, or `default` if beyond the end of the partition
- C) `0.0` only for the very first row; the second row returns `NULL` because `lag(2)` requires two prior rows
- D) The value from 2 rows before in the entire DataFrame (without respecting partition boundaries)

---

### Question 63 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: F.rank() vs F.dense_rank() vs F.row_number() — ranking function differences

**Question**:
Given ordered values `[10, 10, 20, 30]` in a window partition, what do `F.rank()`, `F.dense_rank()`, and `F.row_number()` return for each row?

- A) All three return `[1, 1, 2, 3]` — they are identical when used with the same ORDER BY
- B) `F.rank()` → `[1, 1, 3, 4]` (gaps after ties); `F.dense_rank()` → `[1, 1, 2, 3]` (no gaps); `F.row_number()` → `[1, 2, 3, 4]` (unique sequential regardless of ties) — `rank` leaves gaps in numbering after a tie group (two rows tied at rank 1 means the next rank is 3); `dense_rank` does not skip ranks; `row_number` assigns a unique arbitrary integer to each row including ties
- C) `F.rank()` → `[1, 2, 3, 4]`; `F.dense_rank()` → `[1, 1, 2, 3]`; `F.row_number()` → `[1, 1, 2, 3]`
- D) `F.row_number()` returns `NULL` for tied rows; `F.rank()` and `F.dense_rank()` handle ties by returning the same value

---

### Question 64 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.ntile(n) — dividing rows into equal-size buckets

**Question**:
What does `F.ntile(4).over(w)` do, and what values does it produce for a partition of 7 rows?

- A) Returns the percentile rank of each row as a float between 0.0 and 1.0 divided into 4 quartiles
- B) `F.ntile(n)` assigns an integer bucket number from `1` to `n` to each row in the window partition, distributing rows as evenly as possible — for 7 rows and `n=4`, the distribution is `[1,1,2,2,3,3,4]` (extra rows go to earlier buckets); useful for quartile/decile analysis; unlike `percent_rank`, `ntile` returns integer bucket labels not fractional ranks; requires an `orderBy` clause in the window specification
- C) Returns `NULL` for any partition whose row count is not evenly divisible by `n`
- D) Divides the data into `n` partitions at the DataFrame level (like `repartition`), not per window partition

---

### Question 65 — DataFrame/DataSet API

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Running aggregate with rowsBetween — cumulative sum with explicit frame

**Question**:
Given the code below, what does the result column `running_total` compute?

**Scenario**:
```python
from pyspark.sql import functions as F
from pyspark.sql.window import Window

w = Window.partitionBy("dept") \
          .orderBy("hire_date") \
          .rowsBetween(Window.unboundedPreceding, Window.currentRow)

df.withColumn("running_total", F.sum("salary").over(w))
```

- A) The total salary of all rows in the `dept` partition (the same value for every row, because `sum` is always an unbounded aggregate over the full partition)
- B) The cumulative sum of `salary` ordered by `hire_date` within each `dept` partition — for each row, `running_total` equals the sum of `salary` for all rows in the same `dept` with a `hire_date` ≤ the current row's `hire_date`; the key is `rowsBetween(UNBOUNDED_PRECEDING, CURRENT_ROW)` which explicitly defines a growing frame from the first row of the partition up to and including the current row; without this explicit frame, `F.sum(...).over(w)` with an `orderBy` uses `rangeBetween(UNBOUNDED_PRECEDING, CURRENT_ROW)` by default — which would include all rows tied on `hire_date`, potentially producing the same result here but behaving differently if numeric ranges are used
- C) The average salary of all employees in the `dept` who were hired on or before the current row's `hire_date`
- D) A `SparkAnalysisException` because `rowsBetween` and `orderBy` cannot be combined in the same window specification

---

### Question 66 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.sum().over() without rowsBetween — default window frame behavior

**Question**:
What is the default window frame when `F.sum("value").over(Window.partitionBy("group").orderBy("ts"))` is used WITHOUT an explicit `rowsBetween` or `rangeBetween` call?

- A) The default frame is `rowsBetween(Window.unboundedPreceding, Window.unboundedFollowing)` — an aggregate over the entire partition
- B) When an `orderBy` clause is present but no explicit frame is given, Spark uses `rangeBetween(Window.unboundedPreceding, Window.currentRow)` as the default frame — this is a cumulative (running) aggregate including all rows with an ORDER BY value ≤ the current row's value; when there is NO `orderBy`, the default is `rowsBetween(UNBOUNDED_PRECEDING, UNBOUNDED_FOLLOWING)` — the full partition; understanding these defaults is critical because the same `sum().over()` expression produces different results depending on whether `orderBy` is present
- C) The default frame is always `rowsBetween(Window.currentRow, Window.currentRow)` — only the current row is in the frame, making `sum` equivalent to the column value itself
- D) Spark raises an error if no frame is specified and an `orderBy` is present; explicit frame boundaries are always required with ordered windows

---

### Question 67 — DataFrame/DataSet API

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.sampleBy() — stratified sampling by column value

**Question**:
What does `df.sampleBy("label", fractions={"cat": 0.5, "dog": 0.2}, seed=42)` do?

- A) It randomly shuffles the DataFrame with a random seed of 42 and then takes the first 50% of rows from the `cat` rows and 20% of rows from the `dog` rows after sorting
- B) It performs **stratified random sampling** — rows where `label == "cat"` are sampled at a 50% rate, rows where `label == "dog"` are sampled at a 20% rate, and rows with any other label value are excluded entirely (no entry in `fractions` means 0% sampling rate); the `seed` parameter ensures reproducibility; `sampleBy` is the recommended approach when you need proportional representation across categories, unlike `df.sample(fraction)` which applies a uniform rate across all rows
- C) It guarantees exactly 50% of `cat` rows and exactly 20% of `dog` rows in the output; `sampleBy` is exact, not approximate
- D) It filters the DataFrame to keep only rows where `label` is in `{"cat", "dog"}` and then applies uniform random sampling at the average rate of `(0.5 + 0.2) / 2 = 0.35`

---

### Question 68 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.approx_count_distinct() vs F.countDistinct() — accuracy vs performance

**Question**:
When should `F.approx_count_distinct(col, rsd=0.05)` be preferred over `F.countDistinct(col)`?

- A) `approx_count_distinct` should always be used; `countDistinct` is deprecated in Spark 3.0+
- B) `F.approx_count_distinct(col, rsd)` uses the HyperLogLog++ algorithm to estimate the number of distinct values with a configurable relative standard deviation (`rsd` default `0.05` = ±5% error); it is significantly more memory-efficient and faster than `F.countDistinct(col)` which requires exact deduplication (shuffling all values); prefer `approx_count_distinct` for large datasets where an approximate answer within a small error margin is acceptable — e.g., analytics dashboards, cardinality estimation for optimizer hints; use `countDistinct` only when exact counts are required
- C) `approx_count_distinct` is preferred only for `StringType` columns; for numeric columns both produce the same result with identical performance
- D) `approx_count_distinct` is faster because it uses sampling (reads only 5% of rows by default); `countDistinct` reads all rows

---

### Question 69 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.percentile_approx() — approximate percentile calculation

**Question**:
What does `F.percentile_approx(col("latency"), 0.95, accuracy=100)` compute?

- A) The exact 95th percentile value, with `accuracy=100` meaning 100% precision
- B) An approximation of the 95th percentile of the `latency` column values, computed using the Greenwald-Khanna algorithm; `accuracy` controls the approximation quality — higher values give better precision but use more memory (default is `10000`); passing a list of quantiles `[0.25, 0.5, 0.75]` returns an array of approximate percentile values for each; `F.percentile_approx` is an aggregate function usable in `agg()`, `groupBy().agg()`, and window functions; it is preferred over an exact percentile for large datasets where an approximation is acceptable
- C) The 95th row in the `latency` column after sorting, with `accuracy=100` specifying the number of buckets to use for histogram-based binning
- D) Returns `NULL` when `accuracy < 1000` because the approximation error would exceed the `rsd` threshold

---

### Question 70 — DataFrame/DataSet API

**Difficulty**: Medium
**Answer Type**: one
**Topic**: F.count_if() — conditional row count (Spark 3.3+)

**Question**:
What does `F.count_if(col("status") == "failed")` compute in an aggregation?

- A) A boolean column indicating whether any row in the group has `status == "failed"`
- B) The number of rows in the group where `status == "failed"` — `F.count_if(condition)` is a shorthand for `F.count(F.when(condition, F.lit(1)))` introduced in Spark 3.3; unlike `F.count(col)` which counts non-null values, `count_if` counts rows where the boolean condition is `true` (rows where the condition is `false` or `NULL` are not counted); it is equivalent to SQL `COUNT_IF(status = 'failed')` or `SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END)`
- C) Returns `1` if any row satisfies the condition, `0` otherwise — it behaves like `F.exists(condition)` across the group
- D) Raises an `AnalysisException` because `count_if` only accepts column references, not boolean column expressions

---

### Question 71 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: spark.sql.adaptive.enabled — Adaptive Query Execution overview

**Question**:
What three main optimizations does Adaptive Query Execution (AQE) enable when `spark.sql.adaptive.enabled=true` (default in Spark 3.2+)?

- A) AQE enables speculative execution, dynamic allocation of executors, and automatic data skipping on partitioned tables
- B) AQE re-optimizes the query plan at runtime using actual shuffle statistics; its three core features are: (1) **Dynamically coalescing shuffle partitions** — merges small post-shuffle partitions into fewer larger ones (controlled by `spark.sql.adaptive.advisoryPartitionSizeInBytes`); (2) **Dynamically switching join strategies** — converts sort-merge joins to broadcast joins when the runtime size of one side falls below the broadcast threshold; (3) **Dynamically optimizing skew joins** — splits oversized skewed partitions and replicates the corresponding side to balance the join — all three reduce the need for manual tuning of `spark.sql.shuffle.partitions` and join hints
- C) AQE enables row-level security, column masking, and automatic partition pruning based on predicate analysis
- D) AQE replaces the Catalyst optimizer with a cost-based optimizer that uses table statistics collected by `ANALYZE TABLE`

---

### Question 72 — Troubleshooting & Tuning

**Difficulty**: Easy
**Answer Type**: one
**Topic**: df.repartition() vs df.coalesce() — when to use each

**Question**:
When should `df.repartition(n)` be used instead of `df.coalesce(n)`?

- A) `repartition` is always preferred; `coalesce` is a legacy API that should be avoided in Spark 3.0+
- B) `df.repartition(n)` performs a full shuffle and can both increase and decrease partition count, producing roughly equal-sized partitions — use it when you need to increase parallelism, when current partitions are heavily skewed, or when partitioning by a specific column (`repartition(n, col)`); `df.coalesce(n)` uses a narrow transformation (no shuffle) that can only reduce the partition count by merging adjacent partitions — use it when only decreasing and size balance is not critical (e.g., before writing to avoid too many small files); `coalesce` is cheaper but can create uneven partitions
- C) `coalesce` triggers a full shuffle like `repartition`; the only difference is that `coalesce` uses range partitioning while `repartition` uses hash partitioning
- D) `repartition` should only be called immediately before a `write` operation; calling it mid-pipeline is a performance anti-pattern

---

### Question 73 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.adaptive.advisoryPartitionSizeInBytes — AQE target partition size

**Question**:
What does `spark.sql.adaptive.advisoryPartitionSizeInBytes` (default `64MB`) control?

- A) The maximum size of a single executor's memory before Spark spills to disk
- B) It is the target size hint used by AQE's **dynamic partition coalescing** feature — after a shuffle, AQE examines the actual sizes of post-shuffle partitions and merges consecutive small partitions until the combined size approaches `advisoryPartitionSizeInBytes`; this reduces the number of tasks for the next stage without changing the logical result; setting it too high creates fewer but larger partitions (risk of OOM, less parallelism); too low leaves many small partitions (high task scheduling overhead); it is advisory, not a hard limit
- C) The threshold below which AQE automatically broadcasts a table instead of using sort-merge join; tables smaller than this value are broadcast
- D) The maximum shuffle file size written by a single map task; map output exceeding this size is split into multiple files

---

### Question 74 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.adaptive.skewJoin.enabled — skew join detection and splitting

**Question**:
How does AQE's skew join optimization work when `spark.sql.adaptive.skewJoin.enabled=true`?

- A) Spark samples 1% of the data before the join to estimate skew and pre-partitions the skewed column using range partitioning
- B) After the shuffle phase, AQE inspects actual partition sizes; a partition is considered skewed if its size exceeds both `spark.sql.adaptive.skewJoin.skewedPartitionFactor` (default `5`) × the median partition size AND `spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes` (default `256MB`); skewed partitions on one side are split into multiple sub-partitions and the corresponding partition on the other side is replicated (one copy per sub-partition), distributing the join work across more tasks — this is transparent to the user and requires no join hints
- C) AQE converts the skewed sort-merge join into a cartesian (cross) join to avoid data movement overhead
- D) Skew join optimization repartitions both sides of the join by a salted key automatically, similar to what a developer would do manually with key salting

---

### Question 75 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.explain() — reading query plans

**Question**:
What does `df.explain(mode="extended")` display compared to `df.explain()` (or `df.explain(True)`)?

- A) Both display only the Physical Plan; `mode="extended"` just adds line numbers
- B) `df.explain()` (or `df.explain(False)`) displays only the **Physical Plan**; `df.explain(True)` (or `mode="extended"`) displays four sections: **Parsed Logical Plan** (AST from the query), **Analyzed Logical Plan** (resolved attributes and types), **Optimized Logical Plan** (after Catalyst rule-based optimization), and **Physical Plan** (the actual execution strategy chosen by the planner including join strategies, exchange types, and sort operations) — reading the optimized logical plan vs physical plan helps diagnose when the optimizer selects an unexpected join type or fails to push down a predicate; `mode="cost"` additionally shows statistics used by the cost-based optimizer
- C) `mode="extended"` shows execution metrics (rows read, bytes written) in addition to the plan; this data is only available after the query has been executed
- D) `df.explain(True)` triggers an early execution to collect plan statistics; `df.explain(mode="extended")` shows the plan without executing

---

### Question 76 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.shuffle.service.enabled — external shuffle service role

**Question**:
What problem does enabling the external shuffle service (`spark.shuffle.service.enabled=true`) solve when used with dynamic allocation?

- A) It offloads shuffle sort operations from executors to dedicated shuffle nodes, reducing executor CPU usage
- B) Without the external shuffle service, when dynamic allocation removes an idle executor, the shuffle files it wrote are also lost — any downstream task that needs those shuffle blocks would have to be re-run (or fail); the external shuffle service is a long-running daemon on each worker node (independent of executor processes) that serves shuffle files on behalf of executors; because the shuffle data is served by the node daemon rather than the executor, executors can be safely removed without losing their shuffle output — this makes dynamic allocation practical for shuffle-heavy workloads
- C) The external shuffle service compresses shuffle files and caches them in the cluster-wide block store, reducing shuffle I/O by up to 40%
- D) It encrypts shuffle data at rest on the worker nodes; without it, shuffle files are stored in plaintext

---

### Question 77 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.io.compression.codec — shuffle and storage compression codecs

**Question**:
Which compression codec is the Spark default for shuffle and RDD storage compression, and when might `zstd` be preferable?

- A) Gzip is the default; `zstd` should be used for streaming applications only
- B) `lz4` is the default codec for `spark.io.compression.codec` — it prioritises fast compression/decompression speed with moderate compression ratio, minimising CPU overhead during shuffle; `snappy` is also fast with similar characteristics; `zstd` (available since Spark 2.3) achieves significantly higher compression ratios (smaller shuffle files) at the cost of more CPU — preferable when network bandwidth is the bottleneck rather than CPU (e.g., cross-rack shuffles in bandwidth-constrained clusters or when writing to persistent storage where disk I/O dominates); `spark.sql.parquet.compression.codec` (default `snappy`) is a separate setting controlling Parquet file compression
- C) `deflate` is the default because it produces the smallest shuffle files; `zstd` is only relevant for Parquet files
- D) No compression is applied by default (`none`); `lz4` must be explicitly configured for shuffle compression

---

### Question 78 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark.sql.adaptive.localShuffleReader.enabled — AQE local shuffle reader

**Question**:
What is the AQE local shuffle reader optimization controlled by `spark.sql.adaptive.localShuffleReader.enabled`?

- A) It reads shuffle data directly from local disk without going through the executor block manager, bypassing serialization
- B) After AQE converts a sort-merge join to a broadcast join, the shuffle data for the non-broadcast side has already been written; rather than fetching those shuffle blocks over the network, the local shuffle reader optimisation allows each executor to read only the shuffle blocks written by its own local mapper tasks (those on the same executor/node) directly from local disk — this eliminates unnecessary network transfers for that side of the join; it is only applicable after a join strategy change by AQE and requires `spark.sql.adaptive.enabled=true`
- C) It caches shuffle blocks in the executor's off-heap memory, eliminating disk reads for repeated access to the same shuffle partition
- D) It reads shuffle files using memory-mapped I/O (`mmap`), reducing read latency for large shuffle files stored on SSDs

---

### Question 79 — Troubleshooting & Tuning

**Difficulty**: Medium
**Answer Type**: one
**Topic**: df.cache() / df.persist() — default storage level and when to use

**Question**:
What is the default `StorageLevel` when calling `df.cache()`, and when is persisting a DataFrame beneficial?

- A) The default is `DISK_ONLY`; caching to disk is preferred because executors have limited memory
- B) `df.cache()` is shorthand for `df.persist(StorageLevel.MEMORY_AND_DISK)` — data is stored as deserialized Java objects in executor JVM heap memory; if memory is insufficient, partitions overflow to disk; persisting is beneficial when the same DataFrame is accessed multiple times in subsequent actions (e.g., iterative ML algorithms, a shared base DataFrame used in multiple branches of a pipeline) — without caching, each action re-executes the full lineage from scratch; always call `df.unpersist()` when the cached data is no longer needed to free memory
- C) The default is `MEMORY_ONLY`; partitions that do not fit in memory are silently dropped and recomputed on demand
- D) `df.cache()` stores data in the driver's memory, making it accessible to all workers without network transfer on subsequent accesses

---

### Question 80 — Troubleshooting & Tuning

**Difficulty**: Hard
**Answer Type**: one
**Topic**: Cost-based optimizer — ANALYZE TABLE and CBO statistics

**Question**:
What must a developer do to enable Spark's cost-based optimizer (CBO) to choose between a broadcast join and a sort-merge join based on accurate table statistics?

- A) CBO is always active; Spark automatically collects statistics during `df.write` operations, so no developer action is needed
- B) Two steps are required: (1) set `spark.sql.cbo.enabled=true` (default `false` in most distributions — Databricks Runtime enables it); (2) run `ANALYZE TABLE my_table COMPUTE STATISTICS` (and optionally `ANALYZE TABLE my_table COMPUTE STATISTICS FOR COLUMNS col1, col2` for column-level histograms); without `ANALYZE`, the CBO has no accurate row count or size estimates and falls back to heuristic rules — after `ANALYZE`, the CBO uses the statistics to select join strategies, estimate intermediate result sizes, and choose between `BroadcastHashJoin`, `SortMergeJoin`, or `ShuffleHashJoin` based on actual costs; stale statistics after data changes require re-running `ANALYZE`
- C) CBO is enabled by setting `spark.sql.statistics.fallBackToHdfs=true`, which causes Spark to read file sizes from HDFS metadata as a substitute for statistics
- D) CBO only applies to Hive-format tables registered in the Hive Metastore; Delta and Parquet tables always use the rule-based optimizer regardless of `spark.sql.cbo.enabled`

---

### Question 81 — Structured Streaming

**Difficulty**: Easy
**Answer Type**: one
**Topic**: trigger(availableNow=True) vs trigger(once=True) — one-time batch triggers

**Question**:
What is the difference between `writeStream.trigger(once=True)` and `writeStream.trigger(availableNow=True)`?

- A) They are identical; `availableNow` is just the new name for `once` introduced in Spark 3.3
- B) `trigger(once=True)` processes all available data in a **single micro-batch** then stops the query — it reads everything available at query start in one batch; `trigger(availableNow=True)` (Spark 3.3+) also processes all currently available data then stops, but may split it into **multiple micro-batches** to respect `maxFilesPerTrigger` and `maxBytesPerTrigger` limits — this prevents a single enormous batch and provides better fault tolerance (smaller checkpoints); `availableNow` is the recommended replacement for `once` when data volumes are large
- C) `trigger(once=True)` runs the query exactly once; `trigger(availableNow=True)` runs the query continuously but resets after each micro-batch
- D) `trigger(availableNow=True)` is only supported for Delta Lake sources; `trigger(once=True)` works with all streaming sources

---

### Question 82 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: withWatermark() — late data tolerance and state cleanup

**Question**:
Given `df.withWatermark("event_time", "10 minutes").groupBy(F.window("event_time", "5 minutes")).count()`, what does the 10-minute watermark control?

- A) The query waits 10 minutes after startup before emitting any results
- B) The watermark tells Spark how late data can arrive and still be included in its corresponding window — Spark tracks the maximum `event_time` seen so far; the **watermark threshold** is `max(event_time) − 10 minutes`; any event with `event_time < threshold` is considered late and dropped; state for windows older than the threshold is cleaned up from memory; this allows the streaming aggregation to bound its in-memory state size; with **Append** output mode, a window's result is emitted only after the watermark has advanced past the window's end time
- C) The 10-minute window defines the maximum size of each tumbling window; events outside 10 minutes are placed in the next window
- D) The watermark sets a hard 10-minute timeout — if no data arrives within 10 minutes the query stops automatically

---

### Question 83 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Streaming output modes — Append, Complete, Update

**Question**:
Which output mode must be used with a streaming aggregation that includes `withWatermark`, and why can't `Complete` mode be used in that scenario for unbounded data?

- A) `Complete` mode is required whenever `withWatermark` is used; other modes are incompatible with aggregations
- B) `Append` mode is appropriate for watermarked aggregations — with `Append`, a window's aggregate result is emitted **only once** after the watermark has passed the window's end (guaranteeing no further late data will change the result); state for past windows is then discarded; `Complete` mode re-writes the entire result table on every trigger, requiring ALL aggregation state to be retained in memory indefinitely — this is unbounded and impractical for long-running streaming jobs on unbounded data; `Update` mode emits updated rows on each trigger but does not guarantee clean state eviction without watermark
- C) `Update` mode is the only mode compatible with `groupBy` aggregations in streaming; `Append` and `Complete` require a non-aggregated stream
- D) All three output modes are equally valid for watermarked aggregations; the choice only affects sink compatibility, not state management

---

### Question 84 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: trigger(processingTime="interval") — fixed-interval micro-batch trigger

**Question**:
What happens if a micro-batch triggered by `trigger(processingTime="30 seconds")` takes 45 seconds to process?

- A) Spark kills the micro-batch after 30 seconds and retries it with a smaller data slice
- B) The micro-batch runs to completion (45 seconds); the next trigger fires **immediately** after the current batch finishes rather than waiting an additional 30 seconds — the 30-second interval is measured from the start of one batch to the start of the next; if processing exceeds the interval, Spark does not skip batches or run them in parallel; back-to-back batches run serially; sustained over-run is a sign that `processingTime` should be increased or the query needs optimization
- C) Spark splits the next batch's input data in half to ensure it completes within the 30-second window
- D) Spark automatically switches to `trigger(continuous="1 second")` mode when fixed-interval batches consistently run over the configured interval

---

### Question 85 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: checkpointLocation — what is stored in the checkpoint directory

**Question**:
What does Spark Structured Streaming store in the directory specified by `option("checkpointLocation", "/path/to/cp")`?

- A) Only the raw input data consumed by each micro-batch, enabling full replay from source
- B) Three categories of state are persisted: (1) **Offsets** — the source offsets read in each completed micro-batch (in the `offsets/` subdirectory), allowing the query to resume from where it left off; (2) **Commit log** — confirmation that a batch completed successfully (`commits/` subdirectory), distinguishing "offsets planned" from "batch committed"; (3) **State store** — for stateful operations like aggregations, joins, and deduplication, the in-memory state snapshots (in `state/`) are persisted so they survive query restarts; without a checkpoint, a restarted query starts from the beginning or from `latest` depending on source configuration
- C) Only the schema of the streaming DataFrame and Spark configuration settings; actual data is always re-read from the source on restart
- D) The checkpoint stores completed micro-batch results as Parquet files; the output sink reads from the checkpoint instead of re-executing the query on restart

---

### Question 86 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: foreachBatch() — custom micro-batch processing sink

**Question**:
What is `writeStream.foreachBatch(func)` used for, and what arguments does `func` receive?

- A) `foreachBatch` applies `func` to each row individually (like `foreach`); it is an efficient row-level sink
- B) `foreachBatch(func)` calls `func(micro_batch_df, batch_id)` once per micro-batch — `micro_batch_df` is a static DataFrame containing the data for that batch, and `batch_id` is a monotonically increasing integer; this pattern is used to write to sinks that do not have native streaming support (e.g., JDBC, arbitrary REST APIs, multiple output tables); because `micro_batch_df` is a regular DataFrame, all batch operations and optimizations apply; `batch_id` can be used for idempotent writes — if the same `batch_id` is replayed on restart, the write should produce the same result without duplication
- C) `foreachBatch` is only valid with `trigger(once=True)`; it cannot be used with continuous or fixed-interval triggers
- D) `func` receives a Python iterator of `Row` objects; it cannot use Spark DataFrame or SQL operations inside the function body

---

### Question 87 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Streaming reads from Delta Lake — change data feed and continuous reads

**Question**:
What is the advantage of using Delta Lake as a streaming source (`spark.readStream.format("delta").load(path)`) compared to a plain Parquet source?

- A) Delta Lake streaming sources are faster because they use columnar pushdown that plain Parquet does not support in streaming mode
- B) Delta Lake's transaction log makes it a reliable streaming source — Spark can efficiently discover new data by reading the Delta log rather than listing directory contents on every trigger (expensive for large tables); Delta supports **exactly-once semantics** natively; using `option("readChangeFeed", "true")` (Change Data Feed) allows streaming consumption of insert, update, and delete events rather than just appended data; Delta also supports `startingVersion` and `startingTimestamp` options to begin reading from a specific point in history — none of these capabilities are available with plain Parquet streaming sources
- C) Delta Lake streaming sources eliminate the need for a `checkpointLocation` because the Delta log itself serves as the checkpoint
- D) Delta streaming is only available in Databricks Runtime; open-source `delta-spark` does not support streaming reads

---

### Question 88 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Streaming deduplication with withWatermark — stateful distinct

**Question**:
How does `df.withWatermark("ts", "1 hour").dropDuplicates(["event_id"])` manage its deduplication state?

- A) `dropDuplicates` in streaming mode keeps ALL seen `event_id` values in memory forever, which is unbounded
- B) When `dropDuplicates` is combined with `withWatermark`, Spark maintains a state store of `event_id` values seen within the watermark window; once the watermark advances past `ts − 1 hour`, `event_id` values for events older than the threshold are removed from the state store — this **bounds the state size**; without `withWatermark`, state grows unboundedly; the trade-off is that late duplicates arriving after the watermark threshold are no longer detected and may be emitted as new events
- C) `dropDuplicates` in streaming mode uses a probabilistic data structure (Bloom filter) to detect duplicates, so some false positives (incorrectly dropped unique events) are expected
- D) `withWatermark` has no effect on `dropDuplicates`; it only affects window aggregations; deduplication state is always unbounded in streaming mode

---

### Question 89 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: StreamingQuery.awaitTermination() vs stop() — query lifecycle

**Question**:
What is the difference between `query.awaitTermination()` and `query.stop()`?

- A) `awaitTermination()` stops the streaming query immediately; `stop()` schedules a graceful shutdown after the current micro-batch completes
- B) `query.awaitTermination()` **blocks the calling thread** until the streaming query terminates (either naturally, due to an error, or because `stop()` was called from another thread) — it is commonly used at the end of a streaming application to keep the driver alive while the query runs; `query.stop()` **actively stops** the streaming query, gracefully finishing the current micro-batch before shutting down; `awaitTermination(timeout_ms)` accepts a timeout and returns `True` if the query terminated within the timeout or `False` otherwise — useful in tests and time-bounded runs
- C) `awaitTermination()` waits for the next micro-batch to complete then exits; `stop()` waits for all pending micro-batches in the queue to complete
- D) Both methods immediately terminate the query without waiting for the current micro-batch to finish; the only difference is that `stop()` clears the checkpoint directory

---

### Question 90 — Structured Streaming

**Difficulty**: Medium
**Answer Type**: one
**Topic**: StreamingQuery.recentProgress — monitoring streaming metrics

**Question**:
What information does `query.recentProgress` provide and how is it used?

- A) `recentProgress` returns the full list of all micro-batch IDs since the query started, without per-batch metrics
- B) `query.recentProgress` returns a list of dictionaries (one per recent micro-batch) containing per-batch metrics including: `batchId`, `timestamp`, `numInputRows`, `inputRowsPerSecond`, `processedRowsPerSecond`, source `endOffset`, sink `numOutputRows`, and `durationMs` breakdown (query planning, getting offsets, writing output, etc.) — this information is invaluable for diagnosing throughput bottlenecks, identifying slow batches, and monitoring lag; `query.lastProgress` returns just the most recent batch's dictionary; `query.status` shows the current state (whether a batch is active, what stage it is in)
- C) `recentProgress` provides a live view of rows currently being processed in the active micro-batch, updating every 100 milliseconds
- D) `recentProgress` shows the Spark UI URL and executor assignment for the streaming query but does not include row throughput metrics

---

### Question 91 — Spark Connect

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Spark Connect — client-server architecture overview

**Question**:
What is Spark Connect and what problem does it solve compared to the classic PySpark driver architecture?

- A) Spark Connect is a new cluster manager that replaces YARN for Kubernetes deployments, providing simpler networking configuration
- B) Spark Connect (introduced in Spark 3.4) is a **decoupled client-server architecture** for Spark — a thin client library communicates with a remote Spark server over gRPC (default port `15002`) using a language-agnostic protocol; the Spark driver runs as a separate server process rather than embedded in the client application; this solves several problems of the classic architecture: client crashes can no longer bring down running jobs, the client process has minimal memory footprint (no JVM driver), multiple language clients (Python, Scala, Go, R) can connect to the same server, and the API becomes accessible from environments where embedding a JVM is impractical (notebooks, microservices)
- C) Spark Connect replaces the Spark SQL parser with a REST API that accepts SQL strings over HTTP, removing the need for the Catalyst optimizer
- D) Spark Connect is a Databricks-proprietary feature that provides a unified connection string for both Delta Lake and streaming sources

---

### Question 92 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: SparkSession.builder.remote() — connecting to a Spark Connect server

**Question**:
How does a PySpark client connect to a Spark Connect server, and what is the connection string format?

- A) Use `SparkSession.builder.master("spark-connect://hostname:15002").getOrCreate()`
- B) Use `SparkSession.builder.remote("sc://hostname:15002").getOrCreate()` — the `remote()` method (available in `pyspark.sql.SparkSession` when the `pyspark[connect]` package extras are installed) accepts a Spark Connect URL with the `sc://` scheme, the server hostname, and the gRPC port (default `15002`); the resulting `SparkSession` object exposes the same DataFrame and SQL API but all operations are serialized as protocol buffer messages and sent to the server; no JVM is started locally in the client process
- C) Use `SparkSession.builder.config("spark.connect.url", "sc://hostname:15002").getOrCreate()` — `remote()` is only a Scala API; Python clients must use the config key
- D) Use `SparkConnectClient.from_url("grpc://hostname:15002")` — the `SparkSession` API is not used for Spark Connect; a separate `SparkConnectClient` object must be instantiated

---

### Question 93 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect limitations — APIs unavailable in the client library

**Question**:
Which of the following Spark APIs is NOT available when using Spark Connect in client mode?

- A) `df.select()`, `df.filter()`, and `df.groupBy()` — DataFrame transformation operations
- B) `SparkContext` and the RDD API — Spark Connect exposes only the high-level Structured (DataFrame/Dataset/SQL) API through its protocol; there is no `SparkContext` object in the client (`spark.sparkContext` raises an error); RDD operations (`sc.parallelize`, `rdd.map`, etc.), `sc.broadcast()`, `sc.addFile()`, `sc.setJobGroup()`, and accumulators created via `SparkContext` are not available; this is by design — the low-level RDD API cannot be efficiently serialized over the gRPC protocol; code that relies on `SparkContext` must be refactored before migrating to Spark Connect
- C) `spark.sql("SELECT ...")` — SQL queries are not supported through the gRPC protocol
- D) `spark.read.parquet(path)` and `spark.readStream.format("kafka")` — reading from external sources requires a local Spark context and is not supported remotely

---

### Question 94 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Spark Connect protocol — Apache Arrow for data transfer

**Question**:
How does Spark Connect transfer DataFrame results from the server to the client, and what format is used?

- A) Results are written to a shared HDFS or S3 location and the client reads the Parquet files directly
- B) Spark Connect uses **Apache Arrow** (specifically Arrow IPC / RecordBatch streaming) over gRPC to transfer result data from the server to the client — when a client action (`.collect()`, `.toPandas()`, `.show()`) is invoked, the server executes the plan, serializes the result as Arrow RecordBatches, and streams them to the client over the gRPC connection; the client deserializes the Arrow data into Python objects or a Pandas DataFrame; Arrow's columnar, zero-copy-friendly format minimises serialization overhead compared to Java object serialization used in classic PySpark; this also enables efficient interoperability with Pandas and other Arrow-native tools on the client side
- C) Spark Connect transfers results as JSON strings encoded in the gRPC response metadata; large results are chunked into 1 MB messages
- D) Results are sent using Java serialization (the same mechanism used for RDD operations), ensuring backward compatibility with existing PySpark code

---

### Question 95 — Spark Connect

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Starting a Spark Connect server — command and server lifecycle

**Question**:
How is a Spark Connect server started, and how does it differ from a standard `spark-submit` application?

- A) Spark Connect is started automatically when any `spark-submit` job runs; there is no separate startup command
- B) A Spark Connect server is started with `./sbin/start-connect-server.sh --packages org.apache.spark:spark-connect_2.12:<version>` (or via `spark-submit` with the Connect jar on the classpath and `--class org.apache.spark.sql.connect.service.SparkConnectServer`); unlike a `spark-submit` job that runs a user application and terminates, the Spark Connect server is a **long-running service** that accepts connections from multiple concurrent clients — each client session gets its own isolated `SparkSession` with independent configuration; the server continues running until explicitly stopped (`stop-connect-server.sh`), allowing many short-lived client programs to share the same Spark cluster
- C) Spark Connect is started by passing `--deploy-mode connect` to `spark-submit`; there is no separate server process — the Connect endpoint is embedded in the driver
- D) The Connect server is a cloud-managed service available only through Databricks or Google Dataproc; it cannot be self-hosted on an open-source Spark cluster

---

### Question 96 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.read_csv() vs pd.read_csv() — distributed vs single-node CSV reading

**Question**:
What is the key operational difference between `pyspark.pandas.read_csv(path)` and `pandas.read_csv(path)`?

- A) They are interchangeable; `pyspark.pandas.read_csv` is just a thin wrapper that calls `pandas.read_csv` internally
- B) `pyspark.pandas.read_csv(path)` reads the CSV file(s) in a **distributed manner using Spark** — the path can be a glob or directory of files; data is loaded across multiple partitions and executors; the result is a `pyspark.pandas.DataFrame` (backed by a Spark DataFrame) and the read scales with cluster size; `pandas.read_csv(path)` reads the file **on the driver node into memory** as a native `pandas.DataFrame` — it is single-threaded and limited by the driver's memory; for files larger than a few GB, `pd.read_csv` will cause OOM on the driver whereas `ps.read_csv` distributes the load
- C) `ps.read_csv` only supports files on HDFS; `pd.read_csv` supports local files; neither supports S3 natively
- D) `ps.read_csv` requires the CSV file to have a Spark-compatible header row with no spaces in column names; `pd.read_csv` accepts arbitrary headers

---

### Question 97 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: psdf.to_spark() — converting Pandas-on-Spark DataFrame to Spark DataFrame

**Question**:
What does `psdf.to_spark()` return and when is it necessary?

- A) It triggers eager evaluation and returns a native `pandas.DataFrame` on the driver
- B) `psdf.to_spark()` converts a `pyspark.pandas.DataFrame` (Pandas API on Spark) into a `pyspark.sql.DataFrame` — the underlying Spark DataFrame is returned, allowing use of native Spark APIs that are not available in the Pandas API (e.g., custom aggregations with `groupBy().agg()`, window functions, streaming writes, or any Spark-specific optimization hint); the column order in the resulting Spark DataFrame follows the index handling governed by `ps.get_option("compute.default_index_type")`; use `to_spark(index_col="my_index")` to control how the pandas index is represented in the Spark schema
- C) `to_spark()` collects all data to the driver and creates a Spark DataFrame from the local Pandas data, making it equivalent to `spark.createDataFrame(psdf.to_pandas())`
- D) `to_spark()` is only available after calling `psdf.cache()` first; otherwise it raises a `PandasNotImplementedError`

---

### Question 98 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: spark_df.pandas_api() — converting Spark DataFrame to Pandas-on-Spark

**Question**:
What does `spark_df.pandas_api()` do, and when would you use it over `spark_df.toPandas()`?

- A) Both collect data to the driver; `pandas_api()` is just the new name for `toPandas()` in Spark 3.2+
- B) `spark_df.pandas_api()` (Spark 3.2+) returns a `pyspark.pandas.DataFrame` that wraps the Spark DataFrame — **data stays distributed on the cluster** and all subsequent Pandas API operations (e.g., `.groupby()`, `.merge()`, `.apply()`) are executed as Spark jobs; `spark_df.toPandas()` **collects ALL rows to the driver** as a native pandas DataFrame — this can cause OOM for large datasets; use `pandas_api()` when you want to use familiar Pandas syntax for data exploration or transformation on a large distributed dataset, and use `toPandas()` only when the data is small enough to fit in driver memory
- C) `pandas_api()` is only available on DataFrames with fewer than 100 columns; for wider DataFrames use `toPandas()`
- D) `spark_df.pandas_api()` converts the Spark schema to a `pandas.MultiIndex` and returns a standard pandas DataFrame; no distributed computation is used after this call

---

### Question 99 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.merge() — merge semantics in Pandas-on-Spark

**Question**:
How does `ps.merge(left_psdf, right_psdf, on="id", how="left")` behave differently from the equivalent `spark_df.join()` with respect to duplicate column handling?

- A) `ps.merge` and `spark_df.join` are identical; `ps.merge` is just syntactic sugar
- B) `ps.merge(left, right, on=key, how=join_type)` follows **Pandas merge semantics**: when both DataFrames have non-join columns with the same name, the result includes both with `_x` and `_y` suffixes automatically (e.g., `name_x`, `name_y`); `spark_df.join(other, on=key, how=join_type)` by default keeps both columns with their original names and does **not** add suffixes — accessing ambiguous column names in the result raises an `AnalysisException`; developers must explicitly resolve ambiguity in Spark joins using `.alias()` or dropping duplicate columns; `ps.merge` handles this automatically, matching native Pandas behaviour
- C) `ps.merge` performs a cross join first and then filters; `spark_df.join` uses a hash join strategy
- D) `ps.merge` requires both DataFrames to have the same number of partitions; `spark_df.join` repartitions automatically

---

### Question 100 — Pandas API on Spark

**Difficulty**: Medium
**Answer Type**: one
**Topic**: ps.set_option("compute.max_rows") — safety limit for Pandas-on-Spark operations

**Question**:
What does `ps.set_option("compute.max_rows", 2000)` control, and what happens when it is set to `None`?

- A) It limits the number of partitions used during computation; setting it to `None` triggers automatic repartitioning
- B) `compute.max_rows` sets a safety threshold on operations that require collecting data to the driver (such as `.to_pandas()`, `repr()` in a notebook, and operations that trigger full DataFrame materialisation); when the result has more rows than `max_rows`, Spark raises a `ValueError` to prevent accidentally bringing a large distributed DataFrame to the driver — the default is `1000`; setting it to `None` disables the limit entirely and allows arbitrarily large collections (with the risk of driver OOM); it is recommended to keep a reasonable limit in production and only disable it for explicitly bounded result sets; use `ps.get_option("compute.max_rows")` to read the current value

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | `spark.executor.memoryOverhead` = off-heap container memory for YARN/K8s; not part of the JVM heap set by `spark.executor.memory`. | topic1-prompt1-spark-architecture.md | Easy |
| 2 | B | Speculative task launches when runtime exceeds `1.5 × median` of completed tasks AND `0.75` quantile of stage tasks are done. | topic1-prompt1-spark-architecture.md | Medium |
| 3 | B | `spark.task.maxFailures` (default `4`) = max individual task attempt failures before the stage and job abort. | topic1-prompt1-spark-architecture.md | Medium |
| 4 | B | `sc.setJobGroup(groupId, desc)` tags all subsequent jobs from that thread; `sc.cancelJobGroup(groupId)` cancels all tagged jobs. | topic1-prompt1-spark-architecture.md | Medium |
| 5 | B | `spark.default.parallelism` governs RDD operations; `spark.sql.shuffle.partitions` governs DataFrame/SQL shuffle output partitions. | topic1-prompt1-spark-architecture.md | Easy |
| 6 | B | `spark.network.timeout` is the global default for all network interactions including executor heartbeat loss detection. | topic1-prompt1-spark-architecture.md | Medium |
| 7 | B | An RPC message exceeding `spark.rpc.message.maxSize` raises `SparkException`; the config must be increased to accommodate the payload. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 8 | B | `BypassMergeSortShuffleWriter` is selected when reduce partitions ≤ `bypassMergeThreshold` AND no map-side aggregation is required. | topic1-prompt4-shuffling-performance.md | Hard |
| 9 | B | `spark.reducer.maxSizeInFlight` limits the total bytes a reducer fetches simultaneously from remote shuffle blocks. | topic1-prompt4-shuffling-performance.md | Easy |
| 10 | B | `spark.excludeOnFailure.enabled` is the Spark 3.1 rename of the deprecated `spark.blacklist.enabled`. | topic1-prompt1-spark-architecture.md | Medium |
| 11 | B | `spark.executor.heartbeatInterval` must be much less than `spark.network.timeout`; a single GC-paused heartbeat must not exceed the timeout. | topic1-prompt1-spark-architecture.md | Medium |
| 12 | B | The "dropped events" warning means the async listener bus queue was full; events were dropped to avoid blocking the scheduler. | topic1-prompt1-spark-architecture.md | Medium |
| 13 | B | A second app's UI uses port `4041` (increments from `4040`); the History Server runs on `18080`. | topic1-prompt1-spark-architecture.md | Easy |
| 14 | A | `MEMORY_AND_DISK_SER` stores serialized byte arrays (smaller footprint, lower GC pressure); `MEMORY_AND_DISK` stores deserialized Java objects. | topic1-prompt7-garbage-collection.md | Medium |
| 15 | B | Speculative execution triggers when a task exceeds `1.5 × median` runtime AND at least `75%` of the stage's tasks have completed. | topic1-prompt1-spark-architecture.md | Medium |
| 16 | B | `spark.driver.memoryOverhead` = non-JVM container memory for the driver (Python process, NIO buffers, OS); default is `max(10%, 384 MB)`. | topic1-prompt1-spark-architecture.md | Medium |
| 17 | B | The broadcast variable is serialized once on the driver and transferred to each executor (not each task); executors cache one deserialized copy. | topic1-prompt5-broadcasting-optimization.md | Medium |
| 18 | B | `spark.worker.cleanup.enabled=true` periodically removes finished application working directories; Standalone-mode only. | topic1-prompt1-spark-architecture.md | Medium |
| 19 | B | `cluster` mode: driver runs on a cluster node, submitting client exits after acceptance. `client` mode: driver runs in the submitting process. | topic1-prompt1-spark-architecture.md | Medium |
| 20 | B | `executorIdleTimeout`: idle executors are removed after this period; executors holding shuffle data are exempt from removal. | topic1-prompt1-spark-architecture.md | Medium |
| 21 | B | `timestampdiff('HOUR', ...)` returns `IntegerType` truncated to complete hours; 4 hours 45 minutes → `4`. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 22 | B | `months_between` returns `DoubleType`; fractional portion = day_diff ÷ 31; result ≈ 2.16129. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 23 | A | `last_day('2026-02-10')` = `'2026-02-28'`; for a leap-year February it returns February 29. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 24 | B | `next_day('2026-04-25', 'MON')` = `'2026-04-27'`; returns the first date **after** the input on the specified weekday. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 25 | B | `from_unixtime` returns `StringType`; interprets the epoch value in the session timezone. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 26 | B | `date_add('2026-01-28', 5)` = `'2026-02-02'` as `DateType`. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 27 | B | `to_timestamp(str)` expects ISO 8601; for non-ISO strings use `to_timestamp(str, fmt)` with an explicit pattern; both return `TimestampType`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 28 | B | `dayofweek('2026-04-25')` = `7` (Saturday); Java Calendar convention: Sunday=1, Saturday=7. | topic2-prompt9-builtin-sql-functions.md | Easy |
| 29 | B | `date_trunc` on `TimestampType` → `TimestampType`; `trunc` on `DateType` → `DateType`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 30 | B | `unix_timestamp` returns `LongType` epoch seconds, interpreting the timestamp in the session timezone. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 31 | B | `sha1` and `sha2` both return `StringType` lowercase hex; `sha1` = 40 chars (160-bit); `sha2(col, 256)` = 64 chars; valid `sha2` bit-lengths: `0` (=256), `224`, `256`, `384`, `512`; unsupported length → `NULL`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 32 | B | `base64(BinaryType)` → `StringType`; `unbase64(StringType)` → `BinaryType`; to encode a string column, first `CAST(str_col AS BINARY)`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 33 | B | `sentences(str)` splits at sentence boundaries then at word boundaries, returning `ArrayType(ArrayType(StringType))`; punctuation is excluded. | topic2-prompt9-builtin-sql-functions.md | Hard |
| 34 | B | `levenshtein('kitten', 'sitting')` = `3` edits (k→s, e→i, append g); returns `IntegerType`; Spark 3.5+ accepts a `threshold` argument returning `-1` if exceeded. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 35 | B | `element_at` uses 1-based positive indexing and negative reverse indexing (`-1` = last); out-of-bounds returns `NULL`, not an exception. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 36 | A | `slice(array, start, length)` uses 1-based `start`; `start=2, length=3` → elements at positions 2, 3, 4 = `array(20, 30, 40)`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 37 | B | With `null_replacement`, NULLs are substituted → `'a-N/A-c'`; without it, NULLs are silently skipped → `'a-c'`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 38 | B | `filter(array_col, func)` keeps elements where the lambda returns `true`; `false` or `NULL` elements are excluded → `array(4, 5)`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 39 | B | `transform(array_col, func)` applies the lambda to every element, returning a same-length array of mapped values → `array(1, 4, 9)`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 40 | B | `hex(255)` → `'FF'`; accepts `IntegerType`, `LongType`, or `BinaryType`, returns uppercase `StringType`; `unhex` is the inverse returning `BinaryType`. | topic2-prompt9-builtin-sql-functions.md | Medium |
| 41 | B | `except` = EXCEPT DISTINCT (removes all occurrences of any `df2` row from `df1`); `exceptAll` = EXCEPT ALL (each `df2` occurrence removes exactly one `df1` occurrence). | topic3-prompt18-combining-dataframes.md | Easy |
| 42 | B | `intersect` = INTERSECT DISTINCT (distinct common rows); `intersectAll` = INTERSECT ALL (preserves duplicates; result count = `min(count_df1, count_df2)`). | topic3-prompt18-combining-dataframes.md | Medium |
| 43 | B | `withColumnsRenamed(map)` renames multiple columns in one logical plan node; chained `withColumnRenamed` calls each add a `Project` node, deepening the plan. | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 44 | B | `df.transform(func)` calls `func(df)` and returns the result; enables left-to-right method chaining of modular transformation functions with no side effects. | topic3-prompt12-dataframe-creation-selection.md | Medium |
| 45 | B | `F.greatest(*cols)` = row-wise maximum across multiple columns (horizontal); `F.max(col)` = aggregate maximum of one column across all rows (vertical). | topic3-prompt13-column-manipulation-expressions.md | Medium |
| 46 | B | `F.coalesce(*cols)` = column function returning first non-null value per row; `df.coalesce(n)` = DataFrame transformation reducing partition count without a shuffle. | topic3-prompt16-handling-nulls.md | Easy |
| 47 | B | `F.nanvl(col1, col2)` returns `col1` when normal float; `col2` when `col1` is `NaN`; propagates `NULL` unchanged — unlike `F.coalesce` which handles `NULL` but not `NaN`. | topic3-prompt16-handling-nulls.md | Medium |
| 48 | B | `collect_list` collects all values including duplicates and NULLs; `collect_set` collects distinct values only and silently drops NULLs; both have non-deterministic order. | topic3-prompt15-aggregations-grouping.md | Medium |
| 49 | B | `rollup("year", "quarter")` produces `(year, quarter)`, `(year, NULL)`, and `(NULL, NULL)` — n+1 grouping sets rolling up from most specific to grand total. | topic3-prompt15-aggregations-grouping.md | Medium |
| 50 | B | Without explicit pivot values Spark runs an extra scan to discover distinct values; providing them eliminates that preliminary job and makes the schema deterministic. | topic3-prompt15-aggregations-grouping.md | Medium |
| 51 | B | `F.struct(col("first"), col("last"))` creates a `StructType` column with fields `first` and `last` per row; accessible via dot notation or `getField`. | topic3-prompt21-schemas-data-types.md | Medium |
| 52 | B | Both `col("address.city")` (dot-path notation) and `col("address").getField("city")` correctly extract the nested `city` field; they are equivalent. | topic3-prompt21-schemas-data-types.md | Medium |
| 53 | B | `F.array(*cols)` creates a fixed-length `ArrayType` column; element access: `element_at(arr, idx)` (1-based) or `arr[idx]` (0-based in DataFrame API). | topic3-prompt21-schemas-data-types.md | Medium |
| 54 | B | `F.map_keys(map_col)` → `ArrayType(KeyType)`; `F.map_values(map_col)` → `ArrayType(ValueType)`; order is consistent per row; `F.map_entries` yields an array of key-value structs. | topic3-prompt21-schemas-data-types.md | Medium |
| 55 | B | `explode` drops rows where the array/map is NULL or empty; `explode_outer` preserves those rows by emitting a single row with `NULL` as the exploded value. | topic3-prompt14-filtering-row-manipulation.md | Easy |
| 56 | B | `posexplode` produces two columns: `pos` (0-based integer index) and `col` (element value); `posexplode_outer` is the NULL-preserving variant. | topic3-prompt14-filtering-row-manipulation.md | Medium |
| 57 | B | `F.flatten(ArrayType(ArrayType(T)))` removes one nesting level, concatenating all inner arrays into `ArrayType(T)` → `array(1, 2, 3, 4, 5)`. | topic3-prompt21-schemas-data-types.md | Medium |
| 58 | B | `F.arrays_zip(*array_cols)` produces `ArrayType(StructType)` — each element is a struct of corresponding elements; shorter arrays are padded with `NULL`. | topic3-prompt21-schemas-data-types.md | Medium |
| 59 | B | `F.create_map(*exprs)` takes alternating key-value column expressions and returns a `MapType` column; all keys must share one type, all values must share one type. | topic3-prompt21-schemas-data-types.md | Medium |
| 60 | B | `F.map_filter(map_col, func)` returns a new `MapType` keeping only entries where the lambda `(k, v)` returns `true` → `{"math": 95, "science": 91}`. | topic3-prompt21-schemas-data-types.md | Medium |
| 61 | B | `rowsBetween` uses physical row offsets; `rangeBetween` uses ORDER BY value offsets — they differ when there are ties or when numeric range offsets span multiple rows. | topic2-prompt10-window-functions.md | Medium |
| 62 | B | When the offset exceeds the partition boundary, `F.lag` / `F.lead` return the `default` argument (`0.0` here) instead of `NULL`. | topic2-prompt10-window-functions.md | Medium |
| 63 | B | `rank` → `[1,1,3,4]` (gaps after ties); `dense_rank` → `[1,1,2,3]` (no gaps); `row_number` → `[1,2,3,4]` (unique sequential regardless of ties). | topic2-prompt10-window-functions.md | Easy |
| 64 | B | `F.ntile(n)` assigns integer buckets 1–n as evenly as possible; for 7 rows and n=4: `[1,1,2,2,3,3,4]`; extra rows go to earlier buckets. | topic2-prompt10-window-functions.md | Medium |
| 65 | B | `rowsBetween(UNBOUNDED_PRECEDING, CURRENT_ROW)` defines a growing frame from partition start to current row → cumulative sum of `salary` ordered by `hire_date` per `dept`. | topic2-prompt10-window-functions.md | Hard |
| 66 | B | With `orderBy` present the default frame is `rangeBetween(UNBOUNDED_PRECEDING, CURRENT_ROW)` (cumulative); without `orderBy` the default is `rowsBetween(UNBOUNDED_PRECEDING, UNBOUNDED_FOLLOWING)` (full partition). | topic2-prompt10-window-functions.md | Medium |
| 67 | B | `sampleBy` performs stratified random sampling per label value at the specified fraction; labels absent from `fractions` are excluded entirely; sampling is approximate. | topic3-prompt14-filtering-row-manipulation.md | Easy |
| 68 | B | `approx_count_distinct` uses HyperLogLog++ with configurable `rsd`; significantly more memory-efficient and faster than `countDistinct` which requires exact deduplication via shuffle. | topic3-prompt15-aggregations-grouping.md | Medium |
| 69 | B | `percentile_approx(col, quantile, accuracy)` approximates the percentile using Greenwald-Khanna; higher `accuracy` = better precision but more memory; accepts a list of quantiles. | topic3-prompt15-aggregations-grouping.md | Medium |
| 70 | B | `F.count_if(condition)` counts rows where the boolean condition is `true` (Spark 3.3+); equivalent to `COUNT(CASE WHEN condition THEN 1 END)`. | topic3-prompt15-aggregations-grouping.md | Medium |
| 71 | B | AQE's three core features: (1) dynamically coalescing shuffle partitions; (2) dynamically switching join strategies (SMJ → BHJ); (3) dynamically optimizing skew joins. | topic4-prompt24-performance-tuning.md | Easy |
| 72 | B | `repartition(n)` = full shuffle, can increase or decrease count, produces balanced partitions; `coalesce(n)` = narrow transformation, can only decrease, may produce uneven partitions. | topic3-prompt20-repartition-coalesce.md | Easy |
| 73 | B | `advisoryPartitionSizeInBytes` is the target hint for AQE's dynamic partition coalescing — consecutive small post-shuffle partitions are merged until the combined size approaches this value. | topic4-prompt24-performance-tuning.md | Medium |
| 74 | B | AQE splits partitions exceeding `skewedPartitionFactor × median` AND `skewedPartitionThresholdInBytes`; the other side is replicated per sub-partition to balance work — transparent to users. | topic4-prompt24-performance-tuning.md | Medium |
| 75 | B | `explain()` shows Physical Plan only; `explain(True)` / `mode="extended"` shows all four: Parsed Logical, Analyzed Logical, Optimized Logical, and Physical Plans. | topic4-prompt26-debugging.md | Medium |
| 76 | B | Without the external shuffle service, removing an idle executor also loses its shuffle files; the service is a long-running node daemon serving shuffle blocks independently of executor lifecycle. | topic4-prompt24-performance-tuning.md | Medium |
| 77 | B | `lz4` is the default `spark.io.compression.codec` (fast, moderate ratio); `zstd` achieves higher compression ratios at greater CPU cost — preferred when network bandwidth is the bottleneck. | topic4-prompt24-performance-tuning.md | Medium |
| 78 | B | After AQE converts sort-merge to broadcast, the local shuffle reader lets executors read only locally-written shuffle blocks from disk, eliminating cross-network transfers for that side. | topic4-prompt24-performance-tuning.md | Medium |
| 79 | B | `df.cache()` = `persist(StorageLevel.MEMORY_AND_DISK)`; deserialized objects in JVM heap, spills to disk when full; call `unpersist()` when data is no longer needed. | topic4-prompt24-performance-tuning.md | Medium |
| 80 | B | Two steps: (1) set `spark.sql.cbo.enabled=true`; (2) run `ANALYZE TABLE ... COMPUTE STATISTICS` (optionally per column); without statistics the CBO falls back to heuristics. | topic4-prompt24-performance-tuning.md | Hard |
| 81 | B | `trigger(once=True)` = one micro-batch then stop; `trigger(availableNow=True)` (Spark 3.3+) = multiple micro-batches respecting `maxFilesPerTrigger` limits before stopping. | topic5-prompt27-structured-streaming.md | Easy |
| 82 | B | Watermark = `max(event_time) − 10 minutes`; events older than the threshold are dropped; state for past windows is evicted; with Append mode, results emit only after the watermark passes the window end. | topic5-prompt28-stateful-streaming.md | Medium |
| 83 | B | `Append` mode emits window results once after watermark passes window end, then discards state. `Complete` mode retains all aggregation state forever — unbounded and impractical for long-running streaming. | topic5-prompt27-structured-streaming.md | Medium |
| 84 | B | The batch runs to completion (45 s); the next trigger fires immediately after (not after an additional 30 s); back-to-back batches run serially; sustained overrun signals a need to optimize or widen the interval. | topic5-prompt27-structured-streaming.md | Medium |
| 85 | B | Three categories: (1) `offsets/` — source offsets per micro-batch; (2) `commits/` — commit log confirming batch completion; (3) `state/` — stateful operation snapshots for aggregations, joins, and deduplication. | topic5-prompt27-structured-streaming.md | Medium |
| 86 | B | `foreachBatch(func)` calls `func(micro_batch_df, batch_id)` once per micro-batch; `micro_batch_df` is a regular static DataFrame; `batch_id` enables idempotent writes; used for sinks without native streaming support. | topic5-prompt27-structured-streaming.md | Medium |
| 87 | B | Delta's transaction log enables efficient new-data discovery, exactly-once semantics, Change Data Feed (insert/update/delete streaming), and `startingVersion`/`startingTimestamp` — none available with plain Parquet. | topic5-prompt27-structured-streaming.md | Medium |
| 88 | B | With `withWatermark`, `dropDuplicates` stores seen IDs only within the watermark window; IDs older than `max_ts − threshold` are evicted from state, bounding memory growth. | topic5-prompt28-stateful-streaming.md | Medium |
| 89 | B | `awaitTermination()` blocks the calling thread until the query stops; `stop()` actively and gracefully stops the query (finishes current micro-batch); `awaitTermination(timeout_ms)` returns `True`/`False`. | topic5-prompt27-structured-streaming.md | Medium |
| 90 | B | `recentProgress` = list of per-batch metric dicts (`batchId`, `numInputRows`, `inputRowsPerSecond`, `processedRowsPerSecond`, `durationMs`, etc.); `lastProgress` = most recent dict. | topic5-prompt27-structured-streaming.md | Medium |
| 91 | B | Spark Connect is a client-server architecture over gRPC; the driver runs as a separate server process; thin multi-language clients connect remotely with minimal footprint and no embedded JVM. | topic6-prompt29-spark-connect.md | Easy |
| 92 | B | `SparkSession.builder.remote("sc://hostname:15002").getOrCreate()` using the `sc://` scheme; requires `pyspark[connect]` extras; no local JVM is started. | topic6-prompt29-spark-connect.md | Medium |
| 93 | B | `SparkContext` and the RDD API (`sc.parallelize`, `rdd.map`, accumulators via `SparkContext`, etc.) are unavailable in Spark Connect client mode; only the structured DataFrame/SQL API is supported. | topic6-prompt29-spark-connect.md | Medium |
| 94 | B | Results are serialized as Apache Arrow RecordBatches and streamed over gRPC from server to client; Arrow's columnar format minimises serialization overhead and enables zero-copy interop with Pandas. | topic6-prompt29-spark-connect.md | Medium |
| 95 | B | Started with `start-connect-server.sh`; runs as a long-running service accepting multiple concurrent client sessions, each with an isolated `SparkSession`; stopped with `stop-connect-server.sh`. | topic6-prompt29-spark-connect.md | Medium |
| 96 | B | `ps.read_csv` reads distributed across Spark executors (scales with cluster); `pd.read_csv` reads entirely on the driver (single-threaded, limited by driver memory — OOM risk for large files). | topic7-prompt30-pandas-api.md | Medium |
| 97 | B | `to_spark()` returns the underlying `pyspark.sql.DataFrame`, enabling native Spark APIs (window functions, custom aggregations, streaming writes) unavailable in the Pandas API. | topic7-prompt30-pandas-api.md | Medium |
| 98 | B | `pandas_api()` returns a `pyspark.pandas.DataFrame` (data stays distributed); `toPandas()` collects ALL rows to driver memory — use `pandas_api()` for large datasets, `toPandas()` only for small results. | topic7-prompt30-pandas-api.md | Medium |
| 99 | B | `ps.merge` automatically adds `_x`/`_y` suffixes for conflicting non-join column names (Pandas semantics); `spark_df.join` keeps original names and raises `AnalysisException` on ambiguous column access. | topic7-prompt30-pandas-api.md | Medium |
| 100 | B | `compute.max_rows` (default `1000`) raises `ValueError` when a collect-to-driver operation exceeds the limit; setting to `None` disables the guard entirely, risking driver OOM. | topic7-prompt30-pandas-api.md | Medium |
