package net.mestizoftware.application.service;

import net.mestizoftware.domain.exception.InsufficientBalanceException;
import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.model.OperationType;
import net.mestizoftware.domain.model.Record;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.OperationRepository;
import net.mestizoftware.domain.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CalculatorServiceTest {

    @Mock
    private OperationRepository operationRepository;
    @Mock
    private RecordRepository recordRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CalculatorService calculatorService;

    private User user;

    @BeforeEach
    void setUp() {
        openMocks(this);
        user = new User(1L, "javier@mestizoftware.net", "secret", "active", 100.0);
    }

    @Test
    void shouldPerformAdditionAndDeductBalance(){

        Operation operation = new Operation(1L, OperationType.ADDITION, 2.0);

        when(operationRepository.findByType(OperationType.ADDITION)).thenReturn(Optional.of(operation));
        String result = calculatorService.performOperation(OperationType.ADDITION, 10, user);

        assertEquals("110.0", result);
        assertEquals(108.0, user.getBalance());

        verify(userService).update(user);
        verify(recordRepository).save(any(Record.class));
    }

    @Test
    void shouldThrowWhenInsufficientBalance() {

        Operation operation = new Operation(1L, OperationType.MULTIPLICATION, 5.0);
        user.setBalance(1.0);

        when(operationRepository.findByType(OperationType.MULTIPLICATION)).thenReturn(Optional.of(operation));

        assertThrows(InsufficientBalanceException.class, () -> {
            calculatorService.performOperation(OperationType.MULTIPLICATION, 2, user);
        });

        verify(userService, never()).update(any());
        verify(recordRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenOperationTypeNotFound() {

        when(operationRepository.findByType(OperationType.SQUARE_ROOT)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.performOperation(OperationType.SQUARE_ROOT, 9, new User());
        });

        assertEquals("Operation type not found", exception.getMessage());
        verify(userService, never()).update(any());
        verify(recordRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenInputIsZero() {
        Operation operation = new Operation(1L, OperationType.DIVISION, 5.0);

        when(operationRepository.findByType(OperationType.DIVISION)).thenReturn(Optional.of(operation));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.performOperation(OperationType.DIVISION, 0, user);
        });

        assertEquals("Input must be greater than zero", exception.getMessage());
        verify(userService, never()).update(any());
        verify(recordRepository, never()).save(any());
    }

}