# Main Java Version Changes

All language features from Java 8 serve as very good Java base knowledge and everything else (Java 9-20) is pretty much additional features on top of that baseline

Visit: [Oracle Java Language Updates](https://docs.oracle.com/en/java/javase/20/language/java-language-changes.html#GUID-6459681C-6881-45D8-B0DB-395D1BD6DB9B)

---

## Table of Contents
<!-- TOC -->
* [Main Java Version Changes](#main-java-version-changes)
  * [Table of Contents](#table-of-contents)
  * [Java 1](#java-1)
  * [Java 1.1](#java-11)
  * [Java 1.2](#java-12)
  * [Java 1.3](#java-13)
  * [Java 1.4](#java-14)
  * [Java 5](#java-5)
  * [Java 6 LTS](#java-6-lts)
  * [Java 7](#java-7)
  * [Java 8 LTS](#java-8-lts)
  * [Java 9](#java-9)
  * [Java 10](#java-10)
  * [Java 11 LTS](#java-11-lts)
  * [Java 12](#java-12-1)
  * [Java 13](#java-13-1)
  * [Java 14](#java-14-1)
  * [Java 15](#java-15)
  * [Java 16](#java-16)
  * [Java 17 LTS](#java-17-lts)
  * [Java 18](#java-18)
  * [Java 19](#java-19)
  * [Java 20](#java-20)
  * [Java 21 LTS](#java-21-lts)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

___

## Java 1
(released in January 1996)

- The first stable version

<sub>[Back to top](#table-of-contents)</sub>

## Java 1.1
(released in February 1997)

- AWT event model
- Inner classes
- JavaBeans
- JDBC
- RMI
- Reflection which supported Introspection only, no modification at runtime was possible.
- JIT (Just In Time) compiler for Windows
- Internationalization and Unicode support originating from Taligent

<sub>[Back to top](#table-of-contents)</sub>

## Java 1.2
(released in December 1998)

- strictfp keyword
- Swing graphical API
- Sun’s JVM was equipped with a JIT compiler for the first time
- Java plug-in
- [Collections API](java-1_2/collections-api.md)
- Reference Types

<sub>[Back to top](#table-of-contents)</sub>

## Java 1.3
(released in May 2000)

- HotSpot JVM
- Java Naming and Directory Interface (JNDI)
- Java Platform Debugger Architecture (JPDA)
- JavaSound
- Synthetic proxy classes

<sub>[Back to top](#table-of-contents)</sub>

## Java 1.4
(released in February 2002)

- assert keyword
- Regular expressions
- Exception chaining
- Internet Protocol version 6 (IPv6) support
- New I/O; NIO
- Logging API
- Image I/O API
- Integrated XML parser and XSLT processor (JAXP)
- Integrated security and cryptography extensions (JCE, JSSE, JAAS)
- Java Web Start
- Preferences API (java.util.prefs)

<sub>[Back to top](#table-of-contents)</sub>

## Java 5
(released in September 2004)

- [Generics](java-5/generics.md)
- [Collections API Major Enhancements](java-5/enhanced-collections.md)
- Annotations
- Autoboxing/unboxing
- Enumerations
- Varargs
- Enhanced for each loop
- Static imports
- New concurrency utilities in java.util.concurrent
- Scanner class for parsing data from various input streams and buffers.

<sub>[Back to top](#table-of-contents)</sub>

## Java 6 LTS
(released in December 2006)

- Scripting Language Support
- Performance improvements
- JAX-WS
- JDBC 4.0
- Java Compiler API
- JAXB 2.0 and StAX parser
- Pluggable annotations
- New GC algorithms

<sub>[Back to top](#table-of-contents)</sub>

## Java 7
(released in July 2011)

 - JVM support for dynamic languages
 - Compressed 64-bit pointers
 - Strings in switch
 - Automatic resource management in try-statement
 - [The Diamond Operator](java-7/diamond-operator.md)
 - [Updated Collections API](java-5/enhanced-collections.md)
 - Simplified varargs method declaration
 - Binary integer literals
 - Underscores in numeric literals
 - Improved exception handling
 - ForkJoin Framework
 - NIO 2.0 having support for multiple file systems, file metadata and symbolic links
 - WatchService
 - Timsort is used to sort collections and arrays of objects instead of merge sort
 - APIs for the graphics features
 - Support for new network protocols, including SCTP and Sockets Direct Protocol

<sub>[Back to top](#table-of-contents)</sub>

## Java 8 LTS
(released in March 2014):

 - [Lambda expression support in APIs](java-8/lamda-expression.md)
 - [Method References](java-8/method-references.md)
 - [Stream API](java-8/stream-api.md)
 - [Functional interface and default methods](java-8/functional-interfaces.md)
 - [Built-in Functional Interfaces](java-8/built-in-functional-interfaces.md)
 - [Optional](java-8/stream-api.md#optional)
 - Nashorn — JavaScript runtime which allows developers to embed JavaScript code within applications
 - Annotation on Java Types
 - Unsigned Integer Arithmetic
 - [Repeating annotations](java-8/repeating-annotations.md)
 - New Date and Time API
 - Statically-linked JNI libraries
 - Launch JavaFX applications from jar files
 - Remove the permanent generation from GC

<sub>[Back to top](#table-of-contents)</sub>

## Java 9
(released in September 2017):

- Modular system (Project Jigsaw)
- JShell (interactive Java shell)
- HTTP/2 client
- [Reactive Streams API (Flow API)](java-9/reactive-streams.md)
- Process API updates
- Private methods in interfaces
- Improved version string scheme 

<sub>[Back to top](#table-of-contents)</sub>

## Java 10
(released in March 2018):

- Local variable type inference (var keyword)
- Application class-data sharing
- Garbage Collector Interface
- Time-based release versioning

<sub>[Back to top](#table-of-contents)</sub>

## Java 11 LTS
(released in September 2018):

- Local variable syntax for lambda parameters
- HTTP Client API (standardized)
- Flight Recorder (profiling and diagnostics)
- Launch Single-File Source-Code Programs
- Epsilon Garbage Collector

<sub>[Back to top](#table-of-contents)</sub>

## Java 12
(released in March 2019):

- Switch expressions (preview feature)
- Compact Number Formatting
- Teeing Collectors
- Shenandoah Garbage Collector (experimental)
- Microbenchmark Suite

<sub>[Back to top](#table-of-contents)</sub>

## Java 13
(released in September 2019):

- Text blocks (preview feature)
- Switch expressions (standard feature)
- Dynamic CDS Archives
- ZGC (experimental garbage collector)
- Reimplement the Legacy Socket API

<sub>[Back to top](#table-of-contents)</sub>

## Java 14
(released in March 2020):

- Pattern matching for instanceof (preview feature)
- Records (preview feature)
- Helpful NullPointerExceptions
- ZGC: Concurrent Thread-Stack Processing
- Non-Volatile Mapped Byte Buffers

<sub>[Back to top](#table-of-contents)</sub>

## Java 15
(released in September 2020):

- Sealed classes (preview feature)
- Hidden classes
- Text blocks (standard feature)
- ZGC: NUMA-Aware Memory Allocation
- Unix-Domain Socket Channels

<sub>[Back to top](#table-of-contents)</sub>

## Java 16
(released in March 2021):

- Records (standard feature)
- Sealed classes (standard feature)
- Pattern matching for instanceof (standard feature)
- Foreign function and memory API (Incubator)
- Alpine Linux Support

<sub>[Back to top](#table-of-contents)</sub>

## Java 17 LTS
(released in September 2021):

- Sealed classes (final form)
- Pattern matching for switch (preview feature)
- Strong encapsulation of JDK internals
- Foreign function and memory API (standard feature)
- Sealed class JARs
- macOS rendering pipeline for AWT and Swing

<sub>[Back to top](#table-of-contents)</sub>

## Java 18
(released in March 2022):

-  UTF-8 by Default
-  Simple Web Server
-  Code Snippets in Java API Documentation
-  Reimplement Core Reflection with Method Handles
-  Vector API (Third Incubator)
-  Internet-Address Resolution SPI
-  Foreign Function & Memory API (Second Incubator)
-  Pattern Matching for switch (Second Preview)
-  Deprecate Finalization for Removal

<sub>[Back to top](#table-of-contents)</sub>

## Java 19
(released in September 2022):

-  Record Patterns (Preview)
-  Linux/RISC-V Port
-  Foreign Function & Memory API (Preview)
-  Virtual Threads (Preview)
-  Vector API (Fourth Incubator)
-  Pattern Matching for switch (Third Preview)
-  Structured Concurrency (Incubator)

<sub>[Back to top](#table-of-contents)</sub>

## Java 20
(released in March 2023):

-  Scoped Values (Incubator)
-  Record Patterns (Second Preview)
-  Pattern Matching for switch (Fourth Preview)
-  Foreign Function & Memory API (Second Preview)
-  Virtual Threads (Second Preview)
-  Structured Concurrency (Second Incubator)
-  Vector API (Fifth Incubator)

<sub>[Back to top](#table-of-contents)</sub>

## Java 21 LTS
(to be released in September 2023):

-  String Templates (Preview)
-  Sequenced Collections
-  Generational ZGC
-  Record Patterns
-  Pattern Matching for switch
-  Foreign Function & Memory API (Third Preview)
-  Unnamed Patterns and Variables (Preview)
-  Virtual Threads
-  Unnamed Classes and Instance Main Methods (Preview)
-  Scoped Values (Preview)
-  Vector API (Sixth Incubator)
-  Deprecate the Windows 32-bit x86 Port for Removal
-  Prepare to Disallow the Dynamic Loading of Agents
-  Key Encapsulation Mechanism API
-  Structured Concurrency (Preview)

<sub>[Back to top](#table-of-contents)</sub>


___

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Which of these versions are actually LTS releases I should prioritize learning?**
A: Java 6, 8, 11, 17, and 21 are marked LTS on this page; Java 8 is the most important baseline since, per the page's intro note, all later versions (9-21) are largely additive on top of it.

---

**Q: What's the single most important release to master before moving to newer Java features?**
A: Java 8 (LTS) — it introduced lambda expressions, the Stream API, functional interfaces, and the new Date/Time API, which subsequent versions build on rather than replace.

---

**Q: When were generics and the diamond operator introduced, and how are they related?**
A: Generics arrived in Java 5 (2004) and required repeating the type argument at both declaration and instantiation; the diamond operator, introduced in Java 7 (2011), removed that redundancy by letting the compiler infer the type from context.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Java](java.md) — language overview that links back into this version history.
- [Generics](java-5/generics.md) — the Java 5 feature detailed on this page.
- [Diamond Operator](java-7/diamond-operator.md) — the Java 7 feature detailed on this page.
- [Stream API](java-8/stream-api.md) — the flagship Java 8 feature this page calls out as the modern baseline.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

 - https://docs.oracle.com/en/java/javase/20/language/java-language-changes.html#GUID-6459681C-6881-45D8-B0DB-395D1BD6DB9B
 - https://medium.com/codex/java-history-from-1-0-to-java-18-d62f69b2a48a
 - https://www.marcobehler.com/guides/a-guide-to-java-versions-and
 - https://ondro.inginea.eu/index.php/new-in-java-versions-since-java-8/
 - https://en.wikipedia.org/wiki/Java_version_history

---

[Get Started](../../../../get-started.md) |
[Languajes](../../../../get-started.md#languages) |
[Java](java.md#how-is-java-versioned)

___