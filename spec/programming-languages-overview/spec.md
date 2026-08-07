# Programming Languages Overview Pages

**Severity:** Medium
**Depends on:** `content-template-compliance-audit`

## Problem

`get-started.md`'s Languages list names 15 languages (JavaScript, Python, Java, PHP, Ruby, C#, Go, Rust, Kotlin, Clojure, Swift, JavaScript [duplicate entry], HTML, CSS, TypeScript) but only Java has any page. Java itself has deep, multi-level coverage (versions, collections, concurrency, streams, etc.) while every other language is an unlinked bullet — a stark asymmetry for a repo meant to be a broad architect training path across languages, not a Java-specific deep dive.

## Goal

Give each listed language a single breadth-level overview page (paradigm, typical use cases, ecosystem/tooling notes, where it fits architecturally) at a much lighter depth than the Java tree — Java's depth is the exception, not the bar to match. Fix the duplicate "JavaScript" entry in `get-started.md`.

## Scope hints

- One page per language: what it's for, paradigm(s), typical runtime/deployment shape, 1-2 killer use cases, brief ecosystem note, links to official docs.
- Remove the duplicate "JavaScript" bullet in `get-started.md`'s Languages list.
- Group HTML/CSS together as "web markup/styling fundamentals" rather than treating them as general-purpose languages, if that reads more naturally.
- New directory: `docs/pages/programming/languages/{language}/` following the existing Java convention, but each new language stays to a single overview page (no deep sub-tree) unless a future spec calls for it.

## Out of scope

- Expanding Java further (already well covered).
- Framework-level content (separate spec: `frameworks-overview`).
