package com.wesley.crm.infra.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.wesley.crm.infra.database")
public class DatabaseConfiguration {
    // Configurações específicas do banco se necessário
}