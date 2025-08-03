package net.mestizoftware.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mestizoftware.domain.model.OperationType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDto {
    private Long id;
    private OperationType operation;
    private Double amount;
    private Double userBalance;
    private String operationResponse;
    private LocalDateTime createdAt;
}
