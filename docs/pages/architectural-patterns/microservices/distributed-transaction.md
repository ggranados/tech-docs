# Distributed Transactions in Microservices

---

## Table of Contents
<!-- TOC -->
* [Distributed Transactions in Microservices](#distributed-transactions-in-microservices)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Why ACID Doesn't Span Service Boundaries](#why-acid-doesnt-span-service-boundaries)
  * [Approaches to Distributed Transactions](#approaches-to-distributed-transactions)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

A **distributed transaction** is a business operation that must update data owned by two or more independently deployed microservices as a single, all-or-nothing unit of work. In a monolith this is trivial — one database, one local ACID transaction. Once each service owns its own database (the *Database per Service* pattern), the platform no longer offers a built-in way to guarantee that either all of the updates happen or none of them do. Understanding why this problem exists, and the two broad families of solutions to it, is a prerequisite for the [Saga Pattern](distributed-transaction/saga.md), which is the answer most microservices systems adopt in practice.

---

## Overview

In a single-database application, a transaction wraps several statements and the database engine guarantees atomicity, consistency, isolation, and durability ([ACID](../../data-processing/db-concepts/acid.md)) across all of them via a shared transaction log and lock manager. That guarantee is a property of *one* database engine — it does not extend across a network boundary to a second, independently-owned database.

Microservices architectures deliberately give each service exclusive ownership of its own datastore so that services can be deployed, scaled, and evolved independently. That isolation is what makes the architecture work at organizational scale, but it also means a business process like "place an order" — which might need to reserve inventory, charge a payment method, and schedule a shipment — now touches three separately-owned databases with no shared transaction context. If the payment charge fails after inventory has already been reserved, nothing rolls the reservation back automatically.

Two broad strategies exist to restore consistency in this situation: extend transactional coordination across services (Two-Phase Commit), or accept that data will be briefly inconsistent and design the system to converge to a consistent state over time (eventual consistency, most commonly implemented with the Saga pattern).

<sub>[Back to top](#table-of-contents)</sub>

---

## Why ACID Doesn't Span Service Boundaries

A single-service transaction and a cross-service business operation look similar on paper but rely on fundamentally different guarantees.

- ### No Shared Transaction Log:
  A relational database enforces atomicity using a write-ahead log and lock manager that only knows about the rows inside that one database instance. A second microservice's database has its own, entirely separate log and lock manager. There is no mechanism for one database engine to roll back a commit that already happened in a different database engine.

- ### Service Encapsulation Is the Point:
  The whole reason each microservice owns its own database is to avoid coupling services through shared schema. Any solution that reintroduces a shared transactional resource (like a distributed lock manager spanning services) undermines the independence the architecture was adopted for in the first place.

- ### Failure Is Partial by Default:
  In a distributed system, a request to one service can succeed while a related request to another service fails, times out, or succeeds without the caller ever finding out (the response is lost, not the write). Any consistency strategy has to be designed around these partial-failure modes rather than assuming an all-or-nothing outcome.

  ```mermaid
  sequenceDiagram
      participant Client
      participant OrderService as Order Service<br/>(own DB)
      participant PaymentService as Payment Service<br/>(own DB)

      Client->>OrderService: Place Order
      OrderService->>OrderService: INSERT order (local ACID commit)
      Note over OrderService: Order committed and durable
      OrderService->>PaymentService: Charge payment
      PaymentService->>PaymentService: Attempt charge
      PaymentService-->>OrderService: 500 Error (card declined)
      Note over OrderService,PaymentService: Order already committed;<br/>no local transaction can undo it.<br/>System is now inconsistent.
      OrderService-->>Client: Order created, payment failed
  ```

  **Caption:** The order commit is already durable in its own database by the time the payment call fails — there is no ambient transaction to roll back, so the inconsistency must be resolved explicitly (e.g., with a compensating action).

<sub>[Back to top](#table-of-contents)</sub>

---

## Approaches to Distributed Transactions

Two broad families of solutions exist, trading off consistency guarantees against availability and coupling.

- ### Two-Phase Commit (2PC):
  A transaction coordinator asks every participating service to *prepare* (lock resources and confirm it can commit) in phase one, and only if every participant votes yes does the coordinator tell everyone to *commit* in phase two. This gives strong, immediate (linearizable) consistency, but it requires all participants to hold locks and stay available for the duration of the protocol, and the coordinator itself is a single point of failure and coupling. It is rarely used across microservices because it directly contradicts the goal of independently available, loosely coupled services — a slow or down participant blocks every other participant's resources. 2PC remains common *inside* a single resource manager (e.g., a database's own internal transaction handling) but is largely avoided as a cross-service pattern.

- ### Eventual Consistency:
  Instead of blocking until every participant agrees, each service commits its own local transaction immediately and independently, and the overall business operation is driven to a consistent end state through a sequence of local transactions and, if something fails partway through, explicit compensating actions that undo the effects of the steps that already succeeded. Consumers may observe a transient inconsistent state (e.g., an order marked "pending payment" for a few seconds), but the system is designed to converge. This trades strict, instant consistency for availability and loose coupling — each service only needs to be reachable for its own step, not for the whole operation. This is the approach almost all production microservices systems use, and the [Saga Pattern](distributed-transaction/saga.md) is the standard way to structure it.

  > See also: [BASE](../../data-processing/db-concepts/base.md) and the [CAP Theorem](../../data-processing/db-concepts/cap.md), which formalize the trade-off between consistency and availability that motivates eventual consistency.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Distributed Transaction | A business operation that must atomically update data owned by two or more independently deployed services. |
| Two-Phase Commit (2PC) | A blocking coordination protocol (prepare, then commit) that gives strong consistency at the cost of availability and coupling. |
| Eventual Consistency | A model where data is temporarily inconsistent across services but converges to a consistent state through a defined sequence of steps. |
| Compensating Transaction | An explicit action that semantically undoes the effect of a previously committed local transaction (see [Saga](distributed-transaction/saga.md)). |
| Local Transaction | An ordinary ACID transaction scoped to a single service's own database. |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

Consider an e-commerce checkout that spans three services: Order, Inventory, and Payment, each with its own database. Under 2PC, all three would need to lock their respective rows and wait for a coordinator's final decision — if Payment is slow, Inventory's lock is held open, reducing availability for every other customer trying to buy the same item.

```text
Eventually-consistent flow (no shared lock manager):
1. Order Service:     commit "order = PENDING"        (local ACID transaction)
2. Inventory Service:  commit "reserve stock"           (local ACID transaction)
3. Payment Service:    commit "charge card"             (local ACID transaction)
   -- if step 3 fails --
4. Inventory Service:  commit "release reserved stock"  (compensating transaction)
5. Order Service:      commit "order = CANCELLED"       (compensating transaction)
```

Each numbered step is its own local ACID transaction; consistency across the whole flow is achieved by the *ordering and compensation logic*, not by a single cross-service transaction. This is exactly the coordination problem the [Saga Pattern](distributed-transaction/saga.md) formalizes, using either choreography (event-driven, no central coordinator) or orchestration (a central saga orchestrator directs each step).

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why can't we just use a distributed lock manager or a global transaction coordinator (like XA) across all our microservices?**
A: Technically you can — XA/2PC implementations exist — but doing so reintroduces tight coupling and availability coupling between services that were specifically separated to avoid that. Every participant must stay locked and available for the full duration of the coordinator's decision, so one slow or down service degrades the whole system, and the coordinator becomes a new single point of failure. Most teams accept eventual consistency instead, because it preserves the independent availability that is the main reason to adopt microservices in the first place.

---

**Q: If two services can't share a transaction, how do we ever get correct data back out for reporting or queries that span both?**
A: You generally don't do a live cross-service join. Instead you either use the API Composition pattern (query each service and merge results in the caller) or you replicate the data you need into a read-optimized store using events (as in CQRS or Event Sourcing), and accept that the replica is eventually — not instantly — consistent with the source of truth.

---

**Q: Is 2PC ever the right choice in a microservices system?**
A: It's rarely used across service boundaries, but the underlying idea is not wrong — it's the coupling and availability cost that rules it out at scale. 2PC can still make sense for a small, tightly-scoped set of resources within the same trust and deployment boundary (e.g., coordinating two resource managers behind a single service), where the availability trade-off is acceptable and simpler than building compensation logic.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Saga Pattern](distributed-transaction/saga.md) — the standard pattern for implementing eventually-consistent distributed transactions via local transactions and compensating actions
- [Microservices Architecture](../microservices.md) — the parent architecture whose Database-per-Service principle creates the distributed transaction problem
- [ACID](../../data-processing/db-concepts/acid.md) — the local transaction guarantees that this pattern cannot extend across service boundaries
- [BASE](../../data-processing/db-concepts/base.md) — the consistency model (Basically Available, Soft state, Eventually consistent) that eventually-consistent approaches follow
- [CAP Theorem](../../data-processing/db-concepts/cap.md) — formalizes the consistency-versus-availability trade-off that motivates choosing eventual consistency over 2PC
- [Message-Driven Architecture](../message-driven.md) — the asynchronous communication style most sagas are built on top of
- [Event Sourcing](../message-driven/event-driven/event-sourcing.md) — an event log can double as the record of each local transaction in a saga
- [CQRS](../cqrs.md) — often paired with eventually-consistent read models that reflect the outcome of a distributed transaction

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Pattern: Saga (microservices.io)](https://microservices.io/patterns/data/saga.html) — introduces the distributed transaction problem that motivates the Saga pattern
- [Pattern: Database per Service (microservices.io)](https://microservices.io/patterns/data/database-per-service.html) — the architectural decision that creates the distributed transaction problem
- [Two-Phase Commit Protocol (Jim Gray / distributed systems literature via Microsoft Research)](https://www.microsoft.com/en-us/research/publication/notes-on-database-operating-systems/) — foundational description of 2PC
- [Base: An Acid Alternative (ACM Queue)](https://queue.acm.org/detail.cfm?id=1394128) — Pat Helland's classic paper contrasting ACID and BASE for distributed systems

---

[Get Started](../../../get-started.md) | [Architectural Patterns](../../../get-started.md#architectural-patterns)

---
