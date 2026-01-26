# Translation System (i18n)

Multi-language support for BizCore Platform with English, Russian, and Uzbek languages.

## Overview

The translation system uses:
- **Backend**: Translations stored in PostgreSQL `translations` table
- **Frontend**: React hook `useTranslation()` for accessing translations
- **API**: `/api/translations/{language}` endpoint
- **Storage**: Language preference saved in localStorage

## Supported Languages

| Code | Language | Flag |
|------|----------|------|
| `en` | English | 🇺🇸 |
| `ru` | Russian (Русский) | 🇷🇺 |
| `uz` | Uzbek (O'zbek) | 🇺🇿 |

## How It Works

### 1. Database Structure

```sql
CREATE TABLE translations (
    id BIGSERIAL PRIMARY KEY,
    "key" VARCHAR(255) NOT NULL,      -- e.g., 'users.title'
    language VARCHAR(10) NOT NULL,    -- e.g., 'en', 'ru', 'uz'
    value TEXT NOT NULL,              -- Translated text
    module VARCHAR(100) NOT NULL,     -- e.g., 'USER', 'COMMON'
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT uk_translations_key_language UNIQUE ("key", language)
);
```

### 2. Frontend Usage

#### Import the Hook

```javascript
import { useTranslation } from '../hooks/useTranslation'
```

#### Use in Component

```javascript
export default function MyComponent() {
  const { t, language, changeLanguage, availableLanguages } = useTranslation()

  return (
    <div>
      <h1>{t('users.title')}</h1>
      <button>{t('common.save')}</button>
      <p>{t('common.noData')}</p>
    </div>
  )
}
```

#### Available Functions

| Function | Description | Example |
|----------|-------------|---------|
| `t(key)` | Get translation for a key | `t('users.title')` → "Users" |
| `language` | Current language code | `"en"`, `"ru"`, `"uz"` |
| `changeLanguage(lang)` | Switch language | `changeLanguage('ru')` |
| `availableLanguages` | Array of language codes | `['en', 'ru', 'uz']` |

## Translation Key Naming Convention

Format: `{module}.{key}`

### Module Names

- `common` - Common words used throughout the app
- `dashboard` - Dashboard page
- `users` - User management
- `roles` - Role management
- `permissions` - Permission management

### Examples

```javascript
// Common
t('common.save')          // "Save"
t('common.cancel')        // "Cancel"
t('common.delete')        // "Delete"
t('common.edit')          // "Edit"
t('common.create')        // "Create"
t('common.search')        // "Search"
t('common.actions')       // "Actions"
t('common.status')        // "Status"
t('common.active')        // "Active"
t('common.inactive')      // "Inactive"

// Users
t('users.title')          // "Users"
t('users.create')         // "Create User"
t('users.edit')           // "Edit User"
t('users.username')       // "Username"
t('users.email')          // "Email"
t('users.firstName')      // "First Name"
t('users.lastName')       // "Last Name"

// Roles
t('roles.title')          // "Roles"
t('roles.create')         // "Create Role"
t('roles.name')           // "Role Name"
t('roles.description')    // "Description"

// Dashboard
t('dashboard.title')      // "Dashboard"
t('dashboard.users')      // "Total Users"
t('dashboard.roles')      // "Total Roles"
```

## Adding New Translations

### Step 1: Add to Database

```sql
INSERT INTO translations ("key", language, value, module) VALUES
('users.phoneNumber', 'en', 'Phone Number', 'USER'),
('users.phoneNumber', 'ru', 'Номер телефона', 'USER'),
('users.phoneNumber', 'uz', 'Telefon raqami', 'USER')
ON CONFLICT ("key", language) DO UPDATE SET
    value = EXCLUDED.value,
    updated_at = NOW();
```

### Step 2: Use in Component

```javascript
<FormInput
  label={t('users.phoneNumber')}
  name="phoneNumber"
/>
```

## Complete Example: Translatable Form

```javascript
import { useState } from 'react'
import { useTranslation } from '../hooks/useTranslation'
import { FormInput, Modal } from '../components'

export default function UserForm({ isOpen, onClose, onSave }) {
  const { t } = useTranslation()
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    firstName: '',
    lastName: ''
  })

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={t('users.create')}
      footer={
        <>
          <button className="btn btn-secondary" onClick={onClose}>
            {t('common.cancel')}
          </button>
          <button className="btn btn-primary" onClick={onSave}>
            {t('common.save')}
          </button>
        </>
      }
    >
      <FormInput
        label={t('users.username')}
        name="username"
        value={formData.username}
        onChange={(e) => setFormData({ ...formData, username: e.target.value })}
        error={t('users.usernameRequired')}
        required
      />

      <FormInput
        label={t('users.email')}
        type="email"
        name="email"
        value={formData.email}
        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
        error={t('users.emailInvalid')}
        required
      />

      <FormInput
        label={t('users.firstName')}
        name="firstName"
        value={formData.firstName}
        onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
        required
      />

      <FormInput
        label={t('users.lastName')}
        name="lastName"
        value={formData.lastName}
        onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
        required
      />
    </Modal>
  )
}
```

## Language Switching Flow

1. User clicks language dropdown in header
2. Selects desired language (e.g., Русский)
3. `changeLanguage('ru')` is called
4. System fetches translations: `GET /api/translations/ru`
5. All components with `useTranslation()` re-render
6. Language saved to localStorage: `bizcore_language=ru`
7. Next visit automatically uses saved language

## API Endpoints

### Get Translations for Language

```http
GET /api/translations/{language}

Response:
{
  "common.save": "Save",
  "common.cancel": "Cancel",
  "users.title": "Users",
  ...
}
```

### Get Available Languages

```http
GET /api/translations/languages

Response:
["en", "ru", "uz"]
```

### Add/Update Translation (Admin)

```http
POST /api/translations
Authorization: Bearer {token}

{
  "key": "users.phoneNumber",
  "language": "en",
  "value": "Phone Number",
  "module": "USER"
}
```

## Translation Keys Reference

### Common Module

```javascript
common.save              // Save
common.cancel            // Cancel
common.delete            // Delete
common.edit              // Edit
common.create            // Create
common.search            // Search
common.actions           // Actions
common.status            // Status
common.active            // Active
common.inactive          // Inactive
common.adminPanel        // Admin Panel
common.changeLanguage    // Change Language
common.company           // Company
common.role              // Role
common.logout            // Logout
common.confirmLogout     // Are you sure you want to logout?
```

### Dashboard Module

```javascript
dashboard.title          // Dashboard
dashboard.users          // Total Users
dashboard.roles          // Total Roles
dashboard.permissions    // Permissions
dashboard.activity       // Activity
dashboard.recentActivity // Recent Activity
dashboard.noActivity     // No recent activity
dashboard.quickActions   // Quick Actions
```

### Users Module

```javascript
users.title              // Users
users.create             // Create User
users.edit               // Edit User
users.username           // Username
users.email              // Email
users.firstName          // First Name
users.lastName           // Last Name
users.phoneNumber        // Phone Number
users.roles              // Roles
users.active             // Active
users.addUser            // Add User
```

### Roles Module

```javascript
roles.title              // Roles
roles.create             // Create Role
roles.edit               // Edit Role
roles.name               // Role Name
roles.code               // Role Code
roles.description        // Description
roles.permissions        // Permissions
roles.addRole            // Add Role
```

### Permissions Module

```javascript
permissions.title        // Permissions
permissions.name         // Permission Name
permissions.code         // Permission Code
permissions.module       // Module
permissions.description  // Description
```

## Best Practices

### ✅ DO

- Always use `t()` for user-facing text
- Keep translation keys semantic and descriptive
- Group related keys by module
- Provide translations for all supported languages
- Use fallback text: `t('key') || 'Default Text'`

### ❌ DON'T

- Don't hardcode text in components
- Don't use English text as keys
- Don't mix languages in one key
- Don't forget to add translation for new features

### Good Example

```javascript
<button>{t('common.save')}</button>
<h1>{t('users.title')}</h1>
<p>{t('dashboard.noActivity')}</p>
```

### Bad Example

```javascript
<button>Save</button>  // ❌ Hardcoded
<h1>{t('Users')}</h1>  // ❌ Using English as key
<p>No recent activity</p>  // ❌ Not translated
```

## Troubleshooting

### Translation not showing

1. Check if translation key exists in database
2. Verify language is loaded: Open DevTools → Network → Check `/api/translations/{lang}`
3. Check console for errors
4. Ensure component uses `useTranslation()` hook

### Language not switching

1. Check Bootstrap JS is loaded (`bootstrap.bundle.min.js`)
2. Verify dropdown has `data-bs-toggle="dropdown"`
3. Check `changeLanguage()` function is called correctly

### Adding new language

1. Add translations to database for new language code
2. Update `availableLanguages` if hardcoded anywhere
3. Add language name and flag to Layout component

## Testing Translations

```javascript
// Test in browser console
localStorage.setItem('bizcore_language', 'ru')
location.reload()

// Should load Russian translations
```

## Migration: Add New Translations

See: `database/migrations/2026/01/add-missing-translations.sql`

```sql
-- Template for new translations
INSERT INTO translations ("key", language, value, module) VALUES
('module.key', 'en', 'English Text', 'MODULE'),
('module.key', 'ru', 'Русский текст', 'MODULE'),
('module.key', 'uz', 'O''zbek matni', 'MODULE')
ON CONFLICT ("key", language) DO UPDATE SET
    value = EXCLUDED.value,
    updated_at = NOW();
```
