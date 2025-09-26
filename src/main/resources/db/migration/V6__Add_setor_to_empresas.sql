-- Migration: V6__Add_setor_to_empresas.sql
-- Descrição: Adiciona coluna setor na tabela empresas
-- Data: 2025-09-26

ALTER TABLE empresas ADD COLUMN setor VARCHAR(100);