# Spark Connect Architecture

> **Databricks Certified Associate Developer for Apache Spark**
> Topic 6 — Spark Connect (6%)

---

## Overview

Spark Connect is a decoupled client-server architecture for Spark, introduced as a preview in Spark 3.3 and stabilised in Spark 3.4+. It replaces the tightly coupled embedded driver model with a network-separated client library.

---

## Classic Spark Architecture vs Spark Connect

### Classic Spark (Pre-Spark Connect)

```
Application Process (Python/JVM)
├── SparkSession
├── SparkContext (sc)        ← directly in application process
├── Driver (JVM)             ← embedded in application process
│   └── Py4J socket          ← Python ↔ JVM bridge
└── → Cluster (Executors)
```

- Driver runs **inside** the application process
- Python commands cross via Py4J JVM socket bridge
- Client and cluster are tightly coupled — same process
- Application crash = driver crash = job fails

### Spark Connect Architecture

```
Client Process (Python/JVM)                    Cluster
├── SparkSession.remote()          ─── gRPC/HTTP2 ───→   Spark Connect Server
├── Logical Plan (Protobuf)        ←──────────────────   Results (Apache Arrow)
│   (built in client process)
└── (No SparkContext, no Py4J)
```

- Driver runs **on the cluster** (not in the application)
- Communication via **gRPC over HTTP/2** + **Protocol Buffers** (Protobuf)
- Results returned as **Apache Arrow** record batches
- Client is a thin library — no JVM required locally

---

## Connecting via Spark Connect

```python
# Classic Spark
spark = SparkSession.builder \
                    .appName('MyApp') \
                    .master('local[*]') \
                    .getOrCreate()

# Spark Connect
spark = SparkSession.remote('sc://my-spark-cluster:15002')
```

The `sc://` URL scheme identifies a Spark Connect endpoint. Port 15002 is the default gRPC port.

---

## What Is and Isn't Available

| Feature | Classic Spark | Spark Connect |
|---------|-------------|--------------|
| DataFrame API | ✅ | ✅ |
| Spark SQL (`spark.sql(...)`) | ✅ | ✅ |
| `SparkContext` (`sc`) | ✅ | ❌ NOT available |
| RDD API | ✅ | ❌ NOT supported |
| `spark.sparkContext` | ✅ | ❌ NOT available |
| Streaming (Structured) | ✅ | ✅ (partial, evolving) |
| UDFs | ✅ | ✅ |
| `spark.range()`, `spark.createDataFrame()` | ✅ | ✅ |

**CRITICAL exam facts:**
- `SparkContext` is NOT available via Spark Connect
- The **RDD API is NOT supported** via Spark Connect
- The **DataFrame API and Spark SQL are fully available**

---

## Communication Protocol Details

| Layer | Technology |
|-------|-----------|
| Transport | gRPC |
| Network protocol | HTTP/2 |
| Data serialisation | Protocol Buffers (Protobuf) — for logical plans |
| Result transfer | Apache Arrow — for columnar data |

---

## Version Compatibility

- Client and server can run **different Spark versions**
- Protocol negotiation handles version differences
- Enables independent upgrades of client library and cluster

---

## Use Cases

| Scenario | Why Spark Connect Helps |
|---------|------------------------|
| Notebook connected to remote cluster | Notebook is lightweight client; driver on cluster |
| IDE (VS Code, PyCharm) to cluster | Local IDE, no local Spark install needed |
| Microservice using Spark | Service calls remote Spark; no embedded driver overhead |
| Multi-tenant cluster sharing | Multiple clients connect to one Spark server |
| Better stability | Client crash doesn't kill driver |

---

## Benefits Summary

1. **Stability:** Application crashes don't kill the Spark session
2. **Decoupled versioning:** Client and server versions can differ
3. **Lightweight client:** No JVM required on client machine
4. **Better IDE support:** Language Server Protocol for Spark types
5. **Multi-language:** Any language with gRPC support can be a client

---

## Exam Checklist

- [ ] Know Spark Connect was preview in 3.3, stable in 3.4+
- [ ] Know Classic Spark: driver embedded in app process, uses Py4J bridge
- [ ] Know Spark Connect: driver on cluster, client connects via gRPC/HTTP2 + Protobuf
- [ ] Know results are returned as **Apache Arrow** record batches
- [ ] Know connection string: `SparkSession.remote('sc://host:15002')`
- [ ] Know **SparkContext is NOT available** via Spark Connect
- [ ] Know **RDD API is NOT supported** via Spark Connect
- [ ] Know DataFrame API and Spark SQL are fully available
- [ ] Know client and server can have different Spark versions

---

[⬅️ topic5-prompt28-stateful-streaming.md](topic5-prompt28-stateful-streaming.md) | **29 / 32** | [Next ➡️ topic7-prompt30-pandas-api.md](topic7-prompt30-pandas-api.md)
