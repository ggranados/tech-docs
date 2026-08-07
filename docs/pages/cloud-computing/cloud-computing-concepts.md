# Cloud Computing Concepts

---

## Table of Contents
<!-- TOC -->
* [Cloud Computing Concepts](#cloud-computing-concepts)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [IaaS, PaaS, and SaaS](#iaas-paas-and-saas)
  * [Public, Private, Hybrid, and Multi-Cloud](#public-private-hybrid-and-multi-cloud)
  * [Shared Responsibility Model](#shared-responsibility-model)
  * [Key Concepts](#key-concepts)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Cloud computing is the on-demand delivery of compute, storage, networking, and higher-level application services over the internet, billed on a pay-as-you-go basis instead of purchased and depreciated as owned hardware. For a software architect, cloud computing reframes infrastructure as a design variable: capacity, geography, and even entire categories of undifferentiated heavy lifting (patching, hardware failure, data-center power) can be delegated to a provider. The concepts on this page — service models, deployment models, and the shared responsibility model — are vendor-neutral and apply equally to AWS, Azure, GCP, or any other provider.

---

## Overview

Before cloud computing, running software meant owning or leasing physical servers, provisioning them for peak load, and operating a data center (or paying a colocation provider to help). Cloud computing decouples "using compute" from "owning compute": a provider operates massive shared infrastructure and rents out slices of it — virtual machines, storage buckets, managed databases, entire application platforms — through APIs and self-service consoles.

The value proposition rests on a few pillars: elasticity (scale resources up or down with demand instead of provisioning for a worst case), a shift from capital expenditure to operating expenditure, global reach (deploy close to users without building a data center), and outsourcing of physical and operational concerns like hardware failure, power, and cooling. None of this is free of trade-offs — cost predictability, vendor lock-in, and data residency all become architectural concerns in their own right.

The rest of this page covers three ideas every architect needs before evaluating any specific provider: what layer of the stack a service model hands you versus manages for you, what deployment model fits an organization's constraints, and — most importantly — where the provider's security obligations end and the customer's begin.

<sub>[Back to top](#table-of-contents)</sub>

---

## IaaS, PaaS, and SaaS

Cloud service models describe how much of the stack the provider manages versus how much the customer must operate. They form a spectrum, not three isolated boxes — as you move from IaaS to SaaS, the provider takes on more operational responsibility and the customer gets less control.

- ### Infrastructure as a Service (IaaS):
  The provider supplies raw compute, storage, and networking — virtual machines, block storage, virtual networks — and the customer manages everything above that: the operating system, runtime, middleware, and application code. IaaS is the closest cloud equivalent to renting hardware; it offers maximum control at the cost of maximum operational responsibility. Typical examples: a virtual machine instance, a block storage volume, or a virtual private network — the building blocks an architect would otherwise rack and cable.

<sub>[Back to top](#table-of-contents)</sub>

- ### Platform as a Service (PaaS):
  The provider manages the operating system, runtime, and middleware, leaving the customer responsible only for the application code and its configuration/data. PaaS trades some control for a much shorter path from code to running service — no OS patching, no runtime installation. Typical examples: a managed application-hosting platform that takes a build artifact and runs it, or a managed database engine where the customer manages schemas and queries but not the underlying server.

<sub>[Back to top](#table-of-contents)</sub>

- ### Software as a Service (SaaS):
  The provider operates the entire application stack, and the customer simply uses the software, typically through a browser or API, configuring it rather than deploying to it. The customer's remaining responsibility is largely about *how they use* the software: account access, data entered into it, and integration configuration. Typical examples: a hosted email or office-productivity suite, or a CRM accessed entirely through a web UI.

  ```mermaid
  graph TD
      subgraph IaaS["IaaS — Customer manages most"]
          A1[Applications - Customer]
          A2[Data - Customer]
          A3[Runtime & Middleware - Customer]
          A4[Operating System - Customer]
          A5[Virtualization, Servers, Storage, Networking - Provider]
      end
      subgraph PaaS["PaaS — Shared"]
          B1[Applications - Customer]
          B2[Data - Customer]
          B3[Runtime & Middleware - Provider]
          B4[Operating System - Provider]
          B5[Virtualization, Servers, Storage, Networking - Provider]
      end
      subgraph SaaS["SaaS — Provider manages most"]
          C1[Applications - Provider]
          C2[Data - Provider, Customer configures]
          C3[Runtime & Middleware - Provider]
          C4[Operating System - Provider]
          C5[Virtualization, Servers, Storage, Networking - Provider]
      end
  ```

  **Caption:** As service models move from IaaS to SaaS, each layer of the stack flips from customer-managed to provider-managed, one row at a time.

<sub>[Back to top](#table-of-contents)</sub>

---

## Public, Private, Hybrid, and Multi-Cloud

Deployment models describe *where* cloud infrastructure lives and *who* it's shared with — an orthogonal choice to service models. An organization might run IaaS workloads in a public cloud while operating a PaaS-like internal platform on private infrastructure.

- ### Public Cloud:
  Infrastructure is owned and operated by a third-party provider and shared across many customers (multi-tenant), with logical isolation between tenants. This is the default model most people mean by "the cloud" — low upfront cost, near-unlimited elastic capacity, and no physical infrastructure to manage, in exchange for less control over the underlying hardware and physical location.

<sub>[Back to top](#table-of-contents)</sub>

- ### Private Cloud:
  Infrastructure is dedicated to a single organization, either hosted on-premises or by a third party on single-tenant hardware. Private cloud trades away some of the public cloud's cost efficiency and elasticity for tighter control over security, compliance, and performance — common in heavily regulated industries or where legacy systems can't move to shared infrastructure.

<sub>[Back to top](#table-of-contents)</sub>

- ### Hybrid Cloud:
  A combination of private and public (or on-premises and cloud) infrastructure, connected so workloads and data can move between them. Hybrid is often a pragmatic middle ground: sensitive data or legacy systems stay on private infrastructure while burst capacity, new development, or specific services run in the public cloud.

<sub>[Back to top](#table-of-contents)</sub>

- ### Multi-Cloud:
  Using more than one public cloud provider, either deliberately (best-of-breed services, negotiating leverage, regulatory requirements) or as a byproduct of mergers and acquisitions. Multi-cloud reduces reliance on any single vendor but increases operational complexity — teams need expertise across providers, and portability between them is never as seamless as marketing suggests.

<sub>[Back to top](#table-of-contents)</sub>

---

## Shared Responsibility Model

The shared responsibility model is arguably the single most architecturally important concept in cloud computing, because it defines a security and operational boundary that, if misunderstood, leads directly to breaches, outages, and compliance failures. Every major provider publishes some version of this model, and while the specific wording differs, the underlying principle is identical: **the provider is responsible for the security *of* the cloud, and the customer is responsible for security *in* the cloud.**

Concretely, the provider is always responsible for the physical layer — data center security, hardware lifecycle, the network backbone, and the virtualization layer that isolates tenants from each other. The customer is always responsible for what they put on top: how they configure identity and access, whether storage buckets are accidentally left public, whether data is encrypted, whether application code has vulnerabilities, and whether operating systems (where the customer manages the OS) are patched.

Where the line sits *moves* depending on the service model, which is why understanding IaaS/PaaS/SaaS matters for security, not just for operations:

| Layer | IaaS | PaaS | SaaS |
|---|---|---|---|
| Data & access configuration | Customer | Customer | Customer |
| Application code | Customer | Customer | Provider |
| Runtime, middleware, OS | Customer | Provider | Provider |
| Virtualization, servers, storage, network | Provider | Provider | Provider |
| Physical data center | Provider | Provider | Provider |

The practical failure mode this model exists to prevent is assuming the provider "handles security" as a blanket statement. Providers secure the platform; they do not secure a customer's misconfigured storage bucket, an overly permissive IAM policy, an unencrypted database, or unpatched application dependencies. The large majority of publicly reported cloud security incidents trace back to the *customer* side of this line — not a provider breach. An architect's job is to know exactly which row of that table their team owns for every service in use, and to design controls (least-privilege IAM, encryption at rest and in transit, network segmentation, dependency scanning) accordingly.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Elasticity | The ability to automatically scale resources up or down to match demand, paying only for what's used. |
| Multi-tenancy | Multiple customers share the same underlying physical infrastructure, logically isolated from one another. |
| Vendor lock-in | Difficulty or cost of migrating away from a provider due to reliance on its proprietary services or APIs. |
| Undifferentiated heavy lifting | Operational work (power, cooling, hardware failure, patching) that doesn't differentiate a product but must still be done — a common candidate for offloading to a provider. |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If we move to PaaS instead of IaaS, does that mean we can worry less about security?**
A: No — it means a different set of things fall out of scope, not that security stops mattering. The provider takes over OS and runtime patching, but the customer is still fully responsible for application code, data, identity and access configuration, and how the service is used. Moving up the stack narrows the surface a team must operate, but it never eliminates the customer half of the shared responsibility line.

---

**Q: Our compliance team asked whether a breach caused by a misconfigured storage bucket is "the cloud provider's fault." How do I answer that?**
A: Under the shared responsibility model, storage configuration — including access permissions — sits on the customer side of the line in every service model. The provider is responsible for the physical and virtualization layers being secure; the customer is responsible for how they configure and use the service on top of that. A misconfigured bucket is a customer-side failure regardless of which provider is involved.

---

**Q: Is multi-cloud a security or resilience best practice we should default to?**
A: Not automatically. Multi-cloud can reduce vendor lock-in and provide negotiating leverage, but it multiplies operational complexity — different IAM models, different networking primitives, different failure modes to understand and monitor. It's a deliberate trade-off best justified by a specific requirement (regulatory, redundancy, or commercial), not adopted by default for its own sake.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [AWS, Azure, and GCP Compared](aws-azure-gcp.md) — how the big three public cloud providers implement these same service and deployment models
- [Other Cloud Providers](other-cloud-providers.md) — additional public and private cloud providers beyond the big three
- [Identity and Access Management (IAM)](../cyber-security/access-control-and-authn/iam.md) — identity and access management is a core customer-side responsibility under the shared responsibility model

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [NIST SP 800-145 — The NIST Definition of Cloud Computing](https://nvlpubs.nist.gov/nistpubs/Legacy/SP/nistspecialpublication800-145.pdf) — the foundational, vendor-neutral definition of cloud computing and its service/deployment models
- [Cloud Security Alliance — Shared Responsibility Model](https://cloudsecurityalliance.org/) — vendor-neutral guidance on shared responsibility across service models

---

[Get Started](../../get-started.md) | [Cloud Computing Platform](../../get-started.md#cloud-computing-platform)

---
