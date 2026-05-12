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
public class WhiteLabelConfigResponseDTO {
    private Long id;
    private Long tenantId;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private String faviconUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
