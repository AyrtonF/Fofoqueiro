package com.security.fofoqueiro.application.use_cases.auth;

import com.security.fofoqueiro.application.common.IUseCase;
import com.security.fofoqueiro.domain.dtos.AuthResponseDTO;
import com.security.fofoqueiro.domain.dtos.MfaVerifyRequestDTO;
import com.security.fofoqueiro.domain.dtos.UserAuthDTO;
import com.security.fofoqueiro.domain.exceptions.AuthException;
import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.domain.models.User;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.domain.ports.IUserRepositoryPort;
import com.security.fofoqueiro.infrastructure.security.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerifyMfaUseCase implements IUseCase<MfaVerifyRequestDTO, AuthResponseDTO> {

    private final IUserRepositoryPort userRepositoryPort;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final AuthTokenService authTokenService;

    @Override
    public AuthResponseDTO execute(MfaVerifyRequestDTO input) {
        AuthTokenService.MfaChallenge challenge = authTokenService.validateMfaChallenge(
                input.getMfaToken(),
                input.getCode()
        );

        User user = userRepositoryPort.findById(challenge.userId())
                .orElseThrow(() -> new AuthException("User not found"));
        Tenant tenant = tenantRepositoryPort.findById(user.getTenantId())
                .orElseThrow(() -> new AuthException("Tenant not found"));

        UserAuthDTO authUser = UserAuthDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name((user.getFirstName() + " " + user.getLastName()).trim())
                .role("ADMIN")
                .build();

        return authTokenService.createAuthResponse(user, tenant, authUser, false, null);
    }
}
