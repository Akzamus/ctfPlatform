package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.models.Team;

public interface TeamService extends CrudService<Team, TeamRequestDto, TeamResponseDto> {

}
