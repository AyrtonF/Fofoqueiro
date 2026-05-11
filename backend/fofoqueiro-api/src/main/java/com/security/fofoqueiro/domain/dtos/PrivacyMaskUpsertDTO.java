package com.security.fofoqueiro.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyMaskUpsertDTO {
    private Long id;
    private Long cameraId;
    private Long tenantId;
    private String name;
    private List<PrivacyMaskPointDTO> points;
    private Boolean isActive;
}
