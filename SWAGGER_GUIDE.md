# 🔐 Guia do Swagger - CRM Application API

## 🚀 Como Testar no Swagger UI

Acesse: http://localhost:8080/swagger-ui.html

### 📋 Passo a Passo para Autenticação

#### 1. 🆓 Endpoints Públicos (Não precisam de autenticação)
- `POST /api/auth/register` - Cadastrar usuário
- `POST /api/auth/login` - Fazer login

#### 2. 🔐 Endpoints Protegidos (Precisam de autenticação)
- Todos os outros endpoints requerem JWT Token ou API Key

---

## 🎯 Fluxo Completo de Teste

### Passo 1: Cadastrar Usuário
1. Vá até **Autenticação** → `POST /api/auth/register`
2. Clique em **Try it out**
3. Use este JSON:
```json
{
  "username": "meuusuario",
  "email": "meu@email.com",
  "password": "senha123",
  "nome": "Meu Nome"
}
```
4. Clique em **Execute**

### Passo 2: Fazer Login
1. Vá até `POST /api/auth/login`
2. Use este JSON:
```json
{
  "login": "meuusuario",
  "password": "senha123"
}
```
3. **COPIE O TOKEN** da resposta (sem o "Bearer")

### Passo 3: Configurar Autenticação no Swagger
1. Clique no botão **🔒 Authorize** no topo da página
2. Em **BearerAuth**, cole seu token JWT (sem "Bearer")
3. Clique em **Authorize** e depois **Close**

### Passo 4: Testar Endpoints Protegidos
Agora você pode testar qualquer endpoint! Por exemplo:
- `GET /api/auth/me` - Ver seus dados
- `GET /api/clientes` - Listar clientes
- `POST /api/auth/api-keys` - Criar API Key

---

## 🗝️ Usando API Keys

### Passo 1: Criar API Key
1. **Primeiro faça login** e configure o JWT (passos acima)
2. Vá até `POST /api/auth/api-keys`
3. Use este JSON:
```json
{
  "name": "Minha API Key"
}
```
4. **COPIE A CHAVE** da resposta (formato: crm_...)

### Passo 2: Usar API Key
1. Clique em **🔒 Authorize** novamente
2. Em **ApiKeyAuth**, cole sua API Key completa
3. Clique em **Authorize** e **Close**

---

## ⚡ Tokens de Exemplo

### JWT Token (válido por 24h):
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzd2FnZ2VyX3VzZXIiLCJ1c2VySWQiOjQsImVtYWlsIjoic3dhZ2dlckB0ZXN0ZS5jb20iLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc1ODczOTI3NywiZXhwIjoxNzU4ODI1Njc2fQ.thd3fQRQwvFqHbKL4ACy8CecC9l0pc-0ePuH2-yRF5k
```

### API Key de Exemplo:
```
crm_-X9G3Cb_7iOJN9DK5gu6am3mUSSzECYQWyf0WUJ_xFA
```

---

## 🔧 Troubleshooting

### ❌ "401 Unauthorized"
- Verifique se configurou a autenticação corretamente
- Seu token pode ter expirado (24h)
- Faça logout e login novamente

### ❌ "403 Forbidden"
- Você não tem permissão para este endpoint
- Verifique seu role (USER, ADMIN, API)

### ❌ Token inválido
- Não inclua "Bearer " quando colar no Swagger
- Copie o token completo da resposta do login
- Verifique se não há espaços extras

---

## 🎨 Dicas do Swagger UI

- 🟢 **Verde**: Endpoints públicos
- 🔒 **Com cadeado**: Endpoints protegidos
- 📋 **Try it out**: Testar endpoint
- 🔄 **Execute**: Enviar requisição
- 📝 **Request body**: JSON de exemplo preenchido
- ✅ **Response**: Resultado da chamada

---

## 🚀 Próximos Passos

1. **Cadastre-se** usando `/api/auth/register`
2. **Faça login** com `/api/auth/login`
3. **Configure a autenticação** no botão Authorize
4. **Explore todos os endpoints** do CRM!

**Swagger UI**: http://localhost:8080/swagger-ui.html
**Health Check**: http://localhost:8080/actuator/health