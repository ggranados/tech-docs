# CAP Theorem

## Table of Contents
<!-- TOC -->
* [CAP Theorem](#cap-theorem)
  * [Table of Contents](#table-of-contents)
  * [Components](#components)
    * [Consistency (C)](#consistency-c)
    * [Availability (A)](#availability-a)
    * [Partition Tolerance (P)](#partition-tolerance-p)
  * [Scenarios](#scenarios-)
    * [CA](#ca)
    * [CP](#cp)
    * [AP](#ap)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->
---


The CAP theorem, also known as Brewer's theorem, is a fundamental principle in distributed computing that addresses the trade-offs involved in designing and operating distributed systems. 

It was formulated by computer scientist Eric Brewer in 2000 and has since become a cornerstone concept for architects and engineers working on distributed systems. 

The theorem states that in a distributed system, there are three key properties: **Consistency**, **Availability**, and **Partition Tolerance**, and _it is impossible for a distributed system to simultaneously guarantee all three to their fullest extent_.

Distributed systems must make trade-offs among these properties based on their specific requirements and use cases.

## Components

- **Consistency**: Every read receives the most recent write or an error.


- **Availability**: Every request receives a (non-error) response, without the guarantee that it contains the most recent write.


- **Partition tolerance**: The system continues to operate despite an arbitrary number of messages being dropped (or delayed) by the network between nodes

### Consistency (C)

Consistency refers to the idea that _all nodes in a distributed system see the same data at the same time_, regardless of which node they query.

- In a consistent system, if a write operation succeeds, all subsequent read operations should reflect that write.
- For this to happen, whenever data is written to one node, it must be instantly forwarded or replicated to all the other nodes in the system before the write is deemed ‘successful.’

> Achieving strong **consistency** often involves coordination and synchronization between nodes, which can introduce latency and reduce system **availability**.

<sub>[Back to top](#table-of-contents)</sub>

### Availability (A)

Availability means that _every request (read or write) made to the system receives a response_, even if it's not the most up-to-date data.

In an available system, the system remains operational and responsive to user requests, even in the presence of network failures or node crashes.

> Achieving high **availability** may require sacrificing some degree of **consistency**.


<sub>[Back to top](#table-of-contents)</sub>


### Partition Tolerance (P)

Partition tolerance accounts for the system's ability to continue functioning correctly even in the presence of network partitions or communication failures between nodes.

Network partitions can cause nodes in a distributed system to become isolated, making it impossible for them to communicate with each other.

> A system that is **partition-tolerant** can continue to operate in spite of network disruptions, but this may come at the cost of either **consistency** or **availability**.


<sub>[Back to top](#table-of-contents)</sub>


## Scenarios    
The CAP theorem asserts that a distributed system can achieve at most two out of the three properties at any given time. This leads to three possible scenarios:

![img.png](../../../img/cap.png)

### CA
_A system that prioritizes **Consistency** and **Availability** but is not **Partition-Tolerant**_. 

In other words, it sacrifices partition tolerance for strong consistency and high availability. This approach may work well in scenarios where network partitions are rare and can be quickly resolved.


<sub>[Back to top](#table-of-contents)</sub>


### CP
_A system that prioritizes **Consistency** and **Partition Tolerance** but may sacrifice **Availability**_. 

In this case, the system ensures data consistency and can tolerate network partitions, but it may become temporarily unavailable during network disruptions.


<sub>[Back to top](#table-of-contents)</sub>


### AP
_A system that prioritizes **Availability** and **Partition Tolerance** but may sacrifice **Consistency**_. 

This means the system remains available and can tolerate network partitions, even if it means that different nodes may temporarily have slightly inconsistent views of the data.



<sub>[Back to top](#table-of-contents)</sub>


>It's important to note that the CAP theorem doesn't provide a strict dichotomy; instead, it highlights trade-offs and the need to make design decisions based on the specific requirements and constraints of a distributed system. In practice, many distributed databases and systems aim for a balance between these properties, and various strategies, such as eventual consistency and quorum-based approaches, are used to achieve the desired trade-offs.


---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Since network partitions are rare, can I just design for CA and ignore partition tolerance?**
A: In practice, true CA systems don't survive at scale across a real network — partitions eventually happen. Architects should design for CP or AP and treat CA as viable only within a single node or a tightly coupled cluster where partitions are effectively impossible.

---

**Q: Is CAP theorem still relevant when a system uses eventual consistency?**
A: Yes — eventual consistency is essentially how AP systems resolve the CAP trade-off: they stay available and partition-tolerant by relaxing the consistency guarantee to "eventually consistent" instead of "always consistent."

---

**Q: How does CAP theorem relate to ACID and BASE?**
A: ACID-style strong consistency aligns with CP systems, while BASE's eventual consistency aligns with AP systems. CAP theorem provides the distributed-systems reasoning for why you can't have ACID-level consistency and BASE-level availability simultaneously during a partition.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [ACID](acid.md) — strong consistency model typically associated with CP-oriented systems
- [BASE](base.md) — availability-oriented model typically associated with AP-oriented systems
- [NoSQL (Not Only SQL) Database](../nosql/nosql.md) — NoSQL systems make explicit CAP trade-offs depending on their design goals

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://en.wikipedia.org/wiki/CAP_theorem
- https://www.ibm.com/topics/cap-theorem

---

[Get Started](../../../get-started.md) |
[Database Concepts](../../../get-started.md#database-concepts)

___
