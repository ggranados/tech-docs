# IP Addressing, Subnetting, NAT, and DHCP

---

## Table of Contents
<!-- TOC -->
* [IP Addressing, Subnetting, NAT, and DHCP](#ip-addressing-subnetting-nat-and-dhcp)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [IP Addressing](#ip-addressing)
  * [Subnetting](#subnetting)
  * [NAT (Network Address Translation)](#nat-network-address-translation)
  * [DHCP](#dhcp)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

**IP addressing** identifies hosts on a network, **subnetting** partitions address space into manageable segments, **NAT** lets many private hosts share a small number of public addresses, and **DHCP** automates address assignment. Together these four mechanisms are what let the Internet layer (Layer 3 in [OSI](osi-model.md), the Internet layer in [TCP/IP](tcp-ip.md)) scale from a handful of research hosts to billions of devices. An architect doesn't need to hand-calculate subnet masks, but does need to recognize how these mechanisms constrain deployment topology — VPC design, service discovery, and public/private exposure all trace back to decisions made here.

---

## Overview

IPv4 was designed with a 32-bit address space — about 4.3 billion addresses — which seemed inexhaustible in the 1980s and has since been effectively exhausted by the growth of the Internet and connected devices. Subnetting, NAT, and IPv6 are three different (and complementary) responses to that scarcity and to the operational need to organize address space hierarchically rather than as one flat pool.

These four topics show up constantly in cloud architecture: a VPC's CIDR block is subnetting, a private subnet reaching the internet through a NAT gateway is NAT, and every container or VM getting an address without manual configuration is DHCP (or its cloud-native equivalent).

<sub>[Back to top](#table-of-contents)</sub>

---

## IP Addressing

An **IP address** uniquely identifies a device on a network so packets can be routed to it. Two versions are in use today:

- ### IPv4:
  32-bit addresses written in dotted-decimal notation, e.g. `192.168.1.10`. Provides roughly 4.3 billion unique addresses — long since exhausted for public allocation, which is the core reason NAT and private address ranges (`10.0.0.0/8`, `172.16.0.0/12`, `192.168.0.0/16`) exist.

<sub>[Back to top](#table-of-contents)</sub>

- ### IPv6:
  128-bit addresses written in hexadecimal groups, e.g. `2001:0db8:85a3::8a2e:0370:7334`. The address space (roughly 3.4×10³⁸ addresses) is large enough that every device can plausibly have a globally routable address, removing much of the practical need for NAT — though NAT and private addressing remain common in IPv4 and in hybrid environments. Adoption is steady but incomplete; architects generally need to support **dual-stack** (both IPv4 and IPv6) rather than assume one or the other.

<sub>[Back to top](#table-of-contents)</sub>

---

## Subnetting

**Subnetting** divides a larger IP address block into smaller, routable sub-networks, expressed with **CIDR notation** (Classless Inter-Domain Routing) — a suffix like `/24` denotes how many bits of the address are the fixed network portion, leaving the rest for host addresses.

- ### CIDR Notation:
  `10.0.0.0/24` means the first 24 bits are the network prefix, leaving 8 bits (256 addresses, 254 usable) for hosts. A smaller number after the slash (e.g. `/16`) means a *larger* address block; a larger number (e.g. `/28`) means a *smaller* one.

<sub>[Back to top](#table-of-contents)</sub>

- ### Why It Matters Architecturally:
  Subnetting is how cloud VPCs are structured: a `/16` VPC CIDR gets carved into smaller `/24` subnets, often one set of subnets per Availability Zone and a split between **public subnets** (route to an internet gateway) and **private subnets** (route out only through NAT). Getting subnet sizing wrong early is a common source of pain — too small a block means running out of IPs for autoscaling groups or pods; oversized blocks waste address space and complicate routing tables.

<sub>[Back to top](#table-of-contents)</sub>

---

## NAT (Network Address Translation)

**NAT** rewrites the source (or destination) IP address of packets as they cross a network boundary, most commonly to let many devices with private addresses share one public IP address.

- ### Why NAT Exists:
  IPv4's address scarcity means most organizations cannot get enough public addresses for every internal host. NAT lets an entire private network (using the reserved private ranges like `10.0.0.0/8`) share a small number of — often just one — public IP addresses for outbound Internet access, with the NAT device (router, gateway, or cloud NAT service) tracking which internal host each outbound connection belongs to so return traffic reaches the right host.

<sub>[Back to top](#table-of-contents)</sub>

- ### Why It Matters Architecturally:
  NAT is why hosts behind it are not directly reachable from the outside without extra configuration (port forwarding, a load balancer, or a reverse proxy) — this is precisely why public-facing services sit in public subnets or behind load balancers rather than being reached directly. NAT also affects connection behavior: architects need to be aware that NAT devices maintain **connection state** (a NAT table) with timeouts, which is one of several reasons long-lived idle connections can silently drop and why keep-alives matter. It's also directly relevant to load-balancing decisions — see [TCP/IP](tcp-ip.md) for connection semantics.

<sub>[Back to top](#table-of-contents)</sub>

---

## DHCP

**DHCP** (Dynamic Host Configuration Protocol) automatically assigns IP addresses (and related network configuration — subnet mask, default gateway, DNS servers) to devices joining a network, rather than requiring manual configuration on each host.

- ### How It Works:
  A device joining the network broadcasts a discovery request; a DHCP server responds with an offer of an available IP address (typically leased for a fixed duration), which the device accepts and periodically renews. This is often summarized as the **DORA** process: Discover, Offer, Request, Acknowledge.

<sub>[Back to top](#table-of-contents)</sub>

- ### Why It Matters Operationally:
  Without DHCP, every device joining a network — every laptop, every container, every autoscaled VM — would need manual IP configuration, which doesn't scale and is error-prone (duplicate address conflicts). In cloud environments, DHCP-equivalent mechanisms are what hand out IPs to VMs, pods (via CNI plugins in Kubernetes), and containers automatically as they're created and destroyed. Architects rarely configure DHCP directly, but should understand that dynamic addressing means **services should never be addressed by raw IP** — this is a core reason service discovery and DNS-based addressing exist. See [DNS](dns.md).

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If IPv6 has enough addresses to eliminate address scarcity, why do architects still need to understand NAT?**
A: IPv4 remains dominant in practice and most cloud environments run dual-stack or IPv4-only, so NAT is still the default for outbound Internet access from private subnets. Even in IPv6 environments, NAT-like boundary devices (or their conceptual equivalent, like security perimeters) still matter for understanding what's directly reachable from outside a network.

---

**Q: Why does my VPC's subnet CIDR size matter once the system is running in production?**
A: The subnet's `/prefix` fixes the maximum number of IP addresses (hosts, pods, load balancer nodes, etc.) that segment can ever hold. An undersized subnet can silently block autoscaling or new deployments once it's exhausted, and resizing a CIDR block after resources are already provisioned is often disruptive — so it needs to be sized for expected peak scale up front, not just current usage.

---

**Q: How do NAT and DHCP together explain why services should be addressed by name, not IP?**
A: DHCP means an instance's IP address can change every time it's recreated (scaling event, restart, redeploy), and NAT means many instances may not even have a stable, individually reachable public IP. Relying on a specific IP address anywhere in configuration or code is fragile for both reasons — which is exactly why DNS-based service discovery (see [DNS](dns.md)) is the standard pattern instead.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [TCP/IP](tcp-ip.md) — the Internet layer (IP) that addressing, subnetting, and NAT all operate on
- [The OSI Model](osi-model.md) — IP addressing corresponds to Layer 3 (Network) in the OSI reference model
- [DNS](dns.md) — the name resolution layer that lets systems avoid depending on dynamic, NAT'd IP addresses directly
- [Microservices Architecture](../architectural-patterns/microservices.md) — service discovery patterns exist largely because of dynamic, NAT'd, DHCP-assigned addressing

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [RFC 1918 — Address Allocation for Private Internets](https://www.rfc-editor.org/rfc/rfc1918) — defines the private IPv4 ranges NAT relies on
- [What is NAT? — Cloudflare Learning Center](https://www.cloudflare.com/learning/network-layer/what-is-nat/) — accessible architect-level overview

---

[Get Started](../../get-started.md) | [Networking Concepts](../../get-started.md#networking-concepts)

---
