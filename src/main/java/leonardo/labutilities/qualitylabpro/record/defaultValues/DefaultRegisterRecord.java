package leonardo.labutilities.qualitylabpro.record.defaultValues;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record DefaultRegisterRecord(
        @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
        String name,
        @Digits(integer=3, fraction=2)
        Double normalSd,
        @Digits(integer=3, fraction=2)
        Double normalMean,
        @Digits(integer=3, fraction=2)
        Double highSd,
        @Digits(integer=3, fraction=2)
        Double highMean,
        Long lotId,
        Long user_id
) {
}
