package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataUserSessionRepository extends JpaRepository<UserSessionEntity, Long> {
    Optional<UserSessionEntity> findByTokenId(String tokenId);
    List<UserSessionEntity> findByTenantId(Long tenantId);
    List<UserSessionEntity> findByTenantIdAndUserId(Long tenantId, Long userId);
}
