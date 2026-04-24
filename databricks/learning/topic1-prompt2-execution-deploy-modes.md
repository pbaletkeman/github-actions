# Execution and Deploy Modes

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 1 — Spark Architecture & Internals (20%)

---

## Overview

Understanding how to run a Spark application means knowing two separate choices:

1. **Execution mode** — where Spark runs (which cluster manager it targets)
2. **Deploy mode** — where the **Driver** process runs relative to the cluster

---

## Execution Modes

| Mode | `--master` Flag | Description |
|------|----------------|-------------|
| **Local** | `local` | Single JVM; single thread; no parallelism |
| **Local[N]** | `local[4]` | Single JVM; N worker threads (one per core) |
| **Local[*]** | `local[*]` | Single JVM; one thread per logical CPU core |
| **Standalone** | `spark://host:7077` | Spark's own built-in cluster manager |
| **YARN** | `yarn` | Hadoop YARN cluster manager |
| **Kubernetes** | `k8s://https://host:port` | Kubernetes cluster manager |
| **Mesos** | `mesos://host:5050` | Apache Mesos (legacy) |

**Local mode** is used for development and testing. The Driver and a single Executor run in the same JVM process on your machine.

---

## Deploy Modes

Deploy mode only applies to **cluster** execution (Standalone, YARN, K8s). It controls where the Driver JVM runs.

| Deploy Mode | `--deploy-mode` Flag | Driver Location | When to Use |
|-------------|---------------------|-----------------|-------------|
| **Client** | `--deploy-mode client` (default) | On the **submitting machine** (outside the cluster) | Interactive tools, notebooks, debugging |
| **Cluster** | `--deploy-mode cluster` | On a **worker node inside the cluster** | Production batch jobs; submitting machine can disconnect |

### Key Difference

- In **client** mode, if the submitting machine dies, the Driver dies and the job fails.
- In **cluster** mode, the cluster manager runs the Driver so the submitting machine can disconnect safely.
- Databricks notebooks always run in **client mode** (Driver is the notebook host).

---

## `spark-submit` Reference

```bash
spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --executor-memory 4g \
  --executor-cores 2 \
  --num-executors 10 \
  --driver-memory 2g \
  --conf spark.sql.shuffle.partitions=100 \
  --py-files utils.zip \
  --files config.json \
  --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.0 \
  my_job.py
```

### Key Flags

| Flag | Purpose |
|------|---------|
| `--master` | Cluster manager URI |
| `--deploy-mode` | `client` or `cluster` |
| `--executor-memory` | Memory per executor JVM (e.g., `4g`) |
| `--executor-cores` | CPU cores per executor |
| `--num-executors` | Number of executor processes (YARN only) |
| `--driver-memory` | Memory for the Driver process |
| `--conf key=value` | Override any Spark configuration property |
| `--py-files` | Additional Python files/zips distributed to executors |
| `--files` | Additional files sent to each executor's working directory |
| `--packages` | Maven coordinates of JARs to download (e.g., Kafka connector) |

---

## Local Mode Deep Dive

```python
# local — 1 thread
spark = SparkSession.builder.master("local").getOrCreate()

# local[4] — 4 threads (useful for testing parallelism)
spark = SparkSession.builder.master("local[4]").getOrCreate()

# local[*] — all available CPU cores
spark = SparkSession.builder.master("local[*]").getOrCreate()
```

In `local[N]` mode, the Driver acts as both the Driver and a single Executor. Partitions are processed by N concurrent threads, not separate JVM processes.

---

## Exam Checklist

- [ ] Know the five execution mode `--master` values and when to use each
- [ ] Understand client deploy mode (Driver on submitting machine) vs cluster mode (Driver on worker)
- [ ] Know which mode notebooks and interactive tools use (client)
- [ ] Memorise the key `spark-submit` flags: `--master`, `--deploy-mode`, `--executor-memory`, `--executor-cores`, `--driver-memory`, `--conf`
- [ ] Understand that `local[*]` uses all cores on the local machine

---

[⬅️ topic1-prompt1-spark-architecture.md](topic1-prompt1-spark-architecture.md) | **2 / 32** | [Next ➡️ topic1-prompt3-lazy-evaluation.md](topic1-prompt3-lazy-evaluation.md)
