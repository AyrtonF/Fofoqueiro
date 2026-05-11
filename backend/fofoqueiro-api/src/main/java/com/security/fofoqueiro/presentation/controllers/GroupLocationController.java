package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.services.GroupLocationService;
import com.security.fofoqueiro.domain.dtos.GroupLocationCreateDTO;
import com.security.fofoqueiro.domain.dtos.GroupLocationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group-locations")
@RequiredArgsConstructor
public class GroupLocationController {
    private final GroupLocationService service;

    @GetMapping
    public ResponseEntity<List<GroupLocationResponseDTO>> list(@RequestHeader("X-Tenant-Id") Long tenantId) {
        return ResponseEntity.ok(service.list(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupLocationResponseDTO> get(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        return ResponseEntity.ok(service.get(tenantId, id));
    }

    @PostMapping
    public ResponseEntity<GroupLocationResponseDTO> create(@Valid @RequestBody GroupLocationCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupLocationResponseDTO> update(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id, @RequestBody GroupLocationCreateDTO dto) {
        return ResponseEntity.ok(service.update(tenantId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        service.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}
