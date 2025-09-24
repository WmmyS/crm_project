package com.wesley.crm.app.models.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

  @NotBlank(message = "Login é obrigatório")
  private String login; // pode ser username ou email

  @NotBlank(message = "Senha é obrigatória")
  private String password;

  // Constructors
  public LoginRequestDTO() {
  }

  public LoginRequestDTO(String login, String password) {
    this.login = login;
    this.password = password;
  }

  // Getters and Setters
  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}