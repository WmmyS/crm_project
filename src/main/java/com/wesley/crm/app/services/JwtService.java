package com.wesley.crm.app.services;

import com.wesley.crm.domain.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

  @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
  private String jwtSecret;

  @Value("${jwt.expiration:86400000}") // 24 horas em milliseconds
  private long jwtExpirationMs;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateToken(Authentication authentication) {
    User userPrincipal = (User) authentication.getPrincipal();
    return generateTokenForUser(userPrincipal);
  }

  public String generateTokenForUser(User user) {
    Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);

    return Jwts.builder()
        .subject(user.getUsername())
        .claim("userId", user.getId())
        .claim("email", user.getEmail())
        .claim("role", user.getRole().name())
        .issuedAt(new Date())
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.getSubject();
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("userId", Long.class);
  }

  public String getRoleFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("role", String.class);
  }

  public LocalDateTime getExpirationDateFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return LocalDateTime.ofInstant(
        claims.getExpiration().toInstant(),
        ZoneId.systemDefault());
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (SecurityException ex) {
      System.err.println("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      System.err.println("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      System.err.println("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      System.err.println("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      System.err.println("JWT claims string is empty");
    }
    return false;
  }

  public boolean isTokenExpired(String token) {
    try {
      Claims claims = Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();

      return claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return true;
    }
  }

  public String refreshToken(String token) {
    try {
      Claims claims = Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();

      Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);

      return Jwts.builder()
          .subject(claims.getSubject())
          .claim("userId", claims.get("userId"))
          .claim("email", claims.get("email"))
          .claim("role", claims.get("role"))
          .issuedAt(new Date())
          .expiration(expiryDate)
          .signWith(getSigningKey())
          .compact();
    } catch (Exception e) {
      throw new RuntimeException("Cannot refresh token", e);
    }
  }
}