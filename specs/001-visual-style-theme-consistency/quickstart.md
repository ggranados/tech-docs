# Quickstart: Verifying Visual Style & Theme Consistency

## Prerequisites

- A checkout of this branch (`content/visual-style-theme-consistency`).
- No local Jekyll build is required for verification (GitHub Pages renders `remote_theme` remotely); verification here is by direct inspection of config/source and, where possible, a live preview.

## Verify the theme mismatch is resolved

1. Open `docs/_config.yml` — confirm `remote_theme:` reads `pages-themes/midnight@v0.2.0`.
2. Open `CLAUDE.md`'s Technology Stack section — confirm it names the same theme.
3. **Expected outcome**: identical theme string in both places (SC-001).

## Verify custom CSS is wired up

1. Open `docs/_includes/head-custom.html` — confirm it links `docs/assets/css/custom.css` in addition to the existing Mermaid script tag.
2. Open `docs/assets/css/custom.css` — confirm rules exist for: code block horizontal scroll, table horizontal scroll container, nested list indentation, and Mermaid init theme variables (or a documented note that Mermaid theming is set via the JS init call instead).

## Verify readability on representative content

Pick 5 pages spanning the four content shapes named in the spec, e.g.:

- `docs/pages/design-patterns/creational/singleton.md` (Mermaid class diagram + code blocks)
- `docs/pages/data-processing/db-concepts/cap.md` (table)
- `docs/pages/architectural-patterns/microservices.md` (nested lists)
- `docs/pages/programming/languages/java/java-8/concurrency/threads.md` (deep nesting, 4+ levels)
- `docs/get-started.md` (very long nested list, worst-case nesting depth in the site)

For each, if a live preview is available (e.g., via GitHub Pages preview or a local Jekyll server): load the page and confirm no horizontal page overflow, and that Mermaid diagram text/lines are legible against the background (SC-002). If no live preview is available in this environment, confirm by reading the generated CSS rules that horizontal-scroll containers and contrast rules are present and correctly scoped to the elements in question.

## Verify favicon and title

1. Confirm `docs/_config.yml` (or Jekyll `_layouts` if the theme requires it) sets a favicon and site title.
2. **Expected outcome**: present site-wide via one config/layout change (SC-003), not per-page edits.
