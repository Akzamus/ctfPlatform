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
        log.info("Retrieving users, page number: {}, page size: {}", pageNumber, pageSize);

        Page<User> userPage = userRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<UserResponseDto> userPageResponseDto = userMapper.toDto(userPage);

        log.info("Finished retrieving users, page number: {}, page size: {}", pageNumber, pageSize);

        return userPageResponseDto;
    }

    @Override
    public UserResponseDto getById(long id) {
        log.info("Retrieving user by ID: {}", id);

        UserResponseDto userResponseDto = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));

        log.info("Finished retrieving user by ID: {}", userResponseDto.id());

        return userResponseDto;
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto userRequestDto) {
        log.info("Creating a new user with email: {}", userRequestDto.email());

        throwExceptionIfUserExists(userRequestDto.email());

        User user = userMapper.toEntity(userRequestDto);
        String password = userRequestDto.password();
        user.setPassword(passwordEncoder.encode(password));
        user = userRepository.save(user);
        UserResponseDto userResponseDto = userMapper.toDto(user);

        log.info("Created a new user with email: {}", user.getEmail());

        return userResponseDto;
    }

    @Override
    @Transactional
    public UserResponseDto update(long id, UserRequestDto requestDto) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));

        String newEmail = requestDto.email();

        if (!user.getEmail().equals(newEmail)) {
            throwExceptionIfUserExists(newEmail);
            user.setEmail(newEmail);
        }

        user.setRole(Role.valueOf(requestDto.role()));
        String password = requestDto.password();
        user.setPassword(passwordEncoder.encode(password));

        user = userRepository.save(user);
        UserResponseDto userResponseDto = userMapper.toDto(user);

        log.info("Updated user with ID: {}", id);

        return userResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting user with ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));

        userRepository.delete(existingUser);

        log.info("Deleted user with ID: {}", id);
    }

    private void throwExceptionIfUserExists(String email) {
        userRepository.findByEmail(email)
                .ifPresent(foundUser -> {
                    throw new EntityAlreadyExistsException(
                            "User with the email " + foundUser.getEmail() + " already exists."
                    );
                });
    }

}
