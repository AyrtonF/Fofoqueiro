package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.dtos.HealthMetricCreateDTO;
import com.security.fofoqueiro.domain.dtos.HealthMetricResponseDTO;
import com.security.fofoqueiro.domain.models.HealthMetric;
import com.security.fofoqueiro.infrastructure.entities.HealthMetricEntity;
import org.springframework.stereotype.Component;

@Component
public class HealthMetricMapper {
    public HealthMetric toDomain(HealthMetricEntity entity) {
        if (entity == null) return null;
        return HealthMetric.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .cameraId(entity.getCameraId())
                .online(entity.getOnline())
                .fps(entity.getFps())
                .bitrate(entity.getBitrate())
                .recordingConfidence(entity.getRecordingConfidence())
                .measuredAt(entity.getMeasuredAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public HealthMetricEntity toEntity(HealthMetric domain) {
        if (domain == null) return null;
        return HealthMetricEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .cameraId(domain.getCameraId())
                .online(domain.getOnline())
                .fps(domain.getFps())
                .bitrate(domain.getBitrate())
                .recordingConfidence(domain.getRecordingConfidence())
                .measuredAt(domain.getMeasuredAt())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public HealthMetric toDomain(HealthMetricCreateDTO dto) {
        if (dto == null) return null;
        return HealthMetric.builder()
                .tenantId(dto.getTenantId())
                .cameraId(dto.getCameraId())
                .online(dto.getOnline())
                .fps(dto.getFps())
                .bitrate(dto.getBitrate())
                .recordingConfidence(dto.getRecordingConfidence())
                .measuredAt(dto.getMeasuredAt())
                .build();
    }

    public HealthMetricResponseDTO toResponseDTO(HealthMetric domain) {
        if (domain == null) return null;
        return HealthMetricResponseDTO.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .cameraId(domain.getCameraId())
                .online(domain.getOnline())
                .fps(domain.getFps())
                .bitrate(domain.getBitrate())
                .recordingConfidence(domain.getRecordingConfidence())
                .measuredAt(domain.getMeasuredAt())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
