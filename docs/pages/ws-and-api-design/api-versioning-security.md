# API Versioning and Security

---

## Table of Contents
<!-- TOC -->
* [API Versioning and Security](#api-versioning-and-security)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [API Versioning](#api-versioning)
  * [API Security](#api-security)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

APIs are contracts, and contracts change over time. Two disciplines keep that change manageable: **versioning**, which lets an API evolve without breaking existing consumers, and **security**, which ensures only the right callers can invoke it and that they cannot abuse it. Both are cross-cutting concerns that apply regardless of whether the API is REST, gRPC, or GraphQL.

---

## Overview

A well-designed API assumes it will change: fields get added, resources get restructured, business rules evolve. Without a deliberate versioning strategy, every change risks breaking clients that were never warned. Security has a similar cross-cutting nature — it isn't a feature bolted onto an API, but a set of boundaries (identity, permissions, rate limits, input trust) enforced consistently across every endpoint.

Together, these two concerns determine how safely and predictably an API can evolve and be consumed in production, especially once external or third-party clients depend on it.

<sub>[Back to top](#table-of-contents)</sub>

---

## API Versioning

Versioning lets an API's contract change over time without breaking clients that depend on the previous shape.

- ### URI Versioning
  The version is embedded directly in the path, e.g. `/api/v1/orders` vs. `/api/v2/orders`. It is the most visible and easiest strategy to reason about and to route (e.g., at a gateway or load balancer), but it means the resource technically has a different identity per version.

  ```http request
  GET /api/v2/orders/123 HTTP/1.1
  Host: www.example.com
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Header Versioning
  The version is passed in a custom header, e.g. `Api-Version: 2`, leaving the URI stable. This keeps resource URIs canonical but makes the version less discoverable (it won't show up in logs, bookmarks, or browser address bars) and requires clients to set the header correctly.

  ```http request
  GET /api/orders/123 HTTP/1.1
  Api-Version: 2
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Content Negotiation (Media Type) Versioning
  The version is encoded in the `Accept` header's media type, e.g. `application/vnd.example.v2+json`. This is the most "RESTful-purist" approach — it treats different versions as different representations of the same resource — but it is the least common in practice due to tooling friction.

  ```http request
  GET /api/orders/123 HTTP/1.1
  Accept: application/vnd.example.v2+json
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Breaking vs. Non-Breaking Changes
  Not every change requires a new version. Adding an optional field, a new endpoint, or a new enum value is typically **non-breaking** — well-behaved clients ignore fields they don't recognize. Removing or renaming a field, changing a field's type or meaning, or tightening validation are **breaking changes** that require a new version (or, at minimum, a carefully coordinated migration).

<sub>[Back to top](#table-of-contents)</sub>

- ### Deprecation Policy
  A version should never simply disappear. A deprecation policy communicates a timeline (e.g., "v1 is deprecated as of date X, removed after 12 months"), typically surfaced via response headers such as `Deprecation` and `Sunset`, documentation notices, and direct communication with known consumers. This gives clients time to migrate before a breaking removal.

<sub>[Back to top](#table-of-contents)</sub>

---

## API Security

Security at the API layer protects who can call an endpoint, what they're allowed to do, and how much damage a malicious or malformed request can cause.

- ### Authentication and Authorization
  **Authentication (AuthN)** establishes who the caller is; **authorization (AuthZ)** determines what that caller is allowed to do. These are foundational, cross-cutting concerns for every API and are covered in depth elsewhere rather than duplicated here.

  > See also: [Authentication (AuthN) and Authorization (AuthZ)](authn-and-authz/authn-authz.md) — the core AuthN/AuthZ concepts and models.
  > See also: [OAuth 2.0](authn-and-authz/oauth.md) — the dominant delegated-authorization protocol for APIs.

<sub>[Back to top](#table-of-contents)</sub>

- ### API Keys vs. OAuth Tokens
  An **API key** is a static, long-lived secret identifying a calling application (not a user), commonly used for simple service identification, usage metering, or low-risk internal integrations. An **OAuth access token** identifies a scoped, typically short-lived grant of permission, often tied to a specific user and a specific set of scopes, and can be refreshed or revoked independently of the client's identity. Public-facing or user-context APIs generally favor OAuth tokens; simple machine-to-machine or partner integrations often still rely on API keys, sometimes combined with mTLS or IP allow-listing for extra assurance.

<sub>[Back to top](#table-of-contents)</sub>

- ### Rate Limiting
  Rate limiting caps how many requests a client (per API key, token, or IP) can make in a given time window, protecting the API from abuse, accidental overload, and denial-of-service scenarios. Limits are commonly communicated via `X-RateLimit-*` headers and enforced with a `429 Too Many Requests` response.

  ```mermaid
  flowchart LR
      A[Request] --> B{Under rate limit?}
      B -->|Yes| C[Process request]
      B -->|No| D[429 Too Many Requests]
  ```

  **Caption:** A rate limiter sits in front of the API logic, rejecting requests once a client exceeds its allotted quota.

<sub>[Back to top](#table-of-contents)</sub>

- ### Input Validation as a Security Boundary
  Every field accepted by the API is a potential attack surface — injection attacks, oversized payloads, malformed types, and unexpected structures. Validating input strictly at the API boundary (types, ranges, allowed values, payload size) before it reaches business logic is one of the cheapest and most effective security controls available.

  > See also: [Web Application Security](../cyber-security/web-application-security.md) — broader web application security practices, including input validation, injection prevention, and secure defaults.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Which versioning strategy should a new public API default to?**
A: URI versioning (`/v1/...`) is the most common default — it's the easiest for consumers to discover, route, and debug, even though it's arguably less "pure REST" than content negotiation.

---

**Q: Is rate limiting a security concern or a reliability concern?**
A: Both. It protects reliability by preventing accidental overload from a runaway client, and it protects security by limiting the blast radius of abuse, credential-stuffing attempts, and denial-of-service attacks.

---

**Q: If an API already validates input for correctness, why call it a "security boundary" rather than just data validation?**
A: Because unvalidated input is a common vector for injection and resource-exhaustion attacks, not just bad data. Treating validation as a security control (not only a correctness check) means enforcing it strictly and rejecting by default, rather than trying to sanitize and continue.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [RESTful Architecture](restful.md) — the baseline API style these versioning and security concerns most commonly apply to.
- [gRPC and GraphQL](grpc-graphql.md) — versioning and security apply across API styles, not just REST.
- [Error Handling and CORS](error-handling-cors.md) — error responses must avoid leaking sensitive details, tying error handling to API security.
- [Authentication (AuthN) and Authorization (AuthZ)](authn-and-authz/authn-authz.md) — full detail on AuthN/AuthZ models referenced above.
- [OAuth 2.0](authn-and-authz/oauth.md) — full detail on the OAuth token model referenced above.
- [Web Application Security](../cyber-security/web-application-security.md) — broader security practices this page's input-validation section builds on.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [RFC 8594 — The Sunset HTTP Header Field](https://www.rfc-editor.org/rfc/rfc8594) — standard header for communicating API deprecation timelines.
- [OWASP API Security Top 10](https://owasp.org/API-Security/editions/2023/en/0x00-header/) — authoritative reference on API-specific security risks.

---

[Get Started](../../get-started.md) | [Web Services and API Design](../../get-started.md#web-services-and-api-design)

---
