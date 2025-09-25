-- Migration: V1__Create_initial_tables.sql
-- Descrição: Criação das tabelas iniciais do CRM
-- Data: 2025-09-24
-- =======================
-- Tabela de Usuários
-- =======================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    ativo BOOLEAN NOT NULL DEFAULT true,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    ultimo_login TIMESTAMP,
    -- Constraints
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN', 'USER', 'API'))
);

-- =======================
-- Tabela de Empresas
-- =======================
CREATE TABLE empresas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    cnpj VARCHAR(18) UNIQUE,
    email VARCHAR(100),
    telefone VARCHAR(20),
    endereco TEXT,
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    setor VARCHAR(100),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP
);

-- =======================
-- Tabela de Clientes
-- =======================
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    cpf VARCHAR(14) UNIQUE,
    data_nascimento DATE,
    endereco TEXT,
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    status VARCHAR(20) DEFAULT 'ATIVO',
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    empresa_id BIGINT,
    -- Foreign Keys
    CONSTRAINT fk_cliente_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id),
    -- Constraints
    CONSTRAINT chk_cliente_status CHECK (status IN ('ATIVO', 'INATIVO', 'PROSPECT'))
);

-- =======================
-- Tabela de Contatos
-- =======================
CREATE TABLE contatos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    cargo VARCHAR(100),
    tipo VARCHAR(20) DEFAULT 'COMERCIAL',
    status VARCHAR(20) DEFAULT 'ATIVO',
    observacoes TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    cliente_id BIGINT NOT NULL,
    -- Foreign Keys
    CONSTRAINT fk_contato_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    -- Constraints
    CONSTRAINT chk_contato_tipo CHECK (
        tipo IN ('COMERCIAL', 'TECNICO', 'FINANCEIRO', 'OUTRO')
    ),
    CONSTRAINT chk_contato_status CHECK (status IN ('ATIVO', 'INATIVO'))
);

-- =======================
-- Tabela de Oportunidades
-- =======================
CREATE TABLE oportunidades (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT,
    valor DECIMAL(15, 2),
    data_abertura DATE NOT NULL DEFAULT CURRENT_DATE,
    data_fechamento_prevista DATE,
    data_fechamento_real DATE,
    probabilidade INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ABERTA',
    observacoes TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    cliente_id BIGINT NOT NULL,
    -- Foreign Keys
    CONSTRAINT fk_oportunidade_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    -- Constraints
    CONSTRAINT chk_oportunidade_status CHECK (
        status IN (
            'ABERTA',
            'EM_NEGOCIACAO',
            'FECHADA_GANHA',
            'FECHADA_PERDIDA',
            'CANCELADA'
        )
    ),
    CONSTRAINT chk_oportunidade_probabilidade CHECK (
        probabilidade >= 0
        AND probabilidade <= 100
    )
);

-- =======================
-- Índices para Performance
-- =======================
-- Usuários
CREATE INDEX idx_users_email ON users(email);

CREATE INDEX idx_users_username ON users(username);

CREATE INDEX idx_users_role ON users(role);

-- Empresas
CREATE INDEX idx_empresas_cnpj ON empresas(cnpj);

CREATE INDEX idx_empresas_nome ON empresas(nome);

-- Clientes
CREATE INDEX idx_clientes_email ON clientes(email);

CREATE INDEX idx_clientes_cpf ON clientes(cpf);

CREATE INDEX idx_clientes_empresa_id ON clientes(empresa_id);

CREATE INDEX idx_clientes_status ON clientes(status);

-- Contatos
CREATE INDEX idx_contatos_cliente_id ON contatos(cliente_id);

CREATE INDEX idx_contatos_email ON contatos(email);

CREATE INDEX idx_contatos_status ON contatos(status);

-- Oportunidades
CREATE INDEX idx_oportunidades_cliente_id ON oportunidades(cliente_id);

CREATE INDEX idx_oportunidades_status ON oportunidades(status);

CREATE INDEX idx_oportunidades_data_abertura ON oportunidades(data_abertura);

CREATE INDEX idx_oportunidades_valor ON oportunidades(valor);

-- =======================
-- Comentários das Tabelas
-- =======================
COMMENT ON TABLE users IS 'Usuários do sistema CRM';

COMMENT ON TABLE empresas IS 'Empresas cadastradas no CRM';

COMMENT ON TABLE clientes IS 'Clientes das empresas';

COMMENT ON TABLE contatos IS 'Contatos dos clientes';

COMMENT ON TABLE oportunidades IS 'Oportunidades de negócio com clientes';