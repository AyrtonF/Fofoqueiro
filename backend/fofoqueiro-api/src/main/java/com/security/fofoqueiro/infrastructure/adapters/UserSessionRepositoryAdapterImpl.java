package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.UserSession;
import com.security.fofoqueiro.domain.ports.IUserSessionRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.UserSessionEntity;
import com.security.fofoqueiro.infrastructure.mappers.UserSessionMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataUserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserSessionRepositoryAdapterImpl implements IUserSessionRepositoryPort {
    private final SpringDataUserSessionRepository repository;
    private final UserSessionMapper mapper;

    @Override
    public Optional<UserSession> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<UserSession> findByTokenId(String tokenId) {
        return repository.findByTokenId(tokenId).map(mapper::toDomain);
    }

    @Override
    public UserSession save(UserSession session) {
        UserSessionEntity saved = repository.save(mapper.toEntity(session));
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserSession> findByTenantId(Long tenantId) {
        return repository.findByTenantId(tenantId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<UserSession> findByTenantIdAndUserId(Long tenantId, Long userId) {
        return repository.findByTenantIdAndUserId(tenantId, userId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
