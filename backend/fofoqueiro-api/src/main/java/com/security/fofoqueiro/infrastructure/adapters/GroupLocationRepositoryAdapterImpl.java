package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.GroupLocation;
import com.security.fofoqueiro.domain.ports.IGroupLocationRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.GroupLocationEntity;
import com.security.fofoqueiro.infrastructure.mappers.GroupLocationMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataGroupLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupLocationRepositoryAdapterImpl implements IGroupLocationRepositoryPort {
    private final SpringDataGroupLocationRepository repository;
    private final GroupLocationMapper mapper;

    @Override
    public Optional<GroupLocation> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public GroupLocation save(GroupLocation groupLocation) {
        GroupLocationEntity saved = repository.save(mapper.toEntity(groupLocation));
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<GroupLocation> findByTenantId(Long tenantId) {
        return repository.findByTenantId(tenantId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
