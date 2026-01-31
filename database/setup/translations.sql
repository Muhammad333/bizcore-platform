-- ============================================
-- BizCore Platform - Translations (i18n)
-- ============================================
-- Step 3 of 3: Multi-language support
-- Run AFTER 02-default-data.sql
--
-- Languages: en (English), ru (Russian), uz (Uzbek)
-- Uses ON CONFLICT to safely update existing translations
-- ============================================

-- ==================== ENGLISH (en) ====================

-- Common Module
INSERT INTO translations (key, language, value, module) VALUES
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
('common.yes', 'en', 'Yes', 'common'),
('common.no', 'en', 'No', 'common'),
('common.confirm', 'en', 'Confirm', 'common'),
('common.close', 'en', 'Close', 'common'),
('common.loading', 'en', 'Loading...', 'common'),
('common.error', 'en', 'Error', 'common'),
('common.success', 'en', 'Success', 'common'),
('common.warning', 'en', 'Warning', 'common'),
('common.info', 'en', 'Information', 'common'),
('common.adminPanel', 'en', 'Admin Panel', 'common'),
('common.changeLanguage', 'en', 'Change Language', 'common'),
('common.company', 'en', 'Company', 'common'),
('common.role', 'en', 'Role', 'common'),
('common.logout', 'en', 'Logout', 'common'),
('common.confirmLogout', 'en', 'Are you sure you want to logout?', 'common'),
('common.back', 'en', 'Back', 'common'),
('common.next', 'en', 'Next', 'common'),
('common.previous', 'en', 'Previous', 'common'),
('common.submit', 'en', 'Submit', 'common'),
('common.reset', 'en', 'Reset', 'common')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Auth Module
INSERT INTO translations (key, language, value, module) VALUES
('auth.login', 'en', 'Login', 'auth'),
('auth.username', 'en', 'Username', 'auth'),
('auth.password', 'en', 'Password', 'auth'),
('auth.rememberMe', 'en', 'Remember me', 'auth'),
('auth.forgotPassword', 'en', 'Forgot password?', 'auth'),
('auth.loginFailed', 'en', 'Invalid username or password', 'auth'),
('auth.sessionExpired', 'en', 'Your session has expired. Please login again.', 'auth'),
('auth.unauthorized', 'en', 'You are not authorized to access this resource', 'auth')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Dashboard Module
INSERT INTO translations (key, language, value, module) VALUES
('dashboard.title', 'en', 'Dashboard', 'dashboard'),
('dashboard.welcome', 'en', 'Welcome', 'dashboard'),
('dashboard.users', 'en', 'Users', 'dashboard'),
('dashboard.roles', 'en', 'Roles', 'dashboard'),
('dashboard.permissions', 'en', 'Permissions', 'dashboard'),
('dashboard.activity', 'en', 'Activity', 'dashboard'),
('dashboard.recentActivity', 'en', 'Recent Activity', 'dashboard'),
('dashboard.noActivity', 'en', 'No recent activity', 'dashboard'),
('dashboard.quickActions', 'en', 'Quick Actions', 'dashboard'),
('dashboard.totalUsers', 'en', 'Total Users', 'dashboard'),
('dashboard.activeUsers', 'en', 'Active Users', 'dashboard'),
('dashboard.totalRoles', 'en', 'Total Roles', 'dashboard')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Users Module
INSERT INTO translations (key, language, value, module) VALUES
('users.title', 'en', 'Users', 'users'),
('users.create', 'en', 'Create User', 'users'),
('users.edit', 'en', 'Edit User', 'users'),
('users.addUser', 'en', 'Add User', 'users'),
('users.username', 'en', 'Username', 'users'),
('users.email', 'en', 'Email', 'users'),
('users.firstName', 'en', 'First Name', 'users'),
('users.lastName', 'en', 'Last Name', 'users'),
('users.roles', 'en', 'Roles', 'users'),
('users.createdAt', 'en', 'Created At', 'users'),
('users.lastLogin', 'en', 'Last Login', 'users'),
('users.password', 'en', 'Password', 'users'),
('users.confirmPassword', 'en', 'Confirm Password', 'users'),
('users.changePassword', 'en', 'Change Password', 'users'),
('users.deleteConfirm', 'en', 'Are you sure you want to delete this user?', 'users'),
('users.noUsers', 'en', 'No users found', 'users')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Roles Module
INSERT INTO translations (key, language, value, module) VALUES
('roles.title', 'en', 'Roles', 'roles'),
('roles.create', 'en', 'Create Role', 'roles'),
('roles.edit', 'en', 'Edit Role', 'roles'),
('roles.addRole', 'en', 'Add Role', 'roles'),
('roles.name', 'en', 'Role Name', 'roles'),
('roles.description', 'en', 'Description', 'roles'),
('roles.permissions', 'en', 'Permissions', 'roles'),
('roles.users', 'en', 'Users with this role', 'roles'),
('roles.deleteConfirm', 'en', 'Are you sure you want to delete this role?', 'roles'),
('roles.noRoles', 'en', 'No roles found', 'roles'),
('roles.selectPermissions', 'en', 'Select Permissions', 'roles')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Permissions Module
INSERT INTO translations (key, language, value, module) VALUES
('permissions.title', 'en', 'Permissions', 'permissions'),
('permissions.module', 'en', 'Module', 'permissions'),
('permissions.code', 'en', 'Code', 'permissions'),
('permissions.name', 'en', 'Name', 'permissions'),
('permissions.description', 'en', 'Description', 'permissions'),
('permissions.noPermissions', 'en', 'No permissions found', 'permissions')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Validation Messages
INSERT INTO translations (key, language, value, module) VALUES
('validation.required', 'en', 'This field is required', 'validation'),
('validation.email', 'en', 'Please enter a valid email address', 'validation'),
('validation.minLength', 'en', 'Must be at least {0} characters', 'validation'),
('validation.maxLength', 'en', 'Must be no more than {0} characters', 'validation'),
('validation.passwordMismatch', 'en', 'Passwords do not match', 'validation')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();


-- ==================== RUSSIAN (ru) ====================

-- Common Module
INSERT INTO translations (key, language, value, module) VALUES
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
('common.yes', 'ru', 'Да', 'common'),
('common.no', 'ru', 'Нет', 'common'),
('common.confirm', 'ru', 'Подтвердить', 'common'),
('common.close', 'ru', 'Закрыть', 'common'),
('common.loading', 'ru', 'Загрузка...', 'common'),
('common.error', 'ru', 'Ошибка', 'common'),
('common.success', 'ru', 'Успешно', 'common'),
('common.warning', 'ru', 'Предупреждение', 'common'),
('common.info', 'ru', 'Информация', 'common'),
('common.adminPanel', 'ru', 'Панель администратора', 'common'),
('common.changeLanguage', 'ru', 'Изменить язык', 'common'),
('common.company', 'ru', 'Компания', 'common'),
('common.role', 'ru', 'Роль', 'common'),
('common.logout', 'ru', 'Выход', 'common'),
('common.confirmLogout', 'ru', 'Вы уверены, что хотите выйти?', 'common'),
('common.back', 'ru', 'Назад', 'common'),
('common.next', 'ru', 'Далее', 'common'),
('common.previous', 'ru', 'Предыдущий', 'common'),
('common.submit', 'ru', 'Отправить', 'common'),
('common.reset', 'ru', 'Сбросить', 'common')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Auth Module
INSERT INTO translations (key, language, value, module) VALUES
('auth.login', 'ru', 'Войти', 'auth'),
('auth.username', 'ru', 'Имя пользователя', 'auth'),
('auth.password', 'ru', 'Пароль', 'auth'),
('auth.rememberMe', 'ru', 'Запомнить меня', 'auth'),
('auth.forgotPassword', 'ru', 'Забыли пароль?', 'auth'),
('auth.loginFailed', 'ru', 'Неверное имя пользователя или пароль', 'auth'),
('auth.sessionExpired', 'ru', 'Сессия истекла. Пожалуйста, войдите снова.', 'auth'),
('auth.unauthorized', 'ru', 'У вас нет доступа к этому ресурсу', 'auth')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Dashboard Module
INSERT INTO translations (key, language, value, module) VALUES
('dashboard.title', 'ru', 'Панель управления', 'dashboard'),
('dashboard.welcome', 'ru', 'Добро пожаловать', 'dashboard'),
('dashboard.users', 'ru', 'Пользователи', 'dashboard'),
('dashboard.roles', 'ru', 'Роли', 'dashboard'),
('dashboard.permissions', 'ru', 'Разрешения', 'dashboard'),
('dashboard.activity', 'ru', 'Активность', 'dashboard'),
('dashboard.recentActivity', 'ru', 'Последние действия', 'dashboard'),
('dashboard.noActivity', 'ru', 'Нет недавних действий', 'dashboard'),
('dashboard.quickActions', 'ru', 'Быстрые действия', 'dashboard'),
('dashboard.totalUsers', 'ru', 'Всего пользователей', 'dashboard'),
('dashboard.activeUsers', 'ru', 'Активные пользователи', 'dashboard'),
('dashboard.totalRoles', 'ru', 'Всего ролей', 'dashboard')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Users Module
INSERT INTO translations (key, language, value, module) VALUES
('users.title', 'ru', 'Пользователи', 'users'),
('users.create', 'ru', 'Создать пользователя', 'users'),
('users.edit', 'ru', 'Редактировать пользователя', 'users'),
('users.addUser', 'ru', 'Добавить пользователя', 'users'),
('users.username', 'ru', 'Имя пользователя', 'users'),
('users.email', 'ru', 'Email', 'users'),
('users.firstName', 'ru', 'Имя', 'users'),
('users.lastName', 'ru', 'Фамилия', 'users'),
('users.roles', 'ru', 'Роли', 'users'),
('users.createdAt', 'ru', 'Дата создания', 'users'),
('users.lastLogin', 'ru', 'Последний вход', 'users'),
('users.password', 'ru', 'Пароль', 'users'),
('users.confirmPassword', 'ru', 'Подтвердите пароль', 'users'),
('users.changePassword', 'ru', 'Изменить пароль', 'users'),
('users.deleteConfirm', 'ru', 'Вы уверены, что хотите удалить этого пользователя?', 'users'),
('users.noUsers', 'ru', 'Пользователи не найдены', 'users')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Roles Module
INSERT INTO translations (key, language, value, module) VALUES
('roles.title', 'ru', 'Роли', 'roles'),
('roles.create', 'ru', 'Создать роль', 'roles'),
('roles.edit', 'ru', 'Редактировать роль', 'roles'),
('roles.addRole', 'ru', 'Добавить роль', 'roles'),
('roles.name', 'ru', 'Название роли', 'roles'),
('roles.description', 'ru', 'Описание', 'roles'),
('roles.permissions', 'ru', 'Разрешения', 'roles'),
('roles.users', 'ru', 'Пользователи с этой ролью', 'roles'),
('roles.deleteConfirm', 'ru', 'Вы уверены, что хотите удалить эту роль?', 'roles'),
('roles.noRoles', 'ru', 'Роли не найдены', 'roles'),
('roles.selectPermissions', 'ru', 'Выберите разрешения', 'roles')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Permissions Module
INSERT INTO translations (key, language, value, module) VALUES
('permissions.title', 'ru', 'Разрешения', 'permissions'),
('permissions.module', 'ru', 'Модуль', 'permissions'),
('permissions.code', 'ru', 'Код', 'permissions'),
('permissions.name', 'ru', 'Название', 'permissions'),
('permissions.description', 'ru', 'Описание', 'permissions'),
('permissions.noPermissions', 'ru', 'Разрешения не найдены', 'permissions')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Validation Messages
INSERT INTO translations (key, language, value, module) VALUES
('validation.required', 'ru', 'Это поле обязательно', 'validation'),
('validation.email', 'ru', 'Введите корректный email адрес', 'validation'),
('validation.minLength', 'ru', 'Минимум {0} символов', 'validation'),
('validation.maxLength', 'ru', 'Максимум {0} символов', 'validation'),
('validation.passwordMismatch', 'ru', 'Пароли не совпадают', 'validation')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();


-- ==================== UZBEK (uz) ====================

-- Common Module
INSERT INTO translations (key, language, value, module) VALUES
('common.save', 'uz', 'Saqlash', 'common'),
('common.cancel', 'uz', 'Bekor qilish', 'common'),
('common.delete', 'uz', 'O''chirish', 'common'),
('common.edit', 'uz', 'Tahrirlash', 'common'),
('common.create', 'uz', 'Yaratish', 'common'),
('common.search', 'uz', 'Qidirish', 'common'),
('common.actions', 'uz', 'Amallar', 'common'),
('common.status', 'uz', 'Holat', 'common'),
('common.active', 'uz', 'Faol', 'common'),
('common.inactive', 'uz', 'Faol emas', 'common'),
('common.yes', 'uz', 'Ha', 'common'),
('common.no', 'uz', 'Yo''q', 'common'),
('common.confirm', 'uz', 'Tasdiqlash', 'common'),
('common.close', 'uz', 'Yopish', 'common'),
('common.loading', 'uz', 'Yuklanmoqda...', 'common'),
('common.error', 'uz', 'Xatolik', 'common'),
('common.success', 'uz', 'Muvaffaqiyatli', 'common'),
('common.warning', 'uz', 'Ogohlantirish', 'common'),
('common.info', 'uz', 'Ma''lumot', 'common'),
('common.adminPanel', 'uz', 'Administrator paneli', 'common'),
('common.changeLanguage', 'uz', 'Tilni o''zgartirish', 'common'),
('common.company', 'uz', 'Kompaniya', 'common'),
('common.role', 'uz', 'Rol', 'common'),
('common.logout', 'uz', 'Chiqish', 'common'),
('common.confirmLogout', 'uz', 'Chiqishni xohlaysizmi?', 'common'),
('common.back', 'uz', 'Orqaga', 'common'),
('common.next', 'uz', 'Keyingi', 'common'),
('common.previous', 'uz', 'Oldingi', 'common'),
('common.submit', 'uz', 'Yuborish', 'common'),
('common.reset', 'uz', 'Tozalash', 'common')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Auth Module
INSERT INTO translations (key, language, value, module) VALUES
('auth.login', 'uz', 'Kirish', 'auth'),
('auth.username', 'uz', 'Foydalanuvchi nomi', 'auth'),
('auth.password', 'uz', 'Parol', 'auth'),
('auth.rememberMe', 'uz', 'Meni eslab qol', 'auth'),
('auth.forgotPassword', 'uz', 'Parolni unutdingizmi?', 'auth'),
('auth.loginFailed', 'uz', 'Noto''g''ri foydalanuvchi nomi yoki parol', 'auth'),
('auth.sessionExpired', 'uz', 'Sessiya tugadi. Iltimos, qayta kiring.', 'auth'),
('auth.unauthorized', 'uz', 'Sizda bu resursga ruxsat yo''q', 'auth')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Dashboard Module
INSERT INTO translations (key, language, value, module) VALUES
('dashboard.title', 'uz', 'Boshqaruv paneli', 'dashboard'),
('dashboard.welcome', 'uz', 'Xush kelibsiz', 'dashboard'),
('dashboard.users', 'uz', 'Foydalanuvchilar', 'dashboard'),
('dashboard.roles', 'uz', 'Rollar', 'dashboard'),
('dashboard.permissions', 'uz', 'Ruxsatlar', 'dashboard'),
('dashboard.activity', 'uz', 'Faollik', 'dashboard'),
('dashboard.recentActivity', 'uz', 'Oxirgi harakatlar', 'dashboard'),
('dashboard.noActivity', 'uz', 'Hech qanday harakat yo''q', 'dashboard'),
('dashboard.quickActions', 'uz', 'Tez harakatlar', 'dashboard'),
('dashboard.totalUsers', 'uz', 'Jami foydalanuvchilar', 'dashboard'),
('dashboard.activeUsers', 'uz', 'Faol foydalanuvchilar', 'dashboard'),
('dashboard.totalRoles', 'uz', 'Jami rollar', 'dashboard')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Users Module
INSERT INTO translations (key, language, value, module) VALUES
('users.title', 'uz', 'Foydalanuvchilar', 'users'),
('users.create', 'uz', 'Foydalanuvchi yaratish', 'users'),
('users.edit', 'uz', 'Foydalanuvchini tahrirlash', 'users'),
('users.addUser', 'uz', 'Foydalanuvchi qo''shish', 'users'),
('users.username', 'uz', 'Foydalanuvchi nomi', 'users'),
('users.email', 'uz', 'Email', 'users'),
('users.firstName', 'uz', 'Ism', 'users'),
('users.lastName', 'uz', 'Familiya', 'users'),
('users.roles', 'uz', 'Rollar', 'users'),
('users.createdAt', 'uz', 'Yaratilgan sana', 'users'),
('users.lastLogin', 'uz', 'Oxirgi kirish', 'users'),
('users.password', 'uz', 'Parol', 'users'),
('users.confirmPassword', 'uz', 'Parolni tasdiqlang', 'users'),
('users.changePassword', 'uz', 'Parolni o''zgartirish', 'users'),
('users.deleteConfirm', 'uz', 'Bu foydalanuvchini o''chirishni xohlaysizmi?', 'users'),
('users.noUsers', 'uz', 'Foydalanuvchilar topilmadi', 'users')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Roles Module
INSERT INTO translations (key, language, value, module) VALUES
('roles.title', 'uz', 'Rollar', 'roles'),
('roles.create', 'uz', 'Rol yaratish', 'roles'),
('roles.edit', 'uz', 'Rolni tahrirlash', 'roles'),
('roles.addRole', 'uz', 'Rol qo''shish', 'roles'),
('roles.name', 'uz', 'Rol nomi', 'roles'),
('roles.description', 'uz', 'Tavsif', 'roles'),
('roles.permissions', 'uz', 'Ruxsatlar', 'roles'),
('roles.users', 'uz', 'Bu rolga ega foydalanuvchilar', 'roles'),
('roles.deleteConfirm', 'uz', 'Bu rolni o''chirishni xohlaysizmi?', 'roles'),
('roles.noRoles', 'uz', 'Rollar topilmadi', 'roles'),
('roles.selectPermissions', 'uz', 'Ruxsatlarni tanlang', 'roles')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Permissions Module
INSERT INTO translations (key, language, value, module) VALUES
('permissions.title', 'uz', 'Ruxsatlar', 'permissions'),
('permissions.module', 'uz', 'Modul', 'permissions'),
('permissions.code', 'uz', 'Kod', 'permissions'),
('permissions.name', 'uz', 'Nomi', 'permissions'),
('permissions.description', 'uz', 'Tavsif', 'permissions'),
('permissions.noPermissions', 'uz', 'Ruxsatlar topilmadi', 'permissions')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();

-- Validation Messages
INSERT INTO translations (key, language, value, module) VALUES
('validation.required', 'uz', 'Bu maydon majburiy', 'validation'),
('validation.email', 'uz', 'To''g''ri email kiriting', 'validation'),
('validation.minLength', 'uz', 'Kamida {0} ta belgi bo''lishi kerak', 'validation'),
('validation.maxLength', 'uz', 'Ko''pi bilan {0} ta belgi bo''lishi kerak', 'validation'),
('validation.passwordMismatch', 'uz', 'Parollar mos kelmaydi', 'validation')
ON CONFLICT (key, language) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW();


-- ============================================
-- Translations Loaded Successfully!
-- ============================================
-- Languages: English (en), Russian (ru), Uzbek (uz)
-- Modules: common, auth, dashboard, users, roles, permissions, validation
--
-- Database Setup Complete!
-- You can now start the application.
-- ============================================
