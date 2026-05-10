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
public class CameraResponseDTO {
    private Long id;
    private Long tenantId;
    private String name;
    private String rtspUrl;
    private String status;
    private Double latitude;
    private Double longitude;
    private Integer fps;
    private Long bitrate;
    private Integer recordingRetentionDays;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
