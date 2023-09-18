package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationRequestDto;
import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationResponseDto;
import com.cycnet.ctfPlatform.models.TeamRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = { TeamMapper.class, EventMapper.class })
public interface TeamRegistrationMapper extends Mappable<TeamRegistration, TeamRegistrationRequestDto, TeamRegistrationResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team",ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "teamTaskAssignments", ignore = true)
    @Mapping(target = "teamMembers", ignore = true)
    TeamRegistration toEntity(TeamRegistrationRequestDto request);

}
