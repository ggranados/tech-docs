# Feature Specification: Content & Template Compliance Audit

**Feature Branch**: `content/content-template-compliance-audit`
**Created**: 2026-08-07
**Status**: Draft
**Input**: See spec/content-template-compliance-audit/spec.md — existing pages under docs/pages/ predate .claude/templates/page.md to varying degrees; inconsistent Q&A/Related Topics/Ref sections; legacy docs/pages/common/template.md needs reconciling.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Every page has the sections a reader expects (Priority: P1)

A reader who's used the site for a while (and knows every page ends with Q&A, Related Topics, and Ref. sections) never lands on an older page that's missing one, breaking the pattern.

**Why this priority**: Consistency is this repo's stated core value (CLAUDE.md: "Consistency is Key"). A reader trusting the pattern and hitting a gap undermines the whole training-path structure.

**Independent Test**: Scan every file under `docs/pages/` for `## Q&A`, `## Related Topics`, and `## Ref.` headings; confirm all three exist in every page.

**Acceptance Scenarios**:

1. **Given** any page under `docs/pages/`, **When** inspected, **Then** it has `## Q&A`, `## Related Topics`, and `## Ref.` sections with real content (not placeholders).
2. **Given** a page missing one of these sections, **When** fixed, **Then** the added content is genuinely useful for that specific topic, not generic filler.

---

### User Story 2 - No dead internal links inside content pages (Priority: P1)

A reader clicking a relative link from within a content page (not just the get-started.md hub) always lands on a real page.

**Why this priority**: Internal 404s inside content pages are as damaging as hub-level dead links but harder to catch without a scan, since they're spread across 70+ files.

**Independent Test**: Extract all relative markdown links from every page under `docs/pages/` and confirm each resolves to an existing file.

**Acceptance Scenarios**:

1. **Given** all relative links in `docs/pages/**/*.md`, **When** resolved against the filesystem, **Then** every target file exists.

---

### User Story 3 - One canonical template, not two (Priority: P2)

A contributor (human or AI) writing a new page has exactly one template to follow, not two different files with unclear precedence.

**Why this priority**: `docs/pages/common/template.md` and `.claude/templates/page.md` both exist; `CLAUDE.md` calls the former "legacy" but doesn't say what to do with it. Ambiguity here causes drift every time a new page is added.

**Independent Test**: Check `docs/pages/common/template.md` — it should either be deleted, or clearly marked as superseded with a pointer to the current template.

**Acceptance Scenarios**:

1. **Given** `docs/pages/common/template.md`, **When** inspected, **Then** its status relative to `.claude/templates/page.md` is unambiguous.

### Edge Cases

- Pages that are intentionally short/simple (e.g., a concept with few natural Q&A angles) still need a genuine Q&A section, not a stretched, low-value one — favor 2-3 real questions over padding to hit a count.
- `docs/pages/common/not-found.md` and `docs/pages/common/template.md` are explicitly excluded from this audit's "every page needs Q&A/Related Topics/Ref" rule (per CLAUDE.md, `common/` is excluded from link validation and isn't reader-facing content) — only the template-reconciliation user story (US3) applies to `common/`.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Every reader-facing page under `docs/pages/` (excluding `docs/pages/common/`) MUST have `## Q&A`, `## Related Topics`, and `## Ref.` sections.
- **FR-002**: Added Q&A/Related Topics/Ref content MUST be specific to that page's topic, not generic placeholder text.
- **FR-003**: Every relative internal link within `docs/pages/**/*.md` MUST resolve to an existing file.
- **FR-004**: `<!-- TOC -->` blocks MUST reflect the page's actual headings.
- **FR-005**: `<sub>[Back to top](#table-of-contents)</sub>` MUST appear consistently after each major section, matching the pattern already used in compliant pages (e.g., `singleton.md`'s Observer counterpart, `factory.md`).
- **FR-006**: `docs/pages/common/template.md`'s relationship to `.claude/templates/page.md` MUST be unambiguous — either removed or explicitly marked as superseded.

### Key Entities

- **Content page**: any `.md` file under `docs/pages/` excluding `docs/pages/common/`.
- **Template**: `.claude/templates/page.md`, the canonical structure new/updated pages should follow.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of pages under `docs/pages/` (excluding `common/`) have Q&A, Related Topics, and Ref. sections.
- **SC-002**: Zero broken relative links found across `docs/pages/**/*.md`.
- **SC-003**: `docs/pages/common/template.md` has a single, unambiguous status.

## Assumptions

- This spec runs before the content-completion specs (design patterns, architectural patterns, missing-topic categories) so those specs' new pages are written to the confirmed template from the start, rather than needing a second compliance pass later.
- Where a page is missing Related Topics, links are chosen from genuinely related *existing* pages only — this spec does not invent links to pages that don't exist yet (those will self-resolve as later specs add the missing topic pages).
- Effort is scoped to structural compliance (are the sections present and genuinely useful) rather than a full content rewrite of otherwise-correct existing prose.
