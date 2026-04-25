# 8-Week Study Plan — Databricks Certified Associate Developer for Apache Spark

> **Exam Quick Reference**
> | Detail | Value |
> |--------|-------|
> | Questions | 45 multiple choice |
> | Time | 90 minutes |
> | Fee | $200 |
> | Language | Python |
> | Passing Score | TBD |
> | Validity | 2 years |
> | Prerequisites | None (6+ months hands-on recommended) |

**How to use this plan:**
- Work Monday–Friday for focused study (60–90 min/day).
- Use Saturday for hands-on notebook practice (run the `.ipynb` files).
- Use Sunday for question-bank review and self-assessment.
- Check off `- [ ]` boxes as you complete each task.
- Re-read any notebook you scored below 70% on its related questions.

---

## Topic Weight Reminder

| # | Topic | Exam Weight | Weeks |
|---|-------|-------------|-------|
| 1 | Apache Spark Architecture & Components | 20% | 1–2 |
| 2 | Using Spark SQL | 20% | 2–3 |
| 3 | DataFrame / DataSet API | 30% | 4–5 |
| 4 | Troubleshooting & Tuning | 10% | 6 |
| 5 | Structured Streaming | 10% | 6 |
| 6 | Spark Connect | 5% | 7 |
| 7 | Pandas API on Spark | 5% | 7 |

---

## Week 1 — Spark Architecture Fundamentals

> **Exam weight covered:** Topic 1 — Architecture & Components (20%)  
> **Question bank:** [Iteration 1](./questions/spark-200-iteration-1.md) (~100 questions)

### Key Concepts This Week
- [ ] Driver vs Executor roles and responsibilities
- [ ] Physical execution hierarchy: Driver → Executors → Tasks
- [ ] Logical execution hierarchy: Action → Job → Stage → Task
- [ ] DAGScheduler: builds stage DAG, splits at shuffle boundaries
- [ ] TaskScheduler: submits tasks to Executors via SchedulerBackend
- [ ] SparkContext and SparkSession relationship
- [ ] Cluster Manager role (Standalone, YARN, Kubernetes, Mesos)
- [ ] Execution modes: local, standalone, YARN, Kubernetes
- [ ] Deploy modes: `--deploy-mode client` vs `--deploy-mode cluster`
- [ ] What lazy evaluation means and why Spark uses it
- [ ] Transformations (narrow vs wide) vs Actions

### Daily Schedule

#### Monday — Spark Architecture Fundamentals
- [ ] Read: [topic1-prompt1-spark-architecture.md](./topic1-prompt1-spark-architecture.md)
- [ ] Focus: Driver/Executor/Task hierarchy, DAGScheduler, TaskScheduler, Cluster Manager
- [ ] Write down: in your own words, trace the path of a `df.count()` call from SparkSession to result

#### Tuesday — Execution Modes and Deploy Modes
- [ ] Read: [topic1-prompt2-execution-deploy-modes.md](./topic1-prompt2-execution-deploy-modes.md)
- [ ] Focus: local/standalone/YARN/Kubernetes; client vs cluster deploy mode
- [ ] Memorize: `spark-submit --master yarn --deploy-mode cluster` — where does the driver run?
- [ ] Memorize: In `client` mode, the submitting machine must stay alive; in `cluster` mode it does not

#### Wednesday — Lazy Evaluation & Transformations vs Actions
- [ ] Read: [topic1-prompt3-lazy-evaluation.md](./topic1-prompt3-lazy-evaluation.md)
- [ ] Focus: what makes a transformation lazy, what triggers execution (actions)
- [ ] Write down: 5 transformations and 5 actions from memory
- [ ] Understand: why lazy evaluation allows Catalyst to optimize the whole plan before running anything

#### Thursday — Shuffling and Performance Impact
- [ ] Read: [topic1-prompt4-shuffling-performance.md](./topic1-prompt4-shuffling-performance.md)
- [ ] Focus: what operations cause shuffles (joins, groupBy, orderBy, repartition)
- [ ] Understand: shuffle is expensive because it involves disk I/O and network transfer
- [ ] Practice: identify which of `filter`, `map`, `groupBy`, `union`, `join` cause shuffles

#### Friday — Review Day
- [ ] Re-read any section from Mon–Thu that felt unclear
- [ ] Write a one-paragraph summary of how a Spark job runs end-to-end from SparkSession to task completion
- [ ] Identify your 3 weakest concepts from this week

#### Saturday — Hands-On Practice
- [ ] Run: [topic1-prompt1-spark-architecture.ipynb](./topic1-prompt1-spark-architecture.ipynb)
- [ ] Run: [topic1-prompt2-execution-deploy-modes.ipynb](./topic1-prompt2-execution-deploy-modes.ipynb)
- [ ] Run: [topic1-prompt3-lazy-evaluation.ipynb](./topic1-prompt3-lazy-evaluation.ipynb)
- [ ] Run: [topic1-prompt4-shuffling-performance.ipynb](./topic1-prompt4-shuffling-performance.ipynb)
- [ ] Modify and re-run at least one example in each notebook to confirm your understanding

#### Sunday — Question Bank Practice
- [ ] Attempt: [Iteration 1 — Q1–Q50](./questions/spark-200-iteration-1.md) (Q1–Q50)
- [ ] Attempt: [Iteration 1 — Q51–Q100](./questions/spark-200-iteration-1.md) (Q51–Q100)
- [ ] Review every question you got wrong — trace the answer back to the notebook that covers it
- [ ] Score yourself: \_\_\_ / 100. If < 70, re-read the relevant notebook before Week 2

---

## Week 2 — Architecture Advanced + Spark SQL Introduction

> **Exam weight covered:** Topic 1 Architecture (finish) + Topic 2 SQL intro (20% each)  
> **Question bank:** [Iteration 2](./questions/spark-200-iteration-2.md) (~100 questions)

### Key Concepts This Week
- [ ] Broadcasting: what it is, when to use it, `broadcast()` function, auto-broadcast threshold
- [ ] Fault tolerance: RDD lineage, checkpointing, persistence/caching strategies
- [ ] Garbage collection: GC pressure from large objects in JVM heap, G1GC tuning basics
- [ ] Spark SQL: createOrReplaceTempView, spark.sql(), DataFrame ↔ SQL interoperability
- [ ] Built-in SQL functions: string, numeric, date/time, conditional (when/otherwise)
- [ ] SparkSession.sql() returns a DataFrame; SQL and DataFrame API are equivalent

### Daily Schedule

#### Monday — Broadcasting & Optimization
- [ ] Read: [topic1-prompt5-broadcasting-optimization.md](./topic1-prompt5-broadcasting-optimization.md)
- [ ] Focus: `spark.sql.autoBroadcastJoinThreshold` (default 10 MiB), explicit `broadcast(df)` hint
- [ ] Understand: broadcast join avoids shuffle by sending the small table to every executor

#### Tuesday — Fault Tolerance & Resilience
- [ ] Read: [topic1-prompt6-fault-tolerance.md](./topic1-prompt6-fault-tolerance.md)
- [ ] Focus: RDD lineage graph, `.persist()` / `.cache()` storage levels, `.checkpoint()`
- [ ] Understand: Spark re-computes lost partitions from lineage; checkpointing breaks lineage chains for iterative algorithms

#### Wednesday — Garbage Collection in Spark
- [ ] Read: [topic1-prompt7-garbage-collection.md](./topic1-prompt7-garbage-collection.md)
- [ ] Focus: GC pauses cause task delays; G1GC recommended; minimize object creation in transformations
- [ ] Understand: `spark.memory.fraction` and `spark.memory.storageFraction` split unified memory

#### Thursday — Spark SQL Fundamentals
- [ ] Read: [topic2-prompt8-spark-sql-fundamentals.md](./topic2-prompt8-spark-sql-fundamentals.md)
- [ ] Focus: `createOrReplaceTempView`, `spark.sql("SELECT ...")`, global temp views (`global_temp.view_name`)
- [ ] Practice: write 3 SQL queries using `WHERE`, `GROUP BY`, `ORDER BY`, `JOIN`

#### Friday — Built-in Spark SQL Functions
- [ ] Read: [topic2-prompt9-builtin-sql-functions.md](./topic2-prompt9-builtin-sql-functions.md)
- [ ] Focus: `upper`, `lower`, `trim`, `substring`, `concat`, `coalesce`, `nvl`, date functions, `cast`
- [ ] Write down: 10 functions you are least familiar with and their signatures

#### Saturday — Hands-On Practice
- [ ] Run: [topic1-prompt5-broadcasting-optimization.ipynb](./topic1-prompt5-broadcasting-optimization.ipynb)
- [ ] Run: [topic1-prompt6-fault-tolerance.ipynb](./topic1-prompt6-fault-tolerance.ipynb)
- [ ] Run: [topic1-prompt7-garbage-collection.ipynb](./topic1-prompt7-garbage-collection.ipynb)
- [ ] Run: [topic2-prompt8-spark-sql-fundamentals.ipynb](./topic2-prompt8-spark-sql-fundamentals.ipynb)
- [ ] Run: [topic2-prompt9-builtin-sql-functions.ipynb](./topic2-prompt9-builtin-sql-functions.ipynb)

#### Sunday — Question Bank Practice
- [ ] Attempt: [Iteration 2 — Q1–Q50](./questions/spark-200-iteration-2.md) (Q1–Q50)
- [ ] Attempt: [Iteration 2 — Q51–Q100](./questions/spark-200-iteration-2.md) (Q51–Q100)
- [ ] Compare with Week 1 score. Are Architecture questions improving?
- [ ] Score yourself: \_\_\_ / 100

---

## Week 3 — Spark SQL Deep Dive

> **Exam weight covered:** Topic 2 — Using Spark SQL (finish, 20%)  
> **Question bank:** [Iteration 3](./questions/spark-200-iteration-3.md) (~100 questions)

### Key Concepts This Week
- [ ] Window functions: `RANK()`, `DENSE_RANK()`, `ROW_NUMBER()`, `LAG()`, `LEAD()`
- [ ] Window spec: `PARTITION BY`, `ORDER BY`, `ROWS BETWEEN` / `RANGE BETWEEN`
- [ ] `Window.partitionBy().orderBy().rowsBetween()`
- [ ] Query optimization: Catalyst optimizer, logical vs physical plan, `explain()`
- [ ] Predicate pushdown, column pruning, broadcast join hints
- [ ] `df.explain("extended")` to view all plan stages
- [ ] Common SQL exam patterns: subqueries, CTEs (`WITH`), `CASE WHEN`

### Daily Schedule

#### Monday — Window Functions
- [ ] Read: [topic2-prompt10-window-functions.md](./topic2-prompt10-window-functions.md)
- [ ] Focus: `windowSpec = Window.partitionBy("dept").orderBy("salary")`, then `F.rank().over(windowSpec)`
- [ ] Practice: compute running total, dense rank, and lag/lead for a sample dataset

#### Tuesday — Query Optimization and Execution Plans
- [ ] Read: [topic2-prompt11-query-optimization.md](./topic2-prompt11-query-optimization.md)
- [ ] Focus: Catalyst phases (Analysis → Logical Optimization → Physical Planning → Code Generation)
- [ ] Run `df.explain(True)` on a query with a join and a filter; read the output top-to-bottom

#### Wednesday — SQL Pattern Consolidation
- [ ] Review both Topic 2 notebooks from scratch without looking at notes
- [ ] Write 5 window function queries from memory
- [ ] Write a query using a CTE, a subquery, and a `CASE WHEN`
- [ ] Practice identifying predicate pushdown in `explain()` output

#### Thursday — Cross-Topic Integration
- [ ] Compare: SQL `SELECT col FROM view WHERE cond GROUP BY col` vs equivalent DataFrame API chain
- [ ] Practice converting 5 SQL queries to DataFrame API and vice versa
- [ ] Understand: both compile to the same logical plan — the optimizer treats them identically

#### Friday — Topic 1 + 2 Full Review
- [ ] Write a one-page summary covering all of Topics 1 and 2
- [ ] List every exam bullet point from the overview for Topics 1 and 2 — can you explain each?
- [ ] Flag any remaining gaps for weekend reinforcement

#### Saturday — Hands-On Practice
- [ ] Run: [topic2-prompt10-window-functions.ipynb](./topic2-prompt10-window-functions.ipynb)
- [ ] Run: [topic2-prompt11-query-optimization.ipynb](./topic2-prompt11-query-optimization.ipynb)
- [ ] Create your own notebook: write 10 window function queries on sample data you invent
- [ ] Run `explain()` on at least 5 different query shapes and annotate what each plan section means

#### Sunday — Question Bank Practice
- [ ] Attempt: [Iteration 3 — Q1–Q50](./questions/spark-200-iteration-3.md) (Q1–Q50)
- [ ] Attempt: [Iteration 3 — Q51–Q100](./questions/spark-200-iteration-3.md) (Q51–Q100)
- [ ] Focus especially on Q21–Q40 (SQL topic questions)
- [ ] Score yourself: \_\_\_ / 100

---

## Week 4 — DataFrame API: Core Operations

> **Exam weight covered:** Topic 3 — DataFrame/DataSet API (30%) — Part 1 of 2  
> **Question bank:** [Iteration 4](./questions/spark-200-iteration-4.md) + [Iteration 5](./questions/spark-200-iteration-5.md) (~200 questions)

### Key Concepts This Week
- [ ] DataFrame creation: `spark.createDataFrame()`, `spark.read`
- [ ] Column selection: `select()`, `selectExpr()`, `col()`, `$"col"`, `df["col"]`
- [ ] Renaming: `withColumnRenamed()`, `.alias()`
- [ ] Column manipulation: `withColumn()`, `cast()`, `lit()`, `when().otherwise()`
- [ ] Filtering: `filter()` / `where()`, SQL string predicates, Column expressions
- [ ] Aggregations: `groupBy().agg()`, `count()`, `sum()`, `avg()`, `min()`, `max()`
- [ ] Handling nulls: `isNull()`, `isNotNull()`, `na.fill()`, `na.drop()`, `coalesce()`
- [ ] `dropDuplicates()` vs `distinct()`

### Daily Schedule

#### Monday — DataFrame Creation, Selection, and Renaming
- [ ] Read: [topic3-prompt12-dataframe-creation-selection.md](./topic3-prompt12-dataframe-creation-selection.md)
- [ ] Focus: all three column reference styles (`col()`, `df["col"]`, string in `select`)
- [ ] Practice: create a DataFrame from a Python list of dicts; select and rename 3 columns

#### Tuesday — Column Manipulation and Expressions
- [ ] Read: [topic3-prompt13-column-manipulation-expressions.md](./topic3-prompt13-column-manipulation-expressions.md)
- [ ] Focus: `withColumn`, `cast`, `lit`, `when(...).otherwise(...)`, `expr()`
- [ ] Practice: add a column that is a conditional transformation of another column

#### Wednesday — Filtering and Row Manipulation
- [ ] Read: [topic3-prompt14-filtering-row-manipulation.md](./topic3-prompt14-filtering-row-manipulation.md)
- [ ] Focus: `filter()` vs `where()` (identical), chaining multiple conditions with `&` `|` `~`
- [ ] Memorize: use parentheses around each condition when combining with `&` / `|`
- [ ] Practice: filter a DataFrame with 3 combined conditions

#### Thursday — Aggregations and Grouping
- [ ] Read: [topic3-prompt15-aggregations-grouping.md](./topic3-prompt15-aggregations-grouping.md)
- [ ] Focus: `groupBy().agg()` with multiple aggregation functions, `F.count("*")` vs `F.count("col")`
- [ ] Understand: `count("col")` ignores nulls; `count("*")` or `count(lit(1))` counts all rows
- [ ] Practice: compute multiple aggregations in a single `groupBy().agg()` call

#### Friday — Handling Nulls
- [ ] Read: [topic3-prompt16-handling-nulls.md](./topic3-prompt16-handling-nulls.md)
- [ ] Focus: `na.fill(value)`, `na.fill({col: value})`, `na.drop(how, thresh, subset)`, `coalesce(c1, c2)`
- [ ] Understand: `dropna` / `fillna` are aliases for `na.drop` / `na.fill`
- [ ] Memorize: null-safe equality uses `eqNullSafe()` not `==`

#### Saturday — Hands-On Practice
- [ ] Run: [topic3-prompt12-dataframe-creation-selection.ipynb](./topic3-prompt12-dataframe-creation-selection.ipynb)
- [ ] Run: [topic3-prompt13-column-manipulation-expressions.ipynb](./topic3-prompt13-column-manipulation-expressions.ipynb)
- [ ] Run: [topic3-prompt14-filtering-row-manipulation.ipynb](./topic3-prompt14-filtering-row-manipulation.ipynb)
- [ ] Run: [topic3-prompt15-aggregations-grouping.ipynb](./topic3-prompt15-aggregations-grouping.ipynb)
- [ ] Run: [topic3-prompt16-handling-nulls.ipynb](./topic3-prompt16-handling-nulls.ipynb)

#### Sunday — Question Bank Practice
- [ ] Attempt: [Iteration 4 — Q1–Q50](./questions/spark-200-iteration-4.md) (Q1–Q50)
- [ ] Attempt: [Iteration 4 — Q51–Q100](./questions/spark-200-iteration-4.md) (Q51–Q100)
- [ ] Attempt: [Iteration 5 — Q41–Q70](./questions/spark-200-iteration-5.md) (DataFrame section Q41–Q70)
- [ ] Score yourself on Iter 4: \_\_\_ / 100 and Iter 5 partial: \_\_\_ / 30

---

## Week 5 — DataFrame API: Advanced Operations

> **Exam weight covered:** Topic 3 — DataFrame/DataSet API (30%) — Part 2 of 2  
> **Question bank:** [Iteration 5](./questions/spark-200-iteration-5.md) (finish) + [Iteration 6](./questions/spark-200-iteration-6.md) + [Iteration 7](./questions/spark-200-iteration-7.md)

### Key Concepts This Week
- [ ] Joins: inner, left, right, full outer, cross, left semi, left anti
- [ ] Join syntax: `df1.join(df2, on=..., how=...)`
- [ ] Combining DataFrames: `union()` (positional), `unionByName()` (by name), `intersect()`, `except()`
- [ ] Reading: `spark.read.format().option().load()`, supported formats (parquet, csv, json, orc, avro)
- [ ] Writing: `df.write.format().mode().partitionBy().save()`
- [ ] Write modes: `overwrite`, `append`, `ignore`, `errorIfExists`
- [ ] Partitioning: `repartition(n, col)` (full shuffle) vs `coalesce(n)` (no shuffle, reduce only)
- [ ] Schemas: `StructType`, `StructField`, `ArrayType`, `MapType`; `schema_of_json()`, `schema_of_csv()`
- [ ] UDFs: `@udf(returnType)`, `spark.udf.register()`, performance cost vs Pandas UDF
- [ ] Pandas UDFs (`@pandas_udf`): scalar, grouped map, grouped aggregate

### Daily Schedule

#### Monday — Joins
- [ ] Read: [topic3-prompt17-joins.md](./topic3-prompt17-joins.md)
- [ ] Focus: all 7 join types, when to use each, join on multiple columns (list syntax)
- [ ] Memorize: `left_semi` keeps only left rows where match exists (no right columns); `left_anti` keeps only non-matching left rows
- [ ] Practice: write one query using each join type on sample DataFrames

#### Tuesday — Combining DataFrames
- [ ] Read: [topic3-prompt18-combining-dataframes.md](./topic3-prompt18-combining-dataframes.md)
- [ ] Focus: `union` aligns by position (order matters!), `unionByName` aligns by column name
- [ ] Understand: `intersect()` and `except()` behave like SQL `INTERSECT` / `EXCEPT DISTINCT`
- [ ] Practice: demonstrate a case where `union` and `unionByName` produce different results

#### Wednesday — Reading and Writing DataFrames
- [ ] Read: [topic3-prompt19-reading-writing-dataframes.md](./topic3-prompt19-reading-writing-dataframes.md)
- [ ] Focus: all write modes; `partitionBy` creates directory hierarchy; `bucketBy` for Hive tables
- [ ] Memorize: default write mode is `errorIfExists`; Parquet is Spark's native default format
- [ ] Practice: read CSV with a specified schema; write as Parquet partitioned by a date column

#### Thursday — Partitioning + Schemas + Data Types
- [ ] Read: [topic3-prompt20-repartition-coalesce.md](./topic3-prompt20-repartition-coalesce.md)
- [ ] Read: [topic3-prompt21-schemas-data-types.md](./topic3-prompt21-schemas-data-types.md)
- [ ] Focus: `repartition` always shuffles; `coalesce` avoids shuffle (cannot increase partition count)
- [ ] Focus: define schemas inline with `StructType([StructField("name", StringType(), True)])`
- [ ] Memorize: `nullable=True` is the default for all `StructField` definitions

#### Friday — UDFs
- [ ] Read: [topic3-prompt22-udfs.md](./topic3-prompt22-udfs.md)
- [ ] Focus: `@udf(returnType)` decorator, `spark.udf.register("name", func, returnType)` for SQL
- [ ] Understand: plain Python UDFs serialize row-by-row through the Python interpreter — expensive
- [ ] Understand: `@pandas_udf` operates on Pandas Series batches — much faster
- [ ] Practice: implement a UDF and the equivalent built-in function; compare `explain()` output

#### Saturday — Hands-On Practice
- [ ] Run: [topic3-prompt17-joins.ipynb](./topic3-prompt17-joins.ipynb)
- [ ] Run: [topic3-prompt18-combining-dataframes.ipynb](./topic3-prompt18-combining-dataframes.ipynb)
- [ ] Run: [topic3-prompt19-reading-writing-dataframes.ipynb](./topic3-prompt19-reading-writing-dataframes.ipynb)
- [ ] Run: [topic3-prompt20-repartition-coalesce.ipynb](./topic3-prompt20-repartition-coalesce.ipynb)
- [ ] Run: [topic3-prompt21-schemas-data-types.ipynb](./topic3-prompt21-schemas-data-types.ipynb)
- [ ] Run: [topic3-prompt22-udfs.ipynb](./topic3-prompt22-udfs.ipynb)
- [ ] Run: [topic3-prompt23-end-to-end-scenarios.ipynb](./topic3-prompt23-end-to-end-scenarios.ipynb)

#### Sunday — Question Bank Practice
- [ ] Complete: [Iteration 5 — remaining questions](./questions/spark-200-iteration-5.md) (all 100 if not done)
- [ ] Attempt: [Iteration 6 — Q1–Q50](./questions/spark-200-iteration-6.md)
- [ ] Attempt: [Iteration 7 — Q41–Q70](./questions/spark-200-iteration-7.md) (DataFrame section)
- [ ] Score yourself on Iter 6: \_\_\_ / 50

---

## Week 6 — Troubleshooting, Tuning & Structured Streaming

> **Exam weight covered:** Topic 4 — Tuning (10%) + Topic 5 — Streaming (10%)  
> **Question bank:** [Iteration 7](./questions/spark-200-iteration-7.md) (finish) + [Iteration 8](./questions/spark-200-iteration-8.md)

### Key Concepts This Week

**Tuning & Debugging (10%)**
- [ ] AQE (Adaptive Query Execution): skew join handling, coalescing shuffle partitions, converting sort-merge to broadcast join
- [ ] `spark.sql.adaptive.enabled` (default `true` in Spark 3.2+)
- [ ] `spark.sql.shuffle.partitions` (default 200) — most common tuning knob
- [ ] Spill to disk: happens when executor memory is insufficient during shuffle
- [ ] OOM errors: driver OOM (collect too much) vs executor OOM (partition too large)
- [ ] Spark UI: understanding stages, tasks, DAG, storage, executors tabs
- [ ] `df.explain()` modes: `simple`, `extended`, `codegen`, `cost`, `formatted`
- [ ] Caching strategy: cache DataFrames reused multiple times; unpersist when done

**Structured Streaming (10%)**
- [ ] Streaming DataFrame creation: `spark.readStream`
- [ ] Output modes: `append` (new rows only), `complete` (full result table), `update` (changed rows)
- [ ] Triggers: `processingTime`, `once`, `availableNow`, `continuous`
- [ ] Checkpointing: required for fault tolerance, tracks offsets and state
- [ ] Watermarking: `withWatermark("timestamp", "10 minutes")` for late data handling
- [ ] Stateful aggregations: windowed aggregations with `window()` function
- [ ] Sinks: console, file, Kafka, Delta, memory (testing only)

### Daily Schedule

#### Monday — Performance Tuning Techniques
- [ ] Read: [topic4-prompt24-performance-tuning.md](./topic4-prompt24-performance-tuning.md)
- [ ] Focus: AQE, broadcast threshold, `shuffle.partitions`, caching, partition sizing
- [ ] Memorize: `spark.sql.shuffle.partitions` default is 200; aim for partition size of 100–200 MB
- [ ] Memorize: AQE is enabled by default in Spark 3.2+

#### Tuesday — Common Errors and Failure Scenarios
- [ ] Read: [topic4-prompt25-common-errors.md](./topic4-prompt25-common-errors.md)
- [ ] Focus: OOM (driver vs executor), task timeout, corrupt record handling, data skew
- [ ] Practice: identify which error message corresponds to which root cause
- [ ] Memorize: `AnalysisException` = schema/logic error caught before execution; `SparkException` = runtime error during execution

#### Wednesday — Debugging Spark Applications
- [ ] Read: [topic4-prompt26-debugging.md](./topic4-prompt26-debugging.md)
- [ ] Focus: Spark UI tabs (Jobs, Stages, Tasks, Storage, Executors, SQL), reading stage DAGs
- [ ] Practice: generate a job in a notebook and navigate the Spark UI; find the slow stage
- [ ] Focus: `explain()` output — find where the bottleneck is in the physical plan

#### Thursday — Structured Streaming Fundamentals
- [ ] Read: [topic5-prompt27-structured-streaming.md](./topic5-prompt27-structured-streaming.md)
- [ ] Focus: `readStream`, `writeStream`, output modes, triggers, checkpointing
- [ ] Memorize: `append` mode cannot be used with aggregations that can change existing rows
- [ ] Practice: write a simple rate-source streaming query with console sink

#### Friday — Stateful Streaming
- [ ] Read: [topic5-prompt28-stateful-streaming.md](./topic5-prompt28-stateful-streaming.md)
- [ ] Focus: `window()` for time-based aggregations, `withWatermark()` for late data
- [ ] Understand: watermark defines how long to wait for late data before finalizing a window
- [ ] Memorize: `withWatermark` must be applied before the aggregation for state cleanup to work

#### Saturday — Hands-On Practice
- [ ] Run: [topic4-prompt24-performance-tuning.ipynb](./topic4-prompt24-performance-tuning.ipynb)
- [ ] Run: [topic4-prompt25-common-errors.ipynb](./topic4-prompt25-common-errors.ipynb)
- [ ] Run: [topic4-prompt26-debugging.ipynb](./topic4-prompt26-debugging.ipynb)
- [ ] Run: [topic5-prompt27-structured-streaming.ipynb](./topic5-prompt27-structured-streaming.ipynb)
- [ ] Run: [topic5-prompt28-stateful-streaming.ipynb](./topic5-prompt28-stateful-streaming.ipynb)

#### Sunday — Question Bank Practice
- [ ] Complete: [Iteration 7 — remaining questions](./questions/spark-200-iteration-7.md)
- [ ] Attempt: [Iteration 8 — Q1–Q80](./questions/spark-200-iteration-8.md) (focus on Q71–Q90 — Tuning & Streaming)
- [ ] Score yourself on Iter 7: \_\_\_ / 100 and Iter 8 partial: \_\_\_ / 80

---

## Week 7 — Spark Connect, Pandas API on Spark & Capstone

> **Exam weight covered:** Topic 6 — Spark Connect (5%) + Topic 7 — Pandas API (5%); plus full end-to-end practice  
> **Question bank:** [Iteration 8](./questions/spark-200-iteration-8.md) (finish) + [Iteration 9](./questions/spark-200-iteration-9.md)

### Key Concepts This Week

**Spark Connect (5%)**
- [ ] What Spark Connect is: thin client architecture, gRPC over port 15002
- [ ] `SparkSession.builder.remote("sc://host:15002").getOrCreate()`
- [ ] `spark.addArtifact(path)` — replaces `sc.addFile()` / `sc.addPyFile()` in Connect
- [ ] `start-connect-server.sh` to launch the Spark Connect server
- [ ] `spark.conf.set()` — config changes propagate to the remote server
- [ ] Limitations: no `SparkContext`, no RDD API, no `sc.broadcast()` (use DataFrame broadcast hint)

**Pandas API on Spark (5%)**
- [ ] `import pyspark.pandas as ps` — the module
- [ ] `ps.DataFrame` vs `pyspark.sql.DataFrame` — nearly identical Pandas syntax, distributed execution
- [ ] Converting: `psdf.to_spark()` → Spark DataFrame; `sdf.to_pandas_on_spark()` → Pandas-on-Spark DataFrame
- [ ] `ps.DataFrame.melt()`, `pivot_table()`, `merge()`, `assign()`, `explode()`
- [ ] Performance: avoid `.to_pandas()` on large DataFrames (collects to driver)
- [ ] `ps.set_option("compute.ops_on_diff_frames", True)` — required for operations across separate DataFrames

### Daily Schedule

#### Monday — Spark Connect
- [ ] Read: [topic6-prompt29-spark-connect.md](./topic6-prompt29-spark-connect.md)
- [ ] Focus: remote connection URL syntax, `addArtifact`, server startup, port default (15002)
- [ ] Memorize: Spark Connect removes the need for the client to have a Spark cluster; only needs Python + the Connect library
- [ ] Understand: `SparkContext` is not accessible through Spark Connect — only `SparkSession`

#### Tuesday — Pandas API on Spark
- [ ] Read: [topic7-prompt30-pandas-api.md](./topic7-prompt30-pandas-api.md)
- [ ] Focus: `ps.read_csv()`, `ps.DataFrame`, `.to_spark()`, `.to_pandas_on_spark()`
- [ ] Focus: `ops_on_diff_frames` option — what it does and when you need it
- [ ] Practice: convert a Pandas workflow to Pandas-on-Spark; confirm distributed execution

#### Wednesday — Practice Exam
- [ ] Read: [topic-prompt31-practice-exam.md](./topic-prompt31-practice-exam.md)
- [ ] Attempt all practice questions without looking up answers
- [ ] Grade yourself and note which topics had the most errors
- [ ] Run: [topic-prompt31-practice-exam.ipynb](./topic-prompt31-practice-exam.ipynb)

#### Thursday — Capstone Project
- [ ] Read: [topic-prompt32-capstone.md](./topic-prompt32-capstone.md)
- [ ] Attempt to complete the capstone with no reference material
- [ ] After completing, compare your solution to the provided solution
- [ ] Note any patterns or functions you struggled with

#### Friday — Full Curriculum Review
- [ ] Review your Topic 1–7 concept checklists from Weeks 1–7 — how many can you check off confidently?
- [ ] For each topic, write one paragraph from memory explaining the key exam concepts
- [ ] Identify your 3 weakest topic areas and schedule extra time in Week 8 for them

#### Saturday — Hands-On Practice
- [ ] Run: [topic6-prompt29-spark-connect.ipynb](./topic6-prompt29-spark-connect.ipynb)
- [ ] Run: [topic7-prompt30-pandas-api.ipynb](./topic7-prompt30-pandas-api.ipynb)
- [ ] Run: [topic-prompt32-capstone.ipynb](./topic-prompt32-capstone.ipynb)
- [ ] Write your own mini-capstone: read a CSV, clean nulls, join two DataFrames, aggregate, write Parquet output

#### Sunday — Question Bank Practice
- [ ] Complete: [Iteration 8 — remaining questions](./questions/spark-200-iteration-8.md) (Q81–Q100)
- [ ] Attempt: [Iteration 9 — Q1–Q100](./questions/spark-200-iteration-9.md) (all 100)
- [ ] Score yourself on Iter 8: \_\_\_ / 100 and Iter 9: \_\_\_ / 100
- [ ] Focus on Q91–Q100 (Spark Connect and Pandas topics) — these are small weight but easy points

---

## Week 8 — Full Review, Mock Exams & Exam Readiness

> **Exam weight covered:** All topics — consolidation and exam simulation  
> **Question bank:** [Iteration 10](./questions/spark-200-iteration-10.md) + Timed mock exam

> **Goal this week:** Score 80%+ on every practice set. Identify and eliminate all remaining gaps. Simulate exam conditions daily.

### Self-Assessment Targets Before Exam

| Topic | Target | My Score |
|-------|--------|----------|
| Architecture (Topics 1) | 80%+ | \_\_\_ % |
| Spark SQL (Topic 2) | 80%+ | \_\_\_ % |
| DataFrame API (Topic 3) | 80%+ | \_\_\_ % |
| Tuning & Debug (Topic 4) | 80%+ | \_\_\_ % |
| Streaming (Topic 5) | 80%+ | \_\_\_ % |
| Spark Connect (Topic 6) | 80%+ | \_\_\_ % |
| Pandas API (Topic 7) | 80%+ | \_\_\_ % |

### Daily Schedule

#### Monday — Architecture & SQL Review
- [ ] Re-read weak areas from Topics 1–2 (use your Week 1–3 concept checklists)
- [ ] Attempt [Iteration 10 — Q1–Q40](./questions/spark-200-iteration-10.md) (Architecture + SQL sections)
- [ ] For every wrong answer, read the corresponding notebook section
- [ ] Practice explaining: "How does a Spark job execute from `SparkSession.sql()` to output?"

#### Tuesday — DataFrame API Review
- [ ] Re-read any Topic 3 notebook where you scored below 70% in Weeks 4–5
- [ ] Attempt [Iteration 10 — Q41–Q70](./questions/spark-200-iteration-10.md) (DataFrame section)
- [ ] Write 3 complex DataFrame pipelines from memory (multi-step transformation + aggregation + write)
- [ ] Practice: null handling, UDFs, join types, and window functions without notes

#### Wednesday — Tuning, Streaming & Niche Topics
- [ ] Re-read Topics 4, 5, 6, 7 notebooks for weak areas
- [ ] Attempt [Iteration 10 — Q71–Q100](./questions/spark-200-iteration-10.md) (Tuning, Streaming, Connect, Pandas)
- [ ] Memorize: AQE behaviors, `shuffle.partitions`, output modes, watermarking, `sc://` URL, `ops_on_diff_frames`

#### Thursday — Timed Mock Exam #1
- [ ] **TIMED MOCK EXAM** — Simulate real exam conditions:
  - Select **45 questions** from [Iteration 3](./questions/spark-200-iteration-3.md) (Q1–Q20), [Iteration 5](./questions/spark-200-iteration-5.md) (Q21–Q45 mapped to all topics proportionally)
  - Set a timer for **90 minutes**
  - No notes, no internet
  - Grade immediately after: \_\_\_ / 45
- [ ] Review every wrong answer — trace the concept to its source notebook

#### Friday — Timed Mock Exam #2
- [ ] **TIMED MOCK EXAM** — Simulate real exam conditions:
  - Select **45 questions** from [Iteration 6](./questions/spark-200-iteration-6.md) and [Iteration 7](./questions/spark-200-iteration-7.md) (mixed across all topics proportionally: ~9 Arch, ~9 SQL, ~14 DataFrame, ~4 Tuning, ~4 Streaming, ~2 Connect, ~3 Pandas)
  - Set a timer for **90 minutes**
  - No notes, no internet
  - Grade immediately after: \_\_\_ / 45
- [ ] Compare scores between Mock 1 and Mock 2 — is performance improving?
- [ ] Final targeted review of any remaining weak concept areas

#### Saturday — Exam-Day Preparation
- [ ] Run: [topic-prompt31-practice-exam.ipynb](./topic-prompt31-practice-exam.ipynb) one final time
- [ ] Write down on one sheet of paper: the top 5 facts per topic you most want to remember
- [ ] Confirm exam booking, ID requirements, quiet room, stable internet
- [ ] Light review only — no heavy studying; rest and confidence-building

#### Sunday — Rest and Light Review
- [ ] Review your one-page summary sheets only (no new material)
- [ ] Re-read the exam overview: [plan-databricksSparkExamOverview.prompt.md](../plan-databricksSparkExamOverview.prompt.md)
- [ ] Confirm you can answer: "What are the 7 topics and their weights?"
- [ ] Confirm you can answer: "What is the exam format (45Q, 90 min, Python, multiple choice)?"
- [ ] Get a good night's sleep

---

## Question Bank Progress Tracker

| Iteration | Week | Score | Notes |
|-----------|------|-------|-------|
| [Iteration 1](./questions/spark-200-iteration-1.md) | Week 1 | \_\_\_ / 100 | |
| [Iteration 2](./questions/spark-200-iteration-2.md) | Week 2 | \_\_\_ / 100 | |
| [Iteration 3](./questions/spark-200-iteration-3.md) | Week 3 | \_\_\_ / 100 | |
| [Iteration 4](./questions/spark-200-iteration-4.md) | Week 4 | \_\_\_ / 100 | |
| [Iteration 5](./questions/spark-200-iteration-5.md) | Weeks 4–5 | \_\_\_ / 100 | |
| [Iteration 6](./questions/spark-200-iteration-6.md) | Week 5 | \_\_\_ / 100 | |
| [Iteration 7](./questions/spark-200-iteration-7.md) | Weeks 5–6 | \_\_\_ / 100 | |
| [Iteration 8](./questions/spark-200-iteration-8.md) | Weeks 6–7 | \_\_\_ / 100 | |
| [Iteration 9](./questions/spark-200-iteration-9.md) | Week 7 | \_\_\_ / 100 | |
| [Iteration 10](./questions/spark-200-iteration-10.md) | Week 8 | \_\_\_ / 100 | |
| **Mock Exam 1** | Week 8 Thu | \_\_\_ / 45 | |
| **Mock Exam 2** | Week 8 Fri | \_\_\_ / 45 | |

---

## Notebook Completion Tracker

| Notebook | Topic | Done |
|----------|-------|------|
| [topic1-prompt1-spark-architecture.md](./topic1-prompt1-spark-architecture.md) | Architecture | - [ ] |
| [topic1-prompt2-execution-deploy-modes.md](./topic1-prompt2-execution-deploy-modes.md) | Architecture | - [ ] |
| [topic1-prompt3-lazy-evaluation.md](./topic1-prompt3-lazy-evaluation.md) | Architecture | - [ ] |
| [topic1-prompt4-shuffling-performance.md](./topic1-prompt4-shuffling-performance.md) | Architecture | - [ ] |
| [topic1-prompt5-broadcasting-optimization.md](./topic1-prompt5-broadcasting-optimization.md) | Architecture | - [ ] |
| [topic1-prompt6-fault-tolerance.md](./topic1-prompt6-fault-tolerance.md) | Architecture | - [ ] |
| [topic1-prompt7-garbage-collection.md](./topic1-prompt7-garbage-collection.md) | Architecture | - [ ] |
| [topic2-prompt8-spark-sql-fundamentals.md](./topic2-prompt8-spark-sql-fundamentals.md) | Spark SQL | - [ ] |
| [topic2-prompt9-builtin-sql-functions.md](./topic2-prompt9-builtin-sql-functions.md) | Spark SQL | - [ ] |
| [topic2-prompt10-window-functions.md](./topic2-prompt10-window-functions.md) | Spark SQL | - [ ] |
| [topic2-prompt11-query-optimization.md](./topic2-prompt11-query-optimization.md) | Spark SQL | - [ ] |
| [topic3-prompt12-dataframe-creation-selection.md](./topic3-prompt12-dataframe-creation-selection.md) | DataFrame API | - [ ] |
| [topic3-prompt13-column-manipulation-expressions.md](./topic3-prompt13-column-manipulation-expressions.md) | DataFrame API | - [ ] |
| [topic3-prompt14-filtering-row-manipulation.md](./topic3-prompt14-filtering-row-manipulation.md) | DataFrame API | - [ ] |
| [topic3-prompt15-aggregations-grouping.md](./topic3-prompt15-aggregations-grouping.md) | DataFrame API | - [ ] |
| [topic3-prompt16-handling-nulls.md](./topic3-prompt16-handling-nulls.md) | DataFrame API | - [ ] |
| [topic3-prompt17-joins.md](./topic3-prompt17-joins.md) | DataFrame API | - [ ] |
| [topic3-prompt18-combining-dataframes.md](./topic3-prompt18-combining-dataframes.md) | DataFrame API | - [ ] |
| [topic3-prompt19-reading-writing-dataframes.md](./topic3-prompt19-reading-writing-dataframes.md) | DataFrame API | - [ ] |
| [topic3-prompt20-repartition-coalesce.md](./topic3-prompt20-repartition-coalesce.md) | DataFrame API | - [ ] |
| [topic3-prompt21-schemas-data-types.md](./topic3-prompt21-schemas-data-types.md) | DataFrame API | - [ ] |
| [topic3-prompt22-udfs.md](./topic3-prompt22-udfs.md) | DataFrame API | - [ ] |
| [topic3-prompt23-end-to-end-scenarios.md](./topic3-prompt23-end-to-end-scenarios.md) | DataFrame API | - [ ] |
| [topic4-prompt24-performance-tuning.md](./topic4-prompt24-performance-tuning.md) | Tuning | - [ ] |
| [topic4-prompt25-common-errors.md](./topic4-prompt25-common-errors.md) | Tuning | - [ ] |
| [topic4-prompt26-debugging.md](./topic4-prompt26-debugging.md) | Tuning | - [ ] |
| [topic5-prompt27-structured-streaming.md](./topic5-prompt27-structured-streaming.md) | Streaming | - [ ] |
| [topic5-prompt28-stateful-streaming.md](./topic5-prompt28-stateful-streaming.md) | Streaming | - [ ] |
| [topic6-prompt29-spark-connect.md](./topic6-prompt29-spark-connect.md) | Spark Connect | - [ ] |
| [topic7-prompt30-pandas-api.md](./topic7-prompt30-pandas-api.md) | Pandas API | - [ ] |
| [topic-prompt31-practice-exam.md](./topic-prompt31-practice-exam.md) | Practice | - [ ] |
| [topic-prompt32-capstone.md](./topic-prompt32-capstone.md) | Capstone | - [ ] |

---

## Key Facts Cheat Sheet

> Memorize these before the exam.

### Architecture
| Fact | Value |
|------|-------|
| `spark.sql.autoBroadcastJoinThreshold` default | 10 MiB |
| `spark.sql.shuffle.partitions` default | 200 |
| AQE enabled by default since | Spark 3.2 |
| Deploy mode — driver location in `cluster` mode | Worker node inside the cluster |
| Deploy mode — driver location in `client` mode | The submitting machine |
| Narrow transformation | No shuffle (e.g., `filter`, `map`, `select`) |
| Wide transformation | Causes shuffle (e.g., `groupBy`, `join`, `orderBy`, `repartition`) |

### DataFrame API
| Fact | Value |
|------|-------|
| `filter()` vs `where()` | Identical — aliases |
| `union()` alignment | By column position |
| `unionByName()` alignment | By column name |
| Default write mode | `errorIfExists` |
| `repartition` vs `coalesce` | `repartition` always shuffles; `coalesce` avoids shuffle (reduce only) |
| `count("col")` null behaviour | Ignores nulls |
| `count("*")` null behaviour | Counts all rows including nulls |
| `left_semi` join | Returns left rows where match exists; no right columns |
| `left_anti` join | Returns left rows where NO match exists |
| `dropDuplicates()` vs `distinct()` | `dropDuplicates(subset=[...])` supports column subset; `distinct()` uses all columns |

### Spark SQL
| Fact | Value |
|------|-------|
| Global temp view prefix | `global_temp.view_name` |
| `RANK()` vs `DENSE_RANK()` | `RANK` skips values on ties; `DENSE_RANK` does not |
| Window `rowsBetween` vs `rangeBetween` | `rowsBetween` uses physical row offset; `rangeBetween` uses value offset |

### Streaming
| Fact | Value |
|------|-------|
| Output modes | `append`, `complete`, `update` |
| `append` with aggregations | Not supported when rows can change |
| Checkpointing | Required for fault tolerance and exactly-once delivery |
| Watermark purpose | Define how long to wait for late data |
| `withWatermark` must be applied | Before the aggregation |

### Spark Connect
| Fact | Value |
|------|-------|
| Remote URL format | `sc://host:port` |
| Default port | 15002 |
| `SparkContext` in Connect | Not available |
| `addArtifact` replaces | `sc.addFile()` and `sc.addPyFile()` |

### Pandas API on Spark
| Fact | Value |
|------|-------|
| Module import | `import pyspark.pandas as ps` |
| Cross-frame operations option | `ps.set_option("compute.ops_on_diff_frames", True)` |
| Convert to Spark DF | `psdf.to_spark()` |
| Convert to Pandas-on-Spark | `sdf.to_pandas_on_spark()` |
| Avoid on large data | `.to_pandas()` — collects all data to driver |
