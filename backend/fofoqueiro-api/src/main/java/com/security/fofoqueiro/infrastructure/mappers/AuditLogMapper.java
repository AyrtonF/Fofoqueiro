package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.dtos.AuditLogCreateDTO;
import com.security.fofoqueiro.domain.dtos.AuditLogResponseDTO;
import com.security.fofoqueiro.domain.models.AuditLog;
import com.security.fofoqueiro.infrastructure.entities.AuditLogEntity;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLog toDomain(AuditLogEntity entity) {
        if (entity == null) {
            return null;
        }
        return AuditLog.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .userId(entity.getUserId())
                .action(entity.getAction())
                .entityName(entity.getEntityName())
                .entityId(entity.getEntityId())
                .timestamp(entity.getTimestamp())
                .ipAddress(entity.getIpAddress())
                .details(entity.getDetails())
                .hash(entity.getHash())
                .build();
    }

    public AuditLogEntity toEntity(AuditLog domain) {
        if (domain == null) {
            return null;
        }
        return AuditLogEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .userId(domain.getUserId())
                .action(domain.getAction())
                .entityName(domain.getEntityName())
                .entityId(domain.getEntityId())
                .timestamp(domain.getTimestamp())
                .ipAddress(domain.getIpAddress())
                .details(domain.getDetails())
                .hash(domain.getHash())
                .build();
    }

    public AuditLog toDomain(AuditLogCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        return AuditLog.builder()
                .tenantId(dto.getTenantId())
                .userId(dto.getUserId())
                .action(dto.getAction())
                .entityName(dto.getEntityName())
                .entityId(dto.getEntityId())
                .ipAddress(dto.getIpAddress())
                .details(dto.getDetails())
                .hash(dto.getHash())
                .build();
    }

    public AuditLogResponseDTO toResponseDTO(AuditLog domain) {
        if (domain == null) {
            return null;
        }
        return AuditLogResponseDTO.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .userId(domain.getUserId())
                .action(domain.getAction())
                .entityName(domain.getEntityName())
            .entityId(domain.getEntityId() == null ? null : String.valueOf(domain.getEntityId()))
                .timestamp(domain.getTimestamp())
                .ipAddress(domain.getIpAddress())
                .details(domain.getDetails())
                .hash(domain.getHash())
                .build();
    }
}
