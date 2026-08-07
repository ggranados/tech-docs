# Concurrent Programming

---

## Table of Contents
<!-- TOC -->
* [Concurrent Programming](#concurrent-programming)
  * [Table of Contents](#table-of-contents)
  * [What's Concurrent Programming](#whats-concurrent-programming)
  * [Concurrency vs Parallelism](#concurrency-vs-parallelism)
  * [Core Concepts](#core-concepts)
    * [Processes and Threads](#processes-and-threads)
    * [Thread Safety](#thread-safety)
    * [Synchronization](#synchronization)
    * [Race Conditions](#race-conditions)
    * [Deadlock](#deadlock)
    * [Shared State vs Message Passing](#shared-state-vs-message-passing)
  * [Concurrency Models](#concurrency-models)
    * [Thread-Based Concurrency](#thread-based-concurrency)
    * [Event-Driven Concurrency](#event-driven-concurrency)
    * [Actor Model](#actor-model)
    * [Coroutines](#coroutines)
  * [Common Patterns](#common-patterns)
    * [Producer-Consumer](#producer-consumer)
    * [Thread Pool](#thread-pool)
    * [Future/Promise](#futurepromise)
    * [Lock-Free Data Structures](#lock-free-data-structures)
  * [Benefits and Challenges](#benefits-and-challenges)
    * [Benefits](#benefits)
    * [Challenges](#challenges)
  * [Examples](#examples)
    * [Thread Creation (Multiple Languages)](#thread-creation-multiple-languages)
    * [Synchronization Example](#synchronization-example)
    * [Message Passing Example](#message-passing-example)
  * [Languages](#languages)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## What's Concurrent Programming

Concurrent programming is a programming paradigm that deals with the **execution of multiple tasks or processes simultaneously**. It enables programs to handle multiple operations at the same time, improving responsiveness, resource utilization, and performance.

**Key Characteristics:**
- **Multiple execution paths**: Different parts of a program execute independently
- **Coordination**: Tasks must coordinate access to shared resources
- **Non-deterministic**: The exact order of operations may vary between executions
- **Interleaving**: Tasks may switch execution at any point

Unlike sequential programming where instructions execute one after another, concurrent programming allows multiple sequences of operations to make progress during overlapping time periods.

> Concurrency is about **dealing with** lots of things at once. Parallelism is about **doing** lots of things at once. - Rob Pike

<sub>[Back to top](#table-of-contents)</sub>

---

## Concurrency vs Parallelism

While often used interchangeably, concurrency and parallelism are distinct concepts:

### Concurrency
**Concurrency** is about **structure** - the composition of independently executing tasks.

- **Focus**: Managing multiple tasks at once
- **Execution**: Tasks can run on a single processor (time-slicing)
- **Goal**: Better program structure, responsiveness
- **Example**: A web server handling multiple requests by switching between them

```
Time →
CPU: [Task A]--[Task B]--[Task A]--[Task C]--[Task B]
      ↑ Context switching between tasks
```

### Parallelism
**Parallelism** is about **execution** - the simultaneous execution of multiple tasks.

- **Focus**: Actually running multiple tasks at the same instant
- **Execution**: Requires multiple processors/cores
- **Goal**: Performance improvement through simultaneous execution
- **Example**: Matrix multiplication split across multiple CPU cores

```
Time →
CPU 1: [Task A--------------]
CPU 2: [Task B--------------]
CPU 3: [Task C--------------]
      ↑ True simultaneous execution
```

**Relationship:**
- Concurrency enables parallelism (but doesn't require it)
- Parallel programs must handle concurrency concerns
- Concurrent programs may or may not execute in parallel

<sub>[Back to top](#table-of-contents)</sub>

---

## Core Concepts

### Processes and Threads

**Process:**
- Independent execution unit with its own memory space
- Isolated from other processes
- Heavy-weight (significant overhead to create)
- Communication via inter-process communication (IPC)

**Thread:**
- Lightweight execution unit within a process
- Shares memory space with other threads in the same process
- Lower overhead than processes
- Easier communication but requires synchronization

```
┌─────────────────────────────────────┐
│          Process                     │
│  ┌──────────┐  ┌──────────┐         │
│  │ Thread 1 │  │ Thread 2 │         │
│  └──────────┘  └──────────┘         │
│                                      │
│  ┌────────────────────────┐         │
│  │   Shared Memory        │         │
│  └────────────────────────┘         │
└─────────────────────────────────────┘
```

<sub>[Back to top](#table-of-contents)</sub>

### Thread Safety

A piece of code is **thread-safe** if it functions correctly when accessed by multiple threads simultaneously.

**Thread-safe requirements:**
- No race conditions
- Proper synchronization of shared state
- Consistent state transitions
- No data corruption

**Common thread-safety techniques:**
- **Synchronization**: Using locks/mutexes
- **Immutability**: Using immutable data structures
- **Thread-local storage**: Each thread has its own copy
- **Atomic operations**: Indivisible operations
- **Lock-free algorithms**: Using atomic compare-and-swap

<sub>[Back to top](#table-of-contents)</sub>

### Synchronization

**Synchronization** is the coordination of concurrent tasks to ensure correct execution.

**Synchronization Mechanisms:**

1. **Mutex (Mutual Exclusion)**
   - Ensures only one thread accesses a resource at a time
   - Lock before access, unlock after

2. **Semaphore**
   - Controls access to a resource pool
   - Allows N threads to access simultaneously

3. **Monitor**
   - Combines mutex with condition variables
   - Wait/notify mechanisms

4. **Barriers**
   - Synchronization point where all threads must arrive before proceeding

5. **Read-Write Locks**
   - Multiple readers or single writer
   - Optimizes for read-heavy workloads

<sub>[Back to top](#table-of-contents)</sub>

### Race Conditions

A **race condition** occurs when the program's behavior depends on the relative timing of events.

**Example:**
```
Thread A:              Thread B:
read balance (100)     read balance (100)
add 50                 add 30
write balance (150)    write balance (130)  ← Lost update!
```

**Solution:** Synchronize access to shared state.

<sub>[Back to top](#table-of-contents)</sub>

### Deadlock

**Deadlock** occurs when two or more threads are blocked forever, each waiting for the other to release a resource.

**Four necessary conditions (Coffman conditions):**
1. **Mutual exclusion**: Resources cannot be shared
2. **Hold and wait**: Threads hold resources while waiting for others
3. **No preemption**: Resources cannot be forcibly taken
4. **Circular wait**: Circular chain of waiting threads

**Example:**
```
Thread A:              Thread B:
lock(Resource 1)       lock(Resource 2)
lock(Resource 2) ←─┐   lock(Resource 1) ←─┐
                   │                       │
                   └───── DEADLOCK ────────┘
```

**Prevention strategies:**
- Lock ordering (always acquire locks in same order)
- Lock timeout (give up if lock not acquired in time)
- Deadlock detection and recovery
- Avoid nested locks when possible

<sub>[Back to top](#table-of-contents)</sub>

### Shared State vs Message Passing

Two fundamental approaches to concurrent programming:

#### Shared State (Shared Memory)
Threads communicate by reading/writing shared variables.

**Advantages:**
- Direct access to shared data
- Low overhead for simple cases
- Familiar to most programmers

**Disadvantages:**
- Requires explicit synchronization
- Prone to race conditions
- Difficult to reason about
- Scalability challenges

**Example paradigms:** Traditional multi-threading, OpenMP

#### Message Passing
Threads communicate by sending messages (no shared state).

**Advantages:**
- No shared state = no race conditions
- Easier to reason about
- Natural for distributed systems
- Better scalability

**Disadvantages:**
- Message passing overhead
- May require data copying
- Different programming model

**Example paradigms:** Actor model (Erlang, Akka), Go channels, MPI

<sub>[Back to top](#table-of-contents)</sub>

---

## Concurrency Models

### Thread-Based Concurrency

The traditional model where the operating system manages threads.

**Characteristics:**
- Preemptive multitasking
- OS-scheduled
- Can utilize multiple cores
- Requires synchronization primitives

**Use cases:**
- General-purpose concurrent programming
- CPU-intensive parallel tasks
- Server applications

**Languages:** Java, C++, C#, Python (with GIL limitations)

<sub>[Back to top](#table-of-contents)</sub>

### Event-Driven Concurrency

Single-threaded event loop processing events from a queue.

**Characteristics:**
- Non-blocking I/O
- Callbacks or async/await
- Single-threaded (usually)
- Cooperative multitasking

**Use cases:**
- I/O-heavy applications
- Web servers (Node.js)
- UI programming

**Languages:** JavaScript/Node.js, Python (asyncio), Rust (tokio)

<sub>[Back to top](#table-of-contents)</sub>

### Actor Model

Independent actors communicate via asynchronous messages.

**Characteristics:**
- No shared state
- Each actor has a mailbox
- Message-based communication
- Location transparency

**Principles:**
- Actors process one message at a time
- Actors can create other actors
- Actors can send messages to other actors
- Supervision hierarchies for fault tolerance

**Use cases:**
- Distributed systems
- Fault-tolerant applications
- High-concurrency scenarios

**Languages:** Erlang, Elixir, Akka (Scala/Java), Pony

<sub>[Back to top](#table-of-contents)</sub>

### Coroutines

Lightweight, cooperative concurrent tasks.

**Characteristics:**
- User-space scheduling
- Explicit yield points
- Very low overhead
- Can have millions of coroutines

**Use cases:**
- Async I/O operations
- Game engines
- High-concurrency servers

**Languages:** Kotlin, Go (goroutines), Python (async/await), Lua

<sub>[Back to top](#table-of-contents)</sub>

---

## Common Patterns

### Producer-Consumer

One or more producers create work items; one or more consumers process them.

**Components:**
- Producers: Generate data/tasks
- Queue: Buffers items between producers and consumers
- Consumers: Process data/tasks

**Benefits:**
- Decouples production from consumption
- Balances different processing speeds
- Enables parallel processing

<sub>[Back to top](#table-of-contents)</sub>

### Thread Pool

Reusable pool of worker threads that execute submitted tasks.

**Benefits:**
- Avoids thread creation overhead
- Limits resource consumption
- Better CPU utilization
- Task queue management

**Components:**
- Worker threads (fixed or dynamic size)
- Task queue
- Submission interface
- Lifecycle management

**See also:** [Java Executors](../languages/java/concurrency.md#executors)

<sub>[Back to top](#table-of-contents)</sub>

### Future/Promise

Placeholder for a value that will be available in the future.

**Characteristics:**
- Represents asynchronous computation result
- Can be queried for completion status
- Blocks when retrieving result (if not ready)
- Enables composition of async operations

**Variants:**
- **Future**: Read-only view of async result
- **Promise**: Writable future (one-time)
- **CompletableFuture**: Composable async operations

**See also:**
- [Java Callable and Future](../languages/java/concurrency.md#callable-and-future)
- [Java CompletableFuture](../languages/java/concurrency.md#completablefuture)

<sub>[Back to top](#table-of-contents)</sub>

### Lock-Free Data Structures

Data structures that use atomic operations instead of locks.

**Techniques:**
- Compare-and-swap (CAS)
- Load-link/store-conditional
- Memory barriers

**Benefits:**
- No deadlock risk
- Better performance under contention
- Progress guarantees (wait-free, lock-free)

**Challenges:**
- Complex to implement correctly
- ABA problem
- Memory ordering concerns

<sub>[Back to top](#table-of-contents)</sub>

---

## Benefits and Challenges

### Benefits

1. **Improved Responsiveness**
   - UI remains responsive during long operations
   - Better user experience

2. **Resource Utilization**
   - Utilize multiple CPU cores
   - Overlap I/O with computation
   - Better throughput

3. **Performance**
   - Parallel execution on multi-core systems
   - Reduced latency for I/O operations

4. **Scalability**
   - Handle more concurrent users/requests
   - Efficient use of system resources

5. **Modularity**
   - Natural decomposition of problems
   - Independent components

<sub>[Back to top](#table-of-contents)</sub>

### Challenges

1. **Complexity**
   - Harder to design and understand
   - Non-deterministic behavior
   - Difficult to test and debug

2. **Race Conditions**
   - Subtle timing-dependent bugs
   - Hard to reproduce and fix

3. **Deadlocks**
   - System hangs completely
   - Can be difficult to detect

4. **Synchronization Overhead**
   - Lock contention reduces performance
   - Context switching costs

5. **Memory Consistency**
   - Visibility of shared state
   - Memory model complexities
   - Cache coherence issues

6. **Testing Difficulties**
   - Non-deterministic execution
   - Heisenbugs (disappear when debugging)
   - Need for stress testing

**See also:** [Java Memory Model](../languages/java/concurrency.md#java-memory-model)

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

### Thread Creation (Multiple Languages)

**Java:**
```java
// Using Thread class
Thread thread = new Thread(() -> {
    System.out.println("Running in thread: " + Thread.currentThread().getName());
});
thread.start();

// Using ExecutorService (recommended)
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(() -> {
    System.out.println("Task executed by: " + Thread.currentThread().getName());
});
executor.shutdown();
```

**Python:**
```python
import threading

def worker():
    print(f"Running in thread: {threading.current_thread().name}")

# Create and start thread
thread = threading.Thread(target=worker)
thread.start()
thread.join()  # Wait for completion
```

**Go:**
```go
package main

import (
    "fmt"
    "sync"
)

func worker(id int, wg *sync.WaitGroup) {
    defer wg.Done()
    fmt.Printf("Worker %d executing\n", id)
}

func main() {
    var wg sync.WaitGroup

    for i := 1; i <= 5; i++ {
        wg.Add(1)
        go worker(i, &wg)  // Launch goroutine
    }

    wg.Wait()  // Wait for all goroutines
}
```

**Rust:**
```rust
use std::thread;

fn main() {
    let handle = thread::spawn(|| {
        println!("Running in thread");
    });

    handle.join().unwrap();  // Wait for thread
}
```

<sub>[Back to top](#table-of-contents)</sub>

### Synchronization Example

**Problem:** Multiple threads incrementing a shared counter (unsafe).

**Java (with synchronization):**
```java
public class Counter {
    private int count = 0;

    // Synchronized method ensures thread safety
    public synchronized void increment() {
        count++;
    }

    // Or using explicit lock
    private final Object lock = new Object();

    public void incrementWithLock() {
        synchronized (lock) {
            count++;
        }
    }

    // Modern approach: AtomicInteger
    private AtomicInteger atomicCount = new AtomicInteger(0);

    public void incrementAtomic() {
        atomicCount.incrementAndGet();  // Lock-free!
    }
}
```

**Python (with lock):**
```python
import threading

class Counter:
    def __init__(self):
        self.count = 0
        self.lock = threading.Lock()

    def increment(self):
        with self.lock:
            self.count += 1
```

**Go (with mutex):**
```go
type Counter struct {
    mu    sync.Mutex
    count int
}

func (c *Counter) Increment() {
    c.mu.Lock()
    defer c.mu.Unlock()
    c.count++
}
```

<sub>[Back to top](#table-of-contents)</sub>

### Message Passing Example

**Go (channels):**
```go
func main() {
    messages := make(chan string)

    // Producer goroutine
    go func() {
        messages <- "ping"
    }()

    // Consumer (main goroutine)
    msg := <-messages
    fmt.Println(msg)  // "ping"
}
```

**Erlang (actor model):**
```erlang
% Spawn a process that receives messages
receiver() ->
    receive
        {From, Message} ->
            io:format("Received: ~p~n", [Message]),
            From ! {self(), "acknowledged"},
            receiver()  % Tail recursion
    end.

% Send a message to the receiver
Pid = spawn(fun receiver/0),
Pid ! {self(), "Hello"}.
```

**Rust (channels):**
```rust
use std::sync::mpsc;
use std::thread;

fn main() {
    let (tx, rx) = mpsc::channel();

    thread::spawn(move || {
        tx.send("Hello from thread").unwrap();
    });

    let received = rx.recv().unwrap();
    println!("{}", received);
}
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Languages

Modern programming languages provide varying levels of support for concurrent programming:

### Native Concurrency Support

- **Go**: Goroutines and channels as first-class features
  - Lightweight coroutines (goroutines)
  - Channel-based message passing
  - Simple concurrent programming model

- **Erlang/Elixir**: Actor model built into the language
  - Lightweight processes (actors)
  - Supervision trees for fault tolerance
  - Designed for high-concurrency systems

- **Rust**: Ownership model prevents data races at compile time
  - Thread safety guaranteed by type system
  - Fearless concurrency
  - Zero-cost abstractions

### Strong Standard Library Support

- **Java**: Comprehensive concurrency utilities
  - See also: [Java Concurrency](../languages/java/concurrency.md)
  - Thread API, ExecutorService, concurrent collections
  - CompletableFuture for async programming
  - Fork/Join framework for parallelism
  - Detailed memory model for thread visibility

- **C#**: Task Parallel Library (TPL) and async/await
  - Tasks and async/await keywords
  - PLINQ for parallel queries
  - Concurrent collections

- **Python**: Threading and multiprocessing modules
  - threading module (limited by GIL)
  - multiprocessing for true parallelism
  - asyncio for async/await concurrency

- **JavaScript/TypeScript**: Event loop and async/await
  - Single-threaded event loop
  - Promises and async/await
  - Web Workers for parallelism

### Library/Framework Support

- **Scala**: Akka framework for actor-based concurrency
  - Futures and actors
  - Akka toolkit for distributed systems

- **C/C++**: Pthreads, C++11 threading, OpenMP
  - Manual thread management
  - Low-level primitives
  - High performance

- **Kotlin**: Coroutines for structured concurrency
  - Lightweight coroutines
  - Flow for reactive streams
  - Integration with Java concurrency

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What's the real difference between concurrency and parallelism, and why does it matter for design decisions?**
A: Concurrency is about structuring a program so independent tasks can make progress during overlapping time periods (even on a single core); parallelism is about actually executing tasks at the same instant on multiple cores. A concurrent design enables parallelism but doesn't require it — this matters because you can improve responsiveness with concurrency alone (e.g., an event loop) without needing multiple processors.

---

**Q: When should I reach for the actor model or message passing instead of shared-memory threading?**
A: Choose message passing (actors, channels) when you want to eliminate race conditions by design, need location transparency for distributed systems, or are building fault-tolerant systems with supervision. Choose shared state when you need low-overhead access to data within a single process and can manage synchronization carefully.

---

**Q: How do I prevent deadlocks when a design requires acquiring multiple locks?**
A: Break at least one of the four Coffman conditions — most commonly by enforcing a consistent lock-acquisition order across all threads, using lock timeouts so threads give up instead of waiting forever, or avoiding nested locks altogether by redesigning around lock-free structures or message passing.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Reactive Programming](reactive.md) — event-driven asynchronous programming that often relies on concurrency models like event loops
- [Functional Programming](functional.md) — immutability and pure functions reduce the risk of race conditions in concurrent code
- [Java Concurrency](../languages/java/concurrency.md) — Java's concrete concurrency toolkit (Executors, Future, CompletableFuture, Java Memory Model)

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

**Books:**
- [Java Concurrency in Practice](https://jcip.net/) - Brian Goetz (applicable concepts beyond Java)
- [The Art of Multiprocessor Programming](https://www.elsevier.com/books/the-art-of-multiprocessor-programming/herlihy/978-0-12-415950-1) - Herlihy & Shavit
- [Seven Concurrency Models in Seven Weeks](https://pragprog.com/titles/pb7con/seven-concurrency-models-in-seven-weeks/) - Paul Butcher

**Articles and Papers:**
- [Concurrency is not Parallelism](https://go.dev/blog/waza-talk) - Rob Pike
- [Communicating Sequential Processes](https://www.cs.cmu.edu/~crary/819-f09/Hoare78.pdf) - Tony Hoare (CSP paper)

**Online Resources:**
- [Operating Systems: Three Easy Pieces - Concurrency](https://pages.cs.wisc.edu/~remzi/OSTEP/threads-intro.pdf)
- [The Little Book of Semaphores](https://greenteapress.com/wp/semaphores/) - Allen Downey
- [Patterns for Parallel Programming](https://www.oreilly.com/library/view/patterns-for-parallel/0321228111/)

**Language-Specific:**
- Java: [Java Concurrency Documentation](../languages/java/concurrency.md)
- Go: [Effective Go - Concurrency](https://go.dev/doc/effective_go#concurrency)
- Rust: [The Rust Book - Concurrency](https://doc.rust-lang.org/book/ch16-00-concurrency.html)
- Erlang: [Learn You Some Erlang - Concurrency](https://learnyousomeerlang.com/the-hitchhikers-guide-to-concurrency)

**Related Paradigms:**
- [Reactive Programming](reactive.md) - Event-driven asynchronous programming
- [Functional Programming](functional.md) - Immutability aids concurrent programming

---

[Get Started](../../../get-started.md#paradigms)

---
