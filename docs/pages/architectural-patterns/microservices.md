# Microservices Architecture

## Table of Contents
<!-- TOC -->
* [Microservices Architecture](#microservices-architecture)
  * [Table of Contents](#table-of-contents)
  * [Characteristics and Principles of Microservices](#characteristics-and-principles-of-microservices)
  * [Microservices Patterns](#microservices-patterns)
  * [Challenges of Microservices Implementation](#challenges-of-microservices-implementation)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---


Microservices architecture is a software design approach that _structures an application as a collection of small, loosely coupled, and independently deployable services, each focused on performing a specific business capability_. In a microservices architecture, a large and complex software system is broken down into smaller, manageable services that work together to deliver the overall functionality of the application. 

## Characteristics and Principles of Microservices

- ### Service Decoupling: 
    Microservices are designed to be independent of each other, with minimal dependencies_. Each service can be developed, deployed, and scaled independently. This decoupling allows teams to work on different services without affecting the entire system.

<sub>[Back to top](#table-of-contents)</sub>

- ### Single Responsibility: 
    Each microservice has a well-defined and narrow scope of responsibility. It is responsible for a specific business function or capability, such as user authentication, order processing, or product catalog management.

<sub>[Back to top](#table-of-contents)</sub>

- ### Technology Diversity:
    Microservices architecture permits the use of different technologies, programming languages, and databases for each service. Teams can choose the most suitable technology stack for their specific service's requirements.

<sub>[Back to top](#table-of-contents)</sub>

- ### APIs and Communication:
    Microservices communicate with each other through well-defined APIs, often using lightweight protocols like HTTP/REST or message queues. This enables them to exchange data and collaborate on tasks. 
  - See also: [RESTful](../ws-and-api-design/restful.md)
     

<sub>[Back to top](#table-of-contents)</sub>

- ### Independent Deployment:
    Microservices can be deployed independently of each other. This means that updates, bug fixes, and new features can be rolled out to one service without affecting the entire system.

<sub>[Back to top](#table-of-contents)</sub>

- ### Scalability:
    Each microservice can be scaled independently based on its workload and performance requirements. This allows for efficient resource allocation and the ability to handle varying levels of demand.

<sub>[Back to top](#table-of-contents)</sub>

- ### Resilience and Fault Tolerance:
    Microservices are designed with resilience in mind. They are expected to handle failures gracefully, and fault tolerance mechanisms, such as circuit breakers and retries, are often implemented.

<sub>[Back to top](#table-of-contents)</sub>

- ### Data Management:
    Data in microservices is typically managed by each service's own database. This supports data isolation and ensures that each service can choose the most suitable database technology.

<sub>[Back to top](#table-of-contents)</sub>

- ### Continuous Integration and Deployment (CI/CD):
    Microservices encourage automated testing, continuous integration, and continuous deployment practices. This helps ensure that changes are tested and deployed rapidly.

<sub>[Back to top](#table-of-contents)</sub>

- ### Observability: 
    Monitoring, logging, and tracing are essential in a microservices architecture. These practices enable teams to gain insights into the behavior of individual services and the system as a whole.

<sub>[Back to top](#table-of-contents)</sub>

- ### Organizational Alignment:
    Microservices often align with DevOps and agile development practices. Cross-functional teams take ownership of individual microservices, which promotes rapid development and deployment cycles.

<sub>[Back to top](#table-of-contents)</sub>

- ### Versioning:
    Microservices may have multiple versions running concurrently. API versioning and backward compatibility are important considerations to avoid breaking client applications.

<sub>[Back to top](#table-of-contents)</sub>

- ### Security:
    Security measures like authentication, authorization, and encryption are enforced at both the microservice and API gateway levels to protect the system from threats.


<sub>[Back to top](#table-of-contents)</sub>
    

## Microservices Patterns
There are several common microservices patterns that organizations use to design and implement microservices-based systems. Here are some of the most common microservices patterns:

- ### API Gateway:
  In this pattern, an API Gateway serves as a _single entry point_ for clients to interact with various microservices. It routes requests to the appropriate microservice, aggregates responses if needed, and handles common cross-cutting concerns like authentication and authorization.

<sub>[Back to top](#table-of-contents)</sub>

    
- ### Service Discovery:
  Microservices need a way to discover and communicate with each other dynamically. Service discovery patterns involve tools like **Netflix Eureka** or **Consul** to register and look up service instances.

<sub>[Back to top](#table-of-contents)</sub>
    
- ### Load Balancing:
  Load balancing ensures that _incoming requests are distributed evenly_ among multiple instances of a microservice to improve performance and fault tolerance.

<sub>[Back to top](#table-of-contents)</sub>

    
- ### Circuit Breaker:
  This pattern helps to _prevent cascading failures in a microservices_ system. When a service encounters repeated failures, the circuit breaker "opens" and prevents further requests to that service until it "closes" again, indicating that the service has recovered.


<sub>[Back to top](#table-of-contents)</sub>
  
- ### Database per Service: 
  _Each microservice has its own database_, which allows teams to choose the most appropriate database technology for their service. This pattern supports independent data management and scaling.


<sub>[Back to top](#table-of-contents)</sub>
  
- ### Asynchronous Messaging:
  Microservices can _communicate through asynchronous messaging systems_ like **RabbitMQ** or **Apache Kafka**. This pattern is useful for decoupling services and handling tasks that can be processed in the background.
  
  - See also: [Message Driven]()<!-- TODO: -->
  

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Saga Pattern: 
  In long-running business processes that span multiple microservices (distributed transactions) , the saga pattern helps maintain data consistency. It orchestrates a series of local transactions within each service and compensating actions if a step fails.

  - **Choreography**: Each service publishes events to trigger actions in other services

  - **Orchestration**: , A central component coordinates the actions of multiple services
  
  
  - See also: [Distributed Transactions]()<!-- TODO: -->

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Event Sourcing:
  Instead of storing current state, microservices using event sourcing _store all changes to the state as a sequence of events_. This allows for easy reconstruction of the current state and auditing capabilities.


  - See also: [Event Sourcing]()<!-- TODO: -->
  

  <sub>[Back to top](#table-of-contents)</sub>
  

- ### Polyglot Microservices: 
  This pattern allows each microservice to be implemented using the most suitable programming language and technology stack for its specific functionality, promoting flexibility and specialization.


  <sub>[Back to top](#table-of-contents)</sub>
  
- ### Immutable Deployments: 
  Instead of updating microservice instances in place, new versions of microservices are _deployed as immutable containers or instances_. This ensures that old and new versions do not interfere with each other.


<sub>[Back to top](#table-of-contents)</sub>
  
- ### API Composition: 
  Sometimes, a client needs data from multiple microservices. API composition involves _aggregating data from multiple microservices into a single response_ to reduce the number of client requests.


<sub>[Back to top](#table-of-contents)</sub>

- ### Backend for Frontend (BFF):
  BFF is a design pattern where a dedicated backend service is created specifically for a frontend application or a group of related frontend applications. The purpose is to act as an intermediary between the frontend and the various backend services or APIs, is responsible for aggregating data, handling business logic, and optimizing API requests to suit the needs of the frontend. It provides a more tailored and efficient API for the frontend while keeping the frontend code clean and focused.  

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Shared Data Patterns:
  In situations where data sharing is necessary, patterns like the "Strangler Fig" pattern or an API for shared data can be used to gradually replace monolithic data stores.


  <sub>[Back to top](#table-of-contents)</sub>
  
- ### Observability and Monitoring: 
  Implementing robust observability and monitoring solutions, including logging, tracing, and metrics, is crucial to understanding and debugging microservices-based systems.


  <sub>[Back to top](#table-of-contents)</sub>
  
## Challenges of Microservices Implementation

- ### Initial Rollout
The initial rollout of a distributed system built using microservices architecture can be challenging and potentially painful. While microservices are designed to be independently developed and deployed, the process of transitioning from a monolithic architecture to a microservices architecture or starting a new project with microservices can present several initial challenges

<sub>[Back to top](#table-of-contents)</sub>

- ### Service Dependencies: 
As the number of microservices grows, managing dependencies between services becomes increasingly complex. Changes in one service can impact others, making versioning, backward compatibility, and communication protocols critical.

<sub>[Back to top](#table-of-contents)</sub>

- ### Data Management: 
Handling data in a microservices architecture involves challenges related to data consistency, synchronization, and maintaining a clear data ownership model. Deciding whether to use microservices for data or adopt a separate data store can be an ongoing debate.

<sub>[Back to top](#table-of-contents)</sub>

- ### Testing and Quality Assurance:
  Testing a distributed system with multiple microservices can be challenging. Developing effective strategies for unit testing, integration testing, and end-to-end testing is essential to maintain system reliability.

<sub>[Back to top](#table-of-contents)</sub>
  
-  ### Deployment and Continuous Delivery:
    Managing the deployment of numerous microservices requires robust automation and orchestration tools. Implementing continuous integration and continuous delivery (CI/CD) pipelines for each service is crucial for rapid and reliable deployments.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Monitoring and Observability:

  Continuously monitoring the health, performance, and security of individual microservices and the overall system is vital. Implementing effective logging, tracing, and monitoring solutions can be an ongoing effort.

  <sub>[Back to top](#table-of-contents)</sub>
  
- ### Scalability and Resource Management:
  Scaling microservices horizontally to meet changing demand requires careful resource management. Balancing the allocation of resources (e.g., CPU, memory) and ensuring cost-effective scaling can be challenging.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Security and Access Control:
  Securing microservices and managing access control across services are ongoing concerns. Regularly updating security practices, monitoring for vulnerabilities, and staying vigilant against threats are essential.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Documentation and Communication:
  Maintaining up-to-date documentation for each microservice and ensuring clear communication between development teams are vital to prevent misunderstandings and misalignments.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Technical Debt:
  Over time, as services evolve and requirements change, technical debt can accumulate. Identifying and addressing technical debt regularly is crucial to prevent future development bottlenecks.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Versioning and Backward Compatibility:
  Managing API versioning and backward compatibility as services evolve is an ongoing challenge. Proper versioning strategies and communication between teams are essential.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Team Coordination:
  Effective coordination and collaboration among cross-functional teams that own different microservices are necessary to prevent silos and ensure the overall system's success.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Resilience and Failover:
  Ensuring that the system remains resilient to failures and that failover mechanisms work as expected requires ongoing testing and maintenance.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Cost Optimization:
  As the system scales, managing costs becomes increasingly important. Regularly reviewing infrastructure and resource usage can help optimize costs.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Cultural Shift:
  Transitioning to a microservices culture often requires a shift in mindset and practices. Encouraging a culture of ownership, accountability, and continuous improvement can be an ongoing challenge.

<sub>[Back to top](#table-of-contents)</sub>
  
- ### Regulatory Compliance:
  Complying with regulatory requirements, especially in industries like finance or healthcare, may require continuous monitoring and adaptation of microservices to meet changing compliance standards.

<sub>[Back to top](#table-of-contents)</sub>

___

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How does the Database per Service pattern support independent scaling, and what problem does it create for reporting or cross-service queries?**
A: Because each service owns its own database, it can pick the best-fit storage technology and scale that store independently of others. The trade-off is that a query spanning multiple services can no longer be a simple join — it requires patterns like API Composition or event-driven data replication to assemble a combined view.

---

**Q: What's the difference between choreography and orchestration in the Saga pattern?**
A: In choreography, each service publishes events and reacts to events published by others, with no central coordinator. In orchestration, a central component explicitly directs each step of the distributed transaction and issues compensating actions if a step fails.

---

**Q: Why are API Gateway and Circuit Breaker commonly paired in a microservices system?**
A: The API Gateway is the single entry point routing client requests to backend services, so a failing downstream service can otherwise cascade failures back through the gateway to every client. Pairing it with a Circuit Breaker lets the gateway (or the calling service) stop sending requests to a failing service once failures repeat, protecting the rest of the system until that service recovers.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Event-Driven Architecture](message-driven/event-driven.md) — asynchronous messaging is the common pattern used to decouple microservice communication
- [Reactive Systems](reactive.md) — reactive principles (resilience, elasticity) are frequently applied to individual microservices
- [RESTful API Design](../ws-and-api-design/restful.md) — the synchronous HTTP/REST protocol most commonly used for microservice APIs

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://microservices.io/
- https://dzone.com/microservices
- https://martinfowler.com/
- https://netflixtechblog.com/

___

[Get Started](../../get-started.md) |
[Architectural Patterns](../../get-started.md#architectural-patterns)

---