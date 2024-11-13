package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.AnalyticsValidationComponent;
import leonardo.labutilities.qualitylabpro.components.LevelConverterComponent;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BiochemistryAnalyticsService extends BaseAnalyticsHelperService {

    public BiochemistryAnalyticsService(
            GenericAnalyticsRepository genericAnalyticsRepository,
            RulesValidatorComponent rulesValidatorComponent,
            AnalyticsValidationComponent analyticsValidationComponent,
            LevelConverterComponent levelConverterComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent, analyticsValidationComponent, levelConverterComponent);
    }

    public List<ValuesOfLevelsGenericRecord>
    findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level) {
        ensureNameExists(name);
        return findAllAnalyticsByNameAndLevelProtected(pageable, name,
                convertLevelToCobas(level));
    }

    public List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevelAndDate
            (String name, String level, String dateStart, String dateEnd) {
        ensureNameExists(name);
                return findAllAnalyticsByNameAndLevelAndDateProtected
                                (name, convertLevelToCobas(level), dateStart, dateEnd);
    }
}
