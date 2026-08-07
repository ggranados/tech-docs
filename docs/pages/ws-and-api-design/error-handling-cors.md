# Error Handling and CORS

---

## Table of Contents
<!-- TOC -->
* [Error Handling and CORS](#error-handling-and-cors)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Error Handling and Validation](#error-handling-and-validation)
  * [Cross-Origin Resource Sharing (CORS)](#cross-origin-resource-sharing-cors)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Two everyday concerns separate a professional API from a fragile one: how it communicates failure, and how it lets (or refuses) browsers from other origins to call it. Neither is glamorous, but both are the source of a disproportionate share of real-world API bugs and security incidents.

---

## Overview

Clients cannot build reliable integrations against an API whose errors are inconsistent — different shapes, ambiguous status codes, or leaked internal details from one endpoint to the next. A consistent, predictable error contract is as much a part of the API design as the happy path.

CORS is a related but distinct concern: it governs whether a browser running JavaScript from one origin is allowed to call an API hosted on another. Misunderstanding CORS is one of the most common sources of both confused debugging sessions and genuine security misconfigurations.

<sub>[Back to top](#table-of-contents)</sub>

---

## Error Handling and Validation

Consistent error handling turns failures from a debugging obstacle into predictable, actionable information for the client.

- ### Consistent Error Response Shape
  Rather than each endpoint inventing its own error format, a consistent shape lets clients parse and handle errors uniformly. **RFC 7807 (Problem Details for HTTP APIs)** defines a standard JSON shape for this purpose.

  ```json
  {
    "type": "https://example.com/errors/insufficient-funds",
    "title": "Insufficient Funds",
    "status": 422,
    "detail": "Account 12345 has insufficient funds to complete this transaction.",
    "instance": "/accounts/12345/transactions/98765"
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### HTTP Status Code Discipline
  Status codes communicate the *category* of outcome and should be used consistently: **4xx** means the client did something the server can't or won't process (bad input, missing auth, not found) — the client should not retry unchanged. **5xx** means the server failed to handle a valid request — clients may retry, often with backoff. Reusing `200 OK` with an error payload buried in the body, or defaulting everything unexpected to `500`, defeats this signal and breaks generic HTTP tooling (caches, proxies, retries).

  | Range | Meaning | Client action |
  |---|---|---|
  | 4xx | Client error (bad request, unauthorized, not found, validation failure) | Fix the request; don't blindly retry |
  | 5xx | Server error (unhandled exception, dependency failure, timeout) | Safe to retry, ideally with backoff |

<sub>[Back to top](#table-of-contents)</sub>

- ### Validation at the Boundary
  Input validation should happen as early as possible — at the API boundary, before a request reaches business logic — and failures should return a `400 Bad Request` (malformed syntax) or `422 Unprocessable Entity` (well-formed but semantically invalid) with enough detail for the client to correct the request, without leaking internal implementation details such as stack traces or SQL errors.

  > See also: [API Versioning and Security](api-versioning-security.md) — validation at the boundary is also a security control, not just a correctness check.

<sub>[Back to top](#table-of-contents)</sub>

---

## Cross-Origin Resource Sharing (CORS)

CORS is a browser-enforced mechanism that controls whether JavaScript running on one origin may make requests to an API hosted on a different origin.

- ### The Same-Origin Policy Problem
  Browsers enforce the **same-origin policy**: by default, a script loaded from `https://app.example.com` cannot make requests to `https://api.example.com` (different origin) using credentials, unless the API explicitly allows it. This protects users from malicious sites silently calling APIs on their behalf using their existing session cookies. CORS is the standard mechanism an API uses to *opt in* to being called from specific other origins.

  ```mermaid
  sequenceDiagram
      participant B as Browser (app.example.com)
      participant A as API (api.example.com)
      B->>A: OPTIONS /orders (preflight)
      A-->>B: Access-Control-Allow-Origin: https://app.example.com
      B->>A: GET /orders (actual request)
      A-->>B: 200 OK + data
  ```

  **Caption:** The browser preflights a cross-origin request, and only proceeds with the real request if the API's CORS headers explicitly allow the calling origin.

<sub>[Back to top](#table-of-contents)</sub>

- ### Preflight Requests
  For "non-simple" requests (custom headers, methods like `PUT`/`DELETE`, or certain content types), the browser automatically sends an `OPTIONS` **preflight** request before the real one, asking the server whether the actual request is permitted. The server responds with headers such as `Access-Control-Allow-Origin`, `Access-Control-Allow-Methods`, and `Access-Control-Allow-Headers` to grant (or withhold) permission.

  ```http request
  OPTIONS /api/orders HTTP/1.1
  Origin: https://app.example.com
  Access-Control-Request-Method: PUT
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Common Misconfiguration Risks
  The most common — and most dangerous — CORS mistake is returning `Access-Control-Allow-Origin: *` (or dynamically reflecting any requesting origin) on endpoints that also accept credentials (cookies, `Authorization` headers). This effectively disables the same-origin protection the browser was trying to enforce, allowing any website to make authenticated calls on a victim's behalf. Production APIs should allow-list specific known origins rather than wildcarding, especially on any endpoint that relies on cookies or session credentials.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why not just return `200 OK` for everything and put the real status in the JSON body?**
A: Doing so breaks generic HTTP infrastructure — caches, proxies, monitoring, and client libraries all rely on the status code to make correct decisions (e.g., whether to retry or cache). It also forces every client to parse the body just to know if the call succeeded.

---

**Q: Is CORS a server-side or client-side security mechanism?**
A: It's enforced by the browser (client-side), but configured by the server. The server declares which origins it trusts via response headers; the browser is what actually blocks disallowed cross-origin responses from reaching the calling script.

---

**Q: Does CORS protect an API from being called by non-browser clients, like `curl` or a mobile app?**
A: No. CORS is purely a browser enforcement mechanism. Non-browser clients ignore CORS headers entirely, so CORS is not a substitute for authentication, authorization, or server-side access control.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [RESTful Architecture](restful.md) — HTTP status code usage builds directly on REST's use of standard HTTP semantics.
- [API Versioning and Security](api-versioning-security.md) — input validation and error responses are part of the same API security boundary.
- [gRPC and GraphQL](grpc-graphql.md) — GraphQL's single-status-code error model contrasts with REST's status-code discipline described here.
- [HTTP and Data Formats](http-data-formats.md) — the underlying HTTP methods and status codes referenced throughout error handling.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [RFC 7807 — Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc7807) — the standard for structured HTTP API error responses.
- [MDN — Cross-Origin Resource Sharing (CORS)](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) — authoritative reference on CORS mechanics and headers.

---

[Get Started](../../get-started.md) | [Web Services and API Design](../../get-started.md#web-services-and-api-design)

---
