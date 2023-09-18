package com.cycnet.ctfPlatform.dto.teamRegistration;

import com.cycnet.ctfPlatform.enums.TeamResult;
import jakarta.validation.constraints.*;


public record TeamRegistrationRequestDto (

        @NotNull(message = "Event ID cannot be null")
        @Positive(message = "Event ID must be a positive number")
        long eventId,

        @NotNull(message = "Team ID cannot be null")
        @Positive(message = "Team ID must be a positive number")
        long teamId,

        @Pattern(regexp = "^(NONE|DEFEAT|VICTORY)$", message = "Team result can be NONE, DEFEAT or VICTORY")
        String teamResult

) {

    public TeamRegistrationRequestDto(
            long eventId,
            long teamId,
            String teamResult
    ) {
        this.eventId = eventId;
        this.teamId = teamId;
        this.teamResult = teamResult == null ? TeamResult.NONE.name() : teamResult;
    }

}
