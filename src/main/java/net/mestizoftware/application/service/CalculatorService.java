package net.mestizoftware.application.service;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.model.OperationType;
import net.mestizoftware.domain.model.Record;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.OperationRepository;
import net.mestizoftware.domain.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final OperationRepository operationRepository;
    private final RecordRepository recordRepository;
    private final UserService userService;

    public String performOperation(OperationType type, double input, User user) {
        Operation operation = operationRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation"));

        if (user.getBalance() < operation.getCost()) {
            throw new IllegalStateException("Insufficient balance");
        }

        double result = calculate(type, input);
        user.setBalance(user.getBalance() - operation.getCost());
        userService.update(user);

        Record record = Record.builder()
                .user(user)
                .operation(operation)
                .amount(input)
                .userBalance(user.getBalance())
                .operationResponse(String.valueOf(result))
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();
        recordRepository.save(record);

        return String.valueOf(result);
    }

    private double calculate(OperationType type, double input) {
        return switch (type) {
            case ADDITION -> input + input;
            case SUBTRACTION -> input - input;
            case MULTIPLICATION -> input * input;
            case DIVISION -> input / (input == 0 ? 1 : input); // avoid division by zero
            case SQUARE_ROOT -> Math.sqrt(input);
            default -> throw new UnsupportedOperationException("Use external API for random string");
        };
    }

}
