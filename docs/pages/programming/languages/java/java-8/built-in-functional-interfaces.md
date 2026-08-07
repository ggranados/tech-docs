# Built-in Functional Interfaces

---

## Table of Contents
<!-- TOC -->
* [Built-in Functional Interfaces](#built-in-functional-interfaces)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
    * [Consumer](#consumer)
    * [Supplier](#supplier)
    * [Function](#function)
    * [Predicate](#predicate)
    * [UnaryOperator](#unaryoperator)
    * [BinaryOperator](#binaryoperator)
    * [BiFunction](#bifunction)
    * [BiPredicate](#bipredicate)
  * [Primitive Functional Interfaces](#primitive-functional-interfaces)
    * [Predicate](#predicate-1)
    * [Consumer](#consumer-1)
    * [Function](#function-1)
    * [Supplier](#supplier-1)
    * [UnaryOperator](#unaryoperator-1)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->


## Overview

Functional interfaces, which are gathered in the `java.util.function` [^1] package, satisfy most developers' needs in providing target types for lambda expressions and method references. Each of these interfaces is general and abstract, making them easy to adapt to almost any lambda expression. Developers should explore this package before creating new functional interfaces.


### Consumer

`Consumer<T>`

Represents an operation that takes in one argument of type T and returns no result.

The real outcome is the side-effects it produces. Since it's a functional interface, you can pass a lambda expression wherever a Consumer is expected.

Method: `void accept(T t)`

```java
class Product {
  private double price = 0.0;

  public void setPrice(double price) {
    this.price = price;
  }

  public void printPrice() {
    System.out.println(price);
  }
}

public class Test {
  public static void main(String[] args) {
    Consumer<Product> updatePrice = p -> p.setPrice(5.9);
    Product p = new Product();
    updatePrice.accept(p);
    p.printPrice();
  }
}
```

<sub>[Back to top](#table-of-contents)</sub>

### Supplier

`Supplier<T>`

Represents a supplier of results of type T.

This functional interface does the opposite of the Consumer, it takes no arguments but it returns some value. It may return different values when it is being called more than once. Since it's a functional interface, you can pass a lambda expression wherever a Supplier is expected.

Method: `T get()`

```java
public class Program {
    public static void main(String[] args) {
        int n = 3;
        display(() -> n + 10);
        display(() -> n + 100);
    }

    static void display(Supplier<Integer> arg) {
        System.out.println(arg.get());
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

### Function

`Function<T, R>`

Represents a function that takes an argument of type T and returns a result of type R.

One use, for example, it's to convert or transform from one object to another. Since it's a functional interface, you can pass a lambda expression wherever a Function is expected.

Method: `R apply(T t)`

```java
public class Test {
  public static void main(String[] args) {
    int n = 5;
    modifyTheValue(n, val-> val + 10);
    modifyTheValue(n, val-> val * 100);
  }

  static void modifyValue(int v, Function<Integer, Integer> function){
    int result = function.apply(v);
    System.out.println(newValue);
  }

}
```

<sub>[Back to top](#table-of-contents)</sub>

### Predicate

`Predicate<T>`

Represents a predicate (boolean-valued function) that takes an argument of type T and returns a boolean result.

Method: `boolean test(T t)`

```java
import java.util.function.Predicate;

public class PredicateExample {
  public static void main(String[] args) {
    Predicate<Integer> isEven = (num) -> num % 2 == 0;
    int number = 10;
    if (isEven.test(number)) {
      System.out.println(number + " is even.");
    } else {
      System.out.println(number + " is odd.");
    }
  }
}
````


<sub>[Back to top](#table-of-contents)</sub>

### UnaryOperator

`UnaryOperator<T>`

Represents an operation on a single operand of type T, producing a result of the same type T.

Method: `T apply(T t)`

```java
import java.util.function.UnaryOperator;

public class UnaryOperatorExample {
  public static void main(String[] args) {
    UnaryOperator<Integer> incrementByFive = (num) -> num + 5;
    int result = incrementByFive.apply(10);
    System.out.println("Result: " + result);
  }
}
```

<sub>[Back to top](#table-of-contents)</sub>

### BinaryOperator

 `BinaryOperator<T>`

Represents an operation upon two operands of type T, producing a result of type T.

Method: `T apply(T t1, T t2)`

```java
import java.util.function.BinaryOperator;

public class BinaryOperatorExample {
    public static void main(String[] args) {
        BinaryOperator<Integer> sum = (a, b) -> a + b;
        int result = sum.apply(10, 20);
        System.out.println("Sum: " + result);
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

### BiFunction

`BiFunction<T, U, R>`

Represents a function that takes two arguments of types T and U, and returns a result of type R.

Method: `R apply(T t, U u)`

```java
import java.util.function.BiFunction;

public class BiFunctionExample {
    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        int result = add.apply(10, 20);
        System.out.println("Result: " + result);
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

### BiPredicate

`BiPredicate<T, U>`

Represents a predicate (boolean-valued function) that takes two arguments of types T and U, and returns a boolean result.

Method: `boolean test(T t, U u)`

```java
import java.util.function.BiPredicate;

public class BiPredicateExample {
    public static void main(String[] args) {
        BiPredicate<Integer, Integer> areEqual = (a, b) -> a.equals(b);
        int num1 = 10;
        int num2 = 10;
        if (areEqual.test(num1, num2)) {
            System.out.println("The numbers are equal.");
        } else {
            System.out.println("The numbers are not equal.");
        }
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Primitive Functional Interfaces

Java provides specialized versions of the functional interfaces to avoid autoboxing operations when the inputs or outputs are primitives.

For example, instead of using

```java
Predicate<Integer> p = i -> i > 10;
```

You can use

```java
IntPredicate p = i -> i > 10;
```

In general, the names of functional interfaces that have a primitive version for the input parameter are preceded by the primitive type, like `IntPredicate`. The Function interface also has variants for the output parameter like `ToIntFunction<T>`.

Summary of the primitive version of functional interfaces:

<sub>[Back to top](#table-of-contents)</sub>

### Predicate
- `IntPredicate`: Predicate of one int-valued argument.
- `LongPredicate`: Predicate of one long-valued argument.
- `DoublePredicate`: Predicate of one double-valued argument.

<sub>[Back to top](#table-of-contents)</sub>

### Consumer
- `IntConsumer`: Operation that accepts a single int-valued argument and returns no result.
- `LongConsumer`: Operation that accepts a single long-valued argument and returns no result.
- `DoubleConsumer`: Operation that accepts a single double-valued argument and returns no result.

<sub>[Back to top](#table-of-contents)</sub>

### Function
- `IntFunction<R>`: Function that accepts an int-valued argument and produces a result.
- `IntToDoubleFunction`: Function that accepts an int-valued argument and produces a double-valued result.
- `IntToLongFunction`: Function that accepts an int-valued argument and produces a long-valued result.
- `LongFunction<R>`: Function that accepts a long-valued argument and produces a result.
- `LongToDoubleFunction`: Function that accepts a long-valued argument and produces a double-valued result.
- `LongToIntFunction`: Function that accepts a long-valued argument and produces an int-valued result.
- `DoubleFunction<R>`: Function that accepts a double-valued argument and produces a result.
- `ToIntFunction<T>`: Function that produces an int-valued result.
- `ToDoubleFunction<T>`: Function that produces a double-valued result.
- `ToLongFunction<T>`: Function that produces a long-valued result.

<sub>[Back to top](#table-of-contents)</sub>

### Supplier
- `BooleanSupplier`: Supplier of boolean-valued results.
- `IntSupplier`: Supplier of int-valued results.
- `LongSupplier`: Supplier of long-valued results.
- `DoubleSupplier`: Supplier of double-valued results.

<sub>[Back to top](#table-of-contents)</sub>

### UnaryOperator
- `IntUnaryOperator`: Function operation on a single int-valued operand that produces an int-valued result.
- `LongUnaryOperator`: Function operation on a single long-valued operand that produces a long-valued result.
- `DoubleUnaryOperator`:  Function operation on a single double-valued operand that produces a double-valued result.

<sub>[Back to top](#table-of-contents)</sub>

---

- See also: [java.util.function Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why does Java provide primitive-specialized interfaces like `IntPredicate` instead of just using `Predicate<Integer>`?**
A: To avoid autoboxing/unboxing overhead. Boxing every primitive into a wrapper object on each call is wasteful in hot loops, so Java ships primitive variants that operate directly on `int`, `long`, or `double`.

---

**Q: What's the difference between `Function<T, R>` and `UnaryOperator<T>`?**
A: `UnaryOperator<T>` extends `Function<T, T>` — input and output must be the same type. Use it whenever a transformation preserves the type, since it communicates that intent more precisely than a generic `Function`.

---

**Q: When would I choose `BiFunction` over chaining two `Function` calls?**
A: When an operation genuinely needs two independent inputs at once, such as combining two values into a result. `Function.andThen`/`compose` only chain single-input transformations, so they can't express a two-argument combination.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Functional Interfaces](functional-interfaces.md) — the general single-abstract-method contract these built-in interfaces implement
- [Lambda Expression](lamda-expression.md) — the primary syntax used to implement these interfaces
- [Method References](method-references.md) — a more concise alternative syntax for implementing these interfaces
- [Streams](stream-api.md) — the API that consumes these functional interfaces most heavily (map, filter, reduce)

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html
https://www.baeldung.com/java-8-functional-interfaces
https://eherrera.net/ocpj8-notes/04-lambda-built-in-functional-interfaces#:~:text=In%20Java%208%2C%20a%20Predicate,the%20methods%20of%20this%20interface.

[^1]: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/package-summary.html

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#lambdas-and-functional-interfaces) |
[Java 8](../versions.md#java-8-lts) |
[PF in Java](../versions.md#java-8-lts)

---