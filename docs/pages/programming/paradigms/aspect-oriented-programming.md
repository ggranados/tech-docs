# Aspect-Oriented Programming (AOP)

---

## Table of Contents
<!-- TOC -->
* [Aspect-Oriented Programming (AOP)](#aspect-oriented-programming-aop)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Cross-Cutting Concerns and Weaving](#cross-cutting-concerns-and-weaving)
  * [Where AOP Shows Up in Practice](#where-aop-shows-up-in-practice)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Aspect-Oriented Programming is a paradigm for factoring out *cross-cutting concerns* — logging, security checks, transaction management, caching — that would otherwise be scattered across many unrelated modules, into a single reusable definition applied declaratively wherever needed.

---

## Overview

Most business logic decomposes cleanly into modules (classes, functions), but some concerns cut across that decomposition: you want every service method logged, every write operation wrapped in a transaction, every admin endpoint permission-checked. Implemented naively, that means duplicating the same boilerplate at the start (and often end) of dozens of methods. AOP lets you define that behavior once, as an *aspect*, and declare where it applies — leaving the core business logic focused only on what it's actually about.

<sub>[Back to top](#table-of-contents)</sub>

---

## Cross-Cutting Concerns and Weaving

- ### Aspects, Advice, and Pointcuts:
  An *aspect* bundles the cross-cutting behavior. *Advice* is the code that runs (before, after, or around a method call). A *pointcut* is the expression selecting which join points (typically method calls) the advice applies to.

  ```java
  @Aspect
  class LoggingAspect {
      @Before("execution(* com.example.service.*.*(..))")
      void logMethodEntry(JoinPoint jp) {
          System.out.println("Entering: " + jp.getSignature());
      }
  }
  ```

- ### Weaving:
  *Weaving* is the process of combining aspects with the target code — at compile time, class-load time, or runtime (proxy-based, as Spring AOP does). The weaving strategy determines the performance/flexibility trade-off: compile-time weaving is faster at runtime but less dynamic; proxy-based weaving is simpler to configure but only intercepts calls that go through the proxy.

  ```mermaid
  flowchart LR
      A[Business Method Call] --> B[Proxy / Woven Advice]
      B -->|before advice| C[Cross-Cutting Logic]
      C --> D[Actual Method Body]
      D --> E[Return]
  ```

  **Caption:** A proxy intercepts the call, runs the aspect's advice, then delegates to the real method.

<sub>[Back to top](#table-of-contents)</sub>

---

## Where AOP Shows Up in Practice

Most architects encounter AOP not by writing raw aspects, but through framework features built on it: Spring's `@Transactional` and `@Cacheable` annotations are AOP under the hood — a proxy wraps the annotated method with transaction or cache logic, keeping the business method itself clean. See the [Spring Core Concepts](../../frameworks/backend/spring.md) page for how this fits into that framework's dependency-injection model.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

**Q: How is AOP different from just calling a shared logging/transaction utility method directly?**
A: A shared utility still requires every caller to remember to invoke it — miss one call site and the concern silently doesn't apply. AOP's pointcuts apply the behavior declaratively based on a pattern (e.g., "every public method in this package"), so new methods matching the pattern get the behavior automatically, without each author having to remember.

---

**Q: What's the downside of proxy-based weaving, like Spring AOP uses?**
A: Advice only triggers when the call goes through the proxy — a method calling another method on `this` within the same class bypasses the proxy entirely, so `@Transactional` (or any other aspect) silently doesn't apply to that internal call. This is a common, confusing gotcha in Spring applications.

---

**Q: Is AOP still relevant, or has it been superseded by other patterns?**
A: It's less often hand-written today, but its ideas persist heavily inside frameworks (transaction management, caching, security annotations) — most developers use AOP-powered features constantly without writing an `@Aspect` themselves.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Spring](../../frameworks/backend/spring.md) — `@Transactional`/`@Cacheable` and Spring AOP are the most common real-world exposure to this paradigm
- [Decorator](../../design-patterns/structural/decorator.md) — a design-pattern-level way to add behavior around an object, conceptually related to advice wrapping a method
- [Dependency Injection](../../design-patterns/ioc/dependency-injection.md) — AOP and DI are often provided together by the same container (e.g., Spring's IoC container also weaves aspects)

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Spring Framework: Aspect Oriented Programming with Spring](https://docs.spring.io/spring-framework/reference/core/aop.html) — official documentation
- [AspectJ Documentation](https://www.eclipse.org/aspectj/doc/released/progguide/starting.html) — official documentation

---

[Get Started](../../../get-started.md#paradigms)

---
