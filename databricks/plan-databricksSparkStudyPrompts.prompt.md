# Databricks Spark Developer Associate - Content Creation Prompts
## Use these prompts to generate study materials, practice problems, and deep dives

---

### TOPIC 1: Apache Spark Architecture & Components (20%)

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

#### Prompt 1: Spark Architecture Fundamentals

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a comprehensive guide explaining the Apache Spark architecture including:
- The difference between Driver and Executor nodes
- How the **physical execution hierarchy** works (Driver → Executor → Tasks): the Driver is the JVM process hosting SparkContext; Executors are JVM processes on worker nodes that run Tasks; Tasks are the smallest unit of work, one per partition
- The **logical execution hierarchy**: an **Action** triggers a **Job**; each Job is divided into **Stages** at shuffle boundaries; each Stage contains **Tasks** (one per input partition)
- The **DAGScheduler**: receives the physical plan and converts it into a DAG of stages; splits stages at wide transformation (shuffle) boundaries into ShuffleMapStages and a final ResultStage
- The **TaskScheduler**: receives completed stages from the DAGScheduler and submits individual Tasks to available Executors via the SchedulerBackend
- The **Cluster Manager** role: external resource allocator (Standalone, YARN, Mesos, Kubernetes) that grants worker containers/nodes to Spark on request — Spark does not manage cluster resources itself
- The role of the Spark Context and Session
- Include a detailed diagram description and practical examples in Python
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 2: Execution Modes and Deploy Modes Deep Dive

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Explain Spark execution modes and deploy modes — two related but distinct concepts tested on the exam:
- **Execution modes** (where the Spark cluster runs):
  - Local mode: everything runs in a single JVM on the submitting machine; `master('local')` (1 thread), `master('local[4]')` (4 threads), `master('local[*]')` (all cores)
  - Standalone mode: Spark's built-in cluster manager; `master('spark://host:7077')`
  - YARN mode: resource managed by Hadoop YARN; `master('yarn')`
  - Kubernetes mode: containers managed by Kubernetes; `master('k8s://https://host:port')`
  - When to use each mode, performance implications, and advantages/disadvantages
- **Deploy modes** (where the Driver process runs) — configured with `--deploy-mode` on spark-submit:
  - `--deploy-mode client`: Driver runs on the machine that submitted the job; stdout/stderr visible in the terminal; used for interactive development and debugging
  - `--deploy-mode cluster`: Driver runs on a worker node inside the cluster; client machine can disconnect after submission; used for production scheduled jobs
  - Key exam distinction: in client mode the client machine must stay alive; in cluster mode it does not
- **spark-submit command structure** (exam-relevant): `spark-submit --master yarn --deploy-mode cluster --executor-memory 4g --executor-cores 2 --num-executors 10 my_app.py`
- Common exam scenario: 'You submit a job with --deploy-mode cluster. Where does the driver run?' — answer: on a worker node inside the cluster
- Code examples showing how to configure execution mode in SparkSession.builder
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 3: Lazy Evaluation & Transformation vs Action

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create an educational guide on Lazy Evaluation including:
- What transformations are and why they're lazy
- What actions trigger computation
- Why Spark uses lazy evaluation (benefits)
- Common transformation examples: map, filter, select, join
- Common action examples: collect, count, show, write
- Practical code examples showing lazy vs eager execution
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 4: Shuffling and Performance Impact

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a detailed explanation of Shuffling in Spark covering:
- What shuffling is and when it occurs
- Operations that trigger shuffles (joins, group by, order by)
- Performance implications of shuffles
- How to minimize shuffling in your code
- Code examples demonstrating shuffle-heavy vs optimized operations
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 5: Broadcasting & Optimization

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a guide on Broadcasting in Spark including:
- What broadcasting is and when to use it
- How to broadcast variables in PySpark
- Performance benefits with large lookup tables
- Code examples comparing broadcast joins vs regular joins
- Best practices and limitations
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 6: Fault Tolerance & Resilience

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Explain Spark's fault tolerance mechanisms:
- How RDD lineage provides fault tolerance
- Checkpointing concepts
- Persistence/caching strategies
- Recovery from node failures
- Code examples showing caching and checkpoint usage
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 7: Garbage Collection in Spark

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a technical guide on Garbage Collection:
- Why GC matters in Spark applications
- GC tuning parameters
- Identifying GC overhead in your applications
- Best practices to reduce GC pressure
- Configuration examples
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

---

### TOPIC 2: Using Spark SQL (20%)

> **Exam weight: 20% — 9 questions out of 45.** Focus on SQL functions, query execution, and how SQL and the DataFrame API interoperate.

#### Prompt 8: Spark SQL Fundamentals — Querying DataFrames

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a focused guide on using Spark SQL to query DataFrames for the Databricks Associate exam:
- Registering DataFrames as local temporary views with createOrReplaceTempView()
- Registering global temporary views with createGlobalTempView() and accessing them via global_temp database
- Executing SQL queries with spark.sql() and getting results back as DataFrames
- Mixing SQL queries and DataFrame API operations in the same pipeline
- SQL vs DataFrame API equivalents: SELECT → select(), WHERE → filter(), GROUP BY → groupBy().agg(), ORDER BY → orderBy()
- When to choose SQL vs DataFrame API (readability, team familiarity, dynamic query building)
- 6+ practical end-to-end examples in Python: SELECT with aliases, WHERE with multiple conditions, GROUP BY with HAVING, ORDER BY with LIMIT, subqueries, CASE WHEN statements
- Expected output shown for each example
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 9: Built-in Spark SQL Functions Reference

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a comprehensive reference on Spark SQL built-in functions required for the exam. For each category include the function signature, what it does, and a PySpark code example with expected output:
- **String functions**: upper(), lower(), trim(), ltrim(), rtrim(), length(), initcap(), substring(), split(), concat(), concat_ws(), regexp_replace(), regexp_extract(), translate(), lpad(), rpad()
- **Numeric/math functions**: round(), bround(), abs(), ceil(), floor(), sqrt(), pow(), log(), mod()
- **Date and time functions**: current_date(), current_timestamp(), to_date(), to_timestamp(), date_format(), date_add(), date_sub(), datediff(), months_between(), year(), month(), dayofmonth(), dayofweek(), hour(), minute(), second(), unix_timestamp(), from_unixtime()
- **Conditional/null functions**: when()/otherwise(), coalesce(), nullif(), isnull(), isnotnull(), ifnull(), nvl(), nvl2()
- **Aggregate functions**: count(), countDistinct(), sum(), avg(), mean(), min(), max(), collect_list(), collect_set(), first(), last(), stddev(), variance()
- **Array/collection functions**: array(), explode(), explode_outer(), posexplode(), array_contains(), size(), flatten(), sort_array(), array_distinct(), array_union(), map(), map_keys(), map_values()
- Import pattern: from pyspark.sql.functions import col, lit, when, coalesce ...
- Note which functions are in pyspark.sql.functions vs available as SQL strings in selectExpr()/spark.sql()
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 10: Window Functions in Spark SQL and DataFrame API

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a dedicated guide on Window Functions — a key exam topic:
- What window functions are and how they differ from aggregations (no row reduction)
- Defining a WindowSpec with pyspark.sql.Window: partitionBy(), orderBy(), rowsBetween(), rangeBetween()
- **Ranking functions**: rank(), dense_rank(), row_number(), ntile(), percent_rank() — differences between rank() and dense_rank() with tied values
- **Offset/analytic functions**: lag(), lead(), first(), last() — with and without ignoreNulls
- **Aggregate window functions**: sum(), avg(), min(), max(), count() applied over a window (running totals, moving averages)
- Using window functions in SQL syntax: OVER (PARTITION BY ... ORDER BY ... ROWS BETWEEN ...)
- Using window functions in the DataFrame API with Window.partitionBy().orderBy()
- Practical exam scenarios: rank employees by salary within department, compute running total by date, access previous row value, find top-N records per group
- Code examples for all patterns with expected output showing window function results alongside source data
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 11: Spark SQL Query Optimization and Execution Plans

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a guide on Spark SQL query optimization relevant to the exam:
- Catalyst optimizer pipeline: parse → analyze → logical optimize → physical plan → code generation
- Key Catalyst optimizations to recognize: predicate pushdown, column pruning, constant folding, join reordering
- Adaptive Query Execution (AQE): what it is, which problems it solves (skew join, coalescing shuffle partitions), how to enable/disable
- How to call EXPLAIN in PySpark: df.explain(), df.explain(True), df.explain(mode='formatted'), df.explain(mode='cost')
- Reading EXPLAIN output: identifying the physical plan, recognizing a BroadcastHashJoin vs SortMergeJoin
- How partitioning at write time enables partition pruning at read time
- Practical examples: run EXPLAIN before and after adding a filter to show predicate pushdown in action
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

---

### TOPIC 3: Developing Spark DataFrame/DataSet API Applications (30%)

> **Exam weight: 30% — ~14 questions out of 45.** This is the highest-weighted topic. Cover every subtopic in the exam overview: selecting, renaming, manipulating columns; filtering, dropping, sorting, aggregating rows; missing data; combining, reading, writing, partitioning DataFrames; schemas; UDFs.

#### Prompt 12: DataFrame Creation, Column Selection and Renaming

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a practical guide on creating DataFrames and working with their columns for the exam:
- **Creating DataFrames in-memory** (fundamental — tested as setup in code-reading questions):
  - `spark.createDataFrame(data, schema)` from a Python list of tuples: `spark.createDataFrame([(1, 'Alice'), (2, 'Bob')], ['id', 'name'])`
  - `spark.createDataFrame(data, schema)` with an explicit StructType schema
  - `spark.createDataFrame(pandas_df)` to convert a pandas DataFrame to a Spark DataFrame
  - `spark.range(n)` — creates a DataFrame with a single `id` column (LongType); useful for testing and benchmarking
  - `rdd.toDF(column_names)` — creates a DataFrame from an RDD with optional column name list
  - `df.toDF(*new_names)` — returns a new DataFrame with renamed columns (position-based)
- Selecting specific columns with select() using string names, col(), and df['colname'] notation
- Differences between col('name'), df['name'], and df.name — when each fails (e.g., column names with spaces or dots)
- Renaming a column with withColumnRenamed('old', 'new') and alias()
- Using selectExpr() for SQL-style expressions inline: 'age * 2 as double_age'
- Selecting all columns except specific ones using drop()
- Dynamically selecting a list of columns stored in a Python variable
- Handling column names with spaces or special characters using backtick escaping in SQL and col('`col name`')
- Code examples for each approach showing the resulting DataFrame schema and data
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 13: Column Manipulation and Expressions

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a comprehensive guide on transforming column values — a core exam topic:
- Adding a new column or replacing an existing column with withColumn('name', expression)
- Dropping one or more columns with drop()
- Casting column types with .cast() and .astype(): StringType → IntegerType, StringType → DateType, DoubleType → IntegerType — common pitfalls with nulls on bad casts
- Creating expressions with col(), lit(), expr() — explain the difference between each
- Conditional column logic with when(condition, value).when(...).otherwise(default) — single and nested conditions
- Arithmetic expressions on columns: addition, subtraction, multiplication, division, modulo
- String transformations applied to a column using built-in functions (upper, lower, trim, concat, split, substring)
- Rounding and numeric formatting
- Code examples for every operation with printSchema() and show() output confirming the result
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 14: Data Filtering and Row Manipulation

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a comprehensive guide covering row-level operations:
- Filtering rows with filter() and where() — confirm they are aliases
- Single-condition filter, AND (&), OR (|), NOT (~) with column expressions
- Filtering using SQL string expressions with expr(): filter(expr('age > 18 AND city = \"London\"'))
- isin() and ~isin() for membership filtering against a Python list
- String-based row filters: startswith(), endswith(), contains(), like() (SQL LIKE), rlike() (regex)
- Dropping duplicate rows with distinct() and dropDuplicates() — difference between the two
- Sorting ascending and descending with sort() and orderBy(), single and multi-column, using asc()/desc(), handling nulls with asc_nulls_first()/desc_nulls_last()
- Limiting result rows with limit()
- Code examples for each pattern including edge cases, with expected row counts and output
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 15: Aggregations and Grouping

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create an in-depth aggregation guide covering exam scenarios:
- groupBy() with one column and multiple columns
- agg() with single and multiple named aggregations using F.alias()
- All aggregate functions from pyspark.sql.functions: sum(), avg(), mean(), min(), max(), count(), countDistinct(), collect_list(), collect_set(), first(), last(), stddev(), variance(), approx_count_distinct()
- Performing multiple aggregations in one agg() call with column aliasing
- Filtering aggregated results: applying filter() after groupBy().agg() (HAVING equivalent)
- Pivot tables with groupBy().pivot('col').agg() — pivoting a column into multiple columns
- Difference between collect_list() (preserves duplicates) and collect_set() (unique only)
- When to use window functions instead of groupBy for running aggregates
- Practical scenarios with sample input data, step-by-step code, and expected output
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 16: Handling Missing Data (NULLs)

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write about detecting and handling missing/null data — tested directly on the exam:
- Identifying null values in a column using isNull() and isNotNull() inside filter()
- Counting nulls per column: df.select([count(when(isnan(c) | col(c).isNull(), c)).alias(c) for c in df.columns])
- Dropping rows with nulls using dropna(): how='any' vs how='all', thresh parameter, subset parameter
- Filling null values with fillna(): single value for all columns, dict for column-specific values
- Replacing specific values (including nulls) with replace()
- Using coalesce(col1, col2, ...) to return first non-null value in an expression
- Distinction between NULL and NaN in Spark — isnan() vs isNull()
- Impact of nulls on aggregations (nulls are ignored by sum/avg/count but counted by count(*))
- Code examples demonstrating each scenario with before/after show() output
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 17: Joining DataFrames

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a comprehensive joining guide covering all join types tested in the exam:
- Join syntax: df1.join(df2, on=condition, how='join_type')
- All join types with explanation and example: inner, left (left_outer), right (right_outer), full (outer / full_outer), left_semi, left_anti, cross
- Difference between left_semi (only left rows with a match) and left_anti (only left rows without a match) — exam-favorite question
- Join conditions: single column string shorthand, list of column names, explicit column expression, complex multi-condition
- Handling duplicate column names after joins: drop() the duplicate, alias before join, or select specific columns
- Broadcast joins: from pyspark.sql.functions import broadcast; df1.join(broadcast(df2), ...)
- spark.sql.autoBroadcastJoinThreshold configuration
- Self-joins using aliases: df.alias('a').join(df.alias('b'), ...)
- Code examples for every join type showing input data, join, and expected output with row counts
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 18: Combining DataFrames — Union, Intersect, Except

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a complete guide on set-based and vertical combining operations — exam topic often missed:
- union(other): combines all rows from both DataFrames (includes duplicates), requires same column count and order
- unionAll(other): alias for union() in Spark 2+; explain the history
- unionByName(other): aligns columns by name instead of position — use when schemas have same columns in different order; allowMissingColumns=True parameter
- intersect(other): returns rows present in both DataFrames (deduplicates automatically)
- intersectAll(other): returns common rows preserving duplicates
- except(other) / subtract(other): returns rows in first DataFrame not present in second (deduplicates automatically)
- exceptAll(other): returns rows in first not in second, preserving duplicates
- When deduplication is implicit (intersect, except) vs when you must call distinct() manually
- Practical scenarios: stacking monthly datasets with union, finding shared records with intersect, identifying new records with except
- Code examples with sample DataFrames and expected output showing exact row counts and data
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 19: Reading and Writing DataFrames

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a complete I/O reference covering all formats and options tested on the exam:
- **Reading**: spark.read.format('csv').option(...).load(path) vs spark.read.csv(path, ...)
  - CSV options: header, inferSchema, sep/delimiter, nullValue, nanValue, dateFormat, timestampFormat, encoding, multiLine
  - JSON options: multiLine, allowSingleQuotes, dateFormat
  - Parquet: typically no options needed; schema stored in file
  - Delta: spark.read.format('delta').load(path) and spark.read.table('table_name')
  - Reading multiple files: glob patterns, directory path, list of paths
- **Writing**: df.write.format('parquet').mode('overwrite').save(path)
  - Save modes: overwrite, append, ignore, errorIfExists (default) — behavior of each
  - partitionBy('col') effect on output directory structure and partition pruning on read
  - Writing Delta format and registering as table with saveAsTable()
- **Schema**: explicit StructType schema vs inferSchema=True — performance and reliability trade-offs
- Code examples for each format with read options, write modes, and Delta format
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 20: DataFrame Partitioning — Repartition and Coalesce

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a guide on managing DataFrame partitions for the exam:
- What a partition is in Spark and its relationship to parallelism and task count
- Checking partition count: df.rdd.getNumPartitions()
- repartition(n): full shuffle to redistribute data into exactly n partitions; can increase or decrease; evenly balanced
- repartition(n, col): repartition by column value (hash partitioning); puts same key values in same partition — useful before joins
- coalesce(n): reduces partitions with minimal shuffle (no full shuffle); cannot increase partitions; may produce uneven partitions
- When to use repartition vs coalesce: increasing parallelism → repartition; reducing before write → coalesce
- How partitionBy('col') in write differs from repartition(col): partitionBy writes directory structure; repartition changes in-memory partitions
- Impact on output: coalesce(1) before write to get a single output file
- Code examples: check partition count, repartition, coalesce, write with partitionBy, read back with partition pruning
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 21: Schemas and Data Types

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write about defining, enforcing, and working with schemas for the exam:
- StructType and StructField: constructing schemas in Python with types from pyspark.sql.types
- All Spark data types to know: StringType, IntegerType, LongType, FloatType, DoubleType, DecimalType, BooleanType, DateType, TimestampType, BinaryType, NullType
- Complex types: ArrayType(elementType), MapType(keyType, valueType), StructType (nested)
- Creating an explicit schema and applying it: spark.read.schema(my_schema).csv(path)
- printSchema() output interpretation: field names, types, nullable flag
- Schema inference with inferSchema=True — limitations and when it gets types wrong (e.g., integers read as strings)
- Changing column types after read with cast(): handling nulls when cast fails (returns null, not error)
- Accessing nested StructType fields: df['address.city'] or df.address.city
- Accessing ArrayType elements: df['tags'][0], explode()
- Code examples: define schema, read with schema, printSchema output, cast, access nested fields
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 22: User Defined Functions (UDFs)

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a comprehensive UDF guide covering everything tested on the exam:
- Defining a Python UDF using udf() with explicit return type: my_udf = udf(lambda x: x.upper(), StringType())
- Applying a UDF to a DataFrame column: df.withColumn('new', my_udf(col('existing')))
- Registering a UDF for use in Spark SQL strings: spark.udf.register('my_udf', my_func, StringType())
- Using registered UDF in spark.sql() and selectExpr()
- UDF return types: simple types and complex types (ArrayType, StructType)
- Why UDFs are slower than built-in functions: Python serialization, no Catalyst optimization, row-by-row execution
- Pandas UDFs (Vectorized UDFs) using @pandas_udf decorator from pyspark.sql.functions:
  - Series → Series UDF (SCALAR type): operates on pandas Series, much faster
  - Input/output type annotation required
- When to use: built-in function → UDF → Pandas UDF (prefer built-ins when available)
- Null handling inside UDFs: always guard against None inputs
- Code examples: simple UDF, UDF with complex return, Pandas UDF, SQL-registered UDF, performance comparison
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 23: Complex End-to-End Data Manipulation Scenarios

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create three realistic exam-style pipeline scenarios that chain multiple DataFrame operations:
- **Scenario 1 — Sales Analytics**: Read a partitioned CSV dataset with an explicit schema → cast and clean types → fillna for missing regions → filter for the current year → groupBy region and product, agg total sales and avg price → rank products by sales within each region using a window function → write results to Parquet partitioned by region
- **Scenario 2 — Log Processing**: Read JSON logs → explode an array column → filter for ERROR level → extract fields with regexp_extract → join with a user reference table (broadcast join) → deduplicate with dropDuplicates → count errors per user per day → write to Delta format
- **Scenario 3 — Data Quality & Deduplication**: Read two monthly snapshot DataFrames → union with unionByName → identify new records with except → check for nulls in key columns → fill nulls with defaults → cast all date strings to DateType → add a processed_date column with current_date() → write as a single Parquet file using coalesce(1)
- For each scenario provide: sample data creation code, full step-by-step PySpark code, intermediate show()/printSchema() outputs at key steps, final result, and one explain() call to show the physical plan
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

---

### TOPIC 4: Troubleshooting and Tuning Spark Applications (10%)

> **Exam weight: 10% — ~5 questions out of 45.** Focus on identifying bottlenecks, knowing key configuration properties, and recognizing common error types.

#### Prompt 24: Performance Tuning Techniques

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a comprehensive tuning guide targeting the exam:
- **Spark UI navigation**: Jobs tab (job status), Stages tab (stage timeline, shuffle read/write), Tasks tab (per-task duration, skew detection), SQL tab (plan visualization), Storage tab (cached RDDs/DataFrames), Environment tab (configuration)
- **Memory model**: driver memory (spark.driver.memory), executor memory (spark.executor.memory), executor memory overhead (spark.executor.memoryOverhead), storage vs execution memory split
- **Key configuration properties**: spark.executor.cores, spark.default.parallelism, spark.sql.shuffle.partitions (default 200 — know when to change it), spark.sql.autoBroadcastJoinThreshold
- **Caching**: cache() (default MEMORY_AND_DISK) vs persist(StorageLevel): MEMORY_ONLY, MEMORY_AND_DISK, MEMORY_ONLY_SER, DISK_ONLY — when to unpersist()
- **Shuffle reduction**: avoid wide transformations when possible, repartition before joins on join key, broadcast small DataFrames
- **Adaptive Query Execution (AQE)**: spark.sql.adaptive.enabled — coalesces small shuffle partitions, optimizes skew joins at runtime
- **Data skew**: detecting skewed tasks in Spark UI, salting technique to redistribute skewed keys
- Code examples showing configuration in SparkSession.builder and via spark.conf.set()
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 25: Common Spark Errors and Failure Scenarios

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a troubleshooting reference for error types that appear on the exam:
- **OutOfMemoryError (OOM)**: driver OOM (too much data collected with collect()/toPandas()) vs executor OOM (too large a partition); fix with repartition, increase memory, avoid collect() on large datasets
- **AnalysisException — Column not found**: referencing a column that doesn't exist or was dropped; misspelled column name; column ambiguity after join (two columns with the same name)
- **AnalysisException — Ambiguous column reference**: caused by joining two DataFrames with identically named columns without disambiguation; fix by aliasing DataFrames or dropping duplicate columns
- **Task not serializable**: a lambda or UDF captures a non-serializable object (e.g., a Spark context, database connection); fix by moving the reference inside the function
- **StackOverflowError**: very long lineage chains (many iterative transformations without checkpointing); fix with df.checkpoint() or df.persist()
- **Data skew causing stragglers**: one task takes 10× longer than others; identify in Spark UI Tasks tab; fix with AQE, salting, or repartition by join key
- **Schema mismatch**: reading data where the actual schema differs from expected; columns missing or in wrong order; fix with explicit schema or unionByName
- **NullPointerException in UDF**: UDF receives a null input it doesn't handle; always check for None in UDF body
- For each error: how to reproduce it with a short code example, how to identify it in logs or Spark UI, and the correct fix
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 26: Debugging Spark Applications

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write about debugging and monitoring approaches relevant to the exam:
- Reading the Spark UI: how to access it, what each tab shows, how to navigate from a failed job to the failed stage to the failed task and find the error message
- Interpreting explain() output to understand why a query is slow (no predicate pushdown, SortMergeJoin when BroadcastHashJoin expected)
- Using df.explain(mode='formatted') for a readable physical plan
- Logging in PySpark applications: import logging; set log level with sc.setLogLevel('WARN')
- Intermediate inspection techniques: df.show(5, truncate=False), df.count(), df.printSchema() at pipeline steps to find where data breaks
- Identifying data skew from Spark UI Task metrics: compare median vs max task duration
- Debugging UDFs: print statements inside UDF (visible in executor logs), wrapping UDF body in try/except to surface errors
- Common pattern: cache() a DataFrame after an expensive step and inspect it before proceeding with more transformations
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

---

### TOPIC 5: Structured Streaming (10%)

> **Exam weight: 10% — ~5 questions out of 45.** Focus on the streaming DataFrame API, output modes, triggers, and watermarking.

#### Prompt 27: Structured Streaming Fundamentals

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a guide on the core Structured Streaming API for the exam:
- What Structured Streaming is: an extension of the Spark SQL engine that treats a live data stream as an unbounded table
- How the streaming DataFrame API mirrors the batch DataFrame API — same transformations apply
- Input sources and how to read them: socket (for testing), file source (CSV/JSON/Parquet in a directory), Kafka, rate source (for benchmarking)
- Output sinks: console (for testing), file (write to directory), memory (query as a table), Kafka, Delta, foreachBatch
- Output modes — know each one and when it is valid:
  - append: only new rows added since last trigger (valid for stateless queries and aggregations with watermark)
  - complete: all rows of the result table every trigger (valid for aggregations without watermark)
  - update: only rows that changed since last trigger (valid for aggregations)
- Trigger modes: default micro-batch, Trigger.ProcessingTime('10 seconds'), Trigger.Once(), Trigger.AvailableNow(), Trigger.Continuous('1 second')
- Starting and stopping a stream: query = df.writeStream.format('console').start(); query.awaitTermination(); query.stop()
- Code example: read from socket source, split lines into words, count words, write to console with complete output mode
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

#### Prompt 28: Stateful Streaming — Windows and Watermarking

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a guide on stateful streaming operations — the advanced exam streaming topic:
- Difference between stateless transformations (filter, select, map — applied per row) and stateful operations (groupBy, aggregations, deduplication — require state across rows)
- **Tumbling windows**: non-overlapping fixed windows using window(timeColumn, windowDuration); e.g., count events per 10-minute window
- **Sliding windows**: overlapping windows using window(timeColumn, windowDuration, slideDuration); e.g., events in the last 10 minutes updated every 5 minutes
- **Session windows**: dynamic duration windows that group events with a gap threshold
- **Watermarking with withWatermark(timeColumn, delayThreshold)**: tells Spark how late data can arrive; enables state cleanup; required for append output mode with aggregations
- Watermark and output mode compatibility: complete (no watermark needed), update (watermark optional), append (watermark required for aggregations)
- Stateful deduplication: streaming_df.withWatermark(...).dropDuplicates(['id', 'timestamp'])
- Code examples: tumbling window word count with watermark in update mode, sliding window with console sink, deduplication in a streaming pipeline
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

---

### TOPIC 6: Using Spark Connect to Deploy Applications (5%)

> **Exam weight: 5% — ~2 questions out of 45.** Understand the architecture and how to connect.

#### Prompt 29: Spark Connect — Architecture and Usage

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Write a focused guide on Spark Connect for the exam:
- **What Spark Connect is**: a new client-server architecture (introduced in Spark 3.4) that decouples the client-side API from the Spark cluster — a thin client model
- **How it differs from the classic SparkContext approach**: classic approach embeds the Spark driver in the application process; Spark Connect runs the driver on the cluster and communicates over a network protocol
- **Protocol**: gRPC-based communication using Protocol Buffers — the client sends a logical plan; the server executes it
- **Benefits**: language-agnostic clients, independent client/server versioning, improved stability (client crash doesn't kill the cluster), works from IDEs, notebooks, and microservices without a full Spark installation
- **Starting a Spark Connect server**: ./sbin/start-connect-server.sh
- **Connecting from Python**: SparkSession.builder.remote('sc://localhost:15002').getOrCreate()
- **Supported operations**: all standard DataFrame API operations, Spark SQL, UDFs
- **Current limitations to be aware of**: SparkContext is not available on the client; RDD API not supported via Spark Connect
- Code example: create a SparkSession with Spark Connect, read a CSV, run a groupBy aggregation, show results
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

---

### TOPIC 7: Using Pandas API on Apache Spark (5%)

> **Exam weight: 5% — ~2 questions out of 45.** Focus on the API differences, conversion methods, and trade-offs.

#### Prompt 30: Pandas API on Apache Spark (pyspark.pandas)

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a guide on the Pandas API on Apache Spark for the exam:
- **What it is**: a pandas-compatible API that runs on Spark, enabling pandas-style code to process distributed data; previously called Koalas
- **Import pattern**: import pyspark.pandas as ps (not pandas as pd)
- **Creating a Spark pandas DataFrame**: ps.DataFrame(data), ps.read_csv(path), ps.from_pandas(pandas_df)
- **Converting between types**:
  - Spark pandas → native pandas: psdf.to_pandas() (triggers full collect — use only for small DataFrames)
  - Spark DataFrame → Spark pandas: spark_df.pandas_api() or ps.from_spark(spark_df)
  - Spark pandas → Spark DataFrame: psdf.to_spark()
- **Key behavioral differences from native pandas**:
  - Index behavior: Spark pandas has a default index that may be different from pandas; use default_index_type setting
  - No guaranteed row ordering without explicit sort
  - Some pandas operations are not supported or behave differently (e.g., no in-place operations)
  - Operations that require full data collection are expensive
- **Performance considerations**: when the Pandas API adds overhead vs when it's beneficial for migrating existing pandas code
- **Common operations**: groupby().agg(), merge(), pivot_table(), apply(), value_counts(), describe()
- Code examples comparing equivalent code in: native pandas, Pandas API on Spark (pyspark.pandas), and native PySpark DataFrame API — show the same operation written three ways
- Generate 5 practical real-world scenarios to thoroughly demonstrate the concepts above. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the situation, any relevant PySpark code or CLI commands, the expected outcome, and which exam sub-topic is being demonstrated."

---

### EXAM PRACTICE

#### Prompt 31: Practice Exam Questions — All Topics

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Generate 20 realistic multiple-choice practice questions for the Databricks Certified Associate Developer for Apache Spark exam. Format each question as:

**Q{N}: [Question text]**
A) ...
B) ...
C) ...
D) ...
**Answer: [Letter]**
**Explanation**: [2–3 sentences explaining why the answer is correct and why the others are wrong]

Distribution matching exam weights:
- 4 questions on Spark Architecture (Prompts 1–7 topics)
- 4 questions on Spark SQL and functions (Prompts 8–11 topics)
- 6 questions on DataFrame API operations (Prompts 12–23 topics)
- 2 questions on Troubleshooting and Tuning (Prompts 24–26 topics)
- 2 questions on Structured Streaming (Prompts 27–28 topics)
- 1 question on Spark Connect (Prompt 29 topic)
- 1 question on Pandas API on Spark (Prompt 30 topic)

Include a mix of:
- Conceptual questions (what does X do, what is the difference between A and B)
- Code-reading questions (what does this code output, what error will this produce)
- Best-practice questions (which approach is most efficient for X scenario)
- Edge-case questions (what happens when nulls are present, what happens when schemas differ)
- Generate 5 practical real-world scenarios that illustrate common developer mistakes or tricky situations where knowing the correct exam answer matters. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise situation title and the `<details>` body for the developer context, the exam-style question it raises, the correct answer, and why the other options are wrong."

#### Prompt 32: Hands-On Capstone Project

> **Output Directory**: Save all generated files (notebooks, scripts, notes, exercises) to `databricks\learning\`

"Create a complete capstone project that exercises all 7 exam topic areas in one cohesive pipeline:

**Dataset**: Ride-sharing trip data. Create it in-memory with the following columns:
trip_id (string), driver_id (string), rider_id (string), pickup_time (string — needs casting), dropoff_time (string — needs casting), fare_amount (double), distance_miles (double), status (string), city (string), tip_amount (double, with some nulls)

**Required tasks — implement each with full PySpark code**:
1. Define an explicit StructType schema and read/create the DataFrame with it
2. Inspect with printSchema() and show(); identify null columns and data type issues
3. Cast pickup_time and dropoff_time strings to TimestampType
4. Fill null tip_amount values with 0.0; drop rows where fare_amount is null
5. Filter for completed trips only; sort by fare_amount descending
6. Aggregate: total trips, total revenue, average fare, and average distance per driver
7. Rank drivers by total revenue within each city using a window function
8. Write a UDF that categorizes fare_amount into 'low' (< $10), 'medium' ($10–$30), 'high' (> $30) and apply it as a new column
9. Register the result as a temporary view and query it with spark.sql() to find the top 5 highest-revenue drivers
10. Join with a small driver_details DataFrame (driver_id, name, rating) using a broadcast join
11. Write the final result to Parquet partitioned by city, then read it back and confirm partition pruning with explain()
12. Demonstrate one Structured Streaming equivalent: simulate reading new trip records from a rate source and computing a running fare total per city

For each step: include the code, a show()/printSchema() output, and one brief note on what exam concept it demonstrates

After completing all 12 tasks, generate 5 additional real-world scenarios that extend the capstone — for example, handling late-arriving records in the streaming component, recovering from a corrupt Parquet partition, or diagnosing data skew in the driver rankings. Enclose each scenario in HTML `<details>` and `<summary>` tags — use the `<summary>` tag for a concise scenario title and the `<details>` body for the problem description, the PySpark or configuration resolution steps, the expected outcome, and which exam topic it reinforces."

---

## Usage Instructions

**Coverage Map — Prompt to Exam Topic:**

| Exam Topic | Exam Weight | Prompts | # Prompts |
|---|---|---|---|
| Spark Architecture & Components | 20% | 1–7 | 7 |
| Using Spark SQL | 20% | 8–11 | 4 |
| DataFrame API Applications | 30% | 12–23 | 12 |
| Troubleshooting & Tuning | 10% | 24–26 | 3 |
| Structured Streaming | 10% | 27–28 | 2 |
| Spark Connect | 5% | 29 | 1 |
| Pandas API on Spark | 5% | 30 | 1 |
| Exam Practice | Review | 31–32 | 2 |

**Suggested Study Sequence:**

| Week | Focus | Prompts |
|---|---|---|
| 1 | Spark Architecture foundations | 1–7 |
| 2 | Spark SQL — querying, functions, windows, optimization | 8–11 |
| 3 | DataFrame API — columns, filtering, aggregations, nulls | 12–16 |
| 4 | DataFrame API — joins, combining, I/O, partitioning, schemas, UDFs | 17–23 |
| 5 | Troubleshooting, tuning, Streaming, Spark Connect, Pandas API | 24–30 |
| 6 | Practice exam questions and capstone project | 31–32 |

**How to Use Each Prompt:**
1. Feed the prompt directly to an AI tool (GitHub Copilot, ChatGPT, Claude) to generate detailed study content
2. Save all generated files to `databricks\learning\`
3. Run all code examples in a Databricks Community Edition notebook to verify output
3. After reading generated content, ask follow-up questions on any unclear concept
4. After completing each topic area, use Prompt 31 to self-test — filter by topic if needed
5. Complete Prompt 32 capstone as the final hands-on review before your exam date

---
