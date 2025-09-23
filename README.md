# CRM Application

Sistema de CRM (Customer Relationship Management) desenvolvido em Java com Spring Boot.

## 🚀 Tecnologias Utilizadas

- **Java 17** (LTS)
- **Spring Boot 3.x**
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

## 🛠️ Como Executar

### Pré-requisitos
- **Docker e Docker Compose** (obrigatório)
- **Java 17+** (opcional - apenas para desenvolvimento local)
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

## 🗄️ Banco de Dados

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