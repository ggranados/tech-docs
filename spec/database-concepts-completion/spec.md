# Database & Data Processing Concepts Completion

**Severity:** Medium
**Depends on:** `content-template-compliance-audit`

## Problem

`docs/pages/data-processing/db-concepts/` covers ACID, BASE, and CAP well, but `get-started.md` lists sibling concepts with no page: CRDTs, Snapshot Isolation, Two-Phase Commit (2PC), Eventual Consistency, MVCC, Read Committed Isolation, Distributed Databases. Separately, the broader "Data Processing" intro section lists Batch Processing, ETL, Data Warehousing, In-Memory Processing, Data Integration, MapReduce, Machine Learning and AI, Data Cleaning and Transformation, Data Lakes, NLP, Image/Video Processing, Time Series Analysis — none of which have pages (Real-Time Processing/Event Streaming/Kafka is the one area already covered).

## Goal

Round out database consistency/transaction concepts (these are core architect knowledge and closely tied to the existing ACID/BASE/CAP trio), and add baseline breadth coverage for the data-processing paradigms list (batch/ETL/warehousing/lakes) since it's currently a bare bullet list under the Data Processing heading with no linked content at all besides real-time.

## Scope hints

- Priority group (db-concepts, extends existing ACID/BASE/CAP trio): MVCC, Two-Phase Commit, Eventual Consistency, Distributed Databases — these are the concepts most directly load-bearing alongside ACID/BASE/CAP.
- Secondary: CRDTs, Snapshot Isolation, Read Committed Isolation (could be folded into an "Isolation Levels" page together rather than 3 separate pages).
- Data processing paradigms group (separate from db-concepts): a single "Data Processing Paradigms" overview page covering Batch vs. Real-Time vs. Stream, ETL/ELT, Data Warehousing vs. Data Lakes, MapReduce — architect-level overview, not a data-engineering deep dive. ML/AI, NLP, Image/Video Processing, Time Series Analysis are lower priority — brief mentions or defer.
- Directory: extend `docs/pages/data-processing/db-concepts/` and add `docs/pages/data-processing/paradigms/` (or similar) for the second group.

## Out of scope

- SQL/NoSQL specific database product pages (already has dedicated `relational.md`/`nosql.md` overview pages — not expanding to per-product pages).
