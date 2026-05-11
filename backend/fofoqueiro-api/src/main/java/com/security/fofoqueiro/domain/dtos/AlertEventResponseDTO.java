package com.security.fofoqueiro.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertEventResponseDTO {
    private Long id;
    private Long tenantId;
    private Long cameraId;
    private String eventType;
    private LocalDateTime eventTime;
    private String snapshotUrl;
    private String description;
    private Boolean acknowledged;
    private LocalDateTime createdAt;
}
