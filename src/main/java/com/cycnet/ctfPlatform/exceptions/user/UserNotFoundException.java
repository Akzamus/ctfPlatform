package com.cycnet.ctfPlatform.exceptions.user;

import com.cycnet.ctfPlatform.exceptions.ApiRequestException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiRequestException {

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
