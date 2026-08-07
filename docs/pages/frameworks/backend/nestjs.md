# Nest.js

---

## Table of Contents
<!-- TOC -->
* [Nest.js](#nestjs)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Key Concepts](#key-concepts)
  * [Typical Use Cases](#typical-use-cases)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Nest.js is an opinionated, [TypeScript](../../programming/languages/typescript.md)-first framework for building server-side Node.js applications. Where [Express.js](expressjs.md) deliberately stays minimal, Nest imposes a defined application architecture — modules, controllers, and providers wired together through dependency injection — drawing explicit inspiration from Angular's structure. For teams that have felt the consistency cost of an unopinionated framework at scale, Nest is the structured alternative.

---

## Overview

Nest.js was released in 2017 to address a gap in the Node.js ecosystem: strong low-level tools (Express, Koa) existed, but nothing provided the kind of application-level architecture that backend frameworks in other ecosystems — Spring in Java, Angular in the frontend — had long offered. Nest doesn't replace Express under the hood; by default it runs on top of Express (or optionally Fastify) as its underlying HTTP layer, and adds a structured, opinionated framework on top.

That structure centers on **decorators** and **modules**: classes are annotated (`@Controller`, `@Injectable`, `@Module`) to declare their role, and the framework's built-in dependency injection container wires providers into controllers and into each other based on constructor parameter types — enabled by TypeScript's type metadata. This is a genuinely direct architectural parallel to [Spring](spring.md)'s IoC container and `@Service`/`@Component`/`@Autowired` model: both frameworks took the same core idea — let a container own object construction and wiring instead of the object itself — and applied it to their respective language ecosystems. An architect who understands Spring's DI model will recognize Nest's almost immediately, just expressed in TypeScript decorators instead of Java annotations.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

- ### Modules:
  A `@Module` is Nest's unit of organization — it groups related controllers and providers and declares what it exports for use by other modules. Every Nest application has at least a root module, and larger applications are decomposed into feature modules, giving the codebase an enforced structural boundary that Express leaves entirely to convention.

  ```typescript
  @Module({
    controllers: [OrdersController],
    providers: [OrdersService],
  })
  export class OrdersModule {}
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Decorators and Dependency Injection:
  Providers (`@Injectable()` classes — typically services) are injected into controllers or other providers via constructor parameters; Nest's container resolves and supplies them automatically, the same pattern Spring uses for Java beans.

  ```typescript
  @Injectable()
  export class OrdersService { /* business logic */ }

  @Controller('orders')
  export class OrdersController {
    constructor(private readonly ordersService: OrdersService) {} // injected
  }
  ```

  ```mermaid
  flowchart LR
      subgraph Container["Nest IoC Container"]
          P["Provider: OrdersService"]
      end
      Container -- "instantiates" --> P
      P -- "injected into" --> C["Controller: OrdersController"]
      Req["Incoming Request"] --> C
  ```

  **Caption:** Nest's container constructs providers and injects them into controllers, mirroring Spring's bean-container model.

<sub>[Back to top](#table-of-contents)</sub>

- ### Angular-Inspired Architecture:
  Nest's module/decorator/DI structure was explicitly modeled on Angular's, so teams already familiar with Angular on the frontend find Nest's backend architecture immediately recognizable — the same organizational vocabulary applies on both sides of the stack.

<sub>[Back to top](#table-of-contents)</sub>

- ### Contrast with Express's Minimalism:
  Nest trades [Express](expressjs.md)'s freedom for consistency: project structure, dependency wiring, and module boundaries are prescribed by the framework rather than left to team convention, which pays off as team and codebase size grow.

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

Nest fits larger Node.js backends and teams that need enforced architectural consistency across services — enterprise APIs, systems with multiple contributing teams, or organizations standardizing on TypeScript end-to-end. It's also a natural fit where the team already has Spring or Angular experience, since the DI/module vocabulary transfers directly. For small services or prototypes where Nest's structure would be overhead rather than value, Express remains the lighter-weight choice.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Does Nest.js replace Express, or run on top of it?**
A: By default, Nest runs on top of Express as its underlying HTTP server (Fastify is a supported alternative), and adds an opinionated application architecture — modules, controllers, providers, DI — above it. Nest is a structural layer, not a replacement for the HTTP handling underneath.

---

**Q: How is Nest's dependency injection similar to Spring's?**
A: Both use a container that inspects a class's declared dependencies (constructor parameters) and supplies concrete implementations automatically rather than having the class construct them itself. Spring does this via Java annotations and reflection on the JVM; Nest does the equivalent via TypeScript decorators and type metadata on Node.js. The architectural pattern — inversion of control — is the same, just expressed in each ecosystem's idioms.

---

**Q: When would Express's minimalism be a better choice than Nest's structure?**
A: When the service is small, short-lived, or owned by a single small team where Nest's imposed module/DI structure would be pure overhead rather than a consistency benefit — a narrow microservice or a quick internal API is often faster to build and reason about in plain Express.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Express.js](expressjs.md) — the minimal, unopinionated framework Nest builds on and contrasts with
- [Spring Framework](spring.md) — the Java framework whose IoC/DI model Nest's architecture directly parallels
- [TypeScript](../../programming/languages/typescript.md) — the language Nest is built with and designed around

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [NestJS Documentation](https://docs.nestjs.com/) — official documentation

---

[Get Started](../../../get-started.md) | [Web Development Frameworks](../../../get-started.md#web-development-frameworks)

---
