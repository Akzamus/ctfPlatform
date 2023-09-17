package com.cycnet.ctfPlatform.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TaskRequestDto (

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 15, message = "Name must be between 2 and 15 characters")
        String name,

        @NotBlank(message = "Question cannot be blank")
        String question,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Number of points cannot be null")
        @Positive(message = "Number of points must be positive")
        Integer numberOfPoints,

        @NotNull(message = "Event id cannot be null")
        @Positive(message = "Event id must be positive")
        Long eventId,

        @NotNull(message = "Category id cannot be null")
        @Positive(message = "Category id must be positive")
        Long categoryId

) { }
