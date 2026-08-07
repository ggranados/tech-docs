# Kafka

---

## Table of Contents


Kafka is an open-source, distributed event streaming platform originally developed by LinkedIn and later adopted and maintained by the Apache Software Foundation. _It is designed for the **real-time**, **fault-tolerant**, and **scalable handling** of **data streams**_. Kafka is often used in scenarios that involve ingesting, processing, and transmitting large volumes of data in a real-time or near-real-time fashion.


<sub>[Back to top](#table-of-contents)</sub>

## Key Characteristics and Components

### Publish-Subscribe Messaging System
Kafka operates as a publish-subscribe messaging system, where producers publish data to topics, and consumers subscribe to those topics to receive and process the data. This decouples producers from consumers, allowing for scalable and flexible data processing.

Distributed and Scalable: Kafka is designed to be distributed and horizontally scalable. It can handle very high throughput and large amounts of data by distributing data across multiple brokers, making it suitable for big data and high-velocity data processing.

Topics and Partitions: Data in Kafka is organized into topics, and each topic can be divided into partitions. Partitions allow for parallelism in data processing, enabling multiple consumers to work on different partitions of a topic simultaneously.

Fault Tolerance: Kafka is fault-tolerant, meaning it can recover from node failures without data loss. Data is replicated across multiple brokers, and in the event of a broker failure, another broker can take over the partition, ensuring data availability.

Stream Processing: Kafka Streams is a component that enables real-time stream processing within the Kafka ecosystem. It allows for the creation of real-time data processing applications, such as event-driven microservices and complex data transformations.

Connectors: Kafka Connect is a framework for connecting Kafka to external data sources and sinks. It simplifies the task of importing and exporting data to and from Kafka, making it easier to integrate with various data systems.

Durability and Retention: Kafka retains data for a configurable period of time, making it suitable for use cases like log aggregation and event sourcing. Data can be stored for an extended period, allowing for historical analysis.

Extensive Ecosystem: The Kafka ecosystem includes a variety of tools and technologies, such as Confluent, a company founded by the creators of Kafka, which offers a platform for Kafka and related services. There are also numerous client libraries for different programming languages, allowing developers to build Kafka-based applications in their language of choice.

Security: Kafka provides security features, including authentication and authorization mechanisms, to protect data and access to the system.

Real-World Use Cases: Kafka is used in a wide range of use cases, including log and event data aggregation, real-time analytics, monitoring, fraud detection, recommendation engines, and more.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How do partitions enable parallelism, and how does that affect consumer scaling?**
A: Each partition is consumed by only one consumer within a consumer group at a time, so the number of partitions caps how many consumers can process a topic in parallel. To scale consumption further, you need to increase the partition count.

---

**Q: What happens to data availability if a broker holding a partition fails?**
A: Kafka replicates each partition across multiple brokers. If the leader broker for a partition fails, one of its in-sync replica followers is automatically elected as the new leader, so consumers and producers keep operating without data loss.

---

**Q: What's the difference between using Kafka Streams and Kafka Connect?**
A: Kafka Streams is a client library for building applications that transform or aggregate data already flowing through Kafka topics, while Kafka Connect is a framework for moving data in and out of Kafka to and from external systems — databases, files, other message queues — without writing custom producer/consumer code.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Event Streaming](../event-streaming.md) — the broader concept Kafka implements as a distributed event streaming platform
- [NoSQL (Not Only SQL) Database](../../nosql/nosql.md) — commonly paired with Kafka as a downstream sink for streamed data
- [CAP Theorem](../../db-concepts/cap.md) — explains the consistency/availability trade-offs Kafka's replication model makes

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/) — official documentation
- [Apache Kafka Introduction](https://kafka.apache.org/intro) — official introduction to core concepts

---