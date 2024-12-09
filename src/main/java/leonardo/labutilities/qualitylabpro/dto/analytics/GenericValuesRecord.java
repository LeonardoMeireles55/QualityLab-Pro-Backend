package leonardo.labutilities.qualitylabpro.dto.analytics;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;


public record GenericValuesRecord(
        Long id,
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",
                message = "Date format invalid. Please use 'YYYY-MM-DD HH:MM:SS'.")
        String date,
        @NotNull
        String level_lot,
        @NotNull
        String test_lot,
        @NotNull
        String name,
        @NotNull()
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

    @JsonIgnore
    public Long id() {
        return id;
    }
    public GenericValuesRecord(GenericAnalytics analytics) {
        this(analytics.getId(), analytics.getDate(), analytics.getLevelLot(), analytics.getTestLot(),
                analytics.getName(), analytics.getLevel(),
                analytics.getValue(), analytics.getMean(), analytics.getSd(), analytics.getUnitValue(),
                analytics.getRules(), analytics.getDescription());
    }
}
