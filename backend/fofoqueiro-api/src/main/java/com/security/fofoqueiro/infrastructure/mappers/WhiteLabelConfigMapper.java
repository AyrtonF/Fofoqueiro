package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.dtos.WhiteLabelConfigResponseDTO;
import com.security.fofoqueiro.domain.models.WhiteLabelConfig;
import com.security.fofoqueiro.infrastructure.entities.WhiteLabelConfigEntity;
import org.springframework.stereotype.Component;

@Component
public class WhiteLabelConfigMapper {

    public WhiteLabelConfig toDomain(WhiteLabelConfigEntity entity) {
        if (entity == null) {
            return null;
        }
        return WhiteLabelConfig.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .logoUrl(entity.getLogoUrl())
                .primaryColor(entity.getPrimaryColor())
                .secondaryColor(entity.getSecondaryColor())
                .faviconUrl(entity.getFaviconUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public WhiteLabelConfigEntity toEntity(WhiteLabelConfig domain) {
        if (domain == null) {
            return null;
        }
        return WhiteLabelConfigEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .logoUrl(domain.getLogoUrl())
                .primaryColor(domain.getPrimaryColor())
                .secondaryColor(domain.getSecondaryColor())
                .faviconUrl(domain.getFaviconUrl())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public WhiteLabelConfigResponseDTO toResponseDTO(WhiteLabelConfig domain) {
        if (domain == null) {
            return null;
        }
        return WhiteLabelConfigResponseDTO.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .logoUrl(domain.getLogoUrl())
                .primaryColor(domain.getPrimaryColor())
                .secondaryColor(domain.getSecondaryColor())
                .faviconUrl(domain.getFaviconUrl())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
