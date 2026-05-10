package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.User;
import com.security.fofoqueiro.domain.ports.IUserRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.UserEntity;
import com.security.fofoqueiro.infrastructure.mappers.UserMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapterImpl implements IUserRepositoryPort {

    private final SpringDataUserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedEntity = userRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findByTenantId(Long tenantId) {
        return userRepository.findByTenantId(tenantId)
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
}
