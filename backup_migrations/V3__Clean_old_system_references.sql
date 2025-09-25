-- Migration: V3__Clean_old_system_references.sql
-- Descrição: Remove referências do sistema de autenticação antigo
-- Data: 2025-09-24
-- =======================
-- Remove tabelas antigas se existirem
-- =======================
DROP TABLE IF EXISTS api_keys CASCADE;

DROP TABLE IF EXISTS rotating_tokens CASCADE;