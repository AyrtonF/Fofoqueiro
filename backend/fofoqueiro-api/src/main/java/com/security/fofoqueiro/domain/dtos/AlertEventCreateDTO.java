package com.security.fofoqueiro.domain.dtos;

import jakarta.validation.constraints.NotBlank;
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
public class AlertEventCreateDTO {
    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "Camera ID is required")
    private Long cameraId;

    @NotBlank(message = "Event type is required")
    private String eventType;

    private LocalDateTime eventTime;
    private String snapshotUrl;
    private String description;
    private Boolean acknowledged;
}
