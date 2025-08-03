package net.mestizoftware.web.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OperationRequestDto {

    private double input;

    public double getInput() {
        return this.input;
    }

    public void setInput(double input) {
        this.input = input;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OperationRequestDto)) return false;
        final OperationRequestDto other = (OperationRequestDto) o;
        if (!other.canEqual((Object) this)) return false;
        if (Double.compare(this.getInput(), other.getInput()) != 0) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OperationRequestDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $input = Double.doubleToLongBits(this.getInput());
        result = result * PRIME + (int) ($input >>> 32 ^ $input);
        return result;
    }

    public String toString() {
        return "OperationRequestDto(input=" + this.getInput() + ")";
    }
}
