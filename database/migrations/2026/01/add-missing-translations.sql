-- Add missing translation keys for Russian and Uzbek
-- Date: 2026-01-26

-- Common translations
INSERT INTO translations ("key", language, value, module) VALUES
('common.adminPanel', 'en', 'Admin Panel', 'COMMON'),
('common.adminPanel', 'ru', 'Панель администратора', 'COMMON'),
('common.adminPanel', 'uz', 'Administrator paneli', 'COMMON'),

('common.changeLanguage', 'en', 'Change Language', 'COMMON'),
('common.changeLanguage', 'ru', 'Изменить язык', 'COMMON'),
('common.changeLanguage', 'uz', 'Tilni o''zgartirish', 'COMMON'),

('common.company', 'en', 'Company', 'COMMON'),
('common.company', 'ru', 'Компания', 'COMMON'),
('common.company', 'uz', 'Kompaniya', 'COMMON'),

('common.role', 'en', 'Role', 'COMMON'),
('common.role', 'ru', 'Роль', 'COMMON'),
('common.role', 'uz', 'Rol', 'COMMON'),

('common.logout', 'en', 'Logout', 'COMMON'),
('common.logout', 'ru', 'Выход', 'COMMON'),
('common.logout', 'uz', 'Chiqish', 'COMMON'),

('common.confirmLogout', 'en', 'Are you sure you want to logout?', 'COMMON'),
('common.confirmLogout', 'ru', 'Вы уверены, что хотите выйти?', 'COMMON'),
('common.confirmLogout', 'uz', 'Chiqishni xohlaysizmi?', 'COMMON'),

-- Dashboard translations
('dashboard.activity', 'en', 'Activity', 'DASHBOARD'),
('dashboard.activity', 'ru', 'Активность', 'DASHBOARD'),
('dashboard.activity', 'uz', 'Faollik', 'DASHBOARD'),

('dashboard.recentActivity', 'en', 'Recent Activity', 'DASHBOARD'),
('dashboard.recentActivity', 'ru', 'Последние действия', 'DASHBOARD'),
('dashboard.recentActivity', 'uz', 'Oxirgi harakatlar', 'DASHBOARD'),

('dashboard.noActivity', 'en', 'No recent activity', 'DASHBOARD'),
('dashboard.noActivity', 'ru', 'Нет недавних действий', 'DASHBOARD'),
('dashboard.noActivity', 'uz', 'Hech qanday harakat yo''q', 'DASHBOARD'),

('dashboard.quickActions', 'en', 'Quick Actions', 'DASHBOARD'),
('dashboard.quickActions', 'ru', 'Быстрые действия', 'DASHBOARD'),
('dashboard.quickActions', 'uz', 'Tez harakatlar', 'DASHBOARD'),

-- Users translations
('users.addUser', 'en', 'Add User', 'USER'),
('users.addUser', 'ru', 'Добавить пользователя', 'USER'),
('users.addUser', 'uz', 'Foydalanuvchi qo''shish', 'USER'),

-- Roles translations
('roles.addRole', 'en', 'Add Role', 'ROLE'),
('roles.addRole', 'ru', 'Добавить роль', 'ROLE'),
('roles.addRole', 'uz', 'Rol qo''shish', 'ROLE')

ON CONFLICT ("key", language) DO UPDATE SET
    value = EXCLUDED.value,
    updated_at = NOW();
