package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.GroupLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataGroupLocationRepository extends JpaRepository<GroupLocationEntity, Long> {
    List<GroupLocationEntity> findByTenantId(Long tenantId);
}
