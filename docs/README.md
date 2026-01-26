# BizCore Platform - Documentation

Complete documentation for the BizCore Platform.

## 📚 Available Guides

### [Translation System (i18n)](TRANSLATION.md)
Multi-language support with English, Russian, and Uzbek translations.

**Topics covered:**
- How the translation system works
- Using `useTranslation()` hook in components
- Translation key naming conventions
- Adding new translations
- API endpoints for translations
- Complete translation keys reference

### [RBAC Security Fixes - Summary](RBAC-FIXES-SUMMARY.md)
Summary of all security fixes applied to the Role-Based Access Control system.

**Topics covered:**
- Critical security vulnerabilities fixed
- Backend authorization improvements
- Frontend permission enhancements
- Files modified
- Testing checklist

### [RBAC Testing Guide](RBAC-TESTING-GUIDE.md)
Comprehensive guide for testing the RBAC implementation.

**Topics covered:**
- Security fixes overview
- Step-by-step testing instructions
- Test scenarios by role
- Permission matrix
- Common issues and solutions
- Verification queries

## 🗂️ Project Structure

```
bizcore-platform/
├── README.md                    # Main project documentation
├── docs/                        # Detailed documentation
│   ├── README.md               # This file
│   ├── TRANSLATION.md          # Translation system guide
│   ├── RBAC-FIXES-SUMMARY.md   # RBAC security fixes summary
│   └── RBAC-TESTING-GUIDE.md   # RBAC testing guide
├── bizcore/                     # Main application
│   ├── src/main/java/          # Backend (Spring Boot)
│   └── webapp/                  # Frontend (React)
└── database/
    └── migrations/              # SQL migration scripts
```

## 🚀 Quick Links

- [Main README](../README.md) - Installation and setup guide
- [Translation Guide](TRANSLATION.md) - i18n documentation
- [RBAC Fixes Summary](RBAC-FIXES-SUMMARY.md) - Security fixes overview
- [RBAC Testing Guide](RBAC-TESTING-GUIDE.md) - Testing instructions

## 📖 Additional Resources

### Backend (Spring Boot)
- **Controllers**: `bizcore/src/main/java/com/bizcore/controller/`
- **Services**: `bizcore/src/main/java/com/bizcore/service/`
- **Entities**: `bizcore/src/main/java/com/bizcore/entity/`
- **Security**: `bizcore/src/main/java/com/bizcore/security/`

### Frontend (React)
- **Pages**: `bizcore/webapp/src/pages/`
- **Components**: `bizcore/webapp/src/components/`
- **Hooks**: `bizcore/webapp/src/hooks/`
- **Utils**: `bizcore/webapp/src/utils/`

### Database
- **Migrations**: `database/migrations/2026/01/`
- **Core Tables**: `database/core/tables.sql`
- **Reference Tables**: `database/references/tables.sql`

## 💡 Getting Help

For issues and questions, contact: muhammad.boltayev@greenwhite.uz
