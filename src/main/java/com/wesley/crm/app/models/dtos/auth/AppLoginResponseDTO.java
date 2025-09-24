package com.wesley.crm.app.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta do login da aplicação")
public class AppLoginResponseDTO {

  @Schema(description = "Token de acesso da aplicação", example = "eyJhbGciOiJIUzI1NiJ9...")
  private String token;

  @Schema(description = "Tipo do token", example = "Bearer")
  private String tipo;

  @Schema(description = "Data e hora de criação do token")
  private LocalDateTime criadoEm;

  @Schema(description = "Data e hora de expiração do token")
  private LocalDateTime expiraEm;

  @Schema(description = "Duração em minutos", example = "15")
  private long duracaoMinutos;

  @Schema(description = "Username da aplicação autenticada", example = "appuser")
  private String username;

  // Constructors
  public AppLoginResponseDTO() {
  }

  public AppLoginResponseDTO(String token, String tipo, LocalDateTime criadoEm,
      LocalDateTime expiraEm, long duracaoMinutos, String username) {
    this.token = token;
    this.tipo = tipo;
    this.criadoEm = criadoEm;
    this.expiraEm = expiraEm;
    this.duracaoMinutos = duracaoMinutos;
    this.username = username;
  }

  // Construtor simplificado para casos de erro
  public AppLoginResponseDTO(String token, String tipo, long duracaoMinutos, String username) {
    this.token = token;
    this.tipo = tipo;
    this.duracaoMinutos = duracaoMinutos;
    this.username = username;
  }

  // Getters and Setters
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getExpiraEm() {
    return expiraEm;
  }

  public void setExpiraEm(LocalDateTime expiraEm) {
    this.expiraEm = expiraEm;
  }

  public long getDuracaoMinutos() {
    return duracaoMinutos;
  }

  public void setDuracaoMinutos(long duracaoMinutos) {
    this.duracaoMinutos = duracaoMinutos;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}