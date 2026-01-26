-- ============================================
-- BizCore - Request Logging Migration
-- Created: 2026-01-25
-- Description: Add request_logs table for tracking all API requests
-- ============================================

-- Request Logs (HTTP Request Tracking)
CREATE TABLE request_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT REFERENCES companies(id) ON DELETE SET NULL,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    username VARCHAR(255),
    method VARCHAR(10) NOT NULL,
    endpoint VARCHAR(500) NOT NULL,
    query_params TEXT,
    request_body TEXT,
    response_status INT,
    response_body TEXT,
    error_message TEXT,
    ip_address VARCHAR(50),
    user_agent TEXT,
    duration_ms BIGINT,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_request_logs_company ON request_logs(company_id);
CREATE INDEX idx_request_logs_user ON request_logs(user_id);
CREATE INDEX idx_request_logs_method ON request_logs(method);
CREATE INDEX idx_request_logs_endpoint ON request_logs(endpoint);
CREATE INDEX idx_request_logs_status ON request_logs(response_status);
CREATE INDEX idx_request_logs_created ON request_logs(created_at DESC);
CREATE INDEX idx_request_logs_user_created ON request_logs(user_id, created_at DESC);
