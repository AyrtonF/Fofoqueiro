package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.models.Role;
import com.security.fofoqueiro.infrastructure.entities.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RoleEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }
        return RoleEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
