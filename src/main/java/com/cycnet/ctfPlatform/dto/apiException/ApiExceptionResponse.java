package com.cycnet.ctfPlatform.dto.apiException;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class ApiExceptionResponse extends ApiExceptionResponseBase {

    private final String errorMessage;

    @Builder
    public ApiExceptionResponse(
            int errorCode,
            HttpStatus httpStatus,
            ZonedDateTime timestamp,
            String errorMessage
    ) {
        super(errorCode, httpStatus, timestamp);
        this.errorMessage = errorMessage;
    }

}
