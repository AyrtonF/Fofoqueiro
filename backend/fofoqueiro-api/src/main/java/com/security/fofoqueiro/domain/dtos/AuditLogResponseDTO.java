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
public class AuditLogResponseDTO {
    private Long id;
    private Long tenantId;
    private Long userId;
    private String action;
    private String entityName;
    private Long entityId;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String details;
    private String hash;
}
