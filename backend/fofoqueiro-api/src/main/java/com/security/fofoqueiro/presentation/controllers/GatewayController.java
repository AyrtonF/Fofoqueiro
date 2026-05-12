package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.Gateway;
import com.security.fofoqueiro.domain.ports.IGatewayRepositoryPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gateways")
@RequiredArgsConstructor
public class GatewayController {

    private final IGatewayRepositoryPort gatewayRepositoryPort;

    @GetMapping
    public ResponseEntity<List<GatewayResponse>> list(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @RequestParam(required = false) String status) {
        List<Gateway> gateways = status == null || status.isBlank()
                ? gatewayRepositoryPort.findByTenantId(tenantId)
                : gatewayRepositoryPort.findByTenantIdAndStatus(tenantId, status);
        return ResponseEntity.ok(gateways.stream().map(GatewayController::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GatewayResponse> getById(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long id) {
        Gateway gateway = findGateway(tenantId, id);
        return ResponseEntity.ok(toResponse(gateway));
    }

    @PostMapping
    public ResponseEntity<GatewayResponse> create(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @Valid @RequestBody GatewayRequest request) {
        Gateway saved = gatewayRepositoryPort.save(Gateway.builder()
                .tenantId(tenantId)
                .name(request.name())
                .ipAddress(request.ipAddress())
                .location(request.location())
                .status(request.status())
                .isActive(request.isActive())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GatewayResponse> update(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long id,
            @Valid @RequestBody GatewayRequest request) {
        Gateway existing = findGateway(tenantId, id);
        existing.setTenantId(tenantId);
        existing.setName(request.name());
        existing.setIpAddress(request.ipAddress());
        existing.setLocation(request.location());
        existing.setStatus(request.status());
        existing.setIsActive(request.isActive());
        return ResponseEntity.ok(toResponse(gatewayRepositoryPort.save(existing)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long id) {
        findGateway(tenantId, id);
        gatewayRepositoryPort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Gateway findGateway(Long tenantId, Long id) {
        Gateway gateway = gatewayRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gateway with ID " + id + " not found."));
        if (!tenantId.equals(gateway.getTenantId())) {
            throw new EntityNotFoundException("Gateway with ID " + id + " not found for tenant " + tenantId + ".");
        }
        return gateway;
    }

    private static GatewayResponse toResponse(Gateway gateway) {
        return new GatewayResponse(
                gateway.getId(),
                gateway.getTenantId(),
                gateway.getName(),
                gateway.getIpAddress(),
                gateway.getLocation(),
                gateway.getStatus(),
                gateway.getIsActive(),
                gateway.getCreatedAt(),
                gateway.getUpdatedAt()
        );
    }

    public record GatewayRequest(
            @NotBlank String name,
            @NotBlank String ipAddress,
            String location,
            @NotBlank String status,
            @NotNull Boolean isActive
    ) {}

    public record GatewayResponse(
            Long id,
            Long tenantId,
            String name,
            String ipAddress,
            String location,
            String status,
            Boolean isActive,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
