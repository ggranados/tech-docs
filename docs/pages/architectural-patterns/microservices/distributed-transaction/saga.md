# Saga Pattern

---

## Table of Contents
<!-- TOC -->
* [Saga Pattern](#saga-pattern)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Choreography vs. Orchestration](#choreography-vs-orchestration)
  * [Compensating Transactions](#compensating-transactions)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

The **Saga pattern** implements a [distributed transaction](../distributed-transaction.md) as a sequence of local transactions, one per participating microservice, where each local transaction publishes an event or triggers a request that drives the next step. If any step fails, the saga runs **compensating transactions** for every step that already succeeded, undoing their effects instead of relying on a database rollback that cannot span services. Sagas can be coordinated in two ways — choreography or orchestration — and are the de facto standard for maintaining data consistency across microservices without 2PC.

---

## Overview

A saga treats a long-running business process — placing an order, booking a trip, onboarding a customer — as a chain of individually-committed local transactions rather than one atomic unit. Each participating service commits its own step immediately, using its own local ACID transaction, and then signals completion so the next step can proceed. Because there is no cross-service lock held while the saga runs, every participant stays independently available, which is exactly the property [2PC](../distributed-transaction.md#approaches-to-distributed-transactions) sacrifices.

The trade-off is that the saga must be designed to reach a consistent end state even when a step fails partway through. Rather than a database engine automatically undoing partial work, the saga's own logic must issue compensating actions — semantic "undo" operations like *cancel reservation* or *refund payment* — for every step that already committed. This means saga design is a business-logic exercise as much as a technical one: someone has to define what "undo the shipment" actually means.

The pattern was formalized for long-lived local-transaction chains in the original 1987 "Sagas" database paper by Garcia-Molina and Salem, and was later adapted to microservices as the standard answer to the [distributed transaction](../distributed-transaction.md) problem.

<sub>[Back to top](#table-of-contents)</sub>

---

## Choreography vs. Orchestration

The two participants that must always be decided are *who* triggers the next step and *who* decides to compensate on failure.

- ### Choreography:
  Each service publishes a domain event after committing its local transaction, and other services subscribe to the events they care about and react by executing their own local transaction. There is no central coordinator — the sequence of steps emerges from the chain of event publish/subscribe relationships. This keeps services maximally decoupled and works naturally with an existing [message-driven](../../message-driven.md) or [event-driven](../../message-driven/event-driven.md) architecture, but it becomes hard to see the overall business process in one place as the number of steps grows, and each service must know which events to react to and which compensating event to emit on failure.

- ### Orchestration:
  A dedicated orchestrator (sometimes itself a stateful service, sometimes a workflow engine) explicitly calls each participant in sequence and tells it what to do, including which compensating action to invoke if a later step fails. The business process logic lives in one place, which makes it far easier to understand, test, and modify, but the orchestrator becomes a new, purpose-built coordination component that every step depends on — a form of centralization the saga otherwise avoids.

  > See also: [Microservices Architecture](../../microservices.md) — the Saga Pattern section briefly introduces both coordination styles in the context of the wider pattern catalog.

<sub>[Back to top](#table-of-contents)</sub>

---

## Compensating Transactions

A compensating transaction is the mechanism that replaces the rollback a single-database transaction would have given for free.

- ### Semantic, Not Automatic Undo:
  Unlike a database ROLLBACK, a compensating transaction is application-defined: *cancel reservation*, *issue refund*, *release inventory hold*. It must be written by the service owner because only that service knows what "undoing" its own committed state actually means in business terms.

- ### Not Always a Perfect Undo:
  Some effects can't be perfectly reversed — a payment refund may take days to settle, or a notification email that already sent can't be unsent. Compensations aim to restore the *business-meaningful* state (e.g., balance the ledger, notify the customer of the cancellation) rather than literally rewinding time.

- ### Ordering Matters:
  Compensating transactions typically run in reverse order of the original steps, undoing the most recently completed step first, so that a service is never asked to compensate a step whose downstream dependents haven't been compensated yet.

  ```mermaid
  sequenceDiagram
      participant Orchestrator as Saga Orchestrator
      participant Order as Order Service
      participant Inventory as Inventory Service
      participant Payment as Payment Service

      Orchestrator->>Order: Create order (local tx)
      Order-->>Orchestrator: Order created
      Orchestrator->>Inventory: Reserve stock (local tx)
      Inventory-->>Orchestrator: Stock reserved
      Orchestrator->>Payment: Charge payment (local tx)
      Payment-->>Orchestrator: Payment declined (failure)

      Note over Orchestrator: Failure detected — begin compensation<br/>in reverse order
      Orchestrator->>Inventory: Release reserved stock (compensating tx)
      Inventory-->>Orchestrator: Stock released
      Orchestrator->>Order: Cancel order (compensating tx)
      Order-->>Orchestrator: Order cancelled
  ```

  **Caption:** An orchestrated saga rolls back a failed payment step by issuing compensating transactions to Inventory and Order, in reverse order of the original commits.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Saga | A sequence of local transactions coordinated so that a multi-service business process either completes fully or is undone via compensation. |
| Choreography | Coordination via each service publishing/subscribing to events, with no central controller. |
| Orchestration | Coordination via a central component that explicitly directs each step and compensation. |
| Compensating Transaction | An application-defined action that semantically reverses the effect of a previously committed local transaction. |
| Semantic Lock | An application-level flag (e.g., "order = PENDING") used to signal to other processes that a saga is mid-flight, since no database lock spans the whole saga. |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

A choreographed version of the same checkout saga relies purely on events instead of a coordinator:

```text
1. Order Service    commits "order = PENDING"     → publishes OrderCreated
2. Inventory Service reacts to OrderCreated        → commits "stock reserved" → publishes StockReserved
3. Payment Service   reacts to StockReserved        → attempts charge → publishes PaymentFailed
4. Inventory Service reacts to PaymentFailed        → commits "release stock" (compensating tx)
5. Order Service     reacts to PaymentFailed        → commits "order = CANCELLED" (compensating tx)
```

Each service only needs to know which events to consume and which events (success or compensating) to emit — no participant needs to know the full end-to-end flow, which is what makes choreography attractive for simple, few-step sagas and painful to trace for long ones.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: When should I choose choreography over orchestration, or vice versa?**
A: Choreography suits short sagas (two or three steps) where the decoupling benefit outweighs the difficulty of tracing the flow, and where the team already has solid event infrastructure. Orchestration suits longer or more complex sagas where visibility, testability, and the ability to change the process in one place matter more than avoiding a coordinator — most non-trivial sagas end up orchestrated for exactly this reason.

---

**Q: What happens if a compensating transaction itself fails?**
A: This is the hardest part of saga design. Common mitigations are making compensations idempotent and retryable, using a durable outbox/retry queue so a failed compensation is retried until it succeeds, and — as a last resort — routing to a dead-letter queue with alerting so a human intervenes. A saga implementation is only as reliable as its compensation retry strategy.

---

**Q: Does a saga give the same consistency guarantees as an ACID transaction?**
A: No — a saga gives eventual consistency, not the isolation guarantee of ACID. Other processes can observe intermediate states while the saga is mid-flight (e.g., stock shown as reserved before payment is confirmed), which is why sagas are often paired with semantic locks or status fields like `PENDING` so callers know the process isn't finished yet.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Distributed Transaction](../distributed-transaction.md) — the problem the Saga pattern solves for multi-service business processes
- [Microservices Architecture](../../microservices.md) — the parent architecture where the Saga pattern is applied
- [BASE](../../../data-processing/db-concepts/base.md) — the eventual-consistency model a saga's intermediate states follow
- [CAP Theorem](../../../data-processing/db-concepts/cap.md) — explains the availability-versus-consistency trade-off sagas accept
- [Message-Driven Architecture](../../message-driven.md) — the asynchronous messaging style choreographed sagas are typically built on
- [Event-Driven Architecture](../../message-driven/event-driven.md) — event publish/subscribe is the mechanism choreography uses to trigger each step
- [Event Sourcing](../../message-driven/event-driven/event-sourcing.md) — an event log can serve as the audit trail and trigger source for a saga's steps
- [CQRS](../../cqrs.md) — read models are often updated once a saga reaches a consistent end state

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Pattern: Saga (microservices.io)](https://microservices.io/patterns/data/saga.html) — authoritative reference for the Saga pattern, choreography and orchestration
- [Sagas (Garcia-Molina & Salem, 1987)](https://www.cs.cornell.edu/andru/cs711/2002fa/reading/sagas.pdf) — the original database paper that introduced the saga concept
- [Managing Data Consistency in a Microservice Architecture Using Sagas (AWS Prescriptive Guidance)](https://docs.aws.amazon.com/prescriptive-guidance/latest/cloud-design-patterns/saga.html) — practical orchestration/choreography implementation guidance
- [Compensating Transaction Pattern (Microsoft Azure Architecture Center)](https://learn.microsoft.com/en-us/azure/architecture/patterns/compensating-transaction) — reference on designing compensating actions

---

[Get Started](../../../../get-started.md) | [Architectural Patterns](../../../../get-started.md#architectural-patterns)

---
