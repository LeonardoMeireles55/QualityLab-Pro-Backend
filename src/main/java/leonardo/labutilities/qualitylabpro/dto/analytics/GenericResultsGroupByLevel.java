package leonardo.labutilities.qualitylabpro.dto.analytics;

public record GenericResultsGroupByLevel(
        GenericValuesGroupByLevel genericValuesGroupByLevel,
        MeanAndStandardDeviationRecordGroupByLevel meanAndStandardDeviationRecordGroupByLevel
) {
}
