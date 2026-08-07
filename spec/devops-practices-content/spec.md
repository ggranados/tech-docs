# DevOps Practices Content

**Severity:** Medium-High
**Depends on:** `content-template-compliance-audit`

## Problem

"DevOps Practices" is category #6 in `CLAUDE.md`'s content organization. `get-started.md` lists CI, CD, IaC, Configuration Management, Continuous Deployment, Continuous Monitoring, Agile Development, Automated Testing, Collaboration and Communication, Version Control, DevOps Culture — zero linked pages exist for any of them.

## Goal

Baseline breadth coverage of the DevOps practices most relevant to a software architect's decision-making: CI/CD pipeline design, Infrastructure as Code, Configuration Management, and how these fit into an architecture (deployment topology, environment promotion), plus brief coverage of the process/culture items (Agile, DevOps Culture) since this repo already leans architecture/process-aware.

## Scope hints

- Group into a small number of pages rather than one per bullet, e.g.: "CI/CD" (covers CI, CD, Continuous Deployment together), "Infrastructure as Code & Configuration Management", "DevOps Culture & Practices" (Agile, Collaboration, Version Control, Monitoring, Automated Testing as subsections) — resolve exact grouping during `speckit-clarify`.
- A Mermaid `flowchart` illustrating a CI/CD pipeline (commit → build → test → deploy stages) is high-value here.
- New directory: `docs/pages/devops/`.

## Out of scope

- Specific tool tutorials (Jenkins, GitHub Actions, Terraform syntax, etc.) — link to official docs per this repo's "breadth over depth, link to official docs" philosophy rather than documenting tool internals.
