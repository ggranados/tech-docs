# Quickstart: Verifying Design Patterns Completion

1. Confirm all 21 files listed in `plan.md`'s Project Structure exist under `docs/pages/design-patterns/`.
2. For each, confirm `## Overview`, a `## ... Structure` or similar section with a ```mermaid``` block, `## Q&A`, `## Related Topics`, and `## Ref.` are present.
3. Confirm `docs/get-started.md`'s Design Patterns section links every one of the 21 patterns to its new page.
4. Spot-check Adapter, Decorator, Facade for cross-links to each other in Related Topics (commonly-confused Structural patterns).
5. Confirm no broken relative links were introduced (re-run the link-resolution check used in `content-template-compliance-audit`).
