# Concurrency Utilities

![Advanced](https://img.shields.io/badge/Level-Advanced-red)
![Java 5+](https://img.shields.io/badge/Java-5+-blue)
![Est. Time: 25min](https://img.shields.io/badge/Time-25min-green)

---

## Table of Contents
<!-- TOC -->
* [Concurrency Utilities](#concurrency-utilities)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [CountDownLatch](#countdownlatch)
  * [CyclicBarrier](#cyclicbarrier)
  * [Semaphore](#semaphore)
  * [Phaser](#phaser)
  * [Exchanger](#exchanger)
  * [Comparison Summary](#comparison-summary)
  * [Best Practices](#best-practices)
  * [Common Pitfalls](#common-pitfalls)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Overview

Java provides high-level **synchronization utilities** in `java.util.concurrent` for coordinating threads.

**Main Utilities:**
- **CountDownLatch**: Wait for N operations to complete
- **CyclicBarrier**: Wait for N threads at a synchronization point
- **Semaphore**: Control access to N resources
- **Phaser**: Flexible multi-phase synchronization (Java 7+)
- **Exchanger**: Exchange data between two threads

**Advantages over wait/notify:**
- Higher-level abstractions
- Easier to use correctly
- More readable code
- Built-in error handling

<sub>[Back to top](#table-of-contents)</sub>

---

## CountDownLatch

**Purpose:** One or more threads wait until N operations (counted down) complete.

**Key Characteristics:**
- One-time use (cannot be reset)
- Count decreases from N to 0
- Waiting threads proceed when count reaches 0

### Basic Usage

```java
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        int numWorkers = 3;
        CountDownLatch latch = new CountDownLatch(numWorkers);

        // Start worker threads
        for (int i = 0; i < numWorkers; i++) {
            int workerId = i;
            new Thread(() -> {
                System.out.println("Worker " + workerId + " starting");
                doWork();
                System.out.println("Worker " + workerId + " finished");
                latch.countDown();  // Decrement count
            }).start();
        }

        // Wait for all workers to finish
        latch.await();  // Blocks until count reaches 0
        System.out.println("All workers finished!");
    }

    static void doWork() {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }
}
```

**Output:**
```
Worker 0 starting
Worker 1 starting
Worker 2 starting
Worker 0 finished
Worker 1 finished
Worker 2 finished
All workers finished!
```

### Use Case: Service Startup

```java
public class ApplicationStartup {
    private static final int NUM_SERVICES = 5;
    private final CountDownLatch latch = new CountDownLatch(NUM_SERVICES);

    public void startServices() throws InterruptedException {
        startService("Database", latch);
        startService("Cache", latch);
        startService("MessageQueue", latch);
        startService("WebServer", latch);
        startService("Scheduler", latch);

        // Wait for all services to start
        latch.await();
        System.out.println("All services started. Application ready!");
    }

    private void startService(String name, CountDownLatch latch) {
        new Thread(() -> {
            System.out.println("Starting " + name + "...");
            // Initialize service
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            System.out.println(name + " started");
            latch.countDown();
        }).start();
    }
}
```

### Key Methods

```java
CountDownLatch latch = new CountDownLatch(5);

// Decrement count (called by worker threads)
latch.countDown();

// Wait indefinitely for count to reach 0
latch.await();

// Wait with timeout
boolean done = latch.await(10, TimeUnit.SECONDS);
if (!done) {
    System.out.println("Timeout! Not all workers finished");
}

// Get current count
long current = latch.getCount();
```

<sub>[Back to top](#table-of-contents)</sub>

---

## CyclicBarrier

**Purpose:** N threads wait for each other at a synchronization point, then all proceed together.

**Key Characteristics:**
- Reusable (cyclic) - can be reset
- All threads wait at barrier until N threads arrive
- Optional barrier action executes when all arrive

### Basic Usage

```java
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        int numThreads = 3;
        CyclicBarrier barrier = new CyclicBarrier(numThreads, () -> {
            System.out.println("All threads reached barrier. Proceeding...");
        });

        for (int i = 0; i < numThreads; i++) {
            int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " doing part 1");
                    Thread.sleep(1000 * threadId);

                    barrier.await();  // Wait for all threads

                    System.out.println("Thread " + threadId + " doing part 2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

**Output:**
```
Thread 0 doing part 1
Thread 1 doing part 1
Thread 2 doing part 1
(all wait at barrier)
All threads reached barrier. Proceeding...
Thread 0 doing part 2
Thread 1 doing part 2
Thread 2 doing part 2
```

### Use Case: Parallel Matrix Processing

```java
public class ParallelMatrixProcessor {
    private final int numThreads = 4;
    private final CyclicBarrier barrier;
    private double[][] matrix;

    public ParallelMatrixProcessor() {
        barrier = new CyclicBarrier(numThreads, () -> {
            System.out.println("Phase complete. Matrix normalized.");
        });
    }

    public void processMatrix(double[][] data) {
        this.matrix = data;
        int rowsPerThread = matrix.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numThreads - 1) ? matrix.length : (i + 1) * rowsPerThread;

            new Thread(() -> processRows(startRow, endRow)).start();
        }
    }

    private void processRows(int startRow, int endRow) {
        try {
            // Phase 1: Multiply by 2
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] *= 2;
                }
            }
            barrier.await();  // Synchronize after phase 1

            // Phase 2: Add 10
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] += 10;
                }
            }
            barrier.await();  // Synchronize after phase 2

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Key Methods

```java
CyclicBarrier barrier = new CyclicBarrier(3);

// Wait at barrier (blocks until all threads arrive)
barrier.await();

// Wait with timeout
barrier.await(5, TimeUnit.SECONDS);

// Get number of waiting threads
int waiting = barrier.getNumberWaiting();

// Get required number of parties
int parties = barrier.getParties();

// Reset the barrier (releases all waiting threads with BrokenBarrierException)
barrier.reset();

// Check if barrier is broken
boolean broken = barrier.isBroken();
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Semaphore

**Purpose:** Control access to a resource pool with N permits.

**Key Characteristics:**
- Limits concurrent access to N threads
- Threads acquire permits before accessing resource
- Threads release permits after finishing
- Can be fair or unfair

### Basic Usage

```java
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    // Allow max 3 threads to access resource simultaneously
    private static final Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int threadId = i;
            new Thread(() -> accessResource(threadId)).start();
        }
    }

    static void accessResource(int threadId) {
        try {
            semaphore.acquire();  // Acquire permit (blocks if none available)
            System.out.println("Thread " + threadId + " accessing resource");
            Thread.sleep(2000);  // Simulate resource usage
            System.out.println("Thread " + threadId + " releasing resource");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();  // Release permit
        }
    }
}
```

**Output:** Only 3 threads access resource at a time.

### Use Case: Database Connection Pool

```java
public class ConnectionPool {
    private final Semaphore semaphore;
    private final List<Connection> connections = new ArrayList<>();

    public ConnectionPool(int poolSize) {
        semaphore = new Semaphore(poolSize);
        for (int i = 0; i < poolSize; i++) {
            connections.add(createConnection());
        }
    }

    public Connection getConnection() throws InterruptedException {
        semaphore.acquire();  // Wait for available connection
        return getAvailableConnection();
    }

    public void releaseConnection(Connection conn) {
        returnConnection(conn);
        semaphore.release();  // Make connection available
    }

    // Try to get connection without blocking
    public Connection tryGetConnection() {
        if (semaphore.tryAcquire()) {
            return getAvailableConnection();
        }
        return null;  // No connection available
    }

    // Try with timeout
    public Connection tryGetConnection(long timeout, TimeUnit unit)
            throws InterruptedException {
        if (semaphore.tryAcquire(timeout, unit)) {
            return getAvailableConnection();
        }
        return null;
    }
}
```

### Fair vs Unfair Semaphore

```java
// Unfair (default) - better performance
Semaphore unfair = new Semaphore(5);

// Fair - FIFO ordering (prevents starvation)
Semaphore fair = new Semaphore(5, true);
```

### Key Methods

```java
Semaphore semaphore = new Semaphore(5);

// Acquire one permit
semaphore.acquire();

// Acquire N permits
semaphore.acquire(3);

// Release permit
semaphore.release();

// Try to acquire without blocking
boolean acquired = semaphore.tryAcquire();

// Try with timeout
acquired = semaphore.tryAcquire(10, TimeUnit.SECONDS);

// Get available permits
int available = semaphore.availablePermits();

// Check if fair
boolean fair = semaphore.isFair();
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Phaser

**Purpose:** Flexible multi-phase synchronization (Java 7+). Combines features of CountDownLatch and CyclicBarrier with dynamic thread registration.

**Key Characteristics:**
- Supports multiple phases
- Dynamic thread registration/deregistration
- Reusable across phases
- Optional termination

### Basic Usage

```java
import java.util.concurrent.Phaser;

public class PhaserDemo {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(1);  // Register main thread

        for (int i = 0; i < 3; i++) {
            int threadId = i;
            phaser.register();  // Register each worker
            new Thread(() -> {
                // Phase 0
                System.out.println("Thread " + threadId + " - Phase 0");
                phaser.arriveAndAwaitAdvance();  // Wait for all

                // Phase 1
                System.out.println("Thread " + threadId + " - Phase 1");
                phaser.arriveAndAwaitAdvance();

                // Phase 2
                System.out.println("Thread " + threadId + " - Phase 2");
                phaser.arriveAndDeregister();  // Deregister after completion
            }).start();
        }

        phaser.arriveAndDeregister();  // Main thread deregisters
    }
}
```

### Use Case: Multi-Phase Task Execution

```java
public class MultiPhaseTask {
    public static void main(String[] args) {
        Phaser phaser = new Phaser() {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("=== Phase " + phase + " completed ===");
                return registeredParties == 0;  // Terminate if no parties left
            }
        };

        int numWorkers = 3;
        for (int i = 0; i < numWorkers; i++) {
            int workerId = i;
            phaser.register();
            new Thread(() -> {
                // Phase 0: Download
                System.out.println("Worker " + workerId + ": Downloading");
                sleep(1000);
                phaser.arriveAndAwaitAdvance();

                // Phase 1: Process
                System.out.println("Worker " + workerId + ": Processing");
                sleep(1000);
                phaser.arriveAndAwaitAdvance();

                // Phase 2: Upload
                System.out.println("Worker " + workerId + ": Uploading");
                sleep(1000);
                phaser.arriveAndDeregister();
            }).start();
        }
    }

    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}
```

### Key Methods

```java
Phaser phaser = new Phaser(3);  // 3 registered parties

// Register a new party
phaser.register();

// Arrive and wait for others
phaser.arriveAndAwaitAdvance();

// Arrive without waiting
phaser.arrive();

// Arrive and deregister
phaser.arriveAndDeregister();

// Get current phase number
int phase = phaser.getPhase();

// Get registered parties
int parties = phaser.getRegisteredParties();

// Get arrived parties in current phase
int arrived = phaser.getArrivedParties();

// Check if terminated
boolean terminated = phaser.isTerminated();
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Exchanger

**Purpose:** Two threads exchange data at a synchronization point.

### Basic Usage

```java
import java.util.concurrent.Exchanger;

public class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        // Producer thread
        new Thread(() -> {
            try {
                String data = "Data from producer";
                System.out.println("Producer sending: " + data);
                String received = exchanger.exchange(data);
                System.out.println("Producer received: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Consumer thread
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                String data = "Data from consumer";
                System.out.println("Consumer sending: " + data);
                String received = exchanger.exchange(data);
                System.out.println("Consumer received: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
```

**Output:**
```
Producer sending: Data from producer
Consumer sending: Data from consumer
Producer received: Data from consumer
Consumer received: Data from producer
```

### Use Case: Buffer Swapping

```java
public class BufferSwapping {
    private final Exchanger<List<Integer>> exchanger = new Exchanger<>();

    // Producer thread
    public void producer() {
        List<Integer> buffer = new ArrayList<>();
        try {
            while (true) {
                // Fill buffer
                for (int i = 0; i < 100; i++) {
                    buffer.add(generateData());
                }
                // Exchange full buffer for empty buffer
                buffer = exchanger.exchange(buffer);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Consumer thread
    public void consumer() {
        List<Integer> buffer = new ArrayList<>();
        try {
            while (true) {
                // Exchange empty buffer for full buffer
                buffer = exchanger.exchange(buffer);
                // Process buffer
                processData(buffer);
                buffer.clear();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Comparison Summary

| Utility | Purpose | Reusable | Parties | Direction |
|---------|---------|----------|---------|-----------|
| **CountDownLatch** | Wait for N operations | ❌ No | Fixed | One-way (count down) |
| **CyclicBarrier** | N threads wait for each other | ✅ Yes | Fixed | Mutual wait |
| **Semaphore** | Limit concurrent access | ✅ Yes | Dynamic | Resource control |
| **Phaser** | Multi-phase synchronization | ✅ Yes | Dynamic | Multi-phase |
| **Exchanger** | Exchange data between 2 threads | ✅ Yes | Fixed (2) | Data exchange |

**When to use:**
- **CountDownLatch**: Starting task after N prerequisite tasks complete
- **CyclicBarrier**: Parallel iterative algorithms (phases)
- **Semaphore**: Resource pooling, rate limiting
- **Phaser**: Complex multi-phase algorithms with dynamic participants
- **Exchanger**: Pipeline stages exchanging buffers

<sub>[Back to top](#table-of-contents)</sub>

---

## Best Practices

### 1. Always Handle InterruptedException

```java
try {
    latch.await();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();  // Restore interrupt status
    // Handle interruption appropriately
}
```

### 2. Use try-finally with Semaphore

```java
semaphore.acquire();
try {
    // Use resource
} finally {
    semaphore.release();  // Always release
}
```

### 3. Set Timeouts to Avoid Indefinite Waits

```java
if (!latch.await(10, TimeUnit.SECONDS)) {
    throw new TimeoutException("Latch timeout");
}
```

### 4. Consider Fairness for Semaphores

```java
// Fair semaphore prevents starvation
Semaphore semaphore = new Semaphore(permits, true);
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Common Pitfalls

### ❌ 1. Forgetting to countDown()

```java
latch.countDown();  // If forgotten, await() waits forever!
```

### ❌ 2. Not Releasing Semaphore Permit

```java
semaphore.acquire();
// If exception occurs and no finally block, permit lost!
```

### ❌ 3. Wrong Party Count

```java
// Creating barrier for 3 parties but only 2 threads call await()
CyclicBarrier barrier = new CyclicBarrier(3);  // Deadlock!
```

### ❌ 4. Reusing CountDownLatch

```java
CountDownLatch latch = new CountDownLatch(5);
// Cannot be reset! Use CyclicBarrier or Phaser instead
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: When would I choose `CyclicBarrier` over `CountDownLatch`?**
A: `CountDownLatch` is one-time and models "wait for N events." `CyclicBarrier` is reusable and models "N threads wait for each other," which suits multi-phase parallel algorithms.

---

**Q: Why is a `Semaphore` useful even when I have plenty of threads available?**
A: It caps concurrent access to a limited resource, like a connection pool or rate limit, regardless of how many threads exist — preventing resource exhaustion rather than just coordinating thread timing.

---

**Q: What's unique about `Phaser` compared to `CyclicBarrier`?**
A: `Phaser` supports a dynamic, changing number of registered parties across multiple phases, whereas `CyclicBarrier` requires a fixed party count set at construction.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Threads](threads.md) — the basic execution units these utilities coordinate
- [Executors](executors.md) — thread pools often used alongside these coordination utilities
- [Thread Synchronization](synchronization.md) — the lower-level primitives these utilities are built on top of

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

**Official Documentation:**
- [CountDownLatch JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CountDownLatch.html)
- [CyclicBarrier JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CyclicBarrier.html)
- [Semaphore JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Semaphore.html)
- [Phaser JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Phaser.html)

**Books:**
- [Java Concurrency in Practice](https://jcip.net/) - Chapter 5 (Building Blocks)

**Guides:**
- [Baeldung: CountDownLatch Guide](https://www.baeldung.com/java-countdown-latch)
- [Baeldung: CyclicBarrier Guide](https://www.baeldung.com/java-cyclic-barrier)
- [Baeldung: Semaphores](https://www.baeldung.com/java-semaphore)
- [Baeldung: Phaser](https://www.baeldung.com/java-phaser)

**Related Topics:**
- [Threads](threads.md) - Thread basics
- [Executors](executors.md) - Thread pools
- [Thread Synchronization](synchronization.md) - Low-level synchronization

---

[Get Started](../../../../../../get-started.md) |
[Java Concurrency](../../concurrency.md) |
[Java 8](../../versions.md#java-8-lts)

---
