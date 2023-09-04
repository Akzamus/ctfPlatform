package com.cycnet.ctfPlatform.dto.user;

import jakarta.validation.constraints.*;

public record UserRequestDto (

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotBlank(message = "Role cannot be blank")
        @Pattern(regexp = "^(USER|ADMIN)$", message = "Role can be USER or ADMIN")
        String role
) { }
