package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.PrivacyMask;
import com.security.fofoqueiro.domain.ports.IPrivacyMaskRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.PrivacyMaskEntity;
import com.security.fofoqueiro.infrastructure.mappers.PrivacyMaskMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataPrivacyMaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PrivacyMaskRepositoryAdapterImpl implements IPrivacyMaskRepositoryPort {

    private final SpringDataPrivacyMaskRepository privacyMaskRepository;
    private final PrivacyMaskMapper privacyMaskMapper;

    @Override
    public Optional<PrivacyMask> findById(Long id) {
        return privacyMaskRepository.findById(id)
                .map(privacyMaskMapper::toDomain);
    }

    @Override
    public PrivacyMask save(PrivacyMask privacyMask) {
        PrivacyMaskEntity privacyMaskEntity = privacyMaskMapper.toEntity(privacyMask);
        PrivacyMaskEntity savedEntity = privacyMaskRepository.save(privacyMaskEntity);
        return privacyMaskMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        privacyMaskRepository.deleteById(id);
    }

    @Override
    public List<PrivacyMask> findByTenantId(Long tenantId) {
        return privacyMaskRepository.findByTenantId(tenantId)
                .stream()
                .map(privacyMaskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrivacyMask> findByTenantIdAndCameraId(Long tenantId, Long cameraId) {
        return privacyMaskRepository.findByTenantIdAndCameraId(tenantId, cameraId)
                .stream()
                .map(privacyMaskMapper::toDomain)
                .collect(Collectors.toList());
    }
}
