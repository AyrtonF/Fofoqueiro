package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.dtos.AlertEventCreateDTO;
import com.security.fofoqueiro.domain.dtos.AlertEventResponseDTO;
import com.security.fofoqueiro.domain.models.AlertEvent;
import com.security.fofoqueiro.infrastructure.entities.AlertEventEntity;
import org.springframework.stereotype.Component;

@Component
public class AlertEventMapper {
    public AlertEvent toDomain(AlertEventEntity entity) {
        if (entity == null) {
            return null;
        }
        return AlertEvent.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .cameraId(entity.getCameraId())
                .eventType(entity.getEventType())
                .eventTime(entity.getEventTime())
                .snapshotUrl(entity.getSnapshotUrl())
                .description(entity.getDescription())
                .acknowledged(entity.getAcknowledged())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public AlertEventEntity toEntity(AlertEvent domain) {
        if (domain == null) {
            return null;
        }
        return AlertEventEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .cameraId(domain.getCameraId())
                .eventType(domain.getEventType())
                .eventTime(domain.getEventTime())
                .snapshotUrl(domain.getSnapshotUrl())
                .description(domain.getDescription())
                .acknowledged(domain.getAcknowledged())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public AlertEvent toDomain(AlertEventCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        return AlertEvent.builder()
                .tenantId(dto.getTenantId())
                .cameraId(dto.getCameraId())
                .eventType(dto.getEventType())
                .eventTime(dto.getEventTime())
                .snapshotUrl(dto.getSnapshotUrl())
                .description(dto.getDescription())
                .acknowledged(dto.getAcknowledged())
                .build();
    }

    public AlertEventResponseDTO toResponseDTO(AlertEvent domain) {
        if (domain == null) {
            return null;
        }
        return AlertEventResponseDTO.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .cameraId(domain.getCameraId())
                .eventType(domain.getEventType())
                .eventTime(domain.getEventTime())
                .snapshotUrl(domain.getSnapshotUrl())
                .description(domain.getDescription())
                .acknowledged(domain.getAcknowledged())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
