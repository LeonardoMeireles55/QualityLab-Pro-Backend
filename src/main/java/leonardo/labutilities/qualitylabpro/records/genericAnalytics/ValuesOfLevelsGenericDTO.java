package leonardo.labutilities.qualitylabpro.records.genericAnalytics;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.domain.entitys.GenericAnalytics;


public record ValuesOfLevelsGenericDTO(
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",
                message = "Date format invalid.")
        String date,
        @NotNull
        String level_lot,
        @NotNull
        String test_lot,
        @NotNull
        String name,
        @NotNull
        String level,
        @NotNull
        Double value,
        @NotNull
        Double mean,
        @NotNull
        Double sd,
        @NotNull
        String unit_value,
        String rules,
        String description
) {
    public ValuesOfLevelsGenericDTO(GenericAnalytics analytics) {
        this(analytics.getDate(), analytics.getLevel_lot(), analytics.getTest_lot(),
                analytics.getName(), analytics.getLevel(),
                analytics.getValue(), analytics.getMean(), analytics.getSd(), analytics.getUnit_value(),
                analytics.getRules(), analytics.getDescription());
    }
}
