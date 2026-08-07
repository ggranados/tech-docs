# List Interface

---

## Table of Contents
<!-- TOC -->
* [List Interface](#list-interface)
  * [Table of Contents](#table-of-contents)
  * [List Interface Characteristics](#list-interface-characteristics)
  * [Most Used List Implementations](#most-used-list-implementations)
    * [ArrayList](#arraylist)
    * [LinkedList](#linkedlist)
  * [Choose the appropriate implementation](#choose-the-appropriate-implementation)
    * [Comparison Table](#comparison-table)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## List Interface Characteristics
In Java, the List interface is a part of the Java Collections Framework and represents an *ordered* collection of elements, allowing *duplicates* and maintaining the insertion order. 

Some key characteristics of the List interface include:


- **Ordered Collection**: Elements in a List are stored in a specific order, which is typically the order in which they were inserted.


- **Duplicates Allowed**: A List can contain duplicate elements, meaning you can add the same element multiple times.


- **Indexed Access**: Elements in a List can be accessed using their index, starting from 0 for the first element.


- **Dynamic Size**: Unlike arrays, a List can dynamically resize to accommodate the addition or removal of elements.


- **Common Operations**: The List interface defines various methods for adding, removing, and accessing elements such as `add()`, `remove()`, `get()`, `size()`, etc.


- *Iterable*: Being part of the Collections Framework, the List interface is iterable, allowing for easy iteration using loops or Java streams.

  - See also: [Streams](../java-8/stream-api.md)

<sub>[Back to top](#table-of-contents)</sub>


## Most Used List Implementations
Java provides several implementations of the List interface, each with its own strengths and weaknesses. Here are the most commonly used implementations:


<sub>[Back to top](#table-of-contents)</sub>


### ArrayList

- It uses a dynamic array to store elements.
- Offers fast access and search time `O(1)`, making it suitable for random access.
- Slower insertion and deletion `O(n)` as elements may need to be shifted.

>It's generally preferred when frequent element access is required, but not so much for frequent insertions and deletions.

Example:

```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListDemo {
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();

        // Adding elements to the ArrayList
        arrayList.add("Apple");
        arrayList.add("Banana");
        arrayList.add("Orange");
        arrayList.add("Grapes");

        // Accessing elements using get()
        System.out.println("Element at index 2: " + arrayList.get(2));

        // Removing elements
        arrayList.remove("Banana");
        System.out.println("After removing 'Banana': " + arrayList);

        // Size of the ArrayList
        System.out.println("Size of the ArrayList: " + arrayList.size());

        // Iteration using for-each loop
        System.out.println("Iterating through the ArrayList:");
        for (String fruit : arrayList) {
            System.out.println(fruit);
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>


### LinkedList

- It uses a doubly-linked list to store elements.
- Insertion and deletion are faster `O(1)` as it involves changing pointers.
- Slower access time `O(n)` since you need to traverse the list from the beginning to access an element.

>Suitable when you require frequent insertion and deletion, but not ideal for random access.

Example:

```java
import java.util.LinkedList;
import java.util.List;

public class LinkedListDemo {
    public static void main(String[] args) {
        List<String> linkedList = new LinkedList<>();

        // Adding elements to the LinkedList
        linkedList.add("Apple");
        linkedList.add("Banana");
        linkedList.add("Orange");
        linkedList.add("Grapes");

        // Accessing elements using get()
        System.out.println("Element at index 2: " + linkedList.get(2));

        // Removing elements
        linkedList.remove("Banana");
        System.out.println("After removing 'Banana': " + linkedList);

        // Size of the LinkedList
        System.out.println("Size of the LinkedList: " + linkedList.size());

        // Iteration using for-each loop
        System.out.println("Iterating through the LinkedList:");
        for (String fruit : linkedList) {
            System.out.println(fruit);
        }
    }
}

```

<sub>[Back to top](#table-of-contents)</sub>


## Choose the appropriate implementation

Always consider the trade-offs and performance characteristics to make an informed decision.

>If you need fast random access and infrequent insertion/deletion, use `ArrayList`. 

>For frequent insertion and deletion, `LinkedList` might be a better choice. 


<sub>[Back to top](#table-of-contents)</sub>


### Comparison Table

| Characteristics           | ArrayList                                      | LinkedList                  |
|---------------------------|------------------------------------------------|-----------------------------|
| Underlying Data Structure | Dynamic Array                                  | Doubly Linked List          |
| Access Time               | Fast `O(1)`                                    | Slow `O(n)`                 |
| Insertion/Deletion Time   | Slow `O(n)`                                    | Fast `O(1)`                 |
| Random Access             | Efficient                                      | Inefficient [^1]            |
| Memory Overhead           | Lower                                          | Higher                      |
| Iteration Performance     | Faster                                         | Slower [^1]                 |
| Use Cases                 | Frequent access, infrequent insertion/deletion | Frequent insertion/deletion |

[^1] Elements are not stored in contiguous memory locations. Instead, each element in a LinkedList is stored in a separate node, and these nodes are connected through pointers (references) to the previous and next nodes, forming a chain-like structure.


<sub>[Back to top](#table-of-contents)</sub>


---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: When should I choose `ArrayList` over `LinkedList` per this page's comparison?**
A: Choose `ArrayList` when you need fast, frequent random access (`O(1)`) and insertions/deletions are rare; choose `LinkedList` when you frequently insert or remove elements (`O(1)`) and can tolerate slower `O(n)` indexed access.

---

**Q: Why does `LinkedList` have `O(n)` access time despite `O(1)` insertion?**
A: Its elements live in separate nodes connected by pointers rather than contiguous memory, so reaching an arbitrary index means traversing the chain from the head (or tail) node by node — there's no direct offset calculation like an array provides.

---

**Q: How does the `List` interface fit into the rest of the Collections Framework?**
A: `List` is one of the four core interfaces (alongside `Set`, `Queue`/`Deque`, and `Map`); it's the only one that guarantees both ordering and duplicate elements, accessible by index.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Updated Java Collections API](enhanced-collections.md) — overview covering `List` alongside `Set`, `Map`, and `Queue`/`Deque`.
- [Set Interface](set-interface.md) — the no-duplicates counterpart to `List`.
- [Queue and Deque Interfaces](queue-deque-interfaces.md) — ordered collections built for processing rather than random access.
- [Generics](generics.md) — the type-safety mechanism behind `List<T>`.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://www.geeksforgeeks.org/list-interface-java-examples/
https://docs.oracle.com/javase/8/docs/api/java/util/List.html

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#generics-and-collections) |
[Original Collections API](../java-1_2/collections-api.md) |
[Updated Collections](enhanced-collections.md)

---
