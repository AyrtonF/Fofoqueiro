package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.HealthMetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataHealthMetricRepository extends JpaRepository<HealthMetricEntity, Long> {
    List<HealthMetricEntity> findByTenantId(Long tenantId);
    List<HealthMetricEntity> findByTenantIdAndCameraId(Long tenantId, Long cameraId);
}
