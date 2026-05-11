package com.security.fofoqueiro.infrastructure.config;

import com.security.fofoqueiro.domain.models.Role;
import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.domain.models.User;
import com.security.fofoqueiro.domain.models.WhiteLabelConfig;
import com.security.fofoqueiro.domain.ports.IRoleRepositoryPort;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.domain.ports.IUserRepositoryPort;
import com.security.fofoqueiro.domain.ports.IWhiteLabelConfigRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootstrapDataSeeder implements ApplicationRunner {

    private final ITenantRepositoryPort tenantRepositoryPort;
    private final IWhiteLabelConfigRepositoryPort whiteLabelConfigRepositoryPort;
    private final IUserRepositoryPort userRepositoryPort;
    private final IRoleRepositoryPort roleRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.tenant.name:Fofoqueiro}")
    private String tenantName;

    @Value("${app.bootstrap.tenant.domain:localhost}")
    private String tenantDomain;

    @Value("${app.bootstrap.white-label.logo-url:/images/logo.png}")
    private String logoUrl;

    @Value("${app.bootstrap.white-label.primary-color:#0f172a}")
    private String primaryColor;

    @Value("${app.bootstrap.white-label.secondary-color:#3b82f6}")
    private String secondaryColor;

    @Value("${app.bootstrap.white-label.favicon-url:/images/favicon.ico}")
    private String faviconUrl;

    @Value("${app.bootstrap.admin.email:admin@fofoqueiro.local}")
    private String adminEmail;

    @Value("${app.bootstrap.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.bootstrap.admin.first-name:Admin}")
    private String adminFirstName;

    @Value("${app.bootstrap.admin.last-name:Sistema}")
    private String adminLastName;

    @Value("${app.bootstrap.admin.role:ADMIN}")
    private String adminRole;

    @Value("${app.bootstrap.admin.mfa-enabled:false}")
    private Boolean adminMfaEnabled;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Tenant tenant = tenantRepositoryPort.findByDomain(tenantDomain)
                .orElseGet(() -> tenantRepositoryPort.save(Tenant.builder()
                        .name(tenantName)
                        .domain(tenantDomain)
                        .isActive(true)
                        .build()));

        roleRepositoryPort.findByName(adminRole)
                .orElseGet(() -> roleRepositoryPort.save(Role.builder()
                        .name(adminRole)
                        .description("Bootstrap administrator role")
                        .build()));

        whiteLabelConfigRepositoryPort.findByTenantId(tenant.getId())
                .orElseGet(() -> whiteLabelConfigRepositoryPort.save(WhiteLabelConfig.builder()
                        .tenantId(tenant.getId())
                        .logoUrl(logoUrl)
                        .primaryColor(primaryColor)
                        .secondaryColor(secondaryColor)
                        .faviconUrl(faviconUrl)
                        .build()));

        userRepositoryPort.findByEmail(adminEmail)
                .orElseGet(() -> userRepositoryPort.save(User.builder()
                        .tenantId(tenant.getId())
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .firstName(adminFirstName)
                        .lastName(adminLastName)
                        .isActive(true)
                        .mfaEnabled(adminMfaEnabled)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()));

        log.info("Bootstrap data ensured for tenant '{}' and admin '{}'", tenantDomain, adminEmail);
    }
}
