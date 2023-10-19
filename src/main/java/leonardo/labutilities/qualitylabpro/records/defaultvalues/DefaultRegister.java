package leonardo.labutilities.qualitylabpro.records.defaultvalues;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record DefaultRegister(
        @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
        String name,
        @Digits(integer=3, fraction=2)
        Double normaldp,
        @Digits(integer=3, fraction=2)
        Double normalmean,
        @Digits(integer=3, fraction=2)
        Double highdp,
        @Digits(integer=3, fraction=2)
        Double highmean
) {
}
