package com.cycnet.ctfPlatform.exceptions;

import com.cycnet.ctfPlatform.dto.apiException.ApiExceptionResponse;
import com.cycnet.ctfPlatform.dto.apiException.ApiValidationExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ApiValidationExceptionResponse createApiValidationExceptionResponse(BindingResult bindingResult) {
        Map<String, String> errorFields = buildErrorFields(bindingResult);
        return ApiValidationExceptionResponse.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(ZonedDateTime.now(zoneId))
                .errorFields(errorFields)
                .build();
    }

    private Map<String, String> buildErrorFields(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage,
                                (existing, replacement) -> replacement
                        )
                );
    }

}
