package leonardo.labutilities.qualitylabpro.services.analytics;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import leonardo.labutilities.qualitylabpro.dtos.analytics.*;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.repositories.GenericAnalyticsRepository;
import leonardo.labutilities.qualitylabpro.utils.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.utils.exception.CustomGlobalErrorHandling;
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
        private final Pageable pageable = PageRequest.of(0, 200);

        public AnalyticsHelperService(GenericAnalyticsRepository genericAnalyticsRepository,
                                      RulesValidatorComponent rulesValidatorComponent) {
                this.genericAnalyticsRepository = genericAnalyticsRepository;
                this.rulesValidatorComponent = rulesValidatorComponent;
        }

        public void deleteAnalyticsById(Long id) {
                if (!genericAnalyticsRepository.existsById(id)) {
                        throw new CustomGlobalErrorHandling.ResourceNotFoundException(
                                        "Analytics by id not found");
                }
                genericAnalyticsRepository.deleteById(id);
        }

        private List<Double> extractRecordValues(List<GenericValuesRecord> records) {
                return records.stream().map(GenericValuesRecord::value).toList();
        }

        public void ensureNameExists(String name) {
                if (!genericAnalyticsRepository.existsByName(name.toUpperCase())) {
                        throw new CustomGlobalErrorHandling.ResourceNotFoundException(
                                        "Analytics by name not found");
                }
        }

        public boolean isAnalyticsNonExistent(GenericValuesRecord values) {
                return !genericAnalyticsRepository.existsByDateAndLevelAndName(values.date(),
                                values.level(), values.name());
        }

        public List<GenericValuesRecord> validateAnalyticsNameExists(List<GenericValuesRecord> results) {
                if (results.isEmpty()) {
                        throw new CustomGlobalErrorHandling.ResourceNotFoundException(
                                        "Results not found.");
                }
                return results;
        }

        public boolean isRecordValid(GenericValuesRecord record) {
                String rules = record.rules();
                return (!Objects.equals(rules, "+3s") && !Objects.equals(rules, "-3s"));
        }

        public boolean isGroupedRecordValid(GroupedValuesByLevel record) {
                return record.values().stream().allMatch(genericValuesRecord -> !Objects
                        .equals(genericValuesRecord.rules(), "+3s")
                        && !Objects.equals(genericValuesRecord.rules(), "-3s"));
        }

        private void validateResultsNotEmpty(List<?> results, String message) {
                if (results == null || results.isEmpty()) {
                        throw new CustomGlobalErrorHandling.ResourceNotFoundException(message);
                }
        }

        private MeanAndStdDeviationRecord computeStatistics(List<Double> values) {
                double sum = values.stream().mapToDouble(Double::doubleValue).sum();
                int size = values.size();
                double mean = sum / size;
                double variance = values.stream().mapToDouble(value -> Math.pow(value - mean, 2))
                                .average().orElse(0.0);
                return new MeanAndStdDeviationRecord(mean, Math.sqrt(variance));
        }

        @Cacheable(cacheNames = "analytics-cache", key = "#records.hashCode()")
        public List<GroupedValuesByLevel> findFilteredGroupedAnalytics(
                        List<GroupedValuesByLevel> records) {
                return records.stream().filter(this::isGroupedRecordValid)
                                .map((GroupedValuesByLevel record) -> {
                                        List<GenericValuesRecord> filteredRecords = record.values();
                                        return new GroupedValuesByLevel(record.level(),
                                                        filteredRecords);
                                }).toList();
        }


        @Cacheable(cacheNames = "analytics-cache", key = "#name + '-' + #startDate.toString() + '-' + #endDate.toString() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
        public List<GroupedValuesByLevel> findGroupedAnalyticsByLevel(String name,
                                                                      LocalDateTime startDate, LocalDateTime endDate) {
                List<GenericValuesRecord> records =
                                genericAnalyticsRepository.findAllByNameAndDateBetweenGroupByLevel(
                                                name, startDate, endDate, pageable);
                validateResultsNotEmpty(records, "No analytics found for the given parameters");

                return records.stream().collect(Collectors.groupingBy(GenericValuesRecord::level))
                                .entrySet().stream()
                                .map(entry -> new GroupedValuesByLevel(entry.getKey(),
                                                entry.getValue())).toList();
        }
        @Cacheable(cacheNames = "analytics-cache", key = "#name + '-' + #startDate.toString() + '-' + #endDate.toString()")
        public List<GroupedResultsByLevel> findAnalyticsWithGroupedResults(String name,
                                                                           LocalDateTime startDate, LocalDateTime endDate) {
                List<GroupedValuesByLevel> analytics =
                                findGroupedAnalyticsByLevel(name, startDate, endDate);
                Map<String, MeanAndStdDeviationRecord> statsByLevel = analytics.stream()
                                .collect(Collectors.toMap(GroupedValuesByLevel::level,
                                                group -> computeStatistics(
                                                                extractRecordValues(group.values()))));

                return analytics.stream().map(analytic -> new GroupedResultsByLevel(analytic,
                                new GroupedMeanAndStdRecordByLevel(analytic.level(),
                                                Collections.singletonList(statsByLevel
                                                                .get(analytic.level())))))
                                .toList();
        }
        @Cacheable(cacheNames = "analytics-cache", key = "#records.hashCode()")
        public List<GroupedMeanAndStdRecordByLevel> getMeanAndStandardDeviationForGroups(
                        List<GroupedValuesByLevel> records) {
                return records.stream()
                                .map(group -> new GroupedMeanAndStdRecordByLevel(
                                                group.level(),
                                                Collections.singletonList(computeStatistics(
                                                                extractRecordValues(group.values())))))
                                .toList();
        }
        @Cacheable(cacheNames = "analytics-cache", key = "#name + '-' + #level + '-' + #dateStart.toString() + '-' + #dateEnd.toString()")
        public MeanAndStdDeviationRecord calculateMeanAndStandardDeviation(String name,
                                                                           String level, LocalDateTime dateStart, LocalDateTime dateEnd) {
                List<GenericValuesRecord> values = findAllAnalyticsByNameAndLevelAndDate(name,
                                level, dateStart, dateEnd).stream()
                                                .filter(this::isRecordValid)
                                                .toList();
                return computeStatistics(extractRecordValues(values));
        }
        @Cacheable(cacheNames = "analytics-cache", key = "#name + '-' + #startDate.toString() + '-' + #endDate.toString()")
        public List<GroupedMeanAndStdRecordByLevel> calculateGroupedMeanAndStandardDeviation(
                        String name, LocalDateTime startDate, LocalDateTime endDate) {
                List<GenericValuesRecord> records =
                                genericAnalyticsRepository.findAllByNameAndDateBetweenGroupByLevel(
                                                name, startDate, endDate, pageable);
                var values = records.stream()
                                .collect(Collectors.groupingBy(GenericValuesRecord::level))
                                .entrySet().stream()
                                .map(entry -> new GroupedValuesByLevel(entry.getKey(),
                                                entry.getValue()))
                                .toList();

                return getMeanAndStandardDeviationForGroups(values);
        }
        @Cacheable(cacheNames = "analytics-cache", key = "#names.hashCode() + '-' + #dateStart.toString() + '-' + #dateEnd.toString()")
        public List<GenericValuesRecord> getAllByNameInAndDateBetween(List<String> names,
                        LocalDateTime dateStart, LocalDateTime dateEnd) {
                return genericAnalyticsRepository
                                .findAllByNameInAndDateBetween(names, dateStart, dateEnd).stream()
                                .toList();
        }
        @Cacheable(cacheNames = "analytics-cache", key = "#names.hashCode() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
        public List<GenericValuesRecord> getAllByNameIn(List<String> names, Pageable pageable) {
                return genericAnalyticsRepository
                        .findAllByNameIn(names, pageable).stream()
                        .toList();
        }


        public abstract List<GenericValuesRecord> findAnalyticsByNameAndLevel(Pageable pageable,
                                                                              String name, String level);

        public void saveNewAnalyticsRecords(List<GenericValuesRecord> valuesOfLevelsList) {
                List<GenericAnalytics> newAnalytics =
                                valuesOfLevelsList.stream().filter(this::isAnalyticsNonExistent)
                                                .map(values -> new GenericAnalytics(values,
                                                                rulesValidatorComponent))
                                                .collect(Collectors.toList());

                if (newAnalytics.isEmpty()) {
                        throw new CustomGlobalErrorHandling.DataIntegrityViolationException();
                }

                genericAnalyticsRepository.saveAll(newAnalytics);
        }

        @Cacheable(value = "name")
        public List<GenericValuesRecord> findAll(Pageable pageable) {
                return genericAnalyticsRepository.findAll(pageable).map(GenericValuesRecord::new)
                                .stream().collect(Collectors.collectingAndThen(Collectors.toList(),
                                                this::validateAnalyticsNameExists));
        }

        @Cacheable(cacheNames = "analytics-cache", key = "#name + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
        public List<GenericValuesRecord> findAnalyticsByNameWithPagination(Pageable pageable, String name) {
                List<GenericValuesRecord> analyticsList = genericAnalyticsRepository
                                .findAllByName(name.toUpperCase(), pageable);
                validateResultsNotEmpty(analyticsList, "No analytics found with the given name");
                return analyticsList;
        }

        @Cacheable(cacheNames = "analytics-cache", key = "#id")
        public GenericAnalytics findById(Long id) {
                return genericAnalyticsRepository.findById(id).orElseThrow(
                                () -> new CustomGlobalErrorHandling.ResourceNotFoundException(
                                                "Results not found."));
        }

        @Cacheable(cacheNames = "analytics-cache", key = "#name + '-' + #level + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
        public List<GenericValuesRecord> findAnalyticsByNameAndLevelWithPagination(Pageable pageable, String name, String level) {
                List<GenericValuesRecord> analyticsList = genericAnalyticsRepository
                        .findAllByNameAndLevel(pageable, name.toUpperCase(), level);
                validateResultsNotEmpty(analyticsList, "No analytics found for the given name and level");
                return analyticsList;
        }
        @Cacheable(cacheNames = "analytics-cache", key = "#name + '-' + #level + '-' + #dateStart.toString() + '-' + #dateEnd.toString()")
        List<GenericValuesRecord> findAnalyticsByNameLevelAndDate(String name,
                                                                  String level, LocalDateTime dateStart, LocalDateTime dateEnd) {
                List<GenericValuesRecord> results =
                                genericAnalyticsRepository.findAllByNameAndLevelAndDateBetween(name,
                                                level, dateStart, dateEnd, pageable);
                validateResultsNotEmpty(results, "No analytics found for the given parameters");
                return results;
        }
        @Cacheable(cacheNames = "analytics-cache", key = "#dateStart.toString() + '-' + #dateEnd.toString()")
        public List<GenericValuesRecord> findAllAnalyticsByDate(LocalDateTime dateStart,
                        LocalDateTime dateEnd) {
                List<GenericValuesRecord> results =
                                genericAnalyticsRepository.findAllByDateBetween(dateStart, dateEnd);
                validateResultsNotEmpty(results, "No analytics found for the given date range");
                return results;
        }

}
