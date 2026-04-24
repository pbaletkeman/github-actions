# Complex End-to-End Scenarios

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 3 — DataFrame API (30%)

---

## Overview

This section demonstrates three realistic end-to-end Spark pipelines that chain together the techniques from all of Topic 3. These scenarios are representative of the types of multi-step questions on the Databricks Certified Associate Developer exam.

---

## Scenario 1: Sales Analytics Pipeline

**Goal:** Read CSV, clean data, compute per-category top-3 products by revenue.

```python
from pyspark.sql import functions as F
from pyspark.sql import Window
from pyspark.sql.types import StructType, StructField, StringType, IntegerType, DoubleType

# Step 1: Define explicit schema
schema = StructType([
    StructField('product_id',  StringType(),  nullable=False),
    StructField('category',    StringType(),  nullable=True),
    StructField('quantity',    StringType(),  nullable=True),   # comes as string in CSV
    StructField('unit_price',  StringType(),  nullable=True),   # comes as string
    StructField('sale_date',   StringType(),  nullable=True),
])

# Step 2: Read CSV with explicit schema
df = spark.read.schema(schema) \
              .option('header', True) \
              .option('nullValue', 'N/A') \
              .csv('/data/sales.csv')

# Step 3: Type casting
df = df.withColumn('quantity',   F.col('quantity').cast('integer')) \
       .withColumn('unit_price', F.col('unit_price').cast('double')) \
       .withColumn('sale_date',  F.to_date(F.col('sale_date'), 'yyyy-MM-dd'))

# Step 4: Fill nulls and filter invalid rows
df = df.fillna({'category': 'Unknown', 'quantity': 0, 'unit_price': 0.0}) \
       .filter(F.col('quantity') > 0) \
       .filter(F.col('unit_price') > 0)

# Step 5: Compute revenue per product per category
df = df.withColumn('revenue', F.col('quantity') * F.col('unit_price'))

# Step 6: Aggregate
agg_df = df.groupBy('category', 'product_id') \
           .agg(F.sum('revenue').alias('total_revenue'))

# Step 7: Window rank — top 3 per category using dense_rank
w = Window.partitionBy('category').orderBy(F.col('total_revenue').desc())
ranked_df = agg_df.withColumn('rank', F.dense_rank().over(w)) \
                  .filter(F.col('rank') <= 3)

# Step 8: Write as Parquet
ranked_df.write.mode('overwrite').parquet('/output/top_products')
```

---

## Scenario 2: Log Processing with Nested Data

**Goal:** Read JSON with arrays, explode, extract fields, join with lookup, deduplicate.

```python
from pyspark.sql.functions import explode, regexp_extract, broadcast, col

# Step 1: Read JSON with schema inference
logs_df = spark.read.json('/data/event_logs.json')
# Schema: event_id, timestamp, tags (ARRAY<STRING>), user_agent (STRING)

# Step 2: Explode array column
exploded_df = logs_df.select(
    'event_id', 'timestamp',
    explode(col('tags')).alias('tag'),
    'user_agent'
)

# Step 3: Extract browser from user_agent using regex
cleaned_df = exploded_df.withColumn(
    'browser',
    regexp_extract(col('user_agent'), r'(Chrome|Firefox|Safari|Edge)', 1)
)

# Step 4: Broadcast join with small lookup table
lookup_df = spark.read.csv('/data/browser_lookup.csv', header=True, inferSchema=True)

result_df = cleaned_df.join(broadcast(lookup_df), on='browser', how='left')

# Step 5: Remove duplicates (event_id + tag level)
result_df = result_df.dropDuplicates(['event_id', 'tag'])

# Step 6: Write with partition by date
result_df = result_df.withColumn('date', col('timestamp').cast('date'))
result_df.write \
         .partitionBy('date') \
         .mode('overwrite') \
         .parquet('/output/processed_logs')
```

---

## Scenario 3: Data Integration and Quality Check

**Goal:** Union two DataFrames from different sources, remove bad records, clean and write to CSV.

```python
from pyspark.sql.functions import col, F, to_date, coalesce, lit

# Step 1: Read two sources
df_old = spark.read.parquet('/data/legacy_customers.parquet')
df_new = spark.read.json('/data/new_customers.json')

# Step 2: Union by name — columns may be in different order
combined_df = df_old.unionByName(df_new, allowMissingColumns=True)

# Step 3: Load and remove known-bad records
bad_ids_df = spark.read.csv('/data/blacklist.csv', header=True)
clean_df = combined_df.join(bad_ids_df, on='customer_id', how='left_anti')

# Step 4: Quality checks — drop rows missing critical fields
clean_df = clean_df.dropna(subset=['customer_id', 'email'])

# Step 5: Fill optional missing fields
clean_df = clean_df.fillna({'country': 'Unknown', 'phone': 'N/A'})

# Step 6: Cast string date to DateType
clean_df = clean_df.withColumn(
    'signup_date',
    to_date(col('signup_date'), 'yyyy-MM-dd')
)

# Step 7: Write as single CSV file
clean_df.coalesce(1) \
        .write \
        .option('header', True) \
        .mode('overwrite') \
        .csv('/output/clean_customers')
```

---

## Patterns to Recognise in the Exam

| Pattern | Code | When to Use |
|---------|------|-------------|
| CSV read → explicit schema → cast | `read.schema(schema).csv()` → `.cast()` | Raw CSV ingest |
| Top-N per group | `dense_rank().over(w).filter(rank <= N)` | Leaderboards, analytics |
| Explode arrays → process elements | `explode(col)` → `withColumn(regexp_extract(...))` | Log/event processing |
| Remove known bad records | `left_anti` join with blacklist | Data quality |
| Two sources → union → clean | `unionByName(allowMissingColumns=True)` | Data integration |
| Single-file output | `coalesce(1).write.csv()` | Downstream systems needing one file |

---

## Exam Checklist

- [ ] Know how to chain: read → schema → cast → fillna → filter → groupBy/agg → window → write
- [ ] Know when to use `unionByName(allowMissingColumns=True)` for heterogeneous sources
- [ ] Know `left_anti` join pattern for "remove blacklisted rows"
- [ ] Know `explode()` creates one row per array element
- [ ] Know `dense_rank()` for top-N per group
- [ ] Know `coalesce(1)` for writing a single output file

---

[⬅️ topic3-prompt22-udfs.md](topic3-prompt22-udfs.md) | **23 / 32** | [Next ➡️ topic4-prompt24-performance-tuning.md](topic4-prompt24-performance-tuning.md)
