# Risk, Governance and Compliance

---

## Table of Contents

<!-- TOC -->
* [Risk, Governance and Compliance](#risk-governance-and-compliance)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Risk Management](#risk-management)
  * [Security Governance and Compliance](#security-governance-and-compliance)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Risk Management and Security Governance and Compliance are the disciplines that decide *which* security controls an organization invests in, and *prove* to stakeholders and regulators that those controls actually work. Where other topics in this category are about implementing specific technical controls, this page is about the decision-making and accountability layer that sits above them.

---

## Overview

No organization has unlimited budget or engineering time to address every conceivable threat. Risk Management provides the framework for deciding where to invest limited security resources by systematically identifying, evaluating, and prioritizing risks. Security Governance and Compliance then provides the structure — policies, standards, and external frameworks — that turns those risk decisions into repeatable, auditable organizational practice.

For an architect, this matters practically: a proposed design isn't approved purely on technical merit — it also needs to be defensible against "what's the risk here, and how do we prove we managed it?" Understanding this layer helps translate technical trade-offs into language that risk committees, auditors, and executives can act on.

<sub>[Back to top](#table-of-contents)</sub>

---

## Risk Management

Risk Management is the discipline of identifying, assessing, and responding to threats to an organization's assets in a structured, repeatable way.

- ### Risk Formula:
  A widely used mental model expresses risk as a function of how likely a threat is to occur and how damaging it would be if it did:

  ```text
  Risk = Likelihood × Impact
  ```

  A high-likelihood, low-impact event (e.g., a minor misconfiguration) may warrant less urgent attention than a low-likelihood, catastrophic event (e.g., a full database breach) — the formula forces an explicit, comparable score rather than gut instinct.

- ### Risk Register:
  A risk register is a living document (or system) that catalogs identified risks along with their likelihood, impact, owner, and current treatment status. It gives an organization a single source of truth for what risks exist, who is accountable for them, and how they're being addressed — replacing ad hoc, undocumented risk awareness.

  | Risk | Likelihood | Impact | Owner | Response |
  |------|-----------|--------|-------|----------|
  | Unpatched public-facing server | Medium | High | Platform team | Mitigate (patch cadence) |
  | Vendor data center outage | Low | High | Infra lead | Transfer (SLA + insurance) |

- ### Risk Responses:
  Once a risk is identified and scored, an organization chooses one of four standard responses:

  - **Accept** — acknowledge the risk and take no further action, typically because the cost of mitigation exceeds the potential impact.
  - **Mitigate** — reduce the likelihood or impact through controls (e.g., patching, encryption, additional monitoring).
  - **Transfer** — shift the financial consequence to a third party, such as through cyber insurance or contractual SLAs with a vendor.
  - **Avoid** — eliminate the risk entirely by not engaging in the activity that creates it (e.g., not storing a category of sensitive data at all).

  ```mermaid
  flowchart TD
      R[Identified Risk] --> Q{Likelihood × Impact}
      Q --> Accept
      Q --> Mitigate
      Q --> Transfer
      Q --> Avoid
  ```

  **Caption:** Once a risk is scored, it is routed to one of the four standard risk responses.

<sub>[Back to top](#table-of-contents)</sub>

---

## Security Governance and Compliance

Security Governance establishes the organizational structures and documentation that make security decisions consistent and enforceable; Compliance is the process of demonstrating adherence to external legal, regulatory, or contractual requirements.

- ### Policy vs. Standard vs. Procedure:
  These terms are often used loosely but have distinct meanings in a mature governance program:

  - **Policy** — a high-level statement of intent, approved by leadership (e.g., "all sensitive data must be encrypted at rest").
  - **Standard** — a specific, measurable requirement that implements a policy (e.g., "AES-256 must be used for encryption at rest").
  - **Procedure** — the step-by-step instructions for carrying out a standard (e.g., the exact steps to configure disk encryption on a given platform).

  ```mermaid
  flowchart LR
      Policy --> Standard --> Procedure
  ```

  **Caption:** Governance documentation flows from broad intent (policy) to specific requirement (standard) to executable steps (procedure).

- ### Common Frameworks:
  Organizations rarely invent risk and control frameworks from scratch — they adopt established ones, both to save effort and to have a recognized standard auditors and customers trust. At an overview level:

  - **SOC 2** — an audit framework (AICPA) attesting that a service organization's controls meet trust principles such as security, availability, and confidentiality. Common for SaaS vendors selling to enterprise customers.
  - **ISO 27001** — an international standard for establishing, operating, and continually improving an Information Security Management System (ISMS).
  - **NIST CSF** — the NIST Cybersecurity Framework, a voluntary framework of five core functions (Identify, Protect, Detect, Respond, Recover) widely used to structure a security program regardless of industry.

  None of these are taught in depth here — the architect-relevant takeaway is that they exist as recognized common languages for describing "how mature is your security program," and a design decision may need to map to specific controls within whichever framework the organization has adopted.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why would an organization ever choose to "accept" a known risk instead of fixing it?**
A: When the cost or effort of mitigation exceeds the realistic cost of the risk materializing, acceptance is the economically rational choice. This decision should be explicit, documented in the risk register, and owned by someone with the authority to accept it — not a silent default from inaction.

---

**Q: What's the practical difference between a policy and a standard, and why does it matter for an architect?**
A: A policy states intent ("data must be encrypted"); a standard specifies the measurable requirement ("AES-256, TLS 1.2+"). Architects primarily work against standards — they're specific enough to design and test against, whereas a policy alone doesn't tell you which algorithm or key length to implement.

---

**Q: Does being ISO 27001 certified or SOC 2 compliant mean a system is actually secure?**
A: Not necessarily — these frameworks certify that a *process* for managing security risk exists and is followed consistently, not that every technical control is state-of-the-art. Compliance is a floor and an auditable trust signal for customers/partners, not a substitute for ongoing technical risk management.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Network and Information Security](network-and-information-security.md) — the CIA triad objectives that risk management ultimately protects
- [Vulnerability Assessment and Penetration Testing](vulnerability-assessment-pentesting.md) — a key input to identifying and scoring risks in the register
- [Security Operations and Response](security-operations-response.md) — where governance and risk decisions get executed operationally
- [Security Architecture and Design](security-architecture-design.md) — where risk-driven requirements get translated into system design

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [NIST Cybersecurity Framework (CSF) 2.0](https://www.nist.gov/cyberframework)
- [ISO/IEC 27001 — Information Security Management](https://www.iso.org/standard/27001)

---

[Get Started](../../get-started.md) | [Cyber-security Fundamentals](../../get-started.md#cyber-security-fundamentals)

---
