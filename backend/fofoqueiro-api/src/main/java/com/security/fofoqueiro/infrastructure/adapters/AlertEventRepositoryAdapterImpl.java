package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.AlertEvent;
import com.security.fofoqueiro.domain.ports.IAlertEventRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.AlertEventEntity;
import com.security.fofoqueiro.infrastructure.mappers.AlertEventMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataAlertEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AlertEventRepositoryAdapterImpl implements IAlertEventRepositoryPort {
    private final SpringDataAlertEventRepository repository;
    private final AlertEventMapper mapper;

    @Override
    public Optional<AlertEvent> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public AlertEvent save(AlertEvent alertEvent) {
        AlertEventEntity saved = repository.save(mapper.toEntity(alertEvent));
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<AlertEvent> findByTenantId(Long tenantId) {
        return repository.findByTenantId(tenantId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<AlertEvent> findByTenantIdAndCameraId(Long tenantId, Long cameraId) {
        return repository.findByTenantIdAndCameraId(tenantId, cameraId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
