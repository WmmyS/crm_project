package com.wesley.crm.config.security;

import com.wesley.crm.app.services.JwtService;
import com.wesley.crm.domain.entities.User;
import com.wesley.crm.infra.database.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      String jwt = getJwtFromRequest(request);
      String path = request.getRequestURI();
      logger.info("🔍 [JWT-FILTER] Processando: " + path + " - JWT: " + (jwt != null ? "[PRESENTE]" : "[AUSENTE]"));

      if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
        String username = jwtService.getUsernameFromToken(jwt);
        logger.info("✅ [JWT-FILTER] JWT válido - Username: " + username);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
          User user = userOptional.get();
          logger.info("User found: " + user.getUsername());

          // Atualizar último login
          user.setUltimoLogin(LocalDateTime.now());
          userRepository.save(user);

          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
              user.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
          logger.info("Authentication set for user: " + username);
        } else {
          logger.error("❌ [JWT-FILTER] Usuário não encontrado: " + username);
        }
      } else {
        logger.warn("❌ [JWT-FILTER] JWT inválido ou ausente para: " + path);
      }
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    // Não filtrar endpoints públicos (exceto /api/auth/me, /api/auth/refresh,
    // /api/auth/logout)
    if (path.startsWith("/api/auth/")) {
      return path.equals("/api/auth/login") ||
          path.startsWith("/debug/");
    }

    return path.startsWith("/actuator/") ||
        path.startsWith("/swagger-ui") ||
        path.startsWith("/v3/api-docs") ||
        path.equals("/");
  }
}