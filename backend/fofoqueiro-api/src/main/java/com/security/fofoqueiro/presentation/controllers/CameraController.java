package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.common.UseCaseHandler;
import com.security.fofoqueiro.application.use_cases.camera.CreateCameraUseCase;
import com.security.fofoqueiro.domain.dtos.CameraCreateDTO;
import com.security.fofoqueiro.domain.dtos.CameraResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cameras")
@RequiredArgsConstructor
public class CameraController {

    private final UseCaseHandler useCaseHandler;
    private final CreateCameraUseCase createCameraUseCase;

    @PostMapping
    public ResponseEntity<CameraResponseDTO> createCamera(@Valid @RequestBody CameraCreateDTO dto) {
        return useCaseHandler.execute(createCameraUseCase, dto);
    }

    // Placeholder for GET /api/v1/cameras/health
    // This would likely involve a UseCase to check camera status, but for MVP, it's a simple placeholder
    @GetMapping("/health")
    public ResponseEntity<String> getCameraHealth() {
        // In a real application, this would call a use case to get actual health status
        return ResponseEntity.ok("Camera health status: OK (placeholder)");
    }
}
