import { Navigate } from 'react-router-dom'
import { hasPermission, hasAnyPermission } from '../utils/permissions'

/**
 * ProtectedRoute component - renders children only if user has required permissions
 *
 * @param {Object} props
 * @param {React.ReactNode} props.children - Component to render if authorized
 * @param {string} props.permission - Single permission required (e.g., 'USER_VIEW')
 * @param {Array<string>} props.anyPermission - Array of permissions, user needs at least one
 * @param {string} props.redirectTo - Path to redirect if unauthorized (default: '/')
 * @param {React.ReactNode} props.fallback - Component to render if unauthorized (instead of redirect)
 */
export default function ProtectedRoute({
  children,
  permission,
  anyPermission,
  redirectTo = '/',
  fallback = null
}) {
  let authorized = false

  // Check single permission
  if (permission) {
    authorized = hasPermission(permission)
  }

  // Check multiple permissions (any)
  if (anyPermission && anyPermission.length > 0) {
    authorized = hasAnyPermission(anyPermission)
  }

  // If no permission specified, allow access (protected by outer auth check)
  if (!permission && !anyPermission) {
    authorized = true
  }

  // If not authorized, show fallback or redirect
  if (!authorized) {
    if (fallback) {
      return fallback
    }
    return <Navigate to={redirectTo} replace />
  }

  // Render children if authorized
  return children
}
