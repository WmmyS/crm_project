package com.wesley.crm.app.controllers.auth;

import com.wesley.crm.app.models.dtos.auth.ApiKeyRequestDTO;
import com.wesley.crm.app.models.dtos.auth.ApiKeyResponseDTO;
import com.wesley.crm.app.models.dtos.auth.LoginRequestDTO;
import com.wesley.crm.app.models.dtos.auth.LoginResponseDTO;
import com.wesley.crm.app.models.dtos.auth.RegisterRequestDTO;
import com.wesley.crm.app.models.dtos.auth.RegisterResponseDTO;
import com.wesley.crm.app.services.ApiKeyService;
import com.wesley.crm.app.services.AuthService;
import com.wesley.crm.exceptions.CrmException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @PostMapping("/register")
  @Operation(summary = "Cadastrar usuário", description = "Cria novo usuário no sistema")
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
  @Operation(summary = "Realizar login", description = "Autentica usuário e retorna token JWT")
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
  @Operation(summary = "Renovar token", description = "Renova token JWT válido")
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
  @Operation(summary = "Realizar logout", description = "Invalida token JWT atual")
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
  @Operation(summary = "Obter dados do usuário", description = "Retorna informações do usuário logado")
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
  @Operation(summary = "Criar API Key", description = "Cria nova API Key para o usuário autenticado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "API Key criada com sucesso"),
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
  @Operation(summary = "Listar API Keys", description = "Lista todas as API Keys do usuário autenticado")
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
  @Operation(summary = "Revogar API Key", description = "Revoga uma API Key específica")
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

  // ===== Helper Methods =====

  private String extractTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    throw new CrmException("Token não encontrado na requisição", HttpStatus.BAD_REQUEST);
  }
}