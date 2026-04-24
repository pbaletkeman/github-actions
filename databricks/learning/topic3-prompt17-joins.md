# Joins

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Joins combine two DataFrames based on matching keys. Spark supports all SQL standard join types plus semi/anti joins, and offers three syntactic forms for specifying the join condition. Understanding the duplicate-column problem and its solutions is exam-critical.

---

## Three Join Forms

```python
# Form 1: String column name (single common column)
df_a.join(df_b, on='id', how='inner')

# Form 2: List of strings (multiple common columns)
df_a.join(df_b, on=['id', 'date'], how='inner')

# Form 3: Column expression (explicit condition — handles different column names)
df_a.join(df_b, on=df_a['customer_id'] == df_b['id'], how='inner')
df_a.join(df_b, on=col('a_key') == col('b_key'), how='left')
```

When you use **Forms 1 or 2** (string), the join key appears **once** in the result. When you use **Form 3** (expression), both sides' key columns appear.

---

## Join Types

| Spark `how` Value | SQL Equivalent | Description |
|------------------|----------------|-------------|
| `'inner'` | INNER JOIN | Only matching rows |
| `'left'` / `'left_outer'` | LEFT OUTER JOIN | All left rows; NULLs for unmatched right |
| `'right'` / `'right_outer'` | RIGHT OUTER JOIN | All right rows; NULLs for unmatched left |
| `'outer'` / `'full'` / `'full_outer'` | FULL OUTER JOIN | All rows from both; NULLs for non-matches |
| `'left_semi'` | WHERE EXISTS subquery | Left rows that have a match in right; no right columns |
| `'left_anti'` | WHERE NOT EXISTS | Left rows that have NO match in right; no right columns |
| `'cross'` / `'cartesian'` | CROSS JOIN | Cartesian product — every left row × every right row |

---

## The Duplicate Column Problem

When both DataFrames have a column with the same name (other than the join key), both appear in the result — causing ambiguity:

```python
orders = spark.createDataFrame([(1, 'Alice', 100)], ['order_id', 'name', 'amount'])
customers = spark.createDataFrame([(1, 'Alice', 'US')], ['customer_id', 'name', 'country'])

# Using Form 3 — 'name' appears twice
result = orders.join(customers, on=orders['order_id'] == customers['customer_id'])
result.select('name')  # ❌ AnalysisException: Ambiguous column 'name'
```

### 4 Solutions

**Solution 1: Use string `on=` (Forms 1/2) — preferred when column names match**

```python
orders.join(customers, on='id')  # 'id' appears only once in result
```

**Solution 2: Drop the duplicate column after join**

```python
result = orders.join(customers, on=orders['order_id'] == customers['customer_id'])
result.drop(customers['name'])   # keep orders.name; drop customers.name
```

**Solution 3: Rename before joining**

```python
customers_renamed = customers.withColumnRenamed('name', 'customer_name')
orders.join(customers_renamed, on=orders['order_id'] == customers_renamed['customer_id'])
```

**Solution 4: Use aliases + DataFrame reference**

```python
a = orders.alias('a')
b = customers.alias('b')
result = a.join(b, col('a.order_id') == col('b.customer_id'))
result.select('a.name', 'b.name')  # unambiguous with table prefix
```

---

## Semi and Anti Joins

```python
# left_semi: return orders where a matching customer exists (no customer columns)
orders.join(customers, on='customer_id', how='left_semi')

# left_anti: return orders with NO matching customer
orders.join(customers, on='customer_id', how='left_anti')
```

Semi/anti joins are efficient alternatives to correlated subqueries and are not affected by the duplicate column problem.

---

## Broadcast Join

```python
from pyspark.sql.functions import broadcast

result = large_df.join(broadcast(small_df), on='id')

# SQL hint equivalent
spark.sql("SELECT /*+ BROADCAST(small_df) */ * FROM large_df JOIN small_df ON ...")
```

Auto-triggered when either side is smaller than `spark.sql.autoBroadcastJoinThreshold` (default 10 MB).

---

## Cross Join

```python
# Explicit cross join
a.join(b, how='cross')
a.crossJoin(b)    # alternative syntax
```

Requires `spark.sql.crossJoin.enabled = true` in older Spark versions. Produces `N × M` rows — use with extreme care.

---

## Join Performance Tips

| Strategy | Benefit |
|----------|---------|
| Use `broadcast()` for small tables | Eliminates shuffle for the large table side |
| Filter before joining | Smaller DataFrames = smaller shuffle |
| Enable AQE | Runtime broadcast switch for unexpectedly small tables |
| Use string/list join form | Avoids duplicate columns; cleaner plan |
| Pre-repartition by join key | Co-locates data; reduces shuffle magnitude |

---

## Exam Checklist

- [ ] Know all three join syntax forms
- [ ] Know all join type names (including `left_semi`, `left_anti`, `cross`)
- [ ] Know the duplicate column problem occurs with Form 3 (column expression)
- [ ] Know the 4 solutions for duplicate columns
- [ ] Know `left_semi` returns left rows with a match (no right columns)
- [ ] Know `left_anti` returns left rows with NO match
- [ ] Know NULL keys do NOT match in joins
- [ ] Know `broadcast()` hint for broadcast join

---

[⬅️ topic3-prompt16-handling-nulls.md](topic3-prompt16-handling-nulls.md) | **17 / 32** | [Next ➡️ topic3-prompt18-combining-dataframes.md](topic3-prompt18-combining-dataframes.md)
