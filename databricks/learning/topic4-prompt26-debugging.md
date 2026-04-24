# Debugging Spark Applications

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 4 — Optimisation and Troubleshooting (12%)

---

## Overview

Effective Spark debugging requires knowing how to access the Spark UI in different deployment modes, how to read and interpret execution plans from `explain()`, and how to diagnose slow queries by analysing physical plan nodes.

---

## Accessing the Spark UI

| Mode | URL / Method |
|------|-------------|
| Local mode (running) | `http://localhost:4040` |
| YARN cluster (running) | Link shown in `yarn application -status <app_id>` or YARN ResourceManager UI |
| After job completes | History Server: `http://localhost:18080` (requires `spark.eventLog.enabled = true`) |
| Databricks | Built into workspace UI under **Clusters → Spark UI** |

---

## `explain()` Modes

```python
df.explain()                   # Simple — physical plan only (default)
df.explain('simple')           # Same as default
df.explain('extended')         # Unresolved → Resolved → Optimised Logical → Physical
df.explain('formatted')        # Structured, human-readable physical plan (Spark 3.0+)
df.explain('cost')             # Physical plan + estimated row counts / sizes
df.explain('codegen')          # Generated Java bytecode for WholeStageCodegen
```

**Reading a physical plan:** Execution flows **bottom to top** — the bottom node is the data source; the top node is the final output.

---

## Key Plan Nodes and What They Mean

```
== Physical Plan ==
*(3) SortMergeJoin [customer_id#1], [id#15], Inner
:- *(1) Sort [customer_id#1 ASC NULLS FIRST], false, 0
:  +- Exchange hashpartitioning(customer_id#1, 200), ENSURE_REQUIREMENTS
:     +- *(1) Filter (isnotnull(status#3) AND (status#3 = active))
:        +- *(1) FileScan parquet [customer_id#1,status#3,amount#4]
:           PushedFilters: [IsNotNull(status), EqualTo(status,active)]
+- *(2) Sort [id#15 ASC NULLS FIRST], false, 0
   +- Exchange hashpartitioning(id#15, 200), ENSURE_REQUIREMENTS
      +- BroadcastExchange HashedRelation
         +- *(2) FileScan parquet [id#15,name#16] ...
```

### Node Reference

| Node | Meaning | Performance Signal |
|------|---------|-------------------|
| `FileScan` with `PushedFilters: [...]` | Filter pushed into data source reader | ✅ Predicate pushdown working |
| `FileScan` with `PushedFilters: []` or absent | Filter NOT pushed — reads all data | ⚠️ Suboptimal — check filter placement |
| `Project [col1, col2]` | Column pruning — only needed cols read | ✅ Column pruning working |
| `Filter (condition)` ABOVE `FileScan` | Filter applied after reading all data | ⚠️ Push filter before FileScan |
| `BroadcastHashJoin` | Small table broadcast; no shuffle for large side | ✅ Efficient join |
| `SortMergeJoin` | Both sides shuffled + sorted | ⚠️ Expensive if one side could be broadcast |
| `Exchange hashpartitioning(...)` | Shuffle occurring | ⚠️ Consider if avoidable |
| `Exchange rangepartitioning(...)` | Sort-based shuffle | ⚠️ Caused by global sort |
| `Sort [...]` | Sort step | Neutral — note whether it's global or local |
| `WholeStageCodegen (n)` | Multiple operators fused into JVM bytecode | ✅ Efficient |
| `HashAggregate` | Partial or final aggregation | Neutral |

---

## Diagnosing Common Query Problems via the Plan

### Missing Predicate Pushdown

**Symptom:** `FileScan` has no `PushedFilters`, but a `Filter` node appears above it.

**Cause:** Filter may be on a computed column or after a UDF that Spark can't push down.

**Fix:** Rewrite the filter to use the source column directly; apply filter before transformations.

### SortMergeJoin Instead of BroadcastHashJoin

**Symptom:** `SortMergeJoin` when one table is known to be small.

**Cause:** Spark can't determine table size statically, or size exceeds `autoBroadcastJoinThreshold`.

**Fix:**
```python
# Force broadcast
from pyspark.sql.functions import broadcast
large_df.join(broadcast(small_df), on='id')

# Or increase threshold
spark.conf.set('spark.sql.autoBroadcastJoinThreshold', '50MB')

# Or collect stats for CBO
spark.sql('ANALYZE TABLE my_small_table COMPUTE STATISTICS')
```

### Excessive Shuffle Partitions

**Symptom:** `Exchange` node with 200 output partitions, but dataset is small (200 tiny tasks).

**Fix:**
```python
spark.conf.set('spark.sql.shuffle.partitions', '20')  # tune to data size
# Or enable AQE (auto-coalesces small partitions)
```

### Data Skew

**Symptom:** Tasks tab shows max task time is 10–100× the median task time in the same stage.

**Fix:** Enable AQE skew handling; or manually salt the skewed key.

---

## Logging

```python
# Access Spark's internal log
import logging
logging.getLogger('pyspark').setLevel(logging.ERROR)

# On YARN/Databricks: check executor logs via Spark UI → Executors tab → click stdout/stderr
```

---

## `df.explain()` Workflow

```
1. Write a query
2. Call df.explain('formatted')
3. Identify:
   - Is predicate pushdown happening? (PushedFilters present?)
   - Is the join using BroadcastHashJoin? (or is it SortMergeJoin?)
   - Are there unnecessary Exchange nodes (shuffles)?
   - Are there WholeStageCodegen operators? (good)
4. Apply targeted fix (broadcast hint, filter reorder, config change)
5. Re-run df.explain() to verify improvement
```

---

## Exam Checklist

- [ ] Know Spark UI is at `localhost:4040` for active local jobs; `localhost:18080` for History Server
- [ ] Know the 5 `explain()` modes: `simple`, `extended`, `formatted`, `cost`, `codegen`
- [ ] Know physical plans are read **bottom to top**
- [ ] Know `PushedFilters: [...]` means predicate pushdown is working
- [ ] Know `BroadcastHashJoin` is better than `SortMergeJoin`
- [ ] Know `Exchange` nodes represent shuffles
- [ ] Know `WholeStageCodegen` means operator fusion (good)
- [ ] Know how to diagnose missing predicate pushdown, wrong join type, and skew from the plan

---

[⬅️ topic4-prompt25-common-errors.md](topic4-prompt25-common-errors.md) | **26 / 32** | [Next ➡️ topic5-prompt27-structured-streaming.md](topic5-prompt27-structured-streaming.md)
