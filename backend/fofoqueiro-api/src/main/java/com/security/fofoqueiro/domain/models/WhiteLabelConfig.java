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
public class WhiteLabelConfig {
    private Long id;
    private Long tenantId; // Link to Tenant
    private String logoUrl;
    private String primaryColor; // Hex code
    private String secondaryColor; // Hex code
    private String faviconUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
