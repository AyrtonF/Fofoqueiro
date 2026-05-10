package com.security.fofoqueiro.application.common;

import com.security.fofoqueiro.domain.models.AuditLog;
import com.security.fofoqueiro.domain.ports.IAuditLogRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.AuditLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogSupport {

    private final IAuditLogRepositoryPort auditLogRepositoryPort;
    private final AuditLogMapper auditLogMapper;

    // Use Propagation.REQUIRES_NEW to ensure audit log is saved even if main transaction fails
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(Long tenantId, Long userId, String action,
                    String entityName, Long entityId, String ipAddress,
                    Object beforeState, Object afterState, Object metadata) {
        try {
            // Simplified details and hash for MVP. In a real app, beforeState/afterState
            // would be converted to JSON, and the hash would be calculated from the full log content.
            String details = "Action: " + action + ", Entity: " + entityName +
                             ", Entity ID: " + entityId + ", User: " + userId;
            if (beforeState != null && afterState != null) {
                details += ", Changes: " + getDiff(beforeState, afterState);
            }
            if (metadata != null) {
                details += ", Metadata: " + metadata.toString();
            }

            // Placeholder for SHA-256 hash generation for immutability
            // In a real implementation, a robust hashing mechanism would be used on the full log content
            String hash = UUID.randomUUID().toString(); // Placeholder

            AuditLog auditLog = AuditLog.builder()
                    .tenantId(tenantId)
                    .userId(userId)
                    .action(action)
                    .entityName(entityName)
                    .entityId(entityId)
                    .timestamp(LocalDateTime.now())
                    .ipAddress(ipAddress)
                    .details(details)
                    .hash(hash)
                    .build();

            auditLogRepositoryPort.save(auditLog);
            log.info("Audit Log saved: {}", auditLog);

        } catch (Exception e) {
            log.error("Failed to save audit log for action {} on entity {} (ID: {}): {}",
                      action, entityName, entityId, e.getMessage(), e);
            // Do not rethrow, audit logging should not break main application flow
        }
    }

    // Placeholder for actual diff calculation logic
    private String getDiff(Object before, Object after) {
        // Implement actual diff logic here (e.g., using a JSON diff library)
        return "Not implemented in MVP: Diff between " + before.getClass().getSimpleName() + " and " + after.getClass().getSimpleName();
    }
}
