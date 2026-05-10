package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.WhiteLabelConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataWhiteLabelConfigRepository extends JpaRepository<WhiteLabelConfigEntity, Long> {
    Optional<WhiteLabelConfigEntity> findByTenantId(Long tenantId);
}
