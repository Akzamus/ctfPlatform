package com.cycnet.ctfPlatform.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest (
        @NotBlank(message = "Email cannot be blank")
        String email,
        @NotBlank(message = "Password cannot be blank")
        String password
) { }