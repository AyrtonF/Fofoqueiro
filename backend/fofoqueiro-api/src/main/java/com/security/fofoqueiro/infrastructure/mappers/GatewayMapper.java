package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.models.Gateway;
import com.security.fofoqueiro.infrastructure.entities.GatewayEntity;
import org.springframework.stereotype.Component;

@Component
public class GatewayMapper {

    public Gateway toDomain(GatewayEntity entity) {
        if (entity == null) {
            return null;
        }
        return Gateway.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .ipAddress(entity.getIpAddress())
                .location(entity.getLocation())
                .status(entity.getStatus())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public GatewayEntity toEntity(Gateway domain) {
        if (domain == null) {
            return null;
        }
        return GatewayEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .name(domain.getName())
                .ipAddress(domain.getIpAddress())
                .location(domain.getLocation())
                .status(domain.getStatus())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
