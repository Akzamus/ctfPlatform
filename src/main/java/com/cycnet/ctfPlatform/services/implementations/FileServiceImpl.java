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
import com.cycnet.ctfPlatform.repositories.TaskRepository;
import com.cycnet.ctfPlatform.services.FileService;
import com.cycnet.ctfPlatform.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final TaskRepository taskRepository;
    private final StorageService storageService;
    private final FileMapper fileMapper;
    private final String TASK_FOLDER = "tasks";

    @Override
    public PageResponseDto<FileResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving files, page number: {}, page size : {}", pageNumber, pageSize);

        Page<File> filePage = fileRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<FileResponseDto> studentPageResponseDto = fileMapper.toDto(filePage);

        log.info("Finished retrieving files, page number: {}, page size : {}", pageNumber, pageSize);

        return studentPageResponseDto;
    }

    @Override
    public FileResponseDto getById(long id) {
        log.info("Retrieving file by ID: {}", id);

        FileResponseDto fileResponseDto = fileRepository.findById(id)
                .map(fileMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("File with ID " + id + " does not exist."));

        log.info("Finished retrieving file by ID: {}", fileResponseDto.id());

        return fileResponseDto;
    }

    @Override
    @Transactional
    public FileResponseDto create(FileRequestDto requestDto) {
        log.info("Creating a new file for task with ID: {}", requestDto.taskId());

        Task task = taskRepository.findById(requestDto.taskId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Task with ID " + requestDto.taskId() + " does not exist."
                ));

        MultipartFile multipartFile = requestDto.file();
        String folderPath = TASK_FOLDER + "/" + task.getId();
        String filePath = folderPath + "/" + multipartFile.getOriginalFilename().replaceAll(" ", "");

        throwExceptionIfFileWithTaskAndPathExists(task, filePath);

        filePath = storageService.uploadFile(folderPath, multipartFile);

        File file = fileMapper.toEntity(requestDto);
        file.setPath(filePath);
        file.setTask(task);

        file = fileRepository.save(file);
        FileResponseDto fileResponseDto = fileMapper.toDto(file);

        log.info("Created a new file with ID: {}", file.getId());

        return fileResponseDto;
    }

    @Override
    @Transactional
    public FileResponseDto update(long id, FileRequestDto requestDto) {
        log.info("Updating a new file with id: {}", id);

        File file = fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "File with ID " + id + " does not exist."
                ));

        MultipartFile multipartFile = requestDto.file();
        String folderPath = TASK_FOLDER + "/" + requestDto.taskId();
        String filePath = folderPath + "/" + multipartFile.getOriginalFilename().replaceAll(" ", "");

        if (!file.getTask().getId().equals(requestDto.taskId())) {
            Task task = taskRepository.findById(requestDto.taskId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Task with ID " + requestDto.taskId() + " does not exist."
                    ));

            file.setTask(task);

            log.info("Task with ID {} has been set for file with ID: {}", task.getId(), file.getId());
        }

        if (!file.getPath().equals(filePath)) {
            throwExceptionIfFileWithTaskAndPathExists(file.getTask(), filePath);

            filePath = storageService.uploadFile(folderPath, multipartFile);
            storageService.deleteFile(file.getPath());
        } else {
            filePath = storageService.uploadFile(folderPath, multipartFile);
            log.info("File on the path '{}' is overwritten.",  filePath);
        }

        file.setPath(filePath);

        file = fileRepository.save(file);
        FileResponseDto fileResponseDto = fileMapper.toDto(file);

        log.info("Updated file with ID: {}", file.getId());

        return fileResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting file with ID: {}", id);

        File file = fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "File with ID " + id + " does not exist."
                ));

        fileRepository.delete(file);

        log.info("Deleted file with ID: {}", file.getId());

        storageService.deleteFile(file.getPath());
    }

    private void throwExceptionIfFileWithTaskAndPathExists(Task task, String path) {
        fileRepository.findByTaskAndPath(task, path)
                .ifPresent(foundFile -> {
                    throw new EntityAlreadyExistsException(
                            "A file with the path '" + foundFile.getPath() + "' already exists for task with ID: " +
                                    task.getId() + "."
                    );
                });
    }

}
