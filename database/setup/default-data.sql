-- ============================================
-- BizCore Platform - Default Data
-- ============================================
-- Step 2 of 3: Inserts minimum required data
-- Run AFTER 01-tables.sql
--
-- Creates:
--   - 1 Default Company
--   - 1 Admin User (can do everything)
--   - 1 Admin Role (has all permissions)
--   - All system permissions
--   - 3 Applications (BizCore, SupplyMate, ChatBot)
-- ============================================

-- ==================== 1. DEFAULT COMPANY ====================
INSERT INTO companies (id, code, name, description, email, phone, active, subscription_plan)
SELECT 1, 'DEFAULT', 'Default Company', 'Default system company', 'admin@bizcore.local', '+000000000', true, 'FREE'
WHERE NOT EXISTS (SELECT 1 FROM companies WHERE code = 'DEFAULT');

-- Reset sequence
SELECT setval('companies_id_seq', GREATEST((SELECT MAX(id) FROM companies), 1));


-- ==================== 2. APPLICATIONS ====================
INSERT INTO applications (id, code, name, description, icon, color, base_url, active, display_order)
SELECT 1, 'BIZCORE', 'BizCore Admin', 'Core Administration & User Management Panel', 'settings', '#2a5298', '/admin', true, 0
WHERE NOT EXISTS (SELECT 1 FROM applications WHERE code = 'BIZCORE');

INSERT INTO applications (id, code, name, description, icon, color, base_url, active, display_order)
SELECT 2, 'SUPPLYMATE', 'SupplyMate', 'Supply Chain & Inventory Management System', 'package', '#1e3c72', '/supplymate', true, 1
WHERE NOT EXISTS (SELECT 1 FROM applications WHERE code = 'SUPPLYMATE');

INSERT INTO applications (id, code, name, description, icon, color, base_url, active, display_order)
SELECT 3, 'CHATBOT', 'ChatBot', 'AI-Powered Chat Assistant', 'message-circle', '#7e22ce', '/chatbot', true, 2
WHERE NOT EXISTS (SELECT 1 FROM applications WHERE code = 'CHATBOT');

SELECT setval('applications_id_seq', GREATEST((SELECT MAX(id) FROM applications), 1));


-- ==================== 3. ALL PERMISSIONS ====================
-- User Management
INSERT INTO permissions (id, code, name, description, module_name)
SELECT 1, 'USER_VIEW', 'View Users', 'Can view user list and details', 'Users'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'USER_VIEW');

INSERT INTO permissions (id, code, name, description, module_name)
SELECT 2, 'USER_CREATE', 'Create Users', 'Can create new user accounts', 'Users'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'USER_CREATE');

INSERT INTO permissions (id, code, name, description, module_name)
SELECT 3, 'USER_EDIT', 'Edit Users', 'Can edit existing user accounts', 'Users'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'USER_EDIT');

INSERT INTO permissions (id, code, name, description, module_name)
SELECT 4, 'USER_DELETE', 'Delete Users', 'Can delete user accounts', 'Users'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'USER_DELETE');

-- Role Management
INSERT INTO permissions (id, code, name, description, module_name)
SELECT 5, 'ROLE_VIEW', 'View Roles', 'Can view roles and their permissions', 'Roles'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'ROLE_VIEW');

INSERT INTO permissions (id, code, name, description, module_name)
SELECT 6, 'ROLE_MANAGE', 'Manage Roles', 'Can create, edit, and delete roles', 'Roles'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'ROLE_MANAGE');

-- Company Management
INSERT INTO permissions (id, code, name, description, module_name)
SELECT 7, 'COMPANY_VIEW', 'View Company', 'Can view company information', 'Company'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'COMPANY_VIEW');

INSERT INTO permissions (id, code, name, description, module_name)
SELECT 8, 'COMPANY_EDIT', 'Edit Company', 'Can edit company settings and details', 'Company'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'COMPANY_EDIT');

-- Application Management
INSERT INTO permissions (id, code, name, description, module_name)
SELECT 9, 'APP_MANAGE', 'Manage Applications', 'Can manage application access and settings', 'Applications'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'APP_MANAGE');

-- Audit & Logging
INSERT INTO permissions (id, code, name, description, module_name)
SELECT 10, 'AUDIT_VIEW', 'View Audit Logs', 'Can view system audit logs and request logs', 'Audit'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE code = 'AUDIT_VIEW');

SELECT setval('permissions_id_seq', GREATEST((SELECT MAX(id) FROM permissions), 1));


-- ==================== 4. ADMIN ROLE ====================
INSERT INTO roles (id, company_id, name, description, system_role, active)
SELECT 1, 1, 'ADMIN', 'Administrator with full system access - can do everything', true, true
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE company_id = 1 AND name = 'ADMIN');

SELECT setval('roles_id_seq', GREATEST((SELECT MAX(id) FROM roles), 1));

-- Grant ALL permissions to ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT 1, p.id FROM permissions p
WHERE NOT EXISTS (
    SELECT 1 FROM role_permissions rp
    WHERE rp.role_id = 1 AND rp.permission_id = p.id
);


-- ==================== 5. ADMIN USER ====================
-- Password: admin123 (BCrypt hash)
-- You should change this password after first login!
INSERT INTO users (id, company_id, username, password, email, first_name, last_name, active, email_verified)
SELECT 1, 1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye5b8j3lRN3r.u5BLgXK9Y8q1bVk6G8NO',
       'admin@bizcore.local', 'System', 'Administrator', true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE company_id = 1 AND username = 'admin');

SELECT setval('users_id_seq', GREATEST((SELECT MAX(id) FROM users), 1));

-- Assign ADMIN role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT 1, 1
WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = 1 AND role_id = 1);

-- Grant admin access to all applications
INSERT INTO user_applications (user_id, application_id)
SELECT 1, a.id FROM applications a
WHERE NOT EXISTS (
    SELECT 1 FROM user_applications ua
    WHERE ua.user_id = 1 AND ua.application_id = a.id
);


-- ============================================
-- Default Data Loaded Successfully!
-- ============================================
--
-- LOGIN CREDENTIALS:
--   Username: admin
--   Password: admin123
--
-- IMPORTANT: Change the admin password after first login!
--
-- Next Step: Run 03-translations.sql
-- ============================================
