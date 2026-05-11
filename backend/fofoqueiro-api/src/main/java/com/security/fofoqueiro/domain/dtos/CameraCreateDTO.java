package com.security.fofoqueiro.domain.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraCreateDTO {
    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "Gateway ID is required")
    private Long gatewayId;

    @NotBlank(message = "Camera name is required")
    private String name;

    @NotBlank(message = "RTSP URL is required")
    @JsonAlias({"rtspUrl"})
    @Pattern(regexp = "^rtsp://[a-zA-Z0-9.-]+(:[0-9]{1,5})?(/[a-zA-Z0-9-._~:?#@!$&'()*+,;=]*)*$", message = "Invalid RTSP URL format")
    private String url;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotNull(message = "Recording retention days is required")
    @Min(value = 1, message = "Recording retention days must be at least 1")
    private Integer recordingRetentionDays;
}
