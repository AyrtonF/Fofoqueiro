package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.use_cases.auth.LoginUseCase;
import com.security.fofoqueiro.application.use_cases.auth.VerifyMfaUseCase;
import com.security.fofoqueiro.domain.dtos.LoginRequestDTO;
import com.security.fofoqueiro.domain.dtos.MfaVerifyRequestDTO;
import com.security.fofoqueiro.domain.dtos.AuthResponseDTO;
import com.security.fofoqueiro.domain.dtos.RefreshTokenRequestDTO;
import com.security.fofoqueiro.infrastructure.security.AuthTokenService;
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
    private final AuthTokenService authTokenService;

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
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(authTokenService.refresh(request.getRefreshToken()));
    }
}
