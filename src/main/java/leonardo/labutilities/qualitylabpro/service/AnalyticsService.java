package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.AnalyticsValidationComponent;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.components.LevelConverterComponent;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.dto.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnalyticsService {

    private final GenericAnalyticsRepository genericAnalyticsRepository;
    private final RulesValidatorComponent rulesValidatorComponent;
    private final AnalyticsValidationComponent analyticsValidationComponent;
    private final LevelConverterComponent levelConverterComponent;

    public AnalyticsService(GenericAnalyticsRepository genericAnalyticsRepository,
                            RulesValidatorComponent rulesValidatorComponent,
                            AnalyticsValidationComponent analyticsValidationComponent,
                            LevelConverterComponent levelConverterComponent) {
        this.genericAnalyticsRepository = genericAnalyticsRepository;
        this.rulesValidatorComponent = rulesValidatorComponent;
        this.analyticsValidationComponent = analyticsValidationComponent;
        this.levelConverterComponent = levelConverterComponent;
    }

    public List<GenericAnalytics> submitAnalyticsValues(List<ValuesOfLevelsGenericRecord> valuesOfLevelsList) {
        List<GenericAnalytics> newAnalytics = valuesOfLevelsList.stream()
                .filter(analyticsValidationComponent::doesNotExist)
                .map(values -> new GenericAnalytics(values, rulesValidatorComponent))
                .collect(Collectors.toList());

        return genericAnalyticsRepository.saveAll(newAnalytics);
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> listAllResults(Pageable pageable) {
        return genericAnalyticsRepository.findAll(pageable).map(ValuesOfLevelsGenericRecord::new)
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), analyticsValidationComponent::ensureResultsFound));
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> findResultsByName(Pageable pageable, String name) {
        List<GenericAnalytics> analyticsList = genericAnalyticsRepository.findAllByName(pageable, name.toUpperCase());
        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), analyticsValidationComponent::ensureResultsFound));
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> findResultsByNameOrderedByDate(String name, String order) {
        List<GenericAnalytics> analyticsList = order.equalsIgnoreCase("asc") ?
                genericAnalyticsRepository.findAllByNameOrderByDateAsc(name.toUpperCase()) :
                genericAnalyticsRepository.findAllByNameOrderByDateDesc(name.toUpperCase());

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), analyticsValidationComponent::ensureResultsFound));
    }

    @Cacheable(value = "id")
    public GenericAnalytics findResultsById(Long id) {
        return genericAnalyticsRepository.findById(id)
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));
    }

    @Cacheable(value = { "name", "level" })
    public List<ValuesOfLevelsGenericRecord> getAllResultsByNameAndLevel(Pageable pageable, String name, String level) {
        List<GenericAnalytics> analyticsList = genericAnalyticsRepository
                .findAllByNameAndLevel(pageable, name.toUpperCase(), levelConverterComponent.convertLevelACL(level));

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), analyticsValidationComponent::ensureResultsFound));
    }

    public List<ValuesOfLevelsGenericRecord> getAllResultsByNameAndLevelAndDate
            (String name, String level, String dateStart, String dateEnd) {
        analyticsValidationComponent.ensureNameExists(name);
                return genericAnalyticsRepository.findAllByNameAndLevelAndDateBetween(name, level, dateStart, dateEnd);
    }

    public List<ValuesOfLevelsGenericRecord> getAllResultsByDate(String dateStart, String dateEnd) {
        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepository
                .findAllByDateBetween(dateStart, dateEnd);

        return analyticsOptional.orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."))
                .stream().map(ValuesOfLevelsGenericRecord::new).collect(Collectors.toList());
    }

    public void removeAnalyticsById(Long id) {
        if (!genericAnalyticsRepository.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException("Analytics result does not exist.");
        }
        genericAnalyticsRepository.deleteById(id);
    }
}
