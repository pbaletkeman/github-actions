# Broadcasting and Optimization

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 1 — Spark Architecture & Internals (20%)

---

## Overview

This notebook covers two distinct uses of "broadcast" in Spark:

1. **Broadcast Join (BroadcastHashJoin)** — a join strategy that avoids shuffling by distributing a small table to all executors
2. **Broadcast Variables** — a mechanism to efficiently distribute a read-only value (e.g., a lookup dictionary) to all executors without re-serialising per task

---

## Broadcast Join (BroadcastHashJoin)

### Default Threshold

```
spark.sql.autoBroadcastJoinThreshold = 10 MB (default)
```

When one side of a join is smaller than this threshold, Spark **automatically** uses a BroadcastHashJoin instead of a SortMergeJoin.

### How It Works

```
SortMergeJoin (both sides shuffled):       BroadcastHashJoin (small side broadcast):
  Table A: shuffle → sort                    Table A: NO shuffle
  Table B: shuffle → sort                    Table B (small): broadcast to every executor
  → merge on each executor                   → hash lookup on each executor (no sort)
```

The large table (Table A) stays in place. Each executor receives a full copy of the small table and performs a local hash lookup.

### Using the Broadcast Hint

```python
from pyspark.sql.functions import broadcast

# Python DataFrame API
result = large_df.join(broadcast(small_df), on='id', how='inner')

# SQL hint
result = spark.sql("""
    SELECT /*+ BROADCAST(small_table) */ *
    FROM large_table
    JOIN small_table ON large_table.id = small_table.id
""")
```

### Forcing/Disabling Broadcast

```python
# Force broadcast even if table is larger than threshold
spark.conf.set('spark.sql.autoBroadcastJoinThreshold', '50MB')

# Disable auto-broadcast entirely
spark.conf.set('spark.sql.autoBroadcastJoinThreshold', '-1')
```

### Broadcast Join Limitations

- **Cannot** be used for **FULL OUTER** joins
- The small table must fit in executor memory
- `broadcast()` hint is ignored if the table is too large (Spark falls back to SortMergeJoin)

---

## Broadcast Variables

Broadcast variables are a different feature — they distribute a Python object (a lookup dict, a list, a model) to all executors **once**, rather than re-serialising it per task (which would happen if you captured it in a UDF closure directly).

```python
# Create a broadcast variable from a Python dict
lookup_dict = {'NY': 'New York', 'CA': 'California', 'TX': 'Texas'}
bc = spark.sparkContext.broadcast(lookup_dict)

# Use inside a UDF — access via .value
from pyspark.sql.functions import udf
from pyspark.sql.types import StringType

@udf(returnType=StringType())
def expand_state(code):
    return bc.value.get(code, 'Unknown')   # bc.value accesses the broadcasted object

df.withColumn('state_full', expand_state('state_code'))
```

### Lifecycle

```python
bc.unpersist()   # removes cached copies from executors (will re-fetch if needed)
bc.destroy()     # permanently removes from all executors; cannot be used after this
```

### Broadcast Variable vs Broadcast Join

| | Broadcast Variable | Broadcast Join |
|---|---|---|
| Purpose | Distribute read-only data to executors | Avoid shuffle in a DataFrame join |
| API | `sc.broadcast(obj)` → `bc.value` | `broadcast(df)` / SQL hint / auto-threshold |
| Use case | Lookup dicts in UDFs, shared config | Joining large + small table |

---

## Performance Summary

| Strategy | Benefit | How to Apply |
|----------|---------|-------------|
| Broadcast Join | Eliminates shuffle for small-table joins | Use `broadcast()` hint or tune threshold |
| Broadcast Variable | Avoids per-task serialisation of shared data | `sc.broadcast()` |
| AQE | Runtime plan adjustment | `spark.sql.adaptive.enabled=true` |

---

## Exam Checklist

- [ ] Know the default broadcast join threshold (10 MB)
- [ ] Know how to apply the `broadcast()` hint in Python and SQL
- [ ] Know that broadcast join eliminates the shuffle for the small table side
- [ ] Know that FULL OUTER joins cannot use BroadcastHashJoin
- [ ] Know that broadcast variables use `.value` to access the broadcasted object
- [ ] Distinguish Broadcast Join (join strategy) from Broadcast Variable (shared data mechanism)

---

[⬅️ topic1-prompt4-shuffling-performance.md](topic1-prompt4-shuffling-performance.md) | **5 / 32** | [Next ➡️ topic1-prompt6-fault-tolerance.md](topic1-prompt6-fault-tolerance.md)
- [ ] Know how to disable auto-broadcast: threshold = `-1`
