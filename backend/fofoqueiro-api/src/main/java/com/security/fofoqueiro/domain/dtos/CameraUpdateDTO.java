package com.security.fofoqueiro.domain.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraUpdateDTO {
    private Long id;

    private Long tenantId;

    private Long gatewayId;

    private String name;

    @JsonAlias({"rtspUrl"})
    @Pattern(regexp = "^rtsp://[a-zA-Z0-9.-]+(:[0-9]{1,5})?(/[a-zA-Z0-9-._~:?#@!$&'()*+,;=]*)*$", message = "Invalid RTSP URL format")
    private String url;

    private Double latitude;

    private Double longitude;

    @Min(value = 1, message = "Recording retention days must be at least 1")
    private Integer recordingRetentionDays;

    private String status;
    private Integer fps;
    private Long bitrate;
    private Boolean isActive;
}
