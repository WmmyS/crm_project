package com.wesley.crm.app.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para login da aplicação")
public class AppLoginRequestDTO {

  @NotBlank(message = "Username é obrigatório")
  @Schema(description = "Nome de usuário da aplicação", example = "appuser")
  private String username;

  @NotBlank(message = "Password é obrigatória")
  @Schema(description = "Senha da aplicação", example = "appsecret")
  private String password;

  // Constructors
  public AppLoginRequestDTO() {
  }

  public AppLoginRequestDTO(String username, String password) {
    this.username = username;
    this.password = password;
  }

  // Getters and Setters
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
}