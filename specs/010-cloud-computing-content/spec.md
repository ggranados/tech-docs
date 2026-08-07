# Feature Specification: Cloud Computing Platforms Content

**Feature Branch**: `content/cloud-computing-content`
**Created**: 2026-08-07
**Status**: Draft
**Input**: See spec/cloud-computing-content/spec.md — Cloud Computing Platform category has 10 listed providers and zero pages.

## Requirements *(mandatory)*

- **FR-001**: A page MUST cover cloud computing concepts (IaaS/PaaS/SaaS, deployment models, shared responsibility model) independent of any vendor.
- **FR-002**: A page MUST compare AWS, Azure, and GCP's equivalent core services (compute, storage, networking, managed DB) at overview level.
- **FR-003**: A page MUST briefly cover the remaining listed providers (IBM Cloud, Alibaba Cloud, OCI, Salesforce Cloud, DigitalOcean, VMware Cloud, Rackspace).
- **FR-004**: `get-started.md`'s Cloud Computing Platform section MUST link every listed provider to its covering page.

## Success Criteria *(mandatory)*

- **SC-001**: All 10 listed providers are covered by a real, linked page.

## Assumptions

- 3 pages: Cloud Computing Concepts (foundational, no direct bullet but frames the category); AWS/Azure/GCP comparison; brief coverage of the remaining 7 providers.
- Depth favors concepts over vendor specifics, per this repo's non-vendor-specific primary audience.
