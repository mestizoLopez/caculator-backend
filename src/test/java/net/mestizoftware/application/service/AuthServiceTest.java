package net.mestizoftware.application.service;

import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.UserRepository;
import net.mestizoftware.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    String email;
    String rawPassword;
    String encodedPassword;
    String token;

    @BeforeEach
    void setUp() {
        openMocks(this);
        email = "javier@mestizoftware.com";;
        rawPassword = "password123";
        encodedPassword = "encoded-pass";
        token = "jwt-token";
    }


    @Test
    void shouldRegisterNewUser() {

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation
                -> invocation.getArgument(0));

        User result = authService.register(email, rawPassword);

        assertEquals(email, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals("active", result.getStatus());
        assertEquals(100.0, result.getBalance());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnTokenOnSuccessfulLogin() {

        User user = new User(1L, email, encodedPassword, "active", 100.0);

        when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn(token);

        Optional<String> result = authService.login(email, rawPassword);

        assertTrue(result.isPresent());
        assertEquals(token, result.get());
    }

    @Test
    void shouldFailLoginWithWrongPassword() {

        User user = new User(1L, email, encodedPassword, "active", 100.0);

        when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        Optional<String> result = authService.login(email, rawPassword);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFailLoginIfUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Optional<String> result = authService.login(email, "password");

        assertTrue(result.isEmpty());
    }

}