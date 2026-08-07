# API Design Principles

## Table of Contents
<!-- TOC -->
* [API Design Principles](#api-design-principles)
  * [Table of Contents](#table-of-contents)
  * [API First](#api-first)
    * [API First approach](#api-first-approach)
    * [Automatic Implementation from API](#automatic-implementation-from-api)
  * [Consistency](#consistency)
  * [Simplicity](#simplicity)
  * [Clarity and Intuitiveness](#clarity-and-intuitiveness)
  * [Predictability](#predictability)
  * [Versioning](#versioning)
  * [Error Handling](#error-handling)
  * [Idempotency](#idempotency)
  * [Security](#security)
  * [Performance](#performance)
  * [Scalability](#scalability)
  * [Documentation](#documentation)
  * [Testing](#testing)
  * [Feedback](#feedback)
  * [RESTful Principles (if applicable)](#restful-principles-if-applicable)
  * [Statelessness](#statelessness)
  * [HATEOAS (Hypermedia as the Engine of Application State)](#hateoas-hypermedia-as-the-engine-of-application-state)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->
---

API design principles are guidelines and best practices that help ensure that an Application Programming Interface (API) is well-designed, user-friendly, maintainable, and effective in meeting its intended purpose. Here are some common API design principles:

## API First
Is an approach to software development where you design and define the API before building the underlying functionality or user interface. It involves creating a detailed specification of the API, including endpoints, methods, parameters, and data structures, before any coding takes place

### API First approach

- **Design the API Specification**
You start by creating a detailed specification for the user accounts API. You define the endpoints, methods, request and response formats, and any required authentication mechanisms. Here's a simplified version of what the API specification might look like:

    ```
    Endpoint: /api/v1/users
    Methods: GET, POST, PUT, DELETE
    ```

    For the `POST` method:

    - Request:

    ```json
    {
    "name": "John Doe",
    "email": "john@example.com",
    "password": "secretpassword"
    }
    ```

    - Response:

    ```json
    {
    "id": 123,
    "name": "John Doe",
    "email": "john@example.com"
    }
    ```
  
- **Share and Collaborate**
You share the API specification with frontend and backend developers, as well as other stakeholders. This specification serves as a clear contract that everyone can refer to during development.


- **Develop and Test**
Developers start building the API endpoints based on the specification. Since the API's structure and behavior are well-defined, development can proceed more smoothly without ambiguity.


- **Build User Interfaces**
Once the API is developed and tested, frontend developers can start building user interfaces that interact with the API. They can use the API documentation to understand how to make requests and handle responses.


- **Iterate and Improve**
  As development progresses, if any changes or improvements to the API are needed, they can be discussed and incorporated into the specification before implementing the changes

### Automatic Implementation from API
There are several automated tools and frameworks available that can generate implementation code from an API definition. These tools help streamline the development process by automatically generating boilerplate code, handling request and response serialization, and providing a starting point for developers to build upon. Here are a few examples:

- [Swagger (OpenAPI Generator)]()<!--TODO-->
- Retrofit (for Android)
- NSwag
- LoopBack (for Node.js)
- Postman Code Generator
- Restlet Studio

<sub>[Back to top](#table-of-contents)</sub>

## Consistency
Keep a consistent naming convention and structure throughout the API. This includes naming endpoints, methods, parameters, and responses in a way that is intuitive and follows a logical pattern.


- Consistent naming convention throughout the API:
    ```http request
    GET /users
    POST /users
    GET /users/{userId}
    ```

<sub>[Back to top](#table-of-contents)</sub>

## Simplicity
Keep the API simple and easy to understand. Avoid unnecessary complexity or exposing too many features in a single API. Aim for a minimalistic design that provides just what's needed.

- Avoiding unnecessary complexity:

```http request
GET /products/{productId}/reviews
```

<sub>[Back to top](#table-of-contents)</sub>


## Clarity and Intuitiveness
Use clear and meaningful names for endpoints, methods, and parameters. The API should be self-explanatory and easy for developers to understand without needing extensive documentation.

- Clear and self-explanatory naming:
```http request
GET /weather/forecast?city=NewYork
```

<sub>[Back to top](#table-of-contents)</sub>

## Predictability
Make the API's behavior predictable. Users should be able to anticipate how the API will behave based on their previous interactions and experience with other APIs.

- Consistent behavior for pagination:
```http request
GET /posts?page=2&limit=10
```

<sub>[Back to top](#table-of-contents)</sub>


## Versioning
Plan for versioning from the beginning. As your API evolves, changes might be necessary. Using version numbers in the API endpoints can help manage backward compatibility and prevent breaking changes.

- Versioning in the API endpoint:
```http request
/v1/products
/v2/products
```

<sub>[Back to top](#table-of-contents)</sub>

## Error Handling
Provide clear and informative error messages. Help developers understand what went wrong and how to fix it. Use appropriate HTTP status codes to indicate the outcome of requests.

Informative error response:
```json
{
    "error": {
        "code": 404,
        "message": "Product not found"
    }
}
```
- See also: [HTTP Error Codes]()<!--TODO-->

<sub>[Back to top](#table-of-contents)</sub>

## Idempotency
Design methods and endpoints to be idempotent where possible. This means that _making the same request multiple times will have the same effect as making it once_, preventing unintended side effects.

- Idempotent action (DELETE method):
```http request
DELETE /cart/{itemId}
```
- See also: [Idempotency]()<!--TODO-->

<sub>[Back to top](#table-of-contents)</sub>

## Security
Implement proper _authentication_ and _authorization_ mechanisms to ensure that only authorized users can access the API. Use encryption for sensitive data and follow security best practices.

- Using API keys for authentication:
```http request
GET /secure/resource?api_key=your_api_key
```
- See also: [Authentication and Authorization](../authn-and-authz/authn-authz.md)

<sub>[Back to top](#table-of-contents)</sub>

## Performance
Design the API for optimal performance. Consider factors like response times, data transfer size, and caching mechanisms to minimize latency and maximize efficiency.

- Efficient response with pagination and limited data:
```http request
GET /orders?status=completed&limit=20
```

<sub>[Back to top](#table-of-contents)</sub>

## Scalability
Design the API to handle increased traffic and usage over time. Consider how the API will be deployed and distributed to accommodate growing demand.

- Distributed API architecture for scalability:
````bash
Load Balancer -> API Gateway -> Microservices
````

<sub>[Back to top](#table-of-contents)</sub>

## Documentation
Provide comprehensive and well-structured documentation. Include clear usage examples, explanations of endpoints, request/response formats, and any required authentication methods.

- Clear API documentation with examples:
```http request
GET /docs/api-reference
```
- See also: [OpenAPI]()<!--TODO-->
- See also: [Swagger]()<!--TODO-->

<sub>[Back to top](#table-of-contents)</sub>

## Testing
Thoroughly test the API before releasing it. Implement unit tests, integration tests, and user acceptance tests to ensure the API functions as expected and handles various scenarios.

- Unit test for a POST request:
```python
def test_create_user():
    response = client.post("/users", json={"name": "John"})
    assert response.status_code == 201
```

<sub>[Back to top](#table-of-contents)</sub>

## Feedback
Gather feedback from developers who use the API. This can help identify pain points, areas for improvement, and potential issues that may not be apparent during the design phase.

<sub>[Back to top](#table-of-contents)</sub>

## RESTful Principles (if applicable)
If designing a RESTful API, adhere to REST principles, such as using HTTP methods appropriately (GET, POST, PUT, DELETE), using meaningful URIs, and leveraging status codes.

- Using HTTP methods:
```http request
GET /articles
POST /articles
PUT /articles/{articleId}
DELETE /articles/{articleId}
```

<sub>[Back to top](#table-of-contents)</sub>

## Statelessness
Strive to make the API stateless, meaning that each request contains all the information needed for the server to fulfill it. This simplifies server management and improves scalability.

- Stateless request with all necessary data:
```http request
POST /checkout
{
    "items": [...],
    "shipping_address": {...},
    "payment_info": {...}
}
```

<sub>[Back to top](#table-of-contents)</sub>

## HATEOAS (Hypermedia as the Engine of Application State)
In a RESTful context, consider _including hypermedia links in API responses to guide clients on how to interact with the API further_.

This enables clients to interact with the API more dynamically and autonomously, as they can navigate the application's state based on the links provided in the responses.

- Hypermedia links in response:
  ```json
  {
      "title": "Sample Article",
      "content": "This is a sample article.",
      "_links": {
          "self": { "href": "/articles/123" },
          "author": { "href": "/users/456" }
      }
  }
  ```

- With **HATEOAS**, a client can start at a known entry point (usually called a "root" resource) and explore the API by following links in the responses

  Suppose you have a blog API, and you're using HATEOAS:
  
  A client requests the list of blog posts:
  
  ```http request
  GET /api/posts
  ```
  
  The response not only contains the list of posts but also links to related resources, such as the author's profile and comments:
  
  ```json
  {
    "posts": [
      {
        "id": 1,
        "title": "Introduction to HATEOAS",
        "_links": {
          "self": { "href": "/api/posts/1" },
          "author": { "href": "/api/users/123" },
          "comments": { "href": "/api/posts/1/comments" }
        }
      },
      // ...
    ]
  }
  ```
  The client can now navigate to the author's profile or the comments section by following the links provided in the response.


- See also: [HATEOAS](restful-api-design.md#hateoas)

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Are all of these principles REST-specific, or do they apply to any API style?**
A: Most, such as Consistency, Simplicity, Versioning, Security, and Documentation, apply to any API style (REST, GraphQL, gRPC); only the "RESTful Principles," Statelessness, and HATEOAS sections are REST-specific constraints layered on top of the general principles.

---

**Q: The Idempotency section shows DELETE as idempotent — why does that matter for retry logic?**
A: If a client's request times out and it retries, an idempotent operation like DELETE produces the same end state no matter how many times it's repeated, so blind retries are safe; non-idempotent operations like POST need extra safeguards, such as idempotency keys, to avoid duplicate side effects.

---

**Q: How does the API First approach change the working relationship between frontend and backend teams?**
A: Because the API specification (endpoints, request/response shapes) is agreed upon before implementation, frontend and backend teams can build against the same contract in parallel instead of the frontend waiting on a finished backend.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [RESTful API Design](restful-api-design.md) — applies these principles to concrete endpoint, versioning, and HATEOAS patterns.
- [Resource Design and Representation](resource-design-representation.md) — how resources are modeled to align with these principles.
- [RESTful Architecture](../restful.md) — the architectural constraints these design principles build on.
- [Authentication (AuthN) and Authorization (AuthZ)](../authn-and-authz/authn-authz.md) — detail on the Security principle referenced above.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.contentstack.com/blog/tech-talk/effective-restful-api-design-principles

---

[Get Started](../../../get-started.md) |
[Web Services and API Design](../../../get-started.md#web-services-and-api-design)

___