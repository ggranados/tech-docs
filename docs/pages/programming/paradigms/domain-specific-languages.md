# Domain-Specific Languages (DSLs)

---

## Table of Contents
<!-- TOC -->
* [Domain-Specific Languages (DSLs)](#domain-specific-languages-dsls)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Internal vs. External DSLs](#internal-vs-external-dsls)
  * [Examples an Architect Encounters Constantly](#examples-an-architect-encounters-constantly)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

A Domain-Specific Language is a small, purpose-built language designed to express solutions in one narrow problem domain clearly and concisely — in contrast to a general-purpose language (GPL) like Java or Python, which can express anything but is often more verbose for a narrow, repeated task.

---

## Overview

SQL, regular expressions, CSS, and build-tool config files are all DSLs — each is intentionally limited to its domain (querying relational data, pattern-matching text, styling documents, describing a build) in exchange for being far more concise and readable within that domain than the equivalent general-purpose code would be. The trade-off is always the same: a DSL sacrifices generality for expressiveness inside its narrow scope.

<sub>[Back to top](#table-of-contents)</sub>

---

## Internal vs. External DSLs

- ### External DSLs:
  A standalone language with its own parser and syntax, entirely separate from any host language — SQL, regular expressions, and YAML-based config formats (like a CI pipeline definition) are external DSLs. They require building (or adopting) a dedicated parser, but can have syntax perfectly tailored to the domain.

- ### Internal (Embedded) DSLs:
  A DSL built *within* a host general-purpose language, using that language's own syntax creatively (fluent builders, method chaining, operator overloading) to read like a specialized language without a separate parser.

  ```java
  // An internal DSL: a fluent builder reading almost like a sentence
  Query query = QueryBuilder.select("name", "email")
      .from("users")
      .where("active = true")
      .orderBy("name");
  ```

  Internal DSLs are cheaper to build (no parser needed — they're just a well-designed API) but constrained by the host language's own syntax rules.

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples an Architect Encounters Constantly

Most architects use DSLs daily without necessarily labeling them that way: SQL (querying), regular expressions (pattern matching), Gradle/Maven build files, Terraform's HCL (infrastructure as code — see [Infrastructure as Code](../../devops/infrastructure-as-code.md)), Kubernetes YAML manifests, and Spring's fluent `WebSecurityConfigurerAdapter`-style APIs are all DSLs. Recognizing something as a DSL is useful mainly for the design decision it implies: when a team keeps writing verbose, repetitive general-purpose code for one narrow recurring task, that's the signal a small DSL (usually internal/embedded) might pay for itself.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

**Q: When is it worth building a custom DSL instead of just using a general-purpose language?**
A: When a narrow task is repeated often enough, by people who aren't necessarily full-time developers (e.g., business analysts writing pricing rules), that the reduced verbosity and domain-specific vocabulary meaningfully outweigh the cost of building and maintaining the DSL (or its parser, for an external one).

---

**Q: What's the practical risk of building too many internal DSLs in a codebase?**
A: Every fluent/builder-style API is a small language a new team member has to learn — overused, a codebase can end up with several inconsistent "mini-languages" that are individually clever but collectively raise onboarding cost more than they save in conciseness.

---

**Q: Is Prolog itself a DSL?**
A: No — Prolog is a general-purpose logic-programming language, not domain-specific; it can express arbitrary computation, it's just built around a different [paradigm](logic-programming.md) than imperative or functional languages.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Declarative Programming](declarative.md) — most DSLs (SQL, HCL, YAML manifests) are declarative in style
- [Logic Programming](logic-programming.md) — rule-engine DSLs are often built on logic-programming-style evaluation underneath
- [Infrastructure as Code](../../devops/infrastructure-as-code.md) — Terraform's HCL and Kubernetes YAML are two of the most common DSLs an architect works with directly

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Martin Fowler: Domain-Specific Languages](https://martinfowler.com/dsl.html) — overview and internal/external distinction
- [Wikipedia: Domain-specific language](https://en.wikipedia.org/wiki/Domain-specific_language) — overview and examples

---

[Get Started](../../../get-started.md#paradigms)

---
