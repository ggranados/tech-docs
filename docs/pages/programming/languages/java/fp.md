# FP in Java

---

## Table of Contents

<!-- TOC -->
* [FP in Java](#fp-in-java)
  * [Table of Contents](#table-of-contents)
  * [How is FP implemented in Java?](#how-is-fp-implemented-in-java)
    * [Lambda Expressions](#lambda-expressions)
    * [Functional Interfaces](#functional-interfaces)
    * [Built-in Functional Interfaces](#built-in-functional-interfaces)
    * [Streams API](#streams-api)
  * [FP Principles in Java](#fp-principles-in-java)
    * [Pure Functions](#pure-functions)
    * [Immutability](#immutability)
    * [Higher-order Functions](#higher-order-functions)
    * [Function Composition](#function-composition)
    * [Recursion](#recursion)
    * [First-Class Functions](#first-class-functions)
    * [Closures](#closures)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

## How is FP implemented in Java?

Java introduced functional programming features with the release of [Java 8](versions.md#java-8-lts) in March 2014.

- See also: [Java 8](versions.md#java-8-lts)

Functional programming is a programming paradigm that treats computation as the evaluation of mathematical functions and avoids changing state and mutable data. 

Java implemented functional programming features primarily through the addition of **lambda expressions** and **functional interfaces**. 

<sub>[Back to top](#table-of-contents)</sub>


### Lambda Expressions
Lambda expressions are anonymous functions that allow you to treat functionality as a method argument, or even as a return value. 

They provide a concise way to express behavior that can be passed around, making it easier to write code in a functional style. 

The syntax of a lambda expression is as follows:

```java
(parameters) -> expression
```

For example, a simple lambda expression that adds two numbers would look like this:

```java
(int a, int b) -> a + b
```

- See also: [Lambda Expression](java-8/lamda-expression.md)

<sub>[Back to top](#table-of-contents)</sub>

### Functional Interfaces

Functional interfaces are interfaces that have **exactly one abstract** method. 

Java 8 introduced the `@FunctionalInterface` annotation, which can be used to explicitly declare an interface as a functional interface. This annotation ensures that the interface has only one abstract method, preventing accidental addition of extra abstract methods. 

**Lambdas** and **method references** are used to implement the abstract method of functional interfaces.

For example:

```java
@FunctionalInterface
interface MyFunction {
    int apply(int x, int y);
}
```

- See also: [Functional Interfaces](java-8/functional-interfaces.md)
- See also: [Method References](java-8/method-references.md)

<sub>[Back to top](#table-of-contents)</sub>

### Built-in Functional Interfaces
Java 8 introduced a set of built-in functional interfaces in the `java.util.function` package. 

These interfaces cover common functional use cases, such as `Function`, `Predicate`, `Consumer`, and `Supplier`, among others. They provide a standard way of writing functional-style code without the need to create custom functional interfaces for every use case.

Here's an example using the Function interface:

```java
import java.util.function.Function;

public class FunctionalProgrammingExample {
  public static void main(String[] args) {
    Function<Integer, Integer> incrementByOne = x -> x + 1;
    System.out.println(incrementByOne.apply(5)); // Output: 6
  }
}
```

- See also: [Built-in Functional Interfaces](java-8/built-in-functional-interfaces.md)

<sub>[Back to top](#table-of-contents)</sub>

### Streams API

Java 8 also introduced the Stream API, which allows you to perform aggregate operations on collections in a functional style. 

Streams provide a way to process data in a declarative manner, expressing the desired behavior rather than the step-by-step instructions.

Here's a simple example of using the Stream API to filter and sum a list of numbers:

```java
import java.util.Arrays;
import java.util.List;

public class StreamsExample {
public static void main(String[] args) {
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        int sum = numbers.stream()
                        .filter(x -> x % 2 == 0)
                        .mapToInt(x -> x)
                        .sum();

        System.out.println(sum); // Output: 6
    }
}
```

These functional programming features added in Java 8 and subsequent versions make it easier to write concise, expressive, and more readable code using functional constructs. They are a significant step towards supporting functional programming paradigms in the Java language.

<sub>[Back to top](#table-of-contents)</sub>

---

## FP Principles in Java

### Pure Functions

In Java, a pure function is a function that always produces the same output for the same input and has no side effects, meaning it doesn't modify any state outside of its scope. 

- See also: [Pure Function](../../paradigms/functional.md#pure-functions)

```java
 // Pure function: No side effects, always returns the same output for the same input.
    public static int add(int x, int y) {
        return x + y;
    }
```
Calling add(5, 10) will always return 15, regardless of when or where the method is called.

In Java, a function can still be considered pure even if it takes object references as parameters. The key is that the function should not modify the state of the objects it receives as arguments. It should only use the object's properties or methods to perform computations and return a result based on that, without introducing side effects.

Considering class Point:

```java
class Point {
  private int x;
  private int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
```

The `calculateDistance` method takes two Point objects as parameters and calculates the distance between them using their x and y coordinates. The function doesn't modify the Point objects, nor does it have any side effects.

```java
  // Pure function: No side effects, only uses object properties for computation.
  public static double calculateDistance(Point p1, Point p2) {
      int xDiff = p2.getX() - p1.getX();
      int yDiff = p2.getY() - p1.getY();
      return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
  }
```

The function will always return the same result for the same input Point objects, adhering to the principles of purity.

```java
    public static void main(String[] args) {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(5, 7);

        // Pure function call
        double distance = calculateDistance(p1, p2);

        System.out.println("Distance: " + distance); // Output: Distance: 5.0
    }
```

Pure functions are predictable, easy to test, and have several advantages in terms of reasoning about code and achieving concurrency.

<sub>[Back to top](#table-of-contents)</sub>

### Immutability

Immutable data structures refers to data that cannot be changed after creation.

- See also: [Immutability](../../paradigms/functional.md#immutability)

In Java, To make a class fully immutable, you need to take this steps:

**- Make the properties final:**
  Declare the properties as final to ensure they are assigned only once during object creation and cannot be changed afterward.

**- Remove setters:** 
Remove any methods that allow modifying the properties after the object is created, such as setter methods.

**- Return copies of mutable objects:** 
If the class contains mutable objects, like arrays or other objects, when returning them from getter methods, return a copy instead of the original reference. This ensures that the internal state of the object cannot be altered from outside the class.

Here's the Person class achieving full immutability:

```java
final class Person {
  private final String name;
  private final int age;
  private final List<String> hobbies;

  public Person(String name, int age, List<String> hobbies) {
    this.name = name;
    this.age = age;
    this.hobbies = new ArrayList<>(hobbies); // Creating a new copy to ensure immutability
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public List<String> getHobbies() {
    return Collections.unmodifiableList(hobbies); // Returning an unmodifiable view of the hobbies list
  }
}
```

By following these practices, the Person class becomes immutable, ensuring that once a Person object is created, its state cannot be changed. This property makes the class thread-safe and facilitates reasoning about the code in concurrent environments.

```java
public class ImmutablePointExample {
    public static void main(String[] args) {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(5, 7);

        // Pure function call
        double distance = calculateDistance(p1, p2);

        System.out.println("Distance: " + distance); // Output: Distance: 5.0
    }

    // Pure function: No side effects, only uses object properties for computation.
    public static double calculateDistance(Point p1, Point p2) {
        int xDiff = p2.getX() - p1.getX();
        int yDiff = p2.getY() - p1.getY();
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>

### Higher-order Functions

A higher-order function is a function that either takes another function as an argument or returns a function as its result.

- See also: [Higher-order Functions](../../paradigms/functional.md#higher-order-functions)

In modern Java, you can demonstrate the higher-order functions principle using lambda expressions or method references.

In this example, we have a higher-order function map, which takes a List of input elements and a Function to transform each element of the list. The map function is generic, allowing it to work with different types of input and output. It uses the Stream API to process the elements in the list, applying the transformation defined by the provided mapper function.

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class HigherOrderFunctionExample {
  public static void main(String[] args) {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

            // Higher-order function: map()
            List<String> numberStrings = map(numbers, n -> "Number: " + n);

            // Print the result
            numberStrings.forEach(System.out::println);
  }

  // Higher-order function: Takes a List of input and a Function to transform elements.
  public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
    return list.stream()
                .map(mapper)
                .toList();
  }
}
```

- See also: [Lambda Expression](java-8/lamda-expression.md)
- See also: [Method References](java-8/method-references.md)
- See also: [Built-in Functional](java-8/built-in-functional-interfaces.md)
- See also: [Stream API](java-8/stream-api.md) -->

<sub>[Back to top](#table-of-contents)</sub>

### Function Composition

Functions can be composed by chaining them together, creating more complex behaviors from simpler functions.

- See also:
[Function Composition](../../paradigms/functional.md#function-composition)

Function composition in modern Java can be demonstrated using the andThen and compose methods from the java.util.function.Function interface. These methods allow you to combine multiple functions together to create new functions that represent the composition of the original functions.

```java
import java.util.function.Function;

public class FunctionCompositionExample {
  public static void main(String[] args) {
    // Function to double the input
    Function<Integer, Integer> doubleFunction = num -> num * 2;

    // Function to add 10 to the input
    Function<Integer, Integer> addTenFunction = num -> num + 10;

    // Function composition using andThen: double -> add 10
    Function<Integer, Integer> composedFunction1 = doubleFunction.andThen(addTenFunction);

    // Function composition using compose: add 10 -> double
    Function<Integer, Integer> composedFunction2 = doubleFunction.compose(addTenFunction);

    int inputNumber = 5;

    // Using the composed functions
    int result1 = composedFunction1.apply(inputNumber);
    int result2 = composedFunction2.apply(inputNumber);

    System.out.println("Result using andThen: " + result1); // Output: Result using andThen: 20
    System.out.println("Result using compose: " + result2); // Output: Result using compose: 30
  }
}
```

This example illustrates how the andThen and compose methods affect the function composition differently.

<sub>[Back to top](#table-of-contents)</sub>


### Recursion

Recursion is a programming technique where a function calls itself in order to solve a problem. 

- See also: [Recursion](../../paradigms/functional.md#recursion)

The process continues until a base case is reached, at which point the function stops calling itself and starts returning results, which are then combined to solve the original problem.

```java
public class RecursionExample {
    public static void main(String[] args) {
        int number = 5;
        int factorial = calculateFactorial(number);
        System.out.println("Factorial of " + number + " is: " + factorial);
    }

    public static int calculateFactorial(int n) {
        if (n == 0) {
            return 1; // Base case: Factorial of 0 is 1
        } else {
            return n * calculateFactorial(n - 1); // Recursive call
        }
    }
}
```

The method uses recursion to break down the problem into smaller subproblems:

- Base case: If the input n is 0, the method returns 1, as the factorial of 0 is 1.


- Recursive case: If the input n is greater than 0, the method calls itself with the argument n - 1, effectively reducing the problem to calculating the factorial of n - 1.

The recursive calls continue until the base case is reached (when n becomes 0), and the results are then combined to get the final result, which is the factorial of the original input number.

When we run the code, the output will be:

```java
Factorial of 5 is: 120
```

<sub>[Back to top](#table-of-contents)</sub>

### First-Class Functions
In languages with first-class functions, functions are treated just like any other data type, enabling powerful functional programming paradigms.This means that functions can be assigned to variables, passed as arguments to other functions, returned as values from functions, and stored in data structures like arrays or lists.

- See also: [First-Class Funtions](../../paradigms/functional.md#first-class-functions)

```java
import java.util.function.Function;

public class FirstClassFunctionExample {
  public static void main(String[] args) {
    // Assigning a function to a variable
    Function<Integer, Integer> doubleFunction = num -> num * 2;

    // Passing a function as an argument to another function
    int result1 = applyFunction(5, doubleFunction); // Output: 10
    int result2 = applyFunction(8, num -> num + 3); // Output: 11

    // Returning a function from another function
    Function<Integer, Integer> addFiveFunction = getAddFiveFunction();
    int result3 = addFiveFunction.apply(7); // Output: 12
  }

  // Function that takes a function as an argument
  public static int applyFunction(int value, Function<Integer, Integer> func) {
    return func.apply(value);
  }

  // Function that returns a function
  public static Function<Integer, Integer> getAddFiveFunction() {
    return num -> num + 5;
  }
}

```

In this example, we demonstrate the features of first-class functions in modern Java using lambda expressions. 

- See also: [Lambda Expression](java-8/lamda-expression.md)

<sub>[Back to top](#table-of-contents)</sub>

### Closures

In simple terms, a closure is a function bundled together with its lexical environment (the set of variables and their values) from the outer scope where it was created.

- See also: [Closures](../../paradigms/functional.md#closures)

In Java, we can create an equivalent closure using anonymous inner classes or lambda expressions. Prior to Java 8, anonymous inner classes were commonly used to achieve closure-like behavior. However, with the introduction of lambda expressions, creating closures in Java became more concise and expressive.

Using Anonymous class

```java
public class ClosureExample {
    public static void main(String[] args) {
        Counter counter = createCounter();
        counter.increment(); // Output: Count: 1
        counter.increment(); // Output: Count: 2
        counter.increment(); // Output: Count: 3
    }

    public static Counter createCounter() {
        // Using an anonymous inner class to create a closure
        return new Counter() {
            private int count = 0;

            @Override
            public void increment() {
                count++;
                System.out.println("Count: " + count);
            }
        };
    }

    interface Counter {
        void increment();
    }
}
```

Using Lambda since Java 8

```java
public class ClosureExample {
    public static void main(String[] args) {
        Counter counter = createCounter();
        counter.increment(); // Output: Count: 1
        counter.increment(); // Output: Count: 2
        counter.increment(); // Output: Count: 3
    }

    public static Counter createCounter() {
        // Using a lambda expression to create a closure
        int[] count = { 0 }; // Effectively final variable (for Java 8 and above)
        return () -> {
            count[0]++;
            System.out.println("Count: " + count[0]);
        };
    }

    interface Counter {
        void increment();
    }
}
```

<sub>[Back to top](#table-of-contents)</sub>


## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What must be true about an interface for a lambda expression to implement it?**
A: It must be a functional interface — one with exactly one abstract method (a SAM, "Single Abstract Method"); the optional `@FunctionalInterface` annotation just makes the compiler enforce that constraint.

---

**Q: Why is the immutable `Person` class on this page considered thread-safe?**
A: All fields are `final` and set only in the constructor, there are no setters, and the mutable `hobbies` list is defensively copied on the way in and returned as an unmodifiable view on the way out — so no thread can observe or cause a partial/inconsistent state change.

---

**Q: What's the practical difference between the anonymous-class and lambda closure examples?**
A: Both close over the enclosing scope, but the anonymous class needs a full class body implementing the `Counter` interface plus its own field, while the lambda captures an effectively-final array to hold mutable state and expresses the same behavior in one line — lambdas are more concise but still can't reassign a captured local variable directly.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Functional Programming](../../paradigms/functional.md) — the language-agnostic paradigm this page implements in Java.
- [Lambda Expression](java-8/lamda-expression.md) — full syntax reference for the lambda expressions used throughout this page.
- [Stream API](java-8/stream-api.md) — the primary functional-style API built on top of these lambda and functional-interface features.
- [Concurrency](concurrency.md) — explains why immutability (covered here) matters for thread safety.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.baeldung.com/java-8-lambda-expressions-tips
- https://www.baeldung.com/java-8-functional-interfaces

___

[Get Started](../../../../get-started.md) |
[Paradigms](../../../../get-started.md#paradigms) |
[Functional Programming](../../paradigms/functional.md) |
[Java](java.md)



___



