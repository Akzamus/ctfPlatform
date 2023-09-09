package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.event.EventRequestDto;
import com.cycnet.ctfPlatform.dto.event.EventResponseDto;
import com.cycnet.ctfPlatform.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper extends Mappable<Event, EventRequestDto, EventResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teamRegistrations", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Event toEntity(EventRequestDto request);

}
