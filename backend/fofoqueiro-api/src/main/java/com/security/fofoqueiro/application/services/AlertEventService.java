package com.security.fofoqueiro.application.services;

import com.security.fofoqueiro.domain.dtos.AlertEventCreateDTO;
import com.security.fofoqueiro.domain.dtos.AlertEventResponseDTO;
import com.security.fofoqueiro.domain.dtos.CameraHealthDTO;
import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.AlertEvent;
import com.security.fofoqueiro.domain.ports.IAlertEventRepositoryPort;
import com.security.fofoqueiro.domain.ports.ICameraRepositoryPort;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.AlertEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertEventService {
    private final IAlertEventRepositoryPort repositoryPort;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final ICameraRepositoryPort cameraRepositoryPort;
    private final AlertEventMapper mapper;

    public List<AlertEventResponseDTO> list(Long tenantId, Long cameraId) {
        if (cameraId == null) {
            return repositoryPort.findByTenantId(tenantId).stream().map(mapper::toResponseDTO).toList();
        }
        return repositoryPort.findByTenantIdAndCameraId(tenantId, cameraId).stream().map(mapper::toResponseDTO).toList();
    }

    @Transactional
    public AlertEventResponseDTO create(AlertEventCreateDTO dto) {
        tenantRepositoryPort.findById(dto.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID " + dto.getTenantId() + " not found."));
        cameraRepositoryPort.findById(dto.getCameraId())
                .orElseThrow(() -> new EntityNotFoundException("Camera with ID " + dto.getCameraId() + " not found."));
        AlertEvent domain = mapper.toDomain(dto);
        if (domain.getEventTime() == null) {
            domain.setEventTime(LocalDateTime.now());
        }
        AlertEvent saved = repositoryPort.save(domain);
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    public AlertEventResponseDTO acknowledge(Long tenantId, Long id) {
        AlertEvent existing = findAlertEvent(tenantId, id);
        existing.setAcknowledged(true);
        return mapper.toResponseDTO(repositoryPort.save(existing));
    }

    @Transactional
    public void delete(Long tenantId, Long id) {
        findAlertEvent(tenantId, id);
        repositoryPort.deleteById(id);
    }

    public List<CameraHealthDTO> summarizeHealth(Long tenantId) {
        return cameraRepositoryPort.findByTenantId(tenantId).stream()
                .map(camera -> CameraHealthDTO.builder()
                        .id(camera.getId())
                        .fps(camera.getFps())
                        .bitrate(camera.getBitrate())
                        .status(camera.getStatus())
                        .online("ONLINE".equalsIgnoreCase(camera.getStatus()))
                        .build())
                .toList();
    }

    private AlertEvent findAlertEvent(Long tenantId, Long id) {
        AlertEvent alertEvent = repositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alert event with ID " + id + " not found."));
        if (!tenantId.equals(alertEvent.getTenantId())) {
            throw new EntityNotFoundException("Alert event with ID " + id + " not found for tenant " + tenantId + ".");
        }
        return alertEvent;
    }
}
