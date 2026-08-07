# Resource Design and Representation

## Table of Contents

## Resource Design
Resource design involves identifying the entities within your application or system that you want to expose and interact with through your API.

<sub>[Back to top](#table-of-contents)</sub>

- ### Resources
In REST, a resource is a key abstraction that represents a piece of data or an entity. Resources can be virtually anything: a document, a user, a product, a video, etc.

<sub>[Back to top](#table-of-contents)</sub>

- ### URI
Resources are typically identified by URIs (Uniform Resource Identifiers), commonly known as URLs (Uniform Resource Locators). These URIs act as unique addresses for each resource.
 - See also[URL and URI](../restful.md#uri-uniform-resource-identifier-and-url-uniform-resource-locator)

>Resource design should be based on a clear understanding of your application's domain model. It's crucial to define what resources are available, their properties, relationships, and the operations that can be performed on them.


<sub>[Back to top](#table-of-contents)</sub>

## Resource Representation:

Once you've identified and designed your resources, the next step is to represent them. Resource representation refers to how the state of a resource is conveyed between the client and the server.

## Resource Representation Formats
In REST, resources are typically represented in various formats, such as **JSON**, **XML**, **HTML**, or even plain text. 

>**JSON** is the most commonly used format for modern RESTful APIs due to its simplicity and readability.

 - See also: [DATA Formats]() <!--TODO: -->

When a client requests a resource from a server, the server responds with a representation of that resource. This representation can include the resource's data, metadata, and links to related resources.
 
 - See also: [HATEOAS](api-design-principles.md#hateoas-hypermedia-as-the-engine-of-application-state)
 - See also: [HATEOAS](restful-api-design.md#hateoas)

The client can manipulate or interact with a resource by sending requests to its URI and including the desired action (e.g., `GET`, `POST`, `PUT`, `DELETE`) and any necessary data (e.g., in the request body or query parameters).

 - See also: [HTTP Verbs](../restful.md#http-verbs)
  

The server interprets the client's request, performs the requested action, and sends back a response with the updated representation of the resource or relevant status codes.


<sub>[Back to top](#table-of-contents)</sub>


## Example
Let's walk through a basic example of REST resource design and representation using a simple hypothetical scenario: managing a collection of books in a library.

### Data Modeling

#### Books Resource:
 - URI: `/books`
 - Properties:
   - `id` (a unique identifier for each book)
   - `title` (the title of the book)
   - `publication_year` (the year the book was published)
 - Relationships:
   - `author_id` (a foreign key referencing the author resource)
 
 <sub>[Back to top](#table-of-contents)</sub>
 
#### Authors Resource:

 - URI: `/authors`
 - Properties:
   - `id` (a unique identifier for each author)
   - `name` (the name of the author)

 <sub>[Back to top](#table-of-contents)</sub>
   
### Resource Representation:

With this data model, the relationships between `books` and `authors` are explicitly represented using the `author_id` foreign key in the book resource.

- #### Book Representation (JSON):


```json
{
  "id": 1,
  "title": "The Great Gatsby",
  "publication_year": 1925,
  "author_id": 101
}
```

- #### Authors Representation (JSON):


```json
{
  "id": 101,
  "name": "F. Scott Fitzgerald"
}
```

<sub>[Back to top](#table-of-contents)</sub>

### Handling Relationships:

- #### Fetching Books by Author:

To retrieve all books by a specific author (e.g., F. Scott Fitzgerald), you can issue a **GET** request to `/authors/101/books`. This endpoint would return a list of book representations authored by F. Scott Fitzgerald.

```http request
GET /authors/101/books HTTP/1.1
Host: example.com
```


```json
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "title": "The Great Gatsby",
    "publication_year": 1925,
    "author_id": 101
  },
  {
    "id": 2,
    "title": "Tender Is the Night",
    "publication_year": 1934,
    "author_id": 101
  }
]

```


- #### Fetching Author of a Book:

To find out the author of a specific book (e.g., "The Great Gatsby"), you can issue a **GET** request to `/books/1/author`. This endpoint would return the author representation for that book.


```http request
GET /books/1/author HTTP/1.1
Host: example.com
```


```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 101,
  "name": "F. Scott Fitzgerald"
}
```



- #### Creating a New Book:

When creating a new book, you would send a **POST** request to `/books` with the book's details in the request body. You'd include the author_id to specify the author of the book.


```http request
POST /books HTTP/1.1
Host: example.com
Content-Type: application/json

{
  "title": "New Book Title",
  "publication_year": 2023,
  "author_id": 101
}
```


```json
HTTP/1.1 201 Created
Content-Type: application/json

{
"id": 3,
"title": "New Book Title",
"publication_year": 2023,
"author_id": 101
}

```

This associates the new book with F. Scott Fitzgerald.

- #### Updating Book Author:

To change the author of a book, send a **PUT** or **PATCH** request to `/books/1` with the updated author_id. This action effectively reassigns the book to a different author.

```http request
PUT /books/1 HTTP/1.1
Host: example.com
Content-Type: application/json

{
  "author_id": 102
}
```

```json
HTTP/1.1 200 OK
Content-Type: application/json

{
"id": 1,
"title": "The Great Gatsby",
"publication_year": 1925,
"author_id": 102
}

```

This would move the book from being authored by F. Scott Fitzgerald to the new author.

>By explicitly modeling relationships between resources and providing endpoints to navigate those relationships, you create a more comprehensive and user-friendly API. Clients can easily retrieve books by author or find out who the author of a specific book is, and they can manipulate these relationships when creating or updating resources. This approach ensures data consistency and makes the API more intuitive for consumers.


<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: The example uses both `/authors/{id}/books` and `/books/{id}/author` for the same relationship — why expose it both ways instead of picking one?**
A: Each direction serves a different client access pattern — browsing an author's catalog versus looking up a book's author — so exposing both makes the API more intuitive and avoids forcing clients to fetch and filter the full collection.

---

**Q: Why does updating a book's author use PUT/PATCH on `/books/1` instead of a dedicated endpoint like `/books/1/author`?**
A: The author is just a property (`author_id`) of the book resource, so it's modified like any other resource attribute through the standard update methods on the resource itself, keeping the API uniform rather than adding one-off relationship endpoints.

---

**Q: The page says JSON is the most common representation format — when would XML or HTML still be the right choice?**
A: XML remains common in enterprise, legacy, or SOAP-adjacent systems and where strict schema validation is required; HTML representations matter when a REST API also needs to be directly browsable and HATEOAS-navigable by a human via a browser.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [RESTful API Design](restful-api-design.md) — endpoint and URI patterns used to expose the resources modeled here.
- [API Design Principles](api-design-principles.md) — general principles, including HATEOAS, that apply to resource representations.
- [RESTful Architecture](../restful.md) — the underlying architectural constraints, including URIs and HTTP verbs, referenced throughout this page.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://www.oreilly.com/library/view/restful-web-services/9780596529260/
- https://cloud.google.com/apis/design?hl=es-419

---

[Get Started](../../../get-started.md) |
[Web Services and API Design](../../../get-started.md#web-services-and-api-design)

___