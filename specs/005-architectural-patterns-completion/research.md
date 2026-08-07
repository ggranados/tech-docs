# Phase 0 Research: Architectural Patterns Completion

## Decision: Restructure `event-driven.md` in place rather than rewriting from scratch

**Rationale**: Its existing prose is accurate and reasonably detailed (covers events, producers, consumers, brokers, benefits) — the gap is structural (no title/TOC/Overview headings, no footer nav), not factual. Preserving the prose while wrapping it in template structure is less wasteful than a rewrite and lower-risk (no chance of losing accurate content).

## Decision: Nest Event Sourcing and Saga to match `get-started.md`'s existing indentation

**Rationale**: `get-started.md` already expresses the hierarchy (Message-Driven > Event-Driven > Event Sourcing; Microservices > Distributed Transaction > Saga) via nested bullets. Matching that in the directory structure keeps the relationship visible in the repo layout too, consistent with how `factory.md` and its sub-patterns already nest.

## Open questions resolved

No [NEEDS CLARIFICATION] markers remain.
