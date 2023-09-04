package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.models.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper extends Mappable<Team, TeamRequestDto, TeamResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teamRegistrations", ignore = true)
    Team toEntity(TeamRequestDto request);

}
