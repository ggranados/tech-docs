# Implementation Plan: Content & Template Compliance Audit

**Branch**: `content/content-template-compliance-audit` | **Date**: 2026-08-07 | **Spec**: [spec.md](./spec.md)

## Summary

Scan every page under `docs/pages/` (excluding `common/`) for the three template-mandated sections (Q&A, Related Topics, Ref.), add genuinely useful content where missing, verify internal relative links resolve, normalize TOC/Back-to-top placement, and reconcile the legacy `docs/pages/common/template.md` against `.claude/templates/page.md`.

## Technical Context

**Language/Version**: Markdown (GitHub Flavored), no code/build changes

**Primary Dependencies**: None

**Storage**: N/A

**Testing**: Manual verification per quickstart.md — grep-based section presence check + relative link resolution check

**Target Platform**: N/A (content only)

**Project Type**: Static documentation content audit

**Constraints**: No content rewrites beyond adding missing sections; no invented links to not-yet-existing pages

**Scale/Scope**: ~70 files under `docs/pages/`

## Constitution Check

No project constitution defined. Following `CLAUDE.md`'s existing "Markdown Document Structure" and "Key Patterns" sections directly, since this feature exists specifically to enforce them.

## Project Structure

```text
specs/003-content-template-compliance-audit/
├── plan.md
├── research.md
├── quickstart.md
└── tasks.md
```

No `data-model.md`/`contracts/` — pure content audit, no data entities or interfaces.

### Source Code (repository root)

```text
docs/pages/**/*.md         # add missing Q&A / Related Topics / Ref. sections; fix TOC/back-to-top; fix broken relative links
docs/pages/common/template.md   # reconcile with .claude/templates/page.md
```

**Structure Decision**: In-place edits across existing files; no new directories.

## Complexity Tracking

No constitution violations.
