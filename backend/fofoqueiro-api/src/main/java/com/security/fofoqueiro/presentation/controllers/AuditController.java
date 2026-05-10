package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.common.AuditLogSupport;
import com.security.fofoqueiro.domain.dtos.AuditLogCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogSupport auditLogSupport;

    @PostMapping("/log")
    public ResponseEntity<Void> logAudit(@RequestBody AuditLogCreateDTO dto) {
        // In a real scenario, beforeState, afterState, metadata would be passed as part of the DTO
        // For MVP, using simplified logging
        auditLogSupport.log(dto.getTenantId(), dto.getUserId(), dto.getAction(),
                            dto.getEntityName(), dto.getEntityId(), dto.getIpAddress(),
                            null, null, dto.getDetails()); // details from DTO used as metadata
        return ResponseEntity.ok().build();
    }
}
