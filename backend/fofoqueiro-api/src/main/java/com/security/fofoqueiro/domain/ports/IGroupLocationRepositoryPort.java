package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.GroupLocation;

import java.util.List;
import java.util.Optional;

public interface IGroupLocationRepositoryPort {
    Optional<GroupLocation> findById(Long id);
    GroupLocation save(GroupLocation groupLocation);
    void deleteById(Long id);
    List<GroupLocation> findByTenantId(Long tenantId);
}
