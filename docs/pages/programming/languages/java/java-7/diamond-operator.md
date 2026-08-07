# Diamond Operator

---

## Table of Contents
<!-- TOC -->
* [Diamond Operator](#diamond-operator)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Overview

Before the diamond operator, when creating instances of generic classes, you had to explicitly specify the type parameter twice, once during declaration and once during instantiation:

```java
List<String> myList = new ArrayList<String>(); // Java 6 and earlier
```

With the diamond operator, the redundant type information can be omitted:

```java
List<String> myList = new ArrayList<>(); // Java 7 and later
```

The diamond operator works with any generic class, not just ArrayList

```java
Map<String, Integer> myMap = new HashMap<>(); // Java 7 and later
Set<Double> mySet = new HashSet<>(); // Java 7 and later
```

The Java 1.7 compiler's type inference feature determines the most suitable constructor declaration that matches the invocation.

- See also: [Collections API](../java-1_2/collections-api.md)
- See also: [Generics](../java-5/generics.md)

<sub>[Back to top](#table-of-contents)</sub>


---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What Java version introduced the diamond operator, and what problem did it solve?**
A: Java 7 (2011); it removed the need to repeat the generic type argument at both the declaration and instantiation sides of a statement (e.g. `new ArrayList<String>()` shrinks to `new ArrayList<>()`).

---

**Q: Does the diamond operator work with any generic type, or just `ArrayList`?**
A: Any generic class — this page shows it working with `ArrayList`, `HashMap`, and `HashSet` alike, since it's a general compiler feature, not something specific to one collection type.

---

**Q: How does the compiler figure out the type when I write `<>`?**
A: Through type inference — the compiler looks at the target context (typically the declared variable type on the left-hand side) and infers the matching constructor's type argument automatically.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Generics](../java-5/generics.md) — the feature the diamond operator simplifies the syntax for.
- [Original Collections API](../java-1_2/collections-api.md) — the pre-generics collections API that made explicit type repetition necessary in the first place.
- [Updated Java Collections API](../java-5/enhanced-collections.md) — shows the diamond operator used throughout modern collection examples.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

https://www.baeldung.com/java-diamond-operator

---

[Get Started](../../../../../get-started.md) |
[Languages](../../../../../get-started.md#languages) |
[Java Development](../develop.md#generics-and-collections) |
[Java 7](../versions.md#java-7)

---