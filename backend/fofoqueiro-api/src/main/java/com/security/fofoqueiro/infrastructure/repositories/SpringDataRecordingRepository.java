package com.security.fofoqueiro.infrastructure.repositories;

import com.security.fofoqueiro.infrastructure.entities.RecordingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpringDataRecordingRepository extends JpaRepository<RecordingEntity, Long> {
    List<RecordingEntity> findByTenantIdAndCameraId(Long tenantId, Long cameraId);
    List<RecordingEntity> findByTenantIdAndCameraIdAndStartTimeBetween(Long tenantId, Long cameraId, LocalDateTime start, LocalDateTime end);
}
