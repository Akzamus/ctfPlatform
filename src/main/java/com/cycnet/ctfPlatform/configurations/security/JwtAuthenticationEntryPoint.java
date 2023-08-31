package com.cycnet.ctfPlatform.configurations.security;

import com.cycnet.ctfPlatform.dto.apiException.ApiExceptionResponse;
import com.cycnet.ctfPlatform.exceptions.ApiExceptionResponseFactory;
import com.cycnet.ctfPlatform.exceptions.auth.ApiAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final ApiExceptionResponseFactory apiExceptionResponseFactory;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        if (authException instanceof ApiAuthenticationException apiAuthenticationException) {
            httpStatus = apiAuthenticationException.getHttpStatus();
        }

        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiExceptionResponse errorResponse = apiExceptionResponseFactory.createApiExceptionResponse(
                httpStatus,
                authException.getMessage()
        );

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResponse);
            writer.flush();
        }
    }

}
