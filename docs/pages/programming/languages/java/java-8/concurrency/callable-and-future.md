# Callable and Future

![Advanced](https://img.shields.io/badge/Level-Advanced-red)
![Java 8+](https://img.shields.io/badge/Java-8+-blue)
![Est. Time: 12min](https://img.shields.io/badge/Time-12min-green)

---

## Table of Contents
<!-- TOC -->
* [Callable and Future](#callable-and-future)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Callable vs Runnable](#callable-vs-runnable)
  * [Future Interface](#future-interface)
  * [Working with Callable and Future](#working-with-callable-and-future)
  * [Getting Results](#getting-results)
  * [Cancellation and Interruption](#cancellation-and-interruption)
  * [Limitations](#limitations)
  * [Future Execution Flow](#future-execution-flow)
  * [Ref.](#ref)
<!-- TOC -->

---

## Overview

`Callable` and `Future` work together to enable **asynchronous computation with return values**.

**Key Concepts:**
- **Callable**: A task that can return a result and throw checked exceptions
- **Future**: A placeholder for the result of an asynchronous computation
- Submitted to ExecutorService for execution
- Result retrieved via Future.get() (blocking)

This pattern was the foundation for asynchronous programming in Java before CompletableFuture arrived in Java 8.

<sub>[Back to top](#table-of-contents)</sub>

---

## Callable vs Runnable

### Runnable (Java 1.0)

```java
public interface Runnable {
    void run(); // No return value, cannot throw checked exceptions
}
```

**Limitations:**
- Cannot return a value
- Cannot throw checked exceptions
- Result must be communicated through shared state

### Callable (Java 5+)

```java
public interface Callable<V> {
    V call() throws Exception; // Returns value, can throw exceptions
}
```

**Benefits:**
- Returns a result of type V
- Can throw checked exceptions
- Cleaner error handling

### Comparison Example

**With Runnable:**
```java
// Need shared state to capture result
final List<Integer> result = new ArrayList<>();
final List<Exception> errors = new ArrayList<>();

Runnable task = () -> {
    try {
        int value = performCalculation();
        result.add(value);
    } catch (Exception e) {
        errors.add(e);
    }
};
```

**With Callable:**
```java
// Clean return value and exception propagation
Callable<Integer> task = () -> {
    return performCalculation(); // Exception automatically propagated
};
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Future Interface

`Future<V>` represents the result of an asynchronous computation.

**Core Methods:**

```java
public interface Future<V> {
    // Check status
    boolean isDone();
    boolean isCancelled();

    // Get result (blocks until available)
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;

    // Cancel execution
    boolean cancel(boolean mayInterruptIfRunning);
}
```

**Method Descriptions:**
- `get()`: Blocks until result is available
- `get(timeout, unit)`: Blocks with timeout
- `isDone()`: Returns true when completed (success, exception, or cancellation)
- `isCancelled()`: Returns true if cancelled before completion
- `cancel()`: Attempts to cancel execution

<sub>[Back to top](#table-of-contents)</sub>

---

## Working with Callable and Future

### Basic Example

```java
import java.util.concurrent.*;

public class CallableExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Define a Callable task
        Callable<Integer> task = () -> {
            System.out.println("Task started on: " +
                             Thread.currentThread().getName());
            Thread.sleep(2000); // Simulate long computation
            return 42;
        };

        // Submit and get Future
        Future<Integer> future = executor.submit(task);

        System.out.println("Task submitted, doing other work...");

        // Do other work while task executes
        doOtherWork();

        // Get result (blocks if not ready)
        Integer result = future.get();
        System.out.println("Result: " + result);

        executor.shutdown();
    }

    static void doOtherWork() {
        System.out.println("Doing other work on main thread");
    }
}
```

**Output:**
```
Task submitted, doing other work...
Doing other work on main thread
Task started on: pool-1-thread-1
Result: 42
```

### Multiple Callable Tasks

```java
ExecutorService executor = Executors.newFixedThreadPool(3);
List<Callable<String>> tasks = Arrays.asList(
    () -> { Thread.sleep(1000); return "Task 1"; },
    () -> { Thread.sleep(2000); return "Task 2"; },
    () -> { Thread.sleep(1500); return "Task 3"; }
);

// Submit all tasks
List<Future<String>> futures = new ArrayList<>();
for (Callable<String> task : tasks) {
    futures.add(executor.submit(task));
}

// Collect results
for (Future<String> future : futures) {
    System.out.println(future.get()); // Blocks for each
}

executor.shutdown();
```

### Using invokeAll (Bulk Submission)

```java
ExecutorService executor = Executors.newFixedThreadPool(3);

List<Callable<String>> tasks = Arrays.asList(
    () -> { Thread.sleep(1000); return "Task 1"; },
    () -> { Thread.sleep(2000); return "Task 2"; },
    () -> { Thread.sleep(1500); return "Task 3"; }
);

// Submit all tasks and wait for all to complete
List<Future<String>> futures = executor.invokeAll(tasks);

// All tasks are done at this point
for (Future<String> future : futures) {
    System.out.println(future.get()); // Non-blocking, already complete
}

executor.shutdown();
```

### Using invokeAny (First to Complete)

```java
ExecutorService executor = Executors.newFixedThreadPool(3);

List<Callable<String>> tasks = Arrays.asList(
    () -> { Thread.sleep(3000); return "Slow task"; },
    () -> { Thread.sleep(1000); return "Fast task"; },
    () -> { Thread.sleep(2000); return "Medium task"; }
);

// Returns result of first task to complete successfully
String result = executor.invokeAny(tasks);
System.out.println(result); // "Fast task"

executor.shutdown();
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Getting Results

### Blocking with get()

```java
Future<String> future = executor.submit(() -> {
    Thread.sleep(5000);
    return "Result";
});

// Blocks for up to 5 seconds
String result = future.get();
```

### get() with Timeout

```java
Future<String> future = executor.submit(() -> {
    Thread.sleep(5000);
    return "Result";
});

try {
    // Wait max 2 seconds
    String result = future.get(2, TimeUnit.SECONDS);
    System.out.println(result);
} catch (TimeoutException e) {
    System.out.println("Task timed out!");
    future.cancel(true); // Cancel the slow task
}
```

### Polling with isDone()

```java
Future<Integer> future = executor.submit(() -> {
    Thread.sleep(2000);
    return 100;
});

// Non-blocking poll
while (!future.isDone()) {
    System.out.println("Task still running...");
    Thread.sleep(500);
}

Integer result = future.get(); // Won't block, already done
System.out.println("Result: " + result);
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Cancellation and Interruption

### Canceling a Task

```java
Future<String> future = executor.submit(() -> {
    for (int i = 0; i < 10; i++) {
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("Task was interrupted");
            return "Cancelled";
        }
        Thread.sleep(1000);
        System.out.println("Working... " + i);
    }
    return "Completed";
});

// Cancel after 3 seconds
Thread.sleep(3000);
boolean cancelled = future.cancel(true); // true = interrupt if running
System.out.println("Cancelled: " + cancelled);

try {
    future.get(); // Throws CancellationException
} catch (CancellationException e) {
    System.out.println("Task was cancelled");
}
```

**cancel() Parameters:**
- `cancel(false)`: Task won't start if queued, but won't interrupt if running
- `cancel(true)`: Attempts to interrupt running task

### Handling Interruption in Task

```java
Callable<String> task = () -> {
    try {
        for (int i = 0; i < 100; i++) {
            // Check for interruption
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Task interrupted");
            }
            // Do work
            performWork(i);
        }
        return "Success";
    } catch (InterruptedException e) {
        System.out.println("Cleaning up after interruption");
        return "Interrupted";
    }
};
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Limitations

Despite being useful, `Callable` and `Future` have significant limitations that led to CompletableFuture:

### 1. **Blocking API**
```java
// get() blocks the calling thread
String result = future.get(); // Must wait!
```
No way to attach a callback or continue with a non-blocking operation.

### 2. **No Chaining**
```java
// Cannot chain operations
Future<Integer> future1 = executor.submit(() -> 10);
// How to transform result to String? Need manual code:
Integer result = future1.get();
Future<String> future2 = executor.submit(() -> String.valueOf(result));
```

### 3. **No Combining**
```java
// Cannot easily combine results from multiple futures
Future<Integer> f1 = executor.submit(() -> 10);
Future<Integer> f2 = executor.submit(() -> 20);

// Manual combination needed
Integer sum = f1.get() + f2.get(); // Blocks twice
```

### 4. **Limited Exception Handling**
```java
try {
    String result = future.get();
} catch (ExecutionException e) {
    // Wrapped exception, needs unwrapping
    Throwable cause = e.getCause();
}
```

### 5. **No Completion Notification**
No built-in way to be notified when computation completes without polling or blocking.

**Solution:** Use [CompletableFuture](completable-future.md) for advanced asynchronous patterns (Java 8+).

<sub>[Back to top](#table-of-contents)</sub>

---

## Future Execution Flow

```mermaid
sequenceDiagram
    participant Client
    participant Executor
    participant WorkerThread
    participant Future

    Client->>Executor: submit(Callable)
    Executor->>Future: create Future&lt;T&gt;
    Executor-->>Client: return Future
    Note over Client: Can do other work

    Executor->>WorkerThread: assign task
    WorkerThread->>WorkerThread: execute callable.call()

    alt Task completes successfully
        WorkerThread->>Future: set result(value)
        Client->>Future: get()
        Future-->>Client: return value
    else Task throws exception
        WorkerThread->>Future: set exception(e)
        Client->>Future: get()
        Future-->>Client: throw ExecutionException
    else Task is cancelled
        Client->>Future: cancel(true)
        Future->>WorkerThread: interrupt()
        WorkerThread->>Future: set cancelled
        Client->>Future: get()
        Future-->>Client: throw CancellationException
    end
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

**Official Documentation:**
- [Callable JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Callable.html)
- [Future JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html)
- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)

**Guides:**
- [Baeldung: Callable and Future Guide](https://www.baeldung.com/java-future)
- [Java Concurrency in Practice](https://jcip.net/) - Chapter on task execution

**Next Steps:**
- For modern asynchronous programming, see [CompletableFuture](completable-future.md)
- For thread pool management, see [Executors](executors.md)

---

[Get Started](../../../../../../get-started.md) |
[Java Concurrency](../../concurrency.md) |
[Java 8](../../versions.md#java-8-lts)

---
