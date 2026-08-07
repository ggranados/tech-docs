# Visual Style & Theme Consistency

**Severity:** Critical
**Depends on:** none (foundation)

## Problem

The Jekyll site's `docs/_config.yml` sets `remote_theme: pages-themes/hacker@v0.2.0`, but the project's own `CLAUDE.md` documents the theme as `pages-themes/midnight@v0.2.0`. Whichever is correct, the two disagree, so the actual rendered site's visual identity is undocumented and unverified. Beyond the theme mismatch, the site relies entirely on the stock GitHub Pages theme with no custom styling: no adjustments for long-form technical reading (dense tables, code blocks, Mermaid diagrams, nested bullet lists 3-4 levels deep), no consistent color treatment for callouts/notes, and no verified light/dark handling for embedded diagrams and images.

## Goal

Pick and confirm one theme (resolve the doc/config mismatch), then layer a small custom stylesheet (via `docs/_includes/head-custom.html` or a `docs/assets/css` override) that improves readability of this specific content shape: code blocks, Mermaid diagrams, deeply nested lists, tables, and the `<sub>[Back to top]</sub>` convention. Verify Mermaid diagrams render legibly against the chosen theme's background in both light and dark viewing.

## Scope hints

- Reconcile `docs/_config.yml` theme with `CLAUDE.md` (update whichever is stale).
- Audit contrast/readability of code blocks, tables, and Mermaid diagrams under the chosen theme.
- Add minimal custom CSS for nested list spacing, table readability, and callout/note styling if absent.
- Confirm favicon and page title metadata are set consistently.
- Do not introduce a new theme framework or JS-heavy styling system — this is a light-touch pass, not a redesign.

## Out of scope

- Full rebrand or custom theme build.
- Restructuring navigation (see `navigation-usability-overhaul`).
