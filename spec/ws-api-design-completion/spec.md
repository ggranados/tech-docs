# Web Services & API Design Completion

**Severity:** Medium
**Depends on:** `content-template-compliance-audit`

## Problem

`docs/pages/ws-and-api-design/` has strong coverage of REST and AuthN/AuthZ (OAuth, OIDC, JWT, SAML, SSO), but `get-started.md` lists many sibling topics with no page: SOAP, RPC, gRPC, GraphQL, HTTP and HTTPS, Data Formats, API Security, Error Handling and Validation, Documentation and Discovery, API Versioning, Performance and Scalability, Testing and Mocking, API Lifecycle Management, Webhooks and Event-Driven Architectures, CORS, API Governance and Maintenance. Also Kerberos and LDAP under AuthN/AuthZ have no page.

## Goal

Round out API design/protocol coverage to match the quality bar already set by the REST pages, prioritizing protocol/paradigm topics (gRPC, GraphQL, SOAP as a legacy-context page) and the cross-cutting API design concerns an architect actually makes decisions about (Versioning, Security, Error Handling, CORS) over process-heavy items (Testing and Mocking, Lifecycle Management, Governance) which can be lighter or deferred.

## Scope hints

- Priority group: gRPC, GraphQL, API Versioning, API Security, Error Handling and Validation, CORS.
- Secondary group: SOAP, RPC, HTTP and HTTPS fundamentals, Data Formats, Webhooks and Event-Driven Architectures.
- Lower priority: Documentation and Discovery, Performance and Scalability, Testing and Mocking, API Lifecycle Management, API Governance and Maintenance — brief pages or defer if scope is tight.
- Kerberos, LDAP: brief pages under `docs/pages/ws-and-api-design/authn-and-authz/` alongside existing SAML/SSO pages.
- Cross-link gRPC/GraphQL ↔ REST for a "choosing an API style" comparison, ideally with a decision-oriented Mermaid diagram or table.

## Out of scope

- Re-documenting REST or the AuthN/AuthZ topics that already exist and are strong.
