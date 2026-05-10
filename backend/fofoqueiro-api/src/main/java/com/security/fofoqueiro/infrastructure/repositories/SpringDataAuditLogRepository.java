package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataAuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
    List<AuditLogEntity> findByTenantId(Long tenantId);
    List<AuditLogEntity> findByTenantIdAndUserId(Long tenantId, Long userId);
    List<AuditLogEntity> findByTenantIdAndEntityNameAndEntityId(Long tenantId, String entityName, Long entityId);
    List<AuditLogEntity> findByTenantIdAndTimestampBetween(Long tenantId, LocalDateTime start, LocalDateTime end);
    Optional<AuditLogEntity> findByHash(String hash);
}
