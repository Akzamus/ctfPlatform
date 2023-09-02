package com.cycnet.ctfPlatform.exceptions.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CategoryAlreadyExistsException extends ResponseStatusException {

    public CategoryAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

}
