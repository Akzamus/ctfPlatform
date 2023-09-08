package com.cycnet.ctfPlatform.advice;

import com.cycnet.ctfPlatform.dto.apiException.ApiExceptionResponseDto;
import com.cycnet.ctfPlatform.dto.apiException.ApiValidationExceptionResponseDto;
import com.cycnet.ctfPlatform.exceptions.ApiExceptionResponseFactory;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiExceptionResponseFactory apiExceptionResponseFactory;
    private final Map<Class<?>, String> errorMessages = new HashMap<>();

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiValidationExceptionResponseDto handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        return apiExceptionResponseFactory.createApiValidationExceptionResponseDto(
                exception.getBindingResult()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiValidationExceptionResponseDto handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        return apiExceptionResponseFactory.createApiValidationExceptionResponseDto(
                exception.getConstraintViolations()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ApiExceptionResponseDto handleMissingRequestHeaderException(
            MissingRequestHeaderException exception
    ) {
        return apiExceptionResponseFactory.createApiExceptionResponseDto(
                HttpStatus.BAD_REQUEST,
                "Required header '" + exception.getHeaderName() + "' is missing"
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiExceptionResponseDto handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception
    ) {
        return apiExceptionResponseFactory.createApiExceptionResponseDto(
                HttpStatus.BAD_REQUEST,
                "Failed to convert value of parameter '" + exception.getName() + "' to the required type."
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiExceptionResponseDto handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception
    ) {
        return apiExceptionResponseFactory.createApiExceptionResponseDto(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiExceptionResponseDto> handleResponseStatusException(
            ResponseStatusException exception
    ) {
        HttpStatus httpStatus = HttpStatus.valueOf(exception.getStatusCode().value());
        ApiExceptionResponseDto response = apiExceptionResponseFactory.createApiExceptionResponseDto(
                httpStatus,
                exception.getReason()
        );
        return new ResponseEntity<>(response, httpStatus);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiExceptionResponseDto handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        Throwable mostSpecificCause = exception.getMostSpecificCause();
        return apiExceptionResponseFactory.createApiExceptionResponseDto(
                HttpStatus.BAD_REQUEST,
                errorMessages.getOrDefault(
                        mostSpecificCause.getClass(),
                        exception.getMessage()
                )
        );
    }

    @PostConstruct
    public void initializeExceptionMap() {
        errorMessages.put(DateTimeParseException.class, "Invalid date format, use ISO-8601");
    }
}
