package com.cycnet.ctfPlatform.dto.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponseDto(
        String accessToken,
        String refreshToken
) { }
