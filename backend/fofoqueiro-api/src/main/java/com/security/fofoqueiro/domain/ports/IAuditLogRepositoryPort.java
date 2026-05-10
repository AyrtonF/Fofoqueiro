package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.AuditLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAuditLogRepositoryPort {
    Optional<AuditLog> findById(Long id);
    AuditLog save(AuditLog auditLog);
    List<AuditLog> findByTenantId(Long tenantId);
    List<AuditLog> findByTenantIdAndUserId(Long tenantId, Long userId);
    List<AuditLog> findByTenantIdAndEntity(Long tenantId, String entityName, Long entityId);
    List<AuditLog> findByTenantIdAndTimestampBetween(Long tenantId, LocalDateTime start, LocalDateTime end);
}
