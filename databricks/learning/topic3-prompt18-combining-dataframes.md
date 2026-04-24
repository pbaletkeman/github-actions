# Combining DataFrames — Union, Intersect, Except

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

Spark provides set operations for combining DataFrames vertically (row-wise): union, intersect, and except. Each operation has both a deduplicating variant and an all-variant that keeps duplicates.

---

## Union Operations

### `union()` / `unionAll()`

```python
# By position — column NAMES are ignored; order must match
result = df_a.union(df_b)
result = df_a.unionAll(df_b)   # exact alias for union() — identical behaviour

# Equivalent SQL: UNION ALL (keeps duplicates)
```

**Key facts:**
- Aligns columns by **position**, not by name
- Does **NOT** deduplicate — equivalent to SQL `UNION ALL`
- Both DataFrames must have the same number of columns
- Column names in the result are taken from the **first** DataFrame

### `unionByName()`

```python
# By name — column order doesn't matter; names must match
result = df_a.unionByName(df_b)

# Spark 3.1+: allow missing columns (filled with NULL)
result = df_a.unionByName(df_b, allowMissingColumns=True)
```

**Key facts:**
- Aligns columns by **name** — order can differ between DataFrames
- Does **NOT** deduplicate — equivalent to SQL `UNION ALL`
- `allowMissingColumns=True` fills missing columns with NULL

---

## Union Comparison

| Operation | Alignment | Deduplication | SQL Equivalent |
|-----------|-----------|--------------|----------------|
| `union()` | By position | ❌ None | `UNION ALL` |
| `unionAll()` | By position | ❌ None | `UNION ALL` |
| `unionByName()` | By name | ❌ None | `UNION ALL` |
| `union().distinct()` | By position | ✅ Yes | `UNION DISTINCT` |
| `unionByName().distinct()` | By name | ✅ Yes | `UNION DISTINCT` |

---

## Intersect Operations

### `intersect()`

```python
# Returns rows present in BOTH DataFrames — deduplicates automatically
result = df_a.intersect(df_b)
# SQL equivalent: INTERSECT DISTINCT
```

### `intersectAll()`

```python
# Returns rows present in BOTH — keeps duplicates (as many times as min count)
result = df_a.intersectAll(df_b)
# SQL equivalent: INTERSECT ALL
```

---

## Except / Subtract Operations

### `subtract()` / `exceptAll()`

```python
# Returns rows in df_a that are NOT in df_b — deduplicates result
result = df_a.subtract(df_b)
# SQL equivalent: EXCEPT DISTINCT

# Returns rows in df_a not in df_b — keeps duplicates
result = df_a.exceptAll(df_b)
# SQL equivalent: EXCEPT ALL
```

**Note:** `subtract()` is the Python DataFrame API name. In SQL, the equivalent is `EXCEPT`.

---

## Complete Set Operations Summary

| Operation | Deduplication | Keeps Duplicates | SQL Equivalent |
|-----------|--------------|-----------------|----------------|
| `union()` / `unionAll()` | No | Yes | `UNION ALL` |
| `union().distinct()` | Yes | No | `UNION DISTINCT` |
| `unionByName()` | No | Yes | `UNION ALL` |
| `intersect()` | **Yes** (auto-dedup) | No | `INTERSECT DISTINCT` |
| `intersectAll()` | No | Yes | `INTERSECT ALL` |
| `subtract()` | **Yes** (auto-dedup) | No | `EXCEPT DISTINCT` |
| `exceptAll()` | No | Yes | `EXCEPT ALL` |

---

## Common Pitfalls

### `union()` Aligns by Position, Not Name

```python
schema_a = ['id', 'name', 'salary']
schema_b = ['name', 'id', 'salary']   # different order!

df_a = spark.createDataFrame([(1, 'Alice', 80000)], schema_a)
df_b = spark.createDataFrame([('Bob', 2, 90000)], schema_b)

df_a.union(df_b)
# Result: id=1, name='Alice', salary=80000
#         id='Bob', name=2, salary=90000  ← WRONG! columns misaligned
```

Use `unionByName()` when column order might differ.

### `intersect()` Auto-Deduplicates

```python
a = spark.createDataFrame([(1,), (1,), (2,)], ['x'])
b = spark.createDataFrame([(1,), (1,), (1,)], ['x'])

a.intersect(b)      # result: [(1,)]  — deduped to one row
a.intersectAll(b)   # result: [(1,), (1,)]  — min(2,3) = 2 copies
```

---

## Exam Checklist

- [ ] Know `union()` and `unionAll()` are exact aliases
- [ ] Know both union operations align by **position**, not name
- [ ] Know neither union operation deduplicates (both = UNION ALL)
- [ ] Know `unionByName()` aligns by name; use when column order differs
- [ ] Know `intersect()` auto-deduplicates (= INTERSECT DISTINCT)
- [ ] Know `intersectAll()` keeps duplicates (= INTERSECT ALL)
- [ ] Know `subtract()` auto-deduplicates (= EXCEPT DISTINCT)
- [ ] Know `exceptAll()` keeps duplicates (= EXCEPT ALL)

---

[⬅️ topic3-prompt17-joins.md](topic3-prompt17-joins.md) | **18 / 32** | [Next ➡️ topic3-prompt19-reading-writing-dataframes.md](topic3-prompt19-reading-writing-dataframes.md)
