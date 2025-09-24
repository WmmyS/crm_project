# Sistema de Autenticação CRM

## Visão Geral

O sistema CRM implementa um esquema duplo de autenticação:
- **JWT Tokens**: Para autenticação de usuários via login tradicional
- **API Keys**: Para integração com aplicações frontend e sistemas externos

## Endpoints de Autenticação

### 🔐 Login e Sessão

#### POST `/api/auth/login`
Realiza login do usuário e retorna token JWT.

**Request Body:**
```json
{
  "login": "usuario@email.com",  // username ou email
  "password": "senha123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "usuario",
  "email": "usuario@email.com",
  "nome": "Nome do Usuário",
  "role": "USER",
  "expiresAt": "2024-01-15T10:30:00"
}
```

#### POST `/api/auth/refresh`
Renova um token JWT válido.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:** Mesma estrutura do login com novo token.

#### POST `/api/auth/logout`
Invalida o token JWT atual.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
{
  "message": "Logout realizado com sucesso"
}
```

#### GET `/api/auth/me`
Retorna informações do usuário autenticado.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
{
  "id": 1,
  "username": "usuario",
  "email": "usuario@email.com",
  "role": "USER",
  "isActive": true,
  "dataCriacao": "2024-01-01T08:00:00",
  "ultimoLogin": "2024-01-15T09:30:00"
}
```

### 🔑 Gerenciamento de API Keys

#### POST `/api/auth/api-keys`
Cria uma nova API Key para o usuário autenticado.

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "name": "Frontend App",
  "description": "Chave para aplicação frontend",
  "dataExpiracao": "2024-12-31T23:59:59",  // opcional
  "limiteUso": 10000                        // opcional
}
```

**Response:**
```json
{
  "id": 1,
  "key": "crm_AbCdEfGhIjKlMnOpQrStUvWxYz123456",
  "name": "Frontend App",
  "description": "Chave para aplicação frontend",
  "isActive": true,
  "dataCriacao": "2024-01-15T10:00:00",
  "dataExpiracao": "2024-12-31T23:59:59",
  "ultimoUso": null,
  "contadorUso": 0,
  "limiteUso": 10000
}
```

#### GET `/api/auth/api-keys`
Lista todas as API Keys do usuário autenticado.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
[
  {
    "id": 1,
    "key": "crm_AbCdEfGhIjKlMnOpQrStUvWxYz123456",
    "name": "Frontend App",
    "description": "Chave para aplicação frontend",
    "isActive": true,
    "dataCriacao": "2024-01-15T10:00:00",
    "dataExpiracao": "2024-12-31T23:59:59",
    "ultimoUso": "2024-01-15T11:30:00",
    "contadorUso": 150,
    "limiteUso": 10000
  }
]
```

#### DELETE `/api/auth/api-keys/{id}`
Revoga uma API Key específica.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
{
  "message": "API Key revogada com sucesso"
}
```

## Autenticação via API Key

Para usar API Keys nas requisições, adicione o header:

```
X-API-Key: crm_AbCdEfGhIjKlMnOpQrStUvWxYz123456
```

## Segurança

### JWT Tokens
- **Expiração**: 24 horas (configurável via `jwt.expiration`)
- **Algoritmo**: HMAC SHA-256
- **Claims**: username, userId, email, role
- **Secret**: Configurável via `jwt.secret`

### API Keys
- **Formato**: `crm_` + 32 bytes em Base64URL
- **Validação**: Expiração, limite de uso, status ativo
- **Tracking**: Último uso e contador de utilizações
- **Limite**: Máximo 10 API Keys ativas por usuário

### Blacklist de Tokens
- Tokens são invalidados no logout
- Tokens antigos são invalidados no refresh
- Sistema mantém lista em memória (recomenda-se Redis em produção)

## Configuração

### application.properties
```properties
# JWT Configuration
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/crm_db
spring.datasource.username=${DB_USERNAME:crm_user}
spring.datasource.password=${DB_PASSWORD:crm_pass}
```

## Filtros de Segurança

### Ordem dos Filtros
1. **JwtAuthenticationFilter**: Processa tokens JWT
2. **ApiKeyAuthenticationFilter**: Processa API Keys

### Endpoints Públicos
- `/api/auth/**` - Endpoints de autenticação
- `/actuator/**` - Health checks
- `/swagger-ui/**` - Documentação da API
- `/v3/api-docs/**` - OpenAPI specs

## Tratamento de Erros

### Códigos de Status
- `200` - Sucesso
- `201` - API Key criada
- `400` - Dados inválidos
- `401` - Não autorizado
- `403` - Sem permissão
- `404` - Recurso não encontrado
- `500` - Erro interno

### Exemplos de Erro
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token inválido",
  "path": "/api/auth/refresh"
}
```

## Middleware para Frontend

### Interceptador de Requisições (JavaScript)
```javascript
// Configurar interceptador Axios
axios.interceptors.request.use(
  (config) => {
    const apiKey = localStorage.getItem('api_key');
    if (apiKey) {
      config.headers['X-API-Key'] = apiKey;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Tratamento de resposta
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Redirecionar para login ou renovar token
      handleAuthError();
    }
    return Promise.reject(error);
  }
);
```

### Exemplo de Uso
```javascript
// Login do usuário
const login = async (credentials) => {
  const response = await axios.post('/api/auth/login', credentials);
  localStorage.setItem('jwt_token', response.data.token);
  return response.data;
};

// Criar API Key para frontend
const createApiKey = async () => {
  const token = localStorage.getItem('jwt_token');
  const response = await axios.post('/api/auth/api-keys', {
    name: 'Frontend Application',
    description: 'Chave para aplicação web'
  }, {
    headers: { Authorization: `Bearer ${token}` }
  });
  
  localStorage.setItem('api_key', response.data.key);
  return response.data;
};
```

## Roles e Permissões

### Tipos de Usuário
- **ADMIN**: Acesso total ao sistema
- **USER**: Acesso limitado aos próprios dados
- **API**: Usuário para integrações (apenas API Keys)

### Anotações de Segurança
```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> adminOnlyEndpoint() { ... }

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public ResponseEntity<?> userEndpoint() { ... }
```

---

Este sistema oferece flexibilidade para diferentes tipos de integração, mantendo alta segurança tanto para usuários finais quanto para aplicações que consomem a API.