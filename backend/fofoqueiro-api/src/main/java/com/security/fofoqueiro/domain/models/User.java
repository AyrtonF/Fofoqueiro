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
public class User {
    private Long id;
    private Long tenantId; // Link to Tenant
    private String email;
    private String password; // Encoded password
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Boolean mfaEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
