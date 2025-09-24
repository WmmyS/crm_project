package com.wesley.crm.app.controllers.auth;

import com.wesley.crm.app.models.dtos.auth.ApiKeyRequestDTO;
import com.wesley.crm.app.models.dtos.auth.ApiKeyResponseDTO;
import com.wesley.crm.app.models.dtos.auth.LoginRequestDTO;
import com.wesley.crm.app.models.dtos.auth.LoginResponseDTO;
import com.wesley.crm.app.models.dtos.auth.RegisterRequestDTO;
import com.wesley.crm.app.models.dtos.auth.RegisterResponseDTO;
import com.wesley.crm.app.models.dtos.auth.RotatingTokenResponseDTO;
import com.wesley.crm.app.services.ApiKeyService;
import com.wesley.crm.app.services.AuthService;
import com.wesley.crm.app.services.RotatingTokenService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de API Keys")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private ApiKeyService apiKeyService;

  @Autowired
  private RotatingTokenService rotatingTokenService;

  @PostMapping("/register")
  @Operation(summary = "📝 Cadastrar usuário", description = "🆓 **Endpoint PÚBLICO** - Cria novo usuário no sistema. Não requer autenticação.", security = {} // Remove
                                                                                                                                                               // a
                                                                                                                                                               // autenticação
                                                                                                                                                               // padrão
                                                                                                                                                               // para
                                                                                                                                                               // este
                                                                                                                                                               // endpoint
  )
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

  @PostMapping("/login")
  @Operation(summary = "🔐 Realizar login", description = "🆓 **Endpoint PÚBLICO** - Autentica usuário e retorna token JWT. Use o token retornado nos próximos endpoints.", security = {} // Remove
                                                                                                                                                                                          // a
                                                                                                                                                                                          // autenticação
                                                                                                                                                                                          // padrão
                                                                                                                                                                                          // para
                                                                                                                                                                                          // este
                                                                                                                                                                                          // endpoint
  )
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
  @Operation(summary = "🔄 Renovar token", description = "🔐 **Requer JWT Token** - Renova token JWT válido para estender a sessão.")
  @SecurityRequirement(name = "BearerAuth")
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
  @Operation(summary = "🚪 Realizar logout", description = "🔐 **Requer JWT Token** - Invalida token JWT atual e adiciona à blacklist.")
  @SecurityRequirement(name = "BearerAuth")
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
  @Operation(summary = "👤 Obter dados do usuário", description = "🔐 **Requer Autenticação** - Retorna informações do usuário logado. Use JWT Token ou API Key.")
  @SecurityRequirement(name = "BearerAuth")
  @SecurityRequirement(name = "ApiKeyAuth")
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

  // ===== API Key Management =====

  @PostMapping("/api-keys")
  @Operation(summary = "🔑 Criar API Key", description = "🔐 **Requer JWT Token** - Cria nova API Key para integração com sistemas externos. A chave criada poderá ser usada como alternativa ao JWT.")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "API Key criada com sucesso - Use a chave retornada no header X-API-Key"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
  })
  public ResponseEntity<ApiKeyResponseDTO> createApiKey(@Valid @RequestBody ApiKeyRequestDTO request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CrmException("Usuário não autenticado", HttpStatus.UNAUTHORIZED);
    }

    ApiKeyResponseDTO response = apiKeyService.createApiKey(request, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/api-keys")
  @Operation(summary = "📋 Listar API Keys", description = "🔐 **Requer JWT Token** - Lista todas as API Keys do usuário autenticado.")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "API Keys listadas com sucesso"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
  })
  public ResponseEntity<List<ApiKeyResponseDTO>> getUserApiKeys() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CrmException("Usuário não autenticado", HttpStatus.UNAUTHORIZED);
    }

    List<ApiKeyResponseDTO> apiKeys = apiKeyService.getUserApiKeys(authentication.getName());
    return ResponseEntity.ok(apiKeys);
  }

  @DeleteMapping("/api-keys/{id}")
  @Operation(summary = "❌ Revogar API Key", description = "🔐 **Requer JWT Token** - Revoga uma API Key específica. A chave será desativada imediatamente.")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "API Key revogada com sucesso"),
      @ApiResponse(responseCode = "404", description = "API Key não encontrada"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
      @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para revogar esta API Key")
  })
  public ResponseEntity<Map<String, String>> revokeApiKey(@PathVariable Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CrmException("Usuário não autenticado", HttpStatus.UNAUTHORIZED);
    }

    apiKeyService.revokeApiKey(id, authentication.getName());
    return ResponseEntity.ok(Map.of("message", "API Key revogada com sucesso"));
  }

  // ===== Rotating Token Management =====

  @PostMapping("/rotating-token/generate")
  @Operation(summary = "🔄 Gerar Token Rotativo", description = "🗝️ **Requer API Key** - Gera um token rotativo de 15 minutos para acesso seguro do frontend. Use no header X-Rotating-Token junto com X-API-Key.", security = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Token rotativo gerado com sucesso"),
      @ApiResponse(responseCode = "401", description = "API Key inválida"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
  })
  public ResponseEntity<RotatingTokenResponseDTO> generateRotatingToken(HttpServletRequest request) {
    String apiKey = request.getHeader("X-API-Key");
    if (apiKey == null || apiKey.trim().isEmpty()) {
      throw new CrmException("API Key obrigatória no header X-API-Key", HttpStatus.BAD_REQUEST);
    }

    try {
      String token = rotatingTokenService.generateRotatingToken(apiKey);
      var tokenInfo = rotatingTokenService.getActiveToken(apiKey);

      if (tokenInfo.isPresent()) {
        RotatingTokenResponseDTO response = new RotatingTokenResponseDTO(
            token,
            tokenInfo.get().getExpiresAt(),
            "success",
            "Token rotativo gerado com sucesso. Expira em 15 minutos.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
      } else {
        throw new CrmException("Erro ao gerar token", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (CrmException e) {
      throw e;
    } catch (Exception e) {
      throw new CrmException("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/rotating-token/renew")
  @Operation(summary = "🔄 Renovar Token Rotativo", description = "🗝️ **Requer API Key** - Renova ou estende o tempo de expiração do token rotativo atual.", security = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
      @ApiResponse(responseCode = "401", description = "API Key inválida")
  })
  public ResponseEntity<RotatingTokenResponseDTO> renewRotatingToken(HttpServletRequest request) {
    String apiKey = request.getHeader("X-API-Key");
    if (apiKey == null || apiKey.trim().isEmpty()) {
      throw new CrmException("API Key obrigatória no header X-API-Key", HttpStatus.BAD_REQUEST);
    }

    try {
      String token = rotatingTokenService.renewRotatingToken(apiKey);
      var tokenInfo = rotatingTokenService.getActiveToken(apiKey);

      if (tokenInfo.isPresent()) {
        RotatingTokenResponseDTO response = new RotatingTokenResponseDTO(
            token,
            tokenInfo.get().getExpiresAt(),
            "renewed",
            "Token rotativo renovado com sucesso.");
        return ResponseEntity.ok(response);
      } else {
        throw new CrmException("Erro ao renovar token", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (CrmException e) {
      throw e;
    } catch (Exception e) {
      throw new CrmException("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/rotating-token/status")
  @Operation(summary = "📊 Status do Token Rotativo", description = "🗝️ **Requer API Key** - Verifica o status do token rotativo atual (tempo restante, validade, etc.).", security = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status obtido com sucesso"),
      @ApiResponse(responseCode = "401", description = "API Key inválida"),
      @ApiResponse(responseCode = "404", description = "Nenhum token ativo encontrado")
  })
  public ResponseEntity<RotatingTokenResponseDTO> getRotatingTokenStatus(HttpServletRequest request) {
    String apiKey = request.getHeader("X-API-Key");
    if (apiKey == null || apiKey.trim().isEmpty()) {
      throw new CrmException("API Key obrigatória no header X-API-Key", HttpStatus.BAD_REQUEST);
    }

    try {
      var tokenInfo = rotatingTokenService.getActiveToken(apiKey);

      if (tokenInfo.isEmpty()) {
        RotatingTokenResponseDTO response = new RotatingTokenResponseDTO(
            null, null, "not_found", "Nenhum token ativo encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      var token = tokenInfo.get();
      String status = token.isValid() ? "active" : "expired";
      String message = token.isValid() ? "Token ativo e válido" : "Token expirado, gere um novo";

      RotatingTokenResponseDTO response = new RotatingTokenResponseDTO(
          token.getToken(),
          token.getExpiresAt(),
          status,
          message);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      throw new CrmException("Erro ao verificar status do token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // ===== Helper Methods =====

  private String extractTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    throw new CrmException("Token não encontrado na requisição", HttpStatus.BAD_REQUEST);
  }
}