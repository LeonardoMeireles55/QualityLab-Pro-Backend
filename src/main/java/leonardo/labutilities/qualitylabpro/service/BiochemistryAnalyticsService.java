package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.AnalyticsValidationComponent;
import leonardo.labutilities.qualitylabpro.components.LevelConverterComponent;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BiochemistryAnalyticsService extends AnalyticsService {
    private final GenericAnalyticsRepository genericAnalyticsRepository;
    private final AnalyticsValidationComponent analyticsValidationComponent;
    private final LevelConverterComponent levelConverterComponent;

    public BiochemistryAnalyticsService
            (GenericAnalyticsRepository genericAnalyticsRepository,
             RulesValidatorComponent rulesValidatorComponent,
             AnalyticsValidationComponent analyticsValidationComponent,
             LevelConverterComponent levelConverterComponent
            ) {
        super(genericAnalyticsRepository, rulesValidatorComponent, analyticsValidationComponent, levelConverterComponent);
        this.genericAnalyticsRepository = genericAnalyticsRepository;
        this.analyticsValidationComponent = analyticsValidationComponent;
        this.levelConverterComponent = levelConverterComponent;
    }

    @Override
    public List<ValuesOfLevelsGenericRecord> getAllResultsByNameAndLevelAndDate
            (String name, String level, String dateStart, String dateEnd) {
        analyticsValidationComponent.ensureNameExists(name);
        return findResultsCobasByNameLevelAndDateRange(name, level, dateStart, dateEnd);
    }

    private List<ValuesOfLevelsGenericRecord> findResultsCobasByNameLevelAndDateRange(
            String name, String level, String dateStart, String dateEnd) {
        Optional<List<GenericAnalytics>> analyticsOptional = Optional.ofNullable(genericAnalyticsRepository
                .findAllByNameAndLevelAndDateBetween(name.toUpperCase(),
                        levelConverterComponent.convertLevel(level), dateStart, dateEnd));

        return analyticsOptional.orElseThrow(() ->
                        new ErrorHandling.ResourceNotFoundException("Results not found."))
                .stream().map(ValuesOfLevelsGenericRecord::new).toList();
    }
}
