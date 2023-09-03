package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.models.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper extends Mappable<Team, TeamRequestDto, TeamResponseDto> {

}
