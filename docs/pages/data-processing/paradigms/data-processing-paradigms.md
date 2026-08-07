# Data Processing Paradigms

---

## Table of Contents
<!-- TOC -->
* [Data Processing Paradigms](#data-processing-paradigms)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Batch Processing](#batch-processing)
  * [ETL (Extract, Transform, Load)](#etl-extract-transform-load)
  * [Data Warehousing](#data-warehousing)
  * [Data Lakes](#data-lakes)
  * [In-Memory Processing](#in-memory-processing)
  * [Data Integration](#data-integration)
  * [MapReduce](#mapreduce)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Data processing paradigms are the fundamental models an architecture can choose for moving and transforming data: process it in large scheduled chunks, or process it continuously as it arrives. That single choice — batch vs. real-time — shapes everything downstream, from storage layout to latency guarantees to operational cost. This page covers the batch side of that split and the supporting patterns around it: how data gets moved and reshaped (ETL), where it gets stored for analysis (warehouses and lakes), how it gets processed fast (in-memory engines), and the foundational model (MapReduce) that made distributed batch processing practical at scale.

---

## Overview

Most data platforms are built on a mix of processing models rather than a single one. Batch processing dominates when correctness, cost-efficiency, and throughput matter more than immediacy — nightly financial reconciliation, monthly billing runs, large-scale analytics. Real-time processing (see [Event Streaming](../real-time/event-streaming.md)) dominates when the business value of data decays quickly — fraud detection, live dashboards, operational alerting.

Historically, batch came first: mainframes processed punch-card jobs overnight decades before anyone talked about "streaming." Google's MapReduce paper (2004) and the Hadoop ecosystem it inspired turned batch processing into a distributed, horizontally scalable discipline that could run over commodity hardware. That lineage still shapes how architects think about large-scale data movement today, even as newer engines (Spark, Flink) and streaming-first architectures have largely displaced hand-written MapReduce jobs.

An architect's job is rarely "pick batch or streaming" — it's identifying, for each data flow in the system, which latency/cost/complexity trade-off applies, and choosing the right combination of the patterns below.

<sub>[Back to top](#table-of-contents)</sub>

---

## Batch Processing

Batch processing collects data over a period of time and processes it as a single, discrete unit of work, rather than as individual events.

- ### The Batch Model:
  A batch job reads a bounded dataset (a file, a table snapshot, a day's worth of records), runs a transformation or computation over the whole set, and writes the result. There is no notion of "now" inside a batch job — it operates on data-as-of-collection-time.

  ```text
  [Source data accumulates] -> [Scheduled trigger] -> [Batch job runs over full/incremental dataset] -> [Output written]
  ```

- ### Batch vs. Real-Time — the fundamental split:
  This is the first architectural fork in most data platforms.

  | Aspect | Batch Processing | Real-Time / Streaming |
  |---|---|---|
  | Data unit | Bounded set (file, table, partition) | Unbounded stream of events |
  | Latency | Minutes to hours (or longer) | Milliseconds to seconds |
  | Throughput optimization | High — large sequential reads/writes | Moderate — optimized for low latency |
  | Typical trigger | Schedule (cron, orchestrator) | Continuous consumption |
  | Failure recovery | Re-run the whole/partial job | Replay from offset/checkpoint |
  | Example use case | Nightly billing run, data warehouse load | Fraud detection, live dashboards |

  See [Event Streaming](../real-time/event-streaming.md) for the real-time side of this trade-off.

<sub>[Back to top](#table-of-contents)</sub>

---

## ETL (Extract, Transform, Load)

ETL is the classic pattern for moving data from operational systems into an analytical store, and it's usually implemented as a batch process.

- ### The Three Stages:
  **Extract** pulls raw data from source systems (application databases, APIs, logs, files). **Transform** cleans, reshapes, validates, and enriches that data into a target schema. **Load** writes the transformed data into its destination, typically a data warehouse.

  ```text
  Sources -> Extract -> Transform -> Load -> Data Warehouse
  ```

- ### ETL vs. ELT:
  Modern cloud data platforms often flip the order to ELT — Extract, Load, then Transform — loading raw data first and transforming it inside the destination system (using its own compute, e.g. a warehouse's SQL engine). ELT trades upfront transformation discipline for flexibility: raw data is preserved, and transformation logic can be iterated on without re-extracting from source. This shift is closely tied to the rise of [Data Lakes](#data-lakes) as a landing zone for raw data before it's modeled.

<sub>[Back to top](#table-of-contents)</sub>

---

## Data Warehousing

A data warehouse is a centralized repository designed for structured, query-optimized storage of data intended for reporting and analytics.

- ### Schema-on-Write and OLAP:
  Warehouses enforce a schema at write time — data is modeled (often in star or snowflake schemas) before it lands, which makes queries fast and predictable but requires upfront design work. Warehouses are optimized for OLAP (Online Analytical Processing): large aggregate queries over historical data, rather than the high-frequency small reads/writes typical of OLTP systems (see [Relational Databases (SQL)](../sql/relational.md)).

- ### Data Warehouses vs. Data Lakes:
  This is one of the more consequential architectural decisions in a data platform, and the two are often used together rather than as an either/or choice.

  | Aspect | Data Warehouse | Data Lake |
  |---|---|---|
  | Schema | Schema-on-write (modeled before load) | Schema-on-read (modeled at query time) |
  | Data types | Structured, cleaned, curated | Structured, semi-structured, unstructured (raw) |
  | Primary users | Analysts, BI tools | Data engineers, data scientists, ML pipelines |
  | Cost profile | Higher cost per byte, optimized compute | Lower-cost storage, compute decoupled |
  | Query performance | Fast, predictable (pre-modeled) | Variable — depends on tooling and layout |
  | Governance | Strong, enforced at load time | Weaker by default — risk of becoming a "data swamp" |
  | Typical use | Dashboards, financial reporting | ML training data, exploratory analytics, archival |

  A common modern pattern loads raw data into a lake first, then curates a subset into a warehouse (or a "lakehouse" that blends both) for governed reporting.

<sub>[Back to top](#table-of-contents)</sub>

---

## Data Lakes

A data lake stores data in its raw, native format — files, logs, JSON blobs, images, whatever the source produces — without imposing a schema until read time.

- ### Schema-on-Read:
  Structure is applied by the consumer at query or processing time, not by the storage layer. This gives maximum flexibility (any data can be ingested immediately, without a modeling exercise) at the cost of weaker guarantees — a query can fail or misinterpret data if the reader's assumed schema doesn't match reality.

- ### Governance Risk:
  Because ingestion is cheap and unconstrained, lakes can degrade into "data swamps" — large volumes of undocumented, low-trust data — without deliberate cataloging, access control, and lifecycle policies. This is the main operational trade-off against a warehouse's enforced structure.

<sub>[Back to top](#table-of-contents)</sub>

---

## In-Memory Processing

In-memory processing engines keep working data in RAM across computation steps instead of round-tripping to disk between each stage.

- ### Speed vs. Durability Trade-off:
  Disk I/O is the dominant cost in traditional batch frameworks like early Hadoop MapReduce, which persists intermediate results between each map and reduce step. In-memory engines such as Apache Spark instead hold intermediate datasets in RAM (spilling to disk only when memory is exhausted), which can make iterative workloads — machine learning training, graph algorithms, interactive queries — orders of magnitude faster. The trade-off is durability and cost: RAM is volatile and expensive relative to disk, so in-memory systems need replication or checkpointing strategies to tolerate node failures without losing in-flight computation.

<sub>[Back to top](#table-of-contents)</sub>

---

## Data Integration

Data integration is the broader discipline of combining data from disparate sources into a unified, consistent view — ETL is one specific technique within it.

- ### Beyond ETL:
  Data integration also covers patterns like data virtualization (querying multiple sources live without physically moving data), change data capture (streaming row-level changes out of operational databases), API-based integration, and master data management (reconciling conflicting representations of the same entity across systems). An architect chooses among these based on latency needs, source system constraints, and how much duplication of data the organization can tolerate.

<sub>[Back to top](#table-of-contents)</sub>

---

## MapReduce

MapReduce is a programming model for processing large datasets in parallel across a distributed cluster, popularized by Google's 2004 paper and later implemented in Apache Hadoop.

- ### Map, Shuffle, Reduce:
  A MapReduce job has three conceptual phases. **Map** applies a function to each input record independently, producing intermediate key-value pairs. **Shuffle** groups all values by key across the cluster, redistributing data between nodes. **Reduce** aggregates the values for each key into a final result.

  ```mermaid
  flowchart LR
      A[Input Data] --> B[Map]
      B --> C[Shuffle / Group by Key]
      C --> D[Reduce]
      D --> E[Output]
  ```

  **Caption:** The three-phase MapReduce pipeline — independent per-record mapping, key-based redistribution, then per-key aggregation.

- ### Word Count Example:
  The canonical illustration of MapReduce: counting word occurrences across a large set of documents.

  ```text
  Input:   "the cat sat", "the dog ran"

  Map:     (the,1) (cat,1) (sat,1) (the,1) (dog,1) (ran,1)

  Shuffle: the -> [1,1]   cat -> [1]   sat -> [1]   dog -> [1]   ran -> [1]

  Reduce:  (the,2) (cat,1) (sat,1) (dog,1) (ran,1)
  ```

- ### Why It Still Matters:
  Few teams hand-write raw MapReduce jobs today — Spark, Flink, and managed query engines offer higher-level, faster abstractions. But MapReduce's core idea — decompose a problem into independent per-record work, then aggregate by key across a cluster — underlies how most distributed data-processing engines still reason about parallelism, and it's worth understanding as the model that made "just add more commodity machines" a viable batch-processing strategy.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If Spark is faster, why would anyone still choose a plain batch/MapReduce-style approach?**
A: Raw throughput-per-dollar on very large, non-iterative jobs (a single pass over petabytes) can still favor simpler, disk-based batch pipelines, and some managed data warehouse engines are effectively hidden, highly optimized batch processors under the hood. The choice is rarely "MapReduce vs. Spark" directly anymore — it's about which managed engine or framework best fits the job's access pattern.

---

**Q: Should a new system default to a data warehouse or a data lake?**
A: Neither, exclusively — most architectures land raw data in a lake first (cheap, flexible ingestion) and curate a governed subset into a warehouse or lakehouse for reporting. Defaulting straight to a warehouse forces schema decisions before you understand the data; defaulting to a lake-only approach risks ungoverned sprawl.

---

**Q: Where does ETL fit relative to data integration as a whole?**
A: ETL is one technique for data integration — specifically, batch-oriented, scheduled movement and transformation of data into a target store. Data integration also includes change data capture, live data virtualization, and API-based integration, which an architect reaches for when ETL's batch latency or full-copy approach doesn't fit the requirement.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Specialized Data Workloads](specialized-data-workloads.md) — ML, NLP, and other workload types that typically run downstream of the paradigms on this page
- [Event Streaming](../real-time/event-streaming.md) — the real-time counterpart to batch processing
- [Distributed Consistency](../db-concepts/distributed-consistency.md) — consistency trade-offs that apply when data integration spans distributed stores
- [NoSQL (Not Only SQL) Database](../nosql/nosql.md) — a common storage target for raw, semi-structured data in a data lake
- [Relational Databases (SQL)](../sql/relational.md) — the OLTP source systems that ETL/ELT pipelines typically extract from

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [What is MapReduce? (Google Research)](https://research.google/pubs/mapreduce-simplified-data-processing-on-large-clusters/) — the original MapReduce paper
- [Apache Spark: Overview](https://spark.apache.org/docs/latest/) — official documentation for the dominant in-memory processing engine

---

[Get Started](../../../get-started.md) | [Data Processing](../../../get-started.md#data-processing)

---
