package net.mestizoftware.web.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.mestizoftware.application.service.CalculatorService;
import net.mestizoftware.domain.model.OperationType;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.web.dto.OperationRequestDto;
import net.mestizoftware.web.dto.OperationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/operations")
public class OperationController {

    private final CalculatorService calculatorService;

    public OperationController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/{type}")
    public ResponseEntity<OperationResponseDto> perform(
            @PathVariable OperationType type,
            @RequestBody OperationRequestDto request,
            @AuthenticationPrincipal User user
    ) {
        String result = calculatorService.performOperation(type, request.getInput(), user);
        return ResponseEntity.status(200).body(new OperationResponseDto(result));
    }

}
