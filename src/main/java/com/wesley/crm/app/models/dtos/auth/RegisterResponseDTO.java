package com.wesley.crm.app.models.dtos.auth;

/**
 * DTO para resposta de cadastro de usuário
 */
public class RegisterResponseDTO {

  private Long id;
  private String username;
  private String email;
  private String nome;
  private String role;
  private String message;

  // Construtores
  public RegisterResponseDTO() {
  }

  public RegisterResponseDTO(Long id, String username, String email, String nome, String role, String message) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.nome = nome;
    this.role = role;
    this.message = message;
  }

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}