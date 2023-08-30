package com.cycnet.ctfPlatform.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtParser jwtParser;

    public boolean isTokenExpired(String token) {
        Optional<Date> expirationDate = jwtParser.extractExpiration(token);
        return expirationDate.map(date -> date.before(new Date()))
                .orElse(false);
    }

    public void ifTokenExpiredThrow(
            String token,
            Supplier<? extends RuntimeException> exceptionSupplier
    ) {
        if (isTokenExpired(token)) {
            throw exceptionSupplier.get();
        }
    }
}