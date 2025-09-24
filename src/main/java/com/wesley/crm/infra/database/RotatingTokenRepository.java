package com.wesley.crm.infra.database;

import com.wesley.crm.domain.entities.ApiKey;
import com.wesley.crm.domain.entities.RotatingToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RotatingTokenRepository extends JpaRepository<RotatingToken, Long> {

  Optional<RotatingToken> findByTokenAndIsActiveTrue(String token);

  Optional<RotatingToken> findFirstByApiKeyAndIsActiveTrueOrderByCreatedAtDesc(ApiKey apiKey);

  List<RotatingToken> findByApiKeyAndIsActiveTrue(ApiKey apiKey);

  @Query("SELECT rt FROM RotatingToken rt WHERE rt.expiresAt < :now")
  List<RotatingToken> findExpiredTokens(@Param("now") LocalDateTime now);

  @Modifying
  @Query("UPDATE RotatingToken rt SET rt.isActive = false WHERE rt.expiresAt < :now")
  int deactivateExpiredTokens(@Param("now") LocalDateTime now);

  @Modifying
  @Query("UPDATE RotatingToken rt SET rt.isActive = false WHERE rt.apiKey = :apiKey")
  int deactivateTokensByApiKey(@Param("apiKey") ApiKey apiKey);

  long countByApiKeyAndIsActiveTrue(ApiKey apiKey);
}