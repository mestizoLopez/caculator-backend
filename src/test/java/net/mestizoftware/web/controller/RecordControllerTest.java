package net.mestizoftware.web.controller;

import net.mestizoftware.application.service.RecordService;
import net.mestizoftware.domain.model.OperationType;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.UserRepository;
import net.mestizoftware.infrastructure.security.JwtUtil;
import net.mestizoftware.infrastructure.security.TestSecurityConfig;
import net.mestizoftware.web.dto.RecordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecordController.class)
@Import(TestSecurityConfig.class)
class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecordService recordService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    private RecordDto recordDto;

    private static final String EMAIL = "javier@mestizoftware.net";
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
        when(userRepository.findByUsername(EMAIL)).thenReturn(java.util.Optional.of(user));

        recordDto = RecordDto.builder()
                .id(1L)
                .username(EMAIL)
                .operation(OperationType.ADDITION)
                .amount(10.0)
                .userBalance(90.0)
                .operationResponse("20.0")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldReturnPagedRecords() throws Exception {
        Page<RecordDto> page = new PageImpl<>(List.of(recordDto), PageRequest.of(0, 10), 1);

        when(recordService.findByUserAndDeletedFalse(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/records")
                        .param("page", "0")
                        .param("size", "10")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value(EMAIL))
                .andExpect(jsonPath("$.content[0].operation").value("ADDITION"));
    }

    @Test
    void shouldReturn200WhenRecordDeleted() throws Exception {
        when(recordService.softDeleteRecord(1L, user.getId())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/records/1")
                .with(authentication(authentication)))
                .andExpect(status().isOk());
        verify(recordService).softDeleteRecord(1L, user.getId());
    }

    @Test
    void shouldReturn404WhenRecordNotDeleted() throws Exception {
        when(recordService.softDeleteRecord(eq(1L), any())).thenReturn(false);

        mockMvc.perform(delete("/api/v1/records/1")
                        .requestAttr("user", user)
                        .with(authentication(authentication)))
                .andExpect(status().isNotFound());

        verify(recordService).softDeleteRecord(1L, user.getId());
    }

}