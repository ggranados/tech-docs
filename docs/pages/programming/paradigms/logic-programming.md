# Logic Programming

---

## Table of Contents
<!-- TOC -->
* [Logic Programming](#logic-programming)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Facts, Rules, and Unification](#facts-rules-and-unification)
  * [Where This Shows Up Even If You Don't Write Prolog](#where-this-shows-up-even-if-you-dont-write-prolog)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Logic programming is a paradigm where you declare *facts* and *rules* about a problem, and let the language's inference engine figure out *how* to answer a query — the most extreme point on the declarative end of the spectrum, since you don't write control flow at all, only relationships.

---

## Overview

Where imperative code says "do this, then this," and functional code says "compute this as a transformation of that," logic programming says "these things are true, derive what follows." Prolog is the canonical example: a program is a database of facts and inference rules, and running it means asking a question that the engine answers via backtracking search.

<sub>[Back to top](#table-of-contents)</sub>

---

## Facts, Rules, and Unification

- ### Facts and Rules:
  A fact states something unconditionally true; a rule states something true given other conditions.

  ```prolog
  parent(tom, bob).
  parent(bob, ann).
  grandparent(X, Y) :- parent(X, Z), parent(Z, Y).
  ```

  Querying `grandparent(tom, ann)` succeeds because the engine can find a `Z` (`bob`) satisfying both `parent` facts.

- ### Unification and Backtracking:
  The engine searches for variable bindings that make a query true (*unification*), and if a chosen path fails, it *backtracks* and tries alternatives — this search process is the "how" that a logic program never has to specify explicitly.

  ```mermaid
  flowchart TD
      A["Query: grandparent(tom, Y)?"] --> B["Try parent(tom, Z)"]
      B --> C["Z = bob"]
      C --> D["Try parent(bob, Y)"]
      D --> E["Y = ann -- success"]
  ```

  **Caption:** The engine unifies variables and backtracks through facts/rules to satisfy the query, without the programmer specifying the search order.

<sub>[Back to top](#table-of-contents)</sub>

---

## Where This Shows Up Even If You Don't Write Prolog

Few production systems are written in pure logic-programming languages today, but the ideas resurface constantly: SQL's query planner does declarative fact-matching and lets the database figure out the join order; rule engines (Drools, business-rule systems) are directly logic-programming-flavored; type inference in statically-typed languages and constraint solvers (used in package-dependency resolvers, scheduling systems) both work by unification and search, the same mechanics Prolog popularized.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

**Q: How is logic programming different from declarative programming in general?**
A: Logic programming is a specific, narrower form of declarative programming — SQL and functional programming are also declarative, but logic programming specifically models a problem as facts/rules and answers queries via unification and backtracking search, rather than via set operations (SQL) or function composition (functional).

---

**Q: Why don't most architects need to know Prolog specifically?**
A: Pure logic-programming languages are rare in production; the value for an architect is recognizing the *pattern* — rule engines, constraint solvers, and query planners all use unification/backtracking-style reasoning under the hood, which helps in understanding their behavior and performance characteristics (e.g., why a rule engine can behave unpredictably if rules aren't ordered/scoped carefully).

---

**Q: What's a practical example of backtracking search causing a real performance problem?**
A: A rule engine or constraint solver evaluating many interacting rules can experience combinatorial blowup if the search space isn't pruned — this is the same class of problem as an unindexed SQL join forcing the planner to explore many more paths than necessary.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Declarative Programming](declarative.md) — logic programming is a specific, narrower form of the broader declarative approach
- [Functional Programming](functional.md) — the other major declarative paradigm, contrasted by its transformation-based rather than search-based evaluation model
- [Domain-Specific Languages (DSLs)](domain-specific-languages.md) — rule engines are often exposed to end users through a DSL built on logic-programming-style evaluation

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [SWI-Prolog Documentation](https://www.swi-prolog.org/pldoc/doc_for?object=manual) — official documentation
- [Wikipedia: Logic programming](https://en.wikipedia.org/wiki/Logic_programming) — overview and history

---

[Get Started](../../../get-started.md#paradigms)

---
