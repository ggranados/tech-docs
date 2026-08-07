# Event-Driven Architecture

---

## Table of Contents
<!-- TOC -->
* [Event-Driven Architecture](#event-driven-architecture)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Core Concepts](#core-concepts)
  * [Benefits of Event-Driven Architecture](#benefits-of-event-driven-architecture)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Event-driven architecture (EDA) is a software design pattern that focuses on the production, detection, consumption, and reaction to **events**. In this architectural style, software components or services communicate and interact with each other by producing and consuming events, rather than relying on direct method calls or synchronous communication. EDA is the specific case of [Message-Driven Architecture](../message-driven.md) where the message being exchanged is always an event — a fact about something that has already happened.

---

## Overview

Because producers publish events without knowing who — if anyone — is listening, event-driven systems achieve strong decoupling: services can be added, removed, or changed without disrupting the rest of the system. This makes EDA a natural fit for microservices communication, IoT applications, real-time analytics, and any system that needs to react quickly to a high volume of independent occurrences.

Implementing EDA effectively is not free, however — it requires careful design of event schemas, routing strategies, and fault-tolerance mechanisms so that consumers can evolve independently without breaking on malformed or unexpected events.

<sub>[Back to top](#table-of-contents)</sub>

---

## Core Concepts

- ### Events:
  Events are notifications or messages that represent something of interest that has occurred within a system. These events can be related to various activities, such as user actions, system state changes, or external data updates. Events are typically represented in a structured format and carry information relevant to the event.

- ### Event Producers:
  Event producers are responsible for generating and emitting events when specific conditions or actions occur. These can be applications, services, or components within a larger system. Examples of event producers include user interfaces, sensors, databases, and external services.

- ### Event Consumers:
  Event consumers are entities that subscribe to or listen for specific types of events and take action when those events occur. Consumers can be other applications, services, or components. They react to events by executing predefined logic or triggering further actions. Event consumers are decoupled from event producers, meaning they don't need to know about each other's internal details.

- ### Event Broker or Message Queue:
  To facilitate communication between event producers and consumers, an event-driven architecture often employs an event broker or message queue system. This intermediary component receives events from producers and delivers them to the appropriate consumers. Common event broker technologies include Apache Kafka, RabbitMQ, and AWS SNS/SQS.

  > See also: [Event Streaming](../../data-processing/real-time/event-streaming.md), [Apache Kafka](../../data-processing/real-time/event-streaming/kafka.md)

  ```mermaid
  sequenceDiagram
      participant Producer as Event Producer
      participant Broker as Event Broker
      participant C1 as Consumer 1
      participant C2 as Consumer 2

      Producer->>Broker: publish OrderPlaced event
      Broker-->>C1: deliver event
      Broker-->>C2: deliver event
      C1->>C1: react (e.g. reserve inventory)
      C2->>C2: react (e.g. send confirmation email)
  ```

  **Caption:** A producer publishes an event once; the broker fans it out to every interested consumer, each reacting independently.

- ### Event Processing:
  Event-driven systems can incorporate event processing mechanisms that allow for complex event transformations, filtering, routing, and aggregation. These capabilities enable fine-grained control over how events are handled and which consumers receive them.

<sub>[Back to top](#table-of-contents)</sub>

---

## Benefits of Event-Driven Architecture

| Benefit | Description |
|---|---|
| Loose coupling | EDA promotes loose coupling between components, making it easier to change or extend parts of a system without affecting others. |
| Scalability | EDA can be highly scalable because events can be processed asynchronously, allowing systems to handle bursts of activity more effectively. |
| Real-time processing | EDA is well-suited for real-time applications and scenarios where immediate reactions to events are crucial. |
| Flexibility | New event consumers can be added or removed without disrupting the overall system, making it adaptable to changing requirements. |
| Fault tolerance | EDA can enhance fault tolerance by allowing events to be retried or routed to alternative consumers in case of failures. |

Common use cases for event-driven architecture include real-time analytics, microservices communication, IoT applications, and systems that require high scalability and responsiveness. A common pattern built on top of EDA is [Event Sourcing](event-driven/event-sourcing.md), which stores an entity's state as the sequence of events that produced it, rather than only its current snapshot.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How does using an event broker or message queue differ from having event producers call event consumers directly?**
A: With a broker (e.g., Kafka, RabbitMQ, AWS SNS/SQS) in between, producers and consumers never reference each other directly — the broker receives events and routes them to subscribers. This is what gives EDA loose coupling: consumers can be added, removed, or changed without the producer knowing they exist.

---

**Q: What makes event-driven architecture a good fit for real-time analytics and IoT, but potentially risky for systems that need strict consistency?**
A: EDA processes events asynchronously as they occur, which is exactly what real-time analytics and IoT streams need. But because consumers react independently and on their own schedule, there's no guarantee all consumers have processed an event at any given instant — systems that require strong, immediate consistency across components need extra coordination (e.g., sagas, idempotent handlers) on top of plain EDA.

---

**Q: What's the difference between an event producer/consumer and the event processing layer mentioned in the article?**
A: Producers and consumers are the endpoints that emit and react to events. Event processing is a separate concern that sits between them (often in or alongside the broker) — it handles transformation, filtering, routing, and aggregation of events before they reach consumers, giving fine-grained control over which consumers see which events.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Message-Driven Architecture](../message-driven.md) — the umbrella concept EDA specializes: message-driven communication where messages are always events
- [Event Sourcing](event-driven/event-sourcing.md) — a persistence pattern that models state as a sequence of events, often combined with EDA
- [Reactive Systems](../reactive.md) — reactive systems name message-driven communication as one of their four core traits, closely related to EDA
- [Microservices Architecture](../microservices.md) — asynchronous messaging via EDA is a common pattern for decoupling microservice communication
- [Event Streaming](../../data-processing/real-time/event-streaming.md) — platforms like Kafka implement the event broker role described here at scale

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Event-driven architecture style — Azure Architecture Center](https://learn.microsoft.com/en-us/azure/architecture/guide/architecture-styles/event-driven) — official documentation
- [What is Event-Driven Architecture? — AWS](https://aws.amazon.com/event-driven-architecture/) — official documentation

---

[Get Started](../../../get-started.md) | [Architectural Patterns](../../../get-started.md#architectural-patterns)

---
