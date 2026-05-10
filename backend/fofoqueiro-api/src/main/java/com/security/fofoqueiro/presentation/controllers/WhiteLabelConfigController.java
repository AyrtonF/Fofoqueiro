package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.common.IUseCase;
import com.security.fofoqueiro.application.common.UseCaseHandler;
import com.security.fofoqueiro.domain.dtos.WhiteLabelConfigResponseDTO;
import com.security.fofoqueiro.domain.ports.IWhiteLabelConfigRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.WhiteLabelConfigMapper;
import com.security.fofoqueiro.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant")
@RequiredArgsConstructor
public class WhiteLabelConfigController {

    private final UseCaseHandler useCaseHandler;
    private final GetWhiteLabelConfigUseCase getWhiteLabelConfigUseCase; // Define this UseCase below

    @GetMapping("/config")
    public ResponseEntity<WhiteLabelConfigResponseDTO> getTenantConfig() {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            return ResponseEntity.status(403).build();
        }
        WhiteLabelConfigResponseDTO result = getWhiteLabelConfigUseCase.execute(tenantId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}

// Internal UseCase for fetching WhiteLabelConfig
// This UseCase is defined here for simplicity, but in a real project, it would be in the application.use_cases package
@Service
@RequiredArgsConstructor
class GetWhiteLabelConfigUseCase implements IUseCase<Long, WhiteLabelConfigResponseDTO> {

    private final IWhiteLabelConfigRepositoryPort whiteLabelConfigRepositoryPort;
    private final WhiteLabelConfigMapper whiteLabelConfigMapper;

    @Override
    public WhiteLabelConfigResponseDTO execute(Long tenantId) {
        return whiteLabelConfigRepositoryPort.findByTenantId(tenantId)
                .map(whiteLabelConfigMapper::toResponseDTO)
                .orElse(null); // Return null if not found, UseCaseHandler will handle 404
    }
}
