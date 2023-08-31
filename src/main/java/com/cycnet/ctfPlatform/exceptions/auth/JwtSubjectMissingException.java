package com.cycnet.ctfPlatform.exceptions.auth;

import org.springframework.http.HttpStatus;

public class JwtSubjectMissingException extends ApiAuthenticationException {

    public JwtSubjectMissingException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
