# Hexagonal Architecture (Ports and Adapters)

---

## Table of Contents
<!-- TOC -->
* [Hexagonal Architecture (Ports and Adapters)](#hexagonal-architecture-ports-and-adapters)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Core Components](#core-components)
  * [Adapter Types and Dependency Direction](#adapter-types-and-dependency-direction)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Hexagonal Architecture, also known as **Ports and Adapters**, is a software design approach in which the application's domain logic sits at the center, completely isolated from external technologies such as databases, message brokers, UI frameworks, or third-party APIs. Interaction with the outside world happens exclusively through well-defined **ports** (interfaces), implemented by **adapters** that plug into them. The pattern's core promise is that the domain never depends on infrastructure — infrastructure depends on the domain.

---

## Overview

Hexagonal Architecture was introduced by Alistair Cockburn in 2005 as a response to a recurring problem: business logic that becomes entangled with the UI layer, the database layer, or a specific framework, making the system hard to test and hard to change. Cockburn's insight was to draw the architecture as a hexagon — an arbitrary shape chosen specifically to avoid implying "layers" — with the domain core in the middle and any number of ports around its edge, each connected to one or more adapters.

The motivation is testability and technology independence: because the domain core only talks to abstractions (ports), it can be tested with in-memory fakes and never needs a real database, message queue, or HTTP server running. The same domain logic can be driven by a REST controller today and a CLI or message consumer tomorrow, without touching a single line of business logic.

This pattern is widely used in Domain-Driven Design (DDD) implementations, where protecting the domain model from external concerns is a first-class goal, and in systems expected to swap infrastructure over their lifetime (e.g., replacing a SQL database with a document store, or adding a new API protocol).

<sub>[Back to top](#table-of-contents)</sub>

---

## Core Components

The hexagon is built from three kinds of elements: the domain core, the ports it exposes, and the adapters that implement or consume those ports.

- ### Domain Core:
  The application's business logic and domain model, containing entities, value objects, domain services, and use cases (application services). The core has **zero dependencies on frameworks, databases, or delivery mechanisms** — it depends only on its own ports, expressed as plain interfaces.

- ### Ports:
  Interfaces defined by the domain core that describe what it needs from the outside world (**driven/secondary ports**, e.g., `OrderRepository`) or what it offers to the outside world (**driving/primary ports**, e.g., `PlaceOrderUseCase`). Ports belong to the domain, not to any specific technology.

- ### Adapters:
  Concrete implementations that connect a port to a real technology. A `JpaOrderRepository` adapts the `OrderRepository` port to a relational database; a `RestOrderController` adapts an HTTP request into a call on the `PlaceOrderUseCase` port.

  ```mermaid
  flowchart TB
      subgraph Driving["Driving Adapters (left side)"]
          REST["REST Controller"]
          CLI["CLI Command"]
      end

      subgraph Core["Domain Core"]
          direction TB
          UseCase["PlaceOrderUseCase (driving port)"]
          Domain["Order, Domain Services"]
          RepoPort["OrderRepository (driven port)"]
          UseCase --> Domain --> RepoPort
      end

      subgraph Driven["Driven Adapters (right side)"]
          JPA["JPA Repository Adapter"]
          Mail["Email Gateway Adapter"]
      end

      REST --> UseCase
      CLI --> UseCase
      RepoPort --> JPA
      RepoPort --> Mail
  ```

  **Caption:** Driving adapters call into the domain through primary ports; the domain calls out through secondary ports, implemented by driven adapters — dependencies always point inward, toward the core.

<sub>[Back to top](#table-of-contents)</sub>

---

## Adapter Types and Dependency Direction

Understanding which side of the hexagon an adapter sits on clarifies how control and dependency flow through the system.

- ### Driving (Primary) Adapters:
  Initiate interaction with the domain — a REST controller, a scheduled job, a message consumer, or a CLI entry point. They call a driving port exposed by the domain (typically a use-case interface).

- ### Driven (Secondary) Adapters:
  Are called by the domain to reach external systems — a repository implementation, an email gateway, a payment provider client. The domain defines the port; the adapter implements it, so infrastructure code depends on the domain, never the reverse.

- ### Contrast with Layered Architecture:
  A traditional Layered architecture stacks presentation → business → data-access layers, and dependencies flow strictly downward — the business layer typically depends directly on a concrete data-access layer. Hexagonal inverts this: the domain core depends on nothing but its own ports, and *both* the UI and the database are treated symmetrically as adapters plugged in from the outside. This is why the domain in a Hexagonal system can be unit-tested with zero infrastructure, while a Layered system's business layer is commonly coupled to a specific persistence technology.

  > See also: [Layered Architecture](layered.md)

- ### Wiring Adapters with Dependency Injection:
  Because ports are interfaces and adapters are their implementations, something needs to decide, at startup, which concrete adapter to bind to each port. This is almost always done with **Dependency Injection** — a DI container (e.g., Spring) constructs the adapters and injects them into the domain's use cases, keeping the wiring decision outside the domain itself.

  > See also: [Dependency Injection](../design-patterns/ioc/dependency-injection.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Port | An interface owned by the domain core describing an inbound capability or an outbound need |
| Driving (Primary) Adapter | Code that calls into the domain through a port — UI, API controller, CLI, scheduler |
| Driven (Secondary) Adapter | Code the domain calls out to through a port — database, messaging, external API client |
| Domain Core | The technology-agnostic business logic and model at the center of the hexagon |
| Dependency Inversion | Infrastructure depends on the domain's abstractions; the domain depends on nothing external |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

A minimal Java sketch of a driven port and two interchangeable adapters:

```java
// Port — owned by the domain
public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(OrderId id);
}

// Driven adapter — infrastructure implements the domain's port
public class JpaOrderRepository implements OrderRepository {
    private final EntityManager em;
    public void save(Order order) { em.persist(toEntity(order)); }
    public Optional<Order> findById(OrderId id) { /* ... */ return Optional.empty(); }
}

// Domain use case depends only on the port, never on JPA
public class PlaceOrderUseCase {
    private final OrderRepository repository; // injected
    public PlaceOrderUseCase(OrderRepository repository) { this.repository = repository; }
    public void handle(PlaceOrderCommand cmd) {
        Order order = Order.create(cmd);
        repository.save(order);
    }
}
```

`PlaceOrderUseCase` can be unit-tested with an in-memory `OrderRepository` fake and never needs a real database, demonstrating the pattern's central benefit: the domain is testable and technology-independent by construction.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How is Hexagonal Architecture different from a traditional Layered (n-tier) architecture?**
A: Layered architecture arranges the system as a strict top-to-bottom stack, and the business layer commonly depends directly on a concrete data-access layer below it. Hexagonal instead makes the domain core depend on nothing but its own port interfaces, treating every external concern — UI, database, messaging — as a symmetric, swappable adapter plugged in from outside. The direction of dependency, not the number of tiers, is the key distinction.

---

**Q: Do I need a DI framework to implement Hexagonal Architecture?**
A: No, but it makes the pattern far more practical. Ports must be bound to concrete adapters somewhere, and Dependency Injection is the standard way to do that composition at application startup, keeping the domain core itself free of any wiring logic or knowledge of which adapter is in use.

---

**Q: Does every port need exactly one adapter?**
A: No — a single port can have multiple adapter implementations that are swapped based on environment (an in-memory adapter for tests, a JPA adapter for production, a mock for a demo). This substitutability is precisely what makes the domain core easy to test and technology decisions easy to defer or change later.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Layered Architecture](layered.md) — contrast: strict top-down layering vs. Hexagonal's inward-only dependency direction
- [Dependency Injection](../design-patterns/ioc/dependency-injection.md) — the typical mechanism used to wire adapters into domain ports at startup
- [Microservices Architecture](microservices.md) — each microservice can internally be structured as its own hexagon
- [Reactive Systems](reactive.md) — driven adapters are a natural place to implement non-blocking, reactive I/O

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Alistair Cockburn — Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/) — the original description by its author
- [Martin Fowler's Bliki — mentions and related essays](https://martinfowler.com/) — architecture commentary referencing Ports and Adapters

---

[Get Started](../../get-started.md) | [Architectural Patterns](../../get-started.md#architectural-patterns)

---
