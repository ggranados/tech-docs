# Phase 0 Research: Content & Template Compliance Audit

## Decision: Delete `docs/pages/common/template.md`, keep `.claude/templates/page.md` as sole canonical template

**Rationale**: `.claude/templates/page.md` is richer (includes Key Concepts, Examples, diagram guidance, footer nav convention) and is already the one referenced by the `document-topic`/`doc-writer` workflow that generates new pages. `docs/pages/common/template.md` predates it and duplicates its purpose with less detail. Keeping two templates guarantees drift; `CLAUDE.md` itself already calls the `.claude/` one the one to use for new pages ("richer than common/template.md").

**Alternatives considered**: Keep both, with `common/template.md` marked "legacy — see .claude/templates/page.md instead." Rejected — a template a contributor is explicitly told not to use is dead weight; deleting it removes the ambiguity FR-006 requires resolving, and `CLAUDE.md`'s own Important Files list can simply drop the reference.

## Decision: Q&A/Related Topics/Ref additions are hand-written per page, not templated boilerplate

**Rationale**: FR-002 requires genuine, topic-specific content. Generic filler (e.g., "Q: What is X? A: X is a pattern/concept described above.") would technically satisfy a section-presence check but fail the actual goal (a useful training resource).

## Open questions resolved

No [NEEDS CLARIFICATION] markers remain.
