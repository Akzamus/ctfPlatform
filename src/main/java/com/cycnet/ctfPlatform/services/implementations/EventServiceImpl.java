package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.event.EventRequestDto;
import com.cycnet.ctfPlatform.dto.event.EventResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.EventMapper;
import com.cycnet.ctfPlatform.models.Event;
import com.cycnet.ctfPlatform.repositories.EventRepository;
import com.cycnet.ctfPlatform.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public PageResponseDto<EventResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving events, page number: {}, page size: {}", pageNumber, pageSize);

        Page<Event> eventPage = eventRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<EventResponseDto> eventPageResponseDto = eventMapper.toDto(eventPage);

        log.info("Finished retrieving events, page number: {}, page size: {}", pageNumber, pageSize);

        return eventPageResponseDto;
    }

    @Override
    public EventResponseDto getById(long id) {
        log.info("Retrieving event by ID: {}", id);

        EventResponseDto eventResponseDto = eventRepository.findById(id)
                .map(eventMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + id + " does not exist."));

        log.info("Finished retrieving event by ID: {}", eventResponseDto.id());

        return eventResponseDto;
    }

    @Override
    @Transactional
    public EventResponseDto create(EventRequestDto requestDto) {
        log.info("Creating a new event with name: {}", requestDto.name());

        throwExceptionIfEventExists(requestDto.name());

        Event event = eventMapper.toEntity(requestDto);
        event = eventRepository.save(event);
        EventResponseDto eventResponseDto = eventMapper.toDto(event);

        log.info("Created a new event with name: {}", event.getName());

        return eventResponseDto;
    }

    @Override
    @Transactional
    public EventResponseDto update(long id, EventRequestDto requestDto) {
        log.info("Updating event with ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + id + " does not exist."));

        String newName = requestDto.name();

        if (!event.getName().equals(newName)) {
            throwExceptionIfEventExists(newName);
            event.setName(newName);
        }

        event.setStartedAt(requestDto.startedAt());
        event.setEndedAt(requestDto.endedAt());

        event = eventRepository.save(event);
        EventResponseDto eventResponseDto = eventMapper.toDto(event);

        log.info("Updated event with ID: {}", id);

        return eventResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting event with ID: {}", id);

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + id + " does not exist"));

        eventRepository.delete(existingEvent);

        log.info("Deleted event with ID: {}", id);
    }

    private void throwExceptionIfEventExists(String name) {
        eventRepository.findByName(name)
                .ifPresent(foundEvent -> {
                    throw new EntityAlreadyExistsException(
                            "Event with the name " + foundEvent.getName() + " already exists."
                    );
                });
    }

}
