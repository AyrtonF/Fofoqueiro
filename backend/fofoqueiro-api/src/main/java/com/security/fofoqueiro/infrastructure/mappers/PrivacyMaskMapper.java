package com.security.fofoqueiro.infrastructure.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.fofoqueiro.domain.dtos.PrivacyMaskPointDTO;
import com.security.fofoqueiro.domain.dtos.PrivacyMaskResponseDTO;
import com.security.fofoqueiro.domain.dtos.PrivacyMaskUpsertDTO;
import com.security.fofoqueiro.domain.models.PrivacyMask;
import com.security.fofoqueiro.infrastructure.entities.PrivacyMaskEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PrivacyMaskMapper {

    private final ObjectMapper objectMapper;

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

    public PrivacyMask toDomain(PrivacyMaskUpsertDTO dto, Long tenantId, Long cameraId) {
        if (dto == null) {
            return null;
        }
        return PrivacyMask.builder()
                .tenantId(tenantId)
                .cameraId(cameraId)
                .name(dto.getName())
                .coordinates(serializePoints(dto.getPoints()))
                .isActive(dto.getIsActive())
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

    public PrivacyMaskResponseDTO toResponseDTO(PrivacyMask domain) {
        if (domain == null) {
            return null;
        }
        return PrivacyMaskResponseDTO.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .cameraId(domain.getCameraId())
                .name(domain.getName())
                .points(parsePoints(domain.getCoordinates()))
                .isActive(domain.getIsActive())
                .build();
    }

    private String serializePoints(List<PrivacyMaskPointDTO> points) {
        if (points == null || points.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(points);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not serialize privacy mask points", ex);
        }
    }

    private List<PrivacyMaskPointDTO> parsePoints(String coordinates) {
        if (coordinates == null || coordinates.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(coordinates, new TypeReference<List<PrivacyMaskPointDTO>>() {});
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
