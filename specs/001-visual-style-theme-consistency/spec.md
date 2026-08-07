# Feature Specification: Visual Style & Theme Consistency

**Feature Branch**: `content/visual-style-theme-consistency`

**Created**: 2026-08-07

**Status**: Draft

**Input**: User description: "The Jekyll site's docs/_config.yml sets remote_theme: pages-themes/hacker@v0.2.0, but CLAUDE.md documents the theme as pages-themes/midnight@v0.2.0. Resolve the mismatch, then improve readability of code blocks, Mermaid diagrams, deeply nested lists, and tables under the chosen theme, in both light and dark viewing. See spec/visual-style-theme-consistency/spec.md for full context."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Trustworthy, consistent visual identity (Priority: P1)

A visitor lands on the published GitHub Pages site and sees a coherent, intentional visual style that matches what the project documents about itself — no confusion about which theme is actually in effect.

**Why this priority**: The current config/doc mismatch means nobody — including future maintainers — knows which theme is authoritative. This blocks confident visual changes downstream (navigation work, new content) and is a trust/documentation-accuracy issue on its own.

**Independent Test**: Open `docs/_config.yml` and `CLAUDE.md` side by side; both name the same theme. Load the published site and confirm it renders that theme.

**Acceptance Scenarios**:

1. **Given** `docs/_config.yml` and `CLAUDE.md`, **When** compared, **Then** both reference the same `remote_theme` value.
2. **Given** the published site, **When** loaded, **Then** the rendered theme matches the documented one.

---

### User Story 2 - Readable long-form technical content (Priority: P1)

A reader studying a deeply-nested page (e.g., 4 levels of bullets, a wide comparison table, a multi-branch Mermaid class diagram, a code block) can read it without eye strain, horizontal scrolling surprises, or diagrams that clash with the background.

**Why this priority**: This repo's content shape (dense nested lists, tables, Mermaid diagrams, code blocks) is exactly what stock Jekyll themes handle worst. Poor readability undermines the "training path" purpose regardless of which theme is chosen.

**Independent Test**: Render a representative page with all four content shapes (e.g., `docs/pages/design-patterns/creational/singleton.md`) and visually confirm each is legible with adequate contrast and spacing.

**Acceptance Scenarios**:

1. **Given** a page with a Mermaid diagram, **When** viewed against the site theme's background, **Then** diagram text and lines remain legible (no white-on-white or black-on-black elements).
2. **Given** a page with 3+ levels of nested bullets, **When** viewed, **Then** indentation is visually distinct at each level.
3. **Given** a page with a wide table, **When** viewed on a narrow viewport, **Then** the table scrolls within its own container rather than breaking page layout.

---

### User Story 3 - Consistent site metadata (Priority: P3)

A visitor sees a correct page title and a favicon in the browser tab, and a consistent look whether they arrive via light or dark OS/browser preference.

**Why this priority**: Lower-impact polish; doesn't block reading content, but is part of a professional/finished-feeling site.

**Independent Test**: Open the site in a browser with the tab bar visible; confirm favicon and title are present and sensible.

**Acceptance Scenarios**:

1. **Given** the published site, **When** loaded, **Then** a favicon is present in the browser tab.

---

### Edge Cases

- What happens when a Mermaid diagram is embedded but the CDN script (`docs/_includes/head-custom.html`) fails to load? Content should still degrade to a readable code block rather than a blank area.
- How does the chosen theme handle very wide code blocks (e.g., a 100+ character line)? Must scroll within the block, not overflow the page.
- Does the resolved theme choice affect existing embedded raster images (`docs/img/*.png`) that may assume a light background?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The site MUST use exactly one documented theme, with `docs/_config.yml`'s `remote_theme` and `CLAUDE.md`'s stated theme in agreement.
- **FR-002**: Code blocks MUST render with sufficient contrast and MUST scroll horizontally within their own container rather than overflowing the page on narrow viewports.
- **FR-003**: Mermaid diagrams MUST remain legible (adequate contrast between diagram strokes/text and background) under the resolved theme.
- **FR-004**: Nested bullet lists (3+ levels, as used throughout `docs/pages/`) MUST be visually distinguishable level-to-level (indentation and/or spacing).
- **FR-005**: Tables MUST scroll within their own container on narrow viewports rather than breaking page layout.
- **FR-006**: The site MUST present a favicon and a correct page title.
- **FR-007**: Any custom CSS added MUST be scoped to not conflict with the remote theme's own stylesheet (no !important wars, no broad element selector overrides that could break unrelated theme chrome).

### Key Entities

- **Theme configuration**: `docs/_config.yml`'s `remote_theme` key — the single source of truth for which GitHub Pages theme renders the site.
- **Custom style overrides**: any CSS added via `docs/_includes/head-custom.html` or a new `docs/assets/css` file, layered on top of the remote theme.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: `docs/_config.yml` and `CLAUDE.md` name the identical theme string — zero discrepancy.
- **SC-002**: A representative sample of 5 pages spanning code blocks, Mermaid diagrams, nested lists, and tables (chosen from existing `docs/pages/` content) render with no horizontal page overflow and legible diagram contrast, verified visually.
- **SC-003**: Favicon and page title are present on every page (inherited from one Jekyll layout/config change, not per-page edits).

## Assumptions

- The "hacker" theme currently configured is treated as provisional, not necessarily final — resolving FR-001 means picking one and updating whichever source (config or `CLAUDE.md`) is stale, using best judgment on which better serves a dense, technical, long-form reading experience. Default choice: keep `pages-themes/hacker@v0.2.0` if it already reads legibly with the custom CSS fixes in this spec, since changing config risks disrupting Mermaid/asset paths already tuned for it; update `CLAUDE.md` to match unless visual inspection shows the theme is clearly unsuitable, in which case switch config to `midnight` as `CLAUDE.md` originally intended and confirm Mermaid contrast under it instead.
- This is a light-touch styling pass (custom CSS additions only), not a full theme replacement or redesign, per the source spec's explicit scope boundary.
- No build/preview tooling beyond what already exists (`jekyll-remote-theme` plugin) is assumed available in this environment; changes are validated by direct inspection of rendered HTML/CSS logic rather than a live Jekyll server, unless one is available.
- Existing raster images in `docs/img/` are out of scope to regenerate for theme contrast — only note if any are actually illegible under the resolved theme.
