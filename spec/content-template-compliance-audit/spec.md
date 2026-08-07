# Content & Template Compliance Audit

**Severity:** High
**Depends on:** none (foundation; establishes the baseline all content specs should follow)

## Problem

`.claude/templates/page.md` defines the canonical page structure (TOC, Overview, subtopic sections, Key Concepts, Examples, Q&A, Related Topics, Ref., footer nav). Existing pages predate this template to varying degrees and are inconsistent: some lack a Q&A section, some lack Related Topics or Ref. sections, some have inconsistent `<sub>[Back to top]</sub>` placement, and TOC blocks may be stale relative to actual headings. `docs/pages/common/template.md` is also referenced as a "legacy" template in `CLAUDE.md`, alongside the newer `.claude/templates/page.md` — it's unclear if the legacy one should be deleted or reconciled.

## Goal

Every existing page under `docs/pages/` conforms to the current template structure, so the site reads as one consistent product rather than a set of documents written at different times with different conventions.

## Scope hints

- Inventory all existing pages under `docs/pages/` against `.claude/templates/page.md`'s section structure.
- Add missing Q&A, Related Topics, or Ref. sections to pages that lack them (content should be genuinely useful, not filler).
- Normalize `<!-- TOC -->` blocks and `<sub>[Back to top]</sub>` placement.
- Reconcile `docs/pages/common/template.md` vs `.claude/templates/page.md` — either delete the legacy one or clearly document when each applies.
- Verify internal relative links in existing pages resolve to real files (no silent 404s within `docs/`).

## Out of scope

- Writing net-new topic pages (handled by other specs) — this is a compliance pass over what already exists.
