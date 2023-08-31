package com.cycnet.ctfPlatform.exceptions.auth;

import org.springframework.http.HttpStatus;

public class JwtTokenExpiredException extends ApiAuthenticationException {

    public JwtTokenExpiredException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
