package com.wesley.crm.config.security;

import com.wesley.crm.app.services.RotatingTokenService;
import com.wesley.crm.domain.entities.ApiKey;
import com.wesley.crm.infra.database.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.Optional;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);

  public static final String API_KEY_HEADER = "X-API-Key";
  public static final String API_KEY_PARAM = "apiKey";
  public static final String ROTATING_TOKEN_HEADER = "X-Rotating-Token";

  @Autowired
  private ApiKeyRepository apiKeyRepository;

  @Autowired
  private RotatingTokenService rotatingTokenService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // Para endpoints protegidos, SEMPRE validar API Key + Rotating Token
    // independente de ter JWT ou não (segurança tripla obrigatória)

    // Se já tem autenticação JWT, apenas validar se API Key + Rotating Token estão
    // corretos
    boolean hasJwtAuth = SecurityContextHolder.getContext().getAuthentication() != null;

    String apiKey = extractApiKey(request);
    String rotatingToken = extractRotatingToken(request);

    logger.info("JWT Auth present: " + hasJwtAuth);
    logger.info("API Key extracted: " + (apiKey != null ? "present" : "null"));
    logger.info("Rotating Token extracted: " + (rotatingToken != null ? "present" : "null"));

    // Para endpoints protegidos, tanto API Key quanto Rotating Token são
    // obrigatórios
    if (apiKey != null && rotatingToken != null) {
      Optional<ApiKey> apiKeyEntity = apiKeyRepository.findActiveByKey(apiKey);
      logger.info("API Key found in DB: " + apiKeyEntity.isPresent());

      if (apiKeyEntity.isPresent() && apiKeyEntity.get().isValid()) {
        ApiKey validApiKey = apiKeyEntity.get();

        // Validar o token rotativo associado à API Key
        boolean isValidRotatingToken = rotatingTokenService.validateRotatingToken(apiKey, rotatingToken);
        logger.info("Rotating Token validation result: " + isValidRotatingToken);

        if (isValidRotatingToken) {
          logger.info("API Key and Rotating Token are valid for API Key user: " + validApiKey.getUser().getUsername());

          // Incrementa contador de uso da API Key
          validApiKey.incrementarUso();
          apiKeyRepository.save(validApiKey);

          // Se não tem JWT auth, criar autenticação baseada no usuário da API Key
          if (!hasJwtAuth) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                validApiKey.getUser(),
                null,
                validApiKey.getUser().getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Authentication set for API user: " + validApiKey.getUser().getUsername());
          } else {
            logger.info("JWT authentication already present, API Key validation successful");
          }
        } else {
          logger.warn("Invalid rotating token for API Key: " + apiKey);
          // Para segurança, limpar qualquer autenticação existente se API Key/Token
          // inválidos
          SecurityContextHolder.getContext().setAuthentication(null);
        }
      } else {
        logger.warn("Invalid or inactive API Key: " + apiKey);
        // Limpar autenticação se API Key inválida
        SecurityContextHolder.getContext().setAuthentication(null);
      }
    } else {
      if (apiKey == null) {
        logger.warn("Missing API Key for protected endpoint: " + request.getRequestURI());
      }
      if (rotatingToken == null) {
        logger.warn("Missing Rotating Token for protected endpoint: " + request.getRequestURI());
      }
      // Limpar autenticação se faltam API Key ou Rotating Token
      SecurityContextHolder.getContext().setAuthentication(null);
    }

    filterChain.doFilter(request, response);
  }

  private String extractApiKey(HttpServletRequest request) {
    // Primeiro tenta pegar do header
    String apiKey = request.getHeader(API_KEY_HEADER);

    // Se não encontrou no header, tenta no parâmetro de query
    if (apiKey == null || apiKey.trim().isEmpty()) {
      apiKey = request.getParameter(API_KEY_PARAM);
    }

    return apiKey != null && !apiKey.trim().isEmpty() ? apiKey.trim() : null;
  }

  private String extractRotatingToken(HttpServletRequest request) {
    String rotatingToken = request.getHeader(ROTATING_TOKEN_HEADER);
    return rotatingToken != null && !rotatingToken.trim().isEmpty() ? rotatingToken.trim() : null;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    // Não filtrar endpoints públicos e de autenticação
    return path.equals("/api/auth/login") ||
        path.equals("/api/auth/register") ||
        path.startsWith("/api/auth/rotating-token") ||
        path.startsWith("/debug/") ||
        path.startsWith("/actuator/") ||
        path.startsWith("/swagger-ui") ||
        path.startsWith("/v3/api-docs") ||
        path.equals("/");
  }
}