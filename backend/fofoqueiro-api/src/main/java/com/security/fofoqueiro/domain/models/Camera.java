package com.security.fofoqueiro.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Camera {
    private Long id;
    private Long tenantId; // Link to Tenant
    private String name;
    private String rtspUrl; // URL for the camera stream
    private String status; // e.g., ONLINE, OFFLINE, ERROR (Can be an Enum later)
    private Double latitude;
    private Double longitude;
    private Integer fps;
    private Long bitrate;
    private Integer recordingRetentionDays;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
