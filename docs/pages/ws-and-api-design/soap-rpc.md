# SOAP and RPC

---

## Table of Contents
<!-- TOC -->
* [SOAP and RPC](#soap-and-rpc)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [SOAP](#soap)
  * [RPC](#rpc)
  * [Key Concepts](#key-concepts)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

SOAP (Simple Object Access Protocol) and RPC (Remote Procedure Call) represent an earlier generation of web service design, built around strict contracts and procedure-oriented thinking rather than resource-oriented thinking like REST. They are frequently labeled "legacy," but that label undersells their continued relevance: large swaths of banking, insurance, healthcare, and government systems run on SOAP today, and RPC as a concept underlies modern protocols like gRPC. An architect who only knows REST and GraphQL will be unprepared the first time they need to integrate with a decades-old core banking system or a hospital's HL7/SOAP-based interface.

---

## Overview

SOAP emerged in the late 1990s alongside XML as the dominant data interchange format, standardized by the W3C to let applications on different platforms and languages exchange structured messages over HTTP, SMTP, or other transports. RPC predates SOAP by decades — the general idea of invoking a function on a remote machine as if it were local dates back to the 1970s and 1980s (Sun RPC, CORBA, DCOM), and SOAP was, in essence, one popular way to carry RPC-style calls over XML.

Both approaches prioritize a formally defined contract between client and server over the flexibility that REST or GraphQL offer. That rigidity is exactly why they persist: regulated industries value the ability to generate strict client/server code from a machine-readable contract, validate every message against a schema, and layer in standardized, auditable security extensions.

<sub>[Back to top](#table-of-contents)</sub>

---

## SOAP

SOAP structures every request and response as an XML "envelope," with well-defined header and body sections, and is almost always paired with a contract-first design workflow.

- ### SOAP Envelope:
  Every SOAP message is wrapped in an `<Envelope>` element containing an optional `<Header>` (metadata such as security tokens, transaction IDs) and a mandatory `<Body>` (the actual request or response payload).

  ```xml
  <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
    <soap:Header>
      <!-- auth tokens, correlation IDs, WS-* extensions -->
    </soap:Header>
    <soap:Body>
      <GetAccountBalance xmlns="urn:example:banking">
        <AccountId>12345</AccountId>
      </GetAccountBalance>
    </soap:Body>
  </soap:Envelope>
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### WSDL and Contract-First Design:
  SOAP services are described by a WSDL (Web Services Description Language) document — an XML file specifying every operation, its input/output message shapes, data types (via XML Schema), and endpoint bindings. Client and server stubs are typically code-generated from the WSDL, so both sides agree on an exact, machine-verifiable contract before a single line of business logic is written. This is the opposite of REST's "design the resource, document it afterward" tendency, and it's a major reason enterprises with strict integration governance still favor it.

  ```mermaid
  flowchart LR
      WSDL["WSDL Contract"] -->|generates| ClientStub["Client Stub"]
      WSDL -->|generates| ServerStub["Server Skeleton"]
      ClientStub -->|SOAP/XML over HTTP| ServerStub
  ```

  **Caption:** Both client and server code are generated from the same WSDL contract, eliminating ambiguity about request/response shapes.

<sub>[Back to top](#table-of-contents)</sub>

- ### WS-* Standards:
  SOAP's ecosystem includes a family of WS-* specifications that standardize cross-cutting concerns directly at the protocol level, rather than leaving them to ad-hoc convention: **WS-Security** (message-level encryption and signing), **WS-ReliableMessaging** (guaranteed delivery), **WS-AtomicTransaction** (distributed transactions), and **WS-Addressing** (endpoint routing metadata), among others. This is why SOAP persists in regulated industries — some compliance regimes essentially require the kind of message-level, auditable security that WS-Security provides, which is harder to bolt onto a plain REST/JSON API after the fact.

<sub>[Back to top](#table-of-contents)</sub>

---

## RPC

RPC is the broader concept that SOAP (and later, gRPC) are specific implementations of: calling a procedure on a remote system as though it were a local function call, hiding the network round-trip behind a normal-looking method signature.

- ### The RPC Model:
  An RPC framework provides stubs (client-side proxies) that make a remote call look like `balance = accountService.getBalance(accountId)` instead of manually constructing HTTP requests. The framework handles serialization, transport, and deserialization behind the scenes. Classic RPC systems include Sun RPC/ONC RPC, CORBA, DCOM, and Java RMI — each tightly coupled to a specific platform or language.

  ```mermaid
  sequenceDiagram
      participant Client
      participant Stub as Client Stub
      participant Server
      Client->>Stub: getBalance(accountId)
      Stub->>Server: serialized request over network
      Server-->>Stub: serialized response
      Stub-->>Client: return value
  ```

  **Caption:** RPC hides network serialization and transport behind a call that looks local.

<sub>[Back to top](#table-of-contents)</sub>

- ### From Classic RPC to gRPC:
  Modern RPC frameworks like gRPC keep the same "call a remote method" mental model but replace XML envelopes and heavyweight WS-* tooling with Protocol Buffers for compact binary serialization and HTTP/2 for multiplexed, streaming transport. In that sense, gRPC is RPC's modern evolution — same core idea as SOAP-as-RPC, but optimized for microservices and lower latency instead of document-oriented enterprise integration. See [gRPC and GraphQL](grpc-graphql.md) for how gRPC compares to SOAP and to resource/query-oriented styles.

  > See also: [gRPC and GraphQL](grpc-graphql.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Envelope | The top-level XML wrapper (`Header` + `Body`) around every SOAP message |
| WSDL | Machine-readable contract describing a SOAP service's operations and data types |
| WS-Security | WS-* standard for message-level signing and encryption of SOAP messages |
| Stub | Generated client-side proxy code that makes a remote call look like a local one |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why would a new project ever choose SOAP over REST or gRPC today?**
A: Rarely for a greenfield project — but you don't always get to choose. If you're integrating with an existing enterprise system (a bank's core ledger, an insurance clearinghouse, a hospital's system) that already exposes SOAP with WS-Security, building a SOAP client is the pragmatic path, versus asking that system's owner to re-platform for you.

---

**Q: What's the practical difference between "contract-first" (SOAP/WSDL) and "code-first" (typical REST) API development?**
A: Contract-first means the interface definition (WSDL) is written and agreed upon before implementation, and code is generated from it — strong consistency, but slower to iterate. Code-first means the API emerges from the implementation (and is documented after, e.g. via OpenAPI), which is faster to iterate but risks drift between teams' understanding of the contract.

---

**Q: Is gRPC "the same thing" as SOAP since both are RPC-style?**
A: They share the RPC mental model (call a remote method) but differ heavily in mechanics: gRPC uses compact binary Protocol Buffers over HTTP/2 with built-in streaming, while SOAP uses verbose XML typically over plain HTTP/1.1. gRPC targets internal microservice performance; SOAP targets cross-organization contract rigor and standardized security.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [gRPC and GraphQL](grpc-graphql.md) — gRPC as RPC's modern, binary/HTTP2 evolution.
- [RESTful APIs](restful.md) — the resource-oriented style that displaced SOAP for most new public APIs.
- [Webhooks and Event-Driven APIs](webhooks-event-driven-apis.md) — a lighter-weight, push-based integration style contrasted with request/response RPC.
- [Authentication and Authorization](authn-and-authz/authn-authz.md) — where WS-Security fits among broader authN/authZ approaches.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [W3C SOAP Version 1.2 Specification](https://www.w3.org/TR/soap12/) — official protocol specification.
- [W3C Web Services Description Language (WSDL) 1.1](https://www.w3.org/TR/wsdl/) — official WSDL specification.

---

[Get Started](../../get-started.md) | [Web Services and API Design](../../get-started.md#web-services-and-api-design)

---
