# CLAUDE.md - Project Context

## Project Overview

**Tech Docs** is a comprehensive IT knowledge repository and training path for software architects, built as a collection of markdown documents rendered as a GitHub Pages website.

**Live Site:** https://ggranados.github.io/data-driven-docs/

## Project Purpose

This repository serves as:
- A structured training path for software architect candidates (primary audience: GG)
- A centralized study guide covering broad IT topics
- A quick reference for technical concepts and tools
- A starting point with links to official documentation
- An open-source educational resource for developers

**Content Philosophy:** Breadth over depth — clear overviews with links to official documentation for deeper learning.

---

## Technology Stack

- **Static Site Generator:** Jekyll (GitHub Pages)
- **Theme:** Midnight theme (`pages-themes/midnight@v0.2.0`)
- **Content Format:** Markdown (GitHub Flavored)
- **Utility Code:** Java (for markdown file validation and reference checking)
- **Hosting:** GitHub Pages

---

## Project Structure

```
tech-docs/
├── docs/                           # Main documentation directory
│   ├── _config.yml                 # Jekyll configuration
│   ├── index.md                    # Homepage/landing page
│   ├── get-started.md             # Main navigation hub with full TOC
│   ├── img/                       # Images and diagrams
│   └── pages/                     # Content organized by topic
│       ├── architectural-patterns/
│       ├── common/                # Templates and shared resources
│       ├── cyber-security/
│       ├── data-processing/
│       ├── design-patterns/
│       ├── frameworks/
│       ├── programming/
│       │   ├── languages/
│       │   └── paradigms/
│       └── ws-and-api-design/
├── .claude/                       # Claude Code configuration
│   ├── agents/                    # Sub-agent definitions
│   │   ├── topic-researcher.md    # Researches topics via web + codebase
│   │   ├── diagram-specialist.md  # Generates Mermaid diagrams
│   │   └── doc-writer.md         # Writes final markdown pages
│   ├── commands/                  # Custom slash commands
│   │   ├── explore-topic.md       # /explore-topic {topic-name}
│   │   └── document-topic.md      # /document-topic
│   ├── explorations/              # In-progress and accepted exploration plans
│   │   └── {topic-name}/
│   │       └── exploration.md
│   └── templates/
│       └── page.md               # Standard page template
├── code/                          # Java utilities
│   └── src/edu/datadrivendocs/code/utils/
│       └── MarkdownFileReferences.java
└── README.md
```

---

## Content Organization

### Topic Categories

1. **Programming** — Languages (Java, JS, Python…), Paradigms (OOP, FP, Reactive…)
2. **Web Development Frameworks** — Backend (Spring, Express…), Frontend (React, Angular…)
3. **Database Management Systems** — ACID/BASE/CAP, SQL, NoSQL
4. **Networking Concepts** — TCP/IP, DNS, routing, switching
5. **Cloud Computing Platforms** — AWS, Azure, GCP
6. **DevOps Practices** — CI/CD, IaC, containerization
7. **Data Structures & Algorithms** — Arrays, trees, graphs, sorting, searching
8. **Cyber-security Fundamentals** — Access control, IAM, cryptography
9. **Design Patterns** — Creational, Structural, Behavioral, SOLID
10. **Architectural Patterns** — MVC, MVVM, Microservices, Reactive Systems
11. **Web Services and API Design** — REST, SOAP, GraphQL, gRPC, OAuth, JWT

### File Naming Conventions

- All content files: lowercase with hyphens (`kebab-case.md`)
- Directory structure mirrors the conceptual hierarchy

---

## Markdown Document Structure

Every documentation page follows this standard structure:

```markdown
# Title

---

## Table of Contents
<!-- TOC -->
* Auto-generated table of contents
<!-- TOC -->

---

## Overview

Intro paragraph...

## Content Sections

Main content with subsections...

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

**Q: Question?**
A: Answer.

---

## Related Topics

- [Topic](path) — relationship

---

## Ref.

- [Source](URL) — description

---

[Get Started](../../get-started.md) | [Section Link]

---
```

### Key Patterns

1. **Table of Contents**: `<!-- TOC -->` markers, auto-generated
2. **Back to Top Links**: `<sub>[Back to top](#table-of-contents)</sub>` after each major section
3. **Q&A Section**: Every page includes a Q&A for the architect trainee
4. **Related Topics**: Cross-links between pages
5. **References Section**: Official documentation links only
6. **Navigation Footer**: Links back to `get-started.md` and the section index

---

## Exploration & Documentation Workflow

This project uses a two-phase workflow for adding new content:

### Phase 1 — Explore (`/explore-topic {topic-name}`)

GG selects a topic and runs the command. Claude acts as a learning tutor and:
1. Uses the `topic-researcher` sub-agent to gather structured information
2. Uses the `diagram-specialist` sub-agent to prepare Mermaid diagrams
3. Creates an exploration plan at `.claude/explorations/{topic-name}/exploration.md`
4. Presents a summary and invites GG to ask questions, go deeper, or iterate

**Rules during exploration:**
- No changes are made to `docs/` — exploration only
- GG can ask technical questions freely; Claude answers without saving to the plan unless GG asks
- GG iterates until satisfied, then explicitly accepts the plan
- GG signals acceptance by saying something like "accepted" or "looks good, proceed"

### Phase 2 — Document (`/document-topic`)

Once GG accepts the plan:
1. Claude creates a `content/{topic-kebab-case}` Git branch
2. Uses the `doc-writer` sub-agent to generate markdown files in `docs/`
3. Updates `docs/get-started.md` navigation
4. Presents a summary and asks GG for approval
5. On approval, commits the changes

**Git rules:**
- Only GG merges branches into `develop` or `master`
- Claude never pushes to remote or opens PRs
- Each topic gets its own branch

### Exploration Plan Statuses

| Status | Meaning |
|--------|---------|
| `DRAFT` | Being created or iterated |
| `ACCEPTED` | GG approved — ready to document |
| `DOCUMENTED` | Files generated and committed |

---

## Sub-Agents

| Agent | File | Purpose |
|-------|------|---------|
| `topic-researcher` | `.claude/agents/topic-researcher.md` | Web research, subtopic identification, Q&A generation |
| `diagram-specialist` | `.claude/agents/diagram-specialist.md` | Mermaid diagram generation |
| `doc-writer` | `.claude/agents/doc-writer.md` | Markdown page generation from accepted plans |

---

## Java Utility Code

**Location:** `code/src/edu/datadrivendocs/code/utils/MarkdownFileReferences.java`

Validates documentation structure by scanning `docs/` for orphaned (unreferenced) markdown files. The `common/` directory is excluded.

---

## Important Files

- `docs/index.md` — Homepage
- `docs/get-started.md` — Main navigation hub
- `docs/_config.yml` — Jekyll configuration
- `docs/pages/common/template.md` — Legacy basic template
- `.claude/templates/page.md` — Full page template with Q&A and Related Topics
- `README.md` — GitHub repository description

---

## Git Workflow

**Main Branch:** `master`
**Integration Branch:** `develop` (merge feature branches here first)
**Feature Branches:** `content/{topic-kebab-case}`

Only GG merges to `develop` or `master`.

---

## Notes for AI Assistants

1. **Consistency is Key:** Always follow the established markdown structure
2. **Template:** Use `.claude/templates/page.md` for new pages (richer than `common/template.md`)
3. **Link Validation:** Check that internal links use correct relative paths
4. **Reference Quality:** Prioritize official documentation links over tutorials
5. **No Deep Content:** Overviews, not exhaustive tutorials
6. **Navigation Updates:** Always update `get-started.md` when adding pages
7. **Image Paths:** `docs/img/` — reference with relative paths from the page
8. **Common Directory:** `docs/pages/common/` is for shared resources/templates — excluded from link validation
9. **Explorations:** Never modify `docs/` during `/explore-topic` — exploration only
10. **Branch per Topic:** Each documented topic gets its own `content/` branch
11. **Diagrams:** Always use Mermaid (` ```mermaid `) — never PlantUML. Supported types: `classDiagram`, `flowchart TD/LR`, `sequenceDiagram`, `stateDiagram-v2`, `graph TD`, `erDiagram`. Mermaid renders natively on GitHub.com and on the Jekyll site via `docs/_includes/head-custom.html`.

---

**Last Updated:** 2026-03-22
**Repository:** https://github.com/ggranados/data-driven-docs
