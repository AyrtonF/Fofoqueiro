package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.GatewayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataGatewayRepository extends JpaRepository<GatewayEntity, Long> {
    List<GatewayEntity> findByTenantId(Long tenantId);
    List<GatewayEntity> findByTenantIdAndStatus(Long tenantId, String status);
    Optional<GatewayEntity> findByIpAddress(String ipAddress);
}
