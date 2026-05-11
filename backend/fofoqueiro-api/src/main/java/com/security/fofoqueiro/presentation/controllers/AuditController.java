package com.security.fofoqueiro.presentation.controllers;

import com.security.fofoqueiro.application.common.AuditLogSupport;
import com.security.fofoqueiro.application.services.AuditLogService;
import com.security.fofoqueiro.domain.dtos.AuditLogCreateDTO;
import com.security.fofoqueiro.domain.dtos.AuditLogResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/audit", "/api/v1/audit-logs"})
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogSupport auditLogSupport;
    private final AuditLogService auditLogService;

    @PostMapping("/log")
    public ResponseEntity<Void> logAudit(@RequestBody AuditLogCreateDTO dto) {
        // In a real scenario, beforeState, afterState, metadata would be passed as part of the DTO
        // For MVP, using simplified logging
        auditLogSupport.log(dto.getTenantId(), dto.getUserId(), dto.getAction(),
                            dto.getEntityName(), dto.getEntityId(), dto.getIpAddress(),
                            null, null, dto.getDetails()); // details from DTO used as metadata
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponseDTO>> listAuditLogs(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String resource,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String ipAddress) {
        return ResponseEntity.ok(auditLogService.getAuditLogs(tenantId, date, action, resource, userId, ipAddress));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAuditLogs(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String resource,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String ipAddress) {
        byte[] pdf = auditLogService.exportAuditLogsPdf(tenantId, date, action, resource, userId, ipAddress);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audit-logs.pdf")
            .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
            .body(pdf);
    }
}
