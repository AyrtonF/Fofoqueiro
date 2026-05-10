package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.Camera;
import com.security.fofoqueiro.domain.ports.ICameraRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.CameraEntity;
import com.security.fofoqueiro.infrastructure.mappers.CameraMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataCameraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CameraRepositoryAdapterImpl implements ICameraRepositoryPort {

    private final SpringDataCameraRepository cameraRepository;
    private final CameraMapper cameraMapper;

    @Override
    public Optional<Camera> findById(Long id) {
        return cameraRepository.findById(id)
                .map(cameraMapper::toDomain);
    }

    @Override
    public Camera save(Camera camera) {
        CameraEntity cameraEntity = cameraMapper.toEntity(camera);
        CameraEntity savedEntity = cameraRepository.save(cameraEntity);
        return cameraMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        cameraRepository.deleteById(id);
    }

    @Override
    public List<Camera> findByTenantId(Long tenantId) {
        return cameraRepository.findByTenantId(tenantId)
                .stream()
                .map(cameraMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Camera> findByTenantIdAndStatus(Long tenantId, String status) {
        return cameraRepository.findByTenantIdAndStatus(tenantId, status)
                .stream()
                .map(cameraMapper::toDomain)
                .collect(Collectors.toList());
    }
}
