# Reactive Programming

---

## Table of Contents
<!-- TOC -->
* [Reactive Programming](#reactive-programming)
  * [Table of Contents](#table-of-contents)
  * [What's Reactive Programming](#whats-reactive-programming)
  * [Observer Pattern](#observer-pattern)
  * [Asynchronous vs Synchronous](#asynchronous-vs-synchronous)
    * [Thread Management in Synchronous Programming](#thread-management-in-synchronous-programming)
    * [Thread Management in Asynchronous Programming](#thread-management-in-asynchronous-programming)
  * [Example](#example)
  * [Languages](#languages)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---


## What's Reactive Programming


Reactive programming is a programming paradigm that focuses on asynchronous data streams and the propagation of changes. It is often used to handle events and data flow in a more responsive and efficient manner, particularly in user interfaces and systems where events occur frequently.

At the core of reactive programming is the concept of `reactive streams`. A reactive stream represents a `sequence of events or data changes over time`. These events can be anything from user interactions, sensor readings, database updates, or any other asynchronous events. Reactive programming provides tools and abstractions to process, transform, and react to these events in a declarative and composable way.


For example, in an imperative programming setting, `a := b + c` would mean that `a` is being assigned the result of `b + c` in the instant the expression is evaluated, and later, the values of `b` and `c` can be changed with no effect on the value of `a`. On the other hand, in reactive programming, the value of `a` is automatically updated whenever the values of `b` or `c` change, without the program having to explicitly re-execute the statement `a := b + c` to determine the presently assigned value of a.


```javascript
var b = 1
var c = 2
var a = b + c
b = 10
console.log(a) // 3 (not 12 because "=" is not a reactive assignment operator)

```

Now imagine you have a special operator "$=" that changes the value of a variable (executes code on the right side of the operator and assigns result to left side variable) not only when explicitly initialized, but also when referenced variables (on the right side of the operator) are changed

```javascript
var b = 1
var c = 2
var a $= b + c
b = 10
console.log(a) // 12
```

- See also: [Java 9 Reactive Streams](../languages/java/java-9/reactive-streams.md)

<sub>[Back to top](#table-of-contents)</sub>

## Observer Pattern

Reactive programming is a broader paradigm that encompasses the principles of the *Observer Pattern* but extends them to provide more comprehensive and powerful tools for handling asynchronous and event-driven programming.

Reactive programming often employs the *Observer Pattern* as a fundamental building block. In reactive programming, you have a source of data (the `observable`) that emits events or data changes, and you have subscribers (the `observers`) that react to these events. This is analogous to the traditional *Observer Pattern*, where *a subject notifies its observers about state changes*.

- See also: [Observer Pattern](../../../img/observer.png)

> Reactive programming goes beyond the basic Observer Pattern by providing operators and functions for composing, transforming, filtering, and manipulating data streams. 


<sub>[Back to top](#table-of-contents)</sub>


## Asynchronous vs Synchronous

- **Asynchronous**: Reactive programming is well-suited for handling asynchronous operations and real-time data, making it particularly useful for scenarios where events occur frequently and in an unpredictable manner

- **Synchronous**: Imperative programming typically follows a synchronous execution model, where each operation is executed in sequence, waiting for the previous one to complete before moving on to the next.

<sub>[Back to top](#table-of-contents)</sub>


### Thread Management in Synchronous Programming
**Synchronous** programming typically involves a linear flow of execution, where each operation is executed one after the other in a sequential manner. In the context of thread management, this means that a single thread of execution is used to carry out all tasks and operations. If one operation blocks (e.g., waiting for I/O or a resource), the entire thread is blocked, and no other tasks can be performed until the blocking operation completes.

For example, in a synchronous programming model:

```python
print("Starting")
result = perform_synchronous_io()  # Blocking I/O operation
print("Result:", result)
print("Finished")
```

In this case, if `perform_synchronous_io()` blocks, the *entire thread is blocked*, and the program will not proceed until the I/O operation completes.

<sub>[Back to top](#table-of-contents)</sub>


### Thread Management in Asynchronous Programming
**Asynchronous** programming involves the use of multiple threads (or non-blocking event loops) to manage concurrent operations efficiently. In this model, when an operation blocks, the thread can be released to perform other tasks, allowing for better utilization of resources and improved responsiveness.

For example, in an asynchronous programming model:


```python
print("Starting")
async_result = perform_asynchronous_io()  # Non-blocking I/O operation
print("Continuing other work...")
result = await async_result
print("Result:", result)
print("Finished")
```

In this case, when perform_asynchronous_io() is called, the thread is not blocked, and other work can continue. The await keyword is used to pause the current execution until the asynchronous operation is complete.

>Asynchronous programming is particularly useful in scenarios where tasks can be performed concurrently, such as handling multiple network requests or responding to various user interactions in a responsive way.


![img.png](../../../img/reactive-programming.png)

<sub>[Back to top](#table-of-contents)</sub>


## Example

Here's a basic demonstration of reactive programming using JavaScript and the RxJS library, which is a popular library for reactive programming:

```javascript
// Import the RxJS library
import { fromEvent } from 'rxjs';
import { map, filter, debounceTime } from 'rxjs/operators';

// Create an observable from a DOM element's click events
const button = document.getElementById('myButton');
const clickObservable = fromEvent(button, 'click');

// Use operators to transform and filter the stream
const transformedStream = clickObservable.pipe(
    debounceTime(300), // Wait for 300ms pause between clicks
    map(event => event.clientX), // Extract the x-coordinate of the click
    filter(x => x > 100) // Filter out clicks with x-coordinate less than 100
);

// Subscribe to the transformed stream to react to events
transformedStream.subscribe(x => {
    console.log('Clicked at x-coordinate:', x);
});
```

In this example:

- We create an observable from a DOM element's click events using the fromEvent function.

- We use operators like debounceTime, map, and filter to transform and filter the stream of click events.


- We subscribe to the transformed stream using the subscribe method to react to the events.


<sub>[Back to top](#table-of-contents)</sub>


## Languages

Several modern programming languages and frameworks are commonly used for reactive programming due to their strong support for asynchronous and event-driven programming paradigms. Some of the most popular ones include:

- **Java**: Java is widely used for reactive programming, especially with the introduction of the [Reactive Streams API](../../programming/languages/java/java-9/reactive-streams.md) and libraries like [Project Reactor](../languages/java/java-8/project-reactor.md) and **Akka Streams**. Spring Framework also provides support for building reactive applications ([Spring WebFlux]()).

  - See also: [Reactive Programming in Java](../../programming/languages/java/reactive.md)
  - See also: [Java 9 Reactive Streams](../../programming/languages/java/java-9/reactive-streams.md)
  - See also: [Project Reactor](../languages/java/java-8/project-reactor.md)
  - See also: [Spring WebFlux]()


- **JavaScript/TypeScript**: JavaScript is commonly used for building reactive web applications, especially with the rise of front-end libraries and frameworks like **React**, **Angular**, and **Vue.js**. **TypeScript**, a statically typed superset of JavaScript, is also widely adopted for building reactive systems.


- **Scala**: Scala is a language that runs on the **Java Virtual Machine (JVM)** and is known for its strong support of functional and concurrent programming. It is used with Akka, a popular toolkit for building reactive and distributed systems.


- **Kotlin**: Kotlin is another **JVM-based** language that has gained popularity for reactive programming, often used in conjunction with libraries like Project Reactor and Spring WebFlux.


- **Python**: While not traditionally associated with reactive programming, Python has gained support for asynchronous programming with features like the asyncio library, allowing developers to write reactive code.


- **Swift**: Swift, the language used for developing iOS and macOS applications, supports reactive programming with libraries like RxSwift and Combine.


- **C#**: C# has embraced reactive programming with the Reactive Extensions (Rx) library, which provides an elegant way to work with asynchronous data streams.


- **Elixir**: Elixir, a functional programming language built on the Erlang VM, is inherently designed for building highly concurrent, fault-tolerant, and reactive systems.


- **Go**: While not strictly reactive, Go (Golang) provides concurrency primitives like goroutines and channels that can be used to build reactive-like systems.


- **Rust**: Rust is known for its emphasis on safety and performance, and while not specifically designed for reactive programming, it can be used to build systems that manage asynchronous and concurrent operations effectively.


>Reactive programming allows you to easily compose and manipulate data streams while promoting a more responsive and efficient way of handling asynchronous events.


<sub>[Back to top](#table-of-contents)</sub>

___

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How does reactive programming relate to the classic Observer pattern?**
A: Reactive programming uses the Observer pattern as a building block — an `observable` emits data/events and `observers` subscribe and react — but extends it with operators for composing, transforming, filtering, and combining streams (like `map`, `filter`, `debounceTime`), which the plain Observer pattern doesn't provide.

---

**Q: What's the practical benefit of the asynchronous thread model shown in the synchronous-vs-asynchronous example?**
A: In the synchronous example, a blocking I/O call ties up the entire thread until it completes. In the asynchronous example, the thread is released to do other work while the I/O operation is pending, and `await` only pauses that specific execution path — this is what lets reactive systems handle many concurrent operations without a thread per operation.

---

**Q: Where do Reactive Streams, Project Reactor, and Spring WebFlux fit together in the Java ecosystem?**
A: Reactive Streams (Java 9) is the low-level specification defining the interfaces (`Publisher`, `Subscriber`, `Subscription`) for asynchronous stream processing with backpressure. Project Reactor is a concrete implementation of that spec providing `Mono`/`Flux` types. Spring WebFlux builds on Reactor to let you write reactive, non-blocking web applications.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Concurrent Programming](concurrent.md) — reactive systems rely on concurrency models like event loops and non-blocking I/O
- [Functional Programming](functional.md) — reactive stream operators (map, filter, reduce) are built from functional composition
- [Reactive Programming in Java](../languages/java/reactive.md) — how Reactive Streams, Project Reactor, and WebFlux implement these concepts in Java

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.lightbend.com/white-papers-and-reports/reactive-programming-versus-reactive-systems
- https://www.baeldung.com/cs/reactive-programming
- https://en.wikipedia.org/wiki/Reactive_programming

___

[Get Started](../../../get-started.md#paradigms)

---