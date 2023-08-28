package com.cycnet.ctfPlatform.exceptions;

import com.cycnet.ctfPlatform.dto.apiException.ApiExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class ApiExceptionResponseFactory {

    private final ZoneId zoneId;

    public ApiExceptionResponse createApiExceptionResponse(HttpStatus httpStatus, String errorMessage) {
        return ApiExceptionResponse.builder()
                .errorCode(httpStatus.value())
                .httpStatus(httpStatus)
                .timestamp(ZonedDateTime.now(zoneId))
                .errorMessage(errorMessage)
                .build();
    }

}
