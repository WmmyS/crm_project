-- Migration: V4__Create_audit_logs_table.sql
-- Descrição: Cria tabela para logs de auditoria
-- Data: 2025-01-15

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL DEFAULT 'ANONYMOUS',
    user_role VARCHAR(20),
    method VARCHAR(10) NOT NULL,
    endpoint VARCHAR(500) NOT NULL,
    ip_address VARCHAR(45),
    status_code INTEGER,
    duration_ms BIGINT,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para consultas rápidas
CREATE INDEX idx_audit_logs_username ON audit_logs(username);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_logs_endpoint ON audit_logs(endpoint);
CREATE INDEX idx_audit_logs_method ON audit_logs(method);
CREATE INDEX idx_audit_logs_status_code ON audit_logs(status_code);

-- Comentário da tabela
COMMENT ON TABLE audit_logs IS 'Logs de auditoria para rastreamento de requisições';