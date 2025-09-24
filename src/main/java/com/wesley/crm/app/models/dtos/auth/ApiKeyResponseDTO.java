package com.wesley.crm.app.models.dtos.auth;

import java.time.LocalDateTime;

public class ApiKeyResponseDTO {

  private Long id;
  private String key;
  private String name;
  private String description;
  private Boolean isActive;
  private LocalDateTime dataCriacao;
  private LocalDateTime dataExpiracao;
  private LocalDateTime ultimoUso;
  private Long contadorUso;
  private Long limiteUso;

  // Constructors
  public ApiKeyResponseDTO() {
  }

  public ApiKeyResponseDTO(Long id, String key, String name, String description, Boolean isActive,
      LocalDateTime dataCriacao, LocalDateTime dataExpiracao, LocalDateTime ultimoUso,
      Long contadorUso, Long limiteUso) {
    this.id = id;
    this.key = key;
    this.name = name;
    this.description = description;
    this.isActive = isActive;
    this.dataCriacao = dataCriacao;
    this.dataExpiracao = dataExpiracao;
    this.ultimoUso = ultimoUso;
    this.contadorUso = contadorUso;
    this.limiteUso = limiteUso;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

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

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public LocalDateTime getDataCriacao() {
    return dataCriacao;
  }

  public void setDataCriacao(LocalDateTime dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public LocalDateTime getDataExpiracao() {
    return dataExpiracao;
  }

  public void setDataExpiracao(LocalDateTime dataExpiracao) {
    this.dataExpiracao = dataExpiracao;
  }

  public LocalDateTime getUltimoUso() {
    return ultimoUso;
  }

  public void setUltimoUso(LocalDateTime ultimoUso) {
    this.ultimoUso = ultimoUso;
  }

  public Long getContadorUso() {
    return contadorUso;
  }

  public void setContadorUso(Long contadorUso) {
    this.contadorUso = contadorUso;
  }

  public Long getLimiteUso() {
    return limiteUso;
  }

  public void setLimiteUso(Long limiteUso) {
    this.limiteUso = limiteUso;
  }
}