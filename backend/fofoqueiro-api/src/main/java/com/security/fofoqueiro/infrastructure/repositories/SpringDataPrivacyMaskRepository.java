package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.PrivacyMaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataPrivacyMaskRepository extends JpaRepository<PrivacyMaskEntity, Long> {
    List<PrivacyMaskEntity> findByTenantId(Long tenantId);
    List<PrivacyMaskEntity> findByTenantIdAndCameraId(Long tenantId, Long cameraId);
}
