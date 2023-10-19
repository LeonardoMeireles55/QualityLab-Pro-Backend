package leonardo.labutilities.qualitylabpro.records.valuesOf;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.analytics.DefaultValues;

public record ValuesOfRegisted(
        Long id,
        @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
        String name,
        Double normaldp,
        Double normalmean,
        Double highdp,
        Double highmean) {
    public ValuesOfRegisted(DefaultValues defaultValues) {
        this(defaultValues.getId(), defaultValues.getName(), defaultValues.getNormalDp(), defaultValues.getNormalMean(),
                defaultValues.getHighDp(), defaultValues.getHighMean());
    }
}
