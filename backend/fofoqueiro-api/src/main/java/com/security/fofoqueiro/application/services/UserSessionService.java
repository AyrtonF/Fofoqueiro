package com.security.fofoqueiro.application.services;

import com.security.fofoqueiro.domain.dtos.UserSessionCreateDTO;
import com.security.fofoqueiro.domain.dtos.UserSessionResponseDTO;
import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.UserSession;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.domain.ports.IUserRepositoryPort;
import com.security.fofoqueiro.domain.ports.IUserSessionRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.UserSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final IUserSessionRepositoryPort repositoryPort;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final IUserRepositoryPort userRepositoryPort;
    private final UserSessionMapper mapper;

    public List<UserSessionResponseDTO> list(Long tenantId, Long userId) {
        if (userId == null) {
            return repositoryPort.findByTenantId(tenantId).stream().map(mapper::toResponseDTO).toList();
        }
        return repositoryPort.findByTenantIdAndUserId(tenantId, userId).stream().map(mapper::toResponseDTO).toList();
    }

    @Transactional
    public UserSessionResponseDTO create(UserSessionCreateDTO dto) {
        tenantRepositoryPort.findById(dto.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID " + dto.getTenantId() + " not found."));
        userRepositoryPort.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + dto.getUserId() + " not found."));
        UserSession session = mapper.toDomain(dto);
        if (session.getLastActivityAt() == null) {
            session.setLastActivityAt(LocalDateTime.now());
        }
        return mapper.toResponseDTO(repositoryPort.save(session));
    }

    @Transactional
    public void delete(Long tenantId, Long id) {
        findSession(tenantId, id);
        repositoryPort.deleteById(id);
    }

    private UserSession findSession(Long tenantId, Long id) {
        UserSession session = repositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User session with ID " + id + " not found."));
        if (!tenantId.equals(session.getTenantId())) {
            throw new EntityNotFoundException("User session with ID " + id + " not found for tenant " + tenantId + ".");
        }
        return session;
    }
}
