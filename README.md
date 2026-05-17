# LMS Backend — Learning Management System API

A full-featured REST API backend for a Learning Management System, built with **Quarkus (Java 21)** and **PostgreSQL**. Supports three user roles (Admin, Lecturer, Student) with JWT-based authentication via Keycloak, course/subject/topic management, student enrollment, and granular progress tracking.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Quarkus 3.20.3 |
| Language | Java 21 |
| Database | PostgreSQL |
| ORM | Hibernate ORM with Panache |
| Authentication | Keycloak OIDC + SmallRye JWT |
| Object Mapping | MapStruct |
| Build Tool | Maven |
| API Docs | SmallRye OpenAPI (Swagger UI) |

---



## Features

### Admin
- Create, update, deactivate, and reactivate **student** and **lecturer** accounts
- User accounts are automatically provisioned in Keycloak with the correct role
- View all courses with enrollment counts

### Lecturer
- Create and manage **courses** (with max student capacity)
- Create **subjects** (academic content areas)
- Create **topics** within subjects (individual learning units, ordered)
- Assign subjects to courses to build a curriculum

### Student
- Browse and **enroll** in courses (respects max capacity)
- View subjects and topics within enrolled courses
- **Mark topics as complete** to track learning progress
- View progress at four levels:
  - Per-subject: topics completed / total, percentage
  - Per-course: completion across all subjects in the course
  - Overall: aggregate progress across all enrolled courses

---

## Prerequisites

**Docker (recommended):** Docker + Docker Compose only — no Java or Keycloak installation needed.

**Manual setup:** Java 21+, Maven 3.8+, PostgreSQL 14+, Keycloak 26+

---

## Running with Docker Compose (Recommended)

The fastest way to run the full stack — spins up PostgreSQL, Keycloak (with realm pre-configured), and the API in one command.

```bash
git clone https://github.com/hasithadil/lms-back.git
cd lms-back

# Copy and configure environment
cp .env.example .env
# Edit .env with your passwords and Keycloak client secret

docker-compose up --build
```

Services started:

| Service | URL |
|---------|-----|
| LMS API | `http://localhost:8080` |
| Keycloak Admin UI | `http://localhost:8081` |
| PostgreSQL | `localhost:5432` |

Keycloak is automatically initialised with the `myrealm` realm, `quarkus-BE` client, and `ADMIN`/`LECTURER`/`STUDENT` roles from `keycloak/realm-export.json`.

> **Important:** The default `KEYCLOAK_CLIENT_SECRET` in `realm-export.json` and `.env` is `change-me`. Update both to the same value before running.

Health check endpoint: `http://localhost:8080/q/health`

---

## Local Development Setup (Without Docker)

### 1. Clone the repository

```bash
git clone https://github.com/hasithadil/lms-back.git
cd lms-back
```

### 2. Configure environment variables

```bash
cp .env.example .env
```

Edit `.env` — at minimum set:

```env
DB_PASSWORD=your_postgres_password
KEYCLOAK_CLIENT_SECRET=your_keycloak_client_secret
KEYCLOAK_ADMIN_PASSWORD=your_keycloak_admin_password
```

Quarkus picks up `.env` automatically in dev mode.

### 3. Set up PostgreSQL and Keycloak

```sql
CREATE DATABASE "lms-v3";
```

Start Keycloak 26, then import `keycloak/realm-export.json` via the Admin UI or:

```bash
# Start Keycloak with realm import
docker run -p 8081:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -v ./keycloak/realm-export.json:/opt/keycloak/data/import/realm-export.json \
  quay.io/keycloak/keycloak:26.0 start-dev --import-realm
```


## Project Structure

```
src/main/java/org/university/
├── Controllers/          # REST endpoints (Admin, Student, Lecturer)
├── Services/             # Business logic
├── Repositories/         # Data access (Panache)
├── Models/               # JPA entities
├── Mappers/              # MapStruct entity↔DTO converters
├── dto/                  # Request/response data classes
└── GlobalExceptionHandler.java
```

---

## Authentication Flow

```
1. Admin creates user via POST /admin/students or /admin/lecturers
2. Backend automatically creates the user in Keycloak with the correct role
3. User authenticates directly with Keycloak → receives JWT access token
4. Client sends JWT in Authorization: Bearer <token> header
5. Quarkus validates the token and enforces @RolesAllowed on each endpoint
```

---

