/**
 * Permission utility for checking user permissions and roles
 */

/**
 * Get user permissions from localStorage
 * @returns {Array<string>} Array of permission codes
 */
export const getUserPermissions = () => {
  try {
    const userStr = localStorage.getItem('bizcore_user')
    if (!userStr) return []

    const user = JSON.parse(userStr)
    return user.permissions || []
  } catch (error) {
    console.error('Error getting user permissions:', error)
    return []
  }
}

/**
 * Get user roles from localStorage
 * @returns {Array<string>} Array of role names
 */
export const getUserRoles = () => {
  try {
    const userStr = localStorage.getItem('bizcore_user')
    if (!userStr) return []

    const user = JSON.parse(userStr)
    return user.roles || []
  } catch (error) {
    console.error('Error getting user roles:', error)
    return []
  }
}

/**
 * Check if user has a specific permission
 * @param {string} permission - Permission code (e.g., 'USER_VIEW')
 * @returns {boolean}
 */
export const hasPermission = (permission) => {
  const permissions = getUserPermissions()
  return permissions.includes(permission)
}

/**
 * Check if user has any of the specified permissions
 * @param {Array<string>} permissions - Array of permission codes
 * @returns {boolean}
 */
export const hasAnyPermission = (permissions) => {
  const userPermissions = getUserPermissions()
  return permissions.some(perm => userPermissions.includes(perm))
}

/**
 * Check if user has all of the specified permissions
 * @param {Array<string>} permissions - Array of permission codes
 * @returns {boolean}
 */
export const hasAllPermissions = (permissions) => {
  const userPermissions = getUserPermissions()
  return permissions.every(perm => userPermissions.includes(perm))
}

/**
 * Check if user has a specific role
 * @param {string} role - Role name (e.g., 'ADMIN')
 * @returns {boolean}
 */
export const hasRole = (role) => {
  const roles = getUserRoles()
  return roles.includes(role)
}

/**
 * Check if user has any of the specified roles
 * @param {Array<string>} roles - Array of role names
 * @returns {boolean}
 */
export const hasAnyRole = (roles) => {
  const userRoles = getUserRoles()
  return roles.some(role => userRoles.includes(role))
}

/**
 * Check if user has all of the specified roles
 * @param {Array<string>} roles - Array of role names
 * @returns {boolean}
 */
export const hasAllRoles = (roles) => {
  const userRoles = getUserRoles()
  return roles.every(role => userRoles.includes(role))
}

/**
 * Permission constants for easy reference
 */
export const PERMISSIONS = {
  // User permissions
  USER_VIEW: 'USER_VIEW',
  USER_CREATE: 'USER_CREATE',
  USER_EDIT: 'USER_EDIT',
  USER_DELETE: 'USER_DELETE',

  // Role permissions
  ROLE_VIEW: 'ROLE_VIEW',
  ROLE_MANAGE: 'ROLE_MANAGE',

  // Company permissions
  COMPANY_VIEW: 'COMPANY_VIEW',
  COMPANY_EDIT: 'COMPANY_EDIT',

  // Application permissions
  APP_MANAGE: 'APP_MANAGE',

  // Audit permissions
  AUDIT_VIEW: 'AUDIT_VIEW',
}

export default {
  getUserPermissions,
  getUserRoles,
  hasPermission,
  hasAnyPermission,
  hasAllPermissions,
  hasRole,
  hasAnyRole,
  hasAllRoles,
  PERMISSIONS,
}
