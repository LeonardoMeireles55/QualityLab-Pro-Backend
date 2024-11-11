package leonardo.labutilities.qualitylabpro.components;

import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.dto.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsValidationComponent {
    private final GenericAnalyticsRepository genericAnalyticsRepository;

    public void ensureNameExists(String name) {
        if (!genericAnalyticsRepository.existsByName(name.toUpperCase())) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }
    }

    public boolean doesNotExist(ValuesOfLevelsGenericRecord values) {
        return !genericAnalyticsRepository.existsByDateAndLevelAndName(
                values.date(), values.level(), values.name());
    }

    public List<ValuesOfLevelsGenericRecord> ensureResultsFound(List<ValuesOfLevelsGenericRecord> results) {
        if (results.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }
        return results;
    }
}
