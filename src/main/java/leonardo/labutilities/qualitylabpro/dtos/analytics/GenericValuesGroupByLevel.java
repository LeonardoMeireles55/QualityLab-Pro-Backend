package leonardo.labutilities.qualitylabpro.dtos.analytics;

import java.util.List;

public record GenericValuesGroupByLevel(
    String level,
    List<GenericValuesRecord> values
) {}