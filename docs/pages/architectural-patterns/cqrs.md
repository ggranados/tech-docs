# CQRS (Command and Query Responsibility Segregation)

---

## Table of Contents
<!-- TOC -->
* [CQRS (Command and Query Responsibility Segregation)](#cqrs-command-and-query-responsibility-segregation)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Core Concepts](#core-concepts)
  * [Synchronization and When to Use CQRS](#synchronization-and-when-to-use-cqrs)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

**CQRS (Command and Query Responsibility Segregation)** is an architectural pattern that splits an application's data-handling responsibilities into two distinct models: a **write model** that handles commands (state-changing operations) and a **read model** that handles queries (data retrieval). Rather than a single model serving both purposes, as in conventional layered CRUD, each side is optimized independently for its own concern — consistency and validation on the write side, and shape and speed of reads on the read side.

---

## Overview

CQRS was popularized by Greg Young, building on Bertrand Meyer's earlier **Command-Query Separation (CQS)** principle. CQS states that a method should either be a command that mutates state and returns nothing, or a query that returns data and has no side effects, but never both — CQRS applies that same idea at the architectural level, splitting the *entire model*, not just individual methods.

The motivation is that write and read workloads often have conflicting needs. Writes need to enforce invariants, run validation, and preserve a normalized, consistent representation of the domain. Reads often need denormalized, pre-joined, query-optimized shapes tailored to specific screens or reports, and are typically far more frequent than writes. Forcing both concerns through one shared model — one set of entities, one schema — tends to produce a model that compromises on both.

CQRS is most valuable in systems with complex business logic, high read/write asymmetry, or reporting needs that don't map cleanly onto the transactional schema. It is deliberately not a default: for simple CRUD screens, the added moving parts (two models, a synchronization mechanism) usually outweigh the benefit.

<sub>[Back to top](#table-of-contents)</sub>

---

## Core Concepts

CQRS is defined by the split between the write side and the read side, and how they stay in sync.

- ### Commands and the Write Model:
  A **command** expresses intent to change state (`PlaceOrder`, `CancelSubscription`). Commands are handled by the write model, which validates business rules and persists the change against a normalized schema optimized for consistency, not query convenience.

- ### Queries and the Read Model:
  A **query** requests data without changing anything (`GetOrderSummary`, `ListActiveSubscriptions`). Queries are served by a read model — often a denormalized, pre-computed projection stored in a separate table, cache, or even a different database technology, optimized purely for fast, simple retrieval.

  ```mermaid
  flowchart LR
      Client --Command--> CH["Command Handler"]
      CH --> WM["Write Model\n(normalized, consistent)"]
      WM --Publishes change--> Sync["Synchronization\n(events / projection)"]
      Sync --Updates--> RM["Read Model\n(denormalized, query-optimized)"]
      Client2["Client"] --Query--> QH["Query Handler"]
      QH --> RM
  ```

  **Caption:** Commands flow through a handler into the write model; state changes are propagated asynchronously into one or more read models that queries hit directly.

<sub>[Back to top](#table-of-contents)</sub>

---

## Synchronization and When to Use CQRS

The read and write models must be kept in sync, and how that happens shapes the rest of the design.

- ### Contrast with Plain Layered CRUD:
  In a conventional layered CRUD design, a single set of entities and a single schema serve both reads and writes — one `Order` class, one `orders` table, one repository. CQRS deliberately breaks that symmetry: the write side owns validated, consistent state, and one or more read side projections are derived from it. This adds complexity, so it is a deliberate trade, not a default architectural stance.

- ### Eventual Consistency Between Models:
  Because the read model is a projection updated after the write model changes, there is a window — often milliseconds, sometimes longer with async processing — where the read model has not yet caught up. Systems adopting CQRS must accept **eventual consistency** on the read side, or restrict queries that require read-your-writes guarantees to hit the write model directly.

  > See also: [BASE (Basically Available, Soft-state, Eventual consistency)](../data-processing/db-concepts/base.md)

- ### Pairing with Event Sourcing:
  CQRS is commonly paired with **Event Sourcing**, where every state change is captured as an immutable event. The write model appends events; read models are simply projections built by replaying those events. The two patterns are independent — CQRS does not require Event Sourcing, and Event Sourcing does not require CQRS — but they combine naturally because Event Sourcing already produces the stream of changes that read-model projections need to stay updated.

  > See also: [Event Sourcing](message-driven/event-driven/event-sourcing.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Command | An intent to change state, handled by the write model; no return value beyond success/failure |
| Query | A request for data with no side effects, served by the read model |
| Write Model | Normalized, validated representation optimized for consistency and business rules |
| Read Model | Denormalized, query-optimized projection built for fast retrieval |
| Eventual Consistency | The read model converges to the write model's state after a propagation delay, not instantly |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

A simplified split between a command handler and a query handler:

```java
// Write side
public class PlaceOrderCommandHandler {
    private final OrderWriteRepository writeRepo;
    public void handle(PlaceOrderCommand cmd) {
        Order order = Order.create(cmd.customerId(), cmd.items());
        writeRepo.save(order);            // validated, normalized
        eventPublisher.publish(new OrderPlaced(order.id()));
    }
}

// Read side — separate, denormalized model
public class OrderSummaryQueryHandler {
    private final OrderSummaryReadRepository readRepo;
    public OrderSummaryView handle(GetOrderSummaryQuery query) {
        return readRepo.findSummaryById(query.orderId()); // pre-joined, fast
    }
}

// Projector keeps the read model in sync with write-side events
public class OrderSummaryProjector {
    public void on(OrderPlaced event) {
        readRepo.upsertSummary(event.orderId());
    }
}
```

The command handler never returns query-shaped data, and the query handler never mutates state — each side evolves and scales independently.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Is CQRS the same thing as having separate read and write databases?**
A: Not necessarily. CQRS is about separating the *models* (and typically the code paths) for commands and queries; the underlying storage can be the same database with two different sets of classes/schemas, or genuinely separate databases. Physically separate read stores are a common implementation choice, not a requirement of the pattern itself.

---

**Q: Do CQRS and Event Sourcing have to be used together?**
A: No. CQRS only requires separating the write and read models — the write side can still use a conventional relational table and simply overwrite rows. Event Sourcing is a separate decision about *how* the write model persists state, as a sequence of events rather than current-state rows. They're frequently combined because Event Sourcing's event stream is a natural feed for building read-model projections, but each can be adopted independently.

---

**Q: If the read model can be stale, how do clients handle that?**
A: Applications typically accept the staleness for read-heavy views (dashboards, listings) where a few milliseconds to seconds of lag is invisible to users. For operations that must see their own write immediately (e.g., "your order was placed, here's the confirmation page"), the common approaches are routing that specific read to the write model, returning the just-written data directly from the command handler, or using a version/token the client can poll until the read model catches up.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Event Sourcing](message-driven/event-driven/event-sourcing.md) — commonly paired with CQRS to feed read-model projections from an event stream, though the two are independent
- [BASE (Basically Available, Soft-state, Eventual consistency)](../data-processing/db-concepts/base.md) — the consistency model that governs how quickly the read model converges with the write model
- [Microservices Architecture](microservices.md) — CQRS is often applied within a single service's data layer, and read/write sides can even be split into separate services
- [Hexagonal Architecture (Ports and Adapters)](hexagonal.md) — command and query handlers map naturally onto driving ports in a hexagonal design

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Martin Fowler — CQRS](https://martinfowler.com/bliki/CQRS.html) — widely cited overview and trade-off discussion
- [Greg Young — CQRS Documents](https://cqrs.files.wordpress.com/2010/11/cqrs_documents.pdf) — the original documentation by the practitioner who popularized the pattern

---

[Get Started](../../get-started.md) | [Architectural Patterns](../../get-started.md#architectural-patterns)

---
