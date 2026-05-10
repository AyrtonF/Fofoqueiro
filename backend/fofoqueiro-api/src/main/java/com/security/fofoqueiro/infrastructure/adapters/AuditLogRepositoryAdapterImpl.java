package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.AuditLog;
import com.security.fofoqueiro.domain.ports.IAuditLogRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.AuditLogEntity;
import com.security.fofoqueiro.infrastructure.mappers.AuditLogMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuditLogRepositoryAdapterImpl implements IAuditLogRepositoryPort {

    private final SpringDataAuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public Optional<AuditLog> findById(Long id) {
        return auditLogRepository.findById(id)
                .map(auditLogMapper::toDomain);
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        AuditLogEntity auditLogEntity = auditLogMapper.toEntity(auditLog);
        AuditLogEntity savedEntity = auditLogRepository.save(auditLogEntity);
        return auditLogMapper.toDomain(savedEntity);
    }

    @Override
    public List<AuditLog> findByTenantId(Long tenantId) {
        return auditLogRepository.findByTenantId(tenantId)
                .stream()
                .map(auditLogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByTenantIdAndUserId(Long tenantId, Long userId) {
        return auditLogRepository.findByTenantIdAndUserId(tenantId, userId)
                .stream()
                .map(auditLogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByTenantIdAndEntity(Long tenantId, String entityName, Long entityId) {
        return auditLogRepository.findByTenantIdAndEntityNameAndEntityId(tenantId, entityName, entityId)
                .stream()
                .map(auditLogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByTenantIdAndTimestampBetween(Long tenantId, LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTenantIdAndTimestampBetween(tenantId, start, end)
                .stream()
                .map(auditLogMapper::toDomain)
                .collect(Collectors.toList());
    }
}
