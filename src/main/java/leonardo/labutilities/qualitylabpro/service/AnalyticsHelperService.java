package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.infra.exception.CustomGlobalErrorHandling;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public abstract class AnalyticsHelperService implements IAnalyticsHelperService {

    private final GenericAnalyticsRepository genericAnalyticsRepository;
    private final RulesValidatorComponent rulesValidatorComponent;
    public AnalyticsHelperService(GenericAnalyticsRepository genericAnalyticsRepository,
                                  RulesValidatorComponent rulesValidatorComponent) {
        this.genericAnalyticsRepository = genericAnalyticsRepository;
        this.rulesValidatorComponent = rulesValidatorComponent;
    }

    public List<GenericAnalytics> submitAnalytics(List<ValuesOfLevelsGenericRecord> valuesOfLevelsList) {
        List<GenericAnalytics> newAnalytics = valuesOfLevelsList.stream()
                .filter(this::doesNotExist)
                .map(values -> new GenericAnalytics(values, rulesValidatorComponent))
                .collect(Collectors.toList());

        if (newAnalytics.isEmpty()) {
            throw new CustomGlobalErrorHandling.DataIntegrityViolationException();
        }

        return genericAnalyticsRepository.saveAll(newAnalytics);
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> findAll(Pageable pageable) {
        return genericAnalyticsRepository.findAll(pageable).map(ValuesOfLevelsGenericRecord::new)
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), this::ensureResultsFound));
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> findAnalyticsByName(Pageable pageable, String name) {
        List<GenericAnalytics> analyticsList = genericAnalyticsRepository.findAllByName(pageable, name.toUpperCase());
        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), this::ensureResultsFound));
    }

    @Cacheable(value = "id")
    public GenericAnalytics findAnalyticsById(Long id) {
        return genericAnalyticsRepository.findById(id)
                .orElseThrow(() -> new CustomGlobalErrorHandling.ResourceNotFoundException("Results not found."));
    }

    protected List<ValuesOfLevelsGenericRecord>
    findAllAnalyticsByNameAndLevelProtected(Pageable pageable, String name, String level) {
        List<GenericAnalytics> analyticsList = genericAnalyticsRepository
                .findAllByNameAndLevel(pageable, name.toUpperCase(), level);

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), this::ensureResultsFound));
    }

    protected List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevelAndDateProtected(
            String name, String level, String dateStart, String dateEnd) {
        return genericAnalyticsRepository.findAllByNameAndLevelAndDateBetween(name, level, dateStart, dateEnd)
                .stream().map(ValuesOfLevelsGenericRecord::new).toList();
    }

    public List<ValuesOfLevelsGenericRecord> findAllAnalyticsByDate(String dateStart, String dateEnd) {
        Optional<List<GenericAnalytics>> analyticsOptional = Optional.ofNullable(genericAnalyticsRepository
                .findAllByDateBetween(dateStart, dateEnd));

        return analyticsOptional.orElseThrow(() -> new CustomGlobalErrorHandling.ResourceNotFoundException("Results not found."))
                .stream().map(ValuesOfLevelsGenericRecord::new).collect(Collectors.toList());
    }

    public void removeAnalyticsById(Long id) {
        if (!genericAnalyticsRepository.existsById(id)) {
            throw new CustomGlobalErrorHandling.ResourceNotFoundException("Analytics result does not exist.");
        }
        genericAnalyticsRepository.deleteById(id);
    }

    public void ensureNameExists(String name) {
        if (!genericAnalyticsRepository.existsByName(name.toUpperCase())) {
            throw new CustomGlobalErrorHandling.ResourceNotFoundException("Results not found.");
        }
    }

    public boolean doesNotExist(ValuesOfLevelsGenericRecord values) {
        return !genericAnalyticsRepository.existsByDateAndLevelAndName(
                values.date(), values.level(), values.name());
    }

    public List<ValuesOfLevelsGenericRecord> ensureResultsFound(List<ValuesOfLevelsGenericRecord> results) {
        if (results.isEmpty()) {
            throw new CustomGlobalErrorHandling.ResourceNotFoundException("Results not found.");
        }
        return results;
    }
}
