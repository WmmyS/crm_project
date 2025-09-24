package com.wesley.crm.app.services;

import com.wesley.crm.domain.entities.ApiKey;
import com.wesley.crm.domain.entities.RotatingToken;
import com.wesley.crm.exceptions.CrmException;
import com.wesley.crm.infra.database.ApiKeyRepository;
import com.wesley.crm.infra.database.RotatingTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RotatingTokenService {

  @Autowired
  private RotatingTokenRepository rotatingTokenRepository;

  @Autowired
  private ApiKeyRepository apiKeyRepository;

  private final SecureRandom secureRandom = new SecureRandom();

  // Configurações
  private static final int TOKEN_EXPIRATION_MINUTES = 15; // Token expira em 15 minutos
  private static final int MAX_TOKENS_PER_API_KEY = 1; // Apenas 1 token ativo por API Key

  /**
   * Gera um novo token rotativo para uma API Key específica
   */
  public String generateRotatingToken(String apiKeyValue) {
    // Buscar API Key
    ApiKey apiKey = apiKeyRepository.findActiveByKey(apiKeyValue)
        .orElseThrow(() -> new CrmException("API Key inválida", HttpStatus.UNAUTHORIZED));

    // Revogar tokens existentes para esta API Key
    rotatingTokenRepository.deactivateTokensByApiKey(apiKey);

    // Gerar novo token
    String tokenValue = generateSecureToken();

    // Criar novo token rotativo
    RotatingToken rotatingToken = new RotatingToken(tokenValue, apiKey, TOKEN_EXPIRATION_MINUTES);
    rotatingTokenRepository.save(rotatingToken);

    return tokenValue;
  }

  /**
   * Valida um token rotativo
   */
  public boolean validateRotatingToken(String tokenValue, String apiKeyValue) {
    try {
      // Buscar token
      Optional<RotatingToken> tokenOpt = rotatingTokenRepository.findByTokenAndIsActiveTrue(tokenValue);
      if (tokenOpt.isEmpty()) {
        return false;
      }

      RotatingToken token = tokenOpt.get();

      // Verificar se o token pertence à API Key fornecida
      if (!token.getApiKey().getKey().equals(apiKeyValue)) {
        return false;
      }

      // Verificar se o token é válido
      if (!token.isValid()) {
        // Token expirado/inválido - desativar
        token.revoke();
        rotatingTokenRepository.save(token);
        return false;
      }

      // Incrementar contador de uso
      token.incrementUsage();
      rotatingTokenRepository.save(token);

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Renova um token existente (estende o tempo de expiração)
   */
  public String renewRotatingToken(String apiKeyValue) {
    ApiKey apiKey = apiKeyRepository.findActiveByKey(apiKeyValue)
        .orElseThrow(() -> new CrmException("API Key inválida", HttpStatus.UNAUTHORIZED));

    // Buscar token ativo atual
    Optional<RotatingToken> currentTokenOpt = rotatingTokenRepository
        .findFirstByApiKeyAndIsActiveTrueOrderByCreatedAtDesc(apiKey);

    if (currentTokenOpt.isPresent()) {
      RotatingToken currentToken = currentTokenOpt.get();

      // Se o token ainda tem mais de 5 minutos, apenas renova
      if (currentToken.getExpiresAt().isAfter(LocalDateTime.now().plusMinutes(5))) {
        currentToken.renew(TOKEN_EXPIRATION_MINUTES);
        rotatingTokenRepository.save(currentToken);
        return currentToken.getToken();
      }
    }

    // Caso contrário, gera um novo token
    return generateRotatingToken(apiKeyValue);
  }

  /**
   * Obtém informações sobre o token ativo de uma API Key
   */
  public Optional<RotatingToken> getActiveToken(String apiKeyValue) {
    ApiKey apiKey = apiKeyRepository.findActiveByKey(apiKeyValue)
        .orElse(null);

    if (apiKey == null) {
      return Optional.empty();
    }

    return rotatingTokenRepository.findFirstByApiKeyAndIsActiveTrueOrderByCreatedAtDesc(apiKey);
  }

  /**
   * Limpa tokens expirados automaticamente (executa a cada 5 minutos)
   */
  @Scheduled(fixedRate = 300000) // 5 minutos
  public void cleanupExpiredTokens() {
    int deactivated = rotatingTokenRepository.deactivateExpiredTokens(LocalDateTime.now());
    if (deactivated > 0) {
      System.out.println("🧹 Limpeza automática: " + deactivated + " tokens rotativos expirados foram desativados");
    }
  }

  /**
   * Gera um token seguro usando SHA-256 + Base64URL
   */
  private String generateSecureToken() {
    try {
      // Gerar bytes aleatórios
      byte[] randomBytes = new byte[32];
      secureRandom.nextBytes(randomBytes);

      // Adicionar timestamp para uniqueness
      String data = Base64.getEncoder().encodeToString(randomBytes) + System.currentTimeMillis();

      // Hash SHA-256
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

      // Converter para Base64URL (sem caracteres especiais)
      return "rt_" + Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    } catch (Exception e) {
      throw new CrmException("Erro ao gerar token rotativo", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Revoga todos os tokens de uma API Key
   */
  public void revokeAllTokens(String apiKeyValue) {
    ApiKey apiKey = apiKeyRepository.findActiveByKey(apiKeyValue)
        .orElseThrow(() -> new CrmException("API Key inválida", HttpStatus.UNAUTHORIZED));

    rotatingTokenRepository.deactivateTokensByApiKey(apiKey);
  }

  /**
   * Obtém estatísticas de uso dos tokens
   */
  public long getActiveTokenCount(String apiKeyValue) {
    ApiKey apiKey = apiKeyRepository.findActiveByKey(apiKeyValue)
        .orElse(null);

    if (apiKey == null) {
      return 0;
    }

    return rotatingTokenRepository.countByApiKeyAndIsActiveTrue(apiKey);
  }
}