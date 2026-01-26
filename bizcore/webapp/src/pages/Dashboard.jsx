import { useState, useEffect } from 'react'
import api from '../utils/api'
import { useTranslation } from '../hooks/useTranslation'
import usePermissions from '../hooks/usePermissions'

export default function Dashboard() {
  const { t } = useTranslation()
  const { canViewUsers, canCreateUser, canViewRoles, canManageRoles } = usePermissions()
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalRoles: 0,
    totalPermissions: 0,
    recentActivity: [],
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadDashboardData()
  }, [])

  const loadDashboardData = async () => {
    try {
      setLoading(true)
      const user = api.getUser()
      const companyId = user?.companyId

      const [usersRes, rolesRes, permissionsRes] = await Promise.all([
        api.get(`/users/company/${companyId}?page=0&size=1`).catch(() => ({ totalElements: 0 })),
        api.get(`/roles/company/${companyId}`).catch(() => []),
        api.get('/permissions?page=0&size=1').catch(() => ({ totalElements: 0 })),
      ])

      setStats({
        totalUsers: usersRes.totalElements || usersRes.length || 0,
        totalRoles: Array.isArray(rolesRes) ? rolesRes.length : 0,
        totalPermissions: permissionsRes.totalElements || 0,
        recentActivity: [],
      })
    } catch (error) {
      console.error('Error loading dashboard:', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
      </div>
    )
  }

  // Filter stat cards based on user permissions
  const allStatCards = [
    { titleKey: 'dashboard.users', value: stats.totalUsers, icon: 'bi-people', color: 'primary', permission: canViewUsers },
    { titleKey: 'dashboard.roles', value: stats.totalRoles, icon: 'bi-shield-check', color: 'success', permission: canViewRoles },
    { titleKey: 'dashboard.permissions', value: stats.totalPermissions, icon: 'bi-key', color: 'warning', permission: canViewRoles },
    { titleKey: 'dashboard.activity', value: stats.recentActivity.length, icon: 'bi-activity', color: 'info', permission: true },
  ]

  const statCards = allStatCards.filter(card => card.permission)

  return (
    <div>
      <h2 className="mb-4">{t('dashboard.title')}</h2>

      {/* Stats Cards */}
      <div className="row g-4 mb-4">
        {statCards.map((card) => (
          <div key={card.titleKey} className="col-12 col-sm-6 col-xl-3">
            <div className="stat-card">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <div className="stat-card-title">{t(card.titleKey)}</div>
                  <div className="stat-card-value">{card.value}</div>
                </div>
                <div className={`stat-card-icon text-${card.color}`}>
                  <i className={`bi ${card.icon}`}></i>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Recent Activity */}
      <div className="row">
        <div className="col-12 col-lg-8">
          <div className="stat-card">
            <h5 className="mb-3">{t('dashboard.recentActivity')}</h5>
            {stats.recentActivity.length === 0 ? (
              <p className="text-muted">{t('dashboard.noActivity')}</p>
            ) : (
              <ul className="list-group list-group-flush">
                {stats.recentActivity.map((activity, index) => (
                  <li key={index} className="list-group-item">
                    {activity.action} - {activity.timestamp}
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>

        {/* Quick Actions - only show if user has any permissions */}
        {(canCreateUser || canManageRoles) && (
          <div className="col-12 col-lg-4">
            <div className="stat-card">
              <h5 className="mb-3">{t('dashboard.quickActions')}</h5>
              <div className="d-grid gap-2">
                {canCreateUser && (
                  <a href="/app/users" className="btn btn-primary">
                    <i className="bi bi-person-plus me-2"></i>
                    {t('users.addUser')}
                  </a>
                )}
                {canManageRoles && (
                  <a href="/app/roles" className="btn btn-success">
                    <i className="bi bi-shield-plus me-2"></i>
                    {t('roles.addRole')}
                  </a>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}
