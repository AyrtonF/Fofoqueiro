package com.security.fofoqueiro.domain.ports;

import com.security.fofoqueiro.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepositoryPort {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User save(User user);
    void deleteById(Long id);
    List<User> findByTenantId(Long tenantId);
}
