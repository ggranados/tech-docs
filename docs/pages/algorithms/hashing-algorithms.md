# Hashing Algorithms

---

## Table of Contents
<!-- TOC -->
* [Hashing Algorithms](#hashing-algorithms)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [SHA-1](#sha-1)
  * [MD5](#md5)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

SHA-1 and MD5 are **cryptographic hash functions**: algorithms that take an input of arbitrary size and produce a fixed-length digest, designed so that even a tiny change to the input produces a completely different digest and so that recovering the input from the digest (or finding two inputs that share a digest) should be computationally infeasible. This is a fundamentally different purpose from the hash function used inside a hash table — and, critically for an architect, both algorithms are now considered cryptographically broken and must not be used anywhere security depends on them.

---

## Overview

A cryptographic hash function must satisfy properties a general-purpose hash function does not need: it must be **one-way** (infeasible to reconstruct the input from the output), it must exhibit the **avalanche effect** (a one-bit change in input flips roughly half the output bits, so similar inputs never produce similar-looking digests), and it must be **collision-resistant** (infeasible to find two different inputs that produce the same digest). These properties are what make a cryptographic hash trustworthy for verifying integrity, signing data, and — historically — storing passwords.

SHA-1 and MD5 were both designed for exactly this purpose and were once the industry standard. Both have since had practical, real-world attacks demonstrated against their collision resistance, which is why neither belongs in any new design that depends on cryptographic guarantees.

<sub>[Back to top](#table-of-contents)</sub>

---

## SHA-1

SHA-1 (Secure Hash Algorithm 1), published by NIST in 1995, produces a 160-bit (20-byte) digest and was for years the default choice for certificate signing, Git commit identifiers, and general data integrity verification.

- ### Status:
  In 2017, Google and CWI Amsterdam publicly demonstrated a practical collision attack (dubbed "SHAttered"), producing two different PDF files with identical SHA-1 digests. NIST formally deprecated SHA-1 for digital signatures, and modern TLS/CA standards no longer accept it. It should be treated as broken for any security-sensitive purpose.

<sub>[Back to top](#table-of-contents)</sub>

---

## MD5

MD5 (Message Digest Algorithm 5), published in 1992, produces a 128-bit (16-byte) digest and was for years the ubiquitous default for checksums and (incorrectly) password storage.

- ### Status:
  MD5's collision resistance was broken far earlier and far more severely than SHA-1's — practical collision attacks have existed since 2004, and generating two colliding inputs is now computationally trivial (well under a second on commodity hardware). MD5 must not be relied on for any integrity or security guarantee.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Is MD5/SHA-1's hash function the same kind of thing as the hash function inside a HashMap?**
A: No, and conflating the two is a common mistake. A [Hash Table](../data-structures/hash-table.md)'s hash function only needs to be *fast* and *well-distributed* across buckets — it has no security requirement, does not need to resist an adversary trying to construct collisions, and in fact a hash table happily tolerates occasional collisions because it has a defined resolution strategy (chaining or probing) for them. A cryptographic hash function like SHA-1 or MD5 must additionally be one-way and collision-*resistant against a deliberate attacker* — properties a hash table's hash function was never designed to provide and should never be assumed to have.

---

**Q: If MD5 and SHA-1 are both broken, is there ever a legitimate reason to use them today?**
A: Yes, for non-security purposes where an adversary is not part of the threat model: detecting *accidental* data corruption (a checksum on a downloaded file, deduplication of identical files, cache-key generation from non-sensitive input). In those contexts, the collision attack's cost and complexity are irrelevant because nobody is trying to forge a matching file on purpose. The line to enforce is strict: the moment a hash is used to prove authenticity, verify a signature, or protect a secret, MD5 and SHA-1 must be replaced with SHA-256, SHA-3, or a purpose-built alternative.

---

**Q: What should be used instead, especially for password storage?**
A: For general cryptographic hashing (integrity, signatures, certificates), use SHA-256 or SHA-3 — both remain unbroken and are the current NIST-recommended defaults. For password storage specifically, no general-purpose cryptographic hash (including SHA-256) should be used directly, because they're deliberately fast — exactly the wrong property when an attacker is brute-forcing guesses offline. Use a dedicated password-hashing algorithm such as **bcrypt** or **Argon2**, which are intentionally slow and memory-hard to make brute-force attacks impractical.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Hash Table](../data-structures/hash-table.md) — uses a hash function for speed and distribution, not security; do not confuse the two purposes
- [String Matching Algorithms](string-matching.md) — Rabin-Karp's rolling hash is a non-cryptographic hash used for fast comparison, illustrating the same distinction from a different angle
- [Dynamic Programming and Greedy Algorithms](dynamic-programming-greedy.md) — memoization caches are typically backed by a hash table, not a cryptographic hash

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [NIST Policy on Hash Functions](https://csrc.nist.gov/projects/hash-functions) — Official status and guidance on SHA-1, SHA-2, SHA-3, and deprecated algorithms
- [SHAttered — shattered.io](https://shattered.io/) — Google/CWI Amsterdam's practical SHA-1 collision attack writeup

---

[Get Started](../../get-started.md) | [Algorithms](../../get-started.md#algorithms)

---
