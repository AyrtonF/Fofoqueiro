package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.Camera;

import java.util.List;
import java.util.Optional;

public interface ICameraRepositoryPort {
    Optional<Camera> findById(Long id);
    Camera save(Camera camera);
    void deleteById(Long id);
    List<Camera> findByTenantId(Long tenantId);
    List<Camera> findByTenantIdAndStatus(Long tenantId, String status);
}
