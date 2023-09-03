package com.cycnet.ctfPlatform.exceptions.team;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TeamAlreadyExistsException extends ResponseStatusException {

    public TeamAlreadyExistsException(String message){
        super(HttpStatus.CONFLICT,message);
    }
}
