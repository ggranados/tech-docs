# Event Sourcing

---

## Table of Contents
<!-- TOC -->
* [Event Sourcing](#event-sourcing)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Core Concepts](#core-concepts)
  * [Benefits and Trade-offs](#benefits-and-trade-offs)
  * [Key Concepts](#key-concepts)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Event sourcing is a persistence pattern in which an entity's state is derived from — and stored as — the full, ordered sequence of **events** that produced it, rather than a single mutable record holding only the current state. Instead of updating a row in place, every change is appended as a new, immutable event to an **event store**; the current state is obtained by replaying those events from the beginning (or from a snapshot). Because events are the source of truth, event sourcing is a natural companion to [Event-Driven Architecture](../event-driven.md) and is frequently paired with [CQRS](../../cqrs.md) to serve reads efficiently.

---

## Overview

Traditional persistence (CRUD) keeps only the latest state of an entity — once a value is overwritten, the history of how it got there is lost unless it's captured separately (e.g., in an audit log bolted on afterward). Event sourcing inverts this: the events *are* the primary data. Current state becomes a derived, disposable projection that can always be rebuilt by replaying the event log — a technique often called *event replay* or *state rehydration*.

This approach originates from the same domain-driven design community that popularized CQRS, and it is commonly applied to aggregates in a bounded context — e.g., a bank account, an order, or a shopping cart — where a complete, auditable history of changes has real business value, not just technical convenience.

<sub>[Back to top](#table-of-contents)</sub>

---

## Core Concepts

- ### Event Store:
  An append-only log or database that persists every event ever recorded for an entity, in the order it occurred. Unlike a regular table, records are never updated or deleted — only appended. Popular implementations include EventStoreDB, Apache Kafka (as a durable log), and event tables layered on top of relational or document databases.

- ### Event Replay / State Rehydration:
  To obtain an entity's current state, the system loads all of its events in order and folds them, one by one, into an in-memory object — starting from an empty/initial state. This means the current state is never stored directly; it's always a function of `initial_state + events`.

  ```mermaid
  flowchart LR
      subgraph Write side
          Cmd["Command\n(e.g. WithdrawFunds)"] --> Agg["Aggregate"]
          Agg -->|append| ES[("Event Store\n(append-only log)")]
      end

      subgraph Rebuild state
          ES -->|replay events in order| Fold["Fold events into state"]
          Snap[("Snapshot\n(optional)")] -.->|start from| Fold
          Fold --> State["Current State"]
      end

      ES -->|publish| Bus[("Event Bus / Broker")]
      Bus --> Proj["Read-model projections\n(CQRS query side)"]
  ```

  **Caption:** Commands produce events appended to the event store; current state is rebuilt by replaying events (optionally from a snapshot), while the same events can be published to build read-optimized projections.

- ### Snapshots:
  Replaying thousands of events every time state is needed becomes expensive. A snapshot periodically captures the folded state at a given event position, so replay only needs to resume from that point forward instead of from event zero. Snapshots are a performance optimization, not a second source of truth — they can always be regenerated from the event log.

- ### Aggregate:
  The consistency boundary that owns a stream of events (e.g., one event stream per bank account or per order). Each command validated against the aggregate produces zero or more new events, which are appended atomically to that aggregate's stream.

<sub>[Back to top](#table-of-contents)</sub>

---

## Benefits and Trade-offs

- ### Complete audit trail:
  Every state change is preserved forever, in order, which is invaluable for auditing, debugging, and compliance in domains like finance and healthcare.

- ### Temporal queries:
  Because the full history is retained, the system can answer "what was the state at time T?" simply by replaying events up to that point — something a CRUD table can't do without a separate audit mechanism.

- ### Natural fit with CQRS:
  The same event stream that rebuilds write-side state can be published to build one or more read-optimized projections, which is why event sourcing and [CQRS](../../cqrs.md) are so often used together — though neither pattern requires the other.

- ### Increased complexity:
  Event schemas must be versioned carefully since old events can never be rewritten, only handled by upcasting/migration logic. Debugging also shifts from "inspect a row" to "understand a sequence of events," which has a real learning curve.

  > See also: [Event-Driven Architecture](../event-driven.md), [CQRS](../../cqrs.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Event | An immutable fact recording that something happened to an aggregate (e.g., `FundsWithdrawn`). |
| Event Store | An append-only log that persists all events for all aggregates, typically partitioned by aggregate stream. |
| Replay | Reconstructing current state by folding an aggregate's events, in order, from an initial state. |
| Snapshot | A cached, periodic checkpoint of folded state used to shorten replay time. |
| Aggregate | The consistency boundary and unit of event-stream ownership in the domain model. |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If events are never deleted or modified, how do you fix a mistake in the data or handle a change in business rules?**
A: You don't rewrite history — you append a new compensating event that corrects the state going forward (e.g., `WithdrawalReversed`), the same way accounting ledgers use reversing entries instead of erasing mistakes. For schema changes, you version the event type and use upcasting logic when replaying old events, rather than mutating what was already stored.

---

**Q: Why are event sourcing and CQRS so often mentioned together, and does one require the other?**
A: They're complementary, not dependent. Event sourcing defines how the write side persists state (as an event log), and its output — the event stream — is a natural source for building the read-optimized projections that CQRS's query side needs. But you can use CQRS with a normal CRUD write model, and you can use event sourcing with a single unified read/write model. They just fit unusually well together.

---

**Q: Doesn't replaying every event to compute current state get slower and slower as an aggregate accumulates history?**
A: Yes, which is exactly what snapshots solve. Instead of replaying from event zero, the system loads the most recent snapshot and replays only the events appended after it. Snapshots are purely a performance optimization — they're derived data and can always be regenerated by replaying the full log, so losing a snapshot never loses information.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Event-Driven Architecture](../event-driven.md) — event sourcing's event store is often the same log published to drive event-driven consumers
- [Message-Driven Architecture](../../message-driven.md) — the broader umbrella concept event sourcing's underlying event stream participates in
- [CQRS](../../cqrs.md) — commonly paired with event sourcing, using the event stream to build read-side projections
- [Microservices Architecture](../../microservices.md) — mentions event sourcing as a pattern for reconstructing service state and enabling auditing
- [Distributed Transaction](../../microservices/distributed-transaction.md) — the Saga pattern coordinates events across aggregates/services, complementing per-aggregate event sourcing
- [Saga Pattern](../../microservices/distributed-transaction/saga.md) — choreographed sagas often react to the same domain events an event-sourced aggregate emits
- [Event Streaming](../../../data-processing/real-time/event-streaming.md) — platforms like Kafka can serve as the durable, ordered log an event store relies on
- [Apache Kafka](../../../data-processing/real-time/event-streaming/kafka.md) — a common technology choice for implementing an event store's append-only log

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Event Sourcing pattern — Microsoft Azure Architecture Center](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing) — official documentation
- [Event Sourcing — Martin Fowler](https://martinfowler.com/eaaDev/EventSourcing.html) — foundational reference article
- [EventStoreDB Documentation](https://developers.eventstore.com/) — reference implementation of an event store

---

[Get Started](../../../../get-started.md) | [Architectural Patterns](../../../../get-started.md#architectural-patterns)

---
