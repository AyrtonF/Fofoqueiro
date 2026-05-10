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
public class Recording {
    private Long id;
    private Long tenantId; // Link to Tenant
    private Long cameraId; // Link to Camera
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String s3Path; // Path to the recording in S3
    private Long durationSeconds;
    private Double fileSizeMb;
    private String eventType; // e.g., MOTION_DETECTION, MANUAL_RECORDING (Can be an Enum later)
    private LocalDateTime createdAt;
}
