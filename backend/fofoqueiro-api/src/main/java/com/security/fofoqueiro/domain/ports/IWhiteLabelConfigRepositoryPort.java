package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.WhiteLabelConfig;

import java.util.Optional;

public interface IWhiteLabelConfigRepositoryPort {
    Optional<WhiteLabelConfig> findById(Long id);
    Optional<WhiteLabelConfig> findByTenantId(Long tenantId);
    WhiteLabelConfig save(WhiteLabelConfig whiteLabelConfig);
    void deleteById(Long id);
}
