package com.security.fofoqueiro.application.services;

import com.security.fofoqueiro.domain.dtos.AuditLogResponseDTO;
import com.security.fofoqueiro.domain.models.AuditLog;
import com.security.fofoqueiro.domain.ports.IAuditLogRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.AuditLogMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final IAuditLogRepositoryPort auditLogRepositoryPort;
    private final AuditLogMapper auditLogMapper;

    public List<AuditLogResponseDTO> getAuditLogs(Long tenantId, String date, String action, String resource, Long userId, String ipAddress) {
        List<AuditLog> logs = auditLogRepositoryPort.findByTenantId(tenantId);
        return logs.stream()
                .filter(log -> date == null || date.isBlank() || matchesDate(log, date))
                .filter(log -> action == null || action.isBlank() || action.equalsIgnoreCase(log.getAction()))
                .filter(log -> resource == null || resource.isBlank() || resource.equalsIgnoreCase(log.getEntityName()))
                .filter(log -> userId == null || userId.equals(log.getUserId()))
                .filter(log -> ipAddress == null || ipAddress.isBlank() || ipAddress.equalsIgnoreCase(log.getIpAddress()))
                .map(auditLogMapper::toResponseDTO)
                .toList();
    }

    public byte[] exportAuditLogsPdf(Long tenantId, String date, String action, String resource, Long userId, String ipAddress) {
        List<AuditLogResponseDTO> logs = getAuditLogs(tenantId, date, action, resource, userId, ipAddress);

        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                contentStream.newLineAtOffset(50, 760);
                contentStream.showText("Relatorio de Auditoria");
                contentStream.newLineAtOffset(0, -22);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                contentStream.showText("Tenant: " + tenantId);
                contentStream.newLineAtOffset(0, -16);
                contentStream.showText("Total de registros: " + logs.size());
                contentStream.newLineAtOffset(0, -24);

                int limit = Math.min(logs.size(), 40);
                for (int i = 0; i < limit; i++) {
                    AuditLogResponseDTO log = logs.get(i);
                    String line = String.format("#%s | %s | %s | %s | %s | %s | %s",
                            value(log.getId()),
                            value(log.getTimestamp()),
                            escape(log.getAction()),
                            escape(log.getEntityName()),
                            escape(log.getEntityId()),
                            escape(log.getIpAddress()),
                            escape(log.getHash()));
                    contentStream.showText(truncate(line, 150));
                    contentStream.newLineAtOffset(0, -14);
                }

                if (logs.size() > limit) {
                    contentStream.showText("...");
                }

                contentStream.endText();
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to generate audit PDF", ex);
        }
    }

    private boolean matchesDate(AuditLog log, String date) {
        try {
            LocalDate selectedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDateTime start = selectedDate.atStartOfDay();
            LocalDateTime end = selectedDate.atTime(LocalTime.MAX);
            return log.getTimestamp() != null && !log.getTimestamp().isBefore(start) && !log.getTimestamp().isAfter(end);
        } catch (Exception ex) {
            return true;
        }
    }

    private String escape(Object value) {
        if (value == null) {
            return "";
        }
        String text = String.valueOf(value).replace("\"", "\"\"");
        if (text.contains(",") || text.contains("\n") || text.contains("\r")) {
            return '"' + text + '"';
        }
        return text;
    }

    private String value(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value == null ? "" : value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
