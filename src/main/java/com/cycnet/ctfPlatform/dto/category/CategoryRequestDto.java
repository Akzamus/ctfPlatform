package com.cycnet.ctfPlatform.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto (

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
        @Pattern(
                regexp = "^[A-Z][A-Za-z]*([- ][A-Z][A-Za-z]*)*$",
                message = "Name must start with an uppercase letter, and words can be separated by space or hyphen"
        )
        String name

) { }
