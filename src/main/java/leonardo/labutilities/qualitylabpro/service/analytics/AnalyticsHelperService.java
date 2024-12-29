package leonardo.labutilities.qualitylabpro.service.analytics;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.*;
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

    private final Pageable pageable = PageRequest.of(0, 80);

    public AnalyticsHelperService(
            GenericAnalyticsRepository genericAnalyticsRepository,
            RulesValidatorComponent rulesValidatorComponent
    ) {
        this.genericAnalyticsRepository = genericAnalyticsRepository;
        this.rulesValidatorComponent = rulesValidatorComponent;
    }

    public boolean shouldIncludeRecord(GenericValuesRecord record) {
        String rules = record.rules();
        return (!Objects.equals(rules, "+3s") && !Objects.equals(rules, "-3s"));
    }

    public boolean groupedShouldIncludeRecord(GenericValuesGroupByLevel record) {
        return record.values().stream()
                .allMatch(genericValuesRecord ->
                        !Objects.equals(genericValuesRecord.rules(), "+3s") &&
                                !Objects.equals(genericValuesRecord.rules(), "-3s")
                );
    }

    public List<GenericValuesRecord> getFilteredRecords(List<GenericValuesRecord> records) {
        return records.stream().filter(this::shouldIncludeRecord).toList();
    }
    public List<GenericValuesGroupByLevel> getGroupedFilteredRecords(List<GenericValuesGroupByLevel> records) {
        return records.stream()
                .filter(this::groupedShouldIncludeRecord).map((GenericValuesGroupByLevel record) -> {
                    List<GenericValuesRecord> filteredRecords = getFilteredRecords(record.values());
                    return new GenericValuesGroupByLevel(record.level(), filteredRecords);
                }).toList();
    }

    public List<GenericValuesGroupByLevel> getGroupedByLevel(String name, LocalDateTime startDate, LocalDateTime endDate) {
        List<GenericValuesRecord> records = genericAnalyticsRepository.findAllByNameAndDateBetweenGroupByLevel(name, startDate, endDate, pageable);
        return records.stream()
                .collect(Collectors.groupingBy(GenericValuesRecord::level))
                .entrySet()
                .stream()
                .map(entry -> new GenericValuesGroupByLevel(entry.getKey(), entry.getValue())).toList();
    }

    public MeanAndStandardDeviationRecord calculateMeanAndStandardDeviation(
            Double totalValue,
            Integer size,
            List<Double> values
    ) {
        double mean = totalValue / size;

        double variance =
                values.stream().mapToDouble(value -> Math.pow(value - mean, 2)).sum() / size;

        double standardDeviation = Math.sqrt(variance);

        return new MeanAndStandardDeviationRecord(mean, standardDeviation);
    }
    public List<GenericResultsGroupByLevel> getGroupedResults(String name, LocalDateTime startDate, LocalDateTime endDate) {
        List<GenericValuesGroupByLevel> analytics = getGroupedByLevel(name, startDate, endDate);
        var filteredRecords = getGroupedFilteredRecords(analytics);

        List<MeanAndStandardDeviationRecordGroupByLevel> meanAndStandardDeviation =
                calculateMeanAndStandardDeviationGrouped(filteredRecords);
        return analytics.stream()
                .map(analytic -> {
                    var matchingStats = meanAndStandardDeviation.stream()
                            .filter(stat -> stat.level().equals(analytic.level()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("No matching statistics found for level: " + analytic.level()));

                    return new GenericResultsGroupByLevel(analytic, matchingStats);
                })
                .collect(Collectors.toList());
    }
    public List<MeanAndStandardDeviationRecordGroupByLevel> calculateMeanAndStandardDeviationGrouped(
            List<GenericValuesGroupByLevel> records
    ) {
        return records.stream()
                .map(group -> {
                    double sum = group.values().stream().mapToDouble(GenericValuesRecord::value).sum();
                    List<Double> values = group.values().stream().map(GenericValuesRecord::value).toList();
                    int count = group.values().size();
                    MeanAndStandardDeviationRecord stats = calculateMeanAndStandardDeviation(sum, count, values);
                    return new MeanAndStandardDeviationRecordGroupByLevel(group.level(), Collections.singletonList(stats));
                }).toList();
    }

    public MeanAndStandardDeviationRecord generateMeanAndStandardDeviation(
            String name,
            String level,
            LocalDateTime dateStart,
            LocalDateTime dateEnd
    ) {
        var filteredResult = getFilteredRecords(
                findAllAnalyticsByNameAndLevelAndDate(name, level, dateStart, dateEnd)
        );

        double sum = filteredResult.stream().mapToDouble(GenericValuesRecord::value).sum();

        List<Double> values = filteredResult.stream().map(GenericValuesRecord::value).toList();

        int count = filteredResult.size();

        return calculateMeanAndStandardDeviation(sum, count, values);
    }

    public List<MeanAndStandardDeviationRecordGroupByLevel> generateMeanAndStandardDeviationGrouped(
            String name, LocalDateTime startDate, LocalDateTime endDate
    ) {
        List<GenericValuesRecord> records = genericAnalyticsRepository.findAllByNameAndDateBetweenGroupByLevel(name, startDate, endDate, pageable);

        var values = records.stream()
                .collect(Collectors.groupingBy(GenericValuesRecord::level))
                .entrySet()
                .stream()
                .map(entry -> new GenericValuesGroupByLevel(entry.getKey(), entry.getValue())).toList();

        return calculateMeanAndStandardDeviationGrouped(values);
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
            .saveAll(newAnalytics);
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
