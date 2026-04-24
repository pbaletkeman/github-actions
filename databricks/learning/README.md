# Databricks / Apache Spark Study Notebooks

> **Exam Prep:** These notebooks were created to help learn and prepare for the **Databricks Certified Associate Developer for Apache Spark** certification. See [`plan-databricksSparkExamOverview.prompt.md`](../plan-databricksSparkExamOverview.prompt.md) for full exam details (45 questions, 90 minutes, $200, Python only, 2-year validity).

- [Databricks / Apache Spark Study Notebooks](#databricks--apache-spark-study-notebooks)
  - [Topic 1 — Spark Core Architecture \& Internals (Prompts 1–7)](#topic-1--spark-core-architecture--internals-prompts-17)
  - [Topic 2 — Spark SQL (Prompts 8–11)](#topic-2--spark-sql-prompts-811)
  - [Topic 3 — DataFrame API (Prompts 12–23)](#topic-3--dataframe-api-prompts-1223)
  - [Topic 4 — Performance Tuning \& Debugging (Prompts 24–26)](#topic-4--performance-tuning--debugging-prompts-2426)
  - [Topic 5 — Structured \& Stateful Streaming (Prompts 27–28)](#topic-5--structured--stateful-streaming-prompts-2728)
  - [Topic 6 — Spark Connect (Prompt 29)](#topic-6--spark-connect-prompt-29)
  - [Topic 7 — Pandas API on Spark (Prompt 30)](#topic-7--pandas-api-on-spark-prompt-30)
  - [Practice \& Capstone (Prompts 31–32)](#practice--capstone-prompts-3132)
  - [Summary](#summary)


Jupyter notebooks for studying Apache Spark and Databricks concepts. The series covers 32 prompts organized into 7 topic areas, progressing from core architecture through streaming, Spark Connect, the Pandas API on Spark, and a final practice exam and capstone project.

All notebooks are self-contained with explanatory markdown cells and runnable PySpark/Python code cells.

---

## Topic 1 — Spark Core Architecture & Internals (Prompts 1–7)

| Notebook | Study Guide | Title |
|----------|-------------|-------|
| [topic1-prompt1-spark-architecture.ipynb](topic1-prompt1-spark-architecture.ipynb) | [📄 MD](topic1-prompt1-spark-architecture.md) | Spark Architecture Fundamentals |
| [topic1-prompt2-execution-deploy-modes.ipynb](topic1-prompt2-execution-deploy-modes.ipynb) | [📄 MD](topic1-prompt2-execution-deploy-modes.md) | Execution Modes and Deploy Modes Deep Dive |
| [topic1-prompt3-lazy-evaluation.ipynb](topic1-prompt3-lazy-evaluation.ipynb) | [📄 MD](topic1-prompt3-lazy-evaluation.md) | Lazy Evaluation & Transformation vs Action |
| [topic1-prompt4-shuffling-performance.ipynb](topic1-prompt4-shuffling-performance.ipynb) | [📄 MD](topic1-prompt4-shuffling-performance.md) | Shuffling and Performance Impact |
| [topic1-prompt5-broadcasting-optimization.ipynb](topic1-prompt5-broadcasting-optimization.ipynb) | [📄 MD](topic1-prompt5-broadcasting-optimization.md) | Broadcasting & Optimization |
| [topic1-prompt6-fault-tolerance.ipynb](topic1-prompt6-fault-tolerance.ipynb) | [📄 MD](topic1-prompt6-fault-tolerance.md) | Fault Tolerance & Resilience |
| [topic1-prompt7-garbage-collection.ipynb](topic1-prompt7-garbage-collection.ipynb) | [📄 MD](topic1-prompt7-garbage-collection.md) | Garbage Collection in Spark |

---

## Topic 2 — Spark SQL (Prompts 8–11)

| Notebook | Study Guide | Title |
|----------|-------------|-------|
| [topic2-prompt8-spark-sql-fundamentals.ipynb](topic2-prompt8-spark-sql-fundamentals.ipynb) | [📄 MD](topic2-prompt8-spark-sql-fundamentals.md) | Spark SQL Fundamentals — Querying DataFrames |
| [topic2-prompt9-builtin-sql-functions.ipynb](topic2-prompt9-builtin-sql-functions.ipynb) | [📄 MD](topic2-prompt9-builtin-sql-functions.md) | Built-in Spark SQL Functions Reference |
| [topic2-prompt10-window-functions.ipynb](topic2-prompt10-window-functions.ipynb) | [📄 MD](topic2-prompt10-window-functions.md) | Window Functions in Spark SQL and DataFrame API |
| [topic2-prompt11-query-optimization.ipynb](topic2-prompt11-query-optimization.ipynb) | [📄 MD](topic2-prompt11-query-optimization.md) | Spark SQL Query Optimization and Execution Plans |

---

## Topic 3 — DataFrame API (Prompts 12–23)

| Notebook | Study Guide | Title |
|----------|-------------|-------|
| [topic3-prompt12-dataframe-creation-selection.ipynb](topic3-prompt12-dataframe-creation-selection.ipynb) | [📄 MD](topic3-prompt12-dataframe-creation-selection.md) | DataFrame Creation, Column Selection and Renaming |
| [topic3-prompt13-column-manipulation-expressions.ipynb](topic3-prompt13-column-manipulation-expressions.ipynb) | [📄 MD](topic3-prompt13-column-manipulation-expressions.md) | Column Manipulation and Expressions |
| [topic3-prompt14-filtering-row-manipulation.ipynb](topic3-prompt14-filtering-row-manipulation.ipynb) | [📄 MD](topic3-prompt14-filtering-row-manipulation.md) | Data Filtering and Row Manipulation |
| [topic3-prompt15-aggregations-grouping.ipynb](topic3-prompt15-aggregations-grouping.ipynb) | [📄 MD](topic3-prompt15-aggregations-grouping.md) | Aggregations and Grouping |
| [topic3-prompt16-handling-nulls.ipynb](topic3-prompt16-handling-nulls.ipynb) | [📄 MD](topic3-prompt16-handling-nulls.md) | Handling Missing Data (NULLs) |
| [topic3-prompt17-joins.ipynb](topic3-prompt17-joins.ipynb) | [📄 MD](topic3-prompt17-joins.md) | Joins |
| [topic3-prompt18-combining-dataframes.ipynb](topic3-prompt18-combining-dataframes.ipynb) | [📄 MD](topic3-prompt18-combining-dataframes.md) | Combining DataFrames — Union, Intersect, Except |
| [topic3-prompt19-reading-writing-dataframes.ipynb](topic3-prompt19-reading-writing-dataframes.ipynb) | [📄 MD](topic3-prompt19-reading-writing-dataframes.md) | Reading and Writing DataFrames |
| [topic3-prompt20-repartition-coalesce.ipynb](topic3-prompt20-repartition-coalesce.ipynb) | [📄 MD](topic3-prompt20-repartition-coalesce.md) | DataFrame Partitioning — Repartition and Coalesce |
| [topic3-prompt21-schemas-data-types.ipynb](topic3-prompt21-schemas-data-types.ipynb) | [📄 MD](topic3-prompt21-schemas-data-types.md) | Schemas and Data Types |
| [topic3-prompt22-udfs.ipynb](topic3-prompt22-udfs.ipynb) | [📄 MD](topic3-prompt22-udfs.md) | User Defined Functions (UDFs) |
| [topic3-prompt23-end-to-end-scenarios.ipynb](topic3-prompt23-end-to-end-scenarios.ipynb) | [📄 MD](topic3-prompt23-end-to-end-scenarios.md) | Complex End-to-End Data Manipulation Scenarios |

---

## Topic 4 — Performance Tuning & Debugging (Prompts 24–26)

| Notebook | Study Guide | Title |
|----------|-------------|-------|
| [topic4-prompt24-performance-tuning.ipynb](topic4-prompt24-performance-tuning.ipynb) | [📄 MD](topic4-prompt24-performance-tuning.md) | Performance Tuning Techniques |
| [topic4-prompt25-common-errors.ipynb](topic4-prompt25-common-errors.ipynb) | [📄 MD](topic4-prompt25-common-errors.md) | Common Spark Errors and Failure Scenarios |
| [topic4-prompt26-debugging.ipynb](topic4-prompt26-debugging.ipynb) | [📄 MD](topic4-prompt26-debugging.md) | Debugging Spark Applications |

---

## Topic 5 — Structured & Stateful Streaming (Prompts 27–28)

| Notebook | Study Guide | Title |
|----------|-------------|-------|
| [topic5-prompt27-structured-streaming.ipynb](topic5-prompt27-structured-streaming.ipynb) | [📄 MD](topic5-prompt27-structured-streaming.md) | Structured Streaming Fundamentals |
| [topic5-prompt28-stateful-streaming.ipynb](topic5-prompt28-stateful-streaming.ipynb) | [📄 MD](topic5-prompt28-stateful-streaming.md) | Stateful Streaming — Windows and Watermarking |

---

## Topic 6 — Spark Connect (Prompt 29)

| Notebook | Study Guide | Title |
|----------|-------------|-------|
| [topic6-prompt29-spark-connect.ipynb](topic6-prompt29-spark-connect.ipynb) | [📄 MD](topic6-prompt29-spark-connect.md) | Spark Connect — Architecture and Usage |

---

## Topic 7 — Pandas API on Spark (Prompt 30)

| Notebook | Study Guide | Title |
|----------|-------------|-------|
| [topic7-prompt30-pandas-api.ipynb](topic7-prompt30-pandas-api.ipynb) | [📄 MD](topic7-prompt30-pandas-api.md) | Pandas API on Apache Spark (pyspark.pandas) |

---

## Practice & Capstone (Prompts 31–32)

| Notebook | Study Guide | Title |
|----------|-------------|-------|
| [topic-prompt31-practice-exam.ipynb](topic-prompt31-practice-exam.ipynb) | [📄 MD](topic-prompt31-practice-exam.md) | Practice Exam Questions |
| [topic-prompt32-capstone.ipynb](topic-prompt32-capstone.ipynb) | [📄 MD](topic-prompt32-capstone.md) | Hands-On Capstone Project |

---

## Summary

| Stat | Value |
|------|-------|
| Total notebooks | 32 |
| Total size | ~1.1 MB |
| Topics covered | 7 |
| Practice notebooks | 2 (exam + capstone) |
