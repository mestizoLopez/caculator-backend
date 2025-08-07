package net.mestizoftware.web.dto;

import lombok.Builder;
import lombok.Data;
import net.mestizoftware.domain.model.OperationType;

@Data
@Builder
public class CreateOperationDto {

    private OperationType type;
    private double cost;

}
