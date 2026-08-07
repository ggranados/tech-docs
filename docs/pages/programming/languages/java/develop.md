# Java Development

---

## Table of Contents
<!-- TOC -->
* [Java Development](#java-development)
  * [Table of Contents](#table-of-contents)
  * [Learning Subjects](#learning-subjects)
    * [Java Basics](#java-basics)
    * [Object-Oriented Programming (OOP)](#object-oriented-programming-oop)
    * [Exception Handling](#exception-handling)
    * [Java Standard Library](#java-standard-library)
    * [Generics and Collections](#generics-and-collections)
    * [Lambdas and Functional Interfaces](#lambdas-and-functional-interfaces)
    * [Functional Programming (FP)](#functional-programming--fp-)
    * [Multithreading and Concurrency](#multithreading-and-concurrency)
    * [Reactive Programming](#reactive-programming)
    * [Input/Output (I/O)](#inputoutput-io)
    * [Connecting to databases](#connecting-to-databases)
    * [New Features](#new-features)
    * [Java Tools and Development](#java-tools-and-development)
    * [Java Best Practices](#java-best-practices)
    * [Reflection and dynamic class loading](#reflection-and-dynamic-class-loading)
    * [Java Frameworks and Libraries](#java-frameworks-and-libraries)
    * [Java for Web Development](#java-for-web-development)
    * [Java for Android Development](#java-for-android-development)
    * [Java Security](#java-security)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->


## Learning Subjects

### Java Basics

  - Syntax and data types
  - Variables and operators
  - Control flow statements (if-else, loops)
  - Arrays and collections

<sub>[Back to top](#table-of-contents)</sub>

### Object-Oriented Programming (OOP)

  - [Objects](oop.md#what-is-an-object)
  - [Classes](oop.md#what-is-a-class)
  - [Inheritance](oop.md#what-is-inheritance)
  - [Polymorphism](oop.md#polymorphism)
  - [Encapsulation and data hiding](oop.md#encapsulation-and-data-hiding)
  - [Abstraction and interfaces](oop.md#what-is-an-interface)
  - [Packages and modules](oop.md#what-is-a-package)

<sub>[Back to top](#table-of-contents)</sub>

### Exception Handling

  - Handling and throwing exceptions
  - Using try-catch blocks
  - Checked and unchecked exceptions
  - Custom exceptions

<sub>[Back to top](#table-of-contents)</sub>

### Java Standard Library

  - String manipulation
  - Input/output operations (I/O)
  - File handling
  - Regular expressions
  - Date and time

<sub>[Back to top](#table-of-contents)</sub>

### Generics and Collections

  - [Working with Generics](java-5/generics.md)
  - [Diamond Operator](java-7/diamond-operator.md)
  - [Using Common Collection APIs](java-5/enhanced-collections.md#using-common-collection-apis)
  - [List Interface](java-5/list-interface.md)
  - [Set Interface](java-5/set-interface.md)
  - [Queue and Deque Interfaces](java-5/queue-deque-interfaces.md)
  - [Map interface](java-5/map-interface.md)
  - [Streams](java-8/stream-api.md)
  - [Comparing](java-5/enhanced-collections.md#comparing)
  - [Sorting](java-5/enhanced-collections.md#sorting)
  - [Searching](java-5/enhanced-collections.md#searching-and-other-utilities)
  - [Optionals](java-8/stream-api.md#optional)


<sub>[Back to top](#table-of-contents)</sub>

### Lambdas and Functional Interfaces
  - [Lambda Expression](java-8/lamda-expression.md)
  - [Functional Interfaces](java-8/functional-interfaces.md)
  - [Method References](java-8/method-references.md)
  - [Built in Functional Interfaces](java-8/built-in-functional-interfaces.md)

<sub>[Back to top](#table-of-contents)</sub>

### [Functional Programming (FP)](fp.md)
  - [Pure Functions](fp.md#pure-functions)
  - [Immutability](fp.md#immutability)
  - [Higher-order Functions](fp.md#higher-order-functions)
  - [Function Composition](fp.md#function-composition)
  - [Recursion](fp.md#recursion)
  - [First-Class Functions](fp.md#first-class-functions)
  - [Closures](fp.md#closures)

<sub>[Back to top](#table-of-contents)</sub>

### [Multithreading and Concurrency](concurrency.md)

  - [Creating and managing threads](java-8/concurrency/threads.md)
  - [Thread lifecycle and states](java-8/concurrency/threads.md#thread-lifecycle)
  - [Java Memory Model](java-8/concurrency/java-memory-model.md)
  - [Thread synchronization](java-8/concurrency/synchronization.md)
  - [Locks and Conditions](java-8/concurrency/locks-and-conditions.md)
  - [Inter-thread communication](java-8/concurrency/synchronization.md#inter-thread-communication)
  - [Atomic variables](java-8/concurrency/atomic-variables.md)
  - [Concurrency utilities](java-8/concurrency/concurrency-utilities.md)
  - [Executors and thread pools](java-8/concurrency/executors.md)
  - [Callables and Futures](java-8/concurrency/callable-and-future.md)
  - [CompletableFuture](java-8/concurrency/completable-future.md)
  - [Fork/Join framework](java-8/concurrency/fork-join.md)
  - [Parallel streams](java-8/stream-api.md#parallel-streams)
  - [Common pitfalls and best practices](java-8/concurrency/threads.md#concurrency-issues)

<sub>[Back to top](#table-of-contents)</sub>

### [Reactive Programming](reactive.md)

  - [Reactive Streams](java-9/reactive-streams.md)
  - [Publishers and Subscribers](java-9/reactive-streams.md#core-interfaces)
  - [Operators and Transformations](java-9/reactive-streams.md#operators-and-transformations)
  - [Reactive Libraries for Java](reactive.md#reactive-libraries-for-java)
    - [Project Reactor](java-8/project-reactor.md)
      - [Reactive Data Types](java-8/project-reactor.md#reactive-data-types)
      - [Combining Streams](java-8/project-reactor.md#combining-two-streams)
      - [Dealing with Time](java-8/project-reactor.md#dealing-with-time)
      - [Concurrency](java-8/project-reactor.md#concurrency)
      - [Error Handling](java-8/project-reactor.md#error-handling)
      - [Resource Management](java-8/project-reactor.md#resource-management)

<sub>[Back to top](#table-of-contents)</sub>

### Input/Output (I/O)

  - Reading from and writing to files
  - Byte streams vs. character streams
  - Serialization and deserialization
  - Working with network sockets
  - NIO (non-blocking I/O)
  - Java Database Connectivity (JDBC)

<sub>[Back to top](#table-of-contents)</sub>

### Connecting to databases

  - Executing SQL queries
  - Prepared statements
  - Handling result sets
  - Transactions

<sub>[Back to top](#table-of-contents)</sub>

### New Features

  - Modules
  - Sealed classes and interfaces
  - Pattern matching for instanceof
  - Records (immutable data classes)
  - Switch expressions and yield
  - Text blocks (multi-line strings)

<sub>[Back to top](#table-of-contents)</sub>

### Java Tools and Development

  - Build tools
    - Ant
    - Maven
    - Gradle
  - Integrated Development Environments (IDEs)
  - Debugging and troubleshooting
  - Profiling and optimizing code
  - Unit testing with JUnit

<sub>[Back to top](#table-of-contents)</sub>

### Java Best Practices

  - Writing clean and maintainable code
  - Code organization and naming conventions
  - Error handling and logging
  - Performance optimization techniques
  - Security considerations
  - Advanced Java Topics

<sub>[Back to top](#table-of-contents)</sub>

### Reflection and dynamic class loading

  - Annotations and custom annotations
  - [Repeating Annotations](java-8/repeating-annotations.md)
  - Java Native Interface (JNI)
  - JavaFX for desktop applications
  - Java Servlets and JavaServer Pages (JSP)

<sub>[Back to top](#table-of-contents)</sub>

### Java Frameworks and Libraries

  - Spring Framework
  - Hibernate ORM
  - JavaFX
  - Apache Kafka
  - JUnit and Mockito

<sub>[Back to top](#table-of-contents)</sub>

### Java for Web Development

  - Servlets and JavaServer Pages (JSP)
  - JavaServer Faces (JSF)
  - Java Persistence API (JPA)
  - RESTful web services with JAX-RS
  - Java web frameworks
  - 	Spring MVC
  - 	Spring Boot
  - 	Struts
  - 	Struts2

<sub>[Back to top](#table-of-contents)</sub>

### Java for Android Development

  - Android application structure
  - Activities, Fragments, and Intents
  - UI design with XML layouts
  - Android SDK and API usage
  - Database integration with SQLite

<sub>[Back to top](#table-of-contents)</sub>

### Java Security

  - Cryptography and encryption
  - Secure coding practices
  - Authentication and authorization
  - Secure network communication
  - Java Security API

_____

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What order should I work through these Learning Subjects in?**
A: Start with Java Basics, OOP, and Exception Handling as the language foundation, then move to Generics and Collections before Lambdas/FP, since streams and functional interfaces operate on generic collection types; Multithreading, Concurrency, and Reactive Programming build on all of the above and are best tackled last.

---

**Q: How do the "Generics and Collections" and "Lambdas and Functional Interfaces" subjects relate to each other?**
A: Generics give the Collections Framework compile-time type safety, while Streams (linked under Generics and Collections) consume lambdas and functional interfaces to process those typed collections declaratively — the two subjects are meant to be learned together.

---

**Q: Where do build tools like Maven and Gradle fit into this roadmap?**
A: They're covered under "Java Tools and Development" alongside IDEs, debugging, profiling, and JUnit — treat them as the tooling layer you adopt once you're comfortable writing and testing plain Java code.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Java](java.md) — the language overview this development path expands on.
- [Main Java Version Changes](versions.md) — maps each learning subject to the JDK version that introduced it.
- [OOP in Java](oop.md) — full coverage of the Object-Oriented Programming subject listed here.
- [Multithreading and Concurrency](concurrency.md) — full coverage of the concurrency subject listed here.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.


___

[Get Started](../../../../get-started.md) |
[Languages](../../../../get-started.md#languages)

___