package leonardo.labutilities.qualitylabpro.service.analytics;

import java.time.LocalDateTime;
import java.util.List;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import org.springframework.data.domain.Pageable;

public abstract class AbstractAnalyticsHelperService extends AnalyticsHelperService {

    public AbstractAnalyticsHelperService(
        GenericAnalyticsRepository genericAnalyticsRepository,
        RulesValidatorComponent rulesValidatorComponent
    ) {
        super(genericAnalyticsRepository, rulesValidatorComponent);
    }

    public abstract List<GenericValuesRecord> findAllAnalyticsByNameAndLevel(
        Pageable pageable,
        String name,
        String level
    );

    public abstract List<GenericValuesRecord> findAllAnalyticsByNameAndLevelAndDate(
        String name,
        String level,
        LocalDateTime dateStart,
        LocalDateTime dateEnd
    );

    public abstract String convertLevel(String level);

    public abstract MeanAndStandardDeviationRecord generateMeanAndStandardDeviation(
        String name,
        String level,
        LocalDateTime dateStart,
        LocalDateTime dateEnd
    );



}
