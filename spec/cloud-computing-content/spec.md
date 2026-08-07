# Cloud Computing Platforms Content

**Severity:** Medium-High
**Depends on:** `content-template-compliance-audit`

## Problem

"Cloud Computing Platforms" is category #5 in `CLAUDE.md`'s content organization. `get-started.md` lists AWS, Azure, GCP, IBM Cloud, Alibaba Cloud, OCI, Salesforce Cloud, DigitalOcean, VMware Cloud, Rackspace — zero linked pages exist.

## Goal

Baseline breadth coverage focused on cloud computing *concepts* an architect needs regardless of vendor (service models IaaS/PaaS/SaaS, deployment models, the major providers' core service categories at a comparison level), rather than deep per-vendor documentation.

## Scope hints

- Lead with a "Cloud Computing Concepts" page (IaaS/PaaS/SaaS, public/private/hybrid/multi-cloud, shared responsibility model) — this is more architecturally useful than 10 vendor pages.
- Follow with a single comparison-style page or short pages for the top 3 providers (AWS, Azure, GCP) covering equivalent core services (compute, storage, networking, managed DB) at overview level, linking to official docs for depth.
- Treat IBM Cloud, Alibaba Cloud, OCI, Salesforce Cloud, DigitalOcean, VMware Cloud, Rackspace as brief mentions/links rather than full pages, consistent with breadth-over-depth and this repo's primary (non-vendor-specific) audience.
- New directory: `docs/pages/cloud-computing/`.

## Out of scope

- Hands-on tutorials or account setup guides.
