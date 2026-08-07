# Network and Information Security

---

## Table of Contents

<!-- TOC -->
* [Network and Information Security](#network-and-information-security)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Network Security](#network-security)
  * [Information Security](#information-security)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Network Security and Information Security are two related but distinct disciplines: Network Security is concerned with protecting the pathways data travels across, while Information Security is the broader umbrella discipline concerned with protecting the data itself — its confidentiality, integrity, and availability — regardless of where it lives or moves. Understanding the relationship between the two helps an architect reason about *where* a given control belongs in the system.

---

## Overview

Every other topic in this Cyber-security Fundamentals category — cryptography, access control, secure coding, vulnerability management — exists in service of Information Security's core objectives. Network Security is one *layer* within that broader goal, focused specifically on the infrastructure that carries data between systems: routers, switches, firewalls, VPNs, and the perimeter and internal segmentation that shapes how traffic is allowed to flow.

Architecturally, it helps to think of security as a set of concentric layers — network, host, application, and data — with Information Security as the discipline that defines *what* must be protected and *why*, and Network Security as one of several disciplines that defines *how* it's protected at a specific layer.

<sub>[Back to top](#table-of-contents)</sub>

---

## Network Security

Network Security is the practice of protecting the confidentiality, integrity, and availability of data as it moves across and between networks. Rather than duplicate the mechanics here, this section frames the discipline and defers to the dedicated networking page for the concrete controls.

- ### Defense in Depth at the Network Layer:
  No single network control is assumed sufficient. Perimeter firewalls, internal segmentation, intrusion detection/prevention systems, and encrypted transport are layered so that a failure or bypass of one control doesn't expose the entire network.

- ### Segmentation:
  Dividing a network into smaller zones (e.g., separating a public-facing DMZ from internal application tiers and databases) limits the blast radius of a compromise. An attacker who breaches one segment shouldn't automatically gain access to every other segment.

  ```mermaid
  flowchart LR
      Internet -->|Firewall| DMZ
      DMZ -->|Firewall| AppTier[Application Tier]
      AppTier -->|Firewall| DataTier[Data Tier]
  ```

  **Caption:** Segmented network zones, each gated behind its own firewall boundary rather than one flat perimeter.

- ### Zero-Trust Networking:
  The traditional model trusted anything "inside" the perimeter. Zero-trust networking assumes no implicit trust based on network location alone — every request is authenticated and authorized regardless of whether it originates inside or outside the corporate network, typically enforced via strong identity, device posture checks, and micro-segmentation.

  > See also: [Firewalls and VPNs](../networking/firewall-vpn.md) for the concrete mechanics of perimeter control and encrypted remote access that implement these principles.

<sub>[Back to top](#table-of-contents)</sub>

---

## Information Security

Information Security (InfoSec) is the umbrella discipline that all the other topics in this category ultimately serve. Its objectives are captured by the **CIA triad**:

- ### Confidentiality:
  Ensuring information is accessible only to those authorized to see it. Implemented through encryption, access control, and data classification.

- ### Integrity:
  Ensuring information is accurate and hasn't been tampered with, whether maliciously or accidentally. Implemented through hashing, digital signatures, checksums, and change-control processes.

- ### Availability:
  Ensuring information and systems are accessible to authorized users when needed. Implemented through redundancy, backups, DDoS protection, and resilient infrastructure design.

  ```mermaid
  graph TD
      CIA[CIA Triad] --> C[Confidentiality]
      CIA --> I[Integrity]
      CIA --> A[Availability]
  ```

  **Caption:** The CIA triad — the three properties that every security control in this category ultimately protects.

Every practice elsewhere in the Cyber-security Fundamentals section — access control, cryptography, secure coding, vulnerability management, incident response — is best understood as serving one or more legs of this triad.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Is Network Security a subset of Information Security, or a separate discipline?**
A: It's a subset — one of several layers (alongside application, host, and data security) through which Information Security's broader objectives (confidentiality, integrity, availability) are achieved. Network Security specifically addresses the layer where data is in motion across infrastructure.

---

**Q: How does zero-trust networking change the traditional perimeter model?**
A: The traditional model granted broad trust to anything already inside the network perimeter. Zero-trust removes that assumption entirely — every request is authenticated and authorized on its own merits, regardless of network location, which matters especially now that cloud services and remote work have dissolved the idea of a single defensible perimeter.

---

**Q: If a system has strong encryption and access control, does it automatically satisfy the CIA triad?**
A: Not fully — encryption and access control primarily serve confidentiality. Integrity requires separate controls like hashing and digital signatures, and availability requires redundancy and resilience planning. A well-rounded architecture addresses all three legs deliberately rather than assuming one control covers them all.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Firewalls and VPNs](../networking/firewall-vpn.md) — the concrete network-layer mechanics referenced above
- [Cryptography](cryptography.md) — the primary mechanism for confidentiality and integrity
- [Security Architecture and Design](security-architecture-design.md) — how these principles are applied when designing a system end to end
- [Risk, Governance and Compliance](risk-governance-compliance.md) — how organizations formalize and audit adherence to these principles

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [NIST SP 800-53 — Security and Privacy Controls](https://csrc.nist.gov/pubs/sp/800/53/r5/upd1/final)
- [NIST — Zero Trust Architecture (SP 800-207)](https://csrc.nist.gov/pubs/sp/800/207/final)

---

[Get Started](../../get-started.md) | [Cyber-security Fundamentals](../../get-started.md#cyber-security-fundamentals)

---
