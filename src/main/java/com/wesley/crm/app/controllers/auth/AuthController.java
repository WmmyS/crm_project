package com.wesley.crm.app.controllers.auth;

import com.wesley.crm.app.models.dtos.auth.AppLoginRequestDTO;
import com.wesley.crm.app.models.dtos.auth.AppLoginResponseDTO;
import com.wesley.crm.app.models.dtos.auth.LoginRequestDTO;
import com.wesley.crm.app.models.dtos.auth.LoginResponseDTO;
import com.wesley.crm.app.models.dtos.auth.RegisterRequestDTO;
import com.wesley.crm.app.models.dtos.auth.RegisterResponseDTO;

import com.wesley.crm.app.services.ApplicationTokenService;
import com.wesley.crm.app.services.AuthService;
import com.wesley.crm.config.AppAuthProperties;

import com.wesley.crm.exceptions.CrmException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários e aplicações")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private ApplicationTokenService applicationTokenService;

  @Autowired
  private AppAuthProperties appAuthProperties;

  @PostMapping("/register")
  @Operation(summary = "📝 Cadastrar usuário", description = "🆓 **Endpoint PÚBLICO** - Cria novo usuário no sistema. Não requer autenticação.", security = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
      @ApiResponse(responseCode = "409", description = "Username ou email já estão em uso"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
  })
  public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
    try {
      RegisterResponseDTO response = authService.registerUser(registerRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (CrmException e) {
      throw e;
    } catch (Exception e) {
      throw new CrmException("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/app-login")
  @Operation(summary = "🔐 Login da Aplicação", description = "🆓 **Endpoint PÚBLICO** - Autentica aplicação e retorna Application Token.", security = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Login da aplicação realizado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Credenciais da aplicação inválidas"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
  })
  public ResponseEntity<AppLoginResponseDTO> appLogin(@Valid @RequestBody AppLoginRequestDTO appLoginRequest) {
    try {
      // Validação das credenciais da aplicação (configurável via properties)
      if (!appAuthProperties.getUsername().equals(appLoginRequest.getUsername()) ||
          !appAuthProperties.getPassword().equals(appLoginRequest.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new AppLoginResponseDTO(null, "error", 0, null));
      }

      // Gerar token da aplicação
      String token = applicationTokenService.generateApplicationToken();

      // Criar resposta

      AppLoginResponseDTO response = new AppLoginResponseDTO(
          token,
          "Bearer",
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
          15,
          appLoginRequest.getUsername());

      return ResponseEntity.ok(response);
    } catch (CrmException e) {
      throw e;
    } catch (Exception e) {
      throw new CrmException("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/login")
  @Operation(summary = "🔐 Realizar login", description = "🆓 **Endpoint PÚBLICO** - Autentica usuário e retorna token JWT.", security = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
  })
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
    try {
      LoginResponseDTO response = authService.login(loginRequest);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      throw new CrmException("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/refresh")
  @Operation(summary = "🔄 Renovar token", description = "🔐 **Requer JWT Token** - Renova token JWT válido.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
  })
  public ResponseEntity<LoginResponseDTO> refreshToken(HttpServletRequest request) {
    try {
      String token = extractTokenFromRequest(request);
      LoginResponseDTO response = authService.refreshToken(token);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      throw new CrmException("Token inválido", HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/logout")
  @Operation(summary = "🚪 Realizar logout", description = "🔐 **Requer JWT Token** - Invalida token JWT atual.")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Token inválido")
  })
  public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
    try {
      String token = extractTokenFromRequest(request);
      authService.logout(token);
      return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    } catch (Exception e) {
      throw new CrmException("Erro ao realizar logout", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/me")
  @Operation(summary = "👤 Obter dados do usuário", description = "🔐 **Requer Autenticação Tripla** - JWT + API Key + Application Token.")
  @SecurityRequirement(name = "bearerAuth")
  @SecurityRequirement(name = "apiKey")
  @SecurityRequirement(name = "applicationToken")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Dados do usuário retornados com sucesso"),
      @ApiResponse(responseCode = "401", description = "Token inválido")
  })
  public ResponseEntity<Map<String, Object>> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CrmException("Usuário não autenticado", HttpStatus.UNAUTHORIZED);
    }

    Map<String, Object> userInfo = authService.getCurrentUserInfo(authentication.getName());
    return ResponseEntity.ok(userInfo);
  }

  // ===== Sistema anterior removido =====
  // Os endpoints de API Key foram substituídos pelo sistema de Application Token
  // Use /api/auth/app-login para obter tokens de aplicação

  // ===== Sistema anterior removido =====
  // Os endpoints de Rotating Token foram substituídos pelo sistema de Application
  // Token
  // Use /api/auth/app-login para obter tokens de aplicação

  // ===== Helper Methods =====

  private String extractTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    throw new CrmException("Token não encontrado na requisição", HttpStatus.BAD_REQUEST);
  }
}