# TypeScript

---

## Table of Contents
<!-- TOC -->
* [TypeScript](#typescript)
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

TypeScript is a typed superset of [JavaScript](javascript.md), developed by Microsoft, that adds static types, interfaces, and compile-time checking on top of the exact same language JavaScript developers already write. It is only meaningful in relation to JavaScript — TypeScript isn't a separate runtime or a different language family, it's JavaScript plus a type system that gets removed before the code ever runs.

---

## Overview

JavaScript's dynamic typing makes it flexible but leaves a class of errors — calling a method that doesn't exist, passing the wrong shape of object — undetected until the code actually runs. TypeScript, released by Microsoft in 2012, addresses this by layering an optional, gradual type system on top of JavaScript: types can be added incrementally, and existing JavaScript code is already valid TypeScript (with `any` filling gaps where types aren't yet specified).

Any valid `.ts` file is compiled ("transpiled") down to plain JavaScript by the TypeScript compiler (`tsc`) before it runs anywhere JavaScript runs — browser, Node.js, or any other JS engine. Types exist purely at compile time and are erased entirely from the emitted output.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

TypeScript follows JavaScript's own multi-paradigm nature (procedural, object-oriented, functional) but its defining architectural contribution is its type system's shape:

- ### Structural Typing:
  TypeScript's type system is **structural** ("duck typing" made static), not **nominal**. Two types are compatible if their shapes match — same properties, same types — regardless of whether one was explicitly declared to implement or extend the other. This is a genuine departure from nominally-typed languages like Java or C#, where compatibility requires an explicit `implements`/`extends` relationship.

  ```typescript
  interface Point { x: number; y: number; }

  function logPoint(p: Point) { console.log(p.x, p.y); }

  const obj = { x: 1, y: 2, label: "origin" };
  logPoint(obj); // valid — obj's shape satisfies Point, no "implements" needed
  ```

  In a nominally-typed language, `obj` would need to explicitly declare that it implements `Point` for this to compile. Structural typing instead asks only "does this value have the required shape," which fits JavaScript's existing object-literal-heavy style far more naturally than nominal typing would.

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

TypeScript is the default choice for any JavaScript codebase past a small prototype — large single-page applications (Angular is written in and requires TypeScript; React and Vue both support it heavily), Node.js backend services, and shared libraries where a stable public API surface matters. It's especially valuable on multi-developer teams and long-lived codebases, where the cost of an undetected type error compounds over time.

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

TypeScript has no runtime of its own — it compiles (transpiles) to plain JavaScript and then runs wherever that JavaScript runs: in a browser, on Node.js, or on any other JS engine. Deployment is therefore identical to deploying JavaScript; the TypeScript compilation step is purely a build-time concern (often integrated into a bundler like Vite, webpack, or esbuild) and produces no artifact that differs from ordinary JavaScript at runtime.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

The TypeScript compiler `tsc` is the reference implementation, configured via `tsconfig.json`. Type definitions for untyped JavaScript libraries are supplied through the community-maintained `DefinitelyTyped` project (`@types/*` packages on npm), and most modern frameworks and tools (Angular, Next.js, Vite, ESLint) have first-class TypeScript support. Editors like VS Code use the same TypeScript language service to power autocompletion and refactoring even in plain JavaScript files.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What's the practical difference between structural and nominal typing for architecture?**
A: Structural typing lets you satisfy an interface without any explicit coupling to it — useful for decoupling modules and adapting third-party shapes to an internal contract with no wrapper class. Nominal typing (Java, C#) gives stronger guarantees that a declared relationship is intentional, but requires more ceremony to satisfy an interface, especially across module boundaries.

---

**Q: If types are erased at compile time, what value does TypeScript actually provide at runtime?**
A: None directly — TypeScript catches errors during development and build, not in production. A malformed API response, for instance, will still cause a runtime error identical to plain JavaScript's, since types don't exist anymore by the time the code executes. Runtime validation (e.g., with a schema library) is still needed at trust boundaries like network responses.

---

**Q: Why would a team adopt TypeScript instead of just writing disciplined, well-tested JavaScript?**
A: Types catch a class of errors (wrong argument shapes, typos in property names, null/undefined mishandling) at compile time rather than relying on test coverage or runtime discovery, and they dramatically improve tooling — accurate autocomplete, safe rename-refactoring, and "find all usages" — which pays off increasingly as a codebase and team grow.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [JavaScript](javascript.md) — the language TypeScript compiles down to and is a superset of
- [HTML and CSS](html-css.md) — the other two pillars of a typical front-end stack alongside TypeScript
- [Object Oriented Programming](../paradigms/object-oriented.md) — TypeScript's interfaces and classes extend JavaScript's OOP support

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [TypeScript Official Documentation](https://www.typescriptlang.org/docs/) — language handbook and reference
- [TypeScript on GitHub](https://github.com/microsoft/TypeScript) — source and release notes

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
