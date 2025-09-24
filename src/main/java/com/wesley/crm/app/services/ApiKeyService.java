package com.wesley.crm.app.services;

import com.wesley.crm.app.models.dtos.auth.ApiKeyRequestDTO;
import com.wesley.crm.app.models.dtos.auth.ApiKeyResponseDTO;
import com.wesley.crm.domain.entities.ApiKey;
import com.wesley.crm.domain.entities.User;
import com.wesley.crm.exceptions.CrmException;
import com.wesley.crm.infra.database.ApiKeyRepository;
import com.wesley.crm.infra.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiKeyService {

  @Autowired
  private ApiKeyRepository apiKeyRepository;

  @Autowired
  private UserRepository userRepository;

  public ApiKeyResponseDTO createApiKey(ApiKeyRequestDTO requestDTO, String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CrmException("Usuário não encontrado"));

    // Verificar limite de API Keys por usuário (opcional)
    Long activeKeysCount = apiKeyRepository.countActiveByUser(user);
    if (activeKeysCount >= 10) { // limite de 10 chaves ativas por usuário
      throw new CrmException("Limite de API Keys atingido (máximo 10 por usuário)");
    }

    String generatedKey = generateApiKey();

    ApiKey apiKey = new ApiKey(generatedKey, requestDTO.getName(), requestDTO.getDescription(), user);
    apiKey.setDataExpiracao(requestDTO.getDataExpiracao());
    apiKey.setLimiteUso(requestDTO.getLimiteUso());

    ApiKey savedApiKey = apiKeyRepository.save(apiKey);
    return convertToDTO(savedApiKey);
  }

  public List<ApiKeyResponseDTO> getUserApiKeys(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CrmException("Usuário não encontrado"));

    List<ApiKey> apiKeys = apiKeyRepository.findByUser(user);
    return apiKeys.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public void revokeApiKey(Long apiKeyId, String username) {
    ApiKey apiKey = apiKeyRepository.findById(apiKeyId)
        .orElseThrow(() -> new CrmException("API Key não encontrada"));

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CrmException("Usuário não encontrado"));

    if (!apiKey.getUser().getId().equals(user.getId())) {
      throw new CrmException("Você não tem permissão para revogar esta API Key");
    }

    apiKey.setIsActive(false);
    apiKeyRepository.save(apiKey);
  }

  public boolean validateApiKey(String key) {
    return apiKeyRepository.findActiveByKey(key)
        .map(ApiKey::isValid)
        .orElse(false);
  }

  private String generateApiKey() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[32]; // 256 bits
    random.nextBytes(bytes);
    return "crm_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private ApiKeyResponseDTO convertToDTO(ApiKey apiKey) {
    return new ApiKeyResponseDTO(
        apiKey.getId(),
        apiKey.getKey(),
        apiKey.getName(),
        apiKey.getDescription(),
        apiKey.getIsActive(),
        apiKey.getDataCriacao(),
        apiKey.getDataExpiracao(),
        apiKey.getUltimoUso(),
        apiKey.getContadorUso(),
        apiKey.getLimiteUso());
  }
}