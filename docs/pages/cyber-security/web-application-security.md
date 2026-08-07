# Web Application Security

---

## Table of Contents
<!-- TOC -->
* [Web Application Security](#web-application-security)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [The OWASP Top 10](#the-owasp-top-10)
  * [Cross-Site Scripting (XSS)](#cross-site-scripting-xss)
  * [Cross-Site Request Forgery (CSRF)](#cross-site-request-forgery-csrf)
  * [Cross-Origin Resource Sharing (CORS)](#cross-origin-resource-sharing-cors)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Web application security is the discipline of protecting web-facing applications and APIs from the specific classes of attack that target how browsers, servers, and HTTP interact. It sits on top of the general secure coding practices every developer should follow, but focuses on threats unique to the web platform — the same origin model, session and cookie handling, HTML/JavaScript rendering, and the trust relationships between a browser and the servers it talks to.

---

## Overview

The web's security model is built on the browser enforcing boundaries — the **same-origin policy**, cookie scoping, and content security policies — while the server is responsible for validating everything it receives and never trusting the client to behave. Most web application vulnerabilities are a failure of one side or the other to enforce a boundary it was responsible for: a server that trusts client-supplied input, a browser configuration that allows a malicious page to act on a user's behalf, or a missing check on who is allowed to do what.

The **OWASP Top 10** is the industry's standard reference for the most critical and common web application risks, revised periodically based on real-world data. It's not an exhaustive list of every vulnerability, but it is the baseline every architect should recognize by name and understand at a conceptual level.

<sub>[Back to top](#table-of-contents)</sub>

---

## The OWASP Top 10

The following categories (based on the OWASP Top 10 2021 edition) represent the most critical web application security risks industry-wide. This section gives each a brief, recognition-level description — for the authentication- and authorization-specific categories, see the cross-links to existing dedicated pages rather than duplicated explanations.

- **A01: Broken Access Control** — failures to properly enforce what an authenticated user is permitted to do (e.g., accessing another user's data by changing an ID in a URL). This is covered in depth in [IAM](access-control-and-authn/iam.md) and [AuthN and AuthZ](../ws-and-api-design/authn-and-authz/authn-authz.md#authorization-authz).
- **A02: Cryptographic Failures** — sensitive data exposed due to missing or weak encryption, in transit or at rest. See [Cryptography](cryptography.md).
- **A03: Injection** — untrusted input executed as code by an interpreter (SQL, OS command, LDAP, etc.). See [Common Vulnerability Classes](secure-coding-practices.md#common-vulnerability-classes).
- **A04: Insecure Design** — security flaws baked into the architecture itself, not fixable by better code alone. See [Security Architecture and Design](security-architecture-design.md).
- **A05: Security Misconfiguration** — insecure defaults, unnecessary features enabled, verbose error messages, or missing security headers left in a production deployment.
- **A06: Vulnerable and Outdated Components** — using libraries or frameworks with known, unpatched vulnerabilities. See [Dependency and Supply-Chain Hygiene](secure-coding-practices.md#dependency-and-supply-chain-hygiene).
- **A07: Identification and Authentication Failures** — weaknesses in verifying user identity (weak passwords, missing MFA, session fixation). See [OAuth](../ws-and-api-design/authn-and-authz/oauth.md), [JWT](../ws-and-api-design/authn-and-authz/jwt.md), [SAML](../ws-and-api-design/authn-and-authz/saml.md), and [SSO](../ws-and-api-design/authn-and-authz/sso.md).
- **A08: Software and Data Integrity Failures** — trusting code, updates, or data (e.g., CI/CD pipelines, plugins, serialized data) without verifying their integrity. Related to [Insecure Deserialization](secure-coding-practices.md#common-vulnerability-classes).
- **A09: Security Logging and Monitoring Failures** — insufficient logging and alerting that delays detection of an active breach.
- **A10: Server-Side Request Forgery (SSRF)** — an attacker tricks the server into making unintended requests to internal or external systems, often to reach resources not directly exposed to the internet.

<sub>[Back to top](#table-of-contents)</sub>

---

## Cross-Site Scripting (XSS)

XSS occurs when an attacker injects malicious script into content that is later rendered by another user's browser, allowing that script to run in the victim's session — reading cookies, making authenticated requests, or defacing the page.

- ### Types:
  **Stored XSS** persists the malicious script on the server (e.g., in a comment field) so it executes for every visitor who views it. **Reflected XSS** bounces untrusted input from a request (e.g., a query parameter) directly back into the response. **DOM-based XSS** happens entirely client-side, when JavaScript writes untrusted data into the DOM without sanitization.

- ### Prevention:
  Context-aware output encoding (escape data for the specific context it's rendered in — HTML body, attribute, JavaScript, URL), a **Content Security Policy (CSP)** to restrict what scripts can execute, and using frameworks that auto-escape output by default rather than raw string concatenation into HTML.

<sub>[Back to top](#table-of-contents)</sub>

---

## Cross-Site Request Forgery (CSRF)

CSRF tricks a victim's browser into submitting an authenticated request to a site the victim is logged into, without the victim's knowledge — exploiting the fact that browsers automatically attach cookies to requests regardless of which page initiated them.

- ### How it works:
  A malicious site includes a form or request targeting a trusted site (e.g., a bank). If the victim is logged into that trusted site, their browser sends the request with valid session cookies attached, and the trusted site has no way to distinguish it from a legitimate user action.

- ### Prevention:
  **Anti-CSRF tokens** (a unique, unpredictable value tied to the user's session that must accompany state-changing requests), the `SameSite` cookie attribute (restricting when cookies are sent on cross-site requests), and requiring re-authentication for sensitive actions.

<sub>[Back to top](#table-of-contents)</sub>

---

## Cross-Origin Resource Sharing (CORS)

CORS is a browser mechanism that relaxes the same-origin policy in a controlled way, letting a server explicitly declare which other origins are allowed to make requests to it. It is a *relaxation* mechanism, not a security boundary in itself — a permissive CORS policy (e.g., reflecting any origin with credentials allowed) can expose an API to exactly the cross-origin abuse the same-origin policy was designed to prevent.

- ### The core principle:
  The server states, via response headers (`Access-Control-Allow-Origin`, etc.), which origins may read its responses from browser-based JavaScript. A missing or overly permissive CORS configuration doesn't create a vulnerability on its own, but it removes a layer of protection that other controls (authentication, CSRF tokens) then have to carry alone.

- ### Architectural note:
  CORS only governs browser-enforced, cross-origin JavaScript requests — it does nothing to stop server-to-server requests or tools like `curl`. Never treat CORS configuration as a substitute for authentication or authorization.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why does the OWASP Top 10 list "Broken Access Control" separately from "Identification and Authentication Failures" if they sound similar?**
A: Authentication failures concern verifying *who* a user is (weak passwords, missing MFA, session handling). Access control failures concern what an *already-authenticated* user is permitted to do (e.g., accessing another user's resource by changing an ID). A system can authenticate users perfectly and still be fully exploitable if it fails to check authorization on every request — the two are different layers of the same security model, covered in this repo under [IAM](access-control-and-authn/iam.md) and [AuthN and AuthZ](../ws-and-api-design/authn-and-authz/authn-authz.md).

---

**Q: If CSRF tokens exist, why do modern frameworks also push the `SameSite` cookie attribute so heavily?**
A: CSRF tokens require every state-changing form/request to correctly include and validate the token — easy to miss on a new endpoint. `SameSite=Lax` or `SameSite=Strict` provides a browser-enforced, application-independent baseline defense that doesn't rely on developers remembering to add a token everywhere, though it doesn't fully replace tokens for high-value actions.

---

**Q: Our API has `Access-Control-Allow-Origin: *`. Is that a vulnerability?**
A: Not automatically — a wildcard origin is fine for a fully public, unauthenticated API. It becomes dangerous when combined with credentialed requests (cookies or `Authorization` headers), because it means any website in the world can make authenticated cross-origin calls on a logged-in user's behalf. Browsers actually block wildcard origins from being combined with credentials for this reason — a permissive CORS setup paired with credential-based auth is a real red flag to review.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [IAM](access-control-and-authn/iam.md) — the broader identity and access control model behind Broken Access Control (A01)
- [AuthN and AuthZ](../ws-and-api-design/authn-and-authz/authn-authz.md) — detailed authentication and authorization mechanics
- [OAuth](../ws-and-api-design/authn-and-authz/oauth.md), [JWT](../ws-and-api-design/authn-and-authz/jwt.md), [SAML](../ws-and-api-design/authn-and-authz/saml.md), [SSO](../ws-and-api-design/authn-and-authz/sso.md) — the protocols underlying Identification and Authentication Failures (A07)
- [Cryptography](cryptography.md) — the encryption model behind Cryptographic Failures (A02)
- [Secure Coding Practices](secure-coding-practices.md) — the code-level disciplines that prevent Injection, Insecure Deserialization, and Supply-Chain risks
- [Security Architecture and Design](security-architecture-design.md) — how Insecure Design (A04) is addressed at the architecture level

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [OWASP Top 10](https://owasp.org/www-project-top-ten/) — the authoritative, community-maintained list this page summarizes
- [OWASP Cross-Site Scripting Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html) — detailed XSS prevention guidance
- [MDN: Cross-Origin Resource Sharing (CORS)](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) — authoritative reference on the CORS mechanism

---

[Get Started](../../get-started.md) | [Cyber-security Fundamentals](../../get-started.md#cyber-security-fundamentals)

---
