package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.TenantEntity;
import com.security.fofoqueiro.infrastructure.mappers.TenantMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TenantRepositoryAdapterImpl implements ITenantRepositoryPort {

    private final SpringDataTenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    @Override
    public Optional<Tenant> findById(Long id) {
        return tenantRepository.findById(id)
                .map(tenantMapper::toDomain);
    }

    @Override
    public Optional<Tenant> findByDomain(String domain) {
        return tenantRepository.findByDomain(domain)
                .map(tenantMapper::toDomain);
    }

    @Override
    public Optional<Tenant> findByName(String name) {
        return tenantRepository.findByName(name)
                .map(tenantMapper::toDomain);
    }

    @Override
    public Tenant save(Tenant tenant) {
        TenantEntity tenantEntity = tenantMapper.toEntity(tenant);
        TenantEntity savedEntity = tenantRepository.save(tenantEntity);
        return tenantMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        tenantRepository.deleteById(id);
    }
}
