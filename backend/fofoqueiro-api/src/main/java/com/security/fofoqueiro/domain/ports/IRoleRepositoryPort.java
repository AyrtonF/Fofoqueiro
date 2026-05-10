package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.Role;

import java.util.Optional;

public interface IRoleRepositoryPort {
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
    Role save(Role role);
    void deleteById(Long id);
}
