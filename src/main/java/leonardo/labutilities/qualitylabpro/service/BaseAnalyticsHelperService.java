package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.AnalyticsValidationComponent;
import leonardo.labutilities.qualitylabpro.components.LevelConverterComponent;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseAnalyticsHelperService extends AnalyticsHelperService {

    public BaseAnalyticsHelperService(
            GenericAnalyticsRepository genericAnalyticsRepository,
            RulesValidatorComponent rulesValidatorComponent,
            AnalyticsValidationComponent analyticsValidationComponent,
            LevelConverterComponent levelConverterComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent, analyticsValidationComponent, levelConverterComponent);
    }

    @Override
    public List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevel(
            Pageable pageable, String name, String level) {
        return findAllAnalyticsByNameAndLevelProtected(pageable, name, level);
    }

    @Override
    public List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevelAndDate(
            String name, String level, String dateStart, String dateEnd) {
        return findAllAnalyticsByNameAndLevelAndDateProtected(name, level, dateStart, dateEnd);
    }
}
