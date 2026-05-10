package com.security.fofoqueiro.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId; // Link to Tenant

    @Column(nullable = false)
    private Long userId; // User who performed the action

    @Column(nullable = false)
    private String action; // e.g., VIEW_VIDEO, DOWNLOAD_VIDEO, CONFIG_CHANGE

    @Column(nullable = false)
    private String entityName; // e.g., CAMERA, USER, TENANT

    @Column(nullable = false)
    private Long entityId; // ID of the entity affected

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String details; // JSON string of changes (before/after), or other relevant info

    @Column(nullable = false, unique = true) // Hash must be unique for immutability
    private String hash; // SHA-256 hash for immutability

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
