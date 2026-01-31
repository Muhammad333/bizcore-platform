import { useEffect, useRef } from 'react'
import './Toast.css'

export default function Toast({ toasts, removeToast }) {
  if (toasts.length === 0) {
    return null
  }

  return (
    <div className="toast-container" style={{ position: 'fixed', bottom: '20px', right: '20px', zIndex: 9999 }}>
      {toasts.map((toast) => (
        <ToastItem key={toast.id} toast={toast} removeToast={removeToast} />
      ))}
    </div>
  )
}

function ToastItem({ toast, removeToast }) {
  const timerRef = useRef(null)
  const removeToastRef = useRef(removeToast)

  // Keep removeToast reference updated
  useEffect(() => {
    removeToastRef.current = removeToast
  }, [removeToast])

  useEffect(() => {
    // Clear any existing timer
    if (timerRef.current) {
      clearTimeout(timerRef.current)
    }

    timerRef.current = setTimeout(() => {
      removeToastRef.current(toast.id)
    }, toast.duration || 3000)

    return () => {
      if (timerRef.current) {
        clearTimeout(timerRef.current)
      }
    }
  }, [toast.id, toast.duration])

  const getIcon = () => {
    switch (toast.type) {
      case 'success':
        return '✓'
      case 'error':
        return '✕'
      case 'warning':
        return '⚠'
      case 'info':
        return 'ℹ'
      default:
        return 'ℹ'
    }
  }

  return (
    <div
      className={`toast toast-${toast.type}`}
      style={{
        display: 'flex',
        alignItems: 'center',
        padding: '16px',
        marginBottom: '10px',
        borderRadius: '8px',
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
        minWidth: '300px',
        animation: 'slideIn 0.3s ease-out'
      }}
    >
      <div className="toast-icon">{getIcon()}</div>
      <div className="toast-message" style={{ flex: 1, marginLeft: '12px' }}>{toast.message}</div>
      <button className="toast-close" onClick={() => removeToast(toast.id)} style={{ marginLeft: '12px', cursor: 'pointer' }}>
        ×
      </button>
    </div>
  )
}
