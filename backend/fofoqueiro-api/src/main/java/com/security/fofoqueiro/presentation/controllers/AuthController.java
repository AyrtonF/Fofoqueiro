package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.use_cases.auth.LoginUseCase;
import com.security.fofoqueiro.application.use_cases.auth.VerifyMfaUseCase;
import com.security.fofoqueiro.domain.dtos.LoginRequestDTO;
import com.security.fofoqueiro.domain.dtos.MfaVerifyRequestDTO;
import com.security.fofoqueiro.domain.dtos.AuthResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final VerifyMfaUseCase verifyMfaUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = loginUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mfa/verify")
    public ResponseEntity<AuthResponseDTO> verifyMfa(@Valid @RequestBody MfaVerifyRequestDTO request) {
        AuthResponseDTO response = verifyMfaUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Logout é geralmente stateless em JWT, apenas retorna sucesso
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestHeader("Authorization") String refreshToken) {
        // Implementar refresh token
        return ResponseEntity.ok().build();
    }
}
