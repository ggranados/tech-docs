# HTTP/HTTPS and Data Formats

---

## Table of Contents
<!-- TOC -->
* [HTTP/HTTPS and Data Formats](#httphttps-and-data-formats)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [HTTP and HTTPS](#http-and-https)
  * [Data Formats](#data-formats)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Every API decision described elsewhere in this section — REST, gRPC, GraphQL, versioning, security — ultimately rides on top of two more fundamental choices: which version of HTTP is transporting the bytes, and which format those bytes are serialized in. This page covers both as the foundational layer underneath the rest of API design.

---

## Overview

HTTP is the application-layer protocol nearly every API — REST, GraphQL, and (via HTTP/2) gRPC — is built on. Its evolution from HTTP/1.1 through HTTP/2 to HTTP/3 has been driven almost entirely by performance: reducing connection overhead and head-of-line blocking as APIs and web pages made more and more concurrent requests.

Sitting alongside the transport is the question of data format: how a request or response body is serialized. JSON, XML, Protocol Buffers, and Avro each optimize for a different trade-off between human-readability, size, speed, and schema evolution — and the right choice depends heavily on who (or what) is producing and consuming the data.

<sub>[Back to top](#table-of-contents)</sub>

---

## HTTP and HTTPS

HTTP defines how clients and servers exchange messages; HTTPS adds a layer of transport encryption on top.

- ### Methods and Idempotency
  HTTP methods carry semantic meaning that REST relies on directly: `GET`, `PUT`, and `DELETE` are defined as **idempotent** (repeating the same request has the same effect as making it once), while `POST` is not. This matters architecturally for retry logic — a client can safely retry a failed `PUT` after a timeout, but retrying a `POST` risks creating a duplicate resource unless idempotency is handled explicitly (e.g., via an idempotency key).

  | Method | Idempotent | Typical REST use |
  |---|---|---|
  | GET | Yes | Retrieve a resource |
  | PUT | Yes | Replace a resource |
  | DELETE | Yes | Remove a resource |
  | PATCH | No (by spec) | Partially update a resource |
  | POST | No | Create a resource / non-idempotent action |

  > See also: [RESTful Architecture](restful.md) — REST's uniform interface constraint is built directly on these HTTP method semantics.

<sub>[Back to top](#table-of-contents)</sub>

- ### HTTP/1.1 vs. HTTP/2 vs. HTTP/3
  **HTTP/1.1** sends requests over a connection largely one at a time (mitigated in practice by opening multiple connections), leading to head-of-line blocking. **HTTP/2** introduced multiplexed streams over a single TCP connection, header compression, and server push, letting many requests and responses interleave without blocking each other — this is also the transport gRPC depends on. **HTTP/3** replaces TCP with QUIC (built on UDP), removing TCP-level head-of-line blocking entirely and improving connection setup latency, which matters most on unreliable or high-latency networks.

  ```mermaid
  flowchart LR
      A[HTTP/1.1: sequential requests per connection] --> B[HTTP/2: multiplexed streams, one TCP connection]
      B --> C[HTTP/3: multiplexed streams over QUIC/UDP]
  ```

  **Caption:** Each HTTP version reduces a different source of latency, culminating in HTTP/3's move away from TCP entirely.

<sub>[Back to top](#table-of-contents)</sub>

- ### TLS and HTTPS
  HTTPS is HTTP layered over **TLS (Transport Layer Security)**, providing encryption in transit, server authentication (via certificates), and integrity guarantees against tampering. Virtually every production API should be served exclusively over HTTPS — the mechanics of how TLS establishes a secure channel (handshakes, certificates, cipher suites) are covered in depth elsewhere.

  > See also: [Cryptography](../cyber-security/cryptography.md) — the underlying TLS/cryptographic mechanics that make HTTPS secure.

<sub>[Back to top](#table-of-contents)</sub>

---

## Data Formats

The choice of data format determines how request and response bodies are serialized, with direct consequences for size, speed, readability, and how safely a schema can evolve.

- ### JSON
  JSON (JavaScript Object Notation) is the de facto default for REST and GraphQL APIs: human-readable, natively supported by virtually every language and browser, and simple to debug. Its trade-offs are verbosity (repeated field names in every object) and the lack of a built-in schema — validation is typically layered on top via JSON Schema or OpenAPI.

  ```json
  { "id": 123, "name": "Widget", "price": 9.99 }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### XML
  XML predates JSON as a data interchange format and is still common in enterprise and SOAP-based systems, and anywhere document structure (attributes, namespaces, mixed content) matters more than compactness. It supports strong schema validation via XSD, but is markedly more verbose than JSON and has largely been displaced by JSON for new REST APIs.

  ```xml
  <product id="123"><name>Widget</name><price>9.99</price></product>
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Protocol Buffers
  Protocol Buffers (Protobuf) is a compact binary format defined by a schema (`.proto` file), compiled into typed code per language. It is dramatically smaller and faster to (de)serialize than JSON, at the cost of not being human-readable on the wire and requiring generated code to consume. This is precisely the trade-off gRPC accepts in exchange for performance and strong contracts between internal services.

  > See also: [gRPC and GraphQL](grpc-graphql.md) — Protobuf as gRPC's wire format and interface definition language.

<sub>[Back to top](#table-of-contents)</sub>

- ### Avro
  Avro is a binary serialization format popular in data pipelines and event streaming systems, where records flow through a system over long periods and the schema needs to **evolve** without breaking older consumers or producers. Avro embeds (or references, via a schema registry) the schema alongside the data, enabling controlled schema evolution — adding fields with defaults, for instance — which is a common requirement in systems like Kafka, where producers and consumers are deployed independently and rarely upgrade in lockstep.

  > See also: [Kafka](../data-processing/real-time/event-streaming/kafka.md) — a common home for Avro-encoded events and schema registries in practice.

<sub>[Back to top](#table-of-contents)</sub>

- ### Choosing a Format

  | Format | Human-readable | Size/Speed | Schema evolution | Typical use |
  |---|---|---|---|---|
  | JSON | Yes | Moderate | Ad hoc (OpenAPI/JSON Schema) | REST, GraphQL, public APIs |
  | XML | Yes | Verbose | Strong (XSD) | SOAP, legacy enterprise systems |
  | Protobuf | No | Fast/compact | Strong (proto contracts) | gRPC, internal service-to-service calls |
  | Avro | No | Fast/compact | Strong, designed for evolution | Event streaming, data pipelines (e.g. Kafka) |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why does gRPC require HTTP/2 specifically, rather than working over HTTP/1.1?**
A: gRPC relies on HTTP/2's multiplexed streams and trailers to support its four call types, including bidirectional streaming — HTTP/1.1's request-response-per-connection model cannot express a long-lived, full-duplex stream the way HTTP/2 does.

---

**Q: If Protobuf is faster and smaller than JSON, why do most public REST APIs still use JSON?**
A: Because JSON's ubiquity, human-readability, and zero-setup consumption (no generated code required) outweigh the size/speed cost for APIs consumed by many unknown, heterogeneous clients — exactly the audience REST is designed for.

---

**Q: What makes Avro better suited than JSON for a Kafka event stream specifically?**
A: Kafka producers and consumers are deployed and upgraded independently, often over years. Avro's schema-evolution guarantees (safely adding/removing fields with defaults, tracked via a schema registry) let the event contract change without breaking consumers still running an older schema version — something JSON alone doesn't enforce.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [RESTful Architecture](restful.md) — REST's method semantics and JSON usage build directly on the HTTP and data-format concepts here.
- [gRPC and GraphQL](grpc-graphql.md) — gRPC's dependence on HTTP/2 and Protobuf, detailed here at the transport/format level.
- [API Versioning and Security](api-versioning-security.md) — TLS/HTTPS is a baseline security control referenced from this page.
- [Error Handling and CORS](error-handling-cors.md) — HTTP status codes and methods referenced here are used throughout error handling.
- [Cryptography](../cyber-security/cryptography.md) — full detail on the TLS mechanics underpinning HTTPS.
- [Kafka](../data-processing/real-time/event-streaming/kafka.md) — a common consumer of Avro-encoded, schema-evolved event data.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [RFC 9110 — HTTP Semantics](https://www.rfc-editor.org/rfc/rfc9110) — official specification of HTTP methods, idempotency, and status codes.
- [RFC 9114 — HTTP/3](https://www.rfc-editor.org/rfc/rfc9114) — official HTTP/3 specification.
- [Apache Avro Specification](https://avro.apache.org/docs/current/specification/) — official Avro schema and format specification.

---

[Get Started](../../get-started.md) | [Web Services and API Design](../../get-started.md#web-services-and-api-design)

---
