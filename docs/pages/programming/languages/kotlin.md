# Kotlin

---

## Table of Contents
<!-- TOC -->
* [Kotlin](#kotlin)
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

Kotlin is a statically typed, JVM-based language created by JetBrains and released in 2011, designed to interoperate seamlessly with [Java](java/java.md) while fixing several of its long-standing pain points. Its standout feature is built-in null-safety, which turns Java's infamous `NullPointerException` from a runtime landmine into a compile-time check. Kotlin is Google's officially preferred language for Android development and, through Kotlin Multiplatform, is expanding beyond the JVM into shared cross-platform codebases.

---

## Overview

Kotlin was built by JetBrains — the makers of IntelliJ IDEA — explicitly to be a more concise, safer alternative to Java without abandoning the JVM ecosystem or Java interoperability. Every Kotlin file compiles to the same JVM bytecode as Java, and Kotlin code can call Java libraries directly (and vice versa), which made adoption incremental and low-risk for existing Java codebases.

Google's endorsement of Kotlin as a first-class, then preferred, language for Android development in 2017 was the major inflection point for adoption. Since then Kotlin has expanded its ambitions with Kotlin Multiplatform (KMP), which lets teams share business logic across Android, iOS, web, and backend targets from a single codebase while keeping platform-specific UI native.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

Kotlin is multi-paradigm, blending object-oriented and functional programming with a strong emphasis on conciseness and safety.

- ### Object-Oriented with Less Ceremony:
  Kotlin supports classes, interfaces, and inheritance like [Java's OOP model](java/oop.md), but eliminates much of Java's boilerplate: data classes auto-generate `equals()`, `hashCode()`, and `toString()`; properties replace manual getter/setter pairs.

- ### Null-Safety:
  Kotlin's type system distinguishes nullable types (`String?`) from non-nullable types (`String`) at compile time. Attempting to use a nullable value without checking or handling it is a compile error, not a runtime surprise — directly addressing the class of bugs Java developers know as `NullPointerException`.

  ```kotlin
  var name: String = "Kotlin"      // cannot be null
  var nickname: String? = null     // can be null

  println(nickname?.length ?: 0)   // safe call + default, no NPE possible
  ```

- ### Functional Features:
  Lambdas, higher-order functions, and immutable-by-default (`val`) variables are idiomatic Kotlin, making functional-style collection processing (`map`, `filter`, `fold`) natural without leaving an OOP-shaped codebase.

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

- Android application development (Google's officially preferred language)
- Backend services on the JVM, often as a more concise alternative to Java (e.g., with Spring)
- Cross-platform shared business logic via Kotlin Multiplatform (Android + iOS + web/backend)
- Gradle build scripts, using Kotlin DSL as an alternative to Groovy

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

Kotlin compiles to JVM bytecode by default, running on the same Java Virtual Machine as Java itself — deployment is identical to a Java application (JAR/WAR, containerized JVM process), with full access to the Java standard library and any existing Java dependencies. Kotlin/Native additionally compiles to native binaries for platforms without a JVM (including iOS, via Kotlin Multiplatform), and Kotlin/JS targets JavaScript environments, but the JVM target remains the primary and most mature deployment path.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

Kotlin uses the same build tooling as Java — Gradle (with an idiomatic Kotlin DSL) or Maven — and pulls from the same Maven Central package ecosystem. IntelliJ IDEA and Android Studio, both from JetBrains' lineage, offer first-class Kotlin support, which is part of why the transition from Java has been low-friction for existing JVM teams.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How does Kotlin's null-safety actually prevent NullPointerExceptions?**
A: By making nullability part of the type itself. A `String` can never hold null, while a `String?` might — the compiler forces you to handle the null case (via a safe call `?.`, the elvis operator `?:`, or an explicit check) before you can use a nullable value. NPEs can still occur at the boundary with Java code or via explicit non-null assertions (`!!`), but they're no longer the default risk of every variable access.

---

**Q: Can Kotlin and Java coexist in the same codebase?**
A: Yes, directly and bidirectionally. Both compile to the same JVM bytecode, so a Kotlin class can extend a Java class, implement a Java interface, or call a Java library with no wrapper layer, and Java code can call Kotlin code the same way. This interop is precisely why teams can migrate incrementally rather than needing a full rewrite.

---

**Q: Is Kotlin only for Android, or is it a general-purpose JVM language?**
A: General-purpose. Android is Kotlin's highest-profile use case because Google made it the preferred language there, but Kotlin is equally viable for backend services, scripting (Gradle Kotlin DSL), and, via Kotlin Multiplatform, for sharing logic across mobile, web, and desktop targets.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Java](java/java.md) — the JVM sibling Kotlin interoperates with directly and was designed to improve upon
- [OOP in Java](java/oop.md) — the object-oriented model Kotlin extends with less boilerplate
- [C#](csharp.md) — another statically typed, multi-paradigm managed-runtime language with a similar OOP-plus-functional profile

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Kotlin Documentation](https://kotlinlang.org/docs/home.html) — official Kotlin language documentation
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) — official Kotlin Multiplatform overview

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
