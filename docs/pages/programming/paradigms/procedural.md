# Procedural Programming

---

## Table of Contents
<!-- TOC -->
* [Procedural Programming](#procedural-programming)
  * [Table of Contents](#table-of-contents)
  * [What's Procedural Programming](#whats-procedural-programming)
  * [Examples](#examples)
  * [Languages](#languages)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->


## What's Procedural Programming

Procedural programming organizes code into *reusable procedures or functions* that operate on data. It emphasizes the use of procedures to encapsulate behavior and separate it from data.

## Examples

The example is a C program that defines a function to calculate the sum of two numbers and prints the result.

```c
#include <stdio.h>

int sum(int a, int b) {
    return a + b;
}

int main() {
    int result = sum(3, 4);
    printf("Result: %d\n", result);
    return 0;
}
```

<sub>[Back to top](#table-of-contents)</sub>

## Languages

The first major procedural programming languages appeared circa 1957–1964, including Fortran, ALGOL, COBOL, PL/I and BASIC. Pascal and C were published circa 1970–1972.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How is procedural programming different from structured programming if both use functions?**
A: Procedural programming's defining trait is organizing code into reusable procedures that operate on data. Structured programming is a stricter discipline about *how* the code inside those procedures flows — mandating sequence, selection, and iteration constructs instead of unconditional jumps (like `goto`). Most procedural languages today are also structured.

---

**Q: Is procedural programming just another name for imperative programming?**
A: No — procedural programming is a subset of imperative programming. Imperative programming is the broader category (explicit, step-by-step state changes); procedural programming specifically emphasizes organizing those steps into reusable procedures/functions separated from the data they operate on.

---

**Q: Why would a modern architect still need to understand procedural programming?**
A: Many performance-critical systems, embedded software, and legacy enterprise systems are still written procedurally (C, COBOL, Fortran), and procedural thinking underpins how functions and modules are organized even inside OOP and functional codebases.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Structured Programming](structured.md) — refines procedural code with disciplined sequence, selection, and iteration control flow
- [Imperative Programming](imperative.md) — the broader paradigm that procedural programming is a subset of
- [Object-Oriented Programming](object-oriented.md) — evolved from procedural programming by bundling data and behavior together

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://en.wikipedia.org/wiki/Procedural_programming

---

[Get Started](../../../get-started.md#paradigms) 

---


