# Map Interface

---

## Table of Contents

<!-- TOC -->
* [Map Interface](#map-interface)
  * [Table of Contents](#table-of-contents)
  * [Map Interface Characteristics](#map-interface-characteristics)
  * [Most Used Map Implementations](#most-used-map-implementations)
    * [HashMap](#hashmap)
      * [Hashmap internally](#hashmap-internally)
      * [Hash Collision](#hash-collision)
    * [TreeMap](#treemap)
    * [LinkedHashMap](#linkedhashmap)
  * [Choose the appropriate implementation](#choose-the-appropriate-implementation)
    * [Comparison Table](#comparison-table)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Map Interface Characteristics
In Java, the Map interface is part of the Java Collections Framework and represents a collection of key-value pairs. 

*Each key in the Map is unique, and it is used to retrieve its corresponding value*. 

Some key characteristics of the Map interface include:

- **Key-Value Pairs**: A Map stores elements as *key-value pairs*, where each key maps to a specific value.


- **Keys are Unique**: Each key in the Map is unique; *duplicate keys are not allowed*.


- **Fast Retrieval**: Given a key, the corresponding *value can be quickly retrieved*, making it efficient for data lookup.

- **No Specific Order**: The elements in a Map are *not stored in any specific order*, and their arrangement may not match the insertion order.


- **Replace and Update**: A Map allows you to *replace the value* associated with an existing key or *add new key-value pairs*.


- **Iterable**: Though the Map interface itself is *not directly iterable*, you can obtain collections of keys, values, or entries and iterate through them.


<sub>[Back to top](#table-of-contents)</sub>


## Most Used Map Implementations
Java provides several implementations of the Map interface, each with different characteristics and use cases. Here are the most commonly used Map implementations:

<sub>[Back to top](#table-of-contents)</sub>


### HashMap

- It uses a hash table to store key-value pairs.
- Offers fast access and retrieval time `O(1)` for most operations.
- Does not guarantee any specific order for keys or values.
- Allows null as both key and value.

>Preferred for fast and unordered key-value mapping

Example:

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapDemo {
    public static void main(String[] args) {
        Map<String, Integer> hashMap = new HashMap<>();

        // Adding key-value pairs to the HashMap
        hashMap.put("Apple", 1);
        hashMap.put("Banana", 2);
        hashMap.put("Orange", 3);
        hashMap.put("Grapes", 4);

        // Accessing the value associated with a key
        int value = hashMap.get("Apple");
        System.out.println("Value associated with 'Apple': " + value);

        // Updating the value associated with a key
        hashMap.put("Apple", 10);
        System.out.println("Updated value associated with 'Apple': " + hashMap.get("Apple"));

        // Size of the HashMap
        System.out.println("Size of the HashMap: " + hashMap.size());

        // Iterating through the HashMap using entrySet()
        System.out.println("Iterating through the HashMap:");
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
```

#### Hashmap internally
Internally, a HashMap in Java is implemented as an array of `Node` objects (called the hash table) with a linked list or a balanced tree (starting from Java 8) to handle hash collisions. The Node class represents key-value pairs stored in the `HashMap`

Steps:

1. **Hashing the Key**: When put a key-value pair into a `HashMap`, Java calculates the *hash code* of the key using the `hashCode()` method of the key object. The hash code is an integer value that determines the bucket in the hash table where the key-value pair will be stored.


2. **Index Calculation**: To convert the hash code into a valid index in the hash table, Java applies the following computation: `index = hashcode & (table.length - 1)`. The `table.length` represents the current size of the hash table, which is usually a power of two. The `&` operation with `(table.length - 1)` ensures that the index stays within the valid range of the array.


3. **Handling Collisions**: If two different keys produce the same index after hashing, a collision occurs. To handle collisions, Java uses *separate chaining*: each bucket in the hash table can hold multiple `Node` objects (key-value pairs) forming a linked list or a balanced tree (in case of high collisions).


4. **Linked List or Balanced Tree**: In older versions of Java, collisions are resolved using a linked list. In Java 8 and later, if a bucket's linked list exceeds a certain threshold (`TREEIFY_THRESHOLD`, 8 by default), the linked list is transformed into a balanced tree (red-black tree) to improve lookup time in case of high collisions.


5. **Performance and Load Factor**: The `HashMap` maintains a load factor, which represents the ratio of the number of key-value pairs to the number of buckets in the hash table. When the number of elements exceeds the load factor threshold (`LOAD_FACTOR`, 0.75 by default), the hash table is resized to accommodate more buckets and reduce the chance of collisions, thereby maintaining performance.


6. **Resizing**: When resizing occurs, the number of buckets (array size) is doubled, and each key-value pair is rehashed to determine its new bucket in the resized hash table. This operation may be expensive, but it ensures that the `HashMap` remains efficient even with a large number of elements.


7. **Iterating through HashMap**: Iterating through a `HashMap` can be done using an iterator or by using enhanced for-each loop through the `entrySet()`, `keySet()`, or `values()`.

![img.png](../../../../../img/map-internally.png)

>Keep in mind that the `HashMap` is not thread-safe. If you need a thread-safe version of `HashMap`, you can use `ConcurrentHashMap`, which provides better concurrency and performance in multi-threaded environments.


<sub>[Back to top](#table-of-contents)</sub>

#### Hash Collision

A hash collision occurs in a hash table or hash-based data structure when two different keys produce the same hash code or index in the underlying data storage. In other words, multiple keys are mapped to the same location in the hash table, even though they should ideally be stored in different locations.

Hash collisions can happen due to various reasons, including:

- **Limited Range of Hash Codes**: Most hash functions generate hash codes within a limited range (e.g., 32 or 64 bits). As a result, when you have a large number of possible keys, some of them will inevitably produce the same hash code due to the pigeonhole principle.


- **Low Quality Hash Function**: A poor quality hash function may not distribute the keys evenly across the available hash code range, leading to more collisions.


- **Clustering**: Over time, hash-based data structures can experience clustering, where keys with similar hash codes tend to cluster together, increasing the likelihood of collisions.

To handle hash collisions, data structures that rely on hashing typically use one of the following techniques:

- **Separate Chaining**: In separate chaining, each hash table slot (bucket) contains a linked list or other data structure to hold multiple key-value pairs that have the same hash code. When a collision occurs, new entries are simply added to the list at that slot. This way, multiple keys with the same hash code can coexist in the same bucket.


- **Open Addressing**: Open addressing involves searching for alternative locations in the hash table when a collision occurs. Instead of using separate data structures like linked lists, the table itself is utilized to find an open slot for the key-value pair. Various probing techniques (linear probing, quadratic probing, double hashing) are used to find the next available slot in the table.

- >**Java's `HashMap` uses separate chaining**. When a collision occurs, it stores multiple key-value pairs with the same hash code in a linked list or a balanced tree (after a certain threshold) at the same hash table index (bucket).

- Java's `HashSet` also uses separate chaining. When two distinct objects have the same hash code, they will be placed in the same bucket in the internal hash table.

    - See also: [Set Interface](set-interface.md#hashset)
  

- Java's `Hashtable` and `IdentityHashMap` use open addressing with linear probing for resolving collisions.


<sub>[Back to top](#table-of-contents)</sub>


### TreeMap

- It uses a red-black tree to store key-value pairs.
- Keeps the keys in sorted order, based on their natural ordering or a custom comparator.
- Slower access and retrieval time `O(log n)` compared to HashMap. 

>Suitable when the keys need to be sorted.

Example:

```java
import java.util.Map;
import java.util.TreeMap;

public class TreeMapDemo {
    public static void main(String[] args) {
        Map<String, Integer> treeMap = new TreeMap<>();

        // Adding key-value pairs to the TreeMap
        treeMap.put("Apple", 1);
        treeMap.put("Banana", 2);
        treeMap.put("Orange", 3);
        treeMap.put("Grapes", 4);

        // Accessing the value associated with a key
        int value = treeMap.get("Apple");
        System.out.println("Value associated with 'Apple': " + value);

        // Updating the value associated with a key
        treeMap.put("Apple", 10);
        System.out.println("Updated value associated with 'Apple': " + treeMap.get("Apple"));

        // Size of the TreeMap
        System.out.println("Size of the TreeMap: " + treeMap.size());

        // Iterating through the TreeMap using entrySet()
        System.out.println("Iterating through the TreeMap:");
        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>


### LinkedHashMap

- It uses a hash table along with a linked list to maintain the order of insertion.
- Access and retrieval time similar to HashMap.
- Maintains the order of insertion, offering predictable iteration order.

>Useful when you want to preserve the order of keys as they were inserted.

Example:

```java
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapDemo {
public static void main(String[] args) {
Map<String, Integer> linkedHashMap = new LinkedHashMap<>();

        // Adding key-value pairs to the LinkedHashMap
        linkedHashMap.put("Apple", 1);
        linkedHashMap.put("Banana", 2);
        linkedHashMap.put("Orange", 3);
        linkedHashMap.put("Grapes", 4);

        // Accessing the value associated with a key
        int value = linkedHashMap.get("Apple");
        System.out.println("Value associated with 'Apple': " + value);

        // Updating the value associated with a key
        linkedHashMap.put("Apple", 10);
        System.out.println("Updated value associated with 'Apple': " + linkedHashMap.get("Apple"));

        // Size of the LinkedHashMap
        System.out.println("Size of the LinkedHashMap: " + linkedHashMap.size());

        // Iterating through the LinkedHashMap using entrySet()
        System.out.println("Iterating through the LinkedHashMap:");
        for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>


## Choose the appropriate implementation

Choose the appropriate Map implementation based on your specific use case, considering the performance and ordering requirements of your application.

>If you need fast operations and don't care about the order, go with `HashMap`.

>For sorted elements, `TreeMap` is a good choice.

>If you need to maintain insertion order, `LinkedHashMap` fits the bill.

<sub>[Back to top](#table-of-contents)</sub>


### Comparison Table

| Characteristic            | HashMap             | TreeMap                     | LinkedHashMap                        |
|---------------------------|---------------------|-----------------------------|--------------------------------------|
| Underlying Data Structure | Hash Table          | Red-Black Tree              | Hash Table                           |
| Ordering                  | No ordering         | Sorted according to keys    | Insertion order                      |
| Duplicates Allowed        | No                  | No                          | No                                   |
| Null Elements Allowed     | Yes                 | No                          | Yes                                  |
| Iteration Performance     | `O(n)`              | `O(log n)`                  | `O(n)`                               |
| Use Cases                 | General-purpose Map | When keys need to be sorted | When order of insertion is important |


<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How does `HashMap` resolve hash collisions internally, according to this page?**
A: It uses separate chaining — each bucket holds a linked list of `Node` entries with the same index, and since Java 8, a bucket's list is converted into a balanced red-black tree once it exceeds the `TREEIFY_THRESHOLD` (8 entries), improving worst-case lookup time.

---

**Q: Why would I pick `LinkedHashMap` over a plain `HashMap`?**
A: `LinkedHashMap` preserves insertion order while offering access/retrieval performance close to `HashMap` — useful when you need predictable iteration order without paying for the sorted-tree overhead of a `TreeMap`.

---

**Q: Is `HashMap` thread-safe, and what should I use if it needs to be?**
A: No — as the page notes, `HashMap` is not thread-safe; use `ConcurrentHashMap` for concurrent access, since it provides thread safety without the heavy full-map locking of the legacy `Hashtable`.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Updated Java Collections API](enhanced-collections.md) — overview covering `Map` alongside `List`, `Set`, and `Queue`/`Deque`.
- [Set Interface](set-interface.md) — `HashSet` is internally backed by a `HashMap` and shares the same collision-handling mechanism.
- [List Interface](list-interface.md) — the ordered, index-based counterpart to `Map`'s key-based lookup.
- [Concurrency Utilities](../java-8/concurrency/concurrency-utilities.md) — covers `ConcurrentHashMap`, the thread-safe alternative mentioned here.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://www.geeksforgeeks.org/map-interface-java-examples/?ref=lbp
https://docs.oracle.com/javase/8/docs/api/java/util/Map.html

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#generics-and-collections) |
[Original Collections API](../java-1_2/collections-api.md) |
[Updated Collections](enhanced-collections.md)