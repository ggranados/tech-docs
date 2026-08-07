# Routing, Switching, and VLANs

---

## Table of Contents
<!-- TOC -->
* [Routing, Switching, and VLANs](#routing-switching-and-vlans)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Routing](#routing)
  * [Switching](#switching)
  * [VLAN (Virtual Local Area Network)](#vlan-virtual-local-area-network)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Every request an application makes eventually rides on top of two fundamental forwarding decisions: how a frame moves across a local segment (switching), and how a packet moves between different networks (routing). An architect rarely configures either directly, but understanding the distinction — and where VLANs fit in — is essential for reasoning about network segmentation, blast-radius, latency between tiers, and why "just put it on the same network" is sometimes a security anti-pattern.

---

## Overview

Routing and switching operate at different layers of the OSI model and solve different problems. Switching happens within a single broadcast domain (a LAN segment) and forwards traffic based on physical/link-layer addresses. Routing happens between different networks (or subnets) and forwards traffic based on logical, hierarchical addresses.

VLANs sit conceptually between the two: they let a single physical switching fabric be logically partitioned into multiple isolated broadcast domains, each of which then requires a router (or a Layer 3 switch) to communicate with the others. This combination — switch inside a VLAN, router between VLANs — is the backbone of how modern data centers and cloud VPCs segment workloads.

<sub>[Back to top](#table-of-contents)</sub>

---

## Routing

Routing is the process of forwarding packets between different networks based on their destination IP address, using **Layer 3** of the OSI model.

- ### Routing Table and Next-Hop:
  Every router (and every host, in a simplified form) keeps a routing table: a set of entries mapping destination network prefixes to a **next-hop** — the address of the next device that should receive the packet on its way to the destination. The router does not need to know the full path; it only needs to know the correct next hop for a given prefix, and relies on every other router along the path to do the same. This "hop-by-hop" model is what lets the internet scale without any single device holding a complete map.

  ```text
  Destination        Next Hop        Interface
  10.0.1.0/24         directly connected   eth0
  10.0.2.0/24         10.0.0.1             eth1
  0.0.0.0/0 (default) 203.0.113.1          eth2 (uplink)
  ```

  The **default route** (`0.0.0.0/0`) is the catch-all next hop used when no more specific entry matches — typically pointing toward the internet uplink or a NAT gateway.

<sub>[Back to top](#table-of-contents)</sub>

- ### Static vs. Dynamic Routing:
  Static routes are manually configured and predictable but don't adapt to failures. Dynamic routing protocols (e.g., OSPF, BGP) let routers exchange reachability information and recompute paths automatically when links go down. Cloud VPC route tables and Kubernetes cluster networking are, conceptually, automated forms of the same idea: route tables that are computed and reconciled rather than hand-written.

<sub>[Back to top](#table-of-contents)</sub>

---

## Switching

Switching is the process of forwarding frames within a single local network segment based on **Layer 2** (data link layer) MAC addresses.

- ### MAC Address Forwarding:
  A switch learns which device sits behind which physical port by inspecting the source MAC address of incoming frames and building a MAC address table. When a frame arrives for a known destination MAC, the switch forwards it only out the matching port instead of broadcasting it to every port — unlike an older, simpler hub. This makes switching fast (hardware-level forwarding decisions) and efficient (no unnecessary traffic on unrelated segments).

<sub>[Back to top](#table-of-contents)</sub>

- ### Switching vs. Routing:
  The practical distinction an architect needs is: switching connects devices *within* the same subnet/broadcast domain and is unaware of IP addressing; routing connects *different* subnets and requires an IP-aware Layer 3 device. A "Layer 3 switch" is a hybrid device that performs high-speed switching within VLANs and routes between them in the same box — common in modern data center top-of-rack designs.

<sub>[Back to top](#table-of-contents)</sub>

---

## VLAN (Virtual Local Area Network)

A VLAN is a logical partition of a physical switching infrastructure into multiple, isolated broadcast domains, each behaving as if it were on its own separate switch.

- ### Why VLANs Matter for Segmentation:
  Without VLANs, every device connected to a switch fabric shares the same broadcast domain — any host can, at the network level, reach any other host on the same physical LAN. VLANs let an architect group hosts logically (by tenant, environment, tier, or trust level) regardless of physical cabling, and enforce that traffic between VLANs must pass through a router or firewall, where policy can be applied. This is the same principle behind cloud subnetting (e.g., separate subnets for public web tier, application tier, and database tier within a VPC) and is a foundational building block for both network segmentation and multi-tenant isolation.

  > See also: [Identity and Access Management (IAM)](../cyber-security/access-control-and-authn/iam.md) — VLANs provide network-layer segmentation, while IAM governs who and what can authenticate and act within or across those segments; the two are complementary layers of a defense-in-depth strategy.

<sub>[Back to top](#table-of-contents)</sub>

- ### Trunking and Tagging:
  A single physical link between switches (a **trunk**) can carry traffic for multiple VLANs simultaneously by tagging each frame with a VLAN ID (802.1Q). This avoids running a separate physical cable per VLAN and is what makes VLANs practical at scale.

  ```mermaid
  flowchart TB
      Internet((Internet))
      R[Router / Layer 3 Gateway]
      SW[Layer 2 Switch]

      subgraph VLAN10 [VLAN 10 - App Subnet]
          H1[App Server A]
          H2[App Server B]
      end

      subgraph VLAN20 [VLAN 20 - DB Subnet]
          H3[DB Server A]
          H4[DB Server B]
      end

      Internet --- R
      R ---|Layer 3 routing between VLANs| SW
      SW ---|Layer 2 switching, same VLAN| H1
      SW --- H2
      SW ---|Layer 2 switching, same VLAN| H3
      SW --- H4
  ```

  **Caption:** Hosts within the same VLAN reach each other via Layer 2 switching; traffic between VLAN 10 and VLAN 20 must pass through the Layer 3 router, where segmentation policy (ACLs, firewall rules) can be enforced.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If two servers are in different subnets, do they need a router, even if they're plugged into the same physical switch?**
A: Yes. Physical proximity is irrelevant — what matters is the logical network (subnet/VLAN). Two hosts in different subnets must have their traffic routed at Layer 3, even if a single physical switch (acting as a Layer 3 switch) performs both the switching and the routing internally.

---

**Q: Why would an architect deliberately put the web tier, app tier, and database tier of a system into separate VLANs or subnets?**
A: It creates enforcement points. Because inter-VLAN traffic must pass through a router or firewall, the architect can require that only the app tier — not the public internet — is allowed to reach the database tier, shrinking the blast radius of a compromised web server and satisfying common compliance segmentation requirements.

---

**Q: Does a dynamic routing protocol like BGP matter for someone designing application architecture, or is that purely a network-engineering concern?**
A: Mostly network-engineering, but architects should recognize BGP's role at the edges of their system: it's what determines how traffic reaches a cloud region or on-premises data center from the internet, and BGP-level events (route flaps, hijacks, provider outages) are a real source of the "network partition" failure mode that distributed systems design (e.g., CAP theorem trade-offs) has to account for.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Load Balancing](load-balancing.md) — load balancers typically sit at Layer 4/7 above the switching and routing fabric described here, distributing traffic across backend instances
- [Firewall and VPN](firewall-vpn.md) — firewalls are commonly placed at the routed boundary between VLANs/subnets to enforce segmentation policy
- [Bandwidth and Latency](bandwidth-latency.md) — the number of hops introduced by routing directly affects end-to-end latency
- [OSI Model](osi-model.md) — defines the Layer 2 (data link) and Layer 3 (network) boundaries that separate switching from routing
- [TCP/IP](tcp-ip.md) — the addressing and protocol suite that routing decisions are made against

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Cisco: What Is Routing?](https://www.cisco.com/c/en/us/solutions/enterprise-networks/what-is-routing.html) — official vendor overview of routing fundamentals
- [IEEE 802.1Q — VLAN Tagging Standard](https://www.ieee802.org/1/pages/802.1Q.html) — the IEEE standard defining VLAN trunking and tagging

---

[Get Started](../../get-started.md) | [Networking Concepts](../../get-started.md#networking-concepts)

---
