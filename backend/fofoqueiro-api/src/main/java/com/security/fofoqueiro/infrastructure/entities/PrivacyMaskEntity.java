package com.security.fofoqueiro.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "privacy_masks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyMaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId; // Link to Tenant

    @Column(nullable = false)
    private Long cameraId; // Link to Camera

    @Column(nullable = false)
    private String name; // Name of the mask

    @Column(nullable = false, columnDefinition = "TEXT")
    private String coordinates; // JSON string representing the coordinates of the mask

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        isActive = true; // Default to active
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
