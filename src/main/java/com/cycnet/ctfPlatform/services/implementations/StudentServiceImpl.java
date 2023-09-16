package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.student.StudentRequestDto;
import com.cycnet.ctfPlatform.dto.student.StudentResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.exceptions.user.UserAlreadyLinkedStudentException;
import com.cycnet.ctfPlatform.mappers.StudentMapper;
import com.cycnet.ctfPlatform.models.Student;
import com.cycnet.ctfPlatform.models.User;
import com.cycnet.ctfPlatform.repositories.StudentRepository;
import com.cycnet.ctfPlatform.repositories.UserRepository;
import com.cycnet.ctfPlatform.services.StudentService;
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
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;


    @Override
    public PageResponseDto<StudentResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving students, page number: {}, page size : {}", pageNumber, pageSize);

        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<StudentResponseDto> studentPageResponseDto = studentMapper.toDto(studentPage);

        log.info("Finished retrieving students, page number: {}, page size : {}", pageNumber, pageSize);

        return studentPageResponseDto;
    }

    @Override
    public StudentResponseDto getById(long id) {
        log.info("Retrieving student with ID: {}", id);

        StudentResponseDto studentResponseDto = studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID " + id + " doest not exists"));

        log.info("Finished retrieving student by ID: {}", id);

        return studentResponseDto;
    }

    @Override
    @Transactional
    public StudentResponseDto create(StudentRequestDto requestDto) {
        log.info("Creating a new student for user with ID: {}", requestDto.userId());

        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with ID " + requestDto.userId() + " does not exist."
                ));

        Student student = user.getStudent();

        if (Objects.nonNull(student)) {
            throw new UserAlreadyLinkedStudentException(
                    "User already linked to student with ID : " + student.getId()
            );
        }

        student = studentMapper.toEntity(requestDto);

        student.setUser(user);
        user.setStudent(student);

        log.info("Student is linked to user with id: {}", user.getId());

        student = studentRepository.save(student);
        StudentResponseDto studentResponseDto = studentMapper.toDto(student);

        log.info("Created a new student with ID: {}", student.getId());

        return studentResponseDto;
    }

    @Override
    @Transactional
    public StudentResponseDto update(long id, StudentRequestDto requestDto) {
        log.info("Updating student with ID: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID " + id + " does not exist."));

        log.info("Found existing student with ID: {} ", existingStudent.getId());

        long newUserId = requestDto.userId();

        if (existingStudent.getUser().getId() != newUserId) {

            User user = userRepository.findById(newUserId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User with ID " + newUserId + " does not exist."
                    ));

            Student student = user.getStudent();

            if (Objects.nonNull(student)) {
                throw new UserAlreadyLinkedStudentException(
                        "User already linked to student with ID : " + student.getId()
                );
            }

            existingStudent.setUser(user);
            user.setStudent(existingStudent);

            log.info(
                    "User ID has changed from {} to {}. Updating student...",
                    existingStudent.getUser().getId(),
                    user.getId()
            );

        }

        existingStudent.setFirstName(requestDto.firstName());
        existingStudent.setLastName(requestDto.lastName());

        existingStudent = studentRepository.save(existingStudent);
        StudentResponseDto studentResponseDto = studentMapper.toDto(existingStudent);

        log.info("Updated student with ID: {}", existingStudent.getId());

        return studentResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting student with ID: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + id + " does not exist."));

        studentRepository.delete(existingStudent);

        log.info("Deleted student with ID: {}", id);
    }
}
