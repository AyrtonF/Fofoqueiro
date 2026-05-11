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

    @PostMapping
    public ResponseEntity<HealthMetricResponseDTO> create(@Valid @RequestBody HealthMetricCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}
