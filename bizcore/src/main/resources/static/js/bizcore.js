/**
 * BizCore - Core JavaScript Framework
 * Provides authentication, API calls, menu rendering, and utilities
 */
const BizCore = {
    // Storage keys
    STORAGE_KEYS: {
        TOKEN: 'bizcore_token',
        USER: 'bizcore_user',
        COMPANY: 'bizcore_company',
        APPLICATIONS: 'bizcore_apps',
        MENUS: 'bizcore_menus',
        THEME: 'bizcore_theme'
    },

    // Set authentication data after login
    setAuth(data) {
        localStorage.setItem(this.STORAGE_KEYS.TOKEN, data.token);
        localStorage.setItem(this.STORAGE_KEYS.USER, JSON.stringify(data.user));
        localStorage.setItem(this.STORAGE_KEYS.COMPANY, JSON.stringify(data.company));
        localStorage.setItem(this.STORAGE_KEYS.APPLICATIONS, JSON.stringify(data.applications || []));
        localStorage.setItem(this.STORAGE_KEYS.MENUS, JSON.stringify(data.menus || []));
        localStorage.setItem(this.STORAGE_KEYS.THEME, JSON.stringify(data.theme || {}));

        if (data.theme) {
            this.applyTheme(data.theme);
        }
    },

    // Clear authentication data
    clearAuth() {
        Object.values(this.STORAGE_KEYS).forEach(key => localStorage.removeItem(key));
    },

    // Check if user is authenticated
    isAuthenticated() {
        return !!localStorage.getItem(this.STORAGE_KEYS.TOKEN);
    },

    // Get current token
    getToken() {
        return localStorage.getItem(this.STORAGE_KEYS.TOKEN);
    },

    // Get current user
    getUser() {
        const user = localStorage.getItem(this.STORAGE_KEYS.USER);
        return user ? JSON.parse(user) : null;
    },

    // Get current company
    getCompany() {
        const company = localStorage.getItem(this.STORAGE_KEYS.COMPANY);
        return company ? JSON.parse(company) : null;
    },

    // Get applications
    getApplications() {
        const apps = localStorage.getItem(this.STORAGE_KEYS.APPLICATIONS);
        return apps ? JSON.parse(apps) : [];
    },

    // Get menus
    getMenus() {
        const menus = localStorage.getItem(this.STORAGE_KEYS.MENUS);
        return menus ? JSON.parse(menus) : [];
    },

    // Get menus for specific application
    getMenusForApp(appCode) {
        return this.getMenus().filter(menu => menu.applicationCode === appCode);
    },

    // Get theme
    getTheme() {
        const theme = localStorage.getItem(this.STORAGE_KEYS.THEME);
        return theme ? JSON.parse(theme) : {};
    },

    // Apply theme CSS variables
    applyTheme(theme) {
        const root = document.documentElement;
        if (theme.primaryColor) root.style.setProperty('--primary-color', theme.primaryColor);
        if (theme.secondaryColor) root.style.setProperty('--secondary-color', theme.secondaryColor);
        if (theme.accentColor) root.style.setProperty('--accent-color', theme.accentColor);
        if (theme.backgroundColor) root.style.setProperty('--background-color', theme.backgroundColor);
        if (theme.textColor) root.style.setProperty('--text-color', theme.textColor);
        if (theme.errorColor) root.style.setProperty('--error-color', theme.errorColor);
        if (theme.successColor) root.style.setProperty('--success-color', theme.successColor);
        if (theme.warningColor) root.style.setProperty('--warning-color', theme.warningColor);
        if (theme.fontFamily) root.style.setProperty('--font-family', theme.fontFamily);
        if (theme.borderRadius) root.style.setProperty('--border-radius', theme.borderRadius);
    },

    // Check if user has role
    hasRole(role) {
        const user = this.getUser();
        return user && user.roles && user.roles.includes(role);
    },

    // Check if user has any of the roles
    hasAnyRole(...roles) {
        const user = this.getUser();
        if (!user || !user.roles) return false;
        return roles.some(role => user.roles.includes(role));
    },

    // API request helper
    async api(url, options = {}) {
        const token = this.getToken();
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(url, {
            ...options,
            headers
        });

        if (response.status === 401) {
            this.clearAuth();
            window.location.href = '/login.html';
            return;
        }

        return response.json();
    },

    // GET request
    async get(url) {
        return this.api(url, { method: 'GET' });
    },

    // POST request
    async post(url, data) {
        return this.api(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    // PUT request
    async put(url, data) {
        return this.api(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },

    // DELETE request
    async delete(url) {
        return this.api(url, { method: 'DELETE' });
    },

    // Logout
    logout() {
        this.clearAuth();
        window.location.href = '/login.html';
    },

    // Render sidebar menu
    renderMenu(containerId, appCode) {
        const container = document.getElementById(containerId);
        if (!container) return;

        const menus = this.getMenusForApp(appCode);
        let html = '<nav class="nav-menu">';

        menus.forEach(menu => {
            html += this.renderMenuItem(menu);
        });

        html += '</nav>';
        container.innerHTML = html;
    },

    renderMenuItem(menu) {
        let html = '';
        const hasChildren = menu.children && menu.children.length > 0;
        const isActive = window.location.pathname.includes(menu.path);

        html += `<a href="${menu.path || '#'}" class="nav-item ${isActive ? 'active' : ''}" data-menu="${menu.code}">`;
        html += `<span class="nav-item-icon">${menu.icon || '📄'}</span>`;
        html += `<span class="nav-item-text">${menu.title}</span>`;
        html += '</a>';

        if (hasChildren) {
            html += '<div class="nav-submenu">';
            menu.children.forEach(child => {
                html += this.renderMenuItem(child);
            });
            html += '</div>';
        }

        return html;
    },

    // Render user info in header
    renderUserInfo(containerId) {
        const container = document.getElementById(containerId);
        if (!container) return;

        const user = this.getUser();
        if (!user) return;

        const initials = (user.firstName?.charAt(0) || '') + (user.lastName?.charAt(0) || user.username?.charAt(0) || '');

        container.innerHTML = `
            <div class="user-menu" onclick="BizCore.toggleUserDropdown()">
                <div class="user-info">
                    <div class="user-name">${user.firstName || ''} ${user.lastName || user.username}</div>
                    <div class="user-role">${user.roles?.[0] || 'User'}</div>
                </div>
                <div class="user-avatar">${initials.toUpperCase()}</div>
            </div>
            <div id="userDropdown" class="user-dropdown" style="display:none;">
                <a href="/profile.html">Profile</a>
                <a href="/settings.html">Settings</a>
                <hr>
                <a href="#" onclick="BizCore.logout(); return false;">Logout</a>
            </div>
        `;
    },

    toggleUserDropdown() {
        const dropdown = document.getElementById('userDropdown');
        if (dropdown) {
            dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none';
        }
    },

    // Render app switcher
    renderAppSwitcher(containerId) {
        const container = document.getElementById(containerId);
        if (!container) return;

        const apps = this.getApplications();
        let html = '<div class="app-switcher">';

        apps.forEach(app => {
            html += `
                <div class="app-item" onclick="window.location.href='${app.baseUrl}'" title="${app.name}">
                    <span>${app.icon || '📱'}</span>
                    <span>${app.name}</span>
                </div>
            `;
        });

        html += '</div>';
        container.innerHTML = html;
    },

    // Require authentication
    requireAuth() {
        if (!this.isAuthenticated()) {
            window.location.href = '/login.html';
            return false;
        }
        return true;
    },

    // Initialize on page load
    init(appCode) {
        if (!this.requireAuth()) return;

        const theme = this.getTheme();
        if (theme) this.applyTheme(theme);

        if (appCode) {
            this.renderMenu('sidebarMenu', appCode);
        }
        this.renderUserInfo('userInfo');
    }
};

// Auto-apply theme on load
document.addEventListener('DOMContentLoaded', () => {
    const theme = BizCore.getTheme();
    if (theme && Object.keys(theme).length > 0) {
        BizCore.applyTheme(theme);
    }
});
