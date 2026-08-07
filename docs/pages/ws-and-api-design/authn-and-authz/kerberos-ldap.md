# Kerberos and LDAP

---

## Table of Contents
<!-- TOC -->
* [Kerberos and LDAP](#kerberos-and-ldap)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Kerberos](#kerberos)
  * [LDAP](#ldap)
  * [Key Concepts](#key-concepts)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Kerberos and LDAP are two foundational enterprise identity technologies that predate OAuth and OpenID Connect by decades, and remain the backbone of most corporate Windows/Active Directory networks today. Kerberos answers "how does a user or service prove who they are on this network," while LDAP answers "where do we look up who a user is and what groups they belong to." An architect working on enterprise integrations, especially anything touching Active Directory, needs to recognize both — not as replacements for modern OAuth/OIDC or IAM, but as the plumbing those systems often sit on top of.

---

## Overview

Kerberos was developed at MIT in the 1980s as a network authentication protocol designed to let users and services prove their identity over an untrusted network without repeatedly transmitting passwords. It became the default authentication protocol for Windows domains starting with Windows 2000 and remains the primary mechanism behind Active Directory's "single sign-on to everything on the corporate network" experience.

LDAP (Lightweight Directory Access Protocol) is older still in spirit — a protocol for reading and writing entries in a hierarchical directory service, standardized in the early 1990s as a lightweight alternative to the X.500 directory access protocol. Active Directory is, among other things, an LDAP-compatible directory server, which is why the two technologies are so often deployed and discussed together in enterprise environments even though they solve different problems: Kerberos authenticates, LDAP looks things up.

<sub>[Back to top](#table-of-contents)</sub>

---

## Kerberos

- ### Ticket-Based Authentication:
  Instead of a client presenting a password to every service it wants to access, Kerberos issues time-limited, cryptographically signed **tickets**. A client authenticates once, receives a ticket, and presents that ticket (not the password) to each service it subsequently talks to. This avoids repeatedly transmitting credentials across the network and lets services trust a ticket's claims without contacting the authentication server directly for every request.

<sub>[Back to top](#table-of-contents)</sub>

- ### KDC and Ticket-Granting Ticket:
  The **Key Distribution Center (KDC)** is the trusted third party every client and service relies on. It has two logical parts: the Authentication Server (AS), which verifies the user's initial credentials and issues a **Ticket-Granting Ticket (TGT)**, and the Ticket-Granting Server (TGS), which exchanges that TGT for service-specific tickets on demand. The client never has to re-enter its password to access a new service during its session — it just presents the TGT to the TGS and receives a fresh ticket for whatever it needs next.

  ```mermaid
  sequenceDiagram
      participant Client
      participant AS as Authentication Server (KDC)
      participant TGS as Ticket-Granting Server (KDC)
      participant Service
      Client->>AS: 1. Authenticate (once)
      AS-->>Client: 2. Ticket-Granting Ticket (TGT)
      Client->>TGS: 3. Request service ticket (present TGT)
      TGS-->>Client: 4. Service ticket
      Client->>Service: 5. Present service ticket
      Service-->>Client: 6. Access granted
  ```

  **Caption:** A client authenticates once with the KDC and reuses its Ticket-Granting Ticket to obtain tickets for individual services without re-entering credentials.

<sub>[Back to top](#table-of-contents)</sub>

- ### Kerberos vs. OAuth/OIDC:
  Kerberos and OAuth/OIDC solve overlapping but distinct problems with different assumptions. Kerberos assumes a closed, trusted network (a corporate domain) and a symmetric-key ticket exchange with a central KDC — it's the answer to "authenticate this Windows workstation user to this internal file share or database" and predates the web entirely. [OAuth](oauth.md) and [OpenID Connect](openid-connect.md), by contrast, were designed for the open web: delegated authorization and federated identity across organizational boundaries, using bearer tokens over HTTPS rather than tickets over a trusted internal network. In practice, many enterprises run both side by side — Kerberos/Active Directory for internal desktop and file-share authentication, OAuth/OIDC for web and API-facing applications, sometimes bridged together so a single AD login federates out to modern SSO.

  > See also: [OAuth](oauth.md), [OpenID Connect (OIDC)](openid-connect.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## LDAP

- ### Directory Service Model:
  LDAP defines both a protocol and a data model for a **directory** — a hierarchical, read-heavy store of entries (users, groups, computers, organizational units) organized in a tree structure (the Directory Information Tree). Each entry is identified by a Distinguished Name (DN), such as `cn=jsmith,ou=Engineering,dc=example,dc=com`, and holds attributes like email, group memberships, and phone number. Applications query LDAP to answer questions like "does this user exist," "what groups is this user in," or "what's this user's email address."

  ```
  dc=example,dc=com
  └── ou=Engineering
      └── cn=jsmith
          ├── mail: jsmith@example.com
          └── memberOf: cn=Developers,ou=Groups,dc=example,dc=com
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### LDAP and IAM:
  It's easy to conflate LDAP with Identity and Access Management as a discipline, but they operate at different layers. LDAP is a **directory** — a data store and query protocol for identity and group information. [IAM](../../cyber-security/access-control-and-authn/iam.md) is the broader discipline of provisioning, authenticating, authorizing, and governing access across an organization's systems, of which "where is identity data stored" is only one piece. In most enterprise architectures, an LDAP directory (often Active Directory) is the backing store that IAM systems, single sign-on providers, and applications query against for authentication and group lookups — LDAP is the "phone book," not the policy engine deciding who gets access to what.

  > See also: [Identity and Access Management (IAM)](../../cyber-security/access-control-and-authn/iam.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| KDC | Key Distribution Center — the trusted Kerberos service that issues tickets |
| TGT | Ticket-Granting Ticket — proof of initial authentication, exchanged for service tickets |
| DN | Distinguished Name — the unique hierarchical identifier of an LDAP directory entry |
| Bind | The LDAP operation of authenticating to the directory before performing queries |

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If OAuth/OIDC are more modern, why would an enterprise still rely on Kerberos at all?**
A: Kerberos is deeply embedded in how Windows/Active Directory domains authenticate desktops, file shares, and internal services — it provides fast, transparent single sign-on within a trusted corporate network without needing a browser or HTTPS. Replacing it entirely would mean re-architecting core internal infrastructure; instead, most enterprises keep Kerberos for internal/desktop auth and use OAuth/OIDC at the web and API edge.

---

**Q: Is LDAP itself insecure to use in a modern architecture?**
A: Plain LDAP transmits data (including binds/credentials in some configurations) unencrypted, so production deployments should use LDAPS (LDAP over TLS) or STARTTLS. The protocol's age is not itself a security flaw, but it must be configured with encryption in transit like any other authentication-adjacent traffic.

---

**Q: Our IAM system needs to know a user's group memberships — does that mean LDAP and IAM are the same thing?**
A: No — LDAP is the directory data store IAM commonly queries for that information, but IAM also covers provisioning/deprovisioning workflows, policy decisions, audit logging, and integration with modern protocols like SAML/OAuth. LDAP answers "what does the directory say," IAM decides "what should happen as a result."

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [OAuth](oauth.md) — the modern, web-native alternative Kerberos is often contrasted against for authentication.
- [OpenID Connect (OIDC)](openid-connect.md) — federated identity for the open web, versus Kerberos's trusted-network model.
- [Identity and Access Management (IAM)](../../cyber-security/access-control-and-authn/iam.md) — the broader discipline LDAP directories typically feed into.
- [SAML](saml.md) — another enterprise-era protocol commonly deployed alongside Active Directory/LDAP environments.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [MIT Kerberos: The Network Authentication Protocol](https://web.mit.edu/kerberos/) — origin project and technical documentation.
- [RFC 4511: Lightweight Directory Access Protocol (LDAP): The Protocol](https://www.rfc-editor.org/rfc/rfc4511) — official LDAP protocol specification.

---

[Get Started](../../../get-started.md) | [Web Services and API Design](../../../get-started.md#web-services-and-api-design)

---
