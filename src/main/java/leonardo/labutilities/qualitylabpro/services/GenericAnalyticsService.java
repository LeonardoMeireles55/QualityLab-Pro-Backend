package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.components.GenericValidatorComponent;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entities.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenericAnalyticsService {
    private final GenericAnalyticsRepository genericAnalyticsRepository;
    private final GenericValidatorComponent genericValidatorComponent;

    public List<GenericAnalytics> sendValues(List<ValuesOfLevelsGenericRecord> valuesOfLevelsList) {
        // Filtra os valores que não existem
        List<GenericAnalytics> newAnalytics = valuesOfLevelsList.stream()
                .filter(values -> !genericAnalyticsRepository.existsByDateAndLevelAndName(
                        values.date(),
                        values.level(),
                        values.name()))
                .map(values -> new GenericAnalytics(values, genericValidatorComponent))
                .collect(Collectors.toList());

        // Log dos registros que serão inseridos
        log.info("Inserindo {} registros em batch", newAnalytics.size());

        // Salva todos de uma vez
        return genericAnalyticsRepository.saveAll(newAnalytics);
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getAllResults(Pageable pageable) {
        var resultsList = genericAnalyticsRepository.findAll(pageable).map(ValuesOfLevelsGenericRecord::new)
                .toList();
        if (resultsList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found");
        }
        return resultsList;
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getAllResultsByName(Pageable pageable, String name) {
        var nameUpper = name.toUpperCase();

        List<GenericAnalytics> analyticsList = genericAnalyticsRepository.findAllByName(pageable, nameUpper);

        if (analyticsList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }


    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getResultsByDateAsc(String name) {
        var nameUpper = name.toUpperCase();

        List<GenericAnalytics> analyticsList = genericAnalyticsRepository
                .findAllByNameOrderByDateAsc(nameUpper);

        if (analyticsList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }


    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getResultsByDateDesc(String name) {
        var nameUpper = name.toUpperCase();

        List<GenericAnalytics> analyticsList = genericAnalyticsRepository
                .findAllByNameOrderByDateDesc(nameUpper);

        if (analyticsList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }

    @Cacheable(value = "id")
    public GenericAnalytics getResultsById(Long id) {
        return genericAnalyticsRepository.findById(id)
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));
    }

    public List<ValuesOfLevelsGenericRecord> getAllResultsByNameAndLevelAndDate(String name, String level,
            String dateStart, String dateEnd) {
        var nameUpper = name.toUpperCase();

        if (!genericAnalyticsRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }

        if (name.equals("TTPA") || name.equals("TAP-20")) {
            Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepository
                    .findAllByNameAndLevelAndDateBetween(nameUpper, convertLevelACL(level), dateStart, dateEnd);

            List<GenericAnalytics> analyticsList = analyticsOptional
                    .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));

            return analyticsList.stream()
                    .map(ValuesOfLevelsGenericRecord::new)
                    .toList();
        }

        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepository
                .findAllByNameAndLevelAndDateBetween(nameUpper, convertLevel(level), dateStart, dateEnd);

        List<GenericAnalytics> analyticsList = analyticsOptional
                .orElseThrow(() -> new ErrorHandling.ResourceNotFoundException("Results not found."));

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }

    public List<ValuesOfLevelsGenericRecord> getAllResultsByDate(String dateStart, String dateEnd) {

        Optional<List<GenericAnalytics>> analyticsOptional = genericAnalyticsRepository
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

        List<GenericAnalytics> analyticsList = genericAnalyticsRepository
                .findAllByNameAndLevel(pageable, nameUpper, convertLevelACL(level));

        if (analyticsList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found or level not found.");
        }

        return analyticsList.stream()
                .map(ValuesOfLevelsGenericRecord::new)
                .toList();
    }


    public void deleteAnalyticsById(Long id) {
        if(!genericAnalyticsRepository.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException("Analytics result not exists");
        }
        genericAnalyticsRepository.deleteById(id);
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