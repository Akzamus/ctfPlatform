package com.cycnet.ctfPlatform.advice;

import com.cycnet.ctfPlatform.dto.apiException.ApiExceptionResponse;
import com.cycnet.ctfPlatform.dto.apiException.ApiValidationExceptionResponse;
import com.cycnet.ctfPlatform.exceptions.ApiExceptionResponseFactory;
import com.cycnet.ctfPlatform.exceptions.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiExceptionResponseFactory apiExceptionResponseFactory;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiValidationExceptionResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        return apiExceptionResponseFactory.createApiValidationExceptionResponse(
                exception.getBindingResult()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ApiExceptionResponse handleMissingRequestHeaderException(
            MissingRequestHeaderException exception
    ) {
        return apiExceptionResponseFactory.createApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                "Required header '" + exception.getHeaderName() + "' is missing"
        );
    }

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<ApiExceptionResponse> handleApiRequestException(
            ApiRequestException exception
    ) {
        ApiExceptionResponse response = apiExceptionResponseFactory.createApiExceptionResponse(
                exception.getHttpStatus(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

}
