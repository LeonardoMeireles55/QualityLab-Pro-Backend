package leonardo.labutilities.qualitylabpro.dto.analytics;

import java.util.List;

public record MeanAndStandardDeviationRecordGroupByLevel(
        String level,
        List<MeanAndStandardDeviationRecord> values
) {

}