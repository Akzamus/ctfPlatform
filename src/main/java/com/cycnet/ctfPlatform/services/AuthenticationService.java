package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.auth.AuthenticationRequestDto;
import com.cycnet.ctfPlatform.dto.auth.AuthenticationResponseDto;
import com.cycnet.ctfPlatform.dto.auth.RegisterRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto register(RegisterRequestDto request);
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);
    AuthenticationResponseDto refreshToken(String authHeader);

}
