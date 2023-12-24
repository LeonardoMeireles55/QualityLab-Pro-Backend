package leonardo.labutilities.qualitylabpro.record.valuesOfAnalytics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.domain.entitys.DefaultValues;

public record ValuesOfRegistedRecord(
        Long id,
        @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
        String name,
        Double normaldp,
        Double normalmean,
        Double highdp,
        Double highmean) {
    public ValuesOfRegistedRecord(DefaultValues defaultValues) {
        this(defaultValues.getId(), defaultValues.getName(), defaultValues.getNormalSd(), defaultValues.getNormalMean(),
                defaultValues.getHighSd(), defaultValues.getHighMean());
    }
}
