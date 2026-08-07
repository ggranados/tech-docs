# Concurrency

## Table of Contents
<!-- TOC -->
* [Concurrency](#concurrency)
  * [Table of Contents](#table-of-contents)
  * [Threads:](#threads)
  * [Thread Safety:](#thread-safety)
  * [Executors](#executors)
  * [Concurrency Utilities](#concurrency-utilities)
  * [Callable and Future](#callable-and-future)
  * [CompletableFuture](#completablefuture)
  * [Java Memory Model:](#java-memory-model)
  * [Parallel Streams:](#parallel-streams)
  * [Reactive Programming:](#reactive-programming)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->
---

Concurrency inJava is managed primarily through the use of **threads** and the `java.util.concurrent package`, which provides a framework for working with concurrent code. 

Java's concurrency management has evolved over the years, here are some key aspects of how concurrency is managed in modern Java:

## Threads:

Java supports _multi-threading_, allowing you to create and manage multiple threads of execution within a single Java process.
You can create threads by extending the `Thread` class or implementing the `Runnable` interface.

>The Executor framework and thread pools provide more advanced ways to manage threads.

Java also introduced the `ForkJoinPool` for efficient parallelism in Java 7, which is useful for recursive tasks.

- See also: [Threads](java-8/concurrency/threads.md)
- See also: [Fork/Join Framework](java-8/concurrency/fork-join.md)

## Thread Safety:

Java provides synchronization mechanisms like synchronized blocks and methods, as well as the `volatile` keyword, to ensure thread safety when accessing shared resources.
The `java.util.concurrent` package offers higher-level abstractions like `locks`, `semaphores`, and `barriers` to help manage thread synchronization more effectively.

- See also: [Thread Synchronization](java-8/concurrency/synchronization.md)
- See also: [Locks and Conditions](java-8/concurrency/locks-and-conditions.md)
- See also: [Atomic Variables](java-8/concurrency/atomic-variables.md)


 <sub>[Back to top](#table-of-contents)</sub>
 
## Executors

The `java.util.concurrent.Executors` class provides a factory for creating thread pools, which are useful for managing and reusing threads efficiently. _Executors abstract away the details of thread creation and management_.

>Executors can be used to submit tasks for execution and manage their lifecycles.


- See also: [Executors](java-8/concurrency/executors.md)


<sub>[Back to top](#table-of-contents)</sub>

## Concurrency Utilities

The `java.util.concurrent` package includes various utilities for concurrent programming, such as `ConcurrentHashMap`, `ConcurrentLinkedQueue`, and `CopyOnWriteArrayList`, which are thread-safe data structures.

Other abstractions like `CountDownLatch`, `CyclicBarrier`, `Semaphore`, and `Phaser` help manage synchronization between threads.

- See also: [Concurrency Utilities](java-8/concurrency/concurrency-utilities.md)


<sub>[Back to top](#table-of-contents)</sub>


## Callable and Future

Java provides the `Callable` interface for defining tasks that can return values or throw exceptions. You can submit `Callable` tasks to executors and receive `Future` objects in return.

A `Future` represents a placeholder for the result of a computation, allowing you to retrieve the result asynchronously or cancel the task.


- See also: [Callable and Future](java-8/concurrency/callable-and-future.md)


<sub>[Back to top](#table-of-contents)</sub>

## CompletableFuture

Introduced in Java 8, `CompletableFuture` is a powerful and flexible API for asynchronous and concurrent programming. It allows you to compose asynchronous operations, handle exceptions, and more.

`CompletableFuture` supports callbacks, chaining of operations, and combining multiple asynchronous tasks.


- See also: [CompletableFuture](java-8/concurrency/completable-future.md)


<sub>[Back to top](#table-of-contents)</sub>

## Java Memory Model:

Java's memory model defines how threads interact with memory, ensuring consistency and visibility of data among threads. This includes rules for volatile variables, synchronized blocks, and atomic operations.


- See also: [Java Memory Model](java-8/concurrency/java-memory-model.md)


<sub>[Back to top](#table-of-contents)</sub>


## Parallel Streams:

Java 8 introduced parallel streams as a way to perform parallel processing on collections. You can leverage parallel streams to easily parallelize operations on collections without dealing with low-level threading details.


- See also: [Parallel Streams](java-8/stream-api.md#parallel-streams)


<sub>[Back to top](#table-of-contents)</sub>

## Reactive Programming:

Libraries like **Reactor** and **RxJava** provide reactive programming capabilities in Java for handling asynchronous and event-driven programming.


- See also: [Java Reactive Programming](../../paradigms/reactive.md)
- See also: [Java 9 Reactive Streams](java-9/reactive-streams.md)
- See also: [Reactor](java-9/reactor-core.md)


<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: When should I reach for `CompletableFuture` instead of plain `Callable`/`Future`?**
A: `Callable` submitted to an executor gives you back a `Future` that only supports blocking `get()`; `CompletableFuture` adds composition — chaining, combining multiple async results, and non-blocking callbacks — so prefer it whenever you need to orchestrate several asynchronous steps together.

---

**Q: What's the difference between `synchronized` blocks and the `java.util.concurrent` lock utilities?**
A: `synchronized` uses the JVM's intrinsic lock — simple but all-or-nothing, with no timeout or fairness control; explicit `Lock`/`Semaphore`/`Condition` types from `java.util.concurrent` let you try-lock with a timeout, interrupt a waiting thread, or configure fairness, at the cost of manually releasing the lock.

---

**Q: How do parallel streams relate to the Fork/Join framework mentioned on this page?**
A: Parallel streams split work using the common `ForkJoinPool` under the hood, so the Fork/Join framework is the low-level mechanism that powers `.parallelStream()` without you managing threads directly.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Multithreading and Concurrency](../../paradigms/concurrent.md) — the language-agnostic paradigm this page implements in Java.
- [Threads](java-8/concurrency/threads.md) — detailed coverage of thread creation and lifecycle referenced throughout this page.
- [CompletableFuture](java-8/concurrency/completable-future.md) — deep dive into the async composition API introduced here.
- [Reactive Programming in Java](reactive.md) — the non-blocking, backpressure-aware alternative to the thread-based concurrency covered on this page.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.
- https://jenkov.com/tutorials/java-concurrency/java-memory-model.html

---


[Get Started](../../../../get-started.md) |
[Languages](../../../../get-started.md#languages) |
[Java Development](develop.md#multithreading-and-concurrency) |
[Java 8](versions.md#java-8-lts) 

---
