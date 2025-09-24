package com.wesley.crm.app.models.dtos.auth;

import java.time.LocalDateTime;

/**
 * DTO para resposta de token rotativo
 */
public class RotatingTokenResponseDTO {

  private String token;
  private LocalDateTime expiresAt;
  private String status;
  private String message;
  private Integer expiresInMinutes;

  // Construtores
  public RotatingTokenResponseDTO() {
  }

  public RotatingTokenResponseDTO(String token, LocalDateTime expiresAt, String status, String message) {
    this.token = token;
    this.expiresAt = expiresAt;
    this.status = status;
    this.message = message;
    this.expiresInMinutes = calculateExpiresInMinutes(expiresAt);
  }

  private Integer calculateExpiresInMinutes(LocalDateTime expiresAt) {
    if (expiresAt == null)
      return null;
    return (int) java.time.Duration.between(LocalDateTime.now(), expiresAt).toMinutes();
  }

  // Getters e Setters
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
    this.expiresInMinutes = calculateExpiresInMinutes(expiresAt);
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getExpiresInMinutes() {
    return expiresInMinutes;
  }

  public void setExpiresInMinutes(Integer expiresInMinutes) {
    this.expiresInMinutes = expiresInMinutes;
  }
}