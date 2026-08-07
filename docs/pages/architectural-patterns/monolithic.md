# Monolithic Architecture

---

## Table of Contents
<!-- TOC -->
* [Monolithic Architecture](#monolithic-architecture)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Characteristics of a Monolith](#characteristics-of-a-monolith)
  * [When a Monolith Is the Right Choice](#when-a-monolith-is-the-right-choice)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

A **Monolithic Architecture** structures an application as a single, unified codebase deployed and run as one unit. All modules — UI, business logic, data access — are compiled, packaged, and scaled together. It is the traditional default for building software, and despite the industry's enthusiasm for microservices, it remains a sound, often preferable, choice for a large share of systems, particularly early in a product's life or when a small team owns the whole system.

---

## Overview

Monolithic architecture predates most alternative styles simply because it's the natural result of building software without deliberately splitting it into independently deployable pieces: one repository, one build, one deployable artifact (a WAR/JAR, a single container image, a single process). Internal components communicate through in-process method calls rather than the network, which keeps development, debugging, and end-to-end reasoning about the system considerably simpler than in a distributed architecture.

The reputation of monoliths took a hit as "legacy" or "the thing you migrate away from" during the rise of microservices, largely because *poorly modularized* monoliths — with tangled dependencies between unrelated concerns, no internal boundaries, and a single database everyone writes to freely — become genuinely hard to change safely as they grow. But that outcome is a failure of internal modularity, not an inherent property of the monolithic style itself. A well-modularized monolith (sometimes called a **modular monolith**) can enjoy clear internal boundaries between modules while still deploying as one unit.

Martin Fowler and others have argued for **"MonolithFirst"**: starting new systems as a monolith and only splitting into services once real scaling or team-boundary pain justifies the operational cost of distribution, since the cost of prematurely distributing a system you don't yet understand well is often higher than the cost of a well-organized monolith.

<sub>[Back to top](#table-of-contents)</sub>

---

## Characteristics of a Monolith

A monolith is defined less by its internal code organization and more by its deployment and runtime unity.

- ### Single Codebase and Deployment Unit:
  All functionality lives in one repository and is built, tested, and deployed as a single artifact. There is no independent versioning or independent release cadence per module.

- ### In-Process Communication:
  Modules call each other directly through method/function calls within the same process. There is no network hop, no serialization, and no partial-failure handling between internal components — a call either succeeds or the whole process fails together.

  ```mermaid
  flowchart TB
      subgraph Monolith["Single Deployable Process"]
          UI["Presentation Layer"]
          Orders["Order Module"]
          Inventory["Inventory Module"]
          Billing["Billing Module"]
          DB[("Shared Database")]
          UI --> Orders
          UI --> Inventory
          Orders --> Inventory
          Orders --> Billing
          Orders --> DB
          Inventory --> DB
          Billing --> DB
      end
  ```

  **Caption:** Modules communicate through direct in-process calls and typically share a single database — everything ships and scales as one unit.

- ### Single Shared Database:
  Most monoliths use one database for all modules, which makes transactional consistency across modules trivial (a single ACID transaction can span multiple modules' tables) but also makes it easy for modules to become coupled through shared tables if discipline isn't enforced.

<sub>[Back to top](#table-of-contents)</sub>

---

## When a Monolith Is the Right Choice

Choosing a monolith is not settling for less — for many contexts it is the architecturally correct decision.

- ### Smaller Teams and Simpler Ownership:
  A single team, or a small number of closely collaborating teams, can own and reason about one codebase far more easily than a fleet of independently deployed services, each with its own repo, pipeline, and on-call rotation.

- ### No Network-Call Overhead Between Modules:
  Because inter-module calls are in-process, there is no network latency, no serialization cost, and no need for retries, timeouts, or circuit breakers between components that a microservices split would introduce for the exact same interactions.

- ### Simpler Deployment and Operations:
  One artifact to build, test, version, and deploy means simpler CI/CD pipelines, easier local development (no need to run a dozen services to test one feature), and fewer moving parts to monitor.

- ### Contrast with Microservices:
  Microservices trade this simplicity for independent deployability, independent scaling, and technology diversity per service — valuable when different parts of a system have genuinely different scaling profiles or when many autonomous teams need to ship independently. That trade only pays off once the organizational or scaling need is real; adopting it prematurely mainly adds distributed-systems complexity (network failures, eventual consistency, service discovery) without a corresponding benefit.

  > See also: [Microservices Architecture](microservices.md)

- ### Internal Structure Still Matters:
  A monolith benefits from the same internal discipline a Layered architecture provides — clear separation between presentation, business logic, and data access — so that even though everything deploys together, the codebase itself stays navigable and testable as it grows.

  > See also: [Layered Architecture](layered.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Monolith | A system deployed and run as a single unit, regardless of its internal module count |
| Modular Monolith | A monolith with clear, enforced boundaries between internal modules, despite shipping as one artifact |
| MonolithFirst | The strategy of starting a new system as a monolith and splitting into services later, once justified |
| Big Ball of Mud | A monolith that lacks internal modularity — the failure mode monoliths are (unfairly) generalized from |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

A modular monolith keeps the single-deployment benefit while enforcing boundaries through package structure and internal APIs:

```
src/
  order/
    OrderService.java        // public API for the order module
    OrderRepository.java     // internal — package-private
  inventory/
    InventoryService.java    // public API for the inventory module
    InventoryRepository.java // internal — package-private
  billing/
    BillingService.java
```

`OrderService` calls `InventoryService.reserve(...)` through its public interface rather than touching `InventoryRepository` directly — the module boundary is enforced by visibility rules within a single build, giving much of microservices' decoupling discipline without the network and deployment overhead.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Is a monolith always a legacy anti-pattern I should migrate away from?**
A: No. "Monolith" describes a deployment unit, not code quality. A well-modularized monolith with clear internal boundaries can be a perfectly good long-term architecture, especially for a small team or a system without sharply divergent scaling needs across its parts. The "legacy anti-pattern" reputation really describes an unmodularized *big ball of mud*, which can happen in a monolith or in a poorly designed set of microservices alike.

---

**Q: What's a "modular monolith" and how is it different from a plain monolith?**
A: Both deploy as a single unit. A modular monolith additionally enforces internal boundaries — through package visibility, module systems, or internal APIs — so that modules can only interact through defined contracts, similar in spirit to how microservices interact through APIs, just without the network. This makes a future split into services easier if it's ever needed, and keeps the codebase navigable as it grows.

---

**Q: When should a team choose a monolith over microservices for a new project?**
A: When the team is small enough that one codebase is easier to coordinate than many services, when the system's scaling needs are roughly uniform across its parts (no single module needs to scale independently at a very different rate), and when the operational maturity to run a distributed system (service discovery, distributed tracing, network resilience patterns) doesn't yet exist on the team. Starting as a monolith and splitting later, once real pain justifies it, is a common and defensible strategy.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Microservices Architecture](microservices.md) — the primary alternative, trading deployment simplicity for independent scaling and deployability
- [Layered Architecture](layered.md) — the internal structuring style most monoliths use to stay organized as they grow
- [Hexagonal Architecture (Ports and Adapters)](hexagonal.md) — an internal structuring approach that can keep a monolith's domain logic decoupled from its infrastructure

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Martin Fowler — MonolithFirst](https://martinfowler.com/bliki/MonolithFirst.html) — the case for starting new systems as a monolith
- [Martin Fowler — Monolith](https://martinfowler.com/bliki/Monolith.html) — clarifying what "monolith" does and doesn't mean

---

[Get Started](../../get-started.md) | [Architectural Patterns](../../get-started.md#architectural-patterns)

---
