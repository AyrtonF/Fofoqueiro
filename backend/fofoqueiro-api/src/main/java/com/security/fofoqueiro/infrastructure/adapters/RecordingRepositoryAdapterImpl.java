package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.Recording;
import com.security.fofoqueiro.domain.ports.IRecordingRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.RecordingEntity;
import com.security.fofoqueiro.infrastructure.mappers.RecordingMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataRecordingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecordingRepositoryAdapterImpl implements IRecordingRepositoryPort {

    private final SpringDataRecordingRepository recordingRepository;
    private final RecordingMapper recordingMapper;

    @Override
    public Optional<Recording> findById(Long id) {
        return recordingRepository.findById(id)
                .map(recordingMapper::toDomain);
    }

    @Override
    public Recording save(Recording recording) {
        RecordingEntity recordingEntity = recordingMapper.toEntity(recording);
        RecordingEntity savedEntity = recordingRepository.save(recordingEntity);
        return recordingMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        recordingRepository.deleteById(id);
    }

    @Override
    public List<Recording> findByTenantIdAndCameraId(Long tenantId, Long cameraId) {
        return recordingRepository.findByTenantIdAndCameraId(tenantId, cameraId)
                .stream()
                .map(recordingMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Recording> findByTenantIdAndCameraIdAndBetweenDates(Long tenantId, Long cameraId, LocalDateTime start, LocalDateTime end) {
        return recordingRepository.findByTenantIdAndCameraIdAndStartTimeBetween(tenantId, cameraId, start, end)
                .stream()
                .map(recordingMapper::toDomain)
                .collect(Collectors.toList());
    }
}
