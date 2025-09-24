package com.wesley.crm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("CRM Application API")
                                                .version("1.0.0")
                                                .description("""
                                                                ## 🚀 API REST para sistema de CRM (Customer Relationship Management)

                                                                ### 🔐 Sistema de Autenticação Tripla Obrigatória

                                                                **⚠️ IMPORTANTE**: Esta API requer **3 TOKENS OBRIGATÓRIOS** para todos os endpoints protegidos:

                                                                #### 🔑 Fluxo Completo de Autenticação:

                                                                **1. � Obter JWT Token (identifica o usuário):**
                                                                ```
                                                                POST /api/auth/register → Criar conta (público)
                                                                POST /api/auth/login → { "login": "seu_usuario", "password": "senha" }
                                                                ```

                                                                **2. 🗝️ Obter API Key (identifica a aplicação):**
                                                                ```
                                                                POST /api/auth/api-key → Usar JWT para criar API Key
                                                                ```

                                                                **3. 🔄 Gerar Rotating Token (prova de legitimidade - 15min):**
                                                                ```
                                                                POST /api/auth/rotating-token/generate → Usar JWT + API Key
                                                                ```

                                                                **4. 🛡️ Usar nos endpoints protegidos (TODOS OS 3 OBRIGATÓRIOS):**
                                                                ```
                                                                Authorization: Bearer <jwt-token>
                                                                X-API-Key: <sua-api-key>
                                                                X-Rotating-Token: <rotating-token>
                                                                ```

                                                                ### 📋 Como testar no Swagger:
                                                                1. **Registrar**: Use `/api/auth/register` (não precisa auth)
                                                                2. **Login**: Use `/api/auth/login` com `{"login": "usuario", "password": "senha"}`
                                                                3. **Authorize**: Clique em "Authorize" → Cole JWT Token no BearerAuth
                                                                4. **API Key**: Crie com `/api/auth/api-key` → Cole no ApiKeyAuth
                                                                5. **Rotating Token**: Gere com `/api/auth/rotating-token/generate`
                                                                6. **Testar**: Use qualquer endpoint protegido com os 3 tokens

                                                                ### ⏰ Expiração dos Tokens:
                                                                - **JWT Token**: 24 horas
                                                                - **API Key**: Permanente (até ser revogada)
                                                                - **Rotating Token**: 15 minutos (renovação automática)

                                                                ### 🎯 Tipos de Acesso:
                                                                - **ADMIN**: Acesso completo ao sistema
                                                                - **USER**: Acesso padrão aos recursos de CRM
                                                                - **API**: Acesso programático via API Keys
                                                                """)
                                                .contact(new Contact()
                                                                .name("Wesley")
                                                                .email("contato@wesley.com")
                                                                .url("https://github.com/wesley"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")))

                                // Configuração de Segurança
                                .components(new Components()
                                                // JWT Bearer Token
                                                .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .description("🔑 **JWT Token Authentication**\n\n" +
                                                                                "1. Registre-se em `/api/auth/register`\n"
                                                                                +
                                                                                "2. Faça login em `/api/auth/login`\n" +
                                                                                "3. Copie o token da resposta\n" +
                                                                                "4. Cole aqui **sem** o prefixo 'Bearer'"))

                                                // API Key
                                                .addSecuritySchemes("ApiKeyAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.APIKEY)
                                                                .in(SecurityScheme.In.HEADER)
                                                                .name("X-API-Key")
                                                                .description("🗝️ **API Key Authentication**\n\n" +
                                                                                "1. Faça login para obter JWT token\n" +
                                                                                "2. Use o token para criar API Key em `/api/auth/api-keys`\n"
                                                                                +
                                                                                "3. Copie a chave da resposta (formato: crm_...)\n"
                                                                                +
                                                                                "4. Cole aqui a chave completa")))

                                // Segurança Global - todas as rotas precisam de autenticação exceto as públicas
                                .addSecurityItem(new SecurityRequirement()
                                                .addList("BearerAuth")
                                                .addList("ApiKeyAuth"));
        }
}