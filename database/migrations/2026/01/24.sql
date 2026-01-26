-- ============================================
-- Initial Migration: 2026-01-24
-- Description: Initial data setup - companies, users, roles, permissions, translations, reference data
-- ============================================

-- ==================== COMPANIES ====================
INSERT INTO companies (id, code, name, description, email, phone, active, subscription_plan) VALUES
(1, 'DEMO', 'Demo Company', 'Demo company for testing', 'demo@bizcore.com', '+1234567890', true, 'FREE');

-- Reset sequence
SELECT setval('companies_id_seq', (SELECT MAX(id) FROM companies));


-- ==================== APPLICATIONS ====================
INSERT INTO applications (id, code, name, description, icon, color, base_url, active, display_order) VALUES
(1, 'SUPPLYMATE', 'SupplyMate', 'Supply Chain & Inventory Management', '📦', '#1e3c72', '/supplymate', true, 1),
(2, 'CHATBOT', 'ChatBot', 'AI-Powered Chat Assistant', '🤖', '#7e22ce', '/chatbot', true, 2),
(3, 'BIZCORE', 'BizCore Admin', 'Core Administration Panel', '⚙️', '#2a5298', '/admin', true, 0);

SELECT setval('applications_id_seq', (SELECT MAX(id) FROM applications));


-- ==================== PERMISSIONS ====================
INSERT INTO permissions (id, code, name, description, module_name) VALUES
-- User Management
(1, 'USER_VIEW', 'View Users', 'Can view user list', 'Users'),
(2, 'USER_CREATE', 'Create Users', 'Can create new users', 'Users'),
(3, 'USER_EDIT', 'Edit Users', 'Can edit existing users', 'Users'),
(4, 'USER_DELETE', 'Delete Users', 'Can delete users', 'Users'),
-- Role Management
(5, 'ROLE_VIEW', 'View Roles', 'Can view roles', 'Roles'),
(6, 'ROLE_MANAGE', 'Manage Roles', 'Can create/edit roles', 'Roles'),
-- Company Management
(7, 'COMPANY_VIEW', 'View Company', 'Can view company info', 'Company'),
(8, 'COMPANY_EDIT', 'Edit Company', 'Can edit company settings', 'Company'),
-- Application Management
(9, 'APP_MANAGE', 'Manage Applications', 'Can manage app access', 'Applications'),
-- Audit
(10, 'AUDIT_VIEW', 'View Audit Logs', 'Can view audit logs', 'Audit');

SELECT setval('permissions_id_seq', (SELECT MAX(id) FROM permissions));


-- ==================== ROLES ====================
INSERT INTO roles (id, company_id, name, description, system_role, active) VALUES
(5, 1, 'ADMIN', 'Administrator with full access', false, true),
(6, 1, 'MANAGER', 'Manager with elevated access', false, true),
(7, 1, 'EMPLOYEE', 'Standard employee access', false, true),
(8, 1, 'VIEWER', 'Read-only access', false, true);

SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));


-- ==================== ROLE PERMISSIONS ====================
-- ADMIN role: All permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT 5, id FROM permissions;

-- MANAGER role: User management + view permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
(6, 1), -- USER_VIEW
(6, 2), -- USER_CREATE
(6, 3), -- USER_EDIT
(6, 5), -- ROLE_VIEW
(6, 7), -- COMPANY_VIEW
(6, 10); -- AUDIT_VIEW

-- EMPLOYEE role: Basic view permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
(7, 1), -- USER_VIEW
(7, 7); -- COMPANY_VIEW

-- VIEWER role: No permissions (read-only through UI)


-- ==================== USERS ====================
-- Password for all demo users: password123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO
INSERT INTO users (id, company_id, username, password, email, first_name, last_name, active, email_verified) VALUES
(1, 1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO', 'admin@demo.com', 'Admin', 'User', true, false),
(2, 1, 'manager', '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO', 'manager@demo.com', 'Manager', 'User', true, false),
(3, 1, 'employee', '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO', 'employee@demo.com', 'Employee', 'User', true, false),
(4, 1, 'viewer', '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO', 'viewer@demo.com', 'Viewer', 'User', true, false);

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));


-- ==================== USER ROLES ====================
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 5), -- admin -> ADMIN
(2, 6), -- manager -> MANAGER
(3, 7), -- employee -> EMPLOYEE
(4, 8); -- viewer -> VIEWER


-- ==================== USER APPLICATIONS ====================
-- All users have access to all applications in demo
INSERT INTO user_applications (user_id, application_id)
SELECT u.id, a.id
FROM users u
CROSS JOIN applications a
WHERE u.company_id = 1;


-- ==================== TRANSLATIONS (i18n) ====================
-- English translations
INSERT INTO translations (key, language, value, module) VALUES
-- Common
('common.save', 'en', 'Save', 'common'),
('common.cancel', 'en', 'Cancel', 'common'),
('common.delete', 'en', 'Delete', 'common'),
('common.edit', 'en', 'Edit', 'common'),
('common.create', 'en', 'Create', 'common'),
('common.search', 'en', 'Search', 'common'),
('common.actions', 'en', 'Actions', 'common'),
('common.status', 'en', 'Status', 'common'),
('common.active', 'en', 'Active', 'common'),
('common.inactive', 'en', 'Inactive', 'common'),

-- Users module
('users.title', 'en', 'Users', 'users'),
('users.create', 'en', 'Create User', 'users'),
('users.edit', 'en', 'Edit User', 'users'),
('users.username', 'en', 'Username', 'users'),
('users.email', 'en', 'Email', 'users'),
('users.firstName', 'en', 'First Name', 'users'),
('users.lastName', 'en', 'Last Name', 'users'),
('users.roles', 'en', 'Roles', 'users'),
('users.createdAt', 'en', 'Created At', 'users'),

-- Roles module
('roles.title', 'en', 'Roles', 'roles'),
('roles.create', 'en', 'Create Role', 'roles'),
('roles.edit', 'en', 'Edit Role', 'roles'),
('roles.name', 'en', 'Role Name', 'roles'),
('roles.description', 'en', 'Description', 'roles'),
('roles.permissions', 'en', 'Permissions', 'roles'),

-- Permissions module
('permissions.title', 'en', 'Permissions', 'permissions'),
('permissions.module', 'en', 'Module', 'permissions'),
('permissions.code', 'en', 'Code', 'permissions'),
('permissions.name', 'en', 'Name', 'permissions'),
('permissions.description', 'en', 'Description', 'permissions'),

-- Dashboard
('dashboard.title', 'en', 'Dashboard', 'dashboard'),
('dashboard.welcome', 'en', 'Welcome', 'dashboard'),
('dashboard.users', 'en', 'Users', 'dashboard'),
('dashboard.roles', 'en', 'Roles', 'dashboard'),
('dashboard.permissions', 'en', 'Permissions', 'dashboard');

-- Uzbek translations
INSERT INTO translations (key, language, value, module) VALUES
-- Common
('common.save', 'uz', 'Saqlash', 'common'),
('common.cancel', 'uz', 'Bekor qilish', 'common'),
('common.delete', 'uz', "O'chirish", 'common'),
('common.edit', 'uz', 'Tahrirlash', 'common'),
('common.create', 'uz', 'Yaratish', 'common'),
('common.search', 'uz', 'Qidirish', 'common'),
('common.actions', 'uz', 'Amallar', 'common'),
('common.status', 'uz', 'Holat', 'common'),
('common.active', 'uz', 'Faol', 'common'),
('common.inactive', 'uz', 'Faol emas', 'common'),

-- Users module
('users.title', 'uz', 'Foydalanuvchilar', 'users'),
('users.create', 'uz', 'Foydalanuvchi yaratish', 'users'),
('users.edit', 'uz', 'Foydalanuvchini tahrirlash', 'users'),
('users.username', 'uz', 'Foydalanuvchi nomi', 'users'),
('users.email', 'uz', 'Email', 'users'),
('users.firstName', 'uz', 'Ism', 'users'),
('users.lastName', 'uz', 'Familiya', 'users'),
('users.roles', 'uz', 'Rollar', 'users'),
('users.createdAt', 'uz', 'Yaratilgan sana', 'users'),

-- Roles module
('roles.title', 'uz', 'Rollar', 'roles'),
('roles.create', 'uz', 'Rol yaratish', 'roles'),
('roles.edit', 'uz', 'Rolni tahrirlash', 'roles'),
('roles.name', 'uz', 'Rol nomi', 'roles'),
('roles.description', 'uz', 'Tavsif', 'roles'),
('roles.permissions', 'uz', 'Ruxsatlar', 'roles'),

-- Permissions module
('permissions.title', 'uz', 'Ruxsatlar', 'permissions'),
('permissions.module', 'uz', 'Modul', 'permissions'),
('permissions.code', 'uz', 'Kod', 'permissions'),
('permissions.name', 'uz', 'Nomi', 'permissions'),
('permissions.description', 'uz', 'Tavsif', 'permissions'),

-- Dashboard
('dashboard.title', 'uz', 'Boshqaruv paneli', 'dashboard'),
('dashboard.welcome', 'uz', 'Xush kelibsiz', 'dashboard'),
('dashboard.users', 'uz', 'Foydalanuvchilar', 'dashboard'),
('dashboard.roles', 'uz', 'Rollar', 'dashboard'),
('dashboard.permissions', 'uz', 'Ruxsatlar', 'dashboard');

-- Russian translations
INSERT INTO translations (key, language, value, module) VALUES
-- Common
('common.save', 'ru', 'Сохранить', 'common'),
('common.cancel', 'ru', 'Отмена', 'common'),
('common.delete', 'ru', 'Удалить', 'common'),
('common.edit', 'ru', 'Редактировать', 'common'),
('common.create', 'ru', 'Создать', 'common'),
('common.search', 'ru', 'Поиск', 'common'),
('common.actions', 'ru', 'Действия', 'common'),
('common.status', 'ru', 'Статус', 'common'),
('common.active', 'ru', 'Активный', 'common'),
('common.inactive', 'ru', 'Неактивный', 'common'),

-- Users module
('users.title', 'ru', 'Пользователи', 'users'),
('users.create', 'ru', 'Создать пользователя', 'users'),
('users.edit', 'ru', 'Редактировать пользователя', 'users'),
('users.username', 'ru', 'Имя пользователя', 'users'),
('users.email', 'ru', 'Email', 'users'),
('users.firstName', 'ru', 'Имя', 'users'),
('users.lastName', 'ru', 'Фамилия', 'users'),
('users.roles', 'ru', 'Роли', 'users'),
('users.createdAt', 'ru', 'Дата создания', 'users'),

-- Roles module
('roles.title', 'ru', 'Роли', 'roles'),
('roles.create', 'ru', 'Создать роль', 'roles'),
('roles.edit', 'ru', 'Редактировать роль', 'roles'),
('roles.name', 'ru', 'Название роли', 'roles'),
('roles.description', 'ru', 'Описание', 'roles'),
('roles.permissions', 'ru', 'Разрешения', 'roles'),

-- Permissions module
('permissions.title', 'ru', 'Разрешения', 'permissions'),
('permissions.module', 'ru', 'Модуль', 'permissions'),
('permissions.code', 'ru', 'Код', 'permissions'),
('permissions.name', 'ru', 'Название', 'permissions'),
('permissions.description', 'ru', 'Описание', 'permissions'),

-- Dashboard
('dashboard.title', 'ru', 'Панель управления', 'dashboard'),
('dashboard.welcome', 'ru', 'Добро пожаловать', 'dashboard'),
('dashboard.users', 'ru', 'Пользователи', 'dashboard'),
('dashboard.roles', 'ru', 'Роли', 'dashboard'),
('dashboard.permissions', 'ru', 'Разрешения', 'dashboard');


-- ==================== REFERENCE DATA: COUNTRIES ====================
INSERT INTO ref_countries (code, code2, name, local_name, phone_code, currency_code, active) VALUES
('USA', 'US', 'United States', 'United States', '+1', 'USD', true),
('UZB', 'UZ', 'Uzbekistan', "O'zbekiston", '+998', 'UZS', true),
('RUS', 'RU', 'Russia', 'Россия', '+7', 'RUB', true),
('GBR', 'GB', 'United Kingdom', 'United Kingdom', '+44', 'GBP', true),
('DEU', 'DE', 'Germany', 'Deutschland', '+49', 'EUR', true),
('FRA', 'FR', 'France', 'France', '+33', 'EUR', true),
('CHN', 'CN', 'China', '中国', '+86', 'CNY', true),
('JPN', 'JP', 'Japan', '日本', '+81', 'JPY', true),
('IND', 'IN', 'India', 'भारत', '+91', 'INR', true),
('KAZ', 'KZ', 'Kazakhstan', 'Қазақстан', '+7', 'KZT', true);


-- ==================== REFERENCE DATA: CURRENCIES ====================
INSERT INTO ref_currencies (code, name, symbol, decimal_places, active) VALUES
('USD', 'US Dollar', '$', 2, true),
('UZS', 'Uzbek Som', 'soʻm', 2, true),
('RUB', 'Russian Ruble', '₽', 2, true),
('EUR', 'Euro', '€', 2, true),
('GBP', 'British Pound', '£', 2, true),
('CNY', 'Chinese Yuan', '¥', 2, true),
('JPY', 'Japanese Yen', '¥', 0, true),
('INR', 'Indian Rupee', '₹', 2, true),
('KZT', 'Kazakhstani Tenge', '₸', 2, true);


-- ==================== REFERENCE DATA: UNITS ====================
INSERT INTO ref_units (code, name, category, symbol, active) VALUES
-- Weight
('KG', 'Kilogram', 'weight', 'kg', true),
('G', 'Gram', 'weight', 'g', true),
('T', 'Ton', 'weight', 't', true),
('LB', 'Pound', 'weight', 'lb', true),
-- Volume
('L', 'Liter', 'volume', 'L', true),
('ML', 'Milliliter', 'volume', 'mL', true),
('M3', 'Cubic Meter', 'volume', 'm³', true),
-- Length
('M', 'Meter', 'length', 'm', true),
('CM', 'Centimeter', 'length', 'cm', true),
('KM', 'Kilometer', 'length', 'km', true),
-- Area
('M2', 'Square Meter', 'area', 'm²', true),
('HA', 'Hectare', 'area', 'ha', true),
-- Piece
('PC', 'Piece', 'piece', 'pc', true),
('BOX', 'Box', 'piece', 'box', true),
('PKG', 'Package', 'piece', 'pkg', true);


-- ==================== REFERENCE DATA: CATEGORIES ====================
INSERT INTO ref_categories (code, name, description, type, level, active) VALUES
-- Product categories
('ELECTRONICS', 'Electronics', 'Electronic devices and accessories', 'product', 0, true),
('CLOTHING', 'Clothing', 'Apparel and fashion items', 'product', 0, true),
('FOOD', 'Food & Beverages', 'Food products and drinks', 'product', 0, true),
('FURNITURE', 'Furniture', 'Home and office furniture', 'product', 0, true),
('BOOKS', 'Books', 'Books and publications', 'product', 0, true);
