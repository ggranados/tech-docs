# Implementation Plan: Navigation & Usability Overhaul

**Branch**: `content/navigation-usability-overhaul` | **Date**: 2026-08-07 | **Spec**: [spec.md](./spec.md)

## Summary

Remove dead TODO links from `docs/get-started.md`, clarify `docs/index.md` vs `docs/get-started.md` purpose split, add a lightweight vanilla-JS filter box to the navigation hub, and clean up formatting artifacts.

## Technical Context

**Language/Version**: Markdown + inline HTML/vanilla JS (no framework), Jekyll/Kramdown rendering

**Primary Dependencies**: None new — plain `<script>`/`<input>` embedded directly in `get-started.md` (Kramdown passes raw HTML through)

**Storage**: N/A

**Testing**: Manual verification per quickstart.md (no live Jekyll server assumed; verify JS logic by reading it and confirming DOM selectors match the page's actual list structure)

**Target Platform**: GitHub Pages, all modern browsers

**Project Type**: Static documentation site (Jekyll)

**Constraints**: No external search service/API; must degrade gracefully without JS; must not require a Jekyll plugin beyond what's already installed

**Scale/Scope**: Two files primarily (`get-started.md`, `index.md`); no changes to individual content pages (that's `content-template-compliance-audit`'s scope)

## Constitution Check

No project constitution defined — no formal gates. Following `CLAUDE.md` conventions already in place (this spec touches only `get-started.md`/`index.md`, doesn't introduce new content categories or diagram types).

## Project Structure

```text
specs/002-navigation-usability-overhaul/
├── plan.md
├── research.md
├── quickstart.md
└── tasks.md
```

No `data-model.md`/`contracts/` — no data entities, no external interface.

### Source Code (repository root)

```text
docs/
├── get-started.md   # remove dead links, add filter box + JS, formatting cleanup
└── index.md          # trim redundant framing, clearer CTA to get-started.md
```

**Structure Decision**: In-page filter implemented as a `<script>` block embedded directly at the top of `get-started.md` (Kramdown/Jekyll renders raw HTML in markdown files), operating on the existing `<ul>`/`<li>` DOM structure already produced by the page's Markdown lists — no new include file needed since this behavior is specific to this one page.

## Complexity Tracking

No constitution violations.
