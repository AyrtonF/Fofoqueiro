package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.Recording;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IRecordingRepositoryPort {
    Optional<Recording> findById(Long id);
    Recording save(Recording recording);
    void deleteById(Long id);
    List<Recording> findByTenantIdAndCameraId(Long tenantId, Long cameraId);
    List<Recording> findByTenantIdAndCameraIdAndBetweenDates(Long tenantId, Long cameraId, LocalDateTime start, LocalDateTime end);
}
