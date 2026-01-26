import { useState, useEffect } from 'react'
import api from '../utils/api'
import { DataTable, Modal, FormInput, MultiSelect, ConfirmModal } from '../components'

export default function Users() {
  const [users, setUsers] = useState([])
  const [roles, setRoles] = useState([])
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)
  const [showDeleteModal, setShowDeleteModal] = useState(false)
  const [editingUser, setEditingUser] = useState(null)
  const [deletingUser, setDeletingUser] = useState(null)
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
    password: '',
    passwordConfirm: '',
    active: true,
    roleIds: [],
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
      const [usersRes, rolesRes] = await Promise.all([
        api.get(`/users/company/${companyId}`),
        api.get(`/roles/company/${companyId}`),
      ])
      setUsers(usersRes.content || usersRes || [])
      setRoles(rolesRes || [])
    } catch (error) {
      console.error('Error loading data:', error)
      alert('Failed to load users data')
    } finally {
      setLoading(false)
    }
  }

  const validateForm = () => {
    const errors = {}
    if (!formData.username?.trim()) {
      errors.username = 'Username is required'
    }
    if (!formData.email?.trim()) {
      errors.email = 'Email is required'
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      errors.email = 'Invalid email format'
    }
    if (!formData.firstName?.trim()) {
      errors.firstName = 'First name is required'
    }
    if (!formData.lastName?.trim()) {
      errors.lastName = 'Last name is required'
    }
    if (!formData.roleIds || formData.roleIds.length === 0) {
      errors.roleIds = 'At least one role must be selected'
    }

    // Password validation only for new users
    if (!editingUser) {
      if (!formData.password?.trim()) {
        errors.password = 'Password is required'
      } else if (formData.password.length < 8) {
        errors.password = 'Password must be at least 8 characters'
      }
      if (formData.password !== formData.passwordConfirm) {
        errors.passwordConfirm = 'Passwords do not match'
      }
    }

    setFormErrors(errors)
    return Object.keys(errors).length === 0
  }

  const handleOpenAddModal = () => {
    setEditingUser(null)
    setFormData({
      username: '',
      email: '',
      firstName: '',
      lastName: '',
      phoneNumber: '',
      password: '',
      passwordConfirm: '',
      active: true,
      roleIds: [],
    })
    setFormErrors({})
    setShowModal(true)
  }

  const handleOpenEditModal = (user) => {
    setEditingUser(user)
    setFormData({
      username: user.username || '',
      email: user.email || '',
      firstName: user.firstName || '',
      lastName: user.lastName || '',
      phoneNumber: user.phoneNumber || '',
      password: '',
      passwordConfirm: '',
      active: user.active !== false,
      roleIds: user.roleIds || [],
    })
    setFormErrors({})
    setShowModal(true)
  }

  const handleCloseModal = () => {
    setShowModal(false)
    setEditingUser(null)
    setFormErrors({})
  }

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }))
    if (formErrors[name]) {
      setFormErrors((prev) => ({ ...prev, [name]: '' }))
    }
  }

  const handleSubmit = async () => {
    if (!validateForm()) return

    try {
      setSaving(true)
      // Remove passwordConfirm from payload, it's only for validation
      const { passwordConfirm, ...dataToSend } = formData
      const payload = { ...dataToSend, companyId }

      if (editingUser) {
        await api.put(`/users/${editingUser.id}`, payload)
      } else {
        await api.post('/users', payload)
      }

      await loadData()
      handleCloseModal()
      alert(`User ${editingUser ? 'updated' : 'created'} successfully`)
    } catch (error) {
      console.error('Error saving user:', error)
      alert(error.message || 'Failed to save user')
    } finally {
      setSaving(false)
    }
  }

  const handleOpenDeleteModal = (user) => {
    setDeletingUser(user)
    setShowDeleteModal(true)
  }

  const handleDelete = async () => {
    try {
      await api.delete(`/users/${deletingUser.id}`)
      await loadData()
      setShowDeleteModal(false)
      setDeletingUser(null)
      alert('User deleted successfully')
    } catch (error) {
      console.error('Error deleting user:', error)
      alert(error.message || 'Failed to delete user')
    }
  }

  const handleToggleActive = async (user) => {
    try {
      const endpoint = user.active
        ? `/users/${user.id}/deactivate`
        : `/users/${user.id}/activate`
      await api.put(endpoint, {})
      await loadData()
      alert(`User ${user.active ? 'deactivated' : 'activated'} successfully`)
    } catch (error) {
      console.error('Error toggling user status:', error)
      alert(error.message || 'Failed to update user status')
    }
  }

  const columns = [
    { header: 'Username', accessor: 'username' },
    { header: 'Email', accessor: 'email' },
    { header: 'First Name', accessor: 'firstName' },
    { header: 'Last Name', accessor: 'lastName' },
    {
      header: 'Roles',
      accessor: 'roles',
      cell: (roleNames) =>
        roleNames?.map((roleName, index) => (
          <span key={index} className="badge-role">
            {roleName}
          </span>
        )) || '-',
    },
    {
      header: 'Status',
      accessor: 'active',
      cell: (active) => (
        <span className={active ? 'badge-active' : 'badge-inactive'}>
          {active ? 'Active' : 'Inactive'}
        </span>
      ),
    },
    {
      header: 'Actions',
      accessor: 'id',
      sortable: false,
      cell: (_, row) => (
        <div className="action-buttons">
          <button className="btn btn-sm btn-secondary" onClick={() => handleOpenEditModal(row)}>
            Edit
          </button>
          <button
            className={`btn btn-sm ${row.active ? 'btn-warning' : 'btn-success'}`}
            onClick={() => handleToggleActive(row)}
          >
            {row.active ? 'Deactivate' : 'Activate'}
          </button>
          <button className="btn btn-sm btn-danger" onClick={() => handleOpenDeleteModal(row)}>
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
        <h2>Users Management</h2>
        <button className="btn btn-primary" onClick={handleOpenAddModal}>
          <i className="bi bi-plus-lg me-2"></i>
          Add User
        </button>
      </div>

      <DataTable
        columns={columns}
        data={users}
        searchable
        searchPlaceholder="Search users..."
      />

      {/* Add/Edit Modal */}
      <Modal
        isOpen={showModal}
        onClose={handleCloseModal}
        title={editingUser ? 'Edit User' : 'Add User'}
        footer={
          <>
            <button className="btn btn-secondary" onClick={handleCloseModal}>
              Cancel
            </button>
            <button className="btn btn-primary" onClick={handleSubmit} disabled={saving}>
              {saving ? 'Saving...' : editingUser ? 'Update' : 'Create'}
            </button>
          </>
        }
      >
        <div className="row">
          <div className="col-md-6">
            <FormInput
              label="Username"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              error={formErrors.username}
              required
            />
          </div>
          <div className="col-md-6">
            <FormInput
              label="Email"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              error={formErrors.email}
              required
            />
          </div>
        </div>
        <div className="row">
          <div className="col-md-6">
            <FormInput
              label="Phone Number"
              type="tel"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleInputChange}
            />
          </div>
          <div className="col-md-6">
            {/* Empty column for spacing */}
          </div>
        </div>
        <div className="row">
          <div className="col-md-6">
            <FormInput
              label="First Name"
              name="firstName"
              value={formData.firstName}
              onChange={handleInputChange}
              error={formErrors.firstName}
              required
            />
          </div>
          <div className="col-md-6">
            <FormInput
              label="Last Name"
              name="lastName"
              value={formData.lastName}
              onChange={handleInputChange}
              error={formErrors.lastName}
              required
            />
          </div>
        </div>

        {/* Password fields only shown when creating new user */}
        {!editingUser && (
          <div className="row">
            <div className="col-md-6">
              <FormInput
                label="Password"
                type="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                error={formErrors.password}
                required
                placeholder="Min 8 characters"
              />
            </div>
            <div className="col-md-6">
              <FormInput
                label="Confirm Password"
                type="password"
                name="passwordConfirm"
                value={formData.passwordConfirm}
                onChange={handleInputChange}
                error={formErrors.passwordConfirm}
                required
              />
            </div>
          </div>
        )}

        <MultiSelect
          label="Roles"
          placeholder="Select roles..."
          value={formData.roleIds}
          onChange={(selectedRoleIds) => setFormData({ ...formData, roleIds: selectedRoleIds })}
          options={roles.map((r) => ({ value: r.id, label: r.name }))}
          error={formErrors.roleIds}
          required
        />
        <FormInput
          label="Active"
          type="checkbox"
          name="active"
          value={formData.active}
          onChange={handleInputChange}
        />
      </Modal>

      {/* Delete Confirmation Modal */}
      <ConfirmModal
        isOpen={showDeleteModal}
        onClose={() => {
          setShowDeleteModal(false)
          setDeletingUser(null)
        }}
        onConfirm={handleDelete}
        title="Delete User"
        message={`Are you sure you want to delete user "${deletingUser?.email}"? This action cannot be undone.`}
        confirmText="Delete"
      />
    </div>
  )
}
