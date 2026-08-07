# String Matching Algorithms

---

## Table of Contents
<!-- TOC -->
* [String Matching Algorithms](#string-matching-algorithms)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Knuth-Morris-Pratt (KMP) Algorithm](#knuth-morris-pratt-kmp-algorithm)
  * [Rabin-Karp Algorithm](#rabin-karp-algorithm)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

String matching answers a deceptively simple question: does a pattern of length `m` appear inside a text of length `n`, and if so, where? It underlies text editors' find/replace, log-scanning tools, intrusion detection, plagiarism checkers, and the substring search built into every mainstream language's standard library. The Knuth-Morris-Pratt (KMP) and Rabin-Karp algorithms are the two classic answers to "how do we do this faster than the obvious approach," and each improves on the naive baseline through a different mechanism — precomputed structure versus hashing.

---

## Overview

The naive approach to string matching slides the pattern across the text one position at a time and, at each position, compares characters one by one until a mismatch or a full match is found. In the worst case (e.g., searching for "AAAA...B" inside "AAAA...A") this re-examines large overlapping stretches of the text repeatedly, costing O(n·m) time. Both KMP and Rabin-Karp exist specifically to eliminate that redundant re-scanning, but they attack the problem from opposite directions: KMP precomputes information *about the pattern itself* so it never needs to re-check characters it has already matched, while Rabin-Karp uses hashing to compare candidate windows in (amortized) constant time instead of comparing them character by character.

Neither algorithm changes what is being searched for or found — both still return the same match positions the naive approach would — they only change how much redundant work is avoided along the way. That trade-off between precomputed structure and probabilistic hashing recurs throughout algorithm design, and understanding it here transfers directly to indexing and caching decisions elsewhere in a system.

<sub>[Back to top](#table-of-contents)</sub>

---

## Knuth-Morris-Pratt (KMP) Algorithm

KMP eliminates redundant comparisons by exploiting a simple observation: when a mismatch occurs partway through a match, the characters already matched tell you something about the pattern itself, which can be used to skip ahead in the text without re-checking those characters.

- ### Failure Function (Partial Match Table):
  Before scanning the text, KMP precomputes a table over the pattern alone — for every prefix of the pattern, the table records the length of the longest proper prefix of that prefix which is also a suffix of it. In practical terms, this table tells the algorithm, the moment a mismatch happens, exactly how far it can safely shift the pattern without skipping over a possible match — no need to re-examine text characters already known to match. Deriving the table's values by hand is not the useful skill here; recognizing *what it buys* — the text pointer never moves backward — is.

- ### Complexity:
  Building the failure function costs O(m); scanning the text costs O(n) because the text pointer only ever advances. Total: O(n + m), a strict improvement over the naive O(n·m) worst case.

<sub>[Back to top](#table-of-contents)</sub>

---

## Rabin-Karp Algorithm

Rabin-Karp reframes string matching as a hashing problem: instead of comparing the pattern against every window of the text character by character, compute a hash of the pattern once, then compute a hash of each equal-length window of the text and compare hashes first.

- ### Rolling Hash:
  Recomputing a window's hash from scratch at every shift would cost as much as the naive comparison it's trying to avoid. A rolling hash solves this by updating the previous window's hash in O(1) — subtracting the outgoing character's contribution and adding the incoming character's contribution — rather than rehashing the entire window. This is the same mechanism (fixed-size digest derived from input) discussed generally in [Hashing Algorithms](hashing-algorithms.md), applied here for fast equality pre-checks rather than for security.

  > See also: [Hash Table](../data-structures/hash-table.md) — Rabin-Karp's hash-then-compare strategy is conceptually the same trade a hash table makes: spend a cheap hash computation to avoid an expensive direct comparison.

- ### Why Hashes Alone Aren't Enough:
  Two different windows can hash to the same value (a collision), so a hash match is only a *candidate* — Rabin-Karp still performs a direct character-by-character comparison to confirm a true match before reporting it. This keeps the algorithm correct even though the hash function is not perfect.

- ### Complexity:
  Average case O(n + m), thanks to the O(1) rolling hash update and the fact that most windows are eliminated by a hash mismatch without a full comparison. Worst case degrades to O(n·m) if the hash function produces many collisions (e.g., an adversarial input or a poorly chosen hash), forcing a full character comparison at nearly every window.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If both KMP and Rabin-Karp beat the naive O(n·m) approach, when would I pick one over the other?**
A: KMP has a guaranteed O(n + m) worst case with no dependency on hash quality, making it the safer default for a single pattern search where predictable performance matters. Rabin-Karp's real advantage shows up when searching for *multiple* patterns simultaneously — since hashing lets you compare a text window against a set of pattern hashes in roughly the same cost as comparing against one — which is why it's the basis for plagiarism detection and multi-pattern log scanning tools.

---

**Q: Rabin-Karp sounds like it depends on the hash function being good. What happens if it isn't?**
A: A poor hash function that produces frequent collisions forces Rabin-Karp back down toward the naive O(n·m) worst case, because every collision triggers a full character comparison to rule out a false match. This is the same lesson as hash table design (see [Hash Table](../data-structures/hash-table.md)) — the algorithm's average-case speed is only as good as the hash function's distribution, which is why production implementations use well-studied rolling hash constructions rather than ad-hoc ones.

---

**Q: Why not just always use the language's built-in `indexOf`/`contains` method instead of implementing either of these?**
A: For most application code, yes — standard library substring search is already well-optimized (often a variant of these same algorithms, or Boyer-Moore) and reimplementing it is unnecessary risk. The value of understanding KMP and Rabin-Karp is recognizing *when* the built-in isn't enough: multi-pattern search, streaming text where the whole input isn't in memory at once, or approximate/fuzzy matching — situations where you need to choose or adapt the underlying algorithm rather than call a single library function.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Hashing Algorithms](hashing-algorithms.md) — Rabin-Karp's rolling hash and cryptographic hash functions share the "fixed-size digest from input" concept, though they serve very different purposes
- [Hash Table](../data-structures/hash-table.md) — the data structure built on the same hash-then-compare trade-off Rabin-Karp uses for matching
- [Dynamic Programming and Greedy Algorithms](dynamic-programming-greedy.md) — KMP's failure function is itself a small precomputation table conceptually similar to a DP tabulation step
- [Sorting Algorithms](sorting-algorithms.md) — another family of algorithms whose main goal is eliminating redundant comparisons

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [KMP Algorithm — GeeksforGeeks](https://www.geeksforgeeks.org/dsa/kmp-algorithm-for-pattern-searching/) — Failure function construction and pattern search walkthrough
- [Rabin-Karp Algorithm — GeeksforGeeks](https://www.geeksforgeeks.org/dsa/rabin-karp-algorithm-for-pattern-searching/) — Rolling hash mechanics and complexity analysis

---

[Get Started](../../get-started.md) | [Algorithms](../../get-started.md#algorithms)

---
