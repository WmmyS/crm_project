package com.wesley.crm.config.security;

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

  @Autowired
  private ApiKeyRepository apiKeyRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // Se já está autenticado (por JWT), pule a validação de API Key
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }

    String apiKey = extractApiKey(request);
    logger.info("API Key extracted: " + (apiKey != null ? "present" : "null"));

    if (apiKey != null) {
      Optional<ApiKey> apiKeyEntity = apiKeyRepository.findActiveByKey(apiKey);
      logger.info("API Key found in DB: " + apiKeyEntity.isPresent());

      if (apiKeyEntity.isPresent() && apiKeyEntity.get().isValid()) {
        ApiKey validApiKey = apiKeyEntity.get();
        logger.info("API Key is valid for user: " + validApiKey.getUser().getUsername());

        // Incrementa contador de uso
        validApiKey.incrementarUso();
        apiKeyRepository.save(validApiKey);

        // Criar autenticação baseada no usuário da API Key
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            validApiKey.getUser(),
            null,
            validApiKey.getUser().getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Authentication set for API user: " + validApiKey.getUser().getUsername());
      }
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

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    // Não filtrar apenas endpoints públicos (login)
    return path.equals("/api/auth/login") ||
        path.startsWith("/debug/") ||
        path.startsWith("/actuator/") ||
        path.startsWith("/swagger-ui") ||
        path.startsWith("/v3/api-docs") ||
        path.equals("/");
  }
}