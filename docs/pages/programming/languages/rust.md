# Rust

---

## Table of Contents
<!-- TOC -->
* [Rust](#rust)
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

Rust is a statically typed systems programming language, originally developed at Mozilla and now stewarded by the independent Rust Foundation, built around a single defining idea: memory safety without a garbage collector. Its ownership and borrowing model lets the compiler guarantee — at compile time — that a program has no dangling pointers, no data races, and no use-after-free bugs, with zero runtime overhead. That guarantee has made Rust the language of choice for systems programming, WebAssembly, and performance-critical services where C/C++-level control is needed without C/C++-level risk.

---

## Overview

Rust emerged from Mozilla's search for a safer systems language to build browser engine components (the Servo project) without sacrificing C++'s performance. Its core innovation, the ownership model, moved a class of bugs that traditionally only showed up at runtime (or in production) into compile-time errors, at the cost of a steeper learning curve often nicknamed "fighting the borrow checker."

Because Rust achieves memory safety through compile-time analysis rather than a garbage collector, it has none of the runtime pauses or overhead that GC-based languages accept as a trade-off. This makes it viable in domains that previously had no safe alternative to C/C++: operating system components, embedded systems, browser engines, and latency-sensitive backend services.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

Rust is multi-paradigm — imperative and object-oriented-adjacent (via traits and structs rather than classical inheritance) with strong functional influences (pattern matching, closures, iterators) — but its defining characteristic sits below the paradigm level, in how it manages memory.

- ### Ownership and Borrowing:
  Every value in Rust has a single owner. When the owner goes out of scope, the value is dropped and its memory freed immediately — no garbage collector needed. Values can be *borrowed* — passed by reference — either immutably (many allowed at once) or mutably (only one allowed at a time), and the compiler enforces these rules at compile time, eliminating data races and use-after-free bugs before the program ever runs.

  ```rust
  fn main() {
      let s1 = String::from("hello");
      let s2 = s1; // ownership moves to s2; s1 is no longer valid

      let s3 = String::from("world");
      let len = calculate_length(&s3); // borrowed, not moved
      println!("{s3} has length {len}");
  }

  fn calculate_length(s: &String) -> usize {
      s.len()
  }
  ```

  ```mermaid
  stateDiagram-v2
      [*] --> Owned
      Owned --> Borrowed: &value
      Borrowed --> Owned: borrow ends
      Owned --> Dropped: owner goes out of scope
      Dropped --> [*]
  ```

  **Caption:** A value's lifecycle under Rust's ownership model — memory is freed deterministically when the owner goes out of scope.

- ### Zero-Cost Abstractions:
  High-level constructs like iterators, closures, and generics compile down to code as efficient as hand-written low-level equivalents — you don't pay a runtime performance penalty for writing expressive, safe code.

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

- Systems programming: operating system components, device drivers, embedded systems
- WebAssembly modules requiring near-native performance in the browser
- Performance-critical backend services and databases (e.g., high-throughput data infrastructure)
- Command-line tools and utilities where a single fast, safe binary is desirable
- Components of browsers and other software historically written in C/C++

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

Rust compiles ahead-of-time to native machine code with no VM, bytecode interpreter, or garbage collector at runtime. Memory safety is enforced entirely at compile time by the borrow checker, so there is no runtime safety net to pay for — deployed binaries are statically linked, self-contained executables similar in operational simplicity to Go's, with performance characteristics comparable to equivalent C or C++ code.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

`cargo` is Rust's built-in build tool and package manager, handling dependency resolution, building, testing, and publishing to the central `crates.io` registry — a notably unified experience compared to many older systems languages. `rustc`, the compiler, is known for unusually detailed and actionable error messages, which helps offset the borrow checker's learning curve.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How does Rust guarantee memory safety without a garbage collector?**
A: The compiler's borrow checker statically analyzes every reference in the code at compile time, enforcing that each value has exactly one owner and that mutable references can't coexist with other references. If the rules are violated, the code simply fails to compile — the safety guarantee is proven before the program ever runs, rather than checked or reclaimed at runtime.

---

**Q: Is Rust a replacement for Go, or do they serve different niches?**
A: They overlap in cloud infrastructure but optimize for different things. Go optimizes for developer simplicity and fast iteration with GC-managed memory; Rust optimizes for maximum performance and safety guarantees at the cost of a steeper learning curve. Choose Rust when you need C/C++-level control (embedded, low-latency, WASM) and Go when developer velocity and simplicity matter more than squeezing out every millisecond.

---

**Q: What's the practical downside of the ownership model?**
A: Development velocity, especially early on. Code that would compile immediately in a GC'd language often requires restructuring to satisfy the borrow checker, and the learning curve is real. The payoff is that entire categories of production bugs (data races, use-after-free, double-free) become compile-time errors instead of incidents.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Go](go.md) — another modern systems/infrastructure language, trading Rust's compile-time safety guarantees for GC-based simplicity
- [Concurrent Programming](../paradigms/concurrent.md) — Rust's ownership model extends to prevent data races in concurrent code at compile time
- [Functional Programming](../paradigms/functional.md) — Rust's iterators, closures, and pattern matching draw heavily from this paradigm

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [The Rust Programming Language Book](https://doc.rust-lang.org/book/) — official Rust language guide
- [Rust Documentation](https://doc.rust-lang.org/) — official documentation index

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
