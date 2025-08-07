package net.mestizoftware.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.mestizoftware.application.service.CalculatorService;
import net.mestizoftware.application.service.OperationService;
import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.model.OperationType;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.UserRepository;
import net.mestizoftware.infrastructure.security.JwtUtil;
import net.mestizoftware.infrastructure.security.TestSecurityConfig;
import net.mestizoftware.web.dto.CreateOperationDto;
import net.mestizoftware.web.dto.OperationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OperationController.class)
@Import(TestSecurityConfig.class)
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CalculatorService calculatorService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private OperationService operationService;

    private final String EMAIL = "javier@mestizoftware.net";
    private User user;
    private TestingAuthenticationToken authentication;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .username(EMAIL)
                .password("password")
                .status("ACTIVE")
                .balance(100.0)
                .build();

        authentication = new TestingAuthenticationToken(user, null, "USER");
    }


    @Test
    void shouldCreateOperationSuccessfully() throws Exception {
        CreateOperationDto request = CreateOperationDto.builder()
                .type(OperationType.ADDITION)
                .cost(5.0)
                .build();

        Operation operation = Operation.builder()
                .id(1L)
                .type(OperationType.ADDITION)
                .cost(5.0)
                .build();

        when(operationService.createOperation(any(CreateOperationDto.class))).thenReturn(operation);

        mockMvc.perform(post("/api/v1/operations/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("ADDITION"))
                .andExpect(jsonPath("$.cost").value(5.0));

        verify(operationService).createOperation(any(CreateOperationDto.class));
    }

    @Test
    void shouldPerformAdditionOperationSuccessfully() throws Exception {
        OperationRequestDto request = OperationRequestDto.builder()
                .input(10.0)
                .build();

        when(calculatorService.performOperation(eq(OperationType.ADDITION), eq(10.0), eq(user)))
                .thenReturn("20.0");

        mockMvc.perform(post("/api/v1/operations/ADDITION")
                        .contentType("application/json")
                        .with(authentication(authentication))
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("20.0"));

        verify(calculatorService).performOperation(OperationType.ADDITION, 10.0, user);
    }

    @Test
    void shouldPerformSubtractionOperationSuccessfully() throws Exception {
        OperationRequestDto request = OperationRequestDto.builder()
                .input(5.0)
                .build();

        when(calculatorService.performOperation(eq(OperationType.SUBTRACTION), eq(5.0), eq(user)))
                .thenReturn("15.0");

        mockMvc.perform(post("/api/v1/operations/SUBTRACTION")
                        .contentType("application/json")
                        .with(authentication(authentication))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("15.0"));

        verify(calculatorService).performOperation(OperationType.SUBTRACTION, 5.0, user);
    }

    @Test
    void shouldPerformMultiplicationOperationSuccessfully() throws Exception {
        OperationRequestDto request = OperationRequestDto.builder()
                .input(3.0)
                .build();

        when(calculatorService.performOperation(eq(OperationType.MULTIPLICATION), eq(3.0), eq(user)))
                .thenReturn("30.0");

        mockMvc.perform(post("/api/v1/operations/MULTIPLICATION")
                        .contentType("application/json")
                        .with(authentication(authentication))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("30.0"));

        verify(calculatorService).performOperation(OperationType.MULTIPLICATION, 3.0, user);
    }

    @Test
    void shouldPerformDivisionOperationSuccessfully() throws Exception {
        OperationRequestDto request = OperationRequestDto.builder()
                .input(2.0)
                .build();

        when(calculatorService.performOperation(eq(OperationType.DIVISION), eq(2.0), eq(user)))
                .thenReturn("5.0");

        mockMvc.perform(post("/api/v1/operations/DIVISION")
                        .contentType("application/json")
                        .with(authentication(authentication))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("5.0"));

        verify(calculatorService).performOperation(OperationType.DIVISION, 2.0, user);
    }

    @Test
    void shouldPerformSquareRootOperationSuccessfully() throws Exception {
        OperationRequestDto request = OperationRequestDto.builder()
                .input(16.0)
                .build();

        when(calculatorService.performOperation(eq(OperationType.SQUARE_ROOT), eq(16.0), eq(user)))
                .thenReturn("4.0");

        mockMvc.perform(post("/api/v1/operations/SQUARE_ROOT")
                        .contentType("application/json")
                        .with(authentication(authentication))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("4.0"));

        verify(calculatorService).performOperation(OperationType.SQUARE_ROOT, 16.0, user);
    }

    @Test
    void shouldReturn400WhenInvalidOperationType() throws Exception {
        OperationRequestDto request = OperationRequestDto.builder()
                .input(10.0)
                .build();

        mockMvc.perform(post("/api/v1/operations/INVALID_TYPE")
                        .contentType("application/json")
                        .with(authentication(authentication))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}