# Reactive Programming in Java

---

## Table of Contents
<!-- TOC -->
* [Reactive Programming in Java](#reactive-programming-in-java)
  * [Table of Contents](#table-of-contents)
  * [Reactive Streams Specification](#reactive-streams-specification)
  * [Why Reactive Programming in Java](#why-reactive-programming-in-java)
  * [Concurrency API vs Reactive API](#concurrency-api-vs-reactive-api)
    * [Efficiency and Performance](#efficiency-and-performance)
    * [Asynchronous Calls](#asynchronous-calls)
    * [Reactive version](#reactive-version)
  * [Reactive Libraries for Java](#reactive-libraries-for-java)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Reactive Streams Specification

Is a set of interfaces and rules that define a standard for *asynchronous stream processing with non-blocking backpressure* in Java. It was developed to address challenges in handling asynchronous and potentially unbounded streams of data, ensuring that data producers (*publishers*) don't overwhelm data consumers (*subscribers*) and lead to resource exhaustion.

The specification also defines rules and guidelines to ensure proper behavior and interoperability between different implementations. One of the key aspects of the specification is *backpressure handling*, which ensures that subscribers can signal their demand for items to publishers and prevent data overload.

The *Reactive Streams Specification* doesn't provide a concrete implementation but serves as a *blueprint for building reactive libraries and frameworks*. Libraries like *Project Reactor*, *Akka Streams*, *RxJava*, and others adhere to the Reactive Streams Specification to provide standardized asynchronous stream processing with backpressure support in Java.

By adhering to the *Reactive Streams Specification*, different reactive libraries can interoperate seamlessly, and users can switch between libraries without significant code changes, ensuring a consistent and standardized approach to reactive programming in Java.

- See also: [Java 9 Reactive Streams](java-9/reactive-streams.md)
- See also: [Flow API](java-9/reactive-streams.md#jdk9-javautilconcurrentflow)

<sub>[Back to top](#table-of-contents)</sub>


## Why Reactive Programming in Java

As a Java API developer, delays or multiple concurrent users are usually abstracted away since libraries like Spring MVC handle concurrency, we tend to code as if it were a single request. To do this, we *pay with sequential blocking operations*, and *latent threads*.  

In Java backend development, reactive programming can offer several benefits:

- **Concurrency and Scalability**: Reactive programming enables efficient utilization of system resources by handling multiple asynchronous tasks concurrently. This is especially useful for *handling a large number of incoming requests in a backend system*. Reactive frameworks often use *event loops* and *non-blocking I/O* to achieve high concurrency and scalability.


- **Responsive User Interfaces**: In scenarios where a Java backend is serving a frontend application, reactive programming can help maintain a responsive user interface by *handling asynchronous data updates and interactions without blocking the main UI thread*. This can lead to a smoother user experience.


- **Resource Efficiency**: Reactive programming encourages the efficient use of resources by *minimizing thread creation and context switching*. This can lead to reduced memory consumption and better overall performance.


- **Stream Processing**: Many backend applications deal with streams of data, such as real-time analytics, sensor data, or log processing. Reactive programming provides *constructs for efficiently processing and transforming these data streams*, making it easier to implement complex data processing pipelines.


- **Error Handling and Resilience**: Reactive programming frameworks often come with built-in mechanisms for handling errors and failures in an elegant manner. This can lead to more robust and resilient backend systems, *where failures in one part of the application don't disrupt the entire system*.


- **Composition and Modularity**: Reactive programming promotes a functional programming style that encourages the composition of smaller, reusable components. This can lead to *more modular and maintainable codebases*.


- **Backpressure Handling**: Reactive programming libraries often provide mechanisms for handling backpressure, which occurs when the rate of incoming data exceeds the system's processing capacity. This can *prevent resource exhaustion and degradation of system performance*.


- **Reactive APIs**: Many modern APIs, such as those in microservices architectures, are designed with reactive principles in mind. Using reactive programming in your backend can make it *easier to integrate and interact with such APIs*.


<sub>[Back to top](#table-of-contents)</sub>


## Concurrency API vs Reactive API

Check the following piece of code and try to tell what's the problem with it?

```java
@GetMapping("users/{userId}")
public User getUserDetails(@PathVariable String userId){
    User user = userService.getUser(userId);
    UserPreferences prefs = userPreferences.getPreferences(userId);
    user.setPreferences(prefs);
    return user;
}
```

This REST GET operation implementation straightforward, but there are potential problems and considerations that need to be addressed:

<sub>[Back to top](#table-of-contents)</sub>


### Efficiency and Performance

  In this implementation, two separate database calls are being made: one to retrieve the user details and another to retrieve user preferences. This could lead to performance issues, especially if the `userService.getUser()` and `userPreferences.getPreferences()` methods involve costly database queries or remote service calls. Making multiple calls like *this can significantly impact the response time of the API, leading to slower user experiences*.

<sub>[Back to top](#table-of-contents)</sub>


### Asynchronous Calls

If the data retrieval involves time-consuming operations, consider using asynchronous calls to parallelize the tasks and improve overall response time

Consider now this implementation using `CompletableFuture`

```java
@GetMapping("users/{userId}")
public CompletableFuture<User> getUserDetailsAsync(@PathVariable String userId) {
      CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> userService.getUser(userId));
      CompletableFuture<UserPreferences> prefsFuture = CompletableFuture.supplyAsync(() -> userPreferences.getPreferences(userId));

      CompletableFuture<Void> bothFutures = CompletableFuture.allOf(userFuture ,prefsFuture );

      bothFutures.join();
      User user = userFuture.join();
      UserPreferences prefs = prefsFuture.join();

      user.setPreferences(prefs);
      return user;
}
```

- **CompletableFuture Creation**: Two `CompletableFuture` objects are created: `userFuture` and `prefsFuture`. These are used to perform asynchronous operations for fetching user details and user preferences, respectively. The `supplyAsync()` method is used to initiate these asynchronous tasks.


- **Combining Futures**: The `CompletableFuture.allOf(userFuture, prefsFuture)` method is used to combine both `userFuture` and `prefsFuture` into a single `CompletableFuture<Void>`. This doesn't directly return a combined result, but it can be useful when you want to wait for multiple futures to complete before proceeding.


- **Waiting for Completion**: The `bothFutures.join()` call blocks the current thread until both `userFuture` and `prefsFuture` are completed. This is done to ensure that both asynchronous tasks have finished before moving on.


- **Getting Results**: After waiting for both futures to complete, the `join()` method is used again to retrieve the results from `userFuture` and `prefsFuture`. Since the `join()` method blocks until the future is completed and returns the result or throws an exception, it ensures that you have the values ready.


- **Merging Data**: The retrieved `User` and `UserPreferences` objects are merged by setting the preferences of the user using `user.setPreferences(prefs)`.


- **Returning Result**: The merged User object is then returned from the method

While the provided code does use CompletableFuture for asynchronous operations, there are still aspects that might impact its efficiency and performance:

- **Parallelism vs. Concurrency**: The code uses `CompletableFuture` to execute the two asynchronous tasks, which allows them to potentially run in parallel. However, the performance benefit of parallelism depends on the underlying hardware and the nature of the tasks being performed. *If the tasks are not CPU-bound or if the system does not have sufficient resources, the potential performance gain might not be fully realized*.


- **Blocking Operations**: The `join()` method is used to wait for the completion of both futures and to retrieve their results. This causes *the current thread to block until both tasks are completed*. While other threads can be utilized for other tasks during this blocking period, the overall throughput of the system might still be limited if there is a high degree of blocking.


- **Limited Parallelism**: The *code waits for both futures to complete before merging their results*. This could potentially limit the parallelism and efficient utilization of resources, as the code has to wait for the slowest operation to finish before proceeding.

>Basically Dev has too much to handle, code is meesy, and `bothFutures.join()` is still blocking the main thread  

<!--TODO:- See also: [Miltithreading]()-->


<sub>[Back to top](#table-of-contents)</sub>


### Reactive version

Consider the following code improvement using **Project Reactor**'s reactive programming model

```java
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

// ...

@GetMapping("users/{userId}")
public Mono<User> getUserDetailsAsync(@PathVariable String userId) {
    return userService.get(userId)                        // Fetch user details asynchronously
        .zipWith(userPreferencesService.getPreferences(userId))   // Zip with user preferences
        .map(tuple -> {                                     // Process the zipped result
            User user = tuple.getT1();                     // Extract user from tuple
            UserPreferences prefs = tuple.getT2();          // Extract preferences from tuple
            user.setPreferences(prefs);                     // Set user preferences
            return user;                                    // Return the updated user
        });
}
```


- `userService.get(userId)`: This line asynchronously fetches user details based on the userId using the `userService.get(userId)` method. The result is a `Mono<User>` representing the asynchronous operation of fetching the user details.


- `zipWith(userPreferencesService.getPreferences(userId))`: The `zipWith` operator combines the previously obtained `Mono<User>` with another `Mono<UserPreferences>`, which is fetched asynchronously using the `userPreferencesService.getPreferences(userId)` method. It combines the two Monos into a single `Tuple2<User, UserPreferences>` representing both the user details and preferences.

  
 - `.map(tuple -> { ... })`: This part of the code processes the combined result of user details and preferences, represented as a `Tuple2`. The map operator is used to transform the tuple into a new value, which, in this case, is the updated User object with preferences set.


- `User user = tuple.getT1();`: Here, we extract the `User` object from the tuple using the `getT1()` method. This retrieves the user details fetched earlier.


- `UserPreferences prefs = tuple.getT2();`: Similarly, we extract the `UserPreferences` object from the tuple using the `getT2()` method. This retrieves the user preferences fetched asynchronously.


- `user.setPreferences(prefs);`: The retrieved `UserPreferences` are set on the `User` object, effectively updating the user's preferences.


- `return user;`: The updated User object with the preferences set is returned as the result of the reactive pipeline.

>`Mono<User>` means that the method `getUserDetailsAsync` returns a reactive stream that will eventually emit a single User object

This code uses *Project Reactor*'s reactive programming model to asynchronously fetch user details and preferences, combine the results using the `zipWith` operator, and then update the user's preferences before returning the updated User object. It's a concise and efficient way to handle asynchronous operations and data combination using reactive principles.

*Project Reactor*'s implementation offers several advantages over using CompletableFuture. But related to Efficient Concurrency:

>Reactor's design emphasizes efficient concurrency and resource management, allowing you to *handle large numbers of concurrent tasks with a smaller number of threads, thanks to its non-blocking nature and cooperative multitasking.

See also: [Project Reactor](java-9/reactor-core.md)

<sub>[Back to top](#table-of-contents)</sub>



## Reactive Libraries for Java
Some of the most popular and recommended libraries for reactive programming in Java are:

- **Project Reactor**: This is a foundational library for reactive programming in Java. It provides implementations of the Reactive Streams specification and supports both reactive streams and reactive programming paradigms. It includes two main components: Reactor Core and Reactor Netty.
  - See also: [Project Reactor](java-9/reactor-core.md)


- **RxJava**: RxJava is a reactive programming library that implements the ReactiveX (Rx) API for Java. It provides a wide range of operators for working with asynchronous and event-based programming. RxJava is widely used and has a large community.


- **Akka Streams**: While Akka is primarily known as an actor-based concurrency framework for the Java Virtual Machine (JVM), Akka Streams is a reactive streams library that provides a way to handle and process streams of data asynchronously and reactively.


- **Vert.x**: Vert.x is a toolkit for building reactive applications on the JVM. It supports multiple programming languages, including Java, and provides features for building asynchronous, event-driven applications.


- **Spring WebFlux**: Part of the Spring Framework, Spring WebFlux provides a reactive programming model for building web applications. It's built on top of Project Reactor and offers an alternative to Spring MVC for handling reactive web requests.
  - See also: [Spring WebFlux]()


- **Quasar**: Quasar is a library that provides lightweight threads (fibers) and channels for writing asynchronous and concurrent code in a more sequential style. While not strictly a reactive programming library, it can be used to build reactive systems.

<sub>[Back to top](#table-of-contents)</sub>


_____

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What specific problem does backpressure solve in the Reactive Streams Specification?**
A: It lets a slow subscriber signal how much data it can currently handle, preventing a fast publisher from flooding it and causing memory exhaustion or dropped messages — this is the core guarantee the spec adds on top of plain async callbacks.

---

**Q: Why is the `Mono`-based `getUserDetailsAsync` example considered better than the `CompletableFuture` version on this page?**
A: The `CompletableFuture` version still calls `.join()`, which blocks the calling thread while waiting for both futures; the Reactor version composes the two calls with `zipWith()` and stays non-blocking end-to-end, so the thread is freed to do other work while the pipeline is in flight.

---

**Q: How does the reactive approach differ from the plain `java.util.concurrent` concurrency model covered elsewhere?**
A: `CompletableFuture` (see [Concurrency](concurrency.md)) still requires a blocking call to combine results and offers no backpressure; reactive libraries like Project Reactor stay non-blocking throughout the pipeline and let subscribers control the rate of data they receive.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Reactive Programming](../../paradigms/reactive.md) — the language-agnostic paradigm this page implements in Java.
- [Java 9 Reactive Streams](java-9/reactive-streams.md) — the JDK's built-in Flow API implementing the Reactive Streams Specification discussed here.
- [Project Reactor](java-8/project-reactor.md) — deep dive into the library used in this page's Mono/zipWith example.
- [Concurrency](concurrency.md) — the thread- and `CompletableFuture`-based model this page contrasts reactive programming against.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.youtube.com/playlist?list=PLqq-6Pq4lTTYPR2oH7kgElMYZhJd4vOGI
- http://reactive-streams.org


___

[Get Started](../../../../get-started.md) |
[Paradigms](../../../../get-started.md#paradigms) |
[Reactive Programming](../../paradigms/reactive.md) |
[Java](java.md)

___