# Algorithms Content

**Severity:** High
**Depends on:** `content-template-compliance-audit`, pairs with `data-structures-content`

## Problem

`get-started.md` lists an "Algorithms" section (Sorting, Searching, Graph Algorithms, Dynamic Programming, Greedy, Divide and Conquer, Backtracking, String Matching, Tree Traversal, Hashing) with zero linked pages. Also note two existing formatting bugs in `get-started.md`: `Binary Search)`, `Rabin-Karp Algorithm)`, and `Postorder)` each have a stray trailing `)` — fix these while touching this section.

## Goal

Baseline breadth coverage of core algorithm families, each with complexity, a worked example, and (where useful) a Mermaid diagram of the process (e.g., a `flowchart` for divide-and-conquer recursion, a `sequenceDiagram` for graph traversal order).

## Scope hints

- Group by family rather than one page per named algorithm: "Sorting Algorithms", "Searching Algorithms", "Graph Algorithms" (DFS/BFS/Dijkstra together), "Dynamic Programming & Greedy", "Divide and Conquer & Backtracking", "String Matching", "Tree Traversal", "Hashing Algorithms" — resolve exact grouping during `speckit-clarify`.
- Fix the stray `)` typos in `get-started.md`'s Algorithms section as part of this spec.
- Cross-link heavily with `data-structures-content` (e.g., Graph Algorithms ↔ Graph structure page).
- New directory: `docs/pages/algorithms/`.

## Out of scope

- Data structures themselves (separate spec: `data-structures-content`).
