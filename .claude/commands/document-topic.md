You are acting as a documentation generator for the Tech Docs knowledge base. GG has accepted an exploration plan and wants to produce the actual markdown documentation.

## Step 1 — Identify the Accepted Exploration

Check `.claude/explorations/` for the most recently modified `exploration.md` with status `ACCEPTED`. If multiple exist or none are accepted, list the available explorations and ask GG which one to document.

Read the full exploration plan before proceeding.

## Step 2 — Create a Working Git Branch

Create a new Git branch for this documentation work:

```bash
git checkout -b content/{topic-kebab-case}
```

Where `{topic-kebab-case}` is the kebab-case version of the topic name from the exploration plan.

Report the branch name to GG.

## Step 3 — Generate Documentation

Use the `doc-writer` sub-agent to create all markdown pages. Pass it:
- The full path to the exploration plan: `.claude/explorations/{topic}/exploration.md`
- The template path: `.claude/templates/page.md`
- The current `docs/` structure for context

The doc-writer will create the files and return a summary of what was created.

## Step 4 — Update get-started.md

Read `docs/get-started.md` to understand its structure. Add the new topic entries in the correct section. Follow the existing link format exactly.

## Step 5 — Update the Exploration Status

Edit `.claude/explorations/{topic}/exploration.md` and change the status line from:
```
> Status: ACCEPTED
```
to:
```
> Status: DOCUMENTED
```

## Step 6 — Request GG's Approval

Present a summary of everything created:

```
## Documentation Created

**Branch**: content/{topic-kebab-case}

**New files**:
- docs/pages/{path}/{file}.md
- (list all files)

**Modified files**:
- docs/get-started.md (added {N} entries)

**Preview**: [Live Site](https://ggranados.github.io/data-driven-docs/) will reflect changes after merge.

---
Ready to commit? Reply with:
- ✓ **approve** — to stage and commit all changes
- ✗ **changes needed** — to describe what to fix first
```

## Step 7 — On GG's Approval

When GG approves, stage and commit:

```bash
git add docs/
git add .claude/explorations/
git commit -m "docs: add {topic-name} documentation

- Add {N} pages covering {brief description}
- Update get-started.md navigation

Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>"
```

Then inform GG:
> Committed to branch `content/{topic-kebab-case}`. Only GG can merge this branch into `develop` or `master`.

**Do NOT push to remote or open a PR.** GG handles merges.
