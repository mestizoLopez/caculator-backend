package net.mestizoftware.web.dto;

import lombok.Data;
import net.mestizoftware.domain.model.OperationType;

@Data
public class CreateOperationDto {

    private OperationType type;
    private double cost;

}
