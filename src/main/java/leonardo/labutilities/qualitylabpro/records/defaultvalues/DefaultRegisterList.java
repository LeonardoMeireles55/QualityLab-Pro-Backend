package leonardo.labutilities.qualitylabpro.records.defaultvalues;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.analytics.DefaultValues;

public record DefaultRegisterList(
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
    public DefaultRegisterList(DefaultValues defaultValues) {
        this(defaultValues.getName(), defaultValues.getNormalDp(),
                defaultValues.getNormalMean(), defaultValues.getHighDp(),
                defaultValues.getHighMean(), defaultValues.getNormalMaxValue());
    }
}
