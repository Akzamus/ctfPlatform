package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.auth.AuthenticationRequestDto;
import com.cycnet.ctfPlatform.dto.auth.AuthenticationResponseDto;
import com.cycnet.ctfPlatform.dto.auth.RegisterRequestDto;
import com.cycnet.ctfPlatform.enums.Role;
import com.cycnet.ctfPlatform.exceptions.auth.JwtSubjectMissingException;
import com.cycnet.ctfPlatform.exceptions.auth.JwtTokenExpiredException;
import com.cycnet.ctfPlatform.exceptions.user.UserAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.user.UserNotFoundException;
import com.cycnet.ctfPlatform.jwt.JwtFactory;
import com.cycnet.ctfPlatform.jwt.JwtParser;
import com.cycnet.ctfPlatform.jwt.JwtValidator;
import com.cycnet.ctfPlatform.models.Student;
import com.cycnet.ctfPlatform.models.User;
import com.cycnet.ctfPlatform.repositories.UserRepository;
import com.cycnet.ctfPlatform.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtFactory jwtFactory;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        userRepository.findByEmail(request.email())
                .ifPresent(foundUser -> {
                    throw new UserAlreadyExistsException(
                            "User with the email " + foundUser.getEmail() + " already exists."
                    );
                });

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

        userRepository.save(user);

        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );


        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User with this email was not found"));


        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto refreshToken(String authHeader) {
        String refreshToken = authHeader.substring(7);

        User user = jwtParser.extractEmail(refreshToken)
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new JwtSubjectMissingException("JWT subject cannot be null"))
                .orElseThrow(() -> new UserNotFoundException("User with this email was not found"));

        jwtValidator.ifTokenExpiredThrow(refreshToken, () -> new JwtTokenExpiredException("Refresh token has expired"));

        String accessToken = jwtFactory.generateAccessToken(user);

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
