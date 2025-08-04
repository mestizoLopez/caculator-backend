package net.mestizoftware.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mestizoftware.domain.model.OperationType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordDto {
    private Long id;
    private String username;
    private OperationType operation;
    private Double amount;
    private Double userBalance;
    private String operationResponse;
    private LocalDateTime createdAt;
}
