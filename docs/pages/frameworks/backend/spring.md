# Spring Framework

---

## Table of Contents
<!-- TOC -->
* [Spring Framework](#spring-framework)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Core Concepts](#core-concepts)
  * [Spring Boot](#spring-boot)
  * [Spring Data](#spring-data)
  * [Spring Security](#spring-security)
  * [Spring Cloud](#spring-cloud)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Spring is a comprehensive application framework for [Java](../../programming/languages/java/java.md) built around Inversion of Control (IoC) and Dependency Injection (DI). Rather than a single library, it is an umbrella of composable modules — core container, data access, web, security, messaging, cloud — that share a consistent programming model. For most Java enterprise teams, Spring (and specifically Spring Boot) is the default starting point rather than one option among many.

---

## Overview

Spring emerged in the early 2000s as a reaction to the heavyweight complexity of J2EE/EJB. Its core insight was that business objects ("POJOs" — Plain Old Java Objects) don't need to implement framework interfaces or extend framework base classes to get transaction management, security, or dependency wiring — a container can provide those services externally. That insight, IoC, remains the architectural spine of everything Spring does today.

Spring dominates Java enterprise development because it solves the integration problem, not just the component problem. Individually, Hibernate, a servlet container, a security library, and a messaging client all work; wiring them together consistently, testing them in isolation, and configuring them across environments is where hand-rolled Java applications historically lost weeks. Spring's container standardizes that wiring, and its surrounding ecosystem (Data, Security, Cloud, Batch, Integration) extends the same DI-based model to each concern, so an architect learns one pattern and reapplies it everywhere.

Spring sits directly on top of the Java platform and its object model — generics, annotations, and reflection are what make component scanning and proxy-based features possible. See the [Java](../../programming/languages/java/java.md) page for the language fundamentals Spring builds on.

<sub>[Back to top](#table-of-contents)</sub>

---

## Core Concepts

Two ideas underpin every Spring module: Inversion of Control and the bean container that implements it.

- ### Inversion of Control (IoC) and Dependency Injection:
  In a conventional design, an object creates its own collaborators with `new`, coupling it to concrete implementations. Spring inverts this: objects declare what they need (typically via constructor parameters), and the **ApplicationContext** — Spring's IoC container — constructs the object graph and injects dependencies at startup. This is the general [Dependency Injection](../../design-patterns/ioc/dependency-injection.md) pattern applied consistently across an entire application.

  ```java
  @Service
  public class OrderService {
      private final PaymentClient paymentClient; // interface, not concrete class

      // Constructor injection: Spring supplies the implementation
      public OrderService(PaymentClient paymentClient) {
          this.paymentClient = paymentClient;
      }
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Beans and the Bean Container:
  A **bean** is any object whose lifecycle — instantiation, dependency injection, initialization, and eventual destruction — is managed by the ApplicationContext rather than by application code. Beans are declared via annotations (`@Component`, `@Service`, `@Repository`, `@Configuration` + `@Bean`) or, historically, XML. By default beans are singletons scoped to the container, though prototype, request, and session scopes are available for cases needing a fresh or narrower-lived instance.

  ```mermaid
  flowchart LR
      subgraph Container["ApplicationContext (IoC Container)"]
          B1["Bean: PaymentClient"]
          B2["Bean: OrderService"]
      end
      Container -- "1. instantiates" --> B1
      Container -- "2. instantiates & injects" --> B2
      B1 -- "3. injected into" --> B2
      Consumer["Controller / other code"] -- "4. requests bean" --> Container
      Container -- "5. returns wired instance" --> Consumer
  ```

  **Caption:** The container owns bean creation and wiring; consumers never call `new` on a managed dependency, they receive it fully assembled.

<sub>[Back to top](#table-of-contents)</sub>

---

## Spring Boot

Spring Boot is an opinionated layer on top of the core framework that eliminates the manual XML/Java configuration that made classic Spring projects slow to start. It provides **auto-configuration** (sensible defaults inferred from the classpath — add the JPA starter and Boot configures a `DataSource` and `EntityManager` for you), **starter dependencies** (curated dependency bundles like `spring-boot-starter-web`), and an embedded servlet container (Tomcat/Netty by default) so applications run as a plain executable JAR with no external application server to install.

This is why, in practice, "using Spring" almost always means using Spring Boot today: most new projects never touch raw `ApplicationContext` configuration directly, they start from a Boot starter and override only what deviates from the defaults.

<sub>[Back to top](#table-of-contents)</sub>

---

## Spring Data

Spring Data provides a repository abstraction that removes most boilerplate data-access code. Instead of writing DAO implementations by hand, an architect defines an interface (e.g., `interface UserRepository extends JpaRepository<User, Long>`), and Spring generates the implementation at runtime — including query derivation from method names like `findByEmailAndActiveTrue`.

The same repository abstraction is implemented consistently across relational (JPA/JDBC) and NoSQL (MongoDB, Redis, Cassandra) stores, so switching persistence technology largely means swapping the Spring Data module, not the calling code's shape. Conceptually this mirrors the [Repository Pattern](../../architectural-patterns/repository-pattern.md) — a mediation layer between domain logic and data mapping — and the proxy-backed interface implementation Spring generates at runtime is analogous to how a [Factory Method](../../design-patterns/creational/factory/factory-method.md) defers object creation to a specialized creator rather than the caller.

<sub>[Back to top](#table-of-contents)</sub>

---

## Spring Security

Spring Security handles authentication and authorization through a configurable chain of servlet filters that intercept each request before it reaches application code. Each filter has a single responsibility — extracting credentials, validating a token, checking CSRF, enforcing method-level `@PreAuthorize` rules — and the chain is assembled declaratively via a `SecurityFilterChain` bean rather than scattered `if` checks in controllers.

It supports authentication against multiple sources (in-memory, database, LDAP) and integrates with token-based and delegated authorization flows such as [OAuth](../../ws-and-api-design/authn-and-authz/oauth.md), making it the standard choice for securing both traditional session-based web apps and stateless REST/microservice APIs.

<sub>[Back to top](#table-of-contents)</sub>

---

## Spring Cloud

Spring Cloud extends Spring's DI-based model to distributed systems concerns that a single-process framework doesn't otherwise address: externalized configuration across environments (Config Server), dynamic service registration and lookup (service discovery, e.g., via Eureka or Consul), client-side load balancing, and resilience patterns like circuit breakers.

It is effectively the Spring-native toolkit for building services that follow [Microservices](../../architectural-patterns/microservices.md) architecture — each Spring Boot application becomes one deployable service, and Spring Cloud supplies the cross-cutting plumbing (discovery, config, resilience) that a distributed system of such services needs beyond what any single service's business logic provides.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why does Spring use interfaces so heavily for injected dependencies instead of concrete classes?**
A: Injecting against an interface lets the container supply any implementation — a real `PaymentClient` in production, a mock in tests, a different provider per environment — without changing the consuming class. It also lets Spring insert proxies (for transactions, security, AOP) transparently, since a proxy just needs to satisfy the same interface.

---

**Q: Do I still need to understand core Spring (the IoC container) if I only ever use Spring Boot?**
A: Yes. Spring Boot automates *configuration* of the container, not the container's behavior — bean scopes, injection styles, the ApplicationContext lifecycle, and proxy-based features like AOP are unchanged underneath. Debugging why a bean wasn't wired correctly, or why a proxy isn't applying, requires understanding the container Boot is configuring for you.

---

**Q: When would Spring Cloud's client-side patterns (e.g., discovery, config server) be unnecessary?**
A: When the deployment platform already provides equivalent capability — for example, a Kubernetes environment offers its own service discovery (DNS-based) and config/secret management (ConfigMaps, Secrets), and a service mesh can provide client-side load balancing and circuit breaking at the infrastructure layer. In those cases, duplicating the same concern in the application layer via Spring Cloud adds redundant complexity.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Dependency Injection](../../design-patterns/ioc/dependency-injection.md) — the general pattern Spring's IoC container implements
- [Java](../../programming/languages/java/java.md) — the language and platform Spring is built on
- [Repository Pattern](../../architectural-patterns/repository-pattern.md) — the abstraction Spring Data implements over concrete persistence technologies
- [Microservices](../../architectural-patterns/microservices.md) — the architecture Spring Cloud provides distributed-systems support for
- [OAuth](../../ws-and-api-design/authn-and-authz/oauth.md) — a delegated authorization flow Spring Security integrates with
- [Quarkus](quarkus.md) — a Kubernetes-native alternative Java framework, contrasted with Spring's traditional JVM startup profile

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/) — official reference
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/reference/) — official reference
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud) — official project index

---

[Get Started](../../../get-started.md) | [Web Development Frameworks](../../../get-started.md#web-development-frameworks)

---
