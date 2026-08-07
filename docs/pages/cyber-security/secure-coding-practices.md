# Secure Coding Practices

---

## Table of Contents
<!-- TOC -->
* [Secure Coding Practices](#secure-coding-practices)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Input Validation and Sanitization](#input-validation-and-sanitization)
  * [Principle of Least Privilege in Code](#principle-of-least-privilege-in-code)
  * [Secure Defaults](#secure-defaults)
  * [Dependency and Supply-Chain Hygiene](#dependency-and-supply-chain-hygiene)
  * [Secrets Management](#secrets-management)
  * [Common Vulnerability Classes](#common-vulnerability-classes)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Secure coding practices are the habits and disciplines that prevent vulnerabilities from being written into a system in the first place. Unlike network or infrastructure security, secure coding is squarely the responsibility of every developer and architect on a team — a single unvalidated input or hardcoded credential can undermine an otherwise well-architected system. This page covers the practices that apply broadly across languages and frameworks; specific web vulnerability classes are covered in depth on the [Web Application Security](web-application-security.md) page.

---

## Overview

Most security incidents don't stem from broken cryptography or exotic zero-days — they stem from ordinary coding mistakes: trusting user input, granting more privilege than necessary, shipping insecure defaults, or leaving a secret in a config file that ends up in version control. Secure coding is largely about building habits that make the *safe* choice the *default* choice, so that security doesn't depend on every developer remembering every rule on every line of code.

The practices below are language-agnostic principles. Each language and framework has its own idiomatic way of implementing them (e.g., parameterized queries in SQL, `??` sanitizers in templating engines, secret managers in cloud platforms), but the underlying principle is the same across all of them.

<sub>[Back to top](#table-of-contents)</sub>

---

## Input Validation and Sanitization

Any data that originates outside the trust boundary of your code — user input, query parameters, HTTP headers, file uploads, even data from another internal service — must be treated as untrusted until validated.

- ### Validation:
  Confirm that input conforms to an expected shape (type, length, format, range) *before* it is used. Prefer **allow-lists** (explicitly permitted values/patterns) over **deny-lists** (blocked patterns), since deny-lists are easy to bypass with encoding tricks or edge cases the author didn't anticipate.

- ### Sanitization:
  Where input must be used in a specific context (HTML, SQL, a shell command, a file path), transform or escape it so it cannot be interpreted as code or control syntax in that context. Sanitization is context-specific — data safely escaped for HTML output is not automatically safe to use in a SQL query or a shell command.

  ```text
  untrusted input --> validate (shape/range) --> sanitize for target context --> use
  ```

- ### Where validation belongs:
  Validate at every trust boundary, not just at the edge. A microservice receiving a request from another internal service should still validate it — never assume "internal" traffic is inherently safe.

<sub>[Back to top](#table-of-contents)</sub>

---

## Principle of Least Privilege in Code

Code, services, and the credentials they run with should only have the minimum access required to do their job — nothing more.

- ### Service and process identity:
  A service that only reads from a database should use a database account with read-only permissions, not the same admin credential used for migrations. If that service is compromised, the blast radius is limited to what its credential can do.

- ### API and library scope:
  When integrating a third-party API, request the narrowest OAuth scopes or permission set that satisfies the use case — not broad "full access" tokens out of convenience.

- ### Why it matters architecturally:
  Least privilege limits the impact of any single compromise. It is a design decision made when defining service boundaries and provisioning credentials, not something bolted on after the fact.

<sub>[Back to top](#table-of-contents)</sub>

---

## Secure Defaults

Systems should be secure *out of the box*, without requiring an operator to actively opt into safety.

- ### Fail closed, not open:
  When an error occurs — an authentication check throws an exception, a permissions lookup times out — the system should default to denying access, not granting it.

- ### Minimize the default attack surface:
  Debug endpoints, verbose error messages, and administrative interfaces should be disabled by default in production, not merely "possible to disable."

- ### Opt-in, not opt-out, for risk:
  Features that weaken security (e.g., disabling certificate validation, allowing wildcard CORS origins) should require explicit, deliberate configuration — never be the default.

<sub>[Back to top](#table-of-contents)</sub>

---

## Dependency and Supply-Chain Hygiene

Modern applications are assembled from dozens or hundreds of third-party libraries, and vulnerabilities in those dependencies are as dangerous as vulnerabilities in your own code.

- ### Keep dependencies current:
  Regularly update libraries and track known-vulnerability feeds (e.g., CVE databases, GitHub Dependabot, Snyk) so you learn about a vulnerable transitive dependency before an attacker does.

- ### Verify provenance:
  Prefer packages from trusted registries, pin exact versions or use lockfiles, and be cautious of typosquatted package names — a well-known supply-chain attack vector.

- ### Minimize surface area:
  Every added dependency is added attack surface. Prefer well-maintained, widely-used libraries over obscure ones, and avoid pulling in a large library for a small piece of functionality you could implement directly.

<sub>[Back to top](#table-of-contents)</sub>

---

## Secrets Management

Credentials, API keys, and cryptographic material must never be hardcoded into source code — hardcoded secrets end up in version control history, build artifacts, and logs, all of which are far harder to fully purge than to prevent.

- ### Environment-based configuration:
  Inject secrets at runtime through environment variables or a dedicated secrets manager (e.g., HashiCorp Vault, AWS Secrets Manager, Azure Key Vault), never through committed config files. Application code should read secrets from its environment, not embed them.

- ### Rotation and revocation:
  Treat secrets as rotatable, short-lived where possible, and immediately revocable if exposed — a secret that can never be rotated without a code deployment is an architectural liability.

  > See also: [Key Management](cryptography.md#key-management) for the equivalent concerns around cryptographic keys specifically.

<sub>[Back to top](#table-of-contents)</sub>

---

## Common Vulnerability Classes

At a recognition level, every architect should be able to name the vulnerability classes that recur across nearly all software systems. Deep, example-driven coverage of these (and the full OWASP Top 10) lives on the [Web Application Security](web-application-security.md) page — this section is a quick orientation.

- **Injection** — untrusted input is concatenated into a command interpreter (SQL, shell, LDAP, etc.) and executed as code rather than treated as data. Prevented by parameterized queries/prepared statements and strict input validation.
- **Broken Authentication** — weaknesses in how a system verifies identity (weak password policies, session fixation, missing MFA) that let an attacker assume another user's identity.
- **Insecure Deserialization** — deserializing untrusted data without validation can let an attacker construct objects that trigger unintended code execution or logic. Avoid deserializing untrusted data with formats that support arbitrary object instantiation; prefer plain data formats (JSON with schema validation) over native serialization.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why prefer allow-lists over deny-lists for input validation?**
A: A deny-list must anticipate every possible malicious pattern, and attackers routinely find encoding tricks or edge cases the deny-list author didn't consider. An allow-list only needs to define what's *valid*, which is a much smaller and more stable surface to get right.

---

**Q: If a secrets manager is used, why does the principle still say "never hardcode credentials" — isn't reading from an environment variable good enough on its own?**
A: Environment variables are a mechanism, not a guarantee — a secret in a `.env` file committed to version control is just as exposed as one hardcoded in source. The principle is about where the secret's source of truth lives (a managed, access-controlled, rotatable store) and ensuring it never enters source control, not merely which API is used to read it at runtime.

---

**Q: Our team keeps all dependencies pinned to exact versions and rarely updates them for stability. Is that a good security practice?**
A: Pinning is good for reproducibility, but pinning *without* a regular update cadence means known vulnerabilities discovered in your dependencies sit unpatched indefinitely. The right pattern is pinned-but-monitored: lock exact versions, and use automated tooling (Dependabot, Snyk, etc.) to flag when a pinned version has a disclosed CVE so you can deliberately update.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Web Application Security](web-application-security.md) — deep dive into the OWASP Top 10 and web-specific vulnerability classes introduced above
- [Cryptography](cryptography.md) — key management practices that secrets management builds on
- [Security Architecture and Design](security-architecture-design.md) — how least privilege and defense in depth extend from code-level to system-level design

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [OWASP Secure Coding Practices Quick Reference Guide](https://owasp.org/www-project-secure-coding-practices-quick-reference-guide/) — authoritative checklist of secure coding practices
- [OWASP Top 10](https://owasp.org/www-project-top-ten/) — the industry-standard list of the most critical web application security risks

---

[Get Started](../../get-started.md) | [Cyber-security Fundamentals](../../get-started.md#cyber-security-fundamentals)

---
