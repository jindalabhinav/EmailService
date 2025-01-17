# Email Service with Kafka Integration

This project implements an email service using Spring Boot and Apache Kafka for message processing. The service consumes email messages from a Kafka topic and sends them using JavaMail API.

## Technologies Used

- Java 17
- Spring Boot 3.4.1
- Apache Kafka
- Project Lombok
- JavaMail API
- Docker (for Kafka and ZooKeeper)
- Maven

## Project Structure

- `EmailServiceApplication.java`: Main Spring Boot application entry point
- `EmailConsumer.java`: Kafka consumer that listens to the "sendEmail" topic
- `EmailFormat.java`: DTO class for email message structure
- `EmailUtil.java`: Utility class for sending emails
- `application.properties`: Application configuration

## Features

- Kafka message consumption for email processing
- Email sending functionality using JavaMail API
- JSON serialization support for email messages
- Spring Boot configuration for easy setup and deployment

## Prerequisites

1. Java 17 or higher
2. Maven
3. Docker

## Setup Instructions

### 1. Setup Kafka Environment

Create a Docker network and run Kafka containers:

```bash
# Create a network for Kafka
docker network create kafka-network

# Start ZooKeeper
docker run -d --name zookeeper --network kafka-network \
    -p 2181:2181 \
    -e ZOOKEEPER_CLIENT_PORT=2181 \
    confluentinc/cp-zookeeper:latest

# Start Kafka
docker run -d --name kafka --network kafka-network \
    -p 9092:9092 \
    -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
    -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT \
    -e KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
    -e KAFKA_BROKER_ID=1 \
    confluentinc/cp-kafka:latest
```

### 2. Build and Run the Application

1. Clone the repository
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8082.

## Usage

The service listens to the Kafka topic "sendEmail" for email messages. Messages should be in JSON format with the following structure:

```json
{
    "to": "recipient@example.com",
    "from": "sender@example.com",
    "subject": "Email Subject",
    "content": "Email Content"
}
```

## Configuration

The application can be configured through `application.properties`. Key configurations include:
- Server port: 8082
- Application name: EmailService
- Kafka consumer configurations
- Email server settings