package com.security.fofoqueiro.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraHealthDTO {
    private Long id;
    private Integer fps;
    private Long bitrate;
    private String status;
    private Boolean online;
}
