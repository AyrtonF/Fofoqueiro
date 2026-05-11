package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.HealthMetric;

import java.util.List;
import java.util.Optional;

public interface IHealthMetricRepositoryPort {
    Optional<HealthMetric> findById(Long id);
    HealthMetric save(HealthMetric healthMetric);
    void deleteById(Long id);
    List<HealthMetric> findByTenantId(Long tenantId);
    List<HealthMetric> findByTenantIdAndCameraId(Long tenantId, Long cameraId);
}
