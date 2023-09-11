package com.cycnet.ctfPlatform.dto.student;

import jakarta.validation.constraints.*;

public record StudentRequestDto (

        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 15, message = "First name must be between 2 and 15 characters")
        @Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "First name must start with an uppercase letter and can include only letters"
        )
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 15, message = "Last name must be between 2 and 15 characters")
        @Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "Last name must start with an uppercase letter and can include only letters"
        )
        String lastName,

        @NotNull(message = "User id cannot be null")
        @Positive(message = "User id must be positive")
        long userId
) { }
