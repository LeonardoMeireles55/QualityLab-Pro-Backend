package leonardo.labutilities.qualitylabpro.record.genericAnalytics;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.domain.entitys.GenericAnalytics;


public record ValuesOfLevelsGenericRecord(
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",
                message = "Date format invalid.")
        String date,
        @NotNull
        String levelLot,
        @NotNull
        String testLot,
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
        String unitValue,
        String rules,
        String description
) {
    public ValuesOfLevelsGenericRecord(GenericAnalytics analytics) {
        this(analytics.getDate(), analytics.getLevelLot(), analytics.getTestLot(),
                analytics.getName(), analytics.getLevel(),
                analytics.getValue(), analytics.getMean(), analytics.getSd(), analytics.getUnitValue(),
                analytics.getRules(), analytics.getDescription());
    }
}
