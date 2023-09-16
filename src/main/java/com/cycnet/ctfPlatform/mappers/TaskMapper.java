package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.task.TaskRequestDto;
import com.cycnet.ctfPlatform.dto.task.TaskResponseDto;
import com.cycnet.ctfPlatform.models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { EventMapper.class, CategoryMapper.class })
public interface TaskMapper extends Mappable<Task, TaskRequestDto, TaskResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "teamTaskAssignments", ignore = true)
    @Mapping(target = "incomingDependencies", ignore = true)
    @Mapping(target = "outgoingDependencies", ignore = true)
    Task toEntity(TaskRequestDto request);

}
