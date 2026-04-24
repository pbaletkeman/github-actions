# Spark SQL Query Optimization

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 2 — Spark SQL (20%)

---

## Overview

Spark SQL's **Catalyst Optimizer** automatically transforms a logical query plan through a series of rule-based and cost-based optimisations before generating physical execution code. Understanding Catalyst and Adaptive Query Execution (AQE) is key for both writing performant queries and interpreting `explain()` output.

---

## Catalyst Optimizer — 4 Phases

```
User Query (SQL / DataFrame API)
         │
         ▼
1. Analysis Phase
   • Resolve column names and types from Catalog
   • Validate that referenced tables/views exist
         │
         ▼
2. Logical Optimization Phase
   • Apply rule-based optimisations:
     - Predicate Pushdown
     - Column Pruning
     - Constant Folding
     - Null Propagation
         │
         ▼
3. Physical Planning Phase
   • Choose physical operators (SortMergeJoin vs BroadcastHashJoin)
   • Cost-Based Optimiser (CBO) selects the cheapest plan
         │
         ▼
4. Code Generation (Whole-Stage CodeGen)
   • Generate Java bytecode that runs directly on JVM
   • Fuses multiple operators into a single compiled function
```

---

## Key Optimisations

### Predicate Pushdown

Filters are pushed **down** as close to the data source as possible — ideally into the file format reader (Parquet/ORC column statistics, data source predicates).

```python
# Catalyst will push the filter down to the scan layer
df = spark.read.parquet('/data/sales')
result = df.filter(col('year') == 2024).groupBy('region').sum('amount')
# The year == 2024 filter is applied AT READ TIME — avoids reading irrelevant partitions
```

### Column Pruning

Only the columns needed by the query are read from the data source. For Parquet (columnar format), this is extremely efficient.

```python
# Only 'region' and 'amount' are read from Parquet — other columns are skipped
df.select('region', 'amount').groupBy('region').sum('amount')
```

### Constant Folding

Constant expressions are evaluated at plan time rather than per-row:
```sql
SELECT price * (1 + 0.1)  →  SELECT price * 1.1   -- computed once, not per row
```

### Broadcast Join

When one side of a join is smaller than `spark.sql.autoBroadcastJoinThreshold` (default 10 MB), Catalyst automatically selects a BroadcastHashJoin (no shuffle for the large table).

### Partition Pruning

When data is stored with `partitionBy` and a query filters on the partition column, only matching partition directories are read.

---

## Reading Explain Plans

```python
df.explain()                  # default — shows Physical Plan
df.explain('simple')          # same as default
df.explain('extended')        # Logical Plan + Optimized Logical + Physical
df.explain('formatted')       # formatted tree with stats (Spark 3.0+)
df.explain('cost')            # includes estimated row counts and sizes
df.explain('codegen')         # shows generated Java code
```

### Key Plan Nodes

| Node | Meaning | Good/Bad? |
|------|---------|-----------|
| `FileScan` with `PushedFilters` | Filter pushed to source | ✅ Good |
| `Filter` ABOVE `FileScan` | Filter NOT pushed (applied after reading all data) | ⚠️ Consider repartitioning |
| `BroadcastHashJoin` | Small table broadcast | ✅ Good |
| `SortMergeJoin` | Both sides shuffled and sorted | ⚠️ Expensive |
| `Exchange` | Shuffle | ⚠️ Review if avoidable |
| `WholeStageCodegen` | Multiple operators fused | ✅ Good |
| `Project` | Column selection/projection | Neutral |

---

## Adaptive Query Execution (AQE)

AQE adjusts the query plan at **runtime** using statistics gathered from completed shuffle stages.

```python
spark.conf.set('spark.sql.adaptive.enabled', 'true')  # Spark 3.2+ default: true
```

### What AQE Does

| Feature | Description | Benefit |
|---------|-------------|---------|
| **Dynamic coalescing** | Merges small post-shuffle partitions into fewer, larger ones | Eliminates 200 tiny tasks on small data |
| **Dynamic switching to broadcast** | If a table turns out to be small at runtime, switches SortMergeJoin → BroadcastHashJoin | Eliminates unexpected shuffles |
| **Skew join optimisation** | Splits skewed partitions and handles them separately | Eliminates straggler tasks |

### Key AQE Properties

| Property | Default | Description |
|----------|---------|-------------|
| `spark.sql.adaptive.enabled` | `true` (Spark 3.2+) | Enable AQE |
| `spark.sql.adaptive.coalescePartitions.enabled` | `true` | Auto-coalesce small partitions |
| `spark.sql.adaptive.advisoryPartitionSizeInBytes` | `64MB` | Target partition size after coalescing |
| `spark.sql.adaptive.skewJoin.enabled` | `true` | Auto handle skewed join keys |

---

## Cost-Based Optimiser (CBO)

The CBO uses table and column statistics to choose between physical plans (e.g., which table to broadcast, which join order to use).

```python
spark.conf.set('spark.sql.cbo.enabled', 'true')

# Collect statistics — required for CBO to work
spark.sql('ANALYZE TABLE my_table COMPUTE STATISTICS')
spark.sql('ANALYZE TABLE my_table COMPUTE STATISTICS FOR COLUMNS col1, col2')
```

---

## Exam Checklist

- [ ] Know the 4 Catalyst phases: Analysis → Logical Optimization → Physical Planning → Code Generation
- [ ] Know the 6 key optimisations: Predicate Pushdown, Column Pruning, Constant Folding, Join Reordering, Broadcast Join, Partition Pruning
- [ ] Understand the 6 `explain()` modes and what each shows
- [ ] Know what `Exchange` nodes mean in a plan (shuffle)
- [ ] Know that `BroadcastHashJoin` is better than `SortMergeJoin` (no shuffle)
- [ ] Know how AQE works and its 3 key runtime adjustments
- [ ] Know `spark.sql.adaptive.enabled` default is `true` in Spark 3.2+

---

[⬅️ topic2-prompt10-window-functions.md](topic2-prompt10-window-functions.md) | **11 / 32** | [Next ➡️ topic3-prompt12-dataframe-creation-selection.md](topic3-prompt12-dataframe-creation-selection.md)
