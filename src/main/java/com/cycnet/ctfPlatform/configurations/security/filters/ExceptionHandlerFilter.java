package com.cycnet.ctfPlatform.configurations.security.filters;

import com.cycnet.ctfPlatform.exceptions.auth.ApiAuthenticationException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        AuthenticationException authenticationException;

        try {
            filterChain.doFilter(request, response);
            return;
        } catch (JwtException exception) {
            authenticationException = new ApiAuthenticationException(
                    exception.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (AuthenticationException  exception) {
            authenticationException = exception;
        }

        authenticationEntryPoint.commence(
                request,
                response,
                authenticationException)
        ;
    }

}
