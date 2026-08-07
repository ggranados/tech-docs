# Navigation & Usability Overhaul

**Severity:** Critical
**Depends on:** none (foundation; pairs with `visual-style-theme-consistency`)

## Problem

`docs/get-started.md` is the single navigation hub for the entire site (~380 lines) and has several usability problems:

- Dead links: several entries use empty anchors with a `TODO` comment, e.g. `[Distributed Transaction]()<!-- TODO: -->`, `[Saga]()<!-- TODO: -->`, `[Message-Driven]()<!-- TODO: -->`, `[Event-Driven]()<!-- TODO: -->`, `[Event Sourcing]()<!-- TODO: -->`. These render as broken links to a visitor.
- `docs/index.md` and `docs/get-started.md` duplicate the same "welcome/what is this" framing with no clear distinction of purpose, forcing a new visitor through two similar landing pages before reaching content.
- No section-level index pages — a visitor lands directly on `get-started.md`'s flat, very long TOC with no breadcrumb or "you are here" context once inside a deeply nested page (e.g., `docs/pages/programming/languages/java/java-8/concurrency/threads.md` has no way back except the generic `[Get Started]` footer link).
- No site search. With 70+ pages, browsing a single long TOC is the only discovery mechanism.
- Inconsistent indentation/formatting artifacts in `get-started.md` (stray trailing spaces before `<sub>[Back to top]</sub>`, inconsistent bullet indent levels).

## Goal

Make the navigation hub trustworthy (no dead links) and make it fast to find and return to content from anywhere in the site.

## Scope hints

- Fix or remove every dead/TODO link in `get-started.md` (either link to real content created by other specs, or remove the entry until content exists — do not ship visible broken links).
- Clarify the distinct purpose of `index.md` (site root landing) vs `get-started.md` (full contents/navigation) or merge them if a clear split isn't achievable.
- Add lightweight breadcrumb or "Section Index" links at the top of deeply nested pages, not just the footer.
- Evaluate adding a client-side search (e.g., a simple JS-based search over page titles/headings) appropriate for a static Jekyll/GitHub Pages site — keep it lightweight, no external service dependency required.
- Clean up formatting inconsistencies in `get-started.md`.

## Out of scope

- New content pages themselves (handled by topic-specific specs) — this spec fixes navigation *around* content, and should link to pages other specs create/complete.
