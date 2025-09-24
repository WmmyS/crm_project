# CRM Application

Sistema de CRM (Customer Relationship Management) desenvolvido em Java com Spring Boot.

## 🚀 Tecnologias Utilizadas

- **Java 21** (LTS) ⚡ *Atualizado para a versão mais recente*
- **Spring Boot 3.3.6** (compatível com Java 21)
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Docker & Docker Compose**
- **Swagger/OpenAPI 3**

## 📋 Funcionalidades

### Entidades Principais
- **Empresas**: Gerenciamento de empresas parceiras
- **Clientes**: Cadastro e gestão de clientes
- **Contatos**: Histórico de interações com clientes
- **Oportunidades**: Controle de vendas e negociações

### APIs REST
- CRUD completo para todas as entidades
- Busca paginada e filtros avançados
- Relatórios e estatísticas
- Documentação automática com Swagger

### 🔐 Sistema de Autenticação
- **Autenticação JWT**: Login seguro com tokens JWT
- **API Keys**: Chaves de API para integração com sistemas externos
- **Cadastro de usuários**: Registro público de novos usuários
- **Controle de acesso**: Sistema baseado em roles (ADMIN, USER, API)
- **Segurança**: Senhas criptografadas com BCrypt
- **Middleware**: Filtros de autenticação para JWT e API Keys

## ⬆️ Upgrade para Java 21

Este projeto foi **atualizado para Java 21 LTS** (setembro 2024), a versão mais recente e estável do Java.

### ✅ O que foi atualizado:
- **Java Runtime**: 17 → 21 (LTS)
- **Spring Boot**: 3.2.0 → 3.3.6 (compatibilidade total com Java 21)
- **Dockerfile**: Atualizado para `eclipse-temurin:21`
- **Maven**: Configuração de compilação para Java 21
- **Dependências**: Todas compatíveis com Java 21

### 🚀 Benefícios do Java 21:
- **Performance melhorada** em relação ao Java 17
- **Novos recursos de linguagem** (Pattern Matching, Virtual Threads, etc.)
- **Suporte LTS** até 2031
- **Melhor garbage collection**
- **Compatibilidade completa** com Spring Boot 3.3.x

## 🛠️ Como Executar

### Pré-requisitos
- **Docker e Docker Compose** (obrigatório)
- **Java 21+** (opcional - apenas para desenvolvimento local)
- **Maven 3.6+** (opcional - apenas para desenvolvimento local)

### Executando com Docker (Recomendado - Não precisa instalar Maven)

1. **Clone o repositório e navegue até a pasta:**
```bash
cd www_crm
```

2. **Execute o script automatizado:**
```bash
./start.sh
```

Este script irá:
- ✅ Compilar a aplicação usando Maven **dentro do container**
- ✅ Iniciar PostgreSQL automaticamente
- ✅ Subir a aplicação completa
- ✅ **Não requer Maven instalado na máquina**

3. **Acesse a aplicação:**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Banco PostgreSQL: localhost:5432

### Executando Localmente (Desenvolvimento - Requer Maven local)

Se você preferir desenvolvimento local com hot-reload:

1. **Execute o script de desenvolvimento:**
```bash
./start-dev.sh
```

Este modo:
- 🐳 Inicia apenas PostgreSQL no Docker
- ☕ Executa a aplicação Java localmente
- 🔄 Permite hot-reload durante desenvolvimento
- 📋 **Requer Maven instalado**: `sudo apt install maven`

## 📚 Documentação da API

A documentação completa da API está disponível via Swagger UI:
- **URL**: http://localhost:8080/swagger-ui.html
- **JSON**: http://localhost:8080/v3/api-docs

### Principais Endpoints

#### Clientes
- `GET /api/clientes` - Listar clientes (paginado)
- `POST /api/clientes` - Criar cliente
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `PUT /api/clientes/{id}` - Atualizar cliente
- `DELETE /api/clientes/{id}` - Deletar cliente
- `GET /api/clientes/buscar?termo={termo}` - Buscar clientes

#### Empresas
- `GET /api/empresas` - Listar empresas
- `POST /api/empresas` - Criar empresa
- `GET /api/empresas/{id}` - Buscar empresa por ID

#### Contatos
- `GET /api/contatos` - Listar contatos
- `POST /api/contatos` - Registrar contato
- `GET /api/contatos/cliente/{clienteId}` - Contatos de um cliente

#### Oportunidades
- `GET /api/oportunidades` - Listar oportunidades
- `POST /api/oportunidades` - Criar oportunidade
- `GET /api/oportunidades/status/{status}` - Por status

## 🔐 Sistema de Autenticação e Segurança

### Características do Sistema
- **Autenticação JWT**: Tokens seguros com expiração de 24 horas
- **API Keys**: Chaves para integração com sistemas externos  
- **Cadastro público**: Usuários podem se registrar livremente
- **Controle de acesso**: Sistema baseado em roles (ADMIN, USER, API)
- **Criptografia**: Senhas protegidas com BCrypt
- **Middleware duplo**: Suporte simultâneo a JWT e API Keys

### Endpoints de Autenticação

#### 🔑 Cadastro de Usuário
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "seuusername",
  "email": "seu@email.com",
  "password": "suasenha123",
  "nome": "Seu Nome Completo"
}
```

**Resposta de Sucesso:**
```json
{
  "id": 3,
  "username": "seuusername",
  "email": "seu@email.com",
  "nome": "Seu Nome Completo",
  "role": "USER",
  "message": "Usuário cadastrado com sucesso"
}
```

#### 🔓 Login de Usuário
```bash
POST /api/auth/login
Content-Type: application/json

{
  "login": "seuusername",    # Pode ser username ou email
  "password": "suasenha123"
}
```

**Resposta de Sucesso:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "seuusername",
  "email": "seu@email.com",
  "nome": "Seu Nome Completo",
  "role": "USER",
  "expiresAt": "2025-09-25T18:21:03.123456"
}
```

#### 🔄 Renovar Token
```bash
POST /api/auth/refresh
Authorization: Bearer SEU_TOKEN_ATUAL
```

#### 🚪 Logout
```bash
POST /api/auth/logout
Authorization: Bearer SEU_TOKEN
```

#### 👤 Informações do Usuário
```bash
GET /api/auth/me
Authorization: Bearer SEU_TOKEN
```

### Gerenciamento de API Keys

#### 🔑 Criar API Key
```bash
POST /api/auth/api-keys
Authorization: Bearer SEU_TOKEN
Content-Type: application/json

{
  "name": "Minha App Frontend"
}
```

**Resposta:**
```json
{
  "id": 1,
  "key": "crm_B4q-bLUouXQ6BHMU_ucuah8tE-yhmhYVWGcFwrN9nFw",
  "name": "Minha App Frontend",
  "isActive": true,
  "createdAt": "2025-09-24T15:30:00",
  "lastUsed": null
}
```

#### 📋 Listar API Keys
```bash
GET /api/auth/api-keys
Authorization: Bearer SEU_TOKEN
```

#### ❌ Revogar API Key
```bash
DELETE /api/auth/api-keys/{id}
Authorization: Bearer SEU_TOKEN
```

### Como Usar as API Keys

Para usar uma API Key, inclua ela no header `X-API-Key`:

```bash
GET /api/clientes
X-API-Key: crm_B4q-bLUouXQ6BHMU_ucuah8tE-yhmhYVWGcFwrN9nFw
```

### Roles e Permissões

- **ADMIN**: Acesso completo ao sistema
- **USER**: Acesso aos recursos padrão do CRM
- **API**: Role automática para autenticação via API Key

### Validações de Segurança

- ✅ Username único (mínimo 3 caracteres)
- ✅ Email único e válido
- ✅ Senha mínima de 6 caracteres
- ✅ Tokens JWT com expiração
- ✅ API Keys com prefixo `crm_` para identificação
- ✅ Blacklist de tokens para logout seguro
- ✅ Criptografia BCrypt para senhas

## 🗄️ Configuração do Banco de Dados

### Como Funciona a Conexão

O sistema usa **perfis do Spring Boot** para diferentes ambientes:

#### 🏠 Desenvolvimento Local (`application.properties`)
```properties
# Conecta diretamente no PostgreSQL local
spring.datasource.url=jdbc:postgresql://localhost:5432/crm_db
spring.datasource.username=crm_user
spring.datasource.password=crm_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

#### 🐳 Docker (`application-docker.properties`)
```properties
# Usa variáveis de ambiente do Docker Compose
spring.datasource.url=jdbc:postgresql://${DB_HOST:postgres}:${DB_PORT:5432}/${DB_NAME:crm_db}
spring.datasource.username=${DB_USER:crm_user}
spring.datasource.password=${DB_PASSWORD:crm_password}
```

### Dados de Conexão Atuais

#### Para Docker Compose:
- **Host**: `postgres` (nome do container)
- **Porta**: `5432`
- **Banco**: `crm_db`
- **Usuário**: `crm_user`
- **Senha**: `crm_password`

#### Para Desenvolvimento Local:
- **Host**: `localhost`
- **Porta**: `5432`
- **Banco**: `crm_db`
- **Usuário**: `crm_user`
- **Senha**: `crm_password`

### Configurações do JPA/Hibernate

```properties
# Atualização automática do schema
spring.jpa.hibernate.ddl-auto=update

# Exibir SQLs no console (apenas desenvolvimento)
spring.jpa.show-sql=true

# Formatação das queries SQL
spring.jpa.properties.hibernate.format_sql=true

# Dialeto específico do PostgreSQL
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### Como Alterar as Configurações

1. **Para Docker**: Modifique as variáveis no `docker-compose.yml`
2. **Para Local**: Altere os valores em `application.properties`
3. **Variáveis de Ambiente**: Use `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`

### Comandos Úteis do Banco

```bash
# Conectar no banco via Docker
docker exec -it crm_postgres psql -U crm_user -d crm_db

# Ver usuários cadastrados
docker exec crm_postgres psql -U crm_user -d crm_db -c "SELECT * FROM users;"

# Ver estrutura de uma tabela
docker exec crm_postgres psql -U crm_user -d crm_db -c "\d users"

# Backup do banco
docker exec crm_postgres pg_dump -U crm_user crm_db > backup.sql
```

## 🗄️ Estrutura do Banco

### Configuração
- **Host**: localhost:5432
- **Database**: crm_db
- **User**: crm_user
- **Password**: crm_password

### Estrutura das Tabelas
- `empresas` - Informações das empresas
- `clientes` - Dados dos clientes
- `contatos` - Histórico de interações
- `oportunidades` - Oportunidades de venda

### Dados Iniciais
O banco é populado automaticamente com dados de exemplo para desenvolvimento.

## 🔧 Configuração de Ambiente

### Perfis Disponíveis
- `dev` - Desenvolvimento local
- `docker` - Execução em container
- `test` - Testes automatizados

### Variáveis de Ambiente (Docker)
```bash
DB_HOST=postgres
DB_PORT=5432
DB_NAME=crm_db
DB_USER=crm_user
DB_PASSWORD=crm_password
```

## 📈 Monitoramento

### Spring Boot Actuator
- Health Check: http://localhost:8080/actuator/health
- Métricas: http://localhost:8080/actuator/metrics
- Info: http://localhost:8080/actuator/info

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=ClienteControllerTest
```

## 📊 Relatórios Disponíveis

- Clientes por status
- Clientes por estado
- Contatos por tipo
- Oportunidades por status
- Valor total de vendas
- Probabilidade média de fechamento

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

⭐ Desenvolvido com Spring Boot por Wesley