# Feature Specification: Cyber-Security Fundamentals Expansion

**Feature Branch**: `content/cyber-security-expansion`
**Created**: 2026-08-07
**Status**: Draft
**Input**: See spec/cyber-security-expansion/spec.md — 14 of 15 listed topics have no page (only IAM, under Access Control, exists).

## Requirements *(mandatory)*

- **FR-001**: Priority pages MUST exist: Cryptography, Secure Coding Practices, Web Application Security, Security Architecture and Design, Data Privacy and Protection.
- **FR-002**: Secondary pages MUST exist: Network Security & Information Security (grouped); Risk Management & Security Governance and Compliance (grouped); Vulnerability Assessment and Penetration Testing.
- **FR-003**: Lower-priority topics (Incident Response and Management, Security Awareness and Training, Threat Intelligence and Analysis, Security Operations and Monitoring) MUST be covered together in one "Security Operations & Response" page rather than skipped, so no listed topic is left as a dead bullet.
- **FR-004**: Web Application Security MUST cross-link the existing `ws-and-api-design/authn-and-authz/` pages (OAuth, JWT, SAML, SSO) and the existing IAM page rather than duplicating them.
- **FR-005**: `get-started.md`'s Cyber-security Fundamentals section MUST link every listed topic to its covering page.

## Success Criteria *(mandatory)*

- **SC-001**: All 15 listed topics (14 new + existing IAM) are covered by a real, linked page.

## Assumptions

- 9 new pages: Cryptography; Secure Coding Practices; Web Application Security; Security Architecture and Design; Data Privacy and Protection; Network & Information Security; Risk Management & Governance/Compliance; Vulnerability Assessment & Penetration Testing; Security Operations & Response.
