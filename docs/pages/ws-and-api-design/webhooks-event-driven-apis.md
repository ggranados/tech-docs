# Webhooks and Event-Driven APIs

---

## Table of Contents
<!-- TOC -->
* [Webhooks and Event-Driven APIs](#webhooks-and-event-driven-apis)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Webhooks](#webhooks)
  * [Event-Driven APIs in Context](#event-driven-apis-in-context)
  * [Key Concepts](#key-concepts)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Webhooks flip the usual client-server request model on its head: instead of a client repeatedly asking "has anything changed yet?", the client registers a URL up front and the server calls that URL the moment something happens. They are the simplest, most widely adopted form of event-driven communication between systems — no message broker required, just plain HTTP — which is why they show up everywhere from payment processors to CI/CD platforms to SaaS integrations.

---

## Overview

Traditional API consumption is pull-based: a client polls an endpoint on a schedule to check for new data, wasting requests when nothing has changed and introducing latency between an event occurring and the client noticing it. Webhooks invert this to a push model — the provider holds a callback URL registered by the consumer and issues an HTTP POST to it as soon as a relevant event occurs, typically carrying a JSON payload describing what happened.

This makes webhooks attractive for integrations where near-real-time notification matters (a payment succeeded, a pull request was merged, a shipment status changed) but where standing up a full message broker would be overkill. The tradeoff is that webhooks run over plain HTTP between two independently-operated systems, so delivery is inherently less reliable than an in-house message queue, and the receiving side must be built defensively.

<sub>[Back to top](#table-of-contents)</sub>

---

## Webhooks

- ### Registration and Delivery:
  A consumer registers a callback URL with the provider (often via API or dashboard configuration). When a relevant event fires, the provider issues an HTTP POST to that URL with an event payload. The consumer's endpoint must respond quickly with a 2xx status to acknowledge receipt — providers typically expect a response within a few seconds and treat anything else as a failed delivery.

  ```mermaid
  sequenceDiagram
      participant Consumer
      participant Provider
      Consumer->>Provider: Register callback URL
      Note over Provider: Event occurs (e.g., payment.succeeded)
      Provider->>Consumer: POST /webhook-endpoint (event payload)
      Consumer-->>Provider: 200 OK
  ```

  **Caption:** The consumer registers a URL once; the provider pushes events to it as they happen, instead of the consumer polling.

<sub>[Back to top](#table-of-contents)</sub>

- ### Signature Verification:
  Because a webhook endpoint is a public HTTP URL, anyone who discovers it could send forged payloads. Providers mitigate this by signing each payload with a shared secret (e.g., an HMAC-SHA256 signature sent in a header) so the consumer can verify the payload genuinely came from the provider and wasn't tampered with in transit, before trusting or acting on it.

  ```
  X-Signature: sha256=5d41402abc4b2a76b9719d911017c592
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Retry and Idempotency:
  Webhook delivery is best-effort, not guaranteed exactly-once — networks fail, receiving servers time out or crash mid-processing. Providers typically retry failed deliveries with backoff, which means a consumer can receive the same event more than once. A robust webhook receiver must be idempotent: it tracks event IDs it has already processed and safely ignores duplicates, rather than, say, charging a customer twice because the same `payment.succeeded` event arrived two times.

<sub>[Back to top](#table-of-contents)</sub>

---

## Event-Driven APIs in Context

Webhooks are best understood as the lightweight, HTTP-native end of a broader event-driven communication spectrum. At the other end sit dedicated event brokers like Kafka or RabbitMQ, covered in [Event-Driven Architecture](../architectural-patterns/message-driven/event-driven.md), which add durable storage, replay, multiple consumer groups, and guaranteed ordering — capabilities a simple webhook POST doesn't provide.

- ### Webhooks vs. Event Brokers:
  A webhook is a single, one-shot HTTP call from provider to consumer with no built-in persistence: if the consumer is down and every retry is exhausted, the event is effectively lost unless the provider offers a manual replay/audit log. An event broker, by contrast, durably stores events on a topic/queue so multiple consumers can read them independently, at their own pace, with replay after an outage. Webhooks are the right tool for simple point-to-point notifications between two organizations over the open internet; an event broker is the right tool for high-throughput, multi-consumer event streaming within (or across) a system you control end-to-end.

  > See also: [Event-Driven Architecture](../architectural-patterns/message-driven/event-driven.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Callback URL | The consumer-provided endpoint the provider POSTs event payloads to |
| Signature Verification | Validating an HMAC signature header to confirm a webhook payload's authenticity |
| At-Least-Once Delivery | The delivery guarantee most webhook providers offer — duplicates are possible, loss should not be |
| Idempotency Key | An event ID used by the consumer to detect and safely discard duplicate deliveries |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If webhook delivery isn't guaranteed exactly-once, how should a consumer handle that in practice?**
A: Treat every webhook handler as idempotent — persist the event ID (most providers include one) before processing, and check it on every incoming request so a duplicate delivery is a no-op rather than a repeated side effect like a double charge or duplicate order.

---

**Q: How do you secure a public webhook endpoint that has to be reachable from the open internet?**
A: Verify the provider's signature header on every request before trusting the payload, use HTTPS only, and treat the endpoint as untrusted input — validate and sanitize the payload just as you would any external API call, since the URL being "secret" is not a real security control.

---

**Q: When would you reach for a full event broker like Kafka instead of just using webhooks?**
A: When you need durable replay, multiple independent consumers reading the same event stream, strict ordering guarantees, or very high throughput within a system you control — webhooks are point-to-point and best-effort, which doesn't scale to those requirements.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Event-Driven Architecture](../architectural-patterns/message-driven/event-driven.md) — the heavier, broker-based approach to event-driven communication that webhooks are a lightweight instance of.
- [SOAP and RPC](soap-rpc.md) — contrasts webhooks' push model with traditional synchronous request/response styles.
- [API Lifecycle Management](api-lifecycle-management.md) — documenting and governing webhook contracts alongside the rest of an API surface.
- [Authentication and Authorization](authn-and-authz/authn-authz.md) — background on the signing/verification concepts webhook security builds on.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Stripe: Webhooks Overview](https://docs.stripe.com/webhooks) — practical, widely-referenced implementation guide covering signatures, retries, and idempotency.
- [MDN: Webhooks](https://developer.mozilla.org/en-US/docs/Glossary/Webhook) — general reference definition and use cases.

---

[Get Started](../../get-started.md) | [Web Services and API Design](../../get-started.md#web-services-and-api-design)

---
