export default function FormInput({
  label,
  type = 'text',
  name,
  value,
  onChange,
  error,
  required = false,
  placeholder,
  options = [],
  multiple = false,
  rows = 3,
  disabled = false,
  className = '',
}) {
  const inputId = `input-${name}`

  const renderInput = () => {
    const baseProps = {
      id: inputId,
      name,
      disabled,
      className: `form-control ${error ? 'is-invalid' : ''} ${className}`,
    }

    if (type === 'select') {
      return (
        <select
          {...baseProps}
          value={value}
          onChange={onChange}
          multiple={multiple}
          style={multiple ? { minHeight: '120px' } : {}}
        >
          {!multiple && <option value="">Select...</option>}
          {options.map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
      )
    }

    if (type === 'textarea') {
      return (
        <textarea
          {...baseProps}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          rows={rows}
        />
      )
    }

    if (type === 'checkbox') {
      return (
        <div className="form-check">
          <input
            type="checkbox"
            id={inputId}
            name={name}
            checked={value}
            onChange={onChange}
            disabled={disabled}
            className={`form-check-input ${error ? 'is-invalid' : ''}`}
          />
          <label className="form-check-label" htmlFor={inputId}>
            {label}
          </label>
        </div>
      )
    }

    return (
      <input
        {...baseProps}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
      />
    )
  }

  if (type === 'checkbox') {
    return (
      <div className="form-group">
        {renderInput()}
        {error && <div className="invalid-feedback d-block">{error}</div>}
      </div>
    )
  }

  return (
    <div className="form-group">
      <label htmlFor={inputId} className="form-label">
        {label}
        {required && <span className="text-danger ms-1">*</span>}
      </label>
      {renderInput()}
      {error && <div className="invalid-feedback d-block">{error}</div>}
    </div>
  )
}
