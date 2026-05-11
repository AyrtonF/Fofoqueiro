package com.security.fofoqueiro.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordingResponseDTO {
    private Long id;
    private Long cameraId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String s3Path;
    private Double size;
    private String eventType;
}
