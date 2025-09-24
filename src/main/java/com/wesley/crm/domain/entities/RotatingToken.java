package com.wesley.crm.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade para tokens rotativos - tokens temporários que se renovam
 * automaticamente
 * para acesso seguro do frontend à API
 */
@Entity
@Table(name = "rotating_tokens")
public class RotatingToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "token", unique = true, nullable = false, length = 255)
  private String token;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "api_key_id", nullable = false)
  private ApiKey apiKey;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Column(name = "usage_count", nullable = false)
  private Integer usageCount = 0;

  // Construtores
  public RotatingToken() {
    this.createdAt = LocalDateTime.now();
    this.isActive = true;
    this.usageCount = 0;
  }

  public RotatingToken(String token, ApiKey apiKey, int expirationMinutes) {
    this();
    this.token = token;
    this.apiKey = apiKey;
    this.expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
  }

  // Métodos de negócio
  public boolean isExpired() {
    return LocalDateTime.now().isAfter(this.expiresAt);
  }

  public boolean isValid() {
    return this.isActive && !isExpired() && this.apiKey != null && this.apiKey.isValid();
  }

  public void incrementUsage() {
    this.usageCount++;
  }

  public void revoke() {
    this.isActive = false;
  }

  public void renew(int expirationMinutes) {
    this.expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
  }

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public ApiKey getApiKey() {
    return apiKey;
  }

  public void setApiKey(ApiKey apiKey) {
    this.apiKey = apiKey;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Integer getUsageCount() {
    return usageCount;
  }

  public void setUsageCount(Integer usageCount) {
    this.usageCount = usageCount;
  }
}