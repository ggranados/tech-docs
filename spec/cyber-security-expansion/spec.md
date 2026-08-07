# Cyber-Security Fundamentals Expansion

**Severity:** Medium
**Depends on:** `content-template-compliance-audit`

## Problem

`get-started.md`'s Cyber-security Fundamentals section lists 15 topics; only "Access Control and Authentication" has a sub-item with a page (IAM). Network Security, Information Security, Cryptography, Secure Coding Practices, Risk Management, Security Governance and Compliance, Incident Response, Security Awareness and Training, Threat Intelligence, Vulnerability Assessment and Penetration Testing, Web Application Security, Security Operations and Monitoring, Security Architecture and Design, Data Privacy and Protection all have zero pages. Note: `docs/pages/ws-and-api-design/authn-and-authz/` already has strong coverage (OAuth, OIDC, JWT, SAML, SSO) that's architecturally adjacent — cross-link rather than duplicate.

## Goal

Baseline breadth coverage of the security fundamentals most relevant to a software architect: Cryptography basics, Secure Coding Practices, Security Architecture and Design, Web Application Security (OWASP Top 10 level), Data Privacy and Protection — prioritized over more operational/GRC-heavy topics (Incident Response, Threat Intelligence, Security Awareness Training) which are lower priority for an architect audience.

## Scope hints

- Priority group: Cryptography, Secure Coding Practices, Web Application Security, Security Architecture and Design, Data Privacy and Protection.
- Secondary group: Network Security, Information Security, Risk Management, Security Governance and Compliance, Vulnerability Assessment and Penetration Testing.
- Lower priority (brief mention or skip if scope is tight): Incident Response, Security Awareness and Training, Threat Intelligence, Security Operations and Monitoring.
- Cross-link Web Application Security ↔ existing OWASP-adjacent content if any; Cryptography ↔ JWT/OAuth/TLS-related existing pages.
- New directory: `docs/pages/cyber-security/` (already exists with `access-control-and-authn/` — extend alongside it).

## Out of scope

- Duplicating the already-strong AuthN/AuthZ coverage under `ws-and-api-design/` — link to it instead.
