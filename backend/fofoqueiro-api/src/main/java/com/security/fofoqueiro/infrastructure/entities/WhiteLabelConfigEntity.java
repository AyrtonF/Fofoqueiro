package com.security.fofoqueiro.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "white_label_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhiteLabelConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long tenantId; // Link to Tenant

    @Column
    private String logoUrl;

    @Column
    private String primaryColor; // Hex code

    @Column
    private String secondaryColor; // Hex code

    @Column
    private String faviconUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
