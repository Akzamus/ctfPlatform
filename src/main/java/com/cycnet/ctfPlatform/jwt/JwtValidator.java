package com.cycnet.ctfPlatform.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtParser jwtParser;

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Optional<String> extractedUsername = jwtParser.extractUsername(token);
        return extractedUsername.isPresent() &&
                extractedUsername.get().equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Optional<Date> expirationDate = jwtParser.extractExpiration(token);
        return expirationDate.map(date -> date.before(new Date()))
                .orElse(false);
    }

}