package leonardo.labutilities.qualitylabpro.records.valuesOfAnalytics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.main.DefaultValues;

public record ValuesOfRegistedDTO(
        Long id,
        @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
        String name,
        Double normaldp,
        Double normalmean,
        Double highdp,
        Double highmean) {
    public ValuesOfRegistedDTO(DefaultValues defaultValues) {
        this(defaultValues.getId(), defaultValues.getName(), defaultValues.getNormalSd(), defaultValues.getNormalMean(),
                defaultValues.getHighSd(), defaultValues.getHighMean());
    }
}
