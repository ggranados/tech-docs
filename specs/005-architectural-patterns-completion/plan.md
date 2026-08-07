# Implementation Plan: Architectural Patterns Completion

**Branch**: `content/architectural-patterns-completion` | **Date**: 2026-08-07 | **Spec**: [spec.md](./spec.md)

## Summary

Fill in the empty `message-driven.md`, restructure `event-driven.md` to template format, and write 11 new pattern pages (Event Sourcing, Distributed Transaction, Saga, MVC, MVVM, Repository Pattern, Layered, Hexagonal, CQRS, Monolithic, Microkernel), then wire all 13 into `get-started.md`.

## Technical Context

**Language/Version**: Markdown + Mermaid; Java where a code example helps, otherwise pseudocode/config-style examples (these are architecture-level patterns, less code-example-driven than design patterns)

**Testing**: Manual verification per quickstart.md

**Project Type**: Static documentation content

**Constraints**: Mermaid-only diagrams; template structure; breadth over depth

**Scale/Scope**: 13 files (2 fix/restructure existing, 11 new) across `docs/pages/architectural-patterns/` and 2 nested subdirectories, plus `get-started.md`

## Constitution Check

No project constitution defined. Following `CLAUDE.md` conventions directly.

## Project Structure

```text
specs/005-architectural-patterns-completion/
├── plan.md
├── research.md
├── quickstart.md
└── tasks.md
```

### Source Code (repository root)

```text
docs/pages/architectural-patterns/
├── message-driven.md                                    # fill in (currently empty)
├── message-driven/
│   ├── event-driven.md                                   # restructure to template
│   └── event-driven/
│       └── event-sourcing.md                             # new
├── microservices.md                                       # existing, link update only
├── microservices/
│   └── distributed-transaction.md                         # new
│       └── distributed-transaction/
│           └── saga.md                                    # new
├── mvc.md              # new
├── mvvm.md             # new
├── repository-pattern.md  # new
├── layered.md           # new
├── hexagonal.md         # new
├── cqrs.md               # new
├── monolithic.md        # new
└── microkernel.md       # new

docs/get-started.md      # link all 13 topics
```

**Structure Decision**: Nest Event Sourcing under `message-driven/event-driven/` and Saga under `microservices/distributed-transaction/`, matching `get-started.md`'s existing indentation hierarchy exactly. Flat patterns (MVC, MVVM, etc.) sit directly under `architectural-patterns/`.

## Complexity Tracking

No constitution violations.
