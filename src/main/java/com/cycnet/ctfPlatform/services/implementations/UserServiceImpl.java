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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponseDto<UserResponseDto> getAll(int pageNumber, int pageSize) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return userMapper.toDto(userPage);
    }

    @Override
    public UserResponseDto getById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto userRequestDto) {
        userRepository.findByEmail(userRequestDto.email())
                .ifPresent(foundUser-> {
                    throw new EntityAlreadyExistsException(
                            "User with the email " + foundUser.getEmail() + " already exists."
                    );
                });

        User user = userMapper.toEntity(userRequestDto);
        String password = userRequestDto.password();
        user.setPassword(passwordEncoder.encode(password));
        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto update(long id, UserRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));

        String newEmail = requestDto.email();

        if(!user.getEmail().equals(newEmail)) {
            userRepository.findByEmail(newEmail)
                    .ifPresent(foundUser -> {
                        throw new EntityAlreadyExistsException(
                                "User with the email " + foundUser.getEmail() + " already exists."
                        );
                    });
            user.setEmail(newEmail);
        }

        user.setRole(Role.valueOf(requestDto.role()));
        String password = requestDto.password();
        user.setPassword(passwordEncoder.encode(password));

        user = userRepository.save(user);


        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));

        userRepository.delete(existingUser);
    }

}
