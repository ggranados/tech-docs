# Distributed Consistency

---

## Table of Contents
<!-- TOC -->
* [Distributed Consistency](#distributed-consistency)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Two-Phase Commit (2PC)](#two-phase-commit-2pc)
  * [Eventual Consistency](#eventual-consistency)
  * [CRDTs](#crdts)
  * [Distributed Databases](#distributed-databases)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Once data is spread across multiple nodes, a single database's transaction log can no longer guarantee that "all writes succeed or none do." Distributed systems have to choose between two broad strategies: coordinate every node before any write is considered final (**Two-Phase Commit**), or let each node accept writes independently and reconcile differences afterward (**Eventual Consistency**, often implemented with **CRDTs**). Every **Distributed Database** you'll evaluate as an architect has picked a point on this spectrum, and the [CAP theorem](cap.md) explains why no design gets to avoid the choice.

---

## Overview

A local, single-node database enforces atomicity with one write-ahead log and one lock manager — there's exactly one place that knows the true state. Distribute that data across nodes and there is no longer a single source of truth: a write can succeed on node A and fail on node B, or succeed on both but arrive in different orders, or succeed everywhere but not be visible everywhere yet.

Two-Phase Commit answers this by adding coordination: a coordinator asks every participant to *promise* it can commit before anyone actually commits, turning a distributed write into something that behaves like a single atomic operation — at the cost of blocking and a single point of failure. Eventual consistency answers it the opposite way: accept writes locally, propagate them asynchronously, and guarantee only that replicas *converge* once messages stop flowing — trading a temporary consistency gap for availability and lower latency. CRDTs are the clever piece of engineering that makes the eventual-consistency side of that trade-off safe: data structures whose merge operation is mathematically guaranteed to converge, with no coordination and no conflict-resolution logic required. Distributed databases are the products that package one of these strategies (or a hybrid) into something you can actually run.

<sub>[Back to top](#table-of-contents)</sub>

---

## Two-Phase Commit (2PC)

2PC is the classic coordinator-based protocol for making a write atomic across multiple independent resources.

- ### The Two Phases:
  A coordinator drives every participant through two rounds:

  ```mermaid
  sequenceDiagram
      participant C as Coordinator
      participant A as Participant A
      participant B as Participant B

      Note over C,B: Phase 1 — Prepare (vote)
      C->>A: Prepare
      C->>B: Prepare
      A-->>C: Vote YES (locked, logged)
      B-->>C: Vote YES (locked, logged)
      Note over C,B: Phase 2 — Commit
      C->>A: Commit
      C->>B: Commit
      A-->>C: Ack
      B-->>C: Ack
  ```

  **Caption:** Every participant must vote YES and durably log its prepared state before the coordinator tells anyone to commit — if any participant votes NO, the coordinator tells everyone to abort instead.

- ### The Weakness — Coordinator as Single Point of Failure:
  Once a participant votes YES, it must hold its locks and wait — it cannot unilaterally commit or abort, because the coordinator might still tell the others to abort. If the coordinator crashes after some participants have voted but before it sends the final decision, those participants are **blocked**, holding locks indefinitely, until the coordinator recovers. This blocking behavior, combined with the requirement that participants share a coordinator at all, is exactly why microservices architectures — where each service deliberately owns its own database — tend to avoid 2PC.

  > See also: [Distributed Transactions in Microservices](../../architectural-patterns/microservices/distributed-transaction.md) and the [Saga Pattern](../../architectural-patterns/microservices/distributed-transaction/saga.md), the alternative most microservices systems adopt precisely to avoid 2PC's coordinator-blocking problem.

<sub>[Back to top](#table-of-contents)</sub>

---

## Eventual Consistency

Eventual consistency is the strategy at the opposite end of the spectrum from 2PC: instead of coordinating before a write completes, accept the write locally and let replicas catch up asynchronously.

- ### The Core Guarantee:
  If no new writes are made to a given piece of data, all replicas will *eventually* return the same value — but there is no bound on how long "eventually" takes, and a read immediately after a write on a different replica may return stale data. This is the model behind [BASE](base.md) (Basically Available, Soft state, Eventually consistent), the deliberate alternative to ACID-style strong consistency.

- ### Why Systems Choose It:
  Dropping the requirement to coordinate before every write means a node can accept writes and serve reads even while partitioned from the rest of the cluster — trading consistency for availability, exactly as the AP corner of the CAP theorem describes. This is attractive whenever availability and low latency matter more than every reader seeing the absolute latest value (shopping carts, social media feeds, DNS records, CDN caches).

<sub>[Back to top](#table-of-contents)</sub>

---

## CRDTs

**Conflict-free Replicated Data Types** are the mechanism that lets eventually-consistent systems merge divergent replicas *without* any coordination, locking, or conflict-resolution callback — the merge function itself is designed so that any order, any number of times, produces the same converged result.

- ### The Trick — Commutative, Associative, Idempotent Merges:
  A CRDT restricts its operations so that merging two replicas' states is commutative (order doesn't matter), associative (grouping doesn't matter), and idempotent (merging the same update twice is harmless). Mathematically, replica states form a join-semilattice, so repeatedly merging any subset of updates, in any order, converges to the same value — no coordinator needed to decide which write "wins."

- ### Concrete Example — G-Counter:
  A grow-only counter (G-Counter) that must be incremented from multiple nodes without coordination keeps one counter *per node* instead of one shared counter, and defines the merged total as the element-wise maximum across replicas' vectors:

  ```
  Node A increments its own slot: A=[3, 0, 0]
  Node B increments its own slot: B=[0, 5, 0]
  Merge(A, B) = elementwise max = [3, 5, 0]  → total = 8
  ```

  Because each node only ever increments its own slot and merge takes the max per slot, replicas can merge in any order, any number of times, and always converge — no coordination required.

- ### Concrete Example — Last-Write-Wins Register:
  For a single value that different nodes might overwrite concurrently, an LWW-Register attaches a timestamp (or logical clock) to every write and defines merge as "keep the value with the higher timestamp." This is simple and conflict-free by construction, at the cost of silently discarding one of the two concurrent writes rather than reconciling them.

<sub>[Back to top](#table-of-contents)</sub>

---

## Distributed Databases

Distributed databases are the umbrella category of systems that must make one of the trade-offs described above explicit, because the [CAP theorem](cap.md) guarantees they can't avoid it.

- ### CAP Governs the Design Space:
  Every distributed database picks a stance on consistency versus availability *during a network partition*. Systems built around 2PC-style coordination (or consensus protocols like Paxos/Raft for a similar effect) lean CP — strongly consistent, but they can stall or refuse writes when nodes can't reach each other. Systems built around eventual consistency and CRDT-style merges lean AP — always available, but temporarily inconsistent across replicas.

  > See also: [CAP Theorem](cap.md) — the formal framework for why a distributed database can't offer strong consistency and full availability simultaneously once a partition occurs.

- ### It's a Spectrum in Practice:
  Real products rarely sit at a pure extreme. Many distributed databases offer tunable consistency (per-query or per-table choice between strong and eventual reads), use consensus protocols for metadata while allowing eventual consistency for bulk data, or use CRDTs specifically for the fields that need multi-writer availability while keeping other fields strongly consistent. Knowing where 2PC, eventual consistency, and CRDTs sit lets an architect read a database's documentation and understand exactly which trade-off it made, rather than trusting a marketing label like "strongly consistent" at face value.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Coordinator | The process driving a 2PC transaction through prepare and commit phases |
| Blocking | A participant that voted YES but hasn't heard the final decision must hold its locks, potentially indefinitely |
| Convergence | The guarantee that replicas reach the same state once writes and merges stop, central to both eventual consistency and CRDTs |
| Join-semilattice | The mathematical structure (commutative, associative, idempotent merge) that makes a CRDT conflict-free |
| Tunable consistency | A database's ability to choose strong vs. eventual consistency per query or per table rather than system-wide |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

Contrast how a shopping-cart write would be handled under each strategy:

```text
2PC:     Coordinator locks inventory + cart rows across services,
         waits for both to vote YES, then commits both.
         → Strongly consistent, but the request blocks on the
           slower participant and stalls if the coordinator dies
           mid-protocol.

Eventual: Cart service accepts the add-to-cart write locally and
          returns immediately; the change propagates to other
          replicas/services asynchronously.
          → Available and fast, but a read against a different
            replica right after the write might not show the item yet.

CRDT:     Cart is modeled as an OR-Set (a CRDT for sets that
          supports concurrent add/remove). Two devices can add
          items offline; when they reconnect, the sets merge
          conflict-free with no coordinator and no lost updates.
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If 2PC guarantees atomicity across services, why do most microservices architectures avoid it?**
A: Because of blocking. A participant that has voted YES must hold its locks until the coordinator's final decision arrives — if the coordinator crashes or the network partitions at the wrong moment, those resources stay locked indefinitely. It also reintroduces a shared coordination point across services that were deliberately given independent databases. That's why most microservices systems prefer the Saga pattern's eventual-consistency approach instead.

---

**Q: How can CRDTs achieve consistency without any coordination at all?**
A: By restricting the data type's operations so the merge function is commutative, associative, and idempotent — mathematically a join-semilattice. Because merging never depends on order or repetition, any two replicas can exchange updates whenever they happen to connect and are guaranteed to converge, with no need for a coordinator to arbitrate conflicts.

---

**Q: Where does the CAP theorem fit into choosing a distributed database?**
A: CAP explains the underlying reason a choice between 2PC-style and eventual-consistency-style designs is unavoidable: during a network partition, a distributed database can stay strongly consistent (CP) only by refusing some requests, or stay available (AP) only by allowing replicas to temporarily disagree. 2PC and consensus protocols are how CP systems are built; eventual consistency and CRDTs are how AP systems are built.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Isolation Levels & MVCC](isolation-levels.md) — the single-node analog of this trade-off: coordination (locking) versus concurrency (MVCC snapshots) within one database
- [CAP Theorem](cap.md) — the formal trade-off (consistency vs. availability under partition) that every distributed database strategy on this page is a response to
- [BASE](base.md) — the consistency model eventual consistency and CRDT-based systems implement in practice
- [Distributed Transactions in Microservices](../../architectural-patterns/microservices/distributed-transaction.md) — applies the 2PC-vs-eventual-consistency choice specifically to microservices data ownership
- [Saga Pattern](../../architectural-patterns/microservices/distributed-transaction/saga.md) — the eventual-consistency-based alternative to 2PC most microservices systems adopt

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Two-Phase Commit Protocol — Wikipedia](https://en.wikipedia.org/wiki/Two-phase_commit_protocol) — protocol overview and failure modes
- [Conflict-free Replicated Data Types (CRDT.tech)](https://crdt.tech/) — reference site maintained by the researchers behind CRDTs, including the original Shapiro et al. papers

---

[Get Started](../../../get-started.md) |
[Database Concepts](../../../get-started.md#database-concepts)

___
