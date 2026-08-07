# Feature Specification: Design Patterns Completion

**Feature Branch**: `content/design-patterns-completion`
**Created**: 2026-08-07
**Status**: Draft
**Input**: See spec/design-patterns-completion/spec.md — 21 listed design patterns have no page: Builder, Prototype (Creational); all 7 Structural patterns; 10 of 11 Behavioral patterns; Dependency Injection, Service Locator (IoC).

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Every listed design pattern has a real page (Priority: P1)

A reader browsing the Design Patterns section of `get-started.md` can click any listed pattern and reach a real page at the same quality bar as the existing Singleton/Factory/Observer pages — intent, structure, a diagram, a minimal example, Q&A, related patterns, references.

**Why this priority**: This is the single largest content gap identified in the project audit — Structural Patterns has zero coverage, and most of Behavioral is missing. Design Patterns is a flagship category for this repo's stated audience (architect trainees).

**Independent Test**: For each of the 21 listed patterns, confirm a page exists under `docs/pages/design-patterns/` with Overview, structure, Q&A, Related Topics, and Ref. sections.

**Acceptance Scenarios**:

1. **Given** any pattern name in `get-started.md`'s Design Patterns section, **When** clicked, **Then** it reaches a real page (not plain text, not a dead link).
2. **Given** a new pattern page, **When** compared against `.claude/templates/page.md`, **Then** it has the same section structure as existing compliant pages.

---

### User Story 2 - Related patterns are cross-linked (Priority: P2)

A reader on the Adapter page can find Facade and Decorator (commonly confused/compared patterns) without going back to the hub.

**Why this priority**: Design patterns are usually learned by comparison/contrast; cross-linking is where much of the training value lives.

**Independent Test**: Spot-check 3-4 new pages for Related Topics links to genuinely comparable patterns.

**Acceptance Scenarios**:

1. **Given** a Structural pattern page, **When** its Related Topics section is read, **Then** it links to at least one other Structural pattern it's commonly compared with.

### Edge Cases

- Patterns with very similar intent (Adapter vs. Facade vs. Decorator; Strategy vs. State vs. Template Method) must have text that distinguishes them specifically, not boilerplate that could apply to any pattern.
- IoC/DI/Service Locator overlaps conceptually with Factory (creational) — Related Topics should acknowledge this without duplicating Factory's content.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Pages MUST exist for: Builder, Prototype (`docs/pages/design-patterns/creational/`); Adapter, Decorator, Proxy, Composite, Facade, Bridge, Flyweight (`docs/pages/design-patterns/structural/`, new directory); Strategy, Template Method, Command, Iterator, Mediator, State, Visitor, Chain of Responsibility, Interpreter, Memento (`docs/pages/design-patterns/behavioral/`); Dependency Injection, Service Locator (`docs/pages/design-patterns/ioc/`, new directory).
- **FR-002**: Each page MUST include intent, structure/participants, a Mermaid `classDiagram` (or `sequenceDiagram` where interaction order matters more than structure), a minimal code example, 3 Q&A entries, Related Topics, and Ref. sections.
- **FR-003**: `get-started.md`'s Design Patterns section MUST link every pattern covered by this spec to its real page.
- **FR-004**: Diagrams MUST use Mermaid only, per `CLAUDE.md`.

### Key Entities

- **Pattern page**: one page per design pattern under `docs/pages/design-patterns/{creational|structural|behavioral|ioc}/`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: All 21 patterns listed in scope have a real page reachable from `get-started.md`.
- **SC-002**: Zero plain-text (unlinked) pattern names remain in `get-started.md`'s Design Patterns section for patterns covered by this spec.

## Assumptions

- Page depth matches the existing Singleton/Factory/Observer bar (breadth over depth — one focused page per pattern, not a multi-page deep-dive per pattern).
- New `structural/` and `ioc/` directories follow the same layout convention as the existing `creational/` and `behavioral/` directories.
- Because this is a large batch of similarly-shaped new pages, content is produced by parallel delegated writers grouped by pattern family, each following the same template and quality bar, to keep total turnaround reasonable without sacrificing per-page depth.
