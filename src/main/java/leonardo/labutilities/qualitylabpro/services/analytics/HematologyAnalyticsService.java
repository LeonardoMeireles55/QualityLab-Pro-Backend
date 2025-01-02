package leonardo.labutilities.qualitylabpro.services.analytics;

import java.time.LocalDateTime;
import java.util.List;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.configs.exception.CustomGlobalErrorHandling;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.repositories.GenericAnalyticsRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HematologyAnalyticsService extends AbstractAnalyticsService {

    public HematologyAnalyticsService(
        GenericAnalyticsRepository genericAnalyticsRepository,
        RulesValidatorComponent rulesValidatorComponent
    ) {
        super(genericAnalyticsRepository, rulesValidatorComponent);
    }

    @Override
    public List<GenericValuesRecord> findAllAnalyticsByNameAndLevel(
        Pageable pageable,
        String name,
        String level
    ) {
        ensureNameExists(name);
        return findAllGenericAnalyticsByNameAndLevel(pageable, name, convertLevel(level));
    }

    @Override
    public List<GenericValuesRecord> findAllAnalyticsByNameAndLevelAndDate(
        String name,
        String level,
        LocalDateTime dateStart,
        LocalDateTime dateEnd
    ) {
        ensureNameExists(name);
        return findAllGenericAnalyticsByNameAndLevelAndDate(
            name,
            convertLevel(level),
            dateStart,
            dateEnd
        );
    }

    @Override
    public String convertLevel(String inputLevel) {
        return switch (inputLevel) {
            case "1" -> "low";
            case "2" -> "normal";
            case "3" -> "high";
            default -> throw new CustomGlobalErrorHandling.ResourceNotFoundException(
                "Level not found."
            );
        };
    }

    @Override
    public MeanAndStandardDeviationRecord generateMeanAndStandardDeviation(
        String name,
        String level,
        LocalDateTime dateStart,
        LocalDateTime dateEnd
    ) {
        var filteredResult = findAllAnalyticsByNameAndLevelAndDate(name, level, dateStart, dateEnd);

        double sum = filteredResult.stream().mapToDouble(GenericValuesRecord::value).sum();

        List<Double> values = filteredResult.stream().map(GenericValuesRecord::value).toList();

        int count = filteredResult.size();

        return calculateMeanAndStandardDeviation(sum, count, values);
    }
}
