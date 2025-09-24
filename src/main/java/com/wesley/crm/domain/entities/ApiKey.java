package com.wesley.crm.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
public class ApiKey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "api_key", nullable = false, unique = true, length = 100)
  private String key;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = 255)
  private String description;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Column(name = "data_criacao", nullable = false, updatable = false)
  private LocalDateTime dataCriacao;

  @Column(name = "data_expiracao")
  private LocalDateTime dataExpiracao;

  @Column(name = "ultimo_uso")
  private LocalDateTime ultimoUso;

  @Column(name = "contador_uso", nullable = false)
  private Long contadorUso = 0L;

  @Column(name = "limite_uso")
  private Long limiteUso;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  // Constructors
  public ApiKey() {
  }

  public ApiKey(String key, String name, String description, User user) {
    this.key = key;
    this.name = name;
    this.description = description;
    this.user = user;
  }

  // Lifecycle callbacks
  @PrePersist
  protected void onCreate() {
    dataCriacao = LocalDateTime.now();
  }

  // Business methods
  public boolean isValid() {
    if (!isActive) {
      return false;
    }

    if (dataExpiracao != null && dataExpiracao.isBefore(LocalDateTime.now())) {
      return false;
    }

    if (limiteUso != null && contadorUso >= limiteUso) {
      return false;
    }

    return true;
  }

  public void incrementarUso() {
    this.contadorUso++;
    this.ultimoUso = LocalDateTime.now();
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}