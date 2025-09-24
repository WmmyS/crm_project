package com.wesley.crm.app.services;

import com.wesley.crm.exceptions.CrmException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApplicationTokenService {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationTokenService.class);

  @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
  private String jwtSecret;

  // Token de aplicação com duração de 15 minutos
  private static final long APP_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15 minutos em millisegundos

  /**
   * Gera um token exclusivo para a aplicação com duração de 15 minutos
   */
  public String generateApplicationToken() {
    try {
      Date now = new Date();
      Date expiryDate = new Date(now.getTime() + APP_TOKEN_EXPIRATION);

      Map<String, Object> claims = new HashMap<>();
      claims.put("type", "APPLICATION");
      claims.put("purpose", "API_ACCESS");
      claims.put("iat", now.getTime());

      SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

      String token = Jwts.builder()
          .claims(claims)
          .subject("CRM_APPLICATION")
          .issuedAt(now)
          .expiration(expiryDate)
          .signWith(key)
          .compact();

      logger.info("🔑 Token da aplicação gerado com sucesso. Expira em: {}", expiryDate);
      return token;

    } catch (Exception e) {
      logger.error("❌ Erro ao gerar token da aplicação: {}", e.getMessage());
      throw new CrmException("Erro interno do servidor");
    }
  }

  /**
   * Valida se o token da aplicação é válido e não expirou
   */
  public boolean validateApplicationToken(String token) {
    try {
      if (token == null || token.trim().isEmpty()) {
        logger.warn("⚠️ Token da aplicação está vazio");
        return false;
      }

      SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

      Claims claims = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      // Verificar se é um token de aplicação
      String tokenType = claims.get("type", String.class);
      String purpose = claims.get("purpose", String.class);

      if (!"APPLICATION".equals(tokenType) || !"API_ACCESS".equals(purpose)) {
        logger.warn("⚠️ Token não é do tipo APPLICATION: type={}, purpose={}", tokenType, purpose);
        return false;
      }

      // Verificar se não expirou
      Date expiration = claims.getExpiration();
      if (expiration.before(new Date())) {
        logger.warn("⚠️ Token da aplicação expirado em: {}", expiration);
        return false;
      }

      logger.debug("✅ Token da aplicação válido. Expira em: {}", expiration);
      return true;

    } catch (Exception e) {
      logger.warn("⚠️ Token da aplicação inválido: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Extrai informações do token da aplicação
   */
  public Map<String, Object> getTokenInfo(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

      Claims claims = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      Map<String, Object> info = new HashMap<>();
      info.put("subject", claims.getSubject());
      info.put("type", claims.get("type"));
      info.put("purpose", claims.get("purpose"));
      info.put("issuedAt", claims.getIssuedAt());
      info.put("expiration", claims.getExpiration());

      return info;

    } catch (Exception e) {
      logger.error("❌ Erro ao extrair informações do token: {}", e.getMessage());
      return new HashMap<>();
    }
  }

  /**
   * Retorna o tempo restante em minutos para expiração do token
   */
  public long getMinutesToExpiration(String token) {
    try {
      Map<String, Object> info = getTokenInfo(token);
      Date expiration = (Date) info.get("expiration");

      if (expiration == null) {
        return 0;
      }

      long now = System.currentTimeMillis();
      long expirationTime = expiration.getTime();

      if (expirationTime <= now) {
        return 0; // Já expirou
      }

      return (expirationTime - now) / (60 * 1000); // Converter para minutos

    } catch (Exception e) {
      logger.error("❌ Erro ao calcular tempo de expiração: {}", e.getMessage());
      return 0;
    }
  }
}