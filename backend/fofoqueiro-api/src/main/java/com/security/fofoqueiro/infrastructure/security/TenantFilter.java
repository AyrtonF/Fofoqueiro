package com.security.fofoqueiro.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-Id";
    private static final String TENANT_CONFIG_PATH = "/api/v1/tenant/config";

    private final Long defaultTenantId;

    public TenantFilter(@Value("${app.tenant.default-id:1}") Long defaultTenantId) {
        this.defaultTenantId = defaultTenantId;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tenantIdHeader = request.getHeader(TENANT_HEADER);
            if (tenantIdHeader != null && !tenantIdHeader.isBlank()) {
                TenantContext.setTenantId(Long.valueOf(tenantIdHeader));
            } else if (isTenantConfigRequest(request)) {
                TenantContext.setTenantId(defaultTenantId);
            }

            filterChain.doFilter(request, response);
        } catch (NumberFormatException ignored) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid X-Tenant-Id header");
        } finally {
            TenantContext.clear();
        }
    }

    private boolean isTenantConfigRequest(HttpServletRequest request) {
        return TENANT_CONFIG_PATH.equals(request.getRequestURI());
    }
}
