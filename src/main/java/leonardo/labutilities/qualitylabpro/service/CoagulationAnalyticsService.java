package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.AnalyticsValidationComponent;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.components.LevelConverterComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// Derived class
@Service
public class CoagulationAnalyticsService extends BaseAnalyticsHelperService {

    public CoagulationAnalyticsService(GenericAnalyticsRepository genericAnalyticsRepository,
                                       RulesValidatorComponent rulesValidatorComponent,
                                       AnalyticsValidationComponent analyticsValidationComponent,
                                       LevelConverterComponent levelConverterComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent, analyticsValidationComponent,
                levelConverterComponent);
    }

    @Override
    public List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level) {
        this.ensureNameExists(name);
        return this.findAllAnalyticsByNameAndLevelProtected(pageable, name,
                this.convertLevelToACL(level));
    }

    @Override
    public List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevelAndDate
            (String name, String level, String dateStart, String dateEnd) {
        this.ensureNameExists(name);
        return this.findAllAnalyticsByNameAndLevelAndDateProtected(name.toUpperCase(),
                this.convertLevelToACL(level), dateStart, dateEnd);
    }
}