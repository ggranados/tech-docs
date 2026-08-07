# Security Assertion Markup Language (SAML)

---

## Table of Contents
<!-- TOC -->
* [Security Assertion Markup Language (SAML)](#security-assertion-markup-language-saml)
  * [Table of Contents](#table-of-contents)
  * [How SAML works](#how-saml-works)
    * [Parties Involved](#parties-involved)
    * [Authentication Flow](#authentication-flow)
  * [SAML Benefits](#saml-benefits)
  * [Example](#example)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->
---

SAML, or Security Assertion Markup Language, is an XML-based framework for exchanging authentication and authorization data between parties, primarily for the purpose of **single sign-on (SSO)** and identity federation. It allows different organizations or systems to share authentication and authorization information without the need for users to log in multiple times when accessing different applications or services.

<sub>[Back to top](#table-of-contents)</sub>

## How SAML works

### Parties Involved

- **Identity Provider (IdP)**: The entity responsible for authenticating users and generating SAML assertions. It is essentially the "source of truth" for user identities.


- **Service Provider (SP)**: The application or service that users want to access. It relies on the IdP for authentication and user information.
Authentication Flow:

<sub>[Back to top](#table-of-contents)</sub>


### Authentication Flow

- **User Access**: The user tries to access a resource on the Service Provider (SP) application.


- **SP Redirect**: The SP recognizes that the user is not authenticated and redirects them to the IdP with a SAML request.
User Authentication: The IdP prompts the user for authentication (e.g., username and password).


- **SAML Assertion Generation**: Once the user is authenticated, the IdP generates a SAML assertion containing information about the user's identity, attributes, and the fact that the user has been authenticated.
SAML Response: The IdP sends the SAML assertion back to the SP as a SAML response.


- **SP Validation**: The SP validates the SAML response's authenticity and checks if the user is authorized to access the requested resource.
Access Granted: If the SAML response is valid and the user is authorized, the SP grants the user access to the requested resource without requiring them to log in again.
SAML Assertions:


- **Authentication Statement**: Confirms that the user has been authenticated.
Attribute Statement: Provides additional user attributes or claims.
Authorization Decision Statement: Indicates whether the user is authorized for a specific resource.


<sub>[Back to top](#table-of-contents)</sub>

## SAML Benefits

- **Single Sign-On (SSO)**: Users only need to log in once to access multiple services, improving user experience and security.


- **Centralized Identity Management**: IdPs control user identities, making it easier to manage access and security policies.


- **Cross-Domain Access**: SAML enables secure access to resources across different domains or organizations.


- **Reduced Password Fatigue**: Users don't need to remember multiple passwords for different applications.


>SAML is widely used in enterprise environments, especially when different systems and services need to work together securely and efficiently. It provides a standardized way to establish trust and share authentication and authorization information between parties.


<sub>[Back to top](#table-of-contents)</sub>

## Example

Simple example of a SAML assertion in XML format

```xml
<saml:Assertion xmlns:saml="urn:oasis:names:tc:SAML:2.0:assertion" ID="_abc123" IssueInstant="2023-08-05T10:00:00Z" Version="2.0">

    <saml:Issuer>https://idpcorp.com</saml:Issuer>

    <saml:Subject>
        <saml:NameID Format="urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified">alice@example.com</saml:NameID>
        <saml:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer">
            <saml:SubjectConfirmationData NotOnOrAfter="2023-08-05T10:10:00Z"/></saml:SubjectConfirmation>
    </saml:Subject>

    <saml:Conditions NotBefore="2023-08-05T10:00:00Z" NotOnOrAfter="2023-08-05T10:10:00Z">
        <saml:AudienceRestriction>
            <saml:Audience>https://acmeapp.com</saml:Audience>
        </saml:AudienceRestriction>
    </saml:Conditions>
    
    <saml:AuthnStatement AuthnInstant="2023-08-05T10:05:00Z">
        <saml:AuthnContext>
            <saml:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Password</saml:AuthnContextClassRef>
        </saml:AuthnContext>
    </saml:AuthnStatement>

</saml:Assertion>
```

Explanation of the XML elements in the SAML assertion:

- <saml:Assertion>: The root element representing the SAML assertion.

- <saml:Issuer>: Identifies the issuer (Identity Provider) of the SAML assertion.

- <saml:Subject>: Contains information about the subject of the assertion (the user).

- <saml:NameID>: Unique identifier for the subject (user) in the specified format.

- <saml:SubjectConfirmation>: Specifies the method of subject confirmation (how the user was authenticated).

- <saml:SubjectConfirmationData>: Additional data related to subject confirmation.

- <saml:Conditions>: Specifies the conditions under which the assertion is valid.

- <saml:AudienceRestriction>: Specifies the intended audience (Service Provider) for the assertion.

- <saml:AuthnStatement>: Contains information about the user's authentication.

- <saml:AuthnContext>: Provides context about the user's authentication.

- <saml:AuthnContextClassRef>: Indicates the authentication context class reference (authentication method used).

This XML represents a basic SAML assertion that communicates the fact that the user (Alice) has been authenticated by the Identity Provider (https://idpcorp.com) and is authorized to access the Service Provider (https://acmeapp.com). It also includes information about the authentication method used and the validity period of the assertion.

>In a real-world scenario, the SAML assertion would be included in the SAML response sent from the Identity Provider to the Service Provider during the authentication process. The SAML response would also include digital signatures and other metadata to ensure security and integrity.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: SAML and OAuth/OIDC both enable federated access — when would you choose SAML over OAuth/OIDC for a new project?**
A: SAML fits enterprise/B2B SSO scenarios with existing SAML-based IdPs (e.g., Active Directory Federation Services); for modern web/mobile APIs, OAuth 2.0/OIDC is generally preferred due to lighter JSON-based tokens and native SPA/mobile support.

---

**Q: What's the difference between the Authentication Statement and the Attribute Statement inside a SAML assertion?**
A: The Authentication Statement confirms the user was authenticated (and how), while the Attribute Statement carries additional claims about the user, such as roles or email, that the Service Provider can use for authorization decisions.

---

**Q: How does the Service Provider trust that a SAML assertion hasn't been tampered with?**
A: The IdP digitally signs the SAML response/assertion with its private key, and the SP validates that signature against the IdP's known public certificate before trusting the assertion.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Single Sign-On (SSO)](sso.md) — the primary use case SAML is designed to enable.
- [OAuth](oauth.md) — a modern, JSON-based alternative to SAML for delegated access.
- [OpenID Connect (OIDC)](openid-connect.md) — a modern alternative to SAML for federated authentication.
- [Authentication (AuthN) and Authorization (AuthZ)](authn-authz.md) — SAML as one protocol for exchanging authN/authZ data.

<sub>[Back to top](#table-of-contents)</sub>

___

## Ref.

- https://auth0.com/intro-to-iam/what-is-saml
- https://es.wikipedia.org/wiki/Security_Assertion_Markup_Language


---

[Get Started](../../../get-started.md) |
[Web Services and API Design](../../../get-started.md#web-services-and-api-design)

___

