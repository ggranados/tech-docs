# OAuth

---

## Table of Contents

<!-- TOC -->
* [OAuth](#oauth)
  * [Table of Contents](#table-of-contents)
    * [OAuth Versions](#oauth-versions)
  * [How OAuth Works](#how-oauth-works)
    * [OAuth2.0 Roles](#oauth20-roles)
    * [OAuth Flow Steps](#oauth-flow-steps)
  * [Benefits of OAuth](#benefits-of-oauth)
  * [Grant Types](#grant-types)
    * [Authorization Code Flow](#authorization-code-flow)
    * [Implicit Flow](#implicit-flow)
    * [Client Credentials Flow](#client-credentials-flow)
      * [JWT Bearer Token Flow](#jwt-bearer-token-flow)
    * [Resource Owner Password Credentials Flow](#resource-owner-password-credentials-flow)
  * [Example](#example)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

OAuth (Open Authorization) is an open standard protocol that allows secure and controlled access to protected resources, such as user data or APIs, without the need to share a user's credentials (username and password) with the requesting application. It provides a standardized way for applications to request and obtain authorization to access resources on behalf of a user. OAuth is widely used in modern web and mobile applications to enable secure access to various services and APIs.

<sub>[Back to top](#table-of-contents)</sub>


### OAuth Versions
OAuth 2.0 logoOAuth 2.0 is the industry-standard protocol for authorization. It focuses on client developer simplicity while providing specific authorization flows for web applications, desktop applications, mobile phones, and living room devices. This specification and its extensions are being developed within the IETF OAuth Working Group.

OAuth 2.1 is an in-progress effort to consolidate OAuth 2.0 and many common extensions under a new name.

<sub>[Back to top](#table-of-contents)</sub>

## How OAuth Works

### OAuth2.0 Roles

OAuth involves four main roles: 

- **User (Resource Owner)**: The end-user who owns the protected resources and wants to grant access to a client application without sharing their credentials.


- **Client Application (Third-Party App)**: The application or service that wants to access the user's resources on their behalf. This can be a mobile app, a web app, or any other type of application.


- **Resource Server**: The server hosting the protected resources, such as an API service or a user's data.


- **Authorization Server**: The server responsible for authenticating the user and issuing access tokens to the client application.


<sub>[Back to top](#table-of-contents)</sub>

### OAuth Flow Steps

- **Authorization Request**: The client application initiates the OAuth flow by redirecting the user to the authorization server's endpoint with specific parameters, including the client ID, the requested scope (permissions), and a redirect URL.


- **User Consent**: At the authorization server, the user is presented with a login prompt and an authorization request from the client application. The user can review the requested permissions and grant consent to the client application.


- **Authorization Grant**: Once the user gives consent, the authorization server generates an `authorization grant` (e.g., an *authorization code*) and redirects the user back to the client application's redirect URL.


- **Access Token Request**: The client application exchanges the received `authorization grant` and its `client credentials` (client ID and client secret) with the authorization server to request an access token.


- **Access Token Issuance**: After verifying the `authorization grant` and `client credentials`, the authorization server issues an access token to the client application.

- **Accessing Protected Resources**: With the received access token, the client application can now make requests to the resource server on behalf of the user. The resource server validates the access token and serves the requested resources if the token is valid and has the necessary permissions.

![img.png](../../../img/oauth-flow.png)

<sub>[Back to top](#table-of-contents)</sub>

## Benefits of OAuth

- **Enhanced Security**: OAuth eliminates the need for applications to store and handle user credentials, reducing the risk of data breaches and unauthorized access.


- **User Control**: Users can review the permissions requested by the client application before granting access, giving them greater control over their data.


- **Scalability**: OAuth allows users to grant access to multiple client applications without sharing their credentials with each of them individually.


- **Single Sign-On (SSO) Support**: OAuth can be combined with other authentication protocols to enable seamless single sign-on experiences across multiple applications.

<sub>[Back to top](#table-of-contents)</sub>


## Grant Types

### Authorization Code Flow

(Standard Web Application Flow)

This is the most common and secure OAuth flow for web applications. It involves multiple steps, including redirecting the user to the authorization server for consent and obtaining an `authorization code`, which is then exchanged for an `access token`. This flow is suitable for *server-to-server* communication and allows for *token refresh*.

Flow steps are:

- The user is redirected to the authorization server's login page to authenticate.


- After successful authentication, the authorization server issues an authorization code to the client.


- The client exchanges this authorization code for an access token and a refresh token.


- The access token is then used to access protected resources on behalf of the user.

<sub>[Back to top](#table-of-contents)</sub>

### Implicit Flow

(Single-Page Application Flow)

The Implicit Flow is designed for *browser-based applications* and is commonly used in *single-page applications* (SPAs). Instead of obtaining an `authorization code`, the access *token is returned directly to the client* application after user consent. However, this flow does not support token refresh, which can be a security concern.

Flow steps are:

- The user accesses client application.


- Then client redirects user to authorization server for authentication.


- The user logs in and grants consent.


- Authorization server redirects user back to client with access token as a URL fragment.


- The client retrieves access token from URL fragment.


- Then the client uses access token retrieved to access protected resources.


<sub>[Back to top](#table-of-contents)</sub>

### Client Credentials Flow

The Client Credentials Flow is used when the client application (usually a server-side application) needs to *access its resources without acting on behalf of any specific user*. The client directly requests an access token from the authorization server using *its own credentials* (`client ID` and `client secret`).

Flow steps are:

- The client application sends its client ID and client secret to authorization server.


- Authorization server then validates client credentials.


- Authorization server issues an access token directly to the client.


- Client then uses access token to access protected resources.


<sub>[Back to top](#table-of-contents)</sub>

#### JWT Bearer Token Flow

> The JWT Bearer Token Flow is essentially a JWT (JSON Web Token) implementation of the OAuth 2.0 Client Credentials Flow

The JWT Bearer Token Flow is a simplified OAuth 2.0 flow designed for clients that are not capable of keeping client secrets, such as native mobile apps or single-page applications (SPAs). 

Flow steps are:

- The client requests an access token directly from the authorization server.


- The client authenticates using its client ID and a private key (usually through a JSON Web Key Set, or JWKs).


- The authorization server validates the client's identity and issues a signed JWT (JSON Web Token) as an access token.


- The client uses this JWT as a bearer token to access protected resources.


  - See also: [JWT](jwt.md)


<sub>[Back to top](#table-of-contents)</sub>


### Resource Owner Password Credentials Flow

Is intended for trusted applications (e.g., *native mobile apps*) where the client application collects the user's username and password directly and sends them to the *authorization server* to obtain an `access token`. This flow should be used with caution due to potential security risks of handling user credentials.

Flow steps are:

- The user provides its username and password to client application.


- The client application sends user's credentials to authorization server.


- Then authorization server validates user credentials.


- Authorization server then issues an access token directly to the client.


- The client then uses access token to access protected resources.


>There are also extension grant types and custom flows that can be implemented based on specific requirements, but the four flows mentioned above are the core flows standardized by OAuth 2.0.


<sub>[Back to top](#table-of-contents)</sub>

## Example
Basic example of the Authorization Code Flow implemented in an HTML page using JavaScript. Please remember that this example is simplified for educational purposes and may not cover all security considerations or error handling.

```html
<!DOCTYPE html>
<html>
<head>
    <title>Authorization Code Flow Example</title>
</head>
<body>
    <h1>Authorization Code Flow Example</h1>
    
    <button onclick="authorize()">Authorize</button>
    
    <script>
        function authorize() {
            // Step 1: Redirect user to authorization server for authentication and consent
            const authorizationEndpoint = 'https://example.com/authorization'; // Replace with your authorization endpoint
            const clientId = 'your_client_id'; // Replace with your client ID
            const redirectUri = 'https://your-redirect-uri.com/callback'; // Replace with your redirect URI
            const scope = 'desired_scope'; // Replace with desired scope
            
            const authUrl = `${authorizationEndpoint}?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}`;
            
            window.location.href = authUrl;
        }
        
        // Callback function after user is redirected back from authorization server
        function handleCallback() {
            const authorizationCode = getParameterByName('code');
            
            // Step 3: Exchange authorization code for an access token
            const tokenEndpoint = 'https://example.com/token'; // Replace with your token endpoint
            const clientSecret = 'your_client_secret'; // Replace with your client secret
            
            const tokenData = {
                grant_type: 'authorization_code',
                code: authorizationCode,
                redirect_uri: 'https://your-redirect-uri.com/callback',
                client_id: 'your_client_id',
                client_secret: clientSecret
            };
            
            fetch(tokenEndpoint, {
                method: 'POST',
                body: new URLSearchParams(tokenData),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(response => response.json())
            .then(data => {
                const accessToken = data.access_token;
                
                // Step 4: Use access token to access protected resources
                const resourceEndpoint = 'https://api.example.com/resource'; // Replace with your protected resource endpoint
                
                fetch(resourceEndpoint, {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`
                    }
                })
                .then(response => response.json())
                .then(resourceData => {
                    console.log('Resource Data:', resourceData);
                })
                .catch(error => {
                    console.error('Error accessing resource:', error);
                });
            })
            .catch(error => {
                console.error('Error exchanging authorization code for token:', error);
            });
        }
        
        // Helper function to get query parameter by name from URL
        function getParameterByName(name) {
            const url = window.location.href;
            name = name.replace(/[\[\]]/g, '\\$&');
            const regex = new RegExp(`[?&]${name}(=([^&#]*)|&|#|$)`),
            results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, ' '));
        }
    </script>
</body>
</html>
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why is the Implicit Flow now discouraged even though it's simpler than the Authorization Code Flow?**
A: The access token is returned directly in the URL fragment with no client authentication or refresh-token support, making it vulnerable to token leakage via browser history or referrer headers. The Authorization Code Flow (with PKCE for public clients) is now recommended instead.

---

**Q: What's the difference between the Client Credentials Flow and the Authorization Code Flow in terms of who is being authorized?**
A: Client Credentials authorizes the application itself with no end user involved, used for service-to-service calls, while the Authorization Code Flow authorizes the application to act on behalf of a specific resource owner (end user).

---

**Q: OAuth is described as an authorization protocol — so why do people often use it to "log in"?**
A: OAuth alone only grants access to resources; it doesn't standardize identity. OpenID Connect adds an identity layer (the ID Token) on top of OAuth 2.0 specifically to support authentication/login use cases.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [OpenID Connect (OIDC)](openid-connect.md) — adds an identity/authentication layer on top of OAuth 2.0.
- [JSON Web Token (JWT)](jwt.md) — commonly used as the access token format in OAuth flows.
- [Authentication (AuthN) and Authorization (AuthZ)](authn-authz.md) — OAuth as an authorization method within this broader model.
- [Single Sign-On (SSO)](sso.md) — OAuth is one of the protocols that can be combined to implement SSO.

<sub>[Back to top](#table-of-contents)</sub>

___

## Ref.

- https://oauth.net/2/
- https://auth0.com/intro-to-iam/what-is-oauth-2

---

[Get Started](../../../get-started.md) |
[Web Services and API Design](../../../get-started.md#web-services-and-api-design)

___

