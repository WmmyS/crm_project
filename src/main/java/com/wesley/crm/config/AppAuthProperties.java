package com.wesley.crm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configurações de autenticação da aplicação.
 * 
 * Permite configurar as credenciais da aplicação através de propriedades
 * ao invés de valores hardcoded no código.
 */
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AppAuthProperties {

  /**
   * Nome de usuário para autenticação da aplicação
   */
  private String username = "appuser";

  /**
   * Senha para autenticação da aplicação
   */
  private String password = "appsecret";

  // Getters e Setters
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "AppAuthProperties{" +
        "username='" + username + '\'' +
        ", password='[PROTECTED]'" +
        '}';
  }
}