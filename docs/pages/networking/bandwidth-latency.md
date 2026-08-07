# Bandwidth and Latency

---

## Table of Contents
<!-- TOC -->
* [Bandwidth and Latency](#bandwidth-and-latency)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Bandwidth](#bandwidth)
  * [Latency](#latency)
  * [Bandwidth vs. Latency: The Architect's Trap](#bandwidth-vs-latency-the-architects-trap)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Bandwidth and latency are the two fundamental metrics that describe network performance, and confusing them is one of the most common — and most expensive — mistakes an architect can make. Bandwidth measures *how much* data can move; latency measures *how long* it takes for data to get there. A system can have enormous bandwidth and still feel slow if latency is high, and no amount of additional bandwidth will fix a latency problem.

---

## Overview

Both metrics describe different dimensions of the same physical and logical path between two endpoints. Bandwidth is essentially about pipe *width* — how many bits can be in flight per unit of time. Latency is about pipe *length* in time — how long a single bit takes to travel from one end to the other, largely bounded by the speed of light over distance plus processing delay at each hop.

Because they're independent, architecture decisions that improve one often do nothing for the other. Upgrading a link from 1 Gbps to 10 Gbps helps a large file transfer finish faster (more bandwidth), but does nothing to reduce the round-trip time of a single small API call to a server on another continent (latency, dominated by physical distance). Recognizing which one is the actual bottleneck for a given workload is a core architectural skill.

<sub>[Back to top](#table-of-contents)</sub>

---

## Bandwidth

Bandwidth is the maximum rate at which data can be transferred over a network connection, typically measured in bits per second (Mbps, Gbps).

- ### What It Measures:
  Bandwidth describes *capacity* — the theoretical maximum throughput of a link. It matters most for workloads that move large volumes of data: bulk file transfers, video streaming, database backups, batch data pipelines. A bandwidth-constrained system shows symptoms like slow large downloads/uploads and degraded performance under high concurrent volume, even when individual small requests still return quickly.

<sub>[Back to top](#table-of-contents)</sub>

- ### What Fixes It:
  Bandwidth problems are generally solved by adding capacity: faster network links, higher-throughput NICs, more parallel connections, or compression to reduce the amount of data that needs to move. Content Delivery Networks (CDNs) also help indirectly by serving large static assets from a location with ample local bandwidth to the end user, avoiding contention on the origin server's link.

<sub>[Back to top](#table-of-contents)</sub>

---

## Latency

Latency is the time it takes for a unit of data to travel from source to destination, typically measured in milliseconds (ms), often expressed as round-trip time (RTT).

- ### What It Measures:
  Latency describes *delay* — how long a single request has to wait before a response starts arriving. It's dominated by physical distance (a signal can't travel faster than the speed of light through fiber, roughly two-thirds of light speed in vacuum), the number of network hops, and processing delay at each hop (routers, firewalls, load balancers, application logic). It matters most for interactive, latency-sensitive workloads: API calls, real-time gaming, video calls, database queries in a request path — anywhere a user or a downstream system is waiting on a response.

<sub>[Back to top](#table-of-contents)</sub>

- ### What Fixes It:
  Because a large share of latency on long-distance requests is physical propagation delay, the primary architectural fix is **reducing distance**: placing compute and data geographically closer to the consumer via CDNs (for static/cacheable content), edge computing (for dynamic logic that must run close to the user), and geographic replication of data stores (multi-region deployments serving each region's users from a nearby replica). Reducing the number of round trips (e.g., batching requests, HTTP/2 multiplexing, avoiding chatty protocols) also reduces total observed latency independent of distance.

<sub>[Back to top](#table-of-contents)</sub>

---

## Bandwidth vs. Latency: The Architect's Trap

A classic and costly architectural mistake is diagnosing a latency problem as a bandwidth problem — "the app feels slow, let's upgrade the network link" — when the actual cause is round-trip time to a distant region, or an excessive number of sequential round trips (an N+1 query pattern is a latency problem, not a bandwidth problem). More bandwidth does not make a single round trip complete faster; it only helps when the bottleneck is genuinely the *volume* of data being moved.

| | Bandwidth | Latency |
|---|---|---|
| **What it measures** | Data transfer capacity | Time for data to arrive |
| **Typical unit** | Mbps / Gbps | Milliseconds (ms), often as RTT |
| **Dominant cause** | Link capacity, congestion | Physical distance, hop count, processing delay |
| **Typical symptom** | Slow bulk transfers, throughput ceiling under load | Slow individual request/response, sluggish interactivity |
| **What fixes it** | More capacity, compression, CDN offload of bulk assets | Reduce distance (CDN, edge, geo-replication), reduce round trips |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Our API response times are slow for users in Asia but fine for users in the US, where our servers are. Is this a bandwidth or a latency problem, and what's the fix?**
A: This is almost certainly a latency problem — the physical distance and number of network hops between Asia and the US-hosted servers dominate round-trip time, and adding bandwidth to the US servers won't change the speed of light. The architectural fix is reducing distance: a CDN for static assets, edge compute for logic that can run regionally, or full geographic replication of the backend closer to Asian users.

---

**Q: Why doesn't upgrading from a 1 Gbps to a 10 Gbps network link speed up a single small API request?**
A: A single small request (a few KB) already fits comfortably within a 1 Gbps link's capacity — bandwidth was never the bottleneck for that request. Its response time is dominated by round-trip latency (propagation delay plus processing at each hop), which is a property of distance and hop count, not of link capacity. The upgrade would only help if many large payloads were being transferred concurrently and saturating the link.

---

**Q: How does HTTP/2 (or gRPC) help with latency even without changing the underlying network?**
A: They reduce the number of round trips needed to complete a logical operation — HTTP/2 multiplexes multiple requests over a single connection (avoiding per-request TCP/TLS handshake overhead) and allows request pipelining, while gRPC's binary framing and streaming reduce serialization overhead and allow long-lived streams instead of repeated request/response cycles. Since round trips are a major latency contributor, reducing their count improves perceived responsiveness independent of raw bandwidth or physical distance.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Load Balancing](load-balancing.md) — load balancer placement and health-check overhead both contribute to observed request latency
- [Routing, Switching, and VLANs](routing-switching.md) — each additional routing hop adds incremental processing delay to overall latency
- [Firewall and VPN](firewall-vpn.md) — deep packet inspection and VPN tunnel encryption/decryption add processing latency at each traversal
- [TCP/IP](tcp-ip.md) — connection setup (handshakes) is a direct, measurable contributor to round-trip latency
- [OSI Model](osi-model.md) — bandwidth and latency are shaped by behavior at multiple OSI layers, from physical transmission to application protocol design

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Cloudflare Learning Center: What Is Latency?](https://www.cloudflare.com/learning/performance/glossary/what-is-latency/) — clear vendor explanation distinguishing latency from bandwidth/throughput
- [MDN Web Docs: Performance — Bandwidth and Latency](https://developer.mozilla.org/en-US/docs/Web/Performance) — web performance fundamentals covering both concepts

---

[Get Started](../../get-started.md) | [Networking Concepts](../../get-started.md#networking-concepts)

---
