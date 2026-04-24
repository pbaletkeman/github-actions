# Built-in Spark SQL Functions

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 2 — Spark SQL (20%)

---

## Overview

PySpark provides a rich library of built-in SQL functions in `pyspark.sql.functions`. These functions operate on DataFrame columns and are the preferred alternative to Python UDFs (they run as JVM code — no serialisation overhead).

```python
from pyspark.sql import functions as F
# or import specific functions:
from pyspark.sql.functions import col, upper, when, regexp_extract
```

---

## String Functions

| Function | Example | Description |
|----------|---------|-------------|
| `upper(col)` | `F.upper('name')` | Convert to uppercase |
| `lower(col)` | `F.lower('name')` | Convert to lowercase |
| `length(col)` | `F.length('name')` | Length of string |
| `trim(col)` | `F.trim('name')` | Remove leading/trailing whitespace |
| `ltrim(col)` | `F.ltrim('name')` | Remove leading whitespace |
| `rtrim(col)` | `F.rtrim('name')` | Remove trailing whitespace |
| `lpad(col, len, pad)` | `F.lpad('id', 5, '0')` | Left-pad string |
| `rpad(col, len, pad)` | `F.rpad('id', 5, '0')` | Right-pad string |
| `concat(*cols)` | `F.concat('first', F.lit(' '), 'last')` | Concatenate strings |
| `concat_ws(sep, *cols)` | `F.concat_ws('-', 'year', 'month')` | Join with separator |
| `substring(col, pos, len)` | `F.substring('name', 1, 3)` | Extract substring (1-based) |
| `split(col, pattern)` | `F.split('tags', ',')` | Split string → ArrayType |
| `regexp_replace(col, pattern, replacement)` | `F.regexp_replace('phone', '[^0-9]', '')` | Replace by regex |
| `regexp_extract(col, pattern, group)` | `F.regexp_extract('email', r'@(\w+)', 1)` | Extract regex group |
| `like(col, pattern)` | `col('name').like('A%')` | SQL LIKE pattern |
| `rlike(col, pattern)` | `col('name').rlike('^[A-Z]')` | Regex match |

---

## Numeric Functions

| Function | Example | Description |
|----------|---------|-------------|
| `round(col, scale)` | `F.round('price', 2)` | Round to N decimal places |
| `floor(col)` | `F.floor('price')` | Round down to integer |
| `ceil(col)` | `F.ceil('price')` | Round up to integer |
| `abs(col)` | `F.abs('profit')` | Absolute value |
| `sqrt(col)` | `F.sqrt('area')` | Square root |
| `pow(col, exp)` | `F.pow('base', 2)` | Power |
| `log(base, col)` | `F.log(10, 'value')` | Logarithm |
| `greatest(*cols)` | `F.greatest('a', 'b', 'c')` | Maximum across columns |
| `least(*cols)` | `F.least('a', 'b', 'c')` | Minimum across columns |

---

## Date and Timestamp Functions

| Function | Example | Description |
|----------|---------|-------------|
| `current_date()` | `F.current_date()` | Today's date |
| `current_timestamp()` | `F.current_timestamp()` | Current timestamp |
| `to_date(col, fmt)` | `F.to_date('date_str', 'yyyy-MM-dd')` | Parse string to DateType |
| `to_timestamp(col, fmt)` | `F.to_timestamp('ts_str', 'yyyy-MM-dd HH:mm:ss')` | Parse string to TimestampType |
| `date_format(col, fmt)` | `F.date_format('ts', 'yyyy/MM/dd')` | Format date/timestamp as string |
| `datediff(end, start)` | `F.datediff('end_date', 'start_date')` | Days between two dates |
| `months_between(end, start)` | `F.months_between('end', 'start')` | Months between two dates |
| `date_add(col, n)` | `F.date_add('date', 7)` | Add N days |
| `date_sub(col, n)` | `F.date_sub('date', 7)` | Subtract N days |
| `year(col)` | `F.year('date')` | Extract year |
| `month(col)` | `F.month('date')` | Extract month |
| `dayofweek(col)` | `F.dayofweek('date')` | Day of week (1=Sunday) |
| `hour(col)` | `F.hour('ts')` | Extract hour |
| `minute(col)` | `F.minute('ts')` | Extract minute |

---

## Conditional and Null Functions

| Function | Example | Description |
|----------|---------|-------------|
| `when(condition, value).otherwise(default)` | `F.when(col('score') > 90, 'A').otherwise('B')` | If-then-else |
| `coalesce(*cols)` | `F.coalesce('value', F.lit(0))` | First non-null value |
| `isnull(col)` | `F.isnull('name')` | True if null |
| `isnotnull(col)` | `F.isnotnull('name')` | True if not null |
| `isnan(col)` | `F.isnan('amount')` | True if NaN (float) |
| `nvl(col, default)` | `F.nvl('value', F.lit(0))` | Spark 3.5+ |
| `ifnull(col, default)` | `F.ifnull('value', F.lit(0))` | Spark 3.5+ |
| `nullif(col1, col2)` | `F.nullif('a', 'b')` | Returns null if both equal |

---

## Aggregate Functions

| Function | Description |
|----------|-------------|
| `count('*')` / `count(col)` | Row count / non-null count |
| `countDistinct(col)` | Distinct non-null count |
| `sum(col)` | Sum |
| `avg(col)` / `mean(col)` | Average |
| `max(col)` / `min(col)` | Max / min |
| `stddev(col)` / `stddev_pop(col)` | Sample / population std dev |
| `variance(col)` / `var_pop(col)` | Variance |
| `first(col)` / `last(col)` | First / last value in group |
| `collect_list(col)` | Collect values as ArrayType (with duplicates) |
| `collect_set(col)` | Collect unique values as ArrayType |
| `approx_count_distinct(col)` | Approximate distinct count (HyperLogLog — fast) |
| `percentile_approx(col, p)` | Approximate percentile |
| `sum_distinct(col)` | Sum of distinct values |

---

## Array and Map Functions

| Function | Example | Description |
|----------|---------|-------------|
| `array(*cols)` | `F.array('a', 'b', 'c')` | Create ArrayType from columns |
| `array_contains(col, val)` | `F.array_contains('tags', 'python')` | Check membership |
| `explode(col)` | `F.explode('items')` | Explode array to rows (excludes nulls) |
| `explode_outer(col)` | `F.explode_outer('items')` | Explode including null rows |
| `posexplode(col)` | `F.posexplode('items')` | Explode with position index |
| `array_distinct(col)` | `F.array_distinct('tags')` | Remove duplicates from array |
| `array_sort(col)` | `F.array_sort('scores')` | Sort array elements |
| `size(col)` | `F.size('items')` | Length of array or map |
| `element_at(col, idx)` | `F.element_at('arr', 1)` | Get element at 1-based index |
| `flatten(col)` | `F.flatten('nested_arrays')` | Flatten nested arrays |
| `map_keys(col)` | `F.map_keys('properties')` | Extract map keys as array |
| `map_values(col)` | `F.map_values('properties')` | Extract map values as array |
| `create_map(*cols)` | `F.create_map('k', 'v')` | Create MapType |

---

## Exam Checklist

- [ ] Know the import: `from pyspark.sql import functions as F`
- [ ] Know that all built-in functions work in DataFrame API, `selectExpr()`, `expr()`, and `spark.sql()`
- [ ] Know `when().otherwise()` syntax for conditional logic
- [ ] Know `coalesce()` for the first-non-null pattern
- [ ] Know `explode()` vs `explode_outer()` (null handling)
- [ ] Know aggregate function names: `countDistinct`, `collect_list`, `collect_set`, `approx_count_distinct`
- [ ] Know common date functions: `to_date()`, `to_timestamp()`, `datediff()`, `date_format()`

---

[⬅️ topic2-prompt8-spark-sql-fundamentals.md](topic2-prompt8-spark-sql-fundamentals.md) | **9 / 32** | [Next ➡️ topic2-prompt10-window-functions.md](topic2-prompt10-window-functions.md)
