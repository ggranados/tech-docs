# gRPC and GraphQL

---

## Table of Contents
<!-- TOC -->
* [gRPC and GraphQL](#grpc-and-graphql)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [gRPC](#grpc)
  * [GraphQL](#graphql)
  * [Choosing Between REST, gRPC, and GraphQL](#choosing-between-rest-grpc-and-graphql)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

REST is not the only way to expose an API. gRPC and GraphQL emerged to solve problems REST handles awkwardly: efficient, strongly-typed, streaming communication between internal services, and flexible, client-driven data fetching for front ends with many shapes of consumer. Understanding when each style earns its complexity is a core architectural decision, not a fashion choice.

---

## Overview

Both gRPC and GraphQL trade some of REST's simplicity and ubiquity for capabilities REST does not provide out of the box. gRPC optimizes for performance and contract rigor between services you control on both ends. GraphQL optimizes for flexibility on the client side when many different consumers need different slices of the same data.

Neither replaces REST outright — most real systems use REST at the public edge, gRPC between internal services, and sometimes GraphQL as an aggregation layer for front ends. Picking the right tool per boundary, rather than standardizing on one everywhere, is usually the better architectural call.

<sub>[Back to top](#table-of-contents)</sub>

---

## gRPC

gRPC is a high-performance RPC (Remote Procedure Call) framework built by Google on top of HTTP/2, using Protocol Buffers as its interface definition language and wire format.

- ### Protocol Buffers (Protobuf)
  Protobuf is a binary serialization format defined by a `.proto` schema file. The schema is compiled into client and server stubs in multiple languages, producing a **strongly-typed contract** that both sides must honor. Unlike REST, where the request/response shape is often documented informally (OpenAPI, if you're disciplined), gRPC makes the contract a compile-time artifact.

  ```protobuf
  service OrderService {
    rpc GetOrder (OrderRequest) returns (OrderResponse);
  }
  message OrderRequest {
    string order_id = 1;
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### HTTP/2 Transport
  gRPC runs over HTTP/2, gaining multiplexed streams over a single TCP connection, header compression, and full-duplex communication. This is a key difference from REST, which is commonly implemented over HTTP/1.1 or HTTP/2 without exploiting streaming.

<sub>[Back to top](#table-of-contents)</sub>

- ### Streaming Support
  gRPC natively supports four call types: unary (request/response, like REST), server streaming, client streaming, and bidirectional streaming. This makes it well suited to use cases like live telemetry, chat, or progressive data transfer that REST/JSON handles poorly.

  ```mermaid
  sequenceDiagram
      participant C as Client
      participant S as Server
      C->>S: Open bidirectional stream
      loop while connected
          C->>S: message
          S-->>C: message
      end
  ```

  **Caption:** A gRPC bidirectional stream lets client and server exchange messages independently over one long-lived connection.

<sub>[Back to top](#table-of-contents)</sub>

- ### Why gRPC for Service-to-Service Calls
  Internal microservice communication benefits from gRPC's small binary payloads, generated client/server code (removing hand-written HTTP clients and DTO mapping), and built-in support for deadlines, cancellation, and streaming. Because all participants are under your control, the cost of requiring a shared `.proto` schema and generated stubs is acceptable — unlike a public API where arbitrary, unknown clients must be able to consume it with nothing more than a browser or `curl`. See [Microservices](../architectural-patterns/microservices.md) for the broader architectural context where gRPC is typically adopted for inter-service calls.

  > See also: [RESTful Architecture](restful.md) — gRPC is frequently compared to REST as the alternative for internal, performance-sensitive communication.

<sub>[Back to top](#table-of-contents)</sub>

---

## GraphQL

GraphQL is a query language and runtime for APIs, developed by Facebook, that lets clients request exactly the data they need through a single endpoint.

- ### Single Endpoint, Client-Specified Queries
  Unlike REST, which exposes many resource-oriented URIs (`/users/123`, `/users/123/orders`), GraphQL typically exposes one endpoint (e.g. `/graphql`). The client sends a query describing the exact fields it wants, across potentially multiple related resources, in a single request.

  ```graphql
  query {
    user(id: "123") {
      name
      orders(limit: 5) {
        id
        total
      }
    }
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Solving Over-Fetching and Under-Fetching
  REST endpoints return a fixed shape: a mobile client that only needs a user's name still receives the full user object (**over-fetching**), while a screen needing user plus orders plus payment status may need three separate REST calls (**under-fetching**, addressed via chained requests). GraphQL lets each client shape its own query, fetching precisely what it needs in one round trip.

  > See also: [RESTful Architecture](restful.md) — the REST resource model this problem is contrasted against.

<sub>[Back to top](#table-of-contents)</sub>

- ### Schema and Resolver Model
  A GraphQL API is defined by a strongly-typed schema (types, queries, mutations, subscriptions). Each field in the schema is backed by a **resolver** function responsible for fetching that piece of data, often from a database, cache, or another downstream service.

  ```mermaid
  flowchart LR
      Client -->|GraphQL query| Server
      Server --> R1[Resolver: user]
      Server --> R2[Resolver: orders]
      R1 --> DB1[(Users DB)]
      R2 --> DB2[(Orders DB)]
  ```

  **Caption:** A single GraphQL query fans out to multiple resolvers, each responsible for one part of the response.

<sub>[Back to top](#table-of-contents)</sub>

- ### The N+1 Query Pitfall
  A naive resolver implementation can trigger one database query per parent record when resolving a nested list — e.g., fetching 1 user plus a separate query for each of their orders individually. This **N+1 query problem** is a well-known GraphQL performance pitfall, typically mitigated with batching/caching tools such as `DataLoader`. It is worth flagging in any GraphQL design review.

<sub>[Back to top](#table-of-contents)</sub>

---

## Choosing Between REST, gRPC, and GraphQL

| Client Type | Contract Needs | Streaming Needs | Recommendation |
|---|---|---|---|
| Public/third-party clients, browsers, `curl`-based integrations | Loose, human-readable, widely tooled | None or simple polling | **REST** — universal support, cacheable, simple to document with OpenAPI |
| Internal services you fully control | Strict, versioned, compile-time-checked contract | Frequent (telemetry, chat, bidirectional data) | **gRPC** — Protobuf contracts, HTTP/2 performance, native streaming |
| Front ends (web/mobile) aggregating data from many backend resources/services | Flexible, client-driven shape | Rare (subscriptions possible but less central) | **GraphQL** — single endpoint, avoids over/under-fetching, good for BFF layers |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Can gRPC be used for public-facing APIs?**
A: Technically yes (via grpc-web with a proxy), but it's uncommon — browsers don't support raw HTTP/2 trailers gRPC needs, and public consumers benefit more from REST's ubiquity and human-readable JSON. gRPC shines internally where all clients are known and controlled.

---

**Q: Does adopting GraphQL mean giving up REST entirely?**
A: No. Many systems put a GraphQL layer in front of existing REST or gRPC services to aggregate data for front ends, while those underlying services keep their own REST or gRPC contracts.

---

**Q: How does GraphQL handle over-fetching without introducing the N+1 problem as a trade-off?**
A: It doesn't eliminate the risk automatically — solving over-fetching is a client-facing benefit, while N+1 is a server-side resolver implementation concern. Both must be addressed deliberately, typically with batching/caching (e.g., `DataLoader`) in the resolver layer.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [RESTful Architecture](restful.md) — the baseline architectural style gRPC and GraphQL are each compared against.
- [API Versioning and Security](api-versioning-security.md) — versioning and security concerns apply across REST, gRPC, and GraphQL APIs alike.
- [Error Handling and CORS](error-handling-cors.md) — error handling conventions differ between REST status codes and GraphQL's single-status response model.
- [HTTP and Data Formats](http-data-formats.md) — Protocol Buffers and JSON as the respective wire formats behind gRPC and REST/GraphQL.
- [Microservices](../architectural-patterns/microservices.md) — the architectural pattern where gRPC is most commonly adopted for service-to-service calls.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [gRPC Documentation](https://grpc.io/docs/) — official gRPC documentation.
- [Protocol Buffers Documentation](https://protobuf.dev/) — official Protobuf language guide.
- [GraphQL Specification](https://spec.graphql.org/) — official GraphQL language specification.

---

[Get Started](../../get-started.md) | [Web Services and API Design](../../get-started.md#web-services-and-api-design)

---
