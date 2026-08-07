# Feature Specification: Navigation & Usability Overhaul

**Feature Branch**: `content/navigation-usability-overhaul`

**Created**: 2026-08-07

**Status**: Draft

**Input**: User description: see spec/navigation-usability-overhaul/spec.md — get-started.md has dead TODO links, index.md/get-started.md purpose overlap, no site search, formatting inconsistencies.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - No dead links in the navigation hub (Priority: P1)

A visitor browsing `get-started.md` never clicks a link that goes nowhere.

**Why this priority**: Broken links on the single navigation hub actively damage trust in the whole site on first contact — highest-severity usability defect.

**Independent Test**: Scan `get-started.md` for `<!-- TODO -->` markers and empty `()` link targets; confirm zero remain.

**Acceptance Scenarios**:

1. **Given** `get-started.md`, **When** scanned for empty link targets (`]()`), **Then** none are found.
2. **Given** a topic with no content page yet, **When** listed in `get-started.md`, **Then** it appears as plain text (not a link) rather than a link to nowhere.

---

### User Story 2 - Clear distinction between the landing page and the navigation hub (Priority: P2)

A first-time visitor understands `index.md` (what is this repo, why should I care) and `get-started.md` (the full content map) serve different purposes, instead of reading near-duplicate framing twice.

**Why this priority**: Reduces redundant reading friction for new visitors; moderate impact since both pages are still functional today, just repetitive.

**Independent Test**: Read both pages back to back; confirm each has a distinct, non-redundant purpose statement.

**Acceptance Scenarios**:

1. **Given** `index.md`, **When** read, **Then** it functions as a short welcome/pitch with a clear call-to-action into `get-started.md`.
2. **Given** `get-started.md`, **When** read, **Then** it functions as the complete topic map without repeating the welcome pitch.

---

### User Story 3 - Find a topic without scrolling a 380-line page (Priority: P2)

A visitor who knows roughly what they're looking for (e.g., "kafka") can filter the navigation hub to relevant entries instead of visually scanning the whole page.

**Why this priority**: With 70+ pages and a single flat TOC, discovery is currently scroll-and-scan only. A lightweight in-page filter meaningfully improves this without requiring external services.

**Independent Test**: Type a keyword into the search/filter box on `get-started.md`; confirm only matching list items (and their ancestor headings) remain visible.

**Acceptance Scenarios**:

1. **Given** the navigation hub with a filter box, **When** a visitor types a keyword matching a topic name, **Then** non-matching list items are hidden and matching ones remain visible.
2. **Given** the filter box, **When** cleared, **Then** the full list reappears.

---

### Edge Cases

- What happens when a topic name that's mid-way through being TODO'd gets real content from another in-flight spec? The plain-text entry should be straightforward to re-link later (no structural rework needed).
- Filter behavior when a search term matches a section heading but no child items — heading should still show so context isn't lost, or matching should cascade sensibly.
- Filter must degrade gracefully (page remains fully usable) if JavaScript fails to load — this is a progressive enhancement, not a hard requirement for basic navigation.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: `get-started.md` MUST NOT contain any link with an empty target (`]()`).
- **FR-002**: Topics without an existing content page MUST render as plain text, not as a link, until content exists.
- **FR-003**: `index.md` and `get-started.md` MUST have distinguishable, non-redundant purposes (short pitch+CTA vs. full map).
- **FR-004**: `get-started.md` MUST provide a client-side, no-external-service search/filter over its own listed topics.
- **FR-005**: The filter MUST require no build step beyond what Jekyll/GitHub Pages already provides (plain HTML/CSS/JS, no server, no external API).
- **FR-006**: Formatting inconsistencies in `get-started.md` (stray trailing whitespace, inconsistent bullet indentation before `<sub>[Back to top]</sub>` markers) MUST be cleaned up.

### Key Entities

- **Navigation hub** (`docs/get-started.md`): the single page listing all topics and linking to content pages.
- **Landing page** (`docs/index.md`): the site root, first page a visitor sees.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Zero empty-target links (`]()`) remain in `get-started.md`.
- **SC-002**: A visitor can go from "arrived at the site" to "found a specific named topic" using only the filter box, without manual scrolling through unrelated sections.
- **SC-003**: `index.md` contains no verbatim-duplicated paragraph from `get-started.md` (and vice versa).

## Assumptions

- The dead-link topics (Distributed Transaction, Saga, Message-Driven, Event-Driven, Event Sourcing) will get real pages later via the `architectural-patterns-completion` spec; this spec only removes the broken link today, it does not write that content (avoids duplicate work across branches). When that spec lands, it re-adds the links.
- "Site search" is delivered as an in-page filter over the existing navigation hub content, not a full-text search across all 70+ rendered pages — this matches the no-external-service, light-touch constraint and the hub already lists every topic name.
- No changes to per-page breadcrumbs/footers in this spec — that overlaps with `content-template-compliance-audit`, which owns bringing every existing page to the current template (including its footer nav convention). This spec only touches `index.md` and `get-started.md`.
