package com.cycnet.ctfPlatform.configurations.filters;

import com.cycnet.ctfPlatform.dto.apiException.ApiExceptionResponse;
import com.cycnet.ctfPlatform.exceptions.ApiExceptionResponseFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ApiExceptionResponseFactory apiExceptionResponseFactory;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
            response.setStatus(httpStatus.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ApiExceptionResponse errorResponse = apiExceptionResponseFactory.createApiExceptionResponse(
                    httpStatus,
                    "Invalid JWT token: " + e.getMessage()
            );

            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            try (PrintWriter writer = response.getWriter()) {
                writer.write(jsonResponse);
                writer.flush();
            }
        }
    }

}
