package com.wesley.crm.infra.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CRM API")
                        .version("1.0.0")
                        .description("API do Sistema CRM para gerenciamento de clientes, empresas, contatos e oportunidades")
                        .contact(new Contact()
                                .name("Wesley")
                                .email("wesley@exemplo.com")));
    }
}