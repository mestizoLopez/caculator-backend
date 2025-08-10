package net.mestizoftware.web.controller;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.application.service.CalculatorService;
import net.mestizoftware.application.service.OperationService;
import net.mestizoftware.domain.model.Operation;
import net.mestizoftware.domain.model.OperationType;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.web.dto.CreateOperationDto;
import net.mestizoftware.web.dto.OperationRequestDto;
import net.mestizoftware.web.dto.OperationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"{http://localhost:4200"
        ,"http://calculator-backend.s3-website-us-east-1.amazonaws.com/"}, allowCredentials = "true")
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class OperationController {

    private final CalculatorService calculatorService;
    private final OperationService operationService;

    @PostMapping("/create")
    public ResponseEntity<Operation> createOperation(@RequestBody CreateOperationDto dto) {
        Operation created = operationService.createOperation(dto);
        return ResponseEntity.ok(created);
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
