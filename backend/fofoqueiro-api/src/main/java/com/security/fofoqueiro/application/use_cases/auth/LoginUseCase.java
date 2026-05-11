package com.security.fofoqueiro.application.use_cases.auth;

import com.security.fofoqueiro.application.common.IUseCase;
import com.security.fofoqueiro.domain.dtos.AuthResponseDTO;
import com.security.fofoqueiro.domain.dtos.LoginRequestDTO;
import com.security.fofoqueiro.domain.dtos.UserAuthDTO;
import com.security.fofoqueiro.domain.exceptions.AuthException;
import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.domain.models.User;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.domain.ports.IUserRepositoryPort;
import com.security.fofoqueiro.infrastructure.security.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginUseCase implements IUseCase<LoginRequestDTO, AuthResponseDTO> {

    private final IUserRepositoryPort userRepositoryPort;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;

    @Override
    public AuthResponseDTO execute(LoginRequestDTO input) {
        User user = userRepositoryPort.findByEmail(input.getEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (user.getIsActive() == null || !user.getIsActive()) {
            throw new AuthException("User is inactive");
        }

        if (user.getPassword() == null || !passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid credentials");
        }

        Tenant tenant = tenantRepositoryPort.findById(user.getTenantId())
                .orElseThrow(() -> new AuthException("Tenant not found"));

        UserAuthDTO authUser = toAuthUser(user);

        if (Boolean.TRUE.equals(user.getMfaEnabled())) {
            String mfaToken = authTokenService.createMfaChallengeToken(user, tenant);
            return AuthResponseDTO.builder()
                    .mfaRequired(true)
                    .mfaToken(mfaToken)
                    .user(authUser)
                    .tenantId(String.valueOf(tenant.getId()))
                    .build();
        }

        return authTokenService.createAuthResponse(user, tenant, authUser, false, null);
    }

    private UserAuthDTO toAuthUser(User user) {
        return UserAuthDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name((user.getFirstName() + " " + user.getLastName()).trim())
                .role("ADMIN")
                .build();
    }
}
