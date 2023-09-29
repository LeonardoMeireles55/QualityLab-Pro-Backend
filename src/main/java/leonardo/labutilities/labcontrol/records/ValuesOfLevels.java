package leonardo.labutilities.labcontrol.records;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record ValuesOfLevels(
        String name,
        Double value1,
        Double value2
) {
}
