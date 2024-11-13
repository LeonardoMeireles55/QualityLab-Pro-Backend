package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.infra.exception.CustomGlobalErrorHandling;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BiochemistryAnalyticsService extends AbstractAnalyticsHelperService {

    public BiochemistryAnalyticsService(
            GenericAnalyticsRepository genericAnalyticsRepository,
            RulesValidatorComponent rulesValidatorComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent);
    }

    @Override
    public List<ValuesOfLevelsGenericRecord>
    findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level) {
        ensureNameExists(name);
        return findAllAnalyticsByNameAndLevelProtected(pageable, name,
                convertLevel(level));
    }

    @Override
    public List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevelAndDate
            (String name, String level, String dateStart, String dateEnd) {
        ensureNameExists(name);
        return findAllAnalyticsByNameAndLevelAndDateProtected
                                (name, convertLevel(level), dateStart, dateEnd);
    }
    @Override
    public String convertLevel(String inputLevel) {
        return switch (inputLevel) {
            case "1" -> "PCCC1";
            case "2" -> "PCCC2";
            default -> throw new CustomGlobalErrorHandling.ResourceNotFoundException("Level not found.");
        };
    }

    @Override
    public MeanAndStandardDeviationRecord generateMeanAndStandardDeviation(String name, String level, String dateStart, String dateEnd) {

        var filteredResult =
                getFilteredRecords(findAllAnalyticsByNameAndLevelAndDate(name, level, dateStart, dateEnd));

        double sum = filteredResult.stream().mapToDouble(ValuesOfLevelsGenericRecord::value).sum();

        int count = filteredResult.size();

        return calculateMeanAndStandardDeviation(sum, count);
    }
}
