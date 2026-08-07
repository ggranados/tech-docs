# Functional Interfaces

---

## Table of Contents
<!-- TOC -->
* [Functional Interfaces](#functional-interfaces)
  * [Table of Contents](#table-of-contents)
  * [What's a Functional Interface?](#whats-a-functional-interface)
    * [Default Methods](#default-methods)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->


## What's a Functional Interface?

In Java, a Functional Interface is an interface that has **exactly one abstract method**. Functional Interfaces are a fundamental concept in functional programming and are a key part of Java's support for lambda expressions and method references introduced in Java 8.

The @FunctionalInterface annotation is used to indicate that an interface is intended to be a functional interface. It is optional but serves as a safety mechanism to enforce the single abstract method rule. If an interface is annotated with `@FunctionalInterface` [^1] and it contains more than one abstract method, the compiler will generate an error.

Functional Interfaces provide a convenient way to express behavior as data, which enables a more functional programming style in Java. They are widely used in Java libraries and APIs, especially when working with functional programming features like streams, predicates, consumers, suppliers, and more.

Here's an example of a simple functional interface in Java:

```java
@FunctionalInterface
interface MyFunctionalInterface {
    void doSomething();
}

```

You can use lambda expressions or method references to implement the doSomething method concisely, like this:

```java
MyFunctionalInterface myFunction = () -> System.out.println("Doing something!");
myFunction.doSomething(); // Output: "Doing something!"

```

<sub>[Back to top](#table-of-contents)</sub>


Java 8 introduced a set of built-in functional interfaces in the `java.util.function` package.

- See also: [Built-in Functional Interfaces](built-in-functional-interfaces.md)

---

### Default Methods

Since Java 8, interfaces can have "default" methods with implementations, but for an interface to be considered a Functional Interface, it must contain only one abstract method.

```java
@FunctionalInterface
interface MyFunctionalInterface {
    void doSomething(); // Abstract method

    default void doSomethingElse() {
        System.out.println("Doing something else!"); // Default method implementation
    }
}

```

In this example, the MyFunctionalInterface still retains its **SAM (Single Abstract Method)**, doSomething(). However, it now also includes a default method, doSomethingElse(), which has a default implementation.

Now, let's create an implementing class:

```java
class MyClass implements MyFunctionalInterface {
    @Override
    public void doSomething() {
        System.out.println("Doing something!");
    }
}

```

In this class, we override the doSomething() method, which is required because it's the abstract method from the functional interface. However, we can choose to override the default method doSomethingElse() if we want to provide a custom implementation:

```java
class MyClass implements MyFunctionalInterface {
    @Override
    public void doSomething() {
        System.out.println("Doing something!");
    }

    @Override
    public void doSomethingElse() {
        System.out.println("Overridden: Doing something else differently!");
    }
}

```

If we don't override the default method doSomethingElse() in the implementing class, it will inherit the default implementation from the functional interface.

Let's test the implementation:

```java
public static void main(String[] args) {
    MyFunctionalInterface myFunction = new MyClass();

    myFunction.doSomething();      // Output: "Doing something!"
    myFunction.doSomethingElse();  // Output: "Doing something else!"
}

```

As seen in the output, the overridden method doSomething() provides a custom implementation, while the default method doSomethingElse() is used as-is since it was not overridden in the MyClass implementation.


<sub>[Back to top](#table-of-contents)</sub>


---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why does `@FunctionalInterface` matter if it's optional?**
A: It makes the compiler enforce the single-abstract-method rule. Without it, someone could add a second abstract method later and silently break every lambda that implements the interface.

---

**Q: Can a functional interface have multiple default methods?**
A: Yes. Only the count of abstract methods matters — default and static methods don't count toward the "single abstract method" rule.

---

**Q: How do lambda expressions relate to functional interfaces?**
A: A lambda is a concise instance of the interface's single abstract method. The compiler infers the target functional interface from the context where the lambda is assigned or passed.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Built-in Functional Interfaces](built-in-functional-interfaces.md) — the standard `java.util.function` interfaces that implement this contract
- [Lambda Expression](lamda-expression.md) — the concise syntax used to implement functional interfaces
- [Method References](method-references.md) — an alternate syntax for implementing functional interfaces

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://www.baeldung.com/java-8-lambda-expressions-tips
https://www.baeldung.com/java-8-functional-interfaces

[^1]: https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/FunctionalInterface.html

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#lambdas-and-functional-interfaces) |
[Java 8](../versions.md#java-8-lts) |
[PF in Java](../versions.md#java-8-lts)

---
