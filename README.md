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

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client (Frontend)                         │
│                     http://localhost:5173                        │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTP + JWT Bearer Token
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Quarkus REST API  :8080                        │
│                                                                   │
│  ┌──────────────┐  ┌─────────────────┐  ┌──────────────────┐   │
│  │AdminController│  │StudentController│  │LecturerController│   │
│  │  /admin/**   │  │  /student/**    │  │  /lecturer/**    │   │
│  └──────┬───────┘  └────────┬────────┘  └────────┬─────────┘   │
│         │                   │                     │              │
│         └───────────────────┼─────────────────────┘             │
│                             │                                    │
│  ┌──────────────────────────▼──────────────────────────────┐    │
│  │                     Service Layer                         │    │
│  │  StudentService │ LecturerService │ CourseService         │    │
│  │  SubjectService │ TopicService   │ EnrollmentService     │    │
│  │  CourseSubjectService │ StudentProgressService            │    │
│  │  KeycloakService (user lifecycle sync)                   │    │
│  └──────────────────────────┬──────────────────────────────┘    │
│                             │                                    │
│  ┌──────────────────────────▼──────────────────────────────┐    │
│  │                   Repository Layer (Panache)              │    │
│  │  StudentRepo │ LecturerRepo │ CourseRepo │ SubjectRepo   │    │
│  │  TopicRepo │ EnrollmentRepo │ CourseSubjectRepo           │    │
│  │  StudentTopicProgressRepo                                 │    │
│  └──────────────────────────┬──────────────────────────────┘    │
└─────────────────────────────┼───────────────────────────────────┘
                              │
           ┌──────────────────┴──────────────────┐
           │                                      │
           ▼                                      ▼
┌─────────────────────┐              ┌────────────────────────┐
│  PostgreSQL :5432   │              │   Keycloak :8081        │
│                     │              │   Realm: myrealm        │
│  Tables:            │              │                         │
│  - student          │              │  Roles:                 │
│  - lecturer         │              │  - ADMIN                │
│  - course           │              │  - LECTURER             │
│  - subject          │              │  - STUDENT              │
│  - topic            │              │                         │
│  - enrollment       │              │  Client: quarkus-BE     │
│  - course_subject   │              │                         │
│  - student_topic_   │              │                         │
│    progress         │              │                         │
└─────────────────────┘              └────────────────────────┘
```

### Data Model

```
LecturerModel ──< CourseModel >──< EnrollmentModel >── StudentModel
                      │                                      │
                      │ (via course_subject)                 │
                      ▼                                      │
               SubjectModel ──< TopicModel                   │
                                    │                        │
                                    └──< StudentTopicProgressModel >┘
```

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

## API Endpoints

### Admin (`/admin`) — Role: `ADMIN`

#### Students
| Method | Path | Description |
|--------|------|-------------|
| GET | `/admin/students` | List all students |
| POST | `/admin/students` | Create student (syncs to Keycloak) |
| GET | `/admin/student/{id}` | Get student with enrollment details |
| PUT | `/admin/student/{id}` | Update student info |
| DELETE | `/admin/student/{id}` | Soft-deactivate student |
| PUT | `/admin/student/{id}/reactivate` | Reactivate deactivated student |

#### Lecturers
| Method | Path | Description |
|--------|------|-------------|
| GET | `/admin/lecturers` | List all lecturers |
| POST | `/admin/lecturers` | Create lecturer (syncs to Keycloak) |
| GET | `/admin/lecturer/{id}` | Get lecturer with courses/subjects |
| PUT | `/admin/lecturer/{id}` | Update lecturer info |
| DELETE | `/admin/lecturer/{id}` | Soft-deactivate lecturer |
| PUT | `/admin/lecturer/{id}/reactivate` | Reactivate deactivated lecturer |

#### Courses
| Method | Path | Description |
|--------|------|-------------|
| GET | `/admin/courses` | List all courses |
| GET | `/admin/course/{id}` | Get course with enrollment count |

---

### Student (`/student`) — Role: `STUDENT`

#### Profile & Enrollment
| Method | Path | Description |
|--------|------|-------------|
| GET | `/student/students` | List all students |
| GET | `/student/{id}` | Get student details |
| POST | `/student/enroll` | Enroll in a course |
| GET | `/student/enroll/{id}` | Get student's enrollments |
| DELETE | `/student/unenroll/{studentId}/{courseId}` | Unenroll from course |

#### Courses & Content
| Method | Path | Description |
|--------|------|-------------|
| GET | `/student/courses` | Browse all courses |
| GET | `/student/course/{id}` | Get course details |
| GET | `/student/subject/{subjectId}/topics` | Get topics in a subject |

#### Progress
| Method | Path | Description |
|--------|------|-------------|
| POST | `/student/{studentId}/progress/mark` | Mark a topic as completed |
| GET | `/student/{studentId}/progress/subject/{subjectId}` | Progress in a subject |
| GET | `/student/{studentId}/progress/course/{courseId}` | Progress in a course |
| GET | `/student/{studentId}/progress/overall` | Overall progress (all courses) |

---

### Lecturer (`/lecturer`) — Role: `LECTURER`

#### Courses
| Method | Path | Description |
|--------|------|-------------|
| GET | `/lecturer/courses` | List all courses |
| POST | `/lecturer/courses` | Create course |
| GET | `/lecturer/course/{id}` | Get course details |
| PUT | `/lecturer/course/{id}` | Update course |
| DELETE | `/lecturer/course/{id}` | Delete course |

#### Subjects
| Method | Path | Description |
|--------|------|-------------|
| GET | `/lecturer/subjects` | List all subjects |
| POST | `/lecturer/subjects` | Create subject |
| PUT | `/lecturer/subject/{id}` | Update subject |
| DELETE | `/lecturer/subject/{id}` | Delete subject |

#### Topics
| Method | Path | Description |
|--------|------|-------------|
| GET | `/lecturer/subject/{id}/topics` | Get topics in subject |
| POST | `/lecturer/{subjectId}/topics` | Create topic in subject |

#### Curriculum (Course-Subject Binding)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/lecturer/subjecttocourse` | Add subject to course |
| GET | `/lecturer/{courseId}/subjects` | Get subjects in course |
| DELETE | `/lecturer/course/{courseId}/subject/{subjectId}` | Remove subject from course |

---

## Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 14+
- Keycloak 24+ (running on port 8081)

---

## Keycloak Setup

1. Start Keycloak and create a realm named `myrealm`
2. Create a client named `quarkus-BE` with:
   - Client authentication: ON
   - Authorization: OFF
   - Valid redirect URIs: `http://localhost:8080/*`
3. Copy the client secret from the Credentials tab
4. Create three realm roles: `ADMIN`, `LECTURER`, `STUDENT`
5. Create an admin user in Keycloak and note its credentials

---

## Local Development Setup

### 1. Clone the repository

```bash
git clone https://github.com/hasithadil/lms-back.git
cd lms-back
```

### 2. Configure environment variables

```bash
cp .env.example .env
```

Edit `.env` with your actual values:

```env
DB_PASSWORD=your_postgres_password
KEYCLOAK_CLIENT_SECRET=your_keycloak_client_secret
KEYCLOAK_ADMIN_PASSWORD=your_keycloak_admin_password
DEFAULT_USER_PASSWORD=change_me
```

Quarkus picks up `.env` automatically in dev mode.

### 3. Create the PostgreSQL database

```sql
CREATE DATABASE "lms-v3";
```

### 4. Run in development mode

```bash
./mvnw quarkus:dev
```

The API starts at `http://localhost:8080`. Tables are auto-created by Hibernate on first run.

**Swagger UI** (dev mode only): `http://localhost:8080/q/swagger-ui`

---

## Production Build

```bash
# Standard JAR
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar

# Über-JAR (single file)
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar

# Native executable (requires GraalVM)
./mvnw package -Dnative
./target/lms-back-1.0.0-SNAPSHOT-runner
```

Set environment variables before running in production — do not use the defaults.

---

## Docker

Dockerfiles are provided in `src/main/docker/`:

| File | Description |
|------|-------------|
| `Dockerfile.jvm` | Standard JVM image |
| `Dockerfile.legacy-jar` | Legacy über-JAR image |
| `Dockerfile.native` | Native executable image |
| `Dockerfile.native-micro` | Minimal native image |

```bash
# Build and run (JVM mode)
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t lms-back .
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PASSWORD=yourpassword \
  -e KEYCLOAK_URL=http://host.docker.internal:8081 \
  -e KEYCLOAK_CLIENT_SECRET=yoursecret \
  -e KEYCLOAK_ADMIN_PASSWORD=yourpassword \
  lms-back
```

---

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

## License

MIT
