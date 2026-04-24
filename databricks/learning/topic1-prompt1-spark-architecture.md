# Spark Architecture Fundamentals

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 1 — Spark Architecture & Internals (20%)

---

## Overview

Apache Spark is a distributed computing engine. Every Spark application involves three layers: the **Driver**, one or more **Executors**, and a **Cluster Manager** that orchestrates resource allocation.

---

## Driver vs. Executor

| Component | Role |
|-----------|------|
| **Driver** | Hosts the `SparkContext`/`SparkSession`; converts user code into a DAG; schedules Tasks on Executors via the TaskScheduler; collects results |
| **Executor** | JVM process on a worker node; executes Tasks; stores cached data (RDD/DataFrame blocks) |
| **Cluster Manager** | Allocates resources (containers/cores/memory) — YARN, Kubernetes, Standalone, or Mesos |

The **Driver** never processes data partitions — that is exclusively the **Executor's** job.

---

## Physical Execution Hierarchy

```
Application
└── Job  (triggered by one action, e.g., collect(), count(), write)
    └── Stage  (a set of Tasks with no shuffle boundary between them)
        └── Task  (processes one partition)
```

- One **Action** → one **Job**
- A **Stage** boundary is drawn wherever a **wide transformation** (shuffle) occurs
- Each **Task** maps 1-to-1 with one data **partition**

---

## DAGScheduler and TaskScheduler

| Scheduler | Responsibility |
|-----------|----------------|
| **DAGScheduler** | Converts the logical plan DAG into physical Stages; handles stage-level fault tolerance by re-submitting failed stages |
| **TaskScheduler** | Takes the Stages from DAGScheduler and schedules individual Tasks on available Executors |

---

## SparkContext vs SparkSession

| API | Introduced | Purpose |
|-----|-----------|---------|
| `SparkContext` (`sc`) | Spark 1.x | Low-level entry point; creates RDDs, accumulators, broadcast variables |
| `SparkSession` (`spark`) | Spark 2.0 | Unified entry point; wraps SparkContext; provides DataFrame/Dataset API and `spark.sql()` |

```python
from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("MyApp") \
    .master("local[*]") \
    .getOrCreate()

sc = spark.sparkContext   # access SparkContext from SparkSession
```

**Exam tip:** In modern Spark code always create a `SparkSession`. A `SparkContext` is created automatically inside it.

---

## Narrow vs Wide Transformations

| Type | Definition | Example Operations | Shuffle? |
|------|-----------|-------------------|---------|
| **Narrow** | Each output partition depends on at most one input partition | `filter()`, `select()`, `withColumn()`, `map()`, `union()` | No |
| **Wide** | Each output partition depends on multiple input partitions | `groupBy()`, `join()` (SortMergeJoin), `distinct()`, `orderBy()`, `repartition()` | **Yes** |

Wide transformations introduce **stage boundaries** in the DAG and cause a **shuffle** — data moves between executor nodes over the network.

---

## Jobs → Stages → Tasks (Key Exam Facts)

- **`count()`**, **`collect()`**, **`show()`**, **`write`** — all trigger a Job
- Each shuffle boundary creates a new Stage
- A Stage with a shuffle-write step precedes a Stage with a shuffle-read step
- The number of **Tasks** in a Stage equals the number of **partitions** processed

---

## Key Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| `spark.executor.cores` | cluster-dependent | Cores per executor |
| `spark.executor.memory` | `1g` | Executor JVM heap size |
| `spark.driver.memory` | `1g` | Driver JVM heap size |
| `spark.sql.shuffle.partitions` | `200` | Partition count after a shuffle |
| `spark.default.parallelism` | cluster-dependent | Default parallelism for RDD operations |

---

## Exam Checklist

- [ ] Know the Driver's responsibilities vs the Executor's
- [ ] Know the Job → Stage → Task hierarchy
- [ ] Understand that each Task processes exactly one partition
- [ ] Distinguish narrow (no shuffle) vs wide (shuffle) transformations
- [ ] Know the role of DAGScheduler vs TaskScheduler
- [ ] Know how to create a `SparkSession` and access `SparkContext` from it

---

[⬅️ README](README.md) | **1 / 32** | [Next ➡️ topic1-prompt2-execution-deploy-modes.md](topic1-prompt2-execution-deploy-modes.md)
