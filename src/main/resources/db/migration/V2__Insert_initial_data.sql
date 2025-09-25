-- Migration: V2__Insert_initial_data.sql
-- Descrição: Inserção de dados iniciais do sistema
-- Data: 2025-01-15

-- =======================
-- Usuários Padrão
-- =======================
INSERT INTO users (
    username,
    email,
    password,
    nome,
    role,
    ativo,
    data_criacao
) VALUES
(
    'admin',
    'admin@crm.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P5TA8qnlFlCHLa', -- senha: admin123
    'Administrador do Sistema',
    'ADMIN',
    true,
    CURRENT_TIMESTAMP
),
(
    'testuser',
    'test@crm.com',
    '$2a$10$k4qvM7zmLhKI8PIzLBIDy.ePh3TdENOE4dh4XHe7.RIIiXa4LQ3yK', -- senha: test123
    'Usuário de Teste',
    'USER',
    true,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;

-- =======================
-- Empresas de Exemplo
-- =======================
INSERT INTO empresas (
    nome,
    cnpj,
    email,
    telefone,
    endereco,
    cidade,
    estado,
    cep,
    data_criacao
) VALUES
(
    'Tech Solutions Ltda',
    '12.345.678/0001-90',
    'contato@techsolutions.com.br',
    '(11) 99999-9999',
    'Rua das Tecnologias, 123',
    'São Paulo',
    'SP',
    '01234-567',
    CURRENT_TIMESTAMP
),
(
    'Consultoria Empresarial S/A',
    '98.765.432/0001-12',
    'contato@consultoria.com.br',
    '(21) 88888-8888',
    'Av. Empresarial, 456',
    'Rio de Janeiro',
    'RJ',
    '20000-000',
    CURRENT_TIMESTAMP
),
(
    'Inovação Digital ME',
    '11.222.333/0001-44',
    'hello@inovacaodigital.com.br',
    '(31) 77777-7777',
    'Rua da Inovação, 789',
    'Belo Horizonte',
    'MG',
    '30000-000',
    CURRENT_TIMESTAMP
) ON CONFLICT (cnpj) DO NOTHING;

-- =======================
-- Clientes de Exemplo
-- =======================
INSERT INTO clientes (
    nome,
    email,
    telefone,
    cpf,
    data_nascimento,
    endereco,
    cidade,
    estado,
    cep,
    status,
    empresa_id,
    data_criacao
) VALUES
(
    'João Silva Santos',
    'joao.silva@email.com',
    '(11) 98765-4321',
    '123.456.789-01',
    '1985-03-15',
    'Rua das Flores, 100',
    'São Paulo',
    'SP',
    '05000-000',
    'ATIVO',
    1,
    CURRENT_TIMESTAMP
),
(
    'Maria Oliveira Costa',
    'maria.oliveira@email.com',
    '(21) 91234-5678',
    '987.654.321-09',
    '1990-07-22',
    'Av. Principal, 200',
    'Rio de Janeiro',
    'RJ',
    '22000-000',
    'ATIVO',
    2,
    CURRENT_TIMESTAMP
),
(
    'Pedro Henrique Souza',
    'pedro.souza@email.com',
    '(31) 99988-7766',
    '456.789.123-45',
    '1988-12-10',
    'Rua da Liberdade, 300',
    'Belo Horizonte',
    'MG',
    '31000-000',
    'ATIVO',
    3,
    CURRENT_TIMESTAMP
) ON CONFLICT (email) DO NOTHING;

-- =======================
-- Contatos de Exemplo
-- =======================
INSERT INTO contatos (
    cliente_id,
    tipo,
    assunto,
    descricao,
    data_contato,
    status,
    data_criacao
) VALUES
(
    1,
    'EMAIL',
    'Primeiro contato - Interesse em nossos serviços',
    'Cliente demonstrou interesse em nossos serviços de desenvolvimento de software após visitar nosso site.',
    CURRENT_TIMESTAMP - INTERVAL '7 days',
    'CONCLUIDO',
    CURRENT_TIMESTAMP
),
(
    1,
    'TELEFONE',
    'Follow-up da proposta enviada',
    'Ligação para esclarecer dúvidas sobre a proposta comercial enviada por email.',
    CURRENT_TIMESTAMP - INTERVAL '3 days',
    'CONCLUIDO',
    CURRENT_TIMESTAMP
),
(
    2,
    'REUNIAO',
    'Apresentação da empresa e serviços',
    'Reunião presencial para apresentar nossa empresa e discutir as necessidades do cliente.',
    CURRENT_TIMESTAMP - INTERVAL '5 days',
    'CONCLUIDO',
    CURRENT_TIMESTAMP
),
(
    3,
    'EMAIL',
    'Contato inicial - Marketing Digital',
    'Envio de material sobre nossos serviços de marketing digital.',
    CURRENT_TIMESTAMP - INTERVAL '2 days',
    'CONCLUIDO',
    CURRENT_TIMESTAMP
),
(
    1,
    'REUNIAO',
    'Reunião de alinhamento do projeto',
    'Reunião agendada para definir escopo e cronograma do projeto.',
    CURRENT_TIMESTAMP + INTERVAL '3 days',
    'AGENDADO',
    CURRENT_TIMESTAMP
);

-- =======================
-- Oportunidades de Exemplo
-- =======================
INSERT INTO oportunidades (
    cliente_id,
    titulo,
    descricao,
    valor_estimado,
    probabilidade_fechamento,
    status,
    data_fechamento_prevista,
    data_criacao,
    data_atualizacao
) VALUES
(
    1,
    'Desenvolvimento de Sistema CRM',
    'Desenvolvimento de sistema CRM personalizado para gestão de clientes e vendas.',
    150000.00,
    75,
    'NEGOCIACAO',
    CURRENT_DATE + INTERVAL '30 days',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    2,
    'Consultoria em Processos Empresariais',
    'Consultoria para otimização de processos internos e melhoria da produtividade.',
    80000.00,
    50,
    'PROPOSTA',
    CURRENT_DATE + INTERVAL '45 days',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    3,
    'Campanha de Marketing Digital',
    'Desenvolvimento e execução de campanha de marketing digital para aumentar visibilidade online.',
    25000.00,
    30,
    'QUALIFICACAO',
    CURRENT_DATE + INTERVAL '60 days',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);