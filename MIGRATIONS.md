# 🗄️ Sistema de Migrations - CRM Application

## 📋 Visão Geral

O CRM Application agora utiliza **Flyway** para gerenciamento de migrations de banco de dados, garantindo controle de versão e consistência do schema em todos os ambientes.

## 🔧 Configuração

### Dependencies
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

### Properties
```properties
# Hibernate DDL mudou de 'update' para 'validate'
spring.jpa.hibernate.ddl-auto=validate

# Configurações do Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
```

## 📁 Estrutura de Arquivos

```
src/main/resources/db/migration/
├── V1__Create_initial_tables.sql      # Schema inicial
└── V2__Insert_initial_data.sql        # Dados iniciais
```

## 📊 Tabelas Criadas

### 1. **users** - Usuários do Sistema
- `id` (BIGSERIAL PRIMARY KEY)
- `username` (VARCHAR(50) UNIQUE)
- `email` (VARCHAR(100) UNIQUE) 
- `password` (VARCHAR(255))
- `nome` (VARCHAR(100))
- `role` (VARCHAR(20)) - ADMIN, USER, API
- `ativo` (BOOLEAN)
- `data_criacao`, `data_atualizacao`, `ultimo_login`

### 2. **empresas** - Empresas
- `id` (BIGSERIAL PRIMARY KEY)
- `nome` (VARCHAR(200))
- `cnpj` (VARCHAR(18) UNIQUE)
- `email`, `telefone`, `endereco`, `cidade`, `estado`, `cep`
- `setor` (VARCHAR(100))
- `data_criacao`, `data_atualizacao`

### 3. **clientes** - Clientes
- `id` (BIGSERIAL PRIMARY KEY)
- `nome` (VARCHAR(200))
- `email` (VARCHAR(100) UNIQUE)
- `telefone`, `cpf`, `data_nascimento`
- `endereco`, `cidade`, `estado`, `cep`
- `status` (VARCHAR(20)) - ATIVO, INATIVO, PROSPECT
- `empresa_id` (FK para empresas)
- `data_criacao`, `data_atualizacao`

### 4. **contatos** - Contatos dos Clientes
- `id` (BIGSERIAL PRIMARY KEY)
- `nome` (VARCHAR(200))
- `email` (VARCHAR(100))
- `telefone`, `cargo`
- `tipo` (VARCHAR(20)) - COMERCIAL, TECNICO, FINANCEIRO, OUTRO
- `status` (VARCHAR(20)) - ATIVO, INATIVO
- `observacoes` (TEXT)
- `cliente_id` (FK para clientes)
- `data_criacao`, `data_atualizacao`

### 5. **oportunidades** - Oportunidades de Negócio
- `id` (BIGSERIAL PRIMARY KEY)
- `titulo` (VARCHAR(200))
- `descricao` (TEXT)
- `valor` (DECIMAL(15,2))
- `data_abertura`, `data_fechamento_prevista`, `data_fechamento_real`
- `probabilidade` (INTEGER 0-100)
- `status` (VARCHAR(20)) - ABERTA, EM_NEGOCIACAO, FECHADA_GANHA, FECHADA_PERDIDA, CANCELADA
- `observacoes` (TEXT)
- `cliente_id` (FK para clientes)
- `data_criacao`, `data_atualizacao`

## 👤 Dados Iniciais

### Usuários Padrão:
- **admin** / admin123 (ADMIN)
- **testuser** / test123 (USER)

### Dados de Exemplo:
- 1 Empresa: "Empresa Exemplo Ltda"
- 1 Cliente: "João da Silva"
- 1 Contato: "Maria Santos"
- 1 Oportunidade: "Implementação Sistema CRM"

## 🚀 Como Usar

### Nova Migration
1. Criar arquivo: `V{número}__Descricao.sql`
2. Exemplo: `V3__Add_new_column_to_users.sql`
3. Restart da aplicação aplica automaticamente

### Rollback
⚠️ **Flyway Community não suporta rollback automático**
- Para rollback, criar nova migration que desfaz as mudanças
- Exemplo: `V4__Remove_column_from_users.sql`

### Verificar Status
```sql
-- Tabela flyway_schema_history mostra histórico
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

## 🔍 Benefícios

✅ **Versionamento**: Controle total do schema  
✅ **Consistência**: Mesmo schema em dev/prod  
✅ **Auditoria**: Histórico completo de mudanças  
✅ **Segurança**: Validação automática  
✅ **Colaboração**: Migrations versionadas no Git  

## ⚠️ Mudanças Importantes

- **Antes**: `spring.jpa.hibernate.ddl-auto=update`
- **Agora**: `spring.jpa.hibernate.ddl-auto=validate`

Hibernate agora **apenas valida** o schema ao invés de modificá-lo. Todas as mudanças devem ser feitas via migrations.

## 📝 Exemplos de Migrations

### Adicionar Coluna:
```sql
-- V3__Add_phone_to_users.sql
ALTER TABLE users ADD COLUMN telefone VARCHAR(20);
```

### Criar Índice:
```sql  
-- V4__Add_index_users_email.sql
CREATE INDEX idx_users_email ON users(email);
```

### Inserir Dados:
```sql
-- V5__Add_default_roles.sql
INSERT INTO users (username, email, password, role) 
VALUES ('sistema', 'sistema@crm.com', 'encrypted_pass', 'API');
```

---
🎯 **Sistema de migrations ativo e funcional!**