package leonardo.labutilities.qualitylabpro.dto.analytics;

import java.util.List;

public record GenericValuesGroupByLevel(
    String level,
    List<GenericValuesRecord> values
) {}