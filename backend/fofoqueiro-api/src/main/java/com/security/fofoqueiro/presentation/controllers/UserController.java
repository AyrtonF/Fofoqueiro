package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.User;
import com.security.fofoqueiro.domain.ports.IUserRepositoryPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UserResponse>> list(@RequestHeader("X-Tenant-Id") Long tenantId) {
        return ResponseEntity.ok(userRepositoryPort.findByTenantId(tenantId).stream().map(UserController::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        return ResponseEntity.ok(toResponse(findUser(tenantId, id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestHeader("X-Tenant-Id") Long tenantId, @Valid @RequestBody UserRequest request) {
        User saved = userRepositoryPort.save(User.builder()
                .tenantId(tenantId)
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .isActive(request.isActive())
                .mfaEnabled(request.mfaEnabled())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        User existing = findUser(tenantId, id);
        existing.setTenantId(tenantId);
        existing.setEmail(request.email());
        existing.setPassword(passwordEncoder.encode(request.password()));
        existing.setFirstName(request.firstName());
        existing.setLastName(request.lastName());
        existing.setIsActive(request.isActive());
        existing.setMfaEnabled(request.mfaEnabled());
        return ResponseEntity.ok(toResponse(userRepositoryPort.save(existing)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long id) {
        findUser(tenantId, id);
        userRepositoryPort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private User findUser(Long tenantId, Long id) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found."));
        if (!tenantId.equals(user.getTenantId())) {
            throw new EntityNotFoundException("User with ID " + id + " not found for tenant " + tenantId + ".");
        }
        return user;
    }

    private static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getTenantId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsActive(),
                user.getMfaEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public record UserRequest(
            @Email @NotBlank String email,
            @NotBlank String password,
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotNull Boolean isActive,
            @NotNull Boolean mfaEnabled
    ) {}

    public record UserResponse(
            Long id,
            Long tenantId,
            String email,
            String firstName,
            String lastName,
            Boolean isActive,
            Boolean mfaEnabled,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
