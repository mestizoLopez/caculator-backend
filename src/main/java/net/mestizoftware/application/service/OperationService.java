package net.mestizoftware.application.service;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.repository.OperationRepository;
import net.mestizoftware.web.dto.CreateOperationDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;

    public Operation createOperation(CreateOperationDto dto) {
        Optional<Operation> existing = operationRepository.findByType(dto.getType());

        if (existing.isEmpty()) {
            Operation operation = Operation.builder()
                    .type(dto.getType())
                    .cost(dto.getCost())
                    .build();
            return operationRepository.save(operation);
        }
        return null;
    }

}
