package leonardo.labutilities.qualitylabpro.services.analytics;

import java.time.LocalDateTime;
import java.util.List;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.repositories.GenericAnalyticsRepository;
import leonardo.labutilities.qualitylabpro.utils.components.RulesValidatorComponent;

import org.springframework.data.domain.Pageable;

public abstract class AbstractAnalyticsService extends AnalyticsHelperService {

    public AbstractAnalyticsService(
            GenericAnalyticsRepository genericAnalyticsRepository,
            RulesValidatorComponent rulesValidatorComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent);
    }

    public abstract List<GenericValuesRecord> findAllAnalyticsByNameAndLevel(
            Pageable pageable,
            String name,
            String level);

    public abstract List<GenericValuesRecord> findAllAnalyticsByNameAndLevelAndDate(
            String name,
            String level,
            LocalDateTime dateStart,
            LocalDateTime dateEnd);

    public abstract String convertLevel(String level);

    public abstract MeanAndStandardDeviationRecord generateMeanAndStandardDeviation(
            String name,
            String level,
            LocalDateTime dateStart,
            LocalDateTime dateEnd);

}
