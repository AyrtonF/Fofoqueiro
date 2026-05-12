package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.Role;
import com.security.fofoqueiro.domain.ports.IRoleRepositoryPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleRepositoryPort roleRepositoryPort;

    @GetMapping
    public ResponseEntity<List<RoleResponse>> list() {
        return ResponseEntity.ok(roleRepositoryPort.findAll().stream().map(RoleController::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(findRole(id)));
    }

    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        Role saved = roleRepositoryPort.save(Role.builder()
                .name(request.name())
                .description(request.description())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        Role existing = findRole(id);
        existing.setName(request.name());
        existing.setDescription(request.description());
        return ResponseEntity.ok(toResponse(roleRepositoryPort.save(existing)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        findRole(id);
        roleRepositoryPort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Role findRole(Long id) {
        return roleRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with ID " + id + " not found."));
    }

    private static RoleResponse toResponse(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(), role.getCreatedAt(), role.getUpdatedAt());
    }

    public record RoleRequest(@NotBlank String name, String description) {}

    public record RoleResponse(Long id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {}
}
