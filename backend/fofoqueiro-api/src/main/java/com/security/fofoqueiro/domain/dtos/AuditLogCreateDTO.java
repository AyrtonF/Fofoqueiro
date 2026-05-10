package com.security.fofoqueiro.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogCreateDTO {
    @NotNull(message = "Tenant ID is required for audit log")
    private Long tenantId;

    @NotNull(message = "User ID is required for audit log")
    private Long userId;

    @NotBlank(message = "Action is required for audit log")
    private String action;

    @NotBlank(message = "Entity name is required for audit log")
    private String entityName;

    @NotNull(message = "Entity ID is required for audit log")
    private Long entityId;

    @NotBlank(message = "IP Address is required for audit log")
    private String ipAddress;

    private String details; // JSON string of changes (before/after), or other relevant info
    private String hash; // SHA-256 hash for immutability
}
