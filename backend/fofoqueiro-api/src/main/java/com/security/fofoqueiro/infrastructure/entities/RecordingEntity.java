package com.security.fofoqueiro.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recordings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId; // Link to Tenant

    @Column(nullable = false)
    private Long cameraId; // Link to Camera

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String s3Path; // Path to the recording in S3

    @Column(nullable = false)
    private Long durationSeconds;

    @Column(nullable = false)
    private Double fileSizeMb;

    @Column(nullable = false)
    private String eventType; // e.g., MOTION_DETECTION, MANUAL_RECORDING

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
