import { useState, useRef, useEffect } from 'react'

/**
 * MultiSelect Component - User-friendly multi-select dropdown with checkboxes
 *
 * @param {string} label - Label text
 * @param {Array} options - Array of {value, label} objects
 * @param {Array} value - Array of selected values
 * @param {Function} onChange - Callback when selection changes
 * @param {string} placeholder - Placeholder text
 * @param {string} error - Error message
 * @param {boolean} required - Whether field is required
 */
export default function MultiSelect({
  label,
  options = [],
  value = [],
  onChange,
  placeholder = 'Select options...',
  error,
  required = false
}) {
  const [isOpen, setIsOpen] = useState(false)
  const dropdownRef = useRef(null)

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false)
      }
    }

    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  const handleToggle = (optionValue) => {
    const newValue = value.includes(optionValue)
      ? value.filter(v => v !== optionValue)
      : [...value, optionValue]

    onChange(newValue)
  }

  const handleSelectAll = () => {
    if (value.length === options.length) {
      onChange([])
    } else {
      onChange(options.map(opt => opt.value))
    }
  }

  const getDisplayText = () => {
    if (value.length === 0) return placeholder
    if (value.length === 1) {
      const selected = options.find(opt => opt.value === value[0])
      return selected ? selected.label : ''
    }
    return `${value.length} selected`
  }

  const allSelected = value.length === options.length && options.length > 0

  return (
    <div className="mb-3" ref={dropdownRef}>
      {label && (
        <label className="form-label">
          {label}
          {required && <span className="text-danger ms-1">*</span>}
        </label>
      )}

      <div className="multi-select-wrapper">
        <button
          type="button"
          className={`form-control multi-select-trigger ${error ? 'is-invalid' : ''} ${isOpen ? 'active' : ''}`}
          onClick={() => setIsOpen(!isOpen)}
        >
          <span className="multi-select-value">{getDisplayText()}</span>
          <i className={`bi bi-chevron-${isOpen ? 'up' : 'down'} ms-auto`}></i>
        </button>

        {isOpen && (
          <div className="multi-select-dropdown">
            {options.length > 1 && (
              <div className="multi-select-option multi-select-all">
                <label className="d-flex align-items-center w-100 m-0">
                  <input
                    type="checkbox"
                    checked={allSelected}
                    onChange={handleSelectAll}
                    className="form-check-input me-2"
                  />
                  <strong>{allSelected ? 'Deselect All' : 'Select All'}</strong>
                </label>
              </div>
            )}

            {options.length === 0 ? (
              <div className="multi-select-option text-muted">
                No options available
              </div>
            ) : (
              options.map((option) => (
                <div key={option.value} className="multi-select-option">
                  <label className="d-flex align-items-center w-100 m-0">
                    <input
                      type="checkbox"
                      checked={value.includes(option.value)}
                      onChange={() => handleToggle(option.value)}
                      className="form-check-input me-2"
                    />
                    {option.label}
                  </label>
                </div>
              ))
            )}
          </div>
        )}
      </div>

      {error && <div className="invalid-feedback d-block">{error}</div>}
    </div>
  )
}
