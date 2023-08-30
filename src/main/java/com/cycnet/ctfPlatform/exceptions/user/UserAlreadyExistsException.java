package com.cycnet.ctfPlatform.exceptions.user;

import com.cycnet.ctfPlatform.exceptions.ApiRequestException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiRequestException {

    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}

