package com.wesley.crm.config.security;

import com.wesley.crm.app.services.ApplicationTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationTokenAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationTokenAuthenticationFilter.class);

  @Autowired
  private ApplicationTokenService applicationTokenService;

  // Endpoints que não precisam de token de aplicação
  private final List<String> publicEndpoints = Arrays.asList(
      "/api/auth/app-login",
      "/api/auth/login",
      "/api/auth/register",
      "/debug",
      "/actuator",
      "/swagger-ui",
      "/swagger-ui.html",
      "/v3/api-docs",
      "/");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String path = request.getRequestURI();
    String method = request.getMethod();

    logger.debug("🔍 Verificando token da aplicação para: {} {}", method, path);

    // Verificar se é um endpoint público
    if (isPublicEndpoint(path)) {
      logger.debug("✅ Endpoint público detectado: {}", path);
      filterChain.doFilter(request, response);
      return;
    }

    // Verificar se é uma requisição para /api/**
    if (!path.startsWith("/api/")) {
      logger.debug("✅ Não é endpoint da API: {}", path);
      filterChain.doFilter(request, response);
      return;
    }

    // Extrair token da aplicação do header X-App-Token
    String appToken = request.getHeader("X-App-Token");

    if (appToken == null || appToken.trim().isEmpty()) {
      logger.warn("❌ Token da aplicação não fornecido para: {}", path);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write(
          "{\"error\":\"Token da aplicação é obrigatório\",\"message\":\"Faça login da aplicação primeiro através de /api/auth/app-login\"}");
      return;
    }

    // Remover prefixo "Bearer " se existir
    if (appToken.startsWith("Bearer ")) {
      appToken = appToken.substring(7);
    }

    // Validar token da aplicação
    if (!applicationTokenService.validateApplicationToken(appToken)) {
      logger.warn("❌ Token da aplicação inválido ou expirado para: {}", path);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write(
          "{\"error\":\"Token da aplicação inválido ou expirado\",\"message\":\"Faça login da aplicação novamente através de /api/auth/app-login\"}");
      return;
    }

    logger.debug("✅ Token da aplicação válido para: {}", path);

    // Se chegou até aqui, o token da aplicação é válido
    // Continuar com os próximos filtros (JWT, etc.)
    filterChain.doFilter(request, response);
  }

  private boolean isPublicEndpoint(String path) {
    return publicEndpoints.stream().anyMatch(endpoint -> path.equals(endpoint) || path.startsWith(endpoint + "/"));
  }
}