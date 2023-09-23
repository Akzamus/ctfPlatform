package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.user.UserRequestDto;
import com.cycnet.ctfPlatform.dto.user.UserResponseDto;
import com.cycnet.ctfPlatform.enums.Role;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.UserMapper;
import com.cycnet.ctfPlatform.models.User;
import com.cycnet.ctfPlatform.repositories.UserRepository;
import com.cycnet.ctfPlatform.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponseDto<UserResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving Users, page number: {}, page size: {}", pageNumber, pageSize);

        Page<User> page = userRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<UserResponseDto> pageResponseDto = userMapper.toDto(page);

        log.info("Finished retrieving Users, page number: {}, page size: {}", pageNumber, pageSize);

        return pageResponseDto;
    }

    @Override
    public UserResponseDto getById(long id) {
        log.info("Retrieving User by ID: {}", id);

        User user = getEntityById(id);
        UserResponseDto responseDto = userMapper.toDto(user);

        log.info("Finished retrieving User by ID: {}", user.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto requestDto) {
        log.info("Creating new User with email: {}", requestDto.email());

        throwExceptionIfUserExists(requestDto.email());

        User user = userMapper.toEntity(requestDto);
        user.setPassword(
                passwordEncoder.encode(requestDto.password())
        );

        user = userRepository.save(user);
        UserResponseDto responseDto = userMapper.toDto(user);

        log.info("Created new User with ID: {}", user.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public UserResponseDto update(long id, UserRequestDto requestDto) {
        log.info("Updating User with ID: {}", id);

        User user = getEntityById(id);

        String oldEmail = user.getEmail();
        String newEmail = requestDto.email();

        if (!Objects.equals(oldEmail, newEmail)) {
            throwExceptionIfUserExists(newEmail);
            user.setEmail(newEmail);
        }

        user.setRole(
                Role.valueOf(requestDto.role())
        );
        user.setPassword(
                passwordEncoder.encode(requestDto.password())
        );

        user = userRepository.save(user);
        UserResponseDto responseDto = userMapper.toDto(user);

        log.info("Updated User with ID: {}", user.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting User with ID: {}", id);

        User user = getEntityById(id);
        userRepository.delete(user);

        log.info("Deleted User with ID: {}", user.getId());
    }

    @Override
    public User getEntityById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));
    }

    @Override
    public void throwExceptionIfUserExists(String email) {
        userRepository.findByEmail(email)
                .ifPresent(foundUser -> {
                    throw new EntityAlreadyExistsException(
                            "User with the email " + foundUser.getEmail() + " already exists."
                    );
                });
    }

}
