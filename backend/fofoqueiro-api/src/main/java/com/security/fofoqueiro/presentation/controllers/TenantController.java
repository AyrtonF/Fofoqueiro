package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.common.UseCaseHandler;
import com.security.fofoqueiro.application.use_cases.tenant.CreateTenantUseCase;
import com.security.fofoqueiro.domain.dtos.TenantCreateDTO;
import com.security.fofoqueiro.domain.dtos.TenantResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final UseCaseHandler useCaseHandler;
    private final CreateTenantUseCase createTenantUseCase;

    @PostMapping
    public ResponseEntity<TenantResponseDTO> createTenant(@Valid @RequestBody TenantCreateDTO dto) {
        return useCaseHandler.execute(createTenantUseCase, dto);
    }
}
