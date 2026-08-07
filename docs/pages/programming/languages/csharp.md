# C#

---

## Table of Contents
<!-- TOC -->
* [C#](#c)
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

C# is a statically typed, multi-paradigm language created by Microsoft in 2000 as the flagship language for the .NET platform. It combines strong object-oriented foundations with a growing set of functional-style features, and runs on a managed, garbage-collected runtime comparable in spirit to the JVM. Once tied to Windows, modern C# and .NET are fully cross-platform, making it a common choice for enterprise backends, cloud services, and desktop/game development.

---

## Overview

C# was designed by Microsoft alongside the .NET Framework as a modern, type-safe alternative to C++ for Windows development, drawing heavy influence from Java and later evolving well past it in language features (LINQ, `async`/`await`, records, pattern matching, nullable reference types). Its evolution from .NET Framework (Windows-only) to .NET Core and now unified .NET (5+) reflects a deliberate shift toward cross-platform, open-source development.

Today C# is the primary language for building web APIs with ASP.NET Core, enterprise line-of-business systems, cloud-native services on Azure, and games via the Unity engine. It remains closely associated with Microsoft tooling (Visual Studio) but is fully usable from cross-platform editors and CLI tooling.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

C# is primarily object-oriented — everything from a `class` to an `enum` participates in a unified type system — but it has steadily absorbed functional programming features.

- ### Object-Oriented Core:
  Classes, interfaces, inheritance, and polymorphism form the backbone of C# design, much like Java. Properties, events, and delegates add idiomatic constructs beyond what Java offers natively.

- ### Functional-Style Features:
  LINQ (Language Integrated Query) lets developers query collections declaratively using lambda expressions and higher-order functions such as `Select`, `Where`, and `Aggregate`. `async`/`await` and records (immutable data types) further push C# toward functional idioms without abandoning its OOP roots.

  ```csharp
  var activeAdults = people
      .Where(p => p.Age >= 18 && p.IsActive)
      .Select(p => p.Name)
      .ToList();
  ```

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

- Enterprise web APIs and backends with ASP.NET Core
- Cloud-native microservices, especially on Microsoft Azure
- Desktop applications (WPF, WinForms, .NET MAUI for cross-platform)
- Game development via the Unity engine
- Internal line-of-business and enterprise systems, historically Windows-centric

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

C# compiles to Common Intermediate Language (CIL), which the Common Language Runtime (CLR) executes with JIT compilation — architecturally similar to Java's bytecode-and-JVM model, including automatic garbage collection. Historically the CLR was Windows-only (.NET Framework), but .NET Core and .NET 5+ unified the platform into a single, open-source, cross-platform runtime supporting Windows, Linux, and macOS.

Deployment options include framework-dependent (requires the .NET runtime installed) and self-contained or Native AOT (ahead-of-time compiled) builds that bundle or eliminate the runtime dependency entirely, trading larger artifact size for simpler deployment.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

NuGet is the standard package manager, and the `dotnet` CLI handles building, testing, and publishing across platforms. Visual Studio remains the flagship IDE, with Visual Studio Code and JetBrains Rider as popular cross-platform alternatives.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How does C#'s garbage collector compare to Java's?**
A: Both use generational, managed garbage collection and free developers from manual memory management. The CLR's GC and the JVM's GC differ in tuning knobs and default algorithms, but architecturally they solve the same problem the same way — neither language exposes raw pointers or manual `free()` by default.

---

**Q: Is C# still a Windows-only language?**
A: No. Since .NET Core (and its successor, unified .NET 5+), C# runs natively on Linux and macOS as well as Windows. The Windows-centric reputation is a holdover from the .NET Framework era and no longer reflects the platform's reality.

---

**Q: When would I choose C# over Java for a new backend service?**
A: Both are strong, mature choices for enterprise backends. C# tends to win when the organization is already invested in Azure and Microsoft tooling, or wants first-class LINQ-style querying and modern language ergonomics (records, pattern matching) baked in earlier than Java historically offered them.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Java](java/java.md) — closest architectural peer: managed runtime, JIT compilation, garbage collection
- [Kotlin](kotlin.md) — another statically typed, multi-paradigm managed-runtime language
- [Functional Programming](../paradigms/functional.md) — the paradigm behind LINQ and C#'s functional-style features

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [C# documentation](https://learn.microsoft.com/en-us/dotnet/csharp/) — official Microsoft C# guide
- [.NET documentation](https://learn.microsoft.com/en-us/dotnet/) — official .NET platform reference

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
