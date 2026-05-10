package com.security.fofoqueiro.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "gateways")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId; // Link to Tenant

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String ipAddress;

    @Column
    private String location;

    @Column(nullable = false)
    private String status; // e.g., ONLINE, OFFLINE, DEGRADED

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
