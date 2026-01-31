# BizCore Platform

A modern, enterprise-grade platform for building multi-tenant business applications.

## Tech Stack

| Backend | Frontend | Database |
|---------|----------|----------|
| Java 17+ | React 18 | PostgreSQL 15+ |
| Spring Boot 3.2 | Vite | |
| Spring Security | Bootstrap 5 | |

## Features

- Multi-tenant architecture (company-based data isolation)
- Role-Based Access Control (RBAC) with granular permissions
- JWT authentication
- Multi-language support (English, Russian, Uzbek)
- Modern React admin panel
- Audit logging

## Prerequisites

- Java JDK 17+
- Maven 3.8+
- PostgreSQL 15+
- Node.js 18+ (for frontend development)

## Quick Start

### 1. Clone Repository

```bash
git clone https://github.com/yourusername/bizcore-platform.git
cd bizcore-platform
```

### 2. Setup Database

```bash
cd database

# Windows
setup.bat

# Linux/Mac
chmod +x setup.sh
./setup.sh
```

Or manually:
```bash
psql -U postgres -c "CREATE DATABASE bizcore;"
psql -U postgres -d bizcore -f setup/tables.sql
psql -U postgres -d bizcore -f setup/default-data.sql
psql -U postgres -d bizcore -f setup/translations.sql
```

### 3. Configure Application

Edit `bizcore/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bizcore
    username: postgres
    password: your_password
```

### 4. Run Backend

```bash
cd bizcore
mvn spring-boot:run
```

### 5. Run Frontend (Development)

```bash
cd bizcore/webapp
npm install
npm run dev
```

## Access

| URL | Description |
|-----|-------------|
| http://localhost:5173 | Frontend (dev mode) |
| http://localhost:8091/api | Backend API |

## Default Login

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN (full access) |

**Change the password after first login!**

## Project Structure

```
bizcore-platform/
├── database/
│   ├── setup/
│   │   ├── tables.sql            # Database schema
│   │   ├── default-data.sql      # Default data
│   │   └── translations.sql      # Translations
│   ├── INSTALLATION.md
│   ├── setup.bat
│   └── setup.sh
│
├── bizcore/                       # Backend (Spring Boot)
│   ├── src/main/java/com/bizcore/
│   │   ├── controller/           # REST controllers
│   │   ├── service/              # Business logic
│   │   ├── repository/           # Data access
│   │   ├── entity/               # JPA entities
│   │   └── security/             # Security config
│   └── webapp/                   # Frontend (React)
│       └── src/
│           ├── pages/            # React pages
│           ├── components/       # Components
│           └── hooks/            # Custom hooks
│
└── docs/                         # Documentation
```

## Database Tables

| Table | Description |
|-------|-------------|
| companies | Multi-tenant companies |
| users | User accounts |
| roles | User roles |
| permissions | System permissions |
| role_permissions | Role-permission mapping |
| user_roles | User-role mapping |
| applications | Available applications |
| user_applications | User app access |
| translations | i18n translations |
| audit_logs | Activity audit trail |
| request_logs | HTTP request logging |

## API Endpoints

### Authentication
```
POST /api/auth/login
```

### Users
```
GET    /api/users/company/{companyId}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### Roles
```
GET    /api/roles/company/{companyId}
POST   /api/roles
PUT    /api/roles/{id}
DELETE /api/roles/{id}
```

### Permissions
```
GET /api/permissions
```

### Translations
```
GET /api/translations?lang=en
GET /api/translations?lang=ru&module=users
```

## Development

### Frontend Hot Reload

```bash
cd bizcore/webapp
npm run dev
```

### Build for Production

```bash
cd bizcore
mvn clean package -DskipTests
java -jar target/bizcore-1.0.0.jar
```

## Documentation

- [Database Installation](database/INSTALLATION.md)
- [Translation System](docs/TRANSLATION.md)

## License

MIT License
