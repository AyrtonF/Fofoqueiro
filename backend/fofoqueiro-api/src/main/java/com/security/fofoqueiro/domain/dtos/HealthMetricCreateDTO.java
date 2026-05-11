package com.security.fofoqueiro.domain.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthMetricCreateDTO {
    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "Camera ID is required")
    private Long cameraId;

    @NotNull(message = "Online flag is required")
    private Boolean online;

    private Integer fps;
    private Long bitrate;
    private Double recordingConfidence;
    private LocalDateTime measuredAt;
}
