package com.crm.authservice.service;

import com.crm.authservice.dto.AuthResponse;
import com.crm.authservice.dto.LoginRequest;
import com.crm.authservice.dto.RegisterRequest;
import com.crm.authservice.exception.AuthErrorCode;
import com.crm.authservice.model.User;
import com.crm.authservice.repository.UserRepository;
import com.crm.authservice.security.JwtService;
import com.crm.commonerror.core.AppException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new AppException(AuthErrorCode.USER_ALREADY_EXISTS, Map.of("email", request.email()));
        }

        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(request.email(), passwordHash, "USER");
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(AuthErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new AppException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}