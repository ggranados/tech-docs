# Generics

---

## Table of Contents
<!-- TOC -->
* [Generics](#generics)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
    * [Generic Classes](#generic-classes)
    * [Generic Methods](#generic-methods)
    * [Example](#example)
  * [Generics for Collections API](#generics-for-collections-api)
  * [Bounded Type Parameters](#bounded-type-parameters)
    * [Upper Bounded Type Parameters](#upper-bounded-type-parameters)
    * [Lower Bounded Type Parameters](#lower-bounded-type-parameters)
  * [Wildcards](#wildcards)
    * [Unbounded Wildcards](#unbounded-wildcards)
    * [Bounded Wildcards](#bounded-wildcards)
  * [Type Erasure](#type-erasure)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->


## Overview

JDK 5.0 introduced Java Generics with the aim of reducing bugs and adding an extra layer of abstraction over types.

Generics in Java allow you to create classes, interfaces, and methods that can work with different data types while maintaining type safety. They enable you to write reusable code that can handle a variety of data types without sacrificing type checking at compile-time.

### Generic Classes

To create a generic class, you use angle brackets `<>` and a placeholder type parameter. 

The type parameter can be any valid identifier, but by convention, it's often represented using a single uppercase letter. Inside the class, you can use this type parameter as if it were a regular data type.

```java
public class Box<T> {
    private T content;

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }
}
```



<sub>[Back to top](#table-of-contents)</sub>


### Generic Methods

You can also define generic methods within a non-generic class. To declare a generic method, you follow a similar syntax using angle brackets and type parameters before the return type of the method.

```java
public class GenericUtils {
    
    public static <T> void printArray(T[] array) {
        for (T item : array) {
            System.out.print(item + " ");
        }
        System.out.println();
    }
    
}
```

<sub>[Back to top](#table-of-contents)</sub>


### Example

Let's demonstrate how to use the Box generic class and the printArray generic method

```java
public class Main {
    public static void main(String[] args) {
        
        // Using the generic class
        Box<Integer> integerBox = new Box<>(42);
        System.out.println("Integer Box Content: " + integerBox.getContent());

        Box<String> stringBox = new Box<>("Hello, Generics!");
        System.out.println("String Box Content: " + stringBox.getContent());

        // Using the generic method
        Integer[] intArray = { 1, 2, 3, 4, 5 };
        String[] stringArray = { "Java", "is", "awesome" };

        System.out.print("Integer Array: ");
        GenericUtils.printArray(intArray);

        System.out.print("String Array: ");
        GenericUtils.printArray(stringArray);
    }
}

```

Output:

```bash
Integer Box Content: 42
String Box Content: Hello, Generics!
Integer Array: 1 2 3 4 5 
String Array: Java is awesome 
```

<sub>[Back to top](#table-of-contents)</sub>

## Generics for Collections API

Initially, Java 1.5 introduced Generics, which enabled the parameterization of type arguments for classes, including Collections API. This feature eliminated the need to use raw types, which could potentially lead to casting exceptions at runtime.

Before:

```java
List list = new ArrayList();
list.add("Java");
```

With generics:

```java
List<String> list = new ArrayList<String>();
list.add("Java");
```

In Java 1.5, when declaring and constructing objects, we had to specify the parameterized type in the constructor, which could become cumbersome and hard to read. For instance:

```java
Map<String, List<Map<String, Map<String, Integer>>>> cars = new HashMap<String, List<Map<String, Map<String, Integer>>>>();
```

The reason behind this approach was to maintain backward compatibility with raw types. The compiler needed to distinguish between raw types and generics. Here's an example showing both types:

```java
List<String> generics = new ArrayList<String>();
List<String> raws = new ArrayList();
```

Using raw types in constructors will prompt a warning message from the compiler, reminding us to parameterize the type:

```
ArrayList is a raw type. References to generic type ArrayList<E> should be parameterized
```

In Java 1.7, the Diamond Operator was introduced, which brought type inference and reduced assignment verbosity when using generics. With the diamond operator, the compiler infers the appropriate constructor declaration:

```java
List<String> cars = new ArrayList<>();
```

This enhancement significantly improved code readability and made the usage of generics more concise.

- See also: [Collections API](../java-1_2/collections-api.md)
- See also: [Updated Collection API](enhanced-collections.md)
- See also: [Diamond Operator](../java-7/diamond-operator.md)

<sub>[Back to top](#table-of-contents)</sub>


## Bounded Type Parameters

Bounded type parameters allow you to **restrict the types that can be used as generic arguments** to a specific range of types. 

There are two types of bounded type parameters: upper bounded and lower bounded.

<sub>[Back to top](#table-of-contents)</sub>


### Upper Bounded Type Parameters

An upper bounded type parameter is specified using the `extends` keyword. 

It restricts the generic type argument to be **a specific type or any of its subclasses**.

```java
// Example: A generic class with an upper bounded type parameter
class Box<T extends Number> {
    private T value;

    public Box(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
```

Usage:

```java
// Usage of the generic class with an upper bounded type parameter
Box<Integer> integerBox = new Box<>(42); // Valid, Integer extends Number
    
Box<Double> doubleBox = new Box<>(3.14); // Valid, Double extends Number

// Box<String> stringBox = new Box<>("Hello"); // Invalid, String does not extend Number
```

<sub>[Back to top](#table-of-contents)</sub>


### Lower Bounded Type Parameters

A lower bounded type parameter is specified using the `super` keyword. 

It restricts the generic type argument to be **a specific type or any of its superclasses**.

```java
// Example: A generic method with a lower bounded type parameter
class Box<T> {
    //...
    
    public void setValue(List<? super T> list, T value) {
        list.add(value);
    }
}
````

Usage:

```java
// Usage of the generic method with a lower bounded type parameter
List<Number> numberList = new ArrayList<>();

Box<Integer> integerBox = new Box<>();
integerBox.setValue(numberList, 42); // Valid, Integer is a subtype of Number

Box<Double> doubleBox = new Box<>();
doubleBox.setValue(numberList, 3.14); // Valid, Double is a subtype of Number

// Uncommenting the below lines would show that it is not allowed
// Box<String> stringBox = new Box<>();
// stringBox.setValue(numberList, "Hello"); // Invalid, String is not a subtype of Number

System.out.println(numberList); // Output: [42, 3.14]
```

<sub>[Back to top](#table-of-contents)</sub>

## Wildcards

Wildcards are denoted using the `?` symbol and are used to represent an unknown type. Wildcards are helpful when you want to define methods that operate on generic types without knowing the exact type.

### Unbounded Wildcards

Unbounded wildcards are denoted as <?>, and they represent an unknown type that can be anything.

```java
// Example: A method that prints elements from a list using an unbounded wildcard
class ListPrinter {
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }
}
 ```

Usage:

```java
// Usage of the method with an unbounded wildcard
List<Integer> integerList = Arrays.asList(1, 2, 3);
List<String> stringList = Arrays.asList("A", "B", "C");

ListPrinter.printList(integerList); // Valid, List<Integer> matches List<?>

ListPrinter.printList(stringList); // Valid, List<String> matches List<?>
```

<sub>[Back to top](#table-of-contents)</sub>

### Bounded Wildcards

Bounded wildcards can be upper bounded or lower bounded, just like type parameters.

```java
// Example: A method that calculates the sum of numbers in a list using an upper bounded wildcard
class MathUtils {
    public static double sumOfList(List<? extends Number> list) {
        double sum = 0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }
}
```

Usage:

```java
// Usage of the method with an upper bounded wildcard
List<Integer> integerList = Arrays.asList(1, 2, 3);
List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);

MathUtils.sumOfList(integerList); // Valid, List<Integer> extends List<? extends Number>
        
MathUtils.sumOfList(doubleList); // Valid, List<Double> extends List<? extends Number>

// MathUtils.sumOfList(stringList); // Invalid, List<String> does not extend Number
```

<sub>[Back to top](#table-of-contents)</sub>

## Type Erasure

Generics were added to Java to ensure type safety. And to ensure that generics won't cause overhead at runtime, the compiler applies a process called type erasure on generics at compile time.

Type erasure removes all type parameters and replaces them with their bounds or with Object if the type parameter is unbounded. This way, the bytecode after compilation contains only normal classes, interfaces and methods, ensuring that no new types are produced. Proper casting is applied as well to the Object type at compile time.

This is an example of type erasure:

```java
public <T> List<T> genericMethod(List<T> list) {
return list.stream().collect(Collectors.toList());
}
```

With type erasure, the unbounded type T is replaced with Object:

```java
// for illustration
public List<Object> withErasure(List<Object> list) {
return list.stream().collect(Collectors.toList());
}

// which in practice results in
public List withErasure(List list) {
return list.stream().collect(Collectors.toList());
}
```

If the type is bounded, the type will be replaced by the bound at compile time:

```java
public <T extends Building> void genericMethod(T t) {
...
}
```

and would change after compilation:

```java
public void genericMethod(Building t) {
...
}
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What's the difference between an upper bounded (`<T extends Number>`) and a lower bounded (`<? super T>`) type parameter?**
A: An upper bound restricts the type argument to a specific type or its subclasses (useful when you only need to read/produce values); a lower bound restricts it to a specific type or its superclasses (useful when you need to write/consume values) — this is the PECS rule: "Producer Extends, Consumer Super".

---

**Q: What does type erasure actually do to generic type parameters, and why does Java do it?**
A: At compile time, the compiler replaces each type parameter with its bound (or `Object` if unbounded), inserts the necessary casts, and emits ordinary bytecode with no generic type information left in it — this keeps generics backward compatible with pre-Java-5 code and avoids any runtime overhead.

---

**Q: Why did the diamond operator matter once generics existed?**
A: Before Java 7, you had to repeat the type argument on both sides of an assignment (`new ArrayList<String>()`); the diamond operator lets the compiler infer it from the declared type, cutting the verbosity generics had introduced.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Original Collections API](../java-1_2/collections-api.md) — the pre-generics API that motivated this feature.
- [Updated Java Collections API](enhanced-collections.md) — how generics reshaped the Collections Framework in practice.
- [Diamond Operator](../java-7/diamond-operator.md) — the Java 7 syntax simplification for generic instantiation discussed here.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://www.baeldung.com/java-generics

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#generics-and-collections) |
[Java 5](../versions.md#java-5) |

---
