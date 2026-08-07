# CompletableFuture

![Advanced](https://img.shields.io/badge/Level-Advanced-red)
![Java 8+](https://img.shields.io/badge/Java-8+-blue)
![Est. Time: 15min](https://img.shields.io/badge/Time-15min-green)

---

## Table of Contents
<!-- TOC -->
* [CompletableFuture](#completablefuture)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Creating CompletableFutures](#creating-completablefutures)
  * [Chaining Operations](#chaining-operations)
  * [Combining Multiple Futures](#combining-multiple-futures)
  * [Exception Handling](#exception-handling)
  * [Real-World Example](#real-world-example)
  * [CompletableFuture vs Future](#completablefuture-vs-future)
  * [Best Practices](#best-practices)
  * [Common Pitfalls](#common-pitfalls)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Overview

`CompletableFuture` is Java 8's powerful API for **asynchronous, non-blocking programming**. It addresses all the limitations of the traditional `Future` interface.

**Key Features:**
- **Non-blocking**: Attach callbacks instead of blocking with get()
- **Chainable**: Compose multiple async operations
- **Combinable**: Merge results from multiple futures
- **Exception handling**: Rich error handling capabilities
- **Manual completion**: Can be completed programmatically

<sub>[Back to top](#table-of-contents)</sub>

---

## Creating CompletableFutures

### supplyAsync (with return value)

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // Runs in ForkJoinPool.commonPool()
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    return "Hello from async task";
});

// Non-blocking - attaches callback
future.thenAccept(result -> System.out.println(result));
```

### runAsync (no return value)

```java
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    System.out.println("Running async task");
    // Do work without return value
});

future.thenRun(() -> System.out.println("Task completed"));
```

### With Custom Executor

```java
ExecutorService executor = Executors.newFixedThreadPool(10);

CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Task result";
}, executor); // Use custom thread pool

// Don't forget to shutdown
executor.shutdown();
```

### Manual Completion

```java
CompletableFuture<String> future = new CompletableFuture<>();

// Complete it later from another thread
new Thread(() -> {
    try {
        Thread.sleep(1000);
        future.complete("Manual result");
    } catch (InterruptedException e) {
        future.completeExceptionally(e);
    }
}).start();

// Attach callback
future.thenAccept(System.out::println);
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Chaining Operations

### thenApply (transform result)

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10)
    .thenApply(x -> x * 2)           // 20
    .thenApply(x -> x + 5)           // 25
    .thenApply(x -> x / 5);          // 5

future.thenAccept(result -> System.out.println(result)); // Output: 5
```

### thenCompose (flatten nested futures)

```java
CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() ->
    fetchUser(userId)
);

// thenCompose avoids CompletableFuture<CompletableFuture<Orders>>
CompletableFuture<Orders> ordersFuture = userFuture
    .thenCompose(user -> fetchOrdersAsync(user.getId()));

ordersFuture.thenAccept(orders -> System.out.println(orders));
```

**Analogy:** `thenApply` is like `map`, `thenCompose` is like `flatMap`.

### thenCombine (combine two independent futures)

```java
CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 20);

CompletableFuture<Integer> combined = future1.thenCombine(future2,
    (result1, result2) -> result1 + result2
);

combined.thenAccept(sum -> System.out.println("Sum: " + sum)); // 30
```

### thenAccept (consume result)

```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenAccept(message -> System.out.println(message))
    .thenRun(() -> System.out.println("Done"));
```

### Async vs Sync Variants

```java
// Synchronous - runs in same thread as previous stage
.thenApply(x -> x * 2)

// Asynchronous - runs in ForkJoinPool.commonPool()
.thenApplyAsync(x -> x * 2)

// Asynchronous with custom executor
.thenApplyAsync(x -> x * 2, customExecutor)
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Combining Multiple Futures

### allOf (wait for all)

```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Task1");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Task2");
CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "Task3");

CompletableFuture<Void> allFutures = CompletableFuture.allOf(
    future1, future2, future3
);

// Wait for all to complete
allFutures.thenRun(() -> {
    // All futures are complete now
    try {
        System.out.println(future1.get());
        System.out.println(future2.get());
        System.out.println(future3.get());
    } catch (Exception e) {
        e.printStackTrace();
    }
});
```

**Collecting Results:**
```java
List<CompletableFuture<String>> futures = Arrays.asList(future1, future2, future3);

CompletableFuture<List<String>> allResults = CompletableFuture.allOf(
    futures.toArray(new CompletableFuture[0])
).thenApply(v ->
    futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList())
);

allResults.thenAccept(results -> System.out.println(results));
```

### anyOf (first to complete)

```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
    sleep(3000);
    return "Slow";
});

CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
    sleep(1000);
    return "Fast";
});

CompletableFuture<Object> firstDone = CompletableFuture.anyOf(future1, future2);

firstDone.thenAccept(result ->
    System.out.println("First result: " + result) // "Fast"
);
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Exception Handling

### exceptionally (handle errors)

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    if (Math.random() > 0.5) {
        throw new RuntimeException("Something went wrong");
    }
    return "Success";
}).exceptionally(ex -> {
    System.err.println("Error: " + ex.getMessage());
    return "Default value";
});

future.thenAccept(System.out::println); // Either "Success" or "Default value"
```

### handle (transform result or error)

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    if (Math.random() > 0.5) {
        throw new RuntimeException("Failed");
    }
    return "Success";
}).handle((result, ex) -> {
    if (ex != null) {
        return "Error occurred: " + ex.getMessage();
    }
    return result;
});
```

### whenComplete (peek at result or exception)

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Result")
    .whenComplete((result, ex) -> {
        if (ex != null) {
            System.err.println("Error: " + ex);
        } else {
            System.out.println("Success: " + result);
        }
        // Does not transform result, just observes
    });
```

### Chained Exception Handling

```java
CompletableFuture.supplyAsync(() -> fetchData())
    .thenApply(data -> processData(data))
    .thenApply(processed -> saveData(processed))
    .exceptionally(ex -> {
        System.err.println("Pipeline failed: " + ex);
        return null;
    })
    .thenAccept(result -> {
        if (result != null) {
            System.out.println("Pipeline succeeded");
        }
    });
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Real-World Example

### Async API Calls

```java
public class UserService {

    public CompletableFuture<User> getUserAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate API call
            return httpClient.get("/users/" + userId);
        });
    }

    public CompletableFuture<Orders> getOrdersAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            return httpClient.get("/orders?userId=" + userId);
        });
    }

    public CompletableFuture<Profile> getProfileAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            return httpClient.get("/profiles/" + userId);
        });
    }

    // Fetch all data in parallel
    public CompletableFuture<Dashboard> getDashboard(String userId) {
        CompletableFuture<User> userFuture = getUserAsync(userId);
        CompletableFuture<Orders> ordersFuture = getOrdersAsync(userId);
        CompletableFuture<Profile> profileFuture = getProfileAsync(userId);

        return CompletableFuture.allOf(userFuture, ordersFuture, profileFuture)
            .thenApply(v -> new Dashboard(
                userFuture.join(),
                ordersFuture.join(),
                profileFuture.join()
            ))
            .exceptionally(ex -> {
                log.error("Dashboard load failed", ex);
                return Dashboard.empty();
            });
    }
}

// Usage
userService.getDashboard("user123")
    .thenAccept(dashboard -> render(dashboard))
    .thenRun(() -> log.info("Dashboard rendered"));
```

<sub>[Back to top](#table-of-contents)</sub>

---

## CompletableFuture vs Future

| Feature | Future | CompletableFuture |
|---------|--------|-------------------|
| **Blocking** | `get()` blocks | Non-blocking with callbacks |
| **Chaining** | ❌ Manual | ✅ `thenApply`, `thenCompose` |
| **Combining** | ❌ Manual | ✅ `thenCombine`, `allOf`, `anyOf` |
| **Exception Handling** | ❌ Try-catch on get() | ✅ `exceptionally`, `handle` |
| **Completion** | ❌ By task only | ✅ Manual `complete()` |
| **Async Execution** | ❌ Via Executor | ✅ Built-in `Async` methods |

**Migration Example:**

**Before (Future):**
```java
ExecutorService executor = Executors.newFixedThreadPool(2);

Future<Integer> future1 = executor.submit(() -> compute1());
Future<Integer> future2 = executor.submit(() -> compute2());

try {
    Integer result1 = future1.get(); // Blocks
    Integer result2 = future2.get(); // Blocks
    Integer sum = result1 + result2;
    System.out.println(sum);
} catch (Exception e) {
    e.printStackTrace();
}
```

**After (CompletableFuture):**
```java
CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> compute1());
CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> compute2());

future1.thenCombine(future2, (r1, r2) -> r1 + r2)
    .thenAccept(System.out::println)
    .exceptionally(ex -> {
        ex.printStackTrace();
        return null;
    });
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Best Practices

### 1. Use Custom Executor for I/O Tasks

```java
// DON'T: Uses ForkJoinPool.commonPool() (shared resource)
CompletableFuture.supplyAsync(() -> blockingDatabaseCall());

// DO: Use dedicated executor for I/O
ExecutorService ioExecutor = Executors.newFixedThreadPool(20);
CompletableFuture.supplyAsync(() -> blockingDatabaseCall(), ioExecutor);
```

### 2. Handle Exceptions Appropriately

```java
// Always handle exceptions in async chains
CompletableFuture.supplyAsync(() -> riskyOperation())
    .exceptionally(ex -> {
        log.error("Operation failed", ex);
        return fallbackValue();
    });
```

### 3. Use join() Instead of get() for Unchecked

```java
// get() throws checked exceptions
try {
    String result = future.get();
} catch (InterruptedException | ExecutionException e) {
    // Handle
}

// join() throws unchecked CompletionException
String result = future.join(); // Cleaner in lambda contexts
```

### 4. Avoid Blocking in Callbacks

```java
// BAD: Blocking in callback
future.thenApply(result -> {
    return future2.get(); // Blocks! Defeats async purpose
});

// GOOD: Compose async operations
future.thenCompose(result -> future2);
```

### 5. Set Timeouts for External Calls

```java
// Java 9+: orTimeout
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> slowAPI())
    .orTimeout(5, TimeUnit.SECONDS)
    .exceptionally(ex -> "Timeout");

// Java 8: Manual timeout
CompletableFuture<String> timeoutFuture = new CompletableFuture<>();
scheduler.schedule(() ->
    timeoutFuture.completeExceptionally(new TimeoutException()),
    5, TimeUnit.SECONDS
);

CompletableFuture.anyOf(apiCallFuture, timeoutFuture);
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Common Pitfalls

### ❌ 1. Forgetting Exception Handling

```java
// BAD: Unhandled exceptions disappear silently
CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error");
});
// Exception is swallowed, no logging!

// GOOD: Always handle
CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error");
}).exceptionally(ex -> {
    log.error("Task failed", ex);
    return null;
});
```

### ❌ 2. Blocking the Common Pool

```java
// BAD: Blocking operation in common pool
CompletableFuture.supplyAsync(() -> {
    Thread.sleep(60000); // Blocks common pool thread!
    return result;
});

// GOOD: Use custom executor for blocking
ExecutorService blockingExecutor = Executors.newCachedThreadPool();
CompletableFuture.supplyAsync(() -> {
    Thread.sleep(60000);
    return result;
}, blockingExecutor);
```

### ❌ 3. Creating Nested CompletableFutures

```java
// BAD: Nested futures
CompletableFuture<CompletableFuture<String>> nested =
    future1.thenApply(result -> future2); // Wrong!

// GOOD: Flatten with thenCompose
CompletableFuture<String> flat =
    future1.thenCompose(result -> future2);
```

### ❌ 4. Not Shutting Down Custom Executors

```java
ExecutorService executor = Executors.newFixedThreadPool(10);
CompletableFuture.supplyAsync(() -> task(), executor);
// Forgot executor.shutdown()! JVM won't exit
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What's the difference between `thenApply` and `thenCompose`?**
A: `thenApply` transforms the result in place, like `map`. `thenCompose` flattens a nested `CompletableFuture` returned by the function, like `flatMap`, avoiding a `CompletableFuture<CompletableFuture<T>>`.

---

**Q: Why should blocking I/O calls use a custom executor instead of the default `ForkJoinPool.commonPool()`?**
A: The common pool is shared JVM-wide, including by parallel streams. Blocking it with slow I/O starves other unrelated tasks that depend on the same pool.

---

**Q: What happens to an exception thrown inside `supplyAsync()` if no `exceptionally`/`handle` is attached?**
A: It's captured on the future but never surfaces — the callback chain simply stops producing further results. It must be explicitly handled to avoid silent failures.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Callable and Future](callable-and-future.md) — the older, blocking API CompletableFuture replaces
- [Executors](executors.md) — thread pools used as custom executors for async stages
- [Project Reactor](../project-reactor.md) — a richer reactive alternative for streams of async values

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

**Official Documentation:**
- [CompletableFuture JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
- [Java 8 Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

**Guides:**
- [Baeldung: CompletableFuture Guide](https://www.baeldung.com/java-completablefuture)
- [DZone: CompletableFuture](https://dzone.com/articles/java-8-completablefuture-in-practice)

**Books:**
- [Java Concurrency in Practice](https://jcip.net/) - Brian Goetz
- [Modern Java in Action](https://www.manning.com/books/modern-java-in-action) - Chapters on async programming

**Related Topics:**
- For basic async: [Callable and Future](callable-and-future.md)
- For thread pools: [Executors](executors.md)
- For reactive streams: [Project Reactor](../project-reactor.md)

---

[Get Started](../../../../../../get-started.md) |
[Java Concurrency](../../concurrency.md) |
[Java 8](../../versions.md#java-8-lts)

---
