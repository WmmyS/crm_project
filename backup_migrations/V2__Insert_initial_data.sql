-- Migration: V2__Insert_initial_data.sql
-- Descrição: Inserção de dados iniciais do sistema
-- Data: 2025-09-24
-- =======================
-- Usuário Administrador Padrão
-- =======================
INSERT INTO
    users (
        username,
        email,
        password,
        nome,
        role,
        ativo,
        data_criacao
    )
VALUES
    (
        'admin',
        'admin@crm.com',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P5TA8qnlFlCHLa',
        -- senha: admin123
        'Administrador do Sistema',
        'ADMIN',
        true,
        CURRENT_TIMESTAMP
    ) ON CONFLICT (username) DO NOTHING;

-- =======================
-- Usuário de Teste
-- =======================
INSERT INTO
    users (
        username,
        email,
        password,
        nome,
        role,
        ativo,
        data_criacao
    )
VALUES
    (
        'testuser',
        'test@crm.com',
        '$2a$10$k4qvM7zmLhKI8PIzLBIDy.ePh3TdENOE4dh4XHe7.RIIiXa4LQ3yK',
        -- senha: test123
        'Usuário de Teste',
        'USER',
        true,
        CURRENT_TIMESTAMP
    ) ON CONFLICT (username) DO NOTHING;

-- =======================
-- Empresa de Exemplo
-- =======================
INSERT INTO
    empresas (
        nome,
        cnpj,
        email,
        telefone,
        endereco,
        cidade,
        estado,
        cep,
        setor,
        data_criacao
    )
VALUES
    (
        'Empresa Exemplo Ltda',
        '12.345.678/0001-90',
        'contato@exemploempresa.com',
        '(11) 99999-9999',
        'Rua das Empresas, 123 - Centro',
        'São Paulo',
        'SP',
        '01234-567',
        'Tecnologia',
        CURRENT_TIMESTAMP
    );

-- =======================
-- Cliente de Exemplo
-- =======================
INSERT INTO
    clientes (
        nome,
        email,
        telefone,
        cpf,
        endereco,
        cidade,
        estado,
        cep,
        status,
        data_criacao,
        empresa_id
    )
VALUES
    (
        'João da Silva',
        'joao.silva@email.com',
        '(11) 98888-8888',
        '123.456.789-00',
        'Rua dos Clientes, 456 - Bairro Novo',
        'São Paulo',
        'SP',
        '12345-678',
        'ATIVO',
        CURRENT_TIMESTAMP,
        1 -- ID da empresa de exemplo
    );

-- =======================
-- Contato de Exemplo
-- =======================
INSERT INTO
    contatos (
        nome,
        email,
        telefone,
        cargo,
        tipo,
        status,
        observacoes,
        data_criacao,
        cliente_id
    )
VALUES
    (
        'Maria Santos',
        'maria.santos@email.com',
        '(11) 97777-7777',
        'Gerente de Compras',
        'COMERCIAL',
        'ATIVO',
        'Responsável pelas decisões de compra da empresa',
        CURRENT_TIMESTAMP,
        1 -- ID do cliente de exemplo
    );

-- =======================
-- Oportunidade de Exemplo
-- =======================
INSERT INTO
    oportunidades (
        titulo,
        descricao,
        valor,
        data_abertura,
        data_fechamento_prevista,
        probabilidade,
        status,
        observacoes,
        data_criacao,
        cliente_id
    )
VALUES
    (
        'Implementação Sistema CRM',
        'Projeto para implementar sistema CRM completo na empresa do cliente',
        50000.00,
        CURRENT_DATE,
        CURRENT_DATE + INTERVAL '30 days',
        70,
        'EM_NEGOCIACAO',
        'Cliente demonstrou bastante interesse na solução proposta',
        CURRENT_TIMESTAMP,
        1 -- ID do cliente de exemplo
    );