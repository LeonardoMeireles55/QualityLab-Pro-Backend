package leonardo.labutilities.qualitylabpro.records.valuesOfAnalytics;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.domain.entitys.Analytics;

public record ValuesOfLevelsListRecord(
        @NotBlank @Pattern(regexp = "^[^0-9]+$", message = "test name must contain only letters.")
        String name,
        @Digits(integer=6, fraction=2)
        Double normalValue,
        @Digits(integer=6, fraction=2)
        Double highValue,
        String normalValid, String highValid, String normalObs, String highObs) {
    public ValuesOfLevelsListRecord(Analytics analytics) {
        this(analytics.getName(), analytics.getNormalValue(), analytics.getHighValue(), analytics.getNormalValid(), analytics.getHighValid(), analytics.getNormalObs(), analytics.getHighObs());
    }
}
