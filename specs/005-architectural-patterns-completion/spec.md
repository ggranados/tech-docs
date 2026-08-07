# Feature Specification: Architectural Patterns Completion

**Feature Branch**: `content/architectural-patterns-completion`
**Created**: 2026-08-07
**Status**: Draft
**Input**: See spec/architectural-patterns-completion/spec.md — 5 topics are visibly broken (empty/plain-text after navigation-usability-overhaul removed dead links) and 8 more listed patterns have no page.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - The 5 previously-dead-link topics get real pages (Priority: P1)

A reader who saw Message-Driven, Event-Driven, Event Sourcing, Distributed Transaction, and Saga as plain text in `get-started.md` (fixed from broken links by an earlier spec) can now click through to real content for all five.

**Why this priority**: These were the highest-severity gap identified in the original audit — literal dead links on the hub page.

**Independent Test**: Confirm all 5 topics resolve to real pages with template-compliant sections.

**Acceptance Scenarios**:

1. **Given** `get-started.md`'s Architectural Patterns section, **When** Message-Driven, Event-Driven, Event Sourcing, Distributed Transaction, and Saga are clicked, **Then** each reaches a real page.
2. **Given** `docs/pages/architectural-patterns/message-driven.md` (currently 0 bytes), **When** filled in, **Then** it has genuine parent-concept content (not just a redirect to its Event-Driven child).

---

### User Story 2 - Remaining listed patterns get pages (Priority: P2)

A reader can reach MVC, MVVM, Repository Pattern, Layered, Hexagonal, CQRS, Monolithic, and Microkernel — all currently listed as plain text with no page.

**Why this priority**: Real gap but lower severity than the dead-link topics (these were never links promising content, just unlinked list items).

**Independent Test**: Confirm all 8 topics resolve to real pages.

### Edge Cases

- Event-Driven's existing content (`message-driven/event-driven.md`) is decent prose but lacks proper template structure (no title/TOC/Overview heading) — needs restructuring, not just supplementing.
- Message-Driven (parent) and Event-Driven (child) must be differentiated: Message-Driven is the broader "components communicate via messages" umbrella; Event-Driven is the specific case where those messages are events representing something that happened.
- Distributed Transaction and Saga nest under Microservices in `get-started.md`'s hierarchy; their pages should live at a matching nested path.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: `docs/pages/architectural-patterns/message-driven.md` MUST have real Overview/structure content (not be empty).
- **FR-002**: `docs/pages/architectural-patterns/message-driven/event-driven.md` MUST be restructured to template format (Title, TOC, Overview, etc.) while preserving its existing accurate prose.
- **FR-003**: New pages MUST exist for Event Sourcing, Distributed Transaction, Saga, MVC, MVVM, Repository Pattern, Layered, Hexagonal, CQRS, Monolithic, Microkernel.
- **FR-004**: Each new/restructured page MUST include a Mermaid diagram (`flowchart`/`sequenceDiagram` typically fit these message/event/architecture patterns best), Q&A, Related Topics, and Ref. sections.
- **FR-005**: `get-started.md`'s Architectural Patterns section MUST link every topic covered by this spec.

### Key Entities

- **Architectural pattern page**: one page per pattern under `docs/pages/architectural-patterns/`, nested to match `get-started.md`'s hierarchy where one exists (Message-Driven > Event-Driven > Event Sourcing; Microservices > Distributed Transaction > Saga).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: All 13 topics in scope (5 dead-link + 8 unlinked) have real pages.
- **SC-002**: `docs/pages/architectural-patterns/message-driven.md` is no longer 0 bytes.

## Assumptions

- `docs/pages/frameworks/backend/spring.md` (also found empty-ish/TODO-only during the template compliance audit) is explicitly out of scope here — it belongs to `frameworks-overview`.
- Content depth matches the existing Microservices/Reactive Systems pages — breadth over depth.
