package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.services.UserSessionService;
import com.security.fofoqueiro.domain.dtos.UserSessionCreateDTO;
import com.security.fofoqueiro.domain.dtos.UserSessionResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-sessions")
@RequiredArgsConstructor
public class UserSessionController {
    private final UserSessionService service;

    @GetMapping
    public ResponseEntity<List<UserSessionResponseDTO>> list(@RequestHeader("X-Tenant-Id") Long tenantId,
                                                             @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(service.list(tenantId, userId));
    }

    @PostMapping
    public ResponseEntity<UserSessionResponseDTO> create(@Valid @RequestBody UserSessionCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        service.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}
