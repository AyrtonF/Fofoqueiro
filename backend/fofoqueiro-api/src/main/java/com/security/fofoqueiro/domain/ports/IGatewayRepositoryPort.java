package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.Gateway;

import java.util.List;
import java.util.Optional;

public interface IGatewayRepositoryPort {
    Optional<Gateway> findById(Long id);
    Gateway save(Gateway gateway);
    void deleteById(Long id);
    List<Gateway> findByTenantId(Long tenantId);
    List<Gateway> findByTenantIdAndStatus(Long tenantId, String status);
}
