package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.task.TaskRequestDto;
import com.cycnet.ctfPlatform.dto.task.TaskResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.TaskMapper;
import com.cycnet.ctfPlatform.models.Category;
import com.cycnet.ctfPlatform.models.Event;
import com.cycnet.ctfPlatform.models.Task;
import com.cycnet.ctfPlatform.repositories.TaskRepository;
import com.cycnet.ctfPlatform.services.CategoryService;
import com.cycnet.ctfPlatform.services.EventService;
import com.cycnet.ctfPlatform.services.TaskService;
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
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final TaskMapper taskMapper;

    @Override
    public PageResponseDto<TaskResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving Tasks, page number: {}, page size : {}", pageNumber, pageSize);

        Page<Task> taskPage = taskRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TaskResponseDto> taskPageResponseDto = taskMapper.toDto(taskPage);

        log.info("Finished retrieving tasks, page number: {}, page size : {}", pageNumber, pageSize);

        return taskPageResponseDto;
    }

    @Override
    public TaskResponseDto getById(long id) {
        log.info("Retrieving task by ID: {}", id);

        Task task = getEntityById(id);
        TaskResponseDto taskResponseDto = taskMapper.toDto(task);

        log.info("Finished retrieving team by ID: {}", taskResponseDto.id());

        return taskResponseDto;
    }

    @Override
    @Transactional
    public TaskResponseDto create(TaskRequestDto requestDto) {
        log.info("Creating a new task for event with ID: {}", requestDto.eventId());

        Event event = eventService.getEntityById(requestDto.eventId());
        throwExceptionIfTaskWithEventAndNameExists(event, requestDto.name());
        Category category = categoryService.getEntityById(requestDto.categoryId());

        Task task = taskMapper.toEntity(requestDto);
        task.setEvent(event);
        task.setCategory(category);

        log.info("Category and event values are set for task with name: {}", task.getName());

        task = taskRepository.save(task);
        TaskResponseDto taskResponseDto = taskMapper.toDto(task);

        log.info("Created a new task with ID: {}", task.getId());

        return taskResponseDto;
    }

    @Override
    @Transactional
    public TaskResponseDto update(long id, TaskRequestDto requestDto) {
        log.info("Updating a task with ID: {}", id);

        Task task = getEntityById(id);

        long eventId = requestDto.eventId();

        if (task.getEvent().getId() != eventId) {
            Event event = eventService.getEntityById(eventId);
            throwExceptionIfTaskWithEventAndNameExists(event, task.getName());
            task.setEvent(event);

            log.info("Event with ID {} has been set for task with ID: {}", event.getId(), task.getId());
        }

        long categoryId = requestDto.categoryId();

        if (task.getCategory().getId() != categoryId) {
            Category category = categoryService.getEntityById(categoryId);
            task.setCategory(category);

            log.info("Category with ID {} has been set for task with ID: {}", category.getId(), task.getId());
        }

        task.setName(requestDto.name());
        task.setQuestion(requestDto.question());
        task.setDescription(requestDto.description());
        task.setNumberOfPoints(requestDto.numberOfPoints());

        task = taskRepository.save(task);
        TaskResponseDto taskResponseDto = taskMapper.toDto(task);

        log.info("Updated task with ID: {}", task.getId());

        return taskResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting task with ID: {}", id);

        Task existingTask = getEntityById(id);
        taskRepository.delete(existingTask);

        log.info("Deleted task with ID: {}", id);
    }

    @Override
    public Task getEntityById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist."));
    }

    private void throwExceptionIfTaskWithEventAndNameExists(Event event, String name) {
        taskRepository.findByEventAndName(event, name)
                .ifPresent(foundTask -> {
                    throw new EntityAlreadyExistsException(
                            "A task with the name '" + foundTask.getName() + "' already exists for event with ID: " +
                                    event.getId() + "."
                    );
                });
    }

}
