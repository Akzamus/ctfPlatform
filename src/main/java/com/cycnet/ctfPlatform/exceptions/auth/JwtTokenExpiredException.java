package com.cycnet.ctfPlatform.exceptions.auth;

import io.jsonwebtoken.JwtException;

public class JwtTokenExpiredException extends JwtException {

    public JwtTokenExpiredException(String message) {
        super(message);
    }

}
