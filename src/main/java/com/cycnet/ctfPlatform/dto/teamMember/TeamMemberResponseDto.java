package com.cycnet.ctfPlatform.dto.teamMember;

import com.cycnet.ctfPlatform.dto.student.StudentResponseDto;
import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationResponseDto;
import com.cycnet.ctfPlatform.enums.TeamRole;

public record TeamMemberResponseDto(
        long id,
        TeamRole teamRole,
        TeamRegistrationResponseDto teamRegistration,
        StudentResponseDto student
) { }
