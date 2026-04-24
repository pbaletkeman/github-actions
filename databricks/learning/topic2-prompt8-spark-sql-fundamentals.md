# Spark SQL Fundamentals

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 2 — Spark SQL (20%)

---

## Overview

Spark SQL lets you run standard SQL queries against DataFrames and Hive tables. Temp views provide a bridge between the DataFrame API and SQL, while the Catalog API lets you manage and inspect available databases, tables, and functions programmatically.

---

## Temp Views: Session-Scoped vs Global

### Session-Scoped Temp View

```python
df.createOrReplaceTempView('sales')          # creates or replaces the view in this session
df.createTempView('sales')                   # fails if 'sales' already exists in this session
```

- Scoped to the **current SparkSession**
- Not visible to other sessions (even in the same cluster)
- Automatically dropped when the SparkSession ends
- Referenced by name alone: `SELECT * FROM sales`

### Global Temp View

```python
df.createOrReplaceGlobalTempView('sales_global')
df.createGlobalTempView('sales_global')
```

- Scoped to the **Spark application** (visible across all sessions in the same Spark context)
- Stored in the special database `global_temp`
- **Must** be referenced with the `global_temp.` prefix:
  ```sql
  SELECT * FROM global_temp.sales_global
  ```
- Dropped when the Spark application ends (or manually dropped)

| | Session Temp View | Global Temp View |
|---|---|---|
| Visibility | Current session only | All sessions in the application |
| Reference | `table_name` | `global_temp.table_name` |
| Lifetime | Session ends | Application ends |

---

## Running SQL Queries

```python
# spark.sql() always returns a DataFrame
result = spark.sql("""
    SELECT category, SUM(amount) AS total
    FROM sales
    GROUP BY category
    ORDER BY total DESC
""")

# chain DataFrame operations on the result
result.filter(col('total') > 1000).show()
```

**`spark.sql()` always returns a DataFrame** — this is an exam-tested fact.

---

## Using SQL with `selectExpr()` and `expr()`

```python
from pyspark.sql.functions import expr

# selectExpr — SQL expression strings in select()
df.selectExpr('salary * 1.1 AS adjusted_salary', 'name')

# expr() — SQL expression as a column object, usable anywhere col() is used
df.withColumn('adjusted_salary', expr('salary * 1.1'))
df.filter(expr('salary > 50000 AND department = "Engineering"'))
```

---

## Catalog API

The `spark.catalog` object provides programmatic access to the metadata catalog.

```python
# List all tables available in the current database
spark.catalog.listTables()

# Check if a table or temp view exists
spark.catalog.tableExists('sales')
spark.catalog.tableExists('global_temp.sales_global')

# Drop a temp view
spark.catalog.dropTempView('sales')
spark.catalog.dropGlobalTempView('sales_global')

# List databases
spark.catalog.listDatabases()

# List columns of a table
spark.catalog.listColumns('sales')

# Switch default database
spark.catalog.setCurrentDatabase('my_db')
```

---

## SQL Functions in Spark SQL

SQL built-in functions are available in three ways:

```python
from pyspark.sql import functions as F

# 1. Python API
df.select(F.upper(F.col('name')))

# 2. spark.sql() / SQL string
spark.sql("SELECT upper(name) FROM employees")

# 3. selectExpr / expr
df.selectExpr('upper(name)')
df.withColumn('upper_name', expr('upper(name)'))
```

The same function name works in all three approaches.

---

## Exam Checklist

- [ ] Know that session-scoped temp views are only visible to the current SparkSession
- [ ] Know that global temp views require the `global_temp.` prefix
- [ ] Know that `spark.sql()` always returns a DataFrame
- [ ] Know how to use the Catalog API: `listTables()`, `tableExists()`, `dropTempView()`
- [ ] Know that `selectExpr()` and `expr()` accept SQL expression strings
- [ ] Know `createOrReplaceTempView()` vs `createTempView()` (the latter fails on duplicates)

---

[⬅️ topic1-prompt7-garbage-collection.md](topic1-prompt7-garbage-collection.md) | **8 / 32** | [Next ➡️ topic2-prompt9-builtin-sql-functions.md](topic2-prompt9-builtin-sql-functions.md)
