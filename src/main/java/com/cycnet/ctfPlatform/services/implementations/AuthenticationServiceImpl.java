package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.auth.AuthenticationRequestDto;
import com.cycnet.ctfPlatform.dto.auth.AuthenticationResponseDto;
import com.cycnet.ctfPlatform.dto.auth.RegisterRequestDto;
import com.cycnet.ctfPlatform.enums.Role;
import com.cycnet.ctfPlatform.exceptions.auth.JwtSubjectMissingException;
import com.cycnet.ctfPlatform.exceptions.auth.JwtTokenExpiredException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.jwt.JwtFactory;
import com.cycnet.ctfPlatform.jwt.JwtParser;
import com.cycnet.ctfPlatform.jwt.JwtValidator;
import com.cycnet.ctfPlatform.models.Student;
import com.cycnet.ctfPlatform.models.User;
import com.cycnet.ctfPlatform.repositories.UserRepository;
import com.cycnet.ctfPlatform.services.AuthenticationService;
import com.cycnet.ctfPlatform.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtFactory jwtFactory;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        log.info("Received registration request for email: {}", request.email());

        userService.throwExceptionIfUserExists(request.email());

        Student student = Student.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .student(student)
                .build();

        student.setUser(user);
        userRepository.save(user);

        log.info("User saved to the database with email: {}", user.getEmail());

        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        log.info("User registered successfully with email: {}", user.getEmail());

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        log.info("Received authentication request for email: {}", request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );


        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with email " + request.email() + " not found"
                ));

        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        log.info("User authenticated successfully with email: {}", user.getEmail());

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto refreshToken(String authHeader) {
        log.info("Received refresh token request");

        String refreshToken = authHeader.substring(7);

        User user = jwtParser.extractEmail(refreshToken)
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new JwtSubjectMissingException("JWT subject cannot be null"))
                .orElseThrow(() -> new EntityNotFoundException("User with this email was not found"));

        jwtValidator.ifTokenExpiredThrow(refreshToken, () -> new JwtTokenExpiredException("Refresh token has expired"));

        String accessToken = jwtFactory.generateAccessToken(user);

        log.info("User refreshed token successfully with email: {}", user.getEmail());

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
