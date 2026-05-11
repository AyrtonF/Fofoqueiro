package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.services.AlertEventService;
import com.security.fofoqueiro.domain.dtos.AlertEventCreateDTO;
import com.security.fofoqueiro.domain.dtos.AlertEventResponseDTO;
import com.security.fofoqueiro.domain.dtos.CameraHealthDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alert-events")
@RequiredArgsConstructor
public class AlertEventController {
    private final AlertEventService service;

    @GetMapping
    public ResponseEntity<List<AlertEventResponseDTO>> list(@RequestHeader("X-Tenant-Id") Long tenantId,
                                                            @RequestParam(required = false) Long cameraId) {
        return ResponseEntity.ok(service.list(tenantId, cameraId));
    }

    @PostMapping
    public ResponseEntity<AlertEventResponseDTO> create(@Valid @RequestBody AlertEventCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<AlertEventResponseDTO> acknowledge(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        return ResponseEntity.ok(service.acknowledge(tenantId, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        service.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health-summary")
    public ResponseEntity<List<CameraHealthDTO>> healthSummary(@RequestHeader("X-Tenant-Id") Long tenantId) {
        return ResponseEntity.ok(service.summarizeHealth(tenantId));
    }
}
