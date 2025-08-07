package net.mestizoftware.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.mestizoftware.application.service.AuthService;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.UserRepository;
import net.mestizoftware.infrastructure.security.JwtUtil;
import net.mestizoftware.infrastructure.security.TestSecurityConfig;
import net.mestizoftware.web.dto.AuthRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    private AuthRequestDto authRequest;
    private User user;
    private final String EMAIL = "javier@mestizoftware.net";
    private final String PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        authRequest = AuthRequestDto.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .build();
        user = User.builder()
                .id(1L)
                .username(EMAIL)
                .password("password123")
                .status("ACTIVE")
                .balance(100.0)
                .build();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        when(authService.register(eq(EMAIL), eq(PASSWORD)))
                .thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(EMAIL))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.balance").value(100.0));

        verify(authService).register(EMAIL, PASSWORD);
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        when(authService.login(eq(EMAIL), eq(PASSWORD)))
                .thenReturn(Optional.of(token));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));

        verify(authService).login(EMAIL, PASSWORD);
    }

    @Test
    void shouldReturn401WhenLoginFails() throws Exception {
        when(authService.login(eq(EMAIL), eq(PASSWORD)))
                .thenReturn(Optional.empty());

        AuthRequestDto invalidRequest = AuthRequestDto.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").value("Invalid credentials"));

        verify(authService).login(EMAIL, PASSWORD);
    }


}