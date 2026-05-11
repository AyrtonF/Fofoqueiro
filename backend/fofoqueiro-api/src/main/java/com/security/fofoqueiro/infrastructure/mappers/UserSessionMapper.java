package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.dtos.UserSessionCreateDTO;
import com.security.fofoqueiro.domain.dtos.UserSessionResponseDTO;
import com.security.fofoqueiro.domain.models.UserSession;
import com.security.fofoqueiro.infrastructure.entities.UserSessionEntity;
import org.springframework.stereotype.Component;

@Component
public class UserSessionMapper {
    public UserSession toDomain(UserSessionEntity entity) {
        if (entity == null) return null;
        return UserSession.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .userId(entity.getUserId())
                .tokenId(entity.getTokenId())
                .expiresAt(entity.getExpiresAt())
                .lastActivityAt(entity.getLastActivityAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public UserSessionEntity toEntity(UserSession domain) {
        if (domain == null) return null;
        return UserSessionEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .userId(domain.getUserId())
                .tokenId(domain.getTokenId())
                .expiresAt(domain.getExpiresAt())
                .lastActivityAt(domain.getLastActivityAt())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public UserSession toDomain(UserSessionCreateDTO dto) {
        if (dto == null) return null;
        return UserSession.builder()
                .tenantId(dto.getTenantId())
                .userId(dto.getUserId())
                .tokenId(dto.getTokenId())
                .expiresAt(dto.getExpiresAt())
                .lastActivityAt(dto.getLastActivityAt())
                .build();
    }

    public UserSessionResponseDTO toResponseDTO(UserSession domain) {
        if (domain == null) return null;
        return UserSessionResponseDTO.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .userId(domain.getUserId())
                .tokenId(domain.getTokenId())
                .expiresAt(domain.getExpiresAt())
                .lastActivityAt(domain.getLastActivityAt())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
