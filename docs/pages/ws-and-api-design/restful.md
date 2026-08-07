# RESTful Architecture

---

## Table of Contents
<!-- TOC -->
* [RESTful Architecture](#restful-architecture)
  * [Table of Contents](#table-of-contents)
  * [What is REST?](#what-is-rest)
* [Architectural constraints](#architectural-constraints)
  * [HTTP Fundamentals](#http-fundamentals)
    * [HTTP Verbs](#http-verbs)
    * [URI (Uniform Resource Identifier) and URL (Uniform Resource Locator)](#uri-uniform-resource-identifier-and-url-uniform-resource-locator)
    * [Request and Response Headers](#request-and-response-headers)
    * [HTTP Status Codes in REST](#http-status-codes-in-rest)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## What is REST?

REST stands for "Representational State Transfer." It is an **architectural style** and set of constraints used in *designing and structuring networked applications*, particularly *web services and APIs*.

REST is commonly used to build distributed, stateless and reliable Web-based systems that allow different software components to communicate over a network, typically the internet.

This architectural style emphasises the scalability of interactions between components, uniform interfaces, independent deployment of components, and the creation of a layered architecture to facilitate caching of components to reduce user-perceived latency, enforce security, and encapsulate legacy systems.

<sub>[Back to top](#table-of-contents)</sub>

# Architectural constraints

REST defines six guiding constraints. When these constraints are applied to the system architecture, it gains desirable non-functional properties, such as _performance_, _scalability_, _simplicity_, _modifiability_, _visibility_, _portability_, and _reliability_

<sub>[Back to top](#table-of-contents)</sub>

- ### Statelessness
  Each request from a client to a server must contain all the information necessary for the server to understand and process the request. _The server should not store any client context between requests_. This design promotes scalability and simplifies server maintenance.

<sub>[Back to top](#table-of-contents)</sub>

- ### Client-Server Architecture
  REST _separates the concerns of client and server components_, allowing them to evolve independently. Clients are responsible for the user interface and user experience, while servers handle data storage, processing, and management.

<sub>[Back to top](#table-of-contents)</sub>

- ### Cacheability
 Responses from the server can be labeled as `cacheable` or `non-cacheable`. Caching allows clients to _reuse previously fetched data_, reducing the need for repeated requests to the server and improving performance.

<sub>[Back to top](#table-of-contents)</sub>

- ### Uniform Interface
  REST _employs a consistent and uniform set of operations_ (`HTTP` methods) to interact with resources. These methods include `GET` (retrieve), `POST` (create), `PUT` (update), and `DELETE` (remove).

<sub>[Back to top](#table-of-contents)</sub>

- ### Layered System
  A layered architecture _enables intermediaries, such as proxies and gateways, to be placed between clients and servers without affecting the overall system behavior_. This enhances scalability, flexibility, and security.

<sub>[Back to top](#table-of-contents)</sub>

- ### Code on Demand (Optional)
  Servers _can provide executable code to clients on-demand_. This feature is optional in the REST architecture and _is rarely used in practice_.

  >Historically, this constraint was more relevant in the early days of the web when web browsers were less capable and had limited scripting capabilities. However, with the advancement of web technologies, most dynamic behavior and interactivity are achieved using other techniques such as AJAX (Asynchronous JavaScript and XML) and modern JavaScript frameworks.
  >Old Java applets are an example of the "Code on Demand" constraint in the context of the REST architectural style 

<sub>[Back to top](#table-of-contents)</sub>

## HTTP Fundamentals

HTTP stands for "Hypertext Transfer Protocol." It is the fundamental protocol that underpins data communication on the World Wide Web. HTTP is an application layer protocol used for transmitting various types of data, such as HTML documents, images, videos, and other resources, between a client (typically a web browser) and a server (hosting websites and web applications).

-See also [HTTP & HTTPS]()<!--TODO-->

<sub>[Back to top](#table-of-contents)</sub>

### HTTP Verbs

HTTP defines several methods that indicate the desired action to be performed on a resource. The most commonly used methods in REST are:

- **GET**: Retrieve a resource.
  ```http request
  GET /api/posts/123 HTTP/1.1
  Host: www.example.com
  ```

- **POST**: Create a new resource.
  ```http request
  POST /api/users HTTP/1.1
  Host: www.example.com
  Content-Type: application/json
  
  {
  "name": "John Doe",
  "email": "john@example.com"
  }
  ```

- **PUT**: Update or replace an existing resource.
  ```http request
  PUT /api/products/456 HTTP/1.1
  Host: www.example.com
  Content-Type: application/json
  
  {
  "name": "Updated Product",
  "price": 29.99
  }
  ```
  
- **PATCH**: Partially update an existing resource.
  ```http request
  PATCH /api/orders/789 HTTP/1.1
  Host: www.example.com
  Content-Type: application/json
  
  {
  "status": "shipped"
  }
  ```

- **DELETE**: Remove a resource.
  ```http request
  DELETE /api/comments/321 HTTP/1.1
  Host: www.example.com
  ```

<sub>[Back to top](#table-of-contents)</sub>

### URI (Uniform Resource Identifier) and URL (Uniform Resource Locator)

URIs are used to uniquely identify resources. In RESTful APIs, URIs are used to address resources, and they should be designed to be meaningful and hierarchical. For example:

```http request
GET /api/users/123
```

URL (Uniform Resource Locator): A URL is a specific type of URI that not only identifies a resource but also provides the means to locate it. It includes the information needed to access a resource, such as the _protocol_ (e.g., HTTP), _domain name_, _port number_, _path_, and _query parameters_. URLs are used to specify the location of web pages, images, files, and other resources on the internet. For example:

```thymeleafurlexpressions
https://www.example.com:8080/products?id=123&page=1#details
```

<sub>[Back to top](#table-of-contents)</sub>

### Request and Response Headers
HTTP headers contain additional information about the request or response. Some important headers in REST include:

- **Content-Type**: Specifies the format of the data being sent or received (e.g., JSON, XML).
  ```
  Content-Type: application/json
  ```
  - See also: [Resource Representation Formats]()<!--TODO-->  

- **Accept**: Informs the server about the preferred response format.
  ```
  Accept: application/json
  ```

- **Authorization**: Used for sending credentials or tokens for authentication.
  ```
  Authorization: Bearer your_access_token
  ```
  
  - See also: [Authentication & Authorization](authn-and-authz/authn-authz.md)


- **Cache-Control**: Specifies caching directives for both requests and responses.
  ```
  Cache-Control: max-age=3600
  ```
  - See also: [Caching]()<!--TODO-->  

 - **User-Agent**: Identifies the client making the request, often the web browser or client application.
  ```
  User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36
  ```
- **Cookie**: Sends previously stored cookies to the server for identification and session tracking.
  ```
  Cookie: session_id=abc123; user_id=789
  ```

- **Location**: Used in responses to specify the location of a newly created resource.
  ```
  Location: /api/orders/456
  ```

- **If-None-Match**:Allows conditional requests based on resource's ETag (entity tag) value.
  ```
  If-None-Match: "etag_value"
  ```

- **Referer**: Provides the URL of the referring page, often used for tracking purposes.
  ```
  Referer: https://www.example.com/page1
  ```

<sub>[Back to top](#table-of-contents)</sub>

### HTTP Status Codes in REST

HTTP status codes are three-digit numbers sent by the server in response to a client's request. They provide information about the outcome of the request. Common status codes in REST include:

- **200 OK**: The request was successful.

- **201 Created**: The request resulted in a new resource being created.

- **204 No Content**: The request was successful, and there is no content to send in the response.

- **400 Bad Request**: The request was malformed or invalid.

- **401 Unauthorized**: Authentication is required and has failed or has not been provided.

- **404 Not Found**: The requested resource could not be found.

- **500 Internal Server Error**: An error occurred on the server.


- See also: [HTTP Status Codes]()<!--TODO-->


<sub>[Back to top](#table-of-contents)</sub>



<!--TODO:
- Statelessness and Caching
  - Stateless Architecture in REST
  - Benefits and Challenges of Statelessness
  - Caching Strategies and HTTP Caching Headers
- Security and Authentication
  - Authentication vs. Authorization
  - Token-based Authentication (JWT, OAuth)
  - Securing REST APIs (TLS, HTTPS, CORS)
- Error Handling and Exception Management
  - Common Error Response Patterns
  - Handling Errors and Exception Scenarios
  - Providing Meaningful Error Messages
- Best Practices and Guidelines
  - Naming Conventions for Resources and Endpoints
  - Keeping APIs Consistent and Predictable
  - Performance Optimization Techniques
  - Documentation and API Contracts
- Real-world Examples and Case Studies
  - Building a RESTful API for a Blogging Platform
  - Integrating Third-Party Services via REST
  - Scaling and Load Balancing REST APIs
- Future Trends in REST Architecture
  - GraphQL vs. REST
  - Serverless Architectures and REST
  - Webhooks and Event-Driven REST APIs
-->

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: The Code on Demand constraint is described as optional and rarely used — does skipping it make an API "not RESTful"?**
A: No. Code on Demand is the only optional constraint in Fielding's REST style; an API can satisfy all five other constraints (statelessness, client-server, cacheability, uniform interface, layered system) and still be fully RESTful.

---

**Q: How does the Layered System constraint relate to putting an API Gateway in front of your services?**
A: An API Gateway is a textbook example of a layered-system intermediary — clients only see the gateway, not the underlying services, so gateways, proxies, and load balancers can be inserted or changed without clients noticing.

---

**Q: If a server stores session state to avoid resending data on every request, does that violate REST?**
A: Yes. The Statelessness constraint requires every request to carry all information needed to process it; server-side session state between requests breaks that constraint and reduces scalability.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [RESTful API Design](restful/restful-api-design.md) — practical patterns (endpoints, versioning, HATEOAS) built on these architectural constraints.
- [API Design Principles](restful/api-design-principles.md) — broader design principles that complement REST's constraints.
- [Resource Design and Representation](restful/resource-design-representation.md) — how resources identified by URIs are modeled and represented.
- [Authentication (AuthN) and Authorization (AuthZ)](authn-and-authz/authn-authz.md) — securing REST APIs via the `Authorization` header.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://en.wikipedia.org/wiki/Representational_state_transfer
- https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm
- https://www.tutorialspoint.com/restful/index.htm

---

[Get Started](../../get-started.md) |
[Web Services and API Design](../../get-started.md#web-services-and-api-design)

___