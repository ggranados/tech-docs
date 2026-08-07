# Web Development Frameworks Overview

**Severity:** Medium
**Depends on:** `content-template-compliance-audit`, pairs with `programming-languages-overview`

## Problem

`get-started.md`'s Web Development Frameworks section lists Backend (Spring + its sub-modules, Quarkus, Express.js, Nest.js) and Frontend (React, Angular, Vue.js, Sass, LESS, Bootstrap) — only `docs/pages/frameworks/backend/spring.md` exists. Spring Cloud, Spring Data, Spring Security, SpringBoot are named sub-bullets with no pages. Frontend has zero coverage — an entire named subsection with nothing in it.

## Goal

Baseline breadth coverage: flesh out Spring's named sub-modules briefly (even as sections within `spring.md` rather than separate pages, if that reads better), add Quarkus/Express.js/Nest.js overview pages, and establish the Frontend subsection from scratch (React, Angular, Vue.js at minimum; Sass/LESS/Bootstrap can be brief mentions within a "Frontend Tooling" page rather than full pages each).

## Scope hints

- Spring sub-modules (Cloud, Data, Security, Boot): prefer expanding `spring.md` with subsections + Mermaid diagrams over creating 4 more thin standalone pages, unless `speckit-clarify` determines otherwise.
- Quarkus, Express.js, Nest.js: one overview page each under `docs/pages/frameworks/backend/`.
- React, Angular, Vue.js: one overview page each under new `docs/pages/frameworks/frontend/` — cover component model, rendering approach, typical use case, ecosystem.
- Sass, LESS, Bootstrap: brief joint "Frontend Styling Tooling" page rather than 3 separate pages.

## Out of scope

- Deep framework tutorials or code-heavy walkthroughs — overview level only, per repo philosophy.
