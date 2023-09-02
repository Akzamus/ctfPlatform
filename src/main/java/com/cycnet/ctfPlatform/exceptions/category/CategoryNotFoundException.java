package com.cycnet.ctfPlatform.exceptions.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CategoryNotFoundException extends ResponseStatusException {

    public CategoryNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
