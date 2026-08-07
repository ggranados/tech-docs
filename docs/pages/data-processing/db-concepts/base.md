# BASE

## Table of Contents
<!-- TOC -->
* [BASE](#base)
  * [Table of Contents](#table-of-contents)
  * [Basically Available](#basically-available)
  * [Soft State](#soft-state)
  * [Eventually Consistent](#eventually-consistent)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->
---

**BASE** is an acronym that represents a set of properties for distributed and NoSQL databases. It stands for:

## Basically Available

This means that the system is designed to be always available for read and write operations, even in the face of network partitions or hardware failures. Availability is prioritized over consistency in the CAP theorem (Consistency, Availability, Partition tolerance).


## Soft State

The system's state may change over time due to eventual consistency. In other words, there might be a delay between updates in different parts of a distributed system, and during that time, the system's state is considered "soft" or not completely consistent.


## Eventually Consistent

BASE systems prioritize eventual consistency over strong consistency. This means that while data updates might not be immediately reflected across all nodes in a distributed database, they will eventually reach a consistent state. Eventual consistency allows for better availability and fault tolerance, but it can lead to temporary inconsistencies in the data.


>BASE contrasts with the ACID properties (Atomicity, Consistency, Isolation, Durability), which are typically associated with traditional relational databases. 

>ACID databases prioritize strong consistency and transactional integrity, making them suitable for use cases where data consistency is critical, such as financial systems. On the other hand, BASE databases are often used in scenarios where high availability and fault tolerance are more important than immediate consistency, such as web applications, content delivery networks, and social media platforms.

![img.png](../../../img/base.png)

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How does "eventual consistency" differ from having no consistency guarantees at all?**
A: Eventual consistency guarantees that, given no new updates, all replicas will converge to the same value; it's a weaker but still well-defined guarantee, not an absence of consistency — the system simply doesn't promise when convergence happens.

---

**Q: Why would an architect choose BASE over ACID for a system?**
A: When availability and horizontal scalability under network partitions matter more than immediate consistency — e.g., social feeds, shopping carts, content delivery — BASE trades strict consistency for the ability to keep serving reads and writes even during partitions or node failures.

---

**Q: Can a system mix ACID and BASE approaches?**
A: Yes. Many architectures use ACID for critical transactional data (e.g., payments in a relational store) and BASE for high-volume, less consistency-sensitive data (e.g., activity feeds in a NoSQL store), applying each model where its trade-offs fit the use case.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [ACID](acid.md) — contrasting model that prioritizes strict consistency and transactional integrity
- [CAP Theorem](cap.md) — the theoretical basis explaining why BASE systems favor availability and partition tolerance
- [NoSQL (Not Only SQL) Database](../nosql/nosql.md) — database category that commonly implements BASE semantics

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.geeksforgeeks.org/base-properties-in-dbms/
- https://phoenixnap.com/kb/acid-vs-base

---

[Get Started](../../../get-started.md) |
[Database Concepts](../../../get-started.md#database-concepts)

___
