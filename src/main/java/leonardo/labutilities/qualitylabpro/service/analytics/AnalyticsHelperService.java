package leonardo.labutilities.qualitylabpro.service.analytics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.infra.config.exception.CustomGlobalErrorHandling;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public abstract class AnalyticsHelperService implements IAnalyticsHelperService {

    private final GenericAnalyticsRepository genericAnalyticsRepository;
    private final RulesValidatorComponent rulesValidatorComponent;

    public AnalyticsHelperService(
        GenericAnalyticsRepository genericAnalyticsRepository,
        RulesValidatorComponent rulesValidatorComponent
    ) {
        this.genericAnalyticsRepository = genericAnalyticsRepository;
        this.rulesValidatorComponent = rulesValidatorComponent;
    }

    public List<GenericValuesRecord> getAllByNameInAndDateBetween(
        List<String> names,
        LocalDateTime dateStart,
        LocalDateTime dateEnd
    ) {
        return genericAnalyticsRepository
            .findAllByNameInAndDateBetween(names, dateStart, dateEnd)
            .stream()
            .toList();
    }

    public abstract List<GenericValuesRecord> findAllAnalyticsByNameAndLevel(
        Pageable pageable,
        String name,
        String level
    );

    public void submitAnalytics(List<GenericValuesRecord> valuesOfLevelsList) {
        List<GenericAnalytics> newAnalytics = valuesOfLevelsList
            .stream()
            .filter(this::doesNotExist)
            .map(values -> new GenericAnalytics(values, rulesValidatorComponent))
            .collect(Collectors.toList());

        if (newAnalytics.isEmpty()) {
            throw new CustomGlobalErrorHandling.DataIntegrityViolationException();
        }

        genericAnalyticsRepository
            .saveAll(newAnalytics)
            .stream()
            .map(GenericValuesRecord::new)
            .toList();
    }

    @Cacheable(value = "name")
    public List<GenericValuesRecord> findAll(Pageable pageable) {
        return genericAnalyticsRepository
            .findAll(pageable)
            .map(GenericValuesRecord::new)
            .stream()
            .collect(Collectors.collectingAndThen(Collectors.toList(), this::ensureResultsFound));
    }

    @Cacheable(value = "name")
    public List<GenericValuesRecord> findAnalyticsByName(Pageable pageable, String name) {
        List<GenericValuesRecord> analyticsList = genericAnalyticsRepository.findAllByName(
            pageable,
            name.toUpperCase()
        );
        return analyticsList
            .stream()
            .collect(Collectors.collectingAndThen(Collectors.toList(), this::ensureResultsFound));
    }

    @Cacheable(value = "id")
    public GenericAnalytics findAnalyticsById(Long id) {
        return genericAnalyticsRepository
            .findById(id)
            .orElseThrow(() ->
                new CustomGlobalErrorHandling.ResourceNotFoundException("Results not found.")
            );
    }

    List<GenericValuesRecord> findAllGenericAnalyticsByNameAndLevel(
        Pageable pageable,
        String name,
        String level
    ) {
        List<GenericValuesRecord> analyticsList = genericAnalyticsRepository.findAllByNameAndLevel(
            pageable,
            name.toUpperCase(),
            level
        );
        return analyticsList.stream().toList();
    }

    Pageable pageable = PageRequest.of(0, 80);

    List<GenericValuesRecord> findAllGenericAnalyticsByNameAndLevelAndDate(
        String name,
        String level,
        LocalDateTime dateStart,
        LocalDateTime dateEnd
    ) {
        return genericAnalyticsRepository
            .findAllByNameAndLevelAndDateBetween(name, level, dateStart, dateEnd, pageable)
            .stream()
            .toList();
    }

    public List<GenericValuesRecord> findAllAnalyticsByDate(
        LocalDateTime dateStart,
        LocalDateTime dateEnd
    ) {
        Optional<List<GenericValuesRecord>> analyticsOptional = Optional.ofNullable(
            genericAnalyticsRepository.findAllByDateBetween(dateStart, dateEnd)
        );

        return new ArrayList<>(
            analyticsOptional.orElseThrow(() ->
                new CustomGlobalErrorHandling.ResourceNotFoundException("Results not found.")
            )
        );
    }

    public void removeAnalyticsById(Long id) {
        if (!genericAnalyticsRepository.existsById(id)) {
            throw new CustomGlobalErrorHandling.ResourceNotFoundException(
                "Analytics result does not exist."
            );
        }
        genericAnalyticsRepository.deleteById(id);
    }

    public void ensureNameExists(String name) {
        if (!genericAnalyticsRepository.existsByName(name.toUpperCase())) {
            throw new CustomGlobalErrorHandling.ResourceNotFoundException("Results not found.");
        }
    }

    public boolean doesNotExist(GenericValuesRecord values) {
        return !genericAnalyticsRepository.existsByDateAndLevelAndName(
            values.date(),
            values.level(),
            values.name()
        );
    }

    public List<GenericValuesRecord> ensureResultsFound(List<GenericValuesRecord> results) {
        if (results.isEmpty()) {
            throw new CustomGlobalErrorHandling.ResourceNotFoundException("Results not found.");
        }
        return results;
    }
}
