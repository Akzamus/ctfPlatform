package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.event.EventRequestDto;
import com.cycnet.ctfPlatform.dto.event.EventResponseDto;
import com.cycnet.ctfPlatform.models.Event;

public interface EventService extends CrudService<Event, EventRequestDto, EventResponseDto> {

}
