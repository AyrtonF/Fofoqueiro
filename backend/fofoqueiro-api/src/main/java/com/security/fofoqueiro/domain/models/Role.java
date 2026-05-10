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
public class Role {
    private Long id;
    private String name; // e.g., ADMIN_TENANT, OPERATOR, USER
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
