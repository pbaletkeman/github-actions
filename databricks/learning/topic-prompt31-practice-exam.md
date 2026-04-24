# Practice Exam Questions

> **Databricks Certified Associate Developer for Apache Spark**
> All 7 Topic Areas — Full-Length Practice

---

## Instructions

- 20 questions covering all 7 exam topics
- Choose the single best answer for each question
- Answers and detailed explanations are provided after all questions

---

## Questions

**Q1.** Which component of the Spark architecture is responsible for requesting resources from the cluster manager?

A) Executor
B) Driver
C) SparkContext
D) Task Scheduler

---

**Q2.** A Spark job is submitted in cluster mode to a YARN cluster. Where does the driver process run?

A) On the machine where the `spark-submit` command was executed
B) On one of the worker nodes managed by YARN
C) On the Spark master node
D) In the same JVM as the Spark client

---

**Q3.** Which of the following best describes Spark's lazy evaluation?

A) Spark delays writing output files until explicitly triggered
B) Transformations are not executed until an action triggers execution
C) Spark defers memory allocation until data is accessed
D) Catalyst optimizer delays query planning until runtime

---

**Q4.** Which of the following is NOT a Spark action?

A) `df.count()`
B) `df.collect()`
C) `df.printSchema()`
D) `df.take(5)`

---

**Q5.** What is the return type of `spark.sql("SELECT * FROM my_table")`?

A) Row
B) List
C) DataFrame
D) RDD

---

**Q6.** Which SQL function would you use to number rows within each partition of a result set without gaps?

A) `rank()`
B) `row_number()`
C) `dense_rank()`
D) `ntile()`

---

**Q7.** What is the prefix required to query a global temporary view?

A) `global.`
B) `global_temp.`
C) `temp.`
D) No prefix required

---

**Q8.** Which of the following describes the behaviour of `rank()` compared to `dense_rank()`?

A) `rank()` never has gaps; `dense_rank()` has gaps for ties
B) `rank()` has gaps for ties; `dense_rank()` never has gaps
C) Both functions produce the same result
D) `rank()` is deterministic; `dense_rank()` is not

---

**Q9.** You call `df.cache()` on a DataFrame. What is the default storage level?

A) MEMORY_ONLY
B) MEMORY_ONLY_SER
C) MEMORY_AND_DISK
D) DISK_ONLY

---

**Q10.** You have a DataFrame with a column `score` of type StringType. You want to compute the average score. Which transformation should you apply first?

A) `df.withColumn('score', col('score').cast(DoubleType()))`
B) `df.withColumn('score', col('score').astype('double'))`
C) Either A or B
D) No transformation needed — Spark coerces automatically

---

**Q11.** What is the default value of `spark.sql.shuffle.partitions`?

A) 20
B) 100
C) 200
D) 400

---

**Q12.** You are writing a UDF that processes a column of strings. You forget to specify `returnType`. What happens?

A) Spark raises an error at definition time
B) Spark infers the return type from the function's first call
C) Spark defaults to `StringType()` silently
D) Spark defaults to `AnyType()` silently

---

**Q13.** Which of the following join strategies performs a shuffle on both sides of the join?

A) BroadcastHashJoin
B) SortMergeJoin
C) BroadcastNestedLoopJoin
D) ShuffleHashJoin (single side only)

---

**Q14.** You call `df.explain()` and see `Exchange hashpartitioning(key, 200)` in the physical plan. What does this indicate?

A) Predicate pushdown is working correctly
B) A shuffle is occurring on the `key` column
C) Broadcast join was selected
D) The DataFrame is being cached

---

**Q15.** You are running a streaming query and see that one task regularly takes 100× longer than others in the same stage. What is the most likely cause?

A) Executor memory is too low
B) Data skew in the partitioned data
C) Missing checkpoint directory
D) Incorrect output mode

---

**Q16.** An executor throws `java.lang.OutOfMemoryError: Java heap space`. Which of the following is NOT an appropriate fix?

A) Increase `spark.executor.memory`
B) Use `MEMORY_AND_DISK` persistence instead of `MEMORY_ONLY`
C) Call `df.collect()` on the executor
D) Repartition the DataFrame to reduce data per partition

---

**Q17.** You have a streaming query with a windowed aggregation. You want results to be emitted only after the window has fully closed (using a watermark). Which output mode should you use?

A) `complete`
B) `update`
C) `append`
D) Any mode works equally

---

**Q18.** Which of the following is REQUIRED when using a file-based streaming source?

A) Trigger interval
B) Explicit schema definition
C) Checkpoint location
D) Output mode

---

**Q19.** Which of the following APIs is NOT available via Spark Connect?

A) DataFrame API
B) Spark SQL
C) RDD API
D) `spark.range()`

---

**Q20.** Which method converts a PySpark DataFrame to a pyspark.pandas DataFrame?

A) `sdf.to_pandas()`
B) `ps.from_spark(sdf)`
C) `sdf.pandas_api()`
D) `ps.DataFrame(sdf)`

---

## Answers and Explanations

| Q | Answer | Explanation |
|---|--------|-------------|
| 1 | B | The Driver requests resources from the cluster manager via the SparkContext. |
| 2 | B | In cluster mode, the driver runs on a worker node managed by YARN, not on the submitting machine (that's client mode). |
| 3 | B | Lazy evaluation means transformations build a logical plan but are only executed when an action is called. |
| 4 | C | `printSchema()` returns None and does not trigger a Spark job — it only displays schema metadata. `count()`, `collect()`, and `take()` are all actions. |
| 5 | C | `spark.sql()` always returns a DataFrame, regardless of the SQL statement. |
| 6 | C | `dense_rank()` numbers rows without gaps. `rank()` skips numbers after ties. |
| 7 | B | Global temporary views are accessed with the `global_temp.` prefix (e.g., `SELECT * FROM global_temp.my_view`). |
| 8 | B | `rank()` leaves gaps (e.g., 1, 2, 2, 4); `dense_rank()` does not (e.g., 1, 2, 2, 3). |
| 9 | C | `df.cache()` uses `MEMORY_AND_DISK` by default. `MEMORY_ONLY` requires `persist(StorageLevel.MEMORY_ONLY)`. |
| 10 | C | Both `.cast(DoubleType())` and `.cast('double')` work; you must explicitly cast before numeric aggregation. |
| 11 | C | Default shuffle partitions is 200. Tune to 2–4× total executor cores for your cluster. |
| 12 | C | Omitting `returnType` silently defaults to `StringType()`. This causes incorrect results for non-string returns with no runtime error. |
| 13 | B | SortMergeJoin shuffles and sorts both sides. BroadcastHashJoin only ships the small side. |
| 14 | B | `Exchange hashpartitioning` is Spark's shuffle operator — data is repartitioned by hash of the key across executors. |
| 15 | B | Straggler tasks with one taking 100× longer indicates data skew — one partition has far more data than others. |
| 16 | C | `collect()` is called on the **driver**, not executors. Calling collect from within executor code is not possible in the standard API. |
| 17 | C | `append` mode for windowed agg with watermark emits results only after the watermark passes the window end — window is finalised. |
| 18 | B | **Explicit schema is required** for file streaming sources. Spark cannot infer schema from a streaming directory. |
| 19 | C | The **RDD API is not supported** via Spark Connect. SparkContext is also not available. |
| 20 | C | `sdf.pandas_api()` converts a PySpark DataFrame to a pyspark.pandas DataFrame. `sdf.to_pandas()` collects to a regular pandas DataFrame on the driver. |

---

## Topic Coverage

| Topic | Questions | Pass Priority |
|-------|-----------|--------------|
| Topic 1 — Architecture (17%) | Q1, Q2, Q3 | High |
| Topic 2 — Spark SQL (22%) | Q5, Q6, Q7, Q8 | High |
| Topic 3 — DataFrame API (30%) | Q4, Q9, Q10, Q12, Q13 | Highest |
| Topic 4 — Optimisation (12%) | Q11, Q14, Q15, Q16 | High |
| Topic 5 — Streaming (12%) | Q17, Q18 | High |
| Topic 6 — Spark Connect (6%) | Q19 | Medium |
| Topic 7 — Pandas API (6%) | Q20 | Medium |

---

[⬅️ topic7-prompt30-pandas-api.md](topic7-prompt30-pandas-api.md) | **31 / 32** | [Next ➡️ topic-prompt32-capstone.md](topic-prompt32-capstone.md)
