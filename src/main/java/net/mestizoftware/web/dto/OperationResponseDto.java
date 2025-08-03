package net.mestizoftware.web.dto;

import lombok.Data;

@Data
public class OperationResponseDto {
    private String result;

    public OperationResponseDto(String result) {
        this.result = result;
    }

    public OperationResponseDto() {
    }
}
