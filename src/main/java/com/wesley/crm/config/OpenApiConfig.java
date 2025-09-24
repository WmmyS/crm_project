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

                                                                ### 🔐 Sistema de Autenticação Dual Simplificado

                                                                Esta API oferece **2 TIPOS DE AUTENTICAÇÃO** para diferentes cenários:

                                                                #### 👥 1. Autenticação de Usuário (Interface Web/Mobile)
                                                                **Para usuários do sistema:**
                                                                ```
                                                                POST /api/auth/register → Criar conta (público)
                                                                POST /api/auth/login → { "email": "user@email.com", "senha": "senha" }
                                                                ```
                                                                **Uso nos endpoints:**
                                                                ```
                                                                Authorization: Bearer <jwt-token>
                                                                ```

                                                                #### 🤖 2. Autenticação de Aplicação (Integração/API)
                                                                **Para aplicações que acessam a API programaticamente:**
                                                                ```
                                                                POST /api/auth/app-login → { "username": "<config>", "password": "<config>" }
                                                                ```
                                                                **Credenciais configuráveis via properties (padrão: appuser/appsecret)**
                                                                **Uso nos endpoints:**
                                                                ```
                                                                X-App-Token: <application-token>
                                                                ```

                                                                ### 📋 Como testar no Swagger:

                                                                **✅ Método 1 - Para Usuários:**
                                                                1. Use `/api/auth/login` com email e senha de usuário
                                                                2. Clique em "Authorize" → Cole o JWT Token no campo "BearerAuth"
                                                                3. Teste qualquer endpoint protegido

                                                                **✅ Método 2 - Para Aplicações/Integração:**
                                                                1. Use `/api/auth/app-login` com credenciais configuráveis (padrão: appuser/appsecret)
                                                                2. Clique em "Authorize" → Cole o token no campo "AppTokenAuth"
                                                                3. Teste qualquer endpoint protegido

                                                                ### ⏰ Expiração dos Tokens:
                                                                - **JWT Token**: 24 horas (usuários)
                                                                - **Application Token**: 15 minutos (renovação obrigatória)

                                                                ### 🎯 Tipos de Acesso:
                                                                - **ADMIN**: Acesso completo ao sistema
                                                                - **USER**: Acesso padrão aos recursos de CRM
                                                                - **APPLICATION**: Acesso programático com renovação automática
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
                                                // JWT Bearer Token para usuários
                                                .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .description("🔑 **JWT Token Authentication (Usuários)**\n\n"
                                                                                +
                                                                                "1. Registre-se em `/api/auth/register`\n"
                                                                                +
                                                                                "2. Faça login em `/api/auth/login`\n" +
                                                                                "3. Copie o token da resposta\n" +
                                                                                "4. Cole aqui **sem** o prefixo 'Bearer'"))

                                                // Application Token para aplicações
                                                .addSecuritySchemes("AppTokenAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.APIKEY)
                                                                .in(SecurityScheme.In.HEADER)
                                                                .name("X-App-Token")
                                                                .description("🤖 **Application Token Authentication**\n\n"
                                                                                +
                                                                                "1. Faça login da aplicação em `/api/auth/app-login`\n"
                                                                                +
                                                                                "2. Use credenciais configuradas em `application.properties`\n"
                                                                                +
                                                                                "3. **Padrão**: `{\"username\": \"appuser\", \"password\": \"appsecret\"}`\n"
                                                                                +
                                                                                "4. Copie o token da resposta (válido por 15 minutos)\n"
                                                                                +
                                                                                "5. Cole aqui o token completo\n" +
                                                                                "6. **Renovação obrigatória a cada 15 minutos**")))

                                // Segurança Global - suporte a ambos os tipos de autenticação
                                .addSecurityItem(new SecurityRequirement()
                                                .addList("BearerAuth"))
                                .addSecurityItem(new SecurityRequirement()
                                                .addList("AppTokenAuth"));
        }
}