# Swift

---

## Table of Contents
<!-- TOC -->
* [Swift](#swift)
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

Swift is Apple's statically typed, compiled language for building software across the iOS, macOS, watchOS, tvOS, and visionOS platforms. Introduced in 2014 as a modern replacement for Objective-C, it combines strong static typing and memory safety with an approachable syntax. For an architect, Swift is a useful case study in null-safety enforced by the type system and in reference counting as an alternative to tracing garbage collection.

---

## Overview

Swift was designed by Apple to address long-standing pain points in Objective-C — unsafe pointers, verbose syntax, and a lack of compile-time nil-safety — while remaining fully interoperable with existing Objective-C codebases and frameworks. Since 2015 it has also been open source, with an official (though platform-limited) presence on Linux and Windows.

The language leans heavily on the type system to eliminate whole classes of runtime crashes before code ships, and on value types (structs, enums) as the default building block, reserving reference types (classes) for cases that genuinely need shared, mutable identity.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

Swift is statically typed and multi-paradigm, combining object-oriented and functional elements, with a design philosophy of its own worth calling out:

- ### Optionals (Null-Safety):
  Every type in Swift is non-nullable by default. A value that may be absent must be explicitly declared as an **optional** (`Type?`), and the compiler forces the developer to unwrap it — via `if let`, `guard let`, or optional chaining — before using it. This surfaces "might be nil" bugs at compile time instead of at runtime, the same underlying idea as Kotlin's null-safety.

  ```swift
  var name: String? = nil       // explicitly optional
  if let unwrapped = name {
      print(unwrapped)          // only reachable when name is non-nil
  }
  ```

  > See also: [Kotlin](kotlin.md)

<sub>[Back to top](#table-of-contents)</sub>

- ### Protocol-Oriented Programming:
  Swift favors composing behavior through **protocols** (similar to interfaces) and protocol extensions over deep class inheritance hierarchies. A type can conform to many small protocols, and default implementations supplied via extensions let shared behavior be reused without a common base class — Apple has explicitly promoted this as "protocol-oriented programming" as an alternative to classic OOP inheritance.

  ```swift
  protocol Greetable {
      var name: String { get }
  }
  extension Greetable {
      func greet() -> String { "Hello, \(name)!" }
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

Swift is the primary language for native iOS, iPadOS, macOS, watchOS, and tvOS applications built with UIKit or SwiftUI. It is also used for server-side development (via frameworks like Vapor) and command-line tooling, though its adoption outside Apple's ecosystem remains limited compared to inside it.

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

Swift compiles ahead-of-time to native machine code via LLVM, producing standalone binaries with no interpreter or virtual machine required at run time. Memory management is handled through **Automatic Reference Counting (ARC)**: the compiler inserts retain/release calls at compile time based on ownership, deterministically freeing an object the instant its reference count reaches zero.

This contrasts with tracing garbage collectors (used by the JVM, .NET CLR, or Go's runtime), which periodically scan for unreachable objects. ARC gives more predictable, immediate deallocation and avoids GC pause times, but it requires the developer to explicitly break reference cycles (typically with `weak` or `unowned` references) since ARC alone cannot detect cycles the way a tracing collector can.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

Swift projects are built and dependency-managed with **Swift Package Manager (SwiftPM)**, integrated into **Xcode**, Apple's primary IDE. CocoaPods and Carthage remain in use in older codebases. The language and toolchain are open source and maintained via the Swift Evolution process, which governs how new language features are proposed and adopted.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How is ARC different from garbage collection, and what's the trade-off?**
A: ARC deallocates an object deterministically the moment its reference count hits zero, giving predictable memory release and no GC pause times. The trade-off is that ARC cannot detect reference cycles on its own — two objects strongly referencing each other will leak unless the developer breaks the cycle with a `weak` or `unowned` reference, which a tracing garbage collector handles automatically.

---

**Q: Why does Swift make optionals a core language feature instead of just documenting "this might be null"?**
A: Baking optionality into the type system means the compiler — not runtime testing or discipline — guarantees that a non-optional value is never nil. This eliminates an entire class of null-pointer crashes at compile time, the same motivation behind Kotlin's null-safety.

---

**Q: What does "protocol-oriented programming" actually buy you over classic class inheritance?**
A: It avoids the fragile, deep inheritance hierarchies that classic OOP can produce. Types compose behavior from multiple small protocols (with default implementations via extensions) instead of inheriting from one rigid superclass, which tends to produce more flexible, reusable code.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Kotlin](kotlin.md) — another modern, statically typed language solving null-safety at the type-system level
- [Object Oriented Programming](../paradigms/object-oriented.md) — the paradigm protocol-oriented programming is offered as an alternative to
- [TypeScript](typescript.md) — static typing applied to JavaScript rather than a native compiled language

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [The Swift Programming Language](https://docs.swift.org/swift-book/) — official language guide
- [Swift.org](https://www.swift.org/) — open-source project home and Swift Evolution process

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
