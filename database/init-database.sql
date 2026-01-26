-- ============================================
-- BizCore Platform - Database Initialization
-- ============================================
-- Description: Creates demo company with admin user, roles, and permissions
-- Usage: Run this script after creating tables with core/tables.sql and references/tables.sql
-- Demo Credentials: username=admin, password=password123
-- ============================================

-- ==================== DEMO COMPANY ====================
-- Insert demo company for testing and development
INSERT INTO companies (id, code, name, description, email, phone, active, subscription_plan) VALUES
(1, 'DEMO', 'Demo Company', 'Demo company for testing and development', 'demo@bizcore.com', '+1234567890', true, 'FREE');

-- Reset sequence to continue from the last inserted ID
SELECT setval('companies_id_seq', (SELECT MAX(id) FROM companies));


-- ==================== APPLICATIONS ====================
-- Core applications available in the system
INSERT INTO applications (id, code, name, description, icon, color, base_url, active, display_order) VALUES
(1, 'SUPPLYMATE', 'SupplyMate', 'Supply Chain & Inventory Management System', '📦', '#1e3c72', '/supplymate', true, 1),
(2, 'CHATBOT', 'ChatBot', 'AI-Powered Chat Assistant', '🤖', '#7e22ce', '/chatbot', true, 2),
(3, 'BIZCORE', 'BizCore Admin', 'Core Administration & User Management Panel', '⚙️', '#2a5298', '/admin', true, 0);

SELECT setval('applications_id_seq', (SELECT MAX(id) FROM applications));


-- ==================== PERMISSIONS ====================
-- System-wide permissions for RBAC (Role-Based Access Control)
INSERT INTO permissions (id, code, name, description, module_name) VALUES
-- User Management Permissions
(1, 'USER_VIEW', 'View Users', 'Can view user list and details', 'Users'),
(2, 'USER_CREATE', 'Create Users', 'Can create new user accounts', 'Users'),
(3, 'USER_EDIT', 'Edit Users', 'Can edit existing user accounts', 'Users'),
(4, 'USER_DELETE', 'Delete Users', 'Can delete user accounts', 'Users'),

-- Role Management Permissions
(5, 'ROLE_VIEW', 'View Roles', 'Can view roles and their permissions', 'Roles'),
(6, 'ROLE_MANAGE', 'Manage Roles', 'Can create, edit, and delete roles', 'Roles'),

-- Company Management Permissions
(7, 'COMPANY_VIEW', 'View Company', 'Can view company information', 'Company'),
(8, 'COMPANY_EDIT', 'Edit Company', 'Can edit company settings and details', 'Company'),

-- Application Management Permissions
(9, 'APP_MANAGE', 'Manage Applications', 'Can manage application access and settings', 'Applications'),

-- Audit & Logging Permissions
(10, 'AUDIT_VIEW', 'View Audit Logs', 'Can view system audit logs and request logs', 'Audit');

SELECT setval('permissions_id_seq', (SELECT MAX(id) FROM permissions));


-- ==================== ROLES ====================
-- Pre-defined roles for the demo company
INSERT INTO roles (id, company_id, name, description, system_role, active) VALUES
(5, 1, 'ADMIN', 'Administrator with full system access', false, true),
(6, 1, 'MANAGER', 'Manager with elevated permissions', false, true),
(7, 1, 'EMPLOYEE', 'Standard employee with basic access', false, true),
(8, 1, 'VIEWER', 'Read-only access for viewing information', false, true);

SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));


-- ==================== ROLE PERMISSIONS ASSIGNMENT ====================
-- ADMIN role: Full access to all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT 5, id FROM permissions;

-- MANAGER role: User management + viewing permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
(6, 1),  -- USER_VIEW
(6, 2),  -- USER_CREATE
(6, 3),  -- USER_EDIT
(6, 5),  -- ROLE_VIEW
(6, 7),  -- COMPANY_VIEW
(6, 10); -- AUDIT_VIEW

-- EMPLOYEE role: Basic view permissions only
INSERT INTO role_permissions (role_id, permission_id) VALUES
(7, 1),  -- USER_VIEW
(7, 7);  -- COMPANY_VIEW

-- VIEWER role: Read-only access (no specific permissions, UI-controlled)
-- Note: Viewer role is intentionally left without permissions for testing


-- ==================== DEMO USERS ====================
-- Demo user accounts for testing different access levels
-- Password for all users: password123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO
INSERT INTO users (id, company_id, username, password, email, first_name, last_name, active, email_verified) VALUES
(1, 1, 'admin',    '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO', 'admin@demo.com',    'Admin',    'User', true, false),
(2, 1, 'manager',  '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO', 'manager@demo.com',  'Manager',  'User', true, false),
(3, 1, 'employee', '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO', 'employee@demo.com', 'Employee', 'User', true, false),
(4, 1, 'viewer',   '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO', 'viewer@demo.com',   'Viewer',   'User', true, false);

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));


-- ==================== USER ROLE ASSIGNMENTS ====================
-- Assign roles to demo users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 5),  -- admin    → ADMIN role
(2, 6),  -- manager  → MANAGER role
(3, 7),  -- employee → EMPLOYEE role
(4, 8);  -- viewer   → VIEWER role


-- ==================== USER APPLICATION ACCESS ====================
-- Grant all demo users access to all applications
INSERT INTO user_applications (user_id, application_id)
SELECT u.id, a.id
FROM users u
CROSS JOIN applications a
WHERE u.company_id = 1;


-- ==================== REFERENCE DATA: COUNTRIES ====================
-- Common countries with phone codes and currency info
INSERT INTO ref_countries (code, code2, name, local_name, phone_code, currency_code, active) VALUES
('USA', 'US', 'United States',  'United States', '+1',   'USD', true),
('UZB', 'UZ', 'Uzbekistan',     "O'zbekiston",   '+998', 'UZS', true),
('RUS', 'RU', 'Russia',          'Россия',        '+7',   'RUB', true),
('GBR', 'GB', 'United Kingdom', 'United Kingdom', '+44',  'GBP', true),
('DEU', 'DE', 'Germany',         'Deutschland',   '+49',  'EUR', true),
('FRA', 'FR', 'France',          'France',        '+33',  'EUR', true),
('CHN', 'CN', 'China',           '中国',          '+86',  'CNY', true),
('JPN', 'JP', 'Japan',           '日本',          '+81',  'JPY', true),
('IND', 'IN', 'India',           'भारत',          '+91',  'INR', true),
('KAZ', 'KZ', 'Kazakhstan',      'Қазақстан',     '+7',   'KZT', true);


-- ==================== REFERENCE DATA: CURRENCIES ====================
-- Major world currencies with symbols and decimal places
INSERT INTO ref_currencies (code, name, symbol, decimal_places, active) VALUES
('USD', 'US Dollar',          '$',    2, true),
('UZS', 'Uzbek Som',          'soʻm', 2, true),
('RUB', 'Russian Ruble',      '₽',    2, true),
('EUR', 'Euro',               '€',    2, true),
('GBP', 'British Pound',      '£',    2, true),
('CNY', 'Chinese Yuan',       '¥',    2, true),
('JPY', 'Japanese Yen',       '¥',    0, true),
('INR', 'Indian Rupee',       '₹',    2, true),
('KZT', 'Kazakhstani Tenge',  '₸',    2, true);


-- ==================== REFERENCE DATA: UNITS OF MEASURE ====================
-- Common units categorized by measurement type
INSERT INTO ref_units (code, name, category, symbol, active) VALUES
-- Weight Units
('KG', 'Kilogram', 'weight', 'kg', true),
('G',  'Gram',     'weight', 'g',  true),
('T',  'Ton',      'weight', 't',  true),
('LB', 'Pound',    'weight', 'lb', true),

-- Volume Units
('L',  'Liter',       'volume', 'L',  true),
('ML', 'Milliliter',  'volume', 'mL', true),
('M3', 'Cubic Meter', 'volume', 'm³', true),

-- Length Units
('M',  'Meter',      'length', 'm',  true),
('CM', 'Centimeter', 'length', 'cm', true),
('KM', 'Kilometer',  'length', 'km', true),

-- Area Units
('M2', 'Square Meter', 'area', 'm²', true),
('HA', 'Hectare',      'area', 'ha', true),

-- Count/Piece Units
('PC',  'Piece',   'piece', 'pc',  true),
('BOX', 'Box',     'piece', 'box', true),
('PKG', 'Package', 'piece', 'pkg', true);


-- ==================== REFERENCE DATA: CATEGORIES ====================
-- Sample product categories (can be extended)
INSERT INTO ref_categories (code, name, description, type, level, active) VALUES
('ELECTRONICS', 'Electronics',       'Electronic devices and accessories',  'product', 0, true),
('CLOTHING',    'Clothing',          'Apparel and fashion items',           'product', 0, true),
('FOOD',        'Food & Beverages',  'Food products and drinks',            'product', 0, true),
('FURNITURE',   'Furniture',         'Home and office furniture',           'product', 0, true),
('BOOKS',       'Books',             'Books and publications',              'product', 0, true);


-- ============================================
-- Initialization Complete!
-- ============================================
-- Demo credentials created:
--   Username: admin
--   Password: password123
--   Role: ADMIN (full access)
--
-- Other test users:
--   manager/password123  (MANAGER role)
--   employee/password123 (EMPLOYEE role)
--   viewer/password123   (VIEWER role)
--
-- Next step: Run translations.sql to load multi-language support
-- ============================================
