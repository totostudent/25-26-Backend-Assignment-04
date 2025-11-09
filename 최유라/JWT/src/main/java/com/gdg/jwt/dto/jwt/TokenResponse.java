package com.gdg.jwt.dto.jwt;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken
) {
}
