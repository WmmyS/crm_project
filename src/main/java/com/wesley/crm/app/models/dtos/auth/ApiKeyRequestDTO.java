package com.wesley.crm.app.models.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ApiKeyRequestDTO {

  @NotBlank(message = "Nome é obrigatório")
  @Size(max = 100, message = "Nome não pode ter mais que 100 caracteres")
  private String name;

  @Size(max = 255, message = "Descrição não pode ter mais que 255 caracteres")
  private String description;

  private LocalDateTime dataExpiracao;

  private Long limiteUso;

  // Constructors
  public ApiKeyRequestDTO() {
  }

  public ApiKeyRequestDTO(String name, String description) {
    this.name = name;
    this.description = description;
  }

  // Getters and Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getDataExpiracao() {
    return dataExpiracao;
  }

  public void setDataExpiracao(LocalDateTime dataExpiracao) {
    this.dataExpiracao = dataExpiracao;
  }

  public Long getLimiteUso() {
    return limiteUso;
  }

  public void setLimiteUso(Long limiteUso) {
    this.limiteUso = limiteUso;
  }
}