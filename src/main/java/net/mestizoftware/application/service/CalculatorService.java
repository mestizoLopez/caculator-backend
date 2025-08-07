package net.mestizoftware.application.service;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.domain.exception.InsufficientBalanceException;
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
                .orElseThrow(() -> new IllegalArgumentException("Operation type not found"));

        double  balance = user.getBalance();

        if (balance < operation.getCost()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        double result = calculate(type, balance, input);
        user.setBalance(result - operation.getCost());
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

    private double calculate(OperationType type, double balance, double input) {
        return switch (type) {
            case ADDITION -> balance + input;
            case SUBTRACTION -> balance - input;
            case MULTIPLICATION -> balance * input;
            case DIVISION ->  {
                if(input == 0) {
                    throw new IllegalArgumentException("Input must be greater than zero");
                }
                yield  balance / input;
            }
            case SQUARE_ROOT -> Math.sqrt(input);
            default -> throw new UnsupportedOperationException("Invalid operation");
        };
    }

}
