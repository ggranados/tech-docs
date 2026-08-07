# Dynamic Programming and Greedy Algorithms

---

## Table of Contents
<!-- TOC -->
* [Dynamic Programming and Greedy Algorithms](#dynamic-programming-and-greedy-algorithms)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Dynamic Programming](#dynamic-programming)
  * [Greedy Algorithms](#greedy-algorithms)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Dynamic Programming (DP) and Greedy Algorithms are two problem-solving strategies for optimization problems — "find the best way to do X" — that an architect needs to be able to tell apart on sight. Both build a solution incrementally from smaller decisions, but they differ in one critical way: DP considers and remembers the results of overlapping sub-decisions to guarantee a globally optimal answer, while Greedy commits to the locally best choice at each step and never looks back. Confusing the two — applying a greedy shortcut where DP's exhaustiveness is required — is a common source of subtly wrong "optimized" business logic (pricing, scheduling, resource allocation).

---

## Overview

Both techniques exist to avoid the brute-force alternative: trying every possible combination of choices, which is typically exponential in time. DP tames that explosion by recognizing that many of those combinations recompute the same smaller sub-problem over and over, and by storing (caching) each sub-problem's answer the first time it is solved. Greedy tames it differently — by proving (for a specific class of problems) that always taking the locally best option at each step happens to also produce the globally best overall result, which means no exhaustive search or caching is needed at all.

The practical takeaway for an architect is a decision order: check whether a problem has the greedy-choice property first, because a correct greedy algorithm is simpler to implement, easier to reason about, and typically runs faster (often O(n log n) or better) than the DP alternative. Only fall back to DP when greedy cannot be proven correct for the problem at hand.

<sub>[Back to top](#table-of-contents)</sub>

---

## Dynamic Programming

DP applies to problems that exhibit two properties — recognizing them is the actual skill; the algorithm itself is comparatively mechanical once they're identified.

- ### Optimal Substructure:
  The optimal solution to the overall problem can be constructed from optimal solutions to its sub-problems. If the best way to solve the whole problem does not depend on the best way to solve its pieces, DP does not apply.

- ### Overlapping Subproblems:
  A naive recursive solution solves the *same* smaller sub-problem many times over. This is what separates DP from plain divide-and-conquer — [Divide and Conquer and Backtracking](divide-and-conquer-backtracking.md) splits a problem into *independent* pieces that never repeat, so there is nothing to cache.

- ### Memoization (top-down):
  Keep the natural recursive formulation, but store each sub-problem's result the first time it is computed and return the cached value on every subsequent call with the same inputs.

  ```java
  Map<Integer, Long> cache = new HashMap<>();
  long fib(int n) {
      if (n <= 1) return n;
      if (cache.containsKey(n)) return cache.get(n);
      long result = fib(n - 1) + fib(n - 2);
      cache.put(n, result);
      return result;
  }
  ```

- ### Tabulation (bottom-up):
  Invert the recursion into an iterative loop that fills a table from the smallest sub-problem up to the final answer, avoiding recursion call-stack overhead entirely.

  ```java
  long fib(int n) {
      long[] table = new long[n + 1];
      table[1] = 1;
      for (int i = 2; i <= n; i++) {
          table[i] = table[i - 1] + table[i - 2];
      }
      return table[n];
  }
  ```

  Naive recursive Fibonacci is O(2^n) because `fib(n-2)` is recomputed independently inside both the `fib(n-1)` and `fib(n-2)` branches; both DP forms above collapse this to O(n) by solving each sub-problem exactly once. The same pattern — cache or tabulate to eliminate repeated work — underlies classic DP problems like coin change (minimum coins to make an amount) and the knapsack problem.

<sub>[Back to top](#table-of-contents)</sub>

---

## Greedy Algorithms

A greedy algorithm builds a solution step by step, at each step choosing whatever option looks best *right now*, and never reconsidering that choice later.

- ### The Greedy-Choice Property:
  A problem is safely solvable by a greedy algorithm only if a locally optimal choice at each step is provably part of *some* globally optimal solution. This must be proven for the specific problem — it is not true in general, and assuming it without proof is the most common greedy bug.

- ### Greedy vs. Dynamic Programming — the architect-relevant distinction:
  Greedy is simpler and faster because it makes one pass with no backtracking and no cache of sub-problem results; DP is slower and more complex because it must consider multiple candidate choices per step and remember their outcomes. Use greedy only when the greedy-choice property is proven for the problem; otherwise a greedy shortcut will silently return a suboptimal — not just slower — answer, which is worse than a slow-but-correct DP solution in most production contexts.

  | | Greedy | Dynamic Programming |
  |---|---|---|
  | Choice per step | One, final | Multiple candidates considered |
  | Revisits earlier choices | Never | Implicitly, via cached sub-problems |
  | Correctness | Only if greedy-choice property holds | Always, if optimal substructure holds |
  | Typical complexity | O(n log n) or better | O(n²) or higher, problem-dependent |

- ### Worked Example — Activity Selection:
  Given a set of activities each with a start and end time, select the maximum number of non-overlapping activities on a single resource. The greedy strategy — always pick the next activity with the *earliest end time* among those that don't conflict with what's already chosen — is provably optimal for this problem, making it a textbook example of the greedy-choice property holding.

  Huffman coding (building an optimal prefix-free binary encoding for data compression) is another classic greedy algorithm: at each step it merges the two lowest-frequency symbols, which is provably optimal for constructing a minimum-cost encoding tree.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How do I quickly tell if a problem needs DP instead of greedy?**
A: Ask whether a locally best choice could ever need to be undone later to reach the true optimum. If yes — the classic 0/1 knapsack problem is the standard example, where taking the highest-value item first can block a better combination later — greedy is unsafe and DP's exhaustive-but-cached approach is required. If a locally best choice can be proven to never need undoing (activity selection, minimum spanning tree via Kruskal's/Prim's), greedy is both correct and preferable.

---

**Q: Is memoization or tabulation the better default choice?**
A: Memoization is usually easier to write (it mirrors the natural recursive definition) and only computes sub-problems that are actually needed, but it carries recursion call-stack overhead and risk of stack overflow on deep inputs. Tabulation avoids both of those costs and is often more cache-friendly for the CPU, but sometimes computes sub-problems that turn out to be unnecessary. Default to memoization when prototyping or when the recursion depth is small; switch to tabulation once performance or stack depth becomes a concern.

---

**Q: Where does this show up in everyday architecture decisions, not just algorithm interviews?**
A: Greedy reasoning appears in load balancers (route to the least-loaded node right now), caching eviction (evict the least-recently-used entry now), and job schedulers (run the shortest job next). DP reasoning appears in route optimization, edit-distance/diff algorithms (used by version control and text comparison tools), and inventory/resource allocation planning where an early decision can foreclose a better later option. Recognizing which category a design problem falls into prevents shipping a greedy heuristic where correctness actually requires exhaustive DP.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Divide and Conquer and Backtracking Algorithms](divide-and-conquer-backtracking.md) — another recursive strategy family; contrast overlapping (DP) vs. independent (D&C) sub-problems
- [String Matching Algorithms](string-matching.md) — KMP's failure function is itself a small DP-style precomputation that avoids repeated work
- [Hashing Algorithms](hashing-algorithms.md) — memoization caches typically rely on a hash table under the hood
- [Graph Algorithms](graph-algorithms.md) — Dijkstra's algorithm is a greedy algorithm; many shortest-path variants are solved with DP
- [Hash Table](../data-structures/hash-table.md) — the structure typically used to implement a memoization cache

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Dynamic Programming — GeeksforGeeks](https://www.geeksforgeeks.org/dsa/dynamic-programming/) — Core concepts, memoization vs. tabulation, and worked problems
- [Greedy Algorithms — GeeksforGeeks](https://www.geeksforgeeks.org/dsa/greedy-algorithms/) — Greedy-choice property, proofs of correctness, and classic examples

---

[Get Started](../../get-started.md) | [Algorithms](../../get-started.md#algorithms)

---
