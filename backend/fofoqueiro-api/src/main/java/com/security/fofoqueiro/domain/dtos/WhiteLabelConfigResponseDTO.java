package com.security.fofoqueiro.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhiteLabelConfigResponseDTO {
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private String faviconUrl;
}
