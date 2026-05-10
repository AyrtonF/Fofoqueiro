package com.security.fofoqueiro.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("DEBUG: Incoming request " + request.getMethod() + " " + request.getRequestURI());
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
                System.out.println("DEBUG: Header: " + header + " = " + request.getHeader(header));
            }
        }
        try {
            String tenantIdHeader = request.getHeader(TENANT_HEADER);
            if (tenantIdHeader != null && !tenantIdHeader.isEmpty()) {
                TenantContext.setTenantId(Long.valueOf(tenantIdHeader));
            } else {
                System.out.println("No " + TENANT_HEADER + " header found for request: " + request.getRequestURI());
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
