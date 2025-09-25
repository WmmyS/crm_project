-- Migration: V3__Add_imagem_url_to_clientes.sql
-- Descrição: Adiciona campo imagem_url na tabela clientes
-- Data: 2025-01-15
-- Adicionar coluna imagem_url na tabela clientes
ALTER TABLE
  clientes
ADD
  COLUMN imagem_url VARCHAR(500);

-- Comentário da nova coluna
COMMENT ON COLUMN clientes.imagem_url IS 'URL da imagem do cliente';