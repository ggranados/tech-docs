# Firewall and VPN

---

## Table of Contents
<!-- TOC -->
* [Firewall and VPN](#firewall-and-vpn)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Firewall](#firewall)
  * [VPN (Virtual Private Network)](#vpn-virtual-private-network)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Firewalls and VPNs are two of the most common network-layer security controls an architect will design around: firewalls decide *what* traffic is allowed to cross a network boundary, and VPNs establish an encrypted, trusted path *across* an otherwise untrusted network (typically the public internet). Together they form the outer perimeter of most enterprise and cloud network designs, complementing — not replacing — identity-based access controls at the application layer.

---

## Overview

As networks grew beyond a single trusted LAN, the need arose to explicitly control which traffic could cross the boundary between trusted and untrusted zones (e.g., a corporate network and the internet), and to let legitimate remote users or sites reach trusted internal resources without exposing them directly. Firewalls solve the first problem by inspecting and filtering traffic at a boundary; VPNs solve the second by creating an encrypted tunnel that makes a remote endpoint behave, logically, as if it were inside the trusted network.

Both concepts predate cloud computing but remain foundational there: cloud security groups and network ACLs are firewall concepts re-implemented as software-defined constructs, and site-to-site VPNs (or their modern equivalent, private interconnects) are still the standard way to connect an on-premises data center to a cloud VPC.

<sub>[Back to top](#table-of-contents)</sub>

---

## Firewall

A firewall is a network security control that monitors and filters incoming and outgoing traffic based on a defined set of rules, sitting at the boundary between trust zones.

- ### Packet-Filtering Firewall:
  The earliest and simplest type. It inspects each packet in isolation — source/destination IP, port, protocol — against a static rule set (an access control list) and allows or denies it. It's fast because it doesn't track connection state, but it also can't tell a legitimate reply packet from a spoofed one, since it has no memory of prior traffic.

<sub>[Back to top](#table-of-contents)</sub>

- ### Stateful Firewall:
  Tracks the state of active connections (e.g., a TCP handshake in progress, an established session) and uses that state to make smarter decisions — for example, automatically allowing return traffic for a connection that was legitimately initiated from inside, without needing an explicit rule for the reply direction. This is the baseline behavior of most modern network firewalls and cloud security groups.

<sub>[Back to top](#table-of-contents)</sub>

- ### Next-Generation / Application-Layer Firewall:
  Goes beyond IP/port/state and inspects traffic up to Layer 7 — understanding application protocols like HTTP, identifying specific applications regardless of port, and often integrating intrusion prevention, deep packet inspection, and threat intelligence. A **Web Application Firewall (WAF)** is a specialized case focused specifically on HTTP traffic, filtering requests based on patterns associated with common web attacks (e.g., SQL injection, cross-site scripting).

  > See also: [Identity and Access Management (IAM)](../cyber-security/access-control-and-authn/iam.md) — firewalls control *which network traffic* can reach a resource; IAM controls *who and what* is authorized to act once a connection is permitted. Defense in depth typically layers both.

<sub>[Back to top](#table-of-contents)</sub>

---

## VPN (Virtual Private Network)

A VPN extends a private, trusted network across a public or untrusted network by establishing an encrypted tunnel, so that traffic inside the tunnel is protected from interception and the endpoints behave as if directly connected.

- ### Site-to-Site VPN:
  Connects two entire networks — for example, an on-premises data center and a cloud VPC, or two branch offices — through a persistent encrypted tunnel between two gateway devices. Every host on either network can reach hosts on the other network transparently, without each individual device needing its own VPN configuration. This is the standard pattern for hybrid-cloud connectivity, though many cloud providers now also offer dedicated private interconnects as a lower-latency, non-internet alternative.

<sub>[Back to top](#table-of-contents)</sub>

- ### Remote-Access VPN:
  Connects an individual device (a laptop, a remote worker's machine) to a private network over the internet, typically through client software that establishes an encrypted tunnel to a VPN gateway. This is the pattern behind "connect to the corporate VPN" — it lets a single remote endpoint reach internal resources as though it were physically on the internal network.

<sub>[Back to top](#table-of-contents)</sub>

- ### The Encrypted Tunnel Concept:
  Regardless of type, a VPN works by encapsulating the original packet inside an encrypted outer packet for transit across the untrusted network, then decapsulating and decrypting it at the far end. This provides confidentiality (traffic can't be read in transit) and, depending on protocol, integrity and authentication of the endpoints. Common protocols include IPsec, WireGuard, and TLS-based VPNs — the choice affects performance, ease of NAT traversal, and cryptographic guarantees, but the tunneling concept is consistent across all of them.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If traffic is already encrypted end-to-end with TLS, why would an architecture still need a VPN?**
A: TLS protects the confidentiality and integrity of a single connection's payload, but it doesn't hide *that* a connection exists, doesn't extend network-level reachability, and doesn't protect non-TLS traffic. A VPN additionally makes a remote network or host reachable as if it were local, which is often the actual requirement (e.g., letting a remote data center resolve internal DNS names and reach internal-only services) — TLS and VPN solve different, complementary problems.

---

**Q: What's the practical difference between a stateful firewall and a Web Application Firewall (WAF)?**
A: A stateful firewall operates at Layers 3/4, tracking connections and filtering by IP/port/protocol — it has no concept of what an HTTP request actually contains. A WAF operates at Layer 7 specifically for web traffic, inspecting request content for attack patterns (SQL injection, XSS, malformed payloads). They're commonly layered: the stateful firewall controls broad network reachability, the WAF protects the specific web application logic behind it.

---

**Q: In a cloud VPC, what plays the role of a traditional network firewall?**
A: Security groups (stateful, attached to individual resources/instances) and network ACLs (typically stateless, attached to subnets) implement the same packet-filtering and stateful-inspection concepts as traditional firewalls, but as software-defined, API-managed constructs rather than physical appliances — the underlying model of allow/deny rules based on source, destination, port, and protocol is unchanged.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Routing, Switching, and VLANs](routing-switching.md) — firewalls are commonly placed at the routed boundary between VLANs or subnets
- [Load Balancing](load-balancing.md) — load balancers and firewalls (including WAFs) are frequently deployed together at the network edge
- [Bandwidth and Latency](bandwidth-latency.md) — VPN tunneling and deep packet inspection both add processing overhead that can increase latency
- [OSI Model](osi-model.md) — firewalls operate at different OSI layers depending on type, from packet-filtering (L3/L4) to next-gen (L7)
- [TCP/IP](tcp-ip.md) — the addressing and connection-state concepts that stateful firewalls track

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [NIST SP 800-41: Guidelines on Firewalls and Firewall Policy](https://csrc.nist.gov/pubs/sp/800/41/r1/final) — authoritative U.S. government reference on firewall types and policy
- [Cisco: What Is a VPN?](https://www.cisco.com/c/en/us/products/security/vpn-endpoint-security-clients/what-is-vpn.html) — vendor overview of VPN concepts and types

---

[Get Started](../../get-started.md) | [Networking Concepts](../../get-started.md#networking-concepts)

---
