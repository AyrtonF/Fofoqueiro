package com.security.fofoqueiro.domain.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("resource")
    @JsonAlias({"resource"})
    private String entityName;
    @JsonProperty("resourceId")
    @JsonAlias({"resourceId"})
    private String entityId;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String details;
    private String hash;
}
