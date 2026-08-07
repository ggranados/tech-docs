# Architectural Patterns Completion

**Severity:** High
**Depends on:** `content-template-compliance-audit`

## Problem

`get-started.md`'s Architectural Patterns section contains literal dead links with `TODO` markers: Message-Driven, Event-Driven, Event Sourcing, and Distributed Transaction/Saga (nested under Microservices) all link to `()`. Additionally, MVC, MVVM, Monolithic, Repository Pattern, Layered, Microkernel, Hexagonal, and CQRS are listed with no page at all (only Microservices and Reactive Systems currently have pages).

## Goal

Every pattern listed in `get-started.md`'s Architectural Patterns section has a real page. Resolve the dead-link entries first since they're visibly broken today.

## Scope hints

- Priority 1 (dead links today): Message-Driven, Event-Driven, Event Sourcing, Distributed Transaction, Saga.
- Priority 2 (listed, unlinked): MVC, MVVM, Repository Pattern, Layered, Hexagonal, CQRS, Monolithic, Microkernel.
- Use Mermaid `sequenceDiagram` or `flowchart` for message/event flow patterns (Event-Driven, Saga, CQRS) — these benefit strongly from a visual.
- Cross-link with related design patterns and data-processing pages already created (e.g., Event Streaming/Kafka ↔ Event-Driven; ACID/BASE ↔ Saga).
- Follow existing file layout: `docs/pages/architectural-patterns/{pattern}.md`, with nested patterns under a parent folder as already done for `message-driven/event-driven.md`.

## Out of scope

- Design patterns (separate spec: `design-patterns-completion`).
