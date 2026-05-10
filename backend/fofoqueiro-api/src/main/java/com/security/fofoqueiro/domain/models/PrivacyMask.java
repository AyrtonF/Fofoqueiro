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
public class PrivacyMask {
    private Long id;
    private Long tenantId; // Link to Tenant
    private Long cameraId; // Link to Camera
    private String name; // Name of the mask
    private String coordinates; // JSON string representing the coordinates of the mask (e.g., "[{x:0,y:0,w:100,h:100}]")
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
