-- Script de inicialização do banco CRM
-- Este script será executado automaticamente quando o container PostgreSQL for criado
-- Criação das extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela de Empresas
CREATE TABLE IF NOT EXISTS empresas (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(200) NOT NULL,
  cnpj VARCHAR(18) UNIQUE,
  email VARCHAR(100),
  telefone VARCHAR(20),
  endereco TEXT,
  cidade VARCHAR(100),
  estado VARCHAR(2),
  cep VARCHAR(10),
  data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Clientes
CREATE TABLE IF NOT EXISTS clientes (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(200) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  telefone VARCHAR(20),
  cpf VARCHAR(14) UNIQUE,
  data_nascimento DATE,
  endereco TEXT,
  cidade VARCHAR(100),
  estado VARCHAR(2),
  cep VARCHAR(10),
  empresa_id BIGINT REFERENCES empresas(id),
  status VARCHAR(20) DEFAULT 'ATIVO',
  data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Contatos (interações com clientes)
CREATE TABLE IF NOT EXISTS contatos (
  id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT NOT NULL REFERENCES clientes(id) ON DELETE CASCADE,
  tipo VARCHAR(50) NOT NULL,
  -- EMAIL, TELEFONE, REUNIAO, etc.
  assunto VARCHAR(200) NOT NULL,
  descricao TEXT,
  data_contato TIMESTAMP NOT NULL,
  status VARCHAR(20) DEFAULT 'CONCLUIDO',
  data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Oportunidades de Venda
CREATE TABLE IF NOT EXISTS oportunidades (
  id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT NOT NULL REFERENCES clientes(id),
  titulo VARCHAR(200) NOT NULL,
  descricao TEXT,
  valor_estimado DECIMAL(15, 2),
  probabilidade_fechamento INTEGER DEFAULT 0,
  status VARCHAR(30) DEFAULT 'PROSPECCAO',
  -- PROSPECCAO, QUALIFICACAO, PROPOSTA, NEGOCIACAO, FECHADO, PERDIDO
  data_fechamento_prevista DATE,
  data_fechamento_real DATE,
  data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_clientes_email ON clientes(email);

CREATE INDEX IF NOT EXISTS idx_clientes_empresa ON clientes(empresa_id);

CREATE INDEX IF NOT EXISTS idx_contatos_cliente ON contatos(cliente_id);

CREATE INDEX IF NOT EXISTS idx_contatos_data ON contatos(data_contato);

CREATE INDEX IF NOT EXISTS idx_oportunidades_cliente ON oportunidades(cliente_id);

CREATE INDEX IF NOT EXISTS idx_oportunidades_status ON oportunidades(status);

-- Dados de exemplo para desenvolvimento
INSERT INTO
  empresas (nome, cnpj, email, telefone, cidade, estado)
VALUES
  (
    'Tech Solutions Ltda',
    '12.345.678/0001-90',
    'contato@techsolutions.com',
    '(11) 9999-8888',
    'São Paulo',
    'SP'
  ),
  (
    'Inovação Digital EIRELI',
    '98.765.432/0001-12',
    'info@inovacaodigital.com',
    '(21) 8888-7777',
    'Rio de Janeiro',
    'RJ'
  ) ON CONFLICT (cnpj) DO NOTHING;

INSERT INTO
  clientes (
    nome,
    email,
    telefone,
    cpf,
    empresa_id,
    cidade,
    estado
  )
VALUES
  (
    'João Silva',
    'joao.silva@email.com',
    '(11) 99999-1111',
    '123.456.789-00',
    1,
    'São Paulo',
    'SP'
  ),
  (
    'Maria Santos',
    'maria.santos@email.com',
    '(21) 88888-2222',
    '987.654.321-00',
    2,
    'Rio de Janeiro',
    'RJ'
  ),
  (
    'Pedro Oliveira',
    'pedro.oliveira@email.com',
    '(31) 77777-3333',
    '456.789.123-00',
    NULL,
    'Belo Horizonte',
    'MG'
  ) ON CONFLICT (email) DO NOTHING;

-- Trigger para atualizar data_atualizacao automaticamente
CREATE
OR REPLACE FUNCTION update_timestamp() RETURNS TRIGGER AS $ $ BEGIN NEW.data_atualizacao = CURRENT_TIMESTAMP;

RETURN NEW;

END;

$ $ LANGUAGE 'plpgsql';

-- Aplicar triggers nas tabelas
DROP TRIGGER IF EXISTS update_empresas_timestamp ON empresas;

CREATE TRIGGER update_empresas_timestamp BEFORE
UPDATE
  ON empresas FOR EACH ROW EXECUTE FUNCTION update_timestamp();

DROP TRIGGER IF EXISTS update_clientes_timestamp ON clientes;

CREATE TRIGGER update_clientes_timestamp BEFORE
UPDATE
  ON clientes FOR EACH ROW EXECUTE FUNCTION update_timestamp();

DROP TRIGGER IF EXISTS update_oportunidades_timestamp ON oportunidades;

CREATE TRIGGER update_oportunidades_timestamp BEFORE
UPDATE
  ON oportunidades FOR EACH ROW EXECUTE FUNCTION update_timestamp();