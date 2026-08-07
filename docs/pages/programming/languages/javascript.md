# JavaScript

---

## Table of Contents
<!-- TOC -->
* [JavaScript](#javascript)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Paradigm](#paradigm)
  * [Typical Use Cases](#typical-use-cases)
  * [Runtime and Deployment](#runtime-and-deployment)
  * [Ecosystem](#ecosystem)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

JavaScript is a dynamically typed, single-threaded, event-driven language originally created to script interactivity into web pages. It has since become the dominant language of the browser and, via Node.js, a first-class server-side runtime as well. For an architect, its defining characteristic is not syntax but its concurrency model — a non-blocking, event-loop-based approach to I/O that shapes how systems built with it scale.

---

## Overview

JavaScript was created in 1995 and standardized as ECMAScript, with the language evolving significantly since ES6 (2015) added classes, modules, promises, and arrow functions. It is dynamically typed and interpreted (JIT-compiled at runtime by engines like V8), which favors development speed and flexibility over the compile-time guarantees of statically typed languages.

Its most architecturally significant trait is single-threaded, non-blocking execution via an event loop: rather than spawning a thread per concurrent operation, JavaScript queues callbacks and processes them as the call stack empties, avoiding the overhead and complexity of traditional multithreading for I/O-bound workloads.

The same language now runs in two very different environments — the browser (bound by the DOM and sandboxed by the browser security model) and Node.js (a server-side runtime with filesystem, network, and OS access) — which lets teams share code and skills across front end and back end.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

JavaScript is multi-paradigm: it supports prototypal object-oriented programming (objects can inherit directly from other objects, though the `class` keyword since ES6 provides familiar syntactic sugar over this), and functional programming (functions are first-class values, closures are pervasive, and array methods like `map`/`filter`/`reduce` encourage a declarative style).

The event loop is the concept that most distinguishes JavaScript architecturally. Asynchronous operations (network calls, timers, I/O) are handled via callbacks, Promises, or the `async`/`await` syntax, all of which are scheduled on the event loop rather than blocking the single execution thread.

```javascript
async function fetchUser(id) {
  const response = await fetch(`/api/users/${id}`); // non-blocking
  return response.json();
}
```

See [Functional Programming](../paradigms/functional.md) and [Object-Oriented Programming](../paradigms/object-oriented.md) for the underlying paradigm concepts JavaScript draws from.

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

- **Browser-based UI** — the only language natively executable in web browsers, underpinning frameworks like React, Angular, and Vue.
- **Server-side APIs and services** — via Node.js, commonly for I/O-heavy services (REST/GraphQL APIs, real-time apps) where the non-blocking model shines.
- **Full-stack applications** — sharing validation logic, types (with TypeScript), and even components between client and server.
- **Tooling and automation** — build tools, CLIs, and scripting via the Node.js ecosystem.

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

In the browser, JavaScript is interpreted and JIT-compiled by the engine embedded in the browser (V8 in Chrome/Edge, SpiderMonkey in Firefox, JavaScriptCore in Safari); deployment is simply serving static files, with no separate build artifact required (though bundling/minification is standard practice).

On the server, Node.js embeds V8 to run JavaScript outside the browser, adding APIs for the filesystem, networking, and OS. Node services are typically deployed as long-running processes (containers, PaaS, serverless functions) and scale horizontally — since a single Node process is effectively single-threaded for JS execution, CPU-bound work is usually offloaded to worker threads, separate processes, or another service entirely.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

npm (Node Package Manager) is the default package registry and the largest software package ecosystem in existence, ships with Node.js, and drives both browser and server tooling. Alternative package managers (Yarn, pnpm) target npm's registry with different dependency-resolution and performance trade-offs. TypeScript, a statically typed superset of JavaScript, has become a common default for larger codebases needing compile-time type safety.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

**Q: If JavaScript is single-threaded, how does Node.js handle thousands of concurrent connections?**
A: It doesn't use one thread per connection. I/O operations (database queries, network calls, file reads) are delegated to the OS or a background thread pool (libuv), and the event loop picks up their results via callbacks when ready. The single JS thread is only ever busy running your code, not waiting on I/O — which is why CPU-bound work, not I/O-bound work, is where Node struggles.

---

**Q: When would I choose Node.js over a language like Java or Go for a backend service?**
A: Node.js is a strong fit for I/O-bound services with many concurrent, lightweight requests (APIs, real-time gateways, BFFs) and where sharing code/types with a JavaScript front end has real value. For CPU-intensive workloads, or where strong static typing and mature multithreading are priorities, Java or Go are usually a better default.

---

**Q: Does prototypal inheritance actually matter if I just use `class` syntax?**
A: Mostly not day-to-day, but it matters when debugging: `class` is syntactic sugar over prototype chains, so behaviors like `this` binding, non-own-property lookups, and monkey-patching built-ins are easier to reason about once you know objects are really delegating to prototypes rather than copying a class definition.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Python](python.md) — another dynamic, multi-paradigm language, but with a GIL-constrained threading model instead of an event loop
- [PHP](php.md) — contrasts with Node's persistent event loop via a request-per-process execution model
- [Ruby](ruby.md) — a dynamic language with a stronger OOP-first identity
- [Functional Programming](../paradigms/functional.md) — closures, first-class functions, and array combinators JavaScript relies on
- [Object-Oriented Programming](../paradigms/object-oriented.md) — the class-based and prototypal OOP styles JavaScript supports

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [MDN JavaScript Guide](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide) — official language reference and guide
- [Node.js Documentation](https://nodejs.org/en/docs) — official Node.js runtime documentation

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
