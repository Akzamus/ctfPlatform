package com.cycnet.ctfPlatform.dto.teamTaskAssignment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TeamTaskAssignmentRequestDto (

        @NotBlank(message = "Correct answer cannot be blank")
        String correctAnswer,

        @NotNull(message = "Team registration id cannot be null")
        @Positive(message = "Team registration id must be positive")
        Long teamRegistrationId,

        @NotNull(message = "Task id cannot be null")
        @Positive(message = "Task id must be positive")
        Long taskId

) { }
