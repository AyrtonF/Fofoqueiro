package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.WhiteLabelConfig;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.domain.ports.IWhiteLabelConfigRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.WhiteLabelConfigMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/white-label-configs")
@RequiredArgsConstructor
public class WhiteLabelConfigAdminController {

    private final IWhiteLabelConfigRepositoryPort repositoryPort;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final WhiteLabelConfigMapper mapper;

    @GetMapping
    public ResponseEntity<List<com.security.fofoqueiro.domain.dtos.WhiteLabelConfigResponseDTO>> list() {
        return ResponseEntity.ok(repositoryPort.findAll().stream().map(mapper::toResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.security.fofoqueiro.domain.dtos.WhiteLabelConfigResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponseDTO(findConfigById(id)));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<com.security.fofoqueiro.domain.dtos.WhiteLabelConfigResponseDTO> getByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(repositoryPort.findByTenantId(tenantId)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("White label config for tenant " + tenantId + " not found.")));
    }

    @PostMapping
    public ResponseEntity<com.security.fofoqueiro.domain.dtos.WhiteLabelConfigResponseDTO> create(@Valid @RequestBody WhiteLabelConfigRequest request) {
        tenantRepositoryPort.findById(request.tenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID " + request.tenantId() + " not found."));
        repositoryPort.findByTenantId(request.tenantId()).ifPresent(existing -> {
            throw new IllegalStateException("White label config already exists for tenant " + request.tenantId() + ". Use PUT instead.");
        });
        WhiteLabelConfig saved = repositoryPort.save(toDomain(null, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponseDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.security.fofoqueiro.domain.dtos.WhiteLabelConfigResponseDTO> update(@PathVariable Long id, @Valid @RequestBody WhiteLabelConfigRequest request) {
        WhiteLabelConfig existing = findConfigById(id);
        tenantRepositoryPort.findById(request.tenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID " + request.tenantId() + " not found."));
        existing.setTenantId(request.tenantId());
        existing.setLogoUrl(request.logoUrl());
        existing.setPrimaryColor(request.primaryColor());
        existing.setSecondaryColor(request.secondaryColor());
        existing.setFaviconUrl(request.faviconUrl());
        return ResponseEntity.ok(mapper.toResponseDTO(repositoryPort.save(existing)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        findConfigById(id);
        repositoryPort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private WhiteLabelConfig findConfigById(Long id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("White label config with ID " + id + " not found."));
    }

    private WhiteLabelConfig toDomain(Long id, WhiteLabelConfigRequest request) {
        return WhiteLabelConfig.builder()
                .id(id)
                .tenantId(request.tenantId())
                .logoUrl(request.logoUrl())
                .primaryColor(request.primaryColor())
                .secondaryColor(request.secondaryColor())
                .faviconUrl(request.faviconUrl())
                .build();
    }

    public record WhiteLabelConfigRequest(
            @NotNull Long tenantId,
            String logoUrl,
            String primaryColor,
            String secondaryColor,
            String faviconUrl
    ) {}
}