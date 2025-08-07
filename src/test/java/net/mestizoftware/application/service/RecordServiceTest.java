package net.mestizoftware.application.service;

import net.mestizoftware.domain.exception.RecordNotFoundException;
import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.model.OperationType;
import net.mestizoftware.domain.model.Record;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.RecordRepository;
import net.mestizoftware.web.dto.RecordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;
    @InjectMocks
    private RecordService recordService;

    private User user;

    @BeforeEach
    void setUp() {
        openMocks(this);
        user = new User(1L, "javier@mestizoftware.com", "password", "active", 100.0);
    }


    @Test
    void shouldReturnRecord() {
        Operation operation = new Operation(1L, OperationType.ADDITION, 1.0);

        Record record = new Record(1L, operation, user, 10.0, 90.0, "20.0", LocalDateTime.now(), false);
        Page<Record> page = new PageImpl<>(List.of(record));

        when(recordRepository.findByUserAndDeletedFalse(eq(user), any(Pageable.class))).thenReturn(page);

        Page<RecordDto> result = recordService.findByUserAndDeletedFalse(user, Pageable.unpaged());

        assertEquals(1, result.getContent().size());
        RecordDto dto = result.getContent().get(0);
        assertEquals("javier@mestizoftware.com", dto.getUsername());
        assertEquals(OperationType.ADDITION, dto.getOperation());
    }

    @Test
    void shouldThrowExceptionWhenNoRecordsFound() {
        when(recordRepository.findByUserAndDeletedFalse(eq(user), any(Pageable.class)))
                .thenReturn(Page.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            recordService.findByUserAndDeletedFalse(user, Pageable.unpaged());
        });

        assertEquals("No records found for the user " + user.getUsername(), exception.getMessage());
    }

    @Test
    void shouldSoftDeleteRecordWhenUserMatches() {
        Record record = Record.builder()
                .id(1L)
                .user(user)
                .deleted(false)
                .build();

        when(recordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(recordRepository.findById(1L)).thenReturn(Optional.of(record));

        recordService.softDeleteRecord(1L, user.getId());

        assertTrue(record.isDeleted());
        verify(recordRepository).save(record);
    }

    @Test
    void shouldReturnFalseWhenRecordNotFound() {
        when(recordRepository.findById(1L)).thenReturn(Optional.empty());

        Boolean result = recordService.softDeleteRecord(1L, user.getId());

        assertFalse(result);
    }


}