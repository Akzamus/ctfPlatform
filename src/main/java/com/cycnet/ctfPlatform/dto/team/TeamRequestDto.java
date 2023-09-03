package com.cycnet.ctfPlatform.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TeamRequestDto(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        @Pattern(
                regexp = "^[A-Z][a-zA-Z0-9\\s]*$",
                message = "Name must start with an uppercase letter and can include letters, digits, and spaces"
        )
        String name

) { }