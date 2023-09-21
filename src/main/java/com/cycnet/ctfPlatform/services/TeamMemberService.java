package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.teamMember.TeamMemberRequestDto;
import com.cycnet.ctfPlatform.dto.teamMember.TeamMemberResponseDto;
import com.cycnet.ctfPlatform.models.TeamMember;

public interface TeamMemberService extends CrudService<TeamMember, TeamMemberRequestDto, TeamMemberResponseDto> {

}
