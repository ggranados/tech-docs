# CLAUDE.md - Project Context

## Project Overview

**Tech Docs** is a comprehensive IT knowledge repository and study guide built as a collection of markdown documents. The project is designed to be rendered as a GitHub Pages website, providing an organized, navigable reference for various technology topics.

**Live Site:** https://ggranados.github.io/data-driven-docs/

## Project Purpose

This repository serves as:
- A centralized study guide covering broad IT topics
- A quick reference for technical concepts and tools
- A starting point with links to official documentation
- An open-source educational resource for developers

**Important Note:** The documentation provides breadth over depth - it covers many topics but intentionally does not go into exhaustive detail. The focus is on providing clear overviews and linking to official documentation for deeper learning.

## Technology Stack

- **Static Site Generator:** Jekyll (GitHub Pages)
- **Theme:** Midnight theme (`pages-themes/midnight@v0.2.0`)
- **Content Format:** Markdown (GitHub Flavored)
- **Utility Code:** Java (for markdown file validation and reference checking)
- **Hosting:** GitHub Pages

## Project Structure

```
data-driven-docs/
├── docs/                           # Main documentation directory
│   ├── _config.yml                 # Jekyll configuration
│   ├── index.md                    # Homepage/landing page
│   ├── get-started.md             # Main navigation hub with full TOC
│   ├── img/                       # Images and diagrams
│   └── pages/                     # Content organized by topic
│       ├── architectural-patterns/
│       ├── common/                # Templates and shared resources
│       ├── cyber-security/
│       ├── dbms/
│       ├── design-patterns/
│       ├── programming/
│       │   ├── languages/
│       │   │   └── java/          # Language-specific deep dives
│       │   └── paradigms/
│       └── ws-and-api-design/
├── code/                          # Java utilities
│   └── src/edu/datadrivendocs/code/utils/
│       └── MarkdownFileReferences.java
└── README.md                      # GitHub repository readme
```

## Content Organization

### Topic Categories

The documentation is organized into these main categories:

1. **Programming**
   - Languages (Java, JavaScript, Python, etc.)
   - Paradigms (OOP, Functional, Reactive, etc.)

2. **Web Development Frameworks**
   - Backend (Spring, Express.js, Nest.js, etc.)
   - Frontend (React, Angular, Vue.js, etc.)

3. **Database Management Systems**
   - Concepts (ACID, BASE, CAP Theorem)
   - Relational/SQL databases
   - NoSQL databases

4. **Networking Concepts**
   - TCP/IP, DNS, routing, switching, etc.

5. **Cloud Computing Platforms**
   - AWS, Azure, GCP, etc.

6. **DevOps Practices**
   - CI/CD, IaC, containerization, etc.

7. **Data Structures & Algorithms**
   - Arrays, trees, graphs, sorting, searching, etc.

8. **Cyber-security Fundamentals**
   - Access control, IAM, cryptography, etc.

9. **Design Patterns**
   - Creational, Structural, Behavioral patterns
   - SOLID principles

10. **Architectural Patterns**
    - MVC, MVVM, Microservices, Reactive Systems, etc.

11. **Web Services and API Design**
    - REST, SOAP, GraphQL, gRPC
    - Authentication/Authorization (OAuth, JWT, SAML, etc.)

### File Naming Conventions

- All content files use lowercase with hyphens: `kebab-case.md`
- Files are named descriptively after their topic
- Directory structure mirrors the conceptual hierarchy

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

## Content Sections

Main content with subsections...

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- External reference links

---

[Get Started](../../get-started.md) | [Section Link]

---
```

### Key Patterns

1. **Table of Contents**: Auto-generated TOC at the top of each page
2. **Back to Top Links**: `<sub>[Back to top](#table-of-contents)</sub>` after major sections
3. **References Section**: Links to official documentation and external resources
4. **Navigation Footer**: Links back to get-started.md and relevant section indexes
5. **Images**: Stored in `docs/img/` and referenced with relative paths from the page

## Java Utility Code

### MarkdownFileReferences.java

**Location:** `code/src/edu/datadrivendocs/code/utils/MarkdownFileReferences.java`

**Purpose:** Validates the documentation structure by:
- Scanning all markdown files in the `docs/` directory
- Identifying which files are referenced from other markdown files
- Reporting unreferenced files (orphaned content)
- Helping maintain documentation completeness

**Exclusion:** The `common/` directory is excluded from validation (contains templates)

## Development Guidelines

### Adding New Content

1. **Choose the Appropriate Category:** Place new files in the correct subdirectory under `docs/pages/`
2. **Follow the Template:** Use `docs/pages/common/template.md` as a starting point
3. **Add Table of Contents:** Include auto-generated TOC after the title
4. **Include Navigation:** Add "Back to top" links and footer navigation
5. **Update get-started.md:** Link to the new page from the main navigation hub
6. **Add References:** Include official documentation links in the Ref. section

### Editing Existing Content

1. **Maintain Structure:** Keep the standard document structure intact
2. **Update TOC:** Regenerate if section headings change
3. **Check Links:** Ensure all internal links remain valid
4. **Test Locally:** Preview changes with Jekyll locally if possible

### Content Philosophy

- **Breadth over Depth:** Provide overviews rather than comprehensive tutorials
- **Link to Authority:** Always reference official documentation for detailed information
- **Stay Current:** Focus on concepts that remain relevant across versions
- **Keep it Accessible:** Write for beginners and experienced developers alike

## Important Files

- **docs/index.md** - Homepage introducing the project
- **docs/get-started.md** - Main navigation hub with complete topic index
- **docs/_config.yml** - Jekyll configuration (theme and plugins)
- **docs/pages/common/template.md** - Template for new documentation pages
- **README.md** - GitHub repository description

## Git Workflow

**Main Branch:** `master`

**Current Status:** Repository is clean (no uncommitted changes)

**Recent Activity:**
- RESTful API design content added
- SQL and NoSQL database documentation
- Design patterns section

## Notes for AI Assistants

When working with this project:

1. **Consistency is Key:** Always follow the established markdown structure
2. **Link Validation:** Check that internal links use correct relative paths
3. **Reference Quality:** Prioritize official documentation links over tutorials
4. **No Deep Content:** Don't write exhaustive explanations - keep content concise
5. **Java Utility:** Run `MarkdownFileReferences.java` to find orphaned documentation
6. **Navigation Updates:** When adding pages, update `get-started.md` with proper links
7. **Image Paths:** Images go in `docs/img/` and use relative paths from the page
8. **Common Directory:** The `docs/pages/common/` directory is for shared resources/templates

## Contributing

The project welcomes contributions via pull requests for:
- New topic pages following the template
- Corrections and improvements to existing content
- Additional reference links to official documentation
- Structure and navigation enhancements

---

**Last Updated:** 2025-11-13
**Repository:** https://github.com/ggranados/data-driven-docs
