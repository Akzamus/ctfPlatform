package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.auth.AuthenticationRequest;
import com.cycnet.ctfPlatform.dto.auth.AuthenticationResponse;
import com.cycnet.ctfPlatform.dto.auth.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(String authHeader);

}
