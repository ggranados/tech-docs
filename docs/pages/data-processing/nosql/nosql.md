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
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
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

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: When should I choose a document store over a key-value store?**
A: Choose a document store (MongoDB, Couchbase) when you need to query and index nested fields inside semi-structured records. Choose a key-value store (Redis, DynamoDB) when access is purely by a known key and you need the fastest possible simple lookups.

---

**Q: Does "schema-less" mean NoSQL databases have no data validation at all?**
A: No. Schema-less means the database doesn't enforce a fixed table structure at write time, but applications typically still enforce an implicit schema in code, and many NoSQL databases (e.g., MongoDB) support optional schema validation rules.

---

**Q: If NoSQL scales horizontally, why would I ever pick a relational database?**
A: Relational databases remain the better choice when strong ACID transactional guarantees, complex multi-table joins, and mature tooling matter more than horizontal write scalability. Many real architectures use both, picking per use case — an approach known as polyglot persistence.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [SQL (Relational) Database](../sql/relational.md) — contrasting structured, schema-based database model
- [BASE](../db-concepts/base.md) — the consistency model most NoSQL databases follow instead of ACID
- [CAP Theorem](../db-concepts/cap.md) — explains the availability/consistency trade-offs NoSQL databases make
- [Kafka](../real-time/event-streaming/kafka.md) — often paired with NoSQL stores for real-time data ingestion pipelines

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://en.wikipedia.org/wiki/NoSQL

___

[Get Started](../../../get-started.md) |
[No SQL](../../../get-started.md#nosql-databases)

---
