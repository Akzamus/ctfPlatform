package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.task.TaskRequestDto;
import com.cycnet.ctfPlatform.dto.task.TaskResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.TaskMapper;
import com.cycnet.ctfPlatform.models.Category;
import com.cycnet.ctfPlatform.models.Event;
import com.cycnet.ctfPlatform.models.File;
import com.cycnet.ctfPlatform.models.Task;
import com.cycnet.ctfPlatform.repositories.TaskRepository;
import com.cycnet.ctfPlatform.services.CategoryService;
import com.cycnet.ctfPlatform.services.EventService;
import com.cycnet.ctfPlatform.services.StorageService;
import com.cycnet.ctfPlatform.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final StorageService storageService;
    private final TaskMapper taskMapper;

    @Override
    public PageResponseDto<TaskResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving Tasks, page number: {}, page size : {}", pageNumber, pageSize);

        Page<Task> page = taskRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TaskResponseDto> pageResponseDto = taskMapper.toDto(page);

        log.info("Finished retrieving Tasks, page number: {}, page size : {}", pageNumber, pageSize);

        return pageResponseDto;
    }

    @Override
    public TaskResponseDto getById(long id) {
        log.info("Retrieving Task by ID: {}", id);

        Task task = getEntityById(id);
        TaskResponseDto responseDto = taskMapper.toDto(task);

        log.info("Finished retrieving Task by ID: {}", task.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public TaskResponseDto create(TaskRequestDto requestDto) {
        log.info("Creating new Task for Event with ID: {}", requestDto.eventId());

        Event event = eventService.getEntityById(requestDto.eventId());

        throwExceptionIfTaskExists(event, requestDto.name());

        Category category = categoryService.getEntityById(requestDto.categoryId());

        Task task = taskMapper.toEntity(requestDto);
        task.setEvent(event);
        task.setCategory(category);

        log.info("Event with ID {} and Creating with ID {} are set for Task", event.getId(), category.getId());

        task = taskRepository.save(task);
        TaskResponseDto responseDto = taskMapper.toDto(task);

        log.info("Created new Task with ID: {}", task.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public TaskResponseDto update(long id, TaskRequestDto requestDto) {
        log.info("Updating Task with ID: {}", id);

        Task task = getEntityById(id);

        Event event = task.getEvent();
        Category category = task.getCategory();

        long oldEventId = event.getId();
        long oldCategoryId = category.getId();
        String oldName = task.getName();

        long newEventId = requestDto.eventId();
        long newCategoryId = requestDto.categoryId();
        String newName = requestDto.name();

        if (oldEventId != newEventId || !Objects.equals(oldName, newName)) {
            event = eventService.getEntityById(newEventId);
            throwExceptionIfTaskExists(event, requestDto.name());
            task.setEvent(event);

            log.info("Event with ID {} has been set for Task with ID: {}", event.getId(), task.getId());
        }

        if (oldCategoryId != newCategoryId) {
            category = categoryService.getEntityById(newCategoryId);
            task.setCategory(category);

            log.info("Category with ID {} has been set for Task with ID: {}", category.getId(), task.getId());
        }

        task.setName(requestDto.name());
        task.setQuestion(requestDto.question());
        task.setDescription(requestDto.description());
        task.setNumberOfPoints(requestDto.numberOfPoints());

        task = taskRepository.save(task);
        TaskResponseDto responseDto = taskMapper.toDto(task);

        log.info("Updated Task with ID: {}", task.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting Task with ID: {}", id);

        Task task = getEntityById(id);
        Hibernate.initialize(task.getFiles());
        taskRepository.delete(task);

        task.getFiles().stream()
                .map(File::getPath)
                .forEach(storageService::deleteFile);

        log.info("Deleted Task with ID: {}", task.getId());
    }

    @Override
    public Task getEntityById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist"));
    }

    private void throwExceptionIfTaskExists(Event event, String name) {
        taskRepository.findByEventAndName(event, name)
                .ifPresent(foundTask -> {
                    throw new EntityAlreadyExistsException(
                            String.format(
                                    "Task with name '%s' already exists for event with ID: %d",
                                    foundTask.getName(),
                                    event.getId()
                            )
                    );
                });
    }

}
