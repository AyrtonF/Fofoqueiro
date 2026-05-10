package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.CameraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataCameraRepository extends JpaRepository<CameraEntity, Long> {
    List<CameraEntity> findByTenantId(Long tenantId);
    List<CameraEntity> findByTenantIdAndStatus(Long tenantId, String status);
}
