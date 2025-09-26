-- Migration: V5__Add_request_id_to_audit_logs.sql
-- Descrição: Adiciona campo request_id para correlação de logs
-- Data: 2025-01-15

ALTER TABLE audit_logs ADD COLUMN request_id VARCHAR(36);

-- Índice para consultas por request_id
CREATE INDEX idx_audit_logs_request_id ON audit_logs(request_id);

-- Comentário da nova coluna
COMMENT ON COLUMN audit_logs.request_id IS 'UUID único para correlacionar logs da mesma requisição';