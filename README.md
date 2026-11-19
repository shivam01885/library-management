# Library Management System

A simple library manager with a Spring Boot REST API, a MySQL database, and a React frontend.

## Requirements

- Java
- Maven
- MySQL
- npm

## Database setup

Start MySQL and create the database:

```sql
CREATE DATABASE library_db;
```

Connect database `localhost:3306` as `root` with username & password. Set `DB_USERNAME` and `DB_PASSWORD`.

## Run the backend

```bash
cd backend
mvn spring-boot:run
```

The API is available at `http://localhost:8080/api`.

## Run the frontend

```bash
cd frontend
npm install
npm run dev
```

Open frontend `http://localhost:5173`. And backend at `http://localhost:8080/api`.

## API overview

- `GET`, `POST /api/books`; `GET`, `PUT`, `DELETE /api/books/{id}`
- `GET`, `POST /api/members`; `GET`, `PUT`, `DELETE /api/members/{id}`
- `GET`, `POST /api/issues`; `GET`, `DELETE /api/issues/{id}`
- `PUT /api/issues/{id}/return`
