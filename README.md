# Pet Adoption System

A full-stack pet adoption and animal welfare platform with a public website and a role-based admin console.

This repository is the Spring Boot backend. Frontend: [animal_front](https://github.com/bowang01/animal_front).

| | |
|---|---|
| Live site | https://animal.bowang.tech |
| Frontend | [animal_front](https://github.com/bowang01/animal_front) |
| Backend | this repo |

## Overview

The API supports a public site (browse pets, apply to adopt, report stray or lost animals, feeding points, rescue stations, donations, forum, activities) and an admin console (JWT + RBAC).

Locally the server listens on `9090`. In production Nginx serves the Vue app and proxies `/api` to this service.

## Features

**Public APIs**

- Animals, adoption applications
- Stray rescue, lost-pet reports, feeding points, rescue stations
- Donations, forum articles, knowledge articles, activities
- Register, login, profile

**Admin APIs**

- JWT login and RBAC menus
- CRUD for animals, users, roles, notices, applications, and content
- File upload with MD5 deduplication (`sys_file` + disk)
- Excel import/export
- ECharts data endpoints

## Tech Stack

| Layer | Stack |
|---|---|
| Backend | Spring Boot 2.6, MyBatis-Plus, MySQL 8, JWT |
| Frontend | Vue 2, Vue Router, Vuex, Element UI, Axios, ECharts |
| Storage | Disk / Docker volume (`FILES_UPLOAD_PATH`) |
| Deploy | Docker, Nginx, GitHub Actions, Hostinger Docker Manager |

## Screenshots

Move forward to my portfolio website to check screenshots and test data
https://portfolio.bowang.tech/

## Getting Started

### Requirements

- Java 8
- Maven 3.8+
- MySQL 8
- Database `db_animal` (import your SQL dump; the app does not create tables)

### Config

`src/main/resources/application.yml` defaults:

- Port: `9090`
- MySQL: `root` / `root` at `localhost:3306/db_animal`
- Uploads: `D:/file/` on Windows

Override with env vars: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `FILES_UPLOAD_PATH`, `FILES_ACCESS_BASE_URL`.

### Run

```bash
mvn spring-boot:run
```

API base: `http://localhost:9090`

If you restore a local dump, copy files from `D:/file/` into the upload directory. Production uses `FILES_ACCESS_BASE_URL=/api`.

## Testing

Spring Boot context-load test:

```bash
mvn test
```

There is no broader automated suite yet. Manual checks:

1. `GET` an animal list and open a file URL
2. Upload an image to `/file/upload` and download it from `/file/{uuid}`
3. Login, then call an admin-only path with and without a token
4. Confirm production-style URLs use `/api/file/...` when `FILES_ACCESS_BASE_URL=/api`

## Deployment

Hostinger Docker Manager. This repo has `Dockerfile`, `docker-compose.yml`, and `.github/workflows/deploy.yml`.

```
User
  -> existing Nginx (80/443)  animal.bowang.tech
       -> 127.0.0.1:18080  animal-front
            /        static Vue build
            /api/    host.docker.internal:19090  this service
```

| Service | Host port | Notes |
|---|---|---|
| `animal-server` | `127.0.0.1:19090` | App port 9090 inside the container |
| `animal-mysql` | internal only | Volume `animal-mysql-data` |
| Uploads | volume `animal-uploads` | `/data/uploads/` |

Push `master` to deploy.

Host Nginx example: `deploy/nginx-host-mapping.conf`  
Map `animal.bowang.tech` to `127.0.0.1:18080` (frontend). Do not bind host 80/443 on these containers.

## Live Demo

https://animal.bowang.tech
