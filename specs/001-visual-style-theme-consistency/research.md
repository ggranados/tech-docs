# Phase 0 Research: Visual Style & Theme Consistency

## Decision: Resolve the theme mismatch by switching `docs/_config.yml` to `pages-themes/midnight@v0.2.0`

**Rationale**: `CLAUDE.md` already documents Midnight as the intended theme, suggesting it was the deliberate original choice and `hacker` is either a later experiment or an accidental edit. Both `pages-themes/hacker` and `pages-themes/midnight` are dark GitHub Pages themes, but they target different content styles:

- `hacker` renders a terminal/green-on-black aesthetic — high-contrast novelty styling that works for short marketing-style pages but fights readability at the density this repo actually has (3-4 level nested bullets, wide tables, long code blocks, Mermaid diagrams).
- `midnight` uses a more conventional dark navy/charcoal palette with neutral text and link colors, styled closer to a technical documentation site than a terminal emulator — a better fit for a long-form study-guide product.

**Alternatives considered**:
- *Keep `hacker`, add heavy custom CSS overrides to neutralize its terminal styling.* Rejected — this means fighting the theme's own design intent with `!important`-heavy overrides (a pattern this spec's FR-007 explicitly avoids), for a worse end result than picking the theme already suited to the content.
- *Update `CLAUDE.md` to document `hacker` as authoritative instead of changing the config.* Rejected — no evidence `hacker` was a deliberate choice (no commit message or doc explains it), while Midnight is referenced consistently elsewhere in `CLAUDE.md`'s Technology Stack section as the intended theme.

## Decision: Layer custom CSS via a new `docs/assets/css/custom.css`, linked from `docs/_includes/head-custom.html`

**Rationale**: `head-custom.html` is already the project's established mechanism for injecting head-level assets (it currently loads the Mermaid v11 CDN script). Adding a `<link rel="stylesheet">` there to a new dedicated CSS file keeps the override surface small, versioned, and easy to find — rather than inlining `<style>` blocks in the HTML include or forking the theme's own SCSS.

**Alternatives considered**:
- *Fork/vendor the theme's SCSS and edit directly.* Rejected — couples this repo to theme internals, breaks on `remote_theme` version bumps, far more maintenance than a small override layer.
- *Inline `<style>` block directly in `head-custom.html`.* Rejected — works but mixes concerns (script includes + styling) in one file; a separate `custom.css` is easier to review and extend later.

## Decision: Fix Mermaid diagram contrast via Mermaid's theme configuration, not CSS overrides on rendered SVG

**Rationale**: Mermaid is initialized via its JS API (in `head-custom.html`) and supports a `theme` / `themeVariables` init option that controls diagram colors directly, which is more reliable than trying to override auto-generated inline SVG styles with external CSS (Mermaid inlines many styles on the SVG elements themselves, which CSS specificity often can't beat cleanly).

**Alternatives considered**:
- *CSS overrides targeting `.mermaid svg` descendants.* Rejected as primary approach — fragile against Mermaid version updates that change internal class names/inline styles; kept only as a light fallback if Mermaid's own theme variables can't cover every element (e.g., diagram background).

## Open questions resolved (no remaining [NEEDS CLARIFICATION])

All ambiguity in the source spec (`spec/visual-style-theme-consistency/spec.md`) was resolved directly in `spec.md`'s Assumptions section per the project owner's instruction to resolve conflicts without pausing execution. No outstanding unknowns block Phase 1.
