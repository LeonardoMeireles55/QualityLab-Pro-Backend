package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.components.GenericValidatorComponent;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entities.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GenericAnalyticsService {
    private final GenericAnalyticsRepositoryCustom genericAnalyticsRepositoryCustom;
    private final GenericValidatorComponent genericValidatorComponent;

    public Stream<GenericAnalytics> sendValues(List<ValuesOfLevelsGenericRecord> valuesOfLevelsList) {
        var valuesFilter = valuesOfLevelsList.stream()
                .filter(values -> !genericAnalyticsRepositoryCustom.existsByDateAndLevelAndName(values.date(),
                        values.level(),
                        values.name()));
        return valuesFilter.map(values -> {
            GenericAnalytics genericAnalytics = new GenericAnalytics(values, genericValidatorComponent);
            return genericAnalyticsRepositoryCustom.save(genericAnalytics);
        });
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getAllResults(Pageable pageable) {
        var resultsList = genericAnalyticsRepositoryCustom.findAll(pageable).map(ValuesOfLevelsGenericRecord::new)
                .toList();
        if (resultsList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found");
        }
        return resultsList;
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getAllResultsByName(Pageable pageable, String name) {
        var nameUpper = name.toUpperCase();

        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepositoryCustom.findAllByName(pageable,
                nameUpper);

        List<GenericAnalytics> analyticsList = analyticsOptional
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getResultsByDateAsc(String name) {
        var nameUpper = name.toUpperCase();

        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepositoryCustom
                .findAllByNameOrderByDateAsc(nameUpper);

        List<GenericAnalytics> analyticsList = analyticsOptional
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getResultsByDateDesc(String name) {
        var nameUpper = name.toUpperCase();

        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepositoryCustom
                .findAllByNameOrderByDateDesc(nameUpper);

        List<GenericAnalytics> analyticsList = analyticsOptional
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }

    @Cacheable(value = "id")
    public GenericAnalytics getResultsById(Long id) {
        return genericAnalyticsRepositoryCustom.findById(id)
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));
    }

    public List<ValuesOfLevelsGenericRecord> getAllResultsByNameAndLevelAndDate(String name, String level,
            String dateStart, String dateEnd) {
        var nameUpper = name.toUpperCase();

        if (!genericAnalyticsRepositoryCustom.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }

        if (name.equals("TTPA") || name.equals("TAP-20")) {
            Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepositoryCustom
                    .findAllByNameAndLevelAndDateBetween(nameUpper, convertLevelACL(level), dateStart, dateEnd);

            List<GenericAnalytics> analyticsList = analyticsOptional
                    .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));

            return analyticsList.stream()
                    .map(ValuesOfLevelsGenericRecord::new)
                    .toList();
        }

        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepositoryCustom
                .findAllByNameAndLevelAndDateBetween(nameUpper, convertLevel(level), dateStart, dateEnd);

        List<GenericAnalytics> analyticsList = analyticsOptional
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }

    public List<ValuesOfLevelsGenericRecord> getAllResultsByDate(String dateStart, String dateEnd) {

        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepositoryCustom
                .findAllByDateBetween(dateStart, dateEnd);

        List<GenericAnalytics> analyticsList = analyticsOptional
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }

    @Cacheable(value = { "name", "level" })
    public List<ValuesOfLevelsGenericRecord> getAllResultsByNameAndLevel(Pageable pageable, String name, String level) {
        var nameUpper = name.toUpperCase();

        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepositoryCustom
                .findAllByNameAndLevel(pageable, nameUpper, convertLevelACL(level));

        List<GenericAnalytics> analyticsList = analyticsOptional.orElseThrow(
                () -> new ErrorHandling.ResourceNotFoundException("Results not found or level not found."));

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }

    public void deleteAnalyticsById(Long id) {
        if(!genericAnalyticsRepositoryCustom.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException("Analytics result not exists");
        }
        genericAnalyticsRepositoryCustom.deleteById(id);
    }

    private String convertLevel(String inputLevel) {
        return switch (inputLevel) {
            case "1" -> "PCCC1";
            case "2" -> "PCCC2";
            case "normal" -> "Normal C. Assayed";
            case "low" -> "Low C. Assayed";
            default -> throw new ErrorHandling.ResourceNotFoundException("Level not found.");
        };
    }
    private String convertLevelACL(String inputLevel) {
        return switch (inputLevel) {
            case "1" -> "Normal C. Assayed";
            case "2" -> "Low Abn C. Assayed";
            default -> throw new ErrorHandling.ResourceNotFoundException("Level not found.");
        };
    }
}