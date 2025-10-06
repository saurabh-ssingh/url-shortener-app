# URL Shortener Application

A **Spring Boot** application to shorten URLs, similar to Bitly or TinyURL, with support for:

- Generating unique short URLs
- Expiration of URLs
- Redis caching for fast lookups
- PostgreSQL for persistent storage
- REST API endpoints for URL creation and redirection

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Running with Docker](#running-with-docker)
- [API Endpoints](#api-endpoints)
- [Caching with Redis](#caching-with-redis)
- [Database](#database)
- [Logging](#logging)
- [License](#license)

---

## Features

- Shorten long URLs and get a unique short code
- Redirect short URL to the original URL
- Optional URL expiration date
- Redis caching for fast retrieval
- Checks for existing short URLs and prevents duplicates
- Automatic cleanup of expired URLs

---

## Tech Stack

- **Backend:** Spring Boot 3.x
- **Database:** PostgreSQL
- **Cache:** Redis
- **Build Tool:** Maven
- **Deployment:** Docker, Docker Compose

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose
- Git

### Clone the repository

```bash
git clone git@github.com:your-username/url-shortener-app.git
cd url-shortener-app


Absolutely! Here's a **ready-to-copy `deployment-notes.md`** you can just save in your project and refer to later. It's concise, covers all your steps, and formatted for copy-paste.

````markdown
# TinyURL App - Deployment & Setup Notes

This document contains important commands, configurations, and notes for deploying and running the TinyURL app locally and on AWS EC2 using Docker Compose.

---

## 1. Project Setup

### Build the app
```bash
mvn clean install
````

### Run locally (Spring Boot)

```bash
mvn spring-boot:run
```

---

## 2. Application Properties (Docker-friendly)

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://${SPRING_DATASOURCE_HOST}:${SPRING_DATASOURCE_PORT}/${SPRING_DATASOURCE_DB}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

spring.data.redis.host=${SPRING_REDIS_HOST:localhost}
spring.data.redis.port=${SPRING_REDIS_PORT:6379}
spring.cache.type=redis
spring.redis.database=0

server.port=8080
app.base-url=http://localhost:8080
```

---

## 3. Environment Variables (.env)

Create `.env` in project root:

```env
# PostgreSQL
SPRING_DATASOURCE_HOST=db
SPRING_DATASOURCE_PORT=5432
SPRING_DATASOURCE_DB=urlshortener
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Redis
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
```

> Add `.env` to `.gitignore` to avoid pushing secrets.

---

## 4. Docker Compose

`docker-compose.yml`:

```yaml
version: '3.8'

services:
  db:
    image: postgres:17
    container_name: urlshortener-db
    restart: always
    environment:
      POSTGRES_DB: urlshortener
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - db_data:/var/lib/postgresql/data

  redis:
    image: redis:8.2
    container_name: urlshortener-redis
    restart: always
    ports:
      - "6379:6379"
    command: redis-server --save 60 1 --loglevel warning
    volumes:
      - redis_data:/data

  app:
    build: .
    container_name: urlshortener-app
    restart: always
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_HOST=db
      - SPRING_DATASOURCE_PORT=5432
      - SPRING_DATASOURCE_DB=urlshortener
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
    depends_on:
      - db
      - redis

volumes:
  db_data:
  redis_data:
```

---

## 5. Docker Commands

### Build and start

```bash
docker-compose up --build
```

### Stop

```bash
docker-compose down
```

### Logs

```bash
docker logs urlshortener-app
docker logs urlshortener-redis
```

### Test Redis connectivity

```bash
docker exec -it urlshortener-app nc -zv redis 6379
docker exec -it urlshortener-app ping redis
```

---

## 6. AWS EC2 Deployment

### Copy project to EC2

```bash
scp -i tinyurl.pem -r ./tinyurler ubuntu@<EC2-Public-IP>:/home/ubuntu/
```

### SSH into EC2

```bash
ssh -i tinyurl.pem ubuntu@<EC2-Public-IP>
```

### Security Group Inbound Rules

| Type       | Protocol | Port Range | Source    | Notes                        |
| ---------- | -------- | ---------- | --------- | ---------------------------- |
| SSH        | TCP      | 22         | My IP     | For SSH access               |
| Custom TCP | TCP      | 8080       | 0.0.0.0/0 | App access                   |
| PostgreSQL | TCP      | 5432       | My IP     | Optional, external DB access |
| Custom TCP | TCP      | 6379       | —         | Keep Redis private           |

---

## 7. Notes

* Spring Boot 3 uses `spring.data.redis.host` and `spring.data.redis.port` instead of the old `spring.redis.host`.
* Use `depends_on` in Docker Compose to ensure containers start in the correct order.
* Use `.env` for local dev and Docker environment variable substitution.
* Always verify Redis and Postgres connectivity from the app container before running.

