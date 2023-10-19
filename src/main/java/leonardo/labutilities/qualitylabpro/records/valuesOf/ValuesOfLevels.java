package leonardo.labutilities.qualitylabpro.records.valuesOf;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ValuesOfLevels(
        @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
        String name,
        @Digits(integer=6, fraction=2)
        Double value1,
        @Digits(integer=6, fraction=2)
        Double value2
) {
}
