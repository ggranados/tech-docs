# Tasks: Navigation & Usability Overhaul

**Tests**: Not requested — verified via quickstart.md manual inspection.

## Phase 3: User Story 1 - No dead links (P1)

- [x] T001 [US1] Replace the 5 empty-link TODO entries in `docs/get-started.md` (Distributed Transaction, Saga, Message-Driven, Event-Driven, Event Sourcing) with plain text (no `[..]()`, no `<!-- TODO -->`)

## Phase 4: User Story 2 - Landing vs. hub distinction (P2)

- [x] T002 [US2] Rewrite `docs/index.md` to a short welcome/pitch + clear CTA into `get-started.md`, removing the bullet list that duplicates `get-started.md`'s own topic list
- [x] T003 [US2] Confirm `docs/get-started.md`'s opening lines don't re-state the same welcome pitch as `index.md`

## Phase 5: User Story 3 - In-page filter (P2)

- [x] T004 [US3] Add a search `<input>` + vanilla-JS filter `<script>` near the top of `docs/get-started.md` (above the TOC), filtering `<li>` visibility against typed text

## Phase 6: Polish

- [x] T005 Clean up stray trailing whitespace and inconsistent indentation around `<sub>[Back to top]</sub>` markers in `docs/get-started.md`
- [x] T006 Run through quickstart.md end-to-end
