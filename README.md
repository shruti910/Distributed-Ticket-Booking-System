# High-Concurrency Distributed Ticket Reservation System

A robust, distributed microservices ecosystem designed to handle high-traffic ticket sales with absolute data integrity. This project demonstrates advanced architectural patterns for managing distributed transactions and high-concurrency race conditions using a modern, containerized stack.

---

## Architecture & Services

The system is divided into three specialized microservices that maintain a decoupled architecture through asynchronous event-driven communication:

* **Booking Service:** Orchestrates the reservation lifecycle and manages user-facing booking states.
* **Inventory Service:** The source of truth for seat availability, utilizing optimized persistence logic to handle extreme load.
* **Order Service:** Simulates payment orchestration via internal state transitions. It mimics the financial approval/rejection flow to drive the Saga's success or compensation paths without requiring a third-party payment gateway.

---

## Core Technical Capabilities

### Distributed Saga Pattern
The project implements a choreography-based **Saga Pattern** to ensure eventual consistency across distributed databases. Using **Apache Kafka**, the system manages complex transaction flows. The Order Service simulates payment outcomes, triggering automatic "compensating transactions" (rollbacks) to release inventory if a simulated payment failure occurs.

### High-Concurrency Resilience
Engineered for stability during massive traffic spikes. The system employs thread-safe reservation logic at the database level to eliminate race conditions, ensuring zero overselling occurs even under heavy parallel load.

### Containerization & Orchestration
The entire ecosystem is fully **Dockerized**. Using **Docker Compose**, the complex web of microservices, multiple databases, and the Kafka broker can be orchestrated with a single command, ensuring a consistent environment from development to production.

### Performance-Validated
* **Throughput:** Verified at ~325 requests per second on Apple M4 hardware.
* **Reliability:** Successfully processed 39,000+ simultaneous transaction/compensation loops during stress testing with zero data corruption.

---

## 🛠️ Technology Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **Messaging:** Apache Kafka
* **Containerization:** Docker & Docker Compose
* **Database:** Relational Data (MySQL) with Flyway Migrations
* **Tooling:** Maven, Postman Performance Runner

---

## Getting Started

### Prerequisites
* Docker Desktop
* JDK 21
* Maven 3.9+

### Installation & Deployment
1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/shruti910/Distributed-Ticket-Booking-System.git](https://github.com/shruti910/Distributed-Ticket-Booking-System.git)
    ```
2.  **Spin Up Infrastructure:**
    Use the provided Docker Compose file to start Kafka, Zookeeper, and the databases:
    ```bash
    docker-compose up -d
    ```
3.  **Run the Services:**
    Navigate to each service directory and launch via Maven:
    ```bash
    ./mvnw spring-boot:run
    ```

---

## Future Roadmap
- [ ] **Automated Reclaim:** Implementation of a scheduled background task to unlock expired (unpaid) reservations.
- [ ] **Financial Integration:** Transition from simulated payment states to a live Payment Gateway (e.g., Stripe/Braintree).
- [ ] **Observability:** Integration of distributed tracing for cross-service request monitoring.
