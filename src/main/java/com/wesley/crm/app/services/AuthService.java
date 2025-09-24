package com.wesley.crm.app.services;

import com.wesley.crm.app.models.dtos.auth.LoginRequestDTO;
import com.wesley.crm.app.models.dtos.auth.LoginResponseDTO;
import com.wesley.crm.app.models.dtos.auth.RegisterRequestDTO;
import com.wesley.crm.app.models.dtos.auth.RegisterResponseDTO;
import com.wesley.crm.domain.entities.User;
import com.wesley.crm.exceptions.CrmException;
import com.wesley.crm.infra.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // Set para armazenar tokens invalidados (em um sistema real, use Redis ou cache
  // distribuído)
  private final Set<String> blacklistedTokens = new HashSet<>();

  public LoginResponseDTO login(LoginRequestDTO loginRequest) {
    try {
      // Autenticar usuário
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getLogin(),
              loginRequest.getPassword()));

      // Buscar usuário no banco
      User user = userRepository.findByUsernameOrEmail(loginRequest.getLogin())
          .orElseThrow(() -> new CrmException("Usuário não encontrado", HttpStatus.NOT_FOUND));

      // Atualizar último login
      user.setUltimoLogin(LocalDateTime.now());
      userRepository.save(user);

      // Gerar token JWT
      String token = jwtService.generateTokenForUser(user);

      return new LoginResponseDTO(
          token,
          user.getUsername(),
          user.getEmail(),
          user.getNome(),
          user.getRole().name(),
          jwtService.getExpirationDateFromToken(token));

    } catch (AuthenticationException e) {
      throw new CrmException("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
    }
  }

  public LoginResponseDTO refreshToken(String token) {
    if (blacklistedTokens.contains(token)) {
      throw new CrmException("Token foi invalidado", HttpStatus.UNAUTHORIZED);
    }

    if (!jwtService.validateToken(token)) {
      throw new CrmException("Token inválido ou expirado", HttpStatus.UNAUTHORIZED);
    }

    String username = jwtService.getUsernameFromToken(token);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CrmException("Usuário não encontrado", HttpStatus.NOT_FOUND));

    // Invalidar token antigo
    blacklistedTokens.add(token);

    // Gerar novo token
    String newToken = jwtService.generateTokenForUser(user);

    return new LoginResponseDTO(
        newToken,
        user.getUsername(),
        user.getEmail(),
        user.getNome(),
        user.getRole().name(),
        jwtService.getExpirationDateFromToken(newToken));
  }

  public void logout(String token) {
    if (token != null && jwtService.validateToken(token)) {
      blacklistedTokens.add(token);
    }
  }

  public boolean isTokenBlacklisted(String token) {
    return blacklistedTokens.contains(token);
  }

  public Map<String, Object> getCurrentUserInfo(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CrmException("Usuário não encontrado", HttpStatus.NOT_FOUND));

    Map<String, Object> userInfo = new HashMap<>();
    userInfo.put("id", user.getId());
    userInfo.put("username", user.getUsername());
    userInfo.put("email", user.getEmail());
    userInfo.put("role", user.getRole().name());
    userInfo.put("isActive", user.getAtivo());
    userInfo.put("dataCriacao", user.getDataCriacao());
    userInfo.put("ultimoLogin", user.getUltimoLogin());

    return userInfo;
  }

  public RegisterResponseDTO registerUser(RegisterRequestDTO registerRequest) {
    // Verificar se username já existe
    if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
      throw new CrmException("Username já está em uso", HttpStatus.CONFLICT);
    }

    // Verificar se email já existe
    if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
      throw new CrmException("Email já está em uso", HttpStatus.CONFLICT);
    }

    // Criar novo usuário
    User newUser = new User();
    newUser.setUsername(registerRequest.getUsername());
    newUser.setEmail(registerRequest.getEmail());
    newUser.setNome(registerRequest.getNome());
    newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    newUser.setRole(User.Role.USER); // Por padrão, novos usuários têm role USER
    newUser.setAtivo(true);

    // Salvar no banco
    User savedUser = userRepository.save(newUser);

    return new RegisterResponseDTO(
        savedUser.getId(),
        savedUser.getUsername(),
        savedUser.getEmail(),
        savedUser.getNome(),
        savedUser.getRole().name(),
        "Usuário cadastrado com sucesso");
  }
}