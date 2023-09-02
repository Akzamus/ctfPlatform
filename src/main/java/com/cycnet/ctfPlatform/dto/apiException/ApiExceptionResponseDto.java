package com.cycnet.ctfPlatform.dto.apiException;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class ApiExceptionResponseDto extends ApiExceptionResponseDtoBase {

    private final String errorMessage;

    @Builder
    public ApiExceptionResponseDto(
            int errorCode,
            HttpStatus httpStatus,
            ZonedDateTime timestamp,
            String errorMessage
    ) {
        super(errorCode, httpStatus, timestamp);
        this.errorMessage = errorMessage;
    }

}
