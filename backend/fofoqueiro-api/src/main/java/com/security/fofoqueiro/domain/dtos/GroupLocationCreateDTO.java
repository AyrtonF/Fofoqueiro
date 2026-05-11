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
public class GroupLocationCreateDTO {
    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotBlank(message = "Name is required")
    private String name;

    private Long parentGroupId;
}
