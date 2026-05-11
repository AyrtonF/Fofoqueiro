package com.security.fofoqueiro.application.validators;

import com.security.fofoqueiro.domain.dtos.CameraCreateDTO;
import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.Camera;
import com.security.fofoqueiro.domain.ports.IGatewayRepositoryPort;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.CameraMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCameraUseCaseValidator {

    private final ITenantRepositoryPort tenantRepositoryPort;
    private final IGatewayRepositoryPort gatewayRepositoryPort;
    private final CameraMapper cameraMapper;

    public Camera validate(CameraCreateDTO dto) {
        // Check if the tenant exists
        tenantRepositoryPort.findById(dto.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID " + dto.getTenantId() + " not found."));

        gatewayRepositoryPort.findById(dto.getGatewayId())
            .orElseThrow(() -> new EntityNotFoundException("Gateway with ID " + dto.getGatewayId() + " not found."));

        // Convert DTO to domain model for further processing
        return cameraMapper.toDomain(dto);
    }
}
