package com.security.fofoqueiro.domain.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionCreateDTO {
    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Token ID is required")
    private String tokenId;

    @NotNull(message = "Expires at is required")
    private LocalDateTime expiresAt;

    private LocalDateTime lastActivityAt;
}
