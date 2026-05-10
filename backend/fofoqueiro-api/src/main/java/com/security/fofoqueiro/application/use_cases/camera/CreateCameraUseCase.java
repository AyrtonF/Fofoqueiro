package com.security.fofoqueiro.application.use_cases.camera;

import com.security.fofoqueiro.application.common.IUseCase;
import com.security.fofoqueiro.application.validators.CreateCameraUseCaseValidator;
import com.security.fofoqueiro.domain.dtos.CameraCreateDTO;
import com.security.fofoqueiro.domain.dtos.CameraResponseDTO;
import com.security.fofoqueiro.domain.models.Camera;
import com.security.fofoqueiro.domain.ports.ICameraRepositoryPort;
import com.security.fofoqueiro.infrastructure.mappers.CameraMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCameraUseCase implements IUseCase<CameraCreateDTO, CameraResponseDTO> {

    private final CreateCameraUseCaseValidator validator;
    private final ICameraRepositoryPort cameraRepositoryPort;
    private final CameraMapper cameraMapper;

    @Override
    @Transactional
    public CameraResponseDTO execute(CameraCreateDTO input) {
        // 1. Validate and convert DTO to domain model
        Camera cameraToSave = validator.validate(input);

        // 2. Save the camera
        Camera savedCamera = cameraRepositoryPort.save(cameraToSave);

        // 3. Convert saved domain model to response DTO
        return cameraMapper.toResponseDTO(savedCamera);
    }
}
