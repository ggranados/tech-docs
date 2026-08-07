# Express.js

---

## Table of Contents
<!-- TOC -->
* [Express.js](#expressjs)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Key Concepts](#key-concepts)
  * [Typical Use Cases](#typical-use-cases)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Express.js is a minimal, unopinionated web framework for [Node.js](../../programming/languages/javascript.md). It provides just enough structure — routing and a request/response pipeline — to build HTTP servers and APIs, while deliberately leaving architecture, project layout, and most tooling choices to the developer. It has been the de facto standard Node.js web framework for over a decade precisely because it doesn't impose a way of working.

---

## Overview

Express was released in 2010 and quickly became the baseline most other Node.js web frameworks are described in relation to. Its design philosophy is minimalism: Express itself provides routing, request/response helpers, and a middleware pipeline, and essentially nothing else. There is no built-in ORM, no prescribed folder structure, no built-in dependency injection, no opinion on how you organize business logic. Everything beyond core HTTP handling — validation, authentication, database access, templating — is added through middleware or external libraries, chosen freely by the team.

This unopinionated stance is both Express's strength and its cost. It gives small teams and simple services a very low ceiling to get started, and gives larger teams full freedom to structure an application exactly as they see fit. The cost is consistency: two Express codebases from different teams can look structurally unrelated, since the framework enforces no convention. Frameworks built specifically to counter this, such as [Nest.js](nestjs.md), add the opinionated structure Express intentionally omits.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

- ### Middleware Chain:
  The core mental model in Express. A middleware is a function with access to the request, the response, and a `next` callback; each request flows through an ordered chain of middleware functions, and each one can inspect or modify the request/response, end the cycle by sending a response, or call `next()` to pass control forward. Logging, authentication, body parsing, and error handling are all just middleware in this same chain — there is no separate concept for cross-cutting concerns.

  ```javascript
  app.use((req, res, next) => {
    console.log(`${req.method} ${req.url}`);
    next(); // pass control to the next middleware/route handler
  });

  app.get('/orders/:id', authenticate, (req, res) => {
    res.json({ id: req.params.id });
  });
  ```

  ```mermaid
  flowchart LR
      Req["Incoming Request"] --> M1["Logger middleware"]
      M1 --> M2["Body parser middleware"]
      M2 --> M3["Auth middleware"]
      M3 --> H["Route handler"]
      H --> Res["Response"]
  ```

  **Caption:** Every request passes through an explicit, ordered chain of middleware before reaching (or bypassing) the route handler.

<sub>[Back to top](#table-of-contents)</sub>

- ### Routing:
  Routes bind an HTTP method and path pattern to a handler function, with support for path parameters, query strings, and route-level middleware — the same middleware mechanism used globally can be scoped to a single route or router.

<sub>[Back to top](#table-of-contents)</sub>

- ### Unopinionated by Design:
  Express makes no assumptions about project structure, persistence layer, or validation strategy. This contrasts sharply with opinionated frameworks like [Nest.js](nestjs.md), which impose a module/controller/provider architecture up front.

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

Express fits small to medium services, prototypes, and APIs where the team wants full control over structure without fighting a framework's conventions — a lightweight REST API, a backend-for-frontend layer, or a microservice with a narrow, well-understood scope. It becomes harder to justify as a codebase grows large and multiple teams need enforced consistency, which is where opinionated alternatives tend to pay off.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If Express provides so little structure, how do large teams keep codebases consistent?**
A: Consistency has to come from the team, not the framework — via internal conventions, linting, code review, and shared boilerplate/starter templates. This is exactly the gap opinionated frameworks like Nest.js are built to close: they bake the convention into the framework itself instead of relying on team discipline.

---

**Q: What happens if a middleware function forgets to call `next()`?**
A: The request hangs — control never passes to the next middleware or route handler, and (absent a timeout elsewhere) the client waits indefinitely for a response. This is a common source of bugs in hand-rolled Express middleware and one reason more structured frameworks abstract the pipeline behind higher-level constructs.

---

**Q: Is Express suitable for building a large, multi-team microservices system?**
A: It can be, but the lack of built-in structure means the team must independently establish conventions for module boundaries, dependency wiring, and error handling — conventions frameworks like Nest.js provide out of the box. Express is often chosen for small, narrowly scoped services precisely because minimal structure isn't a liability at that size.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Nest.js](nestjs.md) — an opinionated, TypeScript-first framework built on top of Node.js as a structured contrast to Express's minimalism
- [JavaScript (Node.js)](../../programming/languages/javascript.md) — the language and runtime Express is built for

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Express.js Documentation](https://expressjs.com/) — official documentation

---

[Get Started](../../../get-started.md) | [Web Development Frameworks](../../../get-started.md#web-development-frameworks)

---
