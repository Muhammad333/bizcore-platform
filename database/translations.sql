-- ============================================
-- BizCore Platform - Multi-Language Translations
-- ============================================
-- Description: Translations for all UI elements in English, Russian, and Uzbek
-- Languages: en (English), ru (Russian), uz (Uzbek)
-- Usage: Run this script after init-database.sql
-- ============================================

-- ==================== ENGLISH TRANSLATIONS ====================

-- Common Module (Buttons, Actions, General UI)
INSERT INTO translations (key, language, value, module) VALUES
('common.save',           'en', 'Save',               'common'),
('common.cancel',         'en', 'Cancel',             'common'),
('common.delete',         'en', 'Delete',             'common'),
('common.edit',           'en', 'Edit',               'common'),
('common.create',         'en', 'Create',             'common'),
('common.search',         'en', 'Search',             'common'),
('common.actions',        'en', 'Actions',            'common'),
('common.status',         'en', 'Status',             'common'),
('common.active',         'en', 'Active',             'common'),
('common.inactive',       'en', 'Inactive',           'common'),
('common.adminPanel',     'en', 'Admin Panel',        'common'),
('common.changeLanguage', 'en', 'Change Language',    'common'),
('common.company',        'en', 'Company',            'common'),
('common.role',           'en', 'Role',               'common'),
('common.logout',         'en', 'Logout',             'common'),
('common.confirmLogout',  'en', 'Are you sure you want to logout?', 'common'),

-- Dashboard Module
('dashboard.title',          'en', 'Dashboard',        'dashboard'),
('dashboard.welcome',        'en', 'Welcome',          'dashboard'),
('dashboard.users',          'en', 'Users',            'dashboard'),
('dashboard.roles',          'en', 'Roles',            'dashboard'),
('dashboard.permissions',    'en', 'Permissions',      'dashboard'),
('dashboard.activity',       'en', 'Activity',         'dashboard'),
('dashboard.recentActivity', 'en', 'Recent Activity',  'dashboard'),
('dashboard.noActivity',     'en', 'No recent activity', 'dashboard'),
('dashboard.quickActions',   'en', 'Quick Actions',    'dashboard'),

-- Users Module
('users.title',      'en', 'Users',            'users'),
('users.create',     'en', 'Create User',      'users'),
('users.edit',       'en', 'Edit User',        'users'),
('users.addUser',    'en', 'Add User',         'users'),
('users.username',   'en', 'Username',         'users'),
('users.email',      'en', 'Email',            'users'),
('users.firstName',  'en', 'First Name',       'users'),
('users.lastName',   'en', 'Last Name',        'users'),
('users.roles',      'en', 'Roles',            'users'),
('users.createdAt',  'en', 'Created At',       'users'),

-- Roles Module
('roles.title',       'en', 'Roles',           'roles'),
('roles.create',      'en', 'Create Role',     'roles'),
('roles.edit',        'en', 'Edit Role',       'roles'),
('roles.addRole',     'en', 'Add Role',        'roles'),
('roles.name',        'en', 'Role Name',       'roles'),
('roles.description', 'en', 'Description',     'roles'),
('roles.permissions', 'en', 'Permissions',     'roles'),

-- Permissions Module
('permissions.title',       'en', 'Permissions', 'permissions'),
('permissions.module',      'en', 'Module',      'permissions'),
('permissions.code',        'en', 'Code',        'permissions'),
('permissions.name',        'en', 'Name',        'permissions'),
('permissions.description', 'en', 'Description', 'permissions');


-- ==================== RUSSIAN TRANSLATIONS ====================

-- Common Module (Кнопки, Действия, Общие элементы)
INSERT INTO translations (key, language, value, module) VALUES
('common.save',           'ru', 'Сохранить',          'common'),
('common.cancel',         'ru', 'Отмена',             'common'),
('common.delete',         'ru', 'Удалить',            'common'),
('common.edit',           'ru', 'Редактировать',      'common'),
('common.create',         'ru', 'Создать',            'common'),
('common.search',         'ru', 'Поиск',              'common'),
('common.actions',        'ru', 'Действия',           'common'),
('common.status',         'ru', 'Статус',             'common'),
('common.active',         'ru', 'Активный',           'common'),
('common.inactive',       'ru', 'Неактивный',         'common'),
('common.adminPanel',     'ru', 'Панель администратора', 'common'),
('common.changeLanguage', 'ru', 'Изменить язык',     'common'),
('common.company',        'ru', 'Компания',           'common'),
('common.role',           'ru', 'Роль',               'common'),
('common.logout',         'ru', 'Выход',              'common'),
('common.confirmLogout',  'ru', 'Вы уверены, что хотите выйти?', 'common'),

-- Dashboard Module
('dashboard.title',          'ru', 'Панель управления',   'dashboard'),
('dashboard.welcome',        'ru', 'Добро пожаловать',    'dashboard'),
('dashboard.users',          'ru', 'Пользователи',        'dashboard'),
('dashboard.roles',          'ru', 'Роли',                'dashboard'),
('dashboard.permissions',    'ru', 'Разрешения',          'dashboard'),
('dashboard.activity',       'ru', 'Активность',          'dashboard'),
('dashboard.recentActivity', 'ru', 'Последние действия',  'dashboard'),
('dashboard.noActivity',     'ru', 'Нет недавних действий', 'dashboard'),
('dashboard.quickActions',   'ru', 'Быстрые действия',    'dashboard'),

-- Users Module
('users.title',      'ru', 'Пользователи',              'users'),
('users.create',     'ru', 'Создать пользователя',      'users'),
('users.edit',       'ru', 'Редактировать пользователя', 'users'),
('users.addUser',    'ru', 'Добавить пользователя',     'users'),
('users.username',   'ru', 'Имя пользователя',          'users'),
('users.email',      'ru', 'Email',                      'users'),
('users.firstName',  'ru', 'Имя',                        'users'),
('users.lastName',   'ru', 'Фамилия',                    'users'),
('users.roles',      'ru', 'Роли',                       'users'),
('users.createdAt',  'ru', 'Дата создания',              'users'),

-- Roles Module
('roles.title',       'ru', 'Роли',                 'roles'),
('roles.create',      'ru', 'Создать роль',         'roles'),
('roles.edit',        'ru', 'Редактировать роль',   'roles'),
('roles.addRole',     'ru', 'Добавить роль',        'roles'),
('roles.name',        'ru', 'Название роли',        'roles'),
('roles.description', 'ru', 'Описание',             'roles'),
('roles.permissions', 'ru', 'Разрешения',           'roles'),

-- Permissions Module
('permissions.title',       'ru', 'Разрешения', 'permissions'),
('permissions.module',      'ru', 'Модуль',     'permissions'),
('permissions.code',        'ru', 'Код',        'permissions'),
('permissions.name',        'ru', 'Название',   'permissions'),
('permissions.description', 'ru', 'Описание',   'permissions');


-- ==================== UZBEK TRANSLATIONS ====================

-- Common Module (Tugmalar, Harakatlar, Umumiy elementlar)
INSERT INTO translations (key, language, value, module) VALUES
('common.save',           'uz', 'Saqlash',               'common'),
('common.cancel',         'uz', 'Bekor qilish',          'common'),
('common.delete',         'uz', "O'chirish",             'common'),
('common.edit',           'uz', 'Tahrirlash',            'common'),
('common.create',         'uz', 'Yaratish',              'common'),
('common.search',         'uz', 'Qidirish',              'common'),
('common.actions',        'uz', 'Amallar',               'common'),
('common.status',         'uz', 'Holat',                 'common'),
('common.active',         'uz', 'Faol',                  'common'),
('common.inactive',       'uz', 'Faol emas',             'common'),
('common.adminPanel',     'uz', 'Administrator paneli',  'common'),
('common.changeLanguage', 'uz', "Tilni o'zgartirish",    'common'),
('common.company',        'uz', 'Kompaniya',             'common'),
('common.role',           'uz', 'Rol',                   'common'),
('common.logout',         'uz', 'Chiqish',               'common'),
('common.confirmLogout',  'uz', 'Chiqishni xohlaysizmi?', 'common'),

-- Dashboard Module
('dashboard.title',          'uz', 'Boshqaruv paneli',      'dashboard'),
('dashboard.welcome',        'uz', 'Xush kelibsiz',         'dashboard'),
('dashboard.users',          'uz', 'Foydalanuvchilar',      'dashboard'),
('dashboard.roles',          'uz', 'Rollar',                'dashboard'),
('dashboard.permissions',    'uz', 'Ruxsatlar',             'dashboard'),
('dashboard.activity',       'uz', 'Faollik',               'dashboard'),
('dashboard.recentActivity', 'uz', 'Oxirgi harakatlar',     'dashboard'),
('dashboard.noActivity',     'uz', "Hech qanday harakat yo'q", 'dashboard'),
('dashboard.quickActions',   'uz', 'Tez harakatlar',        'dashboard'),

-- Users Module
('users.title',      'uz', 'Foydalanuvchilar',             'users'),
('users.create',     'uz', 'Foydalanuvchi yaratish',       'users'),
('users.edit',       'uz', 'Foydalanuvchini tahrirlash',   'users'),
('users.addUser',    'uz', "Foydalanuvchi qo'shish",       'users'),
('users.username',   'uz', 'Foydalanuvchi nomi',           'users'),
('users.email',      'uz', 'Email',                         'users'),
('users.firstName',  'uz', 'Ism',                           'users'),
('users.lastName',   'uz', 'Familiya',                      'users'),
('users.roles',      'uz', 'Rollar',                        'users'),
('users.createdAt',  'uz', 'Yaratilgan sana',              'users'),

-- Roles Module
('roles.title',       'uz', 'Rollar',            'roles'),
('roles.create',      'uz', 'Rol yaratish',      'roles'),
('roles.edit',        'uz', 'Rolni tahrirlash',  'roles'),
('roles.addRole',     'uz', "Rol qo'shish",      'roles'),
('roles.name',        'uz', 'Rol nomi',          'roles'),
('roles.description', 'uz', 'Tavsif',            'roles'),
('roles.permissions', 'uz', 'Ruxsatlar',         'roles'),

-- Permissions Module
('permissions.title',       'uz', 'Ruxsatlar', 'permissions'),
('permissions.module',      'uz', 'Modul',     'permissions'),
('permissions.code',        'uz', 'Kod',       'permissions'),
('permissions.name',        'uz', 'Nomi',      'permissions'),
('permissions.description', 'uz', 'Tavsif',    'permissions')

-- Handle conflicts (update if translation key already exists)
ON CONFLICT (key, language) DO UPDATE SET
    value = EXCLUDED.value,
    updated_at = NOW();


-- ============================================
-- Translation Loading Complete!
-- ============================================
-- Loaded translations for:
--   - English (en): 60 keys
--   - Russian (ru): 60 keys
--   - Uzbek (uz):   60 keys
--
-- Total: 180 translation entries
--
-- Modules covered:
--   - common (Common UI elements)
--   - dashboard (Dashboard page)
--   - users (User management)
--   - roles (Role management)
--   - permissions (Permission management)
--
-- To add new translations:
--   1. Add key to all 3 languages (en, ru, uz)
--   2. Follow naming convention: module.key
--   3. Use ON CONFLICT to handle updates
-- ============================================
