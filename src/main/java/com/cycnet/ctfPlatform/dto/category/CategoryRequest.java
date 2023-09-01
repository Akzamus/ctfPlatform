package com.cycnet.ctfPlatform.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryRequest (
        @NotBlank(message = "Name cannot be blank")
        @Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "Name must start with an uppercase letter and can include only letters"
        )
        String name
) { }
