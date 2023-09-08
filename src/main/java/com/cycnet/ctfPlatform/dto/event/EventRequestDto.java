package com.cycnet.ctfPlatform.dto.event;

import com.cycnet.ctfPlatform.customAnnotations.ValidEventRequest;
import jakarta.validation.constraints.*;

import java.time.ZonedDateTime;

@ValidEventRequest
public record EventRequestDto (

        @NotBlank(message = "Name cannot be blank")
        @Pattern(
                regexp = "^[A-Z][A-Za-z]*([- ][A-Z][A-Za-z]*)*$",
                message = "Name must start with an uppercase letter, and words can be separated by space or hyphen"
        )
        String name,

        @NotNull(message = "startedAt cannot be blank")
        @FutureOrPresent(message = "StartedAt must be a future or present date")
        ZonedDateTime startedAt,

        @NotNull(message = "endedAt cannot be blank")
        @Future(message = "EndedAt must be a future date")
        ZonedDateTime endedAt

) { }
