package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.teamMember.TeamMemberRequestDto;
import com.cycnet.ctfPlatform.dto.teamMember.TeamMemberResponseDto;
import com.cycnet.ctfPlatform.models.TeamMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, TeamRegistrationMapper.class})
public interface TeamMemberMapper extends Mappable<TeamMember, TeamMemberRequestDto, TeamMemberResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teamRegistration", ignore = true)
    @Mapping(target = "student", ignore = true)
    TeamMember toEntity(TeamMemberRequestDto request);

}