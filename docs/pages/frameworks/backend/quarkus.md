# Quarkus

---

## Table of Contents
<!-- TOC -->
* [Quarkus](#quarkus)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Key Concepts](#key-concepts)
  * [Typical Use Cases](#typical-use-cases)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Quarkus is a Kubernetes-native Java framework designed from the ground up for fast startup and low memory footprint, rather than adapted to containers after the fact. Where traditional JVM frameworks pay their configuration and reflection costs at every application boot, Quarkus shifts as much of that work as possible to build time, so the runtime artifact starts in milliseconds instead of seconds. For an architect, it's the answer to "Java feels too heavy for this deployment shape" without abandoning the Java ecosystem.

---

## Overview

Quarkus was created by Red Hat and released in 2019, explicitly targeting the gap between Java's mature enterprise ecosystem and the startup/footprint expectations of containers and serverless platforms. Classic JVM frameworks — Spring included — do most of their configuration work (classpath scanning, annotation processing, proxy generation) at application startup, every time the process boots. That's a reasonable trade-off for a long-lived server process, but it's a poor fit for a container that might be created, scaled, and destroyed repeatedly, or a serverless function billed by execution time.

Quarkus moves that work to build time instead. Its build-time metadata processing analyzes beans, configuration, and dependency wiring during compilation rather than reflection at runtime, producing an application that boots dramatically faster and consumes a fraction of the memory of an equivalent traditional JVM app. Combined with **GraalVM native image** support — ahead-of-time compilation of the JVM application into a standalone native executable — Quarkus applications can start in single-digit milliseconds and run with a memory footprint suited to dense container packing.

Despite the different runtime model, Quarkus deliberately reuses familiar APIs (CDI-style dependency injection, JAX-RS/RESTEasy, Hibernate) so Java developers are not learning an unfamiliar programming model — the shift is in how the framework achieves performance, not in the day-to-day coding style.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Build-time augmentation | Quarkus's core technique — as much bean/configuration processing as possible happens at compile time instead of at application startup |
| GraalVM Native Image | Ahead-of-time compiler that turns a JVM application into a standalone native executable, trading JIT flexibility for near-instant startup and lower memory use |
| JVM mode vs. native mode | Quarkus apps can still run in a conventional JVM (faster builds, full JIT optimization over time) or be compiled to a native executable (faster startup, smaller footprint) — the same codebase supports both |
| Cold start | The latency penalty a process pays when starting from zero — the metric Quarkus is explicitly optimized to minimize, critical for serverless and autoscaled workloads |
| Dev mode | Quarkus's live-reload development loop, which applies code changes without a full restart despite the build-time-heavy production model |

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

Quarkus is the right reach when the deployment target punishes slow startup or high idle memory: **serverless functions** (where cold-start latency is billed and directly visible to users), **Kubernetes-based autoscaling** (where pods are frequently created and destroyed and fast readiness matters for scale-out responsiveness), and **container-dense environments** where per-instance memory footprint determines how many instances fit on a node.

It is a less compelling choice for long-lived, monolithic-style enterprise applications with stable instance counts, where Spring's larger ecosystem, longer track record, and one-time startup cost are less of a constraint — startup speed simply matters less when a process runs for weeks.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If Quarkus starts so much faster, why wouldn't every team just switch from Spring Boot?**
A: Startup speed only matters if the deployment model creates and destroys instances frequently — serverless, aggressive autoscaling, or container-dense packing. For a stable, long-running service, Spring Boot's one-time startup cost is amortized over the process's lifetime, and Spring's larger ecosystem, community, and library coverage often outweigh a startup-time advantage the workload doesn't need.

---

**Q: What's the trade-off of using GraalVM native image instead of running Quarkus on a regular JVM?**
A: Native image trades some runtime flexibility and peak throughput for startup speed and footprint — the JIT compiler's ability to optimize hot code paths over a long-running process is largely unavailable, and native builds are slower and pickier (reflection, dynamic class loading, and some libraries need explicit configuration to work under ahead-of-time compilation). JVM mode keeps full JIT behavior at the cost of slower startup.

---

**Q: Does adopting Quarkus mean giving up familiar Java APIs like JAX-RS or Hibernate?**
A: No — Quarkus deliberately supports standard and familiar APIs (JAX-RS/RESTEasy for REST, Hibernate ORM/Panache for persistence, CDI-style injection) so the day-to-day programming model is close to what a Java developer already knows. The difference is under the hood, in when and how that wiring is processed, not in the application code style.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Spring Framework](spring.md) — the traditional JVM framework Quarkus is most often contrasted against for startup and footprint trade-offs
- [Java](../../programming/languages/java/java.md) — the language and platform Quarkus builds on
- [Microservices](../../architectural-patterns/microservices.md) — the architecture style whose per-service scaling and container density make Quarkus's startup profile relevant

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Quarkus Documentation](https://quarkus.io/guides/) — official guides and reference
- [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/) — official reference

---

[Get Started](../../../get-started.md) | [Web Development Frameworks](../../../get-started.md#web-development-frameworks)

---
