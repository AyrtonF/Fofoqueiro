package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.PrivacyMask;

import java.util.List;
import java.util.Optional;

public interface IPrivacyMaskRepositoryPort {
    Optional<PrivacyMask> findById(Long id);
    PrivacyMask save(PrivacyMask privacyMask);
    void deleteById(Long id);
    List<PrivacyMask> findByTenantId(Long tenantId);
    List<PrivacyMask> findByTenantIdAndCameraId(Long tenantId, Long cameraId);
}
