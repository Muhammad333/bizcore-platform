# BizCore Platform - Documentation

## Available Guides

### [Translation System (i18n)](TRANSLATION.md)
Multi-language support with English, Russian, and Uzbek translations.

**Topics:**
- How the translation system works
- Using `useTranslation()` hook
- Adding new translations
- API endpoints

## Project Structure

```
bizcore-platform/
├── database/
│   ├── setup/
│   │   ├── tables.sql            # All database tables
│   │   ├── default-data.sql      # Default data (company, admin, role)
│   │   └── translations.sql      # Multi-language translations
│   ├── INSTALLATION.md           # Database setup guide
│   ├── setup.bat                 # Windows setup script
│   └── setup.sh                  # Linux/Mac setup script
│
├── bizcore/
│   ├── src/main/java/            # Backend (Spring Boot)
│   │   └── com/bizcore/
│   │       ├── controller/       # REST controllers
│   │       ├── service/          # Business logic
│   │       ├── repository/       # Data access
│   │       ├── entity/           # JPA entities
│   │       └── security/         # Security config
│   │
│   └── webapp/                   # Frontend (React)
│       └── src/
│           ├── pages/            # React pages
│           ├── components/       # UI components
│           ├── hooks/            # Custom hooks
│           ├── contexts/         # React contexts
│           └── utils/            # Utilities
│
└── docs/                         # This folder
    ├── README.md                 # This file
    └── TRANSLATION.md            # i18n guide
```

## Quick Links

- [Main README](../README.md) - Installation and setup
- [Database Installation](../database/INSTALLATION.md) - Database setup guide
- [Translation Guide](TRANSLATION.md) - i18n documentation

## Backend Structure

| Folder | Description |
|--------|-------------|
| `controller/` | REST API endpoints |
| `service/` | Business logic layer |
| `repository/` | Database access (JPA) |
| `entity/` | Database entities |
| `dto/` | Data transfer objects |
| `security/` | JWT & authorization |
| `filter/` | Request filters |

## Frontend Structure

| Folder | Description |
|--------|-------------|
| `pages/` | Page components (Dashboard, Users, Roles, etc.) |
| `components/` | Reusable UI components |
| `hooks/` | Custom React hooks (useAuth, useTranslation, etc.) |
| `contexts/` | React context providers |
| `utils/` | Utility functions |

## Contact

For issues and questions: muhammad.boltayev@greenwhite.uz
