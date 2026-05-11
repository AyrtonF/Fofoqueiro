package com.security.fofoqueiro.domain.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraTestConnectionRequestDTO {
    @JsonAlias({"rtspUrl"})
    private String url;
    private Long gatewayId;
}
