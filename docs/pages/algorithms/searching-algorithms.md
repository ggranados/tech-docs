# Searching Algorithms

---

## Table of Contents
<!-- TOC -->
* [Searching Algorithms](#searching-algorithms)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Linear Search](#linear-search)
  * [Binary Search](#binary-search)
  * [Comparison](#comparison)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Searching is the task of determining whether a target value exists within a collection and, if so, locating it. It sounds trivial, but the choice between the two fundamental approaches — Linear Search and Binary Search — is a recurring, concrete example of a broader architectural principle: a stronger precondition on your data (here, sortedness) can unlock an asymptotically faster algorithm. Recognizing that tradeoff is more valuable than memorizing either algorithm's mechanics.

---

## Overview

Every search algorithm makes a tradeoff between how much it assumes about the data's structure and how fast it can run. Linear Search assumes nothing — it works on any collection, sorted or not — and pays for that generality with O(n) time. Binary Search assumes the collection is sorted, and that single precondition is what lets it discard half the remaining search space on every comparison, dropping to O(log n). This is the same pattern that appears throughout system design: an index, a sort order, or a schema constraint is an investment paid once so that every subsequent query is cheaper.

<sub>[Back to top](#table-of-contents)</sub>

---

## Linear Search

Checks each element in the collection one at a time, in order, until the target is found or the collection is exhausted. It requires no precondition on the data — unsorted, sorted, or partially ordered, the algorithm behaves identically.

```text
for i = 0 to n-1:
    if arr[i] == target:
        return i
return -1
```

**When to use:** unsorted collections, collections too small for the overhead of a more complex algorithm to pay off, or one-off searches where the cost of sorting first (O(n log n)) would exceed the savings — sorting a collection purely to speed up a single search is a net loss.

**Stability of result:** returns the first matching index encountered in iteration order, which is well-defined and predictable even with duplicate values.

<sub>[Back to top](#table-of-contents)</sub>

---

## Binary Search

Repeatedly halves the search space: compare the target to the middle element, and if they don't match, recurse into the left half (if the target is smaller) or the right half (if the target is larger). Each comparison eliminates half of the remaining candidates, which is what drives the O(log n) time complexity.

**Precondition — this is the point:** Binary Search only works correctly on **sorted** input. Running it on unsorted data does not just risk missing the target — it produces silently incorrect results (it may report "not found" for a value that is actually present, without any error or warning), because the halving logic assumes an ordering that doesn't hold. This is the single most important architectural fact about Binary Search: the precondition is not optional, and violating it fails silently rather than loudly.

**Worked example:** searching for `23` in `[4, 8, 15, 16, 23, 42, 50]` (indices 0-6):

1. `low=0, high=6` → mid = 3 → `arr[3]=16` → `16 < 23` → search right half, `low=4`
2. `low=4, high=6` → mid = 5 → `arr[5]=42` → `42 > 23` → search left half, `high=4`
3. `low=4, high=4` → mid = 4 → `arr[4]=23` → match, return index 4

Three comparisons located the target in a 7-element array; a Linear Search could have needed up to 5.

**When to use:** any repeated-lookup scenario against data that is sorted or can be kept sorted — this is exactly why database indexes (typically B-trees, a generalization of the same halving principle) exist, and why converting an unsorted list to a sorted structure is worth it whenever lookups significantly outnumber insertions.

<sub>[Back to top](#table-of-contents)</sub>

---

## Comparison

| Algorithm | Best | Average | Worst | Space | Precondition |
|-----------|------|---------|-------|-------|--------------|
| Linear Search | O(1) | O(n) | O(n) | O(1) | None |
| Binary Search | O(1) | O(log n) | O(log n) | O(1) iterative / O(log n) recursive | Sorted input |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If Binary Search is so much faster, why would anyone still use Linear Search?**
A: Because its precondition — sorted data — isn't free. If the data is unsorted and you only need to search it once, sorting first (O(n log n)) plus searching (O(log n)) costs more than a single Linear Search (O(n)). Linear Search is also the only option for data structures without random access, like a plain linked list, and for streaming data where the full collection isn't available up front.

---

**Q: What actually happens if I run Binary Search on unsorted data?**
A: It doesn't throw an error or reliably fail — it silently returns wrong results. Because the algorithm decides which half to discard based on a single comparison against the midpoint, an out-of-order element can cause it to eliminate the half that actually contains the target. This is a classic example of a precondition violation that produces a correctness bug rather than a crash, which makes it dangerous in production if a caller ever passes unsorted input to a function that assumes Binary Search's guarantees.

---

**Q: How does this generalize beyond arrays?**
A: The same halving principle — discard half the remaining possibilities per comparison — underlies binary search trees (see [Trees](../data-structures/trees.md)), database B-tree indexes, and even non-data-structure applications like binary search over a monotonic answer space (e.g., "find the minimum threshold that satisfies a condition"). Recognizing "can I binary search this?" is a genuinely useful pattern-matching skill once the underlying data or answer space is known to be monotonic or sorted.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Sorting Algorithms](sorting-algorithms.md) — Binary Search's precondition (sorted input) makes this page's algorithms its direct prerequisite
- [Graph Algorithms](graph-algorithms.md) — DFS and BFS are the search algorithms of choice once data is structured as a graph rather than a flat sorted collection
- [Tree Traversal Algorithms](tree-traversal.md) — in-order traversal visits a Binary Search Tree in the same sorted order Binary Search assumes
- [Trees](../data-structures/trees.md) — the Binary Search Tree generalizes Binary Search's halving principle into a persistent, mutable data structure

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Binary Search — GeeksforGeeks](https://www.geeksforgeeks.org/dsa/binary-search/) — mechanics, complexity, and variants of Binary Search
- [Linear Search — GeeksforGeeks](https://www.geeksforgeeks.org/dsa/linear-search/) — mechanics and complexity of Linear Search

---

[Get Started](../../get-started.md) | [Algorithms](../../get-started.md#algorithms)

---
