package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.dtos.CameraCreateDTO;
import com.security.fofoqueiro.domain.dtos.CameraResponseDTO;
import com.security.fofoqueiro.domain.dtos.CameraUpdateDTO;
import com.security.fofoqueiro.domain.models.Camera;
import com.security.fofoqueiro.infrastructure.entities.CameraEntity;
import org.springframework.stereotype.Component;

@Component
public class CameraMapper {

    public Camera toDomain(CameraEntity entity) {
        if (entity == null) {
            return null;
        }
        return Camera.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
            .gatewayId(entity.getGatewayId())
                .name(entity.getName())
                .rtspUrl(entity.getRtspUrl())
                .status(entity.getStatus())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .fps(entity.getFps())
                .bitrate(entity.getBitrate())
                .recordingRetentionDays(entity.getRecordingRetentionDays())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public CameraEntity toEntity(Camera domain) {
        if (domain == null) {
            return null;
        }
        return CameraEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
            .gatewayId(domain.getGatewayId())
                .name(domain.getName())
                .rtspUrl(domain.getRtspUrl())
                .status(domain.getStatus())
                .latitude(domain.getLatitude())
                .longitude(domain.getLongitude())
                .fps(domain.getFps())
                .bitrate(domain.getBitrate())
                .recordingRetentionDays(domain.getRecordingRetentionDays())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public Camera toDomain(CameraCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        return Camera.builder()
                .tenantId(dto.getTenantId())
                .gatewayId(dto.getGatewayId())
                .name(dto.getName())
                .rtspUrl(dto.getUrl())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .recordingRetentionDays(dto.getRecordingRetentionDays())
                .build();
    }

    public Camera toDomain(CameraUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        return Camera.builder()
                .id(dto.getId())
                .tenantId(dto.getTenantId())
                .gatewayId(dto.getGatewayId())
                .name(dto.getName())
                .rtspUrl(dto.getUrl())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .recordingRetentionDays(dto.getRecordingRetentionDays())
                .status(dto.getStatus())
                .fps(dto.getFps())
                .bitrate(dto.getBitrate())
                .isActive(dto.getIsActive())
                .build();
    }

    public CameraResponseDTO toResponseDTO(Camera domain) {
        if (domain == null) {
            return null;
        }
        return CameraResponseDTO.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
            .gatewayId(domain.getGatewayId())
                .name(domain.getName())
            .url(domain.getRtspUrl())
                .rtspUrl(domain.getRtspUrl())
                .status(domain.getStatus())
                .latitude(domain.getLatitude())
                .longitude(domain.getLongitude())
                .fps(domain.getFps())
                .bitrate(domain.getBitrate())
                .recordingRetentionDays(domain.getRecordingRetentionDays())
                .isActive(domain.getIsActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
