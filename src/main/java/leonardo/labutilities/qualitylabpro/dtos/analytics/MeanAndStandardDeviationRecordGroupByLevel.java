package leonardo.labutilities.qualitylabpro.dtos.analytics;

import java.util.List;

public record MeanAndStandardDeviationRecordGroupByLevel(
                String level,
                List<MeanAndStandardDeviationRecord> values) {
}