package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.component.GenericValidatorComponent;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entitys.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.record.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenericAnalyticsService {
    private final GenericAnalyticsRepositoryCustom genericAnalyticsRepositoryCustom;
    private final GenericValidatorComponent genericValidatorComponent;

    public Stream<GenericAnalytics> sendValues(List<ValuesOfLevelsGenericRecord> valuesOfLevelsList) {
        var valuesFilter = valuesOfLevelsList.stream()
                .filter(values -> !genericAnalyticsRepositoryCustom.existsByDateAndLevelAndName(values.date(), values.level(),
                        values.name()));
        return valuesFilter.map(values -> {
            GenericAnalytics genericAnalytics = new GenericAnalytics(values, genericValidatorComponent);
                return genericAnalyticsRepositoryCustom.save(genericAnalytics);
        });
    }

    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getAllResults(Pageable pageable) {
        log.info("Retrieve all results...");
        var resultsList = genericAnalyticsRepositoryCustom.findAll(pageable).map(ValuesOfLevelsGenericRecord::new)
                .toList();
        if(resultsList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found");
        }
        return resultsList;
    }
    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getResultsByName(Pageable pageable, String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepositoryCustom.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }
        log.info("Retrieve results by name...");
        return genericAnalyticsRepositoryCustom.findAllByName(pageable, nameUpper).stream()
                .map(ValuesOfLevelsGenericRecord::new).toList();
    }
    @Cacheable(value ={"name", "level"})
    public List<ValuesOfLevelsGenericRecord> getResultsByNameAndLevel(Pageable pageable, String name, String level) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepositoryCustom.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }
        if (Objects.equals(level, "1")) {
            level = "PCCC1";
        } else if (Objects.equals(level, "2")) {
            level = "PCCC2";
        } else {
            throw new ErrorHandling.ResourceNotFoundException("Level not found.");
        }
        log.info("Retrieve results by name and level...");
        return genericAnalyticsRepositoryCustom.findAllByNameAndLevel(pageable, nameUpper, level).stream()
                .map(ValuesOfLevelsGenericRecord::new).toList();
    }
    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getResultsByDateAsc(String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepositoryCustom.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }
        log.info("Retrieve results by dateAsc...");
        return genericAnalyticsRepositoryCustom.findAllByNameOrderByDateAsc(nameUpper).stream()
                .map(ValuesOfLevelsGenericRecord::new).toList();
    }
    @Cacheable(value = "name")
    public List<ValuesOfLevelsGenericRecord> getResultsByDateDesc(String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepositoryCustom.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }
        log.info("Retrieve results by dateDesc...");
        return genericAnalyticsRepositoryCustom.findAllByNameOrderByDateDesc(nameUpper).stream()
                .map(ValuesOfLevelsGenericRecord::new).toList();
    }
    @Cacheable(value = "id")
    public Optional<GenericAnalytics> getResultsById(Long id) {
        if (!genericAnalyticsRepositoryCustom.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException("Results not found.");
        }
        log.info("Retrieve results by dateDesc...");
        return genericAnalyticsRepositoryCustom.findById(id);
    }
}