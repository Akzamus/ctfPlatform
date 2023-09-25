package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.file.FileRequestDto;
import com.cycnet.ctfPlatform.dto.file.FileResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.FileMapper;
import com.cycnet.ctfPlatform.models.File;
import com.cycnet.ctfPlatform.models.Task;
import com.cycnet.ctfPlatform.repositories.FileRepository;
import com.cycnet.ctfPlatform.services.FileService;
import com.cycnet.ctfPlatform.services.StorageService;
import com.cycnet.ctfPlatform.services.TaskService;
import com.cycnet.ctfPlatform.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final TaskService taskService;
    private final StorageService storageService;
    private final FileMapper fileMapper;

    private final String TASK_FILE_FORMAT = "tasks/%d/%s";

    @Override
    public PageResponseDto<FileResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving Files, page number: {}, page size : {}", pageNumber, pageSize);

        Page<File> page = fileRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<FileResponseDto> pageResponseDto = fileMapper.toDto(page);

        log.info("Finished retrieving Files, page number: {}, page size : {}", pageNumber, pageSize);

        return pageResponseDto;
    }

    @Override
    public FileResponseDto getById(long id) {
        log.info("Retrieving File by ID: {}", id);

        File file = getEntityById(id);
        FileResponseDto responseDto = fileMapper.toDto(file);

        log.info("Finished retrieving File by ID: {}", file.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public FileResponseDto create(FileRequestDto requestDto) {
        log.info("Creating new File for Task with ID: {}", requestDto.taskId());

        Task task = taskService.getEntityById(requestDto.taskId());

        MultipartFile multipartFile = requestDto.file();

        String filePath = String.format(
                TASK_FILE_FORMAT,
                task.getId(),
                FileUtils.generateFileName(multipartFile)
        );

        throwExceptionIfFileExists(filePath);

        filePath = storageService.uploadFile(
                filePath,
                FileUtils.getInputStreamOrElseThrow(multipartFile)
        );

        File file = fileMapper.toEntity(requestDto);
        file.setPath(filePath);
        file.setTask(task);

        file = fileRepository.save(file);
        FileResponseDto responseDto = fileMapper.toDto(file);

        log.info("Created new File with ID: {}", file.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public FileResponseDto update(long id, FileRequestDto requestDto) {
        log.info("Updating new File with id: {}", id);

        File file = getEntityById(id);

        Task task = taskService.getEntityById(requestDto.taskId());
        MultipartFile multipartFile = requestDto.file();

        long oldTaskId = task.getId();
        String oldFilePath = file.getPath();

        long newTaskId = requestDto.taskId();
        String newFilePath;

        if (oldTaskId != newTaskId) {
            task = taskService.getEntityById(newTaskId);
            file.setTask(task);

            log.info("Task with ID {} is set for File with ID: {}", task.getId(), file.getId());
        }

        newFilePath = String.format(
                TASK_FILE_FORMAT,
                task.getId(),
                FileUtils.generateFileName(multipartFile)
        );

        if (!Objects.equals(oldFilePath, newFilePath)) {
            throwExceptionIfFileExists(newFilePath);
        }

        newFilePath = storageService.uploadFile(
                newFilePath,
                FileUtils.getInputStreamOrElseThrow(multipartFile)
        );

        if (!Objects.equals(oldFilePath, newFilePath)) {
            storageService.deleteFile(oldFilePath);
        }

        file.setPath(newFilePath);

        file = fileRepository.save(file);
        FileResponseDto responseDto = fileMapper.toDto(file);

        log.info("Updated File with ID: {}", file.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting File with ID: {}", id);

        File file = getEntityById(id);
        fileRepository.delete(file);

        storageService.deleteFile(file.getPath());

        log.info("Deleted File with ID: {}", file.getId());
    }

    @Override
    public File getEntityById(long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "File with ID " + id + " does not exist"
                ));
    }

    private void throwExceptionIfFileExists(String path) {
        fileRepository.findByPath(path)
                .ifPresent(foundFile -> {
                    throw new EntityAlreadyExistsException(
                            "File with the path '" + foundFile.getPath() + "' already exists"
                    );
                });
    }

}
