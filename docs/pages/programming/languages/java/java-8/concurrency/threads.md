# Threads

## Table of Contents
<!-- TOC -->
* [Threads](#threads)
  * [Table of Contents](#table-of-contents)
  * [What's a Thread](#whats-a-thread)
  * [Thread Lifecycle](#thread-lifecycle)
  * [Creation of threads](#creation-of-threads)
  * [Table of Differences](#table-of-differences)
  * [Common Methods on Threads](#common-methods-on-threads)
  * [Concurrency Issues](#concurrency-issues-1)
    * [Race Conditions](#race-conditions)
    * [Deadlocks](#deadlocks)
    * [Livelocks](#livelocks)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

--- 

## What's a Thread
Conceptually, a thread in Java represents a _separate path of execution within a Java program_. It's the smallest unit of a program that can be scheduled and executed independently. Threads allow a Java program to perform multiple tasks concurrently, making better use of available system resources, particularly in multi-core processors.

Here are some key concepts that define what a thread is in Java:

- ### Independent Execution:
    A thread runs independently of other threads in the program. Each thread has its own _program counter_, _stack_, and _local variables_, allowing it to execute its own code path concurrently with other threads.

- ### Lightweight: 
    Threads in Java are relatively lightweight compared to full-fledged processes. Creating and managing threads is more efficient in terms of system resources.

- ### Shared Resources:
  Threads within the same process share the same memory space, which means they can access and modify shared data. This sharing of resources introduces concurrency challenges and the need for synchronization to avoid data corruption and race conditions.
 ![img.png](../../../../../../img/threads-memory.png)

- ### Scheduling:
  The Java Virtual Machine (JVM) or the operating system's thread scheduler determines the order and timing of thread execution. Threads are scheduled based on priority, fairness, and other scheduling policies.

- ### Multithreading:
  Multithreading is the practice of using multiple threads within a single program to perform concurrent tasks. It can lead to improved performance and responsiveness, especially in applications that can take advantage of parallelism.

- ### Thread States:
  Threads in Java go through various states, including `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, and `TERMINATED`. These states reflect the thread's lifecycle and what it's currently doing.

- ### Thread Safety: 
  Due to shared resources, careful consideration must be given to thread safety. Java provides synchronization mechanisms such as `synchronized` blocks/methods and concurrency utilities to ensure that shared data is accessed safely.

- ### Thread Communication:
  Threads often need to communicate and coordinate with each other. Java provides mechanisms like `wait`/`notify`, `CountDownLatch`, `CyclicBarrier`, and semaphores for inter-thread communication and synchronization.

- ### Concurrency Issues:
  Threads can introduce concurrency issues such as _race conditions_, _deadlocks_, and _livelocks_. Developers need to be aware of these issues and use appropriate techniques to prevent or mitigate them.

  - See also: [Concurrency Issues](#concurrency-issues-1)


  <sub>[Back to top](#table-of-contents)</sub>

## Thread Lifecycle

The thread lifecycle in Java includes the following states:

- **New**: A thread is in the "New" state when it has been created but has not yet started its execution. In this state, the thread's start() method has not been called.


- **Runnable**: When the start() method is called, the thread enters the "Runnable" state. It means the thread is eligible for execution, but it might not be running immediately. The Java Virtual Machine (JVM) or the operating system's thread scheduler determines when to execute the thread.


- **Running**: When the thread is executing its code, it is in the "Running" state. At any given time, only one thread can be in this state per CPU core. Threads in this state are actively using CPU resources.


- **Blocked** (Waiting for a Monitor): A thread can transition to the "Blocked" state when it is waiting for a synchronized monitor (`lock`) to be released by another thread. This happens when a thread attempts to enter a synchronized block or method that is already held by another thread. The blocked thread will remain in this state until it acquires the lock and can proceed.


- **Blocked** (Waiting for an Event): Threads can also enter the "Blocked" state when they are waiting for an event, such as input from a user or data from a network socket. Threads in this state are not actively using CPU resources and are waiting for some external event to occur.


- **Blocked** (Timed Waiting): Threads can enter a timed waiting state when they are waiting for a specified amount of time. For example, a thread might be in this state when it calls `Thread.sleep()` or waits for a specific duration using synchronization mechanisms like `wait(timeout)`.


- **Blocked** (Waiting for Join): A thread can enter this state when it is waiting for another thread to finish its execution using the `join()` method. The joining thread will remain blocked until the joined thread completes.
- 

- **Terminated** (Dead): A thread enters the "Terminated" state when it has completed its execution or when an unhandled exception occurs in the thread. Once a thread is terminated, it cannot be restarted. Threads can also explicitly call Thread.stop() to terminate themselves, but this method is discouraged due to its potential for leaving the application in an inconsistent state.


<sub>[Back to top](#table-of-contents)</sub>

## Creation of threads

- ### Extending the Thread Class

You can create a new thread by extending the `Thread` class and overriding its `run()` method. This approach allows you to define the behavior of the thread by implementing the `run()` method.


```java
class MyThread extends Thread {
  public void run() {
    // Define the task to be executed by the thread
  }
}

MyThread thread = new MyThread();
thread.start(); // Start the thread
```

<sub>[Back to top](#table-of-contents)</sub>

- ### Implementing the Runnable Interface:

A more flexible approach is to implement the `Runnable` interface. This separates the task from the threading mechanism and allows you to reuse the same task with multiple threads.

```java
class MyRunnable implements Runnable {
  public void run() {
    // Define the task to be executed by the thread
  }
}

MyRunnable myRunnable = new MyRunnable();
Thread thread = new Thread(myRunnable);
thread.start(); // Start the thread
```

<sub>[Back to top](#table-of-contents)</sub>

- ### Using Lambda Expressions (Java 8 and later):

With Java 8 and later, you can use lambda expressions to simplify the creation of threads when implementing Runnable. This makes the code more concise.


```java
Runnable myRunnable = () -> {
    // Define the task to be executed by the thread
};

Thread thread = new Thread(myRunnable);
thread.start(); // Start the thread
```


<sub>[Back to top](#table-of-contents)</sub>


- ### Using the Executor Framework:

The Executor framework provides a higher-level way to manage threads and execute tasks concurrently. It abstracts away many of the low-level details of thread management.

```java
  Executor executor = Executors.newFixedThreadPool(2); // Create a thread pool
  executor.execute(() -> {
  // Define the task to be executed by a thread in the pool
  });
```
- See also [Executors]()<!--TODO:-->

<sub>[Back to top](#table-of-contents)</sub>

- ### Using Callable and Future (for Task with Results):

If you need to execute tasks that return results or throw exceptions, you can use the `Callable` interface in combination with Future. This allows you to retrieve results or handle exceptions after a thread has completed its execution.


```java
Callable<Integer> myCallable = () -> {
  // Define the task to be executed by the thread
  return 42;
};

ExecutorService executorService = Executors.newSingleThreadExecutor();
Future<Integer> future = executorService.submit(myCallable);
```


- See also: [Callable and Future]()<!-- TODO: -->


<sub>[Back to top](#table-of-contents)</sub>

- ### Using Java's Concurrency Utilities:

Java provides various concurrency utilities in the `java.util.concurrent` package, such as `ExecutorService`, `ThreadPoolExecutor`, and `ForkJoinPool`, which offer advanced features for managing and controlling thread execution.

## Table of Differences

| Concept  | 	Description                                                                               | 	Usage                                                                                                                                                                           |
|----------|--------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Thread	  | A class in Java representing a separate thread of execution.                               | 	Extend the Thread class and override the run() method to define the thread's behavior.                                                                                          |
| Runnable | An interface in Java representing a task that can be executed by a thread.                 | 	Implement the Runnable interface and override the run() method to define the task. Pass the Runnable to a Thread for execution.                                                 |
| Callable | 	An interface similar to Runnable but allows tasks to return a result or throw exceptions. | 	Implement the Callable interface and override the call() method to define the task. Execute it using an ExecutorService, which returns a Future representing the task's result. |


<sub>[Back to top](#table-of-contents)</sub>

## Common Methods on Threads

- ### start():

Method: `void start()`
Use: Initiates the execution of the thread. It calls the run() method, and the thread enters the "Runnable" state.


<sub>[Back to top](#table-of-contents)</sub>

- ### join():

Method: `void join()`
Use: Causes the current thread to wait until the thread on which `join()` is called has completed its execution. Useful for thread coordination.


<sub>[Back to top](#table-of-contents)</sub>

- ### isAlive():

Method: `boolean isAlive()`
Use: Checks whether a thread is alive (i.e., has started and not yet terminated).


<sub>[Back to top](#table-of-contents)</sub>

- ### interrupt():

Method: `void interrupt()`
Use: Interrupts the thread, causing it to throw an `InterruptedException` if it's currently in a blocking or waiting state. Used for thread interruption and termination.


<sub>[Back to top](#table-of-contents)</sub>

- ### isInterrupted():

Method: `boolean isInterrupted()`
Use: Checks if the thread has been interrupted. Returns true if it has been interrupted, false otherwise.


<sub>[Back to top](#table-of-contents)</sub>


- ### interrupted():

Method: `static boolean interrupted()`
Use: Checks if the current thread has been interrupted and clears the interrupted status. Returns true if the thread was interrupted, false otherwise.


<sub>[Back to top](#table-of-contents)</sub>

- ### sleep():

Method: `static void sleep(long millis)` or `static void sleep(long millis, int nanos)`
Use: Causes the currently executing thread to sleep (pause) for the specified amount of time, allowing other threads to run.

<sub>[Back to top](#table-of-contents)</sub>

- ### yield():

Method: static void yield()
Use: Suggests to the scheduler that the current thread is willing to yield its current execution time, giving other threads a chance to run. It's a hint and not a guarantee.

<sub>[Back to top](#table-of-contents)</sub>

- ### setName() and getName():

Methods: `void setName(String name)` and `String getName()`
Use: Sets or retrieves the name of the thread, which can be helpful for identification and debugging.


<sub>[Back to top](#table-of-contents)</sub>


- ### setPriority() and getPriority():

Methods: `void setPriority(int priority)` and `int getPriority()`
Use: Sets or retrieves the priority of the thread, indicating its importance to the thread scheduler.


<sub>[Back to top](#table-of-contents)</sub>

- ### wait():

Method: `void wait()` or `void wait(long timeout)` or `void wait(long timeout, int nanos)`

Use: The `wait()` method is used for inter-thread communication and synchronization. When called within a synchronized context (i.e., within a synchronized block or method), it causes the current thread to release the lock it holds and enter a waiting state. The thread remains in this waiting state until another thread calls `notify()` or `notifyAll()` on the same object or until the specified timeout (if provided) elapses.

 - `void wait()`: The thread waits indefinitely until another thread calls `notify()` or `notifyAll()` on the same object.

 - `void wait(long timeout)`: The thread waits for the specified amount of time (in milliseconds) or until it's notified.

 - `void wait(long timeout, int nanos)`: The thread waits for the specified amount of time (in milliseconds and nanoseconds) or until it's notified.

>The wait() method is commonly used for implementing producer-consumer patterns, where one thread produces data and another thread consumes it. It's also used for various synchronization scenarios where threads need to coordinate their activities based on certain conditions.


<sub>[Back to top](#table-of-contents)</sub>

## Concurrency Issues

### Race Conditions

>A race condition occurs when multiple threads access shared data concurrently, and the final outcome depends on the timing and order of execution of the threads.

Race conditions can lead to unpredictable and incorrect behavior in a program because one thread may modify the shared data while another thread is reading or modifying it simultaneously.

To prevent race conditions, synchronization mechanisms like _locks_, _synchronized blocks/methods_, or the use of _thread-safe data structures_ should be employed to ensure that only one thread can access the shared resource at a time.


<sub>[Back to top](#table-of-contents)</sub>

### Deadlocks

>A deadlock is a situation in which two or more threads are unable to proceed with their execution because each is waiting for a resource that another thread holds, resulting in a circular dependency.

Common conditions for a deadlock to occur are:
- **Mutual Exclusion**: Resources can only be held by one thread at a time
- **Hold and Wait**: Threads hold resources while waiting for others
- **No Preemption**: Resources cannot be forcibly taken from threads
- **Circular Wait** Threads form a circular chain of resource dependency.

Deadlocks can lead to a complete halt in program execution, and they require careful design and use of strategies like deadlock detection and prevention to avoid or resolve them.


<sub>[Back to top](#table-of-contents)</sub>


### Livelocks

>A livelock is a situation where two or more threads are actively trying to resolve a resource conflict but end up repeatedly changing their states without making any progress.

In a livelock, threads are not blocked, but they are constantly responding to each other's actions and remain in a non-productive loop.

Livelocks often occur when multiple threads are trying to be overly polite by repeatedly releasing and reacquiring resources, preventing any of them from making progress.

Livelocks can be mitigated by introducing randomness or timeouts into the thread behavior to break the repeated patterns and allow the threads to make progress.


<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What's the practical difference between extending `Thread` and implementing `Runnable`?**
A: Implementing `Runnable` separates the task from the threading mechanism, allows the class to extend something else (Java has no multiple inheritance), and lets the same task run on different threads or executors.

---

**Q: Why doesn't a thread start running immediately after `start()` is called?**
A: `start()` only makes the thread "Runnable" — eligible for execution. The OS/JVM scheduler decides when it actually gets CPU time, and it can also block waiting for a monitor, I/O, or another thread via `join()`.

---

**Q: What's the difference between a race condition and a deadlock?**
A: A race condition produces incorrect results when threads access shared data without proper ordering or synchronization. A deadlock halts execution entirely because threads circularly wait on resources held by each other.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Thread Synchronization](synchronization.md) — preventing race conditions on shared data
- [Executors](executors.md) — the higher-level framework for managing threads
- [Callable and Future](callable-and-future.md) — running tasks that return results on a thread
- [Java Memory Model](java-memory-model.md) — why shared state needs explicit visibility guarantees

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.
- https://jenkov.com/tutorials/java-concurrency/java-memory-model.html

---


[Get Started](../../../../../../get-started.md) |
[Languages](../../../../../../get-started.md#languages) |
[Java Development](../../develop.md#multithreading-and-concurrency) |
[Java 8](../../versions.md#java-8-lts) |
[Concurrency](../../concurrency.md)

---
