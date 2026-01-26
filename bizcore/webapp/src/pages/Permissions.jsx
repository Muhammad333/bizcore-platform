import { useState, useEffect } from 'react'
import api from '../utils/api'
import { DataTable } from '../components'

export default function Permissions() {
  const [permissions, setPermissions] = useState([])
  const [loading, setLoading] = useState(true)
  const [selectedModule, setSelectedModule] = useState('all')

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      const res = await api.get('/permissions/all')
      setPermissions(res || [])
    } catch (error) {
      console.error('Error loading permissions:', error)
      alert('Failed to load permissions data')
    } finally {
      setLoading(false)
    }
  }

  // Get unique modules
  const modules = [...new Set(permissions.map((p) => p.moduleName || 'Other'))]

  // Filter permissions by module
  const filteredPermissions =
    selectedModule === 'all'
      ? permissions
      : permissions.filter((p) => (p.moduleName || 'Other') === selectedModule)

  const columns = [
    { header: 'Code', accessor: 'code' },
    { header: 'Name', accessor: 'name' },
    { header: 'Description', accessor: 'description' },
    { header: 'Module', accessor: 'moduleName', cell: (m) => m || 'Other' },
  ]

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
      </div>
    )
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Permissions</h2>
        <div className="d-flex align-items-center gap-2">
          <label className="form-label mb-0">Filter by Module:</label>
          <select
            className="form-select"
            style={{ width: '200px' }}
            value={selectedModule}
            onChange={(e) => setSelectedModule(e.target.value)}
          >
            <option value="all">All Modules</option>
            {modules.map((m) => (
              <option key={m} value={m}>
                {m}
              </option>
            ))}
          </select>
        </div>
      </div>

      <DataTable
        columns={columns}
        data={filteredPermissions}
        searchable
        searchPlaceholder="Search permissions..."
      />

      {/* Permission Groups Summary */}
      <div className="row mt-4">
        {modules.map((module) => {
          const modulePermissions = permissions.filter((p) => (p.moduleName || 'Other') === module)
          return (
            <div key={module} className="col-12 col-md-6 col-lg-4 mb-3">
              <div className="stat-card">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <h6 className="mb-1">{module}</h6>
                    <small className="text-muted">{modulePermissions.length} permissions</small>
                  </div>
                  <i className="bi bi-shield-lock text-primary fs-4"></i>
                </div>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}
