package com.security.fofoqueiro.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthMetricEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private Long cameraId;

    @Column(nullable = false)
    private Boolean online;

    @Column
    private Integer fps;

    @Column
    private Long bitrate;

    @Column
    private Double recordingConfidence;

    @Column(nullable = false)
    private LocalDateTime measuredAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (measuredAt == null) {
            measuredAt = createdAt;
        }
        if (online == null) {
            online = false;
        }
    }
}
