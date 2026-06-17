# Kafka Notification System 🚀
A production-style notification system built using **Spring Boot + Kafka** with **Retry Mechanism and Dead Letter Queue (DLQ)**.

---
## 🔥 Features
- Asynchronous processing using Kafka
- Retry mechanism (3 attempts)
- Failure handling with Dead Letter Queue (DLQ)
- Real-time logging of success/failure
- REST API to create notifications

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Apache Kafka
- PostgreSQL / MySQL
- Docker

---

## ⚙️ How It Works

1. User sends notification via REST API
2. Message is published to Kafka topic
3. Consumer processes message:
   - Retry up to 3 times on failure
   - If all retries fail → sent to DLQ
4. Status updated in DB

---

## 📬 API Endpoint

### Create Notification

POST http://localhost:8080/notifications

```json
{
  "message": "Hello Kafka",
  "recipient": "user1",
  "status": "PENDING",
  "type": "EMAIL"
}


## 🔁 Retry Logic Example


Attempt 1 FAILED
Attempt 2 FAILED
Attempt 3 SUCCESS


OR


Attempt 1 FAILED
Attempt 2 FAILED
Attempt 3 FAILED → sent to DLQ


👉 System retries failed messages up to 3 times before moving to Dead Letter Queue.

---

## 🚀 How to Run

1. Start Kafka & Zookeeper (Docker)
2. Run Spring Boot application
3. Use Postman to hit API:

POST http://localhost:8080/notifications

---

## 💡 Why This Project?

This project demonstrates:

- Real-world Kafka event-driven architecture
- Fault-tolerant system design
- Retry and Dead Letter Queue (DLQ) patterns used in production systems
- Asynchronous processing with high reliability
