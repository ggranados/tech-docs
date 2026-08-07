# Other Cloud Providers

---

## Table of Contents
<!-- TOC -->
* [Other Cloud Providers](#other-cloud-providers)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [IBM Cloud](#ibm-cloud)
  * [Alibaba Cloud](#alibaba-cloud)
  * [Oracle Cloud Infrastructure (OCI)](#oracle-cloud-infrastructure-oci)
  * [Salesforce Cloud](#salesforce-cloud)
  * [DigitalOcean](#digitalocean)
  * [VMware Cloud](#vmware-cloud)
  * [Rackspace](#rackspace)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

AWS, Azure, and GCP capture most public cloud market share, but they are far from the only providers an architect will encounter. Legacy enterprise relationships, regional regulation, specific workload niches, and SaaS platforms with their own cloud infrastructure all create situations where one of these other providers is the right — or the only available — choice. This page is not an attempt to give these providers AWS-scale coverage; it's a quick reference for recognizing each one and knowing roughly when it might come up.

---

## Overview

Beyond the big three, the cloud landscape includes providers that succeed by specializing rather than by competing on breadth. Some carry decades of enterprise relationships forward from mainframe and legacy licensing eras (IBM, Oracle). Some are regional leaders shaped by local regulation and market dynamics (Alibaba Cloud in China). Some are cloud infrastructure underneath a specific SaaS product an architect may already be using without thinking of it as "a cloud" (Salesforce). Others compete on simplicity and price for smaller workloads (DigitalOcean), or on bridging existing on-premises virtualization investments into the cloud (VMware Cloud, Rackspace).

Knowing these providers exist — and roughly why an organization would end up on one — is often more useful for an architect than deep expertise in all of them, since the decision to use one is frequently made for reasons outside pure technical merit (an existing licensing relationship, a regulatory requirement, or an acquisition that came with its infrastructure attached).

<sub>[Back to top](#table-of-contents)</sub>

---

## IBM Cloud

IBM Cloud is IBM's public cloud platform, built on a foundation of decades-long enterprise relationships from IBM's mainframe, middleware, and consulting business. Its differentiator is deep integration with IBM's own enterprise software stack (WebSphere, Db2, and IBM's watsonx AI/ML tooling) and strong positioning around hybrid cloud via Red Hat OpenShift, which IBM acquired in 2019. It tends to come up when an organization already runs IBM enterprise software or wants a hybrid-cloud strategy built on Red Hat's open-source stack rather than a single hyperscaler.

<sub>[Back to top](#table-of-contents)</sub>

---

## Alibaba Cloud

Alibaba Cloud is the cloud computing arm of the Alibaba Group and the dominant public cloud provider in China, with a growing footprint across Asia-Pacific. Its core service catalog mirrors AWS/Azure/GCP fairly closely (compute, storage, managed databases), but its real differentiator is regulatory and operational fit for doing business in mainland China, where data residency and regulatory requirements make the Western hyperscalers a poor or impossible fit. It typically comes up for organizations expanding into the Chinese market.

<sub>[Back to top](#table-of-contents)</sub>

---

## Oracle Cloud Infrastructure (OCI)

Oracle Cloud Infrastructure is Oracle's public cloud, most relevant to organizations already running Oracle Database, Oracle E-Business Suite, or other Oracle enterprise applications. OCI's differentiator is aggressive pricing and licensing terms for existing Oracle customers, along with performance-tuned infrastructure specifically for Oracle Database workloads (including "Exadata" cloud offerings). It tends to come up as a migration target for organizations moving Oracle-dependent workloads off self-managed hardware without leaving the Oracle ecosystem.

<sub>[Back to top](#table-of-contents)</sub>

---

## Salesforce Cloud

Salesforce Cloud (often just referred to via its products — Sales Cloud, Service Cloud, Platform/Force.com) is primarily a SaaS CRM platform, but it exposes a genuine application platform (Force.com/Salesforce Platform, using languages like Apex and tools like Lightning) for building custom applications on top of Salesforce's infrastructure. Its differentiator is that it's rarely chosen as general-purpose cloud infrastructure — it comes up specifically when an organization is extending or customizing its existing Salesforce CRM investment rather than building a new system from scratch.

<sub>[Back to top](#table-of-contents)</sub>

---

## DigitalOcean

DigitalOcean is a cloud provider focused on simplicity, predictable flat pricing, and a small, curated set of core products (VMs called "Droplets," managed Kubernetes, object storage, managed databases) rather than the sprawling catalogs of the hyperscalers. Its differentiator is a much lower learning curve and cost for smaller workloads — it's a common choice for startups, side projects, and small-to-mid-size applications that don't need the breadth (or complexity) of AWS/Azure/GCP.

<sub>[Back to top](#table-of-contents)</sub>

---

## VMware Cloud

VMware Cloud extends VMware's on-premises virtualization stack (vSphere, NSX, vSAN) into the public cloud, including offerings that run on top of AWS, Azure, and GCP infrastructure (e.g., VMware Cloud on AWS). Its differentiator is letting organizations with a large existing VMware footprint migrate or extend workloads into the cloud using the same virtualization tools and operational model they already run on-premises, rather than re-architecting for a hyperscaler-native approach. It typically comes up in hybrid-cloud and data-center-exit strategies for VMware-heavy enterprises.

<sub>[Back to top](#table-of-contents)</sub>

---

## Rackspace

Rackspace originated as a managed hosting and colocation provider and has since repositioned as a managed multi-cloud services company — it doesn't primarily compete as an infrastructure owner anymore, but as an operator that manages workloads running on AWS, Azure, GCP, or its own legacy infrastructure on a customer's behalf. Its differentiator is managed operations and support (sometimes called "fanatical support") rather than owning unique infrastructure. It tends to come up when an organization wants hyperscaler infrastructure but lacks the in-house team to operate it directly.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If AWS, Azure, and GCP cover most core cloud needs, why would an organization ever choose one of these other providers?**
A: Usually for a reason outside raw feature comparison — an existing licensing or software relationship (Oracle, IBM, Salesforce), a regulatory or regional requirement the hyperscalers can't satisfy (Alibaba Cloud in China), a preference for simplicity and cost over breadth (DigitalOcean), or a desire to extend existing on-premises tooling rather than replace it (VMware Cloud). None of these are about the big three lacking a capability.

---

**Q: Is "Salesforce Cloud" really comparable to AWS or Azure as a cloud provider?**
A: Not directly — Salesforce is fundamentally a SaaS platform with an application-development layer (the Salesforce Platform/Force.com) bolted on, whereas AWS/Azure/GCP are infrastructure-first platforms that also offer SaaS-like managed services. It's included here because it's a cloud platform an architect will encounter, but it sits much further toward the SaaS end of the IaaS/PaaS/SaaS spectrum than the others on this page.

---

**Q: What's the practical difference between VMware Cloud and Rackspace, since both seem tied to "other people's infrastructure"?**
A: VMware Cloud is fundamentally about a *virtualization technology stack* (vSphere/NSX/vSAN) that can run on-premises or be extended into the cloud — the value is operational consistency with existing VMware tooling. Rackspace is fundamentally a *managed services company* — its value is operating infrastructure (which may itself be AWS, Azure, GCP, or legacy Rackspace hardware) on a customer's behalf. One is a technology choice; the other is an outsourcing choice, and they're often used together.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Cloud Computing Concepts](cloud-computing-concepts.md) — service and deployment models that apply to these providers as much as to the big three
- [AWS, Azure, and GCP Compared](aws-azure-gcp.md) — the three dominant public cloud providers these providers compete against or complement
- [Infrastructure as Code (IaC)](../devops/infrastructure-as-code.md) — provider-agnostic tooling often used to manage resources across a multi-cloud or hybrid footprint

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [IBM Cloud Documentation](https://cloud.ibm.com/docs) — official IBM Cloud documentation
- [Alibaba Cloud Documentation](https://www.alibabacloud.com/help) — official Alibaba Cloud documentation
- [Oracle Cloud Infrastructure Documentation](https://docs.oracle.com/en-us/iaas/Content/home.htm) — official OCI documentation
- [Salesforce Platform Documentation](https://developer.salesforce.com/docs) — official Salesforce developer documentation
- [DigitalOcean Documentation](https://docs.digitalocean.com/) — official DigitalOcean documentation
- [VMware Cloud Documentation](https://docs.vmware.com/en/VMware-Cloud/index.html) — official VMware Cloud documentation
- [Rackspace Technology](https://www.rackspace.com/) — official Rackspace site and service overview

---

[Get Started](../../get-started.md) | [Cloud Computing Platform](../../get-started.md#cloud-computing-platform)

---
