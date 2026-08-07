# Isolation Levels & MVCC

---

## Table of Contents
<!-- TOC -->
* [Isolation Levels & MVCC](#isolation-levels--mvcc)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Read Committed Isolation](#read-committed-isolation)
  * [Snapshot Isolation](#snapshot-isolation)
  * [MVCC (Multi-Version Concurrency Control)](#mvcc-multi-version-concurrency-control)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

**Isolation** is the "I" in [ACID](acid.md) — it defines how much one transaction is allowed to see of another transaction's uncommitted or concurrently-changing work. The SQL standard defines four isolation levels, each permitting fewer read anomalies than the last, at the cost of more coordination. Snapshot Isolation sits outside that original spectrum as a stronger, widely-implemented alternative, and MVCC is the storage-engine mechanism that makes snapshot-style isolation cheap enough to use as a database's *default* behavior rather than an expensive special case.

---

## Overview

Every transactional database has to answer the same question: when transaction A is reading data that transaction B is concurrently modifying, what is A allowed to see? Full isolation — behaving as if transactions ran one after another with no overlap — is the safest answer, but it is also the most expensive, because guaranteeing it typically means blocking transactions against each other. The SQL standard responds by defining four **isolation levels** (Read Uncommitted, Read Committed, Repeatable Read, Serializable), each trading correctness guarantees for concurrency.

Locking is one way to implement isolation: a transaction takes a lock on a row before reading or writing it, and other transactions wait. But locking makes readers block writers and writers block readers, which collapses throughput under contention. **Multi-Version Concurrency Control (MVCC)** solves this differently — instead of one row having one current value that everyone contends over, the engine keeps multiple versions of each row and hands each transaction the version that was current when *its* view of the database began. Readers never wait on writers, and writers never wait on readers.

This page tells that story in three parts: where **Read Committed** sits on the standard isolation spectrum, how **Snapshot Isolation** goes further than the standard's Repeatable Read without paying for full Serializable, and how **MVCC** is the mechanism — not a level itself — that most production databases use to deliver both.

<sub>[Back to top](#table-of-contents)</sub>

---

## Read Committed Isolation

Read Committed is the default isolation level in most production databases (PostgreSQL, Oracle, SQL Server) because it eliminates the worst anomaly — dirty reads — at a low concurrency cost.

- ### The SQL Standard Spectrum:
  The SQL standard defines isolation levels in terms of three read anomalies they must prevent:

  | Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read |
  |---|---|---|---|
  | Read Uncommitted | Possible | Possible | Possible |
  | Read Committed | Prevented | Possible | Possible |
  | Repeatable Read | Prevented | Prevented | Possible* |
  | Serializable | Prevented | Prevented | Prevented |

  \* Many engines (e.g. MySQL InnoDB) prevent phantom reads at Repeatable Read too, exceeding the strict ANSI SQL requirement — but the standard only mandates it at Serializable.

  - **Dirty read**: reading a row that another transaction has written but not yet committed — if that transaction rolls back, the read was of data that never officially existed.
  - **Non-repeatable read**: re-reading the same row within a transaction and getting a different value because another transaction committed a change in between.
  - **Phantom read**: re-running the same range query within a transaction and getting a different *set* of rows because another transaction inserted or deleted a matching row in between.

- ### What Read Committed Actually Guarantees:
  Under Read Committed, every individual statement sees only data committed before that statement began. Two `SELECT`s in the same transaction can still see different data if another transaction commits in between — that's the non-repeatable read anomaly, and Read Committed explicitly allows it. This is usually an acceptable trade-off: most application code issues one query, uses the result, and moves on, rather than depending on a transaction-long stable view.

<sub>[Back to top](#table-of-contents)</sub>

---

## Snapshot Isolation

Snapshot Isolation is not one of the four original SQL standard levels, but it is what most databases actually provide when you ask for `REPEATABLE READ` or `SERIALIZABLE`.

- ### Where It Sits on the Spectrum:
  Snapshot Isolation is strictly stronger than Read Committed: instead of each *statement* seeing the latest committed data, the entire *transaction* sees one consistent snapshot taken at the moment it started. That snapshot never changes for the life of the transaction, so it prevents dirty reads, non-repeatable reads, *and* phantom reads — matching Serializable's anomaly table above.

  ```mermaid
  flowchart LR
      RU["Read Uncommitted"] --> RC["Read Committed"] --> SI["Snapshot Isolation"] --> SER["Serializable"]
      style SI fill:#4a7,stroke:#333
  ```

  **Caption:** Snapshot Isolation sits between Repeatable Read and Serializable on the strength spectrum — stronger than Read Committed, but not equivalent to true Serializable.

- ### Why It Is Not Serializable — Write Skew:
  Despite matching Serializable on the three named anomalies, Snapshot Isolation permits a subtler anomaly called **write skew**: two transactions each read overlapping data, each independently conclude their write is safe based on what they read, and each commit — but the *combination* of both writes violates an invariant that neither transaction's individual write would have violated alone. True Serializable execution (as if transactions ran one at a time) would never allow this outcome; Snapshot Isolation does, because neither transaction's write set overlaps the other's, so there's nothing for the snapshot check to conflict on.

  > See also: [Distributed Consistency](distributed-consistency.md) — the same "read a snapshot, write without coordination" idea reappears at the distributed-systems level as eventual consistency and CRDTs.

<sub>[Back to top](#table-of-contents)</sub>

---

## MVCC (Multi-Version Concurrency Control)

MVCC is the storage-engine *mechanism* that makes Snapshot Isolation (and often Read Committed too) affordable at scale. It is an implementation technique, not an isolation level in its own right.

- ### Multiple Versions, One Row:
  Instead of updating a row in place, an MVCC engine writes a new version of the row and tags each version with the transaction that created it (and, when superseded, the transaction that replaced it). A single logical row can have many physical versions alive simultaneously, one per transaction still using an older snapshot.

  ```mermaid
  flowchart TD
      subgraph "Row id=42 version chain"
          V1["version 1: balance=100<br/>created by T1"] --> V2["version 2: balance=80<br/>created by T2"] --> V3["version 3: balance=80<br/>created by T3 (current)"]
      end
      TxOld["Transaction with old snapshot"] -.reads.-> V1
      TxNew["Transaction with new snapshot"] -.reads.-> V3
  ```

  **Caption:** Each transaction reads the row version that was visible at the start of its snapshot; old versions stay available until no transaction still needs them.

- ### Readers Never Block Writers:
  Because a reader is simply handed an existing, immutable version of a row, it never needs to wait for a writer to finish, and a writer creating a new version never needs to wait for readers to finish with the old one. This is the single biggest practical win of MVCC over lock-based isolation: read-heavy and write-heavy workloads stop contending with each other.

- ### The Cost — Version Cleanup:
  Old row versions are only garbage once no active transaction's snapshot still needs them. Databases built on MVCC (PostgreSQL's `VACUUM`, for example) must periodically scan for and reclaim dead versions, or storage bloats and query performance degrades. This housekeeping cost is the trade a team makes for lock-free reads.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Dirty read | Reading uncommitted data from another transaction |
| Non-repeatable read | Same row, re-read within a transaction, returns a different value |
| Phantom read | Same range query, re-run within a transaction, returns a different row set |
| Write skew | Two transactions each safely modify disjoint data based on overlapping reads, but the combined result violates an invariant |
| Snapshot | The consistent, point-in-time view of the database a transaction reads from under Snapshot Isolation |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

The classic write skew scenario: a hospital rule requires at least one doctor on call at all times. Two doctors, Alice and Bob, are both on call. Each independently decides to go off call, and each checks the rule first.

```sql
-- Both transactions start with the same snapshot: 2 doctors on call

-- Transaction 1 (Alice)                 -- Transaction 2 (Bob)
BEGIN;                                   BEGIN;
SELECT count(*) FROM doctors             SELECT count(*) FROM doctors
  WHERE on_call = true;                    WHERE on_call = true;
-- sees 2, > 1, safe to go off call      -- sees 2, > 1, safe to go off call
UPDATE doctors SET on_call = false       UPDATE doctors SET on_call = false
  WHERE name = 'Alice';                    WHERE name = 'Bob';
COMMIT;                                  COMMIT;
-- Result: 0 doctors on call — the invariant is violated,
-- even though Snapshot Isolation raised no conflict.
```

Under Snapshot Isolation, both transactions read a snapshot where the invariant held and wrote disjoint rows, so no conflict is ever detected — this is exactly the write skew anomaly described above. Preventing it requires either explicit locking (`SELECT ... FOR UPDATE`) or true Serializable isolation (e.g. PostgreSQL's Serializable Snapshot Isolation, which detects the dependency cycle and aborts one transaction).

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If Snapshot Isolation prevents dirty reads, non-repeatable reads, and phantom reads, why isn't it just Serializable?**
A: Because it permits write skew — two transactions can each read overlapping data, write disjoint data, and both commit, producing a result no serial execution order could produce. Serializable closes that gap, usually by detecting the dependency cycle (Serializable Snapshot Isolation) or by locking predicates.

---

**Q: Does using MVCC automatically give me Snapshot Isolation?**
A: No — MVCC is a mechanism, not a guarantee. PostgreSQL, for instance, uses MVCC to implement plain Read Committed by default, taking a fresh snapshot for every *statement*. You only get full transaction-level Snapshot Isolation when you explicitly request `REPEATABLE READ` or `SERIALIZABLE`.

---

**Q: What's the practical cost of "readers never block writers" in MVCC?**
A: Storage and cleanup overhead. Every update creates a new row version instead of mutating in place, so old versions accumulate until no transaction's snapshot still needs them. Databases need background processes (like PostgreSQL's `VACUUM`) to reclaim them, and a long-running transaction can indirectly cause bloat by holding an old snapshot open.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [ACID](acid.md) — isolation is one of the four ACID properties; this page is a deep dive on the "I"
- [Distributed Consistency](distributed-consistency.md) — the same trade-off between coordination and concurrency reappears across nodes, not just within one database
- [BASE](base.md) — contrasts strong, snapshot-style consistency with the weaker eventual-consistency model

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [PostgreSQL: Transaction Isolation](https://www.postgresql.org/docs/current/transaction-iso.html) — official documentation, including Read Committed, Repeatable Read, and Serializable Snapshot Isolation
- [A Critique of ANSI SQL Isolation Levels (Berenson et al., Microsoft Research)](https://www.microsoft.com/en-us/research/publication/a-critique-of-ansi-sql-isolation-levels/) — the paper that formally defined Snapshot Isolation and write skew

---

[Get Started](../../../get-started.md) |
[Database Concepts](../../../get-started.md#database-concepts)

___
