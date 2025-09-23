package com.wesley.crm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CRM API")
                        .version("1.0.0")
                        .description("API para gerenciamento de CRM - Clientes, Empresas, Contatos e Oportunidades")
                        .contact(new Contact()
                                .name("Wesley")
                                .email("wesley@example.com")));
    }
}