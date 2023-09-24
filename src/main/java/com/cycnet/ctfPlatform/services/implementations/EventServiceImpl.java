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

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public PageResponseDto<EventResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving Events, page number: {}, page size: {}", pageNumber, pageSize);

        Page<Event> page = eventRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<EventResponseDto> pageResponseDto = eventMapper.toDto(page);

        log.info("Finished retrieving Events, page number: {}, page size: {}", pageNumber, pageSize);

        return pageResponseDto;
    }

    @Override
    public EventResponseDto getById(long id) {
        log.info("Retrieving Event by ID: {}", id);

        Event event = getEntityById(id);
        EventResponseDto responseDto = eventMapper.toDto(event);

        log.info("Finished retrieving Event by ID: {}", event.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public EventResponseDto create(EventRequestDto requestDto) {
        log.info("Creating new Event with name: {}", requestDto.name());

        throwExceptionIfEventExists(requestDto.name());

        Event event = eventMapper.toEntity(requestDto);
        event = eventRepository.save(event);
        EventResponseDto responseDto = eventMapper.toDto(event);

        log.info("Created new Event with ID: {}", event.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public EventResponseDto update(long id, EventRequestDto requestDto) {
        log.info("Updating Event with ID: {}", id);

        Event event = getEntityById(id);

        String oldName = event.getName();
        String newName = requestDto.name();

        if (!Objects.equals(oldName, newName)) {
            throwExceptionIfEventExists(newName);
            event.setName(newName);
        }

        event.setStartedAt(requestDto.startedAt());
        event.setEndedAt(requestDto.endedAt());

        event = eventRepository.save(event);
        EventResponseDto responseDto = eventMapper.toDto(event);

        log.info("Updated Event with ID: {}", event.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting Event with ID: {}", id);

        Event event = getEntityById(id);
        eventRepository.delete(event);

        log.info("Deleted Event with ID: {}", event.getId());
    }

    @Override
    public Event getEntityById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + id + " does not exist"));
    }

    private void throwExceptionIfEventExists(String name) {
        eventRepository.findByName(name)
                .ifPresent(foundEvent -> {
                    throw new EntityAlreadyExistsException(
                            "Event with the name " + foundEvent.getName() + " already exists"
                    );
                });
    }

}
