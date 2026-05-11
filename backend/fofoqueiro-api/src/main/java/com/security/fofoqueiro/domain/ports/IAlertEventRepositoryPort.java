package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.AlertEvent;

import java.util.List;
import java.util.Optional;

public interface IAlertEventRepositoryPort {
    Optional<AlertEvent> findById(Long id);
    AlertEvent save(AlertEvent alertEvent);
    void deleteById(Long id);
    List<AlertEvent> findByTenantId(Long tenantId);
    List<AlertEvent> findByTenantIdAndCameraId(Long tenantId, Long cameraId);
}
