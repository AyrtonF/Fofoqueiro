package com.security.fofoqueiro.application.services;

import com.security.fofoqueiro.domain.dtos.*;
import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.Camera;
import com.security.fofoqueiro.domain.models.PrivacyMask;
import com.security.fofoqueiro.domain.models.Recording;
import com.security.fofoqueiro.domain.ports.ICameraRepositoryPort;
import com.security.fofoqueiro.domain.ports.IGatewayRepositoryPort;
import com.security.fofoqueiro.domain.ports.IPrivacyMaskRepositoryPort;
import com.security.fofoqueiro.domain.ports.IRecordingRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.CameraMapper;
import com.security.fofoqueiro.infrastructure.mappers.PrivacyMaskMapper;
import com.security.fofoqueiro.infrastructure.mappers.RecordingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CameraManagementService {

    private final ICameraRepositoryPort cameraRepositoryPort;
    private final IGatewayRepositoryPort gatewayRepositoryPort;
    private final IPrivacyMaskRepositoryPort privacyMaskRepositoryPort;
    private final IRecordingRepositoryPort recordingRepositoryPort;
    private final CameraMapper cameraMapper;
    private final PrivacyMaskMapper privacyMaskMapper;
    private final RecordingMapper recordingMapper;

    public List<CameraResponseDTO> getCameras(Long tenantId) {
        return cameraRepositoryPort.findByTenantId(tenantId).stream()
                .map(cameraMapper::toResponseDTO)
                .toList();
    }

    public CameraResponseDTO getCamera(Long tenantId, Long cameraId) {
        Camera camera = findCamera(tenantId, cameraId);
        return cameraMapper.toResponseDTO(camera);
    }

    @Transactional
    public CameraResponseDTO updateCamera(Long tenantId, Long cameraId, CameraUpdateDTO dto) {
        Camera existing = findCamera(tenantId, cameraId);
        if (dto.getGatewayId() != null) {
            gatewayRepositoryPort.findById(dto.getGatewayId())
                    .orElseThrow(() -> new EntityNotFoundException("Gateway with ID " + dto.getGatewayId() + " not found."));
        }

        existing.setTenantId(tenantId);
        existing.setGatewayId(dto.getGatewayId() != null ? dto.getGatewayId() : existing.getGatewayId());
        existing.setName(dto.getName() != null ? dto.getName() : existing.getName());
        existing.setRtspUrl(dto.getUrl() != null ? dto.getUrl() : existing.getRtspUrl());
        existing.setLatitude(dto.getLatitude() != null ? dto.getLatitude() : existing.getLatitude());
        existing.setLongitude(dto.getLongitude() != null ? dto.getLongitude() : existing.getLongitude());
        existing.setRecordingRetentionDays(dto.getRecordingRetentionDays() != null ? dto.getRecordingRetentionDays() : existing.getRecordingRetentionDays());
        existing.setStatus(dto.getStatus() != null ? dto.getStatus() : existing.getStatus());
        existing.setFps(dto.getFps() != null ? dto.getFps() : existing.getFps());
        existing.setBitrate(dto.getBitrate() != null ? dto.getBitrate() : existing.getBitrate());
        existing.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : existing.getIsActive());

        return cameraMapper.toResponseDTO(cameraRepositoryPort.save(existing));
    }

    @Transactional
    public void deleteCamera(Long tenantId, Long cameraId) {
        findCamera(tenantId, cameraId);
        cameraRepositoryPort.deleteById(cameraId);
    }

    public List<CameraHealthDTO> getHealth(Long tenantId) {
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

    public TestConnectionResponseDTO testConnection(Long tenantId, CameraTestConnectionRequestDTO dto) {
        if (dto.getUrl() == null || !dto.getUrl().startsWith("rtsp://")) {
            return TestConnectionResponseDTO.builder()
                    .success(false)
                    .message("RTSP URL inválida")
                    .build();
        }

        if (dto.getGatewayId() != null) {
            gatewayRepositoryPort.findById(dto.getGatewayId())
                    .orElseThrow(() -> new EntityNotFoundException("Gateway with ID " + dto.getGatewayId() + " not found."));
        }

        return TestConnectionResponseDTO.builder()
                .success(true)
                .message("Conexão validada com sucesso")
                .build();
    }

    public CameraStreamResponseDTO getStream(Long tenantId, Long cameraId, Map<String, Object> signalData) {
        Camera camera = findCamera(tenantId, cameraId);
        return CameraStreamResponseDTO.builder()
                .cameraId(camera.getId())
                .streamUrl(camera.getRtspUrl())
                .status(camera.getStatus())
                .build();
    }

    public List<PrivacyMaskResponseDTO> getPrivacyMasks(Long tenantId, Long cameraId) {
        return privacyMaskRepositoryPort.findByTenantIdAndCameraId(tenantId, cameraId).stream()
                .map(privacyMaskMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public List<PrivacyMaskResponseDTO> updatePrivacyMasks(Long tenantId, Long cameraId, List<PrivacyMaskUpsertDTO> masks) {
        findCamera(tenantId, cameraId);
        List<PrivacyMask> existingMasks = privacyMaskRepositoryPort.findByTenantIdAndCameraId(tenantId, cameraId);
        for (PrivacyMask existingMask : existingMasks) {
            if (existingMask.getId() != null) {
                privacyMaskRepositoryPort.deleteById(existingMask.getId());
            }
        }

        List<PrivacyMaskResponseDTO> savedMasks = new ArrayList<>();
        for (PrivacyMaskUpsertDTO dto : masks) {
            PrivacyMask saved = privacyMaskRepositoryPort.save(privacyMaskMapper.toDomain(dto, tenantId, cameraId));
            savedMasks.add(privacyMaskMapper.toResponseDTO(saved));
        }
        return savedMasks;
    }

    public List<RecordingResponseDTO> getRecordings(Long tenantId, Long cameraId, String date) {
        List<Recording> recordings;
        if (date == null || date.isBlank()) {
            recordings = recordingRepositoryPort.findByTenantIdAndCameraId(tenantId, cameraId);
        } else {
            LocalDate selectedDate = LocalDate.parse(date);
            LocalDateTime start = selectedDate.atStartOfDay();
            LocalDateTime end = selectedDate.atTime(LocalTime.MAX);
            recordings = recordingRepositoryPort.findByTenantIdAndCameraIdAndBetweenDates(tenantId, cameraId, start, end);
        }

        return recordings.stream()
                .map(recordingMapper::toResponseDTO)
                .toList();
    }

    private Camera findCamera(Long tenantId, Long cameraId) {
        Camera camera = cameraRepositoryPort.findById(cameraId)
                .orElseThrow(() -> new EntityNotFoundException("Camera with ID " + cameraId + " not found."));
        if (!tenantId.equals(camera.getTenantId())) {
            throw new EntityNotFoundException("Camera with ID " + cameraId + " not found for tenant " + tenantId + ".");
        }
        return camera;
    }
}
