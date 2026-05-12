package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.common.UseCaseHandler;
import com.security.fofoqueiro.application.use_cases.tenant.CreateTenantUseCase;
import com.security.fofoqueiro.domain.dtos.TenantCreateDTO;
import com.security.fofoqueiro.domain.dtos.TenantResponseDTO;
import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.TenantMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final UseCaseHandler useCaseHandler;
    private final CreateTenantUseCase createTenantUseCase;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final TenantMapper tenantMapper;

    @GetMapping
    public ResponseEntity<List<TenantResponseDTO>> listTenants() {
        return ResponseEntity.ok(tenantRepositoryPort.findAll().stream().map(tenantMapper::toResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> getTenant(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(findTenant(id)));
    }

    @PostMapping
    public ResponseEntity<TenantResponseDTO> createTenant(@Valid @RequestBody TenantCreateDTO dto) {
        return useCaseHandler.execute(createTenantUseCase, dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> updateTenant(@PathVariable Long id, @Valid @RequestBody TenantUpdateRequest request) {
        Tenant tenant = findTenant(id);
        tenant.setName(request.name());
        tenant.setDomain(request.domain());
        tenant.setIsActive(request.isActive());
        return ResponseEntity.ok(tenantMapper.toResponseDTO(tenantRepositoryPort.save(tenant)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        findTenant(id);
        tenantRepositoryPort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Tenant findTenant(Long id) {
        return tenantRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID " + id + " not found."));
    }

    private static TenantResponseDTO toResponse(Tenant tenant) {
        return TenantResponseDTO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .domain(tenant.getDomain())
                .isActive(tenant.getIsActive())
                .createdAt(tenant.getCreatedAt())
                .updatedAt(tenant.getUpdatedAt())
                .build();
    }

    public record TenantUpdateRequest(@NotBlank String name, @NotBlank String domain, @NotNull Boolean isActive) {}
}
