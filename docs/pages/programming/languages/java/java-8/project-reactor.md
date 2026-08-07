# Reactor Core

---

## Table of Contents
<!-- TOC -->
* [Reactor Core](#reactor-core)
  * [Table of Contents](#table-of-contents)
  * [Dependencies](#dependencies)
    * [Maven](#maven)
    * [Gradle](#gradle)
    * [Log Dependencies](#log-dependencies)
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
    * [Combining Two Streams](#combining-two-streams)
    * [Dealing with Time](#dealing-with-time)
    * [Choose the Appropriate Operator](#choose-the-appropriate-operator)
  * [Hot Streams](#hot-streams)
    * [Creating a ConnectableFlux](#creating-a-connectableflux)
    * [Throttling](#throttling)
  * [Concurrency](#concurrency)
    * [Example](#example)
  * [Error Handling](#error-handling)
  * [Resource Management](#resource-management)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Reactor is a fourth-generation reactive library, based on the `Reactive Streams` specification, for building non-blocking applications on the JVM

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

### Log Dependencies
We're also adding Logback as a dependency. This is because we'll be logging the output of the Reactor in order to better understand the flow of data. 

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.8</version>
</dependency>
```

```groovy
dependencies {
    implementation 'ch.qos.logback:logback-classic:1.4.8'
}
```


<sub>[Back to top](#table-of-contents)</sub>

## Reactive Data Types
Reactor Core gives us two data types that enable us to produce a stream of data, `Flux` and `Mono`.

In Project Reactor, `Flux` and `Mono` are examples of Publishers. They represent streams of data that emit elements or signals over time.

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
Subscribing to a stream in Project Reactor is quite straightforward. The `subscribe()` method is used to initiate the subscription and start processing the data emitted by the stream

<sub>[Back to top](#table-of-contents)</sub>

### Collecting Elements
```java
        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
        .log()
        .subscribe(elements::add);

        assertThat(elements).containsExactly(1, 2, 3, 4);
```

<sub>[Back to top](#table-of-contents)</sub>

### The Flow of Elements
With logging in place, we can use it to visualize how the data is flowing through our stream:

```bash
20:25:19.550 [main] INFO  reactor.Flux.Array.1 - | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
20:25:19.553 [main] INFO  reactor.Flux.Array.1 - | request(unbounded)
20:25:19.553 [main] INFO  reactor.Flux.Array.1 - | onNext(1)
20:25:19.553 [main] INFO  reactor.Flux.Array.1 - | onNext(2)
20:25:19.553 [main] INFO  reactor.Flux.Array.1 - | onNext(3)
20:25:19.553 [main] INFO  reactor.Flux.Array.1 - | onNext(4)
20:25:19.553 [main] INFO  reactor.Flux.Array.1 - | onComplete()
```

- `onSubscribe()` – This is called when we subscribe to our stream


- `request(unbounded)` – When we call subscribe, behind the scenes we are creating a `Subscription`. This subscription requests elements from the stream. In this case, it defaults to unbounded, meaning it requests every single element available


- `onNext()` – This is called on every single element


- `onComplete()` – This is called last, after receiving the last element. There's actually an onError() as well, which would be called if there is an exception, but in this case, there isn't

This is the flow laid out in the Subscriber interface as part of the Reactive Streams Specification, and in reality, that's what's been instantiated behind the scenes in our call to `onSubscribe()`. It's a useful method, but to better understand what's happening let's provide a Subscriber interface directly:

```java
  List<Integer> elements = new ArrayList<>();

  Flux.just(1, 2, 3, 4)
    .log()
    .subscribe(new Subscriber<Integer>() {
        
        @Override
        public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
                }
        
        @Override
        public void onNext(Integer integer) {
                logger.info("Consuming item: {}", integer);
                elements.add(integer);
                }
        
        @Override
        public void onError(Throwable t) {
                logger.error("An error occurred: {}", t.getCause());
                }
        
        @Override
        public void onComplete() {
                logger.info("Data stream complete");
                }
        });

        logger.info(elements.toString());
```

Output:
```bash
2023-08-17 17:22:03 INFO  reactor.Flux.Array.1 - | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
2023-08-17 17:22:03 INFO  reactor.Flux.Array.1 - | request(unbounded)
2023-08-17 17:22:03 INFO  reactor.Flux.Array.1 - | onNext(1)
2023-08-17 17:22:03 INFO  c.e.r.ReactorDemoApplication - Consuming item:1
2023-08-17 17:22:03 INFO  reactor.Flux.Array.1 - | onNext(2)
2023-08-17 17:22:03 INFO  c.e.r.ReactorDemoApplication - Consuming item:2
2023-08-17 17:22:03 INFO  reactor.Flux.Array.1 - | onNext(3)
2023-08-17 17:22:03 INFO  c.e.r.ReactorDemoApplication - Consuming item:3
2023-08-17 17:22:03 INFO  reactor.Flux.Array.1 - | onNext(4)
2023-08-17 17:22:03 INFO  c.e.r.ReactorDemoApplication - Consuming item:4
2023-08-17 17:22:03 INFO  reactor.Flux.Array.1 - | onComplete()
2023-08-17 17:22:03 INFO  c.e.r.ReactorDemoApplication - Data stream complete
2023-08-17 17:22:03 INFO  c.e.r.ReactorDemoApplication - [1, 2, 3, 4]
```

>We can see that each possible stage in the above flow maps to a method in the Subscriber implementation. It just happens that Flux has provided us with a helper method to reduce this verbosity.

<sub>[Back to top](#table-of-contents)</sub>

### Comparison to Java 8 Streams

It might appear that we have something synonymous to a Java 8 Stream doing collect:

```java
List<Integer> collected = Stream.of(1, 2, 3, 4)
.collect(toList());
```

But we don't.

The core difference is that Reactive is a push model, whereas the Java 8 Streams are a pull model. In a reactive approach, *events are pushed to the subscribers as they come in*.

The next thing to notice is a Streams terminal operator is just that, terminal, pulling all the data and returning a result. With Reactive we could have an infinite stream coming in from an external resource, with multiple subscribers attached and removed on an ad hoc basis. We can also do things like _combine streams_, _throttle streams_, and apply _backpressure_

<sub>[Back to top](#table-of-contents)</sub>

## Backpressure
Backpressure is when a downstream can tell an upstream to send it less data in order to prevent it from being overwhelmed.

Let's tell the upstream to only send two elements at a time by using request()

```java
 List<Integer> elements = new ArrayList<>();

 Flux.just(1, 2, 3, 4)
        .log()
        .subscribe(new Subscriber<Integer>() {
            private Subscription s;
            int onNextAmount;
            
            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                s.request(2);
            }
        
            @Override
            public void onNext(Integer integer) {
                elements.add(integer);
                onNextAmount++;
                if (onNextAmount % 2 == 0) {
                    s.request(2);
                }
            }
        
            @Override
            public void onError(Throwable t) {}
        
            @Override
            public void onComplete() {}
        });
```

Output:
```bash
23:31:15.395 [main] INFO  reactor.Flux.Array.1 - | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
23:31:15.397 [main] INFO  reactor.Flux.Array.1 - | request(2)
23:31:15.397 [main] INFO  reactor.Flux.Array.1 - | onNext(1)
23:31:15.398 [main] INFO  reactor.Flux.Array.1 - | onNext(2)
23:31:15.398 [main] INFO  reactor.Flux.Array.1 - | request(2)
23:31:15.398 [main] INFO  reactor.Flux.Array.1 - | onNext(3)
23:31:15.398 [main] INFO  reactor.Flux.Array.1 - | onNext(4)
23:31:15.398 [main] INFO  reactor.Flux.Array.1 - | request(2)
23:31:15.398 [main] INFO  reactor.Flux.Array.1 - | onComplete()
```

Essentially, this is reactive pull backpressure. We are requesting the upstream to only push a certain amount of elements, and only when we are ready.

<sub>[Back to top](#table-of-contents)</sub>

## Operating on a Stream

We can also perform operations on the data in our stream

- ### map
This is a transformation operator. It takes an input element and applies a function to it, emitting a transformed element as output.

  ```java
import reactor.core.publisher.Flux;

public class MapExample {
  public static void main(String[] args) {
    Flux<Integer> numbers = Flux.range(1, 5);

    Flux<Integer> squaredNumbers = numbers.map(n -> n * n);

    squaredNumbers.subscribe(System.out::println);
  }
}
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### filter
This is a transformation operator. It filters the elements of the input stream based on a predicate and emits only the elements that satisfy the condition.

```java
import reactor.core.publisher.Flux;

public class FilterExample {
  public static void main(String[] args) {
    Flux<Integer> numbers = Flux.range(1, 5);

    Flux<Integer> evenNumbers = numbers.filter(n -> n % 2 == 0);

    evenNumbers.subscribe(System.out::println);
  }
}
```

<sub>[Back to top](#table-of-contents)</sub>

- ### flatMap
This is a transformation operator. It takes an input element and applies a function that returns a stream of output elements. The resulting output elements are then merged into a single output stream.

```java
import reactor.core.publisher.Flux;

public class FlatMapExample {
  public static void main(String[] args) {
    Flux<Integer> numbers = Flux.range(1, 3);

    Flux<Integer> doubledNumbers = numbers.flatMap(n -> Flux.just(n, n * 2));

    doubledNumbers.subscribe(System.out::println);
  }
}
```

<sub>[Back to top](#table-of-contents)</sub>

- ### reduce
This is a transformation operation that aggregates the elements of the input stream using an accumulator function, eventually emitting a single output element.

```java
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReduceExample {
  public static void main(String[] args) {
    Flux<Integer> numbers = Flux.range(1, 5);

    Mono<Integer> sum = numbers.reduce(0, (a, b) -> a + b);

    sum.subscribe(System.out::println);
  }
}
```

<sub>[Back to top](#table-of-contents)</sub>

### Combining Two Streams

- ### zipWith
Allows you to combine elements from two or more streams into pairs or tuples, emitting the combined result

```java
import reactor.core.publisher.Flux;

public class ZipWithExample {
  public static void main(String[] args) {
    Flux<Integer> stream1 = Flux.range(1, 5);
    Flux<Integer> stream2 = Flux.range(6, 5);

    Flux<Tuple2<Integer, Integer>> zippedStream = stream1.zipWith(stream2);

    zippedStream.subscribe(tuple -> System.out.println("Tuple: " + tuple));
  }
}
```

In this example, zipWith combines elements from `stream1` and `stream2` into tuples of two elements. Each tuple contains one element from `stream1` and one element from `stream2`. The resulting tuples are emitted in the `zippedStream`.

The output will be:

```bash
Tuple: (1, 6)
Tuple: (2, 7)
Tuple: (3, 8)
Tuple: (4, 9)
Tuple: (5, 10)
```

<sub>[Back to top](#table-of-contents)</sub>


### Dealing with Time
Project Reactor provides a variety of operators and tools for dealing with time in reactive programming. These operators allow you to work with time-related aspects, such as _delaying emissions_, _setting timeouts_, _scheduling tasks_, and more. 

Common time-related operators and techniques you can use in Reactor:

- #### delayElements
  This operator delays the emission of each element by a specified duration.

  ```java
  import reactor.core.publisher.Flux;
  import java.time.Duration;
  
  public class DelayExample {
  public static void main(String[] args) {
      Flux.range(1, 5)
        .delayElements(Duration.ofSeconds(1))
        .subscribe(System.out::println);
        
      // Wait for a moment to allow emissions to complete
      try {
          Thread.sleep(6000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- #### delay
  The delay operator introduces a delay between the time an element is emitted by the source publisher and the time it reaches the subscriber. It shifts the entire sequence of emissions in time, affecting each emitted element.

  ```java
  import reactor.core.publisher.Flux;
  import java.time.Duration;
  
  public class DelayExample {
  public static void main(String[] args) {
      Flux.range(1, 5)
          .delay(Duration.ofSeconds(2))
          .subscribe(System.out::println);
    
      // Wait for a moment to allow emissions
      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
             e.printStackTrace();
      }
    }
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- #### timeout
  This operator allows you to set a timeout for each element emitted by the stream. If an element takes longer than the specified duration to emit, an error is triggered.

  ```java
  import reactor.core.publisher.Flux;
  import java.time.Duration;
  
  public class TimeoutExample {
  public static void main(String[] args) {
      Flux.range(1, 5)
        .delayElements(Duration.ofSeconds(2))
        .timeout(Duration.ofSeconds(3))
        .subscribe(
          System.out::println,
          error -> System.err.println("Timeout error: " + error)
        );
  
      // Wait for a moment to allow emissions to complete
      try {
          Thread.sleep(7000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  ```


<sub>[Back to top](#table-of-contents)</sub>

- #### interval
  The interval operator creates a `Flux` that emits a sequence of `Long` values representing an increasing clock time at specified intervals. It can be used to simulate periodic emissions or to create a stream of values over time.

- #### take
  This operator  is used to limit the number of elements emitted by a `Flux` or `Mono` stream. It ensures that only the first `n` elements are allowed to pass through the stream, while any subsequent elements are ignored.

  ```java
  import reactor.core.publisher.Flux;
  import java.time.Duration;
  
  public class SchedulingExample {
  public static void main(String[] args) {
      Flux.interval(Duration.ofSeconds(1))
       .take(5)
       .subscribe(System.out::println);
      
      // Wait for a moment to allow emissions to complete
      try {
          Thread.sleep(6000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  ```


- #### timer
  The timer operator creates a `Flux` that emits a single item after a specified delay. It's useful when you want to introduce a delay before emitting a particular element or signal.

  ```java
  import reactor.core.publisher.Mono;
  import java.time.Duration;
  
  public class TimerExample {
  public static void main(String[] args) {
      Mono.timer(Duration.ofSeconds(2))
          .map(value -> "Delayed value")
          .subscribe(System.out::println);
  
      // Wait for a moment to allow emissions
      try {
          Thread.sleep(3000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  ```

- #### delaySubscription
  The delaySubscription operator introduces a delay before subscribing to the source Flux. This can be useful to postpone the start of subscription or to synchronize the subscription with other operations.

  ```java
  import reactor.core.publisher.Flux;
  import java.time.Duration;
  
  public class DelaySubscriptionExample {
  public static void main(String[] args) {
      Flux.range(1, 5)
        .delaySubscription(Duration.ofSeconds(2))
        .subscribe(System.out::println);
  
      // Wait for a moment to allow emissions to complete
      try {
          Thread.sleep(3000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  ```
  
>These are just a few examples of how Reactor enables you to work with time in reactive programming

### Choose the Appropriate Operator

- `ìnterval`: simulates periodic emissions in time
- `delay`: shifts the entire sequence of emissions in time.
- `delayElement`: introduces a delay between individual elements.
- `delaySubscription`: affects the timing of the subscription itself.
- `timer`: emits a value after a specified delay.

<sub>[Back to top](#table-of-contents)</sub>

## Hot Streams
Currently, we've focused primarily on _cold streams_. These are static, fixed-length streams that are easy to deal with. A more realistic use case for reactive might be something that happens infinitely.

These types of streams are called hot streams, as they are always running and can be subscribed to at any point in time, missing the start of the data.


<sub>[Back to top](#table-of-contents)</sub>

### Creating a ConnectableFlux

`ConnectableFlux` is a special type of `Flux` provided by Project Reactor that allows you to control the timing of the subscription to the underlying data source. It is often used in scenarios where you want to share a single source of data among multiple subscribers, but you want to control when the actual data flow starts.

>`ConnectableFlux` doesn't start emitting data to subscribers as soon as you subscribe to it. Instead, it requires an explicit call to the `connect()` method to begin emitting data to all subscribed consumers simultaneously.

Let's create a Flux that lasts forever, outputting the results to the console, which would simulate an infinite stream of data coming from an external resource:

```java
ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
          while(true) {
              fluxSink.next(System.currentTimeMillis());
          }
        })
        .publish();
```

By calling `publish()` we are given a `ConnectableFlux`. This means that calling `subscribe()` won't cause it to start emitting, allowing us to add multiple subscriptions:

```java
publish.subscribe(System.out::println);        
publish.subscribe(System.out::println);
```

If we try running this code, nothing will happen. It's not until we call `connect()`, that the Flux will start emitting:

```java
publish.connect();
```


<sub>[Back to top](#table-of-contents)</sub>

### Throttling
Throttling in reactive programming refers to controlling the rate at which events are emitted or processed

Here are a few throttling operators you can use:

<sub>[Back to top](#table-of-contents)</sub>

- #### sample
  The sample operator emits the most recent value from the source at a regular interval, effectively reducing the rate of emissions to match the specified interval.

  ```java
  import reactor.core.publisher.Flux;
  import java.time.Duration;
  
  public class SampleOperatorExample {
  public static void main(String[] args) {
      Flux.interval(Duration.ofMillis(300))
        .sample(Duration.ofSeconds(1))
        .subscribe(System.out::println);
  
      // Wait for a moment to allow emissions
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  ```

  In this example, the `Flux.interval` emits values every 300 milliseconds, and the `.sample(Duration.ofSeconds(1))` operator samples the most recent value every 1 second. As a result, _only one value is emitted every second_.

<sub>[Back to top](#table-of-contents)</sub>

- #### throttleFirst
  The throttleFirst operator emits the first event within a specified time window and then ignores subsequent events until the window expires.

  ```java
  import reactor.core.publisher.Flux;
  import java.time.Duration;
  
  public class ThrottleFirstExample {
  public static void main(String[] args) {
      Flux.interval(Duration.ofMillis(300))
        .throttleFirst(Duration.ofSeconds(1))
        .subscribe(System.out::println);
  
      // Wait for a moment to allow emissions
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  ```
  In this example, the `Flux.interval` emits values every 300 milliseconds, and the `.throttleFirst(Duration.ofSeconds(1))` operator allows the first event to pass through within a 1-second window, ignoring subsequent events until the window resets.

<sub>[Back to top](#table-of-contents)</sub>


- #### throttleLast
  The throttleLast operator emits the most recent event within a specified time window and then ignores subsequent events until the window expires.

  ```java
  import reactor.core.publisher.Flux;
  import java.time.Duration;
  
  public class ThrottleLastExample {
  public static void main(String[] args) {
      Flux.interval(Duration.ofMillis(300))
        .throttleLast(Duration.ofSeconds(1))
        .subscribe(System.out::println);
  
      // Wait for a moment to allow emissions
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }
  ```
  In this example, the `Flux.interval` emits values every 300 milliseconds, and the `.throttleLast(Duration.ofSeconds(1))` operator emits the most recent value within a 1-second window, ignoring intermediate values until the window resets.

  Both the `sample` operator and the `throttleLast` operator have a similar effect: _they emit the most recent event within a specified time window while ignoring intermediate events_.

  >The key difference between them is in naming and documentation conventions. In Project Reactor, the `sample` operator is generally associated with the concept of sampling the source stream at a regular interval, while the `throttleLast` operator is associated with throttling by only emitting the most recent event within a specified time window.

  However, functionally speaking, they achieve the same result of emitting the most recent event within a time window and ignoring the rest.

> These are just a few examples of how you can use throttling operators in Project Reactor to control the rate of emissions in your reactive streams


<sub>[Back to top](#table-of-contents)</sub>

## Concurrency

All of our above examples have currently run on the main thread. However, we can control which thread our code runs on if we want.

Concurrency management in Project Reactor is achieved through its scheduler system. Reactor provides a scheduler abstraction that allows you to control the execution of reactive streams on different threads and thread pools

Some common implementations include:

- ### Schedulers.immediate()
  This scheduler *executes tasks on the calling thread synchronously*. It is primarily used for testing or situations where you want to ensure that an operation runs immediately.

- ### Schedulers.single()
  This scheduler *uses a single worker thread to execute tasks*. It's useful when you want to ensure that operations are executed sequentially and don't overlap.

- ### Schedulers.parallel()
  This scheduler *provides a pool of worker threads*, suitable for *parallelizing operations*. It's often used when you have CPU-bound or parallelizable tasks.

- ### Schedulers.elastic()
  This scheduler is designed to handle I/O-bound tasks. It *adapts its thread pool size dynamically* based on demand and is optimized for long-running tasks with potentially blocking behavior.

- ### Schedulers.fromExecutorService()
  You can create a custom scheduler by providing your own `ExecutorService`. This allows you to have fine-grained control over thread management.

### Example

```java
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulersExample {
    public static void main(String[] args) throws InterruptedException {
        Flux<Integer> flux = Flux.range(1, 10);

        flux
            .map(value -> {
                System.out.println("Mapping on thread: " + Thread.currentThread().getName());
                return value * 2;
            })
            .subscribeOn(Schedulers.parallel())
            .subscribe(value -> System.out.println("Received value: " + value));

        // Wait for a moment to allow emissions
        Thread.sleep(1000);
    }
}
```

In this example, we're using the `Schedulers.parallel()` scheduler to parallelize the map operation. This means that the map function *will execute on multiple threads* from the parallel scheduler's thread pool. 

The `subscribeOn` operator indicates on which scheduler the whole stream should start executing.

>The choice of scheduler depends on the nature of your operations and the concurrency requirements of your application. Reactor's scheduler system provides you with tools to manage concurrency effectively and efficiently while maintaining the principles of reactive programming.

<sub>[Back to top](#table-of-contents)</sub>

## Error Handling

Error handling is an important aspect of reactive programming, and Project Reactor provides several operators and techniques to handle errors gracefully in your reactive streams. Here's an overview of error handling mechanisms in Reactor:

<sub>[Back to top](#table-of-contents)</sub>


- ### onError and onErrorResume
  These operators handle errors emitted by the source `Flux` or `Mono` by executing a fallback behavior or providing an alternative value.

  ```java
  import reactor.core.publisher.Flux;
  
  public class ErrorHandlingExample {
    public static void main(String[] args) {
      Flux.just(1, 2, 0, 4)
              .map(number -> 10 / number) // This will cause a division by zero error
              .onErrorResume(throwable -> Flux.just(-1)) // Fallback to -1 on error
              .subscribe(
                      value -> System.out.println("Received value: " + value),
                      error -> System.err.println("Error: " + error)
              );
    }
  }
  ```

  In this example, the onErrorResume operator catches the division by zero error and resumes the stream with a fallback value of -1.


<sub>[Back to top](#table-of-contents)</sub>


- ### onErrorReturn
  This operator handles errors by replacing the errored element with a specific value and continues the stream.

  
  ```java
  import reactor.core.publisher.Flux;
  import reactor.core.publisher.Mono;
  
  public class OnErrorResumeWithExample {
    public static void main(String[] args) {
      Flux.just(1, 2, 0, 4)
              .flatMap(number -> divideByNumber(number))
              .onErrorResume(error -> handleDivisionError(error))
              .subscribe(
                      value -> System.out.println("Received value: " + value),
                      error -> System.err.println("Final Error: " + error)
              );
    }
  
    static Mono<Integer> divideByNumber(int number) {
      return Mono.just(10 / number);
    }
  
    static Flux<Integer> handleDivisionError(Throwable error) {
      System.err.println("Handling Error: " + error);
      return Flux.just(-1);
    }
  }
  ```

  - The `divideByNumber` method returns a `Mono` that attempts to perform a division operation.
  - The `handleDivisionError` method returns a Flux that emits a single value of -1 and handles the division error by printing a message.
  In the main method:

  - The `flatMap` operator is used to transform each element of the `Flux` into a `Mono` and then flatten those Mono instances into a single `Flux` stream

  - The `onErrorResumeWith` operator catches any errors emitted by the `divideByNumber` method and resumes with the `handleDivisionError` method, which emits a fallback value of `-1`.
  
  The output will be:

  ```bash
  Received value: 10
  Received value: 5
  Received value: -1
  Handling Error: java.lang.ArithmeticException: / by zero
  ```
  
  As you can see, the division by zero error is caught and handled by the `onErrorResumeWith` operator, which triggers the `handleDivisionError` method to emit a fallback value of `-1`. The subsequent elements are also processed after the error is handled.

<sub>[Back to top](#table-of-contents)</sub>

- ### retry
  The retry operator allows you to retry a failed Flux or Mono a certain number of times before giving up.

  ```java
  import reactor.core.publisher.Flux;
  
  public class RetryExample {
    public static void main(String[] args) {
      Flux.just(1, 2, 0, 4)
              .map(number -> 10 / number) // This will cause a division by zero error
              .retry(2) // Retry twice
              .subscribe(
                      value -> System.out.println("Received value: " + value),
                      error -> System.err.println("Error: " + error)
              );
    }
  }
  ```
  
  In this example, the retry operator attempts to retry the division by zero error twice before giving up.

> These are just a few examples of how you can handle errors in reactive programming using `Project Reactor`. Error handling is crucial to ensure the robustness and stability of your reactive applications.

<sub>[Back to top](#table-of-contents)</sub>

## Resource Management

Resource management is a crucial aspect of reactive programming, as it's important to ensure that resources such as _file handles_, _network connections_, and _database connections_ are properly managed and released when they are no longer needed. 

Project Reactor provides mechanisms to manage resources effectively in a reactive context.

- ### using Operator
  The using operator is used to create a resource and *ensure it's automatically closed or disposed of when it's no longer needed*. It's especially useful when working with resources that need to be explicitly closed, like database connections or I/O streams.

  ```java
  import reactor.core.publisher.Mono;
  import reactor.core.publisher.Flux;
  import java.io.FileReader;
  import java.io.IOException;
  
  public class ResourceManagementExample {
    public static void main(String[] args) {
      Mono.using(
              () -> new FileReader("file.txt"), // Resource creation
              reader -> {
                // Use the resource (in this case, read from the FileReader)
                char[] buffer = new char[1024];
                reader.read(buffer);
                return Flux.just(buffer);
              },
              reader -> {
                try {
                  reader.close(); // Resource cleanup
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
      ).subscribe(
              data -> System.out.println("Received data: " + new String(data)),
              error -> System.err.println("Error: " + error)
      );
    }
  }
  ```

  In this example, the using operator ensures that the `FileReader` resource is properly closed after being used.

  - #### Scopes
    Reactor provides various scopes for controlling resource lifecycle, such as `usingWhen`, `usingWhenMany`,` deferContextual`, and more. These operators are used to manage resources based on the lifecycle of the reactive context.

  - #### Schedulers
    Using the appropriate scheduler for resource-intensive operations can help manage resources effectively. For example, you might use the `Schedulers.elastic()` scheduler for I/O-bound operations that require resource management.

  - #### Custom Resource Management
    Depending on your use case, you might need to implement custom resource management techniques using Project Reactor's operators and mechanisms. This could involve combining operators like `flatMap`, `map`, and `doOnTerminate` to ensure proper resource handling.

>When working with reactive programming and managing resources, it's important to consider the specific characteristics of your resources, the concurrency model, and the operators you use. By leveraging Reactor's built-in resource management features and adopting best practices, you can effectively manage resources in your reactive applications.


<sub>[Back to top](#table-of-contents)</sub>


---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: When should I use `Mono` instead of `Flux` if `Flux` can already represent zero, one, or many elements?**
A: `Mono` communicates intent (at-most-one result), terminates immediately on error rather than continuing to emit, and matches API contracts — like a single database lookup — that expect exactly zero or one result.

---

**Q: What's the practical difference between the `sample` and `throttleLast` operators?**
A: They're functionally equivalent — both emit the most recent value within a time window and ignore the rest. They differ only in naming and the convention (sampling vs. throttling) each is documented under.

---

**Q: Why does the reactive push model behave differently from Java 8 Streams' pull model in practice?**
A: Streams pull all data through a single terminal operation and can't represent infinite or asynchronous sources. Reactor pushes events as they arrive, supports backpressure, and allows multiple subscribers to attach and detach dynamically from a hot source.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Reactive Streams](../java-9/reactive-streams.md) — the specification Project Reactor implements
- [Reactor Core](../java-9/reactor-core.md) — a condensed companion reference for `Flux`/`Mono` basics
- [Streams](stream-api.md) — the pull-based Java 8 Streams API this page contrasts Reactor against
- [CompletableFuture](concurrency/completable-future.md) — another Java 8 async abstraction, useful for single-value async results

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://projectreactor.io/
- https://github.com/reactor/reactor-core
- https://www.baeldung.com/reactor-core

---