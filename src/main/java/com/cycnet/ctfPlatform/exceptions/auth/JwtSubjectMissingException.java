package com.cycnet.ctfPlatform.exceptions.auth;

import io.jsonwebtoken.JwtException;

public class JwtSubjectMissingException extends JwtException {

    public JwtSubjectMissingException(String message) {
        super(message);
    }

}
