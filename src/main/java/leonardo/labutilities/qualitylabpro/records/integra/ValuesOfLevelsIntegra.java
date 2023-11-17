package leonardo.labutilities.qualitylabpro.records.integra;
import leonardo.labutilities.qualitylabpro.main.Integra400;

public record ValuesOfLevelsIntegra(
        String date,
        String level_lot,
        String test_lot,
        String name,
        String level,
        Double value,
        Double mean,
        Double sd
) {
    public ValuesOfLevelsIntegra(Integra400 analytics) {
        this(analytics.getDate(), analytics.getLevel_lot(), analytics.getTest_lot(), analytics.getName(), analytics.getLevel(), analytics.getValue(), analytics.getMean(), analytics.getSd());
    }
}
