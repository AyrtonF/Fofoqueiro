package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.UserSession;

import java.util.List;
import java.util.Optional;

public interface IUserSessionRepositoryPort {
    Optional<UserSession> findById(Long id);
    Optional<UserSession> findByTokenId(String tokenId);
    UserSession save(UserSession session);
    void deleteById(Long id);
    List<UserSession> findByTenantId(Long tenantId);
    List<UserSession> findByTenantIdAndUserId(Long tenantId, Long userId);
}
