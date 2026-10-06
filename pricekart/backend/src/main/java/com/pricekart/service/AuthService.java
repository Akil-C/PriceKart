package com.pricekart.service;

import com.pricekart.dto.auth.LoginRequest;
import com.pricekart.dto.auth.RegisterRequest;
import com.pricekart.entity.RefreshToken;
import com.pricekart.entity.Role;
import com.pricekart.entity.User;
import com.pricekart.repository.RefreshTokenRepository;
import com.pricekart.repository.RoleRepository;
import com.pricekart.repository.UserRepository;
import com.pricekart.util.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Service handling authentication, registration, token refresh and logout.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }
        Role role = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role"));
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .enabled(true)
                .roles(Collections.singleton(role))
                .build();
        return userRepository.save(user);
    }

    /** Authenticate and generate access + refresh tokens */
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(request.getUsername());
        RefreshToken refreshToken = createRefreshToken(userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        return new AuthResponse(jwt, refreshToken.getToken(), request.getUsername());
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
        return refreshTokenRepository.save(token);
    }

    public AuthResponse refresh(String requestToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token expired");
        }
        String jwt = jwtUtils.generateJwtToken(refreshToken.getUser().getUsername());
        return new AuthResponse(jwt, refreshToken.getToken(), refreshToken.getUser().getUsername());
    }

    public void logout(String token) {
        refreshTokenRepository.deleteByUserId(getCurrentUserId());
    }

    public User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .accountLocked(!user.isEnabled())
                .build();
    }

    /** Simple DTO for authentication responses */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;
        private String username;
    }
}
