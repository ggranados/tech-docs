# Networking Fundamentals Content

**Severity:** High
**Depends on:** `content-template-compliance-audit`

## Problem

"Networking Concepts" is one of the 11 topic categories named in `CLAUDE.md`'s content organization, and `get-started.md` lists 15 networking topics (IP Addressing, Subnetting, Routing, Switching, TCP/IP, DNS, DHCP, Firewall, VPN, OSI Model, NAT, VLAN, Load Balancing, Bandwidth, Latency) — but there is not a single page in `docs/pages/` for any of them. This is a fully zero-coverage category despite being explicitly named as core scope.

## Goal

Establish baseline breadth coverage for networking fundamentals, prioritizing the concepts most load-bearing for a software architect (as opposed to a network engineer): OSI Model, TCP/IP, DNS, Load Balancing, VPN/Firewall basics, NAT.

## Scope hints

- Consider consolidating tightly related low-level concepts into fewer, broader pages rather than one page per bullet (e.g., "IP Addressing & Subnetting" together; "Routing & Switching" together) to match breadth-over-depth intent — let `speckit-specify`/`speckit-clarify` settle exact page grouping.
- OSI Model benefits strongly from a Mermaid diagram (layered `flowchart` or `graph TD`).
- New directory: `docs/pages/networking/`.
- Update `get-started.md` links once pages exist.

## Out of scope

- Cloud networking specifics (VPCs, cloud load balancers) — covered under `cloud-computing-content` if needed.
