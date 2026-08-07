# Feature Specification: Programming Languages Overview Pages

**Feature Branch**: `content/programming-languages-overview`
**Created**: 2026-08-07
**Status**: Draft
**Input**: See spec/programming-languages-overview/spec.md — 13 of 15 listed languages have no page (only Java is covered, in depth); duplicate "JavaScript" bullet.

## Requirements *(mandatory)*

- **FR-001**: One breadth-level overview page MUST exist for: JavaScript, Python, PHP, Ruby, C#, Go, Rust, Kotlin, Clojure, Swift, TypeScript, and HTML+CSS (combined as web markup/styling fundamentals).
- **FR-002**: Each page covers: paradigm(s), typical use cases, runtime/deployment shape, ecosystem/tooling note, links to official docs — explicitly NOT Java-tree depth (no multi-page version history, no deep sub-topic breakdown).
- **FR-003**: The duplicate "JavaScript" bullet in `get-started.md`'s Languages list MUST be removed (merged into one entry).
- **FR-004**: `get-started.md`'s Languages list MUST link every language to its page.

## Success Criteria *(mandatory)*

- **SC-001**: All 13 non-Java languages are covered by a real, linked page.
- **SC-002**: Exactly one "JavaScript" entry remains in the Languages list.

## Assumptions

- 12 new pages (13 bullets minus 1 duplicate minus HTML+CSS combined into one).
- File layout: `docs/pages/programming/languages/{language}.md` (flat, single file per language — NOT the deep directory-per-version-era structure Java uses, which is explicitly the exception per the source spec).
