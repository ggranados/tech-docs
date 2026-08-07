# Clojure

---

## Table of Contents
<!-- TOC -->
* [Clojure](#clojure)
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

Clojure is a modern Lisp dialect designed by Rich Hickey and hosted primarily on the Java Virtual Machine. It is a functional-first language built around immutable data structures, a strong emphasis on simplicity, and direct interoperability with the host platform (Java, and via ClojureScript, JavaScript). For an architect, Clojure is most interesting as a case study in what a language looks like when persistent data structures and concurrency-safe state are the default rather than an opt-in library.

---

## Overview

Clojure emerged in 2007 as a response to the difficulty of writing correct concurrent programs on mutable, object-oriented foundations. Rather than inventing a new runtime, Hickey chose to host the language on the JVM (and later on JavaScript engines via ClojureScript, and the CLR), giving Clojure code direct access to the vast Java ecosystem while keeping the language itself small and consistent.

The language is a Lisp: code is written as nested parenthesized lists, and — because Lisp syntax is itself made of the same list/vector/map data structures the language manipulates at runtime — Clojure code can be generated, transformed, and analyzed by Clojure programs using the same tools used for ordinary data. This property is called **homoiconicity** ("code as data"), and it is the foundation of Clojure's powerful macro system, which lets developers extend the language's syntax without a separate preprocessor.

Clojure targets teams that want functional programming's correctness benefits (immutability, pure functions) combined with pragmatic access to an existing platform's libraries, rather than a from-scratch ecosystem.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

Clojure is a **functional-first** language, part of the same family covered in [Functional Programming](../paradigms/functional.md), with a few distinguishing traits:

- ### Immutability by Default:
  All of Clojure's core data structures — lists, vectors, maps, and sets — are **persistent and immutable**. "Persistent" here means updates return a new structure that structurally shares most of the underlying data with the original, so copying is cheap even though nothing is mutated in place.

  ```clojure
  (def original [1 2 3])
  (def updated (conj original 4)) ; original is untouched; updated is [1 2 3 4]
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Homoiconicity:
  Clojure source code is written using the language's own data structures (lists for calls, vectors, maps). This lets macros manipulate code as data at compile time, enabling language extension without external tooling.

  ```clojure
  ;; A list `(+ 1 2)` is both valid code and a valid data structure
  (+ 1 2) ;; => 3
  '(+ 1 2) ;; the same list, unevaluated — just data
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Software Transactional Memory (STM):
  Instead of manual locks, Clojure offers managed reference types — most notably **refs**, coordinated through STM. Multiple refs can be updated together inside a `dosync` transaction that is retried automatically on conflict, giving coordinated, consistent updates to shared state without deadlocks from lock ordering.

  ```clojure
  (def account-a (ref 100))
  (def account-b (ref 0))

  (dosync
    (alter account-a - 50)
    (alter account-b + 50)) ; both changes commit atomically, or the transaction retries
  ```

  > See also: [Concurrent Programming](../paradigms/concurrent.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

Clojure is commonly chosen for backend services and data-processing pipelines where correctness under concurrency matters — financial systems, streaming/event processing, and REPL-driven data analysis. Its JVM hosting also makes it a natural fit for teams that already run Java infrastructure and want a more expressive language without abandoning that platform. ClojureScript extends the same language to browser and Node.js front ends.

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

Clojure code compiles to JVM bytecode and runs on the standard JVM, so it deploys the same way any Java application does — as a JAR, inside a container, or on any JVM-compatible platform-as-a-service. This gives Clojure access to the full Java class library and existing operational tooling (monitoring, profilers, build artifacts) with no separate runtime to provision. ClojureScript instead compiles to JavaScript for browser or Node.js deployment.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

Clojure projects are typically built with **Leiningen** or the official **`tools.deps`/`deps.edn`** toolchain, and can pull in any dependency from Maven Central or Clojars. Because it interoperates directly with Java, Clojure code can call Java libraries and vice versa with minimal friction. The language ships with a REPL as a first-class development tool, and interactive, REPL-driven development is a defining part of the Clojure workflow.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why would a team pick Clojure over plain Java for a JVM-based service?**
A: Mainly for immutability and concurrency safety by default, and for the productivity of a dynamic, REPL-driven workflow. The trade-off is a smaller hiring pool and a steeper learning curve for teams unfamiliar with Lisp syntax.

---

**Q: How does STM in Clojure differ from just using synchronized blocks or locks?**
A: STM coordinates changes to multiple references as a single atomic, composable transaction that retries automatically on conflict, rather than requiring the programmer to reason about lock acquisition order. It trades some throughput for eliminating an entire class of deadlock and race-condition bugs.

---

**Q: Is Clojure a "pure" functional language like Haskell?**
A: No — Clojure encourages immutability and pure functions but does not enforce purity at the type level, and it explicitly supports managed mutable state (atoms, refs, agents) for the cases where state is unavoidable. It's pragmatic rather than doctrinaire.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Functional Programming](../paradigms/functional.md) — the paradigm Clojure is built around
- [Concurrent Programming](../paradigms/concurrent.md) — STM is Clojure's answer to safe concurrent state
- [TypeScript](typescript.md) — a contrasting approach to safety, via static structural typing instead of immutability

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Clojure Official Site](https://clojure.org/) — language reference and guides
- [Clojure Reference: State and Identity](https://clojure.org/reference/refs) — refs and STM documentation

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
