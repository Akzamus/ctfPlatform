package com.cycnet.ctfPlatform.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyLinkedStudentException extends ResponseStatusException {

    public UserAlreadyLinkedStudentException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

}
