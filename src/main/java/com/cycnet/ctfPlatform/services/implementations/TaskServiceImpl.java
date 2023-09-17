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
import com.cycnet.ctfPlatform.repositories.CategoryRepository;
import com.cycnet.ctfPlatform.repositories.EventRepository;
import com.cycnet.ctfPlatform.repositories.TaskRepository;
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
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final TaskMapper taskMapper;

    @Override
    public PageResponseDto<TaskResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving tasks, page number: {}, page size : {}", pageNumber, pageSize);

        Page<Task> taskPage = taskRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TaskResponseDto> taskPageResponseDto = taskMapper.toDto(taskPage);

        log.info("Finished retrieving tasks, page number: {}, page size : {}", pageNumber, pageSize);

        return taskPageResponseDto;
    }

    @Override
    public TaskResponseDto getById(long id) {
        log.info("Retrieving task by ID: {}", id);

        TaskResponseDto taskResponseDto = taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " does not exist"));

        log.info("Finished retrieving team by ID: {}", taskResponseDto.id());

        return taskResponseDto;
    }

    @Override
    @Transactional
    public TaskResponseDto create(TaskRequestDto requestDto) {
        log.info("Creating a new task for event with ID: {}", requestDto.eventId());

        Task task = taskMapper.toEntity(requestDto);

        Event event = eventRepository.findById(requestDto.eventId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Event with ID " + requestDto.eventId() + " doest not exists")
                );

        throwExceptionIfTaskWithEventAndNameExists(event, task.getName());

        Category category = categoryRepository.findById(requestDto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category with ID " + requestDto.categoryId() + " doest not exists")
                );

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
        log.info("Updating a new task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("Task with id " + id + " does not exist."));

        if (!task.getEvent().getId().equals(requestDto.eventId())) {
            Event event = eventRepository.findById(requestDto.eventId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Event with ID " + requestDto.eventId() + " doest not exists")
                    );

            throwExceptionIfTaskWithEventAndNameExists(event, task.getName());

            task.setEvent(event);

            log.info("Event with ID {} has been set for task with ID: {}", event.getId(), task.getId());
        }

        if (!task.getCategory().getId().equals(requestDto.categoryId())) {
            Category category = categoryRepository.findById(requestDto.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Category with ID " + requestDto.categoryId() + " doest not exists")
                    );

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

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " does not exist."));

        taskRepository.delete(existingTask);

        log.info("Deleted task with ID: {}", id);
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
