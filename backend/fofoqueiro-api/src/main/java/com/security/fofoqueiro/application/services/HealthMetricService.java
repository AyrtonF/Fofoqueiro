package com.security.fofoqueiro.application.services;

import com.security.fofoqueiro.domain.dtos.HealthMetricCreateDTO;
import com.security.fofoqueiro.domain.dtos.HealthMetricResponseDTO;
import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.HealthMetric;
import com.security.fofoqueiro.domain.ports.ICameraRepositoryPort;
import com.security.fofoqueiro.domain.ports.IHealthMetricRepositoryPort;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.HealthMetricMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthMetricService {
    private final IHealthMetricRepositoryPort repositoryPort;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final ICameraRepositoryPort cameraRepositoryPort;
    private final HealthMetricMapper mapper;

    public List<HealthMetricResponseDTO> list(Long tenantId, Long cameraId) {
        if (cameraId == null) {
            return repositoryPort.findByTenantId(tenantId).stream().map(mapper::toResponseDTO).toList();
        }
        return repositoryPort.findByTenantIdAndCameraId(tenantId, cameraId).stream().map(mapper::toResponseDTO).toList();
    }

    @Transactional
    public HealthMetricResponseDTO create(HealthMetricCreateDTO dto) {
        tenantRepositoryPort.findById(dto.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID " + dto.getTenantId() + " not found."));
        cameraRepositoryPort.findById(dto.getCameraId())
                .orElseThrow(() -> new EntityNotFoundException("Camera with ID " + dto.getCameraId() + " not found."));
        HealthMetric metric = mapper.toDomain(dto);
        if (metric.getMeasuredAt() == null) {
            metric.setMeasuredAt(LocalDateTime.now());
        }
        return mapper.toResponseDTO(repositoryPort.save(metric));
    }
}
