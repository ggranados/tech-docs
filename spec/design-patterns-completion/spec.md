# Design Patterns Completion

**Severity:** High
**Depends on:** `content-template-compliance-audit` (follow the confirmed template)

## Problem

`get-started.md`'s Design Patterns section lists many patterns with no linked page: Builder, Prototype (Creational); Adapter, Decorator, Proxy, Composite, Facade, Bridge, Flyweight (all of Structural); Strategy, Template Method, Command, Iterator, Mediator, State, Visitor, Chain of Responsibility, Interpreter, Memento (most of Behavioral); Dependency Injection, Service Locator (IoC). Only Factory (+ its 3 sub-patterns), Singleton, Observer, and SOLID currently have pages. Structural Patterns as a category has zero coverage.

## Goal

Every design pattern listed in `get-started.md`'s Design Patterns section has a page, at the same breadth-over-depth quality bar as the existing Singleton/Factory/Observer pages (intent, structure/participants, a Mermaid class or sequence diagram, a minimal code example, Q&A, related patterns, references).

## Scope hints

- Prioritize Structural patterns first (zero current coverage) then remaining Behavioral, then Builder/Prototype, then IoC/DI/Service Locator.
- Follow the file layout convention already established (`docs/pages/design-patterns/{creational|structural|behavioral}/{pattern}.md`).
- Cross-link related patterns (e.g., Adapter ↔ Facade ↔ Decorator; Strategy ↔ Template Method).
- Use Mermaid `classDiagram` for structure, per `CLAUDE.md`'s diagram rule (Mermaid only).

## Out of scope

- Architectural patterns (separate spec: `architectural-patterns-completion`).
