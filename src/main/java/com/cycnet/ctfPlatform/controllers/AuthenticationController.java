package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.auth.AuthenticationRequest;
import com.cycnet.ctfPlatform.dto.auth.AuthenticationResponse;
import com.cycnet.ctfPlatform.dto.auth.RegisterRequest;
import com.cycnet.ctfPlatform.services.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Pattern(regexp = "Bearer .*", message = "Authorization header must start with 'Bearer '")
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authHeader
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(authHeader));
    }
}
