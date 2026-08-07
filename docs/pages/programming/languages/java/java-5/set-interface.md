# Set Interface

---

## Table of Contents
<!-- TOC -->
* [Set Interface](#set-interface)
  * [Table of Contents](#table-of-contents)
  * [Set Interface Characteristics](#set-interface-characteristics)
  * [Most Used Set Implementations](#most-used-set-implementations)
    * [HashSet](#hashset)
      * [Hash Collision](#hash-collision)
    * [TreeSet](#treeset)
    * [LinkedHashSet](#linkedhashset)
  * [Choose the appropriate implementation](#choose-the-appropriate-implementation)
    * [Comparison Table](#comparison-table)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Set Interface Characteristics
In Java, the `Set` interface is a part of the Java Collections Framework and is used to store a collection of *unique elements*, meaning that duplicates are not allowed. 

The Set interface is particularly useful when you need to ensure the uniqueness of elements in a collection.

Key characteristics of the Set interface:

- **Uniqueness**
A Set *cannot contain duplicate elements*. Adding an element that already exists in the Set will have no effect.


- **No Indexing**: Sets do not have a specific position for elements, so you *cannot access elements by index*.


- **Ordering**: Unlike Lists, Sets *do not maintain the order of elements*. The elements are not stored in any predictable sequence.


- **Equality of Objects**: The Set interface uses the `equals()` method to determine whether two objects are equal or not, so you need to ensure that the objects in the `Set` have a proper implementation of the `equals()` method.

<sub>[Back to top](#table-of-contents)</sub>

## Most Used Set Implementations
Java provides several implementations of the Set interface. Let's go through the most commonly used ones:

<sub>[Back to top](#table-of-contents)</sub>

### HashSet

- Implements the Set interface using a hash table.
- Offers constant-time performance for basic operations (`add()`, `remove()`, `contains()`).
- Does not guarantee any specific order of elements.
- Allows null elements (only one null element is allowed).

> It's preferred when you need a fast and unordered Set implementation and uniqueness is essential.

Example:

```java
import java.util.Set;
import java.util.HashSet;

public class SetDemo {
    public static void main(String[] args) {
// Demonstration of HashSet
        Set<String> hashSet = new HashSet<>();

        hashSet.add("Apple");
        hashSet.add("Banana");
        hashSet.add("Orange");
        hashSet.add("Grapes");

        System.out.println("HashSet elements: " + hashSet);
        System.out.println("HashSet size: " + hashSet.size());

        hashSet.remove("Orange");
        System.out.println("HashSet after removing 'Orange': " + hashSet);

        System.out.println("Iterating through HashSet:");
        for (String fruit : hashSet) {
            System.out.println(fruit);
        }
        System.out.println();

    }
}
```

#### Hash Collision

A hash collision occurs in a hash table or hash-based data structure when two different keys produce the same hash code or index in the underlying data storage.

`HashSet` uses separate chaining. When two distinct objects have the same hash code, they will be placed in the same bucket in the internal hash table.

- See also: [Hash Collision](map-interface.md#hash-collision)

<sub>[Back to top](#table-of-contents)</sub>


### TreeSet

- Implements the Set interface using a red-black tree.
- Elements are stored in sorted (ascending) order.
- Provides `O(log n)` time complexity for basic operations.
- Does not allow null elements.
 
>It's preferred when you need elements to be sorted in natural order or a custom comparator-defined order.


Example:

```java
import java.util.Set;
import java.util.TreeSet;

public class SetDemo {
    public static void main(String[] args) {
// Demonstration of TreeSet
        Set<String> treeSet = new TreeSet<>();

        treeSet.add("Apple");
        treeSet.add("Banana");
        treeSet.add("Orange");
        treeSet.add("Grapes");

        System.out.println("TreeSet elements: " + treeSet);
        System.out.println("TreeSet size: " + treeSet.size());

        treeSet.remove("Orange");
        System.out.println("TreeSet after removing 'Orange': " + treeSet);

        System.out.println("Iterating through TreeSet:");
        for (String fruit : treeSet) {
            System.out.println(fruit);
        }
        System.out.println();

    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

### LinkedHashSet

- Implements the Set interface using a hash table with a linked list running through it.
- Maintains insertion order of elements, which means elements are stored in the order they were added.
- Provides slightly slower performance than HashSet but faster iteration.
- Allows null elements (only one null element is allowed).

>Most preferred when you need a `Set` that maintains insertion order while preserving uniqueness.

Example:

```java
import java.util.LinkedHashSet;
import java.util.Set;

public class SetDemo {
    public static void main(String[] args) {

        // Demonstration of LinkedHashSet
        Set<String> linkedHashSet = new LinkedHashSet<>();

        linkedHashSet.add("Apple");
        linkedHashSet.add("Banana");
        linkedHashSet.add("Orange");
        linkedHashSet.add("Grapes");

        System.out.println("LinkedHashSet elements: " + linkedHashSet);
        System.out.println("LinkedHashSet size: " + linkedHashSet.size());

        linkedHashSet.remove("Orange");
        System.out.println("LinkedHashSet after removing 'Orange': " + linkedHashSet);

        System.out.println("Iterating through LinkedHashSet:");
        for (String fruit : linkedHashSet) {
            System.out.println(fruit);
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

## Choose the appropriate implementation

Always consider the trade-offs and performance characteristics to make an informed decision.

>If you need fast operations and don't care about the order, go with `HashSet`.

>For sorted elements, `TreeSet` is a good choice.

>If you need to maintain insertion order, `LinkedHashSet` fits the bill.

<sub>[Back to top](#table-of-contents)</sub>

### Comparison Table

| Characteristic            | 	HashSet                   | 	TreeSet                           | 	LinkedHashSet                        |
|---------------------------|----------------------------|------------------------------------|---------------------------------------|
| Underlying Data Structure | 	Hash Table                | 	Red-Black Tree                    | 	Hash Table                           |
| Ordering                  | 	No ordering               | 	Sorted according to natural order | 	Insertion order                      |
| Duplicates Allowed        | 	No                        | 	No                                | 	No                                   |
| Null Elements Allowed     | 	Yes                       | 	No                                | 	Yes                                  |
| Iteration Performance     | 	`O(n)`                    | 	`O(log n)`	                       | `O(n)`                                |
| Use Cases	                | When order doesn't matter	 | When elements need to be sorted    | 	When order of insertion is important |


<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why must objects placed into a `HashSet` have a correct `equals()`/`hashCode()` implementation?**
A: `HashSet` uses `hashCode()` to pick a bucket and `equals()` to check for duplicates within that bucket; if the two methods are inconsistent (e.g. two "equal" objects with different hash codes), the set can silently accept duplicates or fail to find elements that are logically present.

---

**Q: What ordering guarantee, if any, does `TreeSet` provide over `HashSet`?**
A: `TreeSet` keeps elements in sorted order (natural ordering or a supplied comparator) at `O(log n)` cost per operation; `HashSet` gives no ordering guarantee at all but offers faster average-case `O(1)` operations.

---

**Q: How does `HashSet` handle hash collisions, per this page's cross-reference to `HashMap`?**
A: The same way — `HashSet` is backed internally by a `HashMap`, so it uses separate chaining (a linked list, or a red-black tree past the treeify threshold) within each bucket to store colliding entries.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Updated Java Collections API](enhanced-collections.md) — overview covering `Set` alongside `List`, `Map`, and `Queue`/`Deque`.
- [Map Interface](map-interface.md) — `HashSet`'s internal implementation detail (hash collision handling) referenced on this page.
- [List Interface](list-interface.md) — the duplicate-allowing, ordered counterpart to `Set`.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://www.geeksforgeeks.org/set-in-java/?ref=lbp
https://docs.oracle.com/javase/8/docs/api/java/util/Set.html

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#generics-and-collections) |
[Original Collections API](../java-1_2/collections-api.md) |
[Updated Collections](enhanced-collections.md)

---
