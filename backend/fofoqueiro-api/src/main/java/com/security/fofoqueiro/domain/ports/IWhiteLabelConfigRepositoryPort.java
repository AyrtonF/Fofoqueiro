package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.WhiteLabelConfig;

import java.util.List;
import java.util.Optional;

public interface IWhiteLabelConfigRepositoryPort {
    List<WhiteLabelConfig> findAll();
    Optional<WhiteLabelConfig> findById(Long id);
    Optional<WhiteLabelConfig> findByTenantId(Long tenantId);
    WhiteLabelConfig save(WhiteLabelConfig whiteLabelConfig);
    void deleteById(Long id);
}
