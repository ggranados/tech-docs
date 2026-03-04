# SQL (Relational) Database

## Table of Contents
<!-- TOC -->
* [SQL (Relational) Database](#sql-relational-database)
  * [Table of Contents](#table-of-contents)
  * [Data Structure](#data-structure)
  * [Data Integrity](#data-integrity)
  * [Query Language](#query-language)
  * [Schema](#schema)
  * [Scaling](#scaling)
  * [Examples](#examples)
  * [Ref.](#ref)
<!-- TOC -->

A relational database is a type of database management system (_DBMS_) that organizes and stores data in a structured manner, using a tabular format with rows and columns. In a relational database, _data is organized into tables, and these tables are related to one another through keys or common attributes_. This type of database is based on the principles of the relational model, which was developed by Edgar F. Codd in 1970.


Here are some key characteristics and concepts associated with relational databases:


<sub>[Back to top](#table-of-contents)</sub>

## Data Structure
SQL databases, also known as **Relational Databases**, store data in structured tables with predefined schemas. Each table consists of rows and columns, and data is organized in a tabular format.
    
 - ### Key
    Each table typically has a primary key, which is a unique identifier for each row in the table. Primary keys ensure data integrity and provide a way to uniquely identify and retrieve specific records.

 - ### Relationships
    Tables can be related to each other using keys. The relationships between tables enable the creation of complex and structured data models. The most common types of relationships are one-to-one, one-to-many, and many-to-many.

<sub>[Back to top](#table-of-contents)</sub>

    
## Data Integrity
SQL databases are known for enforcing data integrity through the use of _schemas_, _constraints_, and _relationships_. They support [ACID](../db-concepts/acid.md) (Atomicity, Consistency, Isolation, Durability) transactions, ensuring data consistency.

- See also: [ACID](../db-concepts/acid.md)


<sub>[Back to top](#table-of-contents)</sub>


## Query Language
SQL databases use the SQL language for querying and manipulating data. SQL provides a powerful and standardized way to perform complex queries and joins across multiple tables.

- See also: [SQL]()<!-- TODO: -->


<sub>[Back to top](#table-of-contents)</sub>


## Schema
SQL databases have a fixed schema, meaning the structure of the data (e.g., table columns and data types) is defined in advance.


<sub>[Back to top](#table-of-contents)</sub>

## Scaling
SQL databases typically scale vertically, meaning you can increase the database's capacity by upgrading to a more powerful server.


<sub>[Back to top](#table-of-contents)</sub>


## Examples
Examples of SQL databases include:

- MySQL
- PostgreSQL
- Oracle Database
- Microsoft SQL Server.


___

## Ref.

- https://en.wikipedia.org/wiki/Relational_database#RDBMS

___

[Get Started](../../../get-started.md) |
[SQL](../../../get-started.md#relational-databases--sql-)

---


