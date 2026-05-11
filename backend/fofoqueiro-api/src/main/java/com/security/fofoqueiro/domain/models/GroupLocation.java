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
public class GroupLocation {
    private Long id;
    private Long tenantId;
    private String name;
    private Long parentGroupId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
