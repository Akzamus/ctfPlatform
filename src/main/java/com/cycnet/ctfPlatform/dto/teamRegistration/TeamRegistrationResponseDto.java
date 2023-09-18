package com.cycnet.ctfPlatform.dto.teamRegistration;

import com.cycnet.ctfPlatform.dto.event.EventResponseDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.enums.TeamResult;

public record TeamRegistrationResponseDto (
        long id,
        EventResponseDto event,
        TeamResponseDto  team,
        TeamResult teamResult
) { }

