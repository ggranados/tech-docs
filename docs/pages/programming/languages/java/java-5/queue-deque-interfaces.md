# Queue and Deque Interface

---

## Table of Contents
<!-- TOC -->
* [Queue and Deque Interface](#queue-and-deque-interface)
  * [Table of Contents](#table-of-contents)
  * [Queue Interface Characteristics](#queue-interface-characteristics)
  * [Deque Interface Characteristics](#deque-interface-characteristics)
  * [Most Used Queue and Deque Implementations](#most-used-queue-and-deque-implementations)
    * [Queue Implementations](#queue-implementations)
      * [LinkedList](#linkedlist)
      * [PriorityQueue](#priorityqueue)
      * [ArrayBlockingQueue](#arrayblockingqueue)
    * [Deque Implementations](#deque-implementations)
      * [LinkedList](#linkedlist-1)
      * [ArrayDeque](#arraydeque)
      * [LinkedBlockingDeque](#linkedblockingdeque)
  * [Choose the appropriate implementation](#choose-the-appropriate-implementation)
    * [Queue Comparison Table](#queue-comparison-table)
    * [Deque Comparison Table](#deque-comparison-table)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Queue Interface Characteristics
In Java, the `Queue` interface is a part of the Java Collections Framework and represents a collection that holds elements in a specific order. Elements in a `Queue` are typically processed in a *first-in-first-out (FIFO)* manner, meaning that the element that was added first will be the first one to be removed. Queues are commonly used in scenarios like task scheduling, event handling, and breadth-first search algorithms.

Key  of the `Queue` interface:

- **Ordering**: Elements in a Queue are *ordered based on their insertion time*. The first element inserted will be the first to be removed.


- **FIFO**: The Queue follows the *"First-In-First-Out"* principle, where elements are processed in the order they were added.


- **Basic Operations**: Common methods include `add()`, `offer()`, `remove()`, `poll()`, and `peek()`, among others.

<sub>[Back to top](#table-of-contents)</sub>


## Deque Interface Characteristics
The `Deque` (*Double-Ended Queue*) interface extends the `Queue` interface and represents a queue where you *can add or remove elements from both ends*. You can treat a `Deque` *as both a queue and a stack*. `Deques` are useful in scenarios where you need to efficiently add or remove elements from the beginning or end of the queue.

Key characteristics of the `Deque` interface:

- **Double-Ended**: The Deque allows adding and removing elements from both the front (head) and the rear (tail).


- **Stack and Queue Operations**: As a `Deque` extends the `Queue` interface, you can perform queue operations like *FIFO* as well as stack operations like *LIFO* (Last-In-First-Out).


- **Additional Methods**: In addition to the methods from the `Queue` interface, the `Deque` provides methods such as `addFirst()`, `addLast()`, `removeFirst()`, `removeLast()`, `getFirst()`, and `getLast()`.

<sub>[Back to top](#table-of-contents)</sub>


## Most Used Queue and Deque Implementations
Java provides several implementations of the Queue and Deque interfaces. Let's go through the most commonly used ones:

<sub>[Back to top](#table-of-contents)</sub>

### Queue Implementations

#### LinkedList

- Implements both the Queue and Deque interfaces.

- Provides efficient add and remove operations for both ends.

- Not thread-safe, so if you require thread-safety, consider using `java.util.concurrent.ConcurrentLinkedDeque`.

>Preferred when you need a versatile and efficient Queue implementation with both FIFO capabilities.
  Deque Implementations

Example:

```java
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();

        // Adding elements to the queue
        queue.add("Apple");
        queue.offer("Banana");
        queue.add("Orange");

        // Removing and processing elements (FIFO)
        while (!queue.isEmpty()) {
            String element = queue.poll();
            System.out.println("Processing: " + element);
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

#### PriorityQueue

- Implements the Queue interface and provides elements based on their natural order or a custom comparator.

- Elements are ordered based on their priority.

- Offers quick access to the element with the highest priority.


>Most preferred when you need to process elements based on their priority, such as in a task scheduler or Dijkstra's algorithm.

Example:

```java
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Queue<Integer> priorityQueue = new PriorityQueue<>();

        // Adding elements to the priority queue
        priorityQueue.add(5);
        priorityQueue.add(10);
        priorityQueue.add(2);
        priorityQueue.add(8);

        // Removing and processing elements (based on priority)
        while (!priorityQueue.isEmpty()) {
            Integer element = priorityQueue.poll();
            System.out.println("Processing: " + element);
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>


#### ArrayBlockingQueue
`ArrayBlockingQueue` is a specific implementation of the `BlockingQueue` interface in Java. It is a bounded, blocking queue backed by an array, which means it has a fixed capacity specified during its creation.

- Fixed capacity, meaning it can hold only a limited number of elements. Once the queue reaches its capacity, attempts to add more elements will block until space becomes available.

- Blocking operations, which means if the queue is empty, attempts to retrieve elements (`take()`, `poll()`, etc.) will block until an element becomes available. Similarly, if the queue is full, attempts to add elements (`put()`, `offer()`, etc.) will block until space becomes available.

- Follows the First-In-First-Out (FIFO) order, meaning elements are processed in the order they were added.

- Thread-Safe, is designed to be used in concurrent environments, and all of its methods are thread-safe. This makes it suitable for scenarios where multiple threads need to interact with the queue simultaneously.

- Is not reentrant. If a thread holds a lock on the queue and attempts to perform another blocking operation on the same queue, it will result in a *deadlock*.

- Does not allow null elements. Attempts to add null elements will result in a NullPointerException.

>Is particularly useful in scenarios where you have multiple producers and consumers sharing a bounded buffer. Since it is a blocking queue, it allows producers to block when the buffer is full and consumers to block when the buffer is empty, effectively coordinating the communication between threads.

Example:


```java
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
public static void main(String[] args) {
int capacity = 10;
ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(capacity);

        // Producer adding elements to the queue
        try {
            queue.put(1);
            queue.put(2);
            // ...
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Consumer retrieving elements from the queue
        try {
            Integer element = queue.take();
            // Process the element
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
````

<sub>[Back to top](#table-of-contents)</sub>

### Deque Implementations

#### LinkedList

- Implements both the Queue and Deque interfaces.

- Provides efficient add and remove operations for both ends.

- Not thread-safe, so if you require thread-safety, consider using `java.util.concurrent.ConcurrentLinkedDeque`.

>Preferred when you need a versatile and efficient Queue implementation with both FIFO and LIFO capabilities.
Deque Implementations

Example:

```java
import java.util.LinkedList;
import java.util.Deque;

public class Main {
    public static void main(String[] args) {
        Deque<String> deque = new LinkedList<>();

        // Adding elements to the deque from the rear
        deque.addLast("Element 1");
        deque.addLast("Element 2");
        deque.addLast("Element 3");

        // Adding elements to the deque from the front
        deque.addFirst("Element 0");

        // Removing and processing elements from both ends (FIFO and LIFO)
        while (!deque.isEmpty()) {
            String frontElement = deque.pollFirst(); // Removes from the front (FIFO)
            String rearElement = deque.pollLast();   // Removes from the rear (LIFO)

            System.out.println("Processing from front: " + frontElement);
            System.out.println("Processing from rear: " + rearElement);
        }
    }
}

```

#### ArrayDeque


- Implements both the Deque and Queue interfaces.

- Resizable-array implementation with better performance compared to LinkedList.

- Not thread-safe, so if you require thread-safety, consider using `java.util.concurrent.ConcurrentLinkedDeque`.


>Preferred when you need a fast and efficient double-ended queue implementation.

Example:

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    public static void main(String[] args) {
        Deque<String> deque = new ArrayDeque<>();

        // Adding elements to the deque
        deque.addFirst("Front");
        deque.offerLast("Back");
        deque.add("Middle");

        // Removing and processing elements from both ends (FIFO and LIFO)
        while (!deque.isEmpty()) {
            String firstElement = deque.pollFirst();
            String lastElement = deque.pollLast();

            System.out.println("Processing from front: " + firstElement);
            System.out.println("Processing from back: " + lastElement);
        }
    }
}

```

<sub>[Back to top](#table-of-contents)</sub>

#### LinkedBlockingDeque


- Implements the BlockingDeque interface, extending BlockingQueue and Deque.

- Provides blocking operations for concurrent scenarios.

- Suitable for use in concurrent producer-consumer patterns.

>Preferred when you need a thread-safe Deque implementation to handle concurrent operations efficiently.

Example:

```java
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) {
        BlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>();

        // Adding elements to the blocking deque
        blockingDeque.add("One");
        blockingDeque.offer("Two");
        blockingDeque.addLast("Three");

        // Removing and processing elements from both ends (FIFO and LIFO)
        while (!blockingDeque.isEmpty()) {
            String firstElement = blockingDeque.pollFirst();
            String lastElement = blockingDeque.pollLast();

            System.out.println("Processing from front: " + firstElement);
            System.out.println("Processing from back: " + lastElement);
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

## Choose the appropriate implementation

Always consider the characteristics and performance implications of each implementation for your application's needs.

For Queue:
>Use `LinkedList` (as `Queue`) when you need a basic, versatile, and memory-efficient queue implementation with both FIFO capabilities.


>Use `PriorityQueue` when you need to process elements based on their priority, where elements with higher priority are processed before those with lower priority.

> If you need a fixed-size, thread-safe queue with blocking behavior, `ArrayBlockingQueue` would be more appropriate


For Deque:

>Use `LinkedList` (as `Deque`) when you need a basic, versatile, and memory-efficient queue implementation with both FIFO and LIFO capabilities.

>Use `ArrayDeque` (as `Deque`) when you require a fast and efficient double-ended queue for adding or removing elements from both the front and the back.

>When you need a thread-safe double-ended queue for concurrent producer-consumer scenarios, such as in a multi-threaded environment, use `LinkedBlockingDeque`.

<sub>[Back to top](#table-of-contents)</sub>

### Queue Comparison Table

| Characteristic	        | LinkedList                | 	PriorityQueue       | 	ArrayBlockingQueue                   |
|------------------------|---------------------------|----------------------|---------------------------------------|
| Ordering               | 	Insertion Order          | 	Priority Order	     | No specific order                     |
| Element Ordering	      | FIFO (First-In-First-Out) | 	Depends on priority | 	FIFO (First-In-First-Out)            |
| Null Elements Allowed	 | Yes	                      | No	                  | No                                    |
| Blocking Behavior	     | Not Blocking              | 	Not Blocking        | 	Blocking on insertion/removal        |
| Iteration Performance  | 	`O(n)`                   | 	`O(n)`              | 	`O(n)`                               |
| Use Cases	             | Basic Queue Operations	   | Priority-based Queue | 	Bounded Queue with blocking behavior |

<sub>[Back to top](#table-of-contents)</sub>

### Deque Comparison Table

| Characteristic	        | LinkedList	            | ArrayDeque             | 	LinkedBlockingDeque                  |
|------------------------|------------------------|------------------------|---------------------------------------|
| Ordering	              | Insertion Order        | 	Insertion Order       | 	Insertion Order                      |
| Element Ordering	      | FIFO/LIFO	             | FIFO/LIFO	             | FIFO/LIFO                             |
| Null Elements Allowed  | 	Yes                   | 	No                    | 	No                                   |
| Blocking Behavior	     | Not Blocking           | 	Not Blocking          | 	Blocking on insertion/removal        |
| Iteration Performance	 | `O(n)`	                | `O(n)`                 | 	`O(n)`                               |
| Use Cases	             | Basic Deque Operations | 	General-purpose Deque | 	Bounded Deque with blocking behavior |


<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What's the practical difference between `Queue`'s FIFO processing and `Deque`'s dual-ended access?**
A: `Queue` only exposes single-direction FIFO operations (`offer`/`poll`/`peek`); `Deque` extends it with `addFirst`/`addLast`/`removeFirst`/`removeLast`, letting the same structure behave as either a FIFO queue or a LIFO stack.

---

**Q: Why would I pick `ArrayBlockingQueue` in a producer-consumer scenario?**
A: It's a fixed-capacity, thread-safe queue whose `put()`/`take()` operations block automatically when the queue is full or empty, coordinating producers and consumers without you writing manual wait/notify logic.

---

**Q: When is `PriorityQueue` preferable to a plain `LinkedList`-based `Queue`?**
A: When elements must be processed by priority rather than insertion order — e.g. task scheduling or Dijkstra's algorithm — since `PriorityQueue` orders elements by natural ordering or a supplied `Comparator` instead of FIFO.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Updated Java Collections API](enhanced-collections.md) — overview covering `Queue`/`Deque` alongside `List`, `Set`, and `Map`.
- [List Interface](list-interface.md) — `LinkedList` implements both `List` and `Deque`, covered on both pages.
- [Concurrency Utilities](../java-8/concurrency/concurrency-utilities.md) — background on the blocking-queue concurrency primitives used by `ArrayBlockingQueue` and `LinkedBlockingDeque`.
- [Multithreading and Concurrency](../concurrency.md) — thread coordination context for the blocking queue implementations discussed here.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://www.geeksforgeeks.org/queue-interface-java/?ref=lbp
https://www.geeksforgeeks.org/deque-interface-java-example/?ref=lbp
https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html
https://docs.oracle.com/javase/8/docs/api/java/util/Deque.html

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#generics-and-collections) |
[Original Collections API](../java-1_2/collections-api.md) |
[Updated Collections](enhanced-collections.md)


