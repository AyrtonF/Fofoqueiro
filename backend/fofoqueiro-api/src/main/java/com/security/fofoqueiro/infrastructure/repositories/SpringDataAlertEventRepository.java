package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.AlertEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataAlertEventRepository extends JpaRepository<AlertEventEntity, Long> {
    List<AlertEventEntity> findByTenantId(Long tenantId);
    List<AlertEventEntity> findByTenantIdAndCameraId(Long tenantId, Long cameraId);
}
