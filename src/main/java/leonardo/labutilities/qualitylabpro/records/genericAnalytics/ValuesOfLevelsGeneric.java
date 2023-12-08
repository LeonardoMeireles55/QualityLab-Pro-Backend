package leonardo.labutilities.qualitylabpro.records.genericAnalytics;
import leonardo.labutilities.qualitylabpro.main.GenericAnalytics;


public record ValuesOfLevelsGeneric(
        String date,
        String level_lot,
        String test_lot,
        String name,
        String level,
        Double value,
        Double mean,
        Double sd,
        String unit_value,
        String rules,
        String description
) {
    public ValuesOfLevelsGeneric(GenericAnalytics analytics) {
        this(analytics.getDate(), analytics.getLevel_lot(), analytics.getTest_lot(),
                analytics.getName(), analytics.getLevel(),
                analytics.getValue(), analytics.getMean(), analytics.getSd(), analytics.getUnit_value(),
                analytics.getRules(), analytics.getDescription());
    }
}
