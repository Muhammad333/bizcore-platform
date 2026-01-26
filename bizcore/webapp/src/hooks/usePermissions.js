import { useMemo } from 'react'
import {
  getUserPermissions,
  getUserRoles,
  hasPermission,
  hasAnyPermission,
  hasAllPermissions,
  hasRole,
  hasAnyRole,
  hasAllRoles,
  PERMISSIONS
} from '../utils/permissions'

/**
 * Hook for checking user permissions and roles
 *
 * @returns {Object} Permission checking functions and user permissions/roles
 *
 * @example
 * const { hasPermission, permissions, canEditUser } = usePermissions()
 *
 * if (hasPermission('USER_EDIT')) {
 *   // Show edit button
 * }
 *
 * if (canEditUser) {
 *   // Another way to check
 * }
 */
export default function usePermissions() {
  const permissions = useMemo(() => getUserPermissions(), [])
  const roles = useMemo(() => getUserRoles(), [])

  // Computed permission checks for common operations
  const computed = useMemo(() => ({
    // User permissions
    canViewUsers: hasPermission(PERMISSIONS.USER_VIEW),
    canCreateUser: hasPermission(PERMISSIONS.USER_CREATE),
    canEditUser: hasPermission(PERMISSIONS.USER_EDIT),
    canDeleteUser: hasPermission(PERMISSIONS.USER_DELETE),
    canManageUsers: hasAnyPermission([PERMISSIONS.USER_CREATE, PERMISSIONS.USER_EDIT, PERMISSIONS.USER_DELETE]),

    // Role permissions
    canViewRoles: hasPermission(PERMISSIONS.ROLE_VIEW),
    canManageRoles: hasPermission(PERMISSIONS.ROLE_MANAGE),

    // Company permissions
    canViewCompany: hasPermission(PERMISSIONS.COMPANY_VIEW),
    canEditCompany: hasPermission(PERMISSIONS.COMPANY_EDIT),

    // Application permissions
    canManageApps: hasPermission(PERMISSIONS.APP_MANAGE),

    // Audit permissions
    canViewAudit: hasPermission(PERMISSIONS.AUDIT_VIEW),

    // Role checks
    isAdmin: hasRole('ADMIN'),
    isManager: hasRole('MANAGER'),
    isEmployee: hasRole('EMPLOYEE'),
  }), [])

  return {
    // Raw data
    permissions,
    roles,

    // Permission checking functions
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    hasRole,
    hasAnyRole,
    hasAllRoles,

    // Computed checks
    ...computed,

    // Permission constants
    PERMISSIONS,
  }
}
