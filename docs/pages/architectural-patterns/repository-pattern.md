# Repository Pattern

---

## Table of Contents
<!-- TOC -->
* [Repository Pattern](#repository-pattern)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Core Components](#core-components)
  * [Repository vs. DAO](#repository-vs-dao)
  * [Repository and Dependency Injection](#repository-and-dependency-injection)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

The Repository Pattern is a structural pattern that _mediates between the domain model and the data mapping layer, exposing a collection-like interface for accessing domain objects_ while hiding the details of how they are actually persisted. Callers work with the Repository as if it were an in-memory collection of aggregates — `add`, `remove`, `findById` — with no awareness of SQL, ORMs, or storage engines underneath. It is one of the most common patterns for isolating business logic from persistence concerns.

---

## Overview

The Repository Pattern was popularized by Eric Evans in _Domain-Driven Design_ (2003) and formally cataloged by Martin Fowler in _Patterns of Enterprise Application Architecture_ (2002). Its purpose is to give the domain and application layers a persistence-ignorant way to retrieve and store aggregates, so that business logic never depends directly on a database technology, an ORM API, or a query language.

A Repository is defined per **aggregate root** (in DDD terms) rather than per table — it returns fully reconstituted domain objects, not raw rows or DTOs, and it enforces the aggregate's consistency boundary (e.g., saving an `Order` repository also persists its `OrderLines`). This makes the pattern especially valuable in applications with a rich domain model, where entangling business rules with query code would make both harder to test and evolve independently.

Repositories are almost always accessed through an interface defined in the domain or application layer, with the concrete implementation (backed by an ORM, a raw SQL client, or an external API) living in the infrastructure layer — a direct application of the Dependency Inversion Principle.

<sub>[Back to top](#table-of-contents)</sub>

---

## Core Components

- ### Repository Interface:
  Declares collection-like operations (`add`, `remove`, `findById`, `findByCriteria`) in terms of domain objects. It is owned by the domain/application layer and has no persistence-specific types in its signature.

- ### Concrete Repository:
  Implements the interface using a specific persistence technology (an ORM such as Hibernate/Entity Framework, a raw SQL/JDBC client, or a call to an external service). This implementation lives in the infrastructure layer.

- ### Aggregate / Domain Model:
  The object graph the Repository returns and persists as a unit. The Repository is responsible for reconstructing it fully from storage and for enforcing that it is saved consistently.

- ### Client (Service/Use Case):
  Application or domain services depend only on the Repository interface, never on the concrete implementation, and typically receive it via constructor injection.

  ```mermaid
  classDiagram
      class OrderService {
          -OrderRepository repository
          +placeOrder(order)
      }
      class OrderRepository {
          <<interface>>
          +findById(id) Order
          +add(order)
          +remove(order)
      }
      class SqlOrderRepository {
          +findById(id) Order
          +add(order)
          +remove(order)
      }
      class Order {
          +id
          +orderLines
      }
      OrderService --> OrderRepository : depends on
      SqlOrderRepository ..|> OrderRepository : implements
      SqlOrderRepository --> Order : builds/persists
  ```

  **Caption:** The service depends only on the `OrderRepository` interface; the SQL-backed implementation is swapped in at runtime without the service ever knowing.

<sub>[Back to top](#table-of-contents)</sub>

---

## Repository vs. DAO

Repository and Data Access Object (DAO) are frequently used interchangeably, but they represent different levels of abstraction.

A **DAO** is tied directly to the persistence mechanism: its methods often mirror table operations (`insertRow`, `updateRow`, `selectByColumn`) and it commonly returns or accepts records/DTOs shaped by the database schema rather than by the domain model. A DAO is a technical abstraction over a specific data source.

A **Repository** is a domain-oriented abstraction: it speaks in terms of aggregates and business-meaningful queries (`findActiveCustomers()`, `findOverdueInvoices()`), not tables or columns, and it may internally use one or more DAOs to do the actual data-source work. In short: a DAO abstracts *how* data is accessed; a Repository abstracts *what* the domain needs, and can be layered on top of one or more DAOs.

<sub>[Back to top](#table-of-contents)</sub>

---

## Repository and Dependency Injection

Because consumers depend only on the Repository's interface, the concrete implementation is virtually always supplied through Dependency Injection rather than instantiated directly by the consumer. This is what makes the pattern practically valuable, not just theoretically clean: a service can be unit-tested against an in-memory fake or mock repository with zero database involvement, and the real implementation is wired in only at the composition root or by a DI container/framework (e.g., Spring's `@Repository` beans, or .NET's DI container registering `IOrderRepository`).

> See also: [Dependency Injection](../design-patterns/ioc/dependency-injection.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If I'm just wrapping an ORM's `save()` and `findById()` with no extra logic, is that still "the Repository Pattern"?**
A: It's a Repository in the loose, everyday sense used by most frameworks, but not in the strict DDD sense Evans and Fowler described — the strict version enforces aggregate consistency boundaries and speaks entirely in domain terms. A thin pass-through wrapper is closer to a DAO wearing a Repository-shaped interface; that's fine for simple CRUD, but it doesn't buy you the isolation benefits once business rules around persistence get complex.

**Q: Doesn't the Repository Pattern just duplicate what my ORM already gives me?**
A: An ORM (Hibernate, Entity Framework) already provides an abstraction over SQL, but code that calls the ORM's session/context API directly is coupled to that ORM. A Repository interface adds one more layer of indirection so the domain/application layer is coupled to your own interface instead — letting you swap ORMs, add caching, or write fast in-memory test doubles without touching business logic.

**Q: Where does the Repository Pattern fit in a layered architecture?**
A: The Repository interface belongs in the domain or application layer (it's part of the contract business logic depends on); the concrete implementation belongs in the data-access/infrastructure layer. This split is exactly what keeps the dependency pointing from infrastructure toward the domain rather than the other way around.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Dependency Injection](../design-patterns/ioc/dependency-injection.md) — the mechanism used to supply concrete Repository implementations to their consumers
- [Layered Architecture](layered.md) — Repositories typically live in the data-access layer, exposed through an interface owned by an upper layer
- [Model-View-Controller (MVC)](mvc.md) — Controllers commonly invoke domain services that depend on Repositories rather than accessing storage directly

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Martin Fowler — Repository](https://martinfowler.com/eaaCatalog/repository.html) — Patterns of Enterprise Application Architecture catalog entry
- [Microsoft Docs — Design the Infrastructure Persistence Layer](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/infrastructure-persistence-layer-design) — repository implementation guidance in a DDD context

---

[Get Started](../../get-started.md) | [Architectural Patterns](../../get-started.md#architectural-patterns)

---
