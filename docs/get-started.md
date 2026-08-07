# Tech Docs

---

<input type="text" id="topic-filter" placeholder="Filter topics… (e.g. kafka, oauth, singleton)" style="width:100%;box-sizing:border-box;padding:0.5em;font-size:1em;margin-bottom:1em;">
<script>
document.addEventListener('DOMContentLoaded', function () {
  var input = document.getElementById('topic-filter');
  if (!input) return;
  // Every top-level <li> in the content body, walking nested sub-lists too.
  var items = document.querySelectorAll('h1 ~ ul li, h1 ~ ol li');
  input.addEventListener('input', function () {
    var term = input.value.trim().toLowerCase();
    items.forEach(function (li) {
      if (!term) {
        li.style.display = '';
        return;
      }
      var ownText = li.childNodes.length ? li.textContent.toLowerCase() : '';
      var matches = ownText.indexOf(term) !== -1;
      li.style.display = matches ? '' : 'none';
    });
    if (!term) return;
    // Keep ancestors of a match visible even if the ancestor's own text doesn't match.
    items.forEach(function (li) {
      if (li.style.display !== 'none') {
        var parentLi = li.parentElement && li.parentElement.closest('li');
        while (parentLi) {
          parentLi.style.display = '';
          parentLi = parentLi.parentElement && parentLi.parentElement.closest('li');
        }
      }
    });
  });
});
</script>

## Table of Contents

<!-- TOC -->
* [Tech Docs](#tech-docs)
  * [Table of Contents](#table-of-contents)
  * [Programming](#programming)
    * [Languages](#languages)
    * [Paradigms](#paradigms)
  * [Web Development Frameworks](#web-development-frameworks)
    * [Backend](#backend)
    * [Frontend](#frontend)
  * [Data Processing](#data-processing)
    * [Database Concepts](#database-concepts)
    * [Relational Databases (SQL)](#relational-databases--sql-)
    * [NoSQL Databases](#nosql-databases)
    * [Real-Time Processing](#real-time-processing)
      * [Event Streaming](#event-streaming)
  * [Networking Concepts](#networking-concepts)
  * [Cloud Computing Platform](#cloud-computing-platform)
  * [DevOps Practices](#devops-practices)
  * [Data Structures](#data-structures)
  * [Algorithms](#algorithms)
  * [Cyber-security Fundamentals](#cyber-security-fundamentals)
  * [Design Patterns](#design-patterns)
  * [Architectural Patterns](#architectural-patterns)
  * [Web Services and API Design](#web-services-and-api-design)
<!-- TOC -->

---

## Programming
### Languages
  - [JavaScript (Node.js)](pages/programming/languages/javascript.md)
  - [Python](pages/programming/languages/python.md)
  - [Java](pages/programming/languages/java/java.md)
    - [Java Development](pages/programming/languages/java/develop.md)
    - [Main Java Version Changes](pages/programming/languages/java/versions.md)
  - [PHP](pages/programming/languages/php.md)
  - [Ruby](pages/programming/languages/ruby.md)
  - [C#](pages/programming/languages/csharp.md)
  - [Go](pages/programming/languages/go.md)
  - [Rust](pages/programming/languages/rust.md)
  - [Kotlin](pages/programming/languages/kotlin.md)
  - [Clojure](pages/programming/languages/clojure.md)
  - [Swift](pages/programming/languages/swift.md)
  - [HTML](pages/programming/languages/html-css.md#html)
  - [CSS](pages/programming/languages/html-css.md#css)
  - [TypeScript](pages/programming/languages/typescript.md)

### Paradigms
- [Imperative Programming](pages/programming/paradigms/imperative.md)
- [Procedural Programming](pages/programming/paradigms/procedural.md)
- [Structured Programming](pages/programming/paradigms/structured.md)
- [Object Oriented Programming (OOP)](pages/programming/paradigms/object-oriented.md)
- [Declarative Programming](pages/programming/paradigms/declarative.md)
- [Functional Programming](pages/programming/paradigms/functional.md)
- Event-Driven Programming
- [Reactive Programming](pages/programming/paradigms/reactive.md)
- [Concurrent Programming](pages/programming/paradigms/concurrent.md)
- Aspect-Oriented Programming (AOP)
- Logic Programming
- Domain-Specific Languages (DSLs)

<sub>[Back to top](#table-of-contents)</sub>

## Web Development Frameworks
### Backend
- [Spring](pages/frameworks/backend/spring.md)
  - [Spring Cloud](pages/frameworks/backend/spring.md#spring-cloud)
  - [Spring Data](pages/frameworks/backend/spring.md#spring-data)
  - [Spring Security](pages/frameworks/backend/spring.md#spring-security)
  - [SpringBoot](pages/frameworks/backend/spring.md#spring-boot)
- [Quarkus](pages/frameworks/backend/quarkus.md)
- [Express.js](pages/frameworks/backend/expressjs.md)
- [Nest.js](pages/frameworks/backend/nestjs.md)

### Frontend
- [React](pages/frameworks/frontend/react.md)
- [Angular](pages/frameworks/frontend/angular.md)
- [Vue.js](pages/frameworks/frontend/vuejs.md)
- [Sass](pages/frameworks/frontend/frontend-styling-tooling.md#sass)
- [LESS](pages/frameworks/frontend/frontend-styling-tooling.md#less)
- [Bootstrap](pages/frameworks/frontend/frontend-styling-tooling.md#bootstrap)

<sub>[Back to top](#table-of-contents)</sub>


## Data Processing

- Batch Processing
- [Real-Time Processing](#real-time-processing)
- ETL (Extract, Transform, Load)
- [SQL Databases](#relational-databases--sql-)
- Data Warehousing
- In-Memory Processing
- Data Integration
- [NoSQL Databases](#nosql-databases)
- MapReduce
- Machine Learning and AI
- Data Cleaning and Transformation
- Data Lakes
- Text and Natural Language Processing (NLP)
- Image and Video Processing
- Time Series Analysis

<sub>[Back to top](#table-of-contents)</sub>

### Database Concepts
- [ACID](pages/data-processing/db-concepts/acid.md)
- [BASE](pages/data-processing/db-concepts/base.md)
- [CAP Theorem](pages/data-processing/db-concepts/cap.md)
- CRDTs
- Snapshot Isolation
- Two-Phase Commit (2PC)
- Eventual Consistency
- MVCC (Multi-Version Concurrency Control)
- Read Committed Isolation
- Distributed Databases

<sub>[Back to top](#table-of-contents)</sub>

### [Relational Databases (SQL)](pages/data-processing/sql/relational.md)
  - MySQL
  - PostgreSQL
  - Microsoft SQL Server
  - Oracle Database
  - SQLite
  - IBM Db2
  - MariaDB
  - Amazon RDS (Relational Database Service)
  - Google Cloud SQL
  - Azure SQL Database

<sub>[Back to top](#table-of-contents)</sub>

### [NoSQL Databases](pages/data-processing/nosql/nosql.md)
  - MongoDB
  - Cassandra
  - Couchbase
  - Redis
  - Amazon DynamoDB
  - Apache HBase
  - Apache CouchDB
  - Neo4j
  - Elasticsearch
  - Amazon DocumentDB

<sub>[Back to top](#table-of-contents)</sub>

### Real-Time Processing
  #### [Event Streaming](pages/data-processing/real-time/event-streaming.md)
  - [Apache Kafka](pages/data-processing/real-time/event-streaming/kafka.md)
  - Apache Pulsar
  - RabbitMQ
  - Amazon Kinesis
  - Google Cloud Pub/Sub
  - NATS
  - Apache ActiveMQ
  - Redis Streams
  - IBM MQ
  - Microsoft Azure Event Hubs

<sub>[Back to top](#table-of-contents)</sub>

## Networking Concepts
- [IP Addressing](pages/networking/ip-addressing.md)
- [Subnetting](pages/networking/ip-addressing.md#subnetting)
- [Routing](pages/networking/routing-switching.md#routing)
- [Switching](pages/networking/routing-switching.md#switching)
- [TCP/IP](pages/networking/tcp-ip.md)
- [DNS (Domain Name System)](pages/networking/dns.md)
- [DHCP (Dynamic Host Configuration Protocol)](pages/networking/ip-addressing.md#dhcp)
- [Firewall](pages/networking/firewall-vpn.md#firewall)
- [VPN (Virtual Private Network)](pages/networking/firewall-vpn.md#vpn-virtual-private-network)
- [OSI Model (Open Systems Interconnection Model)](pages/networking/osi-model.md)
- [NAT (Network Address Translation)](pages/networking/ip-addressing.md#nat-network-address-translation)
- [VLAN (Virtual Local Area Network)](pages/networking/routing-switching.md#vlan-virtual-local-area-network)
- [Load Balancing](pages/networking/load-balancing.md)
- [Bandwidth](pages/networking/bandwidth-latency.md#bandwidth)
- [Latency](pages/networking/bandwidth-latency.md#latency)

<sub>[Back to top](#table-of-contents)</sub>

## Cloud Computing Platform
- [Cloud Computing Concepts](pages/cloud-computing/cloud-computing-concepts.md) — service models, deployment models, shared responsibility
- [Amazon Web Services (AWS)](pages/cloud-computing/aws-azure-gcp.md)
- [Microsoft Azure](pages/cloud-computing/aws-azure-gcp.md)
- [Google Cloud Platform (GCP)](pages/cloud-computing/aws-azure-gcp.md)
- [IBM Cloud](pages/cloud-computing/other-cloud-providers.md#ibm-cloud)
- [Alibaba Cloud](pages/cloud-computing/other-cloud-providers.md#alibaba-cloud)
- [Oracle Cloud Infrastructure (OCI)](pages/cloud-computing/other-cloud-providers.md#oracle-cloud-infrastructure-oci)
- [Salesforce Cloud](pages/cloud-computing/other-cloud-providers.md#salesforce-cloud)
- [DigitalOcean](pages/cloud-computing/other-cloud-providers.md#digitalocean)
- [VMware Cloud](pages/cloud-computing/other-cloud-providers.md#vmware-cloud)
- [Rackspace](pages/cloud-computing/other-cloud-providers.md#rackspace)

<sub>[Back to top](#table-of-contents)</sub>

## DevOps Practices
- [Continuous Integration (CI)](pages/devops/ci-cd.md#continuous-integration-ci)
- [Continuous Delivery (CD)](pages/devops/ci-cd.md#continuous-delivery-cd)
- [Infrastructure as Code (IaC)](pages/devops/infrastructure-as-code.md#infrastructure-as-code-iac)
- [Configuration Management](pages/devops/infrastructure-as-code.md#configuration-management)
- [Continuous Deployment](pages/devops/ci-cd.md#continuous-deployment)
- [Continuous Monitoring](pages/devops/devops-culture-practices.md#continuous-monitoring)
- [Agile Development](pages/devops/devops-culture-practices.md#agile-development)
- [Automated Testing](pages/devops/devops-culture-practices.md#automated-testing)
- [Collaboration and Communication](pages/devops/devops-culture-practices.md#collaboration-and-communication)
- [Version Control](pages/devops/devops-culture-practices.md#version-control)
- [DevOps Culture](pages/devops/devops-culture-practices.md#devops-culture)

<sub>[Back to top](#table-of-contents)</sub>

## Data Structures
- [Array](pages/data-structures/linear-structures.md#array)
- [Linked List](pages/data-structures/linear-structures.md#linked-list)
- [Stack](pages/data-structures/linear-structures.md#stack)
- [Queue](pages/data-structures/linear-structures.md#queue)
- [Binary Tree](pages/data-structures/trees.md#binary-tree)
- [Hash Table](pages/data-structures/hash-table.md)
- [Heap](pages/data-structures/heap.md)
- [Graph](pages/data-structures/graph.md)
- [Trie](pages/data-structures/trees.md#trie)
- [AVL Tree](pages/data-structures/trees.md#avl-tree)

<sub>[Back to top](#table-of-contents)</sub>

## Algorithms
- [Sorting Algorithms](pages/algorithms/sorting-algorithms.md)
  - [Bubble Sort](pages/algorithms/sorting-algorithms.md#bubble-sort)
  - [Insertion Sort](pages/algorithms/sorting-algorithms.md#insertion-sort)
  - [Merge Sort](pages/algorithms/sorting-algorithms.md#merge-sort)
  - [Quick Sort](pages/algorithms/sorting-algorithms.md#quick-sort)
- [Searching Algorithms](pages/algorithms/searching-algorithms.md)
  - [Linear Search](pages/algorithms/searching-algorithms.md#linear-search)
  - [Binary Search](pages/algorithms/searching-algorithms.md#binary-search)
- [Graph Algorithms](pages/algorithms/graph-algorithms.md)
  - [Depth-First Search](pages/algorithms/graph-algorithms.md#depth-first-search-dfs)
  - [Breadth-First Search](pages/algorithms/graph-algorithms.md#breadth-first-search-bfs)
  - [Dijkstra's Algorithm](pages/algorithms/graph-algorithms.md#dijkstras-algorithm)
- [Dynamic Programming](pages/algorithms/dynamic-programming-greedy.md#dynamic-programming)
- [Greedy Algorithms](pages/algorithms/dynamic-programming-greedy.md#greedy-algorithms)
- [Divide and Conquer Algorithms](pages/algorithms/divide-and-conquer-backtracking.md#divide-and-conquer)
- [Backtracking Algorithms](pages/algorithms/divide-and-conquer-backtracking.md#backtracking)
- [String Matching Algorithms](pages/algorithms/string-matching.md)
  - [Knuth-Morris-Pratt Algorithm](pages/algorithms/string-matching.md#knuth-morris-pratt-kmp-algorithm)
  - [Rabin-Karp Algorithm](pages/algorithms/string-matching.md#rabin-karp-algorithm)
- [Tree Traversal Algorithms](pages/algorithms/tree-traversal.md)
  - [Inorder](pages/algorithms/tree-traversal.md#inorder-traversal)
  - [Preorder](pages/algorithms/tree-traversal.md#preorder-traversal)
  - [Postorder](pages/algorithms/tree-traversal.md#postorder-traversal)
- [Hashing Algorithms](pages/algorithms/hashing-algorithms.md)
  - [SHA-1](pages/algorithms/hashing-algorithms.md#sha-1)
  - [MD5](pages/algorithms/hashing-algorithms.md#md5)

<sub>[Back to top](#table-of-contents)</sub>

## Cyber-security Fundamentals
- [Network Security](pages/cyber-security/network-and-information-security.md#network-security)
- [Information Security](pages/cyber-security/network-and-information-security.md#information-security)
- [Cryptography](pages/cyber-security/cryptography.md)
- [Secure Coding Practices](pages/cyber-security/secure-coding-practices.md)
- [Risk Management](pages/cyber-security/risk-governance-compliance.md#risk-management)
- [Security Governance and Compliance](pages/cyber-security/risk-governance-compliance.md#security-governance-and-compliance)
- Access Control and Authentication
  - [Identity and Access Management (IAM)](pages/cyber-security/access-control-and-authn/iam.md)
- [Incident Response and Management](pages/cyber-security/security-operations-response.md#incident-response-and-management)
- [Security Awareness and Training](pages/cyber-security/security-operations-response.md#security-awareness-and-training)
- [Threat Intelligence and Analysis](pages/cyber-security/security-operations-response.md#threat-intelligence-and-analysis)
- [Vulnerability Assessment and Penetration Testing](pages/cyber-security/vulnerability-assessment-pentesting.md)
- [Web Application Security](pages/cyber-security/web-application-security.md)
- [Security Operations and Monitoring](pages/cyber-security/security-operations-response.md#security-operations-and-monitoring)
- [Security Architecture and Design](pages/cyber-security/security-architecture-design.md)
- [Data Privacy and Protection](pages/cyber-security/data-privacy-protection.md)

<sub>[Back to top](#table-of-contents)</sub>

## Design Patterns
- Creational Patterns:
  - [Factory Patterns](pages/design-patterns/creational/factory.md)
    - [Simple Factory](pages/design-patterns/creational/factory/simple-factory.md)
    - [Factory Method](pages/design-patterns/creational/factory/factory-method.md)
    - [Abstract Factory](pages/design-patterns/creational/factory/abstract-factory.md)
  - [Singleton](pages/design-patterns/creational/singleton.md)
  - [Builder](pages/design-patterns/creational/builder.md)
  - [Prototype](pages/design-patterns/creational/prototype.md)
- Structural Patterns:
  - [Adapter](pages/design-patterns/structural/adapter.md)
  - [Decorator](pages/design-patterns/structural/decorator.md)
  - [Proxy](pages/design-patterns/structural/proxy.md)
  - [Composite](pages/design-patterns/structural/composite.md)
  - [Facade](pages/design-patterns/structural/facade.md)
  - [Bridge](pages/design-patterns/structural/bridge.md)
  - [Flyweight](pages/design-patterns/structural/flyweight.md)
- Behavioral Patterns:
  - [Observer](pages/design-patterns/behavioral/observer.md)
  - [Strategy](pages/design-patterns/behavioral/strategy.md)
  - [Template Method](pages/design-patterns/behavioral/template-method.md)
  - [Command](pages/design-patterns/behavioral/command.md)
  - [Iterator](pages/design-patterns/behavioral/iterator.md)
  - [Mediator](pages/design-patterns/behavioral/mediator.md)
  - [State](pages/design-patterns/behavioral/state.md)
  - [Visitor](pages/design-patterns/behavioral/visitor.md)
  - [Chain of Responsibility](pages/design-patterns/behavioral/chain-of-responsibility.md)
  - [Interpreter](pages/design-patterns/behavioral/interpreter.md)
  - [Memento](pages/design-patterns/behavioral/memento.md)
- [SOLID](pages/design-patterns/solid.md)
- Inversion of Control (IoC)
  - [Dependency Injection (DI)](pages/design-patterns/ioc/dependency-injection.md)
  - Template Method Pattern
  - Aspect-Oriented Programming (AOP)
  - [Service Locator](pages/design-patterns/ioc/service-locator.md)

<sub>[Back to top](#table-of-contents)</sub>

## Architectural Patterns
  - [Model-View-Controller (MVC)](pages/architectural-patterns/mvc.md)
  - [Model-View-ViewModel (MVVM)](pages/architectural-patterns/mvvm.md)
  - [Monolithic](pages/architectural-patterns/monolithic.md)
  - [Repository Pattern](pages/architectural-patterns/repository-pattern.md)
  - [Layered](pages/architectural-patterns/layered.md)
  - [Microkernel](pages/architectural-patterns/microkernel.md)
  - [Hexagonal](pages/architectural-patterns/hexagonal.md)
  - [Microservices](pages/architectural-patterns/microservices.md)
    - [Distributed Transaction](pages/architectural-patterns/microservices/distributed-transaction.md)
      - [Saga](pages/architectural-patterns/microservices/distributed-transaction/saga.md)
  - [Message-Driven](pages/architectural-patterns/message-driven.md)
    - [Event-Driven](pages/architectural-patterns/message-driven/event-driven.md)
      - [Event Sourcing](pages/architectural-patterns/message-driven/event-driven/event-sourcing.md)
  - [Reactive Systems](pages/architectural-patterns/reactive.md)
  - [Command and Query Responsibility Segregation (CQRS)](pages/architectural-patterns/cqrs.md)

<sub>[Back to top](#table-of-contents)</sub>

## Web Services and API Design
- [RESTful Architecture](pages/ws-and-api-design/restful.md)
  - [API Design Principles](pages/ws-and-api-design/restful/api-design-principles.md)
  - [Resource Design and Representation](pages/ws-and-api-design/restful/resource-design-representation.md)
  - [RESTful API Design](pages/ws-and-api-design/restful/restful-api-design.md)
- [SOAP](pages/ws-and-api-design/soap-rpc.md#soap)
- [RPC](pages/ws-and-api-design/soap-rpc.md#rpc)
- [gRPC](pages/ws-and-api-design/grpc-graphql.md#grpc)
- [GraphQL](pages/ws-and-api-design/grpc-graphql.md#graphql)
- [HTTP and HTTPS](pages/ws-and-api-design/http-data-formats.md#http-and-https)
- [Data Formats](pages/ws-and-api-design/http-data-formats.md#data-formats)
- [Authentication (AuthN) and Authorization (AuthZ)](pages/ws-and-api-design/authn-and-authz/authn-authz.md)
  - [SSO](img/sso.png)
  - [OAuth](pages/ws-and-api-design/authn-and-authz/oauth.md)
  - [OpenID Connect](pages/ws-and-api-design/authn-and-authz/openid-connect.md)
  - [JSON Web Token (JWT)](pages/ws-and-api-design/authn-and-authz/jwt.md)
  - [SAML](pages/ws-and-api-design/authn-and-authz/saml.md)
  - [Kerberos](pages/ws-and-api-design/authn-and-authz/kerberos-ldap.md#kerberos)
  - [LDAP](pages/ws-and-api-design/authn-and-authz/kerberos-ldap.md#ldap)
- [API Security](pages/ws-and-api-design/api-versioning-security.md#api-security)
- [Error Handling and Validation](pages/ws-and-api-design/error-handling-cors.md#error-handling-and-validation)
- [Documentation and Discovery](pages/ws-and-api-design/api-lifecycle-management.md#documentation-and-discovery)
- [API Versioning](pages/ws-and-api-design/api-versioning-security.md#api-versioning)
- [Performance and Scalability](pages/ws-and-api-design/api-lifecycle-management.md#performance-and-scalability)
- [Testing and Mocking](pages/ws-and-api-design/api-lifecycle-management.md#testing-and-mocking)
- [API Lifecycle Management](pages/ws-and-api-design/api-lifecycle-management.md#api-lifecycle-management-1)
- [Webhooks and Event-Driven Architectures](pages/ws-and-api-design/webhooks-event-driven-apis.md)
- [Cross-Origin Resource Sharing (CORS)](pages/ws-and-api-design/error-handling-cors.md#cross-origin-resource-sharing-cors)
- [API Governance and Maintenance](pages/ws-and-api-design/api-lifecycle-management.md#api-governance-and-maintenance)

<sub>[Back to top](#table-of-contents)</sub>

---

[Read Me](index.md) |
[Back to top](#table-of-contents)

---