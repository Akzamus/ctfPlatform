package com.cycnet.ctfPlatform.dto.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse (
        String accessToken,
        String refreshToken
) { }
