# Security Architecture and Design

---

## Table of Contents
<!-- TOC -->
* [Security Architecture and Design](#security-architecture-and-design)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Defense in Depth](#defense-in-depth)
  * [Threat Modeling](#threat-modeling)
  * [Zero Trust Architecture](#zero-trust-architecture)
  * [Secure by Design Principles](#secure-by-design-principles)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Security architecture and design is where security stops being a checklist of individual controls and becomes a property of the system's structure itself. Firewalls, encryption, and input validation all matter, but a system can implement every one of them correctly and still be insecure if its overall architecture places too much trust in the wrong place. This page covers the architectural-level thinking — defense in depth, threat modeling, and zero trust — that determines whether individual security controls actually add up to a secure system.

---

## Overview

Security architecture asks a different question than most of the other pages in this section. Cryptography asks "how do I protect this data," and secure coding asks "how do I write this function safely" — security architecture asks "where should trust boundaries exist in this system, and what happens when any single control fails?" That last clause is the key one: good security architecture assumes individual controls *will* eventually fail or be bypassed, and designs the system so that a single failure doesn't cascade into a full compromise.

This is fundamentally an architectural discipline, not just a security one — decisions about service boundaries, network segmentation, and trust zones are the same kinds of decisions an architect makes when applying patterns like [Hexagonal Architecture](../architectural-patterns/hexagonal.md) or [Microservices](../architectural-patterns/microservices.md): where do the boundaries go, and what is allowed to cross them.

<sub>[Back to top](#table-of-contents)</sub>

---

## Defense in Depth

Defense in depth is the principle of layering multiple, independent security controls so that no single point of failure compromises the entire system. If a perimeter firewall is bypassed, network segmentation should still contain the damage; if network segmentation fails, application-level authorization should still block unauthorized access; if that fails, encryption should still protect the data at rest.

- ### Why redundancy is deliberate, not wasteful:
  In most engineering contexts, redundant controls solving the same problem look like waste. In security, redundancy is the point — every layer is assumed to be fallible, so the system's actual security is the product of all layers together, not any single strongest one.

  ```mermaid
  flowchart TD
      A[Perimeter: firewall, WAF] --> B[Network: segmentation, VPC boundaries]
      B --> C[Application: authN/authZ, input validation]
      C --> D[Data: encryption at rest, least-privilege DB access]
      D --> E[Protected Asset]
  ```

  **Caption:** Each layer is designed to contain a breach of the layer before it, rather than assuming that layer is impenetrable.

<sub>[Back to top](#table-of-contents)</sub>

---

## Threat Modeling

Threat modeling is the structured process of identifying what could go wrong in a system *before* it's built or as it evolves, rather than discovering weaknesses after an incident. It shifts security from a reactive, post-hoc audit into a proactive part of design.

- ### STRIDE:
  **STRIDE** is a widely used threat modeling framework that categorizes threats into six types: **S**poofing (impersonating something or someone), **T**ampering (modifying data or code without authorization), **R**epudiation (denying an action without a way to prove otherwise), **I**nformation Disclosure (exposing data to unauthorized parties), **D**enial of Service (degrading availability), and **E**levation of Privilege (gaining capabilities beyond what was granted).

- ### How it's applied:
  Teams typically diagram the system's data flows and trust boundaries, then walk through each component and boundary asking which STRIDE categories apply. The output isn't a proof of security — it's a prioritized list of risks to design against, informing which controls (from the layers described in defense in depth) are actually necessary.

<sub>[Back to top](#table-of-contents)</sub>

---

## Zero Trust Architecture

Zero trust is a modern architectural model built on a single core assumption: **no request should be trusted by default, regardless of whether it originates inside or outside the traditional network perimeter.** This is a significant departure from the older "castle and moat" model, where anything inside the corporate network was implicitly trusted once past the perimeter firewall.

- ### Why the perimeter model broke down:
  The castle-and-moat model assumed a clear, defensible network boundary. Cloud infrastructure, remote work, third-party SaaS integrations, and mobile devices erased that boundary — there is no longer a single "inside" to trust. Worse, once an attacker breaches the perimeter (via phishing, a compromised credential, a vulnerable third-party connection), the castle-and-moat model offers little further resistance, because everything inside was trusted.

- ### Core principles:
  - **Verify explicitly** — authenticate and authorize every request based on all available signals (identity, device health, location, behavior), not network location alone.
  - **Least privilege access** — grant the minimum access needed, scoped tightly and often just-in-time, rather than broad standing access.
  - **Assume breach** — design as if an attacker is already inside the network. Segment aggressively, encrypt traffic even internally, and monitor continuously so a breach is detected and contained quickly rather than allowed to move laterally unnoticed.

- ### What it looks like architecturally:
  Every service-to-service call is authenticated and authorized (often via mutual TLS and short-lived tokens) regardless of whether both services sit in the same network segment. Identity — of users, services, and devices — becomes the primary trust boundary, replacing the network perimeter.

  ```mermaid
  flowchart LR
      subgraph Old["Castle-and-Moat Model"]
          P1[Perimeter Firewall] --> I1[Implicitly trusted internal network]
      end
      subgraph New["Zero Trust Model"]
          U[Every request] --> V{Verify identity,<br/>device, context}
          V -->|explicit, scoped grant| R[Resource]
      end
  ```

  **Caption:** Zero trust replaces "trusted because internal" with "verified on every request," collapsing the network perimeter as the primary security boundary.

- ### Relation to service-oriented architecture:
  Zero trust and patterns like [Microservices](../architectural-patterns/microservices.md) reinforce each other: as systems decompose into many independently deployed services, there is no longer a single perimeter to defend, which makes per-request verification a necessity rather than an option. Similarly, [Hexagonal Architecture](../architectural-patterns/hexagonal.md)'s emphasis on isolating the application core behind explicit ports is itself a trust-boundary decision — deciding precisely what is allowed to enter the core and through which interface is the same design question zero trust asks at the network level.

<sub>[Back to top](#table-of-contents)</sub>

---

## Secure by Design Principles

Secure by design means security requirements are treated as first-class design constraints from the start of a system's design, not retrofitted after the architecture is set.

- ### Fail securely:
  When something goes wrong, the system should fail into a safe state (deny access, reject the request) rather than a permissive one.

- ### Minimize attack surface:
  Every exposed endpoint, open port, and enabled feature is a potential entry point. Deliberately expose only what's needed, and default unused capability to off.

- ### Separation of duties and least privilege:
  No single component or credential should have more capability than its role requires — echoing the least-privilege principle covered at the code level in [Secure Coding Practices](secure-coding-practices.md), applied here at the level of services, teams, and infrastructure.

- ### Design for auditability:
  A secure system should be able to answer "who did what, when" after the fact — this is a design decision (what gets logged, how logs are protected from tampering) as much as an operational one.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If defense in depth means layering many controls, doesn't that just mean "add more security everywhere"? How do you know when it's enough?**
A: Defense in depth isn't about adding controls indiscriminately — it's guided by threat modeling. STRIDE (or an equivalent framework) tells you *which* threats are realistic against *which* components, and defense in depth is the discipline of ensuring each identified threat is covered by more than one independent layer, so a single control's failure doesn't equal a full breach.

---

**Q: Is zero trust just a marketing term for "use a VPN and MFA," or is there a real architectural difference?**
A: There's a real difference. A VPN extends the old perimeter model — once connected, a VPN user is often broadly trusted on the internal network. Zero trust rejects that: every request is independently verified and scoped, VPN-connected or not, internal or not. MFA is one input to that verification, not the whole model — zero trust also demands least-privilege, per-request authorization, and assuming any given segment could already be compromised.

---

**Q: How does threat modeling relate to secure coding practices — don't they cover the same ground?**
A: They operate at different altitudes. Threat modeling happens at design time, at the level of components and data flows, and asks structurally "where could this system be attacked." Secure coding practices are the implementation-level techniques (input validation, least privilege in code, secure defaults) that address the specific threats threat modeling identifies. Threat modeling without secure coding produces a good diagram and a vulnerable system; secure coding without threat modeling produces well-written code defending against the wrong threats.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Hexagonal Architecture](../architectural-patterns/hexagonal.md) — isolating the application core behind explicit ports is itself a trust-boundary decision
- [Microservices](../architectural-patterns/microservices.md) — why the decomposition into independent services makes zero trust's per-request verification necessary
- [Secure Coding Practices](secure-coding-practices.md) — the implementation-level techniques that support secure-by-design and least privilege
- [Cryptography](cryptography.md) — encryption and key management as one of the layers in a defense-in-depth model
- [Web Application Security](web-application-security.md) — Insecure Design (OWASP A04), the vulnerability class this page's principles most directly prevent

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [NIST SP 800-207: Zero Trust Architecture](https://csrc.nist.gov/publications/detail/sp/800-207/final) — the authoritative federal standard defining zero trust architecture
- [OWASP Threat Modeling](https://owasp.org/www-community/Threat_Modeling) — overview of threat modeling methodologies including STRIDE

---

[Get Started](../../get-started.md) | [Cyber-security Fundamentals](../../get-started.md#cyber-security-fundamentals)

---
