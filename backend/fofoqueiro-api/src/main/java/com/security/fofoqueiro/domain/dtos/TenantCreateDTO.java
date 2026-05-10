package com.security.fofoqueiro.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantCreateDTO {
    @NotBlank(message = "Tenant name is required")
    private String name;

    @NotBlank(message = "Tenant domain is required")
    @Pattern(regexp = "^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid domain format")
    private String domain;
}
