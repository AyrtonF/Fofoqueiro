package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.services.HealthMetricService;
import com.security.fofoqueiro.domain.dtos.HealthMetricCreateDTO;
import com.security.fofoqueiro.domain.dtos.HealthMetricResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/health-metrics")
@RequiredArgsConstructor
public class HealthMetricController {
    private final HealthMetricService service;

    @GetMapping
    public ResponseEntity<List<HealthMetricResponseDTO>> list(@RequestHeader("X-Tenant-Id") Long tenantId,
                                                              @RequestParam(required = false) Long cameraId) {
        return ResponseEntity.ok(service.list(tenantId, cameraId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthMetricResponseDTO> getById(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(tenantId, id));
    }

    @PostMapping
    public ResponseEntity<HealthMetricResponseDTO> create(@Valid @RequestBody HealthMetricCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthMetricResponseDTO> update(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long id,
            @Valid @RequestBody HealthMetricCreateDTO dto) {
        return ResponseEntity.ok(service.update(tenantId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        service.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}
