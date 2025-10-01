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
