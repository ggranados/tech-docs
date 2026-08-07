# Cryptography

---

## Table of Contents
<!-- TOC -->
* [Cryptography](#cryptography)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Symmetric Encryption](#symmetric-encryption)
  * [Asymmetric Encryption](#asymmetric-encryption)
  * [Hashing](#hashing)
  * [TLS and Certificates](#tls-and-certificates)
  * [Key Management](#key-management)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Cryptography is the practice of securing information so that only intended parties can read or verify it. For a software architect, cryptography is not something you implement from scratch — it's a set of building blocks (symmetric ciphers, asymmetric ciphers, hashing, and the protocols built on top of them like TLS) that you must know how to select, combine, and configure correctly. Getting the high-level model right matters more than knowing the math: most real-world breaches come from misuse of crypto primitives, not from breaking the primitives themselves.

---

## Overview

Modern systems rely on three cryptographic primitives working together: symmetric encryption for bulk data confidentiality, asymmetric (public-key) encryption for key exchange and identity, and cryptographic hashing for integrity and verification. None of these exist in isolation — TLS, the protocol that secures nearly all web traffic, is essentially an orchestration of all three: asymmetric cryptography to authenticate the server and agree on a shared secret, symmetric cryptography to encrypt the actual session data efficiently, and hashing to guarantee that data hasn't been tampered with in transit.

The architectural decisions that matter are rarely "which algorithm" (use well-vetted, current standards and let libraries handle it) but rather: where are keys stored and rotated, what happens when a key is compromised, and which layer of the system is responsible for encryption (application-level, transport-level, or storage-level).

<sub>[Back to top](#table-of-contents)</sub>

---

## Symmetric Encryption

Symmetric encryption uses a single shared secret key for both encryption and decryption. It is fast and well-suited to encrypting large volumes of data, which is why it's used for bulk data — file contents, database fields, and the actual payload of a TLS session.

- ### How it works:
  Both parties must possess the same secret key before communication begins. The sender encrypts plaintext with the key; the receiver decrypts the ciphertext with the same key.

  ```text
  plaintext --[encrypt with key K]--> ciphertext --[decrypt with key K]--> plaintext
  ```

- ### Common algorithms:
  **AES (Advanced Encryption Standard)** is the current industry standard, typically used as AES-256-GCM, which provides both confidentiality and integrity (an "authenticated encryption" mode). Older algorithms like DES and 3DES are considered broken or weak and should not be used in new systems.

- ### The key distribution problem:
  Symmetric encryption's core weakness is architectural, not mathematical: both parties need the same key, but how do you get that key to the other party securely over an insecure channel? This is exactly the problem asymmetric encryption solves.

<sub>[Back to top](#table-of-contents)</sub>

---

## Asymmetric Encryption

Asymmetric (public-key) encryption uses a mathematically linked key pair: a **public key**, which can be shared freely, and a **private key**, which must never leave its owner's control. Data encrypted with the public key can only be decrypted with the corresponding private key, and vice versa (the latter property underpins digital signatures).

- ### Why it exists:
  Asymmetric cryptography solves the key distribution problem that symmetric encryption cannot: two parties who have never met can establish secure communication, because only the public key — not the private key — needs to travel over the network.

- ### Common algorithms:
  **RSA** is the traditional choice, based on the difficulty of factoring large prime products. **ECC (Elliptic Curve Cryptography)** achieves equivalent security with much smaller keys and less computation, and is now the preferred choice for TLS and modern protocols (e.g., ECDSA for signatures, ECDHE for key exchange).

- ### Why it isn't used for bulk data:
  Asymmetric operations are computationally expensive — orders of magnitude slower than symmetric encryption. In practice, systems use a **hybrid approach**: asymmetric cryptography to securely exchange a short-lived symmetric session key, then symmetric encryption for the actual data. This is precisely the model TLS uses.

  ```mermaid
  flowchart LR
      A[Asymmetric crypto] -->|securely exchanges| B[Symmetric session key]
      B -->|encrypts| C[Bulk application data]
  ```

  **Caption:** Asymmetric cryptography solves key exchange; symmetric cryptography does the heavy lifting on actual data.

<sub>[Back to top](#table-of-contents)</sub>

---

## Hashing

A cryptographic hash function takes an input of any size and produces a fixed-size output (a "digest") such that the same input always produces the same digest, a small change in input produces a completely different digest, and it is computationally infeasible to reverse the digest back to the input or find two inputs that produce the same digest (a "collision").

Hashing is used for **integrity verification** (confirming a file or message hasn't been altered), **password storage** (storing a salted hash instead of the plaintext password), and as a building block inside digital signatures and TLS.

For a detailed comparison of specific hashing algorithms (SHA-1, MD5, and their known weaknesses), see [Hashing Algorithms](../algorithms/hashing-algorithms.md) — this page focuses on hashing's role within the broader cryptographic model rather than duplicating that algorithm-level detail.

> **Architectural note:** never use general-purpose hash functions (like SHA-256 alone) directly for password storage — use a purpose-built, slow, salted algorithm such as **bcrypt**, **scrypt**, or **Argon2**, which are deliberately expensive to compute in order to resist brute-force and rainbow-table attacks.

<sub>[Back to top](#table-of-contents)</sub>

---

## TLS and Certificates

TLS (Transport Layer Security) is the protocol that secures HTTPS and most other encrypted network traffic. It combines everything above: asymmetric cryptography to authenticate the server (and optionally the client) and negotiate a shared secret, symmetric cryptography to encrypt the session efficiently, and hashing to detect tampering.

- ### Certificates and the chain of trust:
  A **digital certificate** binds a public key to an identity (e.g., a domain name), and is signed by a **Certificate Authority (CA)** that browsers and operating systems trust by default. When a client connects to a server, it verifies the server's certificate chains back to a trusted root CA — this is how a browser knows it's really talking to `example.com` and not an impersonator.

- ### The handshake, conceptually:
  At a high level, the client and server agree on a protocol version and cipher suite, the server proves its identity with a certificate, both sides derive a shared symmetric key, and all subsequent traffic is encrypted with that key.

  ```mermaid
  sequenceDiagram
      participant Client
      participant Server

      Client->>Server: ClientHello (supported versions, cipher suites, random)
      Server->>Client: ServerHello + Certificate (server random, chosen cipher suite)
      Note over Client: Verify certificate against trusted CA chain
      Client->>Server: Key exchange material (encrypted with server's public key)
      Note over Client,Server: Both derive the same symmetric session key
      Client->>Server: Encrypted application data
      Server->>Client: Encrypted application data
  ```

  **Caption:** A simplified TLS handshake — asymmetric cryptography establishes trust and a shared key; symmetric cryptography secures everything after.

- ### Why it matters architecturally:
  Terminating TLS is a deliberate design decision — at a load balancer/API gateway (common, simpler certificate management) versus end-to-end to each service (stronger guarantees, more operational overhead). Choosing where TLS terminates defines your system's trust boundary.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Management

Cryptography is only as strong as the process protecting its keys. Key management covers how keys are **generated** (using cryptographically secure random sources), **stored** (never in source code or plaintext config — use a dedicated secrets manager or HSM), **rotated** (periodically, and immediately after suspected compromise), and **revoked** (certificates can be revoked before their natural expiry via CRLs or OCSP).

| Concern | Practice |
|---------|----------|
| Storage | Use a secrets manager or Hardware Security Module (HSM), never hardcode or commit keys |
| Rotation | Rotate keys on a schedule and immediately after any suspected exposure |
| Scope | Use different keys for different purposes/environments (dev vs. prod) to limit blast radius |
| Revocation | Have a tested process for revoking compromised certificates and keys quickly |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If asymmetric encryption solves the key distribution problem, why does TLS still use symmetric encryption at all?**
A: Asymmetric operations are computationally expensive relative to symmetric ones. TLS uses asymmetric cryptography only to bootstrap trust and agree on a shared secret; the actual application data is encrypted with fast symmetric ciphers (like AES) using that shared secret.

---

**Q: Why is it wrong to hash passwords with plain SHA-256?**
A: General-purpose hash functions are designed to be fast, which makes them vulnerable to brute-force and rainbow-table attacks against passwords. Purpose-built password hashing algorithms (bcrypt, scrypt, Argon2) are deliberately slow and incorporate salting, making large-scale guessing attacks impractical.

---

**Q: What's the architectural difference between terminating TLS at a load balancer versus end-to-end to each backend service?**
A: Terminating at the load balancer simplifies certificate management and offloads CPU cost, but traffic between the load balancer and internal services may be unencrypted unless a service mesh or internal TLS is added. End-to-end TLS extends the trust boundary to each service, which is stronger but adds certificate management overhead per service — a common driver for adopting a service mesh.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Hashing Algorithms](../algorithms/hashing-algorithms.md) — algorithm-level detail on SHA-1, MD5, and their known weaknesses
- [Secure Coding Practices](secure-coding-practices.md) — how secrets and cryptographic material should be handled in application code
- [Web Application Security](web-application-security.md) — how transport-layer security relates to broader web application threats
- [Security Architecture and Design](security-architecture-design.md) — where encryption fits into defense-in-depth and zero trust models

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [NIST Cryptographic Standards and Guidelines](https://csrc.nist.gov/projects/cryptographic-standards-and-guidelines) — authoritative federal cryptography standards
- [Mozilla TLS/SSL Guide](https://wiki.mozilla.org/Security/Server_Side_TLS) — practical TLS configuration guidance

---

[Get Started](../../get-started.md) | [Cyber-security Fundamentals](../../get-started.md#cyber-security-fundamentals)

---
