package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.dtos.GroupLocationCreateDTO;
import com.security.fofoqueiro.domain.dtos.GroupLocationResponseDTO;
import com.security.fofoqueiro.domain.models.GroupLocation;
import com.security.fofoqueiro.infrastructure.entities.GroupLocationEntity;
import org.springframework.stereotype.Component;

@Component
public class GroupLocationMapper {
    public GroupLocation toDomain(GroupLocationEntity entity) {
        if (entity == null) {
            return null;
        }
        return GroupLocation.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .parentGroupId(entity.getParentGroupId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public GroupLocationEntity toEntity(GroupLocation domain) {
        if (domain == null) {
            return null;
        }
        return GroupLocationEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .name(domain.getName())
                .parentGroupId(domain.getParentGroupId())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public GroupLocation toDomain(GroupLocationCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        return GroupLocation.builder()
                .tenantId(dto.getTenantId())
                .name(dto.getName())
                .parentGroupId(dto.getParentGroupId())
                .build();
    }

    public GroupLocationResponseDTO toResponseDTO(GroupLocation domain) {
        if (domain == null) {
            return null;
        }
        return GroupLocationResponseDTO.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .name(domain.getName())
                .parentGroupId(domain.getParentGroupId())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
