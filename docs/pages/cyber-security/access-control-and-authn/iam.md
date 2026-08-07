# Identity and Access Management (IAM)

---

## Table of Contents

<!-- TOC -->
* [Identity and Access Management (IAM)](#identity-and-access-management-iam)
  * [Table of Contents](#table-of-contents)
  * [Key components of IAM](#key-components-of-iam)
  * [Commercial IAM Solutions](#commercial-iam-solutions)
    * [Okta](#okta)
    * [Auth0](#auth0)
    * [Ping Identity](#ping-identity)
    * [Microsoft Azure Active Directory (Azure AD)](#microsoft-azure-active-directory-azure-ad)
    * [Salesforce Identity](#salesforce-identity)
    * [OneLogin](#onelogin)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

IAM stands for Identity and Access Management. It is a framework of policies, processes, and technologies that facilitate the management of digital identities and control access to resources within an organization's IT environment. *The primary goal of IAM is to ensure that the right individuals have the appropriate level of access to the right resources at the right time*.

IAM plays a critical role in ensuring data security, protecting sensitive information, and mitigating the risk of unauthorized access or data breaches. It is an essential part of modern IT infrastructure, especially in large organizations and enterprises where managing access control for a large number of users and resources can be complex.

<sub>[Back to top](#table-of-contents)</sub>

## Key components of IAM

- **Identification**: The process of uniquely identifying users, devices, or services within the system. This involves assigning a unique digital identity to each entity.


- **Authentication**: Verifying the identity of users or entities through various means such as passwords, biometrics, security tokens, or multi-factor authentication (MFA).

    - See also: [AuthN](../../ws-and-api-design/authn-and-authz/authn-authz.md#authentication-authn)


- **Authorization**: The process of granting appropriate access rights and permissions to authenticated users based on their roles, responsibilities, and the resources they need to access.

  - See also: [AuthZ](../../ws-and-api-design/authn-and-authz/authn-authz.md#authorization-authz)


- **Access Control**: Enforcing policies that determine what resources (e.g., files, applications, databases) users are allowed to access and what actions they can perform on those resources.


- **User Provisioning and De-provisioning**: Managing the process of creating user accounts, granting access, and removing access when it is no longer needed (e.g., when an employee leaves the organization).


- **Single Sign-On (SSO)**: A mechanism that allows users to authenticate once and access multiple applications and services without having to log in again for each application.


- **Federation**: Enabling users to access resources in one domain (e.g., within an organization) using their credentials from another trusted domain (e.g., a partner organization).


- **Audit and Compliance**: Monitoring and recording user activities to ensure compliance with security policies and regulatory requirements.


<sub>[Back to top](#table-of-contents)</sub>


##  Commercial IAM Solutions

Various commercial IAM solutions provide comprehensive tools and features for organizations to implement robust identity and access management practices, securing their digital assets and ensuring the privacy of their users and customers.

<sub>[Back to top](#table-of-contents)</sub>


### Okta
Okta is a popular cloud-based identity and access management (IAM) platform that provides secure authentication, single sign-on (SSO), and multi-factor authentication (MFA) services. 

It supports **OAuth 2.0** and **OpenID Connect** protocols for enabling secure access to applications and APIs. Okta integrates with various third-party applications and offers comprehensive features for user management, centralized access control, and identity federation.

<sub>[Back to top](#table-of-contents)</sub>

### Auth0
Auth0 is another widely used IAM platform that supports **OAuth 2.0** and **OpenID Connect**. 

It provides identity and access management services for web, mobile, and single-page applications, making it easier for developers to implement secure authentication and authorization. Auth0 offers features like social login integration, MFA, user management, and identity federation with multiple identity providers.

<sub>[Back to top](#table-of-contents)</sub>

### Ping Identity
Ping Identity offers a comprehensive IAM solution that includes **OAuth 2.0** and **OpenID Connect** support. 

It provides secure access management for applications, APIs, and IoT devices, along with features like adaptive authentication, single sign-on, and centralized policy management. Ping Identity caters to various industries, including enterprises, government agencies, and healthcare organizations.

<sub>[Back to top](#table-of-contents)</sub>

### Microsoft Azure Active Directory (Azure AD)
Azure AD is Microsoft's cloud-based identity and access management service that supports **OAuth 2.0** and **OpenID Connect**. 

It provides a robust identity platform for securing access to Microsoft services, as well as third-party applications and APIs. Azure AD offers SSO capabilities, user provisioning, and conditional access policies for enhanced security.

<sub>[Back to top](#table-of-contents)</sub>

### Salesforce Identity
Salesforce Identity is an IAM solution provided by Salesforce that supports **OAuth 2.0** and **OpenID Connect**. 

It allows organizations to manage user access to Salesforce and other connected applications securely. Salesforce Identity provides features like SSO, MFA, and identity federation to streamline user authentication and authorization processes.


<sub>[Back to top](#table-of-contents)</sub>

### OneLogin
OneLogin is an IAM platform that supports **OAuth 2.0**, **SAML**, and **OpenID Connect** for secure authentication and access control. 

It offers features like SSO, MFA, and user provisioning to simplify identity management for organizations. OneLogin integrates with various applications and directories to provide a seamless user experience.

Each of these commercial implementations offers unique features and capabilities, catering to different use cases and business requirements. Organizations can choose the one that best suits their needs to implement a secure and scalable identity and access management solution.

- See also: [OAuth](../../ws-and-api-design/authn-and-authz/oauth.md) 
- See also: [OpenId Connect](../../ws-and-api-design/authn-and-authz/openid-connect.md)


<sub>[Back to top](#table-of-contents)</sub>

___

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: What's the practical difference between Authentication and Authorization within IAM?**
A: Authentication verifies who an entity is (passwords, biometrics, MFA), while Authorization determines what that already-verified entity is allowed to do, based on assigned roles, responsibilities, and permissions.

---

**Q: How does Federation differ from Single Sign-On (SSO)?**
A: SSO lets a user authenticate once to access multiple applications within the same trust domain (e.g., one organization). Federation extends that trust across domains, allowing a user to access resources in one domain using credentials issued and verified by another trusted domain, such as a partner organization.

---

**Q: Why do nearly all commercial IAM platforms (Okta, Auth0, Azure AD, Ping Identity, Salesforce Identity, OneLogin) standardize on OAuth 2.0 and OpenID Connect?**
A: These are the industry-standard, interoperable protocols for delegated authorization (OAuth 2.0) and federated identity verification (OpenID Connect), letting each platform integrate with third-party apps, APIs, and other identity providers without proprietary, one-off integrations.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [AuthN and AuthZ](../../ws-and-api-design/authn-and-authz/authn-authz.md) — deeper dive into the authentication and authorization mechanics IAM relies on
- [Single Sign-On (SSO)](../../ws-and-api-design/authn-and-authz/sso.md) — the mechanism behind the SSO capability described above
- [SAML](../../ws-and-api-design/authn-and-authz/saml.md) — the federation protocol used by OneLogin and other IAM platforms alongside OAuth 2.0

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.techtarget.com/searchsecurity/definition/identity-access-management-IAM-system
- https://cloud.google.com/iam/docs/overview
- https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html

---

[Get Started](../../../get-started.md) |
[Cyber-security](../../../get-started.md#cyber-security-fundamentals)

___
