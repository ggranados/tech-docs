# Object-Oriented Programming (OOP)

---

## Table of Contents
<!-- TOC -->
* [Object-Oriented Programming (OOP)](#object-oriented-programming-oop)
  * [Table of Contents](#table-of-contents)
  * [What's OOP](#whats-oop)
  * [OOP Concepts](#oop-concepts)
    * [Encapsulation](#encapsulation-)
    * [Inheritance](#inheritance-)
    * [Polymorphism](#polymorphism-)
      * [Static (Compile-time) Polymorphism:](#static-compile-time-polymorphism)
      * [Dynamic (Runtime) Polymorphism:](#dynamic-runtime-polymorphism)
    * [Abstraction](#abstraction-)
    * [Class](#class-)
    * [Object](#object-)
    * [Message Passing](#message-passing)
  * [OOP Languages](#oop-languages)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

___

## What's OOP

Object-Oriented Programming (OOP) is a programming paradigm in computer science that relies on the concept of classes and objects. It is used to structure a software program into simple, reusable pieces of code blueprints (usually called classes), which are used to create individual instances of objects. There are many object-oriented programming languages, including JavaScript, C++, [Java](../languages/java/java.md#whats-oop), and Python.[^1]

![class-diagram.png](../../../img/class-diagram.png)

<sub>[Back to top](#table-of-contents)</sub>

## OOP Concepts

The core concepts of object-oriented programming (OOP) include:

### Encapsulation 
Encapsulation is the principle of bundling data and the methods that operate on that data into a single unit called an object. It allows for data hiding and abstraction, ensuring that the internal representation and behavior of an object are hidden from the outside and can only be accessed through well-defined interfaces.

![encapsulation.jpg](../../../img/encapsulation.jpg)

<sub>[Back to top](#table-of-contents)</sub>

### Inheritance 
Inheritance enables the creation of new classes (derived classes) based on existing classes (base or parent classes). Derived classes inherit properties and behaviors from their parent classes, allowing for code reuse and the creation of class hierarchies.

![InheritanceinObjectOrientedProgramming.png](../../../img/InheritanceinObjectOrientedProgramming.png)

<sub>[Back to top](#table-of-contents)</sub>

### Polymorphism 
Polymorphism means the ability of objects to take on multiple forms or behave differently based on the context. It allows objects of different classes to be treated as instances of a common superclass, enabling flexibility and extensibility in code design.

There are two main types of polymorphism in object-oriented programming:


#### Static (Compile-time) Polymorphism:
Static polymorphism is achieved through method overloading and operator overloading. 

- **Method overloading** allows multiple methods with the same name but different parameter types or a different number of parameters to coexist in a class. The appropriate method to execute is determined by the compiler based on the method's signature at compile-time. 

- **Operator overloading** (in some languages) enables the use of operators (such as +, -, *, /) with custom classes, defining their behavior according to the operands' types.

#### Dynamic (Runtime) Polymorphism:
Dynamic polymorphism is achieved through method overriding and inheritance. Method overriding allows a subclass to provide a different implementation of a method that is already defined in its superclass. The decision on which implementation to execute is made at runtime based on the actual type of the object being referenced. This allows for the invocation of the appropriate method based on the specific object's type during runtime. Dynamic polymorphism is commonly associated with the "IS-A" relationship, where a subclass is considered an instance of its superclass.

![PolymorphisminObjectOrientedProgramming.png](../../../img/PolymorphisminObjectOrientedProgramming.png)

<sub>[Back to top](#table-of-contents)</sub>

### Abstraction 
Abstraction involves focusing on essential characteristics and behavior while ignoring unnecessary details. It allows programmers to create abstract classes or interfaces that define a common set of properties and methods, without specifying their implementation. Abstraction helps in designing modular and maintainable code.

<sub>[Back to top](#table-of-contents)</sub>

### Class 
A class is a blueprint or template that defines the structure and behavior of objects. It encapsulates data (attributes) and methods (functions or operations) that manipulate that data. Objects are instances of classes.

<sub>[Back to top](#table-of-contents)</sub>

### Object 
An object is an instance of a class. It represents a particular entity with its own state (data) and behavior (methods). Objects interact with each other by invoking methods and exchanging messages.

<sub>[Back to top](#table-of-contents)</sub>

### Message Passing
In OOP, communication between objects occurs through message passing. Objects send messages to other objects to request or provide information, invoking the appropriate methods to carry out the desired actions.

<sub>[Back to top](#table-of-contents)</sub>

## OOP Languages

- Java
  - Visit: [OOP in Java](../languages/java/oop.md)
- C++
- C#
- Python
- Ruby
- JavaScript
- PHP
- Swift
- Kotlin
- Objective-C
- Go
- TypeScript
- Scala
- Rust
- Perl
- Groovy
- R
- Dart
- Lua
- Smalltalk

<sub>[Back to top](#table-of-contents)</sub>

## Examples

The example defines a Rectangle class in Java, which has data (width and height) and behavior (calculateArea method) to calculate the area of a rectangle.

```java
class Rectangle {
private int width;
private int height;

public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int calculateArea() {
        return width * height;
    }
}

Rectangle rectangle = new Rectangle(5, 7);
int area = rectangle.calculateArea();
System.out.println("Area: " + area);
```


<sub>[Back to top](#table-of-contents)</sub>

___

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What's the actual difference between static and dynamic polymorphism?**
A: Static (compile-time) polymorphism is resolved by the compiler based on method signatures — method and operator overloading. Dynamic (runtime) polymorphism is resolved based on the actual object type at runtime through method overriding, and is what enables the "IS-A" substitution central to OOP design.

---

**Q: Aren't encapsulation and abstraction the same thing?**
A: No. Encapsulation is about bundling data and behavior together and restricting direct access to internal state. Abstraction is about exposing only essential characteristics through interfaces or abstract classes while hiding implementation detail. Encapsulation is a mechanism; abstraction is a design goal it helps achieve.

---

**Q: Why is OOP classified as a subset of imperative programming?**
A: Because OOP still relies on explicit, step-by-step statements and mutable object state to accomplish tasks — objects just organize that state and behavior into units. It doesn't change the fundamental "how to do it" execution model that defines imperative programming.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Imperative Programming](imperative.md) — the broader paradigm that OOP is a subset of
- [Structured Programming](structured.md) — the control-flow discipline that OOP builds upon
- [OOP in Java](../languages/java/oop.md) — how these concepts (encapsulation, inheritance, polymorphism) are implemented in Java

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.geeksforgeeks.org/introduction-of-object-oriented-programming/
- https://www.enjoyalgorithms.com/blog/encapsulation-in-oops

[^1]: https://www.educative.io/blog/object-oriented-programming

___

[Get Started](../../../get-started.md) |
[Paradigms](../../../get-started.md#paradigms) |
[Java](../languages/java/java.md#whats-oop)

---