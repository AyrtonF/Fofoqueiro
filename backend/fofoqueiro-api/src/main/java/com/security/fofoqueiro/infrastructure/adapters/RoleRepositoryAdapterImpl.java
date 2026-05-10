package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.Role;
import com.security.fofoqueiro.domain.ports.IRoleRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.RoleEntity;
import com.security.fofoqueiro.infrastructure.mappers.RoleMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapterImpl implements IRoleRepositoryPort {

    private final SpringDataRoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name)
                .map(roleMapper::toDomain);
    }

    @Override
    public Role save(Role role) {
        RoleEntity roleEntity = roleMapper.toEntity(role);
        RoleEntity savedEntity = roleRepository.save(roleEntity);
        return roleMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
