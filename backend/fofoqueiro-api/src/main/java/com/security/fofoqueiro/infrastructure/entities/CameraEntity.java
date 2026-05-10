package com.security.fofoqueiro.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cameras")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId; // Link to Tenant

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String rtspUrl;

    @Column(nullable = false)
    private String status; // e.g., ONLINE, OFFLINE, ERROR

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private Integer fps;

    @Column
    private Long bitrate;

    @Column(nullable = false)
    private Integer recordingRetentionDays;

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
        if (status == null) {
            status = "UNKNOWN"; // Default status
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
