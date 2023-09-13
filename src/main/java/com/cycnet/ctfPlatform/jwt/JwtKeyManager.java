package com.cycnet.ctfPlatform.jwt;

import com.cycnet.ctfPlatform.configurations.properties.JwtProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtKeyManager {

    private final JwtProperties jwtProperties;

    public Key getSignInKey() {
        String secretKey = jwtProperties.secretKey();
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
