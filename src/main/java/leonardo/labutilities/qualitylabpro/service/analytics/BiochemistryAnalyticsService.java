package leonardo.labutilities.qualitylabpro.service.analytics;

import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.infra.config.exception.CustomGlobalErrorHandling;
import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BiochemistryAnalyticsService extends AbstractAnalyticsHelperService {

    public BiochemistryAnalyticsService(
            GenericAnalyticsRepository genericAnalyticsRepository,
            RulesValidatorComponent rulesValidatorComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent);
    }


    @Override
    public List<GenericValuesRecord>
    findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level) {
        ensureNameExists(name);
        return findAllGenericAnalyticsByNameAndLevel(pageable, name,
                convertLevel(level));
    }

    @Override
    public List<GenericValuesRecord> findAllAnalyticsByNameAndLevelAndDate
            (String name, String level, LocalDateTime dateStart, LocalDateTime dateEnd) {
        ensureNameExists(name);
        return findAllGenericAnalyticsByNameAndLevelAndDate
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
    public MeanAndStandardDeviationRecord
    generateMeanAndStandardDeviation(String name, String level, LocalDateTime dateStart, LocalDateTime dateEnd) {

        var filteredResult =
                getFilteredRecords(findAllAnalyticsByNameAndLevelAndDate(name, level, dateStart, dateEnd));

        double sum = filteredResult.stream().mapToDouble(GenericValuesRecord::value).sum();

        List<Double> values = filteredResult.stream()
                .map(GenericValuesRecord::value)
                .toList();

        int count = filteredResult.size();

        return calculateMeanAndStandardDeviation(sum, count, values);
    }

}
