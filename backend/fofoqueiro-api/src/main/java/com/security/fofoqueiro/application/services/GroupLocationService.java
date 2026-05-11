package com.security.fofoqueiro.application.services;

import com.security.fofoqueiro.domain.dtos.GroupLocationCreateDTO;
import com.security.fofoqueiro.domain.dtos.GroupLocationResponseDTO;
import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.models.GroupLocation;
import com.security.fofoqueiro.domain.ports.IGroupLocationRepositoryPort;
import com.security.fofoqueiro.domain.ports.ITenantRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.GroupLocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupLocationService {
    private final IGroupLocationRepositoryPort repositoryPort;
    private final ITenantRepositoryPort tenantRepositoryPort;
    private final GroupLocationMapper mapper;

    public List<GroupLocationResponseDTO> list(Long tenantId) {
        return repositoryPort.findByTenantId(tenantId).stream().map(mapper::toResponseDTO).toList();
    }

    public GroupLocationResponseDTO get(Long tenantId, Long id) {
        return mapper.toResponseDTO(findGroupLocation(tenantId, id));
    }

    @Transactional
    public GroupLocationResponseDTO create(GroupLocationCreateDTO dto) {
        tenantRepositoryPort.findById(dto.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID " + dto.getTenantId() + " not found."));
        GroupLocation saved = repositoryPort.save(mapper.toDomain(dto));
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    public GroupLocationResponseDTO update(Long tenantId, Long id, GroupLocationCreateDTO dto) {
        GroupLocation existing = findGroupLocation(tenantId, id);
        existing.setName(dto.getName() != null ? dto.getName() : existing.getName());
        existing.setParentGroupId(dto.getParentGroupId() != null ? dto.getParentGroupId() : existing.getParentGroupId());
        return mapper.toResponseDTO(repositoryPort.save(existing));
    }

    @Transactional
    public void delete(Long tenantId, Long id) {
        findGroupLocation(tenantId, id);
        repositoryPort.deleteById(id);
    }

    private GroupLocation findGroupLocation(Long tenantId, Long id) {
        GroupLocation groupLocation = repositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group location with ID " + id + " not found."));
        if (!tenantId.equals(groupLocation.getTenantId())) {
            throw new EntityNotFoundException("Group location with ID " + id + " not found for tenant " + tenantId + ".");
        }
        return groupLocation;
    }
}
