# RESTful API Design

## Table of Contents
<!-- TOC -->
* [RESTful API Design](#restful-api-design)
  * [Table of Contents](#table-of-contents)
  * [Defining Endpoints and URI Patterns](#defining-endpoints-and-uri-patterns)
    * [Example](#example)
  * [Query Parameters and Filtering](#query-parameters-and-filtering)
    * [Query Parameters](#query-parameters)
    * [Filtering](#filtering)
    * [Validation and Security](#validation-and-security)
    * [Documentation](#documentation)
  * [Pagination and Data Limiting](#pagination-and-data-limiting)
    * [Pagination Basics](#pagination-basics)
    * [Implementing Pagination](#implementing-pagination)
    * [Handling Edge Cases](#handling-edge-cases)
    * [Sorting and Filtering with Pagination](#sorting-and-filtering-with-pagination)
    * [Default Values](#default-values)
    * [Caching and Optimization](#caching-and-optimization)
  * [Versioning APIs](#versioning-apis)
    * [Common Approaches to Versioning REST APIs](#common-approaches-to-versioning-rest-apis)
  * [HATEOAS](#hateoas)
    * [How to implement HATEOAS in a REST API](#how-to-implement-hateoas-in-a-rest-api)
    * [Include Link Relations](#include-link-relations)
    * [Hypermedia Controls](#hypermedia-controls)
    * [API Documentation](#api-documentation)
    * [Client Implementation](#client-implementation)
    * [Error Handling](#error-handling)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

## Defining Endpoints and URI Patterns

Endpoints are the specific URLs that clients use to interact with your API, and URI patterns define the structure and format of these URLs. Properly defining endpoints and URI patterns is essential for creating a consistent and user-friendly API. 

- ### Identify Resources

    _Start by identifying the resources that your API will expose_. Resources are the key entities or objects in your application that clients will interact with. In a library management system, for example, resources could include books, authors, and library branches.

<sub>[Back to top](#table-of-contents)</sub>

- ### Choose Resource Names

    _Select meaningful names for your resources_. These names should be nouns and represent the entities they describe. For example, use `books` for a resource that represents books and `authors` for a resource that represents authors.

<sub>[Back to top](#table-of-contents)</sub>

- ### Use Plural Nouns

    It's a common convention in **REST** to use plural nouns for resource names. _This makes it clear that you're dealing with a collection of items_. For example, use `/books` instead of `/book` to represent a collection of books.

<sub>[Back to top](#table-of-contents)</sub>

- ### Define URI Patterns

    **URI** patterns define the structure of your API's endpoints. T*hey should be consistent, predictable, and follow a hierarchical structure that reflects the relationships between resources*. Some common patterns include:

  - #### Basic Resource URI:

     `/resources`: Represents a collection of resources.
     `/resources/{id}`: Represents an individual resource with a unique identifier.
    
  - #### Nested Resource URI:

    `/resources/{id}/subresources`: Represents a nested resource within another resource.
    `/resources/{id}/subresources/{sub_id}`: Represents an individual nested resource.

  - #### Action or Operation URI:

    `/resources/{id}/action`: Represents an action or operation that can be performed on a resource.

  - #### Filtering and Querying URI:

    `/resources?param1=value1&param2=value2`: Allows clients to filter or query a collection of resources based on parameters.

  - #### Versioning URI:

    `/v1/resources`: To indicate the version of your API (e.g., v1) in the URI.

  - #### Custom URI Patterns: 
    You can define custom URI patterns that make sense for your application's domain and use cases.

<sub>[Back to top](#table-of-contents)</sub>

- ### Consistency and Predictability:

Keep the URI patterns consistent throughout your API to make it easier for developers to understand and use. Predictability is also essential; users should be able to guess resource URIs based on established patterns.

<sub>[Back to top](#table-of-contents)</sub>

- ### Avoid Verb-based URIs:

In **REST**, HTTP methods (`GET`, `POST`, `PUT`, `DELETE`, etc.) are used to indicate the action to be taken on a resource. 
>Avoid using verbs in your URI patterns, as this goes against RESTful principles.

 - See also: [HTTP Verbs](../restful.md#http-verbs)
  

  <sub>[Back to top](#table-of-contents)</sub>
  
- ### Document Your API:

Clearly document your API, including the URI patterns, in your API documentation. This helps developers understand how to interact with your API and ensures consistent usage.

 - See also: [OpenAPI]()<!--TODO-->
 - See also: [Swagger]()<!--TODO-->


### Example

Here's an example of URI patterns for a simple library management API:

- `/books`: Represents a collection of books.
- `/books/{book_id}`: Represents an individual book.
- `/authors`: Represents a collection of authors.
- `/authors/{author_id}`: Represents an individual author.
- `/books/{book_id}/borrow`: Represents an action to borrow a book.

>By following these steps and guidelines, you can define endpoints and URI patterns that make your RESTful API intuitive and easy to use for both developers and clients.
 
 - See also: [API Design Principles](api-design-principles.md)
 - See also: [Resource Design and Representation](resource-design-representation.md)
  

  <sub>[Back to top](#table-of-contents)</sub>
  

## Query Parameters and Filtering

### Query Parameters

Query parameters are _key-value pairs included in the URL_ of an HTTP request after a question mark `?` and separated by ampersands `&`. They are used to customize the behavior of a request, such as _filtering_, _sorting_, or _paginating_ the data. Query parameters are optional, and clients can include one or more of them in a request to tailor the response to their needs.

Common query parameters include:

- #### Filtering Parameters
  Used to filter resources based on specific criteria.
  
  - `/books?author=Stephen+King`: fetches all books written by Stephen King


- #### Sorting Parameters
  Used to specify the sorting order of the results. 
  - `/books?sort=title`: retrieves books sorted by title in ascending order.
  

- #### Pagination Parameters
  Used to control the number of results per page and navigate through large data sets.

  - `/books?page=2&per_page=10`: retrieves the second page of 10 books per page.
  
 
- #### Search Parameters
  Used for full-text search or keyword-based filtering.

  - `/books?query=science+fiction`: searches for books containing the term "science fiction."

- #### Fields Parameters
  Used to specify which fields of a resource should be included in the response.

  - `/books?fields=title,author`: fetches only the title and author fields of books.
  
- #### Custom Parameters
  You can define custom query parameters to support specific functionalities in your API.


  <sub>[Back to top](#table-of-contents)</sub>
  
### Filtering

Filtering is the process of selecting a subset of resources from a collection based on specific conditions or criteria. It allows clients to narrow down their search and retrieve only the data that meets their requirements. Filtering is often accomplished using query parameters.

Example of filtering in a REST API:

- Retrieves electronic products with a maximum price of $500:
  ```http request
  GET /products?category=electronics&price_max=500
  ``` 


- Fetches active employees in the engineering department:
  ```http request
  GET /employees?department=engineering&status=active
  ```

> Filtering can be as simple or as complex as needed, depending on the API's capabilities and the data model.


<sub>[Back to top](#table-of-contents)</sub>

### Validation and Security

When implementing query **parameters** and **filtering** in your REST API, it's essential to _validate and sanitize input to prevent security vulnerabilities_ like SQL injection or other attacks. Ensure that query parameters are validated against acceptable values and constraints.

 - See also: [SQL Injection]()<!--TODO: -->
 - See also: [Cross-Site Scripting (XSS):]()<!--TODO: -->
   
  

  <sub>[Back to top](#table-of-contents)</sub>
  
### Documentation

Clearly document the available query parameters, their usage, and the expected behavior in your API documentation. This helps developers understand how to use filtering and query parameters effectively.


- See also: [OpenAPI]()<!--TODO-->
- See also: [Swagger]()<!--TODO-->


<sub>[Back to top](#table-of-contents)</sub>


>In summary, query parameters and filtering are powerful features in a REST API that enhance its flexibility and usability. They allow clients to request specific subsets of data, sort results, paginate through large data sets, and customize their interactions with the API based on their needs. Properly implementing and documenting these features can greatly improve the developer experience and make your API more versatile.


<sub>[Back to top](#table-of-contents)</sub>

## Pagination and Data Limiting

Pagination and data limiting are essential techniques for managing large datasets in a RESTful API. They _allow clients to retrieve a subset of the data in a controlled and efficient manner_, which is especially important when dealing with large collections of resources.

### Pagination Basics

Pagination divides a large dataset into smaller **"pages"** or chunks of data, making it easier for clients to navigate through the dataset. The key components of pagination are the page and per_page parameters:

 - `page`: Specifies the page number to retrieve. It typically starts at 1 for the first page.
 - `per_page`: Defines the number of items to include on each page.

For example, if you have a collection of 100 books and you set per_page to 10, you can retrieve the first 10 books on page 1, the next 10 books on page 2, and so on.

### Implementing Pagination

 - #### Add Pagination Parameters to the Endpoint:
  Modify your endpoint to accept the `page` and `per_page` query parameters. For example:

```http request
  GET /books?page=2&per_page=10
```
 
 - #### Calculate Offset and Limit
 In your API code, calculate the `offset` and `limit` based on the values of `page` and per_page. 
  The `offset` determines where to start fetching data, and the `limit` specifies the maximum number of items to return.

```javascript
offset = (page - 1) * per_page
limit = per_page
```
  
- #### Retrieve and Return Data
  Use the offset and limit values to retrieve the appropriate subset of data from your database or data source. Return this data as the response to the client.

- #### Include Pagination Metadata
  In the API response, include metadata that informs clients about the current page, total number of pages, and total count of items. This helps clients navigate through the dataset.


```json
{
  "data": [/* Array of items */],
  "meta": {
    "page": 2,
    "per_page": 10,
    "total_pages": 10,
    "total_items": 100
  }
}  
```


<sub>[Back to top](#table-of-contents)</sub>


### Handling Edge Cases

Be prepared to handle edge cases, such as when the requested page number exceeds the total number of pages or when invalid values are provided for page or per_page.
You can enforce limits on `per_page` to prevent excessively large requests.

<sub>[Back to top](#table-of-contents)</sub>


### Sorting and Filtering with Pagination

Clients may want to sort or filter data while paginating. Ensure that your API supports these features by allowing sorting and filtering query parameters.

<sub>[Back to top](#table-of-contents)</sub>


```http request
GET /books?page=2&per_page=10&sort=title&filter=author:Stephen+King
```

<sub>[Back to top](#table-of-contents)</sub>

### Default Values

Consider providing default values for `page` and `per_page` in case clients don't specify them in their requests. This helps ensure a consistent user experience.

<sub>[Back to top](#table-of-contents)</sub>

### Caching and Optimization

 - Implement caching strategies to reduce the load on your server, especially for frequently requested pages.

   - See also: [Caching]()<!--TODO: -->
  

 - Optimize database queries to efficiently retrieve paginated data.



>Pagination and data limiting enhance the usability and performance of your API, especially when dealing with large datasets. They provide clients with control over the amount of data they retrieve while maintaining a predictable and user-friendly experience.


<sub>[Back to top](#table-of-contents)</sub>

## Versioning APIs

Versioning in RESTful APIs is a crucial aspect of API design that _allows you to introduce changes, updates, and improvements to your API while ensuring backward compatibility for existing clients_. It helps prevent breaking changes for existing consumers of your API.

### Common Approaches to Versioning REST APIs

- #### URI Versioning (Path Versioning):
In URI versioning, the API version is included directly in the URL path. This is one of the most common and straightforward approaches.


- **Example URI**: 
  - `https://api.example.com/v1/resources`


- **Advantages**: 
  - Clear and explicit versioning. 
  - Easy to implement and understand.


- **Considerations**: 
  - Ensure consistency in versioning across all API endpoints. 
  - Be mindful of backward compatibility when introducing new versions.


<sub>[Back to top](#table-of-contents)</sub>
    
- #### Accept Header Versioning
In this approach, clients specify the desired API version using the Accept header in their HTTP request.

- **Example Header**: 
  - `Accept: application/vnd.example.v1+json`
  

- **Advantages**:
  - Keeps the URI cleaner. 
  - Allows clients to request specific versions dynamically. 
  

- **Considerations**:
  - Requires clients to set the header correctly. 
  - May be less intuitive for some developers.
    

<sub>[Back to top](#table-of-contents)</sub>
    
- #### Custom Header Versioning
Similar to Accept Header Versioning, you can define a custom header specifically for versioning, such as `X-API-Version`.

- **Example Header**: 
  - `X-API-Version: 1`
  

- **Advantages**:
  - Separates versioning concerns from content negotiation. 
  - Customizable to your API's needs.


- **Considerations**:
  - Ensure that clients are aware of the custom header and use it correctly. 
    

<sub>[Back to top](#table-of-contents)</sub>
    

- #### Subdomain Versioning
In subdomain versioning, the API version is included as a subdomain in the URL.

- **Example URI**: 
  - `https://v1.api.example.com/resources`
  

- **Advantages**:
  - Provides a clear and isolated versioning structure. 


- **Considerations**:
  - Requires DNS configuration for subdomains. 
  - May be less common than URI versioning.


<sub>[Back to top](#table-of-contents)</sub>
    

- #### Query Parameter Versioning
In this approach, clients specify the API version using a query parameter.

- **Example URI**: 
  - `https://api.example.com/resources?api_version=1`
  

- **Advantages**:
  - Keeps the URI clean. 
  - Allows clients to request specific versions dynamically. 
  

- **Considerations**:
  - May not be as common or intuitive as other versioning methods. 


<sub>[Back to top](#table-of-contents)</sub>
        
    
- #### Media Type (Content-Type) Versioning
You can include the API version within the media type (e.g., JSON or XML) used for request and response bodies.

- **Example Header** 
  - `Content-Type:application/json; version=1`
  

- **Advantages**:
  - Integrates versioning with content negotiation.


- **Considerations**:
  - May require adjustments in content type parsing on the server.
    

<sub>[Back to top](#table-of-contents)</sub>
    

- #### No Versioning (HATEOAS)

In some cases, you might choose to implement **HATEOAS** (Hypertext as the Engine of Application State) instead of versioning. With HATEOAS, clients navigate the API using links provided in resource representations, reducing the need for version-specific endpoints.

- **Advantages**:
  - Allows for more flexible and self-discoverable APIs. 
  - Minimizes the need for versioning. 


- **Considerations**:
  - Requires careful design and adoption of HATEOAS principles. 
  - May not be suitable for all API scenarios.


<sub>[Back to top](#table-of-contents)</sub>
    

## HATEOAS
**HATEOAS**, which stands for "_Hypertext as the Engine of Application State_" is a constraint of the REST architectural style that _encourages the inclusion of hyperlinks within resource representations to enable clients to navigate the API dynamically_. 

HATEOAS makes an API self-discoverable and reduces the need for clients to have prior knowledge of URIs or resource relationships. 

### How to implement HATEOAS in a REST API

- #### Resource Representation with Links

When designing the representation of your API resources, include hyperlinks (links) within the resource representations. These links provide information about related resources and actions that clients can take. Links typically consist of two key components:

  - #### Rel (Relation)
    Describes the relationship between the current resource and the linked resource or action. It serves as a hint to the client about the purpose of the link.
  
  - #### Href (Hypertext Reference)
    Contains the URI to access the linked resource or perform the linked action. 

Here's an example of how a resource representation with links might look in JSON:

```json
{
  "id": 1,
  "name": "Example Resource",
  "links": [
    {
      "rel": "self",
      "href": "https://api.example.com/resources/1"
    },
    {
      "rel": "author",
      "href": "https://api.example.com/author/42"
    },
    {
      "rel": "related",
      "href": "https://api.example.com/related-resource"
    }
  ]
}
```


<sub>[Back to top](#table-of-contents)</sub>

        
### Include Link Relations

Choose meaningful and well-defined link relation names (`rels`) to indicate the purpose of each link. 

- #### Common Link Relation Names (rels)
  - `self`: Indicates the link to the current resource. It's often used to retrieve the resource itself or to confirm the URL of the current resource.
  - `related`: Points to related resources that might be of interest to the client but aren't part of the primary data. This rel is useful for providing links to related resources, such as authors of a book or comments on a post.
  - `item`: Used in paginated APIs to link to individual items within a collection. For example, you might have a "next" rel for the next page and an "item" rel for each item on the page.
  - `collection`: Denotes a link to the entire collection or list of resources. This is often used for paginated results when you want to provide a link to the full list of items.
  - `prev and next`: Commonly used in paginated APIs to navigate between previous and next pages of results. For example, "prev" points to the previous page, and "next" points to the next page.
  - `first and last`: Indicate links to the first and last pages of paginated results. These are helpful for users who want to jump to the beginning or end of a large dataset.
  - `create`: Points to a resource or endpoint where clients can create a new resource. It's often associated with a POST request and includes information about the required fields.
  - `update`: Indicates a link to update or edit the current resource. It's typically associated with a PUT or PATCH request and may include a link to a form or a template for updating the resource.
  - `delete`: Points to a resource or endpoint that allows clients to delete the current resource. It's associated with a DELETE request.
  - `edit`: Similar to "update," this link provides a way to edit the current resource. It's often used interchangeably with "update."
  - `search`: Used to link to a search endpoint or resource where clients can perform a search operation, specifying search parameters or criteria.
  - `author`: Links to the author or creator of a resource. For example, in a blog post representation, you might include a link with the "author" rel to the author's profile.
  - `license`: Indicates the licensing information for the resource, providing clients with information about how the resource can be used.
  - `alternate`: Specifies alternative representations or formats of the current resource, such as different media types (e.g., JSON, XML, HTML).
  - `parent and child`: Denote hierarchical relationships between resources, where "parent" links to the parent resource, and "child" links to child resources.
  - `prev-version and next-version`: Used in versioned APIs to navigate to previous and next versions of a resource.
  - `up and down`: Indicate upward and downward navigation in a resource hierarchy, such as moving between sections or chapters in a document.
  - `payment`: Links to a payment or checkout page when dealing with e-commerce or payment processing.



<sub>[Back to top](#table-of-contents)</sub>

### Hypermedia Controls

Alongside links, you can include hypermedia controls that provide hints to clients about the actions they can perform. These controls often take the form of link templates or standardized forms that guide clients on how to construct requests for specific actions. 

For example, you might include a form template for creating a new resource with predefined input fields.

```json
{
  "id": 1,
  "name": "Example Resource",
  "controls": {
    "create": {
      "href": "https://api.example.com/resources",
      "method": "POST",
      "fields": [
        {
          "name": "name",
          "type": "text",
          "required": true
        },
        {
          "name": "description",
          "type": "text",
          "required": false
        }
      ]
    }
  }
}
```


<sub>[Back to top](#table-of-contents)</sub>

### API Documentation

Document your API's hypermedia controls and link relations in your API documentation. Clearly explain the purpose of each link and how clients should use hypermedia controls for actions. This documentation helps clients understand how to interact with your API dynamically.

- See also: [OpenAPI]()<!--TODO-->
- See also: [Swagger]()<!--TODO-->

<sub>[Back to top](#table-of-contents)</sub>


### Client Implementation

Clients of your API should be designed to follow links and use hypermedia controls to discover and navigate the API's resources and functionality dynamically. Clients can traverse links by inspecting the "rel" attribute of links and following the associated "href" to access related resources or initiate actions.


<sub>[Back to top](#table-of-contents)</sub>

### Error Handling

Implement clear error handling and provide informative error responses that also include links to relevant resources for further actions or guidance.


<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: With five versioning approaches shown (URI, Accept header, custom header, subdomain, query parameter), which is actually most widely recommended?**
A: URI/path versioning (e.g., `/v1/resources`) is the most widely adopted in practice because it's explicit, cache-friendly, and easy for developers to discover and test, even though header-based versioning is considered more "RESTfully pure."

---

**Q: The page suggests HATEOAS can replace the need for API versioning entirely — is that realistic for most teams?**
A: Rarely in practice. HATEOAS requires clients to be built to dynamically follow links rather than hardcode URIs, which most client teams don't do, so most real-world APIs still combine some form of versioning with partial hypermedia support.

---

**Q: When paginating with `page`/`per_page`, what's the risk of not enforcing a maximum on `per_page`?**
A: A client could request an extremely large page size, forcing the server to load and serialize a huge dataset in one response, which can degrade performance or be used as a denial-of-service vector — this is why the "Handling Edge Cases" section calls out enforcing such limits.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [API Design Principles](api-design-principles.md) — the broader principles (including HATEOAS) that these patterns implement.
- [Resource Design and Representation](resource-design-representation.md) — how the resources referenced by these endpoint patterns are modeled.
- [RESTful Architecture](../restful.md) — the architectural constraints (uniform interface, statelessness) these design patterns satisfy.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.oreilly.com/library/view/restful-web-services/9780596529260/
- https://cloud.google.com/apis/design?hl=es-419

---

[Get Started](../../../get-started.md) |
[Web Services and API Design](../../../get-started.md#web-services-and-api-design)

___