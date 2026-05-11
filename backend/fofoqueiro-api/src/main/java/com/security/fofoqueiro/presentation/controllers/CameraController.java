package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.common.UseCaseHandler;
import com.security.fofoqueiro.application.services.CameraManagementService;
import com.security.fofoqueiro.application.use_cases.camera.CreateCameraUseCase;
import com.security.fofoqueiro.domain.dtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cameras")
@RequiredArgsConstructor
public class CameraController {

    private final UseCaseHandler useCaseHandler;
    private final CreateCameraUseCase createCameraUseCase;
    private final CameraManagementService cameraManagementService;

    @PostMapping
    public ResponseEntity<CameraResponseDTO> createCamera(@Valid @RequestBody CameraCreateDTO dto) {
        return useCaseHandler.execute(createCameraUseCase, dto);
    }

    @GetMapping
    public ResponseEntity<List<CameraResponseDTO>> listCameras(@RequestHeader("X-Tenant-Id") Long tenantId) {
        return ResponseEntity.ok(cameraManagementService.getCameras(tenantId));
    }

    @GetMapping("/{cameraId}")
    public ResponseEntity<CameraResponseDTO> getCamera(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long cameraId) {
        return ResponseEntity.ok(cameraManagementService.getCamera(tenantId, cameraId));
    }

    @PutMapping("/{cameraId}")
    public ResponseEntity<CameraResponseDTO> updateCamera(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long cameraId,
            @Valid @RequestBody CameraUpdateDTO dto) {
        return ResponseEntity.ok(cameraManagementService.updateCamera(tenantId, cameraId, dto));
    }

    @DeleteMapping("/{cameraId}")
    public ResponseEntity<Void> deleteCamera(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long cameraId) {
        cameraManagementService.deleteCamera(tenantId, cameraId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<List<CameraHealthDTO>> getCameraHealth(@RequestHeader("X-Tenant-Id") Long tenantId) {
        return ResponseEntity.ok(cameraManagementService.getHealth(tenantId));
    }

    @PostMapping("/test-connection")
    public ResponseEntity<TestConnectionResponseDTO> testConnection(@RequestHeader("X-Tenant-Id") Long tenantId, @RequestBody CameraTestConnectionRequestDTO dto) {
        return ResponseEntity.ok(cameraManagementService.testConnection(tenantId, dto));
    }

    @PostMapping("/stream/{cameraId}")
    public ResponseEntity<CameraStreamResponseDTO> streamCamera(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long cameraId,
            @RequestBody(required = false) Map<String, Object> signalData) {
        return ResponseEntity.ok(cameraManagementService.getStream(tenantId, cameraId, signalData == null ? Map.of() : signalData));
    }

    @GetMapping("/{cameraId}/privacy-masks")
    public ResponseEntity<List<PrivacyMaskResponseDTO>> getPrivacyMasks(@RequestHeader("X-Tenant-Id") Long tenantId, @PathVariable Long cameraId) {
        return ResponseEntity.ok(cameraManagementService.getPrivacyMasks(tenantId, cameraId));
    }

    @PutMapping("/{cameraId}/privacy-masks")
    public ResponseEntity<List<PrivacyMaskResponseDTO>> updatePrivacyMasks(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long cameraId,
            @RequestBody List<PrivacyMaskUpsertDTO> masks) {
        return ResponseEntity.ok(cameraManagementService.updatePrivacyMasks(tenantId, cameraId, masks));
    }

    @GetMapping("/{cameraId}/recordings")
    public ResponseEntity<List<RecordingResponseDTO>> getRecordings(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @PathVariable Long cameraId,
            @RequestParam(required = false) String date) {
        return ResponseEntity.ok(cameraManagementService.getRecordings(tenantId, cameraId, date));
    }
}
