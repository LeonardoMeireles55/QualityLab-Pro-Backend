package leonardo.labutilities.qualitylabpro.dtos.analytics;

import java.util.List;

public record GroupedValuesByLevel(String level, List<AnalyticsRecord> values) {
}
