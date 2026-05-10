package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.Tenant;

import java.util.Optional;

public interface ITenantRepositoryPort {
    Optional<Tenant> findById(Long id);
    Optional<Tenant> findByDomain(String domain);
    Optional<Tenant> findByName(String name);
    Tenant save(Tenant tenant);
    void deleteById(Long id);
}
