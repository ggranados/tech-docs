# Reactor Core

---

## Table of Contents
<!-- TOC -->
* [Reactor Core](#reactor-core)
  * [Table of Contents](#table-of-contents)
  * [Dependencies](#dependencies)
    * [Maven](#maven)
    * [Gradle](#gradle)
  * [Reactive Data Types](#reactive-data-types)
    * [Flux](#flux)
    * [Mono](#mono)
  * [Producing a Stream of Data](#producing-a-stream-of-data)
    * [Using Flux](#using-flux)
    * [Using Mono](#using-mono)
  * [Why Not Only Flux?](#why-not-only-flux)
  * [Subscribing to a Stream](#subscribing-to-a-stream)
    * [Collecting Elements](#collecting-elements)
    * [The Flow of Elements](#the-flow-of-elements)
    * [Comparison to Java 8 Streams](#comparison-to-java-8-streams)
  * [Backpressure](#backpressure)
  * [Operating on a Stream](#operating-on-a-stream)
    * [Mapping Data in a Stream](#mapping-data-in-a-stream)
    * [Combining Two Streams](#combining-two-streams)
  * [Hot Streams](#hot-streams)
    * [Creating a ConnectableFlux](#creating-a-connectableflux)
    * [Throttling](#throttling)
  * [Concurrency](#concurrency)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Reactor is a fourth-generation reactive library, based on the `Reactive Streams` specification, for building non-blocking applications on the JVM

- See also: [Java 9 Reactive Streams](reactive-streams.md)

>Reactor 3 requires Java 8 or + to run

<sub>[Back to top](#table-of-contents)</sub>


## Dependencies
### Maven
```xml
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-core</artifactId>
    <version>3.5.9</version>
</dependency>
```

<sub>[Back to top](#table-of-contents)</sub>

### Gradle
```groovy
dependencies {
    compile "io.projectreactor:reactor-core:3.5.9"
}
```

<sub>[Back to top](#table-of-contents)</sub>

## Reactive Data Types
Reactive Core gives us two data types that enable us to produce a stream of data, `Flux` and `Mono`.

Both, `Flux` and `Mono` are implementations of the **Reactive Streams** `Publisher` interface. Both classes are compliant with the specification, and we could use this interface in their place:

```java
Publisher<String> just = Mono.just("foo");
```


<sub>[Back to top](#table-of-contents)</sub>


### Flux

- `Flux` is used to represent a stream of *zero to many elements* (0-n).


- It is similar to a collection, but it's designed to handle asynchronous and non-blocking scenarios.


- A `Flux` can emit any number of items over time, including zero items.


- It supports various reactive operations like *transformation*, *filtering*, *mapping*, *combining*, and more.


- Examples of sources for creating a `Flux` include *lists*, *arrays*, *generators*, *intervals*, and more.


<sub>[Back to top](#table-of-contents)</sub>


### Mono
- `Mono` is used to represent a stream of zero or one element.


- It is conceptually similar to an `Optional` in Java, but it's designed for reactive and asynchronous use cases.


- A `Mono` can emit either a single item or no item at all (an empty signal) (0-1).


- It is often used for representing the outcome of an asynchronous operation, such as a database query or a network call.


- Like `Flux`, `Mono` also supports various reactive operations.

<sub>[Back to top](#table-of-contents)</sub>

## Producing a Stream of Data
In order for an application to be reactive, the first thing it must be able to do is to produce a stream of data.

Without this data, we wouldn't have anything to react to

<sub>[Back to top](#table-of-contents)</sub>

### Using Flux

- #### Flux.just()
    You can create a `Flux` that emits a predefined sequence of values using the `Flux.just()` method.
    
    ```java
    import reactor.core.publisher.Flux;
    
    public class FluxDemo {
        public static void main(String[] args) {
            Flux<String> flux = Flux.just("apple", "banana", "cherry", "date");
    
            flux.subscribe(System.out::println);
        }
    }
    ```

<sub>[Back to top](#table-of-contents)</sub>


- #### Flux.fromIterable()
    You can create a `Flux` from an existing iterable, such as a list or a collection.
    
    ```java
    import reactor.core.publisher.Flux;
    import java.util.Arrays;
    import java.util.List;
    
    public class FluxDemo {
        public static void main(String[] args) {
            List<String> fruits = Arrays.asList("apple", "banana", "cherry", "date");
            Flux<String> flux = Flux.fromIterable(fruits);
    
            flux.subscribe(System.out::println);
        }
    }
    ```


<sub>[Back to top](#table-of-contents)</sub>

- #### Flux.generate()
    You can generate a stream of data using a custom stateful generator function.
    
    ```java
    import reactor.core.publisher.Flux;
    
    public class FluxDemo {
        public static void main(String[] args) {
            Flux<Integer> flux = Flux.generate(
                    () -> 0,
                    (state, sink) -> {
                        sink.next(state);
                        if (state == 5) {
                            sink.complete();
                        }
                        return state + 1;
                    }
            );
    
            flux.subscribe(System.out::println);
        }
    }
    ```

  - `Flux.generate(state,generator)`: Creates a Flux using the generate method. The generate method takes two arguments: an *initial state* (provided by a supplier) and a generator function (`BiFunction`). The *generator function generates values and provides them to the sink* while maintaining some state.

  - `() -> 0`: This is the initial state supplier. It returns an initial state of 0.

  - `(state, sink) -> {}`: This is the generator function. It takes the current state and a sink as its arguments. *The sink is used to emit values, signals, and control the flow of the `Flux`*.

  - `sink.next(state)`: Emits the current state value through the sink.

  - `if (state == 5) { sink.complete(); }`: Checks if the current state is equal to 5. If so, it completes the Flux by calling `sink.complete()`. This *ensures that the stream terminates* after emitting the value 5.

  - `return state + 1`: Updates the state for the next iteration by incrementing it.


<sub>[Back to top](#table-of-contents)</sub>


- #### Flux.interval()
    You can create a stream that emits elements at a regular interval.

    ```java
    import reactor.core.publisher.Flux;
    import java.time.Duration;
    
    public class FluxDemo {
        public static void main(String[] args) {
            Flux<Long> flux = Flux.interval(Duration.ofSeconds(1));
    
            flux.subscribe(System.out::println);
    
            // Sleep for a while to allow some emissions
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    ```


<sub>[Back to top](#table-of-contents)</sub>

- #### Flux.range()
    You can create a stream that emits a range of integers.
    
    ```java
    import reactor.core.publisher.Flux;
    
    public class FluxDemo {
        public static void main(String[] args) {
            Flux<Integer> flux = Flux.range(1, 5);
    
            flux.subscribe(System.out::println);
        }
    }
    ```

<sub>[Back to top](#table-of-contents)</sub>

### Using Mono

`Mono` is typically used for producing a stream of zero or one element in **Project Reactor**. While it might not seem as common as using `Flux`, there are still scenarios where you would use `Mono` to represent a single result or outcome.

<sub>[Back to top](#table-of-contents)</sub>

- #### Mono.just()
    You can create a `Mono` that emits a single value using the Mono.just() method.

    ```java
    import reactor.core.publisher.Mono;
    
    public class MonoDemo {
        public static void main(String[] args) {
            Mono<String> mono = Mono.just("Hello, world!");
    
            mono.subscribe(System.out::println);
        }
    }
    ```


<sub>[Back to top](#table-of-contents)</sub>

- #### Mono.empty()
    You can create an empty `Mono` that doesn't emit any value. This is useful for representing the absence of a result.
    
    ```java
    import reactor.core.publisher.Mono;
    
    public class MonoDemo {
    public static void main(String[] args) {
    Mono<Object> emptyMono = Mono.empty();
    
            emptyMono.subscribe(
                value -> System.out.println("Received value: " + value),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Completed")
            );
        }
    }
    ```


<sub>[Back to top](#table-of-contents)</sub>

- #### Mono.defer()
    You can use `Mono.defer()` to create a `Mono` that is generated on each subscription. This can be useful when you want to create a new instance of `Mono` with potentially different behavior for each subscriber.
    
    ```java
    import reactor.core.publisher.Mono;
    
    public class DynamicMonoDemo {
        public static void main(String[] args) {
            // Create a supplier that generates a Mono with a random number
            Mono<Integer> dynamicMono = Mono.defer(() -> Mono.just((int) (Math.random() * 100)));
    
            // Subscribe and print a different random number on each subscription
            dynamicMono.subscribe(value -> System.out.println("Subscriber 1: " + value));
            dynamicMono.subscribe(value -> System.out.println("Subscriber 2: " + value));
        }
    }
    ```

    In the current code there's no practical difference between using `Mono.defer(() -> Mono.just((int) (Math.random() * 100)))` and simply using `Mono.just((int) (Math.random() * 100)))` directly, but there is a subtle but important difference between them:

  - **Using Mono.just() directly**:
    ```java
    Mono<Integer> dynamicMono = Mono.just((int) (Math.random() * 100));
    ```
    In this version, you are creating a single Mono instance that generates a random number between 0 and 100 at the time of creation. This random number is then emitted to all subscribers of this single Mono. Both subscribers will receive the same random value because the Mono emits the value only once when it is created.

  - **Using Mono.defer()**:
    ```java
    Mono<Integer> dynamicMono = Mono.defer(() -> Mono.just((int) (Math.random() * 100)));
    ```
    In this version, you are using Mono.defer() to create a new Mono instance for each subscription. Each time a subscriber subscribes to dynamicMono, the lambda inside Mono.defer() is executed, resulting in a new random value being generated and emitted separately for each subscriber. As a result, each subscriber receives a different random value. 



<sub>[Back to top](#table-of-contents)</sub>
    

- #### Mono.fromSupplier()
    You can create a `Mono` from a supplier function that provides the value to be emitted.
    
    ```java
    import reactor.core.publisher.Mono;
    
    public class MonoDemo {
        public static void main(String[] args) {
            Mono<String> mono = Mono.fromSupplier(() -> "Value from supplier");
    
            mono.subscribe(System.out::println);
        }
    }
    ```

<sub>[Back to top](#table-of-contents)</sub>


- #### Mono.fromCallable()
    You can create a `Mono` from a callable that represents a potentially blocking operation.
    
    ```java
    import reactor.core.publisher.Mono;
    
    public class MonoDemo {
        public static void main(String[] args) {
            Mono<Integer> mono = Mono.fromCallable(() -> {
                // Simulate a potentially blocking operation
                Thread.sleep(1000);
                return 42;
            });
    
            mono.subscribe(System.out::println);
        }
    }
    ```

<sub>[Back to top](#table-of-contents)</sub>

## Why Not Only Flux?

You might wonder why you shouldn't just use Flux for all cases. After all, Flux can represent zero, one, or multiple elements, which seems to cover all possibilities. However, there are reasons to use Mono in certain situations:

- **Semantic Clarity**: Using `Mono` when you expect at most one result or outcome can make your *code more semantically clear*.


- **Error Handling**: *In a `Mono`, errors terminate the stream immediately*, while in a `Flux`, other items can still be emitted after an error.


- **Synchronization and Composition**: In some scenarios, you might need to wait for a single result before proceeding with subsequent operations. Using `Mono` allows for *clear synchronization points* and more straightforward composition in such cases.


- **Resource Management**: When dealing with resources that need explicit management, such as connections or files, a `Mono` can be a better fit. It *ensures that the resource is released promptly after it's no longer needed*.


- **Backpressure Considerations**: If you're working with downstream systems that are sensitive to backpressure (*rate at which data is consumed*), using `Mono` ensures a strict *one-on-one relationship without the complexity of backpressure management* that comes with `Flux`.


- **API Contract**: Some libraries and APIs might expect or return `Mono` instances for certain operations.


>While Flux is more versatile and can handle a wide range of scenarios, using Mono when appropriate can lead to clearer and more maintainable code.

<sub>[Back to top](#table-of-contents)</sub>


## Subscribing to a Stream
### Collecting Elements
### The Flow of Elements
### Comparison to Java 8 Streams

## Backpressure

## Operating on a Stream
### Mapping Data in a Stream
### Combining Two Streams

## Hot Streams

### Creating a ConnectableFlux

### Throttling

## Concurrency

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why does this page define `Mono` separately when `Flux` could represent zero, one, or many elements?**
A: `Mono` adds semantic clarity for at-most-one results, terminates immediately on error rather than continuing to emit, and matches API contracts — like a single database lookup — that explicitly return a single async result.

---

**Q: What's the difference between `Flux.just()` and `Flux.fromIterable()` for creating a stream?**
A: `Flux.just()` takes a fixed varargs list of literal values, while `fromIterable()` wraps an existing `Iterable` (such as a `List`) as its source.

---

**Q: Both `Flux` and `Mono` implement `Publisher` — what does that buy you?**
A: It lets you program against the Reactive Streams `Publisher` interface itself, making code interoperable with any compliant reactive library, not just Reactor's own types.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Reactive Streams](reactive-streams.md) — the specification Reactor Core implements
- [Project Reactor](../java-8/project-reactor.md) — the fuller reference covering operators, backpressure, and error handling
- [Streams](../java-8/stream-api.md) — the pull-based Java 8 Streams API, contrasted with Reactor's push model

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://projectreactor.io/
- https://github.com/reactor/reactor-core
- https://www.baeldung.com/reactor-core