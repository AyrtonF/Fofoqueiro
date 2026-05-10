package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.models.User;
import com.security.fofoqueiro.infrastructure.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .isActive(entity.getIsActive())
                .mfaEnabled(entity.getMfaEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        return UserEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .isActive(domain.getIsActive())
                .mfaEnabled(domain.getMfaEnabled())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
