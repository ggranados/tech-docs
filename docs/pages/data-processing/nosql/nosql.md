# NoSQL (Not Only SQL) Database

## Table of Contents
<!-- TOC -->
* [NoSQL (Not Only SQL) Database](#nosql-not-only-sql-database)
  * [Table of Contents](#table-of-contents)
  * [Data Structure](#data-structure)
  * [Data Integrity](#data-integrity)
  * [Query Language](#query-language)
  * [Schema](#schema)
  * [Scaling](#scaling)
  * [Examples](#examples)
  * [Ref.](#ref)
<!-- TOC -->

A NoSQL (which stands for "**not only SQL**") database is a type of database management system that is designed to handle and store data in ways that are different from traditional relational databases. NoSQL databases are particularly _well-suited for handling large volumes of unstructured or semi-structured data_, and they provide more flexibility and scalability than traditional SQL databases in certain use cases. 

Here are some key characteristics and features of NoSQL databases:


<sub>[Back to top](#table-of-contents)</sub>

## Data Structure
NoSQL databases are designed to store unstructured, semi-structured, or highly variable data. They use various data models, such as document, key-value, column-family, and graph, to accommodate different data types.

 - ### Schema-less:
    NoSQL databases are typically schema-less, meaning they don't require a fixed table structure with predefined columns and data types.

 - ### Variety of Data Models:
   - **Key-Value Stores**: These databases store data as key-value pairs, making them ideal for simple and fast data retrieval. Examples include Redis and Amazon DynamoDB.
   - **Document Stores**: These databases store data as JSON, BSON, or XML documents, making them suitable for semi-structured data. Examples include MongoDB and Couchbase.
   - **Column-Family Stores**: These databases organize data into column families and columns, making them efficient for storing and retrieving large amounts of data. Apache Cassandra is an example.
   - **Graph Databases**: These databases are designed for managing and querying data with complex relationships. Neo4j is a well-known graph database.


<sub>[Back to top](#table-of-contents)</sub>
    
## Data Integrity
NoSQL databases may offer eventual consistency instead of strong ACID guarantees. They prioritize availability and partition tolerance over strict consistency, which is suitable for certain use cases.


<sub>[Back to top](#table-of-contents)</sub>

## Query Language
NoSQL databases use query languages specific to their data model. Some support flexible and dynamic querying, while others offer simple key-based retrieval.


<sub>[Back to top](#table-of-contents)</sub>

## Schema
NoSQL databases are schema-flexible, meaning you can add or change fields without requiring a predefined schema. This flexibility is well-suited for applications with evolving data requirements.


<sub>[Back to top](#table-of-contents)</sub>

## Scaling
NoSQL databases are designed for horizontal scalability, allowing you to add more servers to distribute data and handle increased load. They are suitable for large-scale and distributed systems.

<sub>[Back to top](#table-of-contents)</sub>

## Examples
Examples of NoSQL databases include MongoDB (document store), Cassandra (wide-column store), Redis (key-value store), and Neo4j (graph database).


<sub>[Back to top](#table-of-contents)</sub>


___

## Ref.

- https://en.wikipedia.org/wiki/NoSQL

___

[Get Started](../../../get-started.md) |
[No SQL](../../../get-started.md#nosql-databases)

---
