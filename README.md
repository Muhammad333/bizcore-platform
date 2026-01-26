# BizCore Platform

<div align="center">

![BizCore Logo](https://img.shields.io/badge/BizCore-Platform-blue?style=for-the-badge)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue?style=flat-square&logo=react)](https://reactjs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)](LICENSE)

**A modern, enterprise-grade platform for building multi-tenant business applications**

[Features](#features) • [Quick Start](#quick-start) • [Documentation](#documentation) • [Demo](#demo-credentials) • [Contributing](#contributing)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Installation](#-installation)
- [Database Setup](#-database-setup)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [Demo Credentials](#-demo-credentials)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Development](#-development)
- [Deployment](#-deployment)
- [Contributing](#-contributing)

---

## 🎯 Overview

**BizCore Platform** is a comprehensive enterprise application framework that provides a solid foundation for building multi-tenant SaaS applications. It comes with built-in user management, role-based access control (RBAC), multi-language support, and a modern React-based admin interface.

The platform is designed to be a reusable framework that can be extended to build various business applications while maintaining consistent authentication, authorization, and administration capabilities.

### What's Included

- ✅ **Multi-tenant Architecture** - Isolated data per company/organization
- ✅ **Role-Based Access Control (RBAC)** - Fine-grained permission system
- ✅ **JWT Authentication** - Secure token-based authentication
- ✅ **Multi-language Support (i18n)** - English, Russian, Uzbek (easily extensible)
- ✅ **Admin Panel** - Modern React UI for user and role management
- ✅ **REST API** - Well-documented RESTful endpoints
- ✅ **Audit Logging** - Track all system activities and requests
- ✅ **Reference Data** - Pre-loaded countries, currencies, units, categories

---

## ✨ Features

### Core Features

#### 🔐 Authentication & Authorization
- JWT-based authentication with secure tokens
- Role-based access control (RBAC) with granular permissions
- Multi-tenant data isolation
- Password encryption using BCrypt
- Permission-based UI component visibility

#### 👥 User Management
- Complete CRUD operations for users
- User-friendly multi-select for role assignment
- Company-scoped user management
- Email verification support
- User activity tracking

#### 🛡️ Role & Permission Management
- Dynamic role creation and management
- Granular permission assignment
- System-wide and company-specific roles
- Pre-defined roles: Admin, Manager, Employee, Viewer

#### 🌍 Multi-Language Support
- Built-in support for English, Russian, Uzbek
- Database-driven translations
- Easy to add new languages
- Language switcher in UI

#### 📊 Dashboard & Analytics
- Permission-based dashboard visibility
- Quick actions for common tasks
- Activity tracking
- Statistics cards

---

## 🛠️ Tech Stack

### Backend
- **Java 17+** - Programming language
- **Spring Boot 3.2.1** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data access layer
- **PostgreSQL** - Database
- **JWT (jjwt)** - Token-based authentication
- **Maven** - Build tool

### Frontend
- **React 18** - UI library
- **Vite** - Build tool
- **React Router** - Client-side routing
- **Bootstrap 5** - CSS framework
- **Bootstrap Icons** - Icon library

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

| Software | Version | Download Link |
|----------|---------|---------------|
| **Java JDK** | 17 or higher | [Download](https://adoptium.net/) |
| **Maven** | 3.8+ | [Download](https://maven.apache.org/download.cgi) |
| **PostgreSQL** | 15+ | [Download](https://www.postgresql.org/download/) |
| **Git** | Latest | [Download](https://git-scm.com/downloads) |

### Verify Installation

```bash
java -version  # Should be 17 or higher
mvn -version   # Should be 3.8+
psql --version # Should be 15+
```

---

## 🚀 Quick Start

Get up and running in 5 minutes:

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/bizcore-platform.git
cd bizcore-platform

# 2. Create database
psql -U postgres
CREATE DATABASE bizcore_db;
\q

# 3. Run database setup
cd database
psql -U postgres -d bizcore_db -f core/tables.sql
psql -U postgres -d bizcore_db -f references/tables.sql
psql -U postgres -d bizcore_db -f init-database.sql
psql -U postgres -d bizcore_db -f translations.sql

# 4. Configure database (edit application.yml)
cd ../bizcore/src/main/resources
# Update database credentials in application.yml

# 5. Build and run
cd ../../..
mvn clean install
mvn spring-boot:run

# 6. Open browser → http://localhost:8091/app/
# Login: admin / password123
```

---

## 📥 Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/bizcore-platform.git
cd bizcore-platform
```

### Step 2: Database Setup

#### Create PostgreSQL Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE bizcore_db;

# Exit
\q
```

#### Run Database Scripts (in order)

```bash
cd database

# 1. Core tables (users, roles, companies, etc.)
psql -U postgres -d bizcore_db -f core/tables.sql

# 2. Reference tables (countries, currencies, etc.)
psql -U postgres -d bizcore_db -f references/tables.sql

# 3. Initial data (demo company, admin user, roles)
psql -U postgres -d bizcore_db -f init-database.sql

# 4. Translations (English, Russian, Uzbek)
psql -U postgres -d bizcore_db -f translations.sql
```

**Or use the setup script:**

```bash
# Windows
setup.bat

# Linux/Mac
chmod +x setup.sh
./setup.sh
```

### Step 3: Configure Application

Edit `bizcore/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bizcore_db
    username: postgres
    password: your_password  # Change this!

jwt:
  secret: your-secret-key-min-256-bits  # Change this!
```

### Step 4: Build and Run

```bash
cd bizcore

# Build (includes React frontend)
mvn clean install

# Run application
mvn spring-boot:run
```

Application starts on **http://localhost:8091**

---

## 🗄️ Database Setup

### Database Files (run in order)

| # | File | Description | Required |
|---|------|-------------|----------|
| 1 | `core/tables.sql` | Core schema (users, roles, companies) | ✅ |
| 2 | `references/tables.sql` | Reference data schema | ✅ |
| 3 | `init-database.sql` | Demo company & admin user | ✅ |
| 4 | `translations.sql` | Multi-language translations | ✅ |

### What Gets Created

After running all scripts, you'll have:
- ✅ 1 demo company
- ✅ 4 demo users (admin, manager, employee, viewer)
- ✅ 4 roles (ADMIN, MANAGER, EMPLOYEE, VIEWER)
- ✅ 10 permissions
- ✅ 3 applications
- ✅ 180 translations (60 keys × 3 languages)
- ✅ Reference data (countries, currencies, units, categories)

---

## ⚙️ Configuration

### Application Settings

`bizcore/src/main/resources/application.yml`:

```yaml
server:
  port: 8091

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bizcore_db
    username: postgres
    password: your_password

  jpa:
    hibernate:
      ddl-auto: none  # Use SQL scripts

jwt:
  secret: your-jwt-secret-key
  expiration: 86400000  # 24 hours

logging:
  level:
    root: INFO
    com.bizcore: DEBUG
```

### Environment Variables (Production)

```bash
export DB_URL=jdbc:postgresql://localhost:5432/bizcore_db
export DB_USERNAME=bizcore_user
export DB_PASSWORD=secure_password
export JWT_SECRET=your-secure-jwt-secret
```

---

## 🎬 Running the Application

### Development Mode

```bash
cd bizcore
mvn spring-boot:run
```

### Production Mode

```bash
mvn clean package -DskipTests
java -jar target/bizcore-1.0.0.jar
```

### Access Points

- **Web UI:** http://localhost:8091/app/
- **API:** http://localhost:8091/api/
- **Health:** http://localhost:8091/actuator/health

---

## 🔑 Demo Credentials

| Username | Password | Role | Access Level |
|----------|----------|------|--------------|
| `admin` | `password123` | ADMIN | Full access |
| `manager` | `password123` | MANAGER | User & role management |
| `employee` | `password123` | EMPLOYEE | View users & company |
| `viewer` | `password123` | VIEWER | Read-only |

**⚠️ Change these passwords in production!**

---

## 📚 API Documentation

### Authentication

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

### User Management

```http
GET /api/users/company/{companyId}
Authorization: Bearer {token}

POST /api/users
Authorization: Bearer {token}

PUT /api/users/{id}
Authorization: Bearer {token}

DELETE /api/users/{id}
Authorization: Bearer {token}
```

### Role Management

```http
GET /api/roles/company/{companyId}
POST /api/roles
PUT /api/roles/{id}
DELETE /api/roles/{id}
```

### Translations

```http
GET /api/translations?lang=en
GET /api/translations?lang=ru&module=users
```

For complete API docs, see [docs/README.md](docs/README.md)

---

## 📁 Project Structure

```
bizcore-platform/
├── database/
│   ├── core/tables.sql           # Core schema
│   ├── references/tables.sql     # Reference data
│   ├── init-database.sql         # Initial data
│   └── translations.sql          # i18n translations
│
├── docs/                         # Documentation
│   ├── README.md
│   ├── TRANSLATION.md
│   └── ROLE-SELECTION-FIX.md
│
└── bizcore/
    ├── src/main/java/com/bizcore/
    │   ├── controller/           # REST controllers
    │   ├── service/              # Business logic
    │   ├── repository/           # Data access
    │   ├── entity/               # JPA entities
    │   ├── dto/                  # DTOs
    │   ├── security/             # Security config
    │   └── filter/               # Filters
    │
    ├── src/main/resources/
    │   ├── application.yml       # Configuration
    │   └── static/               # Built frontend
    │
    └── webapp/                   # React frontend
        ├── src/
        │   ├── pages/            # React pages
        │   ├── components/       # Components
        │   ├── hooks/            # Custom hooks
        │   ├── utils/            # Utilities
        │   └── styles/           # CSS
        └── package.json
```

---

## 👨‍💻 Development

### Frontend Development with Hot Reload

```bash
cd bizcore/webapp
npm install
npm run dev

# Frontend: http://localhost:5173
# Backend API: http://localhost:8091
```

### Build Frontend Only

```bash
cd bizcore/webapp
npm run build
```

### Run Tests

```bash
cd bizcore
mvn test
```

---

## 🚢 Deployment

### Build for Production

```bash
mvn clean package -DskipTests
```

### Run Production JAR

```bash
java -jar target/bizcore-1.0.0.jar --spring.profiles.active=prod
```

### Docker Deployment

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/bizcore-1.0.0.jar app.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t bizcore-platform .
docker run -p 8091:8091 bizcore-platform
```

---

## 🤝 Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

---

## 📄 License

This project is licensed under the MIT License - see [LICENSE](LICENSE) for details.

---

## 📞 Support

- **Documentation**: [docs/](docs/)
- **Issues**: [GitHub Issues](https://github.com/yourusername/bizcore-platform/issues)
- **Email**: support@bizcore.com

---

<div align="center">

Made with ❤️ by the BizCore Team

[⬆ Back to Top](#bizcore-platform)

</div>
