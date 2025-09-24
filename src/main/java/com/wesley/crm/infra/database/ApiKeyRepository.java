package com.wesley.crm.infra.database;

import com.wesley.crm.domain.entities.ApiKey;
import com.wesley.crm.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

  Optional<ApiKey> findByKey(String key);

  List<ApiKey> findByUser(User user);

  List<ApiKey> findByIsActiveTrue();

  @Query("SELECT a FROM ApiKey a WHERE a.key = :key AND a.isActive = true")
  Optional<ApiKey> findActiveByKey(@Param("key") String key);

  @Query("SELECT COUNT(a) FROM ApiKey a WHERE a.user = :user AND a.isActive = true")
  Long countActiveByUser(@Param("user") User user);
}