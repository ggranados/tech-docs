# Feature Specification: Web Development Frameworks Overview

**Feature Branch**: `content/frameworks-overview`
**Created**: 2026-08-07
**Status**: Draft
**Input**: See spec/frameworks-overview/spec.md — Spring's page is a TODO-only stub; Quarkus/Express.js/Nest.js have no pages; Frontend subsection is entirely uncovered.

## Requirements *(mandatory)*

- **FR-001**: `docs/pages/frameworks/backend/spring.md` MUST be filled in with real content covering Spring Core (IoC/DI), and Spring Cloud, Spring Data, Spring Security, and SpringBoot as named subsections.
- **FR-002**: Pages MUST exist for Quarkus, Express.js, Nest.js (backend).
- **FR-003**: Pages MUST exist for React, Angular, Vue.js (frontend, new directory).
- **FR-004**: A page MUST cover Sass, LESS, and Bootstrap together as frontend styling tooling.
- **FR-005**: `get-started.md`'s Web Development Frameworks section MUST link every listed topic.

## Success Criteria *(mandatory)*

- **SC-001**: `spring.md` is no longer a TODO-only stub.
- **SC-002**: All Backend and Frontend topics are covered by a real, linked page.

## Assumptions

- 8 files total: spring.md (filled in), quarkus.md, expressjs.md, nestjs.md, react.md, angular.md, vuejs.md, frontend-styling-tooling.md (Sass/LESS/Bootstrap combined).
- Overview depth, matching the language-overview pages' bar — not exhaustive framework documentation.
