# BizCore Platform - Database Installation Guide

## Prerequisites

- **PostgreSQL 14+** installed and running
- **psql** command available in terminal
- Database user with CREATE DATABASE privileges

## Quick Start

### Option 1: Automatic Setup (Recommended)

**Windows:**
```cmd
cd database
setup.bat
```

**Linux/Mac:**
```bash
cd database
chmod +x setup.sh
./setup.sh
```

### Option 2: Manual Setup

Follow these steps in order:

#### Step 1: Create Database
```sql
-- Connect to PostgreSQL as admin
psql -U postgres

-- Create the database
CREATE DATABASE bizcore;

-- Exit
\q
```

#### Step 2: Create Tables
```bash
psql -U postgres -d bizcore -f setup/tables.sql
```

#### Step 3: Insert Default Data
```bash
psql -U postgres -d bizcore -f setup/default-data.sql
```

#### Step 4: Load Translations
```bash
psql -U postgres -d bizcore -f setup/translations.sql
```

## Default Login Credentials

After installation, use these credentials to login:

| Field    | Value            |
|----------|------------------|
| Username | `admin`          |
| Password | `admin123`       |

**IMPORTANT:** Change the admin password after your first login!

## What Gets Created

### Tables (tables.sql)
- `companies` - Multi-tenant company data
- `applications` - Available applications
- `users` - User accounts
- `roles` - User roles
- `permissions` - System permissions
- `role_permissions` - Role-permission mappings
- `user_roles` - User-role assignments
- `user_applications` - User app access
- `translations` - i18n translations
- `audit_logs` - Audit trail
- `request_logs` - HTTP request logging

### Default Data (default-data.sql)
- 1 Default Company (`DEFAULT`)
- 1 Admin User (`admin`)
- 1 Admin Role (`ADMIN`) with all permissions
- 10 System Permissions (USER_*, ROLE_*, COMPANY_*, APP_*, AUDIT_*)
- 3 Applications (BizCore, SupplyMate, ChatBot)

### Translations (translations.sql)
- English (en)
- Russian (ru)
- Uzbek (uz)

## File Structure

```
database/
├── setup/
│   ├── tables.sql            # All table schemas (run first)
│   ├── default-data.sql      # Minimum required data (run second)
│   └── translations.sql      # Multi-language support (run third)
├── INSTALLATION.md           # This file
├── setup.bat                 # Windows setup script
└── setup.sh                  # Linux/Mac setup script
```

## Troubleshooting

### Connection Refused
```
psql: error: could not connect to server: Connection refused
```
**Solution:** Make sure PostgreSQL service is running:
```bash
# Windows
net start postgresql-x64-17

# Linux
sudo systemctl start postgresql

# Mac
brew services start postgresql
```

### Permission Denied
```
psql: error: FATAL: password authentication failed
```
**Solution:** Check your PostgreSQL password or use peer authentication:
```bash
sudo -u postgres psql
```

### Database Already Exists
If you want to start fresh:
```sql
DROP DATABASE IF EXISTS bizcore;
CREATE DATABASE bizcore;
```

### Scripts Run Multiple Times
All scripts are safe to run multiple times:
- Tables use `CREATE TABLE IF NOT EXISTS`
- Data uses `INSERT ... WHERE NOT EXISTS`
- Translations use `ON CONFLICT DO UPDATE`

## Running the Application

After database setup, start the backend:
```bash
cd bizcore
mvn spring-boot:run
```

Then start the frontend:
```bash
cd bizcore/webapp
npm install
npm run dev
```

Access the application at: http://localhost:5173

## Adding New Data

### Add New Permission
```sql
INSERT INTO permissions (code, name, description, module_name)
SELECT 'NEW_PERMISSION', 'New Permission', 'Description here', 'ModuleName'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'NEW_PERMISSION');
```

### Add New Translation
```sql
INSERT INTO translations (key, language, value, module) VALUES
('module.key', 'en', 'English value', 'module'),
('module.key', 'ru', 'Russian value', 'module'),
('module.key', 'uz', 'Uzbek value', 'module')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value;
```

### Add New User
```sql
-- Password should be BCrypt hashed
INSERT INTO users (company_id, username, password, email, first_name, last_name, active)
VALUES (1, 'newuser', '$2a$10$HASH_HERE', 'email@example.com', 'First', 'Last', true);
```

## Support

For issues or questions, check the main project README or create an issue in the repository.
