package com.cycnet.ctfPlatform.dto.event;

import com.cycnet.ctfPlatform.customAnnotations.ValidEventRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@ValidEventRequest
public record EventRequestDto (
        @NotBlank(message = "Name cannot be blank")
        @Pattern(
                regexp = "^[A-Z][A-Za-z]*([- ][A-Z][A-Za-z]*)*$",
                message = "Name must start with an uppercase letter, and words can be separated by space or hyphen"
        )
        String name,
        String startedAt,
        String endedAt
) { }
