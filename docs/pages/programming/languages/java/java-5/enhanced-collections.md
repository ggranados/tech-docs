# Updated Collections API

---

## Table of Contents
<!-- TOC -->
* [Updated Collections API](#updated-collections-api)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Using Common Collection APIs](#using-common-collection-apis)
    * [List Interface](#list-interface)
    * [Set Interface](#set-interface)
    * [Map Interface](#map-interface)
    * [Queue and Deque Interface](#queue-and-deque-interface)
    * [Comparison Table](#comparison-table)
  * [Comparing](#comparing)
    * [Comparable](#comparable)
      * [Consistent compareTo() and equals()](#consistent-compareto-and-equals-)
    * [Comparator](#comparator)
    * [Sorting](#sorting)
    * [Searching and Other Utilities](#searching-and-other-utilities)
      * [Binary Search](#binary-search)
      * [Finding Max/Min Elements](#finding-maxmin-elements)
      * [Reversing Collections](#reversing-collections)
      * [Searching for Occurrences](#searching-for-occurrences)
      * [Searching with Custom Comparator](#searching-with-custom-comparator)
      * [Other Methods that Accept Custom Comparator](#other-methods-that-accept-custom-comparator)
        * [Sorting Methods:](#sorting-methods)
        * [Searching Methods:](#searching-methods)
        * [Searching for Sublist:](#searching-for-sublist)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Overview

- See also [Original Collections API](../java-1_2/collections-api.md)

The addition of **generics** in Java 5 brought significant enhancements to the Collections Framework.

- See also: [Generics](generics.md)

The **diamond operator** is a feature introduced in Java 7 to simplify the usage of generics in collections.

- See also: [Diamond Operator](../java-7/diamond-operator.md)

<sub>[Back to top](#table-of-contents)</sub>

## Using Common Collection APIs

Common usage of the Collections API in modern Java:

![img.png](../../../../../img/collections-hierarchy.png)

![img.png](../../../../../img/collections-hierarchy-map.png)


<sub>[Back to top](#table-of-contents)</sub>

### List Interface

The List interface represents an **ordered** collection of elements and **allows duplicate elements**. 

Common implementations include: `ArrayList` and `LinkedList`.

```java
List<String> list = new ArrayList<>();
list.add("Java");
list.add("Python");
list.add("C++");
System.out.println(list); // Output: [Java, Python, C++]
```

- See also: [List Interface](list-interface.md)

<sub>[Back to top](#table-of-contents)</sub>

### Set Interface

The Set interface represents an **unordered** collection of elements with **no duplicates**. 

Common implementations include: `HashSet` and `TreeSet`.

```java
Set<Integer> set = new HashSet<>();
set.add(1);
set.add(2);
set.add(1); // This will not be added as duplicates are not allowed
System.out.println(set); // Output: [1, 2]
```

- See also: [Set Interface](set-interface.md)

<sub>[Back to top](#table-of-contents)</sub>


### Map Interface

The Map interface represents a mapping of **keys to values**, where **each key is unique**. 

Common implementations include `HashMap` and `TreeMap`.

```java
Map<String, Integer> map = new HashMap<>();
map.put("apple", 10);
map.put("orange", 5);
System.out.println(map.get("apple")); // Output: 10
```
- Se also: [Map Interface](map-interface.md)

<sub>[Back to top](#table-of-contents)</sub>


### Queue and Deque Interface

The Queue and Deque interfaces were added to the Java Collections Framework in Java 1.5 (also known as Java 5) as part of the major enhancements made to the collections API.

- See also: [Java 5](../versions.md#java-5)

The Queue interface represents a collection that holds elements **in a specific order**. Typically processed in a *first-in-first-out (FIFO)* manner

The Deque interface interface extends the `Queue` interface and represents a queue where you *can add or remove elements from both ends* *first-in-first-out (FIFO)* and *last-in-first-out (LIFO)*. You can treat a `Deque` *as both a queue and a stack*

Common implementations include: `LinkedList` and `PriorityQueue`.

```java
Queue<String> queue = new LinkedList<>();
queue.offer("First");
queue.offer("Second");
System.out.println(queue.poll()); // Output: First
```

- See also: [Queue and Deque Interfaces](queue-deque-interfaces.md)

<sub>[Back to top](#table-of-contents)</sub>


### Comparison Table

| Type  | duplicate elements? | ordered?                         | keys and values? | add/remove in order? |
|-------|---------------------|----------------------------------|------------------|----------------------|
| List  | Yes                 | Yes (by index)                   | No               | No                   |
| Map   | Yes (for values)    | No                               | Yes              | No                   |
| Queue | Yes                 | Yes (retrieved in defined order) | No               | Yes                  |
| Set   | No                  | No                               | No               | No                   |



<sub>[Back to top](#table-of-contents)</sub>

## Comparing

Comparing objects in Java 8 collections typically involves implementing the `Comparable` interface or using a custom `Comparator` to define the comparison logic.

<sub>[Back to top](#table-of-contents)</sub>


### Comparable
The Comparable interface allows you to define a natural ordering for the elements in a collection. To use this interface, the class of the objects you want to compare must implement it and override the `compareTo()` method.

Example of implementing Comparable:

```java
public class Person implements Comparable<Person> {
private String name;
private int age;

    // Constructors, getters, setters

    @Override
    public int compareTo(Person otherPerson) {
        return this.name.compareTo(otherPerson.name);
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>


#### Consistent compareTo() and equals() 
When you implement the Comparable interface in a class, you introduce new logic to determine equality between objects. The `compareTo()` method is used, and it returns 0 if two objects are considered equal, whereas the `equals()` method returns true if the two objects are equal.

>It is highly recommended to ensure that your Comparable classes follow this consistency with the equals() method because some collection classes may not behave predictably if the compareTo() and equals() methods are not consistent

<sub>[Back to top](#table-of-contents)</sub>


### Comparator
If the class you want to compare does not implement the `Comparable` interface or if you want to define multiple comparison criteria, you can use a custom `Comparator`. A `Comparator` allows you to specify different comparison logic for a class without modifying the original class.

Example of using Comparator:

```java
public class PersonAgeComparator implements Comparator<Person> {
  @Override
  public int compare(Person person1, Person person2) {
    return Integer.compare(person1.getAge(), person2.getAge());
  }
}
```

<sub>[Back to top](#table-of-contents)</sub>


### Sorting

Sorting a collection in Java 8 can be done using the `sort()` method provided by the List interface. For custom sorting, you can pass a `Comparator` as an argument to the sort() method.

- Example of sorting with `Comparable`:

```java
List<Person> personList = new ArrayList<>();
// Add elements to the list
        
Collections.sort(personList); // This will use the natural order defined by the compareTo method in the Person class
```
        
- Example of sorting with `Comparator`:

Considering this implementation of PersonAgeComparator:

```java
import java.util.Comparator;

public class PersonAgeComparator implements Comparator<Person> {
    @Override
    public int compare(Person person1, Person person2) {
        // Compare by age (ascending order)
        return Integer.compare(person1.getAge(), person2.getAge());
    }
}
```

Sorting by it will be:

```java
List<Person> personList = new ArrayList<>();
// Add elements to the list
personList.add(new Person("Alice", 25));
personList.add(new Person("Bob", 20));
personList.add(new Person("Charlie", 30));
        
Comparator<Person> ageComparator = new PersonAgeComparator();
Collections.sort(personList, ageComparator); // This will sort the list based on the age field

// Now the personList is sorted based on age (ascending order)
for (Person person : personList) {
    System.out.println(person.getName() + " - Age: " + person.getAge());
}
```

Output:   

```
Bob - Age: 20
Alice - Age: 25
Charlie - Age: 30
```
     
In Java 8, the List interface also has a new sort() method that takes a Comparator directly:

```java
List<Person> personList = new ArrayList<>();
// Add elements to the list

personList.sort(ageComparator);
```

And also Comparator  implementation can be reduced by calling `Comparator` utility method `comparing` or `comparingInt` in this case, plus method reference for `Person` method `getAge()`:

```java
personList.sort(Comparator.comparingInt(Person::getAge));
```

Finally:

```java
public static void main(String[] args) {
    List<Person> personList = new ArrayList<>();

    // Add elements to the list (example data)
    personList.add(new Person("Alice", 25));
    personList.add(new Person("Bob", 20));
    personList.add(new Person("Charlie", 30));

    personList.sort(Comparator.comparing(Person::getAge));

    // Now the personList is sorted based on age (ascending order)
    for (Person person : personList) {
        System.out.println(person.getName() + " - Age: " + person.getAge());
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>



### Searching and Other Utilities

The Collections class offers methods for binary search, finding max/min elements, reversing collections, and more.

```java
List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 6);
int index = Collections.binarySearch(numbers, 8);
System.out.println(index); // Output: 4 (index of element 8)

int maxElement = Collections.max(numbers);
System.out.println(maxElement); // Output: 8
```


<sub>[Back to top](#table-of-contents)</sub>

#### Binary Search
Binary search requires that the list be sorted in ascending order before performing the search.

```java
import java.util.*;

public class BinarySearchExample {
  public static void main(String[] args) {
    List<Integer> numbers = Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15, 17, 19);

    int index = Collections.binarySearch(numbers, 9);

    if (index >= 0) {
      System.out.println("Element found at index: " + index);
    } else {
      System.out.println("Element not found.");
    }
  }
}
```

Output:

```
Element found at index: 4
```

<sub>[Back to top](#table-of-contents)</sub>

#### Finding Max/Min Elements
To find the maximum and minimum elements in a collection, you can use `Collections.max()` and `Collections.min()` methods.

```java
import java.util.*;

public class MaxMinExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(10, 2, 8, 4, 6);

        int max = Collections.max(numbers);
        int min = Collections.min(numbers);

        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
    }
}

```

Output:

```
Max: 10
Min: 2
```

<sub>[Back to top](#table-of-contents)</sub>

#### Reversing Collections
To reverse the order of elements in a list, you can use the `Collections.reverse()` method.


```java
import java.util.*;

public class ReverseExample {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("Original List: " + numbers);

        Collections.reverse(numbers);
        System.out.println("Reversed List: " + numbers);
    }
}
```

Output:

```
Original List: [1, 2, 3, 4, 5]
Reversed List: [5, 4, 3, 2, 1]
```

<sub>[Back to top](#table-of-contents)</sub>

#### Searching for Occurrences
You can use `Collections.frequency()` to count the occurrences of an element in a collection.

```java
import java.util.*;

public class FrequencyExample {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("apple", "banana", "orange", "apple", "grape");

        int frequency = Collections.frequency(words, "apple");
        System.out.println("Frequency of 'apple': " + frequency);
    }
}
```

Output:

```
Frequency of 'apple': 2
```

<sub>[Back to top](#table-of-contents)</sub>

#### Searching with Custom Comparator

If you need to search or perform other operations with a custom sorting order, you can use the `Collections.binarySearch()` and other methods that take a Comparator as an argument.

Suppose we have a list of strings representing names, and we want to perform a binary search to find a specific name in the list, but we want the search to be case-insensitive. For that, we can use a custom comparator that ignores the case when comparing strings

```java
import java.util.*;

public class CustomComparatorExample {
  public static void main(String[] args) {
    List<String> names = Arrays.asList("John", "Alice", "bob", "Tom", "jane", "Michael");
    String targetName = "Jane";

    // Sorting the list in a case-insensitive manner using a custom comparator
    Collections.sort(names, new CaseInsensitiveComparator());

    // Performing binary search with custom comparator
    int index = Collections.binarySearch(names, targetName, new CaseInsensitiveComparator());

    if (index >= 0) {
      System.out.println("Name found at index: " + index);
      System.out.println(names);
      System.out.println(names.get(index));
    } else {
      System.out.println("Name not found.");
    }
  }

  // Custom Comparator that ignores the case of strings during comparison
  static class CaseInsensitiveComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
      return str1.compareToIgnoreCase(str2);
    }
  }
}

```

Output:

```
Name found at index: 2
[Alice, bob, jane, John, Michael, Tom]
jane
```

<sub>[Back to top](#table-of-contents)</sub>


#### Other Methods that Accept Custom Comparator

Apart from `Collections.binarySearch()` that takes a `Comparator` as an argument for custom sorting order, there are several other methods in the Java Collections framework that also accept a `Comparator to customize the sorting and searching behavior. Some of these methods include:

<sub>[Back to top](#table-of-contents)</sub>

##### Sorting Methods:

- `Collections.sort(List<T> list, Comparator<? super T> c)`:
Sorts the elements of the list using the specified Comparator.


- `Collections.sort(List<T> list, int fromIndex, int toIndex, Comparator<? super T> c)`:
Sorts the specified range of elements in the list using the specified Comparator.

<sub>[Back to top](#table-of-contents)</sub>


##### Searching Methods:

- `Collections.binarySearch(List<? extends T> list, T key, Comparator<? super T> c)`: Searches for the specified element in the list using the specified Comparator.
Max/Min Methods:


- `Collections.max(Collection<? extends T> coll, Comparator<? super T> c)`: Returns the maximum element of the given collection according to the provided Comparator.

- `Collections.min(Collection<? extends T> coll, Comparator<? super T> c)`: Returns the minimum element of the given collection according to the provided Comparator.

<sub>[Back to top](#table-of-contents)</sub>

##### Searching for Sublist:

- `Collections.indexOfSubList(List<?> source, List<?> target, Comparator<?> c)`: Returns the starting index of the first occurrence of the target sublist within the source list using the specified Comparator.

<sub>[Back to top](#table-of-contents)</sub>


---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why must `compareTo()` be consistent with `equals()` when implementing `Comparable`?**
A: Sorted collections like `TreeSet` and `TreeMap` treat two elements as duplicates when `compareTo()` returns 0, regardless of what `equals()` says; if the two methods disagree, elements you'd expect to coexist can silently overwrite each other or vanish.

---

**Q: When would I use a `Comparator` instead of implementing `Comparable` directly on a class?**
A: When you don't own the class (so you can't add `compareTo()`), or when you need more than one ordering — `Comparable` only gives a class a single natural order, while you can define as many `Comparator` implementations as you need.

---

**Q: What precondition does `Collections.binarySearch()` require, and what happens if I skip it?**
A: The list must already be sorted in ascending order matching the comparator (or natural order) used for the search; running binary search on an unsorted list gives undefined, unreliable results instead of throwing an error.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Original Collections API](../java-1_2/collections-api.md) — the pre-generics API this page replaces.
- [List Interface](list-interface.md) — implementation details for `List`, referenced throughout the comparing/sorting examples.
- [Set Interface](set-interface.md) — implementation details for `Set`, one of the four interfaces summarized in the comparison table.
- [Map Interface](map-interface.md) — implementation details for `Map`, the other interface in the comparison table.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://www.geeksforgeeks.org/how-to-learn-java-collections-a-complete-guide/

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#reflection-and-dynamic-class-loading) |
[Java 7](../versions.md#java-7) 

---
