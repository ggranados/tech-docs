# Quickstart: Verifying Content & Template Compliance Audit

1. For every `*.md` under `docs/pages/` excluding `docs/pages/common/`, confirm `## Q&A`, `## Related Topics`, and `## Ref.` headings are present.
2. Extract every `](relative/path.md)`-style link from `docs/pages/**/*.md` and confirm the target file exists on disk relative to the linking file.
3. Confirm `docs/pages/common/template.md` no longer exists (or, if kept, contains an explicit "superseded by .claude/templates/page.md" notice).
4. Spot-check 3-4 fixed pages for Q&A quality — questions should be specific to that page's actual content, not generic.
