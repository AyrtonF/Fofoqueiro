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
public class HealthMetricResponseDTO {
    private Long id;
    private Long tenantId;
    private Long cameraId;
    private Boolean online;
    private Integer fps;
    private Long bitrate;
    private Double recordingConfidence;
    private LocalDateTime measuredAt;
    private LocalDateTime createdAt;
}
