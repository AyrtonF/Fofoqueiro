package com.security.fofoqueiro.application.validators;

import com.security.fofoqueiro.domain.dtos.TenantCreateDTO;
import com.security.fofoqueiro.domain.exceptions.InvalidOperationException;
import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.TenantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateTenantUseCaseValidator {

    private final ITenantRepositoryPort tenantRepositoryPort;
    private final TenantMapper tenantMapper;

    public Tenant validate(TenantCreateDTO dto) {
        // Check if a tenant with the same name already exists
        Optional<Tenant> existingTenantByName = tenantRepositoryPort.findByName(dto.getName());
        if (existingTenantByName.isPresent()) {
            throw new InvalidOperationException("Tenant with name '" + dto.getName() + "' already exists.");
        }

        // Check if a tenant with the same domain already exists
        Optional<Tenant> existingTenantByDomain = tenantRepositoryPort.findByDomain(dto.getDomain());
        if (existingTenantByDomain.isPresent()) {
            throw new InvalidOperationException("Tenant with domain '" + dto.getDomain() + "' already exists.");
        }

        // Convert DTO to domain model for further processing
        return tenantMapper.toDomain(dto);
    }
}
