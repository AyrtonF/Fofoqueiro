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
public class UserSessionResponseDTO {
    private Long id;
    private Long tenantId;
    private Long userId;
    private String tokenId;
    private LocalDateTime expiresAt;
    private LocalDateTime lastActivityAt;
    private LocalDateTime createdAt;
}
