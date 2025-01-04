package leonardo.labutilities.qualitylabpro.dtos.analytics;

import java.util.List;

public record GroupedMeanAndStdRecordByLevel(
        String level,
        List<MeanAndStdDeviationRecord> values) {
}
