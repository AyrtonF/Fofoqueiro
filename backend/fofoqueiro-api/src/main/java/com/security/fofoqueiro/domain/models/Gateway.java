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
public class Gateway {
    private Long id;
    private Long tenantId; // Link to Tenant
    private String name;
    private String ipAddress;
    private String location;
    private String status; // e.g., ONLINE, OFFLINE, DEGRADED (Can be an Enum later)
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
