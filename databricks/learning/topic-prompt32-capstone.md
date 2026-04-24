# Hands-On Capstone Project: Ride-Sharing Pipeline

> **Databricks Certified Associate Developer for Apache Spark**
> All 7 Topics — End-to-End Integration

---

## Overview

This capstone project ties together all seven topic areas of the Databricks Certified Associate Developer exam through a realistic ride-sharing analytics pipeline. Each step is labelled with the exam topic it exercises.

---

## Dataset Schema

```python
from pyspark.sql.types import (
    StructType, StructField, StringType, DoubleType, TimestampType
)

trip_schema = StructType([
    StructField('trip_id',       StringType(),    nullable=False),
    StructField('driver_id',     StringType(),    nullable=True),
    StructField('rider_id',      StringType(),    nullable=True),
    StructField('pickup_time',   TimestampType(), nullable=True),
    StructField('dropoff_time',  TimestampType(), nullable=True),
    StructField('fare_amount',   DoubleType(),    nullable=True),
    StructField('distance_miles',DoubleType(),    nullable=True),
    StructField('status',        StringType(),    nullable=True),
    StructField('city',          StringType(),    nullable=True),
    StructField('tip_amount',    DoubleType(),    nullable=True),
])
```

---

## Pipeline: All 12 Steps

### Step 1 — Read CSV with Explicit Schema (Topic 3)

```python
trips_df = (
    spark.read
         .schema(trip_schema)
         .option('header', True)
         .option('timestampFormat', 'yyyy-MM-dd HH:mm:ss')
         .csv('/data/trips.csv')
)
trips_df.printSchema()
```

### Step 2 — Clean Data: Nulls, Filters, Casts (Topic 3)

```python
from pyspark.sql import functions as F

trips_df = (
    trips_df
    .fillna({'fare_amount': 0.0, 'tip_amount': 0.0, 'distance_miles': 0.0})
    .filter(F.col('status') == 'completed')
    .filter(F.col('fare_amount') > 0)
    .filter(F.col('pickup_time').isNotNull())
    .filter(F.col('dropoff_time').isNotNull())
)
```

### Step 3 — Compute Revenue per City (Topic 3)

```python
revenue_df = (
    trips_df
    .groupBy('city')
    .agg(
        F.sum('fare_amount').alias('total_revenue'),
        F.avg('fare_amount').alias('avg_fare'),
        F.count('trip_id').alias('trip_count'),
        F.sum('tip_amount').alias('total_tips')
    )
    .withColumn('revenue_with_tips', F.col('total_revenue') + F.col('total_tips'))
)
revenue_df.show()
```

### Step 4 — Rank Drivers by Revenue per City (Topic 3 — Window Functions)

```python
from pyspark.sql import Window

driver_revenue_df = (
    trips_df
    .groupBy('city', 'driver_id')
    .agg(F.sum('fare_amount').alias('driver_revenue'))
)

w = Window.partitionBy('city').orderBy(F.col('driver_revenue').desc())

ranked_drivers_df = (
    driver_revenue_df
    .withColumn('rank', F.dense_rank().over(w))
    .filter(F.col('rank') <= 5)   # top 5 drivers per city
)
ranked_drivers_df.show()
```

### Step 5 — UDF: Trip Duration in Minutes (Topic 3 — UDFs)

```python
from pyspark.sql.functions import udf
from pyspark.sql.types import DoubleType

@udf(returnType=DoubleType())
def trip_duration_minutes(pickup, dropoff):
    if pickup is None or dropoff is None:
        return None
    delta = dropoff - pickup
    return delta.total_seconds() / 60.0

trips_df = trips_df.withColumn(
    'duration_minutes',
    trip_duration_minutes(F.col('pickup_time'), F.col('dropoff_time'))
)
```

### Step 6 — Spark SQL Query (Topic 2)

```python
trips_df.createOrReplaceTempView('trips')
revenue_df.createOrReplaceTempView('city_revenue')

sql_result = spark.sql("""
    SELECT
        t.city,
        COUNT(*) AS trip_count,
        AVG(t.fare_amount) AS avg_fare,
        MAX(t.duration_minutes) AS max_duration
    FROM trips t
    WHERE t.duration_minutes > 0
    GROUP BY t.city
    HAVING COUNT(*) > 10
    ORDER BY avg_fare DESC
""")
sql_result.show()
```

### Step 7 — Broadcast Join with Driver Metadata (Topic 1 — Broadcasting)

```python
driver_metadata_df = spark.read.csv(
    '/data/drivers.csv',
    header=True,
    inferSchema=True
)
# driver_metadata is small → broadcast

from pyspark.sql.functions import broadcast

enriched_df = trips_df.join(
    broadcast(driver_metadata_df),
    on='driver_id',
    how='left'
)
enriched_df.printSchema()
```

### Step 8 — Write Parquet Partitioned by City (Topic 3 — Reading/Writing)

```python
enriched_df.write \
           .partitionBy('city') \
           .mode('overwrite') \
           .parquet('/output/trips_enriched')
```

### Step 9 — Partition Pruning Read (Topic 2/3)

```python
# Read only the 'Chicago' partition — Spark uses partition pruning to skip others
chicago_df = spark.read \
                  .parquet('/output/trips_enriched') \
                  .filter(F.col('city') == 'Chicago')

chicago_df.explain('formatted')  # check for partition pruning in physical plan
```

### Step 10 — Pandas API Analysis (Topic 7)

```python
import pyspark.pandas as ps

psdf = enriched_df.pandas_api()

# pandas-style operations on distributed data
summary = psdf.groupby('city')['fare_amount'].describe()
print(summary)

# Convert small result to pandas for plotting
pdf = summary.to_pandas()
```

### Step 11 — Structured Streaming Simulation (Topic 5)

```python
stream_schema = trip_schema

incoming_trips = (
    spark.readStream
         .schema(stream_schema)
         .option('header', True)
         .format('csv')
         .load('/data/stream_drop_zone/')   # new CSV files land here
)

# Compute running revenue by city
streaming_revenue = (
    incoming_trips
    .filter(F.col('status') == 'completed')
    .groupBy('city')
    .agg(
        F.sum('fare_amount').alias('running_revenue'),
        F.count('trip_id').alias('running_trips')
    )
)

query = (
    streaming_revenue
    .writeStream
    .outputMode('complete')         # complete because of aggregation
    .format('console')
    .trigger(processingTime='30 seconds')
    .option('checkpointLocation', '/checkpoints/streaming_revenue')
    .start()
)

query.awaitTermination()
```

---

## Topics Exercised

| Step | Topic | Technique |
|------|-------|-----------|
| 1 | Topic 3 | Explicit schema, CSV read |
| 2 | Topic 3 | fillna, filter, null handling |
| 3 | Topic 3 | groupBy, agg, withColumn |
| 4 | Topic 3 | Window functions, dense_rank, filter |
| 5 | Topic 3 | UDF with decorator, null guard |
| 6 | Topic 2 | createOrReplaceTempView, spark.sql, HAVING |
| 7 | Topic 1 | broadcast join |
| 8 | Topic 3 | partitionBy, write Parquet |
| 9 | Topic 2/3 | partition pruning, explain() |
| 10 | Topic 7 | pandas_api(), groupby in pyspark.pandas, to_pandas() |
| 11 | Topic 5 | readStream, groupBy agg, writeStream complete mode, checkpoint |

---

## Exam Readiness Checklist

After building and running this capstone, verify you can:

- [ ] Define a StructType schema programmatically and use it for reading
- [ ] Chain filter, fillna, withColumn, groupBy/agg in correct order
- [ ] Apply window functions with dense_rank partitioned by a grouping column
- [ ] Write a UDF with returnType and None guard
- [ ] Register a temp view and query it with `spark.sql()`
- [ ] Use `broadcast()` on the small side of a join
- [ ] Write Parquet output with `partitionBy()`
- [ ] Read back with partition pruning and verify with `explain()`
- [ ] Convert a PySpark DataFrame to pyspark.pandas and use groupby
- [ ] Write a streaming query with `readStream`, windowed agg, `writeStream`, checkpoint

---

[⬅️ topic-prompt31-practice-exam.md](topic-prompt31-practice-exam.md) | **32 / 32** | [Next ➡️ README.md](README.md)
