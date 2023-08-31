package com.cycnet.ctfPlatform.dto.apiException;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
public class ApiValidationExceptionResponse extends ApiExceptionResponseBase {

    private final Map<String, String> errorFields;

    @Builder
    public ApiValidationExceptionResponse(
            int errorCode,
            HttpStatus httpStatus,
            ZonedDateTime timestamp,
            Map<String, String> errorFields
    ) {
        super(errorCode, httpStatus, timestamp);
        this.errorFields = errorFields;
    }
}
