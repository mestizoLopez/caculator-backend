package net.mestizoftware.application.service;

import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.model.OperationType;
import net.mestizoftware.domain.repository.OperationRepository;
import net.mestizoftware.web.dto.CreateOperationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;
    @InjectMocks
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldCreateOperationWithCorrectTypeAndCost() {
        // Arrange
        OperationType type = OperationType.DIVISION;
        double cost = 3.5;

        CreateOperationDto dto = CreateOperationDto.builder()
                .type(type)
                .cost(cost)
                .build();

        when(operationRepository.save(any(Operation.class))).thenAnswer(inv -> inv.getArgument(0));

        Operation result = operationService.createOperation(dto);

        assertEquals(type, result.getType());
        assertEquals(cost, result.getCost());
    }

}