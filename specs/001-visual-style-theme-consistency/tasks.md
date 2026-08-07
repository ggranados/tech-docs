# Tasks: Visual Style & Theme Consistency

**Input**: Design documents from `specs/001-visual-style-theme-consistency/`
**Prerequisites**: plan.md, research.md, quickstart.md

**Tests**: Not requested for this feature (static-site visual change, verified via `quickstart.md` manual inspection).

## Phase 1: Setup

- [x] T001 Create `docs/assets/css/` directory for the new custom stylesheet

## Phase 2: Foundational

None — the theme decision (US1) is itself the first user-facing increment; no separate blocking infrastructure exists.

## Phase 3: User Story 1 - Trustworthy, consistent visual identity (Priority: P1)

**Goal**: `docs/_config.yml` and `CLAUDE.md` agree on one theme, and the published site renders it.

**Independent Test**: Read both files side by side; confirm identical `remote_theme` value.

- [x] T002 [US1] Update `remote_theme:` in `docs/_config.yml` to `pages-themes/midnight@v0.2.0` (per research.md decision)
- [x] T003 [US1] Update the Technology Stack section of `CLAUDE.md` to confirm `pages-themes/midnight@v0.2.0` as the theme (verify it already matches after T002; fix if wording is stale, e.g. version pin)

**Checkpoint**: `docs/_config.yml` and `CLAUDE.md` name the same theme — SC-001 satisfied.

---

## Phase 4: User Story 2 - Readable long-form technical content (Priority: P1)

**Goal**: Code blocks, Mermaid diagrams, nested lists, and tables are readable under the resolved theme.

**Independent Test**: Render `docs/pages/design-patterns/creational/singleton.md` (or inspect its generated structure) and confirm code blocks, the diagram, and lists follow the new CSS rules.

- [x] T004 [P] [US2] Create `docs/assets/css/custom.css` with: (a) `overflow-x: auto` on code block and table containers so wide content scrolls within itself rather than breaking layout, (b) increased/visually distinct indentation spacing for nested `<ul>`/`<ol>` at 3+ levels
- [x] T005 [US2] Link `docs/assets/css/custom.css` from `docs/_includes/head-custom.html` via a `<link rel="stylesheet">` tag, after the existing Mermaid script include
- [x] T006 [US2] Add a Mermaid `theme`/`themeVariables` init configuration in `docs/_includes/head-custom.html`'s existing Mermaid initialization call so diagram strokes/text/background have adequate contrast against the Midnight theme's dark background
- [x] T007 [US2] Spot-check the 5 representative pages listed in `quickstart.md` (`singleton.md`, `cap.md`, `microservices.md`, `threads.md`, `get-started.md`) against the new CSS rules per the quickstart verification steps

**Checkpoint**: Code blocks/tables scroll within their containers, nested lists are visually distinct per level, Mermaid diagrams are legible — SC-002 satisfied.

---

## Phase 5: User Story 3 - Consistent site metadata (Priority: P3)

**Goal**: Favicon and page title present site-wide.

**Independent Test**: Load the site with the browser tab bar visible; favicon and title appear.

- [x] T008 [US3] Confirm/add a favicon reference in `docs/_config.yml` (or an overriding `_layouts/default.html` if the Midnight theme doesn't expose a config-level favicon hook) so it applies to every page from one place

**Checkpoint**: Favicon appears in the browser tab on any page — SC-003 satisfied.

---

## Phase 6: Polish & Cross-Cutting

- [x] T009 Run through all steps in `quickstart.md` end-to-end and note any remaining contrast/overflow issues found during spot-checks
- [x] T010 Update `spec/visual-style-theme-consistency/spec.md`'s status implicitly by confirming this feature's outcomes match its original "Goal"/"Scope hints" (no file change expected unless a gap is found)

## Dependencies

- T001 blocks T004 (directory must exist before the file is created in it).
- US1 (T002-T003) should land before US2 (T004-T007) — the CSS/Mermaid contrast work in US2 targets the Midnight theme's actual palette, so the theme decision must be final first.
- US3 (T008) is independent of US1/US2 and can run in parallel with either.
- T009-T010 (Polish) run last, after all user stories are complete.

## Parallel Execution Example

```text
# After T001 (directory exists) and T002-T003 (theme decided), these can run together:
T004 [P] [US2] Create docs/assets/css/custom.css
T008 [US3] Add favicon config
```

## Implementation Strategy

**MVP** = User Story 1 alone (T001-T003): resolves the trust/documentation-accuracy problem even before any CSS polish ships. User Story 2 (readability CSS) is the next increment and delivers the bulk of the visible value. User Story 3 (favicon) is a small independent polish item that can slot in anywhere.
