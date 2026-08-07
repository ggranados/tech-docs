# ACID

## Table of Contents
<!-- TOC -->
* [ACID](#acid)
  * [Table of Contents](#table-of-contents)
  * [Atomicity](#atomicity)
  * [Consistency](#consistency)
  * [Isolation](#isolation)
  * [Durability](#durability)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

ACID is an acronym that stands for **Atomicity**, **Consistency**, **Isolation**, and **Durability**. It is a set of properties or characteristics that ensure the reliable and predictable behavior of database transactions. 


ACID is used to guarantee the integrity of data within a relational database management system (RDBMS) even in the presence of hardware failures, software bugs, or other unexpected issues.

## Atomicity

This property ensures that a _transaction is treated as a single, indivisible unit of work_. 

Either all the changes made within a transaction are committed, or none of them are. 

If a failure occurs during the execution of a transaction, the system will roll back any changes made, maintaining the database in a consistent state.

<sub>[Back to top](#table-of-contents)</sub>

## Consistency

Consistency ensures that a transaction brings the database from one valid state to another. In other words, the data is transformed according to predefined rules and constraints, maintaining its validity and integrity. Any transaction that violates these rules is not allowed to complete.

<sub>[Back to top](#table-of-contents)</sub>

## Isolation

Isolation guarantees that transactions can run concurrently without interfering with each other. Each transaction is isolated from others, and their effects are not visible to other transactions until they are committed. This prevents situations like dirty reads, where one transaction reads uncommitted changes made by another transaction.


<sub>[Back to top](#table-of-contents)</sub>

## Durability
Durability ensures that once a transaction is committed, its changes are permanent and will survive any subsequent failures, including system crashes. Committed data is stored in a way that it can be restored in case of a failure without compromising the integrity of the database.


<sub>[Back to top](#table-of-contents)</sub>


![acid.png](../../../img/acid.png)

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why does isolation matter if atomicity already guarantees all-or-nothing changes?**
A: Atomicity guarantees that a single transaction's changes are all-or-nothing, but isolation governs how concurrent transactions see each other's in-flight changes. Without isolation, even fully atomic transactions can produce dirty reads or lost updates when they run in parallel.

---

**Q: Do NoSQL databases provide ACID guarantees?**
A: Most NoSQL databases relax ACID in favor of the BASE model (basically available, soft state, eventually consistent) to gain availability and horizontal scalability, though some — such as MongoDB or modern Cassandra — offer ACID-like guarantees at the single-document or configurable level.

---

**Q: What isolation level should I use to avoid dirty reads without hurting performance too much?**
A: Read Committed is usually the practical default: it prevents dirty reads while allowing higher concurrency than Serializable. Reach for stricter levels (Repeatable Read, Serializable) only when the specific consistency risk they solve — non-repeatable reads or phantom reads — justifies the added locking and latency cost.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [BASE](base.md) — contrasting model that prioritizes availability over strict consistency
- [CAP Theorem](cap.md) — explains the trade-offs distributed systems make between consistency, availability, and partition tolerance
- [SQL (Relational) Database](../sql/relational.md) — RDBMSs are the primary systems that implement ACID transactions

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://en.wikipedia.org/wiki/ACID
- https://www.mongodb.com/basics/acid-transactions

---

[Get Started](../../../get-started.md) |
[Database Concepts](../../../get-started.md#database-concepts)

___
