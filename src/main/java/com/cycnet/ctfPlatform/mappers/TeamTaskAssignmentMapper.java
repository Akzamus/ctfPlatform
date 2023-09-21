package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.teamTaskAssignment.TeamTaskAssignmentRequestDto;
import com.cycnet.ctfPlatform.models.TeamTaskAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = { TeamRegistrationMapper.class, TaskMapper.class })
public interface TeamTaskAssignmentMapper extends Mappable<
        TeamTaskAssignment,
        TeamTaskAssignmentRequestDto,
        TeamTaskAssignmentMapper
> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "teamRegistration", ignore = true)
    @Mapping(target = "task", ignore = true)
    TeamTaskAssignment toEntity(TeamTaskAssignmentRequestDto request);

}
