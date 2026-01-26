import { useState, useEffect } from 'react'
import api from '../utils/api'
import { DataTable, Modal, FormInput, ConfirmModal } from '../components'

export default function Roles() {
  const [roles, setRoles] = useState([])
  const [permissions, setPermissions] = useState([])
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)
  const [showDeleteModal, setShowDeleteModal] = useState(false)
  const [editingRole, setEditingRole] = useState(null)
  const [deletingRole, setDeletingRole] = useState(null)
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    permissionIds: [],
  })
  const [formErrors, setFormErrors] = useState({})
  const [saving, setSaving] = useState(false)

  const user = api.getUser()
  const companyId = user?.companyId

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      const [rolesRes, permissionsRes] = await Promise.all([
        api.get(`/roles/company/${companyId}`),
        api.get('/permissions/all'),
      ])
      setRoles(rolesRes || [])
      setPermissions(permissionsRes || [])
    } catch (error) {
      console.error('Error loading data:', error)
      alert('Failed to load roles data')
    } finally {
      setLoading(false)
    }
  }

  const validateForm = () => {
    const errors = {}
    if (!formData.name?.trim()) {
      errors.name = 'Role name is required'
    }
    setFormErrors(errors)
    return Object.keys(errors).length === 0
  }

  const handleOpenAddModal = () => {
    setEditingRole(null)
    setFormData({
      name: '',
      description: '',
      permissionIds: [],
    })
    setFormErrors({})
    setShowModal(true)
  }

  const handleOpenEditModal = (role) => {
    setEditingRole(role)
    setFormData({
      name: role.name || '',
      description: role.description || '',
      permissionIds: role.permissions?.map((p) => p.id) || [],
    })
    setFormErrors({})
    setShowModal(true)
  }

  const handleCloseModal = () => {
    setShowModal(false)
    setEditingRole(null)
    setFormErrors({})
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    if (formErrors[name]) {
      setFormErrors((prev) => ({ ...prev, [name]: '' }))
    }
  }

  const handlePermissionsChange = (e) => {
    const selectedOptions = Array.from(e.target.selectedOptions, (opt) => parseInt(opt.value))
    setFormData((prev) => ({ ...prev, permissionIds: selectedOptions }))
  }

  const handleSubmit = async () => {
    if (!validateForm()) return

    try {
      setSaving(true)
      const payload = { ...formData, companyId }

      if (editingRole) {
        await api.put(`/roles/${editingRole.id}`, payload)
      } else {
        await api.post('/roles', payload)
      }

      await loadData()
      handleCloseModal()
      alert(`Role ${editingRole ? 'updated' : 'created'} successfully`)
    } catch (error) {
      console.error('Error saving role:', error)
      alert(error.message || 'Failed to save role')
    } finally {
      setSaving(false)
    }
  }

  const handleOpenDeleteModal = (role) => {
    setDeletingRole(role)
    setShowDeleteModal(true)
  }

  const handleDelete = async () => {
    try {
      await api.delete(`/roles/${deletingRole.id}`)
      await loadData()
      setShowDeleteModal(false)
      setDeletingRole(null)
      alert('Role deleted successfully')
    } catch (error) {
      console.error('Error deleting role:', error)
      alert(error.message || 'Failed to delete role')
    }
  }

  // Group permissions by module
  const permissionsByModule = permissions.reduce((acc, perm) => {
    const module = perm.moduleName || 'Other'
    if (!acc[module]) acc[module] = []
    acc[module].push(perm)
    return acc
  }, {})

  const columns = [
    { header: 'Name', accessor: 'name' },
    { header: 'Description', accessor: 'description' },
    {
      header: 'Permissions',
      accessor: 'permissions',
      cell: (perms) => (
        <span>{perms?.length || 0} permissions</span>
      ),
    },
    {
      header: 'System Role',
      accessor: 'systemRole',
      cell: (isSystem) => (
        <span className={isSystem ? 'badge-active' : 'badge-inactive'}>
          {isSystem ? 'Yes' : 'No'}
        </span>
      ),
    },
    {
      header: 'Actions',
      accessor: 'id',
      sortable: false,
      cell: (_, row) => (
        <div className="action-buttons">
          <button
            className="btn btn-sm btn-secondary"
            onClick={() => handleOpenEditModal(row)}
            disabled={row.systemRole}
          >
            Edit
          </button>
          <button
            className="btn btn-sm btn-danger"
            onClick={() => handleOpenDeleteModal(row)}
            disabled={row.systemRole}
          >
            Delete
          </button>
        </div>
      ),
    },
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
        <h2>Roles Management</h2>
        <button className="btn btn-primary" onClick={handleOpenAddModal}>
          <i className="bi bi-plus-lg me-2"></i>
          Add Role
        </button>
      </div>

      <DataTable
        columns={columns}
        data={roles}
        searchable
        searchPlaceholder="Search roles..."
      />

      {/* Add/Edit Modal */}
      <Modal
        isOpen={showModal}
        onClose={handleCloseModal}
        title={editingRole ? 'Edit Role' : 'Add Role'}
        size="lg"
        footer={
          <>
            <button className="btn btn-secondary" onClick={handleCloseModal}>
              Cancel
            </button>
            <button className="btn btn-primary" onClick={handleSubmit} disabled={saving}>
              {saving ? 'Saving...' : editingRole ? 'Update' : 'Create'}
            </button>
          </>
        }
      >
        <FormInput
          label="Role Name"
          name="name"
          value={formData.name}
          onChange={handleInputChange}
          error={formErrors.name}
          required
        />
        <FormInput
          label="Description"
          type="textarea"
          name="description"
          value={formData.description}
          onChange={handleInputChange}
          rows={2}
        />

        <div className="form-group">
          <label className="form-label">Permissions</label>
          <div className="border rounded p-3" style={{ maxHeight: '300px', overflowY: 'auto' }}>
            {Object.entries(permissionsByModule).map(([module, perms]) => (
              <div key={module} className="mb-3">
                <h6 className="text-muted">{module}</h6>
                <div className="row">
                  {perms.map((perm) => (
                    <div key={perm.id} className="col-md-6">
                      <div className="form-check">
                        <input
                          type="checkbox"
                          className="form-check-input"
                          id={`perm-${perm.id}`}
                          checked={formData.permissionIds.includes(perm.id)}
                          onChange={(e) => {
                            if (e.target.checked) {
                              setFormData((prev) => ({
                                ...prev,
                                permissionIds: [...prev.permissionIds, perm.id],
                              }))
                            } else {
                              setFormData((prev) => ({
                                ...prev,
                                permissionIds: prev.permissionIds.filter((id) => id !== perm.id),
                              }))
                            }
                          }}
                        />
                        <label className="form-check-label" htmlFor={`perm-${perm.id}`}>
                          {perm.name}
                        </label>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </div>
      </Modal>

      {/* Delete Confirmation Modal */}
      <ConfirmModal
        isOpen={showDeleteModal}
        onClose={() => {
          setShowDeleteModal(false)
          setDeletingRole(null)
        }}
        onConfirm={handleDelete}
        title="Delete Role"
        message={`Are you sure you want to delete role "${deletingRole?.name}"? This action cannot be undone.`}
        confirmText="Delete"
      />
    </div>
  )
}
