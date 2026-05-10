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
public class AuditLog {
    private Long id;
    private Long tenantId; // Link to Tenant
    private Long userId; // User who performed the action
    private String action; // e.g., VIEW_VIDEO, DOWNLOAD_VIDEO, CONFIG_CHANGE
    private String entityName; // e.g., CAMERA, USER, TENANT
    private Long entityId; // ID of the entity affected
    private LocalDateTime timestamp;
    private String ipAddress;
    private String details; // JSON string of changes (before/after), or other relevant info
    private String hash; // SHA-256 hash for immutability
}
