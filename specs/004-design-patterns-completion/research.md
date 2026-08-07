# Phase 0 Research: Design Patterns Completion

## Decision: Group Dependency Injection & Service Locator under a new `ioc/` directory rather than folding into `creational/`

**Rationale**: `get-started.md` already lists these under their own "Inversion of Control (IoC)" sub-heading, distinct from Creational/Structural/Behavioral. Matching that existing taxonomy in the directory structure keeps navigation and content organization consistent.

## Decision: Illustrative code examples default to Java

**Rationale**: Java is this repo's most deeply covered language and the implicit default for existing pattern pages (Singleton, Factory, Observer all use Java). Consistency with existing pages beats introducing a second example language.

## Decision: Content produced via parallel delegated batches by pattern family

**Rationale**: 21 similarly-structured pages is a large, parallelizable, mechanical-shape task (each page follows the identical template skeleton). Splitting by family (Creational remainder, Structural, Behavioral, IoC) lets each batch's related patterns be cross-linked coherently by the same writer while keeping total wall-clock time reasonable.

## Open questions resolved

No [NEEDS CLARIFICATION] markers remain.
