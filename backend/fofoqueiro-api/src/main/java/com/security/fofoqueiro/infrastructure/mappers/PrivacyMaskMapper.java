package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.models.PrivacyMask;
import com.security.fofoqueiro.infrastructure.entities.PrivacyMaskEntity;
import org.springframework.stereotype.Component;

@Component
public class PrivacyMaskMapper {

    public PrivacyMask toDomain(PrivacyMaskEntity entity) {
        if (entity == null) {
            return null;
        }
        return PrivacyMask.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .cameraId(entity.getCameraId())
                .name(entity.getName())
                .coordinates(entity.getCoordinates())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public PrivacyMaskEntity toEntity(PrivacyMask domain) {
        if (domain == null) {
            return null;
        }
        return PrivacyMaskEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .cameraId(domain.getCameraId())
                .name(domain.getName())
                .coordinates(domain.getCoordinates())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
