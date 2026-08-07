# Method references

---

## Table of Contents

<!-- TOC -->
* [Method references](#method-references)
  * [Table of Contents](#table-of-contents)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

___

Shorthand syntax for referring to an existing method by its name. They provide a concise way to pass a method as an argument or to assign a method to a functional interface. 

Method references can be seen as a more compact alternative to lambda expressions when the lambda expression simply calls an existing method without any additional logic.

The syntax for method references in Java is as follows:

```java
ClassName::methodName
```

There are several types of method references in Java:

Reference to a static method:

```java
ClassName::staticMethodName
```
        
Reference to an instance method of a particular object:

```java
instance::instanceMethodName
```

Reference to an instance method of an arbitrary object of a particular type:

```java
ClassName::instanceMethodName
```        
        
Reference to a constructor:

```java
ClassName::new
```

<sub>[Back to top](#table-of-contents)</sub>

___

## Examples

Capitalizing and printing a list of Strings:
   ````java
   List<String> messages = Arrays.asList("hello", "baeldung", "readers!");
   ````

We can achieve this by leveraging a simple lambda expression calling the ``StringUtils.capitalize()`` method directly:
   ````java
   messages.forEach(word -> StringUtils.capitalize(word));
   ````

Or, we can use a method reference to simply refer to the capitalize static method:
   ````java 
   messages.forEach(StringUtils::capitalize);
   ````

<sub>[Back to top](#table-of-contents)</sub>

___

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: When should I prefer a method reference over a lambda expression?**
A: When the lambda body does nothing but call an existing method with a matching signature. A method reference is shorter and communicates intent without adding new behavior.

---

**Q: What's the difference between `ClassName::instanceMethodName` and `instance::instanceMethodName`?**
A: The former is unbound — the first parameter supplied by the functional interface becomes the receiver of the call. The latter is bound to a specific, already-existing object instance.

---

**Q: Can a constructor reference (`ClassName::new`) be used with any functional interface?**
A: Only if the constructor's parameter list matches the interface's abstract method signature — for example, a no-arg constructor matches `Supplier<T>`.

<sub>[Back to top](#table-of-contents)</sub>

___

## Related Topics

- [Lambda Expression](lamda-expression.md) — the syntax method references act as shorthand for
- [Functional Interfaces](functional-interfaces.md) — the contract method references must satisfy
- [Built-in Functional Interfaces](built-in-functional-interfaces.md) — common target types for method references

<sub>[Back to top](#table-of-contents)</sub>

___

## Ref.

___

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#lambdas-and-functional-interfaces) |
[Java 8](../versions.md#java-8-lts) |
[PF in Java](../versions.md#java-8-lts)


___