package com.cycnet.ctfPlatform.advice;

import com.cycnet.ctfPlatform.dto.apiException.ApiExceptionResponseDto;
import com.cycnet.ctfPlatform.dto.apiException.ApiValidationExceptionResponseDto;
import com.cycnet.ctfPlatform.exceptions.ApiExceptionResponseFactory;
import com.cycnet.ctfPlatform.exceptions.server.InternalServerErrorException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

        ApiValidationExceptionResponseDto responseDto = apiExceptionResponseFactory
                .createApiValidationExceptionResponseDto(exception.getBindingResult());

        log.warn("Client sent the wrong request body: {}", responseDto.getErrorFields());

        return responseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiValidationExceptionResponseDto handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        ApiValidationExceptionResponseDto responseDto = apiExceptionResponseFactory
                .createApiValidationExceptionResponseDto(exception.getConstraintViolations());

        log.warn("Client sent the wrong variables in the path: {}", responseDto.getErrorFields());

        return responseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ApiExceptionResponseDto handleMissingRequestHeaderException(
            MissingRequestHeaderException exception
    ) {
        log.warn("Client did not send the required header: {}", exception.getHeaderName());

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
        log.warn("Client sent the wrong parameter type in path: {}", exception.getName());

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
        log.warn("Client sent a request with an unavailable method: {}", exception.getMethod());

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

        log.warn(
                "Client received an exception with the status {} and the message: {}",
                httpStatus,
                response.getErrorMessage()
        );

        return new ResponseEntity<>(response, httpStatus);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiExceptionResponseDto handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        Throwable mostSpecificCause = exception.getMostSpecificCause();
        String errorMessage = errorMessages.getOrDefault(
                mostSpecificCause.getClass(),
                exception.getMessage()
        );

        log.warn(
                "Client sent an invalid argument value in the body and received a response: {}",
                errorMessage
        );

        return apiExceptionResponseFactory.createApiExceptionResponseDto(
                HttpStatus.BAD_REQUEST,
                errorMessage
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ApiExceptionResponseDto handleInternalServerErrorException(
            InternalServerErrorException exception
    ) {
        String errorMessage = "Internal server error";
        log.warn(errorMessage, exception.getCause());

        return apiExceptionResponseFactory.createApiExceptionResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                errorMessage
        );
    }

    @PostConstruct
    private void initializeExceptionMap() {
        errorMessages.put(DateTimeParseException.class, "Invalid date format, use ISO-8601");
    }

}
