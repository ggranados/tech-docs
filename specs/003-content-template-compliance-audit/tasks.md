# Tasks: Content & Template Compliance Audit

**Tests**: Not requested — verified via quickstart.md manual inspection (grep for section headings + link resolution check).

## Phase 3: User Story 1 - Every page has the sections a reader expects (P1)

- [x] T001 [P] [US1] Audit and fix pages under `docs/pages/architectural-patterns/`, `docs/pages/frameworks/`, `docs/pages/cyber-security/`, `docs/pages/design-patterns/` missing Q&A/Related Topics/Ref. (delegated batch 1) — 2 files skipped as out of scope: `architectural-patterns/message-driven.md` (0 bytes, no content) and `frameworks/backend/spring.md` (TODO-only stub); both need real Overview/topic content before Q&A can be meaningful — deferred to `architectural-patterns-completion` and `frameworks-overview` respectively
- [x] T002 [P] [US1] Audit and fix pages under `docs/pages/data-processing/` missing Q&A/Related Topics/Ref. (delegated batch 2)
- [x] T003 [P] [US1] Audit and fix Java core + collections pages under `docs/pages/programming/languages/java/` missing Q&A/Related Topics (delegated batch 3)
- [x] T004 [P] [US1] Audit and fix Java 8/9 concurrency + streams pages missing Q&A/Related Topics (delegated batch 4)
- [x] T005 [P] [US1] Audit and fix `docs/pages/programming/paradigms/` pages missing Q&A/Related Topics (delegated batch 5)
- [x] T006 [P] [US1] Audit and fix `docs/pages/ws-and-api-design/` pages missing Q&A/Related Topics (delegated batch 6)

## Phase 4: User Story 2 - No dead internal links (P1)

- [x] T007 [US2] Scan all relative links in `docs/pages/**/*.md` (including newly-added Related Topics links) and confirm every target resolves to an existing file; fix any that don't — found and fixed 10 pre-existing broken links (factory.md family get-started.md path depth, java-8 concurrency/*.md concurrency.md path depth, imperative.md's empty Declarative Programming link)

## Phase 5: User Story 3 - One canonical template (P2)

- [x] T008 [US3] Delete `docs/pages/common/template.md`
- [x] T009 [US3] Update `CLAUDE.md`'s Important Files and Notes for AI Assistants sections to drop the `common/template.md` reference

## Phase 6: Polish

- [x] T010 Run quickstart.md end-to-end
