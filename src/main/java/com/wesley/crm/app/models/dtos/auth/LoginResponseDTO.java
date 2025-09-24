package com.wesley.crm.app.models.dtos.auth;

import java.time.LocalDateTime;

public class LoginResponseDTO {

  private String token;
  private String type = "Bearer";
  private String username;
  private String email;
  private String nome;
  private String role;
  private LocalDateTime expiresAt;

  // Constructors
  public LoginResponseDTO() {
  }

  public LoginResponseDTO(String token, String username, String email, String nome, String role,
      LocalDateTime expiresAt) {
    this.token = token;
    this.username = username;
    this.email = email;
    this.nome = nome;
    this.role = role;
    this.expiresAt = expiresAt;
  }

  // Getters and Setters
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }
}