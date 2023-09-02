package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.auth.AuthenticationRequestDto;
import com.cycnet.ctfPlatform.dto.auth.AuthenticationResponseDto;
import com.cycnet.ctfPlatform.dto.auth.RegisterRequestDto;
import com.cycnet.ctfPlatform.services.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
            @RequestBody @Valid RegisterRequestDto request
    ) {
        AuthenticationResponseDto responseDto = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody @Valid AuthenticationRequestDto request)
    {
        AuthenticationResponseDto responseDto = authenticationService.authenticate(request);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            @Pattern(regexp = "Bearer .*", message = "Authorization header must start with 'Bearer '")
            String authHeader
    ) {
        AuthenticationResponseDto responseDto = authenticationService.refreshToken(authHeader);
        return ResponseEntity.ok(responseDto);
    }

}
