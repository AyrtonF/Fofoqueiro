package com.security.fofoqueiro.infrastructure.mappers;

import com.security.fofoqueiro.domain.models.Recording;
import com.security.fofoqueiro.infrastructure.entities.RecordingEntity;
import org.springframework.stereotype.Component;

@Component
public class RecordingMapper {

    public Recording toDomain(RecordingEntity entity) {
        if (entity == null) {
            return null;
        }
        return Recording.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .cameraId(entity.getCameraId())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .s3Path(entity.getS3Path())
                .durationSeconds(entity.getDurationSeconds())
                .fileSizeMb(entity.getFileSizeMb())
                .eventType(entity.getEventType())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public RecordingEntity toEntity(Recording domain) {
        if (domain == null) {
            return null;
        }
        return RecordingEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .cameraId(domain.getCameraId())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .s3Path(domain.getS3Path())
                .durationSeconds(domain.getDurationSeconds())
                .fileSizeMb(domain.getFileSizeMb())
                .eventType(domain.getEventType())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
