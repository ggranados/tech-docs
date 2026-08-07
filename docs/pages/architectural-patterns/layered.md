# Layered (N-Tier) Architecture

---

## Table of Contents
<!-- TOC -->
* [Layered (N-Tier) Architecture](#layered-n-tier-architecture)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Core Layers](#core-layers)
  * [Layered vs. Hexagonal Architecture](#layered-vs-hexagonal-architecture)
  * [Layered Architecture and Microservices](#layered-architecture-and-microservices)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Layered (or N-tier) architecture is a structural pattern that _organizes an application into horizontal layers, each responsible for a distinct concern, where a layer may only call the layer immediately below it_. It is one of the oldest and most widely adopted architectural styles for enterprise software, providing a simple, well-understood way to separate presentation, business logic, and data access. Its strict top-to-bottom dependency direction is what most distinguishes it from newer, dependency-inverting styles such as Hexagonal architecture.

---

## Overview

The layered style predates most other architectural patterns in this catalog — it emerged organically from client-server and mainframe application design and was formalized in enterprise architecture literature through the 1990s and 2000s, notably in Martin Fowler's _Patterns of Enterprise Application Architecture_. It remains the default mental model taught to most developers: presentation on top, business logic in the middle, data access at the bottom.

The core rule is strict layering: each layer exposes an interface to the layer above it and depends only on the layer directly beneath it (in "strict" layering) — a Presentation layer never calls the Data Access layer directly, it goes through the Business layer. This constrains change: a modification to the database schema should, in principle, only ripple through the Data Access layer and whatever the Business layer's abstraction exposes.

The pattern's simplicity is also its main risk. Because dependencies flow strictly downward, and downward tends toward the database, business logic can end up implicitly coupled to persistence concerns unless discipline is maintained — this is precisely the coupling that later patterns like Hexagonal architecture and the Repository Pattern were designed to prevent.

<sub>[Back to top](#table-of-contents)</sub>

---

## Core Layers

Most layered applications converge on some variation of the same four layers, though smaller applications often collapse Business and Domain into one.

- ### Presentation Layer:
  Handles user interaction — UI rendering, HTTP controllers, or API endpoints. Depends on the Business layer and contains no business rules of its own.

- ### Business (Application/Service) Layer:
  Contains use cases and orchestration logic — validation, workflow, transaction boundaries. Depends on the Domain layer (if present) and the Data Access layer's abstractions.

- ### Domain Layer (optional, in richer designs):
  Encapsulates core business entities and rules independent of any specific use case or technology.

- ### Data Access (Persistence) Layer:
  Talks to the database or external storage, typically through a Repository or DAO abstraction, and returns domain objects to the layer above.

  ```mermaid
  flowchart TD
      P["Presentation Layer<br/>(UI / Controllers / API)"]
      B["Business Layer<br/>(Use Cases / Services)"]
      D["Domain Layer<br/>(Entities / Business Rules)"]
      DA["Data Access Layer<br/>(Repositories / DAOs)"]
      DB[("Database / External Storage")]

      P --> B
      B --> D
      B --> DA
      DA --> DB
  ```

  **Caption:** Each layer depends only on the layer directly beneath it; a request flows top-to-bottom and a response flows bottom-to-top through the same layers.

<sub>[Back to top](#table-of-contents)</sub>

---

## Layered vs. Hexagonal Architecture

Both styles aim to separate concerns, but they differ in the direction dependencies are allowed to point.

**Layered architecture** stacks layers strictly top-to-bottom: Presentation depends on Business, Business depends on Data Access, and ultimately the whole stack tends to depend — directly or transitively — on the persistence technology at the bottom. The domain logic in the middle is not fully isolated from infrastructure; it typically knows it sits above a specific kind of data-access layer.

**Hexagonal (Ports and Adapters) architecture** inverts this: the domain core defines *ports* (interfaces) for everything it needs — persistence, messaging, external APIs — and has zero outward dependencies on any of them. Infrastructure code (databases, web frameworks, message brokers) is written as *adapters* that implement those ports and plug into the core from the outside. Where Layered has business logic depending on infrastructure abstractions below it, Hexagonal has infrastructure depending on the domain core, never the reverse. See [Hexagonal Architecture](hexagonal.md) for the full pattern.

<sub>[Back to top](#table-of-contents)</sub>

---

## Layered Architecture and Microservices

Layered architecture is commonly not an alternative to microservices but a complement to them: it is very often the *internal* structure of a single microservice. A microservice's codebase frequently still separates its own Presentation (REST controllers), Business, and Data Access layers internally, even though the service as a whole is one independently deployable unit within a larger [Microservices Architecture](microservices.md). The two patterns operate at different levels of granularity — Layered describes internal code organization, Microservices describes system-level service decomposition.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Is it ever acceptable for the Presentation layer to call the Data Access layer directly, skipping Business?**
A: That's "relaxed" layering, and some frameworks allow it for simple read-only queries where there's no business logic to apply. Strict layering forbids it because it defeats the purpose of the boundary — any future business rule added to that data path would have to be retrofitted into code that was written assuming there wasn't one.

**Q: Why is Layered architecture considered more prone to "database-driven design" than Hexagonal?**
A: Because dependencies point downward toward the Data Access layer, it's easy — and often the path of least resistance — for the Business layer to be shaped around what the database schema or ORM naturally provides, rather than around the actual domain problem. Hexagonal architecture prevents this structurally by making the domain core define its own interfaces and forcing persistence code to adapt to the domain, not the other way around.

**Q: If a microservice already isolates business logic behind a Repository interface, is it still "just" Layered, or has it become Hexagonal?**
A: Introducing a Repository interface is a step toward inversion, but it doesn't automatically make the design Hexagonal — that depends on whether *every* outward dependency (not just persistence) is expressed as a port the domain owns, and whether the domain has zero compile-time dependency on any infrastructure code. Many "layered with interfaces" designs are a reasonable middle ground between the two styles.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Hexagonal Architecture](hexagonal.md) — inverts the dependency direction so the domain core has no outward dependencies at all
- [Microservices Architecture](microservices.md) — Layered architecture is frequently the internal structure of a single microservice
- [Repository Pattern](repository-pattern.md) — the typical abstraction used at the boundary between the Business and Data Access layers

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Microsoft Docs — N-Tier Architecture Style](https://learn.microsoft.com/en-us/azure/architecture/guide/architecture-styles/n-tier) — official architectural guidance and trade-offs
- [Martin Fowler — Patterns of Enterprise Application Architecture](https://martinfowler.com/books/eaa.html) — foundational reference for layering and enterprise application structure

---

[Get Started](../../get-started.md) | [Architectural Patterns](../../get-started.md#architectural-patterns)

---
