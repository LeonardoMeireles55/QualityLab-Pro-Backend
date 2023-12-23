package leonardo.labutilities.qualitylabpro.records.defaultValues;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record DefaultRegisterRecord(
        @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
        String name,
        @Digits(integer=3, fraction=2)
        Double normalsd,
        @Digits(integer=3, fraction=2)
        Double normalmean,
        @Digits(integer=3, fraction=2)
        Double highsd,
        @Digits(integer=3, fraction=2)
        Double highmean,
        Long lotId,

        Long user_id
) {
}
