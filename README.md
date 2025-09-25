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

## 🔐 Sistema de Autenticação Dupla

Este CRM implementa um **sistema de autenticação dupla obrigatória** para máxima segurança:

### **🔑 Duas Camadas de Segurança:**
1. **JWT Token** - Identifica e autentica o **usuário** que está usando a aplicação
2. **Application Token** - Identifica e autentica a **aplicação frontend** (renova a cada 15min)

### **📱 Para Aplicações Frontend:**
```bash
# Todas as requisições para endpoints protegidos DEVEM incluir:
Authorization: Bearer <jwt-token>        # Usuário autenticado
X-App-Token: <application-token>         # Aplicação autorizada
```

### **🔄 Fluxo de Autenticação Completo:**

#### **1. Login da Aplicação (Frontend)**
```bash
POST /api/auth/app-login
Content-Type: application/json

{
  "username": "appuser",
  "password": "appsecret"
}
```

**Resposta:**
```json
{
  "token": "app_eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "issuedAt": "2025-01-15T10:30:00",
  "expiresAt": "2025-01-15T10:45:00",
  "expiresInMinutes": 15,
  "appName": "appuser"
}
```

#### **2. Login do Usuário**
```bash
POST /api/auth/login
Content-Type: application/json

{
  "login": "wesley",           # Pode ser username ou email
  "password": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "wesley",
  "email": "wesley@email.com",
  "nome": "Wesley",
  "role": "USER",
  "expiresAt": "2025-01-16T10:30:00"
}
```

#### **3. Usando Endpoints Protegidos**
```bash
GET /api/clientes
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...    # JWT do usuário
X-App-Token: app_eyJhbGciOiJIUzI1NiJ9...         # Token da aplicação
```

### **🛡️ Vantagens da Segurança Dupla:**
- **Controle de usuário**: JWT identifica quem está usando
- **Controle de aplicação**: Application Token identifica qual app frontend
- **Renovação automática**: Application Token expira em 15min (anti-replay)
- **Zero acesso sem ambos**: Impossível acessar com apenas 1 token

### **⚠️ Importante para o Frontend:**
1. **Sempre faça login da aplicação primeiro** para obter o Application Token
2. **Renove o Application Token a cada 10-12 minutos** para evitar expiração
3. **Inclua ambos os tokens em TODAS as requisições** para endpoints protegidos
4. **Trate erros 401** renovando os tokens conforme necessário

## 🛠️ Como Executar

### Pré-requisitos
- **Docker e Docker Compose** (obrigatório)
- **Java 21+** (opcional - apenas para desenvolvimento local)
- **Maven 3.6+** (opcional - apenas para desenvolvimento local)

### Executando com Docker (Recomendado)

1. **Clone o repositório e navegue até a pasta:**
```bash
cd www_crm
```

2. **Execute o script automatizado:**
```bash
./start.sh
```

3. **Acesse a aplicação:**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Banco PostgreSQL: localhost:5432

### Executando Localmente (Desenvolvimento)

```bash
./start-dev.sh
```

## 📚 Documentação da API

A documentação completa da API está disponível via Swagger UI:
- **URL**: http://localhost:8080/swagger-ui.html
- **JSON**: http://localhost:8080/v3/api-docs

### **🔐 Como Usar o Swagger UI:**

1. **Acesse**: http://localhost:8080/swagger-ui.html
2. **Faça login da aplicação**: Use o endpoint `/api/auth/app-login`
3. **Faça login do usuário**: Use o endpoint `/api/auth/login`
4. **Clique em "Authorize"** (botão do cadeado no topo direito)
5. **Configure os tokens:**
   - **bearerAuth**: Cole seu JWT token
   - **appToken**: Cole seu Application Token
6. **Teste os endpoints protegidos**

### Principais Endpoints

#### **🔑 Autenticação**
- `POST /api/auth/app-login` - Login da aplicação (público)
- `POST /api/auth/register` - Cadastrar usuário (público)
- `POST /api/auth/login` - Login do usuário (público)
- `POST /api/auth/refresh` - Renovar JWT (requer apenas JWT)
- `POST /api/auth/logout` - Logout (requer apenas JWT)
- `GET /api/auth/me` - Dados do usuário (requer ambos os tokens)

#### **👥 Clientes** (Requer ambos os tokens)
- `GET /api/clientes` - Listar clientes (paginado)
- `POST /api/clientes` - Criar cliente
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `PUT /api/clientes/{id}` - Atualizar cliente
- `DELETE /api/clientes/{id}` - Deletar cliente

#### **🏢 Empresas** (Requer ambos os tokens)
- `GET /api/empresas` - Listar empresas
- `POST /api/empresas` - Criar empresa
- `GET /api/empresas/{id}` - Buscar empresa por ID

#### **📞 Contatos** (Requer ambos os tokens)
- `GET /api/contatos` - Listar contatos
- `POST /api/contatos` - Registrar contato
- `GET /api/contatos/cliente/{clienteId}` - Contatos de um cliente

#### **💼 Oportunidades** (Requer ambos os tokens)
- `GET /api/oportunidades` - Listar oportunidades
- `POST /api/oportunidades` - Criar oportunidade
- `GET /api/oportunidades/status/{status}` - Por status

## 🧪 Teste Completo da Autenticação

### **Via cURL:**

```bash
# 1. Login da aplicação
curl -X POST http://localhost:8080/api/auth/app-login \
  -H "Content-Type: application/json" \
  -d '{"username": "appuser", "password": "appsecret"}'

# 2. Cadastrar usuário (opcional)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "test123", "email": "test@email.com", "nome": "Test User"}'

# 3. Login do usuário
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "testuser", "password": "test123"}'

# 4. Usar endpoint protegido (substitua os tokens)
curl -X GET http://localhost:8080/api/clientes \
  -H "Authorization: Bearer SEU_JWT_TOKEN" \
  -H "X-App-Token: SEU_APPLICATION_TOKEN"
```

### **Via JavaScript (Frontend):**

```javascript
// 1. Login da aplicação
const appLogin = async () => {
  const response = await fetch('/api/auth/app-login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      username: 'appuser',
      password: 'appsecret'
    })
  });
  const data = await response.json();
  localStorage.setItem('appToken', data.token);
  return data.token;
};

// 2. Login do usuário
const userLogin = async (username, password) => {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      login: username,
      password: password
    })
  });
  const data = await response.json();
  localStorage.setItem('userToken', data.token);
  return data.token;
};

// 3. Fazer requisições autenticadas
const fetchClientes = async () => {
  const userToken = localStorage.getItem('userToken');
  const appToken = localStorage.getItem('appToken');
  
  const response = await fetch('/api/clientes', {
    headers: {
      'Authorization': `Bearer ${userToken}`,
      'X-App-Token': appToken
    }
  });
  
  if (response.status === 401) {
    // Renovar tokens se necessário
    await appLogin();
    // Retry da requisição...
  }
  
  return response.json();
};

// 4. Renovar Application Token automaticamente
setInterval(async () => {
  await appLogin();
}, 12 * 60 * 1000); // A cada 12 minutos
```

## 🔐 Configurações de Segurança

### **Credenciais da Aplicação (application.properties):**
```properties
# Configuração de Autenticação da Aplicação
app.auth.username=appuser
app.auth.password=appsecret

# Configuração JWT
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000  # 24 horas
```

### **Roles e Permissões:**
- **ADMIN**: Acesso completo ao sistema
- **USER**: Acesso aos recursos padrão do CRM
- **API**: Role automática para autenticação via Application Token

### **Validações de Segurança:**
- ✅ Username único (mínimo 3 caracteres)
- ✅ Email único e válido
- ✅ Senha mínima de 6 caracteres
- ✅ JWT com expiração de 24 horas
- ✅ Application Token com expiração de 15 minutos
- ✅ Blacklist de tokens para logout seguro
- ✅ Criptografia BCrypt para senhas

## 🗄️ Configuração do Banco de Dados

### **Dados de Conexão:**
- **Host**: localhost:5432 (local) / postgres:5432 (Docker)
- **Database**: crm_db
- **User**: crm_user
- **Password**: crm_password

### **Estrutura das Tabelas:**
- `users` - Usuários do sistema
- `empresas` - Informações das empresas
- `clientes` - Dados dos clientes
- `contatos` - Histórico de interações
- `oportunidades` - Oportunidades de venda

### **Comandos Úteis:**
```bash
# Conectar no banco via Docker
docker exec -it crm_postgres psql -U crm_user -d crm_db

# Ver usuários cadastrados
docker exec crm_postgres psql -U crm_user -d crm_db -c "SELECT * FROM users;"
```

## 📈 Monitoramento

### **Spring Boot Actuator:**
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