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

        public boolean isGroupedRecordValid(GenericValuesGroupByLevel record) {
                return record.values().stream().allMatch(genericValuesRecord -> !Objects
                        .equals(genericValuesRecord.rules(), "+3s")
                        && !Objects.equals(genericValuesRecord.rules(), "-3s"));
        }

        private void validateResultsNotEmpty(List<?> results, String message) {
                if (results == null || results.isEmpty()) {
                        throw new CustomGlobalErrorHandling.ResourceNotFoundException(message);
                }
        }

        private MeanAndStandardDeviationRecord computeStatistics(List<Double> values) {
                double sum = values.stream().mapToDouble(Double::doubleValue).sum();
                int size = values.size();
                double mean = sum / size;
                double variance = values.stream().mapToDouble(value -> Math.pow(value - mean, 2))
                                .average().orElse(0.0);
                return new MeanAndStandardDeviationRecord(mean, Math.sqrt(variance));
        }


        public List<GenericValuesGroupByLevel> findFilteredGroupedAnalytics(
                        List<GenericValuesGroupByLevel> records) {
                return records.stream().filter(this::isGroupedRecordValid)
                                .map((GenericValuesGroupByLevel record) -> {
                                        List<GenericValuesRecord> filteredRecords = record.values();
                                        return new GenericValuesGroupByLevel(record.level(),
                                                        filteredRecords);
                                }).toList();
        }



        public List<GenericValuesGroupByLevel> findGroupedAnalyticsByLevel(String name,
                                                                           LocalDateTime startDate, LocalDateTime endDate) {
                List<GenericValuesRecord> records =
                                genericAnalyticsRepository.findAllByNameAndDateBetweenGroupByLevel(
                                                name, startDate, endDate, pageable);
                validateResultsNotEmpty(records, "No analytics found for the given parameters");

                return records.stream().collect(Collectors.groupingBy(GenericValuesRecord::level))
                                .entrySet().stream()
                                .map(entry -> new GenericValuesGroupByLevel(entry.getKey(),
                                                entry.getValue())).toList();
        }

        public List<GenericResultsGroupByLevel> findAnalyticsWithGroupedResults(String name,
                                                                                LocalDateTime startDate, LocalDateTime endDate) {
                List<GenericValuesGroupByLevel> analytics =
                                findGroupedAnalyticsByLevel(name, startDate, endDate);
                Map<String, MeanAndStandardDeviationRecord> statsByLevel = analytics.stream()
                                .collect(Collectors.toMap(GenericValuesGroupByLevel::level,
                                                group -> computeStatistics(
                                                                extractRecordValues(group.values()))));

                return analytics.stream().map(analytic -> new GenericResultsGroupByLevel(analytic,
                                new MeanAndStandardDeviationRecordGroupByLevel(analytic.level(),
                                                Collections.singletonList(statsByLevel
                                                                .get(analytic.level())))))
                                .toList();
        }

        public List<MeanAndStandardDeviationRecordGroupByLevel> getMeanAndStandardDeviationForGroups(
                        List<GenericValuesGroupByLevel> records) {
                return records.stream()
                                .map(group -> new MeanAndStandardDeviationRecordGroupByLevel(
                                                group.level(),
                                                Collections.singletonList(computeStatistics(
                                                                extractRecordValues(group.values())))))
                                .toList();
        }

        public MeanAndStandardDeviationRecord calculateMeanAndStandardDeviation(String name,
                                                                                String level, LocalDateTime dateStart, LocalDateTime dateEnd) {
                List<GenericValuesRecord> values = findAllAnalyticsByNameAndLevelAndDate(name,
                                level, dateStart, dateEnd).stream()
                                                .filter(this::isRecordValid)
                                                .toList();
                return computeStatistics(extractRecordValues(values));
        }

        public List<MeanAndStandardDeviationRecordGroupByLevel> calculateGroupedMeanAndStandardDeviation(
                        String name, LocalDateTime startDate, LocalDateTime endDate) {
                List<GenericValuesRecord> records =
                                genericAnalyticsRepository.findAllByNameAndDateBetweenGroupByLevel(
                                                name, startDate, endDate, pageable);
                var values = records.stream()
                                .collect(Collectors.groupingBy(GenericValuesRecord::level))
                                .entrySet().stream()
                                .map(entry -> new GenericValuesGroupByLevel(entry.getKey(),
                                                entry.getValue()))
                                .toList();

                return getMeanAndStandardDeviationForGroups(values);
        }

        public List<GenericValuesRecord> getAllByNameInAndDateBetween(List<String> names,
                        LocalDateTime dateStart, LocalDateTime dateEnd) {
                return genericAnalyticsRepository
                                .findAllByNameInAndDateBetween(names, dateStart, dateEnd).stream()
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

        @Cacheable(value = "name")
        public List<GenericValuesRecord> findAnalyticsByNameWithPagination(Pageable pageable, String name) {
                List<GenericValuesRecord> analyticsList = genericAnalyticsRepository
                                .findAllByName(pageable, name.toUpperCase());
                validateResultsNotEmpty(analyticsList, "No analytics found with the given name");
                return analyticsList;
        }

        @Cacheable(value = "id")
        public GenericAnalytics findById(Long id) {
                return genericAnalyticsRepository.findById(id).orElseThrow(
                                () -> new CustomGlobalErrorHandling.ResourceNotFoundException(
                                                "Results not found."));
        }

        List<GenericValuesRecord> findAnalyticsByNameAndLevelWithPagination(Pageable pageable,
                                                                            String name, String level) {
                List<GenericValuesRecord> analyticsList = genericAnalyticsRepository
                                .findAllByNameAndLevel(pageable, name.toUpperCase(), level);
                validateResultsNotEmpty(analyticsList, "No analytics found for the given name and level");
                return analyticsList;
        }

        List<GenericValuesRecord> findAnalyticsByNameLevelAndDate(String name,
                                                                  String level, LocalDateTime dateStart, LocalDateTime dateEnd) {
                List<GenericValuesRecord> results =
                                genericAnalyticsRepository.findAllByNameAndLevelAndDateBetween(name,
                                                level, dateStart, dateEnd, pageable);
                validateResultsNotEmpty(results, "No analytics found for the given parameters");
                return results;
        }

        public List<GenericValuesRecord> findAllAnalyticsByDate(LocalDateTime dateStart,
                        LocalDateTime dateEnd) {
                List<GenericValuesRecord> results =
                                genericAnalyticsRepository.findAllByDateBetween(dateStart, dateEnd);
                validateResultsNotEmpty(results, "No analytics found for the given date range");
                return results;
        }

}
