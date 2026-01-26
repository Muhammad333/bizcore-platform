import { useState, useMemo } from 'react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import api from '../utils/api'
import { useTranslation } from '../hooks/useTranslation'
import usePermissions from '../hooks/usePermissions'

export default function Layout() {
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false)
  const navigate = useNavigate()
  const user = api.getUser()
  const { t, language, changeLanguage, availableLanguages } = useTranslation()
  const { hasPermission, hasAnyPermission } = usePermissions()

  // Menu items - using translation keys with permission checks
  const allMenuItems = [
    { id: 'dashboard', labelKey: 'dashboard.title', icon: 'bi-speedometer2', path: '/', permission: null },
    { id: 'users', labelKey: 'users.title', icon: 'bi-people', path: '/users', permission: 'USER_VIEW' },
    { id: 'roles', labelKey: 'roles.title', icon: 'bi-shield-check', path: '/roles', permission: 'ROLE_VIEW' },
    { id: 'permissions', labelKey: 'permissions.title', icon: 'bi-key', path: '/permissions', anyPermission: ['ROLE_MANAGE', 'ROLE_VIEW'] },
  ]

  // Filter menu items based on user permissions
  const menuItems = useMemo(() => {
    return allMenuItems.filter(item => {
      // Dashboard is always visible
      if (!item.permission && !item.anyPermission) return true

      // Check single permission
      if (item.permission) {
        return hasPermission(item.permission)
      }

      // Check any of multiple permissions
      if (item.anyPermission) {
        return hasAnyPermission(item.anyPermission)
      }

      return false
    })
  }, [hasPermission, hasAnyPermission])

  const handleLogout = () => {
    if (confirm(t('common.confirmLogout') || 'Are you sure you want to logout?')) {
      api.logout()
      navigate('/login')
    }
  }

  const languageNames = {
    'en': 'English',
    'uz': "O'zbek",
    'ru': 'Русский'
  }

  const languageFlags = {
    'en': '🇺🇸',
    'uz': '🇺🇿',
    'ru': '🇷🇺'
  }

  const getInitials = (user) => {
    if (!user) return 'U'
    const first = user.firstName?.[0] || ''
    const last = user.lastName?.[0] || ''
    return (first + last).toUpperCase() || 'U'
  }

  return (
    <div className="app-container">
      {/* Sidebar */}
      <aside className={`sidebar ${sidebarCollapsed ? 'collapsed' : ''}`}>
        <div className="sidebar-header">
          <a href="/" className="sidebar-brand">
            <i className="bi bi-grid-fill me-2"></i>
            <span className="sidebar-brand-text">BizCore</span>
          </a>
          <button
            className="sidebar-toggle"
            onClick={() => setSidebarCollapsed(!sidebarCollapsed)}
          >
            <i className={`bi ${sidebarCollapsed ? 'bi-chevron-right' : 'bi-chevron-left'}`}></i>
          </button>
        </div>

        <nav className="sidebar-nav">
          {menuItems.map((item) => (
            <NavLink
              key={item.id}
              to={item.path}
              className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
              end={item.path === '/'}
            >
              <i className={`bi ${item.icon}`}></i>
              <span>{t(item.labelKey)}</span>
            </NavLink>
          ))}
        </nav>
      </aside>

      {/* Main Content */}
      <main className="main-content">
        {/* Topbar */}
        <header className="topbar">
          <h1 className="topbar-title">{t('common.adminPanel')}</h1>

          <div className="topbar-actions">
            {/* Language Selector */}
            <div className="dropdown me-3">
              <button
                className="btn btn-outline-secondary dropdown-toggle"
                type="button"
                data-bs-toggle="dropdown"
                title={t('common.changeLanguage')}
              >
                <span className="me-1">{languageFlags[language]}</span>
                <span className="d-none d-md-inline">{languageNames[language]}</span>
              </button>
              <ul className="dropdown-menu">
                {availableLanguages.map((lang) => (
                  <li key={lang}>
                    <button
                      className={`dropdown-item ${lang === language ? 'active' : ''}`}
                      onClick={() => changeLanguage(lang)}
                    >
                      <span className="me-2">{languageFlags[lang]}</span>
                      {languageNames[lang]}
                      {lang === language && <i className="bi bi-check2 ms-2"></i>}
                    </button>
                  </li>
                ))}
              </ul>
            </div>

            {/* User Dropdown */}
            <div className="dropdown">
              <button
                className="btn btn-link text-decoration-none dropdown-toggle"
                type="button"
                data-bs-toggle="dropdown"
              >
                <div className="user-dropdown">
                  <div className="user-avatar">{getInitials(user)}</div>
                  <span className="d-none d-md-inline">
                    {user?.firstName || 'User'} {user?.lastName || ''}
                  </span>
                </div>
              </button>
              <ul className="dropdown-menu dropdown-menu-end">
                <li>
                  <div className="dropdown-item-text">
                    <div><strong>{user?.firstName} {user?.lastName}</strong></div>
                    <small className="text-muted">{user?.email}</small>
                  </div>
                </li>
                <li><hr className="dropdown-divider" /></li>
                <li>
                  <div className="dropdown-item-text">
                    <small className="text-muted">{t('common.company')}: {user?.companyName}</small>
                  </div>
                </li>
                <li>
                  <div className="dropdown-item-text">
                    <small className="text-muted">{t('common.role')}: {user?.roles?.join(', ')}</small>
                  </div>
                </li>
                <li><hr className="dropdown-divider" /></li>
                <li>
                  <button className="dropdown-item text-danger" onClick={handleLogout}>
                    <i className="bi bi-box-arrow-right me-2"></i>
                    {t('common.logout')}
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </header>

        {/* Page Content */}
        <div className="page-content">
          <Outlet />
        </div>
      </main>
    </div>
  )
}
