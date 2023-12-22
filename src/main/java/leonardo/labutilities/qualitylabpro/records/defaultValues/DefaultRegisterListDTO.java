package leonardo.labutilities.qualitylabpro.records.defaultValues;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.main.entitys.DefaultValues;

public record DefaultRegisterListDTO(
         @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
         String name,
//         @Digits(integer=6, fraction=2)
         Double normaldp,
         @Digits(integer=6, fraction=2)
         Double normalmean,
         @Digits(integer=6, fraction=2)
         Double highdp,
         @Digits(integer=6, fraction=2)
         Double highmean,
         Double normalmaxvalue) {
    public DefaultRegisterListDTO(DefaultValues defaultValues) {
        this(defaultValues.getName(), defaultValues.getNormalSd(),
                defaultValues.getNormalMean(), defaultValues.getHighSd(),
                defaultValues.getHighMean(), defaultValues.getNormalMaxValue());
    }
}