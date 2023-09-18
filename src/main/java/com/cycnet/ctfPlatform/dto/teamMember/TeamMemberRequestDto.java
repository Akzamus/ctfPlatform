package com.cycnet.ctfPlatform.dto.teamMember;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record TeamMemberRequestDto (

        @NotBlank(message = "Team role cannot be blank")
        @Pattern(regexp = "^(CAPTAIN|PLAYER)$", message = "Team role can be CAPTAIN or PLAYER")
        String teamRole,

        @NotNull(message = "Team registration id cannot be null")
        @Positive(message = "Team registration id must be positive")
        Long teamRegistrationId,

        @NotNull(message = "Student id cannot be null")
        @Positive(message = "Student id must be positive")
        Long studentId

) { }
