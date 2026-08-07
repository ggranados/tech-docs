# Imperative Programming

---

## Table of Contents
<!-- TOC -->
* [Imperative Programming](#imperative-programming)
  * [Table of Contents](#table-of-contents)
  * [What's Imperative Programming](#whats-imperative-programming)
  * [Languages](#languages)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## What's Imperative Programming

Imperative programming focuses on *explicitly specifying a sequence of statements* that modify program state. It emphasizes how to achieve a result through step-by-step instructions and control flow structures like loops and conditionals. 

Procedural programming and structured programming are subsets of imperative programming.

<sub>[Back to top](#table-of-contents)</sub>

## Languages

[Procedural](procedural.md) and [object-oriented programming (OOP)](object-oriented.md) languages fall under imperative programming, such as C, C++, C#, and [Java](../languages/java).


<sub>[Back to top](#table-of-contents)</sub>

## Examples

The example calculates the sum of numbers from 1 to 10 using a for loop in Java.

```java
int sum = 0;
for (int i = 1; i <= 10; i++) {
    sum += i;
}
System.out.println("Sum: " + sum);
```

> Declarative style is a contrast to the Imperative style

- See also: [Declarative Programming](declarative.md)

<sub>[Back to top](#table-of-contents)</sub>


---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How do procedural and object-oriented programming relate to imperative programming?**
A: Both are subsets of imperative programming — they share the core trait of specifying explicit, step-by-step statements that modify program state. Procedural programming organizes those statements into reusable functions, while OOP additionally bundles state and behavior into objects.

---

**Q: What's the practical difference between writing imperative code and declarative code for the same task?**
A: Imperative code specifies *how* to reach a result — the exact loop, conditionals, and state mutations. Declarative code specifies *what* result you want and lets the underlying implementation (e.g., a query engine or stream API) decide how to get there. The `for` loop summation example on this page is imperative; the same sum via `IntStream.rangeClosed(1, 10).sum()` is declarative.

---

**Q: Why is Java still considered an imperative language even though it has streams and lambdas?**
A: Java's core execution model — statements executed in sequence, mutable variables, explicit control flow — remains imperative. Streams and lambdas (introduced in Java 8) let you write declarative/functional-style code on top of that imperative foundation, but they don't change what the language fundamentally is.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Procedural Programming](procedural.md) — a subset of imperative programming organized around reusable functions
- [Object-Oriented Programming](object-oriented.md) — a subset of imperative programming that bundles state and behavior into objects
- [Declarative Programming](declarative.md) — the contrasting paradigm that specifies outcomes instead of steps

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.techtarget.com/whatis/definition/imperative-programming#:~:text=Imperative%20programming%20is%20a%20software,models%20are%20not%20called%20on.
- https://en.wikipedia.org/wiki/Imperative_programming

___

[Get Started](../../../get-started.md#paradigms) |
[Java](../languages/java/java.md#whats-oop)

---