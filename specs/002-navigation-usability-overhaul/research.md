# Phase 0 Research: Navigation & Usability Overhaul

## Decision: Dead-link topics become plain text, not removed entirely

**Rationale**: Removing the topic names outright would silently shrink the site's declared scope (they're real, planned topics per `CLAUDE.md`'s category list). Rendering as plain text preserves the information ("this topic is in scope") without the broken-link defect, and is a trivial one-line diff to re-link once `architectural-patterns-completion` ships those pages.

**Alternatives considered**: Link to a "coming soon" stub page — rejected, adds a page that immediately becomes stale/needs deletion later, more churn than plain text.

## Decision: In-page vanilla-JS filter, not a hosted search service

**Rationale**: Matches FR-005 (no external service) and this repo's existing pattern (Mermaid is the only external script dependency, loaded from a CDN with no account/API key). A `<script>` filtering `<li>` visibility by substring match against `textContent` is ~20 lines, has zero dependencies, and works entirely client-side on GitHub Pages static hosting.

**Alternatives considered**:
- Lunr.js/Algolia-style full-text search index — rejected as disproportionate to a single navigation page; would require a build step to generate a search index, which this static-hosted repo doesn't currently have.
- Browser's native Ctrl+F — already available or doesn't require work, but doesn't solve the "scan a 380-line page for related items across sections" problem the way a filter that hides non-matches does.

## Open questions resolved

No [NEEDS CLARIFICATION] markers remain; ambiguity resolved directly in spec.md's Assumptions section.
