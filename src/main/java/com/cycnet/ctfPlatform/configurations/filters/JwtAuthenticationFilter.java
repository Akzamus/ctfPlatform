package com.cycnet.ctfPlatform.configurations.filters;

import com.cycnet.ctfPlatform.exceptions.auth.JwtSubjectMissingException;
import com.cycnet.ctfPlatform.exceptions.auth.JwtTokenExpiredException;
import com.cycnet.ctfPlatform.jwt.JwtParser;
import com.cycnet.ctfPlatform.jwt.JwtUtils;
import com.cycnet.ctfPlatform.jwt.JwtValidator;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtParser jwtParser;
    private final JwtValidator jwtValidator;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(JwtUtils.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = JwtUtils.extractJwtFromHeader(authHeader);

        UserDetails userDetails = jwtParser
                .extractEmail(jwt)
                .map(userDetailsService::loadUserByUsername)
                .orElseThrow(() -> new JwtSubjectMissingException("JWT subject cannot be null"));

        jwtValidator.ifTokenExpiredThrow(jwt, () -> new JwtTokenExpiredException("Token has expired"));

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

}
