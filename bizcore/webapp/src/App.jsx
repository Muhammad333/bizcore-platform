import { useEffect } from 'react'
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import api from './utils/api'
import { Layout } from './components'
import { Dashboard, Users, Roles, Permissions } from './pages'
import { ToastProvider } from './contexts/ToastContext'

function ProtectedRoute({ children }) {
  const navigate = useNavigate()

  useEffect(() => {
    if (!api.isAuthenticated()) {
      window.location.href = '/app/login.html'
    }
  }, [navigate])

  if (!api.isAuthenticated()) {
    return null
  }

  return children
}

export default function App() {
  return (
    <ToastProvider>
      <Routes>
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <Layout />
            </ProtectedRoute>
          }
        >
          <Route index element={<Dashboard />} />
          <Route path="users" element={<Users />} />
          <Route path="roles" element={<Roles />} />
          <Route path="permissions" element={<Permissions />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </ToastProvider>
  )
}
