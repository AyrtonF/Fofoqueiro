package com.security.fofoqueiro.application.use_cases.tenant;

import com.security.fofoqueiro.application.common.IUseCase;
import com.security.fofoqueiro.application.validators.CreateTenantUseCaseValidator;
import com.security.fofoqueiro.domain.dtos.TenantCreateDTO;
import com.security.fofoqueiro.domain.dtos.TenantResponseDTO;
import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.TenantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTenantUseCase implements IUseCase<TenantCreateDTO, TenantResponseDTO> {

    private final CreateTenantUseCaseValidator validator;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final TenantMapper tenantMapper;

    @Override
    @Transactional
    public TenantResponseDTO execute(TenantCreateDTO input) {
        // 1. Validate and convert DTO to domain model
        Tenant tenantToSave = validator.validate(input);

        // 2. Save the tenant
        Tenant savedTenant = tenantRepositoryPort.save(tenantToSave);

        // 3. Convert saved domain model to response DTO
        return tenantMapper.toResponseDTO(savedTenant);
    }
}
