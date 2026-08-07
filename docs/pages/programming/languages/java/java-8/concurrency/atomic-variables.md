# Atomic Variables

![Advanced](https://img.shields.io/badge/Level-Advanced-red)
![Java 5+](https://img.shields.io/badge/Java-5+-blue)
![Est. Time: 20min](https://img.shields.io/badge/Time-20min-green)

---

## Table of Contents
<!-- TOC -->
* [Atomic Variables](#atomic-variables)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Why Atomic Variables](#why-atomic-variables)
  * [Atomic Scalar Classes](#atomic-scalar-classes)
  * [AtomicInteger](#atomicinteger)
  * [AtomicLong](#atomiclong)
  * [AtomicBoolean](#atomicboolean)
  * [AtomicReference](#atomicreference)
  * [Atomic Array Classes](#atomic-array-classes)
  * [Atomic Field Updaters](#atomic-field-updaters)
  * [LongAdder and DoubleAdder](#longadder-and-doubleadder)
  * [Compare-and-Swap (CAS)](#compare-and-swap-cas)
  * [Performance Considerations](#performance-considerations)
  * [Best Practices](#best-practices)
  * [Common Pitfalls](#common-pitfalls)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Overview

**Atomic variables** provide **lock-free,** **thread-safe** operations on single variables using **hardware-level atomic instructions**.

**Key Package:** `java.util.concurrent.atomic`

**Main Classes:**
- **AtomicInteger**: Atomic int operations
- **AtomicLong**: Atomic long operations
- **AtomicBoolean**: Atomic boolean operations
- **AtomicReference**: Atomic object reference operations
- **LongAdder/DoubleAdder**: High-performance counters (Java 8+)
- **Atomic Arrays**: AtomicIntegerArray, AtomicLongArray, AtomicReferenceArray
- **Field Updaters**: AtomicIntegerFieldUpdater, etc.

**Advantages:**
- **No locks**: Lock-free, non-blocking
- **Better performance**: Under high contention
- **No deadlock**: No locks to acquire
- **Atomicity**: Read-modify-write operations are atomic
- **Visibility**: Changes visible to all threads

<sub>[Back to top](#table-of-contents)</sub>

---

## Why Atomic Variables

### The Problem: Non-Atomic Operations

```java
// NOT thread-safe!
class Counter {
    private int count = 0;

    public void increment() {
        count++;  // Three operations:
                  // 1. Read count
                  // 2. Add 1
                  // 3. Write count
                  // Race condition possible!
    }
}
```

### Solution 1: synchronized (With Locks)

```java
class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;  // Thread-safe but uses lock
    }
}
```

**Drawbacks:**
- Lock contention under high load
- Thread blocking
- Potential deadlock (if multiple locks)

### Solution 2: AtomicInteger (Lock-Free)

```java
import java.util.concurrent.atomic.AtomicInteger;

class Counter {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();  // Atomic! No locks!
    }
}
```

**Benefits:**
- No locks = no blocking
- Better performance under contention
- Simple API

<sub>[Back to top](#table-of-contents)</sub>

---

## Atomic Scalar Classes

### Common Methods (All Atomic Classes)

| Method | Description |
|--------|-------------|
| `get()` | Returns current value |
| `set(value)` | Sets to given value |
| `lazySet(value)` | Eventually sets to given value |
| `getAndSet(value)` | Atomically sets and returns old value |
| `compareAndSet(expect, update)` | CAS operation |
| `weakCompareAndSet(expect, update)` | Weak CAS (may fail spuriously) |

<sub>[Back to top](#table-of-contents)</sub>

---

## AtomicInteger

Thread-safe integer with atomic operations.

### Basic Operations

```java
import java.util.concurrent.atomic.AtomicInteger;

AtomicInteger counter = new AtomicInteger(0);

// Get and set
int value = counter.get();           // 0
counter.set(10);                      // Set to 10

// Atomic increment/decrement
int newValue = counter.incrementAndGet();  // 11 (++counter)
newValue = counter.getAndIncrement();      // 11 (counter++)
newValue = counter.decrementAndGet();      // 11 (--counter)
newValue = counter.getAndDecrement();      // 11 (counter--)

// Atomic add
newValue = counter.addAndGet(5);     // 16 (counter += 5)
newValue = counter.getAndAdd(5);     // 16 (returns 16, then adds 5)

// Atomic update
int oldValue = counter.getAndSet(100);  // Returns 21, sets to 100
```

### Real-World Example: ID Generator

```java
public class IDGenerator {
    private static final AtomicInteger nextId = new AtomicInteger(1);

    public static int generateID() {
        return nextId.getAndIncrement();  // Thread-safe, unique IDs
    }
}

// Usage from multiple threads - all get unique IDs
int id1 = IDGenerator.generateID();  // 1
int id2 = IDGenerator.generateID();  // 2
int id3 = IDGenerator.generateID();  // 3
```

### updateAndGet() - Custom Updates (Java 8+)

```java
AtomicInteger balance = new AtomicInteger(1000);

// Apply custom update function
int newBalance = balance.updateAndGet(current -> {
    if (current >= 100) {
        return current - 100;  // Withdraw if sufficient funds
    }
    return current;  // Otherwise, no change
});

// Or use getAndUpdate()
int oldBalance = balance.getAndUpdate(current -> current * 2);
```

### accumulateAndGet() - With Argument (Java 8+)

```java
AtomicInteger score = new AtomicInteger(100);

// Add bonus points atomically
int newScore = score.accumulateAndGet(50, (current, bonus) -> {
    return current + bonus;
});

System.out.println(newScore);  // 150
```

<sub>[Back to top](#table-of-contents)</sub>

---

## AtomicLong

Same as AtomicInteger but for `long` values.

```java
import java.util.concurrent.atomic.AtomicLong;

AtomicLong counter = new AtomicLong(0L);

counter.incrementAndGet();
counter.addAndGet(1000L);
long value = counter.get();
```

**Use Case:** High-frequency counters, timestamps, large numbers.

<sub>[Back to top](#table-of-contents)</sub>

---

## AtomicBoolean

Thread-safe boolean with atomic operations.

```java
import java.util.concurrent.atomic.AtomicBoolean;

AtomicBoolean initialized = new AtomicBoolean(false);

// Atomic test-and-set
if (initialized.compareAndSet(false, true)) {
    // Only one thread will enter here
    performInitialization();
}

// Simple get/set
boolean isInit = initialized.get();
initialized.set(true);
```

### Real-World Example: One-Time Initialization

```java
public class Database {
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static Connection connection;

    public static Connection getConnection() {
        if (initialized.compareAndSet(false, true)) {
            // Only one thread initializes
            connection = createConnection();
        }
        return connection;
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

---

## AtomicReference

Thread-safe reference to an object.

```java
import java.util.concurrent.atomic.AtomicReference;

AtomicReference<String> ref = new AtomicReference<>("Initial");

// Get and set
String value = ref.get();            // "Initial"
ref.set("Updated");

// Atomic swap
String old = ref.getAndSet("New");   // Returns "Updated", sets to "New"

// Compare-and-set
boolean success = ref.compareAndSet("New", "Final");
if (success) {
    System.out.println("Updated to Final");
}
```

### Real-World Example: Immutable Configuration

```java
public class Configuration {
    private static final AtomicReference<Config> current =
        new AtomicReference<>(new Config());

    public static Config getConfig() {
        return current.get();
    }

    public static void updateConfig(Config newConfig) {
        current.set(newConfig);  // Atomic swap
    }

    // Thread-safe update with validation
    public static boolean updateIfValid(Config newConfig) {
        Config oldConfig = current.get();
        if (newConfig.isValid()) {
            return current.compareAndSet(oldConfig, newConfig);
        }
        return false;
    }
}
```

### Stamped Reference (ABA Problem Solution)

```java
import java.util.concurrent.atomic.AtomicStampedReference;

// Pair value with version stamp to detect ABA problem
AtomicStampedReference<String> ref =
    new AtomicStampedReference<>("A", 0);

int[] stampHolder = new int[1];
String value = ref.get(stampHolder);
int stamp = stampHolder[0];

// CAS with stamp check
boolean success = ref.compareAndSet(
    "A",           // expected value
    "B",           // new value
    stamp,         // expected stamp
    stamp + 1      // new stamp
);
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Atomic Array Classes

Atomic operations on array elements.

### AtomicIntegerArray

```java
import java.util.concurrent.atomic.AtomicIntegerArray;

AtomicIntegerArray array = new AtomicIntegerArray(10);

// Atomic operations on individual elements
array.set(0, 100);
int value = array.get(0);

// Atomic increment at index
int newValue = array.incrementAndGet(0);

// Atomic add at index
array.addAndGet(0, 50);

// CAS at index
boolean success = array.compareAndSet(0, 150, 200);
```

### Use Case: Parallel Histogram

```java
public class ParallelHistogram {
    private final AtomicIntegerArray buckets;

    public ParallelHistogram(int numBuckets) {
        buckets = new AtomicIntegerArray(numBuckets);
    }

    public void recordValue(int value) {
        int bucket = value / 10;  // Bucket by tens
        if (bucket < buckets.length()) {
            buckets.incrementAndGet(bucket);  // Thread-safe!
        }
    }

    public int getCount(int bucket) {
        return buckets.get(bucket);
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Atomic Field Updaters

Update fields of existing classes atomically **without changing the class**.

### AtomicIntegerFieldUpdater

```java
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Counter {
    // Must be volatile!
    private volatile int count = 0;

    private static final AtomicIntegerFieldUpdater<Counter> updater =
        AtomicIntegerFieldUpdater.newUpdater(Counter.class, "count");

    public void increment() {
        updater.incrementAndGet(this);
    }

    public int getCount() {
        return count;  // Or updater.get(this)
    }
}
```

**When to use:**
- Can't modify existing class to use AtomicInteger
- Memory efficiency (no extra object per instance)
- Legacy code integration

**Requirements:**
- Field must be `volatile`
- Field must be accessible
- Can't be `static` or `final`

<sub>[Back to top](#table-of-contents)</sub>

---

## LongAdder and DoubleAdder

**High-performance** alternatives to AtomicLong for counters (Java 8+).

### Why LongAdder?

**AtomicLong** under **high contention**:
- Multiple threads compete for the same memory location
- Cache line contention
- Performance degradation

**LongAdder** solution:
- Maintains multiple variables internally
- Each thread updates different variable (less contention)
- Sum all variables when reading

### Basic Usage

```java
import java.util.concurrent.atomic.LongAdder;

LongAdder counter = new LongAdder();

// Increment (very fast, even under high contention)
counter.increment();
counter.add(10);

// Get sum (slower, but rare operation)
long total = counter.sum();

// Reset
counter.reset();
```

### Performance Comparison

```java
// Benchmark: 10 threads, 1 million increments each

// AtomicLong: ~2000ms
AtomicLong atomicCounter = new AtomicLong();
atomicCounter.incrementAndGet();  // Contention!

// LongAdder: ~300ms (6-7x faster!)
LongAdder adder = new LongAdder();
adder.increment();  // Less contention
```

### When to Use Which

| Use Case | AtomicLong | LongAdder |
|----------|-----------|-----------|
| **Low Contention** | ✅ Use this | Overkill |
| **High Contention** | Slow | ✅ Use this |
| **Frequent Reads** | ✅ Fast get() | Slower sum() |
| **Infrequent Reads** | OK | ✅ Use this |
| **Counter/Accumulator** | OK | ✅ Use this |
| **Need exact value constantly** | ✅ Use this | Use AtomicLong |

### DoubleAdder

Same concept for floating-point numbers:

```java
import java.util.concurrent.atomic.DoubleAdder;

DoubleAdder adder = new DoubleAdder();
adder.add(3.14);
adder.add(2.71);
double sum = adder.sum();  // 5.85
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Compare-and-Swap (CAS)

**CAS** is the foundation of all atomic operations.

### How CAS Works

```
compareAndSet(expectedValue, newValue):
    if (currentValue == expectedValue) {
        currentValue = newValue;
        return true;  // Success
    } else {
        return false;  // Failed, value changed
    }
```

**Key Point:** All three steps are **atomic** (hardware instruction).

### CAS Loop Pattern

```java
AtomicInteger counter = new AtomicInteger(0);

public void safeIncrement() {
    int current;
    int next;
    do {
        current = counter.get();
        next = current + 1;
    } while (!counter.compareAndSet(current, next));
    // Loop until CAS succeeds
}
```

**This is what `incrementAndGet()` does internally!**

### Custom CAS Example

```java
public class BankAccount {
    private AtomicInteger balance = new AtomicInteger(1000);

    public boolean withdraw(int amount) {
        int current;
        int newBalance;
        do {
            current = balance.get();
            if (current < amount) {
                return false;  // Insufficient funds
            }
            newBalance = current - amount;
        } while (!balance.compareAndSet(current, newBalance));
        return true;  // Success
    }
}
```

### ABA Problem

**Problem:**
1. Thread 1 reads value A
2. Thread 2 changes A → B → A
3. Thread 1's CAS succeeds (thinks nothing changed!)

**Solution:** Use `AtomicStampedReference` or `AtomicMarkableReference`:

```java
AtomicStampedReference<String> ref =
    new AtomicStampedReference<>("A", 0);

// CAS with version check prevents ABA
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Performance Considerations

### Lock-Free Benefits

```java
// With locks - threads block
synchronized (lock) {
    counter++;  // Other threads wait
}

// Lock-free - threads never block
atomicCounter.incrementAndGet();  // No waiting!
```

**Benefits:**
- No thread blocking
- No context switching
- Better throughput under high contention
- No deadlock possibility

### When Atomic Variables Excel

✅ **Good:**
- Simple read-modify-write operations
- High contention scenarios
- Single-variable updates
- Counter/accumulator patterns

❌ **Poor:**
- Complex state updates (multiple variables)
- Need transactional semantics
- Long computations in CAS loop

### Memory Ordering

Atomic variables provide **volatile semantics**:
- Writes are immediately visible to all threads
- Happens-before relationship established
- No instruction reordering

**See also:** [Java Memory Model](java-memory-model.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Best Practices

### 1. Prefer Atomic Classes Over synchronized

```java
// Instead of:
private int count = 0;
public synchronized void increment() {
    count++;
}

// Use:
private AtomicInteger count = new AtomicInteger();
public void increment() {
    count.incrementAndGet();
}
```

### 2. Use LongAdder for High-Contention Counters

```java
// High-contention scenario
LongAdder requestCount = new LongAdder();

public void handleRequest() {
    requestCount.increment();  // Very fast
    // ... process request
}

public long getRequestCount() {
    return requestCount.sum();  // Called infrequently
}
```

### 3. Keep CAS Loops Short

```java
// BAD: Long computation in CAS loop
do {
    current = atomic.get();
    next = expensiveComputation(current);  // Retries waste CPU!
} while (!atomic.compareAndSet(current, next));

// GOOD: Compute outside loop
int computed = expensiveComputation(initialValue);
do {
    current = atomic.get();
    next = current + computed;  // Simple calculation
} while (!atomic.compareAndSet(current, next));
```

### 4. Use lazySet() for Performance

```java
// set() - full memory barrier (expensive)
atomic.set(value);

// lazySet() - eventual visibility (cheaper)
atomic.lazySet(value);  // Use when immediate visibility not required
```

**Use `lazySet()` when:**
- Value will be read much later
- Publishing to other threads not time-critical
- Example: Cleanup operations

<sub>[Back to top](#table-of-contents)</sub>

---

## Common Pitfalls

### ❌ 1. Assuming Multiple Operations Are Atomic

```java
// WRONG: Two separate atomic operations != atomic together
AtomicInteger counter = new AtomicInteger(0);

public void incrementIfLessThan(int max) {
    if (counter.get() < max) {          // Read
        counter.incrementAndGet();       // Write
    }  // Race condition between get() and incrementAndGet()!
}

// CORRECT: Use CAS loop
public void incrementIfLessThan(int max) {
    int current;
    do {
        current = counter.get();
        if (current >= max) {
            return;
        }
    } while (!counter.compareAndSet(current, current + 1));
}
```

### ❌ 2. Excessive CAS Retries

```java
// BAD: Can livelock under heavy contention
do {
    current = atomic.get();
    next = current + veryExpensiveOperation();
} while (!atomic.compareAndSet(current, next));

// BETTER: Add backoff or limit retries
int retries = 0;
do {
    if (retries++ > 100) {
        // Fall back to synchronized or throw exception
    }
    current = atomic.get();
    next = current + operation();
} while (!atomic.compareAndSet(current, next));
```

### ❌ 3. Using Atomic Variables for Complex State

```java
// WRONG: Multiple atomic variables don't compose
AtomicInteger balance = new AtomicInteger(1000);
AtomicInteger transactionCount = new AtomicInteger(0);

public void withdraw(int amount) {
    balance.addAndGet(-amount);
    transactionCount.incrementAndGet();
    // Race condition! Not atomic together
}

// CORRECT: Use synchronized or AtomicReference with immutable object
class State {
    final int balance;
    final int transactionCount;
    // ... immutable
}
AtomicReference<State> state = new AtomicReference<>(new State(1000, 0));
```

### ❌ 4. Forgetting volatile for Field Updaters

```java
// WRONG: Field must be volatile
private int count = 0;  // Missing volatile!
private static final AtomicIntegerFieldUpdater<Counter> updater = ...;

// CORRECT:
private volatile int count = 0;
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why is `count++` unsafe even though it looks like a single operation?**
A: It's actually three steps — read, add, write — that aren't atomic together. Two threads can interleave between those steps and one increment gets lost.

---

**Q: When should I use `LongAdder` instead of `AtomicLong`?**
A: Under high contention with infrequent reads, such as request counters. `LongAdder` spreads updates across multiple internal cells to reduce cache-line contention, at the cost of a slower `sum()`.

---

**Q: Why does calling `counter.get()` and then `counter.incrementAndGet()` separately still risk a race condition?**
A: Each call is atomic individually, but the combination isn't — another thread can act between the two calls. A CAS loop (`compareAndSet` retried until it succeeds) is needed to make the compound check-then-act operation atomic.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Java Memory Model](java-memory-model.md) — the visibility and ordering guarantees atomic variables rely on
- [Thread Synchronization](synchronization.md) — the lock-based alternative to lock-free atomics
- [Locks and Conditions](locks-and-conditions.md) — explicit locking for cases atomics can't handle

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

**Official Documentation:**
- [java.util.concurrent.atomic Package](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/package-summary.html)
- [AtomicInteger JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html)
- [LongAdder JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/LongAdder.html)

**Books:**
- [Java Concurrency in Practice](https://jcip.net/) - Chapter 15 (Atomic Variables)

**Articles:**
- [Oracle: Atomic Access](https://docs.oracle.com/javase/tutorial/essential/concurrency/atomic.html)
- [Baeldung: Guide to java.util.concurrent.atomic](https://www.baeldung.com/java-atomic-variables)
- [Baeldung: LongAdder and LongAccumulator](https://www.baeldung.com/java-longadder-and-longaccumulator)

**Related Topics:**
- [Java Memory Model](java-memory-model.md) - Memory visibility and ordering
- [Thread Synchronization](synchronization.md) - Lock-based alternatives
- [Locks and Conditions](locks-and-conditions.md) - Explicit locks

---

[Get Started](../../../../../../get-started.md) |
[Java Concurrency](../../concurrency.md) |
[Java 8](../../versions.md#java-8-lts)

---
