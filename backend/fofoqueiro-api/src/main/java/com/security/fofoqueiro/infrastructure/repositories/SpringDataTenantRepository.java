package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataTenantRepository extends JpaRepository<TenantEntity, Long> {
    Optional<TenantEntity> findByDomain(String domain);
    Optional<TenantEntity> findByName(String name);
}
