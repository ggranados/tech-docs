# Implementation Plan: Visual Style & Theme Consistency

**Branch**: `content/visual-style-theme-consistency` | **Date**: 2026-08-07 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/001-visual-style-theme-consistency/spec.md`

## Summary

Reconcile the theme mismatch between `docs/_config.yml` (`remote_theme: pages-themes/hacker@v0.2.0`) and `CLAUDE.md` (documents `midnight@v0.2.0`), then add a small custom CSS layer (via `docs/_includes/head-custom.html` or a new `docs/assets/css/custom.css`) that fixes readability of code blocks, Mermaid diagrams, deeply nested lists, and tables under the resolved theme — no new theme framework, no JS-heavy styling.

## Technical Context

**Language/Version**: HTML/CSS (Jekyll/Liquid templating), GitHub Pages `remote_theme`

**Primary Dependencies**: `jekyll-remote-theme` plugin, `pages-themes/hacker@v0.2.0` (existing), Mermaid v11 (loaded via `docs/_includes/head-custom.html`)

**Storage**: N/A — static site, no data storage

**Testing**: Manual visual verification (no Jekyll build server assumed available in this environment); inspect rendered HTML/CSS by reading generated theme source and confirming selector scoping does not collide with theme classes

**Target Platform**: GitHub Pages (static hosting), all modern browsers, light/dark OS preference

**Project Type**: Static documentation site (Jekyll)

**Performance Goals**: N/A — negligible CSS payload increase, no runtime performance concern

**Constraints**: Must not require a new remote theme dependency; must not break existing Mermaid rendering already wired through `head-custom.html`; CSS additions must be scoped (no broad selector overrides that fight the remote theme's own stylesheet)

**Scale/Scope**: Site-wide (one config decision + one shared CSS layer), touches no individual content pages

## Constitution Check

No project constitution is defined (`.specify/memory/constitution.md` is the unfilled template) — no formal gates to evaluate. Proceeding directly per `CLAUDE.md`'s existing project conventions (Mermaid-only diagrams, kebab-case paths, standard page structure — none of which this feature touches).

## Project Structure

### Documentation (this feature)

```text
specs/001-visual-style-theme-consistency/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── quickstart.md         # Phase 1 output (manual verification guide)
└── tasks.md             # Phase 2 output (/speckit-tasks)
```

No `data-model.md` or `contracts/` — this feature has no data entities and no external interface/API; it is a config + stylesheet change to a static site.

### Source Code (repository root)

```text
docs/
├── _config.yml              # remote_theme key — reconcile with CLAUDE.md
├── _includes/
│   └── head-custom.html     # existing Mermaid script include; custom CSS may live here or...
└── assets/
    └── css/
        └── custom.css        # ...new file, linked from head-custom.html (preferred: keeps CSS out of HTML)

CLAUDE.md                    # Technology Stack section — update theme reference to match _config.yml
```

**Structure Decision**: Add `docs/assets/css/custom.css` (new) for the readability fixes, linked via a `<link>` tag added to `docs/_includes/head-custom.html` (existing file, already the site's mechanism for injecting head-level assets). Update whichever of `docs/_config.yml` / `CLAUDE.md` is stale so both name the same theme (see `research.md` for the decision).

## Complexity Tracking

No constitution violations — table not applicable.
