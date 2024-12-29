package leonardo.labutilities.qualitylabpro.dto.analytics;

import leonardo.labutilities.qualitylabpro.infra.config.exception.CustomGlobalErrorHandling;

import java.util.List;

public record GenericValuesGroupByLevel(
    String level,
    List<GenericValuesRecord> values
) {}