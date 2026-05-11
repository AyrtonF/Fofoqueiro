package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.HealthMetric;
import com.security.fofoqueiro.domain.ports.IHealthMetricRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.HealthMetricEntity;
import com.security.fofoqueiro.infrastructure.mappers.HealthMetricMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataHealthMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HealthMetricRepositoryAdapterImpl implements IHealthMetricRepositoryPort {
    private final SpringDataHealthMetricRepository repository;
    private final HealthMetricMapper mapper;

    @Override
    public Optional<HealthMetric> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public HealthMetric save(HealthMetric healthMetric) {
        HealthMetricEntity saved = repository.save(mapper.toEntity(healthMetric));
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<HealthMetric> findByTenantId(Long tenantId) {
        return repository.findByTenantId(tenantId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<HealthMetric> findByTenantIdAndCameraId(Long tenantId, Long cameraId) {
        return repository.findByTenantIdAndCameraId(tenantId, cameraId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
