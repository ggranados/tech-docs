# AWS, Azure, and GCP Compared

---

## Table of Contents
<!-- TOC -->
* [AWS, Azure, and GCP Compared](#aws-azure-and-gcp-compared)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Compute](#compute)
  * [Storage](#storage)
  * [Networking](#networking)
  * [Managed Databases](#managed-databases)
  * [Choosing Between Them](#choosing-between-them)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Amazon Web Services (AWS), Microsoft Azure, and Google Cloud Platform (GCP) are the three largest public cloud providers, and together dominate enough market share that most architects will work with at least one of them directly. Their core building blocks — virtual machines, object storage, virtual networks, managed databases — are conceptually interchangeable, even though the product names, APIs, and consoles differ. This page maps the equivalent core services side by side so an architect fluent in one provider can quickly orient in another, without going deep into any single one's configuration.

---

## Overview

Despite heavy marketing differentiation, the three providers converge on the same underlying primitives because they're solving the same problems: elastic compute, durable storage, low-latency networking, and managed data stores, all metered and billed by usage. The differences that matter architecturally are rarely about a service being "missing" a capability — it's more often about defaults, integration depth with the rest of that provider's ecosystem, pricing structure, and regional availability.

AWS was first to market (2006) and has the broadest service catalog and market share. Azure has the deepest integration with Microsoft's enterprise ecosystem (Active Directory, Windows Server, Microsoft 365) and is often the default choice for organizations already standardized on Microsoft. GCP leans on Google's strengths in data analytics, machine learning, and Kubernetes (which Google originated internally as Borg before open-sourcing the pattern). None of this should be read as one provider being objectively "better" — it's context that should inform, not decide, an architecture choice.

The tables below name the closest equivalent service per provider in each core category. Naming an equivalent doesn't imply feature parity in every dimension — it's a starting point for translating architecture between providers, not a substitute for reading each provider's own documentation before committing to a design.

<sub>[Back to top](#table-of-contents)</sub>

---

## Compute

| Category | AWS | Azure | GCP |
|---|---|---|---|
| Virtual machines | EC2 (Elastic Compute Cloud) | Virtual Machines | Compute Engine |
| Container orchestration (managed Kubernetes) | EKS (Elastic Kubernetes Service) | AKS (Azure Kubernetes Service) | GKE (Google Kubernetes Engine) |
| Serverless functions | Lambda | Azure Functions | Cloud Functions |
| Managed container hosting (no cluster ops) | Fargate / App Runner | Container Apps | Cloud Run |
| Platform as a Service (app hosting) | Elastic Beanstalk | App Service | App Engine |

<sub>[Back to top](#table-of-contents)</sub>

---

## Storage

| Category | AWS | Azure | GCP |
|---|---|---|---|
| Object storage | S3 (Simple Storage Service) | Blob Storage | Cloud Storage |
| Block storage (attached to a VM) | EBS (Elastic Block Store) | Managed Disks | Persistent Disk |
| File storage (network file system) | EFS (Elastic File System) | Azure Files | Filestore |
| Archival / cold storage | S3 Glacier | Archive Storage tier | Coldline / Archive Storage class |

<sub>[Back to top](#table-of-contents)</sub>

---

## Networking

| Category | AWS | Azure | GCP |
|---|---|---|---|
| Virtual private network | VPC (Virtual Private Cloud) | Virtual Network (VNet) | VPC (Virtual Private Cloud) |
| Load balancing | Elastic Load Balancing (ALB/NLB) | Load Balancer / Application Gateway | Cloud Load Balancing |
| Content delivery network (CDN) | CloudFront | Azure CDN / Front Door | Cloud CDN |
| DNS | Route 53 | Azure DNS | Cloud DNS |

<sub>[Back to top](#table-of-contents)</sub>

---

## Managed Databases

| Category | AWS | Azure | GCP |
|---|---|---|---|
| Managed relational database | RDS (Relational Database Service) | Azure SQL Database / Azure Database for PostgreSQL/MySQL | Cloud SQL |
| Globally distributed relational database | Aurora (with Global Database) | Azure Cosmos DB (multi-model) | Cloud Spanner |
| Managed NoSQL (key-value / document) | DynamoDB | Cosmos DB | Firestore / Bigtable |
| Managed in-memory cache | ElastiCache | Azure Cache for Redis | Memorystore |
| Managed data warehouse | Redshift | Synapse Analytics | BigQuery |

<sub>[Back to top](#table-of-contents)</sub>

---

## Choosing Between Them

Choosing a cloud provider is rarely a pure technical decision, and treating it as one is a common mistake for architects new to this kind of evaluation. Feature comparisons like the tables above matter, but in practice they're often outweighed by factors outside the technology itself:

- **Existing organizational expertise** — a team fluent in Azure and Active Directory will move faster and make fewer mistakes on Azure than on a technically superior feature on another platform they don't know.
- **Negotiated pricing and existing commitments** — enterprise agreements, committed-use discounts, and existing licensing deals (e.g., Microsoft Enterprise Agreements bundling Azure credits) can make the "worse fit" provider the cheaper one.
- **Compliance and data residency requirements** — a specific region, certification (FedRAMP, HIPAA, data sovereignty law), or audit requirement can eliminate a provider from consideration regardless of feature set.
- **Ecosystem and integration** — an organization already deep in Google Workspace or Microsoft 365 gains real integration value from GCP or Azure respectively that a feature comparison won't capture.
- **Existing multi-cloud or M&A history** — some organizations end up on multiple providers not by strategy but by acquisition, and "choosing" is really about which existing footprint to consolidate around.

An architect's job is to surface these non-technical factors explicitly rather than let a feature checklist stand in for the real decision.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If GCP's Cloud Spanner and AWS's Aurora are both "globally distributed relational databases," are they interchangeable?**
A: Not directly — they solve similar problems with different consistency models and underlying architecture. Spanner offers strong global consistency backed by Google's TrueTime infrastructure; Aurora Global Database replicates a primary region to secondary regions with typically sub-second but eventually-consistent replication lag. The comparison table is a starting point for translating vocabulary between clouds, not a guarantee of matching semantics — always read the specific provider's consistency guarantees before designing around them.

---

**Q: My team knows AWS well. Is it ever worth choosing a different provider for a specific project?**
A: Sometimes — if that project has a hard requirement the team's default provider can't meet well (e.g., a customer mandates Azure for compliance reasons, or a data-heavy workload benefits significantly from GCP's BigQuery). But switching providers has a real cost in ramp-up time and operational risk, so the bar should be a concrete requirement, not a marginal feature advantage.

---

**Q: Why do AWS, Azure, and GCP all seem to converge on nearly the same set of services?**
A: They're competing for the same customers solving the same categories of problems — compute, storage, networking, and data — so the market pressures each to cover the same baseline. Genuine differentiation shows up less in "does the category exist" and more in depth of specific services (e.g., GCP's data/ML tooling, Azure's enterprise identity integration), pricing models, and how tightly services integrate with each other within one provider's ecosystem.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Cloud Computing Concepts](cloud-computing-concepts.md) — the service and deployment models (IaaS/PaaS/SaaS, shared responsibility) that apply across all three providers
- [Other Cloud Providers](other-cloud-providers.md) — providers beyond the big three, and when they come up
- [NoSQL Databases](../data-processing/nosql/nosql.md) — background on the NoSQL database categories referenced in the Managed Databases table

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [AWS Documentation](https://docs.aws.amazon.com/) — official AWS service documentation
- [Microsoft Azure Documentation](https://learn.microsoft.com/en-us/azure/) — official Azure service documentation
- [Google Cloud Documentation](https://cloud.google.com/docs) — official GCP service documentation

---

[Get Started](../../get-started.md) | [Cloud Computing Platform](../../get-started.md#cloud-computing-platform)

---
