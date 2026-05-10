package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.dtos.TenantCreateDTO;
import com.security.fofoqueiro.domain.dtos.TenantResponseDTO;
import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.infrastructure.entities.TenantEntity;
import org.springframework.stereotype.Component;

@Component
public class TenantMapper {

    public Tenant toDomain(TenantEntity entity) {
        if (entity == null) {
            return null;
        }
        return Tenant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .domain(entity.getDomain())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public TenantEntity toEntity(Tenant domain) {
        if (domain == null) {
            return null;
        }
        return TenantEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .domain(domain.getDomain())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public Tenant toDomain(TenantCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        return Tenant.builder()
                .name(dto.getName())
                .domain(dto.getDomain())
                .build();
    }

    public TenantResponseDTO toResponseDTO(Tenant domain) {
        if (domain == null) {
            return null;
        }
        return TenantResponseDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .domain(domain.getDomain())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
