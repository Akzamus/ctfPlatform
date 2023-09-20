package com.cycnet.ctfPlatform.dto.teamTaskAssignment;

import com.cycnet.ctfPlatform.dto.task.TaskResponseDto;
import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationResponseDto;

import java.time.ZonedDateTime;

public record TeamTaskAssignmentResponseDto(
        long id,
        String correctAnswer,
        ZonedDateTime completedAt,
        TeamRegistrationResponseDto teamRegistration,
        TaskResponseDto task
) { }
